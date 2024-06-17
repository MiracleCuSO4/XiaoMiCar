package com.xiaomi.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaomi.WarnApplication;
import com.xiaomi.common.Result;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.domain.rule.FormulaRateConfig;
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
    public void testAddRule(){
        Rule rule = new Rule();
        rule.setWarnId(1);
        rule.setWarnName("电压差报警");
        rule.setBatteryType("三元电池");
        rule.setFormulaRateConfig(JSON.parseObject(ruleJson, FormulaRateConfig.class));
        Result<Void> result = ruleService.addRule(rule);
    }

    @Test
    public void testSelectRule(){
        Rule rule = ruleService.getById(1);
        System.out.println(rule);
    }

    @Test
    public void testAddAllRule(){
        int[] warnId = {1, 1, 2, 2};
        String[] warnNameList = {"电压差报警", "电压差报警", "电流差报警", "电流差报警"};
        String[] batteryTypeList = {"三元电池", "铁锂电池", "三元电池", "铁锂电池"};
        String[] jsonFileNameList = {"rule1.json", "rule2.json", "rule3.json", "rule4.json"};

        for(int i = 0; i < 4; i++){
            try {
                Rule rule = new Rule();
                rule.setWarnId(warnId[i]);
                rule.setWarnName(warnNameList[i]);
                rule.setBatteryType(batteryTypeList[i]);
                rule.setFormulaRateConfig(JSON.parseObject(readJsonFile(jsonFileNameList[i]), FormulaRateConfig.class));
                ruleService.addRule(rule);
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