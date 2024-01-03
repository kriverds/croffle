package com.soluvis.croffle.v1.gcloud.mapper;

import java.util.Map;

public interface AggregateMapper {

	public int insertAgentStatic(Map<String,Object> params);
	public int insertQueueStatic(Map<String,Object> params);
	public int insertSkillStatic(Map<String,Object> params);
	
	public int deleteAgentDayData(Map<String,Object> params);
	public int deleteQueueDayData(Map<String,Object> params);
	public int deleteSkillDayData(Map<String,Object> params);
}
