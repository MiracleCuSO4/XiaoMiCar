package com.xiaomi.domain.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "告警级别和条件类")
public class Rate {

    @ApiModelProperty(value = "告警级别,为空表示不报警")
    @Min(value = 0, message = "告警级别处于0~100")
    @Max(value = 100, message = "告警级别处于0~100")
    private Byte warnLever;

    @ApiModelProperty(value = "条件列表", required = true)
    @NotNull(message = "条件列表不能为空")
    @Valid
    private List<Condition> condition;
}
