package com.ran.sns.interceptor;

import com.ran.sns.dao.LoginTicketDAO;
import com.ran.sns.dao.UserDAO;
import com.ran.sns.model.HostHolder;
import com.ran.sns.model.LoginTicket;
import com.ran.sns.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 13:49
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private LoginTicketDAO loginTicketDAO;

	@Autowired
	private UserDAO userDAO;


	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		String ticket = null;
		if (httpServletRequest.getCookies() != null) {
			for (Cookie cookie : httpServletRequest.getCookies()) {
				if ("ticket".equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}

		if (ticket != null) {
			LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
			if (loginTicket==null || loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
				return true;
			}
			User user = userDAO.selectById(loginTicket.getUserId());
			hostHolder.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		if (modelAndView!=null&&hostHolder.getUser()!=null){
			modelAndView.addObject("user",hostHolder.getUser());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
		hostHolder.clear();
	}
}
