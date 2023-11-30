package com.soluvis.croffle.v1.scheduler.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SchedulerWebMvcConfig implements WebMvcConfigurer {

	@Autowired
	SchedulerInterceptor interceptor;

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.extendMessageConverters(converters);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry) ;

		registry.addInterceptor(interceptor).addPathPatterns("/v1/api/scheduler/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);

		registry.addMapping("/**").allowedOrigins("http://localhost:8101").allowedMethods("GET","POST","PUT","DELETE","PATCH");
	}



}
