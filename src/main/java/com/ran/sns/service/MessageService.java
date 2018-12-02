package com.ran.sns.service;

import com.ran.sns.dao.MessageDAO;
import com.ran.sns.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 22:00
 */
@Service
public class MessageService {
	@Autowired
	private MessageDAO messageDAO;

	@Autowired
	private SensitiveService sensitiveService;

	public int addMessage(Message message){
		message.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(message.getContent())));
		return messageDAO.addMessage(message)>0?message.getId():0;
	}

	public List<Message> getConversationList(int userId,int offset,int limit){
		return messageDAO.getConversationList(userId, offset, limit);
	}

	public List<Message> getConversationDetail(String conversationId,int offset,int limit){
		return messageDAO.getConversationDetail(conversationId, offset, limit);
	}

	public int getConversationUnreadCount(String conversationId){
		return messageDAO.getConversationUnreadCount(conversationId);
	}
}
