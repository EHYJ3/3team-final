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

    }

    /**
     * 스프링부트3.x 버전
     * 호출하는 쪽 : /api/track
     * 호출받는 쪽 : /api/track/
     *  - 스프링부트2 에서는 호출되었지만 스프링부트3에서는 안됨. 똑같이 맞춰야 함.
     *    아래 설정하면 스프링부트2처럼 호출되지만 deprecated 됨.
     */
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.setUseTrailingSlashMatch(true);
//    }
}
