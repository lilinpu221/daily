package com.linzi.daily.ocr.opencv;

import lombok.Getter;

/**
 * @author Lil
 */
@Getter
public class BarcodeResult extends BaseResult{
    private final String format;
    private final int showText = 0;

    public BarcodeResult(int x, int y, int width, int height, String type, String value,String format) {
        super(x, y, width, height, type, value);
        this.format = format;
    }
}
