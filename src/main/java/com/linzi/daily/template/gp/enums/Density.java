package com.linzi.daily.template.gp.enums;

public enum Density {
    /**
     * 打印浓度
     */
    LIGHT,NORMAL,HEAVY;

    public static Density explain(String density){
        return Density.valueOf(density.toUpperCase());
    }
}
