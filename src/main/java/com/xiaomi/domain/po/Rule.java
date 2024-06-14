package com.xiaomi.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 规则表
 */
@Data
@ApiModel(description = "规则表")
@TableName("rule")
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则序号")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "规则编号id")
    private Integer warnId;

    @ApiModelProperty(value = "规则名称")
    private String warnName;

    @ApiModelProperty(value = "电池类型")
    private String batteryType;

    @ApiModelProperty(value = "预警规则")
    private String rule;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "0=未删除,1=逻辑删除")
    private Byte isDelete;
}
