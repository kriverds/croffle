package com.soluvis.croffle.v1.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice("com.soluvis.croffle.v1.sample.controller")
public class SampleExceptionHandler extends DefaultHandlerExceptionResolver{


	private static final Logger log = LoggerFactory.getLogger(SampleExceptionHandler.class);


	@Override
	@ExceptionHandler(Exception.class)
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.error("message", ex);

		return super.doResolveException(request, response, handler, ex);
	}


}
