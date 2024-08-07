package com.javalab.board.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * 로그인 요청을 처리하는 필터
 * - /generateToken 경로로 들어오는 로그인 요청시 사용자의 인증 정보를 검증하고, 성공적으로 인증되면
 *   APILoginSuccessHandler를 통해 JWT 생성 후 클라이언트에 응답한다.
 * - UsernamePasswordAuthenticationFilter 전에 추가된다.
 */
@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    /**
     * 로그인 요청 처리
     * - 이 메소드 호출하는 시점은 UsernamePasswordAuthenticationToken을 생성해서 인증 매니저에게 전달하는 시점
     * - Spring Security의 필터 체인에서 요청이 필터를 거칠 때 doFilter() 메소드 내부에서 이 메소드를 호출함.
     * - attemptAuthentication 메소드는 JSON 데이터를 파싱(분석)해서 mid, mpw를 추출하고
     * - UsernamePasswordAuthenticationToken을 생성해서 인증 매니저에게 전달한다.
     * - 인증 매니저는 UserDetailsService를 통해 사용자 정보를 가져와서 인증 처리(비밀번호 비교)
     * - 인증이 완료되면 Authentication 객체를 반환하고 SecurityContextHolder에
     *   Authentication 객체를 저장한다.
     *
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("APILoginFilter - attemptAuthentication 메소드----------------------------------");

        // GET 방식은 지원하지 않음, 왜? 로그인은 POST 방식으로만 처리한다.
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET 메소드는 지원하지 않음");
            return null;
        }

        log.info("-----------------------------------------");
        log.info("요청 메소드 종류 : ", request.getMethod());

        // JSON 데이터를 파싱해서 mid, mpw를 추출
        Map<String, String> jsonData = parseRequestJSON(request);

        log.info("클라이언트가 요청한 mid/mpw jsonData : " + jsonData);

        // 클라이언트가 제공한 mid, mpw로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw"));

        // 위에서 생성한 UsernamePasswordAuthenticationToken 객체를 인증 매니저에게 전달해서 인증 처리
        // 인증 처리는 UserDetailsService를 통해 사용자 정보를 가져와서 인증 처리하게 된다.
        // 인증이 성공하면 처리 결과는 Authentication 객체로 반환
        return getAuthenticationManager().authenticate(authenticationToken);
    }


    /**
     * JSON 데이터를 파싱해서 mid, mpw를 추출해주는 역할
     * @param request
     * @return
     */
    private Map<String,String> parseRequestJSON(HttpServletRequest request) {

        //JSON 데이터를 분석해서 mid, mpw 전달 값을 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);

        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
