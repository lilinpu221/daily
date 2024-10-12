package com.linzi.daily.template.jd;

import lombok.Data;

@Data
public abstract class BaseItem {

    /**
     * 元素类型
     */
    private String type;
    /**
     * x坐标
     */
    private int left;
    /**
     * y坐标
     */
    private int top;

    /**
     * 元素宽度
     */
    private int width;

    /**
     * 元素高度
     */
    private int height;

    /**
     * 元素数据内容（带@{}代表动态元素）
     */
    private String content;

    public abstract String builderTspl();
}
