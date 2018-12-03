package com.ran.sns.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.ran.sns.async.EventHandler;
import com.ran.sns.async.EventModel;
import com.ran.sns.async.EventType;
import com.ran.sns.model.EntityType;
import com.ran.sns.model.Feed;
import com.ran.sns.model.Question;
import com.ran.sns.model.User;
import com.ran.sns.service.FeedService;
import com.ran.sns.service.FollowService;
import com.ran.sns.service.QuestionService;
import com.ran.sns.service.UserService;
import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/3 18:55
 */
@Service
public class FeedHandler implements EventHandler {

	@Autowired
	UserService userService;

	@Autowired
	FeedService feedService;

	@Autowired
	QuestionService questionService;

	@Autowired
	FollowService followService;

	@Autowired
	JedisAdapter jedisAdapter;

	private String buildFeedDate(EventModel eventModel) {
		Map<String, String> map = new HashMap<>();
//      触发用户是通用的
		User actor = userService.getUser(eventModel.getActorId());
		if (actor == null) {
			return null;
		}

		map.put("userName", actor.getName());
		map.put("userId", String.valueOf(actor.getId()));
		map.put("userHeadUrl", actor.getHeadUrl());

//		回答问题或者关注问题才会触发新鲜事
		if (EventType.COMMIT == eventModel.getType() ||
				(EventType.FOLLOW == eventModel.getType() && EntityType.ENTITY_QUESTION == eventModel.getEntityType())) {
			Question question = questionService.getQuestionById(eventModel.getEntityId());
			if (question == null) {
				return null;
			}
			map.put("questionId", String.valueOf(question.getId()));
			map.put("questionTitle", question.getTitle());
			return JSONObject.toJSONString(map);
		}
		return null;
	}

	@Override
	public void doHandler(EventModel model) {
		//为了测试，把model的userId随机
//		Random random = new Random();
//		model.setActorId(1 + random.nextInt(10));

		//构建一个Feed
		Feed feed = new Feed();
		feed.setCreatedDate(new Date());
		feed.setType(model.getType().getValue());
		feed.setUserId(model.getActorId());
		feed.setData(buildFeedDate(model));
		if (feed.getData() == null) {
			//不支持的Feed
			return;
		}
//		将feed存入数据库，便于之后的pullfeeds
		feedService.addFeed(feed);
		//向用户的全部粉丝推事件
		List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, feed.getUserId(), Integer.MAX_VALUE);
		//系统队列，0代表系统用户
		followerIds.add(0);
		for (int followerId : followerIds) {
			String timelineKey = RedisKeyUtil.getTimelineKey(followerId);
			jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
			//限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
		}
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.COMMIT, EventType.FOLLOW);
	}
}
