package com.linzi.daily.ocr.tess4j;

import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

public interface Tess4jOcrService {

    /**
     * 整图ocr
     * @param image 内存图片
     * @return ocr识别文字
     */
    String wholeOcr(BufferedImage image) throws TesseractException;

    /**
     * 部分ocr
     * @param image 内存图片
     * @param x1 区域x1坐标
     * @param y1 区域y1坐标
     * @param x2 区域x2坐标
     * @param y2 区域y2坐标
     * @return ocr识别文字
     */
    String partOcr(BufferedImage image,int x1,int y1,int x2,int y2) throws TesseractException;
}
