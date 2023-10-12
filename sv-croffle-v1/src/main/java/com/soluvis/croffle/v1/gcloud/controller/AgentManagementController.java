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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mypurecloud.sdk.v2.model.User;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 상담사 매니지먼트 컨트롤러
 * @Class Name 	: AgentManagementController
 * @date   		: 2023. 10. 10.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Controller
@RequestMapping(value = "/v1/api/gcloud/management/agent")
public class AgentManagementController {

	@Autowired
	GCConnector gcconnector;

	Logger logger = LoggerFactory.getLogger(AgentManagementController.class);
	JSONObject result = new JSONObject();
	ObjectMapper om = new ObjectMapper();


	@GetMapping(value="/getExtensionList", produces="application/json; charset=UTF-8")
	public @ResponseBody String getExtensionList(HttpServletRequest request ) throws Exception{
		gcconnector.connect();
		gcconnector.getExtensionList();
		gcconnector.close();

		return "OK";
	}

	/**
	 * 메서드 설명	: 상담사 전체 리스트를 조회한다.
	 * @Method Name : getAvailableUserList
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
	@GetMapping(value="/users", produces="application/json; charset=UTF-8")
	public @ResponseBody String getAvailableUserList(HttpServletRequest request ) throws Exception{
		gcconnector.connect();
		JSONObject rJO = gcconnector.getAvailableUserList();
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사를 조회한다.
	 * @Method Name : getAvailableUser
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
	@GetMapping(value="/users/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getAvailableUser(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		gcconnector.connect();
		User rVO = gcconnector.getAvailableUser(userId);
		gcconnector.close();

		JSONObject rJO = new JSONObject(om.writeValueAsString(rVO));
		result.put("item", rJO);

		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사를 생성한다.
	 * @Method Name : postUsers
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PostMapping(value="/users", produces="application/json; charset=UTF-8")
	public @ResponseBody String postUsers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		String name = param.get("name")==null?"":(String)param.get("name");
		String email = param.get("email")==null?"":(String)param.get("email");

		gcconnector.connect();
		JSONObject rJO = gcconnector.postUsers(name, email);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사를 수정한다.
	 * @Method Name : patchUser
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PatchMapping(value="/users/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String patchUser(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody Map<String,Object> param) throws Exception{
		String department = param.get("department")==null?"":(String)param.get("department");

		gcconnector.connect();
		JSONObject rJO = gcconnector.patchUser(userId, department);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사를 삭제한다.
	 * @Method Name : deleteUser
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
	@DeleteMapping(value="/users/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteUser(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		gcconnector.connect();
		gcconnector.deleteUser(userId);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}
}
