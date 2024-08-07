package com.javalab.board.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerVo {
    private String jobSeekerId;
    private String email;
    private String password;
    private String name;
    private Date birth;
    private String tel;
    private String fileName;
    private String filePath;
    private String address;
}
