package com.soluvis.croffle.v1.lgup.mapper;

import java.util.List;
import java.util.Map;

public interface MiniWallBoardMapper {

	public List<Map<String,Object>> getAgentStat();
	public List<Map<String,Object>> getSkillGroupList();
	public List<Map<String,Object>> getQueueGroupList();

	public List<Map<String,Object>> getPrivateSkillList(Map<String,Object> param);
	public List<Map<String,Object>> getPrivateQueueList(Map<String,Object> param);
}
