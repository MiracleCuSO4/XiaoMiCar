package com.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomi.common.PageResult;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.dto.RuleDto;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.domain.vo.RuleVo;

import java.util.List;

public interface RuleService extends IService<Rule> {

    List<Rule> selectByWarnIdAndCar(Integer warnId, String batteryType);

    RuleVo selectByRuleId(Integer ruleId);

    PageResult<RuleVo> selectPageList(PageRequest pageRequest);

    void insertRule(RuleDto ruleDto);

    void updateRule(RuleDto ruleDto);

    void deleteByRuleId(Integer ruleId);
}
