package com.soluvis.croffle.v1.gcloud.service;

import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mypurecloud.sdk.v2.model.User;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.util.CommUtil;

/**
 * 클래스 설명	: GCloud 유저 매니징 서비스
 * @Class Name 	: AgentManagementService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class AgentManagementService {

	@Autowired
	GCConnector gcconnector;

	private final Logger logger = LoggerFactory.getLogger(AgentManagementService.class);

	ObjectMapper om = new ObjectMapper();

	/**
	 * 메서드 설명	: 상담사 조회/생성/수정/삭제
	 * @Method Name : getAvailableUserList/postUsers/patchUser/deleteUser
	 * @date   		: 2023. 12. 18.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	// 조회
	public JSONObject getAvailableUser(Map<String,Object> param) throws Exception {
		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		User result = gcconnector.getAvailableUser(param.get("userId").toString()); // GCloud 조회
		GCConnector.close(rUUID);
		return new JSONObject(om.writeValueAsString(result));
	}
	public JSONObject getAvailableUserList(Map<String,Object> param) throws Exception {
		UUID rUUID = CommUtil.getAttrUUID(param);

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

		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String name = CommUtil.getJString(user, "name");
			String email = CommUtil.getJString(user, "email");
			String department = CommUtil.getJString(user, "department");
			String title = CommUtil.getJString(user, "title");
			String divisionId = CommUtil.getJString(user, "divisionId");
			JSONObject cJO = gcconnector.postUsers(name, email, department, title, divisionId); // GCloud 생성
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

		UUID rUUID = CommUtil.getAttrUUID(param);

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

		UUID rUUID = CommUtil.getAttrUUID(param);

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
}
