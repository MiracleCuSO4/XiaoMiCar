package com.xiaomi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.SignalDto;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.domain.rule.Condition;
import com.xiaomi.domain.rule.Rate;
import com.xiaomi.domain.vo.CarVo;
import com.xiaomi.domain.vo.WarnVo;
import com.xiaomi.service.CarService;
import com.xiaomi.service.RuleService;
import com.xiaomi.service.WarnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xiaomi.common.AppHttpCodeEnum.ILLEGAL_ARGUMENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarnServiceImpl implements WarnService {

    private final CarService carService;

    private final RuleService ruleService;

    // 使用js脚本引擎计算公式值
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * 根据请求的数据响应报警结果
     * @param dtoList 预警所需的车架和信号的list
     * @return 报警结果list
     */
    @Override
    public Result<List<WarnVo>> warn(List<SignalDto> dtoList) {
        boolean isTolerableBadRequest = false;
        List<WarnVo> resultList = new ArrayList<>();
        for (SignalDto signalDto : dtoList) {
            // 根据车架编号carId获取汽车信息
            CarVo carVo = carService.selectByCarId(signalDto.getCarId());
            // 根据规则编号warnId与车辆电池类型batteryType获取规则
            List<Rule> ruleList = ruleService.selectByWarnIdAndCar(signalDto.getWarnId(), carVo.getBatteryType());
            // 把信号jsonString比如"{\"Mx\":12.0,\"Mi\":0.6}"转为map
            Map<String, Double> signal;
            try {
                signal = JSON.parseObject(signalDto.getSignal(), new TypeReference<Map<String, Double>>(){});
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("BadRequest: signal由json转Map<String, Double>时出错, Caused by: ");
            }
            // 逐一判断是否要报警
            log.info("开始判断汽车{}是否需要报警", carVo);
            for (Rule rule : ruleList) {
                double value;
                try {
                    // 根据公式例如"${Mx} - ${Mi}"和信号Map{"Mx":12.0, "Mi":0.6}计算出结果
                    value = calculate(rule.getFormulaRateConfig().getFormula(), signal);
                } catch (ScriptException e) {
                    e.printStackTrace();
                    isTolerableBadRequest = true; // 公式无法计算出结果,因为缺少计算这个公式的信号参数或者公式有误,这个错误可以容忍
                    continue; // 忽略掉,数据清洗,跳过这个规则
                }
                // 检查是否需要报警
                WarnVo warnVo = checkIfNeedsWarn(carVo, rule, value);
                if(warnVo != null){
                    resultList.add(warnVo);
                }
            }
        }
        if(isTolerableBadRequest){
            return Result.errorResult(ILLEGAL_ARGUMENT.getCode(), "缺少计算公式的信号参数或者公式有误,已跳过无法计算的公式", resultList);
        } else {
            return Result.okResult(resultList);
        }
    }

    /**
     * 根据计算值和规则条件判断是否需要报警
     * @param carVo 车辆信息
     * @param rule 一条规则
     * @param value 这个车辆的信号数据使用这个规则的公式计算出的结果
     * @return 报警记录,如果无需报警返回null
     */
    public WarnVo checkIfNeedsWarn(CarVo carVo, Rule rule, double value) {
        for (Rate rate : rule.getFormulaRateConfig().getRate()) {
            boolean match = true;
            for (Condition condition : rate.getCondition()) {
                /* "condition": [
                 *         {"operator": ">=", "value": 3},
                 *         {"operator": "<", "value": 5}
                 *       ]
                 */
                if (!compare(value, condition.getOperator(), condition.getValue())) {
                    // 这个报警等级中有任何一个条件condition不满足就退出这个报警等级break
                    match = false;
                    break;
                }
            }
            // 找到完全匹配的报警等级,比如3<=(Mx-Mi)<5  注意不报警的规则对应的warnLever为null,如果为null就不记录
            if (match && rate.getWarnLever() != null) {
                // 产生报警信息
                WarnVo warnVo = new WarnVo();
                warnVo.setCarId(carVo.getCarId());
                warnVo.setBatteryType(carVo.getBatteryType());
                warnVo.setWarnName(rule.getWarnName());
                warnVo.setWarnLevel(rate.getWarnLever());
                log.warn("产生一条报警:{},因为满足规则编号:{},电池类型:{},报警等级:{}的公式:{}",
                        warnVo, rule.getWarnId(), rule.getBatteryType(), rate.getWarnLever(), rule.getDescription(value, rate));
                return warnVo; // 跳出当前规则的判断
            }
        }
        return null; // 无需报警
    }


    /**
     * 根据公式和信号计算出结果
     *
     * @param formula 公式,例如:"${Mx} - ${Mi}"
     * @param signal  信号Map,例如Map{"Mx":12.0, "Mi":0.6}
     * @return 把信号数据代入公式计算得到的结果
     * @throws ScriptException 脚本不能得出结果,通常是缺少信号参数
     */
    public double calculate(String formula, Map<String, Double> signal) throws ScriptException {
        String originalFormula = formula;
        // 使用字符串替换来计算公式
        for (Map.Entry<String, Double> entry : signal.entrySet()) {
            // entry.getValue()是double数字可以避免注入攻击
            formula = formula.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        // 使用JavaScript脚本引擎计算公式的值
        log.info("公式'{}'被解析为'{}'", originalFormula, formula);
        return (Double) engine.eval(formula);
    }

    /**
     * 比较 number1 operator number2的结果,比如1<=2
     *
     * @param number1  左侧数字
     * @param operator 比较符号
     * @param number2  右侧数字
     * @return 两数字的比较结果
     */
    public boolean compare(double number1, String operator, double number2) {
        switch (operator) {
            case ">=":
                return number1 >= number2;
            case ">":
                return number1 > number2;
            case "<=":
                return number1 <= number2;
            case "<":
                return number1 < number2;
            case "==":
                return number1 == number2;
            case "!=":
                return number1 != number2;
            default:
                throw new IllegalArgumentException("不支持的比较器:'" + operator + "',请检查公式");
        }
    }
}
