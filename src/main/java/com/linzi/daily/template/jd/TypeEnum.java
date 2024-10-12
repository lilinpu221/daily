package com.linzi.daily.template.jd;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * 组件类型枚举
 */
@AllArgsConstructor
public enum TypeEnum {
    /**
     * 文本
     */
    TEXT("text");

    private final String type;

    public static TypeEnum explain(String text){
        return Arrays.stream(TypeEnum.values())
                .filter(itemType->text.equals(itemType.type)).findFirst()
                .orElseThrow(()->new IllegalArgumentException("不支持的元素"));
    }
}
