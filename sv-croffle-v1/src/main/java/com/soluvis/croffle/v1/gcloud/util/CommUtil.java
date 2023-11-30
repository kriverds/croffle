package com.soluvis.croffle.v1.gcloud.util;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public class CommUtil {

	private CommUtil() {}

	public static UUID setAttrUUID(HttpServletRequest request) {
		UUID rUUID = UUID.randomUUID();
		request.setAttribute("rUUID", rUUID);
		return rUUID;
	}
}
