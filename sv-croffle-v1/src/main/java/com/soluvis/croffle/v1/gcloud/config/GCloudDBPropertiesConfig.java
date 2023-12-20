package com.soluvis.croffle.v1.gcloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soluvis.croffle.v1.config.RdsProperties;

@Configuration
public class GCloudDBPropertiesConfig {

	@Bean
	@ConfigurationProperties(prefix="com.soluvis.croffle.v1.gcloud")
	RdsProperties gCloudRdsProperties(){
		return new RdsProperties();
	}
}
