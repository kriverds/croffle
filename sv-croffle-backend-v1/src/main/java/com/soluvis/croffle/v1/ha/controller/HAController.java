package com.soluvis.croffle.v1.ha.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.ha.config.HAConfig;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 상담사 매니지먼트 컨트롤러
 * @Class Name 	: AgentManagementController
 * @date   		: 2023. 10. 10.
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
		logger.info("receive heartbeat... currentRole[{}]", HAConfig.getCurrentRole());

		return "answer heartbeat... "+HAConfig.getCurrentRole()+"["+ new Date()+"]";
	}
}
