package com.xiaomi.domain.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "条件类")
public class Condition {

    @ApiModelProperty(value = "运算符")
    private String operator;

    @ApiModelProperty(value = "值")
    private double value;
}
