package com.project.homepage.cmmn.util;

import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Component;

@Component
public class CommonmarkUtil {
	public String markdown(String md) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(md);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		return renderer.render(document);
	}
}