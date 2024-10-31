package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.Tools;
import com.linzi.daily.template.gp.enums.Element;
import com.linzi.daily.template.gp.enums.Shape;
import lombok.Getter;
import lombok.Setter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 矩形
 * TSC 仅支持长方形
 * ZPL 支持所有形状
 */
@Getter
@Setter
public class ShapeElement extends BaseElement{
    private Float borderWidth = 2.0f;
    private Shape shape = Shape.RECTANGLE;

    public ShapeElement() {
        setType(Element.SHAPE);
    }
    @Override
    public Boolean needToImg() {
        return shape!=Shape.RECTANGLE;
    }

    @Override
    public BufferedImage getImage() {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 定义画布
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        //定义线条
        BasicStroke stokeLine = new BasicStroke(borderWidth);
        g2d.setStroke(stokeLine);
        switch (shape){
            case ROUND ->{
                g2d.drawRoundRect(0,0,getWidth(),getHeight(),20,20);
            }
            case OVAL ->{
                g2d.drawOval(0,0,getWidth(),getHeight());
            }
            case CIRCLE ->{
                g2d.drawOval(0,0,getWidth(),getWidth());
            }
            default -> {
                g2d.drawRect(0,0,getWidth(),getHeight());
            }
        }
        g2d.dispose();
        return Tools.compressImage(bi,0);
    }
}
