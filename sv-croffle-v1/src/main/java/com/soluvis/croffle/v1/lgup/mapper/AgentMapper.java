package com.soluvis.croffle.v1.lgup.mapper;

import java.util.List;
import java.util.Map;

public interface AgentMapper {

	public List<Map<String,Object>> selectModifiedAgent();

	public int updateHiredAgent(Map<String,Object> param);

	public int updateRetiredAgent(Map<String,Object> param);
}
