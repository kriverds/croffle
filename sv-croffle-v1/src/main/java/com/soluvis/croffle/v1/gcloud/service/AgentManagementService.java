package com.soluvis.croffle.v1.gcloud.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.gcloud.mapper.AgentManagementMapper;

@Service
public class AgentManagementService {

	@Autowired
	GCConnector gcconnector;

	@Autowired
	AgentManagementMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(AgentManagementService.class);


	public JSONObject getAvailableUserList(Map<String,Object> param) throws Exception {
		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getAvailableUserList();
		GCConnector.close(rUUID);

		return result;
	}


	public JSONObject postUsers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String name = user.getString("name");
			String email = user.getString("email");
			String department = user.getString("department");
			JSONObject cJO = gcconnector.postUsers(name, email, department);
			result.put(cJO.get("id").toString(), cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject patchUser(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String id = user.getString("userId");
			String department = user.getString("department");
			JSONObject cJO = gcconnector.patchUser(id, department);
			result.put(id, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject deleteUser(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String id = user.getString("id");
			gcconnector.deleteUser(id);
			result.put(id, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}

	public List<Map<String,Object>> selectDoNotCreatedAgent() {

		return mapper.selectDoNotCreatedAgent();
	}
}
