package com.soluvis.croffle.v1.lgup.config;

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
public class LGUPInterceptor implements HandlerInterceptor{

	Logger logger = LoggerFactory.getLogger(LGUPInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method =  handlerMethod.getMethod();
		logger.info("{} {} Method[{}]", "#PRH#",
				request.getRequestURI()+(request.getQueryString()==null?"":"?"+request.getQueryString())
				, method);

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("{}\n{}", "#POH#", "");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}


}
