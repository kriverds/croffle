package com.soluvis.croffle.v1.gcloud.service;

import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

@Service
public class AgentManagementService {

	@Autowired
	GCConnector gcconnector;

	private final Logger logger = LoggerFactory.getLogger(AgentManagementService.class);


	/**
	 * 메서드 설명	: 상담사 조회/생성/수정/삭제
	 * @Method Name : getAvailableUserList/postUsers/patchUser/deleteUser
	 * @date   		: 2023. 12. 18.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	// 조회
	public JSONObject getAvailableUserList(Map<String,Object> param) throws Exception {
		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getAvailableUserList(); // GCloud 조회
		GCConnector.close(rUUID);

		return result;
	}
	// 생성
	public JSONObject postUsers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) { //## 파라미터 어떻게?
			JSONObject user = userList.getJSONObject(i);
			String name = user.getString("name");
			String email = user.getString("email");
			String department = user.getString("department");
			JSONObject cJO = gcconnector.postUsers(name, email, department); // GCloud 생성
			result.put(cJO.get("id").toString(), cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}
	// 수정
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
			JSONObject cJO = gcconnector.patchUser(id, department); // GCloud 수정
			result.put(id, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}
	// 삭제
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
			gcconnector.deleteUser(id); // GCloud 삭제
			result.put(id, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject getAuthorizationRoles(Map<String,Object> param) throws Exception {
		UUID rUUID = (UUID) param.get("rUUID");

		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getAuthorizationRoles(); // GCloud 조회
		GCConnector.close(rUUID);

		return result;
	}
}
