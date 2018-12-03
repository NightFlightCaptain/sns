package com.ran.sns.service;

import com.ran.sns.dao.FeedDAO;
import com.ran.sns.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/3 19:35
 */
@Service
public class FeedService {
	@Autowired
	FeedDAO feedDAO;

	public List<Feed> getUserFeeds(List<Integer> userIds, int count) {
		return feedDAO.selectUserFeeds(userIds, count);
	}

	public boolean addFeed(Feed feed) {
		feedDAO.addFeed(feed);
		return feed.getId() > 0;
	}

	public Feed getById(int id) {
		return feedDAO.selectFeedById(id);
	}
}
