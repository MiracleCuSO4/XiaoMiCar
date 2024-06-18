package com.xiaomi.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignalDto {

    @ApiModelProperty(value = "车架编号", required = true)
    @NotNull(message = "车架编号不能为空")
    private Integer carId;

    @ApiModelProperty(value = "规则编号")
    private Integer warnId;

    @ApiModelProperty(value = "信号jsonString", required = true)
    @NotNull(message = "信号jsonString不能为空")
    private String signal;
}
