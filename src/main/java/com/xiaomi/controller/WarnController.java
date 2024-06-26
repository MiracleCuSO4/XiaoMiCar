package com.xiaomi.controller;

import com.xiaomi.common.Result;
import com.xiaomi.domain.dto.SignalDto;
import com.xiaomi.domain.vo.WarnVo;
import com.xiaomi.service.WarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/warn")
@RequiredArgsConstructor
@Api(tags = "预警信号接收处理接口")
@Validated
public class WarnController {

    private final WarnService warnService;

    @PostMapping
    @ApiOperation("上报接口")
    public Result<List<WarnVo>> warn(@RequestBody List<@Valid SignalDto> dtoList){
        return warnService.warn(dtoList);
    }
}
