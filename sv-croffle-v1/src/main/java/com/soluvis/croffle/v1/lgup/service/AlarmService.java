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

import com.soluvis.croffle.v1.lgup.mapper.MiniWallBoardMapper;
import com.soluvis.croffle.v1.lgup.memobj.RealTimeStat;
import com.soluvis.croffle.v1.util.CommUtil;

/**
 * 클래스 설명	: 알람 발송 관련 서비스
 * @Class Name 	: AlarmService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class AlarmService {

	@Autowired
	MiniWallBoardMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(AlarmService.class);
	private static Map<Integer, Boolean> pgAlarmIdList = new HashMap<>();

	/**
	 * 메서드 설명	: 대기호 알람
	 * @Method Name : sendQueueGroupWaitAlarm
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	public void sendQueueGroupWaitAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 큐그룹 알람 리스트 조회 1.큐그룹ID 2.큐ID리스트 3.대기호 4.문자발송간격 5.알람ID

		if (!qResult.isEmpty()) {
			for (Map<String, Object> qgq : qResult) {
				Map<String, Object> mybatisParam = new HashMap<>();
				int alarmId = (int) qgq.get("alarmId");
				List<String> queueIdList = Arrays.asList((String[]) qgq.get("queueId"));
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

								mybatisParam.put("alarmId", alarmId);
								List<Map<String, Object>> phoneResult = new ArrayList<>(); // 전화번호 리스트
								for(Map<String, Object> phone : phoneResult) {
									logger.info("큐 문자 발송!!![{}]", alarmId);
								}
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

	public void sendQueueWaitAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 큐그룹 알람 리스트 조회 1.큐ID 2.대기호 3.문자발송간격 4.알람ID

		if (!qResult.isEmpty()) {
			for (Map<String, Object> q : qResult) {
				Map<String, Object> mybatisParam = new HashMap<>();
				int alarmId = (int) q.get("alarmId");
				String queueId = q.get("queueId").toString();
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
								
								mybatisParam.put("alarmId", alarmId);
								List<Map<String, Object>> phoneResult = new ArrayList<>(); // 전화번호 리스트
								for(Map<String, Object> phone : phoneResult) {
									logger.info("큐 문자 발송!!![{}]", alarmId);
								}
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
	
	/**
	 * 메서드 설명	: 포기호 알람
	 * @Method Name : sendQueueGroupAbandonAlarm
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	public void sendQueueGroupAbandonAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 포기호 알람 리스트 조회 1.큐그룹ID 2.큐ID리스트 3.문자발송간격 4.알람ID

		if (!qResult.isEmpty()) {
			for (Map<String, Object> qgq : qResult) {
				Map<String, Object> mybatisParam = new HashMap<>();
				int alarmId = (int) qgq.get("alarmId");
				List<String> queueIdList = Arrays.asList((String[]) qgq.get("queueId"));
				mybatisParam.put("queueList", queueIdList);

				List<Map<String, Object>> abandonList = new ArrayList<>(); // 포기호 리스트
				
				if(!abandonList.isEmpty()) {
					String strAbandonList = "";
					for(Map<String, Object> map : abandonList) {
						strAbandonList += "[" + map.get("시간").toString() + "]" + map.get("큐이름").toString();
					}
					
					mybatisParam.put("alarmId", alarmId);
					List<Map<String, Object>> phoneResult = new ArrayList<>(); // 전화번호 리스트
					for(Map<String, Object> phone : phoneResult) {
						logger.info("큐 문자 발송!!![{}]", alarmId);
					}
				}
			}
		}
	}
	
	public void sendQueueAbandonAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 포기호 알람 리스트 조회 1.큐그룹ID 2.큐ID리스트 3.문자발송간격 4.알람ID

		if (!qResult.isEmpty()) {
			for (Map<String, Object> qgq : qResult) {
				Map<String, Object> mybatisParam = new HashMap<>();
				int alarmId = (int) qgq.get("alarmId");
				String queueId = qgq.get("queueId").toString();
				mybatisParam.put("queueId", queueId);
				
				List<Map<String, Object>> abandonList = new ArrayList<>(); // 포기호 리스트
				
				if(!abandonList.isEmpty()) {
					String strAbandonList = "";
					for(Map<String, Object> map : abandonList) {
						strAbandonList += "[" + map.get("시간").toString() + "]" + map.get("큐이름").toString();
					}
					
					mybatisParam.put("alarmId", alarmId);
					List<Map<String, Object>> phoneResult = new ArrayList<>(); // 전화번호 리스트
					for(Map<String, Object> phone : phoneResult) {
						logger.info("큐 문자 발송!!![{}]", alarmId);
					}
				}
			}
		}
	}
	
	/**
	 * 메서드 설명	: 실적 알람
	 * @Method Name : sendAgentStatAlarm
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws Exception
	 * @notify
	 * 
	 */
	public void sendAgentStatAlarm() throws Exception {
		List<Map<String, Object>> qResult = new ArrayList<>(); // 포기호 알람 리스트 조회 1.큐그룹ID 2.큐ID리스트 3.문자발송간격 4.알람ID
		Map<String, Object> mybatisParam = new HashMap<>();
		List<Map<String, Object>> statList = new ArrayList<>(); // 상담원 실적 통
	}
}