package com.linzi.daily.ocr.tess4j;

import jakarta.annotation.Resource;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class Tess4jOcrServiceImpl implements Tess4jOcrService{

    @Resource
    ITesseract iTesseract;

    @Override
    public String wholeOcr(BufferedImage image) throws TesseractException {
        String result = iTesseract.doOCR(image);
        System.out.println("======"+result);
        return result;
    }

    @Override
    public String partOcr(BufferedImage image, int x1, int y1, int x2, int y2) throws TesseractException {
        String result = iTesseract.doOCR(image,new Rectangle(x1,y1,x2-x1,y2-y1));
        System.out.println("======"+result);
        return result;
    }
}
