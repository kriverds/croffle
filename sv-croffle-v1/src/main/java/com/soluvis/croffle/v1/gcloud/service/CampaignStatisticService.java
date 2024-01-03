package com.soluvis.croffle.v1.gcloud.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.soluvis.croffle.v1.util.CommUtil;
import com.soluvis.croffle.v1.util.GVal;

/**
 * 클래스 설명	: GCloud 캠페인 통계 서비스
 * @Class Name 	: CampaignStatisticService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class CampaignStatisticService {

	@Autowired
	GCConnector gcconnector;

	@Autowired
	CampaignStatisticMapper campaignStatisticMapper;

	private final Logger logger = LoggerFactory.getLogger(CampaignStatisticService.class);

	public JSONObject getOutboundCampaigns(Map<String,Object> param) throws Exception {
		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.getOutboundCampaigns();
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject getOutboundCampaign(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);
		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		String campaignId = jParam.getString("campaignId");
		JSONObject result = gcconnector.getOutboundCampaign(campaignId);
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject getOutboundCampaignStats(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		String campaignId = jParam.getString("campaignId");
		JSONObject result = gcconnector.getOutboundCampaignStats(campaignId);
		GCConnector.close(rUUID);

		return result;
	}

	public JSONObject getOutboundCampaignProgress(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = CommUtil.getAttrUUID(param);

		GCConnector.connect(rUUID);
		String campaignId = jParam.getString("campaignId");
		JSONObject result = gcconnector.getOutboundCampaignProgress(campaignId);
		GCConnector.close(rUUID);

		return result;
	}

	/**
	 * 메서드 설명	: 캠페인 별 15분 집계
	 * @Method Name : campaignAggregateQuery
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	public JSONObject campaignAggregateQuery(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = CommUtil.getAttrUUID(param);
		String startDate = jParam.getString("startDate");
		if(Long.parseLong(startDate.substring(startDate.length()-2, startDate.length()))%15 != 0) {
			throw new ParseException();
		}
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.campaignAggregateQuery(startDate);
		GCConnector.close(rUUID);
		List<Map<String,Object>> iList = new ArrayList<>();
		Iterator<String> it = result.keys();
		while(it.hasNext()) {
			String campaignId = it.next();
			Map<String,Object> iMap = new HashMap<>();
			iMap.put("campaignId", campaignId);

			JSONObject aggregates = result.getJSONObject(campaignId);
			JSONArray results = aggregates.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				JSONObject cResult = results.getJSONObject(i);
				JSONObject group = cResult.getJSONObject("group");
				String mediaType = CommUtil.getJString(group, "mediaType");
				if("voice".equals(mediaType)) {


					JSONArray datas = cResult.getJSONArray("data");

					for (int j = 0; j < datas.length(); j++) {
						JSONObject data = datas.getJSONObject(j);

						JSONArray metrics = data.getJSONArray("metrics");

						for (int k = 0; k < metrics.length(); k++) {
							JSONObject metric = metrics.getJSONObject(k);

							String metricName = CommUtil.getJString(metric, "metric");
							JSONObject stats = metric.getJSONObject("stats");

							if("nOutboundAttempted".equals(metricName) || "nConnected".equals(metricName) || "nOutboundConnected".equals(metricName)
									|| "nOffered".equals(metricName) || "nOutboundAbandoned".equals(metricName)) {
								iMap.put(metricName, CommUtil.getJInt(stats, "count"));
							}
						}
					}
				}
			}
			if(iMap.get("nOutboundAttempted") == null) {
				iMap.put("nOutboundAttempted", 0);
			}
			if(iMap.get("nConnected") == null) {
				iMap.put("nConnected", 0);
			}
			if(iMap.get("nOutboundConnected") == null) {
				iMap.put("nOutboundConnected", 0);
			}
			if(iMap.get("nOffered") == null) {
				iMap.put("nOffered", 0);
			}
			if(iMap.get("nOutboundAbandoned") == null) {
				iMap.put("nOutboundAbandoned", 0);
			}
			iMap.put("progressYMD", startDate.substring(0,8));
			iMap.put("progressHM", startDate.substring(8,12));
			iList.add(iMap);
		}

		JSONObject rResult = new JSONObject();
		rResult.put("total", iList.size());
		param.put("progressYMD", startDate.substring(0,8));
		param.put("campaignResult", iList);
		logger.info("{}", param);

		int iResult = 0;
		if(result.length() > 0) {
			iResult = campaignStatisticMapper.insert15MinData(param);
		}
		rResult.put("progress", iResult);
		return rResult;
	}

	/**
	 * 메서드 설명	: 캠페인 RawData
	 * @Method Name : getCampaignConversationQuery
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	public JSONObject getCampaignConversationQuery(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = CommUtil.getAttrUUID(param);
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
			String conversationId = CommUtil.getJString(conversation, "conversationId");
			long conversationEnd = CommUtil.getJLong(conversation, "conversationEnd");
			if(conversationEnd == 0L) {
				GVal.getProgressConversationList().add(conversationId);
				continue;
			}
			iMap.put("conversationId", conversationId);

			JSONArray participants = conversation.getJSONArray("participants");
			for (int j = 0; j < participants.length(); j++) {
				JSONObject participant = participants.getJSONObject(j);

				String purpose = CommUtil.getJString(participant, "purpose");

				if("CUSTOMER".equals(purpose)) {
					JSONArray sessions = participant.getJSONArray("sessions");
					for (int k = 0; k < sessions.length();k++) {
						JSONObject session = sessions.getJSONObject(k);

						JSONArray segments = session.getJSONArray("segments");
						long minStart = 9999999999999L;
						long maxEnd = 0L;
						for (int l = 0; l < segments.length();l++) {
							JSONObject segment = segments.getJSONObject(l);

							long segmentStart = CommUtil.getJLong(segment, "segmentStart");
							long segmentEnd = CommUtil.getJLong(segment, "segmentEnd");

							if(segmentStart < minStart) minStart = segmentStart;
							if(segmentEnd > maxEnd) maxEnd = segmentEnd;

							String segmentType = CommUtil.getJString(segment, "segmentType");
							if("DIALING".equals(segmentType)) {
								iMap.put("tDialing", segmentEnd-segmentStart);
								String disconnectType = CommUtil.getJString(segment, "disconnectType");
								iMap.put("disconnectType", disconnectType);
							}
						}
						if(iMap.get("tDialing") != null) {
							// START_YMD START_HMSS END_YMD END_HMSS T_DURATION
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
							Calendar scal = Calendar.getInstance();
							Calendar ecal = Calendar.getInstance();
							scal.setTimeInMillis(minStart);
							ecal.setTimeInMillis(maxEnd);
							Date sdate = scal.getTime();
							Date edate = ecal.getTime();
							String sStr = sdf.format(sdate);
							String eStr = sdf.format(edate);

							iMap.put("startYMD", sStr.substring(0,8));
							iMap.put("startHMSS", sStr.substring(8,17));
							iMap.put("endYMD", eStr.substring(0,8));
							iMap.put("endHMSS", eStr.substring(8,17));
							iMap.put("tDuration", maxEnd-minStart);

							// CAMPAIGN_ID / CONTACTLIST_ID / ANI / DNIS
							String campaignId = CommUtil.getJString(session, "outboundCampaignId");
							String contactListId = CommUtil.getJString(session, "outboundContactListId");
							String ani = CommUtil.getJString(session, "ani");
							String dnis = CommUtil.getJString(session, "dnis");

							iMap.put("campaignId", campaignId);
							iMap.put("contactlistId", contactListId);
							iMap.put("ani", ani);
							iMap.put("dnis", dnis);

						}
					}

				} else if("AGENT".equals(purpose)) {
					JSONArray sessions = participant.getJSONArray("sessions");
					for (int k = 0; k < sessions.length();k++) {
						JSONObject session = sessions.getJSONObject(k);

						JSONArray segments = session.getJSONArray("segments");
						for (int l = 0; l < segments.length();l++) {
							JSONObject segment = segments.getJSONObject(l);
							logger.info("{}", segment);

							long segmentStart = CommUtil.getJLong(segment, "segmentStart");
							long segmentEnd = CommUtil.getJLong(segment, "segmentEnd");

							String segmentType = segment.getString("segmentType");
							if("ALERT".equals(segmentType)) {
								iMap.put("tRinging", segmentEnd-segmentStart);
							}
							if("INTERACT".equals(segmentType)) {
								iMap.put("tAgent", segmentEnd-segmentStart);
								String disconnectType = CommUtil.getJString(segment, "disconnectType");
								String selectedAgentId = CommUtil.getJString(session, "selectedAgentId");
								iMap.put("disconnectType", disconnectType);
								iMap.put("agentId", selectedAgentId);

							}
						}
					}
				}
			}
			if (iMap.get("tRinging") == null) {
				iMap.put("tRinging", 0);
			}
			if (iMap.get("tAgent") == null) {
				iMap.put("tAgent", 0);
			}
			if (iMap.get("disconnectType") == null) {
				iMap.put("disconnectType", "");
			}
			if (iMap.get("agentId") == null) {
				iMap.put("agentId", "");
			}
			if (iMap.get("tDialing") != null) {
				iList.add(iMap);
			}
		}

		JSONObject rResult = new JSONObject();
		rResult.put("total", iList.size());
		param.put("campaignResult", iList);
		logger.info("{}", param);

		int iResult = 0;
		if(conversations.length() > 0) {
			iResult = campaignStatisticMapper.insertRawData(param);
		}
		rResult.put("progress", iResult);
		return rResult;
	}
	
	public int deleteDayData(Map<String,Object> param) throws Exception {
		return campaignStatisticMapper.deleteDayData(param);
	}

}
