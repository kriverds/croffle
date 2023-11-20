package com.soluvis.croffle.v1.scheduler.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice("com.soluvis.croffle.v1.scheduler.controller")
public class SchedulerExceptionHandler extends DefaultHandlerExceptionResolver{

	private static final Logger log = LoggerFactory.getLogger(SchedulerExceptionHandler.class);

	@Override
	@ExceptionHandler(Exception.class)
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.error("###Error", ex);
		ModelAndView pResult = super.doResolveException(request, response, handler, ex);

		String errorMsg = "";

		if(pResult == null) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}
		return pResult;
	}


}
