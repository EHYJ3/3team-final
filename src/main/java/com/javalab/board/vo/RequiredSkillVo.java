package com.javalab.board.vo;


import lombok.*;

import java.time.LocalDate;


/**
 * 게시물 도메인 클래스
 * - BoardVo: 이는 보통 데이터베이스와 직접 상호작용하는 엔티티로, 데이터베이스의 테이블과
 *   매핑되는 객체이다. 데이터베이스의 상태를 그대로 반영하며, 이를 통해 데이터베이스로부터
 *   데이터를 읽거나 쓸 수 있다. 데이터베이스 테이블과 거의 똑같다고 보면 된다.
 * [빌더패턴]
 * BoardDto: 빌더 패턴을 사용하여 객체 생성 시, 가독성과 유지보수성을 높일 수 있다.
 * AllArgsConstructor : 모든 필드를 파라미터로 받는 생성자를 만들어준다. 빌더패턴 사용시 필요함.
 *                      빌더패턴 사용시 모든 필드를 파라미터로 받는 생성자를 이용해서 객체를 생성함.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class RequiredSkillVo {
	private Long requiredSkillId; // 시퀀스를 통해 자동 생성될 ID
	private Long jobPostId;       // 외래 키로 참조되는 jobPost ID
	private String skill;         // 저장할 스킬
}
