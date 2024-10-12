package com.linzi.daily.template.gp.enums;

/**
 * @author Lil
 */

public enum Method {
    /**
     * 打印方式
     */
    THERMAL,THERMALTRANSFER;

    public static Method explain(String method) {
        return Method.valueOf(method.toUpperCase());
    }
}
