package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.Tools;
import com.linzi.daily.template.gp.enums.Element;
import com.linzi.daily.template.gp.enums.FontFamily;
import com.linzi.daily.template.gp.enums.TextAlign;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文本框元素
 * TSC和ZPL指令和图片支持度一致
 */
@Getter
@Setter
public class TextElement extends BaseElement{
    /**
     * 字体类型(默认宋体)
     */
    private FontFamily fontFamily = FontFamily.SIMSUN;
    /**
     * 字号大小(默认8)
     */
    private Integer fontSize = 8;
    /**
     * 加粗(默认否)
     */
    private Boolean fontBold = false;
    /**
     * 斜体(默认否)
     */
    private Boolean fontItalic = false;
    /**
     * 删除线(默认否)
     */
    private Boolean fontDelete = false;
    /**
     * 下划线(默认否)
     */
    private Boolean fontUnderline = false;
    /**
     * 水平对齐方式(默认居左)
     */
    private TextAlign textAlign = TextAlign.LEFT;
    /**
     * 自动换行(默认否)
     */
    private Boolean textWrap = false;

    private static final Pattern P_CHINESE = Pattern.compile("[\\u4e00-\\u9fa5\\u3000-\\u303f\\uff00-\\uffef]");
    public TextElement(){
        setType(Element.TEXT);
    }

    @Override
    public Boolean needToImg() {
        //指令无法处理需要转图片的判断逻辑
        return fontFamily.getNeedImg()||fontItalic||fontDelete||fontUnderline;
    }

    @Override
    public BufferedImage getImage() {
        //画笔中字体大小乘以2
        this.fontSize = fontSize * 2;
        BufferedImage resultImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = resultImg.createGraphics();
        //开启文字抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 定义画布
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        // 定义字体样式
        int fontStyle = Font.PLAIN;
        if (Boolean.TRUE.equals(fontBold)) {
            fontStyle |= Font.BOLD;
        }
        if (Boolean.TRUE.equals(fontItalic)) {
            fontStyle |= Font.ITALIC;
        }
        Font font = new Font(fontFamily.getName(), fontStyle, fontSize);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        // 文字换行处理
        List<String> lineList = textWrap();
        // 画布支持的最大行数
        int lineCount = Math.max(1, getHeight() / fontSize);
        for (int i = 0; i < lineCount; i++) {
            if (i >= lineList.size()) {
                // 超过文本总数
                break;
            }
            String lineValue = lineList.get(i);
            // 计算每行打印实际需要的宽度=文字宽度+文字个数*字间距
            int factLineWidth = metrics.stringWidth(lineValue) + (lineValue.length() - 1);
            // 计算字体的坐标起始点
            int x;
            int y = fontSize * (i+1);
            x = switch (textAlign) {
                case CENTER -> (getWidth() - factLineWidth) / 2;
                case RIGHT -> getWidth() - factLineWidth;
                default -> 1;
            };
            AttributedString as = new AttributedString(lineValue);
            as.addAttribute(TextAttribute.FONT, font);
            if (Boolean.TRUE.equals(fontUnderline)) {
                // 下划线
                as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            }
            if (Boolean.TRUE.equals(fontDelete)) {
                // 删除线
                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            }
            g2d.drawString(as.getIterator(), x, y);
        }
        g2d.dispose();
        return Tools.compressImage(resultImg);
    }


    public List<String> textWrap() {
        List<String> textList = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        int factWidth = 0;
        char[] textChars = getValue().toCharArray();
        for (int i = 0; i < textChars.length; i++) {
            char c = textChars[i];
            int fontFactWidth = (P_CHINESE.matcher(String.valueOf(c)).find() ? fontSize : fontSize / 2);
            if (factWidth + fontFactWidth >= getWidth()) {
                // 需要换行
                textList.add(line.toString());
                line = new StringBuilder();
                factWidth = fontFactWidth;
            } else {
                factWidth += fontFactWidth;
            }
            line.append(c);
            // 字间距使用空格补充（避免在最后一个字符后添加空格）
//            if (i < textChars.length - 1) {
//                line.append(" ".repeat(Math.max(0, letterSpacing)));
//                // 更新宽度
//                factWidth += letterSpacing;
//            }
            // 处理最后一行
            if (i == textChars.length - 1 && !line.isEmpty()) {
                textList.add(line.toString());
            }
        }
        if(!textWrap){
            //不换行返回第一行
            return List.of(textList.get(0));
        }
        return textList;
    }
}
