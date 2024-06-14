package com.xiaomi.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignalDto {

    @ApiModelProperty(value = "车架编号", required = true)
    private Integer carId;

    @ApiModelProperty(value = "规则编号")
    private Integer warnId;

    @ApiModelProperty(value = "信号jsonString", required = true)
    private String signal;
}
