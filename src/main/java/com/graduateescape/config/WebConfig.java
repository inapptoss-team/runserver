package com.graduateescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false) // 테스트를 위해 credentials 비활성화
        .maxAge(3600);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 정적 리소스 서빙 설정
    registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/")
        .setCachePeriod(3600);
  }
}
