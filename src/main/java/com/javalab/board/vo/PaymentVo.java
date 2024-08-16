package com.javalab.board.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


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
public class PaymentVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long paymentId;        // payment_id
	private String compId;         // comp_id
	private String accountNum;     // account_num
	private Long jobPostId;        // jobPost_id
	private String paymentStatus;  // payment_status
	private Date paymentDate;      // payment_date
	private Date postDuration;     // post_duration
	private BigDecimal dailyRate;  // daily_rate
	private BigDecimal totalAmount; // total_amount
	// 날짜 바인딩 패턴 : yyyy-MM-dd
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dueDate ;

}