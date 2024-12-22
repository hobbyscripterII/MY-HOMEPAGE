package com.project.homepage.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
	public List<Map<String, Object>> visitLogsGet(Map<String, Object> requestMap);
}