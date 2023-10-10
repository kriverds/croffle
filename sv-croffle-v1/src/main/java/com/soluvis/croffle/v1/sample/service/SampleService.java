package com.soluvis.croffle.v1.sample.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.sample.mapper.SampleMapper;

@Service
public class SampleService {
	@Autowired
	private SampleMapper sampleMapper;

	Logger logger = LoggerFactory.getLogger(SampleService.class);

	public List<Map<String,Object>> selectSample(Map<String, Object> params){
		logger.info("start service selectSample");
		return sampleMapper.selectSample(params);
	}

	public int insertSample(Map<String, Object> params){
		logger.info("start service insertSample");
		return sampleMapper.insertSample(params);
	}

}
