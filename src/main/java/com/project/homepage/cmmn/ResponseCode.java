package com.project.homepage.cmmn;

import lombok.Getter;

@Getter
public enum ResponseCode {
	SUCCESS(1, "SUCCESS"),
	FAIL(0, "FAIL"),
	SERVER_ERROR(-5, "SERVER_ERROR"),
	SQL_ERROR(-3, "SQL_ERROR");
	
	public final int code;
	public final String msg;
	
	ResponseCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
