package com.ran.sns.service;

import com.ran.sns.dao.CommentDAO;
import com.ran.sns.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 17:28
 */
@Service
public class CommentService {
	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private SensitiveService sensitiveService;

	public int addComment(Comment comment){
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		comment.setContent(sensitiveService.filter(comment.getContent()));
		return commentDAO.addComment(comment)>0?comment.getId():0;
	}

	public List<Comment> getCommentByEntity(int entityId, int entityType){
		return commentDAO.selectByEntity(entityId, entityType);
	}

	public int getCountByUserId(int userId){
		return commentDAO.getUserCount(userId);
	}

	public int getCountByEntity(int entityId, int entityType){
		return commentDAO.getCommentCount(entityId, entityType);
	}

	public Comment getCommentById(int id){
		return commentDAO.selectById(id);
	}

	public int getCommentCount(int entityId, int entityType){
		return commentDAO.getCommentCount(entityId, entityType);
	}

}
