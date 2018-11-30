package com.ran.sns.controller;

import com.ran.sns.model.HostHolder;
import com.ran.sns.model.Question;
import com.ran.sns.service.QuestionService;
import com.ran.sns.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 17:36
 */
@Controller
public class QuestionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private QuestionService questionService;

	@RequestMapping(value = "/question/add",method = RequestMethod.POST)
	@ResponseBody
	public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCreatedDate(new Date());
			if (hostHolder.getUser() == null){
				question.setUserId(SnsUtil.ANONYMOUS_USERID);
			}else {
				question.setUserId(hostHolder.getUser().getId());
			}
			if (questionService.addQuestion(question)>0){
				//0表示成功，可以制定相应的前后端规则
				return SnsUtil.getJSONString(0);
			}
		} catch (Exception e) {
			LOGGER.error("添加问题出错",e.getMessage());
		}
		return SnsUtil.getJSONString(1,"失败");
	}
}
