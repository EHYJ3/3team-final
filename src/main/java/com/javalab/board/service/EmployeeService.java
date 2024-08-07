package com.javalab.board.service;

import com.javalab.board.vo.EmployeeCommonDto;
import com.javalab.board.vo.Employees;

import java.util.List;


public interface EmployeeService {

	List<EmployeeCommonDto> getEmployeesList(EmployeeCommonDto dto);
	EmployeeCommonDto getEmployees(int employeeId);
	int register(Employees emp);

}