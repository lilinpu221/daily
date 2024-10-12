package com.linzi.daily.template.jd.text;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum Space {

    /**
     * 截断
     */
    PRELINE("pre-line"),
    PREWRAP("pre-wrap"),
    COMPRESSWRAP("compress-wrap");

    private final String value;

    public static Space explain(String value){
        return Arrays.stream(Space.values())
                .filter(space ->value.equals(space.value)).findFirst()
                .orElse(COMPRESSWRAP);
    }
}
