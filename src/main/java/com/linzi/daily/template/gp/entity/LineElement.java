package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.Tools;
import com.linzi.daily.template.gp.enums.Border;
import com.linzi.daily.template.gp.enums.Element;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Getter
@Setter
public class LineElement extends BaseElement{

    private Float borderWidth = 2.0f;
    private Border borderType = Border.SOLID;

    public LineElement(){
        setType(Element.LINE);
    }

    @Override
    public Boolean needToImg() {
        return borderType!=Border.SOLID;
    }

    @Override
    public BufferedImage getImage() {
        //定义画布大小
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 定义画布
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        int x2 = 0;
        int y2= 0;
        //线宽
        int borderWidth;
        //横线和竖线处理
        if(getWidth()>getHeight()){
            //横线
            x2 = getWidth();
            borderWidth = getHeight();
        }else{
            //竖线
            y2 = getHeight();
            borderWidth = getWidth();
        }
        if (Objects.requireNonNull(borderType) == Border.DOT) {
            //点虚线
            g2d.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{borderWidth, borderWidth*2}, 0));
        } else {
            //横虚线
            g2d.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{borderWidth*2, borderWidth*4}, 0));
        }
        g2d.drawLine(0, 0, x2, y2);
        g2d.dispose();
        return Tools.compressImage(bi);
    }
}