package com.xiaomi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.xiaomi.WarnApplication;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.RuleDto;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.domain.rule.FormulaRateConfig;
import com.xiaomi.domain.vo.RuleVo;
import com.xiaomi.service.RuleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@SpringBootTest(classes = WarnApplication.class)
@RunWith(SpringRunner.class)
public class RuleServiceImplTest {

    @Autowired
    private RuleService ruleService;

    @Test
    public void testInsertRule(){
        RuleDto ruleDto = new RuleDto();
        ruleDto.setWarnId(1);
        ruleDto.setWarnName("添加电压差报警规则测试");
        ruleDto.setBatteryType("三元电池");
        ruleDto.setFormulaRateConfig(JSON.parseObject(ruleJson, FormulaRateConfig.class));
        ruleService.insertRule(ruleDto);
    }

    @Test
    public void testSelectByRuleId(){
        RuleVo ruleVo = ruleService.selectByRuleId(5);
        System.out.println(ruleVo);
    }

    @Test
    public void testUpdateRule(){
        RuleVo ruleVo = ruleService.selectByRuleId(5);
        ruleVo.getFormulaRateConfig().setFormula("${Mx} - ${Mi} + 1.2");
        RuleDto ruleDto = BeanUtil.copyProperties(ruleVo, RuleDto.class);
        ruleService.updateRule(ruleDto);
    }

    @Test
    public void testDeleteByRuleId(){
        ruleService.deleteByRuleId(5);
    }

    @Test
    public void testAddAllRule(){
        int[] warnId = {1, 1, 2, 2};
        String[] warnNameList = {"电压差报警", "电压差报警", "电流差报警", "电流差报警"};
        String[] batteryTypeList = {"三元电池", "铁锂电池", "三元电池", "铁锂电池"};
        String[] jsonFileNameList = {"rule1.json", "rule2.json", "rule3.json", "rule4.json"};

        for(int i = 0; i < 4; i++){
            try {
                RuleDto ruleDto = new RuleDto();
                ruleDto.setWarnId(warnId[i]);
                ruleDto.setWarnName(warnNameList[i]);
                ruleDto.setBatteryType(batteryTypeList[i]);
                ruleDto.setFormulaRateConfig(JSON.parseObject(readJsonFile(jsonFileNameList[i]), FormulaRateConfig.class));
                ruleService.insertRule(ruleDto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readJsonFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("ruleJson/" + fileName);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }

    private static final String ruleJson = "{\n" +
            "  \"formula\": \"${Mx} - ${Mi}\",\n" +
            "  \"rate\": [\n" +
            "    {\n" +
            "      \"warnLever\": 0,\n" +
            "      \"condition\": [\n" +
            "        {\"operator\": \">=\", \"value\": 5}\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"warnLever\": 1,\n" +
            "      \"condition\": [\n" +
            "        {\"operator\": \">=\", \"value\": 3},\n" +
            "        {\"operator\": \"<\", \"value\": 5}\n" +
            "      ]\n" +
            "    },{\n" +
            "      \"warnLever\": 2,\n" +
            "      \"condition\": [\n" +
            "        {\"operator\": \">=\", \"value\": 1},\n" +
            "        {\"operator\": \"<\", \"value\": 3}\n" +
            "      ]\n" +
            "    },{\n" +
            "      \"warnLever\": 3,\n" +
            "      \"condition\": [\n" +
            "        {\"operator\": \">=\", \"value\": 0.6},\n" +
            "        {\"operator\": \"<\", \"value\": 1}\n" +
            "      ]\n" +
            "    },{\n" +
            "      \"warnLever\": 4,\n" +
            "      \"condition\": [\n" +
            "        {\"operator\": \">=\", \"value\": 0.2},\n" +
            "        {\"operator\": \"<\", \"value\": 0.6}\n" +
            "      ]\n" +
            "    },{\n" +
            "      \"warnLever\": null,\n" +
            "      \"condition\": [\n" +
            "        {\"operator\": \"<\", \"value\": 0.2}\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}