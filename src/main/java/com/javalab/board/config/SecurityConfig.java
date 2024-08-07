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
        http.authenticationManager(authenticationManager);

        // APILoginFilter 설정
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // [3] APILoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // [2] APILoginFilter
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // [4] TokenCheckFilter
        http.addFilterBefore(tokenCheckFilter(jwtUtil, apiUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        // [3] RefreshTokenFilter
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), TokenCheckFilter.class);

        // Disable CSRF and configure session management
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // [1] CORS configuration
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        // Configure HTTP security
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/public/**", "/", "/member/**").permitAll() // Example of permitting public GET access
                        .requestMatchers(HttpMethod.POST, "/generateToken", "/refreshToken").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/memberList").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/scss/**").permitAll()  // Allow static resources
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
