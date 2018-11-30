package com.ran.sns;

import com.ran.sns.service.SensitiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 16:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SnsApplication.class)
@WebAppConfiguration
public class SensitiveTest {
	@Autowired
	SensitiveService sensitiveService;

	@Test
	public void SensitiveWordsTest(){

		System.out.println(sensitiveService.filter("你好色情啊"));
	}
}
