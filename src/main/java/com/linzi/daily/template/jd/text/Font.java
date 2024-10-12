package com.linzi.daily.template.jd.text;

import cn.hutool.core.lang.Validator;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum Font {

    /**
     * 8X12
     */
    _8X12(12,"1",8,12),
    _12X20(20,"2",12,20),
    _16X16(16,"TSS16.BF2",16,16),
    _24X24(24,"TSS24.BF2",24,24);
    private final int fontSize;
    private final String fontType;
    private final int width;
    private final int height;

    Font(int fontSize,String fontType,int width,int height){
        this.fontSize=fontSize;
        this.fontType=fontType;
        this.width=width;
        this.height=height;
    }
    @Setter
    private int multipleW = 1;
    @Setter
    private int multipleH = 1;

    /**
     * 固定字号选择字体
     * @param fontSize fontSize
     * @param text text
     * @return Font
     */
    public static Font explain(int fontSize,String text){
        Font font;
        if(Validator.hasChinese(text)){
            //有中文
            if(fontSize <= 20){
                return Font._16X16;
            }else if(fontSize < 32){
                return Font._24X24;
            }else{
                //取余数靠近的
                int mul16 = fontSize % Font._16X16.getHeight();
                int mul24 = fontSize % Font._24X24.getHeight();
                font = mul16 < mul24 ? Font._16X16 : Font._24X24;
            }
        }else{
            if(fontSize <= 16){
                return Font._8X12;
            }else if(fontSize < 24){
                return Font._12X20;
            }else{
                //取余数靠近的
                int mul12 = fontSize % Font._8X12.getHeight();
                int mul20 = fontSize % Font._12X20.getHeight();
                font = mul12 < mul20 ? Font._8X12 : Font._12X20;
            }
        }
        //设置放大倍数
        font.setMultipleW(fontSize/font.getWidth());
        font.setMultipleH(fontSize/font.getHeight());
        return font;
    }
}
