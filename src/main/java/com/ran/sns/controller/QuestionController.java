package com.ran.sns.controller;

import com.ran.sns.model.*;
import com.ran.sns.service.*;
import com.ran.sns.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 17:36
 */
@Controller
public class QuestionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private UserService userService;

	@Autowired
	private FollowService followService;

	@RequestMapping(value = "/question/add",method = RequestMethod.POST)
	@ResponseBody
	public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCreatedDate(new Date());
			if (hostHolder.getUser() == null){
				question.setUserId(SnsUtil.ANONYMOUS_USERID);
			}else {
				question.setUserId(hostHolder.getUser().getId());
			}
			if (questionService.addQuestion(question)>0){
				//0表示成功，可以制定相应的前后端规则
				return SnsUtil.getJSONString(0);
			}
		} catch (Exception e) {
			LOGGER.error("添加问题出错",e.getMessage());
		}
		return SnsUtil.getJSONString(1,"失败");
	}

	@RequestMapping(path = "/question/{questionId}",method = RequestMethod.GET)
	public String questionDetail(Model model, @PathVariable("questionId")int questionId){
		Question question = questionService.getQuestionById(questionId);
		model.addAttribute("question",question);

		List<Comment> commentList = commentService.getCommentByEntity(questionId, EntityType.ENTITY_QUESTION);
		List<ViewObject> comments = new ArrayList<>();

		for(Comment comment:commentList){
			ViewObject vo = new ViewObject();
			vo.set("comment",comment);
			// 当前用户对该回答是否喜欢（点赞）
			if(hostHolder.getUser()==null){
				vo.set("liked",0);
			}else {
				vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
			}
			vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
			vo.set("user",userService.getUser(comment.getUserId()));
			comments.add(vo);
		}

		model.addAttribute("comments",comments);

		List<ViewObject> followUsers = new ArrayList<>();
		List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION,questionId,20);
		for(Integer userId :users){
			ViewObject vo = new ViewObject();
			User user = userService.getUser(userId);
			if (user == null){
				continue;
			}
			vo.set("name",user.getName());
			vo.set("headUrl",user.getHeadUrl());
			vo.set("id",user.getId());
			followUsers.add(vo);
		}
		model.addAttribute("followers",followUsers);

		if (hostHolder.getUser()!=null){
			model.addAttribute("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId));
		}else {
			model.addAttribute("followed",false);
		}

		return "detail";
	}
}
