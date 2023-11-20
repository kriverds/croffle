package com.soluvis.croffle.v1.scheduler.controller;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.scheduler.service.SchedulerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/v1/api/scheduler/management")
public class SchedulerController {

	@Autowired
	SchedulerService schedulerService;

	Logger logger = LoggerFactory.getLogger(SchedulerController.class);
	ObjectMapper om = new ObjectMapper();

	@GetMapping(value = "/optiongroups", produces = "application/json; charset=UTF-8")
	public @ResponseBody String selectOptionGroup(HttpServletRequest request) throws Exception {

		JSONObject result = new JSONObject();
		JSONArray ja = new JSONArray(om.writeValueAsString(schedulerService.selectOptionGroup()));

		result.put("data", ja);

		logger.info("{}", result);
		return result.toString();
	}

	@PostMapping(value = "/optiongroups", produces = "application/json; charset=UTF-8")
	public @ResponseBody String insertOptionGroup(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {

		logger.info("{}", param);

		JSONObject result = new JSONObject();

		int dbResult = 0;

		try {
			dbResult = schedulerService.insertOptionGroup(param);
			if (dbResult > 0) {
				result.put("code", "200");
				result.put("message", "성공");
			} else {
				result.put("code", "901");
				result.put("message", "그룹 이름을 확인하세요.");
			}
		} catch (Exception e) {
			result.put("code", "901");
			result.put("message", "그룹 이름을 확인하세요.");
		}

		logger.info("처리 건수[{}]", dbResult);

		return result.toString();
	}

	@DeleteMapping(value = "/optiongroups", produces = "application/json; charset=UTF-8")
	public @ResponseBody String deleteOptionGroup(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {

		logger.info("{}", param);

		JSONObject result = new JSONObject();

		int dbResult = 0;

		try {
			dbResult = schedulerService.deleteOptionGroup(param);
			if (dbResult > 0) {
				result.put("code", "200");
				result.put("message", "성공");
			} else {
				result.put("code", "901");
				result.put("message", "그룹 이름을 확인하세요.");
			}
		} catch (Exception e) {
			result.put("code", "901");
			result.put("message", "그룹 이름을 확인하세요.");
		}

		logger.info("처리 건수[{}]", dbResult);

		return result.toString();
	}

	@GetMapping(value = "/option", produces = "application/json; charset=UTF-8")
	public @ResponseBody String selectOption(HttpServletRequest request
	/* , @RequestBody Map<String,Object> param */) throws Exception {

		JSONObject result = new JSONObject();
		JSONArray ja = new JSONArray(om.writeValueAsString(schedulerService.selectOption()));

		result.put("data", ja);

		logger.info("{}", result);
		return result.toString();
	}

	@PostMapping(value = "/option", produces = "application/json; charset=UTF-8")
	public @ResponseBody String saveOption(HttpServletRequest request, @RequestBody List<Map<String, Object>> param)
			throws Exception {

		logger.info("{}", param);

		JSONObject result = new JSONObject();

		int dbResult = 0;
		try {
			dbResult = schedulerService.saveOption(param);
			if (dbResult > 0) {
				result.put("code", "200");
				result.put("message", "성공");
			} else {
				result.put("code", "901");
				result.put("message", "실패.");
			}
		} catch (Exception e) {
			result.put("code", "901");
			result.put("message", "그룹 이름을 확인하세요.");
		}

		logger.info("처리 건수[{}]", dbResult);

		return result.toString();
	}

	@GetMapping(value = "/test", produces = "application/json; charset=UTF-8")
	public @ResponseBody String test(HttpServletRequest request
	/* , @RequestBody Map<String,Object> param */) throws Exception {
		logger.info("{}", "init");

		schedulerService.test();

		return "OK";
	}

}
