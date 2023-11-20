package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.mapper.UVoiceMapper;

@Service
public class UVoiceService {

	@Autowired
	UVoiceMapper uVoiceMapper;

	private final Logger logger = LoggerFactory.getLogger(UVoiceService.class);
	
	public List<Map<String, Object>> serviceByIfId(String ifId, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = null;
		
		switch (ifId) {
		case "0001":
			result = uVoiceMapper.ifUVC0001(params);
			break;
		case "0002":
			result = uVoiceMapper.ifUVC0002(params);
			break;
		case "0003":
			result = uVoiceMapper.ifUVC0003(params);
			break;
		case "0004":
			result = uVoiceMapper.ifUVC0004(params);
			break;
		case "0005":
			result = uVoiceMapper.ifUVC0005(params);
			break;
		case "0006":
			result = uVoiceMapper.ifUVC0006(params);
			break;
		case "0007":
			result = uVoiceMapper.ifUVC0007(params);
			break;
		case "0008":
			result = uVoiceMapper.ifUVC0008(params);
			break;
		case "0009":
			result = uVoiceMapper.ifUVC0009(params);
			break;
		case "0010":
			result = uVoiceMapper.ifUVC0010(params);
			break;
		case "0011":
			result = uVoiceMapper.ifUVC0011(params);
			break;
		default:
			result = new ArrayList<>();
			break;
		}
		return result;
	}


}
