package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.mapper.UVoiceMapper;

/**
 * 클래스 설명	: 유보이스 인터페이스 서비스
 * @Class Name 	: UVoiceService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class UVoiceService {

	@Autowired
	UVoiceMapper uVoiceMapper;

	private final Logger logger = LoggerFactory.getLogger(UVoiceService.class);

	public List<Map<String, Object>> serviceByIfId(String ifId, Map<String, Object> params) throws Exception {
		logger.info("UVoice I/F ID{}", ifId);
		List<Map<String, Object>> result = null;

		switch (ifId) {
		case "1010":
			result = uVoiceMapper.ifSTA1010(params);
			break;
		case "1020":
			result = uVoiceMapper.ifSTA1020(params);
			break;
		case "1030":
			result = uVoiceMapper.ifSTA1030(params);
			break;
		case "1040":
			result = uVoiceMapper.ifSTA1040(params);
			break;
		case "1050":
			result = uVoiceMapper.ifSTA1050(params);
			break;
		case "1060":
			result = uVoiceMapper.ifSTA1060(params);
			break;
		case "1070":
			result = uVoiceMapper.ifSTA1070(params);
			break;
		case "1080":
			result = uVoiceMapper.ifSTA1080(params);
			break;
		case "1090":
			result = uVoiceMapper.ifSTA1090(params);
			break;
		case "1100":
			result = uVoiceMapper.ifSTA1100(params);
			break;
		case "1110":
			result = uVoiceMapper.ifSTA1110(params);
			break;
		default:
			result = new ArrayList<>();
			break;
		}
		return result;
	}


}
