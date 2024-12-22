package com.project.homepage.home;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	private final HomeService service;
	private final String ip_home;
	private final String ip_oneroom;
	
	public HomeController(HomeService service, @Value("${ip.home}") String ip_home, @Value("${ip.oneroom}") String ip_oneroom) {
		this.service 	= service;
		this.ip_home    = ip_home;
		this.ip_oneroom = ip_oneroom;
	}
	
	@GetMapping("/")
	public String home(HttpServletRequest request, Model model) {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		String IP_ADDRESS  			   = request.getHeader("X-Forwarded-For");
		
		if(IP_ADDRESS == null) {
			String IPV6 = request.getRemoteAddr();
			IP_ADDRESS  = nullToEmpty(IPV6);
		}
		
		String USER_AGENT  = nullToEmpty(request.getHeader("User-Agent"));
		String REFERER     = nullToEmpty(request.getHeader("Referer"));
		String CURRENT_URI = nullToEmpty(request.getRequestURI());
		
		requestMap.put("IP_ADDRESS" , IP_ADDRESS);
		requestMap.put("USER_AGENT" , USER_AGENT);
		requestMap.put("REFERER"    , REFERER);
		requestMap.put("CURRENT_URI", CURRENT_URI);
		
		if(!IP_ADDRESS.equals(ip_home) && !IP_ADDRESS.equals(ip_oneroom) && !USER_AGENT.contains("SM-F731N")) {
			service.visitLogsInsert(requestMap);
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
	
//	@GetMapping("/deploy")
//	public String deploy() {
//		return "deploy";
//	}
	
	private String nullToEmpty(String str) {
		String UNKNOWN = "UNKNOWN";
		return str == null ? UNKNOWN : str;
	}
}