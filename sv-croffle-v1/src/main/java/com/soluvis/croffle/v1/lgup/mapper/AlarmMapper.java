package com.soluvis.croffle.v1.lgup.mapper;

import java.util.List;
import java.util.Map;

public interface AlarmMapper {

	public List<Map<String,Object>> getQueuGroupAlarmList();
	public List<Map<String,Object>> getQueueAlarmList();
}
