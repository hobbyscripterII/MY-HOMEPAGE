package com.project.homepage.board;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.ResponseCode;

@Controller
@RequestMapping("/board")
public class BoardController {
	private final BoardService service;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public BoardController(BoardService service) {
		this.service = service;
	}
	
	// 목록형 게시판
	@GetMapping("/list")
	public String mainList(@RequestParam Map<String, Object> requestMap, Model model) {
		model.addAttribute(Const.DATA, service.boardGet(requestMap));
		return "board/list";
	}
	
	// 사진형 게시판
	@GetMapping("/photo")
	public String mainPhoto() {
		return null;
	}
	
	@GetMapping("/write-md")
	public String write(@RequestParam Map<String, Object> requestMap) {
		log.info("requestMap = {}", requestMap);
		
		return "board/write-md";
	}
	
	@ResponseBody
	@PostMapping("/write-md")
	public Map<String, Object> writeMarkdown(@RequestBody Map<String, Object> requestMap) {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
//		String icode = (String)requestMap.get("icode");
//		String title = (String) requestMap.get("title");
//		String contents = (String)requestMap.get("contents");
		
		int boardInsert = service.boardInsert(requestMap);
		
		if(boardInsert == 1) {
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
		} else {
			responseMap.put(Const.RESULT, ResponseCode.FAIL.code);
		}
		
		return responseMap;
	}
	
	@GetMapping("/update-md")
	public String update(@RequestParam("iboard") String iboard, Model model) {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("iboard", iboard);
		Map<String, Object> boardSelect = service.boardSelect(requestMap);
		model.addAttribute(Const.DATA, boardSelect);
		return "board/write-md";
	}
	
	@GetMapping("/read-md")
	public String readMarkdown(@RequestParam Map<String, Object> requestMap, Model model) {
		Map<String, Object> boardSelect = service.boardSelect(requestMap);
		String code = (String) boardSelect.get("icode");
		boardSelect.put("article_title", articleTitleGet(code));
		
		model.addAttribute(Const.DATA, boardSelect);
		return "board/read-md";
	}
	
	@ResponseBody
	@PatchMapping("/update-md")
	public Map<String, Object> updateMarkdown(@RequestBody Map<String, Object> requestMap) {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
		
		int boardUpdate = service.boardUpdate(requestMap);
		
		if(boardUpdate == 1) {
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
		} else {
			responseMap.put(Const.RESULT, ResponseCode.FAIL.code);
		}
		
		return responseMap;
	}
	
	@ResponseBody
	@PatchMapping("/delete")
	public Map<String, Object> delete(@RequestParam("iboard") String iboard) {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
		
		int boardDelete = service.boardDelete(iboard);
		log.info("boardDelete = {}", boardDelete);
		
		if(boardDelete == 1) {
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
		} else {
			responseMap.put(Const.RESULT, ResponseCode.FAIL.code);
		}
		
		return responseMap;
	}
	
	private String articleTitleGet(String code) {
		String title = null;
		
		switch (code) {
		case "B001": title = "NOTICE"; break;
		}
		return title;
	}
}
