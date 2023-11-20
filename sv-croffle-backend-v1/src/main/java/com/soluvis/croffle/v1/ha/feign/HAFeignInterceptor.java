package com.soluvis.croffle.v1.ha.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class HAFeignInterceptor implements RequestInterceptor{


	private final Logger logger = LoggerFactory.getLogger(HAFeignInterceptor.class);


	@Override
	public void apply(RequestTemplate template) {
		template.header("header1", "head1");
		logger.info("{}", template.headers());
	}




}
