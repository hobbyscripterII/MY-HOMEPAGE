package com.project.homepage.admin;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
	private final AdminMapper adminMapper;
	
	public AdminService(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}
	
	
}