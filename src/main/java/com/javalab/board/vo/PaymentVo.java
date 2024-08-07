package com.javalab.board.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVo {
    private String paymentId;
    private String compId;
    private String accountNum;
    private Long jobPostId;
    private String paymentStatus;
    private Date paymentDate;
    private Date postDuration;
    private double dailyRate;
    private double totalAmount;
    private Date dueDate;
}
