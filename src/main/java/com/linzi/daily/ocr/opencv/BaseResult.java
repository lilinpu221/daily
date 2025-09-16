package com.linzi.daily.ocr.opencv;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lil
 */
@Getter
@AllArgsConstructor
public class BaseResult {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String type;
    private final String value;

    @Override
    public String toString() {
        return String.format("类型: %s, 内容: %s, 位置: [x=%d, y=%d, w=%d, h=%d]",
                type, value, x, y, width, height);
    }
}
