package com.linzi.daily.template.gp.enums;

public enum Shape {
    /**
     * 矩形，圆角矩形，椭圆形，圆形
     */
    RECTANGLE,ROUND,OVAL,CIRCLE;

    public static Shape explain(String shape){
        return Shape.valueOf(shape.toUpperCase());
    }
}
