package com.xiaomi.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageRequest {

    @NotNull(message = "pageNumber不能为空")
    private Integer pageNumber;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    public void checkParam() {
        if(this.pageNumber <= 0){
            this.pageNumber = 1;
        }
        if(this.pageSize <= 0 || this.pageSize > 100){
            this.pageSize = 10;
        }
    }
}
