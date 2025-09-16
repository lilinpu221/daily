package com.linzi.daily.ocr.paddle;


import com.alibaba.fastjson2.JSONObject;

import java.awt.image.BufferedImage;

public interface PaddleOcrService {
    String wholeOcrText(BufferedImage image) throws Exception;

    JSONObject wholeOcrJson(BufferedImage image) throws Exception;
}
