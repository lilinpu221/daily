package com.linzi.daily.template.gp;

import cn.hutool.core.text.CharSequenceUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
public class EscFunc {

    private static final Pattern pTb = Pattern.compile("3C67705461626C65([0-9A-F]*?)3E([0-9A-F]*?)3C2F67705461626C653E");
    private static final Pattern pTr = Pattern.compile("3C74723E([0-9A-F]*?)3C2F74723E");
    private static final Pattern pTd = Pattern.compile("3C74643E([0-9A-F]*?)3C2F74643E");

    public static String getTextTableColumnStr(String source) {
        //<gpTable Type=1 Width=6,6,4,6,10><tr><td>内容11</td><td>内容12</td><td>内容13</td><td>内容14</td><td>内容15</td></tr></gpTable>
        StringBuilder result = new StringBuilder();
        Matcher mTb = pTb.matcher(source);
        while(mTb.find()) {
            try {
                int[][] showLen58 = {{32},{22,10},{18,6,8},{14,6,6,6},{11,5,5,6,5},{9,5,5,4,5,4}};
                int[][] showLen80 = {{48},{32,16},{28,8,12},{24,8,8,8},{21,7,7,8,5},{17,7,7,7,7,5}};
                result.setLength(0);
                boolean defaultWidth = false;
                String attr  = hexStr2Str(mTb.group(1),"GB18030");
                String[] arrAttr = StringUtils.splitByWholeSeparator(attr, "");
                String printType = "0";
                String printWidth = ",";
                for (String s : arrAttr) {
                    if (CharSequenceUtil.EMPTY.equals(s) || !s.contains("=")) {
                        continue;
                    }
                    String[] arrAttrInfo = StringUtils.split(s, "=");
                    if (arrAttrInfo.length < 2) {
                        continue;
                    }
                    switch (arrAttrInfo[0]) {
                        case "Type":
                            if ("1".equals(arrAttrInfo[1])) {
                                printType = "1";
                            } else {
                                printType = "0";
                            }
                            break;
                        case "Width":
                            printWidth = arrAttrInfo[1];
                            break;
                        default:
                            break;
                    }
                }
                String printDatas = mTb.group(2);
                String[] colStrWidth = StringUtils.split(printWidth, ",");
                int[] colWidth = new int[colStrWidth.length];
                int colWidthSum = 0;
                for(int i=0; i<colStrWidth.length; i++){
                    colWidth[i] = Integer.parseInt(colStrWidth[i]);
                    if(colWidth[i]<2) {
                        defaultWidth = true;
                    }
                    colWidthSum += Integer.parseInt(colStrWidth[i]);
                }
                if(("0".equals(printType) && colWidthSum != 32) || ("1".equals(printType) && colWidthSum != 48)) {
                    defaultWidth = true;
                }
                //<tr>(.*?)</tr>
                Matcher mTr = pTr.matcher(printDatas);
                while (mTr.find()) {
                    String trDatas = mTr.group(1);
                    //<td>(.*?)</td>
                    Matcher mTd = pTd.matcher(trDatas);
                    List<String> tdList = new ArrayList<String>();
                    while(mTd.find()) {
                        tdList.add(mTd.group(1).toString());
                    }
                    int tdSize = tdList.size() > 6 ? 6 : tdList.size();
                    if(tdSize<1) {
                        continue;
                    }
                    String[] content = new String[tdSize];
                    for(int i=0; i<tdSize; i++) {
                        content[i] = tdList.get(i);
                    }

                    if(tdList.size()<6 && tdList.size()>1 && tdList.size() == colWidth.length && !defaultWidth) {
                        result.append(setFormat_Table_Td(content, colWidth));
                    } else {
                        switch (printType) {
                            case "0":
                                result.append(setFormat_Table_Td(content, showLen58[tdSize-1]));
                                break;
                            case "1":
                                result.append(setFormat_Table_Td(content, showLen80[tdSize-1]));
                                break;
                            default:
                                break;
                        }
                    }
                }
                source = source.replace(mTb.group(), result.toString());

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return source;
    }

    public static String setFormat_Table_Td(String[] content, int[] colLenWidth) throws Exception {

        int colLen = content.length;
        int colLast = colLen - 1;
        int[] colWidth = new int[colLen];
        for(int i=0; i<colLen; i++) {
            if(i==colLast) {
                colWidth[i] = colLenWidth[i];
            } else {
                colWidth[i] = colLenWidth[i] - 1;
            }
        }
        for(int i=0; i<colLen; i++) {
            content[i] = hexStr2Str(content[i], "GB18030");
        }
        int maxRow = 1;
        ArrayList<ArrayList<String>> arrTbData = new ArrayList<>(colLen);
        for(int j=0; j<colLen; j++) {
            String rowColData;
            int spaceLen = colWidth[j];
            ArrayList<String> arrTdData = new ArrayList<>();
            int contentRow = 0;
            while(!content[j].equals("")) {
                StringBuffer rowColSpace = new StringBuffer();
                rowColData = gSubstring(content[j],colWidth[j]);
                int rowColDataLen = rowColData.getBytes("GB18030").length;
                spaceLen = colLenWidth[j] - rowColDataLen;
                content[j] = content[j].toString().substring(rowColData.toString().length(),content[j].toString().length());
                for(int m=0; m<spaceLen; m++) {
                    rowColSpace.append(" ");
                }
                if(j==colLast && colLast>0) {
                    arrTdData.add(rowColSpace.toString().concat(rowColData));
                } else {
                    arrTdData.add(rowColData.concat(rowColSpace.toString()));
                }
                contentRow++;
            }
            arrTbData.add(arrTdData);
            maxRow = maxRow < contentRow ? contentRow : maxRow;
        }
        StringBuffer tbData = new StringBuffer();
        for(int i=0; i<maxRow; i++) {
            for(int j=0; j<colLen; j++) {
                if(arrTbData.get(j).size() > i) {
                    tbData.append(str2HexStr(arrTbData.get(j).get(i), "GB18030"));
                } else {
                    for(int m=0; m < colLenWidth[j]; m++) {
                        tbData.append("20");
                    }
                }
            }
            //tbData.append("0A");
        }
        return tbData.toString().concat("0A");
    }

    public static String gSubstring(String s, int length) {

        try {
            byte[] bytes;
            bytes = s.getBytes("Unicode");
            // 表示当前的字节数
            int n = 0;
            // 要截取的字节数，从第3个字节开始
            int i = 2;
            for (; i < bytes.length && n < length; i++) {
                // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
                if (i % 2 == 1) {
                    n++; // 在UCS2第二个字节时n加1
                } else {
                    // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }
            // 如果i为奇数时，处理成偶数
            if (i % 2 == 1) {
                // 该UCS2字符是汉字时，去掉这个截一半的汉字
                if (bytes[i - 1] != 0) {
                    i = i - 1;
                }
                // 该UCS2字符是字母或数字，则保留该字符
                else {
                    i = i + 1;
                }
            }
            return new String(bytes, 0, i, "Unicode");

        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    public static String hexStr2Str(String hexStr, String charset) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    public static String str2HexString(String s) {
        String str = "";
        try {
            byte[] b = s.getBytes("GB18030");
            for (int i = 0; i < b.length; i++) {
                Integer I = new Integer(b[i]);
                String strTmp = I.toHexString(b[i]);
                if (strTmp.length() > 2) {
                    strTmp = strTmp.substring(strTmp.length() - 2);
                }
                str = str + strTmp;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return str.toUpperCase();
    }
}
 **/
