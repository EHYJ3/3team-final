package com.javalab.board.vo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResumeVo {
    private Long resumeId;
    private String jobSeekerId;
    private String title;
    private String content;
    private String education;
    private String experience;
    private String link;
    private int hitNo;
    private String fileName;
    private String filePath;

}
