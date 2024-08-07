package com.javalab.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 스프링부트 + 타임리프 프로젝트에서는 기본적으로
 * 정적자원들은 static 폴더 밑에서 경로를 찾고 해당 파일(CSS/JS)를 참조한다.
 * 매핑 작업을 하지 않아도 된다.
 * 반면 정적인 자원들 이외의 폴더(src와 동일한 레벨에 생성한 upload폴더)에
 * 올린 이미지들은 컨트롤러에 요청해서 받아오게 한다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Static resources such as CSS, JS, images
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");

        registry.addResourceHandler("/lib/**")
                .addResourceLocations("classpath:/static/lib/");

        registry.addResourceHandler("/scss/**")
                .addResourceLocations("classpath:/static/scss/");

    }
}
