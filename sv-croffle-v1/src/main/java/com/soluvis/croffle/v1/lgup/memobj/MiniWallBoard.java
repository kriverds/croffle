package com.soluvis.croffle.v1.lgup.memobj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class MiniWallBoard {
	private static JSONObject agentStat = new JSONObject();
	private static JSONObject queueStat = new JSONObject();
	private static JSONObject skillStat = new JSONObject();

	private MiniWallBoard() {}

	public static void setAgentStat(List<Map<String,Object>> params) {
		if(params == null) params = new ArrayList<>();
		MiniWallBoard.agentStat = new JSONObject();
//		for (Map<String, Object> param : params) {
//			JSONObject jparam = new JSONObject(param);
//			String empId =  jparam.getString("emp_id");
//			MiniWallBoard.agentStat.put(empId, jparam);
//		}
		JSONObject jparam = new JSONObject();
		jparam.put("empId", "U018507");
		jparam.put("inboundCnt", 10);
		jparam.put("inboundTime", 100);
		jparam.put("outboundCnt", 5);
		jparam.put("outboundTime", 200);
		jparam.put("loginTime", 8000);
		jparam.put("readyTime", 1000);
		jparam.put("acwTime", 2000);
		jparam.put("notReadyTime", 3000);
		jparam.put("stateTime", 588);
		jparam.put("workMode", 50);
		MiniWallBoard.agentStat.put("U018507", jparam);
	}
	public static JSONObject getAgentStat(String empId) {
		return MiniWallBoard.agentStat.getJSONObject(empId);
	}
	
	
	public static void setQueueStat(List<Map<String,Object>> params) {
		if(params == null) params = new ArrayList<>();
		MiniWallBoard.queueStat = new JSONObject();
//		for (Map<String, Object> param : params) {
//			JSONObject jparam = new JSONObject(param);
//			String queueId =  jparam.getString("queueId");
//			MiniWallBoard.queueStat.put(queueId, jparam);
//		}
		JSONObject jparam = new JSONObject();
		jparam.put("queueId", 1);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("1", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 2);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("2", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 3);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("3", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 4);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("4", jparam);
		
		jparam = new JSONObject();
		jparam.put("queueId", 21);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("21", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 22);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("22", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 23);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("23", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 24);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("24", jparam);
		
		jparam = new JSONObject();
		jparam.put("queueId", 31);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("31", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 32);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("32", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 33);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("33", jparam);
		jparam = new JSONObject();
		jparam.put("queueId", 34);
		jparam.put("inboundCnt", 1000);
		jparam.put("answerCnt", 899);
		jparam.put("waitingCnt", 20);
		MiniWallBoard.queueStat.put("34", jparam);
	}
	public static JSONObject getQueueStat(String queueGroupId) {
		return MiniWallBoard.queueStat.getJSONObject(queueGroupId);
	}
	
	
	public static void setSkillStat(List<Map<String,Object>> params) {
		if(params == null) params = new ArrayList<>();
		MiniWallBoard.skillStat = new JSONObject();
//		for (Map<String, Object> param : params) {
//			JSONObject jparam = new JSONObject(param);
//			String skillId =  jparam.getString("skillId");
//			MiniWallBoard.skillStat.put(skillId, jparam);
//		}
		JSONObject jparam = new JSONObject();
		jparam.put("skillId", 1);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("1", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 2);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("2", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 3);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("3", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 4);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("4", jparam);
		
		jparam = new JSONObject();
		jparam.put("skillId", 21);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("21", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 22);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("22", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 23);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("23", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 24);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("24", jparam);
		
		jparam = new JSONObject();
		jparam.put("skillId", 31);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("31", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 32);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("32", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 33);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("33", jparam);
		jparam = new JSONObject();
		jparam.put("skillId", 34);
		jparam.put("readyCnt", 13);
		MiniWallBoard.skillStat.put("34", jparam);
	}
	public static JSONObject getSkillStat(String skillGroupId) {
		return MiniWallBoard.skillStat.getJSONObject(skillGroupId);
	}
}
