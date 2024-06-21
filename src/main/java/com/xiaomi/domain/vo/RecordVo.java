package com.xiaomi.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 预警记录表
 */
@Data
@ApiModel(description = "预警记录vo")
public class RecordVo {

    @ApiModelProperty(value = "预警记录id", required = true)
    private Integer id;

    @ApiModelProperty(value = "车辆识别码Vehicle Identification", required = true)
    private String vid;

    @ApiModelProperty(value = "规则序号", required = true)
    private Integer ruleId;

    @ApiModelProperty(value = "报警等级", required = true)
    private Byte warnLevel;

    @ApiModelProperty(value = "报警信息", required = true)
    private String message;

    @ApiModelProperty(value = "发生时间", required = true)
    private Timestamp occurTime;

    @ApiModelProperty(value = "处理时间")
    private Timestamp dealTime;

}
