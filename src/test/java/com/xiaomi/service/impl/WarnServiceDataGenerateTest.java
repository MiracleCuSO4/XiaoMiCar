package com.xiaomi.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.xiaomi.WarnApplication;
import com.xiaomi.common.PageResult;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.dto.SignalDto;
import com.xiaomi.domain.rule.Condition;
import com.xiaomi.domain.rule.Rate;
import com.xiaomi.domain.vo.CarVo;
import com.xiaomi.domain.vo.RuleVo;
import com.xiaomi.domain.vo.WarnVo;
import com.xiaomi.service.CarService;
import com.xiaomi.service.RuleService;
import com.xiaomi.service.WarnService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest(classes = WarnApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class WarnServiceDataGenerateTest {

    @Autowired
    private WarnService warnService;

    @Autowired
    private CarService carService;

    @Autowired
    private RuleService ruleService;

    private List<CarVo> carVoList;
    private List<RuleVo> ruleVoList;

    /**
     * 获取需要的数据
     */
    @Before
    public void setup(){
        PageResult<CarVo> carVoPageResult = carService.selectPageList(new PageRequest(1, 100));
        carVoList = carVoPageResult.getRecords();
        PageResult<RuleVo> ruleVoPageResult = ruleService.selectPageList(new PageRequest(1, 4));
        ruleVoList = ruleVoPageResult.getRecords();
    }

    @Test
    public void testWarn() throws InterruptedException {
        // 产生1~10个汽车的信号
        int signalDtoCnt = RandomUtil.randomInt(1, 10);
        List<SignalDto> signalDtoList = new ArrayList<>(signalDtoCnt);
        List<String> answerList = new ArrayList<>(signalDtoCnt); // 随机生成的报警等级数据的答案
        for(int i = 0; i < signalDtoCnt; i++){
            Map<String, Object> data = generateData();
            signalDtoList.add((SignalDto)data.get("signalDto"));
            answerList.add((String)data.get("warnLevel"));
        }
        // 调接口获取结果
        Result<List<WarnVo>> result = warnService.warn(signalDtoList);
        List<WarnVo> warnVoList = result.getData();
        // 把接口得出的结果与生成数据的答案比较
        int rightCnt = 0;
        for(int i = 0; i < signalDtoCnt; i++){
            SignalDto problem = signalDtoList.get(i); // 题目
            WarnVo answer = warnVoList.get(i); // 回答
            String warnLeverAnswer = answerList.get(i); // 答案
            // 报警则返回报警等级,不报警则返回不报警,下面验证答案
            if(warnLeverAnswer.equals(answer.getWarnLevel())){
                log.info("第{}题,正确! 题目{},回答报警等级:{}", i, problem, answer.getWarnLevel());
                rightCnt ++;
            } else {
                log.error("第{}题,错误: 题目{},回答报警等级:{},答案是报警等级:{}", i, problem, answer.getWarnLevel(), warnLeverAnswer);
            }
        }
        log.info("total:{}, passed:{}, 正确率:{}", signalDtoCnt, rightCnt, 100.0 * rightCnt / signalDtoCnt);
        Thread.sleep(2000); // 测试完程序就停了,也就停止与mysql的连接了,那个独立线程可能还没把队列中的报警记录保存到数据库,所以测试完成前停一会儿
    }

    /**
     * 随机的根据规则和已有车辆生成信号数据
     * @return Map<String, Object>:{"warnLevel": String, "signalDto": SignalDto.class}
     */
    private Map<String, Object> generateData(){
        SignalDto signalDto = new SignalDto();
        HashMap<String, Double> signalMap = new HashMap<>();
        signalMap.put("Mi", RandomUtil.randomDouble());
        signalMap.put("Ii", RandomUtil.randomDouble());

        // 随机获取一个汽车
        CarVo carVo = getRandomElement(carVoList);
        signalDto.setCarId(carVo.getCarId());
        String batteryType = carVo.getBatteryType();

        // 找到与这个汽车电池类型相匹配的规则
        List<RuleVo> targetRuleList = new ArrayList<>();
        for (RuleVo ruleVo : ruleVoList) {
            if(ruleVo.getBatteryType().equals(batteryType)){
                targetRuleList.add(ruleVo);
            }
        }

        // 指定warnId的情况,随机挑选相匹配规则中的一个规则
        RuleVo ruleVo = getRandomElement(targetRuleList);
        signalDto.setWarnId(ruleVo.getWarnId());
        List<Rate> rateList = ruleVo.getFormulaRateConfig().getRate();
        // 在这个规则中随机挑选一个报警等级,生成这个等级对应的测试数据
        Rate rate = getRandomElement(rateList);
        List<Condition> conditionList = rate.getCondition();
        if(conditionList.size() == 1){
            // {"operator": ">=", "value": 5} 或 {"operator": "<", "value": 0.2}
            Condition condition = conditionList.get(0);
            if(">=".equals(condition.getOperator())){
                // 根据Mi生成的Mx = Mi + value + rand[0, 1), 所以Mx-Mi>=value
                signalMap.put("Mx", signalMap.get("Mi") + condition.getValue() + RandomUtil.randomDouble());
                signalMap.put("Ix", signalMap.get("Ii") + condition.getValue() + RandomUtil.randomDouble());
            } else if ("<".equals(condition.getOperator())){
                // 根据Mi生成的Mx = Mi + rand[0.001, value), 所以Mx-Mi < value
                signalMap.put("Mx", signalMap.get("Mi") + RandomUtil.randomDouble(0.001, condition.getValue()));
                signalMap.put("Ix", signalMap.get("Ii") + RandomUtil.randomDouble(0.001, condition.getValue()));
            }
        } else if (conditionList.size() == 2){
            // [{"operator": ">=", "value": 3},{"operator": "<", "value": 5}]
            // 找到其中的最小值与最大值
            double minValue = conditionList.get(0).getValue();
            double maxValue = conditionList.get(1).getValue();
            if(minValue > maxValue){
                double temp = minValue;
                minValue = maxValue;
                maxValue = temp;
            }
            // minValue + 0.001 <= Mx - Mi < maxValue, 即 minValue < Mx - Mi < maxValue
            signalMap.put("Mx", signalMap.get("Mi") + RandomUtil.randomDouble(minValue + 0.001, maxValue));
            signalMap.put("Ix", signalMap.get("Ii") + RandomUtil.randomDouble(minValue + 0.001, maxValue));
        }

        signalDto.setSignal(JSON.toJSONString(signalMap));
        HashMap<String, Object> data = new HashMap<>();
        data.put("warnLevel", rate.getWarnLever() == null ? "不报警" : rate.getWarnLever().toString());
        data.put("signalDto", signalDto);
        return data;
    }

    private <T> T getRandomElement(List<T> list){
        if (list == null || list.isEmpty()) {
            throw new NullPointerException();
        }
        return list.get(RandomUtil.randomInt(list.size()));
    }
}