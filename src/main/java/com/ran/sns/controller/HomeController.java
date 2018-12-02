package com.ran.sns.controller;

import com.ran.sns.model.HostHolder;
import com.ran.sns.model.Question;
import com.ran.sns.model.User;
import com.ran.sns.model.ViewObject;
import com.ran.sns.service.CommentService;
import com.ran.sns.service.QuestionService;
import com.ran.sns.service.SensitiveService;
import com.ran.sns.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 13:35
 */
@Controller
public class HomeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	UserService userService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	SensitiveService sensitiveService;

	@Autowired
	QuestionService questionService;

	@Autowired
	CommentService commentService;


	private List<ViewObject> getQuestions(int userId, int offset, int limit) {
		List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
		List<ViewObject> vos = new ArrayList<>();
		for (Question question : questions) {
			ViewObject vo = new ViewObject();
			vo.set("question", question);
			vo.set("user", userService.getUser(question.getUserId()));
			vos.add(vo);
		}
		return vos;
	}

	@RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String index(Model model,
	                    @RequestParam(value = "pop", defaultValue = "0") int pop) {
		model.addAttribute("vos", getQuestions(0, 0, 10));
		return "index";
	}

	@RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String userIndex(Model model,@PathVariable("userId") int userId) {
		model.addAttribute("vos",getQuestions(userId,0,10));

		User user = userService.getUser(userId);
		ViewObject vo = new ViewObject();
		vo.set("user",user);
		vo.set("commentCount",commentService.getCountByUserId(userId));
		model.addAttribute("profileUser",vo);
		return "profile";
	}
}
