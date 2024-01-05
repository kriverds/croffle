package com.soluvis.croffle.v1.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.ha.mapper.HAMapper;
import com.soluvis.croffle.v1.util.GVal;

import jakarta.annotation.PostConstruct;

/**
 * 클래스 설명	: Application 실행 시 global valiable 설정 / init method 실행 용도 Class
 * @Class Name 	: InitCroffle
 * @date   		: 2023. 12. 21.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Component
public class InitCroffle {

	private final Logger logger = LoggerFactory.getLogger(InitCroffle.class);

	@Autowired
	HAMapper hAMapper;
	
	@Value("${gcloud.client.id}")
	private String gcClientId;
	@Value("${gcloud.client.secret}")
	private String gcClientSecret;

	@PostConstruct
//	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		logger.info("{}", "@PostConstruct");
		initGVal();
		try {
			initHA();
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

	/**
	 * 메서드 설명	: 이중화 initialize
	 * @Method Name : initHA
	 * @date   		: 2023. 12. 21.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @notify
	 *
	 */
	private void initHA() throws Exception{
		boolean primaryFlag = false;
		List<Map<String, Object>> qResult = hAMapper.initSelect();
		if (qResult.isEmpty()) {
			Map<String, Object> dbparam = new HashMap<>();
			logger.info("{}", GVal.getApplicationId());

			dbparam.put("id", Integer.parseInt(GVal.getApplicationId()));
			hAMapper.initInsert(dbparam);
			primaryFlag = true;
		} else {
			String id = qResult.get(0).get("id").toString();
			if (id.equals(GVal.getApplicationId())) {
				primaryFlag = true;
			}
		}
		if (primaryFlag) {
			GVal.setHaRole("primary");
		}
	}
	
	private void initGVal() {
		String applicationId = System.getProperty("applicationId");
		GVal.setApplicationId(applicationId);
		
		GVal.setGcClientId(gcClientId);
		GVal.setGcClientSecret(gcClientSecret);
	}
}
