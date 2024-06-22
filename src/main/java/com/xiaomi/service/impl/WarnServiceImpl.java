package com.xiaomi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.SignalDto;
import com.xiaomi.domain.po.Record;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.domain.rule.Condition;
import com.xiaomi.domain.rule.Rate;
import com.xiaomi.domain.vo.CarVo;
import com.xiaomi.domain.vo.WarnVo;
import com.xiaomi.service.CarService;
import com.xiaomi.service.RecordService;
import com.xiaomi.service.RuleService;
import com.xiaomi.service.WarnService;
import com.xiaomi.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.xiaomi.common.AppHttpCodeEnum.ILLEGAL_ARGUMENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarnServiceImpl implements WarnService {

    private final CarService carService;

    private final RuleService ruleService;

    private final RecordService recordService;

    // 使用js脚本引擎计算公式值
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    private final BlockingQueue<List<Record>> TASKS = new ArrayBlockingQueue<>(4 * 1024 * 1024); // jdk阻塞队列
    private static final ExecutorService DATABASE_EXECUTOR = Executors.newSingleThreadExecutor(); // 单线程工厂


    /**
     * 根据请求的数据响应报警结果
     * @param dtoList 预警所需的车架和信号的list
     * @return 报警结果list
     */
    @Override
    public Result<List<WarnVo>> warn(List<SignalDto> dtoList) {
        boolean isTolerableBadRequest = false;
        List<WarnVo> resultList = new ArrayList<>();
        List<Record> recordList = new ArrayList<>();
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
                Rate rate = checkIfNeedsWarn(rule, value);
                if(rate != null){
                    String description = rule.generateDescription(value, rate);
                    // 产生报警记录
                    Record record = new Record();
                    record.setVid(carVo.getVid());
                    record.setRuleId(rule.getId());
                    record.setWarnLevel(rate.getWarnLever());
                    record.setMessage(String.format("规则编号:%s,%s,%s,公式:%s,%s",
                            rule.getWarnId(), rule.getWarnName(), rule.getBatteryType(), rule.getFormulaRateConfig().getFormula(), description));
                    recordList.add(record);
                    // 产生报警信息
                    WarnVo warnVo = new WarnVo();
                    warnVo.setCarId(carVo.getCarId());
                    warnVo.setBatteryType(carVo.getBatteryType());
                    warnVo.setWarnName(rule.getWarnName());
                    warnVo.setWarnLevel(rate.getWarnLever() == null ? "不报警" : rate.getWarnLever().toString());
                    resultList.add(warnVo);
                    log.warn("产生一条报警等级{}的报警,因为满足:{}", rate.getWarnLever(), description);
                }
            }
        }
        // 队列存放预警记录,由独立线程写入数据库
        if(recordList.size() > 0){
            TASKS.add(recordList);
        }
        if(isTolerableBadRequest){
            return Result.errorResult(ILLEGAL_ARGUMENT.getCode(), "缺少计算公式的信号参数或者公式有误,已跳过无法计算的公式", resultList);
        } else {
            return Result.okResult(resultList);
        }
    }

    /**
     * 根据计算值和规则条件判断是否需要报警
     * @param rule 一条规则
     * @param value 这个车辆的信号数据使用这个规则的公式计算出的结果
     * @return 返回对应的报警等级
     */
    public Rate checkIfNeedsWarn(Rule rule, double value) {
        for (Rate rate : rule.getFormulaRateConfig().getRate()) {
            boolean match = true;
            for (Condition condition : rate.getCondition()) {
                /* "condition": [
                 *         {"operator": ">=", "value": 3},
                 *         {"operator": "<", "value": 5}
                 *       ]
                 */
                if (!Comparator.compare(value, condition.getOperator(), condition.getValue())) {
                    // 这个报警等级中有任何一个条件condition不满足就退出这个报警等级break
                    match = false;
                    break;
                }
            }
            // 找到完全匹配的报警等级,比如3<=(Mx-Mi)<5
            if (match) {
                return rate; // 跳出当前规则的判断
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


    @PostConstruct
    private void saveRecordTask() {
        DATABASE_EXECUTOR.submit(() -> {
            log.info("已启动独立线程准备保存预警记录到数据库");
            List<Record> recordList = null;
            while (true) {
                try {
                    recordList = TASKS.take(); //从阻塞队列获取待写入的数据
                    recordService.saveBatch(recordList);
                    log.info("保存报警记录至数据库成功");
                } catch (Exception e) {
                    log.error("保存报警记录至数据库失败,数据为{}", recordList);
                    e.printStackTrace();
                }
            }
        });
    }
}
