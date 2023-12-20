package com.soluvis.croffle.v1.gcloud.util;

import java.util.UUID;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public class CommUtil {

	private CommUtil() {}

	public static UUID setAttrUUID(HttpServletRequest request) {
		UUID rUUID = UUID.randomUUID();
		request.setAttribute("rUUID", rUUID);
		return rUUID;
	}

	public static String getJString(JSONObject parents, String param) {
		try {
			String result = parents.get(param).toString();
			if (result.equals("null")) {
				result = "";
			}
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	public static long getJLong(JSONObject parents, String param) {
		try {
			return Long.parseLong(parents.get(param).toString());
		} catch (Exception e) {
			return 0L;
		}
	}
	public static int getJInt(JSONObject parents, String param) {
		try {
			return Integer.parseInt(parents.get(param).toString());
		} catch (Exception e) {
			return 0;
		}
	}
}
