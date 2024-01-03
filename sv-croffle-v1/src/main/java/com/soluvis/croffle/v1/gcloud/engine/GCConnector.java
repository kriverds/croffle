package com.soluvis.croffle.v1.gcloud.engine;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.api.AnalyticsApi;
import com.mypurecloud.sdk.v2.api.ConversationsApi;
import com.mypurecloud.sdk.v2.api.GroupsApi;
import com.mypurecloud.sdk.v2.api.NotificationsApi;
import com.mypurecloud.sdk.v2.api.OutboundApi;
import com.mypurecloud.sdk.v2.api.RoutingApi;
import com.mypurecloud.sdk.v2.api.StationsApi;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.connector.ApiClientConnectorProperty;
import com.mypurecloud.sdk.v2.connector.apache.ApacheHttpClientConnectorProvider;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;
import com.mypurecloud.sdk.v2.extensions.notifications.NotificationHandler;
import com.mypurecloud.sdk.v2.model.AnalyticsConversationQueryResponse;
import com.mypurecloud.sdk.v2.model.Campaign;
import com.mypurecloud.sdk.v2.model.CampaignEntityListing;
import com.mypurecloud.sdk.v2.model.CampaignProgress;
import com.mypurecloud.sdk.v2.model.CampaignStats;
import com.mypurecloud.sdk.v2.model.Channel;
import com.mypurecloud.sdk.v2.model.ConversationAggregateQueryClause;
import com.mypurecloud.sdk.v2.model.ConversationAggregateQueryFilter;
import com.mypurecloud.sdk.v2.model.ConversationAggregateQueryPredicate;
import com.mypurecloud.sdk.v2.model.ConversationAggregateQueryPredicate.DimensionEnum;
import com.mypurecloud.sdk.v2.model.ConversationAggregateQueryResponse;
import com.mypurecloud.sdk.v2.model.ConversationAggregationQuery;
import com.mypurecloud.sdk.v2.model.ConversationAggregationQuery.MetricsEnum;
import com.mypurecloud.sdk.v2.model.ConversationQuery;
import com.mypurecloud.sdk.v2.model.CreateUser;
import com.mypurecloud.sdk.v2.model.Group;
import com.mypurecloud.sdk.v2.model.GroupCreate;
import com.mypurecloud.sdk.v2.model.GroupCreate.VisibilityEnum;
import com.mypurecloud.sdk.v2.model.GroupEntityListing;
import com.mypurecloud.sdk.v2.model.GroupMembersUpdate;
import com.mypurecloud.sdk.v2.model.GroupUpdate;
import com.mypurecloud.sdk.v2.model.Queue;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueMember;
import com.mypurecloud.sdk.v2.model.QueueMemberEntityListing;
import com.mypurecloud.sdk.v2.model.RoleDivisionGrants;
import com.mypurecloud.sdk.v2.model.RoleDivisionPair;
import com.mypurecloud.sdk.v2.model.RoutingSkill;
import com.mypurecloud.sdk.v2.model.SegmentDetailQueryClause;
import com.mypurecloud.sdk.v2.model.SegmentDetailQueryFilter;
import com.mypurecloud.sdk.v2.model.SegmentDetailQueryPredicate;
import com.mypurecloud.sdk.v2.model.SkillEntityListing;
import com.mypurecloud.sdk.v2.model.StationEntityListing;
import com.mypurecloud.sdk.v2.model.UpdateUser;
import com.mypurecloud.sdk.v2.model.User;
import com.mypurecloud.sdk.v2.model.UserAggregateQueryClause;
import com.mypurecloud.sdk.v2.model.UserAggregateQueryFilter;
import com.mypurecloud.sdk.v2.model.UserAggregateQueryPredicate;
import com.mypurecloud.sdk.v2.model.UserAggregateQueryResponse;
import com.mypurecloud.sdk.v2.model.UserAggregationQuery;
import com.mypurecloud.sdk.v2.model.UserEntityListing;
import com.mypurecloud.sdk.v2.model.UserQueue;
import com.mypurecloud.sdk.v2.model.UserQueueEntityListing;
import com.mypurecloud.sdk.v2.model.UserRoutingSkill;
import com.mypurecloud.sdk.v2.model.UserRoutingSkillPost;
import com.mypurecloud.sdk.v2.model.UserSkillEntityListing;
import com.mypurecloud.sdk.v2.model.WritableEntity;
import com.neovisionaries.ws.client.WebSocketException;
import com.soluvis.croffle.v1.gcloud.engine.listener.ChannelMetadataListener;
import com.soluvis.croffle.v1.util.GVal;

/**
 * 클래스 설명 : GCloud와 연결 및 Interface 제공.
 *
 * @Class Name : GCConnector
 * @date : 2023. 9. 27.
 * @author : Kriverds
 * @version : 1.0 ----------------------------------------
 * @notify
 *
 */
@Component
public class GCConnector {

	private static final Logger logger = LoggerFactory.getLogger(GCConnector.class);

	// GS개발
//	private static final String CLIENT_ID = "ecf91a65-0043-4da7-92eb-bdd83a5c2cb6";
//	private static final String CLIENT_SECRET = "nhK4fw7a3vh-BPCdfRe311m1rWDbnXJoATteyyeVfvk";
//	private static final String DIVISION_HOME = "fa910857-f776-4dd5-91a9-1d245882ab11";
	// uplusdev
//	private static final String CLIENT_ID = "e9cc4eaa-5545-4e69-885a-e828949b6204";
//	private static final String CLIENT_SECRET = "8zoDLvhXlX-O3C38HD01A69uVkvX3u30M4tm08C8bk0";
//	private static final String DIVISION_HOME = "0aebaa53-2e66-4720-acdb-8a766e2ea6ae";
	// uplus
//	private static final String CLIENT_ID = "0a89dcf2-1296-4142-a101-849ed636f7f0";
//	private static final String CLIENT_SECRET = "ZarK8MInb98lEN6ekhPUON3YUd6eTqgzPa2yuitXmf8";

