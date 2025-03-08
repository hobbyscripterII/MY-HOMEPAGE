package com.project.homepage.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.project.homepage.cmmn.CategoryCode;
import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.Pagination;
import com.project.homepage.cmmn.ResponseCode;
import com.project.homepage.cmmn.Utils;
import com.project.homepage.cmmn.util.CommonmarkUtil;
import com.project.homepage.cmmn.util.FileUploadUtil;
import com.project.homepage.cmmn.util.RSSParseUtil;
import com.project.homepage.security.MyUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
public class BoardController {
	private final BoardService service;
	private final MyUserDetailsService myUserDetailsService;
	private final CommonmarkUtil commonmarkUtil;
	private final FileUploadUtil fileUploadUtil;
	private final RSSParseUtil rssParseUtil;
	
	Logger log = LoggerFactory.getLogger(getClass());

	public BoardController(BoardService service, MyUserDetailsService myUserDetailsService, CommonmarkUtil commonmarkUtil, FileUploadUtil fileUploadUtil, RSSParseUtil rssParseUtil) {
		this.service = service;
		this.myUserDetailsService = myUserDetailsService;
		this.commonmarkUtil = commonmarkUtil;
		this.fileUploadUtil = fileUploadUtil;
		this.rssParseUtil = rssParseUtil;
	}

	@RequestMapping("/upload-img")
	public ModelAndView uploadImg(MultipartHttpServletRequest request) throws IOException {
		try {
			ModelAndView mv = new ModelAndView("jsonView");
			MultipartFile image = request.getFile("upload");
			String path = fileUploadUtil.fileUpload(image, "/img/");

			mv.addObject("uploaded", true);
			mv.addObject("url", path);

			return mv;
		} catch (IOException e) {
			return null;
		}
	}

	@GetMapping("/list")
	public String main(@RequestParam(name = "page", required = false, defaultValue = "1") int page, @RequestParam Map<String, Object> requestMap, Model model, HttpServletResponse response) throws IOException, NotFoundException {
		List<Map<String, Object>> boardGet = null;
		String code = (String) requestMap.get(Const.CODE);
		String search = (String) requestMap.get(Const.SEARCH) == null ? "" : (String) requestMap.get("search");
		String title = CategoryCode.getTitle(code);
		String url = "board/list";
		String role = myUserDetailsService.getRole();
		int amount = code.equals(CategoryCode.REVIEW.getCode()) ? 5 : Const.AMOUNT;
		int offset = getOffset(page, amount);
		int boardGetCnt = 0;

		requestMap.put("offset", offset);
		requestMap.put("amount", amount);
		requestMap.put("search", search);
		requestMap.put("role", role);

		if (title == null || Utils.isNull(title)) {
			throw new NotFoundException();
		}
		
		if (code.equals(CategoryCode.DAILY.code) && role.equals(Const.ROLE_ANONYMOUS)) {
			throw new AccessDeniedException(null);
		}

		if (code.equals(CategoryCode.STUDY.code)) {
			Map<String, Object> rssGet = rssParseUtil.rssGet(page, amount);
			Integer result = (Integer) rssGet.get(Const.RESULT);
			boardGetCnt = (int) rssGet.get(Const.TOTAL);
			boardGet = (List<Map<String, Object>>) rssGet.get(Const.RSS);

			model.addAttribute(Const.RESULT, result);
		} else {
			boardGet = service.boardGet(requestMap);
			boardGetCnt = service.boardGetCnt(requestMap);
		}

		if (code.equals(CategoryCode.REVIEW.code)) {
			url = "board/list2";
			
			for(int i = 0; i < boardGet.size(); i++) {
				Map<String, Object> post = boardGet.get(i);
				
				String contents = (String) post.get("CONTENTS");
				
				if(contents != null) {
					String removeHtmlTagContents = contents.replaceAll("<[^>]*>", "");
					post.put("CONTENTS", removeHtmlTagContents);
				}
			}
		}
		
		Pagination pagination = new Pagination(page, amount, boardGetCnt);
		int idx = boardGet.size();

		if (!search.isEmpty()) {
			for (int i = 0; i < idx; i++) {
				Map<String, Object> post = boardGet.get(i);
				String contents_ = (String) post.get(Const.CONTENTS);
				String contents = Jsoup.parse(contents_).text();
				int startIdx = contents.indexOf(search);
				int endIdx = Math.min((startIdx + 100), contents.length());
				String previewContents = contents.substring(startIdx, endIdx);

				post.put("PREVIEW_CONTENTS", previewContents);
			}
		}

		model.addAttribute(Const.DATA, boardGet);
		model.addAttribute(Const.ARTICLE_TITLE, title);
		model.addAttribute(Const.PAGINATION, pagination);
		model.addAttribute(Const.SEARCH_DATA, requestMap);

		return url;
	}

