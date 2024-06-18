package com.xiaomi.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.common.RedisConstant;
import com.xiaomi.common.Result;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.mapper.RuleMapper;
import com.xiaomi.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {

    private final RuleMapper ruleMapper;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Void> addRule(Rule rule) {
        save(rule);
        return Result.okResult(null);
    }

    /**
     * 获取匹配的规则列表,缓存查询条件对应的规则列表
     * @param warnId 规则编号(非必须)
     * @param batteryType 车辆电池类型(必须)
     * @return 规则列表
     */
    @Override
    public List<Rule> getByWarnIdAndCar(Integer warnId, String batteryType) {
        if(StrUtil.isBlank(batteryType)){
            return Collections.emptyList();
        }
        List<Rule> ruleList;
        String ruleQueryKey = String.format(RedisConstant.RULE_QUERY_KEY, (warnId == null ? RedisConstant.MATCH_ALL_WARN_ID : warnId), batteryType);
        String json = stringRedisTemplate.opsForValue().get(ruleQueryKey);
        if (StrUtil.isNotBlank(json)){
            ruleList = JSON.parseObject(json, new TypeReference<List<Rule>>(){});
        } else {
            ruleList = ruleMapper.selectList(Wrappers.<Rule>lambdaQuery()
                    .eq(warnId != null, Rule::getWarnId, warnId)
                    .eq(Rule::getBatteryType, batteryType));
            stringRedisTemplate.opsForValue().set(ruleQueryKey, JSON.toJSONString(ruleList), RedisConstant.TTL, TimeUnit.MILLISECONDS);
        }
        return CollectionUtil.emptyIfNull(ruleList);
    }
}
