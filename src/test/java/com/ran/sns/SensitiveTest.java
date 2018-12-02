package com.ran.sns;

import com.ran.sns.model.Message;
import com.ran.sns.model.Question;
import com.ran.sns.model.User;
import com.ran.sns.service.MessageService;
import com.ran.sns.service.QuestionService;
import com.ran.sns.service.SensitiveService;
import com.ran.sns.service.UserService;
import com.ran.sns.util.SnsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 16:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SnsApplication.class)
@WebAppConfiguration
public class SensitiveTest {
	@Autowired
	SensitiveService sensitiveService;

	@Autowired
	QuestionService questionService;

	@Autowired
	UserService userService;

	@Autowired
	MessageService messageService;

	@Test
	public void SensitiveWordsTest(){
		for (int i =2;i<13;i++){
			Message message = new Message();
			message.setContent("message"+String.valueOf(i));
			int fromId = new Random().nextInt(9)+3;
			message.setFromId(new Random().nextInt(9)+3);
			int toId = new Random().nextInt(9) + 3;
			while (fromId == toId) {
				toId = new Random().nextInt(9) + 3;
			}
			message.setToId(toId);
			message.setCreatedDate(new Date(new Date().getTime()-1000*3600*i));
			message.setHasRead(0);
			messageService.addMessage(message);
		}

	}
}
