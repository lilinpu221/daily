package com.linzi.daily.template.gp.entity;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.linzi.daily.template.gp.Tools;
import com.linzi.daily.template.gp.enums.BarCodeFormat;
import com.linzi.daily.template.gp.enums.Element;
import com.linzi.daily.template.gp.enums.FontFamily;
import lombok.Getter;
import lombok.Setter;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.HashMap;

/**
 * 条码元素
 * TSC 指令不支持上方显示
 * ZPL 指令不支持文字对齐
 */
@Getter
@Setter
public class BarCodeElement extends BaseElement {

    /**
     * 条码类型
     */
    private BarCodeFormat barformat = BarCodeFormat.CODE128;
    /**
     * 文字显示方式(默认不显示)
     * 0 不显示
     * 1 显示在下方
     * 2 显示在上方
     */
    private Integer bardisplay = 0;
    /**
     * 字体类型(默认宋体)
     */
    private FontFamily fontFamily = FontFamily.ARIAL;
    /**
     * 字号大小(默认8)
     */
    private Integer fontSize = 8;

    public BarCodeElement(){
        setType(Element.BARCODE);
    }

    @Override
    public Boolean needToImg() {
        return fontFamily != FontFamily.ARIAL || fontSize != 8 || bardisplay == 2 ;
    }

    @Override
    public BufferedImage getImage() {
        int barCodeHeight = getHeight();
        //画笔中字体大小乘以2
        int wordHeight = Float.valueOf(fontSize*Tools.VECTOR_FONT_RATE).intValue();
        int textY = 1;
        int barCodeY = 1;
        //条码画布
        BufferedImage barcodeImage;
        if (bardisplay > 0) {
            //显示文字时需减去文字高度
            barCodeHeight = barCodeHeight - wordHeight - 1;
            if(bardisplay == 1){
                //文字显示在下方
                textY = barCodeHeight + 2;
            }else{
                //文字显示在上方
                barCodeY = wordHeight + 4;
            }
        }
        //条码属性
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.CODE128_COMPACT, true);
        try {
            //生成条码到内存
            BitMatrix bitMatrix = new MultiFormatWriter().encode(getValue(), Tools.getZxingFormat(getBarformat()),
                    getWidth(), barCodeHeight, hints);
            barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
        //总画布
        BufferedImage resultImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D resultG2d = resultImage.createGraphics();
        resultG2d.setBackground(Color.WHITE);
        resultG2d.setColor(Color.BLACK);
        resultG2d.clearRect(0, 0, getWidth(), getHeight());
        if(bardisplay > 0){
            //文字画布
            BufferedImage textImage = new BufferedImage(getWidth(), wordHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D textG2d = textImage.createGraphics();
            //开启文字抗锯齿
            textG2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            textG2d.setBackground(Color.WHITE);
            textG2d.setColor(Color.BLACK);
            textG2d.clearRect(0, 0, getWidth(), wordHeight);
            //定义文字
            Font font = new Font(fontFamily.getName(), Font.PLAIN, wordHeight);
            textG2d.setFont(font);
            AttributedString as = new AttributedString(getValue());
            as.addAttribute(TextAttribute.FONT, font);
            //对齐处理
            FontMetrics metrics = textG2d.getFontMetrics(font);
            //文字的实际宽度(文字固定居中)
            int factLineWidth = metrics.stringWidth(getValue());
            textG2d.drawString(as.getIterator(), (getWidth() - factLineWidth) / 2, wordHeight);
            textG2d.dispose();
            //文字添加到画布
            resultG2d.drawImage(textImage, 0, textY, textImage.getWidth(null), textImage.getHeight(null), Color.WHITE, null);
        }
        //条码添加到画布
        resultG2d.drawImage(barcodeImage, 0, barCodeY, barcodeImage.getWidth(null), barcodeImage.getHeight(null), Color.WHITE, null);
        resultG2d.dispose();
        return Tools.compressImage(resultImage,getRotation());
    }
}
