	package com.soluvis.croffle.v1.gcloud.controller;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

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
	GCConnector gcconnector;

	Logger logger = LoggerFactory.getLogger(RoutingQueueManagementController.class);
	JSONObject result = new JSONObject();
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
		gcconnector.connect();
		JSONObject rJO = gcconnector.getRoutingQueues();
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@GetMapping(value="/queues/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getUserQueues(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		gcconnector.connect();
		JSONObject rJO = gcconnector.getUserQueues(userId);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@GetMapping(value="/members/{queueId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getRoutingQueueMembers(HttpServletRequest request
			, @PathVariable(name = "queueId", required = true) String queueId) throws Exception{
		gcconnector.connect();
		JSONObject rJO = gcconnector.getRoutingQueueMembers(queueId);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@PostMapping(value="/members/{queueId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String postRoutingQueueMembers(HttpServletRequest request
			, @PathVariable(name = "queueId", required = true) String queueId
			, @RequestBody Map<String,Object> param) throws Exception{
		String userId = param.get("userId")==null?"":(String)param.get("userId");

		gcconnector.connect();
		gcconnector.postRoutingQueueMembers(queueId, userId, false);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@DeleteMapping(value="/members/{queueId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteRoutingQueueMember(HttpServletRequest request
			, @PathVariable(name = "queueId", required = true) String queueId
			, @RequestBody Map<String,Object> param) throws Exception{
		String userId = param.get("userId")==null?"":(String)param.get("userId");

		gcconnector.connect();
		gcconnector.deleteRoutingQueueMember(queueId, userId);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}
}
