package com.xiaomi.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 通用的结果返回类
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private String msg;
    private T data;

    public Result() {
        this.status = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMessage();
    }

    public Result(Integer code, T data) {
        this.status = code;
        this.data = data;
    }

    public Result(Integer code, String message, T data) {
        this.status = code;
        this.msg = message;
        this.data = data;
    }

    public Result(Integer code, String message) {
        this.status = code;
        this.msg = message;
    }

    public static <T> Result<T> okResult(T data) {
        return new Result<>(AppHttpCodeEnum.SUCCESS.getCode(), AppHttpCodeEnum.SUCCESS.getMessage(), data);
    }

    public static Result<?> errorResult(Integer code, String message) {
        return new Result<>(code, message);
    }

    public static Result<?> errorResult(AppHttpCodeEnum enums) {
        return new Result<>(enums.getCode(), enums.getMessage());
    }

    public static Result<?> errorResult(AppHttpCodeEnum enums, String errorMessage) {
        return new Result<>(enums.getCode(), errorMessage);
    }

    private static Result<?> setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage) {
        return new Result<>(enums.getCode(), errorMessage);
    }

    public Result<T> error(Integer code, String message) {
        this.status = code;
        this.msg = message;
        return this;
    }

    public Result<T> ok(T data) {
        this.data = data;
        return this;
    }

    public Result<T> ok(Integer code, T data) {
        this.status = code;
        this.data = data;
        return this;
    }

    public Result<T> ok(Integer code, T data, String message) {
        this.status = code;
        this.data = data;
        this.msg = message;
        return this;
    }
}
