package com.soluvis.croffle.v1.gcloud.mapper;

import java.util.List;
import java.util.Map;

public interface GCloudMapper {

	public List<Map<String,Object>> selectSample(Map<String,Object> params);

	public int insertSample(Map<String,Object> params);

}
