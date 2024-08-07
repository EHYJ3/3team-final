package com.javalab.board.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyScrapVo {
    private Long scrapId;
    private String compId;
    private Long resumeId;
    private Date created;
}
