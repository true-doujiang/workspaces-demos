package com.yhh.spbootssm.service.impl;

import com.yhh.spbootssm.domain.User;
import com.yhh.spbootssm.mapper.UserMapper;
import com.yhh.spbootssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> findAll() {
        List<User> users = userMapper.findAll();
        return users;
    }

    @Override
    public int inser(User user) {
        //int i = userMapper.inser(user);
        return 0;
    }
}
