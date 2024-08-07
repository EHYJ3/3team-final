package com.javalab.board.repository;

import com.javalab.board.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberVoMapper {
    List<MemberVo> selectAllMembers();

    MemberVo selectMemberById(String memberId);

    void insertMember(MemberVo member);

    void updateMember(MemberVo member);

    void deleteMember(String memberId);

    // 이메일로 회원 조회 메소드 추가
    MemberVo selectMemberByEmail(String email);
}
