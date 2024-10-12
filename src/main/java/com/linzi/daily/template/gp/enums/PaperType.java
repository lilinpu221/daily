package com.linzi.daily.template.gp.enums;

/**
 * @author Lil
 */

public enum PaperType {
    /**
     * 耗材类型:
     * 间隙纸
     * 黑标纸
     * 连续纸
     */
    GAP,BLINE,COILED;

    public static Density explain(String type){
        return Density.valueOf(type.toUpperCase());
    }
}
