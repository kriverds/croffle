package com.soluvis.croffle.v1.lgup.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soluvis.croffle.v1.config.RdsProperties;

@Configuration
public class LGUPDBPropertiesConfig {

	@Bean
	@ConfigurationProperties(prefix="com.soluvis.croffle.v1.lgup")
	RdsProperties lGUPRdsProperties(){
		return new RdsProperties();
	}
}
