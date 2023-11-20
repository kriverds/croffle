package com.soluvis.croffle.v1.ha.config;

public class HAConfig {

	private HAConfig() {
	    throw new IllegalStateException("Utility class");
	  }

	private static String defaultRole = "";
	private static String currentRole = "";

	private static boolean emergencyFlag = false;

	public static void setRoleDefault() {
		currentRole = defaultRole;
	}

	public static void setRolePrimary() {
		currentRole = "primary";
	}

	public static boolean isEmergencyFlag() {
		return emergencyFlag;
	}

	public static void setEmergencyFlag(boolean emergencyFlag) {
		HAConfig.emergencyFlag = emergencyFlag;
	}

	public static String getCurrentRole() {
		return currentRole;
	}

	public static String getDefaultRole() {
		return defaultRole;
	}

	public static void setDefaultRole(String defaultRole) {
		HAConfig.defaultRole = defaultRole;
	}

	public static void setCurrentRole(String currentRole) {
		HAConfig.currentRole = currentRole;
	}


}
