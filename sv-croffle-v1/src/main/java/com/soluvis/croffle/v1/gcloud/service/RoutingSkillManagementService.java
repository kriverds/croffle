package com.soluvis.croffle.v1.gcloud.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.gcloud.mapper.RoutingSkillManagementMapper;
import com.soluvis.croffle.v1.util.CommUtil;

/**
 * 클래스 설명	: GCloud 스킬 매니징 서비스
 * @Class Name 	: RoutingSkillManagementService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class RoutingSkillManagementService {

	@Autowired
	GCConnector gcconnector;
	@Autowired
	RoutingSkillManagementMapper routingSkillManagementMapper;

	@Value("${gcloud.skill.outbound.id}")
	String outboundSkillId;


	private final Logger logger = LoggerFactory.getLogger(RoutingSkillManagementService.class);

	ObjectMapper om = new ObjectMapper();
	TypeReference<List<Map<String, Object>>> listMapTypeReference = new TypeReference<>() {};

	/**
	 * 메서드 설명	: 상담사가 보유한 스킬 조회
	 * @Method Name : getUserRoutingskills
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	public JSONObject getUserRoutingskills(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = gcconnector.getUserRoutingskills(userId);
			result.put(userId, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	/**
	 * 메서드 설명	: 스킬 전체 목록 조회
	 * @Method Name : getRoutingSkillList
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	public JSONObject getRoutingSkillList(Map<String,Object> param) throws Exception {
		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getRoutingSkillList();
		GCConnector.close(rUUID);

		return result;
	}

	/**
	 * 메서드 설명	: 스킬 다중 부여/변경
	 * @Method Name : patchUserRoutingskillsBulk
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Transactional(transactionManager = "GCloudTransactionManager")
	public JSONObject patchUserRoutingskillsBulk(Map<String,Object> param) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		Map<String, Object> mybatisParam = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String jobDate = sdf.format(new Date());
		String jobCategory = "추가";
		int targetCart = -1;
		mybatisParam.put("jobDate", jobDate);
		mybatisParam.put("jobCategory", jobCategory);
		mybatisParam.put("targetCart", targetCart);
		mybatisParam.put("operatorId", jParam.get("operatorId"));

		//유저 리스트 세팅
		JSONArray userJA = jParam.getJSONArray("userList");
		//스킬 리스트 세팅
		JSONArray skillJA = jParam.getJSONArray("skillList");
		List<String> skillList = new ArrayList<>();
		List<Double> levelList = new ArrayList<>();
		for (int i = 0; i < skillJA.length(); i++) {
			JSONObject skill = skillJA.getJSONObject(i);
			skillList.add(skill.getString("id"));
			levelList.add(skill.getDouble("level"));
		}
		mybatisParam.put("skillList", skillList);
		mybatisParam.put("levelList", levelList);

		//스킬 추가
		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		for (int i = 0; i < userJA.length(); i++) {
			JSONObject user = userJA.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = gcconnector.patchUserRoutingskillsBulk(userId, skillJA);
			result.put(userId, cJO);
		}
		//스킬 현황 업데이트
		List<Map<String, Object>> userJAtoMap = om.readValue(userJA.toString(), listMapTypeReference);
		mybatisParam.put("userList", userJAtoMap);
		replaceAgentSkillPresent(mybatisParam);
		GCConnector.close(rUUID);

		//스킬 관리 이력 저장
		logger.info("{}", mybatisParam);
		saveSkillManagementHistory(mybatisParam);

		return result;
	}

	/**
	 * 메서드 설명	: 스킬 삭제
	 * @Method Name : deleteUserRoutingskill
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Transactional(transactionManager = "GCloudTransactionManager")
	public JSONObject deleteUserRoutingskill(Map<String,Object> param) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		Map<String, Object> mybatisParam = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String jobDate = sdf.format(new Date());
		String jobCategory = "삭제";
		int targetCart = -1;
		mybatisParam.put("jobDate", jobDate);
		mybatisParam.put("jobCategory", jobCategory);
		mybatisParam.put("targetCart", targetCart);
		mybatisParam.put("operatorId", jParam.get("operatorId"));

		//유저 리스트 세팅
		JSONArray userJA = jParam.getJSONArray("userList");
		//스킬 리스트 세팅
		JSONArray skillJA = jParam.getJSONArray("skillList");
		List<String> skillList = new ArrayList<>();
		List<Double> levelList = new ArrayList<>();
		for (int i = 0; i < skillJA.length(); i++) {
			JSONObject skill = skillJA.getJSONObject(i);
			skillList.add(skill.getString("id"));
			levelList.add(-1D);
		}
		mybatisParam.put("skillList", skillList);
		mybatisParam.put("levelList", levelList);

		//스킬 삭제
		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		for (int i = 0; i < userJA.length(); i++) {
			JSONObject user = userJA.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = new JSONObject();
			int total = 0;
			for (int j = 0; j < skillJA.length(); j++) {
				JSONObject skill = skillJA.getJSONObject(j);
				String skillId = skill.getString("id");
				gcconnector.deleteUserRoutingskill(userId, skillId);
				total++;
			}
			cJO.put("total", total);
			result.put(userId, cJO);
		}

		//스킬 현황 업데이트
		List<Map<String, Object>> userJAtoMap = om.readValue(userJA.toString(), listMapTypeReference);
		mybatisParam.put("userList", userJAtoMap);
		replaceAgentSkillPresent(mybatisParam);
		GCConnector.close(rUUID);

		//스킬 관리 이력 저장
		logger.info("{}", mybatisParam);
		saveSkillManagementHistory(mybatisParam);

		return result;
	}

	/**
	 * 메서드 설명	: 스킬 카트 부여
	 * @Method Name : changeSkillBySkillCart
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Transactional(transactionManager = "GCloudTransactionManager")
	public JSONObject changeSkillBySkillCart(Map<String, Object> param) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		Map<String, Object> mybatisParam = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String jobDate = sdf.format(new Date());
		String jobCategory = "카트";
		int targetCart = (Integer) param.get("skillCartId");
		mybatisParam.put("jobDate", jobDate);
		mybatisParam.put("jobCategory", jobCategory);
		mybatisParam.put("targetCart", targetCart);
		mybatisParam.put("operatorId", jParam.get("operatorId"));

		//유저 리스트 세팅
		JSONArray userJA = jParam.getJSONArray("userList");

		//스킬 리스트 세팅
		List<Map<String, Object>> skillListByCart = routingSkillManagementMapper.selectSkillByCart(mybatisParam);
		//아웃바운드 스킬 기본으로 추가
		Map<String, Object> outboundSkillMap = new HashMap<>();
		outboundSkillMap.put("skillId", outboundSkillId);
		outboundSkillMap.put("skillLevel", 1);
		skillListByCart.add(outboundSkillMap);

		List<String> skillList = new ArrayList<>();
		List<Double> levelList = new ArrayList<>();
		for (Map<String, Object> map : skillListByCart) {
			skillList.add(map.get("skillId").toString());
			levelList.add(Double.parseDouble(map.get("skillLevel").toString()));
		}
		mybatisParam.put("skillList", skillList);
		mybatisParam.put("levelList", levelList);

		//스킬 부여
		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		for (int i = 0; i < userJA.length(); i++) {
			JSONObject user = userJA.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = gcconnector.putUserRoutingskillsBulk(userId, skillListByCart);
			result.put(userId, cJO);
		}
		logger.info("####{}", result);


		//스킬 현황 업데이트
		List<Map<String, Object>> userJAtoMap = om.readValue(userJA.toString(), listMapTypeReference);
		mybatisParam.put("userList", userJAtoMap);
		replaceAgentSkillPresent(mybatisParam);
		GCConnector.close(rUUID);

		//스킬 관리 이력 저장
		logger.info("{}", mybatisParam);
		saveSkillManagementHistory(mybatisParam);

		return result;
	}

	/**
	 * 메서드 설명	: 스킬관리 히스토리 적재
	 * @Method Name : saveSkillManagementHistory
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	public int saveSkillManagementHistory(Map<String,Object> param) throws Exception {
		return routingSkillManagementMapper.insertSkillManagementHistory(param);
	}

	/**
	 * 메서드 설명	: 상담사 스킬 매핑 적재
	 * @Method Name : replaceAgentSkillPresent
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	@Transactional(transactionManager = "GCloudTransactionManager")
	public JSONObject replaceAgentSkillPresent(Map<String,Object> param) throws Exception {
		logger.info("{}", param);

		JSONObject result = new JSONObject();
		List<Map<String,Object>> presentList = new ArrayList<>();
		List<Map<String,Object>> deleteUserList = new ArrayList<>();
		JSONObject jParam = new JSONObject(param);
		JSONArray userList = jParam.getJSONArray("userList");
		int paramCnt = userList.length();

		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		if(paramCnt==0) {
			userList = new JSONArray(routingSkillManagementMapper.selectAvailableUserAll(param));
		}

		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			Map<String,Object> deleteUserMap = new HashMap<>();

			String userUUID = user.getString("id");

			deleteUserMap.put("userId", userUUID);
			deleteUserList.add(deleteUserMap);

			JSONArray userSkillList = gcconnector.getUserRoutingskills(userUUID).getJSONArray("list");
			logger.info("{}", userSkillList);

			for (int j = 0; j < userSkillList.length(); j++) {
				Map<String,Object> presentMap = new HashMap<>();
				JSONObject userSkill = userSkillList.getJSONObject(j);
				presentMap.put("userId", userUUID);
				presentMap.put("skillId", userSkill.getString("id"));
				presentMap.put("skillLevel", userSkill.get("proficiency"));
				presentList.add(presentMap);
			}
		}
		GCConnector.close(rUUID);

		int dbDeleteCnt = 0;
		int dbInsertCnt = 0;
		if(paramCnt == 0) {
			dbDeleteCnt = routingSkillManagementMapper.deleteAllAgentSkillPresent();
		}else {
			Map<String,Object> dbParam = new HashMap<>();
			dbParam.put("userList", deleteUserList);

			dbDeleteCnt = routingSkillManagementMapper.deleteAgentSkillPresent(dbParam);
		}

		Map<String,Object> dbParam = new HashMap<>();
		dbParam.put("presentList", presentList);

		logger.info("##{}", dbParam);

		if(!presentList.isEmpty()) {
			dbInsertCnt = routingSkillManagementMapper.insertAgentSkillPresent(dbParam);
		}

		result.put("insertRowCnt", dbInsertCnt);
		result.put("deleteRowCnt", dbDeleteCnt);

		return result;
	}

}
