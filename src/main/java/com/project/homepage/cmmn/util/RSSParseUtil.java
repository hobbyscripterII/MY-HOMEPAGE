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

import jakarta.annotation.PostConstruct;

@Component
public class RSSParseUtil {
	public Map<String, Object> responseMap	        = new HashMap<String, Object>();
	private final Logger log						= LoggerFactory.getLogger(getClass());
//	private final int fixedRate 					= 60000;	// 1분(테스트용)
//	private final int fixedRate 					= 21600000;	// 6시간(배포용)
	private final int fixedRate 					= 3600000;	// 1시간(배포용)
	private final String url;
	
	public RSSParseUtil(@Value("${rss.url}") String url) {
		this.url = url;
	}
	
  	@Scheduled(fixedRate = fixedRate)
//  	@PostConstruct
	public void rssParse() throws ParserConfigurationException, SAXException, IOException {
		try {
			responseMap.clear();
			
			List<Map<String, Object>> rss   = new ArrayList<Map<String, Object>>();
			DocumentBuilderFactory factory	= DocumentBuilderFactory.newInstance();
			DocumentBuilder builder			= factory.newDocumentBuilder();
			Document document				= builder.parse(url);
			NodeList list					= document.getElementsByTagName("item");

			for (int i = 0; i < 10; i++) {
				Node node = list.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element 		= (Element) node;
					String title_ 			= element.getElementsByTagName("title").item(0).getTextContent();
					String title		    = removeCategory(title_);
					String link 			= element.getElementsByTagName("link").item(0).getTextContent();
					String date 			= element.getElementsByTagName("pubDate").item(0).getTextContent();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title"	, title);
					map.put("link"	, link);
					map.put("date"	, date);
					rss.add(map);
				}
			}
			
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
			responseMap.put(Const.RSS	, rss);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			responseMap.put(Const.RESULT, ResponseCode.RSS_PARSE_ERROR.code);
		}
	}
  	
  	public String removeCategory(String title) {
  		int endIdx = title.indexOf(']');
  		
  		if(endIdx != -1) {
  			title = title.substring(endIdx + 1).trim();
  		}
  		
  		return title;
  	}
}