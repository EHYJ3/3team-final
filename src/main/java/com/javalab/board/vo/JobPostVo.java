package com.javalab.board.vo;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobPostVo {
    private Long jobPostId;
    private String compId;
    private String title;
    private String content;
    private String position;
    private String salary;
    private String experience;
    private String education;
    private String address;
    private int scrapCount;
    private Date endDate;
    private String homepage;
    private Date created;
    private int hitNo;
    private String status;
}
