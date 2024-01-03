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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soluvis.croffle.v1.gcloud.service.GroupAgentManagementService;
import com.soluvis.croffle.v1.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 상담사 그룹 매니지먼트 컨트롤러
 * @Class Name 	: GroupAgentManagementController
 * @date   		: 2023. 10. 10.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@RestController
@RequestMapping(value = "/v1/api/gcloud/management/group/agent")
public class GroupAgentManagementController {

	@Autowired
	GroupAgentManagementService groupAgentManagementService;

	Logger logger = LoggerFactory.getLogger(GroupAgentManagementController.class);
	ObjectMapper om = new ObjectMapper();


	/**
	 * 메서드 설명	: 그룹 리스트를 가져온다.
	 * @Method Name : getGroups
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@GetMapping(value="/groups", produces="application/json; charset=UTF-8")
	public String getGroups(HttpServletRequest request ) throws Exception{
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
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PostMapping(value="/groups", produces="application/json; charset=UTF-8")
	public String postGroups(HttpServletRequest request
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
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PutMapping(value="/groups", produces="application/json; charset=UTF-8")
	public String putGroup(HttpServletRequest request
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
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@DeleteMapping(value="/groups", produces="application/json; charset=UTF-8")
	public String deleteGroup(HttpServletRequest request
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
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@GetMapping(value="/members", produces="application/json; charset=UTF-8")
	public String getGroupMembers(HttpServletRequest request
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
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@PostMapping(value="/members", produces="application/json; charset=UTF-8")
	public String postGroupMembers(HttpServletRequest request
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
	 * @date   		: 2024. 1. 2.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@DeleteMapping(value="/members", produces="application/json; charset=UTF-8")
	public String deleteGroupMemebers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);

		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = groupAgentManagementService.deleteGroupMemebers(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

}
