package com.xiaomi.controller;

import com.xiaomi.common.Result;
import com.xiaomi.service.WarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warn")
@RequiredArgsConstructor
@Api("预警接口")
public class WarnController {

    private final WarnService warnService;

    // TODO
    /*@PostMapping
    @ApiOperation("上报接口")
    public Result<List<WarnVo>> warn(@RequestBody List<SignalDto> dtoList){
        return warnService.warn(dtoList);
    }*/
}
