package com.soluvis.croffle.v1.gcloud.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.gcloud.mapper.CampaignStatisticMapper;

@Service
public class CampaignStatisticService {

	@Autowired
	GCConnector gcconnector;
	
	@Autowired
	CampaignStatisticMapper campaignStatisticMapper;

	private final Logger logger = LoggerFactory.getLogger(CampaignStatisticService.class);

	public JSONObject getOutboundCampaigns(Map<String,Object> param) throws Exception {
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getOutboundCampaigns();
		GCConnector.close(rUUID);

		return result;
	}
	
	public JSONObject getOutboundCampaign(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		String campaignId = jParam.getString("campaignId");
		JSONObject result = gcconnector.getOutboundCampaign(campaignId);
		GCConnector.close(rUUID);

		return result;
	}
	
	public JSONObject getOutboundCampaignStats(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		String campaignId = jParam.getString("campaignId");
		JSONObject result = gcconnector.getOutboundCampaignStats(campaignId);
		GCConnector.close(rUUID);

		return result;
	}
	
	public JSONObject getOutboundCampaignProgress(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		String campaignId = jParam.getString("campaignId");
		JSONObject result = gcconnector.getOutboundCampaignProgress(campaignId);
		GCConnector.close(rUUID);

		return result;
	}
	
	public JSONObject getCampaignAggregateQuery(Map<String,Object> param) throws Exception {
//		JSONObject jParam = new JSONObject(param);
		
		UUID rUUID = (UUID) param.get("rUUID");
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.campaignAggregateQuery();
		GCConnector.close(rUUID);

		return result;
	}
	
	public JSONObject getCampaignConversationQuery(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		JSONObject rResult = new JSONObject();
		
		UUID rUUID = (UUID) param.get("rUUID");
		String startDate = jParam.getString("startDate");
		if(Long.parseLong(startDate.substring(startDate.length()-2, startDate.length()))%15 != 0) {
			throw new ParseException();
		}
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.campaignConversationQuery(startDate);
		logger.info("{}", result);

		GCConnector.close(rUUID);
		
		
		JSONArray conversations = result.getJSONArray("conversations");
		List<Map<String,Object>> iList = new ArrayList<>();
		// conversations > participants > sessions > segments
		for (int i = 0; i < conversations.length(); i++) {
			Map<String,Object> iMap = new HashMap<>();
			JSONObject conversation = conversations.getJSONObject(i);
			
			JSONArray participants = conversation.getJSONArray("participants");
			for (int j = 0; j < participants.length(); j++) {
				JSONObject participant = participants.getJSONObject(j);
				
				String purpose = participant.getString("purpose");
				
				if("CUSTOMER".equals(purpose)) {
					iMap.put("PARTICIPANT_ID", participant.get("participantId"));
					
					JSONArray sessions = participant.getJSONArray("sessions");
					for (int k = 0; k < sessions.length();k++) {
						JSONObject session = sessions.getJSONObject(k);
						
						iMap.put("CAMPAIGN_ID", session.get("outboundCampaignId"));
						iMap.put("CONTACTLIST_ID", session.get("outboundContactListId"));
						iMap.put("ANI", session.get("ani"));
						iMap.put("DNIS", session.get("dnis"));
						
						JSONArray segments = session.getJSONArray("segments");
						long minStart = 9999999999999L;
						long maxEnd = 0L; 
						for (int l = 0; l < segments.length();l++) {
							JSONObject segment = segments.getJSONObject(l);
							
							Object disconnectType = segment.get("disconnectType");
							if(disconnectType != null && !disconnectType.toString().equals("null")) {

								iMap.put("DISCONNECT_TYPE", segment.getString("disconnectType"));
							}
							
							long segmentStart = segment.getLong("segmentStart");
							long segmentEnd = segment.getLong("segmentEnd");
							
							if(segmentStart < minStart) minStart = segmentStart;
							if(segmentEnd > maxEnd) maxEnd = segmentEnd;
							
							String segmentType = segment.getString("segmentType");
							if("DIALING".equals(segmentType)) iMap.put("T_DIALING", segmentEnd-segmentStart);
							if("INTERACT".equals(segmentType)) iMap.put("T_INTERACT", segmentEnd-segmentStart);
							if("AGENT".equals(segmentType)) iMap.put("T_AGENT", segmentEnd-segmentStart);
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						Calendar scal = Calendar.getInstance();
						Calendar ecal = Calendar.getInstance();
						scal.setTimeInMillis(minStart);
						ecal.setTimeInMillis(maxEnd);
						Date sdate = scal.getTime();
						Date edate = ecal.getTime();
						String sStr = sdf.format(sdate);
						String eStr = sdf.format(edate);
						
						iMap.put("START_DATE", sStr.substring(0,8));
						iMap.put("START_TIME", sStr.substring(8,14));
						iMap.put("END_DATE", eStr.substring(0,8));
						iMap.put("END_TIME", eStr.substring(8,14));
						iMap.put("T_DURATION", maxEnd-minStart);
						
						if(iMap.get("T_DIALING") == null) iMap.put("T_DIALING", 0L);
						if(iMap.get("T_INTERACT") == null) iMap.put("T_INTERACT", 0L);
						if(iMap.get("T_AGENT") == null) iMap.put("T_AGENT", 0L);
						
					}
				}
			}
			iList.add(iMap);
		}
		
		rResult.put("total", iList.size());
		param.put("campaignResult", iList);
		int iResult = campaignStatisticMapper.insertRawData(param);
		rResult.put("progress", iResult);
		return rResult;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
