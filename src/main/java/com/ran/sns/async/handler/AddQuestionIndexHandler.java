package com.ran.sns.async.handler;

import com.ran.sns.async.EventHandler;
import com.ran.sns.async.EventModel;
import com.ran.sns.async.EventType;
import com.ran.sns.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/5 21:27
 */
@Component
public class AddQuestionIndexHandler implements EventHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddQuestionIndexHandler.class);

	@Autowired
	SearchService searchService;

	@Override
	public void doHandler(EventModel model) {
		try {
			searchService.indexQuestion(model.getEntityId(),
					model.getExt("title"),model.getExt("content"));
		} catch (Exception e) {
			LOGGER.error("增加题目搜索索引出错"+e.getMessage());
		}
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.ADD_QUESTION);
	}
}
