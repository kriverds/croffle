package com.soluvis.croffle.v1.test.controller;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping(value = "/test/redis")
public class RedisTestController {
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	Logger logger = LoggerFactory.getLogger(RedisTestController.class);
	ObjectMapper om = new ObjectMapper();


	@PostMapping(value="/add", produces="application/json; charset=UTF-8")
	public @ResponseBody String addTest(HttpServletRequest request,
			@RequestBody Map<String, Object> param) throws Exception{
		logger.info("{}", param);
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		
		Iterator<String> it = param.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			vop.set(key, param.get(key));
		}
		
		return "OK";
	}
	@GetMapping(value="/get", produces="application/json; charset=UTF-8")
	public @ResponseBody String getTest(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception{
		logger.info("{}", param);
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		
		String keys = param.get("keys").toString();
		String[] arrayKey = keys.split(",");
		
		JSONObject result = new JSONObject();
		
		for(String key : arrayKey) {
			Object value = vop.get(key);
			result.put(key, value==null?"":value);
		}
		return result.toString();
	}
	@DeleteMapping(value="/delete", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteTest(HttpServletRequest request,
			@RequestBody Map<String, Object> param) throws Exception{
		logger.info("{}", param);
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		
		Iterator<String> it = param.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			Object value = vop.getAndDelete(key);
		}
		
		return "OK";
	}
	
    @PostMapping("/session/add")
    public @ResponseBody String sessionTest(HttpSession httpSession){
        String strSessionTest = "1234";
        httpSession.setAttribute("stest" ,strSessionTest);
        return "OK";
    }
}
