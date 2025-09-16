package com.linzi.daily.template.gp.domain;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import com.linzi.daily.template.gp.Tools;
import com.linzi.daily.template.gp.entity.*;
import com.linzi.daily.template.gp.enums.*;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * TSC使用点阵字体
 * 字号单位（points），1点等于1/72英寸
 * DPI表示每英寸的点数
 * 每点的宽度(像素)= DPI/72，默认DPI=203
 * 203DPI，每个点的宽度(像素)=203/72=2.8194
 * 300DPI，每个点的宽度(像素)=300/72=4.1666
 * 字号大小(点) = 点阵高度(像素）/每点的宽度(像素）
 * @author Lil
 */
public class TscFunc extends AbstractLabelTemplate {
    private static final String WIN_LINE_END = "\r\n";
    private static final Charset GB18030 = Charset.forName("GB18030");

    public TscFunc(Layout layout) {
        super(layout);
    }

    @Override
    protected String init() {
        return HexUtil.encodeHexStr("""
                SET HEAD ON
                SET PRINTKEY OFF
                SET KEY1 ON
                SET KEY2 ON
                CLS
                """, GB18030);
    }

    @Override
    protected String buildHeader() {
        return HexUtil.encodeHexStr("SIZE "+getLayout().getWidth()+" mm,"+ getLayout().getHeight()+" mm"+WIN_LINE_END+
                getPaperType(getLayout().getPaperType(), getLayout().getGap())+WIN_LINE_END+
                "SPEED "+getSpeed(getLayout().getSpeed())+WIN_LINE_END+
                "DENSITY "+getDensity(getLayout().getDensity())+WIN_LINE_END+
                "SET TEAR "+(getLayout().getTear()?"ON":"OFF")+WIN_LINE_END+
                "SET RIBBON "+(getLayout().getMode()== Method.THERMAL?"OFF":"ON")+WIN_LINE_END+
                "SHIFT "+getLayout().getShift()+WIN_LINE_END,GB18030);
    }

    @Override
    protected String handleTextElement(TextElement textElement) {
        if(textElement.needToImg()){
            return getTextImage(textElement);
        }else{
            int oldSize = textElement.getFontSize();
            TscFont tscFont = TscFont.explain(textElement.getFontFamily(),oldSize);
            //放大倍数
            int multi = oldSize/tscFont.getSize();
            //先使用实际字体宽度*倍数计算文本分行
            textElement.setFontSize(tscFont.getWidth()*multi);
            List<String> lineList = textElement.textWrap();
            return parseTextElement(textElement,tscFont,lineList,multi);
        }
    }

    @Override
    protected String handleBarCodeElement(BarCodeElement barCodeElement) {
        if(barCodeElement.needToImg()){
            //google zxing生成的条码会有空白间距
            barCodeElement.setX(barCodeElement.getX()-20);
            return getTextImage(barCodeElement);
        }else{
            return parseBarCodeElement(barCodeElement);
        }
    }

    @Override
    protected String handleQrCodeElement(QrCodeElement qrCodeElement) {
        return parseQrCodeElement(qrCodeElement);
    }

    @Override
    protected String handleShapeElement(ShapeElement shapeElement) {
        if(shapeElement.needToImg()){
            return getTextImage(shapeElement);
        }else{
            return parseShapeElement(shapeElement);
        }
    }

    @Override
    protected String handleLineElement(LineElement lineElement) {
        if(lineElement.needToImg()){
            return getTextImage(lineElement);
        }else{
            return parseLineElement(lineElement);
        }
    }

    @Override
    protected String handleVLineElement(VLineElement vLineElement) {
        if(vLineElement.needToImg()){
            return getTextImage(vLineElement);
        }else{
            return parseVLineElement(vLineElement);
        }
    }

    @Override
    protected String handleImageElement(ImageElement element) {
        BufferedImage bi;
        if(element.getImgType()== ImageType.LOCAL){
            //去除base64前缀
            String base64 = element.getValue().substring(element.getValue().indexOf(Tools.BASE64_PREFIX)+7);
            //BASE64数据转图片
            try {
                bi = Thumbnails.of(ImgUtil.toImage(base64)).forceSize(element.getWidth(),element.getHeight()).asBufferedImage();
            } catch (IOException e) {
                return CharSequenceUtil.EMPTY;
            }
        }else{
            try {
                //URL转图片
                bi = Thumbnails.of(new URL(element.getValue())).forceSize(element.getWidth(),element.getHeight()).asBufferedImage();
            } catch (IOException e) {
                return CharSequenceUtil.EMPTY;
            }
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int hModByte = (height%8)==0 ? 0 : 8-(height%8);
        //向上取整
        int wPrintByte = (int)Math.ceil((double)(width+wModByte)/8);
        int hPrintByte = height+hModByte;
        String imgHex = Tools.imgToBitmapHex(bi,Tools.IMAGE_THRESHOLD,1,0);
        return HexUtil.encodeHexStr("BITMAP "+element.getX()+","+element.getY()+","+wPrintByte+","+hPrintByte+",0,",GB18030)+imgHex+"0d0a";
    }
    @Override
    protected String buildTail() {
        return HexUtil.encodeHexStr("PRINT 1,"+getLayout().getNumber()+WIN_LINE_END,GB18030);
    }

    private String parseTextElement(TextElement textElement,TscFont tscFont,List<String> lineList,int multi){
        if(textElement.getFontFamily()!=FontFamily.SIMSUN&&textElement.getFontFamily()!=FontFamily.ARIAL){
            return CharSequenceUtil.EMPTY;
        }
        //文本框支持的最大行数
        int lineCount = textElement.getHeight()<tscFont.getHeight()?1:textElement.getHeight()/(tscFont.getHeight()*multi);
        StringBuilder textBuilder = new StringBuilder();
        int y = textElement.getY();
        int x = textElement.getX();
        for(int i=0;i<lineCount;i++) {
            if (i >= lineList.size()) {
                //超过文本总数
                break;
            }
            String lineText = lineList.get(i);
            int alignValue = Tools.textHorizontal(textElement.getWidth(),lineText,tscFont.getWidth()*multi,textElement.getTextAlign());
            switch (textElement.getRotation()){
                default -> {}
                case 0 -> {
                    if(TextAlign.LEFT!=textElement.getTextAlign()) {
                        //居左x不用变
                        x = x + alignValue;
                    }
                }
                case 180 ->{
                    if(TextAlign.LEFT!=textElement.getTextAlign()) {
                        //居左x不用变
                        x = x - alignValue;
                    }
                }
                case 90 -> {
                    if(TextAlign.LEFT!=textElement.getTextAlign()) {
                        //居左y不用变
                        y = y+alignValue;
                    }
                }
                case 270 -> {
                    if(TextAlign.LEFT!=textElement.getTextAlign()) {
                        //居左y不用变
                        y = y-alignValue;
                    }
                }
            }
            textBuilder.append("TEXT ").append(x).append(",").append(y).append(",").append(getTextStr(tscFont.getName(), textElement.getRotation(),multi))
                    .append(",").append("\"").append(lineText).append("\"").append(WIN_LINE_END);
            if (textElement.getFontBold()) {
                //加粗处理
                textBuilder.append("TEXT ").append(x - 1).append(",").append(y - 1).append(",").append(getTextStr(tscFont.getName(), textElement.getRotation(),multi))
                        .append(",").append("\"").append(lineText).append("\"").append(WIN_LINE_END);
            }
            //判断旋转
            switch (textElement.getRotation()){
                default ->{
                    x = textElement.getX();
                    y = y+(tscFont.getHeight()*multi);
                }
                case 90->{
                    x = x-(tscFont.getHeight()*multi);
                    y = textElement.getY();
                }
                case 180->{
                    x = textElement.getX();
                    y = y-(tscFont.getHeight()*multi);
                }
                case 270->{
                    x = x+(tscFont.getHeight()*multi);
                    y = textElement.getY();
                }

            }
        }
        return HexUtil.encodeHexStr(textBuilder.toString(),GB18030);
    }

    private String parseBarCodeElement(BarCodeElement element){
        int barCodeHeight = element.getHeight();
        //画笔中字体大小乘以2
        int wordHeight = element.getFontSize()*2;
        barCodeHeight = barCodeHeight - wordHeight - 1;
        return HexUtil.encodeHexStr("BARCODE "+element.getX()+","+element.getY()+",\""+getBarCode(element.getBarformat(),element.getValue())+"\","+barCodeHeight+","
                +(element.getBardisplay()>0?1:0)+","+element.getRotation()+",2,4,\""+element.getValue()+"\""+WIN_LINE_END,GB18030);
    }

    private String parseQrCodeElement(QrCodeElement element){
        return HexUtil.encodeHexStr("QRCODE "+element.getX()+","+element.getY()+","+element.getLevel().name()+","+element.getCellWidth()+",A,0,\""+element.getValue()+"\""+WIN_LINE_END,GB18030);
    }

    private String parseShapeElement(ShapeElement element){
        return HexUtil.encodeHexStr("BOX "+element.getX()+","+element.getY()+","+(element.getX()+element.getWidth())
                +","+(element.getY()+element.getHeight())+","+element.getBorderWidth().intValue()+WIN_LINE_END,GB18030);
    }

    private String parseLineElement(LineElement element){
        return HexUtil.encodeHexStr("BAR "+element.getX()+","+element.getY()+","+element.getWidth()+","+element.getBorderWidth()+WIN_LINE_END,GB18030);
    }

    private String parseVLineElement(VLineElement element){
        return HexUtil.encodeHexStr("BAR "+element.getX()+","+element.getY()+","+element.getBorderWidth()+","+element.getHeight()+WIN_LINE_END,GB18030);
    }

    private String getBarCode(BarCodeFormat format,String value){
        String formatStr;
        if(format==BarCodeFormat.ADAPT){
            format = format.adaptBarType(value);
        }
        switch (format){
            case CODE39 -> formatStr="39";
            case EAN13 -> formatStr="EAN13";
            case EAN8 -> formatStr="EAN8";
            case UPC -> formatStr="UPCA";
            default -> formatStr="128";
        }
        return formatStr;
    }

    private String getTextStr(String fontName,Integer rotation,int multi) {
        return "\""+ fontName+"\","+ rotation+"," +multi+","+multi;
    }
    private int getSpeed(Speed speedEnum){
        int speed;
        switch (speedEnum){
            case FASTEST->speed=4;
            case FAST->speed=3;
            case SLOW->speed=1;
            default->speed=2;
        }
        return speed;
    }

    private int getDensity(Density densityEnum){
        int density;
        switch (densityEnum){
            case LIGHT->density=0;
            case HEAVY->density=15;
            default->density=7;
        }
        return density;
    }

    private String getPaperType(PaperType paperTypeEnum, int gap){
        switch (paperTypeEnum){
            case GAP -> {
                return "GAP "+gap+" mm,0 mm";
            }
            case BLINE -> {
                return "BLINE "+gap+" mm,0 mm";
            }
            default -> {
                return "GAP 0 mm,0 mm";
            }
        }
    }

    private String getTextImage(BaseElement element){
        BufferedImage bi = element.getImage();
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int hModByte = (height%8)==0 ? 0 : 8-(height%8);
        int wPrintByte = (width+wModByte)/8;
        int hPrintByte = height+hModByte;
        String imgHex = Tools.imgToBitmapHex(bi,Tools.TEXT_THRESHOLD,1,0);
        int x = element.getX();
        int y = element.getY();
        switch (element.getRotation()){
            default -> {}
            case 90 -> x = x-width;
            case 180 -> {
                x = x-width;
                y = y-height;
            }
            case 270 -> y = y-height;
        }
        return HexUtil.encodeHexStr("BITMAP "+x+","+y+","+wPrintByte+","+hPrintByte+",0,",GB18030)+imgHex+"0d0a";
    }
}
