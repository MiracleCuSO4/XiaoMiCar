package com.xiaomi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.common.PageResult;
import com.xiaomi.common.RedisConstant;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.dto.RuleDto;
import com.xiaomi.domain.po.Rule;
import com.xiaomi.domain.vo.RuleVo;
import com.xiaomi.exception.DataNotExistException;
import com.xiaomi.mapper.RuleMapper;
import com.xiaomi.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {

    private final RuleMapper ruleMapper;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 获取匹配的规则列表,缓存查询条件对应的规则列表
     * @param warnId 规则编号(非必须)
     * @param batteryType 车辆电池类型(必须)
     * @return 规则列表
     */
    @Override
    public List<Rule> selectByWarnIdAndCar(Integer warnId, String batteryType) {
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

    /**
     * 更新规则时调用这个方法删除与这个规则编号warnId相关的查询缓存
     * @param warnId 规则编号(非必须)
     * @param batteryType 车辆电池类型(必须)
     */
    public void deleteRelatedRuleCache(Integer warnId, String batteryType){
        stringRedisTemplate.delete(String.format(RedisConstant.RULE_QUERY_KEY, warnId, batteryType));
        stringRedisTemplate.delete(String.format(RedisConstant.RULE_QUERY_KEY, RedisConstant.MATCH_ALL_WARN_ID, batteryType));
    }

    @Override
    public RuleVo selectByRuleId(Integer ruleId) {
        Rule rule = getOne(Wrappers.<Rule>lambdaQuery().eq(Rule::getId, ruleId));
        if(rule == null) {
            throw new DataNotExistException("不存在规则序号ruleId为" + ruleId + "的规则");
        }
        return BeanUtil.copyProperties(rule, RuleVo.class);
    }

    @Override
    public PageResult<RuleVo> selectPageList(PageRequest pageRequest) {
        IPage<Rule> page = new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize());
        page = page(page, Wrappers.<Rule>lambdaQuery().orderByAsc(Rule::getUpdateTime));
        List<RuleVo> ruleVoList = page.getRecords().stream()
                .map(rule -> BeanUtil.copyProperties(rule, RuleVo.class))
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), ruleVoList);
    }

    @Override
    public void insertRule(RuleDto ruleDto) {
        Rule rule = BeanUtil.copyProperties(ruleDto, Rule.class);
        save(rule);
        // 删除与这个规则编号warnId相关的查询缓存
        deleteRelatedRuleCache(rule.getWarnId(), rule.getBatteryType());
    }

    @Override
    public void updateRule(RuleDto ruleDto) {
        if(ruleDto.getId() == null){
            throw new IllegalArgumentException("需要规则序号id,只有warnId不能唯一确定规则");
        }
        updateById(BeanUtil.copyProperties(ruleDto, Rule.class));
        deleteRelatedRuleCache(ruleDto.getWarnId(), ruleDto.getBatteryType());
    }

    @Override
    public void deleteByRuleId(Integer ruleId) {
        Rule rule = getById(ruleId);
        removeById(ruleId);
        deleteRelatedRuleCache(ruleId, rule.getBatteryType());
    }

}
