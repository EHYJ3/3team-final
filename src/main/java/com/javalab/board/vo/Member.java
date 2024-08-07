package com.javalab.board.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    private Long id;        // member_id
    private String name;
    private String email;
    private String password;
    private String address;

    // 여러 권한을 가질 수 있도록 설정
    private List<Role> roles;

    private boolean del;
    private boolean social;
    private Long cartId;

    // 개별 Role 객체를 추가
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();  // roles가 null인 경우 초기화
        }
        this.roles.add(role);
    }

    // 문자열 리스트를 받아 Role 객체 리스트로 설정
//    public void setRoles(List<String> roleStrings) {
//        this.roles = roleStrings.stream()
//                .map(roleString -> new Role(null, roleString)) // 문자열을 Role 객체로 변환
//                .collect(Collectors.toList());
//    }
}
