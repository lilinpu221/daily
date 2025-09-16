package com.linzi.daily.tcpapi;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lil
 */
public class GpEscProcessor {

    private static final Pattern SPACE_PATTERN = Pattern.compile(" {4,}");
    private static final Pattern GPWORD_PATTERN = Pattern.compile("<gpWord[^>]*>(.*?)</gpWord>");
    // 使用 lookahead 正则，支持重叠匹配
    private static final Pattern GPBR_PATTERN = Pattern.compile("<gpBr/>(?=(.*?)(<gpBr/>))");

    // 判断字符是否是中文或中文标点
    public static boolean isChinese(char c) {
        return (c >= 0x4E00 && c <= 0x9FFF) ||     // 汉字
                (c >= 0x3400 && c <= 0x4DBF) ||     // 扩展A
                (c >= 0xF900 && c <= 0xFAFF) ||     // 兼容汉字
                (c >= 0x3000 && c <= 0x303F) ||     // 标点符号
                (c >= 0xFF00 && c <= 0xFFEF);       // 全角符号
    }

    // 计算字符串显示宽度（中文=2，英文数字=1）
    public static int calculateDisplayLength(String str) {
        int length = 0;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }

    // 处理包含连续4个空格的字符串
    public static String processContentPart(String content) {
        Matcher matcher = SPACE_PATTERN.matcher(content);
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            String left = content.substring(0, start);
            String right = content.substring(end);

            int totalLength = calculateDisplayLength(left + right);
            int spaceCount = Math.max(0, 48 - totalLength);

            return left + " ".repeat(spaceCount) + right;
        }
        return content;
    }

    // 主处理方法：先处理 <gpWord> 内容，再处理 <gpBr/> 包裹的内容
    public static String process58To80(String input) {
        input = input.replaceAll("-{5,}","------------------------------------------------");
        input = input.replace("gpTR2 Type=0","gpTR2 Type=1");
        input = input.replace("gpTR3 Type=0","gpTR3 Type=1");
        input = input.replace("gpTR4 Type=0","gpTR4 Type=1");
        input = input.replace("</gpWord><gpBr/>","</gpWord>");
        input = input.replace("Wsize=0 Hsize=2","Wsize=0 Hsize=1");
        Map<String,String> replaceMap = new HashMap<>();
        // 1. 处理 <gpWord> 中的内容
        Matcher wordMatcher = GPWORD_PATTERN.matcher(input);
        while (wordMatcher.find()) {
            String original = wordMatcher.group(1);
            if (original != null && original.contains("    ") && !original.contains("<gp")) {
                String processed = processContentPart(original);
                replaceMap.put(original, processed);
            }
        }
        // 2. 处理 <gpBr/> 包裹的内容
        Matcher brMatcher = GPBR_PATTERN.matcher(input);
        while (brMatcher.find()) {
            String original = brMatcher.group(1);
            if (original != null && original.contains("    ") && !original.contains("<gp")) {
                String processed = processContentPart(original);
                replaceMap.put(original, processed);
            }
        }
        for(Map.Entry<String, String> entry:replaceMap.entrySet()){
            input = input.replace(entry.getKey(),entry.getValue());
        }
        return input;
    }
}

