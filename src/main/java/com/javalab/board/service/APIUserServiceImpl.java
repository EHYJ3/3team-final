package com.javalab.board.service;

import com.javalab.board.repository.APIUserMapper;
import com.javalab.board.vo.APIUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class APIUserServiceImpl implements APIUserService {

    private final APIUserMapper apiUserMapper;

    @Override
    public APIUser findUserByMid(String mid) {
        return apiUserMapper.findUserByMid(mid);
    }


    @Override
    public int register(APIUser apiUser) {
        return apiUserMapper.insert(apiUser);
    }

    @Override
    public boolean changePassword(String mid, String newPassword) {
        return apiUserMapper.updatePassword(mid, newPassword) > 0;
    }
}
