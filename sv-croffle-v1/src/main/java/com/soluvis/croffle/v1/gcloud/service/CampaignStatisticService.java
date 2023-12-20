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
import com.soluvis.croffle.v1.gcloud.util.CommUtil;
import com.soluvis.croffle.v1.util.GVal;

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
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = (UUID) param.get("rUUID");
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
			iMap.put("CAMPAIGN_ID", campaignId);

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

							if("nOutboundAttempted".equals(metricName)) {
								iMap.put("N_OUTBOUND_ATTEMPTED", CommUtil.getJInt(stats, "count"));
							} else if("nConnected".equals(metricName)) {
								iMap.put("N_CONNECTED", CommUtil.getJInt(stats, "count"));
							} else if("nOutboundConnected".equals(metricName)) {
								iMap.put("N_OUTBOUND_CONNECTED", CommUtil.getJInt(stats, "count"));
							} else if("nOffered".equals(metricName)) {
								iMap.put("N_OFFERED", CommUtil.getJInt(stats, "count"));
							} else if("nOutboundAbandoned".equals(metricName)) {
								iMap.put("N_OUTBOUND_ABANDONED", CommUtil.getJInt(stats, "count"));
							}
						}
					}
				}
			}
			if(iMap.get("N_OUTBOUND_ATTEMPTED") == null) {
				iMap.put("N_OUTBOUND_ATTEMPTED", 0);
			}
			if(iMap.get("N_CONNECTED") == null) {
				iMap.put("N_CONNECTED", 0);
			}
			if(iMap.get("N_OUTBOUND_CONNECTED") == null) {
				iMap.put("N_OUTBOUND_CONNECTED", 0);
			}
			if(iMap.get("N_OFFERED") == null) {
				iMap.put("N_OFFERED", 0);
			}
			if(iMap.get("N_OUTBOUND_ABANDONED") == null) {
				iMap.put("N_OUTBOUND_ABANDONED", 0);
			}
			iMap.put("PROGRESS_YMD", startDate.substring(0,8));
			iMap.put("PROGRESS_HM", startDate.substring(8,12));
			iList.add(iMap);
		}

		JSONObject rResult = new JSONObject();
		rResult.put("total", iList.size());
		param.put("campaignResult", iList);
		logger.info("{}", param);

		int iResult = 0;
		if(result.length() > 0) {
			iResult = campaignStatisticMapper.insert15MinData(param);
		}
		rResult.put("progress", iResult);
		return rResult;
	}

	public JSONObject getCampaignConversationQuery(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

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
			String conversationId = CommUtil.getJString(conversation, "conversationId");
			long conversationEnd = CommUtil.getJLong(conversation, "conversationEnd");
			if(conversationEnd == 0L) {
				GVal.getProgressConversationList().add(conversationId);
				continue;
			}
			iMap.put("CONVERSATION_ID", conversationId);

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
								iMap.put("T_DIALING", segmentEnd-segmentStart);
								String disconnectType = CommUtil.getJString(segment, "disconnectType");
								iMap.put("DISCONNECT_TYPE", disconnectType);
							}
						}
						if(iMap.get("T_DIALING") != null) {
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

							iMap.put("START_YMD", sStr.substring(0,8));
							iMap.put("START_HMSS", sStr.substring(8,17));
							iMap.put("END_YMD", eStr.substring(0,8));
							iMap.put("END_HMSS", eStr.substring(8,17));
							iMap.put("T_DURATION", maxEnd-minStart);

							// CAMPAIGN_ID / CONTACTLIST_ID / ANI / DNIS
							String campaignId = CommUtil.getJString(session, "outboundCampaignId");
							String contactListId = CommUtil.getJString(session, "outboundContactListId");
							String ani = CommUtil.getJString(session, "ani");
							String dnis = CommUtil.getJString(session, "dnis");

							iMap.put("CAMPAIGN_ID", campaignId);
							iMap.put("CONTACTLIST_ID", contactListId);
							iMap.put("ANI", ani);
							iMap.put("DNIS", dnis);

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
								iMap.put("T_RINGING", segmentEnd-segmentStart);
							}
							if("INTERACT".equals(segmentType)) {
								iMap.put("T_AGENT", segmentEnd-segmentStart);
								String disconnectType = CommUtil.getJString(segment, "disconnectType");
								String selectedAgentId = CommUtil.getJString(session, "selectedAgentId");
								iMap.put("DISCONNECT_TYPE", disconnectType);
								iMap.put("AGENT_ID", selectedAgentId);

							}
						}
					}
				}
			}
			if (iMap.get("T_RINGING") == null) {
				iMap.put("T_RINGING", 0);
			}
			if (iMap.get("T_AGENT") == null) {
				iMap.put("T_AGENT", 0);
			}
			if (iMap.get("DISCONNECT_TYPE") == null) {
				iMap.put("DISCONNECT_TYPE", "");
			}
			if (iMap.get("AGENT_ID") == null) {
				iMap.put("AGENT_ID", "");
			}
			if (iMap.get("T_DIALING") != null) {
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

}
