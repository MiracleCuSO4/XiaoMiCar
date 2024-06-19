package com.xiaomi.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆信息vo
 */
@Data
@ApiModel(description = "车辆信息vo")
public class CarVo {

    @ApiModelProperty(value = "车辆识别码Vehicle Identification")
    private String vid;

    @ApiModelProperty(value = "车架编号")
    private Integer carId;

    @ApiModelProperty(value = "电池类型")
    private String batteryType;

    @ApiModelProperty(value = "总里程(km)")
    private Integer totalDistance;

    @ApiModelProperty(value = "电池健康状态(%)")
    private Integer batteryHealth;
}
