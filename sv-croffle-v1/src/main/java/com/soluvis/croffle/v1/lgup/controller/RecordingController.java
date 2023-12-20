package com.soluvis.croffle.v1.lgup.controller;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.lgup.service.RecordingService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/v1/api/sta/recording")
public class RecordingController {

	@Autowired
	RecordingService recordingService;

	Logger logger = LoggerFactory.getLogger(RecordingController.class);
	ObjectMapper om = new ObjectMapper();

	@GetMapping(value = "/rest/{ifId}", produces = "application/json; charset=UTF-8")
	public @ResponseBody String restAPI(HttpServletRequest request, @PathVariable("ifId") String ifId,
			@RequestParam Map<String, Object> param) throws Exception {
		logger.info("Recording restAPI ID[{}]", ifId);
		logger.info("Recording restAPI param[{}]", param);

		JSONObject result = new JSONObject();
		JSONArray ja = new JSONArray(om.writeValueAsString(recordingService.serviceByIfId(ifId, param)));

		result.put("list", ja);
		result.put("count", ja.length());
		result.put("status", 200);

		logger.info("{}", result);
		return result.toString();
	}
}