	@Value("${gcloud.division.home.id}")
	private String divisionHome;
	@Value("${gcloud.division.mobile.id}")
	private String divisionMobile;
	@Value("${gcloud.division.business.id}")
	private String divisionBuisiness;

	private static NotificationHandler notificationHandler = null;

	private static List<UUID> jobList = new ArrayList<>();

	ObjectMapper om = new ObjectMapper();

	/**
	 * 메서드 설명 : GCloud와 연결한다.
	 *
	 * @Method Name : connect
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	public static void connect(UUID uuid) throws IOException, ApiException, WebSocketException {

		if (GCConnector.getJobList().isEmpty()) {
			GCConnector.getJobList().add(uuid);
			logger.info("add Job[{}] Size[{}]", uuid, GCConnector.getJobList().size());
			PureCloudRegionHosts region = PureCloudRegionHosts.ap_northeast_2;
			ApiClient apiClient = ApiClient.Builder.standard().withBasePath(region).withShouldRefreshAccessToken(true)
					.withProperty(ApiClientConnectorProperty.CONNECTOR_PROVIDER,
							new ApacheHttpClientConnectorProvider())
					.build();
			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(GVal.getGcClientId(), GVal.getGcClientSecret());
//			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
			logger.info("{}", "Authentication successful. Access token expires in "
					+ authResponse.getBody().getExpires_in() + " seconds");
			logger.info("{}", authResponse.getBody());

			Configuration.setDefaultApiClient(apiClient);

			NotificationsApi notificationApiInstance = new NotificationsApi();
			Channel channel = notificationApiInstance.postNotificationsChannels();
			logger.info("{}", channel);

			ChannelMetadataListener listenerch = new ChannelMetadataListener();

			notificationHandler = NotificationHandler.Builder.standard().withNotificationListener(listenerch)
					.withAutoConnect(false).build();
			logger.info("{}", "GCConnector connection open");

			notificationHandler.sendPing();
		} else {
			if(!GCConnector.getJobList().contains(uuid) && uuid != null) {
				GCConnector.getJobList().add(uuid);
				logger.info("add Job[{}] Size[{}]", uuid, GCConnector.getJobList().size());
			}
		}

	}

	/**
	 * 메서드 설명 : GCloud와 연결을 종료한다.
	 *
	 * @Method Name : close
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @notify
	 *
	 */
	public static void close(UUID uuid) {
		if(uuid != null) {
			GCConnector.getJobList().remove(uuid);
			logger.info("close Job[{}] Size[{}]", uuid, GCConnector.getJobList().size());
			if (GCConnector.getJobList().isEmpty()) {
				notificationHandler.disconnect();
				logger.info("{}", "GCConnector connection close");
			}
		}
	}

	/**
	 * 메서드 설명 : 내선번호 리스트를 조회한다.
	 *
	 * @Method Name : getExtensionList
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws IOException
	 * @throws ApiException
	 * @notify
	 *
	 */
	public void getExtensionList() throws IOException, ApiException {
		StationsApi apiInstance = new StationsApi();

		Integer pageSize = 50; // Integer | Page size
		Integer pageNumber = 1; // Integer | Page number

		String sortBy = "name"; // String | Sort by
		String name = null; // "name_example"; // String | Name
		String userSelectable = "false"; // "userSelectable_example"; // String | True for stations that the user can
											// select otherwise false
		String webRtcUserId = null; // "webRtcUserId_example"; // String | Filter for the webRtc station of the
									// webRtcUserId
		String id = null; // "id_example"; // String | Comma separated list of stationIds
		String lineAppearanceId = null; // "lineAppearanceId_example"; // String | lineAppearanceId
		StationEntityListing result = apiInstance.getStations(pageSize, pageNumber, sortBy, name, userSelectable,
				webRtcUserId, id, lineAppearanceId);
		logger.info("{}", result);
	}

	/*
	 * #############################################################################
	 * ################################## : 상담사
	 * #############################################################################
	 */

