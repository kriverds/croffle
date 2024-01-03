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

import com.soluvis.croffle.v1.gcloud.service.CampaignStatisticService;


/**
 * 클래스 설명	: 캠페인 통계 적재용 스케줄러
 * @Class Name 	: CampaignScheduler
 * @date   		: 2023. 12. 22.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Component
public class CampaignScheduler {

	Logger logger = LoggerFactory.getLogger(CampaignScheduler.class);

	@Autowired
	CampaignStatisticService campaignStatisticService;

	/**
	 * 메서드 설명	: 캠페인 Aggregate
	 * @Method Name : aggregateCampaignStat
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Scheduled(cron = "${scheduler.campaign.15min.cron}")
	public void aggregateCampaignStat() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Map<String, Object> param = new HashMap<>();
		param.put("startDate", sdf.format(new Date()));

		campaignStatisticService.campaignAggregateQuery(param);
		Thread.sleep(1_000L);
	}

	@Scheduled(cron = "${scheduler.campaign.day.cron}")
	public void aggregateCampaignStatDayEnd() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		// 해당 날짜 전체 데이터 삭제
		Map<String, Object> delParam = new HashMap<>();
		delParam.put("startDate", sdf.format(cal.getTime()));
		campaignStatisticService.deleteDayData(delParam);
		
		for (int i = 0; i < 96; i++) {
			Map<String, Object> param = new HashMap<>();
			param.put("startDate", sdf.format(cal.getTime()));
			campaignStatisticService.campaignAggregateQuery(param);
			cal.add(Calendar.MINUTE, 15);
		}

		Thread.sleep(1_000L);
	}
}