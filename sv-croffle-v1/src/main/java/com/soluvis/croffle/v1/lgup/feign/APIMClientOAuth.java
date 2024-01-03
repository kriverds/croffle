package com.soluvis.croffle.v1.lgup.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 클래스 설명	: APIM OAuth 관련
 * @Class Name 	: APIMClientOAuth
 * @date   		: 2024. 1. 2.
 * @author   	: Kriverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 * 
 */
@FeignClient(name = "APIMClientOAuth", url = "${apim.pv.url}")
public interface APIMClientOAuth {

	@GetMapping(value="/uplus/intuser/oauth2/token", produces="application/x-www-form-urlencoded")
	String getAccessToken(@RequestParam Map<String,Object> param);
}
