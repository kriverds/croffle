package com.soluvis.croffle.v1.lgup.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.mypurecloud.sdk.v2.ApiException;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice("com.soluvis.croffle.v1.lgup.controller")
public class LGUPExceptionHandler extends DefaultHandlerExceptionResolver{

	@Autowired
	GCConnector gcconnector;

	private static final Logger log = LoggerFactory.getLogger(LGUPExceptionHandler.class);

	@Override
	@ExceptionHandler(Exception.class)
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.error("###Error", ex);
		ModelAndView pResult = super.doResolveException(request, response, handler, ex);

		String errorMsg = "";
		if(ex instanceof ApiException) {
			UUID rUUID = (UUID)request.getAttribute("rUUID");
			gcconnector.close(rUUID);
		}else if(ex instanceof ClassCastException) {
			errorMsg = "check parameter type";
		}


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
