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
		//String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		SpringApplication.run(DailyApplication.class, args);
	}

}
