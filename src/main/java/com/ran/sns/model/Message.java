package com.ran.sns.model;

import java.util.Date;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 21:34
 */
public class Message {
	private int id;
	private int fromId;
	private int toId;
	private String content;
	private Date createdDate;
	private int hasRead;
	private String conversationId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getHasRead() {
		return hasRead;
	}

	public void setHasRead(int hasRead) {
		this.hasRead = hasRead;
	}

	public String getConversationId() {
		if (fromId < toId){
			return String.valueOf(fromId)+"_"+String.valueOf(toId);
		}else {
			return String.valueOf(toId)+"_"+String.valueOf(fromId);
		}
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
}