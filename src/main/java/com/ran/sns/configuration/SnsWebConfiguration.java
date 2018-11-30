package com.ran.sns.configuration;

import com.ran.sns.interceptor.LoginRequiredInterceptor;
import com.ran.sns.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 13:59
 */
@Component
public class SnsWebConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	PassportInterceptor passportInterceptor;

	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}
}
