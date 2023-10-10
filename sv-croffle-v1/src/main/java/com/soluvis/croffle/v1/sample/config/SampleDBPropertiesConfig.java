package com.soluvis.croffle.v1.sample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleDBPropertiesConfig {

	@Bean
	@ConfigurationProperties(prefix="com.soluvis.croffle.v1.sample")
	SampleRdsProperties sampleRdsProperties(){
		return new SampleRdsProperties();
	}
}
