package com.ran.sns.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 22:40
 */
@Service
public class MailSender implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
	private JavaMailSenderImpl mailSender;


	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
