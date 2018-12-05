package com.ran.sns.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/11/30 14:55
 */
@Service
public class SensitiveService implements InitializingBean{
	private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveService.class);

	private static final String DEFAULT_REPLECEMENT = "敏感词";

	@Override
	public void afterPropertiesSet() throws Exception {
		rootNode = new TrieNode();
		InputStream is;
		try {
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("SensitiveWords.txt");
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String lineTxt;
			while ((lineTxt = bufferedReader.readLine())!=null){
				lineTxt = lineTxt.trim();
				addWord(lineTxt);
			}
			reader.close();
		}catch (Exception e ){
			LOGGER.error("读取敏感词文件出错",e.getMessage());
		}
	}

	private class TrieNode {

		private boolean end = false;

		private Map<Character, TrieNode> subNodes = new HashMap<>();

		void addSubNodes(Character key, TrieNode node) {
			subNodes.put(key, node);
		}

		TrieNode getSubNode(Character key) {
			return subNodes.get(key);
		}

		boolean isKeywordEnd() {
			return end;
		}

		void setKeywordEnd(boolean end) {
			this.end = end;
		}

		public int getSubNodeCount() {
			return subNodes.size();
		}
	}

	private TrieNode rootNode = new TrieNode();

	/**
	 * 判断是否为一个符号
	 *
	 * @param c
	 * @return
	 */
	private boolean isSymbol(char c) {
		int ic = (int) c;
		//东亚文字范围
		return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
	}

	private void addWord(String lineTxt) {
		TrieNode trieNode = rootNode;
		for (int i = 0; i < lineTxt.length(); i++) {
			char c = lineTxt.charAt(i);
			//符号
			if (isSymbol(c)) {
				continue;
			}
			TrieNode node = trieNode.getSubNode(c);
			// 没初始化
			if (node == null) {
				node = new TrieNode();
				trieNode.addSubNodes(c, node);
			}

			trieNode = node;
			if (i == lineTxt.length() - 1) {
				// 关键词结束，设置结束标志
				trieNode.setKeywordEnd(true);
			}
		}
	}

	public String filter(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}

		StringBuilder result = new StringBuilder();

		TrieNode tempNode = rootNode;
		int begin = 0;
		int position = 0;

		while (position < text.length()) {
			char c = text.charAt(position);
			if (isSymbol(c)) {
				position++;
				continue;
			}
			tempNode = tempNode.getSubNode(c);
			if (tempNode == null) {
				//以begin为首的字符不存在敏感词，也就是begin可以被排除
				result.append(text.charAt(begin));
				position = begin + 1;
				begin = position;
				tempNode = rootNode;
			} else if (tempNode.isKeywordEnd()) {
				result.append(DEFAULT_REPLECEMENT);
				position = position + 1;
				begin = position;
				tempNode = rootNode;
			} else {
				++position;
			}
		}
		return result.append(text.substring(begin)).toString();
	}


}
