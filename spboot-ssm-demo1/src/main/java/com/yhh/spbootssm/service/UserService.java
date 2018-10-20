package com.yhh.spbootssm.service;

import com.yhh.spbootssm.domain.User;

import java.util.List;

public interface UserService {


    List<User> findAll();

    int inser(User user);


}
