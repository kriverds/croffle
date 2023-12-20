package com.soluvis.croffle.v1.lgup.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soluvis.croffle.v1.lgup.config.APIMServiceHeaderConfig;

@FeignClient(name = "TestFeignClient", url = "http://localhost:8002", configuration = APIMServiceHeaderConfig.class)
public interface TestFeignClient {

//	@GetMapping(value="/test/get", produces="application/x-www-form-urlencoded")
	@GetMapping(value="/test/get", produces="application/json")
	String test1(@RequestParam Map<String,Object> param);
}
