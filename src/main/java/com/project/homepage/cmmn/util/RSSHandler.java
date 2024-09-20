package com.project.homepage.cmmn.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

public class RSSHandler extends DefaultHandler {
	private List<String> title = new ArrayList<>();
	private List<String> link = new ArrayList<>();
	private boolean isTitle = false;
	private boolean isLink = false;
	private int cnt = 0;
	
	public List<String> getTitle() {
		return title;
	}
	public void setTitle(List<String> title) {
		this.title = title;
	}
	public List<String> getLink() {
		return link;
	}
	public void setLink(List<String> link) {
		this.link = link;
	}
	public boolean isTitle() {
		return isTitle;
	}
	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
	public boolean isLink() {
		return isLink;
	}
	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
}
