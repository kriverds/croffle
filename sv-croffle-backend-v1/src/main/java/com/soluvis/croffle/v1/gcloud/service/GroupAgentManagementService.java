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
public class GroupAgentManagementService {

	@Autowired
	GCConnector gcconnector;

	private final Logger logger = LoggerFactory.getLogger(GroupAgentManagementService.class);

	public JSONObject getGroups() throws Exception {
		UUID rUUID = UUID.randomUUID();
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getGroups();
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject postGroups(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray groupList = jParam.getJSONArray("groupList");

		UUID rUUID = UUID.randomUUID();
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

	public JSONObject putGroup(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray groupList = jParam.getJSONArray("groupList");

		UUID rUUID = UUID.randomUUID();
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

	public JSONObject deleteGroup(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		logger.info("{}", groupList);

		UUID rUUID = UUID.randomUUID();
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

	public JSONObject getGroupMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		logger.info("{}", groupList);

		UUID rUUID = UUID.randomUUID();
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

	public JSONObject postGroupMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = UUID.randomUUID();
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

	public JSONObject deleteGroupMemebers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);

		JSONArray groupList = jParam.getJSONArray("groupList");
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = UUID.randomUUID();
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
