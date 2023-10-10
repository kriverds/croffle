package com.soluvis.croffle.v1.sample.mapper;

import java.util.List;
import java.util.Map;

public interface SampleMapper {

	public List<Map<String,Object>> selectSample(Map<String,Object> params);

	public int insertSample(Map<String,Object> params);

}
