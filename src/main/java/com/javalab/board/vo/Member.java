package com.javalab.board.vo;

import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    private Long id;                // 회원 ID
    private String password;        // 비밀번호
    private String name;            // 이름
    private LocalDate birth;        // 생년월일
    private String tel;             // 연락처
    private String email;           // 이메일
    private String address;         // 주소
    private int point;              // 포인트

    private List<MemberRole> memberRoles; // 여러 MemberRole을 가질 수 있도록 설정

    private boolean del;            // 회원 삭제 여부
    private boolean social;         // 소셜 로그인 여부
    private Long cartId;            // 장바구니 ID

    private String memberType;      // 개인회원 또는 기업회원 구분 ("PERSONAL", "BUSINESS")

    // 개별 MemberRole 객체를 추가
    public void addMemberRole(MemberRole memberRole) {
        if (this.memberRoles == null) {
            this.memberRoles = new ArrayList<>();  // memberRoles가 null인 경우 초기화
        }
        this.memberRoles.add(Objects.requireNonNull(memberRole, "MemberRole cannot be null"));
    }

    // 문자열 리스트를 받아 Role 객체 리스트로 설정
    public void setRolesFromStrings(List<String> roleStrings) {
        this.memberRoles = roleStrings.stream()
                .map(roleString -> new Role(null, roleString)) // 문자열을 Role 객체로 변환
                .map(role -> new MemberRole(null, this, role)) // Role을 MemberRole로 변환
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return id != null ? id.equals(member.id) : member.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
