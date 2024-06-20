package com.xiaomi.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.xiaomi.domain.rule.Condition;
import com.xiaomi.domain.rule.FormulaRateConfig;
import com.xiaomi.domain.rule.Rate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 规则表
 */
@Data
@ApiModel(description = "规则表")
@TableName(value = "rule",autoResultMap = true)
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则序号id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "规则编号warnId")
    private Integer warnId;

    @ApiModelProperty(value = "规则名称")
    private String warnName;

    @ApiModelProperty(value = "电池类型")
    private String batteryType;

    @ApiModelProperty(value = "预警规则")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private FormulaRateConfig formulaRateConfig;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "0=未删除,1=逻辑删除")
    private Byte isDelete;

    public String getDescription(double value, Rate rate){
        StringBuilder stringBuilder = new StringBuilder();
        List<Condition> conditionList = rate.getCondition();
        for (Condition condition : conditionList) {
            stringBuilder.append(value).append(" ").append(condition.getOperator()).append(" ").append(condition.getValue());
            stringBuilder.append(" && ");
        }
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length()); // 去掉最后多余的&&
        return stringBuilder.toString();
    }
}
