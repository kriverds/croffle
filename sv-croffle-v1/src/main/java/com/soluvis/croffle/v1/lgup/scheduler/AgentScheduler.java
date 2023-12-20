package com.soluvis.croffle.v1.lgup.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.lgup.service.AgentService;



/**
 * 클래스 설명	:
 * @Class Name 	: AgentScheduler
 * @date   		: 2023. 12. 19.
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
	AgentService agentService;

	@Scheduled(cron = "${scheduler.agent.check.cron}")
	public void checkAgent() throws Exception {
		agentService.checkLGUPAgent();
		Thread.sleep(1_000L);
	}

}
