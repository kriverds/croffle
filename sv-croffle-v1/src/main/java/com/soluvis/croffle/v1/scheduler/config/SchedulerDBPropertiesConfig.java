package com.soluvis.croffle.v1.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerDBPropertiesConfig {

	@Bean
	@ConfigurationProperties(prefix="com.soluvis.croffle.v1.scheduler")
	SchedulerRdsProperties schedulerRdsProperties(){
		return new SchedulerRdsProperties();
	}
}
