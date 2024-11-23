package com.project.homepage.cmmn.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.ResponseCode;

@Component
public class RSSParseUtil {
	private final String url;
	Map<String, Object> responseMap = new HashMap<String, Object>();
//	private final int fixedRate     = 60000;   // 1분(테스트용)
	private final int fixedRate     = 3600000; // 1시간(배포용)
	NodeList list					= null;
	Logger log					    = LoggerFactory.getLogger(getClass());
	
	public RSSParseUtil(@Value("${rss.url}") String url) {
		this.url = url;
	}
	
	@Scheduled(fixedRate = fixedRate)
	public void rssListGet() throws ParserConfigurationException, SAXException, IOException {
		try {
			responseMap.clear();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder 	   = factory.newDocumentBuilder();
			Document document 			   = builder.parse(url);
			this.list 					   = document.getElementsByTagName("item");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			responseMap.put(Const.RESULT, ResponseCode.RSS_PARSE_ERROR.code);
		}
	}
	
	public Map<String, Object> rssGet(int page) {
		List<Map<String, Object>> rss = new ArrayList<Map<String, Object>>();
		int amount = 10;
		int offset = (page - 1) * 10;
		int total  = list.getLength();
		int idx    = Math.min(offset + amount, list.getLength());

		for (int i = offset; i < idx; i++) {
			Node node = list.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;

				// 카테고리 제거 표출용
//				String title_ 			= element.getElementsByTagName("title").item(0).getTextContent();
//				String title		    = removeCategory(title_);

				String title 			= element.getElementsByTagName("title").item(0).getTextContent();
				String link  			= element.getElementsByTagName("link").item(0).getTextContent();
				String date  		    = element.getElementsByTagName("pubDate").item(0).getTextContent();
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("TITLE"		, title);
				map.put("LINK"	    , link);
				map.put("CREATED_AT", date);
				map.put("ROWNUMBER"	, i + 1);
				
				rss.add(map);
				}
			}
			
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
			responseMap.put(Const.TOTAL , total);
			responseMap.put(Const.RSS   , rss);
			
		return responseMap;
	}
  	
//  	public String removeCategory(String title) {
//  		int endIdx = title.indexOf(']');
//  		
//  		if(endIdx != -1) {
//  			title = title.substring(endIdx + 1).trim();
//  		}
//  		
//  		return title;
//  	}
}