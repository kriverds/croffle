package com.soluvis.croffle.v1.lgup.controller;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.lgup.service.MiniWallBoardService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/v1/wallboard/mini")
public class MiniWallBoardController {

	Logger logger = LoggerFactory.getLogger(MiniWallBoardController.class);

	@Autowired
	MiniWallBoardService miniWallBoardService;

	/**
	 * 메서드 설명	: 미니 전광판 화면을 띄워준다.
	 * @Method Name : viewMiniWallBoardPage
	 * @date   		: 2023. 11. 22.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param model
	 * @param request
	 * @param empId
	 * @return
	 * @throws Exception
	 * @notify
	 *
	 */
	@RequestMapping(value = "/agent")
	public String viewMiniWallBoardPage(Model model, HttpServletRequest request,
			@RequestParam(value = "empId") String empId) throws Exception {
		logger.info("{}", empId);

		model.addAttribute("title", "Agent Present");
		model.addAttribute("empId", empId);
		return "agent-present";
	}

	@RequestMapping(value = "/agent/setting")
	public String viewMiniWallBoardSetting(Model model, HttpServletRequest request,
			@RequestParam(value = "empId") String empId) throws Exception {
		logger.info("{}", empId);

		model.addAttribute("title", "Private Setting");
		model.addAttribute("empId", empId);
		return "private-setting";
	}

	/**
	 * 메서드 설명	: 요청된 사번에 대한 통계 제공
	 * @Method Name : getAgentStat
	 * @date   		: 2023. 11. 22.
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
	@GetMapping(value = "/agent/stat/private", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getAgentStatPrivate(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception {
//		logger.info("{}", param);
		JSONObject result = new JSONObject();
		JSONArray list = miniWallBoardService.getAgentStat(param);
		result.put("list", list);
		result.put("status", 200);
//		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/agent/stat/total/skill", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getSkillStatTotal(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception {
//		logger.info("{}", param);
		JSONObject result = new JSONObject();
		JSONArray list = miniWallBoardService.getSkillStat(param);
		result.put("list", list);
		result.put("status", 200);
//		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/agent/stat/total/queue", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getQueueStatTotal(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception {
//		logger.info("{}", param);
		JSONObject result = new JSONObject();
		JSONArray list = miniWallBoardService.getQueueStat(param);
		result.put("list", list);
		result.put("status", 200);
//		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/agent/setting/skillgroup", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getSkillGroupList(HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		ObjectMapper om = new ObjectMapper();
		JSONArray ja = new JSONArray(om.writeValueAsString(miniWallBoardService.getSkillGroupList()));
		result.put("data", ja);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/agent/setting/queuegroup", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getQueueGroupList(HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		ObjectMapper om = new ObjectMapper();
		JSONArray ja = new JSONArray(om.writeValueAsString(miniWallBoardService.getQueueGroupList()));
		result.put("data", ja);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/agent/skill/list", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getPrivateSkillList(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception {
		logger.info("{}", param);

		JSONObject result = new JSONObject();
		ObjectMapper om = new ObjectMapper();
		JSONArray ja = new JSONArray(om.writeValueAsString(miniWallBoardService.getPrivateSkillList(param)));
		result.put("data", ja);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}

	@GetMapping(value = "/agent/queue/list", produces = "application/json; charset=UTF-8")
	public @ResponseBody String getPrivateQueueList(HttpServletRequest request,
			@RequestParam Map<String, Object> param) throws Exception {
		logger.info("{}", param);
		JSONObject result = new JSONObject();
		ObjectMapper om = new ObjectMapper();
		JSONArray ja = new JSONArray(om.writeValueAsString(miniWallBoardService.getPrivateQueueList(param)));
		result.put("data", ja);

		result.put("status", 200);
		logger.info("{}", result);
		return result.toString();
	}
}
