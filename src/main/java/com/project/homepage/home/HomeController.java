package com.project.homepage.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.homepage.cmmn.Const;
import com.project.homepage.security.MyUserDetailsService;

@Controller
public class HomeController {
	private final MyUserDetailsService myUserDetailService;
	
	public HomeController(MyUserDetailsService myUserDetailsService) {
		this.myUserDetailService = myUserDetailsService;
	}
	
	@GetMapping("/")
	public String home() {
		String url  = "home";
		String role = myUserDetailService.getRole();
		
		if (role.equals(Const.ROLE_ANONYMOUS)) {
			url = "redirect:login";
		}
		
		return url;
	}
	

}