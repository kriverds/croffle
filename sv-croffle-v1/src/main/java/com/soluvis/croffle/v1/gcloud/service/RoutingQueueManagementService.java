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
public class RoutingQueueManagementService {

	@Autowired
	GCConnector gcconnector;

	private final Logger logger = LoggerFactory.getLogger(RoutingQueueManagementService.class);

	public JSONObject getUserQueues(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < userList.length(); i++) {
			JSONObject user = userList.getJSONObject(i);
			String userId = user.getString("id");
			JSONObject cJO = gcconnector.getUserQueues(userId);
			result.put(userId, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject getRoutingQueues(Map<String,Object> param) throws Exception {
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getRoutingQueues();
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject getRoutingQueueMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray queueList = jParam.getJSONArray("queueList");

		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < queueList.length(); i++) {
			JSONObject queue = queueList.getJSONObject(i);
			String queueId = queue.getString("id");
			JSONObject cJO = gcconnector.getRoutingQueueMembers(queueId);
			result.put(queueId, cJO);
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject postRoutingQueueMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		JSONArray queueList = jParam.getJSONArray("queueList");

		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < queueList.length(); i++) {
			JSONObject queue = queueList.getJSONObject(i);
			String queueId = queue.getString("id");
			gcconnector.postRoutingQueueMembers(queueId, userList, false);
			result.put(queueId, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject deleteRoutingQueueMember(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		JSONArray queueList = jParam.getJSONArray("queueList");

		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = new JSONObject();
		for (int i = 0; i < queueList.length(); i++) {
			JSONObject queue = queueList.getJSONObject(i);
			String queueId = queue.getString("id");
			gcconnector.postRoutingQueueMembers(queueId, userList, true);
			result.put(queueId, "Y");
		}
		GCConnector.close(rUUID);

		return result;
	}
}
