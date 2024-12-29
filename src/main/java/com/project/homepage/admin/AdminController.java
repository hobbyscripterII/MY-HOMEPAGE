package com.project.homepage.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final AdminService service;
	
	public AdminController(AdminService service) {
		this.service = service;
	}
}
