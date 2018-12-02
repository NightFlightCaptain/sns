package com.ran.sns.service;

import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 13:47
 */
@Service
public class LikeService {
	@Autowired
	private JedisAdapter jedisAdapter;

	public long like(int userId, int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.sadd(likeKey, String.valueOf(userId));

		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
		jedisAdapter.srem(disLikeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
	}


	public long disLike(int userId, int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.srem(likeKey, String.valueOf(userId));

		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
		jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

		return jedisAdapter.scard(likeKey);
	}


	public long getLikeCount(int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		return jedisAdapter.scard(likeKey);
	}

	public int getLikeStatus(int userId, int entityType, int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
			return 1;
		}
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
		return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
	}

}
