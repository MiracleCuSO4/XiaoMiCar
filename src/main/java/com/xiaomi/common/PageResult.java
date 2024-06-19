package com.xiaomi.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("分页查询结果")
public class PageResult<T> implements Serializable {

    @ApiModelProperty(value = "总记录数")
    private long total;

    @ApiModelProperty(value = "当前页数据集合")
    private List<T> records;

}