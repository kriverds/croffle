package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.mapper.RecordingMapper;

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
