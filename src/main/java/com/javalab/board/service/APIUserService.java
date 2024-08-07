package com.javalab.board.service;

import com.javalab.board.vo.APIUser;

public interface APIUserService {

    APIUser findUserByMid(String mid);

    int register(APIUser apiUser);

    boolean changePassword(String mid, String newPassword);
}
