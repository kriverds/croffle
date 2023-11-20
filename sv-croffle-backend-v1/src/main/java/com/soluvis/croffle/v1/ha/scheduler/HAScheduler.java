package com.soluvis.croffle.v1.ha.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.ha.config.HAConfig;
import com.soluvis.croffle.v1.ha.feign.HAFeignService;

import feign.RetryableException;


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
	HAFeignService hAService;

//	@Scheduled(fixedDelayString = "${ha.inverval}")
	public void checkHA() throws Exception {
		logger.info("send heartbeat... currentRole[{}]", HAConfig.getCurrentRole());
		String result = "";
		try {
			result = hAService.healthCheck();
			logger.info("{}", result);
			if(HAConfig.isEmergencyFlag()) {
				HAConfig.setEmergencyFlag(false);
				HAConfig.setRoleDefault();
				logger.error("set Role Default[{}]", HAConfig.getCurrentRole());
			}
		} catch (RetryableException e) {
			logger.error("HA Error {}", e.getMessage());
			HAConfig.setEmergencyFlag(true);
			HAConfig.setRolePrimary();
			logger.error("set Role Primary[{}]", HAConfig.getCurrentRole());
		}

		Thread.sleep(1_000L*1);
	}

}
