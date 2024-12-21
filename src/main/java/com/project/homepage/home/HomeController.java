package com.project.homepage.home;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	private final HomeService service;
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public HomeController(HomeService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public String home(HttpServletRequest request, Model model) {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		String IP_ADDRESS  			   = request.getRemoteAddr();
		String USER_AGENT  			   = request.getHeader("User-Agent");
		String REFERER     			   = request.getHeader("Referer");
		String CURRENT_URI 			   = request.getRequestURI();
		
		requestMap.put("IP_ADDRESS" , IP_ADDRESS);
		requestMap.put("USER_AGENT" , USER_AGENT);
		requestMap.put("REFERER"    , REFERER);
		requestMap.put("CURRENT_URI", CURRENT_URI);
		
		if(!IP_ADDRESS.equals("0:0:0:0:0:0:0:1") && !USER_AGENT.contains("SM-F731N")) {
			service.visitInsert(requestMap);
		}
		
		return "home";
	}
	
	@GetMapping("/aboutme")
	public String aboutMe() {
		return "about-me";
	}
	
	@GetMapping("/portfolio")
	public String portfolio() {
		return "portfolio";
	}
	
	@GetMapping("/deploy")
	public String deploy() {
		return "deploy";
	}
}