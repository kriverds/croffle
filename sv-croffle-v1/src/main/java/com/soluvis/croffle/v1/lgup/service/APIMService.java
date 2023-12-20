package com.soluvis.croffle.v1.lgup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.lgup.feign.APIMClientOAuth;
import com.soluvis.croffle.v1.lgup.feign.APIMClientPrivateService;
import com.soluvis.croffle.v1.lgup.feign.APIMClientPublicService;

@Service
public class APIMService {
	
	@Value("${apim.client-id}")
	String clientId;
	@Value("${apim.secret}")
	String secret;

	@Autowired
	APIMClientOAuth apimClientOAuth;
	
	@Autowired
	APIMClientPrivateService apimClientPrivateService;
	
	@Autowired
	APIMClientPublicService apimClientPublicService;

	Logger logger = LoggerFactory.getLogger(APIMService.class);
	
	public String getAccessToken(Map<String, Object> param){
		Map<String, Object> map = new HashMap<>();
		map.put("grant_type", "client_credentials"); // 인증타입
		map.put("client_id", clientId); // 인증타입
		map.put("client_secret", secret); // 인증타입
		
		String result = apimClientOAuth.getAccessToken(map);
		logger.info("{}", result);

		return result;
	}

	public JSONObject sendSMS(Map<String, Object> param) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("msgSendReqId", ""); // 메시지발송요청ID
		map.put("msgEvetReqId", ""); // 메시지이벤트요청ID
		map.put("sendReqDttm", ""); // 발송요청일시
		map.put("entrId", ""); // 가입ID
		map.put("encnId", ""); // 가입계약ID
		//#필수
		map.put("rcpNo", "01071733413"); // 수신번호
		map.put("rplyNo", "MTEO"); // 회신번호
		//#필수
		map.put("msgFomId", "MF_NUAR_G000001"); // 메시지양식ID
		//#필수
		map.put("msgCntn", "MMS 테스트 메시지 생성"); // 메시지내용
		map.put("mulaSendLangCdv", ""); // 다국어발송언어코드
		map.put("mulaMsgCntn", ""); // 다국어메시지내용
		map.put("adMsgFomId", ""); // 광고메시지양식ID
		map.put("adMsgCntn", ""); // 광고메시지내용
		//#필수
		map.put("msgSendJobDivsCd", "NUNO"); // 메시지발송업무구분코드
		//#필수
		map.put("msgFomSendFormCd", "MMS"); // 메시지양식발송형태코드
		map.put("onlnYn", ""); // 온라인여부
		map.put("piorSendYn", ""); // 우선발송여부
		map.put("lawcMndtSendYn", ""); // 법정필수여부
		map.put("lrgSendYn", ""); // 대량발송여부
		map.put("ocmpSendYn", ""); // 타사발송여부
		map.put("arerYn", ""); // 연체여부 
		map.put("msgSendRsvId", ""); // 메시지발송예약ID 
		map.put("rsvYn", ""); // 예약여부 
		map.put("hldrCustId", ""); // 명의자고객ID 
		map.put("billAcntId", ""); // 청구계정ID 
		map.put("dealCd", ""); // 대리젘코드
		//#필수
		map.put("userId", ""); // 전체사용자ID
		map.put("custRspoSqno", ""); // 고객응대누적번호
		map.put("baseYymm", ""); // 기준년월 
		map.put("dstbGrpSqno", ""); // 분배그룹누적번호
		map.put("wapUrl", ""); // WAP URL
		map.put("teleId", ""); // 텔레서비스ID
		map.put("sendWorkDttm", ""); // 발송작업일시
		map.put("sendNo", ""); // 발송번호
		map.put("rlMsgFomSendFormCd", ""); // 실제발송형태코드
		map.put("sendRsltCd", ""); // 발송결과코드
		map.put("errCntn", ""); // 에러내용
		map.put("rlSendMsgCntn", ""); // 실제발송메시지내용 
		map.put("kkaoMsgFomId", ""); // 카카오메시지양식ID
		map.put("btnOponCntn", ""); // 버튼옵션내용
		//#필수
		map.put("msgCrteKdCd", "AUTO"); // 메시지생성유형코드
		map.put("adRcpAgrYn", ""); // 광고수신동의여부
		map.put("lttrRcpDenyYn", ""); // 문자수신거부여부 
		return new JSONObject(apimClientPrivateService.sendSMS(map));
	}
	
	public JSONObject ctiAgentInfo(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("clsYymm", "202103"); // 마감년월 #필수
		map.put("cslrId", "1902010"); // 상담원ID #필수
		map.put("cslrNm", "홍선미"); // 상담원명
		map.put("jobDt", "20070110"); // 업무일자
		map.put("etcoDt", "20070110"); // 입사일자
		map.put("rtrDt", "19990101"); // 퇴직일자
		map.put("lginCnt", "0"); // 로그인건수
		map.put("intgUserId", "sjm73"); // 통합사용자ID
		map.put("dataCrteDttm", "2022/06/24 16:53:42"); // 데이터생성일시
		list.add(map);
		rootMap.put("ctiCslrInfoList", list);
		return new JSONObject(apimClientPrivateService.ctiAgentNewInfo(rootMap));
	}
	
	public JSONObject ctiAgentLoginInfo(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("clsDt", "20210301"); // 마감일자 #필수
		map.put("cslrId", "2100801"); // 상담원ID #필수
		map.put("lginCnt", "4"); // 로그인건수
		map.put("dataCrteDttm", "2022/06/24 16:53:42"); // 데이터생성일시
		list.add(map);
		rootMap.put("ctiCslrLginInfoList", list);
		return new JSONObject(apimClientPrivateService.ctiAgentNewInfo(rootMap));
	}
	
	public JSONObject ctiAgentCallStatic(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("clsDt", "20210301"); // 마감일자 #필수
		map.put("cslrId", "2100801"); // 상담원ID #필수
		map.put("cslrGrpId", "21900"); // 상담원그룹ID #필수
		map.put("queId", "5351"); // 큐ID #필수
		map.put("cslrNm", "최태근"); // 상담원명
		map.put("cslrGrpNm", "지원그룹"); // 상담원그룹명
		map.put("queNm", "U+ Shop 가입"); // 큐명
		map.put("inbnPhclCnt", "3"); // 인바운드통화건수
		map.put("inbnPhclTmSec", "938"); // 인바운드통화시간초
		map.put("obndPhclCnt", "0"); // 아웃바운드통화건수
		map.put("obndPhclTmSec", "0"); // 아웃바운드통화시간초
		map.put("cvrtPhclCnt", "2"); // 전환통화건수
		map.put("cvrtPhclTmSec", "1078"); // 전환통화시간초
		map.put("dataCrteDttm", "2022/06/24 16:53:42"); // 데이터생성일시
		list.add(map);
		rootMap.put("cslrPhclArstList", list);
		return new JSONObject(apimClientPrivateService.ctiAgentNewInfo(rootMap));
	}
	
	public JSONObject postMblArst(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ttznDt", "20220528"); // 집계일자 #필수
		map.put("endTm", "23"); // 종료시간 #필수
		map.put("intgUserId", "test2"); // 통합사용자ID #필수
		map.put("ctiDeptCd", "4055"); // CTI부서코드 #필수
		map.put("ctiDeptGrpCd", "12123"); // CTI부서그룹코드 #필수
		map.put("emno", "2222"); // 사원번호 #필수
		map.put("empNm", "염인익2"); // 사원명
		map.put("grpNm", "U+Shop상담"); // 그룹명 
		map.put("inbnCallCnt", 20); // 인바운드콜건수 
		map.put("inbnCallTmct", 20); // 인바운드콜시간수 
		map.put("psecInbnCallCnt", 20); // 초당인바운드콜건수 
		map.put("obndCallCnt", 20); // 아웃바운드콜건수
		map.put("obndTmct", 20); // 아웃바운드시간수 
		map.put("cvrtCallCnt", 20); // 전환콜건수 
		map.put("psecObndCallCnt", 20); // 초당아웃바운드콜건수 
		map.put("etcCallCnt", 20); // 기타콜건수 
		map.put("cvrtCallRqmScnt", 20); // 전환콜소요초수
		map.put("psecEtcCallCnt", 20); // 초당기타콜건수 
		map.put("etcCallRqmScnt", 20); // 기타콜소요초수 
		map.put("wrkTmRqmScnt", 20); // 근무시간소요초수
		map.put("phclRqmScnt", 20); // 통화소요초수 
		map.put("logtRqmScnt", 20); // 로그아웃소요초수
		map.put("lginRqmScnt", 20); // 로그인소요초수 
		map.put("waitRqmScnt1", 20); // 대기소요초수1 
		map.put("prssRqmScnt", 20); // 처리소요초수 
		map.put("allPrssRqmScnt", 20); // 전체처리소요초수 
		map.put("waitRqmScnt", 20); // 대기소요초수
		map.put("restTmRqmScnt", 20); // 휴식시간소요초수 
		map.put("restTmct", 20); // 휴식시간수 
		map.put("eduTmRqmScnt", 20); // 교육시간소요초수 
		map.put("restTmRqmScnt2", 20); // 휴식시간소요초수2 
		map.put("eduTmct", 20); // 교육시간수
		map.put("masgTmRqmScnt", 20); // 안마시간소요초수 
		map.put("psycCnslTmct", 20); //  심리상담시간수
		map.put("mtngTmRqmScnt", 20); // 회의시간소요초수 
		map.put("indvJobTm", 20); // 개인업무시간 
		map.put("etcJobTm1", 20); // 기타업무시간1
		map.put("ltrPrssRqmScnt", 20); // 이후처리소요초수 
		map.put("indvJobRqmScnt", 20); // 개인업무소요초수 
		map.put("clamRqmScnt", 20); // 클레임소요초수 
		map.put("ltrPrssTmct", 20); // 이후처리시간수 
		map.put("custCnslRqmScnt", 20); // 고객상담소요초수
		map.put("etcJobRqmScnt", 20); // 기타업무소요초수 
		map.put("dscsRqmScnt", 20); // 협의소요초수 
		map.put("jobDrctRqmScnt", 20); // 업무지시소요초수 
		map.put("difJobRqmScnt", 20); // 차이업무소요초수 
		map.put("outsInquRqmScnt", 20); // 외부문의소요초수
		map.put("ddAvrPhclCnt", 20); //  일평균통화건수
		map.put("rcptLtrPrssRqmScnt", 20); // 접수이후처리소요초수 
		map.put("wrkTmRqmScnt1", 20); // 근무시간소요초수1 
		map.put("tmznAvrPhclCnt", 20); // 시간대평균통화건수
		list.add(map);
		rootMap.put("ctiMblCslrArst", list);
		return new JSONObject(apimClientPublicService.postMblArst(rootMap));
	}
	public JSONObject putMblArst(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ttznDt", "20220528"); // 집계일자 #필수
		map.put("endTm", "23"); // 종료시간 #필수
		map.put("intgUserId", "test2"); // 통합사용자ID #필수
		map.put("ctiDeptCd", "4055"); // CTI부서코드 #필수
		map.put("ctiDeptGrpCd", "12123"); // CTI부서그룹코드 #필수
		map.put("emno", "2222"); // 사원번호 #필수
		map.put("empNm", "염인익2"); // 사원명
		map.put("grpNm", "U+Shop상담"); // 그룹명 
		map.put("inbnCallCnt", 20); // 인바운드콜건수 
		map.put("inbnCallTmct", 20); // 인바운드콜시간수 
		map.put("psecInbnCallCnt", 20); // 초당인바운드콜건수 
		map.put("obndCallCnt", 20); // 아웃바운드콜건수
		map.put("obndTmct", 20); // 아웃바운드시간수 
		map.put("cvrtCallCnt", 20); // 전환콜건수 
		map.put("psecObndCallCnt", 20); // 초당아웃바운드콜건수 
		map.put("etcCallCnt", 20); // 기타콜건수 
		map.put("cvrtCallRqmScnt", 20); // 전환콜소요초수
		map.put("psecEtcCallCnt", 20); // 초당기타콜건수 
		map.put("etcCallRqmScnt", 20); // 기타콜소요초수 
		map.put("wrkTmRqmScnt", 20); // 근무시간소요초수
		map.put("phclRqmScnt", 20); // 통화소요초수 
		map.put("logtRqmScnt", 20); // 로그아웃소요초수
		map.put("lginRqmScnt", 20); // 로그인소요초수 
		map.put("waitRqmScnt1", 20); // 대기소요초수1 
		map.put("prssRqmScnt", 20); // 처리소요초수 
		map.put("allPrssRqmScnt", 20); // 전체처리소요초수 
		map.put("waitRqmScnt", 20); // 대기소요초수
		map.put("restTmRqmScnt", 20); // 휴식시간소요초수 
		map.put("restTmct", 20); // 휴식시간수 
		map.put("eduTmRqmScnt", 20); // 교육시간소요초수 
		map.put("restTmRqmScnt2", 20); // 휴식시간소요초수2 
		map.put("eduTmct", 20); // 교육시간수
		map.put("masgTmRqmScnt", 20); // 안마시간소요초수 
		map.put("psycCnslTmct", 20); //  심리상담시간수
		map.put("mtngTmRqmScnt", 20); // 회의시간소요초수 
		map.put("indvJobTm", 20); // 개인업무시간 
		map.put("etcJobTm1", 20); // 기타업무시간1
		map.put("ltrPrssRqmScnt", 20); // 이후처리소요초수 
		map.put("indvJobRqmScnt", 20); // 개인업무소요초수 
		map.put("clamRqmScnt", 20); // 클레임소요초수 
		map.put("ltrPrssTmct", 20); // 이후처리시간수 
		map.put("custCnslRqmScnt", 20); // 고객상담소요초수
		map.put("etcJobRqmScnt", 20); // 기타업무소요초수 
		map.put("dscsRqmScnt", 20); // 협의소요초수 
		map.put("jobDrctRqmScnt", 20); // 업무지시소요초수 
		map.put("difJobRqmScnt", 20); // 차이업무소요초수 
		map.put("outsInquRqmScnt", 20); // 외부문의소요초수
		map.put("ddAvrPhclCnt", 20); //  일평균통화건수
		map.put("rcptLtrPrssRqmScnt", 20); // 접수이후처리소요초수 
		map.put("wrkTmRqmScnt1", 20); // 근무시간소요초수1 
		map.put("tmznAvrPhclCnt", 20); // 시간대평균통화건수
		list.add(map);
		rootMap.put("ctiMblCslrArst", list);
		
		return new JSONObject(apimClientPublicService.putMblArst(rootMap));
	}
	public JSONObject deleteMblArst(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ttznDt", "20220528"); // 집계일자 #필수
		map.put("endTm", "23"); // 종료시간 #필수
		map.put("intgUserId", "test2"); // 통합사용자ID #필수
		map.put("ctiDeptCd", "4055"); // CTI부서코드 #필수
		map.put("ctiDeptGrpCd", "12123"); // CTI부서그룹코드 #필수
		map.put("emno", "2222"); // 사원번호 #필수
		list.add(map);
		rootMap.put("ctiMblCslrArst", list);
		return new JSONObject(apimClientPublicService.deleteMblArst(rootMap));
	}
	
	public JSONObject postHmArst(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ttznDt", "20220528"); // 집계일자 #필수
		map.put("endTm", "23"); // 종료시간 #필수
		map.put("intgUserId", "test2"); // 통합사용자ID #필수
		map.put("ctiDeptCd", "4055"); // CTI부서코드 #필수
		map.put("ctiDeptGrpCd", "12123"); // CTI부서그룹코드 #필수
		map.put("emno", "2222"); // 사원번호 #필수
		map.put("empNm", "염인익2"); // 사원명
		map.put("cntrNm", "홈_서울2"); // 센터명
		map.put("ctusMmct", 20); // 근속개월수
		map.put("inbnCallTmct", 20); // 인바운드콜시간수
		map.put("inbnCallCnt", 20); // 인바운드콜건수 
		map.put("obndTmct", 20); // 아웃바운드시간수 
		map.put("obndCallCnt", 20); // 아웃바운드콜건수
		map.put("itntQueRspoCnt", 20); // 인터넷큐응대건수
		map.put("regTmRqmScnt", 20); // 등록시간소요초수
		map.put("prssRqmScnt", 20); // 처리소요초수
		map.put("itrPrssTmct", 20); // 이후처리시간수
		map.put("waitRqmScnt", 20); // 대기소요초수
		map.put("restTmct", 20); // 휴식시간수
		map.put("eduTmRqmScnt", 20); // 교육시간소요초수
		map.put("masgTmRqmScnt", 20); // 안마시간소요초수
		map.put("eduTmct", 20); // 교육시간수
		map.put("psycCnslTmct", 20); // 심리상담시간수
		map.put("etcRqmScnt", 20); // 기타소요초수
		map.put("svrtCallCnt", 20); // 전환콜건수
		map.put("cvrtCallRqmScnt", 20); // 전환콜소요초수
		list.add(map);
		rootMap.put("ctiHmCslrArst", list);
		return new JSONObject(apimClientPublicService.postHmArst(rootMap));
	}
	public JSONObject putHmArst(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ttznDt", "20220528"); // 집계일자 #필수
		map.put("endTm", "23"); // 종료시간 #필수
		map.put("intgUserId", "test2"); // 통합사용자ID #필수
		map.put("ctiDeptCd", "4055"); // CTI부서코드 #필수
		map.put("ctiDeptGrpCd", "12123"); // CTI부서그룹코드 #필수
		map.put("emno", "2222"); // 사원번호 #필수
		map.put("empNm", "염인익2"); // 사원명
		map.put("cntrNm", "홈_서울2"); // 센터명
		map.put("ctusMmct", 20); // 근속개월수
		map.put("inbnCallTmct", 20); // 인바운드콜시간수
		map.put("inbnCallCnt", 20); // 인바운드콜건수 
		map.put("obndTmct", 20); // 아웃바운드시간수 
		map.put("obndCallCnt", 20); // 아웃바운드콜건수
		map.put("itntQueRspoCnt", 20); // 인터넷큐응대건수
		map.put("regTmRqmScnt", 20); // 등록시간소요초수
		map.put("prssRqmScnt", 20); // 처리소요초수
		map.put("itrPrssTmct", 20); // 이후처리시간수
		map.put("waitRqmScnt", 20); // 대기소요초수
		map.put("restTmct", 20); // 휴식시간수
		map.put("eduTmRqmScnt", 20); // 교육시간소요초수
		map.put("masgTmRqmScnt", 20); // 안마시간소요초수
		map.put("eduTmct", 20); // 교육시간수
		map.put("psycCnslTmct", 20); // 심리상담시간수
		map.put("etcRqmScnt", 20); // 기타소요초수
		map.put("svrtCallCnt", 20); // 전환콜건수
		map.put("cvrtCallRqmScnt", 20); // 전환콜소요초수
		list.add(map);
		rootMap.put("ctiHmCslrArst", list);
		return new JSONObject(apimClientPublicService.putHmArst(rootMap));
	}
	public JSONObject deleteHmArst(Map<String, Object> param) throws Exception{
		Map<String, Object> rootMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("ttznDt", "20220528"); // 집계일자 #필수
		map.put("endTm", "23"); // 종료시간 #필수
		map.put("intgUserId", "test2"); // 통합사용자ID #필수
		map.put("ctiDeptCd", "4055"); // CTI부서코드 #필수
		map.put("ctiDeptGrpCd", "12123"); // CTI부서그룹코드 #필수
		map.put("emno", "2222"); // 사원번호 #필수
		list.add(map);
		rootMap.put("ctiHmCslrArst", list);
		return new JSONObject(apimClientPublicService.deleteHmArst(rootMap));
	}

}
