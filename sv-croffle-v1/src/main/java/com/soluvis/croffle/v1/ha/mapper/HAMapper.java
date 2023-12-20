package com.soluvis.croffle.v1.ha.mapper;

import java.util.List;
import java.util.Map;

public interface HAMapper {

	public List<Map<String,Object>> initSelect();
	public int initInsert(Map<String,Object> param);

	public List<Map<String,Object>> checkTimeout(Map<String,Object> param);

	public int updateHA(Map<String,Object> param);
}
