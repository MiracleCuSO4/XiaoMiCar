package com.xiaomi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaomi.mapper")
public class WarnApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarnApplication.class, args);
    }
}
