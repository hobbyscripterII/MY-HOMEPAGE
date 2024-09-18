package com.project.homepage.cmmn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUploadUtil {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final String prefixPath;
	
	public FileUploadUtil(@Value("${file.path}") String prefixPath) {
		this.prefixPath = prefixPath;
		log.info("prefixPath = {}", prefixPath);
	}
}
