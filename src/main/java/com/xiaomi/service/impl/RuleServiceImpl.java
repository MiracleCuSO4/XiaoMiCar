package com.xiaomi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.common.Result;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.mapper.RuleMapper;
import com.xiaomi.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {
    @Override
    public Result<Void> addRule(Rule rule) {
        save(rule);
        return Result.okResult(null);
    }
}
