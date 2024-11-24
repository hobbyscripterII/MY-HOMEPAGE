package com.project.homepage.home;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HomeMapper {
	public List<Map<String, Object>> latestPostGet(Map<String, Object> requestMap);
}