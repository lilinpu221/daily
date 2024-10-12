package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.enums.Element;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public abstract class BaseElement {
    /**
     * 元素唯一标识
     */
    private String id;
    /**
     * 起始横坐标x
     */
    private Integer x;
    /**
     * 起始纵坐标y
     */
    private Integer y;
    /**
     * 元素宽度(像素)
     */
    private Integer width;
    /**
     * 元素高度(像素)
     */
    private Integer height;
    /**
     * 元素旋转角度
     */
    private Integer rotation = 0;
    /**
     * 元素类型
     */
    private Element type;
    /**
     * 是否动态参数
     */
    private Boolean isField;
    /**
     * 参数名
     */
    private String fieldName;
    /**
     * 值
     */
    private String value;

    public abstract Boolean needToImg();

    public abstract BufferedImage getImage();
}
