package com.soluvis.croffle.v1.ha.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class HAFeignConfig {

    @Bean
    RequestInterceptor requestInterceptor() {
		return HAFeignInterceptor.of();
	}
}
