package com.ran.sns.controller;

import com.ran.sns.async.EventModel;
import com.ran.sns.async.EventProducer;
import com.ran.sns.async.EventType;
import com.ran.sns.model.*;
import com.ran.sns.service.*;
import com.ran.sns.util.SnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/3 11:51
 */
@Controller
public class FollowController {
	@Autowired
	private FollowService followService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private EventProducer eventProducer;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@RequestMapping(path = "/followUser", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String followUser(@RequestParam("userId") int userId) {
		if (hostHolder.getUser() == null) {
			return SnsUtil.getJSONString(999);
		}

		boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setEntityType(EntityType.ENTITY_USER)
				.setEntityId(userId)
				.setActorId(hostHolder.getUser().getId())
				.setEntityOwnerId(userId));

		//返回当前用户关注的人数
		return SnsUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
	}

	@RequestMapping(path = "/unfollowUser", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String unfollowUser(@RequestParam("userId") int userId) {
		if (hostHolder.getUser() == null) {
			return SnsUtil.getJSONString(999);
		}

		boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
				.setEntityType(EntityType.ENTITY_USER)
				.setEntityId(userId)
				.setActorId(hostHolder.getUser().getId())
				.setEntityOwnerId(userId));

		//返回当前用户关注的人数
		return SnsUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
	}

	@RequestMapping(path = "/followQuestion", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String followQuestion(@RequestParam("questionId") int questionId) {
		User actorUser = hostHolder.getUser();
		if (actorUser == null) {
			return SnsUtil.getJSONString(999);
		}

		Question question = questionService.getQuestionById(questionId);
		if (question == null){
			return SnsUtil.getJSONString(1,"字符串不存在");
		}

		boolean ret = followService.follow(actorUser.getId(), EntityType.ENTITY_QUESTION, questionId);

		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				.setEntityType(EntityType.ENTITY_QUESTION)
				.setEntityId(questionId)
				.setActorId(actorUser.getId())
				.setEntityOwnerId(question.getUserId()));


		Map<String,Object> info = new HashMap<>();
		info.put("headUrl",actorUser.getHeadUrl());
		info.put("name",actorUser.getName());
		info.put("id",actorUser.getId());
		info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
		//返回当前用户关注的人数
		return SnsUtil.getJSONString(ret ? 0 : 1, info);
	}

	@RequestMapping(path = "/unfollowQuestion", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String unfollowQuestion(@RequestParam("questionId") int questionId) {
		User actorUser = hostHolder.getUser();
		if (actorUser == null) {
			return SnsUtil.getJSONString(999);
		}

		Question question = questionService.getQuestionById(questionId);
		if (question == null){
			return SnsUtil.getJSONString(1,"问题不存在");
		}

		boolean ret = followService.unfollow(actorUser.getId(), EntityType.ENTITY_QUESTION, questionId);

		Map<String,Object> info = new HashMap<>();
		info.put("headUrl",actorUser.getHeadUrl());
		info.put("name",actorUser.getName());
		info.put("id",actorUser.getId());
		info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
		//返回当前用户关注的人数
		return SnsUtil.getJSONString(ret ? 0 : 1, info);
	}

	@RequestMapping(path = {"/user/{userId}/followers"},method = RequestMethod.GET)
	public String followers(Model model,@PathVariable("userId")int userId){
		List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER,userId,20);
		if (hostHolder.getUser()==null){
			model.addAttribute("followers",getUserInfo(0,followerIds));
		}else {
			model.addAttribute("followers",getUserInfo(hostHolder.getUser().getId(),followerIds));
		}

		model.addAttribute("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
		model.addAttribute("followeeCount",followService.getFolloweeCount(EntityType.ENTITY_USER,userId));
		model.addAttribute("curUser",userService.getUser(userId));
		return "followers";
	}

	@RequestMapping(path = {"/user/{userId}/followees"},method = RequestMethod.GET)
	public String followees(Model model,@PathVariable("userId")int userId){
		List<Integer> followeeIds = followService.getFollowees(userId,EntityType.ENTITY_USER,20);
		if (hostHolder.getUser()==null){
			model.addAttribute("followees",getUserInfo(0,followeeIds));
		}else {
			model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(),followeeIds));
		}

		model.addAttribute("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
		model.addAttribute("followeeCount",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
		model.addAttribute("curUser",userService.getUser(userId));
		return "followees";
	}

	private List<ViewObject> getUserInfo(int localUserId,List<Integer> userIds){
		List<ViewObject> userInfos = new ArrayList<>();
		for (Integer id:userIds){
			User user = userService.getUser(id);
			if (user==null){
				continue;
			}

			ViewObject vo = new ViewObject();
			vo.set("user",user);
			vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,id));
			vo.set("followeeCount",followService.getFolloweeCount(id,EntityType.ENTITY_USER));
			vo.set("commentCount",commentService.getCountByUserId(id));
			if (localUserId!=0){
				vo.set("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,id));
			}else {
				vo.set("followed",false);
			}
			userInfos.add(vo);
		}
		return userInfos;
	}
}
