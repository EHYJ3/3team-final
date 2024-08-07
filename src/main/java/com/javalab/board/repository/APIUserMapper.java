package com.javalab.board.repository;

import com.javalab.board.vo.APIUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface APIUserMapper {

    APIUser findUserByMid(String mid);

    int insert(APIUser apiUser);

    // 패스워드 업데이트 예시
    int updatePassword(@Param("mid") String mid, @Param("newPassword") String newPassword);
}
