package com.ran.sns.model;

import org.springframework.stereotype.Component;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 13:44
 */
@Component
public class HostHolder {
	private static ThreadLocal<User> users = new ThreadLocal<User>();

	public void setUser(User user){
		users.set(user);
	}

	public User getUser(){
		return users.get();
	}

	public void clear(){
		users.remove();
	}
}
