package com.xiaomi.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 预警记录表
 */
@Data
@ApiModel(description = "预警记录表")
@TableName("record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警记录id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "车辆识别码Vehicle Identification")
    private String vid;

    @ApiModelProperty(value = "规则序号")
    private Integer ruleId;

    @ApiModelProperty(value = "报警等级")
    private Byte warnLevel;

    @ApiModelProperty(value = "报警信息")
    private String message;

    @ApiModelProperty(value = "发生时间")
    private Timestamp occurTime;

    @ApiModelProperty(value = "处理时间")
    private Timestamp dealTime;

    @ApiModelProperty(value = "0=未删除,1=逻辑删除")
    @TableLogic(value = "0", delval = "1")
    private Byte isDelete;
}
