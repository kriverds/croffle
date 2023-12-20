package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.gcloud.service.AgentManagementService;
import com.soluvis.croffle.v1.lgup.mapper.AgentMapper;

@Service
public class AgentService {

	@Autowired
	AgentMapper agentMapper;
	@Autowired
	AgentManagementService agentManagementService;

	private final Logger logger = LoggerFactory.getLogger(AgentService.class);
	private static Map<Integer, Boolean> pgAlarmIdList = new HashMap<>();

	/**
	 * 메서드 설명	: 재직/퇴직 상담사를 체크한다.
	 * @Method Name : checkLGUPAgent
	 * @date   		: 2023. 12. 18.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	public int checkLGUPAgent() throws Exception {
		int iResult = 0;
		List<Map<String, Object>> agentList = agentMapper.selectModifiedAgent(); // 재직/퇴직 상담사 조회
		if(agentList.isEmpty()) {
			return iResult;
		}
		List<Map<String, Object>> hiredList = new ArrayList<>();
		List<Map<String, Object>> retiredList = new ArrayList<>();

		for(Map<String, Object> agent : agentList) { // 재직/퇴직 상담사 분리
			String sttsCd = agent.get("exHofcSttsCd").toString();
			if("C".equals(sttsCd)) {
				hiredList.add(agent);
			}else {
				retiredList.add(agent);
			}
		}
		logger.info("hiredList[{}] retiredList[{}]", hiredList.size(), retiredList.size());


		int hiredIResult = createLGUPAgent(hiredList); // GCloud 상담사 생성
		int retiredIResult = deleteLGUPAgent(hiredList); // GCloud 상담사 삭제

		logger.info("hiredIResult[{}] retiredIResult[{}]", hiredIResult, retiredIResult);

		return hiredIResult+retiredIResult;
	}
	// 생성
	public int createLGUPAgent(List<Map<String, Object>> hiredList) throws Exception {
		int iResult = 0;
		List<Map<String, Object>> agentList = hiredList;
		if(agentList.isEmpty()) {
			return iResult;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("userList", agentList);
		JSONObject jo = agentManagementService.postUsers(map); // GCloud 상담사 생성
		for(Map<String, Object> agent : hiredList) {
			int updateResult = agentMapper.updateHiredAgent(agent); // 상담사 DB 업데이트
			iResult += updateResult;
		}
		if(jo.length() != iResult) {
			logger.error("create error//GCloud update count[{}] Database update count[{}]", jo.length(), iResult);
		}

		return iResult;
	}
	// 삭제
	public int deleteLGUPAgent(List<Map<String, Object>> retiredList) throws Exception {
		int iResult = 0;
		List<Map<String, Object>> agentList = retiredList;
		if(agentList.isEmpty()) {
			return iResult;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("userList", agentList);
		JSONObject jo = agentManagementService.deleteUser(map); // GCloud 상담사 삭제
		for(Map<String, Object> agent : retiredList) {
			int updateResult = agentMapper.updateRetiredAgent(agent); // 상담사 DB 업데이트
			iResult += updateResult;
		}
		if(jo.length() != iResult) {
			logger.error("delete error//GCloud update count[{}] Database update count[{}]", jo.length(), iResult);
		}

		return iResult;
	}
}