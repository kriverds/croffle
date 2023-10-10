package com.soluvis.croffle.v1.gcloud.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/v1/api/gcloud/management/skill")
public class SkillManagementController {

	@Autowired
	GCConnector gcconnector;

	Logger logger = LoggerFactory.getLogger(SkillManagementController.class);


	@GetMapping(value="/routingskills/list", produces="application/json; charset=UTF-8")
	public @ResponseBody String getRoutingSkillList(HttpServletRequest request) throws Exception{
		gcconnector.connect();
		gcconnector.getRoutingSkillList();
		gcconnector.close();

		return "OK";
	}

	@GetMapping(value="/routingskills/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getUserRoutingskills(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		logger.info("{}", userId);

		gcconnector.connect();
		gcconnector.getUserRoutingskills(userId);
		gcconnector.close();

		return "OK";
	}

	@PostMapping(value="/routingskills/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String postUserRoutingskills(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody Map<String,Object> param) throws Exception{

		String skillId = param.get("skillId")==null?"":(String)param.get("skillId");
		Double level = param.get("level")==null?0D:(Double)param.get("level");

		gcconnector.connect();
		gcconnector.postUserRoutingskills(userId, skillId, level);
		gcconnector.close();

		return "OK";
	}

	@DeleteMapping(value="/routingskills/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteUserRoutingskill(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody Map<String,Object> param) throws Exception{

		String skillId = param.get("skillId")==null?"":(String)param.get("skillId");

		gcconnector.connect();
		gcconnector.deleteUserRoutingskill(userId, skillId);
		gcconnector.close();

		return "OK";
	}

	/**
	 * 메서드 설명	- 스킬 리스트 insert/update
	 * @Method Name : patchUserRoutingskillsBulk
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
	@PostMapping(value="/routingskills/{userId}/bulk", produces="application/json; charset=UTF-8")
	public @ResponseBody String patchUserRoutingskillsBulk(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody List<Map<String,Object>> lParam) throws Exception{

		String[] skillIdList = new String[lParam.size()];
		Double[] dLevelList = new Double[lParam.size()];

		for (int i = 0; i < lParam.size(); i++) {
			Map<String,Object> param = lParam.get(i);
			skillIdList[i] = param.get("skillId")==null?"":(String)param.get("skillId");
			dLevelList[i] = param.get("level")==null?0D:(Double)param.get("level");
		}

		gcconnector.connect();
		gcconnector.patchUserRoutingskillsBulk(userId, skillIdList, dLevelList);
		gcconnector.close();

		return "OK";
	}

	/**
	 * 메서드 설명	- 상담원이 보유중인 스킬을 전달받은 스킬 리스트로 교체
	 * @Method Name : putUserRoutingskillsBulk
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
	@PutMapping(value="/routingskills/{userId}/bulk", produces="application/json; charset=UTF-8")
	public @ResponseBody String putUserRoutingskillsBulk(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody List<Map<String,Object>> lParam) throws Exception{

		String[] skillIdList = new String[lParam.size()];
		Double[] dLevelList = new Double[lParam.size()];

		for (int i = 0; i < lParam.size(); i++) {
			Map<String,Object> param = lParam.get(i);
			skillIdList[i] = param.get("skillId")==null?"":(String)param.get("skillId");
			dLevelList[i] = param.get("level")==null?0D:(Double)param.get("level");
		}

		gcconnector.connect();
		gcconnector.putUserRoutingskillsBulk(userId, skillIdList, dLevelList);
		gcconnector.close();

		return "OK";
	}
}
