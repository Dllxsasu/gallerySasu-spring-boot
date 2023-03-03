package com.jeremias.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync //habilida la asincronidad
@SpringBootApplication
public class GalleryPhotosBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(GalleryPhotosBackApplication.class, args);
	}

}
