package com.soluvis.croffle.v1.lgup.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.lgup.memobj.MiniWallBoard;
import com.soluvis.croffle.v1.lgup.service.MiniWallBoardService;


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
public class MiniWallBoardScheduler {

	Logger logger = LoggerFactory.getLogger(MiniWallBoardScheduler.class);

	@Autowired
	MiniWallBoardService service;

	@Scheduled(fixedDelay =  2000)
	public void setAgentStat() throws Exception {
//		MiniWallBoard.setAgentStat(service.getAgentStat());
		MiniWallBoard.setAgentStat(null);
	}
	
	@Scheduled(fixedDelay =  2000)
	public void setSkillStat() throws Exception {
//		MiniWallBoard.setSkillStat(service.getAgentStat());
		MiniWallBoard.setSkillStat(null);
	}
	
	@Scheduled(fixedDelay =  2000)
	public void setQueueStat() throws Exception {
//		MiniWallBoard.setQueueStat(service.getAgentStat());
		MiniWallBoard.setQueueStat(null);
	}
}
