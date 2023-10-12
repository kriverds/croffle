package com.soluvis.croffle.v1.sample.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleRdsProperties {
	private String jdbcurl = null;
	private String username = null;

	private String rdsType = null;
	private String rdsHost = null;
	private int rdsPort = 0;

	private final Logger logger = LoggerFactory.getLogger(SampleRdsProperties.class);

	boolean parse(){
		Pattern jdbcUrlPattern = Pattern.compile("jdbc:([\\w:]*)://([^:]*):?(\\d*)?/.*");
		Matcher m = jdbcUrlPattern.matcher(jdbcurl);

		if(!m.matches()) return false;

		rdsType = m.group(1);
		rdsHost = m.group(2);
		String port = m.group(3);

		if(rdsType == null || rdsType.isEmpty() || rdsHost == null || rdsHost.isEmpty()) return false;

		if(port == null || port.isEmpty()){
			if(rdsType.startsWith("mysql")){
				rdsPort = 3306;
			}else if(rdsType.startsWith("redshift")){
				rdsPort = 5439;
			}
		}else{
			rdsPort = Integer.parseInt(port);
		}
		return true;
	}

	public boolean isValid(){
		boolean result = false;
		if(jdbcurl == null || username == null) {
			logger.error("{}", "jdbcurl == null || username == null");
		} else if(rdsType == null || rdsHost == null || rdsPort == 0 && (!parse())) {
			logger.error("{}", "rdsType == null || rdsHost == null || rdsPort == 0 && (!parse())");
		} else {
			result = true;
		}
		return result;
	}

	public String getJdbcurl() {
		return jdbcurl;
	}

	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRdsType() {
		return rdsType;
	}

	public void setRdsType(String rdsType) {
		this.rdsType = rdsType;
	}

	public String getRdsHost() {
		return rdsHost;
	}

	public void setRdsHost(String rdsHost) {
		this.rdsHost = rdsHost;
	}

	public int getRdsPort() {
		return rdsPort;
	}

	public void setRdsPort(int rdsPort) {
		this.rdsPort = rdsPort;
	}



}
