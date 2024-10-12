package com.linzi.daily.ocr.paddle;

import cn.hutool.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface PaddleOcrService {
    String wholeOcrText(BufferedImage image) throws Exception;

    JSONObject wholeOcrJson(BufferedImage image) throws Exception;
}
