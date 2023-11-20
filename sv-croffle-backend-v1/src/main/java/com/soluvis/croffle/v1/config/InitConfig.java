package com.soluvis.croffle.v1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.soluvis.croffle.v1.ha.config.HAConfig;

import jakarta.annotation.PostConstruct;

@Component
public class InitConfig {


	private final Logger logger = LoggerFactory.getLogger(InitConfig.class);


	@Value(value = "${ha.default.role}")
	private String defaultRole;

	@PostConstruct
	private void init() {
		logger.info("set Role [{}]", defaultRole);
		HAConfig.setDefaultRole(defaultRole);
		HAConfig.setCurrentRole(defaultRole);
	}
}
