package com.soluvis.croffle.v1.ha.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.util.GVal;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 서버 상태체크 컨트롤러
 * @Class Name 	: HAController
 * @date   		: 2023. 12. 20.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Controller
@RequestMapping(value = "/health")
public class HAController {

	Logger logger = LoggerFactory.getLogger(HAController.class);
	@GetMapping(value="/", produces="application/json; charset=UTF-8")
	public @ResponseBody String healthCheck(HttpServletRequest request ) throws Exception{
		logger.info("currentRole[{}]", GVal.getHaRole());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return GVal.getHaRole()+"["+sdf.format(new Date())+"]";
	}
}
