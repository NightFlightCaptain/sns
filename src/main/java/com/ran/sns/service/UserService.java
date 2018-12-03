package com.ran.sns.service;

import com.ran.sns.dao.LoginTicketDAO;
import com.ran.sns.dao.UserDAO;
import com.ran.sns.model.LoginTicket;
import com.ran.sns.model.User;
import com.ran.sns.util.SnsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/29 21:24
 */
@Service
public class UserService {
	@Autowired
	UserDAO userDAO;

	@Autowired
	LoginTicketDAO loginTicketDAO;


	public User selectByName(String name){
		return userDAO.selectByName(name);
	}

	/**
	 *
	 * @param username
	 * @param password
	 * @return 如何失败，map中包含msg的key；如果成功，map中包含ticket
	 */
	public Map<String,Object> register(String username,String password){
		Map<String,Object> map = new HashMap<String,Object>();
		if (StringUtils.isBlank(username)){
			map.put("msg","用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)){
			map.put("msg","密码不能为空");
			return map;
		}

		User user = userDAO.selectByName(username);
		if (user != null){
			map.put("msg","用户名已经被注册");
			return map;
		}

		user = new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		user.setPassword(SnsUtil.MD5(password+user.getSalt()));
		String headUrl = String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
		user.setHeadUrl(headUrl);
		userDAO.addUser(user);

		String ticket  = addLoginTicket(user.getId());
		map.put("ticket",ticket);
		return map;
	}

	public static void main(String[] args) {
		for (int i =0;i<10;i++){

		}
	}

	public Map<String,Object> login(String username,String password){
		Map<String,Object> map = new HashMap<String,Object>();
		if (StringUtils.isBlank(username)){
			map.put("msg","用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)){
			map.put("msg","密码不能为空");
			return map;
		}

		User user = userDAO.selectByName(username);
		if (user == null){
			map.put("msg","用户不存在");
			return map;
		}

		if (!SnsUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
			map.put("msg","密码不正确");
			return map;
		}

		String ticket  = addLoginTicket(user.getId());
		map.put("ticket",ticket);
		map.put("userId",user.getId());
		return map;
	}

	public User getUser(int id){
		return userDAO.selectById(id);
	}

	public void logout(String ticket){
		loginTicketDAO.updateStatus(ticket,1);
	}

	/**
	 * 添加t票，之后可以改写到redis中
	 * @param userId
	 * @return
	 */
	private String addLoginTicket(int userId){
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(userId);
		Date date = new Date();
		date.setTime(date.getTime() + 1000*3600*24);//一天
		loginTicket.setExpired(date);
		loginTicket.setStatus(0);
		loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
		loginTicketDAO.addTicket(loginTicket);
		return loginTicket.getTicket();
	}
}
