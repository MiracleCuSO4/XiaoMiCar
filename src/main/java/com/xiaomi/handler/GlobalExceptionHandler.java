package com.xiaomi.handler;

import com.xiaomi.common.Result;
import com.xiaomi.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.xiaomi.common.AppHttpCodeEnum.ILLEGAL_ARGUMENT;
import static com.xiaomi.common.AppHttpCodeEnum.SERVER_ERROR;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public Result<?> baseExceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.errorResult(SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler
    public Result<?> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.errorResult(ILLEGAL_ARGUMENT, ex.getMessage());
    }



}