	/**
	 * 메서드 설명 : 사용 가능한 유저 정보를 조회한다.
	 *
	 * @Method Name : getAvailableUser
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public User getAvailableUser(String userId) throws IOException, ApiException, JSONException {
		logger.info("{}", "흠");

		UsersApi apiInstance = new UsersApi();

		List<String> expand = Arrays.asList(); // List<String> | Which fields, if any, to expand
		String integrationPresenceSource = null;// "integrationPresenceSource_example"; // String | Gets an integration
												// presence for users instead of their defaults. This parameter will
												// only be used when presence is provided as an \"expand\". When using
												// this parameter the maximum number of users that can be returned is
												// 100.
		String state = "active"; // ""active"; // String | Only list users of this state

		User result = apiInstance.getUser(userId, expand, integrationPresenceSource, state);

		logger.info("{}", new JSONObject(om.writeValueAsString(result)));
		return result;
	}

	/**
	 * 메서드 설명 : 사용 가능한 유저 리스트를 조회한다.
	 *
	 * @Method Name : getAvailableUserList
	 * @date : 2023. 9. 25.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONObject getAvailableUserList() throws IOException, ApiException, JSONException {

		UsersApi apiInstance = new UsersApi();
		Integer pageSize = 50; // Integer | Page size
		Integer pageNumber = 1; // Integer | Page number
		List<String> id = Arrays.asList(); // List<String> | A list of user IDs to fetch by bulk
		List<String> jabberId = Arrays.asList(); // List<String> | A list of jabberIds to fetch by bulk (cannot be used
													// with the \"id\" parameter)
		String sortOrder = "ASC"; // String | Ascending or descending sort order
		List<String> expand = Arrays.asList(); // List<String> | Which fields, if any, to expand
		String integrationPresenceSource = null;// "integrationPresenceSource_example"; // String | Gets an integration
												// presence for users instead of their defaults. This parameter will
												// only be used when presence is provided as an \"expand\". When using
												// this parameter the maximum number of users that can be returned is
												// 100.
		String state = "active"; // ""active"; // String | Only list users of this state

		UserEntityListing result = apiInstance.getUsers(pageSize, pageNumber, id, jabberId, sortOrder, expand,
				integrationPresenceSource, state);
		List<User> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (User child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 유저를 생성한다.
	 *
	 * @Method Name : postUsers
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param name
	 * @param department
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify - email이 Key로 중복 발생시 ApiException 에러
	 */
	public JSONObject postUsers(String name, String email, String department, String title) throws IOException, ApiException, JSONException {
		UsersApi apiInstance = new UsersApi();

		CreateUser body = new CreateUser();
		body.setName(name);
		body.setEmail(email);
		body.setDivisionId(divisionHome);
		body.setDepartment(department);
		logger.info("{}", new JSONObject(om.writeValueAsString(body)));

		User result = apiInstance.postUsers(body);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 유저를 수정한다.
	 *
	 * @Method Name : patchUser
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @param department
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify - 현재 상담사의 Version 정보를 가져와서 세팅해 주어야 함. 이상한 Version 정보나 세팅 안할 시
	 *         ApiException 발생
	 */
	public JSONObject patchUser(String userId, String department) throws IOException, ApiException, JSONException {
		UsersApi apiInstance = new UsersApi();

		UpdateUser body = new UpdateUser();
		User preUser = getAvailableUser(userId);

		body.setVersion(preUser.getVersion());
		body.setDepartment(department);

		User result = apiInstance.patchUser(userId, body);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 유저를 삭제한다.
	 *
	 * @Method Name : deleteUser
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @throws ApiException
	 * @throws IOException
	 * @notify - 계속 지워도 에러발생 안함.
	 */
	public void deleteUser(String userId) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

		apiInstance.deleteUser(userId);
	}

	/**
	 * 메서드 설명	: Division_Role Pair를 삽입한다.
	 * @Method Name : postAuthorizationSubjectBulkadd
	 * @date   		: 2023. 12. 21.
	 * @author   	: Kriverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject postAuthorizationSubjectBulkadd(String userId, String divisionId, String roleId) throws IOException, ApiException, JSONException {
		UsersApi apiInstance = new UsersApi();
        List<RoleDivisionPair> rdpList = new ArrayList<>();
        RoleDivisionPair rdp = new RoleDivisionPair();
//        rdp.setDivisionId("232637ae-d261-46e5-92ea-62e8e4696eb5");
//        rdp.setRoleId("72725af5-f4b1-4e8a-90ff-1fdf026fbb98");
        rdp.setDivisionId(divisionId);
        rdp.setRoleId(roleId);
        rdpList.add(rdp);
        RoleDivisionGrants body = new RoleDivisionGrants(); // RoleDivisionGrants | Pairs of role and division IDs
        body.setGrants(rdpList);
        String subjectType = "PC_USER"; // String | what the type of the subject is (PC_GROUP, PC_USER or PC_OAUTH_CLIENT)
        apiInstance.postAuthorizationSubjectBulkadd(userId, body, subjectType);

		return new JSONObject();
	}

	/*
	 * #############################################################################
	 * ################################## : 라우팅스킬
	 * #############################################################################
	 */

	/**
	 * 메서드 설명 : 라우팅스킬 리스트를 조회한다.
	 *
	 * @Method Name : getRoutingSkillList
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getRoutingSkillList() throws IOException, ApiException, JSONException {
		RoutingApi apiInstance = new RoutingApi();
		Integer pageNumber = 1; // Integer | Page number
		Integer pageSize = 100; // Integer | Page size
		String name = null; // String | Filter by queue name
		List<String> peerId = null; // Arrays.asList(new String[]{}); // List<String> | Filter by queue peer ID(s)
		SkillEntityListing result = apiInstance.getRoutingSkills(pageSize, pageNumber, name, peerId);
		List<RoutingSkill> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (RoutingSkill child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 유저가 보유중인 라우팅스킬 리스트를 조회한다.
	 *
	 * @Method Name : getUserRoutingskills
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getUserRoutingskills(String userId) throws IOException, ApiException, JSONException {
		RoutingApi apiInstance = new RoutingApi();
		Integer pageNumber = 1; // Integer | Page number
		Integer pageSize = 100; // Integer | Page size
		String sortOrder = "asc"; // String | Note: results are sorted by name.
		logger.info("##getUserRoutingSkill{}", userId);

		UserSkillEntityListing result = apiInstance.getUserRoutingskills(userId, pageSize, pageNumber, sortOrder);
		logger.info("{}", result);

		List<UserRoutingSkill> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (UserRoutingSkill child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 라우팅 스킬을 단일 부여/수정한다
	 *
	 * @Method Name : postUserRoutingskills
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @param level
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public JSONObject postUserRoutingskills(String userId, String skillId, Double level)
			throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

		UserRoutingSkillPost ursp = new UserRoutingSkillPost();
		ursp.setId(skillId);
		ursp.setProficiency(level);

		UserRoutingSkill result = apiInstance.postUserRoutingskills(userId, ursp);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 라우팅스킬을 다중 부여/수정한다.
	 *
	 * @Method Name : patchUserRoutingskillsBulk
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @param level
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public JSONObject patchUserRoutingskillsBulk(String userId, JSONArray skillList) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

		List<UserRoutingSkillPost> body = new ArrayList<>();

		for (int i = 0; i < skillList.length(); i++) {
			UserRoutingSkillPost ursp = new UserRoutingSkillPost();
			JSONObject cJO = (JSONObject) skillList.get(i);
			ursp.setId(cJO.getString("id"));
			ursp.setProficiency((double) cJO.getInt("level"));
			body.add(ursp);
		}

		UserSkillEntityListing result = apiInstance.patchUserRoutingskillsBulk(userId, body);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 라우팅스킬을 단일 삭제한다.
	 *
	 * @Method Name : deleteUserRoutingskill
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public void deleteUserRoutingskill(String userId, String skillId) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

		apiInstance.deleteUserRoutingskill(userId, skillId);
	}

	/**
	 * 메서드 설명 : 라우팅스킬을 해당 리스트로 전부 바꾼다 (Replace All)
	 *
	 * @Method Name : putUserRoutingskillsBulk
	 * @date : 2023. 9. 27.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @param level
	 * @throws ApiException
	 * @throws IOException
	 * @throws Exception
	 * @notify
	 *
	 */
	public JSONObject putUserRoutingskillsBulk(String userId, List<Map<String,Object>> skillList) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

		List<UserRoutingSkillPost> body = new ArrayList<>(); // List<UserRoutingSkillPost> | Skill

		for(Map<String,Object> map : skillList) {
			UserRoutingSkillPost ursp = new UserRoutingSkillPost();
			ursp.setId(map.get("skill_id").toString());
			double level = (int) map.get("skill_level");
			ursp.setProficiency(level);
			body.add(ursp);
		}
		logger.info("{}:{}", userId, body);

		UserSkillEntityListing result = apiInstance.putUserRoutingskillsBulk(userId, body);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("##result{}", resultJO);
		return resultJO;
	}

