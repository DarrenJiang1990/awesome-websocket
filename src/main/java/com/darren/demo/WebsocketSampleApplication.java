package com.darren.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //启用定时功能
public class WebsocketSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketSampleApplication.class, args);
	}

}
