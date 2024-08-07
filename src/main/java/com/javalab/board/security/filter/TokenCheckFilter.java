package com.javalab.board.security.filter;

import com.javalab.board.security.APIUserDetailsService;
import com.javalab.board.security.exception.AccessTokenException;
import com.javalab.board.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * TokenCheckFilter 클래스
 * - JWT 토큰을 검증하는 필터로 사용자의 요청이 들어올 때마다 토큰을 검증
 * - JWTUtil, APIUserDetailsService를 주입받아 토큰을 검증하고 인증 처리
 * - 토큰이 유효하면 UserDetails 객체를 생성하고 UsernamePasswordAuthenticationToken을 생성
 * - SecurityContextHolder에 Authentication 객체를 저장
 * - 필터 체인을 통해 다음 필터로 전달
 */
@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    // JWTUtil, APIUserDetailsService 주입
    private final APIUserDetailsService apiUserDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // /api/로 시작하는 경로만 필터링, 그 외에는 필터링하지 않음
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);    // 다음 필터로 전달, 다음 필터는? DispatcherServlet
            return;
        }

        log.info("Token Check Filter..........................");
        log.info("JWTUtil: " + jwtUtil);



        try{

            Map<String, Object> payload = validateAccessToken(request);

            //mid
            String mid = (String)payload.get("mid");

            log.info("mid: " + mid);

            UserDetails userDetails = apiUserDetailsService.loadUserByUsername(mid);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());


            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request,response);
        }catch(AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }
    }

    /**
     * 토큰을 검증하는 메소드
     * - 토큰이 유효하면 토큰의 payload를 반환
     * - 토큰이 유효하지 않으면 AccessTokenException을 발생
     */
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        log.info("validateAccessToken check token method-----------------");
        String headerStr = request.getHeader("Authorization");  // 헤더에서 Authorization 키의 값을 추출

        if(headerStr == null  || headerStr.length() < 8){ // "Bearer " 문자열의 길이가 8보다 작으면 토큰이 아님
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        log.info("headerStr: " + headerStr);

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);    // "Bearer " 문자열 추출
        String tokenStr =  headerStr.substring(7);  // 토큰 문자열 추출

        log.info("tokenType: " + tokenType);
        log.info("tokenStr: " + tokenStr);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);   // 토큰 검증

            return values;
        }catch(MalformedJwtException malformedJwtException){    // 토큰이 잘못된 경우
            log.error("MalformedJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){  // 토큰의 서명이 잘못된 경우
            log.error("SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){    // 토큰이 만료된 경우
            log.error("ExpiredJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }

}
