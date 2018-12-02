package com.ran.sns.controller;

import com.ran.sns.model.HostHolder;
import com.ran.sns.model.Message;
import com.ran.sns.model.ViewObject;
import com.ran.sns.service.MessageService;
import com.ran.sns.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/1 10:16
 */
@Controller
public class MessageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private UserService userService;

	@RequestMapping(path = "/msg/list", method = RequestMethod.GET)
	public String getConversationList(Model model) {
		if (hostHolder.getUser() == null) {
			return "redirect:/reglogin";
		}
		int localUserId = hostHolder.getUser().getId();
		List<Message> messages = messageService.getConversationList(localUserId, 0, 10);
		List<ViewObject> vos = new ArrayList<>();
		for (Message message : messages) {
			ViewObject vo = new ViewObject();
			vo.set("message", message);
			int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
			vo.set("user", userService.getUser(targetId));
			vo.set("unread",messageService.getConversationUnreadCount(message.getConversationId()));
			vos.add(vo);
		}
		model.addAttribute("conversations",vos);
		return "letter";
	}

	@RequestMapping(path = "/msg/detail",method = RequestMethod.GET)
	public String getMessageDetail(Model model,@RequestParam("conversationId") String conversationId){
		try {
			List<Message> messages = messageService.getConversationDetail(conversationId,0,10);
			List<ViewObject> vos = new ArrayList<>();
			for (Message message:messages){
				ViewObject vo = new ViewObject();
				vo.set("message",message);
				vo.set("user",userService.getUser(message.getFromId()));
				vos.add(vo);
			}
			model.addAttribute("messages",vos);
		} catch (Exception e) {
			LOGGER.error("打开消息列表详细页面失败",e.getMessage());
		}
		return "letterDetail";
	}
}
