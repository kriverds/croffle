package com.soluvis.croffle.v1.lgup.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.soluvis.croffle.v1.util.GVal;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 클래스 설명	: APIM Feign 수행 시 헤더 추가
 * @Class Name 	: APIMFeignServiceHeaderInterceptor
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
public class APIMFeignServiceHeaderInterceptor implements RequestInterceptor {

	private final Logger logger = LoggerFactory.getLogger(APIMFeignServiceHeaderInterceptor.class);

	@Value("${apim.client-id}")
	String clientId;
	@Value("${apim.secret}")
	String secret;

	@Override
	public void apply(RequestTemplate template) {
		template.header("X-IBM-Client-id", clientId);
		template.header("X-IBM-Client-Secret", secret);
		template.header("Authorization", "Bearer " + GVal.getAccessToken());
		template.header("accept", "application/json");
		template.header("Content-Type", "application/json");
		logger.info("{}", template.headers());
	}

}
