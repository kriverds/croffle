package com.soluvis.croffle.v1.ha.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "HAFeignClient", url = "${ha.target.url}")//, configuration = HAFeignInterceptor.class
public interface HAFeignClient {

	@GetMapping(value="/", produces="application/json; charset=UTF-8")
	String healthCheck();
}
