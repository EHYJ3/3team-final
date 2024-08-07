package com.javalab.board.security.handler;

import com.google.gson.Gson;
import com.javalab.board.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

/**
 * 로그인 성공 핸들러
 * - 로그인 성공시 JWT 토큰을 생성하고 클라이언트에게 전달하는 역할.
 */
@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Login Success Handler......................");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);
        log.info(authentication.getName()); //username

        // 클레임 claim : JWT payload에 저장할 데이터로, 사용자 정보나 권한 등
        // JWT의 페이로드에 저장되는 정보로, 여기서는 사용자 아이디 또는 이메일이 "mid"라는 키를 통해 저장됩니다.
        // Access, Refresh Token 모두 동일한 클레임 세트를 기반으로 생성되기 때문에, "mid" 키에 동일한 값이 포함됨.
        Map<String, Object> claim = Map.of("mid", authentication.getName());    // 아이디 또는 이메일

        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1); // 반환결과는 String 타입,

        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim, 3);

        Gson gson = new Gson();

        Map<String,String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken);

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);

    }
}
