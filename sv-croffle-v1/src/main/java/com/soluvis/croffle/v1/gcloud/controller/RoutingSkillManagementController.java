package com.soluvis.croffle.v1.gcloud.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

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
	GCConnector gcconnector;

	Logger logger = LoggerFactory.getLogger(RoutingSkillManagementController.class);
	JSONObject result = new JSONObject();
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
	@GetMapping(value="/skills", produces="application/json; charset=UTF-8")
	public @ResponseBody String getRoutingSkillList(HttpServletRequest request) throws Exception{
		gcconnector.connect();
		JSONObject rJO = gcconnector.getRoutingSkillList();
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@GetMapping(value="/skills/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getUserRoutingskills(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		logger.info("{}", userId);

		gcconnector.connect();
		JSONObject rJO = gcconnector.getUserRoutingskills(userId);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@PostMapping(value="/skills/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String postUserRoutingskills(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody Map<String,Object> param) throws Exception{

		String skillId = param.get("skillId")==null?"":(String)param.get("skillId");

		Double level = param.get("level")==null?0D:(Double)param.get("level");

		gcconnector.connect();
		JSONObject rJO = gcconnector.postUserRoutingskills(userId, skillId, level);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
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
	@DeleteMapping(value="/skills/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteUserRoutingskill(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody Map<String,Object> param) throws Exception{

		String skillId = param.get("skillId")==null?"":(String)param.get("skillId");

		gcconnector.connect();
		gcconnector.deleteUserRoutingskill(userId, skillId);
		gcconnector.close();

		JSONObject rJO = new JSONObject();
		rJO.put("object", "");
		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
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
	@PostMapping(value="/skills/bulk/{userId}", produces="application/json; charset=UTF-8")
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
		JSONObject rJO = gcconnector.patchUserRoutingskillsBulk(userId, skillIdList, dLevelList);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
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
	@PutMapping(value="/skills/bulk/{userId}", produces="application/json; charset=UTF-8")
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
		JSONObject rJO = gcconnector.putUserRoutingskillsBulk(userId, skillIdList, dLevelList);
		gcconnector.close();

		result.put("item", rJO);

		logger.info("{}", rJO);
		return result.toString();
	}
}
