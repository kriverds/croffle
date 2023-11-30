package com.soluvis.croffle.v1.lgup.config;

import java.sql.SQLRecoverableException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluvis.croffle.v1.gcloud.config.GCloudExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice("com.soluvis.croffle.v1.lgup.controller")
public class LGUPExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GCloudExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	    @ResponseBody
	    public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, Exception ex) {
			log.error("###Error", ex);
			Map<String,Object> resultMap = new HashMap<>();
			HttpStatus hs = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			resultMap.put("timestamp", sdf.format(new Date()));

			String errorMsg = "";
			try {
				if(ex instanceof ClassCastException) {
					hs = HttpStatus.BAD_REQUEST;
					errorMsg = "check parameter type";
				}else if(ex instanceof SQLRecoverableException) {
					hs = HttpStatus.INTERNAL_SERVER_ERROR;
					errorMsg = "DB Connection ERROR";
				}else if(ex instanceof MyBatisSystemException) {
					hs = HttpStatus.INTERNAL_SERVER_ERROR;
					errorMsg = "Mybatis ERROR";
				}else if(ex instanceof JSONException) {
					hs = HttpStatus.BAD_REQUEST;
					errorMsg = "JSON ERROR";
				}else {
					hs = HttpStatus.INTERNAL_SERVER_ERROR;
					errorMsg = "ERROR";
				}
			} catch (Exception e) {
				log.error("Error", e);
			}
			resultMap.put("status", hs.value());
			resultMap.put("error", hs);
			resultMap.put("exceptionMessage", ex.getMessage());
			resultMap.put("message", errorMsg);

	        return new ResponseEntity<>(resultMap, hs);
	    }
}