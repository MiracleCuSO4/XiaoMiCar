package com.xiaomi.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 车辆信息表
 */
@Data
@ApiModel(description = "车辆信息表")
@TableName("car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆识别码Vehicle Identification")
    @TableId
    private String vid;

    @ApiModelProperty(value = "车架编号")
    private Integer carId;

    @ApiModelProperty(value = "电池类型")
    private String batteryType;

    @ApiModelProperty(value = "总里程(km)")
    private Integer totalDistance;

    @ApiModelProperty(value = "电池健康状态(%)")
    private Integer batteryHealth;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "0=未删除,1=逻辑删除")
    @TableLogic(value = "0", delval = "1")
    private Byte isDelete;
}
