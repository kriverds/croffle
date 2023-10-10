package com.soluvis.croffle.v1.sample.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleFeignService {

	@Autowired
	SampleFeignClient sampleFeignClient;

	Logger logger = LoggerFactory.getLogger(SampleFeignService.class);

	public String getSample(){
		logger.info("start service getSample");
		return sampleFeignClient.getTest("A");
	}

}
