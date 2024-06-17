package com.xiaomi.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.common.Result;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.mapper.RuleMapper;
import com.xiaomi.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {

    private final RuleMapper ruleMapper;

    @Override
    public Result<Void> addRule(Rule rule) {
        save(rule);
        return Result.okResult(null);
    }

    /**
     * 获取匹配的规则列表
     * @param warnId 规则编号(非必须)
     * @param batteryType 车辆电池类型(必须)
     * @return 规则列表
     */
    @Override
    public List<Rule> getByWarnIdAndBatteryType(Integer warnId, String batteryType) {
        List<Rule> ruleList = ruleMapper.selectList(Wrappers.<Rule>lambdaQuery()
                .eq(warnId != null, Rule::getWarnId, warnId)
                .eq(Rule::getBatteryType, batteryType));
        return CollectionUtil.emptyIfNull(ruleList);
    }
}
