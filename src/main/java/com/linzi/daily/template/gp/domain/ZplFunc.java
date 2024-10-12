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
                "^LT"+this.getLayout().getShift();
    }

    @Override
    protected String handleTextElement(TextElement textElement) {
        if(textElement.needToImg()){
            return getImage(textElement);
        }else{
            List<String> lineList = textElement.textWrap();
            return parseFont(textElement,lineList);
        }
    }

    @Override
    protected String handleBarCodeElement(BarCodeElement barCodeElement) {
        if(barCodeElement.needToImg()){
            return getImage(barCodeElement);
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
            return getImage(lineElement);
        }else{
            return parseLineElement(lineElement);
        }
    }

    @Override
    protected String handleImageElement(ImageElement element) {
        BufferedImage bi;
        if(element.getImgType()== ImageType.LOCAL){
            //BASE64图片数据
            bi = ImgUtil.toImage(element.getValue());
        }else{
            try {
                //URL转图片
                bi = Thumbnails.of(new URL(element.getValue())).asBufferedImage();
            } catch (IOException e) {
                return CharSequenceUtil.EMPTY;
            }
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int wPrintByte = (width+wModByte)/8;
        String imgHex = Tools.imgToBitmapHex(bi,0,1);
        return "~DGGRAPHIC1," + imgHex.length() / 2 + "," + wPrintByte + "," + Tools.zebraCompress(imgHex) + WIN_LINE_END +
                "^FO" + element.getX() + "," + element.getY() + "^XGGRAPHIC1"+WIN_LINE_END;
    }

    @Override
    protected String buildTail() {
        return "^PQ"+getLayout().getNumber()+",0,0,Y" + WIN_LINE_END + "^XZ"+ WIN_LINE_END;
    }

    private String parseFont(TextElement element, List<String> lineList){
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
        for(int i=0;i<lineCount;i++) {
            if (i >= lineList.size()) {
                //超过文本总数
                break;
            }
            String lineText = lineList.get(i);
            if(i==lineCount-1){
                //最后一行，计算水平对齐的起始坐标
                x = Tools.textHorizontal(x,element.getWidth(),lineText,fontWidth,element.getTextAlign());
            }
            textBuilder.append("^FO").append(x).append(",").append(y).append("^AA,")
                    .append(getRevolve(element.getRotation())).append(fontWidth).append(",").append(fontHight)
                    .append("^FD").append(lineText).append("^FS").append(WIN_LINE_END);
            if (element.getFontBold()) {
                //加粗处理
                textBuilder.append("^FO").append(x-1).append(",").append(y-1).append("^AA,")
                        .append(getRevolve(element.getRotation())).append(fontWidth).append(",").append(fontHight)
                        .append("^FD").append(lineText).append("^FS").append(WIN_LINE_END);
                textBuilder.append("^FO").append(x+1).append(",").append(y+1).append("^AA,")
                        .append(getRevolve(element.getRotation())).append(fontWidth).append(",").append(fontHight)
                        .append("^FD").append(lineText).append("^FS").append(WIN_LINE_END);
            }
            y = y+fontHight;
        }
        return textBuilder.toString();
    }

    private String parseBarCodeElement(BarCodeElement element,String value){
        String formatStr = "^FO"+element.getX()+","+element.getY();
        if(element.getBarformat()==BarCodeFormat.ADAPT){
            BarCodeFormat adaptFormate = element.getBarformat().adaptBarType(value);
            element.setBarformat(adaptFormate);
        }
        switch (element.getBarformat()){
            case CODE39 -> formatStr+="^B3"+getRevolve(element.getRotation())+",N,"+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N");
            case EAN13 -> formatStr+="^BE"+getRevolve(element.getRotation())+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N");
            case EAN8 -> formatStr+="^B8"+getRevolve(element.getRotation())+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N");
            case UPCA -> formatStr+="^BU"+getRevolve(element.getRotation())+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N")+",N";
            default -> formatStr+="^BC"+getRevolve(element.getRotation())+","+element.getHeight()+","
                    +(element.getBardisplay()>0?"Y":"N")+","+(element.getBardisplay()>1?"Y":"N")+",N,A";
        }
        formatStr+="^FD"+element.getValue()+"^FS"+WIN_LINE_END;
        return formatStr;
    }

    private String parseQrCodeElement(QrCodeElement element){
        return "^FO"+element.getX()+","+element.getY()+"^BQ,2,3"+element.getLevel().name()+",7^FD"+element.getValue()+"^FS"+WIN_LINE_END;
    }

    private String parseShapeElement(ShapeElement element){
        String shapStr = "^FO"+element.getX()+","+element.getY();
        switch (element.getShape()){
            case ROUND -> shapStr += "^GB"+element.getWidth()+","+element.getHeight()+","+element.getBorderWidth().intValue()+"B,2";
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

    private String getImage(BaseElement element){
        BufferedImage bi = element.getImage();
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int wPrintByte = (width+wModByte)/8;
        String imgHex = Tools.imgToBitmapHex(bi,0,1);
        return "~DGGRAPHIC1," + imgHex.length() / 2 + "," + wPrintByte + "," + Tools.zebraCompress(imgHex) + WIN_LINE_END +
                "^FO" + element.getX() + "," + element.getY() + "^XGGRAPHIC1"+WIN_LINE_END;
    }
}
