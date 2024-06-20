package com.xiaomi.controller;

import com.xiaomi.common.PageResult;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.vo.RuleVo;
import com.xiaomi.service.RuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rule")
@RequiredArgsConstructor
@Api("规则接口")
@Validated
public class RuleController {

    private final RuleService ruleService;

    @GetMapping("/{id}")
    @ApiOperation("通过规则序号(ruleId)查询规则")
    public Result<RuleVo> selectByRuleId(@PathVariable("id") Integer ruleId){
        return Result.okResult(ruleService.selectByRuleId(ruleId));
    }

    @PostMapping("/list")
    @ApiOperation("分页查询规则列表")
    public Result<PageResult<RuleVo>> selectPageList(@RequestBody PageRequest pageRequest){
        pageRequest.checkParam();
        return Result.okResult(ruleService.selectPageList(pageRequest));
    }

    @PostMapping
    @ApiOperation("新增规则")
    public Result<Void> insertRule(@RequestBody @Valid com.xiaomi.domain.dto.RuleDto ruleDto){
        ruleService.insertRule(ruleDto);
        return Result.okResult(null);
    }

    @PutMapping
    @ApiOperation("修改规则")
    public Result<Void> updateRule(@RequestBody @Valid com.xiaomi.domain.dto.RuleDto ruleDto){
        ruleService.updateRule(ruleDto);
        return Result.okResult(null);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过规则序号(ruleId)删除规则")
    public Result<Void> deleteByRuleId(@PathVariable("id") Integer ruleId){
        ruleService.deleteByRuleId(ruleId);
        return Result.okResult(null);
    }


}
