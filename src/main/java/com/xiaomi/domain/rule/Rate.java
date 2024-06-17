package com.xiaomi.domain.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "告警级别和条件类")
public class Rate {

    @ApiModelProperty(value = "告警级别")
    private Byte warnLever;

    @ApiModelProperty(value = "条件列表")
    private List<Condition> condition;
}
