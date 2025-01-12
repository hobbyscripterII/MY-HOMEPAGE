package com.project.homepage.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.homepage.cmmn.Const;
import com.project.homepage.security.MyUserDetailsService;

@Controller
public class HomeController {
	// 미사용으로 주석 처리
//	private final MyUserDetailsService myUserDetailService;
//	
//	public HomeController(MyUserDetailsService myUserDetailsService) {
//		this.myUserDetailService = myUserDetailsService;
//	}
	
//	@GetMapping("/")
//	public String index() {
//		return "index";
//	}
	
	@GetMapping("/")
	public String home() {
		String url  = "home";
		
		// 비회원일 경우 login 화면으로 redirect
//		String role = myUserDetailService.getRole();
//		
//		if (role.equals(Const.ROLE_ANONYMOUS)) {
//			url = "redirect:login";
//		}
		
		return url;
	}
}