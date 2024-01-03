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
import com.soluvis.croffle.v1.util.CommUtil;

/**
 * 클래스 설명	: GCloud 상담그룹 매니징 서비스
 * @Class Name 	: GroupAgentManagementService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class GroupAgentManagementService {

	@Autowired
	GCConnector gcconnector;

	private final Logger logger = LoggerFactory.getLogger(GroupAgentManagementService.class);


	/**
	 * 메서드 설명	: 상담그룹 조회/생성/수정/삭제
	 * @Method Name : getGroups/postGroups/putGroup/deleteGroup
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
	//조회
	public JSONObject getGroups(Map<String,Object> param) throws Exception {
		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getGroups();
		GCConnector.close(rUUID);

		return result;
	}
	//생성
	public JSONObject postGroups(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray groupList = jParam.getJSONArray("groupList");

		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < groupList.length(); i++) {
			JSONObject group = groupList.getJSONObject(i);
			String name = group.getString("name");
			JSONObject cJO = gcconnector.postGroups(name);
			result.put(cJO.get("id").toString(), cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}
	//수정
	public JSONObject putGroup(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray groupList = jParam.getJSONArray("groupList");

		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < groupList.length(); i++) {
			JSONObject group = groupList.getJSONObject(i);
			String groupId = group.getString("id");
			String name = group.getString("name");
			JSONObject cJO = gcconnector.putGroup(groupId, name);
			result.put(groupId, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}
	//삭제
	public JSONObject deleteGroup(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		logger.info("{}", groupList);

		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < groupList.length(); i++) {
			JSONObject group = groupList.getJSONObject(i);
			String groupId = group.getString("id");
			gcconnector.deleteGroup(groupId);
			result.put(groupId, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}

	/**
	 * 메서드 설명	: 상담그룹 멤버 조회/추가/삭제
	 * @Method Name : getGroupMembers/postGroupMembers/deleteGroupMemebers
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
	//조회
	public JSONObject getGroupMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		logger.info("{}", groupList);

		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < groupList.length(); i++) {
			JSONObject group = groupList.getJSONObject(i);
			String groupId = group.getString("id");
			JSONObject cJO = gcconnector.getGroupMembers(groupId);
			result.put(cJO.get("id").toString(), cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}
	//추가
	public JSONObject postGroupMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < groupList.length(); i++) {
			JSONObject group = groupList.getJSONObject(i);
			String groupId = group.getString("id");

			gcconnector.postGroupMembers(groupId, userList);
			result.put(groupId, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}
	//삭제
	public JSONObject deleteGroupMemebers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < groupList.length(); i++) {
			JSONObject group = groupList.getJSONObject(i);
			String groupId = group.getString("id");

			gcconnector.deleteGroupMemebers(groupId, userList);
			result.put(groupId, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}
}
