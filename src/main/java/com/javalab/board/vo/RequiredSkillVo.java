package com.javalab.board.vo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequiredSkillVo {
    private Long requiredSkillId;
    private Long jobPostId;
    private String skill;
}
