package com.project.homepage.home;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.util.RSSParseUtil;

@Controller
public class HomeController {
	private final HomeService service;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public HomeController(HomeService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) throws ParserConfigurationException, SAXException, IOException {
		Map<String, Object> daysGet = service.daysGet();
		int num 				    = (int) (Math.random() * 7) + 1;
		
        model.addAttribute(Const.DATE, daysGet);
        model.addAttribute(Const.NUM , num);
		
		return "home";
	}
}