package com.ran.sns.util;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 13:49
 */
public class RedisKeyUtil {
	private static final String SPLIT = ":";
	private static final String BIZ_LIKE = "LIKE";
	private static final String BIZ_DISLIKE = "DISLIKE";
	private static final String BIZ_EVENTQUEUE = "EVENT_QUEUE";

	private static final String BIZ_FOLLOWER = "FOLLOWER";
	private static final String BIZ_FOLLOWEE = "FOLLOWEE";
	private static final String BIZ_TIMELINE = "TIMELINE";

	public static String getLikeKey(int entityType, int entityId) {
		return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getDisLikeKey(int entityType, int entityId) {
		return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getFollowerKey(int entityType, int entityId) {
		return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getFolloweeKey(int userId,int entityType) {
		return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
	}

	public static String getEventQueueKey(){
		return BIZ_EVENTQUEUE;
	}

	public static String getTimelineKey(){
		return BIZ_TIMELINE;
	}

}
