package com.soluvis.croffle.v1.gcloud.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.api.GroupsApi;
import com.mypurecloud.sdk.v2.api.NotificationsApi;
import com.mypurecloud.sdk.v2.api.RoutingApi;
import com.mypurecloud.sdk.v2.api.StationsApi;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.connector.ApiClientConnectorProperty;
import com.mypurecloud.sdk.v2.connector.apache.ApacheHttpClientConnectorProvider;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;
import com.mypurecloud.sdk.v2.extensions.notifications.NotificationHandler;
import com.mypurecloud.sdk.v2.model.Channel;
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
import com.mypurecloud.sdk.v2.model.RoutingSkill;
import com.mypurecloud.sdk.v2.model.SkillEntityListing;
import com.mypurecloud.sdk.v2.model.StationEntityListing;
import com.mypurecloud.sdk.v2.model.UpdateUser;
import com.mypurecloud.sdk.v2.model.User;
import com.mypurecloud.sdk.v2.model.UserEntityListing;
import com.mypurecloud.sdk.v2.model.UserQueue;
import com.mypurecloud.sdk.v2.model.UserQueueEntityListing;
import com.mypurecloud.sdk.v2.model.UserRoutingSkill;
import com.mypurecloud.sdk.v2.model.UserRoutingSkillPost;
import com.mypurecloud.sdk.v2.model.UserSkillEntityListing;
import com.mypurecloud.sdk.v2.model.WritableEntity;
import com.neovisionaries.ws.client.WebSocketException;
import com.soluvis.croffle.v1.gcloud.engine.listener.ChannelMetadataListener;



/**
 * 클래스 설명	: GCloud와 연결 및 Interface 제공.
 * @Class Name 	: GCConnector
 * @date   		: 2023. 9. 27.
 * @author   	: Riverds
 * @version		: 1.0
 * ----------------------------------------
 * @notify
 *
 */
@Component
public class GCConnector {


	private final Logger logger = LoggerFactory.getLogger(GCConnector.class);


	private static final String CLIENT_ID = "ecf91a65-0043-4da7-92eb-bdd83a5c2cb6";
	private static final String CLIENT_SECRET = "nhK4fw7a3vh-BPCdfRe311m1rWDbnXJoATteyyeVfvk";
	private static final String DIVISION_HOME = "fa910857-f776-4dd5-91a9-1d245882ab11";

	Channel channel = null;
	PureCloudRegionHosts region = null;
	ApiClient apiClient = null;

	NotificationHandler notificationHandler = null;
	
	ObjectMapper om = new ObjectMapper();

	public GCConnector() {
		region = PureCloudRegionHosts.ap_northeast_2;
		apiClient =
				ApiClient.Builder
				.standard()
				.withBasePath(region)
				.withShouldRefreshAccessToken(true)
				.withProperty(ApiClientConnectorProperty.CONNECTOR_PROVIDER, new ApacheHttpClientConnectorProvider())
				.build();
	}

	/**
	 * 메서드 설명	: GCloud와 연결한다.
	 * @Method Name : connect
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @throws WebSocketException
	 * @notify
	 *
	 */
	public void connect() throws IOException, ApiException, WebSocketException {
		ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
		logger.info("{}", "Authentication successful. Access token expires in " + authResponse.getBody().getExpires_in() + " seconds");
		logger.info("{}", authResponse.getBody());

		Configuration.setDefaultApiClient(apiClient);

		NotificationsApi notificationApiInstance = new NotificationsApi();
		channel = notificationApiInstance.postNotificationsChannels();
		logger.info("{}", channel);

		ChannelMetadataListener listenerch = new ChannelMetadataListener();

		notificationHandler = NotificationHandler.Builder.standard()
				.withNotificationListener(listenerch)
				.withAutoConnect(false).build();

		notificationHandler.sendPing();
	}

	/**
	 * 메서드 설명	: GCloud와 연결을 종료한다.
	 * @Method Name : close
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @notify
	 *
	 */
	public void close() {
		notificationHandler.disconnect();
		logger.info("{}", "GCConnector Connection close()");

	}

