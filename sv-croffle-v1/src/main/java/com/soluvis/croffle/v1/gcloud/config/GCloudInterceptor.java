package com.soluvis.croffle.v1.gcloud.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GCloudInterceptor implements HandlerInterceptor{

	Logger logger = LoggerFactory.getLogger(GCloudInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method =  handlerMethod.getMethod();
		logger.info("{} {} Controller[{}] Method[{}]", "#PRH#",
				request.getRequestURI()+(request.getQueryString()==null?"":"?"+request.getQueryString())
				, handlerMethod.getBean(), method);

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("{}\n{}", "#POH#", "");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}


}
