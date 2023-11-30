	package com.soluvis.croffle.v1.gcloud.controller;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.service.RoutingQueueManagementService;
import com.soluvis.croffle.v1.gcloud.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 라우팅큐 매니지먼트 컨트롤러
 * @Class Name 	: AgentQueueManagementController
 * @date   		: 2023. 10. 10.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Controller
@RequestMapping(value = "/v1/api/gcloud/management/queue/routingqueue")
public class RoutingQueueManagementController {

	@Autowired
	RoutingQueueManagementService routingQueueManagementService;

	Logger logger = LoggerFactory.getLogger(RoutingQueueManagementController.class);
	ObjectMapper om = new ObjectMapper();


	/**
	 * 메서드 설명	: 라우팅큐 리스트를 조회한다.
	 * @Method Name : getRoutingQueues
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@GetMapping(value="/queues", produces="application/json; charset=UTF-8")
	public @ResponseBody String getRoutingQueues(HttpServletRequest request) throws Exception{
		Map<String, Object> param = new HashMap<>();
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingQueueManagementService.getRoutingQueues(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사가 보유 한 라우팅큐 리스트를 조회한다.
	 * @Method Name : getUserQueues
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param userId
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@GetMapping(value="/queues/own", produces="application/json; charset=UTF-8")
	public @ResponseBody String getUserQueues(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingQueueManagementService.getUserQueues(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 라우팅큐에 할당 된 상담사 리스트를 조회한다.
	 * @Method Name : getRoutingQueueMembers
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param queueId
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@GetMapping(value="/members", produces="application/json; charset=UTF-8")
	public @ResponseBody String getRoutingQueueMembers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingQueueManagementService.getRoutingQueueMembers(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 라우팅큐에 상담사를 추가한다.
	 * @Method Name : postRoutingQueueMembers
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param queueId
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PostMapping(value="/members", produces="application/json; charset=UTF-8")
	public @ResponseBody String postRoutingQueueMembers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingQueueManagementService.postRoutingQueueMembers(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 라우팅큐에서 상담사를 삭제한다.
	 * @Method Name : deleteRoutingQueueMember
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param queueId
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@DeleteMapping(value="/members", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteRoutingQueueMember(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingQueueManagementService.deleteRoutingQueueMember(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
}
