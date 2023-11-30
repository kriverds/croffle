package com.soluvis.croffle.v1.gcloud.controller;

import java.io.IOException;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mypurecloud.sdk.v2.ApiException;
import com.neovisionaries.ws.client.WebSocketException;
import com.soluvis.croffle.v1.gcloud.service.GroupAgentManagementService;
import com.soluvis.croffle.v1.gcloud.util.CommUtil;

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
	GroupAgentManagementService groupAgentManagementService;

	Logger logger = LoggerFactory.getLogger(GroupAgentManagementController.class);
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
		Map<String, Object> param = new HashMap<>();
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.getGroups(param);

		result.put("status", 200);
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
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.postGroups(param);

		result.put("status", 200);
		logger.info("{}", result);
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
	@PutMapping(value="/groups", produces="application/json; charset=UTF-8")
	public @ResponseBody String putGroup(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.putGroup(param);

		result.put("status", 200);
		logger.info("{}", result);
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
	@DeleteMapping(value="/groups", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteGroup(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.deleteGroup(param);

		result.put("status", 200);
		logger.info("{}", result);
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
	@GetMapping(value="/members", produces="application/json; charset=UTF-8")
	public @ResponseBody String getGroupMembers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.getGroupMembers(param);

		result.put("status", 200);
		logger.info("{}", result);
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
	@PostMapping(value="/members", produces="application/json; charset=UTF-8")
	public @ResponseBody String postGroupMembers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.postGroupMembers(param);

		result.put("status", 200);
		logger.info("{}", result);
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
	@DeleteMapping(value="/members", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteGroupMemebers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.deleteGroupMemebers(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

}
