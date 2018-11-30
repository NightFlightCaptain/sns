package com.ran.sns.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/29 18:25
 */
@Component
@Aspect
public class LogAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

	@Before("execution(* com.ran.sns.controller.IndexController.*(..))")
	public void beforeMethod(JoinPoint joinPoint){
		StringBuilder sb = new StringBuilder();
		for (Object arg:joinPoint.getArgs()){
			if (arg!=null){
				sb.append("arg:"+arg.toString()+"|");
			}
		}
		LOGGER.info("before method:"+sb.toString());
	}
}

