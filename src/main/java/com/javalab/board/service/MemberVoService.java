package com.javalab.board.service;

import com.javalab.board.vo.MemberVo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MemberVoService {

    // 모든 회원 조회
    public List<MemberVo> getAllMembers();

    // 회원 ID로 특정 회원 조회
    public MemberVo getMemberById(String memberId);

    // 회원 추가
    public void addMember(MemberVo member);

    // 회원 정보 수정
    public void updateMember(MemberVo member);

    // 회원 삭제
    public void deleteMember(String memberId);
}
