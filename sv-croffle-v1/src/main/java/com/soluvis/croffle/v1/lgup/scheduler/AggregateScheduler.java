package com.soluvis.croffle.v1.lgup.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.gcloud.service.AggregateService;


/**
 * 클래스 설명	: Aggregate 통계 적재용 스케줄러
 * @Class Name 	: CampaignScheduler
 * @date   		: 2023. 12. 22.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Component
public class AggregateScheduler {

	Logger logger = LoggerFactory.getLogger(AggregateScheduler.class);

	@Autowired
	AggregateService aggregateService;

	/**
	 * 메서드 설명	: 상담사 Aggregate
	 * @Method Name : aggregateAgentStat
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Scheduled(cron = "${scheduler.aggregate.15min.cron}")
	public void aggregateAgentStat() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Map<String, Object> param = new HashMap<>();
		param.put("startDate", sdf.format(new Date()));

		aggregateService.aggregateAgentStatistic(param);
		Thread.sleep(1_000L);
	}

	@Scheduled(cron = "${scheduler.aggregate.day.cron}")
	public void aggregateAgentStatDayEnd() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		// 해당 날짜 전체 데이터 삭제
		Map<String, Object> delParam = new HashMap<>();
		delParam.put("startDate", sdf.format(cal.getTime()));
		aggregateService.deleteAgentDayData(delParam);

		for (int i = 0; i < 96; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startDate", sdf.format(cal.getTime()));
			aggregateService.aggregateAgentStatistic(param);
			cal.add(Calendar.MINUTE, 15);
		}

		Thread.sleep(1_000L);
	}

	/**
	 * 메서드 설명	: 스킬 Aggregate
	 * @Method Name : aggregateSkillStat
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Scheduled(cron = "${scheduler.aggregate.15min.cron}")
	public void aggregateSkillStat() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Map<String, Object> param = new HashMap<>();
		param.put("startDate", sdf.format(new Date()));

		aggregateService.aggregateSkillStatistic(param);
		Thread.sleep(1_000L);
	}

	@Scheduled(cron = "${scheduler.aggregate.day.cron}")
	public void aggregateSkillStatDayEnd() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		// 해당 날짜 전체 데이터 삭제
		Map<String, Object> delParam = new HashMap<>();
		delParam.put("startDate", sdf.format(cal.getTime()));
		aggregateService.deleteSkillDayData(delParam);

		for (int i = 0; i < 96; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startDate", sdf.format(cal.getTime()));
			aggregateService.aggregateSkillStatistic(param);
			cal.add(Calendar.MINUTE, 15);
		}

		Thread.sleep(1_000L);
	}

	/**
	 * 메서드 설명	: 큐 Aggregate
	 * @Method Name : aggregateQueueStat
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Scheduled(cron = "${scheduler.aggregate.15min.cron}")
	public void aggregateQueueStat() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Map<String, Object> param = new HashMap<>();
		param.put("startDate", sdf.format(new Date()));

		aggregateService.aggregateQueueStatistic(param);
		Thread.sleep(1_000L);
	}

	@Scheduled(cron = "${scheduler.aggregate.day.cron}")
	public void aggregateQueueStatDayEnd() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		// 해당 날짜 전체 데이터 삭제
		Map<String, Object> delParam = new HashMap<>();
		delParam.put("startDate", sdf.format(cal.getTime()));
		aggregateService.deleteQueueDayData(delParam);

		for (int i = 0; i < 96; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startDate", sdf.format(cal.getTime()));
			aggregateService.aggregateQueueStatistic(param);
			cal.add(Calendar.MINUTE, 15);
		}

		Thread.sleep(1_000L);
	}
}