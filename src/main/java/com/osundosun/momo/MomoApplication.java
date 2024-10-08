package com.osundosun.momo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:naver.properties")
public class MomoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MomoApplication.class, args);
	}

}
