package com.soluvis.croffle.v1.gcloud.config;

import java.sql.SQLRecoverableException;
import java.util.UUID;

import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.mypurecloud.sdk.v2.ApiException;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice("com.soluvis.croffle.v1.gcloud.controller")
public class GCloudExceptionHandler extends DefaultHandlerExceptionResolver{

	private static final Logger log = LoggerFactory.getLogger(GCloudExceptionHandler.class);

	@Override
	@ExceptionHandler(Exception.class)
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.error("###Error", ex);
		ModelAndView pResult = super.doResolveException(request, response, handler, ex);

		String errorMsg = "";
		try {
			if(ex instanceof ApiException) {
				UUID rUUID = (UUID)request.getAttribute("rUUID");
				GCConnector.close(rUUID);
				errorMsg = "GCloud API Error";
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
			}else if(ex instanceof ClassCastException) {
				errorMsg = "check parameter type";
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
			}else if(ex instanceof SQLRecoverableException) {
				errorMsg = "DB Connection Error";
				response.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, errorMsg);
			}else if(ex instanceof MyBatisSystemException) {
				errorMsg = "Mybatis Error";
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
			}else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
			}
		} catch (Exception e) {
			logger.error("{}", e);
		}

		return pResult;
	}


}
