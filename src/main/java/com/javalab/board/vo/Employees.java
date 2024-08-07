package com.javalab.board.vo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employees {

	 private Integer employeeId;
	 private String lastName;
	 private String firstName;
	 private String email;
	 private String phoneNumber;
	 private String hireDate;
	 private String jobId;
	 private Long salary;
	 private Integer commissionPct;
	 private Integer managerId;
	 private Integer departmentId;
	 
	// 단위테스트를 위한 생성자
	public Employees(int employeeId, String lastName, String firstName) {
		this.employeeId = employeeId;
		this.lastName = lastName;
		this.firstName = firstName;
	}
	 
	 
}