package com.linzi.daily.template.gp.enums;

public enum BarCodeFormat {
    /**
     *
     */
    ADAPT,CODE128,CODE39,EAN13,EAN8,UPCA;

    public static BarCodeFormat explain(String format){
        return BarCodeFormat.valueOf(format.toUpperCase());
    }

    public BarCodeFormat adaptBarType(String value){
        //根据条码字符长度自动适配固长类型
        int length = value.length();
        return switch (length) {
            case 8 -> BarCodeFormat.EAN8;
            case 12 -> BarCodeFormat.UPCA;
            case 13 -> BarCodeFormat.EAN13;
            default -> BarCodeFormat.CODE128;
        };
    }
}
