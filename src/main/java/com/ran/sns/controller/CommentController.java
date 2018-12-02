package com.ran.sns.controller;

import com.ran.sns.model.Comment;
import com.ran.sns.model.EntityType;
import com.ran.sns.model.HostHolder;
import com.ran.sns.service.CommentService;
import com.ran.sns.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 21:17
 */
@Controller
public class CommentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private CommentService commentService;


	@RequestMapping(path = "/addComment",method = RequestMethod.POST)
	public String addComment(@RequestParam("questionId")int questionId,
	                         @RequestParam("content")String content){
		try {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setCreatedDate(new Date());
			comment.setEntityId(questionId);
			comment.setEntityType(EntityType.ENTITY_QUESTION);
			if (hostHolder.getUser() == null){
				comment.setUserId(SnsUtil.ANONYMOUS_USERID);
			}else {
				comment.setUserId(hostHolder.getUser().getId());
			}
			commentService.addComment(comment);

		} catch (Exception e) {
			LOGGER.error("添加评论出错",e.getMessage());
		}
		return "redirect:/question/"+questionId;
	}

}
