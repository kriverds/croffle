package com.soluvis.croffle.v1.sample.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @date	: 2023. 9. 21.
 * @author	: dalo9
 * @version	: 1.0
 */
@Component
public class SampleScheduler {

	Logger logger = LoggerFactory.getLogger(SampleScheduler.class);

	@Scheduled(cron="* */15 * * * *")
	public void fifteenMin() throws Exception {
		logger.info("fifteenMin scheduler Start");
		Thread.sleep(1_000L*60);
	}

//	@Scheduled(cron="*/10 * * * * * ")
	public void tenSec() throws Exception {
		logger.info("tenSec scheduler Start");
		Thread.sleep(1_000L*1);
	}

}
