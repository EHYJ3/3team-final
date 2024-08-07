package com.javalab.board.security;

import com.javalab.board.dto.APIUserDTO;
import com.javalab.board.repository.APIUserMapper;
import com.javalab.board.vo.APIUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 시큐리티의 UserDetailsService를 구현한 클래스
 * - 로그인 시 사용자 정보를 가져오는 역할
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    // MyBatis 매퍼 주입
    private final APIUserMapper apiUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        APIUser apiUser = apiUserMapper.findUserByMid(username);

        if (apiUser == null) {
            throw new UsernameNotFoundException("Cannot find mid");
        }

        log.info("APIUserDetailsService apiUser-------------------------------------");

        // 시큐리티의 User 객체의 자식 객체인 APIUserDTO 객체 생성, ApiUser 객체가 시큐리티 인증 객체로 사용됨
        APIUserDTO dto = new APIUserDTO(
                apiUser.getMid(),
                apiUser.getMpw(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        log.info(dto);

        return dto;
    }
}
