package com.soluvis.croffle.v1.lgup.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.lgup.service.MiniWallBoardService;


/**
 * 클래스 설명	: 알람 등록 체크 스케줄러
 * @Class Name 	: AlarmScheduler
 * @date   		: 2023. 11. 6.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 */
@Component
public class AlarmScheduler {

	Logger logger = LoggerFactory.getLogger(AlarmScheduler.class);

	@Autowired
	MiniWallBoardService service;

	@Scheduled(fixedDelayString = "${scheduler.alram.threshold.delay}")
	public void checkThreshold() throws Exception {
//		logger.info("{}", "2 second");
	}

	@Scheduled(cron = "${scheduler.alram.abandon.cron}")
	public void checkAbandonCall() throws Exception {
		logger.info("{}", "minute");
	}

	@Scheduled(cron = "${scheduler.alram.agentstat.cron}")
	public void sendAgentHourStatic() throws Exception {
		logger.info("{}", "1 hour");
	}
}
