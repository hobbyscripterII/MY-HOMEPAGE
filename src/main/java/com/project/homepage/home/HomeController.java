package com.project.homepage.home;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.homepage.cmmn.Const;

@Controller
public class HomeController {
	@GetMapping("/")
	public String home() {
		String url = "home";
		
		if (getRole().equals(Const.ROLE_ANONYMOUS)) {
			url = "redirect:login";
		}
		
		return url;
	}
	
	public String getRole() {
		Authentication authentication 	   				   = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) authentication.getAuthorities();
		
		return authorities.stream()
						  .findFirst()
						  .map(GrantedAuthority :: getAuthority)
						  .orElse(null);
	}
}