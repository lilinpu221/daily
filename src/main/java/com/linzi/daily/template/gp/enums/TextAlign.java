package com.linzi.daily.template.gp.enums;

public enum TextAlign {
    /**
     * 文字水平对齐方式
     */
    LEFT, CENTER, RIGHT;

    public static TextAlign explain(String align) {
        return TextAlign.valueOf(align.toUpperCase());
    }
}
