package com.linzi.daily.template.gp.domain;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.linzi.daily.template.gp.Tools;
import com.linzi.daily.template.gp.entity.*;
import com.linzi.daily.template.gp.enums.*;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * ZPL使用矢量字体
 * @author Lil
 */
public class ZplFunc extends AbstractLabelTemplate {

    private static final String WIN_LINE_END = "\n";
    public ZplFunc(Layout layout) {
        super(layout);
    }

    @Override
    protected String init() {
        return """
                ^XA
                ^MCY^LT0^PMN^JMA^LH0,0^LRN^CWA,E:MSUNG.FNT^CWB,E:MSUNG.FNT^CI28
                """;
    }

    @Override
    protected String buildHeader() {
        return "^PW"+this.getLayout().getWidth()*8+"^LL"+this.getLayout().getHeight()*8+
                "^PR"+getSpeed(this.getLayout().getSpeed())+
                "^MD"+getDensity(this.getLayout().getDensity())+
                "^MM"+(this.getLayout().getTear()?"T":"P")+
                "^MT"+(this.getLayout().getMode()== Method.THERMAL?"D":"T")+
                "^MN"+getPaperType(this.getLayout().getPaperType())+
                "^LT"+this.getLayout().getShift()+ WIN_LINE_END;
    }

    @Override
    protected String handleTextElement(TextElement textElement) {
        if(textElement.needToImg()){
            return getTextImage(textElement);
        }else{
            //矢量字体字号转像素比
            int factFontSize = Float.valueOf(textElement.getFontSize()*Tools.VECTOR_FONT_RATE).intValue();
            textElement.setFontSize(factFontSize);
            List<String> lineList = textElement.textWrap();
            return parseTextElement(textElement,lineList);
        }
    }

    @Override
    protected String handleBarCodeElement(BarCodeElement barCodeElement) {
        if(barCodeElement.needToImg()){
            return getTextImage(barCodeElement);
        }else{
            return parseBarCodeElement(barCodeElement,barCodeElement.getValue());
        }
    }

    @Override
    protected String handleQrCodeElement(QrCodeElement qrCodeElement) {
        return parseQrCodeElement(qrCodeElement);
    }

    @Override
    protected String handleShapeElement(ShapeElement shapeElement) {
        return parseShapeElement(shapeElement);
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
        int wPrintByte = (width+wModByte)/8;
        String imgHex = Tools.imgToBitmapHex(bi,Tools.IMAGE_THRESHOLD,0,1);
        return "~DGGRAPHIC1," + imgHex.length() / 2 + "," + wPrintByte + "," + Tools.zebraCompress(imgHex) + WIN_LINE_END +
                "^FO" + element.getX() + "," + element.getY() + "^XGGRAPHIC1"+WIN_LINE_END;
    }

    @Override
    protected String buildTail() {
        return "^PQ"+getLayout().getNumber()+",0,0,Y" + WIN_LINE_END + "^XZ"+ WIN_LINE_END;
    }

    private String parseTextElement(TextElement element, List<String> lineList){
        if(element.getFontFamily()!= FontFamily.SIMSUN&&element.getFontFamily()!=FontFamily.ARIAL){
            return CharSequenceUtil.EMPTY;
        }
        //矢量字体中，字号通常指的是字体的高度
        int fontWidth = element.getFontSize();
        int fontHight = element.getFontSize();
        //文本框支持的最大行数
        int lineCount = element.getHeight()<fontHight?1:element.getHeight()/fontHight;
        StringBuilder textBuilder = new StringBuilder();
        int y = element.getY();
        int x = element.getX();
        //旋转角度的坐标初始化处理
        switch (element.getRotation()){
            default -> {}
            case 90 -> {
                x = x-(element.getHeight()/2);
            }
            case 180 -> {
                x = x-element.getWidth();
            }
            case 270 -> {
                y = y-element.getWidth();
            }
        }
        for(int i=0;i<lineCount;i++) {
            if (i >= lineList.size()) {
                //超过文本总数
                break;
            }
            String lineText = lineList.get(i);
            //对齐方式处理
            int alignValue = Tools.textHorizontal(element.getWidth(),lineText,fontWidth,element.getTextAlign());
            switch (element.getRotation()){
                case 0 -> {
                    if(TextAlign.LEFT!=element.getTextAlign()) {
                        //居左x不用变
                        x = x + alignValue;
                    }
                }
                case 180 -> {
                    if(TextAlign.RIGHT!=element.getTextAlign()) {
                        //居右x不用变
                        x = x + alignValue;
                    }
                }
                case 90 -> {
                    if(TextAlign.LEFT!=element.getTextAlign()) {
                        //居左y不用变
                        y = y+alignValue;
                    }
                }
                case 270 ->{
                    if(TextAlign.RIGHT!=element.getTextAlign()){
                        //居右y不用变
                        y = y+alignValue;
                    }
                }
                default -> {}
            }
            textBuilder.append("^FW").append(getRevolve(element.getRotation())).append(WIN_LINE_END);
            textBuilder.append("^FO").append(x).append(",").append(y).append("^AA,")
                    .append(fontWidth).append(",").append(fontHight)
                    .append("^FD").append(lineText).append("^FS").append(WIN_LINE_END);
            if (element.getFontBold()) {
                //加粗处理
                textBuilder.append("^FO").append(x-1).append(",").append(y-1).append("^AA,")
                        .append(fontWidth).append(",").append(fontHight)
                        .append("^FD").append(lineText).append("^FS").append(WIN_LINE_END);
            }
            //下一行数据处理
            switch (element.getRotation()){
                default -> {
                    x = element.getX();
                    y = y+fontHight;
                }
                case 90 -> {
                    x = x-fontHight;
                    y = element.getY();
                }
                case 180 -> {
                    x = element.getX()-element.getWidth();
                    y = y-fontHight;
                }
                case 270 -> {
                    x = x+fontHight;
                    y = element.getY()-element.getWidth();
                }
            }
        }
        return textBuilder.toString();
    }

