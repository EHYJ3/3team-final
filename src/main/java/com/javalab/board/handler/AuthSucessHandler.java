package com.javalab.board.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인 성공후 다음 처리를 담당하는 클래스
 *  - SimpleUrlAuthenticationSuccessHandler는 AuthenticationSuccessHandler 구현체임
 *  - @Component 붙어서 앱 구동시 빈으로 등록되어서 다른 객체에서 의존성 주입가능
 *  - 관리자이면 관리자 메인화면으로 이동.
 *  - 일반 사용자이면 쇼핑몰 메인화면으로 이동.
 */
@RequiredArgsConstructor
@Component
@Log4j2
public class AuthSucessHandler extends SimpleUrlAuthenticationSuccessHandler {
	

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {

        log.info("AuthSucessHandler 권한 확인 authentication :" + authentication);

        // 관리자면 관리자 메인화면으로 이동
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            setDefaultTargetUrl("/admin/main"); // ADMIN 사용자의 경우
        } else {    // 일반 USER의 경우 쇼핑몰 메인화면으로 이동
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
