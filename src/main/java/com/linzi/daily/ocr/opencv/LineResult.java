package com.linzi.daily.ocr.opencv;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Getter;

/**
 * @author Lil
 */
@Getter
public class LineResult extends BaseResult{
    // 线条方向(0-水平线 1-垂直线 )
    private final int direction;
    // 线条粗细
    private final double thickness;

    public LineResult(int x, int y, int width, int height, String type, int direction, double thickness) {
        super(x, y, width, height, type, CharSequenceUtil.EMPTY);
        this.direction = direction;
        this.thickness = thickness;
    }
}
