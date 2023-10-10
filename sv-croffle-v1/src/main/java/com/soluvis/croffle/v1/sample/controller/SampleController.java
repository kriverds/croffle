package com.soluvis.croffle.v1.sample.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.sample.feign.SampleFeignService;
import com.soluvis.croffle.v1.sample.service.SampleService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/v1/api/sample/sample")
public class SampleController {

	@Autowired
	SampleService sampleService;
	@Autowired
	SampleFeignService sampleFeignService;

	Logger logger = LoggerFactory.getLogger(SampleController.class);

	@GetMapping(value="/get/querystring", produces="application/json; charset=UTF-8")
	public @ResponseBody String getSample(HttpServletRequest request
			, @RequestParam("param1") String param1 ) throws Exception{
		logger.info("{}{}", param1, param1);
		JSONObject jo = new JSONObject();
		Map<String,Object> params = new HashMap<>();
		ObjectMapper om = new ObjectMapper();
		List<Map<String,Object>> list = sampleService.selectSample(params);

		JSONArray ja = null;
		ja = new JSONArray(om.writeValueAsString(list));
		jo.put("list", ja);
		logger.info("{}", jo);

		return jo.toString();
	}

	@PostMapping("/post/json")
	public @ResponseBody String postSample(HttpServletRequest request
			, @RequestBody Map<String,Object> param ) {
			logger.info("{}", param);

			int result = sampleService.insertSample(param);

			logger.info("{}", result);

		return "postSample";
	}

	@GetMapping(value="/get/feigntest", produces="application/json; charset=UTF-8")
	public @ResponseBody String getFeign(HttpServletRequest request){
		Enumeration<?> enu =  request.getHeaderNames();
		while(enu.hasMoreElements()) {
			String key = enu.nextElement().toString();
			String val = request.getHeader(key);
			logger.info("{}:{}", key, val);
		}

		return sampleFeignService.getSample();
	}
}
