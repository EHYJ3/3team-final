package com.javalab.board.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Role {
    private Long id;         // 역할 ID
    private String roleName; // 역할 이름 (예: "USER", "ADMIN", "BUSINESS")

    public Role() {
    }

    public Role(Long id, String roleName) {
        this.id = id;
        this.roleName = Objects.requireNonNull(roleName, "Role name cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return id != null ? id.equals(role.id) : role.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
