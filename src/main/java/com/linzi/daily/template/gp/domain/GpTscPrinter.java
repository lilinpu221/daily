package com.linzi.daily.template.gp.domain;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 佳博TSC指令打印实现类
 */
public class GpTscPrinter {
    private static final Pattern P_CHINESE = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
    private static final String FONT_FAMILY = "宋体";

    private static final int RGB_THRESHOLD = 128;

    /**
     * 打印的页面的宽度和高度
     *
     * @param PageWidth  宽度(dot)
     * @param PageHeight 高度(dot)
     * @return
     */
    public String pageSetup(int PageWidth, int PageHeight) {
        int pageWidthMM = PageWidth/8;
        int PageHeightMM = PageHeight/8;
        StringBuilder tscBuilder = new StringBuilder();
        tscBuilder.append("SIZE " + pageWidthMM + " mm," + PageHeightMM + " mm\r\n")
                .append("REFERENCE 0,0\r\n")
                .append("SET HEAD ON\r\n")
                .append("SET PRINTKEY OFF\r\n")
                .append("SET KEY1 ON\r\n")
                .append("SET KEY2 ON\r\n")
                .append("SET SHIFT 0\r\n")
                .append("CLS\r\n");
        return tscBuilder.toString();
    }

    /**
     * 打印字符串
     *
     * @param x        文字x方向起始点坐标
     * @param y        文字y方向起始点坐标
     * @param text     字符串
     * @param fontSize 字体大小(0-21)
     * @param rotate   文字旋转角度
     * @param bold     是否加粗
     * @param reverse  是否反色
     * @return
     */
    public String text(int x, int y, String text, int fontSize, int rotate, boolean bold, boolean reverse) {
        Font font = Font.getFont(fontSize);
        StringBuilder textBuilder = new StringBuilder();
        String fontOrder = font.getOrder().replace("0,", (rotate + ","));
        textBuilder.append("TEXT " + x + "," + y + "," + fontOrder + ",\"" + text + "\"\r\n");
        if (bold) {
            //加粗处理(通过偏移1dot重打实现)
            textBuilder.append("TEXT " + (x - 1 < 0 ? 0 : x - 1) + "," + (y - 1 < 0 ? 0 : y - 1) + "," + fontOrder + ",\"" + text + "\"\r\n");
            textBuilder.append("TEXT " + (x - 1 < 0 ? 0 : x - 1) + "," + (y + 1) + "," + fontOrder + ",\"" + text + "\"\r\n");
            textBuilder.append("TEXT " + (x + 1) + "," + (y - 1 < 0 ? 0 : y - 1) + "," + fontOrder + ",\"" + text + "\"\r\n");
            textBuilder.append("TEXT " + (x + 1) + "," + (y + 1) + "," + fontOrder + ",\"" + text + "\"\r\n");
        }
        if (reverse) {
            int fontFactWidth = 0;
            char[] textChars = text.toCharArray();
            for (int i = 0; i < textChars.length; i++) {
                char c = textChars[i];
                int tempWidth = font.getWidth() / 2;
                if (isChinese(c)) {
                    //英文字符占半个字体宽度，中文占一个字体宽度
                    tempWidth = font.getWidth();
                }
                fontFactWidth += tempWidth;
            }
            //反色处理
            textBuilder.append("REVERSE " + x + "," + y + "," + (x + fontFactWidth) + "," + font.getHeight() + "\r\n");
        }
        return textBuilder.toString();
    }

    /**
     * 打印条码
     *
     * @param start_x    x方向起始点坐标
     * @param start_y    y方向起始点坐标
     * @param barType    条码类型(0-表示code128 1-表示code39)
     * @param text       条码内容
     * @param unit_width 条码宽度(dot)
     * @param height     条码高度
     * @return
     */
    
