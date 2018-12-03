package com.ran.sns.async;

import com.alibaba.fastjson.JSONObject;
import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 17:02
 */
@Service
public class EventProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventModel.class);

	@Autowired
	private JedisAdapter jedisAdapter;

	public boolean fireEvent(EventModel eventModel){
		try {
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisKeyUtil.getEventQueueKey();
			jedisAdapter.lpush(key,json);
			return true;
		} catch (Exception e) {
			LOGGER.error("添加队列失败"+e.getMessage());
			return false;
		}
	}
}
