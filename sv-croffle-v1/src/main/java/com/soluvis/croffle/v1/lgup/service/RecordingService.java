package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.mapper.RecordingMapper;

/**
 * 클래스 설명	: 녹취 인터페이스 서비스
 * @Class Name 	: RecordingService
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@Service
public class RecordingService {

	@Autowired
	RecordingMapper recordingMapper;

	private final Logger logger = LoggerFactory.getLogger(RecordingService.class);

	public List<Map<String, Object>> serviceByIfId(String ifId, Map<String, Object> params) throws Exception {
		logger.info("Recording I/F ID{}", ifId);

		List<Map<String, Object>> result = null;

		switch (ifId) {
		case "3010":
			result = recordingMapper.ifSTA3010(params);
			break;
		case "3020":
			result = recordingMapper.ifSTA3020(params);
			break;
		default:
			result = new ArrayList<>();
			break;
		}
		return result;
	}


}
