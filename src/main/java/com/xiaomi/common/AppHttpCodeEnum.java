package com.xiaomi.common;

public enum AppHttpCodeEnum {

    SUCCESS(200, "ok"),

    SIGN_INVALID(400, "无效的SIGN"),
    SIG_TIMEOUT(400, "SIGN已过期"),
    PARAM_REQUIRE(400, "缺少参数"),
    ILLEGAL_ARGUMENT(400, "非法参数"),

    NEED_LOGIN(401, "需要登录后操作"),
    LOGIN_PASSWORD_ERROR(401, "密码错误"),
    TOKEN_INVALID(401, "无效的TOKEN"),
    TOKEN_EXPIRE(401, "TOKEN已过期"),
    TOKEN_REQUIRE(401, "TOKEN是必须的"),

    NO_OPERATOR_AUTH(403, "无权限操作"),
    NEED_ADMIN(403, "需要管理员权限"),

    DATA_NOT_EXIST(404, "数据不存在"),

    DATA_EXIST(409, "数据已经存在"),

    SERVER_ERROR(500, "服务器内部错误");

    final int code;
    final String message;

    AppHttpCodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
