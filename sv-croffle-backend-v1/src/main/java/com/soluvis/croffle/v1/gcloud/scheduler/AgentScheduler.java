package com.soluvis.croffle.v1.gcloud.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.gcloud.service.AgentManagementService;


/**
 * 클래스 설명	: CTI 상담원 연동 스케쥴러
 * @Class Name 	: AgentScheduler
 * @date   		: 2023. 11. 6.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Component
public class AgentScheduler {

	Logger logger = LoggerFactory.getLogger(AgentScheduler.class);

	@Autowired
	AgentManagementService service;

//	@Scheduled(fixedDelayString = "${scheduler.agent.interval}")
	public void checkAgent() throws Exception {
		logger.info("{}", "checkAgent...");

		List<Map<String,Object>> rMap = null;
			rMap = service.selectDoNotCreatedAgent();
			if(!rMap.isEmpty()) {
				//To do
			}

		Thread.sleep(1_000L*1);
	}

}
