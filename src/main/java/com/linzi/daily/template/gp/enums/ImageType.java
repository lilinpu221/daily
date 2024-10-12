package com.linzi.daily.template.gp.enums;

public enum ImageType {
    /**
     * 图片类型
     */
    LOCAL,URL;

    public static ImageType explain(String type){
        return ImageType.valueOf(type.toUpperCase());
    }
}
