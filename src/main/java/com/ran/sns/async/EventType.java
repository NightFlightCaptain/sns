package com.ran.sns.async;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/2 16:53
 */
public enum  EventType {
	LIKE(0),
	COMMIT(1),
	LOGIN(2),
	MAIL(3),
	FOLLOW(4),
	UNFOLLOW(5);

	private int value;
	EventType(int value){
		this.value = value;
	}
	public int getValue(){
		return value;
	}
}
