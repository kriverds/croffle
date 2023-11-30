package com.soluvis.croffle.v1.lgup.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LGUPDBPropertiesConfig {

	@Bean
	@ConfigurationProperties(prefix="com.soluvis.croffle.v1.lgup")
	LGUPRdsProperties lGUPRdsProperties(){
		return new LGUPRdsProperties();
	}
}
