package com.linzi.daily;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

/**
 * @author Lil
 */
@Slf4j
@SpringBootApplication
public class DailyApplication {

	public static void main(String[] args) {
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		log.info("===========支持的字体列表===========");
		for(String font:fonts){
			log.info(font);
		}
		SpringApplication.run(DailyApplication.class, args);
	}

}