	/*
	 * #############################################################################
	 * ################################## : 그룹
	 * #############################################################################
	 */

	/**
	 * 메서드 설명 : 그룹 리스트를 조회한다.
	 *
	 * @Method Name : getGroups
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getGroups() throws IOException, ApiException, JSONException {
		GroupsApi apiInstance = new GroupsApi();
		Integer pageNumber = 1; // Integer | Page number
		Integer pageSize = 100; // Integer | Page size
		List<String> id = null; // Arrays.asList(new String[]{}); // List<String> | Filter by queue peer ID(s)
		List<String> jabberId = null; // List<String> | A list of jabberIds to fetch by bulk (cannot be used with the
										// \"id\" parameter)
		String sortOrder = "ASC"; // String | Ascending or descending sort order
		GroupEntityListing result = apiInstance.getGroups(pageSize, pageNumber, id, jabberId, sortOrder);
		List<Group> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (Group child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 그룹 정보를 조회한다.
	 *
	 * @Method Name : getGroup
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public Group getGroup(String groupId) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		Group result = apiInstance.getGroup(groupId);

		logger.info("{}", new JSONObject(om.writeValueAsString(result)));
		return result;
	}

	/**
	 * 메서드 설명 : 그룹을 생성한다.
	 *
	 * @Method Name : postGroup
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param groupId
	 * @throws ApiException
	 * @throws IOException
	 * @notify - Type: OUTDATEDSDKVERSION/SOCIAL/OFFICIAL - Visibility:
	 *         PUBLIC/OUTDATEDSDKVERSION/OWNERS/MEMBERS
	 */
	public JSONObject postGroups(String name) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		GroupCreate body = new GroupCreate();
		body.setName(name);
		body.setType(GroupCreate.TypeEnum.OFFICIAL);
		body.setRulesVisible(true);
		body.setVisibility(VisibilityEnum.PUBLIC);

		Group result = apiInstance.postGroups(body);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 그룹을 수정한다.
	 *
	 * @Method Name : putGroup
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param groupId
	 * @param name
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public JSONObject putGroup(String groupId, String name) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		GroupUpdate body = new GroupUpdate();
		Group preGroup = getGroup(groupId);
		body.setVersion(preGroup.getVersion());
		body.setName(name);

