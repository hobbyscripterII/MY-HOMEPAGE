package com.project.homepage.home;

import org.springframework.stereotype.Service;

@Service
public class HomeService {
	private final HomeMapper mapper;
	
	public HomeService(HomeMapper mapper) {
		this.mapper = mapper;
	}
}