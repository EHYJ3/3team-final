package com.javalab.board.vo;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberVo {
    private String id;                // 아이디
    private String password;          // 비밀번호
    private String passwordConfirm;   // 비밀번호 확인
    private String name;              // 이름
    private LocalDate birth;          // 생년월일
    private String tel;               // 연락처
    private String email;             // 이메일
    private String address;           // 주소

    // 추가적인 필드를 설정할 수 있습니다. 예를 들어, 포인트, 등록일자 등
    private int point;                // 포인트 (선택적)

    // 회원 유형 추가
    private String memberType;        // 개인회원 또는 기업회원 구분 ("PERSONAL", "BUSINESS")
}
