package com.xiaomi.common;

public class RedisConstant {
    // TTL ms
    public static final long TTL = 1000 * 3600 * 2; // 2小时

    // 规则
    public static final String RULE_QUERY_KEY = "ruleQuery:%s:%s"; // 根据warnId与car.batteryType查询的规则列表
    public static final String MATCH_ALL_WARN_ID = "matchAllWarnId";

    // 汽车
    public static final String CAR_KEY = "car:%s"; // 根据carId

}
