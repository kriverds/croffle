package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.mapper.UCubeMapper;

@Service
public class UCubeService {

	@Autowired
	UCubeMapper uCubeMapper;
	
	@Autowired
	APIMService apimSercice;

	private final Logger logger = LoggerFactory.getLogger(UCubeService.class);

	public JSONObject serviceByIfId(String ifId, Map<String, Object> params) throws Exception {
		JSONObject jResult = null;
		List<Map<String, Object>> lmResult = null;
		logger.info("UVoice I/F ID{}", ifId);
		

		switch (ifId) {
		case "0010":
			jResult = ifSTA0010(params);
			break;
		case "0020":
			jResult = ifSTA0020(params);
			break;
		case "0030":
			jResult = ifSTA0030(params);
			break;
		case "0040":
			jResult = ifSTA0040(params);
			break;
		case "0050":
			jResult = ifSTA0050(params);
			break;
		case "0060":
			jResult = ifSTA0060(params);
			break;
		case "0070":
			jResult = ifSTA0070(params);
			break;
		case "0080":
			jResult = ifSTA0080(params);
			break;
		case "0090":
			jResult = ifSTA0090(params);
			break;
		case "0100":
			jResult = ifSTA0100(params);
			break;
		case "0110":
			jResult = ifSTA0110(params);
			break;
		case "0120":
			jResult = ifSTA0120(params);
			break;
		case "0130":
			jResult = ifSTA0130(params);
			break;
		case "0140":
			jResult = ifSTA0140(params);
			break;
		case "0150":
			jResult = ifSTA0150(params);
			break;
		case "0160":
			jResult = ifSTA0160(params);
			break;
		default:
			lmResult = new ArrayList<>();
			break;
		}
		if(jResult == null) {
			JSONArray ja = new JSONArray(lmResult);
			jResult = new JSONObject();
			jResult.put("list", ja);
		}
		return jResult;
	}
	
	public JSONObject ifSTA0010(Map<String, Object> params) throws Exception{
		return apimSercice.sendSMS(params);
	}
	public JSONObject ifSTA0020(Map<String, Object> params) throws Exception{
		return apimSercice.sendSMS(params);
	}
	public JSONObject ifSTA0030(Map<String, Object> params) throws Exception{
		return apimSercice.postMblArst(params);
	}
	public JSONObject ifSTA0040(Map<String, Object> params) throws Exception{
		return apimSercice.postHmArst(params);
	}
	public JSONObject ifSTA0050(Map<String, Object> params) throws Exception{
		return apimSercice.sendSMS(params);
	}
	public JSONObject ifSTA0060(Map<String, Object> params) throws Exception{
		return apimSercice.sendSMS(params);
	}
	public JSONObject ifSTA0070(Map<String, Object> params) throws Exception{
		return apimSercice.sendSMS(params);
	}
	public JSONObject ifSTA0080(Map<String, Object> params) throws Exception{
		return apimSercice.sendSMS(params);
	}
	public JSONObject ifSTA0090(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentInfo(params);
	}
	public JSONObject ifSTA0100(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentLoginInfo(params);
	}
	public JSONObject ifSTA0110(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentCallStatic(params);
	}
	public JSONObject ifSTA0120(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentInfo(params);
	}
	public JSONObject ifSTA0130(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentInfo(params);
	}
	public JSONObject ifSTA0140(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentLoginInfo(params);
	}
	public JSONObject ifSTA0150(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentCallStatic(params);
	}
	public JSONObject ifSTA0160(Map<String, Object> params) throws Exception{
		return apimSercice.ctiAgentInfo(params);
	}

}
