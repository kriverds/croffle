package com.soluvis.croffle.v1.gcloud.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.service.RoutingSkillManagementService;
import com.soluvis.croffle.v1.gcloud.util.CommUtil;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 클래스 설명	: 라우팅스킬 매니지먼트 컨트롤러
 * @Class Name 	: RoutingSkillManagementController
 * @date   		: 2023. 10. 10.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Controller
@RequestMapping(value = "/v1/api/gcloud/management/routingskill")
public class RoutingSkillManagementController {

	@Autowired
	RoutingSkillManagementService routingSkillManagementService;

	Logger logger = LoggerFactory.getLogger(RoutingSkillManagementController.class);
	ObjectMapper om = new ObjectMapper();

	/**
	 * 메서드 설명	: 라우팅스킬 리스트를 조회한다.
	 * @Method Name : getRoutingSkillList
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
	@GetMapping(value = "/skills", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getRoutingSkillList(HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("rUUID", CommUtil.setAttrUUID(request));
		
		JSONObject result = routingSkillManagementService.getRoutingSkillList();
		
		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사가 보유한 라우팅스킬 리스트를 조회한다.
	 * @Method Name : getUserRoutingskills
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
	@GetMapping(value="/skills/own", produces="application/json; charset=UTF-8")
	public @ResponseBody String getUserRoutingskills(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);
		
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingSkillManagementService.getUserRoutingskills(param);
		result.put("count", result.length());

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사에게 라우팅스킬을 추가한다.
	 * @Method Name : postUserRoutingskills
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
	@PostMapping(value="/skills", produces="application/json; charset=UTF-8")
	public @ResponseBody String postUserRoutingskills(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);
		
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingSkillManagementService.patchUserRoutingskillsBulk(param);
		result.put("count", result.length());

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	/**
	 * 메서드 설명	: 상담사에게 부여된 라우팅스킬을 삭제한다.
	 * @Method Name : deleteUserRoutingskill
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
	@DeleteMapping(value="/skills", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteUserRoutingskill(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);
		
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingSkillManagementService.deleteUserRoutingskill(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}


	/**
	 * 메서드 설명	- 상담원이 보유중인 스킬을 전달받은 스킬 리스트로 교체
	 * @Method Name : changeSkillBySkillCart
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param request
	 * @param userId
	 * @param skillId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value="/skills", produces="application/json; charset=UTF-8")
	public @ResponseBody String changeSkillBySkillCart(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);
		
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingSkillManagementService.changeSkillBySkillCart(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
	
	@PostMapping(value="/skills/presents", produces="application/json; charset=UTF-8")
	public @ResponseBody String postAgentSkillPresent(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{
		logger.info("{}", param);
		
		param.put("rUUID", CommUtil.setAttrUUID(request));

		JSONObject result = routingSkillManagementService.replaceAgentSkillPresent(param);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
}
