package com.linzi.daily.template.gp;

import com.google.zxing.BarcodeFormat;
import com.linzi.daily.template.gp.enums.BarCodeFormat;
import com.linzi.daily.template.gp.enums.TextAlign;
import com.linzi.daily.template.jd.Tool;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tools {
    private static final float SCALE_RATIO = 1.0f;
    private static final float QUALITY_RATIO = 0.8f;
    //203DPI，矢量字体对应每个点的宽度(像素)比值
    public static float VECTOR_FONT_RATE = 2.8194f;
    public static final String BASE64_PREFIX = "base64,";

    public static final int IMAGE_THRESHOLD = 128;

    public static final int TEXT_THRESHOLD = 180;

    public static String imgToBitmapHex(BufferedImage bi,int threshold,int whiteFill,int blackFill){
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int hModByte = (height%8)==0 ? 0 : 8-(height%8);
        //图片RGB属性
        StringBuilder res = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 下面三行代码将一个数字转换为RGB数字
                final int rgb = bi.getRGB(x, y);
                //默认白底，非透明底色使用阈值判断
                int bitStr = whiteFill;
                Color color = new Color(rgb,true);
                if(color.getAlpha()>0){
                    //透明低色直接用白色
                    final int r = (rgb >> 16) & 0xff;
                    final int g = (rgb >> 8) & 0xff;
                    final int b = rgb & 0xff;
                    //使用加权灰度算法
                    bitStr = (0.3 * r + 0.59 * g + 0.11 * b) >= threshold ? whiteFill : blackFill;
                    /**
                     * 使用平均值灰度算法
                     * int bitStr = (rgb[0] + rgb[1] + rgb[2])/3 >= RGB_THRESHOLD ? whiteFill : blackFill;
                     */
                }
                res.append(bitStr);
            }
            res.append(String.valueOf(whiteFill).repeat(Math.max(0, wModByte)));
        }
        res.append(String.valueOf(whiteFill).repeat(Math.max(0, width + wModByte)).repeat(Math.max(0, hModByte)));
        //转成十六进制字符串
        String bitTmp;
        String hex;
        String bitStr = res.toString();
        StringBuilder hexStr = new StringBuilder();
        int length = bitStr.length();
        for (int i = 0; i < length; i+=4){
            bitTmp = bitStr.substring(i, i+4);
            hex = Integer.toString (Integer.parseInt(bitTmp, 2), 16);
            hexStr.append(hex);
        }
        return hexStr.toString();
    }

    /**
     * 计算文本实际长度
     * @param text 文本内容
     * @param fontWidth 字体宽度
     * @return int
     */
    public static int textFactWidth(String text,int fontWidth){
        char[] textChars = text.toCharArray();
        int textWidth = 0;
        for (char c : textChars) {
            //英文/数字为字体大小/2，中文为字体实际大小宽度
            int tempWidth = fontWidth / 2;
            if (Tool.isChinese(c)) {
                tempWidth = fontWidth;
            }
            textWidth = textWidth + tempWidth;
        }
        return textWidth;
    }

    /**
     * 计算文本水平对齐差值
     * @param width 文本框宽度
     * @param text 文本内容
     * @param fontWidth 字体宽度
     * @param textAlign 水平对齐方式
     * @return int
     */
    public static int textHorizontal(int width, String text, int fontWidth, TextAlign textAlign){
        int textWidth = textFactWidth(text,fontWidth);
        int remainWidth = width-textWidth;
        int alignValue = 0;
        if(textAlign== TextAlign.CENTER){
            //居中，x坐标起始等于余数除以2
            alignValue = remainWidth/2;
        }else if (textAlign== TextAlign.RIGHT){
            //居右，x坐标起始等于余数
            alignValue=remainWidth;
        }
        return alignValue;
    }

    public static BufferedImage compressImage(BufferedImage source,double rotate) {
        try {
            return Thumbnails.of(source).scale(SCALE_RATIO).rotate(rotate).outputQuality(QUALITY_RATIO).asBufferedImage();
        } catch (IOException e) {
            return source;
        }
    }

    public static BarcodeFormat getZxingFormat(BarCodeFormat barCodeFormat){
        BarcodeFormat format;
        switch (barCodeFormat){
            case CODE39 -> format = BarcodeFormat.CODE_39;
            case EAN13 -> format = BarcodeFormat.EAN_13;
            case EAN8 -> format = BarcodeFormat.EAN_8;
            case UPC -> format = BarcodeFormat.UPC_A;
            default -> format = BarcodeFormat.CODE_128;
        }
        return format;
    }

    public static String zebraCompress(String zplContent) {
        String[] unitsArr = {"","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y"};
        String[] tensArr = new String[381];
        tensArr[0] = "";
        tensArr[20] = "g";
        tensArr[40] = "h";
        tensArr[60] = "i";
        tensArr[80] = "j";
        tensArr[100] = "k";
        tensArr[120] = "l";
        tensArr[140] = "m";
        tensArr[160] = "n";
        tensArr[180] = "o";
        tensArr[200] = "p";
        tensArr[220] = "q";
        tensArr[240] = "r";
        tensArr[260] = "s";
        tensArr[280] = "t";
        tensArr[300] = "u";
        tensArr[320] = "v";
        tensArr[340] = "w";
        tensArr[360] = "x";
        tensArr[380] = "y";
        //压缩结果
        StringBuilder result = new StringBuilder();
        //当前统计数量的相同值
        String value = "";
        String tempEx;
        String tempNow;
        //当前统计相同值的数量
        int key = 0;
        int keyTmp;
        int len = zplContent.length();
        for (int i=0; i<=len; i++){
            if (i==0){
                value = zplContent.substring(i, 1);
                key = 1;
                continue;
            }
            tempEx = zplContent.substring(i-1, i);
            keyTmp = i+1;
            if(keyTmp<=len) {
                tempNow = zplContent.substring(i, keyTmp);
            } else {
                tempNow = "";
            }
            if (tempEx.equals(tempNow)){
                key++;
            }else{
                if (key == 1){
                    result.append(value);
                }else if (key <= 19){
                    result.append(unitsArr[key]).append(value);
                }else if (key <= 380){
                    int tens = key/20 * 20;
                    int units = (key%20)==0 ? 0 : key%20;
                    result.append(tensArr[tens]).append(unitsArr[units]).append(value);
                }else{
                    StringBuilder hundreds = new StringBuilder();
                    int keyTemp = key;
                    do {
                        hundreds.append(tensArr[380]);
                        keyTemp -= 380;
                    }while(keyTemp > 380);

                    int tens = keyTemp/20 * 20;
                    int units = (keyTemp%20)==0 ? 0 : keyTemp%20;
                    result.append(hundreds).append(tensArr[tens]).append(unitsArr[units]).append(value);
                }
                value = tempNow;
                key = 1;
            }
        }
        return result.toString();
    }
}
