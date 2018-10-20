package com.yhh.spbootssm.controller;


import com.yhh.spbootssm.domain.User;
import com.yhh.spbootssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/list/all")
    public List<User> findAll(){
        List<User> users = userService.findAll();
        return users;
    }

}
