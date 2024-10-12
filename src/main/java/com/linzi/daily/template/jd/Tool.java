package com.linzi.daily.template.jd;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import com.linzi.daily.template.jd.text.Align;
import com.linzi.daily.template.jd.text.Font;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    private static final Pattern P_CHINESE = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");

    public static boolean isChinese(char c){
        Matcher m = P_CHINESE.matcher(String.valueOf(c));
        return m.find();
    }

    /**
     * 计算文字换行后的集合
     * @param font 字体
     * @param width 元素宽度
     * @param textContent 元素内容值
     * @return List<String>
     */
    public static List<String> textWrap(Font font, int width, String textContent){
        List<String> textList = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        int factWidth = 0;
        //转为字符数组
        char[] textChars = textContent.toCharArray();
        for(int i=0;i<textChars.length;i++){
            char c = textChars[i];
            //英文/数字为字体大小/2，中文为字体实际大小宽度
            int fontFactWidth = font.getWidth()/2;
            if(Tool.isChinese(c)){
                fontFactWidth = font.getWidth();
            }
            if(factWidth+fontFactWidth>=width){
                //需要换行
                textList.add(line.toString());
                line.setLength(0);
                factWidth = 0;
            }
            line.append(c);
            factWidth += fontFactWidth;
            if(i==textChars.length-1){
                //最后一行
                textList.add(line.toString());
            }
        }
        return textList;
    }

    /**
     * 计算文本实际长度
     * @param font 字体
     * @param text 文本内容
     * @return int
     */
    public static int textWidth(Font font, String text){
        char[] textChars = text.toCharArray();
        int textWidth = 0;
        for (char c : textChars) {
            //英文/数字为字体大小/2，中文为字体实际大小宽度
            int tempWidth = font.getWidth() / 2;
            if (Tool.isChinese(c)) {
                tempWidth = font.getWidth();
            }
            textWidth = textWidth + tempWidth;
        }
        return textWidth;
    }

    /**
     * 计算文本垂直对齐y坐标便宜
     * @param font 字体
     * @param textLines 文本总行数
     * @param height 元素高
     * @param align 垂直对齐方式
     * @return int
     */
    public static int textVertical(Font font, int textLines, int height, Align align){
        //支持的最大行数(元素高度/字体高度)
        int lineCount = height<font.getHeight()?1:height/font.getHeight();
        //进行垂直对齐判断
        if(lineCount-textLines > 0){
            //只有元素高度行数大于文本总数量，才考虑垂直对齐
            int remainLine = lineCount-textLines;
            if(align== Align.CENTER){
                //居中，y坐标起始等于余数除以2再乘以字体高度
                return (Double.valueOf((remainLine/2.0)*font.getHeight()).intValue());
            }else if (align== Align.END){
                //居底，y坐标起始等于余数乘以字体高度
                return (remainLine * font.getHeight());
            }
        }
        return 0;
    }

    /**
     * 计算文本水平对齐x坐标
     * @param font 字体
     * @param width 元素宽度
     * @param textContent 文本内容
     * @param justifyContent 水平对齐方式
     * @param x 初始x坐标
     * @return int
     */
    public static int textHorizontal(Font font, int width, String textContent, String justifyContent, int x){
        int textWidth = textWidth(font,textContent);
        int remainWidth = width-textWidth;
        Align align = Align.explain(justifyContent);
        if(align== Align.CENTER){
            //居中，x坐标起始等于余数除以2
            return x+(remainWidth/2);
        }else if (align== Align.END){
            //居右，x坐标起始等于余数
            return x+remainWidth;
        }else{
            //居左
            return x;
        }
    }

    /**
     * 文本压缩自适应选择字体
     * @param text 文本内容
     * @param width 元素宽度
     * @param height 元素高度
     * @return Font
     */
    public static int adaptiveFonSize(String text,int width,int height){
        Font relt;
        Font[] fonts;
        if(Validator.hasChinese(text)){
            //中文字体集合
            relt = Font._16X16;
            fonts = new Font[]{Font._24X24, Font._16X16};
        }else{
            //英数字体集合
            relt = Font._8X12;
            fonts = new Font[]{Font._12X20, Font._8X12};
        }
        //逐个匹配
        for(Font font:fonts){
            //文字实际需要行数
            int textLineCount = Tool.textWrap(font,width,text).size();
            //元素实际能打的行数
            int itemLineCount = height/font.getHeight();
            if(itemLineCount >= textLineCount){
                //能匹配上就使用当前字体，否则默认使用最小字体
                relt = font;
                break;
            }
        }
        return relt.getFontSize();
    }

    public static String printBc(int charWidth, String text) {
        //条码都默认居中
        StringBuilder barCode = new StringBuilder("1B6101")
                //文字显示位置
                .append("1D4801")
                //固定50像素高度，2毫米宽度
                .append("1D68501D7702")
                //CODE128
                .append("1D6B49");

        //CODE128条码，前后各需要2个标识字符
        int maxNumLen = charWidth-4;
        if(NumberUtil.isNumber(text)) {
            //数字字符串使用CODEC
            if(text.length() > maxNumLen) {
                return "0A";
            }
            barCode.append(barcodeC(text));
        }else {
            if(text.length() > maxNumLen/2) {
                return "0A";
            }
            //字母+数字字符串使用CODEB
            barCode.append(barcodeB(text));
        }
        barCode.append("0A");
        return barCode.toString();
    }

    private static String barcodeB(String text){
        String hexText =HexUtil.encodeHexStr(text,
                Charset.forName("GB18030")).toUpperCase();
        String textLenStr = HexUtil.toHex(text.length()+2).toUpperCase();
        if(textLenStr.length()==1) {
            textLenStr = "0"+textLenStr;
        }
        return textLenStr+"7B42"+hexText;
    }

    private static String barcodeC(String text){
        String tempText;
        String tailText = "";
        if(text.length()%2==0){
            tempText = text;
        }else{
            tempText = text.substring(0, text.length()-1);
            tailText = text.substring(text.length()-1);
        }
        //两位数字转十六进制字符串
        int index = 0;
        StringBuilder codeC = new StringBuilder();
        while(index < tempText.length()) {
            String hexInt = HexUtil.toHex(Integer.parseInt(tempText.substring(index, Math.min(index + 2, tempText.length()))));
            if(hexInt.length()==1){
                hexInt = "0"+hexInt;
            }
            //两位转十六进制
            codeC.append(hexInt);
            index = index +2;
        }
        if(CharSequenceUtil.isNotBlank(tailText)){
            codeC.append("7B42"+HexUtil.encodeHexStr(tailText, Charset.forName("GB18030")));
        }
        String textLenStr = HexUtil.toHex(codeC.toString().length()/2+2);
        if(textLenStr.length()==1){
            textLenStr = "0"+textLenStr;
        }
        return textLenStr+"7B43"+codeC;
    }

    public static void main(String[] args) {
        System.out.println("1B40"+printBc(32, "8093340161928976878"));
    }
}
