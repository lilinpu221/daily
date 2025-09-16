package com.linzi.daily.ocr.opencv;

import lombok.Getter;

/**
 * @author Lil
 */
@Getter
public class QrcodeResult extends BaseResult{

    private final String level = "L";
    public QrcodeResult(int x, int y,  int width, int height,String type, String value) {
        super(x, y, width, height,type,value);
    }
}