	/**
	 * 메서드 설명	: 내선번호 리스트를 조회한다.
	 * @Method Name : getExtensionList
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws IOException
	 * @throws ApiException
	 * @notify
	 *
	 */
	public void getExtensionList() throws IOException, ApiException   {
		StationsApi apiInstance = new StationsApi();

        Integer pageSize = 50; // Integer | Page size
	    Integer pageNumber = 1; // Integer | Page number

	    String sortBy = "name"; // String | Sort by
	    String name = null ; //"name_example"; // String | Name
	    String userSelectable = "false" ; // "userSelectable_example"; // String | True for stations that the user can select otherwise false
	    String webRtcUserId = null; // "webRtcUserId_example"; // String | Filter for the webRtc station of the webRtcUserId
	    String id = null ; //"id_example"; // String | Comma separated list of stationIds
	    String lineAppearanceId = null ; // "lineAppearanceId_example"; // String | lineAppearanceId
	    StationEntityListing result = apiInstance.getStations(pageSize, pageNumber, sortBy, name, userSelectable, webRtcUserId, id, lineAppearanceId);
	    logger.info("{}", result);
	}



	/*
	 * ###############################################################################################################
	 * : 상담사
	 * ###############################################################################################################
	 */



	/**
	 * 메서드 설명	: 사용 가능한 유저 정보를 조회한다.
	 * @Method Name : getAvailableUser
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
    	String integrationPresenceSource = null ;// "integrationPresenceSource_example"; // String | Gets an integration presence for users instead of their defaults. This parameter will only be used when presence is provided as an \"expand\". When using this parameter the maximum number of users that can be returned is 100.
    	String state = "active" ; //""active"; // String | Only list users of this state

    	User result = apiInstance.getUser(userId, expand, integrationPresenceSource, state);
    	
    	logger.info("{}", new JSONObject(om.writeValueAsString(result)));
    	return result;
	}

	/**
	 * 메서드 설명	: 사용 가능한 유저 리스트를 조회한다.
	 * @Method Name : getAvailableUserList
	 * @date   		: 2023. 9. 25.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONObject getAvailableUserList() throws IOException, ApiException, JSONException {

		UsersApi apiInstance = new UsersApi();
        Integer pageSize = 50; // Integer | Page size
    	Integer pageNumber = 1; // Integer | Page number
    	List<String> id = Arrays.asList(); // List<String> | A list of user IDs to fetch by bulk
    	List<String> jabberId = Arrays.asList(); // List<String> | A list of jabberIds to fetch by bulk (cannot be used with the \"id\" parameter)
    	String sortOrder = "ASC"; // String | Ascending or descending sort order
    	List<String> expand = Arrays.asList(); // List<String> | Which fields, if any, to expand
    	String integrationPresenceSource = null ;// "integrationPresenceSource_example"; // String | Gets an integration presence for users instead of their defaults. This parameter will only be used when presence is provided as an \"expand\". When using this parameter the maximum number of users that can be returned is 100.
    	String state = "active" ; //""active"; // String | Only list users of this state

    	UserEntityListing result = apiInstance.getUsers(pageSize, pageNumber, id, jabberId, sortOrder, expand, integrationPresenceSource, state);
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
	 * 메서드 설명	: 유저를 생성한다.
	 * @Method Name : postUsers
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param name
	 * @param department
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify
	 * - email이 Key로 중복 발생시 ApiException 에러
	 */
	public JSONObject postUsers(String name, String email) throws IOException, ApiException, JSONException {
		UsersApi apiInstance = new UsersApi();

		CreateUser body = new CreateUser();
		body.setName(name);
		body.setEmail(email);
		body.setDivisionId(DIVISION_HOME);

		User result = apiInstance.postUsers(body);

		JSONObject resultJO = new JSONObject();

		resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
    	return resultJO;
	}

	/**
	 * 메서드 설명	: 유저를 수정한다.
	 * @Method Name : patchUser
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param userId
	 * @param department
	 * @throws ApiException
	 * @throws IOException
	 * @throws JSONException
	 * @notify
	 * - 현재 상담사의 Version 정보를 가져와서 세팅해 주어야 함.
	 *   이상한 Version 정보나 세팅 안할 시 ApiException 발생
	 */
	public JSONObject patchUser(String userId, String department) throws IOException, ApiException, JSONException {
		UsersApi apiInstance = new UsersApi();

		UpdateUser body = new UpdateUser();
		User preUser = getAvailableUser(userId);

		body.setVersion(preUser.getVersion());
		body.setDepartment(department);

		User result = apiInstance.patchUser(userId, body);

		JSONObject resultJO = new JSONObject();

		resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
		return resultJO;
	}

