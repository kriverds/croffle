package com.soluvis.croffle.v1.ha.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.ha.service.HAService;
import com.soluvis.croffle.v1.util.GVal;


/**
 * 클래스 설명	: HA Heartbeat Check Scheduler
 * @Class Name 	: HAScheduler
 * @date   		: 2023. 10. 17.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Component
public class HAScheduler {

	Logger logger = LoggerFactory.getLogger(HAScheduler.class);

	@Autowired
	HAService hAService;

	@Scheduled(fixedDelayString = "${scheduler.ha.check.delay}")
	public void checkHA() throws Exception {
		logger.info("check role... currentRole[{}]", GVal.getHaRole());
		int result = 0;
		result = hAService.checkTimeout();

		if("primary".equals(GVal.getHaRole()) && result != 2) {
			GVal.setHaRole("backup");
			logger.error("{}", "role change[primary > backup]");
		} else if(result == 1) {
			GVal.setHaRole("primary");
			logger.error("{}", "role change[backup > primary]");
		}
	}

}
