package com.javalab.board.service;

import com.javalab.board.vo.MemberVo;
import java.util.List;

public interface MemberVoService {

    List<MemberVo> getAllMembers();

    MemberVo getMemberById(String memberId);

    void addMember(MemberVo member);

    void updateMember(MemberVo member);

    void deleteMember(String memberId);
}
