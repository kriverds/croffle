package com.soluvis.croffle.v1.scheduler.mapper;

import java.util.List;
import java.util.Map;

public interface SchedulerMapper {

	public List<Map<String,Object>> selectOptionGroup();
	public List<Map<String,Object>> selectOption();

	public int insertOptionGroup(Map<String,Object> params);
	public int insertOption(Map<String,Object> params);

	public int updateOptionGroup(Map<String,Object> params);
	public int updateOption(Map<String,Object> params);

	public int deleteOptionGroup(Map<String,Object> params);
	public int deleteOption(Map<String,Object> params);

}
