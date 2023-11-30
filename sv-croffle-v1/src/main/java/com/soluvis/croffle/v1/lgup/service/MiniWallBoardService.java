package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.mapper.MiniWallBoardMapper;
import com.soluvis.croffle.v1.lgup.memobj.MiniWallBoard;

@Service
public class MiniWallBoardService {

	@Autowired
	MiniWallBoardMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(MiniWallBoardService.class);

	public JSONArray getAgentStat(Map<String,Object> param) throws Exception {
		JSONArray result = new JSONArray();
		String empId = param.get("empId").toString();
		String[] arrEmpId = empId.split(",");
		for (int i = 0; i < arrEmpId.length; i++) {
			result.put(MiniWallBoard.getAgentStat(arrEmpId[i]));
		}
		return result;
	}
	public JSONArray getQueueStat(Map<String,Object> param) throws Exception {
		JSONArray result = new JSONArray();
		String queueId = param.get("queueId").toString();
		String[] arrQueueId = queueId.split(",");
		for (int i = 0; i < arrQueueId.length; i++) {
			result.put(MiniWallBoard.getQueueStat(arrQueueId[i]));
		}
		return result;
	}
	public JSONArray getSkillStat(Map<String,Object> param) throws Exception {
		JSONArray result = new JSONArray();
		String skillId = param.get("skillId").toString();
		String[] arrSkillId = skillId.split(",");
		for (int i = 0; i < arrSkillId.length; i++) {
			result.put(MiniWallBoard.getSkillStat(arrSkillId[i]));
		}
		return result;
	}
	public List<Map<String, Object>> getSkillGroupList() throws Exception {
		return mapper.getSkillGroupList();
	}
	public List<Map<String, Object>> getQueueGroupList() throws Exception {
		return mapper.getQueueGroupList();
	}
	public List<Map<String, Object>> getPrivateSkillList(Map<String,Object> param) throws Exception {
		String[] strArr = param.get("skillGroupList").toString().split(",");
		List<Integer> skillGroupList = new ArrayList<>();
		for (int i = 0; i < strArr.length; i++) {
			skillGroupList.add(Integer.parseInt(strArr[i]));
		}
		logger.info("{}", skillGroupList);
		param.put("skillGroupList", skillGroupList);
		return mapper.getPrivateSkillList(param);
	}
	public List<Map<String, Object>> getPrivateQueueList(Map<String,Object> param) throws Exception {
		String[] strArr = param.get("queueGroupList").toString().split(",");
		List<Integer> queueGroupList = new ArrayList<>();
		for (int i = 0; i < strArr.length; i++) {
			queueGroupList.add(Integer.parseInt(strArr[i]));
		}
		logger.info("{}", queueGroupList);

		param.put("queueGroupList", queueGroupList);
		return mapper.getPrivateQueueList(param);
	}

}