package com.linzi.daily.ocr.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TesseractTest {

    public static void main(String[] args) throws TesseractException, IOException {
        //获取本地图片
        File file = new File("F:\\tesseract-ocr-training\\express\\3.png");
        //创建Tesseract对象
        ITesseract tesseract = new Tesseract();
        //设置字体库路径 （你的chi_sim.traineddata放的个文件夹
        tesseract.setDatapath("F:\\tesseract-ocr-training\\tessdata");
        //中文识别 chi_sim.traineddata的前缀
        tesseract.setLanguage("chi_sim");
        BufferedImage bufferedImage = ImageIO.read(file);
        Rectangle rect = new Rectangle(2581,510,249,196);
        //执行ocr识别
        String result = tesseract.doOCR(file);
        System.out.println("=====识别开始=====");
        System.out.println(result);
        System.out.println("=====识别结束=====");
        //替换回车和tal键  使结果为一行
        //result = result.replaceAll("\\r|\\n","-").replaceAll(" ","");
        //System.out.println("识别的结果为："+result);
    }
}
