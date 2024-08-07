package com.javalab.board.repository;

import com.javalab.board.vo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface MemberRepository {

    // 회원 저장
    int save(Member member);

    // 이메일로 회원 조회
    Member findByEmail(String email);

    // ID로 회원 조회
    Optional<Member> findById(Long id);

    // 회원 업데이트
    int update(Member member);

    // 회원 삭제
    int deleteById(Long id);

    // 모든 회원 조회
    List<Member> findAll();

    // 비밀번호와 소셜 상태 업데이트
    int updatePasswordAndSocial(@Param("email") String email, @Param("password") String encodedPassword);
}
