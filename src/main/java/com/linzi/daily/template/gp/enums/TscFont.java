package com.linzi.daily.template.gp.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum TscFont {

    /*
    * Tsc基本字体
     */
    _8X12("1",4,8,12),
    _12X20("2",7,12,20),
    _16X16("TSS16.BF2",6,16,16),
    _24X24("TSS24.BF2",8,24,24);

    private final String name;
    private final int size;
    private final int width;
    private final int height;


    TscFont(String name, int size, int width, int height){
        this.name = name;
        this.size = size;
        this.width = width;
        this.height = height;
    }

    public static TscFont explain(FontFamily fontFamily,int size){
        TscFont result;
        if(fontFamily == FontFamily.ARIAL){
            result = size%4<size%7?_8X12:_12X20;
        }else{
            result = size%6<size%8?_16X16:_24X24;
        }
        return result;
    }
}
