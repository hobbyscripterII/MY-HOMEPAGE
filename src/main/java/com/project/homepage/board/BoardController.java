package com.project.homepage.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.Pagination;
import com.project.homepage.cmmn.ResponseCode;
import com.project.homepage.cmmn.util.CommonmarkUtil;
import com.project.homepage.cmmn.util.FileUploadUtil;

@Controller
@RequestMapping("/board")
public class BoardController {
	private final BoardService service;
	private final CommonmarkUtil commonmarkUtil;
	private final FileUploadUtil fileUploadUtil;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public BoardController(BoardService service, CommonmarkUtil commonmarkUtil, FileUploadUtil fileUploadUtil) {
		this.service		= service;
		this.commonmarkUtil	= commonmarkUtil;
		this.fileUploadUtil = fileUploadUtil;
	}
	
	@RequestMapping("/upload-img")
	public ModelAndView uploadImg(MultipartHttpServletRequest request) throws IOException {
		try {
			ModelAndView mv		= new ModelAndView("jsonView");
			MultipartFile image = request.getFile("upload");
			String path 		= fileUploadUtil.fileUpload(image, "/img/");
			
			mv.addObject("uploaded"	, true);
			mv.addObject("url"		, path);
			
			return mv;
		} catch(IOException e) {
			return null;
		}
	}
	
	// 리스트 목록형 & 사진 목록형 게시판
	@GetMapping("/list")
	public String mainList(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestParam Map<String, Object> requestMap, Model model) {
		Map<String, Object> map = articleTitleAndUrlGet((String) requestMap.get("code"));
		String title			= (String) map.get("title");
		String url				= (String) map.get("url");
		int amount				= (int) map.get("amount");
		Pagination pagination	= new Pagination(page, amount, service.boardGetCnt(requestMap));
		int offset 				= (page == 1 ? 0 : (page - 1) * 10);
		
		requestMap.put("offset" , offset);
		requestMap.put("amount" , amount);
		
		model.addAttribute(Const.DATA			, service.boardGet(requestMap));
		model.addAttribute(Const.ARTICLE_TITLE	, title);
		model.addAttribute(Const.PAGINATION		, pagination);
		return url;
	}
	
	@GetMapping("/write-md")
	public String write(@RequestParam Map<String, Object> requestMap, Model model) {
		List<Map<String, Object>> boardGenreGet = service.boardGenreGet();
		model.addAttribute(Const.GENRE, boardGenreGet);
		return "board/write-we";
	}
	
	@ResponseBody
	@PostMapping("/write-md")
	public Map<String, Object> writeMarkdown(@RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail, @RequestParam Map<String, Object> requestMap) throws IOException {
		return service.boardInsert(thumbnail, requestMap);
	}
	
	@GetMapping("/update-md")
	public String update(@RequestParam("iboard") String iboard, Model model) {
		Map<String, Object> requestMap 			= new HashMap<>();
		requestMap.put("iboard"			, iboard);
		
		Map<String, Object> boardSelect			= service.boardSelect(requestMap);
		List<Map<String, Object>> boardGenreGet = service.boardGenreGet();
		
		model.addAttribute(Const.DATA	, boardSelect);
		model.addAttribute(Const.GENRE	, boardGenreGet);
		return "board/write-we";
	}
	
	@GetMapping("/read-md")
	public String readMarkdown(@RequestParam Map<String, Object> requestMap, Model model) {
		Map<String, Object> boardSelect		= service.boardSelect(requestMap);
		String code 						= (String) boardSelect.get("icode");
		String iboard 					    = (String) requestMap.get("iboard");
		List<Map<String, Object>> prevPost 	= service.prevPostGet(iboard);
		List<Map<String, Object>> nextPost 	= service.nextPostGet(iboard);
		Map<String, Object> map 			= articleTitleAndUrlGet(code);
		String title						= (String) map.get("title");
		
		boardSelect.put("article_title"		, title);
		boardSelect.put("contents"			, commonmarkUtil.markdown((String) boardSelect.get("contents")));
		boardSelect.putIfAbsent("name"		, ""); // 쿼리에서 받아온 결과가 NULL인 경우 Map에 추가되지 않으므로 해당 Key 값이 없을 경우 직접 넣어준다.
		model.addAttribute(Const.PREV_POST 	, prevPost);
		model.addAttribute(Const.NEXT_POST 	, nextPost);
		model.addAttribute(Const.DATA		, boardSelect);
		
		return "board/read";
	}
	
	@ResponseBody
	@PatchMapping("/update-md")
	public Map<String, Object> updateMarkdown(@RequestBody Map<String, Object> requestMap) {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
		
		int boardUpdate 				= service.boardUpdate(requestMap);
		
		if(boardUpdate == 1) { responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code); } 
		else 				 { responseMap.put(Const.RESULT, ResponseCode.FAIL.code); }
		
		return responseMap;
	}
	
	@ResponseBody
	@PatchMapping("/delete")
	public Map<String, Object> delete(@RequestParam("iboard") String iboard) {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
		
		int boardDelete 				= service.boardDelete(iboard);
		
		if(boardDelete == 1) { responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code); } 
		else 				 { responseMap.put(Const.RESULT, ResponseCode.FAIL.code); }
		
		return responseMap;
	}
	
	private Map<String, Object> articleTitleAndUrlGet(String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String title = null;
		String url   = null;
		int amount   = 10;
		
		switch (code) {
		case "B001": title = "NOTICE"; url = "board/list";    			   break;
		case "B002": title = "STUDY"; 				          			   break;
		case "B003": title = "PHOTO";  url = "board/list-ph"; amount = 12; break;
		case "B004": title = "MUSIC";  url = "board/list-ph"; amount = 12; break;
		case "B005": title = "DAILY";  url = "board/list-ph"; amount = 12; break;
		case "B006": title = "DESIGN"; url = "board/list-ph"; amount = 12; break;
		case "B007": title = "ADMIN";  						  			   break;
		}
		
		map.put("title" , title);
		map.put("url"   , url);
		map.put("amount", amount);
		
		return map;
	}
}
