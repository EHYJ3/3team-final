package com.javalab.board.vo;

import lombok.Data;

@Data
public class MemberVo {
    private String memberId; // MEMBER_ID
    private String password; // PASSWORD
    private String name;     // NAME
    private String email;    // EMAIL
    private int point;       // POINT
}