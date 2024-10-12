package com.linzi.daily.ocr.paddle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class PaddleOcrServiceImpl implements PaddleOcrService{

    @Resource
    RapidOcrClient rapidOcrClient;

    @Override
    public String wholeOcrText(BufferedImage image) throws IOException {
        File localFile = new File("F:\\tesseract-ocr-training\\test.png");
        ImageIO.write(image,"png",localFile);
        String ocrResult = rapidOcrClient.ocr(localFile);
        System.out.println("======"+ocrResult);
        StringBuilder textBuilder = new StringBuilder();
        for(Map.Entry<String,Object> entity: new JSONObject(ocrResult).entrySet()){
            String text = JSONUtil.parseObj(entity.getValue()).getStr("rec_txt")+"\n";
            textBuilder.append(text);
        }
        localFile.deleteOnExit();
        return textBuilder.toString();
    }

    @Override
    public JSONObject wholeOcrJson(BufferedImage image) throws Exception {
        File localFile = new File("F:\\tesseract-ocr-training\\test.png");
        ImageIO.write(image,"png",localFile);
        String ocrResult = rapidOcrClient.ocr(localFile);
        System.out.println("======"+ocrResult);
        return JSONUtil.parseObj(ocrResult);
    }
}
