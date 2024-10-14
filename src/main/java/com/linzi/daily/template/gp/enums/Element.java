package com.linzi.daily.template.gp.enums;

import java.util.Arrays;

public enum Element {
    /**
     * 元素类型
     */
    TEXT,BARCODE,QRCODE,SHAPE,LINE,VLINE,IMAGE;

    public static Element explain(String type){
        return Element.valueOf(type.toUpperCase());
    }
}
