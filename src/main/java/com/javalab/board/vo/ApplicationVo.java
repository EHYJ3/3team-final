package com.javalab.board.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationVo {
    private Long applicationId;
    private Long resumeId;
    private Long jobPostId;
    private Date created;
}
