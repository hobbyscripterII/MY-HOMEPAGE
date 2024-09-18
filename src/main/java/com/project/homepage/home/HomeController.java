package com.project.homepage.home;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.homepage.cmmn.Const;

@Controller
public class HomeController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final HomeService service;
	
	public HomeController(HomeService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) {
		List<Map<String, Object>> latestPostGet = service.latestPostGet(requestMap);
		log.info("latestPostGet = {}", latestPostGet);
		
		model.addAttribute(Const.DATA, latestPostGet);
		return "home";
	}
}
