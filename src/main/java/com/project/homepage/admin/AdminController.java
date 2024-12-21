package com.project.homepage.admin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.homepage.cmmn.CategoryCode;
import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.Pagination;
import com.project.homepage.cmmn.Utils;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final AdminService service;
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public AdminController(AdminService service) {
		this.service = service;
	}
	
	@GetMapping("/visit")
	public String main(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestParam Map<String, Object> requestMap, Model model) throws NotFoundException {
		String title = "VISIT";
		int amount   = 50;
		int offset   = getOffset(page, amount);
		
		requestMap.put("offset", offset);
		requestMap.put("amount", amount);
		
		if (getRole().equals(Const.ROLE_ANONYMOUS)) {
			throw new AccessDeniedException("접근이 거부되었습니다.");
		}
		
		List<Map<String, Object>> visitGet = service.visitGet(requestMap);
		Pagination pagination 			   = new Pagination(page, amount, 0);
		int idx 			  			   = visitGet.size();
		
		model.addAttribute(Const.DATA		  , visitGet);
		model.addAttribute(Const.ARTICLE_TITLE, title);
		model.addAttribute(Const.PAGINATION	  , pagination);
		
		return "admin/visit";
	}
	
	// 권한 가져오기
	public String getRole() {
		Authentication authentication 	   				   = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) authentication.getAuthorities();
		
		return authorities.stream()
						  .findFirst()
						  .map(GrantedAuthority :: getAuthority)
						  .orElse(null);
	}
	
	private int getOffset(int page, int amount) {
		return (page == 1 ? 0 : (page - 1) * amount);
	}
}
