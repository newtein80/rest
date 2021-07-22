package com.service.rest.common.config;

import java.util.Arrays;
import java.util.List;

import com.service.rest.common.interceptor.RestRequestInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    private static final List<String> URL_PATTERNS = Arrays.asList("/async/*", "/board", "/user", "/user/**");  //인터셉터가 동작 해야 될 요청 주소 mapping 목록
	
	//인터셉터 주소 세팅
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RestRequestInterceptor()).addPathPatterns(URL_PATTERNS);
	}
}
