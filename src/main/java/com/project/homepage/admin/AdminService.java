package com.project.homepage.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
	private final AdminMapper adminMapper;
	
	public AdminService(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}
	
	public List<Map<String, Object>> visitLogsGet(Map<String, Object> requestMap) {
		return adminMapper.visitLogsGet(requestMap);
	}
}