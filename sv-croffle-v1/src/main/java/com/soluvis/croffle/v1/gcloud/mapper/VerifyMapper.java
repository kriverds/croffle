package com.soluvis.croffle.v1.gcloud.mapper;

import java.util.Map;

public interface VerifyMapper {

	public int insertAgentStatic(Map<String,Object> params);
	public int insertQueueStatic(Map<String,Object> params);
	public int insertSkillStatic(Map<String,Object> params);
}
