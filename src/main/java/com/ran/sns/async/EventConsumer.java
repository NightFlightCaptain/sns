package com.ran.sns.async;

import com.alibaba.fastjson.JSONObject;
import com.ran.sns.util.JedisAdapter;
import com.ran.sns.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 17:07
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);
	Map<EventType, List<EventHandler>> config = new HashMap<>();
	private ApplicationContext applicationContext;

	@Autowired
	private JedisAdapter jedisAdapter;

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		if (beans != null) {
			for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
				List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
				for (EventType eventType : eventTypes) {
					if (!config.containsKey(eventType)) {
						config.put(eventType, new ArrayList<EventHandler>());
					}
					config.get(eventType).add(entry.getValue());
				}
			}
		}

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String key = RedisKeyUtil.getEventQueueKey();
					List<String> events = jedisAdapter.brpop(0, key);

					for (String message : events) {
						if (key.equals(message)) {
							continue;
						}

						EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
						if (!config.containsKey(eventModel.getType())){
							LOGGER.error("无法识别的事件handler");
							continue;
						}
						for (EventHandler eventHandler:config.get(eventModel.getType())){
							eventHandler.doHandler(eventModel);
						}

					}
				}
			}
		});
		thread.start();

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
