package com.ran.sns.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/29 17:44
 */
//@Controller
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping(path = {"/index"},method = RequestMethod.GET)
	@ResponseBody
	public String index(HttpSession httpSession){
		LOGGER.info("VISIT HOME1");
		return "VISIT HOME1";
	}


	@RequestMapping(path = {"/vm"},method = RequestMethod.GET)
	public String template(Model model){
		List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
		model.addAttribute("colors", colors);

		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < 4; ++i) {
			map.put(String.valueOf(i), String.valueOf(i * i));
		}
		model.addAttribute("map", map);

		return "login";
	}

	@RequestMapping(path = {"/request"},method = RequestMethod.GET)
	@ResponseBody
	public String request(Model model, HttpSession httpSession,
	                      HttpServletRequest httpServletRequest,
	                      HttpServletResponse httpServletResponse,
	                      @CookieValue("JSESSIONID") String sessionId){
		StringBuilder sb = new StringBuilder();
		sb.append("COOKIEVALUE:").append(sessionId);
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()){
			String name = headerNames.nextElement();
			sb.append(name).append(":").append(httpServletRequest.getHeader(name)).append("<br>");
		}

		if (httpServletRequest.getCookies() !=null){
			for (Cookie cookie:httpServletRequest.getCookies()){
				sb.append("Cookie:").append(cookie.getName()).append(" value:").append(cookie.getValue());
			}
		}
		sb.append(httpServletRequest.getMethod()).append("<br>");
		sb.append(httpServletRequest.getQueryString()).append("<br>");
		sb.append(httpServletRequest.getPathInfo()).append("<br>");
		sb.append(httpServletRequest.getRequestURL()).append("<br>");

		httpServletResponse.addHeader("ranId","520");
		httpServletResponse.addCookie(new Cookie("ran","wan"));
		return sb.toString();

	}
}