    private String parseBarCodeElement(BarCodeElement element,String value){
        int x = element.getX();
        int y = element.getY();
        switch (element.getRotation()){
            default -> {}
            case 90 -> {
                x = x-(element.getHeight()/2);
            }
            case 180 -> {
                x = x-element.getWidth();
            }
            case 270 -> {
                y = y-element.getWidth();
            }
        }
        String formatStr = "^FW"+getRevolve(element.getRotation())+WIN_LINE_END+"^FO"+x+","+y;
        if(element.getBarformat()==BarCodeFormat.ADAPT){
            BarCodeFormat adaptFormate = element.getBarformat().adaptBarType(value);
            element.setBarformat(adaptFormate);
        }
        switch (element.getBarformat()){
            case CODE39 -> formatStr+="^B3"+",N,"+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N");
            case EAN13 -> formatStr+="^BE"+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N");
            case EAN8 -> formatStr+="^B8"+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N");
            case UPC -> formatStr+="^BU"+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N")+",N";
            default -> formatStr+="^BC"+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N")+",N,A";
        }
        formatStr+="^FD"+element.getValue()+"^FS"+WIN_LINE_END;
        return formatStr;
    }

    private String parseQrCodeElement(QrCodeElement element){
        return "^FO"+element.getX()+","+element.getY()+"^BQ,2,"+element.getCellWidth()+","+element.getLevel().name()+",7^FDLA,"+element.getValue()+"^FS"+WIN_LINE_END;
    }

    private String parseShapeElement(ShapeElement element){
        String shapStr = "^FO"+element.getX()+","+element.getY();
        switch (element.getShape()){
            case ROUND -> shapStr += "^GB"+element.getWidth()+","+element.getHeight()+","+element.getBorderWidth().intValue()+",B,2";
            case OVAL -> shapStr += "^GE"+element.getWidth()+","+element.getHeight()+","+element.getBorderWidth().intValue();
            case CIRCLE -> shapStr += "^GC"+element.getWidth()+","+element.getBorderWidth().intValue();
            default -> shapStr += "^GB"+element.getWidth()+","+element.getHeight()+","+element.getBorderWidth().intValue();
        }
        shapStr += "^FS"+WIN_LINE_END;
        return shapStr;
    }

    private String parseLineElement(LineElement element){
        return "^FO"+element.getX()+","+element.getY()+"^GB"+element.getWidth()+","+element.getHeight()+","+element.getBorderWidth().intValue()+"^FS"+WIN_LINE_END;
    }

    private String parseVLineElement(VLineElement element){
        return "^FO"+element.getX()+","+element.getY()+"^GB"+element.getWidth()+","+element.getHeight()+","+element.getBorderWidth().intValue()+"^FS"+WIN_LINE_END;
    }

    private int getSpeed(Speed speedEnum){
        int speed;
        switch (speedEnum){
            case FASTEST->speed=5;
            case FAST->speed=4;
            case SLOW->speed=2;
            default->speed=3;
        }
        return speed;
    }

    private int getDensity(Density densityEnum){
        int density;
        switch (densityEnum){
            case LIGHT->density=0;
            case HEAVY->density=30;
            default->density=15;
        }
        return density;
    }

    private String getPaperType(PaperType paperTypeEnum){
        String paparType;
        switch (paperTypeEnum){
            case GAP -> paparType="Y";
            case BLINE -> paparType="M";
            default -> paparType="N";
        }
        return paparType;
    }

    private String getRevolve(Integer rotation){
        String direction;
        switch (rotation){
            case 0 -> direction = "N";
            case 90 -> direction = "R";
            case 180 -> direction = "I";
            case 270 -> direction = "B";
            default -> direction = "N";
        }
        return direction;
    }

    private String getTextImage(BaseElement element){
        BufferedImage bi = element.getImage();
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int wPrintByte = (width+wModByte)/8;
        String imgHex = Tools.imgToBitmapHex(bi,Tools.TEXT_THRESHOLD,0,1);
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
        return "~DGGRAPHIC1," + imgHex.length() / 2 + "," + wPrintByte + "," + Tools.zebraCompress(imgHex) + WIN_LINE_END +
                "^FO" + x + "," + y + "^XGGRAPHIC1"+WIN_LINE_END;
    }
}
