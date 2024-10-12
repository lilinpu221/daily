package com.linzi.daily.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    private static final Pattern P_CHINESE = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
    public static boolean isChinese(char c){
        Matcher m = P_CHINESE.matcher(String.valueOf(c));
        return m.find();
    }
    public static List<String> textWrap(String text, int regionWidth, int fontSize, int letterSpacing){
        List<String> textList = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        int factWidth = 0;
        char[] textChars = text.toCharArray();
        for(int i=0;i<textChars.length;i++){
            char c = textChars[i];
            int fontFactWidth = fontSize/2+letterSpacing;
            if(isChinese(c)){
                fontFactWidth = fontSize+letterSpacing;
            }
            if(factWidth+fontFactWidth>=regionWidth){
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

    public static String imgToBitmapHex(BufferedImage bi, int whiteFill, int blackFill){
        int width = bi.getWidth();
        int height = bi.getHeight();
        int wModByte = (width%8)==0 ? 0 : 8-(width%8);
        int hModByte = (height%8)==0 ? 0 : 8-(height%8);
        //图片RGB属性
        int[] rgb = new int[3];
        StringBuilder res = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 下面三行代码将一个数字转换为RGB数字
                int pixel = bi.getRGB(x,y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                int bitStr = (rgb[0] + rgb[1] + rgb[2])/3 >= 200 ? whiteFill : blackFill;
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
}
