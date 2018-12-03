package com.ran.sns.controller;

import com.ran.sns.model.EntityType;
import com.ran.sns.model.Feed;
import com.ran.sns.model.HostHolder;
import com.ran.sns.service.FeedService;
import com.ran.sns.service.FollowService;
import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/3 18:56
 */
@Controller
public class FeedController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedController.class);

	@Autowired
	HostHolder hostHolder;

	@Autowired
	FeedService feedService;

	@Autowired
	JedisAdapter jedisAdapter;

	@Autowired
	FollowService followService;

	@RequestMapping(path = "/pushfeeds", method = {RequestMethod.GET, RequestMethod.POST})
	public String getPushFeeds(Model model) {
		int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
		List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0,10);
		List<Feed> feeds = new ArrayList<>();
		for (String feedId:feedIds){
			Feed feed = feedService.getById(Integer.parseInt(feedId));
			if (feed!=null){
				feeds.add(feed);
			}
		}
		model.addAttribute("feeds",feeds);
		return "feeds";
	}

	@RequestMapping(path = "/pullfeeds",method = {RequestMethod.GET,RequestMethod.POST})
	public String getPullFeeds(Model model){
		int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
		List<Integer> followees = new ArrayList<>();

		if (localUserId !=0){
			followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);
		}
		List<Feed> feeds = feedService.getUserFeeds(followees,10);
		model.addAttribute("feeds",feeds);
		return "feeds";
	}
}
