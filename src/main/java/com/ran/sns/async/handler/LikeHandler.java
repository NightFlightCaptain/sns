package com.ran.sns.async.handler;

import com.ran.sns.async.EventHandler;
import com.ran.sns.async.EventModel;
import com.ran.sns.async.EventType;
import com.ran.sns.model.Message;
import com.ran.sns.service.MessageService;
import com.ran.sns.service.UserService;
import com.ran.sns.util.SnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 22:26
 */
@Service
public class LikeHandler implements EventHandler{
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;

	@Override
	public void doHandler(EventModel model) {
		Message message = new Message();

		message.setFromId(SnsUtil.SYSTEM_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		message.setHasRead(0);
		message.setContent("用户"+userService.getUser(model.getActorId()).getName()+
				"赞了你的评论,http://127.0.0.1:8004/question/"+model.getExt("questionId"));

		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LIKE);
	}
}
