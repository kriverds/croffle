package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.gcloud.service.AgentManagementService;
import com.soluvis.croffle.v1.lgup.mapper.AgentMapper;

/**
 * 클래스 설명	: 상담사 연동 서비스
 * @Class Name 	: AgentService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class AgentService {

	@Autowired
	AgentMapper agentMapper;
	@Autowired
	AgentManagementService agentManagementService;
	@Autowired
	GCConnector gcconnector;

	@Value("${gcloud.skill.outbound.id}")
	String outboundSkillId;

	@Value("${gcloud.division.home.id}")
	String divisionHomeId;
	@Value("${gcloud.division.mobile.id}")
	String divisionMobileId;
	@Value("${gcloud.division.business.id}")
	String divisionBusinessId;

	@Value("${gcloud.role.agentdefault.id}")
	String roleAgentdefaultId;

	private final Logger logger = LoggerFactory.getLogger(AgentService.class);
	private static Map<Integer, Boolean> pgAlarmIdList = new HashMap<>();

	/**
	 * 메서드 설명	: 재직/퇴직 상담사를 체크한다.
	 * @Method Name : checkLGUPAgent
	 * @date   		: 2023. 12. 18.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	public int checkLGUPAgent(UUID rUUID) throws Exception {
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


		int hiredIResult = createLGUPAgent(hiredList, rUUID); // 상담사 생성
		int retiredIResult = deleteLGUPAgent(hiredList, rUUID); // 상담사 삭제

		logger.info("hiredIResult[{}] retiredIResult[{}]", hiredIResult, retiredIResult);

		return hiredIResult+retiredIResult;
	}
	// 생성
	public int createLGUPAgent(List<Map<String, Object>> hiredList, UUID rUUID) throws Exception {
		int iResult = 0;
		List<Map<String, Object>> agentList = hiredList;
		if(agentList.isEmpty()) {
			return iResult;
		}
		logger.info("{}", agentList);

		JSONArray userList = new JSONArray(agentList);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String channel = user.getString("channel"); // 채널에따라 Division 구분
			String divisionId = "";
			if ("기업".equals(channel)) {
				divisionId = divisionBusinessId;
			} else if ("홈".equals(channel)) {
				divisionId = divisionHomeId;
			} else {
				divisionId = divisionMobileId;
			}
			String name = user.getString("empName"); //이름
			String email = user.getString("email"); //이메일
			String title = user.getString("exDutyname"); //직책
//			String department = user.getString("flOrgName"); //조직 풀네임
			String department = user.getString("orgName"); //조직 풀네임
			JSONObject cJO = gcconnector.postUsers(name, email, department, title, divisionId); // GCloud User 생성
			String userId = cJO.get("id").toString();

			

			gcconnector.postAuthorizationSubjectBulkadd(userId, divisionId, roleAgentdefaultId); // GCloud User Role 부여
			gcconnector.postUserRoutingskills(userId, outboundSkillId, 1.0D); // GCloud User Outbound Skill 부여

			result.put(userId, cJO);
		}
		GCConnector.close(rUUID);

		for(Map<String, Object> agent : hiredList) {
			int updateResult = agentMapper.updateHiredAgent(agent); // 상담사 DB 업데이트
			iResult += updateResult;
		}
		if(result.length() != iResult) {
			logger.error("create error//GCloud update count[{}] Database update count[{}]", result.length(), iResult);
		}

		return iResult;
	}
	// 삭제
	public int deleteLGUPAgent(List<Map<String, Object>> retiredList, UUID rUUID) throws Exception {
		int iResult = 0;
		List<Map<String, Object>> agentList = retiredList;
		if(agentList.isEmpty()) {
			return iResult;
		}
		JSONArray userList = new JSONArray(agentList);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String id = user.getString("id");
			gcconnector.deleteUser(id); // GCloud 삭제
			result.put(id, "Y");
		}
		GCConnector.close(rUUID);

		for(Map<String, Object> agent : retiredList) {
			int updateResult = agentMapper.updateRetiredAgent(agent); // 상담사 DB 업데이트
			iResult += updateResult;
		}
		if(result.length() != iResult) {
			logger.error("delete error//GCloud update count[{}] Database update count[{}]", result.length(), iResult);
		}

		return iResult;
	}
}