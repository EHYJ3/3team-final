package com.javalab.board.vo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistVo {
    private String jobSeekerId;
    private String blackId;
    private String compId;
    private String reason;
}
