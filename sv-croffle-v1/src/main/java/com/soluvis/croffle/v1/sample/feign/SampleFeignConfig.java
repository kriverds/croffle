package com.soluvis.croffle.v1.sample.feign;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class SampleFeignConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return SampleFeignInterceptor.of();
	}
}
