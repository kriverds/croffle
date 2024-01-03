package com.soluvis.croffle.v1.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;


public class GVal {
	
	private GVal() {}
	
	@Getter
	@Setter
	private static String applicationId;

	@Getter
	@Setter
	private static String haRole = "backup";

	@Getter
	@Setter
	private static String accessToken = "";

	@Getter
	@Setter
	private static List<String> progressConversationList = new ArrayList<>();

	@Getter
	@Setter
	private static String gcClientId;
	@Getter
	@Setter
	private static String gcClientSecret;
}
