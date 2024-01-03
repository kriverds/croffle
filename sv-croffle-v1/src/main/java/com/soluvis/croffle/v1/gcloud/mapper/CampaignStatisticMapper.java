package com.soluvis.croffle.v1.gcloud.mapper;

import java.util.Map;

public interface CampaignStatisticMapper {

	public int insertRawData(Map<String,Object> params);

	public int insert15MinData(Map<String,Object> params);
	
	public int deleteDayData(Map<String,Object> params);
	
	
}
