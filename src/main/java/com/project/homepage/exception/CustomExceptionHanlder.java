package com.project.homepage.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.project.homepage.cmmn.Const;
import com.project.homepage.cmmn.ErrorCode;

@ControllerAdvice
public class CustomExceptionHanlder {
	private static ErrorCode errorCode;
	
	@ExceptionHandler({NotFoundException.class, NoResourceFoundException.class, AccessDeniedException.class, RuntimeException.class})
	private String handleException(Exception e, Model model) {
		Map<String, Object> error = new HashMap<String, Object>();
		
		if(e instanceof NotFoundException) 		  {errorCode = ErrorCode.NOT_FOUND;}
		if(e instanceof NoResourceFoundException) {errorCode = ErrorCode.NOT_FOUND;}
		if(e instanceof AccessDeniedException) 	  {errorCode = ErrorCode.FORBIDDEN;}
		if(e instanceof RuntimeException) 		  {errorCode = ErrorCode.RUNTIME;}
		
		error.put(Const.ARTICLE_TITLE , errorCode.title);
		error.put(Const.MSG			  , errorCode.msg);
		
		model.addAttribute(Const.ERROR, error);
		
		return "error/error";
	}
}
