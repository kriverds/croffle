package com.soluvis.croffle.v1.lgup.mapper;

import java.util.List;
import java.util.Map;

public interface RecordingMapper {

	public List<Map<String,Object>> ifSTA3010(Map<String,Object> params);
	public List<Map<String,Object>> ifSTA3020(Map<String,Object> params);

}
