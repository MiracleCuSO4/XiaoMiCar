package com.xiaomi.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 车辆信息dto
 */
@Data
@ApiModel(description = "车辆信息dto")
public class CarDto {

    @ApiModelProperty(value = "车辆识别码Vehicle Identification")
    private String vid;

    @ApiModelProperty(value = "车架编号")
    @NotNull(message = "车架编号不能为空")
    @Min(value = 0, message = "车架编号不能是负数")
    private Integer carId;

    @ApiModelProperty(value = "电池类型")
    @NotNull(message = "电池类型不能为空")
    private String batteryType;

    @ApiModelProperty(value = "总里程(km)")
    @Min(value = 0, message = "总里程不能是负数")
    private Integer totalDistance;

    @ApiModelProperty(value = "电池健康状态(%)")
    @Min(value = 0, message = "电池健康状态在0~100之间")
    @Max(value = 100, message = "电池健康状态在0~100之间")
    private Integer batteryHealth;
}
