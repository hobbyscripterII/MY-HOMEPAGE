package com.project.homepage.home;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.util.RSSReaderUtil;

@Controller
public class HomeController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final HomeService service;
	private final RSSReaderUtil rssReaderUtil;
	
	public HomeController(HomeService service, RSSReaderUtil rssReaderUtil) {
		this.service = service;
		this.rssReaderUtil = rssReaderUtil;
	}
	
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) throws ParserConfigurationException, SAXException, IOException {
		List<Map<String, Object>> latestPostGet = service.latestPostGet(requestMap);
		rssReaderUtil.rssReader();
		
		for(Map<String, Object> post : latestPostGet) {
			post.putIfAbsent("NAME", "");
		}
		
		model.addAttribute(Const.DATA, latestPostGet);
		return "home";
	}
}