    public String barcode1D(int start_x, int start_y, int barType, String text, int unit_width, int height) {
        return "TEXT " + (start_x + 36) + "," + (start_y + height + 5) + "," + Font.getFont(1).getOrder() + ",\"" + text + "\"\r\n"
                +"BARCODE " + start_x + "," + start_y + "," + "\"" + TscBarType.getBarTypeName(barType) + "\","
                + height + ",1,0,2," + unit_width + "," + "\"" + text + "\"\r\n";
    }

    /**
     * 打印二维码
     *
     * @param x        x方向起始点坐标
     * @param y        y方向起始点坐标
     * @param text     二维码内容
     * @param errLevel 纠错等级
     * @param unit     二维码宽度
     * @return
     */
    
    public String QrCode(int x, int y, String text, int errLevel, int unit) {
        return "QRCODE " + x + "," + y + "," + getQrLevlName(errLevel) + "," + unit + ",A," + "0," + "\"" + text + "\"\r\n";
    }

    /**
     * 打印线条
     *
     * @param start_x   x方向起始点坐标
     * @param start_y   y方向起始点坐标
     * @param end_x     x方向结束点坐标
     * @param end_y     y方向结束点坐标
     * @param lineWidth 线宽
     * @return
     */
    
    public String line(int start_x, int start_y, int end_x, int end_y, int lineWidth) {
        int lineWidthx = end_x - start_x;
        int lineHeighty = end_y - start_y;
        if (lineWidthx >= lineHeighty) {
            //代表是横线
            lineHeighty = lineWidth;
        } else {
            //代表竖线
            lineWidthx = lineWidth;
        }
        return "BAR " + start_x + "," + start_y + "," + lineWidthx + "," + lineHeighty + "\r\n";
    }

    /**
     * 打印方框
     *
     * @param left      方框左上角x坐标
     * @param left2     方框左上角y坐标
     * @param right     方框右下角x坐标
     * @param bottom    方框右下角y坐标
     * @param lineWidth 方框线宽
     * @return
     */
    
    public String box(int left, int left2, int right, int bottom, int lineWidth) {
        return "BOX " + left + "," + left2 + "," + right + "," + bottom + "," + lineWidth + "\r\n";
    }

    public static void main(String[] args) {
        GpTscPrinter tscPrinter = new GpTscPrinter();
        System.out.println(tscPrinter.image(16,16, "D:\\test.png"));
    }

    /**
     * 打印服务器文件
     *
     * @param x
     * @param y
     * @param imgName
     * @return
     */
    
    public String image(int x, int y, String imgName) {
        File imgFile = new File(imgName);
        if (!imgFile.exists()) {
            //文件不存在
            return "";
        }
        try {
            BufferedImage bi = ImageIO.read(imgFile);
            int width = bi.getWidth();
            int height = bi.getHeight();
            int wModByte = (width%8)==0 ? 0 : 8-(width%8);
            int hModByte = (height%8)==0 ? 0 : 8-(height%8);
            //向上取整
            int wPrintByte = (int)Math.ceil((double)(width+wModByte)/8);
            int hPrintByte = height+hModByte;
            String imgHex = imgToBitmapHex(bi);
            return "BITMAP "+x+","+y+","+wPrintByte+","+hPrintByte+",0,"+imgHex+"\r\n";
        } catch (IOException e) {
            //文件读取失败
            return "";
        }
    }

    /**
     * 打印本地图片
     *
     * @param x       x方向起始点坐标
     * @param y       y方向起始点坐标
     * @param imgName 文件名称
     * @return
     */
    
    public String localImage(int x, int y, String imgName) {
        return "PUTBMP " + x + "," + y + ",\"" + imgName + "\"\r\n";
    }

    /**
     * 多行文字打印
     *
     * @param x         x方向起始点坐标
     * @param y         y方向起始点坐标
     * @param width     打印宽度(dot)
     * @param text      文字内容
     * @param fontSize  文字字号（会转成支持的字号）
     * @param bold      是否加粗
     * @param reverse   是否反色
     * @param lineSpace 行间距(dot)
     * @return
     */
    
