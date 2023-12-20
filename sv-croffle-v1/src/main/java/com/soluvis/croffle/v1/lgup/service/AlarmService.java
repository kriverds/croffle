package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.gcloud.util.CommUtil;
import com.soluvis.croffle.v1.lgup.mapper.MiniWallBoardMapper;
import com.soluvis.croffle.v1.lgup.memobj.RealTimeStat;

@Service
public class AlarmService {

	@Autowired
	MiniWallBoardMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(AlarmService.class);
	private static Map<Integer, Boolean> pgAlarmIdList = new HashMap<>();

	public void sendQueueGroupAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 큐그룹 알람 리스트 조회 1.큐그룹ID 2.큐ID리스트 3.대기호 4.문자발송간격 5.알람ID

		if (!qResult.isEmpty()) {
			for (Map<String, Object> qgq : qResult) {
				int alarmId = (int) qgq.get("alarmId");
				List<String> queueIdList = Arrays.asList((String[]) qgq.get("queId"));
				int thWaitCnt = (int) qgq.get("waitCnt");

				int waitCntSum = 0;
				for (String queueId : queueIdList) {
					JSONObject queueStat = RealTimeStat.getQueueStat(queueId);
					waitCntSum += CommUtil.getJInt(queueStat, "waitCnt");
				}

				if (waitCntSum >= thWaitCnt) {
					new Thread() {
						@Override
						public void run() {
							if (AlarmService.pgAlarmIdList.get(alarmId) == null) {
								AlarmService.pgAlarmIdList.put(alarmId, true);
								logger.info("큐그룹 문자 발송!!![{}]", queueIdList);
								long smsSendInterval = (long)qgq.get("smsSendInterval") * 60 * 1_000;
								try {
									Thread.sleep(smsSendInterval);
								} catch (InterruptedException e) {
									logger.error("{}", e);
									Thread.currentThread().interrupt();
								} finally {
									AlarmService.pgAlarmIdList.remove(alarmId);
								}
							}
							super.run();
						}
					}.start();
				}
			}
		}
	}

	public void sendQueueAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 큐그룹 알람 리스트 조회 1.큐ID 2.대기호 3.문자발송간격 4.알람ID

		if (!qResult.isEmpty()) {
			for (Map<String, Object> q : qResult) {
				int alarmId = (int) q.get("alarmId");
				String queueId = q.get("queId").toString();
				int thWaitCnt = (int) q.get("waitCnt");

				int waitCnt = 0;
				JSONObject queueStat = RealTimeStat.getQueueStat(queueId);
				waitCnt = CommUtil.getJInt(queueStat, "waitCnt");

				if (waitCnt >= thWaitCnt) {
					new Thread() {
						@Override
						public void run() {
							if (AlarmService.pgAlarmIdList.get(alarmId) == null) {
								AlarmService.pgAlarmIdList.put(alarmId, true);
								logger.info("큐 문자 발송!!![{}]", queueId);
								long smsSendInterval = (long)q.get("smsSendInterval") * 60 * 1_000;
								try {
									Thread.sleep(smsSendInterval);
								} catch (InterruptedException e) {
									logger.error("{}", e);
									Thread.currentThread().interrupt();
								} finally {
									AlarmService.pgAlarmIdList.remove(alarmId);
								}
							}
							super.run();
						}
					}.start();
				}
			}
		}
	}
}