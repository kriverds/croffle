package com.soluvis.croffle.v1.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SampleFeignClient", url = "https://api.github.com/repos", configuration = SampleFeignInterceptor.class)
public interface SampleFeignClient {

	@GetMapping(value="/OpenFeign/feign/contributors", produces="application/json; charset=EUC-KR")
	String getTest(@RequestParam("Param1") String param1);
}
