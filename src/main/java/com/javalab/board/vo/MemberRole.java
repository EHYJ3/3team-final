package com.javalab.board.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRole {
    private Long memberId;  // member 테이블의 FK
    private Long roleId;    // role 테이블의 FK

    // 생성자와 접근자 메서드는 Lombok을 사용하여 자동 생성
}
