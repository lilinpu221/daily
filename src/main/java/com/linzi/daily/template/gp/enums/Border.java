package com.linzi.daily.template.gp.enums;

public enum Border {
    /**
     * 实线、点线、虚线
     */
    SOLID,DOT,DASH;

    public static Border explain(String type){
        return Border.valueOf(type.toUpperCase());
    }
}
