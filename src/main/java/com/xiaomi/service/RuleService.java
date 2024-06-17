package com.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomi.common.Result;
import com.xiaomi.domain.po.Rule;

public interface RuleService extends IService<Rule> {
    Result<Void> addRule(Rule rule);

}
