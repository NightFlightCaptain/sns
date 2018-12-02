package com.ran.sns.controller;

import com.ran.sns.model.Comment;
import com.ran.sns.model.EntityType;
import com.ran.sns.model.HostHolder;
import com.ran.sns.service.CommentService;
import com.ran.sns.service.LikeService;
import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 14:04
 */
@Controller
public class LikeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LikeController.class);
	@Autowired
	private LikeService likeService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	JedisAdapter jedisAdapter;

	@RequestMapping(path = "/like",method = RequestMethod.POST)
	@ResponseBody
	public String like(@RequestParam("commentId")int commentId){
		if(hostHolder.getUser()==null){
			return SnsUtil.getJSONString(999);
		}
		Comment comment = commentService.getCommentById(commentId);

		long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);

		return SnsUtil.getJSONString(0,String.valueOf(likeCount));
	}


	@RequestMapping(path = "/dislike",method = RequestMethod.POST)
	@ResponseBody
	public String dislike(@RequestParam("commentId")int commentId){
		if(hostHolder.getUser()==null){
			return SnsUtil.getJSONString(999);
		}
		Comment comment = commentService.getCommentById(commentId);

		long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
		return SnsUtil.getJSONString(0,String.valueOf(likeCount));
	}
}
