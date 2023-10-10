package com.soluvis.croffle.v1.gcloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GCloudDBPropertiesConfig {

	@Bean
	@ConfigurationProperties(prefix="com.soluvis.croffle.v1.gcloud")
	GCloudRdsProperties gCloudRdsProperties(){
		return new GCloudRdsProperties();
	}
}
