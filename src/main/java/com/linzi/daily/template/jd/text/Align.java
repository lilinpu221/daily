package com.linzi.daily.template.jd.text;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * 文字垂直对齐
 */
@AllArgsConstructor
public enum Align {

    /**
     * 居上
     */
    START("flex-start"),
    /**
     * 居中
     */
    CENTER("center"),
    /**
     * 置底
     */
    END("flex-end");

    private final String align;

    public static Align explain(String align){
        return Arrays.stream(Align.values())
                .filter(fontAlign ->align.equals(fontAlign.align)).findFirst()
                .orElse(CENTER);
    }
}
