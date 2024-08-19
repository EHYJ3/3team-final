package com.javalab.board.security;

import com.javalab.board.dto.SocialMemberDto;
import com.javalab.board.repository.LoginMapper;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.CompanyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest....{}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        // Check for supported clients
        if (!"GitHub".equals(clientName) && !"Kakao".equals(clientName)) {
            throw new OAuth2AuthenticationException("Unsupported client: " + clientName);
        }

        log.info("clientName {} ", clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userId = null;

        if ("GitHub".equals(clientName)) {
            userId = String.valueOf(attributes.get("id")); // GitHub ID를 사용
        } else if ("Kakao".equals(clientName)) {
            userId = String.valueOf(attributes.get("id")); // 카카오 ID를 사용
        }

        log.info("===============================");
        log.info("Received ID from {}: {}", clientName, userId);
        log.info("===============================");

        return processUser(userId, attributes, clientName);
    }

    private OAuth2User processUser(String userId, Map<String, Object> attributes, String clientName) {
        // Determine if the user is a JobSeeker or Company based on the client
        if ("GitHub".equals(clientName)) {
            return processJobSeeker(userId, attributes);
        } else if ("Kakao".equals(clientName)) {
            return processCompany(userId, attributes);
        } else {
            throw new OAuth2AuthenticationException("Unsupported client: " + clientName);
        }
    }

    private OAuth2User processJobSeeker(String userId, Map<String, Object> attributes) {
        JobSeekerVo jobSeeker = loginMapper.loginJobSeeker(userId);
        log.info("UserID (JobSeeker): {}", userId);

        String email = (String) attributes.get("email");

        if (jobSeeker == null) { // DB에 저장된 사용자 정보가 없으면
            String password = passwordEncoder.encode("1111"); // 기본 비밀번호 설정

            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            log.info("New User Authorities: {}", authorities);

            JobSeekerVo newJobSeeker = new JobSeekerVo();
            newJobSeeker.setJobSeekerId(userId); // ID로 설정
            newJobSeeker.setPassword(password);
            newJobSeeker.setConfirmPassword(password);
            newJobSeeker.setName("NoName");
            newJobSeeker.setEmail(email != null ? email : "noemail@example.com");
            newJobSeeker.setAddress("NoAddress");
            loginMapper.saveJobSeeker(newJobSeeker);

            loginMapper.saveRole(userId, "USER"); // "USER" 역할 저장

            return new SocialMemberDto(
                    userId,
                    "NoName",
                    email != null ? email : "noemail@example.com",
                    authorities,
                    attributes,
                    true // Indicate this is a social user
            );
        } else {
            Collection<SimpleGrantedAuthority> authorities = loginMapper.getRolesByUserId(jobSeeker.getJobSeekerId()).stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // ROLE_ prefix 추가
                    .collect(Collectors.toList());

            log.info("Existing User Authorities: {}", authorities);

            return new SocialMemberDto(
                    userId,
                    jobSeeker.getName(),
                    email != null ? email : "noemail@example.com",
                    authorities,
                    attributes,
                    true // Indicate this is a social user
            );
        }
    }

    private OAuth2User processCompany(String userId, Map<String, Object> attributes) {
        CompanyVo company = loginMapper.loginCompany(userId);
        log.info("UserID (Company): {}", userId);

        String email = (String) attributes.get("email");

        if (company == null) { // DB에 저장된 사용자 정보가 없으면
            String password = passwordEncoder.encode("1111"); // 기본 비밀번호 설정

            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMPANY"));
            log.info("New User Authorities: {}", authorities);

            CompanyVo newCompany = new CompanyVo();
            newCompany.setCompId(userId); // ID로 설정
            newCompany.setPassword(password);
            newCompany.setCompanyName("NoName");
            newCompany.setEmail(email != null ? email : "noemail@example.com");
            newCompany.setAddress("NoAddress");
            loginMapper.saveCompany(newCompany);

            loginMapper.saveRole(userId, "COMPANY"); // "COMPANY" 역할 저장

            return new SocialMemberDto(
                    userId,
                    "NoName",
                    email != null ? email : "noemail@example.com",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMPANY")),
                    attributes,
                    true // Indicate this is a social user
            );
        } else {
            Collection<SimpleGrantedAuthority> authorities = loginMapper.getRolesByUserId(company.getCompId()).stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // ROLE_ prefix 추가
                    .collect(Collectors.toList());

            log.info("Existing User Authorities: {}", authorities);

            return new SocialMemberDto(
                    userId,
                    company.getCompanyName(),
                    email != null ? email : "noemail@example.com",
                    authorities,
                    attributes,
                    true // Indicate this is a social user
            );
        }
    }
}
