package com.osundosun.momo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

  // application.properties 파일의 설정값 저장
  @Value("${service.file.uploadurl}")
  public String UP_DIR;
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/profile/**")
    .addResourceLocations("file:" + UP_DIR + "/profile/");
  }
  
}
