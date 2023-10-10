package com.soluvis.croffle.v1.gcloud.controller;

import java.util.Map;

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

import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/v1/api/gcloud/management/agent")
public class AgentManagementController {

	@Autowired
	GCConnector gcconnector;

	Logger logger = LoggerFactory.getLogger(AgentManagementController.class);


	@GetMapping(value="/getExtensionList", produces="application/json; charset=UTF-8")
	public @ResponseBody String getExtensionList(HttpServletRequest request ) throws Exception{
		gcconnector.connect();
		gcconnector.getExtensionList();
		gcconnector.close();

		return "OK";
	}

	@GetMapping(value="/users/list", produces="application/json; charset=UTF-8")
	public @ResponseBody String getAvailableUserList(HttpServletRequest request ) throws Exception{
		gcconnector.connect();
		gcconnector.getAvailableUserList();
		gcconnector.close();

		return "OK";
	}
	
	@GetMapping(value="/users/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String getAvailableUser(HttpServletRequest request 
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		gcconnector.connect();
		gcconnector.getAvailableUser(userId);
		gcconnector.close();

		return "OK";
	}

	@PostMapping(value="/users", produces="application/json; charset=UTF-8")
	public @ResponseBody String postUsers(HttpServletRequest request
			, @RequestBody Map<String,Object> param) throws Exception{

		String name = param.get("name")==null?"":(String)param.get("name");
		String email = param.get("email")==null?"":(String)param.get("email");

		gcconnector.connect();
		gcconnector.postUsers(name, email);
		gcconnector.close();

		return "OK";
	}

	@PatchMapping(value="/users/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String patchUser(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId
			, @RequestBody Map<String,Object> param) throws Exception{

		String department = param.get("department")==null?"":(String)param.get("department");

		gcconnector.connect();
		gcconnector.patchUser(userId, department);
		gcconnector.close();

		return "OK";
	}

	@DeleteMapping(value="/users/{userId}", produces="application/json; charset=UTF-8")
	public @ResponseBody String deleteUser(HttpServletRequest request
			, @PathVariable(name = "userId", required = true) String userId) throws Exception{
		gcconnector.connect();
		gcconnector.deleteUser(userId);
		gcconnector.close();

		return "OK";
	}
}
