package com.soluvis.croffle.v1.scheduler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SchedulerInterceptor implements HandlerInterceptor {

	Logger logger = LoggerFactory.getLogger(SchedulerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		/*
		 * HandlerMethod handlerMethod = (HandlerMethod) handler; Method method =
		 * handlerMethod.getMethod();
		 */
		logger.info("{} {} {}", "#PRH#",
				request.getMethod() ,request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("{}\n{}", "#POH#", "");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