		Group result = apiInstance.putGroup(groupId, body);

		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 그룹을 삭제한다.
	 *
	 * @Method Name : deleteGroup
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param groupId
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 */
	public void deleteGroup(String groupId) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		apiInstance.deleteGroup(groupId);
	}

	/*
	 * #############################################################################
	 * ################################## : 그룹 멤버
	 * #############################################################################
	 */

	/**
	 * 메서드 설명 : 그룹 멤버를 조회한다.
	 *
	 * @Method Name : getGroupMembers
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param groupId
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getGroupMembers(String groupId) throws IOException, ApiException, JSONException {
		GroupsApi apiInstance = new GroupsApi();

		Integer pageSize = 100; // Integer | Page size
		Integer pageNumber = 1; // Integer | Page number
		String sortOrder = "ASC"; // String | Ascending or descending sort order
		List<String> expand = null; // List<String> | Which fields, if any, to expand

		UserEntityListing result = apiInstance.getGroupMembers(groupId, pageSize, pageNumber, sortOrder, expand);
		List<User> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (User child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 그룹에 멤버를 추가한다.
	 *
	 * @Method Name : postGroupMembers
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param groupId
	 * @throws ApiException
	 * @throws IOException
	 * @notify - setMemberIds가 List<String> 형태이므로 여러 ID 한번에 처리 가능.
	 */
	public void postGroupMembers(String groupId, JSONArray userList) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		GroupMembersUpdate body = new GroupMembersUpdate();

		int userCnt = userList.length();
		int currentCnt = 0;

		List<String> ids = new ArrayList<>();
		while (currentCnt < userCnt) {
			JSONObject cJO = userList.getJSONObject(currentCnt);
			ids.add(cJO.getString("id"));
			currentCnt++;
			if (currentCnt % 100 == 0 || currentCnt == userCnt) {
				Group preGroup = getGroup(groupId);
				body.setVersion(preGroup.getVersion());
				body.setMemberIds(ids);

				apiInstance.postGroupMembers(groupId, body);
				ids.clear();
			}
		}
	}

	/**
	 * 메서드 설명 : 그룹에서 멤버를 삭제한다.
	 *
	 * @Method Name : deleteGroupMemebers
	 * @date : 2023. 9. 26.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param groupId - Comma(,) separated list of userIds to remove
	 * @throws ApiException
	 * @throws IOException
	 * @notify - ID를 (,)구분자로 여러개 한번에 처리 가능.
	 */
	public void deleteGroupMemebers(String groupId, JSONArray userList) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		int userCnt = userList.length();
		int currentCnt = 0;

		String ids = "";
		StringBuilder sb = new StringBuilder();
		while (currentCnt < userCnt) {
			JSONObject cJO = userList.getJSONObject(currentCnt);
			sb.append(cJO.getString("id") + ",");
			currentCnt++;
			if (currentCnt % 100 == 0 || currentCnt == userCnt) {
				ids = sb.toString().substring(0, ids.length() - 1);
				apiInstance.deleteGroupMembers(groupId, ids);
				sb = new StringBuilder();
			}
		}

	}

	/**
	 * 메서드 설명 : 라우팅큐 리스트를 조회한다.
	 *
	 * @Method Name : getRoutingQueues
	 * @date : 2023. 10. 10.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @throws IOException
	 * @throws ApiException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getRoutingQueues() throws IOException, ApiException, JSONException {
		RoutingApi apiInstance = new RoutingApi();
		Integer pageNumber = 1; // Integer | Page number
		Integer pageSize = 100; // Integer | Page size
		String sortOrder = "asc"; // String | Note: results are sorted by name.
		String name = null; // String | Filter by queue name
		List<String> id = null; // List<String> | Filter by queue ID(s)
		List<String> divisionId = null; // List<String> | Filter by queue division ID(s)
		List<String> peerId = null; // List<String> | Filter by queue peer ID(s)
		Boolean hasPeer = null; // Boolean | Filter by queues associated with peer
		QueueEntityListing result = apiInstance.getRoutingQueues(pageNumber, pageSize, sortOrder, name, id, divisionId,
				peerId, hasPeer);

		List<Queue> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (Queue child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 유저가 보유중인 큐 리스트를 조회한다.
	 *
	 * @Method Name : getUserQueues
	 * @date : 2023. 10. 10.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param userId
	 * @throws IOException
	 * @throws ApiException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getUserQueues(String userId) throws IOException, ApiException, JSONException {
		UsersApi apiInstance = new UsersApi();

		Integer pageSize = 100; // Integer | Page size
		Integer pageNumber = 1; // Integer | Page number
		Boolean joined = true; // Boolean | Is joined to the queue
		List<String> divisionId = null; // List<String> | Division ID(s)

		UserQueueEntityListing result = apiInstance.getUserQueues(userId, pageSize, pageNumber, joined, divisionId);

		List<UserQueue> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (UserQueue child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 라우팅큐에 할당 된 상담사 리스트를 조회한다.
	 *
	 * @Method Name : getRoutingQueueMembers
	 * @date : 2023. 10. 10.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param queueId
	 * @throws IOException
	 * @throws ApiException
	 * @throws JSONException
	 * @notify
	 *
	 */
	public JSONObject getRoutingQueueMembers(String queueId) throws IOException, ApiException, JSONException {
		RoutingApi apiInstance = new RoutingApi();
		Integer pageNumber = 1; // Integer |
		Integer pageSize = 100; // Integer | Max value is 100
		String sortOrder = "asc"; // String | Note: results are sorted by name.
		List<String> expand = null; // List<String> | Which fields, if any, to expand.
		Boolean joined = null; // Boolean | Filter by joined status
		String name = null; // String | Filter by queue member name
		List<String> profileSkills = null; // List<String> | Filter by profile skill
		List<String> skills = null; // List<String> | Filter by skill
		List<String> languages = null; // List<String> | Filter by language
		List<String> routingStatus = null; // List<String> | Filter by routing status
		List<String> presence = null; // List<String> | Filter by presence

		QueueMemberEntityListing result = apiInstance.getRoutingQueueMembers(queueId, pageNumber, pageSize, sortOrder,
				expand, name, profileSkills, skills, languages, routingStatus, presence, name, joined);

		List<QueueMember> en = result.getEntities();

		JSONObject resultJO = new JSONObject();
		JSONArray resultJA = new JSONArray();

		for (QueueMember child : en) {
			JSONObject cJO = new JSONObject(om.writeValueAsString(child));
			resultJA.put(cJO);
		}

		resultJO.put("count", resultJA.length());
		resultJO.put("list", resultJA);

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명 : 라우팅큐에 상담사를 추가/삭제한다.
	 *
	 * @Method Name : postRoutingQueueMembers
	 * @date : 2023. 10. 10.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param queueId
	 * @param userId
	 * @param deleteFlag : true[삭제] false[추가]
	 * @throws IOException
	 * @throws ApiException
	 * @notify - 최대 100명
	 */
	public void postRoutingQueueMembers(String queueId, JSONArray userList, boolean deleteFlag)
			throws IOException, ApiException {
		RoutingApi apiInstance = new RoutingApi();
		List<WritableEntity> body = new ArrayList<>(); // List<WritableEntity> | Queue Members

		int userCnt = userList.length();
		int currentCnt = 0;
		while (currentCnt < userCnt) {
			JSONObject cJO = userList.getJSONObject(currentCnt);
			WritableEntity we = new WritableEntity();
			we.setId(cJO.getString("id"));
			body.add(we);
			currentCnt++;

			if (currentCnt % 100 == 0 || currentCnt == userCnt) {
				apiInstance.postRoutingQueueMembers(queueId, body, deleteFlag);
				body.clear();
			}
		}
	}

	/**
	 * 메서드 설명 : 라우팅큐에서 상담사를 삭제한다.
	 *
	 * @Method Name : deleteRoutingQueueMember
	 * @date : 2023. 10. 10.
	 * @author : Kriverds
	 * @version : 1.0 ----------------------------------------
	 * @param queueId
	 * @param userId
	 * @throws IOException
	 * @throws ApiException
	 * @notify
	 *
	 */
	public void deleteRoutingQueueMember(String queueId, String userId) throws IOException, ApiException {
		RoutingApi apiInstance = new RoutingApi();
		apiInstance.deleteRoutingQueueMember(queueId, userId);
	}

	/*
	 * #############################################################################
	 * ################################## : 캠페인 통계
	 * #############################################################################
	 */
	public JSONObject getOutboundCampaigns() throws IOException, ApiException, JSONException {
		OutboundApi apiInstance = new OutboundApi();
		Integer pageSize = 100; // Integer | Page size. The max that will be returned is 100.
		Integer pageNumber = 1; // Integer | Page number
		String filterType = "";// "Prefix"; // String | Filter type
		String name = "";// "name_example"; // String | Name
		List<String> id = null; // List<String> | id
		String contactListId = "";// "contactListId_example"; // String | Contact List ID
		String dncListIds = "";// "dncListIds_example"; // String | DNC list ID
		String distributionQueueId = "";// "distributionQueueId_example"; // String | Distribution queue ID
		String edgeGroupId = "";// "edgeGroupId_example"; // String | Edge group ID
		String callAnalysisResponseSetId = "";// "callAnalysisResponseSetId_example"; // String | Call analysis response
												// set ID
		List<String> divisionId = null; // List<String> | Division ID(s)
		String sortBy = "";// "sortBy_example"; // String | Sort by
		String sortOrder = "";// "a"; // String | Sort order
		CampaignEntityListing result = apiInstance.getOutboundCampaigns(pageSize, pageNumber, filterType, name, id,
				contactListId, dncListIds, distributionQueueId, edgeGroupId, callAnalysisResponseSetId, divisionId,
				sortBy, sortOrder);
//		System.out.println(result);
		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		return resultJO;
	}

	public JSONObject getOutboundCampaign(String campaignId) throws IOException, ApiException, JSONException {
		OutboundApi apiInstance = new OutboundApi();
		Campaign result = apiInstance.getOutboundCampaign(campaignId);
		System.out.println(result);
		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		return resultJO;
	}

	public JSONObject getOutboundCampaignStats(String campainId) throws IOException, ApiException, JSONException {
		OutboundApi apiInstance = new OutboundApi();

		CampaignStats result = apiInstance.getOutboundCampaignStats(campainId);
		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		return resultJO;
	}

	public JSONObject getOutboundCampaignProgress(String campainId) throws IOException, ApiException, JSONException {
		OutboundApi apiInstance = new OutboundApi();

		CampaignProgress result = apiInstance.getOutboundCampaignProgress(campainId);
		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		return resultJO;
	}

	public JSONObject campaignConversationQuery(String startDate) throws IOException, ApiException, JSONException, ParseException {
		String fsDate = "";
		String feDate = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf1.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		fsDate = sdf2.format(cal.getTime());
		cal.add(Calendar.MILLISECOND, (15*60*1_000));
		feDate = sdf2.format(cal.getTime());
		fsDate = fsDate.replace(' ', 'T')+"Z";
		feDate = feDate.replace(' ', 'T')+"Z";

		ConversationsApi apiInstance = new ConversationsApi();
		ConversationQuery body = new ConversationQuery(); // ConversationQuery | query
		logger.info("{}", fsDate+"/"+feDate);


		body.setInterval(fsDate+"/"+feDate);
//		body.setInterval("2023-11-01T00:00:00.000"+"/"+"2023-11-30T23:59:59.999");

		JSONObject campaigns = getOutboundCampaigns();
		JSONArray entities = campaigns.getJSONArray("entities");
		List<String> campaignsIdList = new ArrayList<>();
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);
			campaignsIdList.add(entity.getString("id"));
		}

		List<SegmentDetailQueryFilter> filterList = new ArrayList<>();
		SegmentDetailQueryFilter filter = new SegmentDetailQueryFilter();
		filter.setType(SegmentDetailQueryFilter.TypeEnum.OR);
		List<SegmentDetailQueryClause> clauseList = new ArrayList<>();
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			SegmentDetailQueryClause clause = new SegmentDetailQueryClause();
			clause.setType(SegmentDetailQueryClause.TypeEnum.OR);
			List<SegmentDetailQueryPredicate> predicateList = new ArrayList<>();
			SegmentDetailQueryPredicate predicate = new SegmentDetailQueryPredicate();
			predicate.setDimension(SegmentDetailQueryPredicate.DimensionEnum.OUTBOUNDCAMPAIGNID);
			predicate.setValue(entity.getString("id"));
			predicateList.add(predicate);
			clause.setPredicates(predicateList);
			clauseList.add(clause);
		}

		filter.setClauses(clauseList);
		filterList.add(filter);

		body.setSegmentFilters(filterList);

		AnalyticsConversationQueryResponse result = apiInstance.postAnalyticsConversationsDetailsQuery(body);
		JSONObject resultJO = new JSONObject(om.writeValueAsString(result));

		return resultJO;
	}

	public JSONObject campaignAggregateQuery(String startDate) throws IOException, ApiException, JSONException, ParseException {
		JSONObject returnJO = new JSONObject();
		String fsDate = "";
		String feDate = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf1.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		fsDate = sdf2.format(cal.getTime());
		cal.add(Calendar.MILLISECOND, (15*60*1_000));
		feDate = sdf2.format(cal.getTime());
		fsDate = fsDate.replace(' ', 'T')+"Z";
		feDate = feDate.replace(' ', 'T')+"Z";

		AnalyticsApi apiInstance = new AnalyticsApi();

		ConversationAggregationQuery body = new ConversationAggregationQuery();
//		body.setInterval("2023-11-01T00:00:00.000"+"/"+"2023-11-30T23:59:59.999");
		body.setInterval(fsDate+"/"+feDate);

		JSONObject campaigns = getOutboundCampaigns();
		JSONArray entities = campaigns.getJSONArray("entities");

		List<MetricsEnum> meList = new ArrayList<>();
		meList.add(MetricsEnum.NOUTBOUNDATTEMPTED);
		meList.add(MetricsEnum.NCONNECTED);
		meList.add(MetricsEnum.NOFFERED);
		meList.add(MetricsEnum.NOUTBOUNDCONNECTED);
		meList.add(MetricsEnum.NOUTBOUNDABANDONED);
		body.setMetrics(meList);
		ConversationAggregateQueryFilter filter = new ConversationAggregateQueryFilter();
		filter.setType(ConversationAggregateQueryFilter.TypeEnum.OR);



		List<ConversationAggregateQueryClause> clauseList = new ArrayList<>();
		ConversationAggregateQueryClause clause = new ConversationAggregateQueryClause();
		clause.setType(ConversationAggregateQueryClause.TypeEnum.OR);
		List<ConversationAggregateQueryPredicate> predicateList = new ArrayList<>();
		ConversationAggregateQueryPredicate predicate = new ConversationAggregateQueryPredicate();
		predicate.setDimension(DimensionEnum.OUTBOUNDCAMPAIGNID);
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			predicate.setValue(entity.getString("id"));
			predicateList.clear();
			predicateList.add(predicate);
			clause.setPredicates(predicateList);
			clauseList.clear();
			clauseList.add(clause);
			filter.setClauses(clauseList);
			body.setFilter(filter);

			ConversationAggregateQueryResponse result = apiInstance.postAnalyticsConversationsAggregatesQuery(body);

			JSONObject resultJO = new JSONObject(om.writeValueAsString(result));
			returnJO.put(entity.getString("id"), resultJO);
		}

		return returnJO;
	}

	/*
	 * #############################################################################
	 * ################################## : Aggregate 통계
	 * #############################################################################
	 */
	public JSONObject agentCallAggregateQuery(String startDate) throws IOException, ApiException, JSONException, ParseException {
		JSONObject returnJO = new JSONObject();
		String fsDate = "";
		String feDate = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf1.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		fsDate = sdf2.format(cal.getTime());
		cal.add(Calendar.MILLISECOND, (15*60*1_000));
		feDate = sdf2.format(cal.getTime());
		fsDate = fsDate.replace(' ', 'T')+"Z";
		feDate = feDate.replace(' ', 'T')+"Z";

		AnalyticsApi apiInstance = new AnalyticsApi();

		ConversationAggregationQuery body = new ConversationAggregationQuery();
//		body.setInterval("2023-11-01T00:00:00.000"+"/"+"2023-11-30T23:59:59.999");
		body.setInterval(fsDate+"/"+feDate);

		JSONObject agents = getAvailableUserList();
		JSONArray entities = agents.getJSONArray("list");

		List<MetricsEnum> meList = new ArrayList<>();
		for (int i = 0; i < MetricsEnum.values().length; i++) {
			if (i == 21 || i == 22 || i == 28 || i == 29) {
			} else {
				meList.add(MetricsEnum.values()[i]);
			}
		}
		body.setMetrics(meList);
		ConversationAggregateQueryFilter filter = new ConversationAggregateQueryFilter();
		filter.setType(ConversationAggregateQueryFilter.TypeEnum.OR);



		List<ConversationAggregateQueryClause> clauseList = new ArrayList<>();
		ConversationAggregateQueryClause clause = new ConversationAggregateQueryClause();
		clause.setType(ConversationAggregateQueryClause.TypeEnum.OR);
		List<ConversationAggregateQueryPredicate> predicateList = new ArrayList<>();
		ConversationAggregateQueryPredicate predicate = new ConversationAggregateQueryPredicate();
		predicate.setDimension(DimensionEnum.USERID);
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			predicate.setValue(entity.getString("id"));
			predicateList.clear();
			predicateList.add(predicate);
			clause.setPredicates(predicateList);
			clauseList.clear();
			clauseList.add(clause);
			filter.setClauses(clauseList);
			body.setFilter(filter);

			ConversationAggregateQueryResponse result = apiInstance.postAnalyticsConversationsAggregatesQuery(body);

			JSONObject resultJO = new JSONObject(om.writeValueAsString(result));
			returnJO.put(entity.getString("id"), resultJO);
		}

		return returnJO;
	}

	public JSONObject agentStatusAggregateQuery(String startDate) throws IOException, ApiException, JSONException, ParseException {
		JSONObject returnJO = new JSONObject();
		String fsDate = "";
		String feDate = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf1.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		fsDate = sdf2.format(cal.getTime());
		cal.add(Calendar.MILLISECOND, (15*60*1_000));
		feDate = sdf2.format(cal.getTime());
		fsDate = fsDate.replace(' ', 'T')+"Z";
		feDate = feDate.replace(' ', 'T')+"Z";

		AnalyticsApi apiInstance = new AnalyticsApi();

		UserAggregationQuery body = new UserAggregationQuery();
//		body.setInterval("2023-11-01T00:00:00.000"+"/"+"2023-11-30T23:59:59.999");
		body.setInterval(fsDate+"/"+feDate);

		JSONObject agents = getAvailableUserList();
		JSONArray entities = agents.getJSONArray("list");

		List<UserAggregationQuery.MetricsEnum> meList = new ArrayList<>();
		for (int i = 0; i < UserAggregationQuery.MetricsEnum.values().length; i++) {
			if (i != 2) {
				meList.add(UserAggregationQuery.MetricsEnum.values()[i]);
			}
		}
		body.setMetrics(meList);
		UserAggregateQueryFilter filter = new UserAggregateQueryFilter();
		filter.setType(UserAggregateQueryFilter.TypeEnum.OR);



		List<UserAggregateQueryClause> clauseList = new ArrayList<>();
		UserAggregateQueryClause clause = new UserAggregateQueryClause();
		clause.setType(UserAggregateQueryClause.TypeEnum.OR);
		List<UserAggregateQueryPredicate> predicateList = new ArrayList<>();
		UserAggregateQueryPredicate predicate = new UserAggregateQueryPredicate();
		predicate.setDimension(UserAggregateQueryPredicate.DimensionEnum.USERID);
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			predicate.setValue(entity.getString("id"));
			predicateList.clear();
			predicateList.add(predicate);
			clause.setPredicates(predicateList);
			clauseList.clear();
			clauseList.add(clause);
			filter.setClauses(clauseList);
			body.setFilter(filter);

			UserAggregateQueryResponse result = apiInstance.postAnalyticsUsersAggregatesQuery(body);

			JSONObject resultJO = new JSONObject(om.writeValueAsString(result));
			returnJO.put(entity.getString("id"), resultJO);
		}

		return returnJO;
	}

	public JSONObject queueAggregateQuery(String startDate) throws IOException, ApiException, JSONException, ParseException {
		JSONObject returnJO = new JSONObject();
		String fsDate = "";
		String feDate = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf1.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		fsDate = sdf2.format(cal.getTime());
		cal.add(Calendar.MILLISECOND, (15*60*1_000));
		feDate = sdf2.format(cal.getTime());
		fsDate = fsDate.replace(' ', 'T')+"Z";
		feDate = feDate.replace(' ', 'T')+"Z";

		AnalyticsApi apiInstance = new AnalyticsApi();

		ConversationAggregationQuery body = new ConversationAggregationQuery();
//		body.setInterval("2023-11-01T00:00:00.000"+"/"+"2023-11-30T23:59:59.999");
		body.setInterval(fsDate+"/"+feDate);

		JSONObject queues = getRoutingQueues();
		JSONArray entities = queues.getJSONArray("list");

		List<MetricsEnum> meList = new ArrayList<>();
		for (int i = 0; i < MetricsEnum.values().length; i++) {
			if (i == 21 || i == 22 || i == 28 || i == 29) {
			} else {
				meList.add(MetricsEnum.values()[i]);
			}
		}
		body.setMetrics(meList);
		ConversationAggregateQueryFilter filter = new ConversationAggregateQueryFilter();
		filter.setType(ConversationAggregateQueryFilter.TypeEnum.OR);



		List<ConversationAggregateQueryClause> clauseList = new ArrayList<>();
		ConversationAggregateQueryClause clause = new ConversationAggregateQueryClause();
		clause.setType(ConversationAggregateQueryClause.TypeEnum.OR);
		List<ConversationAggregateQueryPredicate> predicateList = new ArrayList<>();
		ConversationAggregateQueryPredicate predicate = new ConversationAggregateQueryPredicate();
		predicate.setDimension(DimensionEnum.QUEUEID);
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			predicate.setValue(entity.getString("id"));
			predicateList.clear();
			predicateList.add(predicate);
			clause.setPredicates(predicateList);
			clauseList.clear();
			clauseList.add(clause);
			filter.setClauses(clauseList);
			body.setFilter(filter);

			ConversationAggregateQueryResponse result = apiInstance.postAnalyticsConversationsAggregatesQuery(body);

			JSONObject resultJO = new JSONObject(om.writeValueAsString(result));
			returnJO.put(entity.getString("id"), resultJO);
		}

		return returnJO;
	}

	public JSONObject skillAggregateQuery(String startDate) throws IOException, ApiException, JSONException, ParseException {
		JSONObject returnJO = new JSONObject();
		String fsDate = "";
		String feDate = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = sdf1.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		fsDate = sdf2.format(cal.getTime());
		cal.add(Calendar.MILLISECOND, (15*60*1_000));
		feDate = sdf2.format(cal.getTime());
		fsDate = fsDate.replace(' ', 'T')+"Z";
		feDate = feDate.replace(' ', 'T')+"Z";

		AnalyticsApi apiInstance = new AnalyticsApi();

		ConversationAggregationQuery body = new ConversationAggregationQuery();
//		body.setInterval("2023-11-01T00:00:00.000"+"/"+"2023-11-30T23:59:59.999");
		body.setInterval(fsDate+"/"+feDate);

		JSONObject skills = getRoutingSkillList();
		JSONArray entities = skills.getJSONArray("list");

		List<MetricsEnum> meList = new ArrayList<>();
		for (int i = 0; i < MetricsEnum.values().length; i++) {
			if (i == 21 || i == 22 || i == 28 || i == 29) {
			} else {
				meList.add(MetricsEnum.values()[i]);
			}
		}
		body.setMetrics(meList);
		ConversationAggregateQueryFilter filter = new ConversationAggregateQueryFilter();
		filter.setType(ConversationAggregateQueryFilter.TypeEnum.OR);



		List<ConversationAggregateQueryClause> clauseList = new ArrayList<>();
		ConversationAggregateQueryClause clause = new ConversationAggregateQueryClause();
		clause.setType(ConversationAggregateQueryClause.TypeEnum.OR);
		List<ConversationAggregateQueryPredicate> predicateList = new ArrayList<>();
		ConversationAggregateQueryPredicate predicate = new ConversationAggregateQueryPredicate();
		predicate.setDimension(DimensionEnum.REQUESTEDROUTINGSKILLID);
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			predicate.setValue(entity.getString("id"));
			predicateList.clear();
			predicateList.add(predicate);
			clause.setPredicates(predicateList);
			clauseList.clear();
			clauseList.add(clause);
			filter.setClauses(clauseList);
			body.setFilter(filter);

			ConversationAggregateQueryResponse result = apiInstance.postAnalyticsConversationsAggregatesQuery(body);

			JSONObject resultJO = new JSONObject(om.writeValueAsString(result));
			returnJO.put(entity.getString("id"), resultJO);
		}

		return returnJO;
	}


	public static NotificationHandler getNotificationHandler() {
		return notificationHandler;
	}

	public static void setNotificationHandler(NotificationHandler notificationHandler) {
		GCConnector.notificationHandler = notificationHandler;
	}

	public static List<UUID> getJobList() {
		return jobList;
	}

	public static void setJobList(List<UUID> jobList) {
		GCConnector.jobList = jobList;
	}

}
