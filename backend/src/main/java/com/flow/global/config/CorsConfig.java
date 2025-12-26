package com.flow.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                        "http://localhost:*", // 로컬 개발
                        "https://*.vercel.app" // 모든 Vercel 도메인
                )
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
