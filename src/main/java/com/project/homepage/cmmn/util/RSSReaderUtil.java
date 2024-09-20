package com.project.homepage.cmmn.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class RSSReaderUtil {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final String url;
	
	public RSSReaderUtil(@Value("${rss.url}") String url) {
		this.url = url;
	}
	
	public void rssReader() throws ParserConfigurationException, SAXException, IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(url);
			NodeList list = document.getElementsByTagName("item");
			
			for(int i = 0; i < 5; i++) {
				Node node = list.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String title = element.getElementsByTagName("title").item(0).getTextContent();
					String link = element.getElementsByTagName("link").item(0).getTextContent();
					log.info("title = {}", title);
					log.info("link = {}", link);
				}
			}
			
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}