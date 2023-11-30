package com.soluvis.croffle.v1.gcloud.config;

import java.sql.SQLRecoverableException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mypurecloud.sdk.v2.ApiException;
import com.soluvis.croffle.v1.gcloud.engine.GCConnector;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice("com.soluvis.croffle.v1.gcloud.controller")
public class GCloudExceptionHandler {

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
			if(ex instanceof ApiException) {
				hs = HttpStatus.BAD_REQUEST;
				UUID rUUID = (UUID)request.getAttribute("rUUID");
				GCConnector.close(rUUID);
//				GCConnector.getJobList().clear();
				errorMsg = "GCloud API ERROR";
			}else if(ex instanceof ClassCastException) {
				hs = HttpStatus.BAD_REQUEST;
				errorMsg = "check parameter type";
			}else if(ex instanceof SQLRecoverableException) {
				hs = HttpStatus.INTERNAL_SERVER_ERROR;
				errorMsg = "DB Connection ERROR";
			}else if(ex instanceof MyBatisSystemException) {
				hs = HttpStatus.INTERNAL_SERVER_ERROR;
				errorMsg = "Mybatis ERROR";
			}else if(ex instanceof BadSqlGrammarException) {
				hs = HttpStatus.INTERNAL_SERVER_ERROR;
				errorMsg = "SQL ERROR";
			}else if(ex instanceof JSONException) {
				hs = HttpStatus.BAD_REQUEST;
				errorMsg = "JSON ERROR";
			}else {
				hs = HttpStatus.INTERNAL_SERVER_ERROR;
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
