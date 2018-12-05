package com.ran.sns.service;

import com.alibaba.fastjson.JSONObject;
import com.ran.sns.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/12/5 16:04
 */
@Service
public class SearchService {

	private static final String SOLR_URL = "http://127.0.0.1:8983/solr/snsss";
	private static final String QUESTION_TITLE_FIELD = "question_title";
	private static final String QUESTION_CONTENT_FIELD = "question_content";

	private HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(SOLR_URL).build();

	/**
	 * 搜索出的Question只有id，title，content三个属性
	 * @param keyword
	 * @param offset
	 * @param count
	 * @param hlPre
	 * @param hlPos
	 * @return
	 * @throws Exception
	 */
	public List<Question> searchQuestion(String keyword, int offset, int count, String hlPre, String hlPos) throws Exception {
		List<Question> questionList = new ArrayList<>();
		SolrQuery query = new SolrQuery(keyword);
		query.setRows(count);
		query.setStart(offset);
		query.setHighlight(true);
		query.setHighlightSimplePre(hlPre);
		query.setHighlightSimplePost(hlPos);

		query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);

		QueryResponse response = httpSolrClient.query(query);

		for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
			Question q = new Question();
			System.out.println(JSONObject.toJSONString(entry));
			q.setId(Integer.parseInt(entry.getKey()));
			if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
				List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
				if (titleList.size() > 0) {
					q.setTitle(titleList.get(0));
				}
			}

			if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
				List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
				if (contentList.size() > 0) {
					q.setTitle(contentList.get(0));
				}
			}
			questionList.add(q);

		}

		return questionList;
	}

	/**
	 * 新增一个问题时，将该问题添加到搜索引擎中
	 * @param questionId
	 * @param title
	 * @param content
	 * @return
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public boolean indexQuestion(int questionId, String title, String content) throws IOException, SolrServerException {
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", questionId);
		doc.setField(QUESTION_TITLE_FIELD, title);
		doc.setField(QUESTION_CONTENT_FIELD, content);
		UpdateResponse response = httpSolrClient.add(doc, 1000);
		return response != null && response.getStatus() == 0;
	}
}
