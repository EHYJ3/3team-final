package com.javalab.board.controller;

import com.javalab.board.service.MemberVoService;
import com.javalab.board.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberVoService memberService;

    // 모든 회원 목록 페이지
    @GetMapping
    public String listMembers(Model model) {
        List<MemberVo> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "memberList"; // 타임리프 페이지 이름
    }

    // 특정 회원 정보 페이지
    @GetMapping("/{memberId}")
    public String getMember(@PathVariable String memberId, Model model) {
        MemberVo member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "memberDetail"; // 타임리프 페이지 이름
    }

    // 개인회원 추가 폼 페이지
    @GetMapping("/join")
    public String addPersonalMemberForm(Model model) {
        model.addAttribute("member", new MemberVo());
        return "member/join"; // 개인회원 추가 폼 타임리프 페이지 이름
    }

    // 기업회원 추가 폼 페이지
    @GetMapping("/add/business")
    public String addBusinessMemberForm(Model model) {
        model.addAttribute("member", new MemberVo());
        return "addBusinessMember"; // 기업회원 추가 폼 타임리프 페이지 이름
    }

    // 개인회원 추가 처리
    @PostMapping("/add/personal")
    public String addPersonalMember(@ModelAttribute MemberVo member) {
        member.setMemberType("PERSONAL"); // 개인회원 유형 설정
        memberService.addMember(member);
        return "redirect:/members";
    }

    // 기업회원 추가 처리
    @PostMapping("/add/business")
    public String addBusinessMember(@ModelAttribute MemberVo member) {
        member.setMemberType("BUSINESS"); // 기업회원 유형 설정
        memberService.addMember(member);
        return "redirect:/members";
    }

    // 회원 수정 폼 페이지
    @GetMapping("/edit/{memberId}")
    public String editMemberForm(@PathVariable String memberId, Model model) {
        MemberVo member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "editMember"; // 타임리프 페이지 이름
    }

    // 회원 수정 처리
    @PostMapping("/edit/{memberId}")
    public String editMember(@PathVariable String memberId, @ModelAttribute MemberVo member) {
        member.setId(memberId);
        memberService.updateMember(member);
        return "redirect:/members"; // 회원 목록으로 리다이렉트
    }

    // 회원 삭제 처리
    @GetMapping("/delete/{memberId}")
    public String deleteMember(@PathVariable String memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/members"; // 회원 목록으로 리다이렉트
    }
}
