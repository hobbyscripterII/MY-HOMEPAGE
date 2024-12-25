package com.project.homepage.cmmn;

import lombok.Getter;

@Getter
public enum ErrorCode {
	FORBIDDEN ("403 FORBIDDEN"	   		   , "접근 권한이 없습니다. 올바른 경로로 이동해주세요."),
	NOT_FOUND ("404 NOT FOUND ERROR"	   , "존재하지 않는 URL입니다. URL을 다시 확인해주세요."),
	RUNTIME   ("500 INTERNAL SERVER ERROR" , "시스템 에러가 발생하여 페이지를 표시할 수 없습니다. 관리자에게 문의해주세요.");
	
	public final String title;
	public final String msg;
	
	ErrorCode(String title, String msg) {
		this.title = title;
		this.msg   = msg;
	}
}