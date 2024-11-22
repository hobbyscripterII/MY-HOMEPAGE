package com.project.homepage.home;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class HomeService {
	private final HomeMapper mapper;
	
	public HomeService(HomeMapper mapper) {
		this.mapper = mapper;
	}
	
	public List<Map<String, Object>> latestPostGet(Map<String, Object> requestMap) {
		return mapper.latestPostGet(requestMap);
	}
	
	public Map<String, Object> daysGet() {
		return mapper.daysGet();
	}
}