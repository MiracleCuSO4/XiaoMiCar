package com.xiaomi.domain.vo;

import io.swagger.annotations.ApiModelProperty;

public class WarnVo {
    @ApiModelProperty(value = "车架编号", required = true)
    private Integer carId;

    @ApiModelProperty(value = "电池类型", required = true)
    private String batteryType;

    @ApiModelProperty(value = "规则名称", required = true)
    private String warnName;

    @ApiModelProperty(value = "报警等级", required = true)
    private Byte warnLevel;
}
