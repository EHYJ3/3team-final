package com.javalab.board.config;

import com.javalab.board.security.APIUserDetailsService;
import com.javalab.board.security.filter.APILoginFilter;
import com.javalab.board.security.filter.RefreshTokenFilter;
import com.javalab.board.security.filter.TokenCheckFilter;
import com.javalab.board.security.handler.APILoginSuccessHandler;
import com.javalab.board.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // Enable method security for pre/post annotations
@RequiredArgsConstructor
public class SecurityConfig {

    // Dependencies injected
    private final APIUserDetailsService apiUserDetailsService;
    private final JWTUtil jwtUtil;

    // PasswordEncoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // WebSecurityCustomizer Bean to ignore static resources
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------web configure-------------------");

        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    // Main SecurityFilterChain Bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("------------configure-------------------");

        // Build AuthenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(apiUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Set custom AuthenticationManager
        http.authenticationManager(authenticationManager);

        // APILoginFilter 설정
        // "/generateToken" 경로로 들어오는 요청을 처리, /generateToken 엔드포인트는 클라이언트가
        // 서버에 로그인 요청을 할 때 사용하는 경로, UsernamePasswordAuthenticationToken을 생성
        // 하고 인증 매니저에게 전달, 인증 매니저는 UserDetailsService를 통해 사용자 정보를 가져와서 인증 처리
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // [3] APILoginSuccessHandler - APILoginFilter 인증 성공시 즉시 실행
        // JWT Token을 생성해서 클라이언트에게 응답, 이때 필요한 JWTUtil을 주입.
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil); // Create success handler
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler); // Set success handler

        // [2] APILoginFilter
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // [4] 토큰 체크 필터 - 클라이언트의 모든 요청에 대해 적용. 토큰이 유효하지 않거나 없는 경우 요청 차단
        http.addFilterBefore(tokenCheckFilter(jwtUtil, apiUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        // [3] 리프레시 토큰 필터 - TokenCheckFilter보다 앞서 있는 이유는 만료된 액세스 토큰을 자동으로 새로 발급받고 검증받기 위함
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), TokenCheckFilter.class);

        // Disable CSRF and configure session management
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // [1] CORS configuration(가장 먼저 호출되는 필터 설정)
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));





        // Configure HTTP security
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/public/**", "/", "/member/**").permitAll() // Example of permitting public GET access
                        .requestMatchers(HttpMethod.POST, "/generateToken", "/refreshToken").permitAll()
                        // Swagger UI 관련 경로를 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**").permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }

    // CORS configuration source
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Custom TokenCheckFilter setup
    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil, APIUserDetailsService apiUserDetailsService) {
        return new TokenCheckFilter(apiUserDetailsService, jwtUtil);
    }
}
