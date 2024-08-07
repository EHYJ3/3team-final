package com.javalab.board.security.filter;


import com.google.gson.Gson;
import com.javalab.board.security.exception.RefreshTokenException;
import com.javalab.board.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter  extends OncePerRequestFilter {

    // 생성자 의존성 주입
    // SecurityConfig에서 설정한 refreshPath가 주입된다.
    private final String refreshPath;
    // SecurityConfig에서 설정한 JWTUtil이 주입된다.
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.equals(refreshPath)) {
            log.info("skip refresh token filter.....");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Refresh Token Filter...run..............1");

        //전송된 JSON에서 accessToken과 refreshToken을 얻어온다.
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken"); // Access Token 추출
        String refreshToken = tokens.get("refreshToken");   // Refresh Token 추출

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        try{
            checkAccessToken(accessToken);
        }catch(RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        Map<String, Object> refreshClaims = null; // Refresh Token의 클레임 정보(페이로드) 저장할 변수

        try {

            refreshClaims = checkRefreshToken(refreshToken);
            log.info("리프레시토큰 클레임 "  + refreshClaims);

        }catch(RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        //Refresh Token의 유효시간이 얼마 남지 않은 경우
        //Integer exp = (Integer)refreshClaims.get("exp"); // 만료 시간 조회
        // exp 값을 Long으로 변환
        Long exp = ((Number) refreshClaims.get("exp")).longValue(); // 만료 시간 조회


        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);   // 만료 시간을 Date 객체로 변환

        Date current = new Date(System.currentTimeMillis());    // 현재 시간을 Date 객체로 변환

        //만료 시간과 현재 시간의 간격 계산
        //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
        long gapTime = (expTime.getTime() - current.getTime());

        log.info("-----------------------------------------");
        log.info("current: " + current);
        log.info("expTime: " + expTime);
        log.info("gap: " + gapTime );

        String mid = (String)refreshClaims.get("mid");  // mid 추출

        //이상태까지 오면 무조건 AccessToken은 새로 생성
        String accessTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 1);

        String refreshTokenValue = tokens.get("refreshToken");

        //RefrshToken이 3일도 안남았다면..
        if(gapTime < (1000 * 60  * 3  ) ){
        //if(gapTime < (1000 * 60 * 60 * 24 * 3  ) ){
            log.info("new Refresh Token required...  ");
            refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 3);  // Refresh Token 재생성
        }

        log.info("Refresh Token result....................");
        log.info("accessToken: " + accessTokenValue);
        log.info("refreshToken: " + refreshTokenValue);

        // 클라이언트에게 새로운 AccessToken과 RefreshToken을 전달
        sendTokens(accessTokenValue, refreshTokenValue, response);

    }

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

    /**
     * Access Token의 유효성 검사
     * @param accessToken
     * @throws RefreshTokenException
     */
    private void checkAccessToken(String accessToken)throws RefreshTokenException {

        try{
            jwtUtil.validateToken(accessToken);
        }catch (ExpiredJwtException expiredJwtException){
            log.info("Access Token has expired");
        }catch(Exception exception){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    /**
     * Refresh Token의 유효성 검사
     * - 만료된 경우, 오류 발생
     * - 그 외의 경우, 클레임 정보 반환
     */
    private Map<String, Object> checkRefreshToken(String refreshToken)throws RefreshTokenException{

        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken); // Refresh Token의 클레임 정보 반환됨
            return values;  // 클레임 정보 반환
        }catch(ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch(Exception exception){
            exception.printStackTrace();
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    /**
     * 클라이언트에게 새로운 AccessToken과 RefreshToken을 전달
     * @param accessTokenValue
     * @param refreshTokenValue
     * @param response
     */
    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        // Gson 객체를 사용하여 Map을 JSON 형식으로 직렬화.
        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,
                "refreshToken", refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
