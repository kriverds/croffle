package com.soluvis.croffle.v1.gcloud.controller;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.Group;
import com.neovisionaries.ws.client.WebSocketException;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 상담사 그룹 매니지먼트 컨트롤러
 * @Class Name 	: GroupAgentManagementController
 * @date   		: 2023. 10. 10.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Controller
@RequestMapping(value = "/v1/api/gcloud/management/group/agent")
public class GroupAgentManagementController {

	@Autowired
	GCConnector gcconnector;

	Logger logger = LoggerFactory.getLogger(GroupAgentManagementController.class);
	JSONObject result = new JSONObject();
	ObjectMapper om = new ObjectMapper();
	

	/**
	 * 메서드 설명	: 그룹 리스트를 가져온다.
	 * @Method Name : getGroups
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @throws JSONException
	 * @notify
	 *
	 */
	@GetMapping(value="/groups", produces="application/json; charset=UTF-8")
	public @ResponseBody String getGroups(HttpServletRequest request ) throws Exception{
		gcconnector.connect();
		JSONObject rJO = gcconnector.getGroups();
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 그룹 정보를 가져온다.
	 * @Method Name : getGroup
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param groupId
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	@GetMapping(value="/groups/{groupId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getGroup(HttpServletRequest request
			, @PathVariable(name = "groupId", required = true) String groupId) throws Exception{
		gcconnector.connect();
		Group rVO = gcconnector.getGroup(groupId);
		gcconnector.close();

		JSONObject rJO = new JSONObject(om.writeValueAsString(rVO));
		result.put("item", rJO);

		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 그룹을 생성한다.
	 * @Method Name : postGroups
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	@PostMapping(value="/groups", produces="application/json; charset=UTF-8")
	public @ResponseBody String postGroups(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		String name = param.get("name")==null?"":(String)param.get("name");

		gcconnector.connect();
		JSONObject rJO = gcconnector.postGroups(name);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 그룹을 수정한다.
	 * @Method Name : putGroup
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param groupId
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	@PutMapping(value="/groups/{groupId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String putGroup(HttpServletRequest request
			, @PathVariable(name = "groupId", required = true) String groupId
			, @RequestBody Map<String,Object> param) throws Exception{
		String name = param.get("name")==null?"":(String)param.get("name");

		gcconnector.connect();
		JSONObject rJO = gcconnector.putGroup(groupId, name);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 그룹을 삭제한다.
	 * @Method Name : deleteGroup
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param groupId
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	@DeleteMapping(value="/groups/{groupId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteGroup(HttpServletRequest request
			, @PathVariable(name = "groupId", required = true) String groupId) throws Exception{
		gcconnector.connect();
		gcconnector.deleteGroup(groupId);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 그룹 멤버를 조회한다.
	 * @Method Name : getGroupMembers
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param groupId
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @throws JSONException
	 * @notify
	 *
	 */
	@GetMapping(value="/members/{groupId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getGroupMembers(HttpServletRequest request
			, @PathVariable(name = "groupId", required = true) String groupId) throws Exception{
		gcconnector.connect();
		JSONObject rJO = gcconnector.getGroupMembers(groupId);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}


	/**
	 * 메서드 설명	: 그룹 멤버를 추가한다.
	 * @Method Name : postGroupMembers
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param groupId
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	@PostMapping(value="/members/{groupId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String postGroupMembers(HttpServletRequest request
			, @PathVariable(name = "groupId", required = true) String groupId
			, @RequestBody Map<String,Object> param) throws Exception{
		String userId = param.get("userId")==null?"":(String)param.get("userId");

		gcconnector.connect();
		gcconnector.postGroupMembers(groupId, userId);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 그룹 멤버를 삭제한다.
	 * @Method Name : deleteGroupMemebers
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param groupId
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	@DeleteMapping(value="/members/{groupId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteGroupMemebers(HttpServletRequest request
			, @PathVariable(name = "groupId", required = true) String groupId
			, @RequestBody Map<String,Object> param) throws Exception{
		String userId = param.get("userId")==null?"":(String)param.get("userId");

		gcconnector.connect();
		gcconnector.deleteGroupMemebers(groupId, userId);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}

}
