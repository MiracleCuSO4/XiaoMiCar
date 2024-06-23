package com.xiaomi.controller;

import com.xiaomi.common.PageResult;
import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.PageRequest;
import com.xiaomi.domain.vo.RecordVo;
import com.xiaomi.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
@Api(tags = "预警记录接口")
@Validated
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/{id}")
    @ApiOperation("通过id查询预警记录")
    public Result<RecordVo> selectById(@PathVariable("id") Integer id){
        return Result.okResult(recordService.selectById(id));
    }

    @PostMapping("/list")
    @ApiOperation("分页查询汽车列表")
        public Result<PageResult<RecordVo>> selectPageList(@RequestBody @Valid PageRequest pageRequest){
        pageRequest.checkParam();
        return Result.okResult(recordService.selectPageList(pageRequest));
    }

    @PutMapping("/{id}")
    @ApiOperation("修改预警记录为已读")
    public Result<Void> updateMarkAsRead(@PathVariable("id") Integer id){
        recordService.updateMarkAsRead(id);
        return Result.okResult(null);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除预警记录")
    public Result<Void> deleteById(@PathVariable("id") Integer id){
        recordService.deleteById(id);
        return Result.okResult(null);
    }


}
