package com.soluvis.croffle.v1.sample.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class SampleFeignInterceptor implements RequestInterceptor{


	private final Logger logger = LoggerFactory.getLogger(SampleFeignInterceptor.class);


	@Override
	public void apply(RequestTemplate template) {
		template.header("header1", "head1");
		logger.info("{}", template.headers());
	}


}
