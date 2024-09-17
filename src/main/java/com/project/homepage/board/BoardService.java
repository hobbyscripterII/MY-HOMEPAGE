package com.project.homepage.board;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
	private final BoardMapper mapper;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public BoardService(BoardMapper mapper) {
		this.mapper = mapper;
	}
	
	public int boardInsert(Map<String, Object> requestMap) {
		return mapper.boardInsert(requestMap);
	}
	
	public List<Map<String, Object>> boardGet(Map<String, Object> requestMap) {
		return mapper.boardGet(requestMap);
	}
	
	public Map<String, Object> boardSelect(Map<String, Object> requestMap) {
		return mapper.boardSelect(requestMap);
	}
	
	public int boardDelete(String iboard) {
		return mapper.boardDelete(iboard);
	}
	
	public int boardUpdate(Map<String, Object> requestMap) {
		return mapper.boardUpdate(requestMap);
	}
}
