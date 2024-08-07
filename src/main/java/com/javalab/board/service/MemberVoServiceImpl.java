package com.javalab.board.service;


import com.javalab.board.repository.MemberVoMapper;
import com.javalab.board.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberVoServiceImpl implements MemberVoService {

    @Autowired
    private MemberVoMapper memberMapper;

    // 모든 회원 조회
    public List<MemberVo> getAllMembers() {
        return memberMapper.selectAllMembers();
    }

    // 회원 ID로 특정 회원 조회
    public MemberVo getMemberById(String memberId) {
        return memberMapper.selectMemberById(memberId);
    }

    // 회원 추가
    public void addMember(MemberVo member) {
        memberMapper.insertMember(member);
    }

    // 회원 정보 수정
    public void updateMember(MemberVo member) {
        memberMapper.updateMember(member);
    }

    // 회원 삭제
    public void deleteMember(String memberId) {
        memberMapper.deleteMember(memberId);
    }
}
