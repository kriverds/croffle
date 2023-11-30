package com.soluvis.croffle.v1.gcloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoutingSkillManagementServiceTests {

	@Autowired
	RoutingSkillManagementService service;
	
	@Test
	@DisplayName("스킬리스트 가져오기")
	void testGetRoutingSkillList() {
		Map<String,Object> param = new HashMap<>();
		try {
			service.getRoutingSkillList(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("스킬 추가하기")
	void testAddRoutingSkill() {
		Map<String,Object> param = new HashMap<>();
		
		List<Map<String,Object>> userList = new ArrayList<>();
		Map<String,Object> user1 = new HashMap<>();
		user1.put("id", "c3add500-da54-494b-a4de-2ee8ba6255f1");
		userList.add(user1);
		Map<String,Object> user2 = new HashMap<>();
		user1.put("id", "ffa93b72-b4ab-4931-9677-c59f9d77228d");
		userList.add(user2);
		param.put("userList", userList);
		
		List<Map<String,Object>> skillList = new ArrayList<>();
		Map<String,Object> skill1 = new HashMap<>();
		skill1.put("id", "985335ed-5e41-45d8-b2ee-7d09591a683e");
		skill1.put("level", 3);
		skillList.add(skill1);
		Map<String,Object> skill2 = new HashMap<>();
		skill2.put("id", "e78778c3-0397-4dee-a89b-10c1e473be11");
		skill2.put("level", 5);
		skillList.add(skill2);
		param.put("skillList", skillList);
		param.put("operatorId", "DS00018");
		try {
			service.patchUserRoutingskillsBulk(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	@DisplayName("스킬 삭제하기")
//	void testDeleteRoutingSkill() {
//		Map<String,Object> param = new HashMap<>();
//		try {
////			service.getRoutingSkillList(param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("스킬 카트 변경")
//	void testCartRoutingSkill() {
//		Map<String,Object> param = new HashMap<>();
//		try {
////			service.getRoutingSkillList(param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
