package com.project.homepage.board;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BoardMapper {
	public int boardInsert(Map<String, Object> requestMap);
	public List<Map<String, Object>> boardGet(Map<String, Object> requestMap);
	public int boardGetCnt(Map<String, Object> requestMap);
	public Map<String, Object> boardSelect(Map<String, Object> requestMap);
	public int boardDelete(String iboard);
	public int boardUpdate(Map<String, Object> requestMap);
	public int thumbnailInsert(Map<String, Object> requestMap);
	public List<Map<String, Object>> boardGenreGet();
	public List<Map<String, Object>> prevPostGet(Map<String, Object> requestMap);
	public List<Map<String, Object>> nextPostGet(Map<String, Object> requestMap);
}
