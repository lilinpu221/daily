package com.linzi.daily.ocr.tess4j;

import jakarta.annotation.Resource;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    public String partOcr(BufferedImage image, int x1, int y1, int x2, int y2) throws TesseractException, IOException {
        File file = new File("F:\\tesseract-ocr-training\\tesseract.png");
        ImageIO.write(image,"png",file);
        ArrayList<Rectangle> list = new ArrayList<>();
        list.add(new Rectangle(x1,y1,x2-x1,y2-y1));
        String result = iTesseract.doOCR(file,list);
        System.out.println("======"+result);
        return result;
    }
}
