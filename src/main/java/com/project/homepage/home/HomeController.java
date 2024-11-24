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
		// 날짜 및 일수 계산
		Map<String, Object> daysGet = new HashMap<String, Object>();
		String CURRENT 		        = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
		String DEPOLY               = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.of(2024, 9, 24));
		long CNT				    = ChronoUnit.DAYS.between(LocalDate.of(2024, 9, 24), LocalDateTime.now());
		
		daysGet.put("CURRENT", CURRENT);
		daysGet.put("DEPOLY" , DEPOLY);
		daysGet.put("CNT"    , CNT);
		
        model.addAttribute(Const.DATE, daysGet);
		
		return "home";
	}
}