	/**
	 * 메서드 설명	: 유저를 삭제한다.
	 * @Method Name : deleteUser
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param userId
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 * - 계속 지워도 에러발생 안함.
	 */
	public void deleteUser(String userId) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

		apiInstance.deleteUser(userId);
	}



	/*
	 * ###############################################################################################################
	 * : 라우팅스킬
	 * ###############################################################################################################
	 */



	/**
	 * 메서드 설명	: 라우팅스킬 리스트를 조회한다.
	 * @Method Name : getRoutingSkillList
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
    	List<String> peerId = null ; //Arrays.asList(new String[]{}); // List<String> | Filter by queue peer ID(s)
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
	 * 메서드 설명	: 유저가 보유중인 라우팅스킬 리스트를 조회한다.
	 * @Method Name : getUserRoutingskills
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
    	UserSkillEntityListing result = apiInstance.getUserRoutingskills(userId, pageSize, pageNumber, sortOrder);
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
	 * 메서드 설명	: 라우팅 스킬을 단일 부여/수정한다
	 * @Method Name : postUserRoutingskills
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @param level
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public JSONObject postUserRoutingskills(String userId, String skillId, Double level) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();


    	UserRoutingSkillPost ursp = new UserRoutingSkillPost();
    	ursp.setId(skillId);
    	ursp.setProficiency(level);

    	UserRoutingSkill result = apiInstance.postUserRoutingskills(userId, ursp);

    	JSONObject resultJO = new JSONObject();

    	resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
    	return resultJO;
	}

	/**
	 * 메서드 설명	: 라우팅스킬을 다중 부여/수정한다.
	 * @Method Name : patchUserRoutingskillsBulk
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @param level
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 *
	 */
	public JSONObject patchUserRoutingskillsBulk(String userId, String[] skillId, Double[] level) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

    	List<UserRoutingSkillPost> body = new ArrayList<>();

    	for (int i = 0; i < skillId.length; i++) {
    		UserRoutingSkillPost ursp = new UserRoutingSkillPost();
        	ursp.setId(skillId[i]);
        	ursp.setProficiency(level[i]);
        	body.add(ursp);
		}

    	UserSkillEntityListing result = apiInstance.patchUserRoutingskillsBulk(userId, body);

    	JSONObject resultJO = new JSONObject();

    	resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
    	return resultJO;
	}

	/**
	 * 메서드 설명	: 라우팅스킬을 단일 삭제한다.
	 * @Method Name : deleteUserRoutingskill
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
	 * 메서드 설명	: 라우팅스킬을 해당 리스트로 전부 바꾼다 (Replace All)
	 * @Method Name : putUserRoutingskillsBulk
	 * @date   		: 2023. 9. 27.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param userId
	 * @param skillId
	 * @param level
	 * @throws ApiException
	 * @throws IOException
	 * @throws Exception
	 * @notify
	 *
	 */
	public JSONObject putUserRoutingskillsBulk(String userId, String[] skillId, Double[] level) throws IOException, ApiException {
		UsersApi apiInstance = new UsersApi();

    	List<UserRoutingSkillPost> body = new ArrayList<>(); // List<UserRoutingSkillPost> | Skill

    	for (int i = 0; i < skillId.length; i++) {
    		UserRoutingSkillPost ursp = new UserRoutingSkillPost();
        	ursp.setId(skillId[i]);
        	ursp.setProficiency(level[i]);
        	body.add(ursp);
		}

	    UserSkillEntityListing result = apiInstance.putUserRoutingskillsBulk(userId, body);

	    JSONObject resultJO = new JSONObject();

	    resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
    	return resultJO;
	}



	/*
	 * ###############################################################################################################
	 * : 그룹
	 * ###############################################################################################################
	 */



	/**
	 * 메서드 설명	: 그룹 리스트를 조회한다.
	 * @Method Name : getGroups
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
    	List<String> id = null ; //Arrays.asList(new String[]{}); // List<String> | Filter by queue peer ID(s)
    	List<String> jabberId = null; // List<String> | A list of jabberIds to fetch by bulk (cannot be used with the \"id\" parameter)
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
	 * 메서드 설명	: 그룹 정보를 조회한다.
	 * @Method Name : getGroup
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
	 * 메서드 설명	: 그룹을 생성한다.
	 * @Method Name : postGroup
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param groupId
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 * - Type: OUTDATEDSDKVERSION/SOCIAL/OFFICIAL
	 * - Visibility: PUBLIC/OUTDATEDSDKVERSION/OWNERS/MEMBERS
	 */
	public JSONObject postGroups(String name) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		GroupCreate body = new GroupCreate();
		body.setName(name);
		body.setType(GroupCreate.TypeEnum.OFFICIAL);
		body.setRulesVisible(true);
		body.setVisibility(VisibilityEnum.PUBLIC);

		Group result = apiInstance.postGroups(body);

		JSONObject resultJO = new JSONObject();

		resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
    	return resultJO;
	}

	/**
	 * 메서드 설명	: 그룹을 수정한다.
	 * @Method Name : putGroup
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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

		JSONObject resultJO = new JSONObject();

		resultJO.put("object", new JSONObject(om.writeValueAsString(result)));

		logger.info("{}", resultJO);
    	return resultJO;
	}

	/**
	 * 메서드 설명	: 그룹을 삭제한다.
	 * @Method Name : deleteGroup
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
	 * ###############################################################################################################
	 * : 그룹 멤버
	 * ###############################################################################################################
	 */



	/**
	 * 메서드 설명	: 그룹 멤버를 조회한다.
	 * @Method Name : getGroupMembers
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
	 * 메서드 설명	: 그룹에 멤버를 추가한다.
	 * @Method Name : postGroupMembers
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param groupId
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 * - setMemberIds가 List<String> 형태이므로 여러 ID 한번에 처리 가능.
	 */
	public void postGroupMembers(String groupId, String userId) throws IOException, ApiException  {
		GroupsApi apiInstance = new GroupsApi();

		GroupMembersUpdate body = new GroupMembersUpdate();
		Group preGroup = getGroup(groupId);
		List<String> ids = new ArrayList<>();
		ids.add(userId);

		body.setVersion(preGroup.getVersion());
		body.setMemberIds(ids);

		apiInstance.postGroupMembers(groupId, body);
	}

	/**
	 * 메서드 설명	: 그룹에서 멤버를 삭제한다.
	 * @Method Name : deleteGroupMemebers
	 * @date   		: 2023. 9. 26.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param groupId - Comma(,) separated list of userIds to remove
	 * @throws ApiException
	 * @throws IOException
	 * @notify
	 * - ID를 (,)구분자로 여러개 한번에 처리 가능.
	 */
	public void deleteGroupMemebers(String groupId, String userId) throws IOException, ApiException {
		GroupsApi apiInstance = new GroupsApi();

		apiInstance.deleteGroupMembers(groupId, userId);
	}

	/**
	 * 메서드 설명	: 라우팅큐 리스트를 조회한다.
	 * @Method Name : getRoutingQueues
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
		QueueEntityListing result = apiInstance.getRoutingQueues(pageNumber, pageSize, sortOrder, name, id, divisionId, peerId, hasPeer);

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
	 * 메서드 설명	: 유저가 보유중인 큐 리스트를 조회한다.
	 * @Method Name : getUserQueues
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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
	 * 메서드 설명	: 라우팅큐에 할당 된 상담사 리스트를 조회한다.
	 * @Method Name : getRoutingQueueMembers
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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

		QueueMemberEntityListing result = apiInstance.getRoutingQueueMembers(queueId, pageNumber, pageSize, sortOrder, expand, name, profileSkills, skills, languages, routingStatus, presence, name, joined);

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
	 * 메서드 설명	: 라우팅큐에 상담사를 추가/삭제한다.
	 * @Method Name : postRoutingQueueMembers
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
	 * @param queueId
	 * @param userId
	 * @param deleteFlag : true[삭제] false[추가]
	 * @throws IOException
	 * @throws ApiException
	 * @notify
	 * - 최대 100명
	 */
	public void postRoutingQueueMembers(String queueId, String userId, boolean deleteFlag) throws IOException, ApiException {
		RoutingApi apiInstance = new RoutingApi();
		WritableEntity we = new WritableEntity();
		we.setId(userId);
		List<WritableEntity> body = new ArrayList<>(); // List<WritableEntity> | Queue Members
		body.add(we);
		apiInstance.postRoutingQueueMembers(queueId, body, deleteFlag);
	}

	/**
	 * 메서드 설명	: 라우팅큐에서 상담사를 삭제한다.
	 * @Method Name : deleteRoutingQueueMember
	 * @date   		: 2023. 10. 10.
	 * @author   	: Riverds
	 * @version		: 1.0
	 * ----------------------------------------
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

}
