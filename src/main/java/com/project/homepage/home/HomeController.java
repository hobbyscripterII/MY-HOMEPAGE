package com.project.homepage.home;

import java.io.IOException;
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
import com.project.homepage.cmmn.util.RSSParseUtil;

import jakarta.annotation.PostConstruct;

@Controller
public class HomeController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final HomeService service;
	private final RSSParseUtil rssReaderUtil;
	
	public HomeController(HomeService service, RSSParseUtil rssReaderUtil) {
		this.service		= service;
		this.rssReaderUtil	= rssReaderUtil;
	}
	
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) throws ParserConfigurationException, SAXException, IOException {
		List<Map<String, Object>> latestPostGet = service.latestPostGet(requestMap);				// 게시판 별 최신 글 5개씩 ...

		// RSS 관련
		Map<String, Object> responseMap = rssReaderUtil.responseMap;								// rssParseUtil 클래스 rssParse 메소드의 결과 값
		int responseCode				= (int) responseMap.get(Const.RESULT);						// responseMap에서 꺼낸 응답 코드(정상 / 에러)
		List<Map<String, Object>> rss 	= (List<Map<String, Object>>)responseMap.get(Const.RSS);	// responseMap에서 꺼낸 Tistory Blog RSS 결과물
		
		for(Map<String, Object> post : latestPostGet) {
			post.putIfAbsent("NAME", "");
		}
		
		if(responseCode == 1) {
			model.addAttribute(Const.RSS, rss);
		}
		
		model.addAttribute(Const.RESULT	, responseCode);
		model.addAttribute(Const.DATA	, latestPostGet);
		return "home";
	}
}
