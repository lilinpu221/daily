package com.linzi.daily.template.gp.enums;

public enum ErrorCorrection {

    /**
     * 纠错等级
     */
    L,M,Q,H;

    public static ErrorCorrection explain(String level) {
        return ErrorCorrection.valueOf(level.toUpperCase());
    }
}
