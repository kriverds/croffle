package com.soluvis.croffle.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class SvCroffleBackendV1Application {

	public static void main(String[] args) {
		SpringApplication.run(SvCroffleBackendV1Application.class, args);
	}
}
