package com.ran.sns.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 13:33
 */
public class ViewObject {
	private Map<String,Object> objs = new HashMap<>();
	public void set(String key,Object value){
		objs.put(key,value);
	}

	public Object get(String key){
		return objs.get(key);
	}
}
