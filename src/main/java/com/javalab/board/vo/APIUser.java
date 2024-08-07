package com.javalab.board.vo;  // 변경: 패키지명을 vo로 변경

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIUser {

    private String mid;  // id
    private String mpw; // pw

    public void changePw(String mpw){   // 변경: changePw -> changePw
        this.mpw = mpw;
    }
}