    public String textMulti(int x, int y, int width, String text, int fontSize, boolean bold, boolean reverse, int lineSpace) {
        //字号
        Font font = Font.getFont(fontSize);
        //文字换行处理
        List<String> lineList = textWrap(text, width, font.getWidth(), 0);
        StringBuilder textBuilder = new StringBuilder();
        for (String line : lineList) {
            textBuilder.append("TEXT " + x + "," + y + "," + font.getOrder() + ",\"" + line + "\"\r\n");
            if (bold) {
                //加粗处理(通过偏移1dot重打实现)
                textBuilder.append("TEXT " + (x - 1) + "," + (y - 1) + "," + font.getOrder() + ",\"" + line + "\"\r\n");
                textBuilder.append("TEXT " + (x - 1) + "," + (y + 1) + "," + font.getOrder() + ",\"" + line + "\"\r\n");
                textBuilder.append("TEXT " + (x + 1) + "," + (y - 1) + "," + font.getOrder() + ",\"" + line + "\"\r\n");
                textBuilder.append("TEXT " + (x + 1) + "," + (y + 1) + "," + font.getOrder() + ",\"" + line + "\"\r\n");
            }
            //往下移动
            y += font.getHeight() + lineSpace;
        }
        if (reverse) {
            //反色高
            int height = y + (font.getHeight() + lineSpace) * lineList.size();
            //反色处理
            textBuilder.append("REVERSE " + x + "," + y + "," + width + "," + height);
        }
        return textBuilder.toString();
    }

    /**
     * 打印水印
     *
     * @param x         x方向起始点坐标
     * @param y         y方向起始点坐标
     * @param width     文本框宽度（单位：dot），当文字内容超过此宽度，将换行
     * @param text      水印内容
     * @param blackness 水印深度    分为级别0～10，0为最浅，10为最深
     * @param fontsize  字体
     * @param bold      加粗
     * @param lineSpac  行间距（单位：dot）
     * @return
     */
    
    public String prn_Canvas_DrawText(int x, int y, int width, String text, int blackness, int fontsize, boolean bold, int lineSpac) {
        try {
            BufferedImage bi = textToImg(width, text, fontsize, bold, lineSpac);
            int iWidth = bi.getWidth();
            int iHeight = bi.getHeight();
            int wModByte = (iWidth % 8) == 0 ? 0 : 8 - (iWidth % 8);
            int hModByte = (iHeight % 8) == 0 ? 0 : 8 - (iHeight % 8);
            int wPrintByte = (iWidth + wModByte) / 8;
            int hPrintByte = iHeight + hModByte;
            String imgHex = imgToBitmapHex(bi);
            //char[] imgChars = hexToCharArray(imgHex);
            return "BITMAP " + x + "," + y + "," + wPrintByte + "," + hPrintByte + ",0," + imgHex + "\r\n";
        }catch(Exception e){
            return "";
        }
    }

    /**
     * 打印条码2
     *
     * @param start_x    x方向起始点坐标
     * @param start_y    y方向起始点坐标
     * @param barType    条码类型
     * @param text       一维码内容
     * @param rotate     条码旋转（0或2表示水平打印，1或3表示垂直打印）
     * @param unit_width 单位宽度(单位：dot)
     * @param height     条码高度
     * @return
     */
    
    public String barcode1D2(int start_x, int start_y, int barType, String text, int rotate, int unit_width, int height) {
        return "BARCODE " + start_x + "," + start_y + "," + "\"" + TscBarType.getBarTypeName(barType) + "\","
                + height + ",1,"+getRotateAngle(rotate)+",2," + unit_width + "," + "\"" + text + "\"\r\n";
    }

    
    public String print() {
        return "PRINT 1,1\r\n";
    }

