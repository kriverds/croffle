package com.soluvis.croffle.v1.lgup.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.soluvis.croffle.v1.lgup.config.APIMServiceHeaderConfig;

@FeignClient(name = "APIMClientPrivateService", url = "${apim.pv.url}", configuration = APIMServiceHeaderConfig.class)
public interface APIMClientPrivateService {

	@PostMapping(value="/uplus/intuser/pv/ea/cm/no/insMsg/v1/gnrOnlnMsg", produces="application/json; charset=UTF-8")
	String sendSMS(@RequestBody Map<String,Object> param); // 문자 전송 : IF-CCS-850,851,858,859,861,862
	
	@PostMapping(value="/uplus/intuser/pv/pm/co/co/cucnCti/v1/cslrInfo", produces="application/json; charset=UTF-8")
	String ctiAgentNewInfo(@RequestBody Map<String,Object> param); // CTI 상담사 신규 정보 수신 : IF-COS-117,120,121,124
	
	@PostMapping(value="/uplus/intuser/pv/pm/co/co/cucnCti/v1/cslrLginInfo", produces="application/json; charset=UTF-8")
	String ctiAgentLoginInfo(@RequestBody Map<String,Object> param); // CTI 상담사 로그인 정보 수신 : IF-COS-118,122
	
	@PostMapping(value="/uplus/intuser/pv/pm/co/co/cucnCti/v1/cslrPhclArst", produces="application/json; charset=UTF-8")
	String ctiAgentCallStatic(@RequestBody Map<String,Object> param); // CTI 상담사 통화실적 수신 : IF-COS-119,123
	
}