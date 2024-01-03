package com.soluvis.croffle.v1.lgup.scheduler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.lgup.service.AgentService;

/**
 * 클래스 설명	: LGU+ > 통계 > GCloud 상담사 연동 스케줄러
 * @Class Name 	: AgentScheduler
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
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
		UUID rUUID = UUID.randomUUID();
		try {
			agentService.checkLGUPAgent(rUUID);
		} catch (Exception e) {
			logger.error("{}", e);
			GCConnector.close(rUUID);
		}

		Thread.sleep(1_000L);
	}

}
