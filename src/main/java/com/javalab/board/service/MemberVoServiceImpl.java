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

    @Override
    public List<MemberVo> getAllMembers() {
        return memberMapper.selectAllMembers();
    }

    @Override
    public MemberVo getMemberById(String memberId) {
        return memberMapper.selectMemberById(memberId);
    }

    @Override
    public void addMember(MemberVo member) {
        if (member.getId() == null || member.getEmail() == null) {
            throw new IllegalArgumentException("회원 ID와 이메일은 필수입니다.");
        }
        if (!member.getPassword().equals(member.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if (member.getMemberType() == null) {
            throw new IllegalArgumentException("회원 유형은 필수입니다.");
        }
        MemberVo existingMember = memberMapper.selectMemberByEmail(member.getEmail());
        if (existingMember != null) {
            throw new IllegalArgumentException("이메일이 이미 사용 중입니다.");
        }
        memberMapper.insertMember(member);
    }

    @Override
    public void updateMember(MemberVo member) {
        if (member.getId() == null) {
            throw new IllegalArgumentException("회원 ID는 필수입니다.");
        }
        memberMapper.updateMember(member);
    }

    @Override
    public void deleteMember(String memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("회원 ID는 필수입니다.");
        }
        memberMapper.deleteMember(memberId);
    }
}
