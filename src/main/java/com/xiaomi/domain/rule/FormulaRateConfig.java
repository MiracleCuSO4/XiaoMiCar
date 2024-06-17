package com.xiaomi.domain.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "公式和告警配置类")
public class FormulaRateConfig {

    @ApiModelProperty(value = "公式")
    private String formula;

    @ApiModelProperty(value = "告警级别和条件列表")
    private List<Rate> rate;
}
