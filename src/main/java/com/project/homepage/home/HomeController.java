package com.project.homepage.home;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.project.homepage.cmmn.Const;

@Controller
public class HomeController {
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) throws ParserConfigurationException, SAXException, IOException {
		return "home";
	}
}