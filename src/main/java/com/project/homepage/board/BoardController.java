package com.project.homepage.board;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.project.homepage.security.MyUserDetails;

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
	
	// 권한 가져오기
	public String getRole() {
		Authentication authentication 	   				   = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) authentication.getAuthorities();
		String role 									   = null;
		
		if(authorities != null) {
			role = authorities.stream()
				   .findFirst()
				   .map(GrantedAuthority :: getAuthority)
				   .orElse(null);
		}
		
		return role;
	}
	
	// 리스트 목록형 & 사진 목록형 게시판(미사용)
	@GetMapping("/list")
	public String main(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestParam Map<String, Object> requestMap, Model model) {
		String code			  		  	   = (String) requestMap.get("code");
		String title		  		  	   = getTitle(code);
		String url			 		  	   = "board/list";
		int amount            		  	   = 10;
		int offset			  		  	   = (page == 1 ? 0 : (page - 1) * amount);
		
		requestMap.put("offset" , offset);
		requestMap.put("amount" , amount);
		requestMap.put("role"   , getRole());
		
		List<Map<String, Object>> boardGet = service.boardGet(requestMap);
		int boardGetCnt 			  	   = service.boardGetCnt(requestMap);
		Pagination pagination 		  	   = new Pagination(page, amount, boardGetCnt);

		model.addAttribute(Const.DATA			, boardGet);
		model.addAttribute(Const.ARTICLE_TITLE	, title);
		model.addAttribute(Const.PAGINATION		, pagination);
		model.addAttribute(Const.SEARCH_DATA    , requestMap);
		
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
	public Map<String, Object> write(@RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail, @RequestParam Map<String, Object> requestMap) throws IOException {
		return service.boardInsert(thumbnail, requestMap);
	}
	
	@GetMapping("/update-md")
	public String update(@RequestParam("iboard") String iboard, Model model) {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("iboard" , iboard);
		
		Map<String, Object> boardSelect			= service.boardSelect(requestMap);
		List<Map<String, Object>> boardGenreGet = service.boardGenreGet();
		
		model.addAttribute(Const.DATA  , boardSelect);
		model.addAttribute(Const.GENRE , boardGenreGet);
		return "board/write-we";
	}
	
	@GetMapping("/read-md")
	public String readMarkdown(@RequestParam Map<String, Object> requestMap, Model model) {
		try {
			Map<String, Object> boardSelect		= service.boardSelect(requestMap);
			String code 						= (String) boardSelect.get("icode");
			String secYn 					    = (String) boardSelect.get("sec_yn");
			String role							= getRole();
			
			requestMap.put("role", role);
			
			log.info("requestMap = {}", requestMap);
			
			if(secYn.equals("Y") && role.equals(Const.ROLE_ANONYMOUS)) {
				throw new AccessDeniedException("권한이 없습니다.");
			}
			
			List<Map<String, Object>> prevPost 	= service.prevPostGet(requestMap);
			List<Map<String, Object>> nextPost 	= service.nextPostGet(requestMap);
			
			log.info("prevPost = {}", prevPost);
			log.info("nextPost = {}", nextPost);
			
			String title						= getTitle(code);
			
			boardSelect.put("article_title"		, title);
			boardSelect.put("contents"			, commonmarkUtil.markdown((String) boardSelect.get("contents")));
			model.addAttribute(Const.PREV_POST 	, prevPost);
			model.addAttribute(Const.NEXT_POST 	, nextPost);
			model.addAttribute(Const.DATA		, boardSelect);
			
			return "board/read";
		} catch(AccessDeniedException e) {
			return "access-denied";
		}
	}
	
	@ResponseBody
	@PatchMapping("/update-md")
	public Map<String, Object> updateMarkdown(@RequestBody Map<String, Object> requestMap) {
		Map<String, Object> responseMap = new HashMap();
		
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
		
		int boardUpdate = service.boardUpdate(requestMap);
		
		if(boardUpdate == 1) { responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code); } 
		else 				 { responseMap.put(Const.RESULT, ResponseCode.FAIL.code); }
		
		return responseMap;
	}
	
	@ResponseBody
	@PatchMapping("/delete")
	public Map<String, Object> delete(@RequestParam("iboard") String iboard) {
		Map<String, Object> responseMap = new HashMap();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);
		
		int boardDelete = service.boardDelete(iboard);
		
		if(boardDelete == 1) { responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code); } 
		else 				 { responseMap.put(Const.RESULT, ResponseCode.FAIL.code); }
		
		return responseMap;
	}
	
	private String getTitle(String code) {
		String title = null;
		
		switch (code) {
		case "B001": title = "NOTICE"; break;
		case "B003": title = "PHOTO";  break;
		case "B004": title = "MUSIC";  break;
		case "B005": title = "DAILY";  break;
		case "B006": title = "DESIGN"; break;
		case "B007": title = "ADMIN";  break;
		}
		
		return title;
	}
}
