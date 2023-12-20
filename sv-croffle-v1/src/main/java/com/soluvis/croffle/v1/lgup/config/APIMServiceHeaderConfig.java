package com.soluvis.croffle.v1.lgup.config;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class APIMServiceHeaderConfig {

    @Bean
    RequestInterceptor apimRequestInterceptor() {
		return new APIMServiceHeaderInterceptor();
	}
}
