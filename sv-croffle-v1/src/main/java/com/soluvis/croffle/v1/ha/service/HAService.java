package com.soluvis.croffle.v1.ha.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.ha.mapper.HAMapper;
import com.soluvis.croffle.v1.util.GVal;

/**
 * 클래스 설명	: HA Service
 * @Class Name 	: HAService
 * @date   		: 2023. 12. 12.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Service
public class HAService {

	@Value("${ha.timeout}")
	String hATimeout;

	@Autowired
	HAMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(HAService.class);

	/**
	 * 메서드 설명	: HA Timeout을 체크한다.
	 * @Method Name : checkTimeout
	 * @date   		: 2023. 12. 12.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	public int checkTimeout() throws Exception {
		int iResult = 0;
		Map<String,Object> dbParam = new HashMap<>();
		dbParam.put("id", Integer.parseInt(GVal.getApplicationId()));
		dbParam.put("timeout", Integer.parseInt(hATimeout));
		List<Map<String,Object>> qResult = mapper.checkTimeout(dbParam);
		if(!qResult.isEmpty()) {
			String id = qResult.get(0).get("id").toString();
			updateHA();
			if(!id.equals(GVal.getApplicationId())) {
				iResult = 1;
			}else {
				iResult = 2;
			}
		}
		return iResult;
	}

	/**
	 * 메서드 설명	: HA 테이블을 갱신한다.
	 * @Method Name : mergeHA
	 * @date   		: 2023. 12. 12.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	public int updateHA() throws Exception {
		Map<String,Object> dbParam = new HashMap<>();
		dbParam.put("id", Integer.parseInt(GVal.getApplicationId()));
		return mapper.updateHA(dbParam);
	}

}

//