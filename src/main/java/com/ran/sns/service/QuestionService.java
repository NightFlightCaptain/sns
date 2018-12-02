package com.ran.sns.service;

import com.ran.sns.dao.QuestionDAO;
import com.ran.sns.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 14:51
 */
@Service
public class QuestionService {
	@Autowired
	private QuestionDAO questionDAO;

	@Autowired
	private SensitiveService sensitiveService;

	/**
	 * 如果成功返回问题的id，不成功返回0
	 * @param question
	 * @return
	 */
	public int addQuestion(Question question){
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		question.setTitle(sensitiveService.filter(question.getTitle()));
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		question.setContent(sensitiveService.filter(question.getContent()));
		return questionDAO.addQuestion(question) >0?question.getId():0;
	}

	public List<Question> getLatestQuestions(int userId,int offset,int limit){
		return questionDAO.selectLatestQuestions(userId, offset, limit);
	}

	public int updateCommentCount(int id,int count){
		return questionDAO.updateCommentCount(id, count);
	}

	public Question getQuestionById(int id){
		return questionDAO.selectById(id);
	}
}
