package com.soluvis.croffle.v1.lgup.config;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class APIMFeignServiceHeaderConfig {

    @Bean
    RequestInterceptor apimRequestInterceptor() {
		return new APIMFeignServiceHeaderInterceptor();
	}
}
