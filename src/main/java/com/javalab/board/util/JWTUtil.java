package com.javalab.board.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 토큰 생성 및 검증을 위한 유틸리티 클래스
 * - JWTUtil 클래스는 JWT 토큰을 생성하고 검증하는 기능을 제공
 * - JWTUtil 클래스는 JWT 토큰을 생성할 때 사용할 비밀 키를 생성하고, 생성된 비밀 키를 사용하여 JWT 토큰을 생성
 * - JWT 토큰을 검증할 때는 생성할 때 사용한 비밀 키를 사용하여 검증
 * - JWT 토큰은 헤더, 페이로드, 서명 세 부분으로 구성
 * - 헤더는 토큰의 타입과 해싱 알고리즘을 지정
 * - 페이로드는 클레임(claim) 정보를 포함, 클레임은? 토큰에 담을 정보, 예를들면 사용자 ID, 권한 등
 * - 서명은 헤더와 페이로드를 합친 후 비밀 키를 사용해 해싱한 값
 * - JWT 토큰은 Base64로 인코딩되어 있어서 눈으로 확인 가능
 * - JWT 토큰은 클라이언트에서 서버로 전송할 때 사용
 */
@Component
@Log4j2
public class JWTUtil {

    // 키를 문자열로 받아 사용
    @Value("${org.zerock.jwt.secret}") // 환경 변수나 application.properties에서 비밀 키를 불러옵니다.
    private String keyString;

    // 비밀 키 객체 생성
    private Key key;

    // 초기화 블록을 통해 Key 객체를 생성
    // @PostConstruct 어노테이션을 추가하여 init() 메소드가 자동으로 호출되도록 합니다.
    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(keyString.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param valueMap 클레임 정보가 담긴 맵
     * @param days     유효 기간(일 단위)
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(Map<String, Object> valueMap, int days) {

        log.info("generateKey...");

        // JWT 토큰에 담을 정보 설정
        // 헤더 부분
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");      // 토큰의 타입
        headers.put("alg", "HS256");    // 암호화 알고리즘

        // payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);  // 클레임 정보

        // 유효 기간 설정 (기본적으로 분 단위)
        //int time = (60 * 24) * days;    // 테스트는 분 단위로 나중에 60*24 (일) 단위 변경
        int time = (3) * days;    // 테스트는 분 단위 3분(임시로 지정)

        // JWT 토큰 생성
        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)    // 페이로드에 클레임 정보 설정, 페이로드에는 실제로 저장할 데이터가 포함. 예를 들어 사용자 ID, 권한, 유효기간 등의 정보를 JSON 형태로 저장
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(key) // 생성된 비밀 키 사용하여 서명을 생성한다. 토큰의 유효성 보장을 위해서 별도의 구성 파일에 설정해놓은 비밀키로 암호화된 key를 사용한다.
                .compact(); // JWT문자열(토큰)이 생성됨.

        return jwtStr;
    }

    /**
     * 토큰 유효성 검사
     * - 토큰을 검증하고 클레임 정보(payload)를 반환
     * - 토큰이 유효하지 않으면 JwtException 발생
     * - 이 메소드가 호출되는 시점은? 클라이언트가 서버로 요청을 보낼 때 토큰을 함께 전송하면서 호출 됨.
     */
    public Map<String, Object> validateToken(String token) throws JwtException {

        Map<String, Object> claim = null;

        // 토큰 유효성 검사, Jwts : JWT 라이브러리로 토큰을 파싱하고 검증하는 기능을 제공
        // JwtParser 객체를 생성. 이 객체는 JWT를 파싱하고 검증하는 데 사용됩니다
        claim = Jwts.parserBuilder() // parserBuilder()를 사용하여 JwtParser를 생성
                .setSigningKey(key) // 비밀 키 객체 사용, 암호를 풀 때도 사용한 비밀 키를 사용하여 검증. 해커는 별도 구성 파일의 내용을 모르기 때문에 그 비밀키에 기반하여 위조할 수 없다.
                .build() // 빌더 패턴을 사용하여 JwtParser 객체를 생성
                .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                .getBody(); // 토큰의 클레임 정보를 반환

        return claim;
    }
}
