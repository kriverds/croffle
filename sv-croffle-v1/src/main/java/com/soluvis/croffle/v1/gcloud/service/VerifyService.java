package com.soluvis.croffle.v1.gcloud.service;

import java.util.ArrayList;
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

import com.mypurecloud.sdk.v2.model.ConversationAggregationQuery;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;
import com.soluvis.croffle.v1.gcloud.mapper.VerifyMapper;
import com.soluvis.croffle.v1.gcloud.util.CommUtil;

@Service
public class VerifyService {

	@Autowired
	GCConnector gcconnector;
	@Autowired
	VerifyMapper verifyMapper;

	private final Logger logger = LoggerFactory.getLogger(VerifyService.class);

	public JSONObject verifyAgentStatistic(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = (UUID) param.get("rUUID");
		String startDate = jParam.getString("startDate");
		if(Long.parseLong(startDate.substring(startDate.length()-2, startDate.length()))%15 != 0) {
			throw new ParseException();
		}
		GCConnector.connect(rUUID);
		JSONObject resultCall = gcconnector.agentCallAggregateQuery(startDate);
		JSONObject resultStatus = gcconnector.agentStatusAggregateQuery(startDate);
		GCConnector.close(rUUID);

		List<Map<String,Object>> iList = new ArrayList<>();
		Iterator<String> it = resultCall.keys();
		while(it.hasNext()) {
			String agentId = it.next();
			Map<String,Object> iMap = new HashMap<>();
			iMap.put("USER_ID", agentId);

			for (int i = 0; i < ConversationAggregationQuery.MetricsEnum.values().length; i++) {
				if (i == 21 || i == 22 || i == 28 || i == 29) {
				} else {
					iMap.put(ConversationAggregationQuery.MetricsEnum.values()[i].toString(), 0);
				}
			}
			// Call 통계 항목
			JSONObject aggregatesCall = resultCall.getJSONObject(agentId);
			JSONArray resultsCall = aggregatesCall.getJSONArray("results");
			for (int i = 0; i < resultsCall.length(); i++) {
				JSONObject cResult = resultsCall.getJSONObject(i);
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

							iMap.put(metricName, CommUtil.getJInt(stats, "count"));
						}
					}
				}
			}
			// Status 통계 항목
			JSONObject aggregatesStatus = resultStatus.getJSONObject(agentId);
			Map<String, String> stom = new HashMap<>();
			JSONObject jstom = aggregatesStatus.getJSONObject("systemToOrganizationMappings");
			logger.info("{}", jstom);

			Iterator<String> it2 = jstom.keys();
			while(it2.hasNext()) {
				String key = it2.next();
				JSONArray valArray = jstom.getJSONArray(key);

				stom.put(valArray.getString(0), key);
			}
			JSONArray resultsStatus = aggregatesStatus.getJSONArray("results");
			logger.info("{}", resultsStatus);

			List<String> statNameList = new ArrayList<>();
			List<Long> statValueList = new ArrayList<>();
			for (int i = 0; i < resultsStatus.length(); i++) {
				JSONObject cResult = resultsStatus.getJSONObject(i);
				JSONArray datas = cResult.getJSONArray("data");

				for (int j = 0; j < datas.length(); j++) {
					JSONObject data = datas.getJSONObject(j);

					JSONArray metrics = data.getJSONArray("metrics");

					for (int k = 0; k < metrics.length(); k++) {
						JSONObject metric = metrics.getJSONObject(k);

						String qualifier = CommUtil.getJString(metric, "qualifier");
						if(qualifier.length() == 36) {
							qualifier = stom.get(qualifier) == null ? qualifier : stom.get(qualifier);
						}
						JSONObject stats = metric.getJSONObject("stats");
						statNameList.add(qualifier);
						statValueList.add(CommUtil.getJLong(stats, "sum"));
					}
				}
			}
			iMap.put("statNameList", statNameList);
			iMap.put("statValueList", statValueList);
			iMap.put("PROGRESS_YMD", startDate.substring(0,8));
			iMap.put("PROGRESS_HM", startDate.substring(8,12));
			iList.add(iMap);
		}

		JSONObject rResult = new JSONObject();
		rResult.put("total", iList.size());
		param.put("agentResult", iList);
		logger.info("{}", param);

		int iResult = 0;
		if(resultCall.length() > 0) {
			iResult = verifyMapper.insertAgentStatic(param);
		}
		rResult.put("progress", iResult);
		return rResult;
	}

	public JSONObject verifyQueueStatistic(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);



		UUID rUUID = (UUID) param.get("rUUID");
		String startDate = jParam.getString("startDate");
		if(Long.parseLong(startDate.substring(startDate.length()-2, startDate.length()))%15 != 0) {
			throw new ParseException();
		}
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.queueAggregateQuery(startDate);
		GCConnector.close(rUUID);

		List<Map<String,Object>> iList = new ArrayList<>();
		Iterator<String> it = result.keys();
		while(it.hasNext()) {
			String queueId = it.next();
			Map<String,Object> iMap = new HashMap<>();
			iMap.put("QUEUE_ID", queueId);
			for (int i = 0; i < ConversationAggregationQuery.MetricsEnum.values().length; i++) {
				if (i == 21 || i == 22 || i == 28 || i == 29) {
				} else {
					iMap.put(ConversationAggregationQuery.MetricsEnum.values()[i].toString(), 0);
				}
			}

			JSONObject aggregates = result.getJSONObject(queueId);
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

							iMap.put(metricName, CommUtil.getJInt(stats, "count"));
						}
					}
				}
			}
			iMap.put("PROGRESS_YMD", startDate.substring(0,8));
			iMap.put("PROGRESS_HM", startDate.substring(8,12));
			iList.add(iMap);
		}

		JSONObject rResult = new JSONObject();
		rResult.put("total", iList.size());
		param.put("queueResult", iList);
		logger.info("{}", param);

		int iResult = 0;
		if(result.length() > 0) {
			iResult = verifyMapper.insertQueueStatic(param);
		}
		rResult.put("progress", iResult);
		return rResult;
	}

	public JSONObject verifySkillStatistic(Map<String,Object> param) throws Exception {
		JSONObject jParam = new JSONObject(param);

		UUID rUUID = (UUID) param.get("rUUID");
		String startDate = jParam.getString("startDate");
		if(Long.parseLong(startDate.substring(startDate.length()-2, startDate.length()))%15 != 0) {
			throw new ParseException();
		}
		GCConnector.connect(rUUID);
		JSONObject result = gcconnector.skillAggregateQuery(startDate);
		GCConnector.close(rUUID);

		List<Map<String,Object>> iList = new ArrayList<>();
		Iterator<String> it = result.keys();
		while(it.hasNext()) {
			String skillId = it.next();
			Map<String,Object> iMap = new HashMap<>();
			iMap.put("SKILL_ID", skillId);
			for (int i = 0; i < ConversationAggregationQuery.MetricsEnum.values().length; i++) {
				if (i == 21 || i == 22 || i == 28 || i == 29) {
				} else {
					iMap.put(ConversationAggregationQuery.MetricsEnum.values()[i].toString(), 0);
				}
			}

			JSONObject aggregates = result.getJSONObject(skillId);
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

							iMap.put(metricName, CommUtil.getJInt(stats, "count"));
						}
					}
				}
			}
			iMap.put("PROGRESS_YMD", startDate.substring(0,8));
			iMap.put("PROGRESS_HM", startDate.substring(8,12));
			iList.add(iMap);
		}

		JSONObject rResult = new JSONObject();
		rResult.put("total", iList.size());
		param.put("skillResult", iList);
		logger.info("{}", param);

		int iResult = 0;
		if(result.length() > 0) {
			iResult = verifyMapper.insertSkillStatic(param);
		}
		rResult.put("progress", iResult);
		return rResult;
	}
}
