package com.soluvis.croffle.v1.lgup.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LGUPWebMvcConfig implements WebMvcConfigurer {

	@Autowired
	LGUPInterceptor interceptor;

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.extendMessageConverters(converters);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry) ;

		registry.addInterceptor(interceptor).addPathPatterns("/v1/api/**");
	}

}
