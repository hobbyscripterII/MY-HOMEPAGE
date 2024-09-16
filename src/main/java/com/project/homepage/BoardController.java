package com.project.homepage;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class BoardController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/main")
	public String main(@RequestParam Map<String, Object> requestMap) {
		log.info("requestMap = {}", requestMap);
		
		return "board/main-list";
	}
	
	@GetMapping("/write")
	public String write(@RequestParam Map<String, Object> requestMap) {
		log.info("requestMap = {}", requestMap);
		
		return "board/write-md";
	}
}
