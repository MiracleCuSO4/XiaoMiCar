package com.xiaomi.domain.dto;

import lombok.Data;

@Data
public class PageRequest {

    private Integer pageNumber;

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
