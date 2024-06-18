package com.xiaomi.handler;

import com.xiaomi.common.Result;
import com.xiaomi.exception.BaseException;
import com.xiaomi.exception.DataNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.xiaomi.common.AppHttpCodeEnum.*;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result<?> dataNotExistExceptionHandler(DataNotExistException ex) {
        log.error("数据不存在异常: {}", ex.getMessage());
        return Result.errorResult(DATA_NOT_EXIST, ex.getMessage());
    }

    @ExceptionHandler
    public Result<?> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("非法参数异常: {}", ex.getMessage());
        return Result.errorResult(ILLEGAL_ARGUMENT, ex.getMessage());
    }

    /**
     * 捕获通用业务异常
     */
    @ExceptionHandler
    public Result<?> baseExceptionHandler(BaseException ex) {
        log.error("通用业务异常: {}", ex.getMessage());
        return Result.errorResult(SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler
    public Result<?> exceptionHandler(Exception ex) {
        log.error("异常: {}", ex.getMessage());
        return Result.errorResult(SERVER_ERROR, ex.getMessage());
    }



}
