package com.soluvis.croffle.v1.gcloud.controller;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soluvis.croffle.v1.gcloud.service.VerifyService;
import com.soluvis.croffle.v1.gcloud.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 클래스 설명 : 캠페인 통계 컨트롤러
 *
 * @Class Name : AgentManagementController
 * @date : 2023. 10. 10.
 * @author : Riverds
 * @version : 1.0 ----------------------------------------
 * @notify
 *
 */
@RestController
@RequestMapping(value = "/v1/api/gcloud/statistic/verify")
public class VerifyController {

	@Autowired
	VerifyService verifyService;

	Logger logger = LoggerFactory.getLogger(VerifyController.class);
	ObjectMapper om = new ObjectMapper();

	@GetMapping(value = "/agents", produces = "application/json; charset=UTF-8")
	public String verifyAgentStatistic(HttpServletRequest request, @RequestParam Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = verifyService.verifyAgentStatistic(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/queues", produces = "application/json; charset=UTF-8")
	public String verifyQueueStatistic(HttpServletRequest request, @RequestParam Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = verifyService.verifyQueueStatistic(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/skills", produces = "application/json; charset=UTF-8")
	public String verifySkillStatistic(HttpServletRequest request, @RequestParam Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = verifyService.verifySkillStatistic(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
}