package com.soluvis.croffle.v1.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.ha.mapper.HAMapper;
import com.soluvis.croffle.v1.util.GVal;

import jakarta.annotation.PostConstruct;

@Component
public class InitCroffle {

	private final Logger logger = LoggerFactory.getLogger(InitCroffle.class);

	@Autowired
	HAMapper hAMapper;
//	HAService service;

	@PostConstruct
//	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		logger.info("{}", "ApplicationReadyEvent");

		String applicationId = System.getProperty("applicationId");
		GVal.setApplicationId(applicationId);
		initHA();
	}

	private void initHA() {
		boolean primaryFlag = false;
		List<Map<String, Object>> qResult = hAMapper.initSelect();
		if (qResult.isEmpty()) {
			Map<String, Object> dbparam = new HashMap<>();
			logger.info("{}", GVal.getApplicationId());

			dbparam.put("id", Integer.parseInt(GVal.getApplicationId()));
			hAMapper.initInsert(dbparam);
			primaryFlag = true;
		} else {
			String id = qResult.get(0).get("id").toString();
			if (id.equals(GVal.getApplicationId())) {
				primaryFlag = true;
			}
		}
		if (primaryFlag) {
			GVal.setHaRole("primary");
		}
	}
}
