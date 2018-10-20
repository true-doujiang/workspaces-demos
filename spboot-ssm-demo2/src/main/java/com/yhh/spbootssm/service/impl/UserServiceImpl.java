package com.yhh.spbootssm.service.impl;

import com.yhh.spbootssm.mapper.UserMapper;
import com.yhh.spbootssm.domain.User;
import com.yhh.spbootssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User showTest(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> tests() {
        return userMapper.selectAll();
    }

    @Override
    public void testAdd(User test) {
        userMapper.insertSelective(test);
    }

    @Transactional
    @Override
    public void testTra(Integer id1, Integer id2) {
        User test1 = new User();
        test1.setId(id1);
        test1.setName("hh");
        test1.setAge(12);
        userMapper.insertSelective(test1);

        User test2 =  new User();
        test2.setId(id2);
        test2.setName("swg");
        test2.setAge(22);
        userMapper.insertSelective(test2);

    }
}
