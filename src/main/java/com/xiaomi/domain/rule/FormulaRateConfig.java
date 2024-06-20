package com.xiaomi.domain.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "公式和告警配置类")
public class FormulaRateConfig {

    @ApiModelProperty(value = "公式", required = true)
    @NotNull(message = "公式不能为空")
    private String formula;

    @ApiModelProperty(value = "告警级别和条件列表", required = true)
    @NotNull(message = "告警级别和条件列表不能为空")
    @Valid
    private List<Rate> rate;
}
