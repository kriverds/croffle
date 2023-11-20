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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.gcloud.mapper.RoutingSkillManagementMapper;

@Service
public class RoutingSkillManagementService {

	@Autowired
	GCConnector gcconnector;
	@Autowired
	RoutingSkillManagementMapper routingSkillManagementMapper;

	private final Logger logger = LoggerFactory.getLogger(RoutingSkillManagementService.class);

	public JSONObject getUserRoutingskills(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = (UUID) param.get("rUUID");
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

	public JSONObject getRoutingSkillList() throws Exception {
		UUID rUUID = UUID.randomUUID();
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getRoutingSkillList();
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject patchUserRoutingskillsBulk(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		JSONArray skillList = jParam.getJSONArray("skillList");

		UUID rUUID = UUID.randomUUID();
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = gcconnector.patchUserRoutingskillsBulk(userId, skillList);
			result.put(userId, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject deleteUserRoutingskill(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		JSONArray skillList = jParam.getJSONArray("skillList");

		UUID rUUID = UUID.randomUUID();
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = new JSONObject();
			int total = 0;
			for (int j = 0; j < skillList.length(); j++) {
				JSONObject skill = skillList.getJSONObject(j);
				String skillId = skill.getString("id");
				gcconnector.deleteUserRoutingskill(userId, skillId);
				total++;
			}
			cJO.put("total", total);
			result.put(userId, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	@Transactional(transactionManager = "GCloudTransactionManager")
	public JSONObject changeSkillBySkillCart(Map<String, Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		ObjectMapper om = new ObjectMapper();
		TypeReference<List<Map<String, Object>>> typeReference = new TypeReference<>() {
		};

		Map<String, Object> historyParamMap = new HashMap<>();

		List<Map<String, Object>> userJAtoMap = null;
		Map<String, Object> userParamMap = new HashMap<>();
		List<Map<String, Object>> userAllInfoList = null;

		Map<String, Object> skillListByCartParam = new HashMap<>();
		List<Map<String, Object>> skillListByCart = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		String jobDate = sdf.format(new Date());
		String jobCategory = "카트";
		int targetCart = (Integer) param.get("skillCart");

		JSONArray userJA = jParam.getJSONArray("userList");
		userJAtoMap = om.readValue(userJA.toString(), typeReference);
		userParamMap.put("userList", userJAtoMap);
		userAllInfoList = routingSkillManagementMapper.selectUser(userParamMap);

		skillListByCartParam.put("CART_ID", targetCart);
		skillListByCart = routingSkillManagementMapper.selectSkillByCart(skillListByCartParam);
		List<String> skillList = new ArrayList<>();
		List<Double> levelList = new ArrayList<>();
		for (Map<String, Object> map : skillListByCart) {
			skillList.add(map.get("skill_id").toString());
			levelList.add(Double.parseDouble(map.get("skill_level").toString()));
		}

		UUID rUUID = UUID.randomUUID();
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();

		for (int i = 0; i < userAllInfoList.size(); i++) {
			Map<String, Object> userAllInfoMap = userAllInfoList.get(i);
			String userId = userAllInfoMap.get("gcloud_uuid").toString();
			JSONObject cJO = gcconnector.putUserRoutingskillsBulk(userId, skillListByCart);
			result.put(userId, cJO);
		}

		replaceAgentSkillPresent(param);
		GCConnector.close(rUUID);

		historyParamMap.put("JOB_DATE", jobDate);
		historyParamMap.put("JOB_CATEGORY", jobCategory);
		historyParamMap.put("TARGET_CART", targetCart);
		historyParamMap.put("skillList", skillList);
		historyParamMap.put("levelList", levelList);
		historyParamMap.put("operator_id", jParam.get("operator"));
		historyParamMap.put("userList", userAllInfoList);
		saveSkillManagementHistory(historyParamMap);

		return result;
	}
	
	public int saveSkillManagementHistory(Map<String,Object> param) throws Exception {
		return routingSkillManagementMapper.insertSkillManagementHistory(param);
	}
	
	@Transactional(transactionManager = "GCloudTransactionManager")
	public JSONObject replaceAgentSkillPresent(Map<String,Object> param) throws Exception {
		JSONObject result = new JSONObject();
		List<Map<String,Object>> presentList = new ArrayList<>();
		List<Map<String,Object>> deleteUserList = new ArrayList<>();
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		int paramCnt = userList.length();
		
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		if(paramCnt==0) {
			userList = gcconnector.getAvailableUserList().getJSONArray("list");
		}
		
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			Map<String,Object> deleteUserMap = new HashMap<>();
			
			String userUUID = user.getString("id");
			deleteUserMap.put("USER_ID", userUUID);
			
			JSONArray userSkillList = gcconnector.getUserRoutingskills(user.getString("id")).getJSONArray("list");
			for (int j = 0; j < userSkillList.length(); j++) {
				Map<String,Object> presentMap = new HashMap<>();
				JSONObject userSkill = userSkillList.getJSONObject(j);
				presentMap.put("USER_ID", userUUID);
				presentMap.put("SKILL_ID", userSkill.getString("id"));
				presentMap.put("SKILL_LEVEL", userSkill.get("proficiency"));
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
		
		dbInsertCnt = routingSkillManagementMapper.insertAgentSkillPresent(dbParam);
		
		result.put("insertRowCnt", dbInsertCnt);
		result.put("deleteRowCnt", dbDeleteCnt);

		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
