package com.xiaomi.util;

public class Comparator {
    /**
     * 比较 number1 operator number2的结果,比如1<=2
     *
     * @param number1  左侧数字
     * @param operator 比较符号
     * @param number2  右侧数字
     * @return 两数字的比较结果
     */
    public static boolean compare(double number1, String operator, double number2) {
        switch (operator) {
            case ">=":
                return number1 >= number2;
            case ">":
                return number1 > number2;
            case "<=":
                return number1 <= number2;
            case "<":
                return number1 < number2;
            case "==":
                return number1 == number2;
            case "!=":
                return number1 != number2;
            default:
                throw new IllegalArgumentException("不支持的比较器:'" + operator + "',请检查公式");
        }
    }
}
