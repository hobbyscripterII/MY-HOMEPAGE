package com.project.homepage.board;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.ResponseCode;
import com.project.homepage.cmmn.util.FileUploadUtil;

@Service
public class BoardService {
	private final BoardMapper mapper;
	private final FileUploadUtil fileUploadUtil;
	
	public BoardService(BoardMapper mapper, FileUploadUtil fileUploadUtil) {
		this.mapper 	    = mapper;
		this.fileUploadUtil = fileUploadUtil;
	}
	
	@Transactional
	public Map<String, Object> boardInsert(MultipartFile thumbnail, Map<String, Object> requestMap) throws IOException {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);

		try {
			String uploadPath = "";
			String code       = (String) requestMap.get("code");
			
			if(thumbnail != null) {
				uploadPath = fileUploadUtil.fileUpload(thumbnail, "/thumbnail/");
				requestMap.put("name", uploadPath);
			}
			
			if(mapper.boardInsert(requestMap) != 1) {
				throw new SQLException();
			}
			
			if(thumbnail != null) {
				if(mapper.thumbnailInsert(requestMap) != 1) {
					throw new SQLException();
				}
			}
			
		responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
		
		} catch(IOException e) {
			responseMap.put(Const.RESULT, ResponseCode.SERVER_ERROR.code);
		} catch(SQLException e) {
			responseMap.put(Const.RESULT, ResponseCode.SQL_ERROR.code);
		}
		
		return responseMap;
	}
	
	public List<Map<String, Object>> boardGet(Map<String, Object> requestMap) {
		return mapper.boardGet(requestMap);
	}
	
	public int boardGetCnt(Map<String, Object> requestMap) {
		return mapper.boardGetCnt(requestMap);
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
	
	public List<Map<String, Object>> boardGenreGet() {
		return mapper.boardGenreGet();
	}
	
	public List<Map<String, Object>> prevPostGet(Map<String, Object> requestMap) {
		return mapper.prevPostGet(requestMap);
	}
	
	public List<Map<String, Object>> nextPostGet(Map<String, Object> requestMap) {
		return mapper.nextPostGet(requestMap);
	}
}
