package com.javalab.board.vo;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerScrapVo {
    private Long scrapId;
    private String jobSeekerId;
    private Long jobPostId;
    private Date created;
}