	@GetMapping("/write")
	public String write(@RequestParam Map<String, Object> requestMap, Model model) {
		List<Map<String, Object>> boardGenreGet = service.boardGenreGet();

		model.addAttribute(Const.GENRE, boardGenreGet);

		return "board/write";
	}

	@ResponseBody
	@PostMapping("/write")
	public Map<String, Object> write(@RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail, @RequestParam Map<String, Object> requestMap) throws IOException {
		return service.boardInsert(thumbnail, requestMap);
	}

	@GetMapping("/read/{iboard}")
	public String read(@PathVariable(name = "iboard") String iboard, @RequestParam Map<String, Object> requestMap, Model model, HttpServletResponse response) throws IOException, NotFoundException, AccessDeniedException {
		requestMap.put("iboard", iboard);

		Map<String, Object> boardSelect = service.boardSelect(requestMap);

		if (boardSelect == null) {
			throw new NotFoundException();
		}

		String code = (String) boardSelect.get("icode");
		String secYn = (String) boardSelect.get("sec_yn");
		String role = myUserDetailsService.getRole();

		requestMap.put("role", role);

		if (secYn.equals("Y") && role.equals(Const.ROLE_ANONYMOUS)) {
			throw new AccessDeniedException(null);
		}

		List<Map<String, Object>> prevPost = service.prevPostGet(requestMap);
		List<Map<String, Object>> nextPost = service.nextPostGet(requestMap);

		boardSelect.put(Const.ARTICLE_TITLE, CategoryCode.getTitle(code));
		boardSelect.put(Const.CONTENTS, commonmarkUtil.markdown((String) boardSelect.get("contents")));

		model.addAttribute(Const.PREV_POST, prevPost);
		model.addAttribute(Const.NEXT_POST, nextPost);
		model.addAttribute(Const.DATA, boardSelect);

		return "board/read";
	}

	@GetMapping("/update/{iboard}")
	public String update(@PathVariable(name = "iboard") String iboard, Model model) {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("iboard", iboard);

		Map<String, Object> boardSelect = service.boardSelect(requestMap);
		List<Map<String, Object>> boardGenreGet = service.boardGenreGet();

		model.addAttribute(Const.DATA, boardSelect);
		model.addAttribute(Const.GENRE, boardGenreGet);

		return "board/write";
	}

	@ResponseBody
	@PatchMapping("/update")
	public Map<String, Object> updateMarkdown(@RequestBody Map<String, Object> requestMap) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);

		int boardUpdate = service.boardUpdate(requestMap);

		if (boardUpdate == 1) {
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
		} else {
			responseMap.put(Const.RESULT, ResponseCode.FAIL.code);
		}

		return responseMap;
	}

	@ResponseBody
	@PatchMapping("/delete")
	public Map<String, Object> delete(@RequestParam("iboard") String iboard) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put(ResponseCode.SUCCESS.msg, ResponseCode.SUCCESS.code);

		int boardDelete = service.boardDelete(iboard);

		if (boardDelete == 1) {
			responseMap.put(Const.RESULT, ResponseCode.SUCCESS.code);
		} else {
			responseMap.put(Const.RESULT, ResponseCode.FAIL.code);
		}

		return responseMap;
	}
	
	@ResponseBody
	@GetMapping("/rss")
	public Map<String, Object> rss() {
		int page = 1;
		int amount = 30;
		Map<String, Object> rssGet = rssParseUtil.rssGet(page, amount);
		
		return rssGet;
	}
	
	private int getOffset(int page, int amount) {
		return (page == 1 ? 0 : (page - 1) * amount);
	}
}
