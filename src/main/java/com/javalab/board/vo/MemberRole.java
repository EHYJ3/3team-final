package com.javalab.board.vo;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class MemberRole {
    private Long id;         // MemberRole ID
    private Member member;  // 회원 정보
    private Role role;      // 역할 정보

    public MemberRole() {
    }

    public MemberRole(Long id, Member member, Role role) {
        this.id = id;
        // Null check to ensure member and role are not null
        this.member = Objects.requireNonNull(member, "Member cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberRole that = (MemberRole) o;

        if (!id.equals(that.id)) return false;
        if (!member.equals(that.member)) return false;
        return role.equals(that.role);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + member.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }
}
