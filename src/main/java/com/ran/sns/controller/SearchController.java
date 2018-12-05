package com.ran.sns.controller;

import com.ran.sns.model.EntityType;
import com.ran.sns.model.Question;
import com.ran.sns.model.ViewObject;
import com.ran.sns.service.FollowService;
import com.ran.sns.service.QuestionService;
import com.ran.sns.service.SearchService;
import com.ran.sns.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/5 16:04
 */
@Controller
public class SearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	SearchService searchService;

	@Autowired
	QuestionService questionService;

	@Autowired
	FollowService followService;

	@Autowired
	UserService userService;

	@RequestMapping(path = "/search",method = RequestMethod.GET)
	public String search(Model model, @RequestParam("keyword")String keyword,
	                     @RequestParam(value = "offset",defaultValue = "0")int offset,
	                     @RequestParam(value = "count",defaultValue = "10")int count){
		try {

			List<Question> questionList = searchService.searchQuestion(keyword,offset,count,"<em>","</em>");

			List<ViewObject> vos = new ArrayList<>();

			for (Question question:questionList){
				Question wholeQuestion = questionService.getQuestionById(question.getId());
				ViewObject vo = new ViewObject();
				if (question.getTitle() !=null){
					wholeQuestion.setTitle(question.getTitle());
				}
				if (question.getContent()!=null){
					wholeQuestion.setContent(question.getContent());
				}
				vo.set("question",wholeQuestion);
				vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,wholeQuestion.getId()));
				vo.set("user",userService.getUser(wholeQuestion.getUserId()));
				vos.add(vo);
			}
			model.addAttribute("vos",vos);
			model.addAttribute("keyword",keyword);
		} catch (Exception e) {
			LOGGER.error("搜索评论失败"+e.getMessage());
		}
		return "result";
	}

}
