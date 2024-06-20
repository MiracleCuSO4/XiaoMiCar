package com.xiaomi.domain.vo;

import com.xiaomi.domain.rule.FormulaRateConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 规则表
 */
@Data
@ApiModel(description = "规则vo")
public class RuleVo {

    @ApiModelProperty(value = "规则序号id", required = true)
    private Integer id;

    @ApiModelProperty(value = "规则编号warnId", required = true)
    private Integer warnId;

    @ApiModelProperty(value = "规则名称", required = true)
    private String warnName;

    @ApiModelProperty(value = "电池类型", required = true)
    private String batteryType;

    @ApiModelProperty(value = "预警规则", required = true)
    private FormulaRateConfig formulaRateConfig;


}
