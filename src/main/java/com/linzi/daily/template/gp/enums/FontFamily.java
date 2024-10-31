package com.linzi.daily.template.gp.enums;

import lombok.Getter;

@Getter
public enum FontFamily {
    /**
     * 字体列表
     */
    SIMSUN("宋体", new int[]{6, 8, 12, 16, 18, 24,32,48},false),
    SIMHEI("黑体",new int[]{6, 8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    SIYUANSUN("思源宋体",new int[]{6,8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    SIYUANHEI("思源黑体",new int[]{6,8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    YAHEI("微软雅黑",new int[]{6,8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    REGULAR("楷书",new int[]{6,8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    CLERICAL("隶书",new int[]{6,8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    YOUYUAN("幼圆",new int[]{6,8, 10, 12, 14, 16, 18, 20, 22,24,26,28,36,48},true),
    ARIAL("Arial",new int[]{4, 7, 8, 14, 16, 28, 32, 56},false);

    private final String name;
    private final int[] sizes;
    private final Boolean needImg;

    FontFamily(String name,int [] sizes,Boolean needImg) {
        this.name = name;
        this.sizes = sizes;
        this.needImg = needImg;
    }

    public static FontFamily explain(String fontFamily){
        return FontFamily.valueOf(fontFamily.toUpperCase());
    }
}
