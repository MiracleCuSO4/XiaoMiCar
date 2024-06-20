package com.xiaomi.domain.dto;

import com.xiaomi.domain.rule.FormulaRateConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 规则表
 */
@Data
@ApiModel(description = "规则dto")
public class RuleDto {

    @ApiModelProperty(value = "规则序号id")
    private Integer id;

    @ApiModelProperty(value = "规则编号warnId", required = true)
    @NotNull(message = "规则编号warnId不能为空")
    @Min(value = 0, message = "规则编号warnId不能是负数")
    private Integer warnId;

    @ApiModelProperty(value = "规则名称", required = true)
    @NotNull(message = "规则名称不能为空")
    private String warnName;

    @ApiModelProperty(value = "电池类型", required = true)
    @NotNull(message = "电池类型不能为空")
    private String batteryType;

    @ApiModelProperty(value = "预警规则", required = true)
    @Valid
    @NotNull(message = "预警规则不能为空")
    private FormulaRateConfig formulaRateConfig;


}
