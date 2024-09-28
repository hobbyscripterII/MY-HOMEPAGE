package com.project.homepage.cmmn.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadUtil {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final String prefixPath;
	
	public FileUploadUtil(@Value("${file.path}") String prefixPath) {
		this.prefixPath = prefixPath;
	}
	
	public String fileUpload(MultipartFile file, String suffixPath) throws IOException {
		String fileName = uuidGet(file);
		Path path		= Paths.get(prefixPath);
		
		// 해당 경로에 폴더가 없을 경우 생성한다.
		if(!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch(IOException e) {
				throw new IOException();
			}
		}
		
		/*
		 * savePath		- LOCAL에 저장되는 실제 경로
		 * uploadPath	- WebConfig를 통해 연결되는 외부 리소스 경로
		 */
		String savePath   = Paths.get(prefixPath + fileName).toString();
		String uploadPath = suffixPath + fileName;
		File saveFile 	  = new File(savePath);
		
		try {
			file.transferTo(saveFile);
		} catch(IOException e) {
			// ...
		}
		
		return uploadPath;
	}
	
	public String uuidGet(MultipartFile file) {
		String originalName = file.getOriginalFilename();
		String ext 			= originalName.substring(originalName.lastIndexOf("."));
		return UUID.randomUUID() + ext;
	}
}
