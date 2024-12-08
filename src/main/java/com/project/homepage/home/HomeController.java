package com.project.homepage.home;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

@Controller
public class HomeController {
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) throws ParserConfigurationException, SAXException, IOException {
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