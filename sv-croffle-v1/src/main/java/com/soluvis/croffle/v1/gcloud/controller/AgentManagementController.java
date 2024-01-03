package com.soluvis.croffle.v1.gcloud.controller;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soluvis.croffle.v1.gcloud.service.AgentManagementService;
import com.soluvis.croffle.v1.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 상담사 매니지먼트 컨트롤러
 * @Class Name 	: AgentManagementController
 * @date   		: 2023. 10. 10.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@RestController
@RequestMapping(value = "/v1/api/gcloud/management/agent")
public class AgentManagementController {

	@Autowired
	AgentManagementService agentManagementService;

	Logger logger = LoggerFactory.getLogger(AgentManagementController.class);
	ObjectMapper om = new ObjectMapper();


	@GetMapping(value="/user", produces="application/json; charset=UTF-8")
	public String getAvailableUser(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception{
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = agentManagementService.getAvailableUser(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
	/**
	 * 메서드 설명	: 상담사 전체 리스트를 조회한다.
	 * @Method Name : getAvailableUserList
	 * @date   		: 2023. 10. 10.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@GetMapping(value="/users", produces="application/json; charset=UTF-8")
	public String getAvailableUserList(HttpServletRequest request) throws Exception{
		Map<String, Object> param = new HashMap<>();
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = agentManagementService.getAvailableUserList(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}


	/**
	 * 메서드 설명	: 상담사를 생성한다.
	 * @Method Name : postUsers
	 * @date   		: 2023. 10. 10.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param : name, email, title, department
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PostMapping(value="/users", produces="application/json; charset=UTF-8")
	public String postUsers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception {
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = agentManagementService.postUsers(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사를 수정한다.
	 * @Method Name : patchUser
	 * @date   		: 2023. 10. 10.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *	기능만 구현해 놓음
	 */
	@PatchMapping(value="/users", produces="application/json; charset=UTF-8")
	public String patchUser(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = agentManagementService.patchUser(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}


	/**
	 * 메서드 설명	: 상담사를 삭제한다.
	 * @Method Name : deleteUser
	 * @date   		: 2023. 12. 22.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param : id
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@DeleteMapping(value="/users", produces="application/json; charset=UTF-8")
	public String deleteUser(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = agentManagementService.deleteUser(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
}
