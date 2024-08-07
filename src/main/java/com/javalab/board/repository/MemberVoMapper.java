package com.javalab.board.repository;

import com.javalab.board.vo.Member;
import com.javalab.board.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberVoMapper {
    // 모든 회원 조회
    List<MemberVo> selectAllMembers();

    // 회원 ID로 특정 회원 조회
    MemberVo selectMemberById(String memberId);

    // 회원 추가
    void insertMember(MemberVo member);

    // 회원 정보 수정
    void updateMember(MemberVo member);

    // 회원 삭제
    void deleteMember(String memberId);
}
