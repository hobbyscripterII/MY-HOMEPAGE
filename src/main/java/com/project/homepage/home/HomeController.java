package com.project.homepage.home;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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
	@GetMapping("/")
	public String home(@RequestParam Map<String, Object> requestMap, Model model) throws ParserConfigurationException, SAXException, IOException {
		// 날짜 및 일수 계산
		Map<String, Object> daysGet = new HashMap<String, Object>();
		LocalDateTime CURRENT_      = LocalDateTime.now();
		String CURRENT 		        = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(CURRENT_);
		LocalDate DEPOLY_           = LocalDate.of(2024, 9, 24);
		String DEPOLY               = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DEPOLY_);
		long CNT				    = ChronoUnit.DAYS.between(DEPOLY_, CURRENT_);
		
		daysGet.put("CNT"    , CNT);
		daysGet.put("CURRENT", CURRENT);
		daysGet.put("DEPOLY" , DEPOLY);
		
		// 랜덤 이미지 표출
		int num = (int) (Math.random() * 7) + 1;
		
        model.addAttribute(Const.DATE, daysGet);
        model.addAttribute(Const.NUM , num);
		
		return "home";
	}
}