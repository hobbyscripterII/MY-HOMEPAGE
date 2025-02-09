package com.project.homepage.cmmn;

public class Utils {
	public static boolean isNull(String str) {
		return str == "";
	}
	
	public static boolean isNull(int str) {
		return str == 0;
	}
	
	public static boolean isNotNull(String str) {
		return str != "";
	}
	
	public static boolean isNotNull(int str) {
		return str != 0;
	}
}
