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
 * 클래스 설명	: GCloud 큐 매니징 서비스
 * @Class Name 	: RoutingQueueManagementService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class RoutingQueueManagementService {

	@Autowired
	GCConnector gcconnector;

	private final Logger logger = LoggerFactory.getLogger(RoutingQueueManagementService.class);

	/**
	 * 메서드 설명	: 상담사가 보유 한 큐 조회
	 * @Method Name : getUserQueues
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
	public JSONObject getUserQueues(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");

		UUID rUUID = CommUtil.getAttrUUID(param);
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

	/**
	 * 메서드 설명	: 큐 목록 조회
	 * @Method Name : getRoutingQueues
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
	public JSONObject getRoutingQueues(Map<String,Object> param) throws Exception {
		UUID rUUID = CommUtil.getAttrUUID(param);
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getRoutingQueues();
		GCConnector.close(rUUID);

		return result;
	}

	/**
	 * 메서드 설명	: 큐 멤버 조회/추가/삭제
	 * @Method Name : getRoutingQueueMembers/postRoutingQueueMembers/deleteRoutingQueueMember
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
	public JSONObject getRoutingQueueMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray queueList = jParam.getJSONArray("queueList");

		UUID rUUID = CommUtil.getAttrUUID(param);
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
	//추가
	public JSONObject postRoutingQueueMembers(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		JSONArray queueList = jParam.getJSONArray("queueList");

		UUID rUUID = CommUtil.getAttrUUID(param);
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
	//삭제
	public JSONObject deleteRoutingQueueMember(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		logger.info("{}", jParam);
		JSONArray userList = jParam.getJSONArray("userList");
		JSONArray queueList = jParam.getJSONArray("queueList");

		UUID rUUID = CommUtil.getAttrUUID(param);
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
