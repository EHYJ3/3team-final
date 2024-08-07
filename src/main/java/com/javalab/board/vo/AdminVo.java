package com.javalab.board.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminVo {
    private String adminId;
    private String email;
    private String password;
    private String name;
    private Date birth;
    private String tel;
}
