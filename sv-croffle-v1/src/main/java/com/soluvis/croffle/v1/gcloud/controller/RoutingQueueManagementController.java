package com.soluvis.croffle.v1.gcloud.controller;

import java.util.Map;

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
		gcconnector.getRoutingQueues();
		gcconnector.close();

		return "OK";
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
		logger.info("{}", userId);
		
		gcconnector.connect();
		gcconnector.getUserQueues(userId);
		gcconnector.close();

		return "OK";
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
		logger.info("{}", queueId);
		
		gcconnector.connect();
		gcconnector.getRoutingQueueMembers(queueId);
		gcconnector.close();

		return "OK";
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
		logger.info("{}", queueId);
		
		String userId = param.get("userId")==null?"":(String)param.get("userId");
		
		gcconnector.connect();
		gcconnector.postRoutingQueueMembers(queueId, userId, false);
		gcconnector.close();

		return "OK";
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
		logger.info("{}", queueId);
		
		String userId = param.get("userId")==null?"":(String)param.get("userId");
		
		gcconnector.connect();
		gcconnector.deleteRoutingQueueMember(queueId, userId);
		gcconnector.close();

		return "OK";
	}
}
