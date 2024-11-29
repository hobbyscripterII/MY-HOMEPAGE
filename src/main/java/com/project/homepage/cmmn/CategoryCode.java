package com.project.homepage.cmmn;

import lombok.Getter;

@Getter
public enum CategoryCode {
	NOTICE    ("B001", "NOTICE"),
	STUDY     ("B002", "STUDY"),
	PHOTO     ("B003", "PHOTO"),
	MUSIC     ("B004", "MUSIC"),
	DAILY     ("B005", "DAILY"),
	DESIGN    ("B006", "DESIGN"),
	DATA      ("B007", "DATA"),
	ABOUT_ME  ("D001", "ABOUT ME"),
	PORTFOLIO ("D002", "PORTFOLIO");
	
	public final String code;
	public final String title;
	
	CategoryCode(String code, String title) {
		this.code   = code;
		this.title  = title;
	}
	
	public static String getTitle(String code) {
		for(CategoryCode c :  values()) {
			if(c.code.equals(code)) {
				return c.title;
			}
		}
		
		return null;
	}
}
