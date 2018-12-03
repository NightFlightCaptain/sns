package com.ran.sns.service;

import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/3 10:39
 */
@Service
public class FollowService {
	@Autowired
	JedisAdapter jedisAdapter;

	/**
	 * 用户关注了某个实体，可以是用户，问题，评论
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean follow(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

		Jedis jedis = jedisAdapter.getJedis();
		Transaction tx = jedisAdapter.multi(jedis);

		Date date = new Date();
		//某个实体被 该用户关注了——粉丝
		tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
		//该用户 关注了某个实体——关注
		tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
		List<Object> ret = jedisAdapter.exec(tx, jedis);
		return ret.size() == 2 && (long) ret.get(0) > 0 && (long) ret.get(1) > 0;
	}

	public boolean unfollow(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

		Jedis jedis = jedisAdapter.getJedis();
		Transaction tx = jedisAdapter.multi(jedis);

		//某个实体被 该用户关注了——粉丝
		tx.zrem(followerKey, String.valueOf(userId));
		//该用户 关注了某个实体——关注
		tx.zrem(followeeKey, String.valueOf(entityId));
		List<Object> ret = jedisAdapter.exec(tx, jedis);
		return ret.size() == 2 && (long) ret.get(0) > 0 && (long) ret.get(1) > 0;
	}

	public List<Integer> getFollowers(int entityType, int entityId, int count) {
		return getFollowers(entityType, entityId, 0, count);
	}

	public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
	}

	public List<Integer> getFollowees(int userId, int entityType, int count) {
		return getFollowees(userId, entityType, 0, count);
	}

	public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset + count));
	}

	public Long getFollowerCount(int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdapter.zcard(followerKey);
	}

	public Long getFolloweeCount(int userId, int entityType) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return jedisAdapter.zcard(followeeKey);
	}

	/**
	 * 判断某个用户是否关注了某个实体
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean isFollower(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
	}

	private List<Integer> getIdsFromSet(Set<String> idSet) {
		List<Integer> ids = new ArrayList<>();
		for (String id : idSet) {
			ids.add(Integer.parseInt(id));
		}
		return ids;
	}
}
