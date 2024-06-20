package com.xiaomi.domain.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "条件类")
public class Condition {

    @ApiModelProperty(value = "运算符", required = true)
    @NotNull(message = "运算符不能为空")
    @Pattern(regexp = ">=|<=|>|<|==|!=", message = "运算符必须是 >=, <=, >, <, ==, != 之一")
    private String operator;

    @ApiModelProperty(value = "值", required = true)
    @NotNull(message = "值不能为空")
    private Double value;
}
