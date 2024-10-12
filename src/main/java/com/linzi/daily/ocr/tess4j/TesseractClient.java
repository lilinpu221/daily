package com.linzi.daily.ocr.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractClient {

    @Bean
    public ITesseract getTesseract() {
        ITesseract instance = new Tesseract();
        instance.setDatapath("F:\\tesseract-ocr-training\\tessdata");
        instance.setLanguage("chi_sim");
        return instance;
    }
}
