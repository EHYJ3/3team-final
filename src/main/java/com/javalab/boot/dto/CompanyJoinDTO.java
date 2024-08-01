package com.javalab.boot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyJoinDTO {
    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;

    @NotBlank(message = "사업자 등록번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{10}$", message = "사업자 등록번호는 10자리 숫자여야 합니다.")
    private String businessNumber;

    @NotBlank(message = "대표자명은 필수 입력 값입니다.")
    private String representativeName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상이며, 숫자, 문자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPassword;

    @NotBlank(message = "연락처는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phoneNumber;

    @NotBlank(message = "회사 주소는 필수 입력 값입니다.")
    private String companyAddress;
}


