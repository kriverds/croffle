package com.soluvis.croffle.v1.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController {

	Logger logger = LoggerFactory.getLogger(SchedulerController.class);

	@RequestMapping(value="")
	public String index(Model model) throws Exception{
		model.addAttribute("title", "Home");
		return "scheduler/index";
	}

	@RequestMapping(value="/options")
	public String scheduler(Model model) throws Exception{
		model.addAttribute("title", "Options");
		return "scheduler/pages/options";
	}
}
