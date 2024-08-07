package com.javalab.board.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CustomServletConfig
 * - 웹 애플리케이션에서 요청되는 URL에 따른 처리를 세밀하게 제어하는 역할을 수행합니다.
 *   이는 주로 Spring MVC에서 제공하는 기능을 커스터마이징하고, 정적 리소스 처리,
 *   CORS(Cross-Origin Resource Sharing) 설정, 인터셉터 등록, 리소스 핸들링 등 다양한
 *   기능을 제어하는 데 사용됩니다. CustomServletConfig는 웹 레이어의 설정을 다룹니다.
 *   정적 리소스 관리, 인터셉터, CORS 설정, 뷰 매핑 등을 포함하는 MVC 관련 설정을 처리하는 데 적합합니다.
 *   @EnableWebMvc
 *   - 웹 애플리케이션에서 요청되는 URL에 따른 처리를 세밀하게 제어하는 역할을 수행합니다.
 *   - 스웨거 설정을 위해 필요합니다.
 *   - CORS 설정을 위해 필요합니다.
 *   - 정적 리소스 관리를 위해 필요합니다.
 */
@Configuration
@EnableWebMvc // 역할은? 웹 애플리케이션에서 요청되는 URL에 따른 처리를 세밀하게 제어하는 역할을 수행합니다. 스웨거 설정을 위해 필요합니다., CORS 설정을 위해 필요합니다., 정적 리소스 관리를 위해 필요합니다.
@Log4j2
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        // /swagger-ui/**로 시작하는 URL 요청은 서버의 /META-INF/resources/webjars/swagger-ui/ 디렉토리에서 파일을 찾습니다.
        // 이 경로는 Swagger UI의 HTML, CSS, JavaScript 파일들을 포함하고 있습니다.
        // Spring은 이 리소스들을 classpath:/META-INF/resources/webjars/swagger-ui/ 경로에서 찾아 제공하게 됩니다.
        // 이로 인해 사용자가 브라우저에서 Swagger UI에 접근할 수 있게 됩니다.
        registry.addResourceHandler("/swagger-ui/**")   // 어떤 URL 패턴이 정적 리소스로 처리되어야 하는지를 정의합니다.
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .resourceChain(false); // 주로 디버깅이나 개발 중에 파일 변경 사항을 바로 반영하기 위해 사용됩니다.   쉽게 말해서, 리소스 체인은 정적 파일을 더 빠르고 효율적으로 제공하려는 "비서" 같은 역할을 하고, resourceChain(false)는 그 비서를 쉬게 하고 직접 파일을 제공하는 방식입니다.
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                // .allowedOrigins("http://localhost:9000") // 특정 도메인에 대해서만 허용하는 경우 사용
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
                .allowCredentials(true);
    }

}
