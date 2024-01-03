package com.soluvis.croffle.v1.gcloud.controller;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soluvis.croffle.v1.gcloud.service.AggregateService;
import com.soluvis.croffle.v1.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 클래스 설명	: 통계 검증용 데이터 집계 컨트롤러
 * @Class Name 	: AggregateController
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@RestController
@RequestMapping(value = "/v1/api/gcloud/statistic/aggregate")
public class AggregateController {

	@Autowired
	AggregateService aggregateService;

	Logger logger = LoggerFactory.getLogger(AggregateController.class);
	ObjectMapper om = new ObjectMapper();

	@PostMapping(value = "/agents", produces = "application/json; charset=UTF-8")
	public String aggregateAgentStatistic(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = aggregateService.aggregateAgentStatistic(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@PostMapping(value = "/queues", produces = "application/json; charset=UTF-8")
	public String aggregateQueueStatistic(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = aggregateService.aggregateQueueStatistic(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@PostMapping(value = "/skills", produces = "application/json; charset=UTF-8")
	public String aggregateSkillStatistic(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = aggregateService.aggregateSkillStatistic(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
}