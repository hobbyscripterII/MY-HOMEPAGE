package com.project.homepage.cmmn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class WebConfig  implements WebMvcConfigurer {
	private final String prefixPath;
	
	public WebConfig(@Value("${file.path}") String prefixPath) {
		this.prefixPath = prefixPath;
	}
	
	@Bean
	MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler  ("/thumbnail/**", "/img/**")
				.addResourceLocations("file:" + prefixPath);
	}
}
