package com.ran.sns.async.handler;

import com.ran.sns.async.EventHandler;
import com.ran.sns.async.EventModel;
import com.ran.sns.async.EventType;
import com.ran.sns.model.EntityType;
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
 * Date: 2018/12/3 12:01
 */
@Service
public class FollowHandler implements EventHandler{
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
		if(model.getEntityType() == EntityType.ENTITY_USER){
			message.setContent("用户"+userService.getUser(model.getActorId()).getName() +
					"关注了你,http://127.0.0.1:8004/user/"+model.getActorId());
		}else if (model.getEntityType() == EntityType.ENTITY_QUESTION){
			message.setContent("用户"+userService.getUser(model.getActorId()).getName() +
					"关注了你的问题,http://127.0.0.1:8004/question/"+model.getEntityId());
		}
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.FOLLOW);
	}
}
