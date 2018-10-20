package com.yhh.spbootssm.controller;

import com.yhh.spbootssm.domain.User;
import com.yhh.spbootssm.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("/getUserById/{id}")
    public User getTest(@PathVariable int id){
        User test = userService.showTest(id);
        return test;
    }

    @RequestMapping("/users")
    public List<User> tests(){
        List<User> tests = userService.tests();
        return tests;
    }

    @RequestMapping("/addNew")
    public void testAdd(Integer id){
        User test1 = new User();
        test1.setId(id);
        test1.setName("wangwu");
        test1.setAge(32);
        userService.testAdd(test1);
    }

    @RequestMapping("/insertTra")
    public void insertTra(Integer id1, Integer id2){
        userService.testTra(id1, id2);
    }
}
