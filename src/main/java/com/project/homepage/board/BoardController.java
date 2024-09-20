package com.project.homepage.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.ResponseCode;
import com.project.homepage.cmmn.util.CommonmarkUtil;

@Controller
@RequestMapping("/board")
public class BoardController {
	private final BoardService service;
	private final CommonmarkUtil commonmarkUtil;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public BoardController(BoardService service, CommonmarkUtil commonmarkUtil) {
		this.service = service;
		this.commonmarkUtil = commonmarkUtil;
	}
	
	// 목록형 게시판
	@GetMapping("/list")
	public String mainList(@RequestParam Map<String, Object> requestMap, Model model) {
		String code = (String) requestMap.get("code");
		
		model.addAttribute(Const.DATA, service.boardGet(requestMap));
		model.addAttribute(Const.ARTICLE_TITLE, articleTitleGet(code));
		return "board/list";
	}
	
	// 사진형 게시판
	@GetMapping("/photo")
	public String mainPhoto() {
		return null;
	}
	
	@GetMapping("/write-md")
	public String write(@RequestParam Map<String, Object> requestMap) {
		
		return "board/write-md";
	}
	
	@ResponseBody
	@PostMapping("/write-md")
	public Map<String, Object> writeMarkdown(@RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail, @RequestParam Map<String, Object> requestMap) throws IOException {
		return service.boardInsert(thumbnail, requestMap);
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
		// 쿼리에서 받아온 결과가 NULL인 경우 Map에 추가되지 않으므로 해당 Key 값이 없을 경우 직접 넣어준다.
		boardSelect.putIfAbsent("NAME", "");
		String code = (String) boardSelect.get("icode");
		boardSelect.put("article_title", articleTitleGet(code));
		boardSelect.put("contents", commonmarkUtil.markdown((String) boardSelect.get("contents")));
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
		case "B002": title = "STUDY"; break;
		case "B003": title = "PHOTO"; break;
		case "B004": title = "MUSIC"; break;
		case "B005": title = "DAILY"; break;
		}
		return title;
	}
}
