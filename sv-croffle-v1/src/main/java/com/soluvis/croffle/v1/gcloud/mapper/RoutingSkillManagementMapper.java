package com.soluvis.croffle.v1.gcloud.mapper;

import java.util.List;
import java.util.Map;

public interface RoutingSkillManagementMapper {

	public int deleteAllAgentSkillPresent();
	public int deleteAgentSkillPresent(Map<String,Object> params);
	public int insertAgentSkillPresent(Map<String,Object> params);

	public int insertSkillManagementHistory(Map<String,Object> params);

	public List<Map<String,Object>> selectUser(Map<String,Object> params);
	public List<Map<String,Object>> selectSkillByCart(Map<String,Object> params);
}
