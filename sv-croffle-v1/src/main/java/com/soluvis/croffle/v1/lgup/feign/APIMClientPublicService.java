package com.soluvis.croffle.v1.lgup.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.soluvis.croffle.v1.lgup.config.APIMServiceHeaderConfig;

@FeignClient(name = "APIMClientPublicService", url = "${apim.pb.url}", configuration = APIMServiceHeaderConfig.class)
public interface APIMClientPublicService {

	// 모바일 고객센터 상담사실적집계 전송 : IF-CCS-856
	@PostMapping(value = "/uplus/intuser/pb/cc/cs/cs/cucnCslrMgmt/v1/mblArst", produces = "application/json; charset=UTF-8")
	String postMblArst(@RequestBody Map<String, Object> param);
	@PutMapping(value = "/uplus/intuser/pb/cc/cs/cs/cucnCslrMgmt/v1/mblArst", produces = "application/json; charset=UTF-8")
	String putMblArst(@RequestBody Map<String, Object> param);
	@DeleteMapping(value = "/uplus/intuser/pb/cc/cs/cs/cucnCslrMgmt/v1/mblArst", produces = "application/json; charset=UTF-8")
	String deleteMblArst(@RequestBody Map<String, Object> param);

	// 홈/기업 고객센터 상담사 실적 집계 등록 : IF-CCS-857
	@PostMapping(value = "/uplus/intuser/pb/cc/cs/cs/cucnCslrMgmt/v1/hmArst", produces = "application/json; charset=UTF-8")
	String postHmArst(@RequestBody Map<String, Object> param);
	@PutMapping(value = "/uplus/intuser/pb/cc/cs/cs/cucnCslrMgmt/v1/hmArst", produces = "application/json; charset=UTF-8")
	String putHmArst(@RequestBody Map<String, Object> param);
	@DeleteMapping(value = "/uplus/intuser/pb/cc/cs/cs/cucnCslrMgmt/v1/hmArst", produces = "application/json; charset=UTF-8")
	String deleteHmArst(@RequestBody Map<String, Object> param);
}