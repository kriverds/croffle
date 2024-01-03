package com.soluvis.croffle.v1.lgup.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.lgup.memobj.RealTimeStat;
import com.soluvis.croffle.v1.lgup.service.MiniWallBoardService;


/**
 * 클래스 설명	: 실시간 통계 조회 스케줄러
 * @Class Name 	: RealTimeStatScheduler
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Component
public class RealTimeStatScheduler {

	Logger logger = LoggerFactory.getLogger(RealTimeStatScheduler.class);

	@Autowired
	MiniWallBoardService service;

	@Scheduled(fixedDelayString = "${scheduler.realtime.agent.delay}")
	public void setAgentStat() throws Exception {
//		MiniWallBoard.setAgentStat(service.getAgentStat());
		RealTimeStat.setAgentStat(null);
	}

	@Scheduled(fixedDelayString = "${scheduler.realtime.skill.delay}")
	public void setSkillStat() throws Exception {
//		MiniWallBoard.setSkillStat(service.getAgentStat());
		RealTimeStat.setSkillStat(null);
	}

	@Scheduled(fixedDelayString = "${scheduler.realtime.queue.delay}")
	public void setQueueStat() throws Exception {
//		MiniWallBoard.setQueueStat(service.getAgentStat());
		RealTimeStat.setQueueStat(null);
	}
}
