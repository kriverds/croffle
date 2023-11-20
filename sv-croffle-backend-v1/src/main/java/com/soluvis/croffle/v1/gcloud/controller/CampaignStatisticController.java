package com.soluvis.croffle.v1.gcloud.controller;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.service.CampaignStatisticService;
import com.soluvis.croffle.v1.gcloud.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 클래스 설명 : 캠페인 통계 컨트롤러
 * 
 * @Class Name : AgentManagementController
 * @date : 2023. 10. 10.
 * @author : Riverds
 * @version : 1.0 ----------------------------------------
 * @notify
 *
 */
@Controller
@RequestMapping(value = "/v1/api/gcloud/statistic/campaign")
public class CampaignStatisticController {

	@Autowired
	CampaignStatisticService outboundStatisticService;

	Logger logger = LoggerFactory.getLogger(CampaignStatisticController.class);
	ObjectMapper om = new ObjectMapper();

	@GetMapping(value = "/campaigns", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getOutboundCampaigns(HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = outboundStatisticService.getOutboundCampaigns(param);
		
		
		JSONArray ja = result.getJSONArray("entities");
		JSONArray resultJa = new JSONArray();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("campaignId", jo.getString("id"));
			System.out.println(jo.getString("id"));
//			resultJa.put(new JSONObject(getOutboundCampaignStats(request, map)));
		}

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
	
	@GetMapping(value = "/campaigns/aggregate", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getCampaignAggregateQuery(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = outboundStatisticService.getCampaignAggregateQuery(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
	
	/**
	 * 메서드 설명	: 시간을 받아서 해당 시간대의 로우데이터 적재
	 * @Method Name : campaignConversationQuery
	 * @date   		: 2023. 11. 16.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 * 
	 */
	@GetMapping(value = "/campaigns/conversation", produces = "application/json; charset=UTF-8")
	public @ResponseBody String campaignConversationQuery(HttpServletRequest request, @RequestBody Map<String, Object> param)
			throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = outboundStatisticService.getCampaignConversationQuery(param);

		result.put("status", 200);
		return result.toString();
	}

}
