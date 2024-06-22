package com.xiaomi.handler;

import com.xiaomi.common.Result;
import com.xiaomi.exception.BaseException;
import com.xiaomi.exception.DataNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

import static com.xiaomi.common.AppHttpCodeEnum.*;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("参数校验错误: ", ex);
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return Result.errorResult(HttpStatus.BAD_REQUEST.value(), "参数校验错误", errors);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(ValidationException ex) {
        log.error("参数校验错误: ", ex);
        return Result.errorResult(HttpStatus.BAD_REQUEST.value(), "参数校验错误: " + ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handlerDataNotExistException(DataNotExistException ex) {
        log.warn("数据不存在异常: {}", ex.getMessage());
        return Result.errorResult(DATA_NOT_EXIST, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handlerIllegalArgumentException(IllegalArgumentException ex) {
        log.error("非法参数异常: {}", ex.getMessage());
        return Result.errorResult(ILLEGAL_ARGUMENT, ex.getMessage());
    }

    /**
     * 捕获通用业务异常
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handlerBaseException(BaseException ex) {
        log.error("通用业务异常: {}", ex.getMessage());
        return Result.errorResult(SERVER_ERROR, ex.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Result<?> handlerException(Exception ex) {
//        log.error("异常: {}", ex.getMessage());
//        ex.printStackTrace();
//        return Result.errorResult(SERVER_ERROR, ex.getMessage());
//    }



}
