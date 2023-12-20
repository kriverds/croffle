package com.soluvis.croffle.v1.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class GVal {
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
}
