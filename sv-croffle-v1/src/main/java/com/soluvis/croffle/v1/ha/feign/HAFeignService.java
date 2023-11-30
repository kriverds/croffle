package com.soluvis.croffle.v1.ha.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HAFeignService {

	@Autowired
	HAFeignClient hAClient;

	Logger logger = LoggerFactory.getLogger(HAFeignService.class);

	public String healthCheck(){
		return hAClient.healthCheck();
	}

}