    public static char[] hexToCharArray(String hexData) {
        int len = hexData.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string");
        }
        char[] charArray = new char[len / 2];
        for (int i = 0, j = 0; i < len; i += 2, j++) {
            String hexByte = hexData.substring(i, i + 2);
            charArray[j] = (char) Integer.parseInt(hexByte, 16);
        }
        return charArray;
    }

    public static String charArrayToHex(char[] charArray) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : charArray) {
            stringBuilder.append(String.format("%02x", (int) c & 0xFF));
        }
        return stringBuilder.toString().toUpperCase();
    }

    public boolean isChinese(char c) {
        Matcher m = P_CHINESE.matcher(String.valueOf(c));
        return m.find();
    }

    /**
     * 文字换行处理
     *
     * @param text          文字
     * @param regionWidth   可打印宽度(dot)
     * @param fontWidth     文字宽度(dot)
     * @param letterSpacing 字间距(dot)
     * @return
     */
    private List<String> textWrap(String text, int regionWidth, int fontWidth, int letterSpacing) {
        List<String> textList = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        int factWidth = 0;
        char[] textChars = text.toCharArray();
        for (int i = 0; i < textChars.length; i++) {
            char c = textChars[i];
            int fontFactWidth = fontWidth / 2 + letterSpacing;
            if (isChinese(c)) {
                //英文字符占半个字体宽度，中文占一个字体宽度
                fontFactWidth = fontWidth + letterSpacing;
            }
            if (factWidth + fontFactWidth >= regionWidth) {
                //需要换行
                textList.add(line.toString());
                line.setLength(0);
                factWidth = 0;
            }
            line.append(c);
            factWidth += fontFactWidth;
            if (i == textChars.length - 1) {
                //最后一行
                textList.add(line.toString());
            }
        }
        return textList;
    }

    private int getRotateAngle(int rotate) {
        return switch (rotate) {
            case 0 -> 0;
            case 1 -> 90;
            case 2 -> 180;
            case 3 -> 270;
            default -> 0;
        };
    }

    private String getQrLevlName(int errLevel) {
        return switch (errLevel) {
            case 0 -> "L";
            case 1 -> "M";
            case 2 -> "Q";
            case 3 -> "H";
            default -> "L";
        };
    }

    private String imgToBitmapHex(BufferedImage bi){
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int hModByte = (height%8)==0 ? 0 : 8-(height%8);
        //图片RGB属性
        StringBuilder res = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 下面三行代码将一个数字转换为RGB数字
                final int color = bi.getRGB(x, y);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                //使用加权灰度算法
                int bitStr = (0.3 * r + 0.59 * g + 0.11 * b) >= RGB_THRESHOLD ? 1 : 0;
                /**
                 * 使用平均值灰度算法
                 * int bitStr = (rgb[0] + rgb[1] + rgb[2])/3 >= RGB_THRESHOLD ? 1 : 0;
                 */
                res.append(bitStr);
            }
            for (int x = 0; x < wModByte; x++){
                res.append(1);
            }
        }
        for (int y = 0; y < hModByte; y++){
            for (int x = 0; x < width+wModByte; x++){
                res.append(1);
            }
        }
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

    private BufferedImage textToImg(int width, String text, int fontsize, boolean bold, int lineSpace) {
        //文字换行处理
        List<String> lineList = textWrap(text, width, fontsize, lineSpace);
        //得到打印高度
        int height = lineList.size() * fontsize + lineSpace;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = bi.createGraphics();
        // 画图
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);
        g2d.clearRect(0, 0, width, height);
        //设置字体风格
        int fontStyle = java.awt.Font.PLAIN;
        if (bold) {
            //加粗
            fontStyle = fontStyle + java.awt.Font.BOLD;
        }
        java.awt.Font font = new java.awt.Font(FONT_FAMILY, fontStyle, fontsize);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        //画布支持的最大行数
        int lineCount = height < fontsize ? 1 : height / fontsize;
        for (int i = 0; i < lineCount; i++) {
            if (i >= lineList.size()) {
                //超过文本总数
                break;
            }
            String lineValue = lineList.get(i);
            char[] values = lineValue.toCharArray();
            //字体打印的起始点(默认居左)
            int x = 0;
            int y = fontsize * (i + 1);
            for (char c : values) {
                String value = String.valueOf(c);
                AttributedString as = new AttributedString(value);
                as.addAttribute(TextAttribute.FONT, font);
                g2d.drawString(as.getIterator(), x, y);
                //计算下一个字的x坐标
                int factCharWidth = metrics.stringWidth(value) + lineSpace;
                x += factCharWidth;
            }
        }
        g2d.dispose();
        return bi;
    }

    @Getter
    enum Font {
        /**/
        _16x16(0, 16, 16, "\"" + "TSS16.BF2\",0,1,1"),
        _24x24(1, 24, 24, "\"" + "TSS24.BF2\",0,1,1"),
        _32x32(2, 32, 32, "\"" + "TSS16.BF2\",0,2,2"),
        _48x48(3, 48, 48, "\"" + "TSS24.BF2\",0,2,2"),
        _64x64(4, 64, 64, "\"" + "TSS16.BF2\",0,3,3"),
        _72x72(5, 72, 72, "\"" + "TSS24.BF2\",0,3,3"),
        _96x96(6, 96, 96, "\"" + "TSS24.BF2\",0,4,4"),
        _120x120(63, 120, 120, "\"" + "TSS24.BF2\",0,5,5"),
        _144x144(66, 144, 144, "\"" + "TSS24.BF2\",0,6,6"),
        _168x168(69, 168, 168, "\"" + "TSS24.BF2\",0,7,7"),
        _32x16(7, 32, 16, "\"" + "TSS16.BF2\",0,2,1"),
        _48x24(8, 48, 24, "\"" + "TSS24.BF2\",0,2,1"),
        _48x32(9, 48, 32, "\"" + "TSS16.BF2\",0,3,2"),
        _64x32(10, 64, 32, "\"" + "TSS16.BF2\",0,4,2"),
        _72x48(11, 72, 48, "\"" + "TSS24.BF2\",0,3,2"),
        _96x48(12, 96, 48, "\"" + "TSS24.BF2\",0,4,2"),
        _96x72(13, 96, 72, "\"" + "TSS24.BF2\",0,4,3"),
        _16x32(14, 16, 32, "\"" + "TSS16.BF2\",0,1,2"),
        _24x48(15, 24, 48, "\"" + "TSS24.BF2\",0,1,2"),
        _32x48(16, 32, 48, "\"" + "TSS16.BF2\",0,2,3"),
        _32x64(17, 32, 64, "\"" + "TSS16.BF2\",0,2,4"),
        _48x72(18, 48, 72, "\"" + "TSS24.BF2\",0,2,3"),
        _48x96(19, 48, 96, "\"" + "TSS24.BF2\",0,2,4"),
        _72x96(20, 72, 96, "\"" + "TSS24.BF2\",0,3,4");

        private final int fontSize;
        private final int width;
        private final int height;
        private String order;

        Font(int fontSize, int width, int height, String order) {
            this.fontSize = fontSize;
            this.width = width;
            this.height = height;
            this.order = order;
        }

        public int getFontSize() {
            return this.fontSize;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public String getOrder() {
            return this.order;
        }

        public static Font getFont(int fontSize) {
            return Arrays.stream(Font.values())
                    .filter(font -> fontSize == font.getFontSize())
                    .findFirst()
                    .orElse(Font._24x24);
        }
    }

    @Getter
    enum TscBarType {
        /**/
        CODE128(0), CODE39(1);
        //CODE93(2),
        //EAN8(3), EAN13(4), EAN14(5), EAN128(6),
        //CODA(7), UPCA(8), UPCE(9);

        private final int type;

        TscBarType(int type) {
            this.type = type;
        }

        public static String getBarTypeName(int type) {
            String typeName = Arrays.stream(TscBarType.values())
                    .filter(barType -> barType.getType() == type)
                    .findFirst()
                    .orElse(TscBarType.CODE128).name();
            return typeName.startsWith("CODE") ? typeName.substring(4) : typeName;
        }
    }
}
