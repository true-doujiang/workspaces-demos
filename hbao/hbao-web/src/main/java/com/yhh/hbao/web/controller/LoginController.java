package com.yhh.hbao.web.controller;


import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.service.WeixinServiceImpl;
import com.yhh.hbao.web.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-19 上午9:48
 **/
@RestController
@RequestMapping("/auth/login")
public class LoginController {


    @Autowired
    private WeixinServiceImpl weixinService;

    /***
     * toc 用户登录
     * @param loginVo
     * @return
     */
    @PostMapping
    public ResultResponse login(@Valid @RequestBody LoginVo loginVo){
        Map<String,Object> resultMap = weixinService.login(loginVo);
        return ResultResponse.success(resultMap);
    }

    /***
     * tob 用户登录
     * @return
     */
    @PostMapping(value="/tob")
    public ResultResponse loginTob(String username,String password){
        if("yhh".equals(username) && password.equals("ok")){
            return ResultResponse.success();
        }
        return ResultResponse.error();
    }

}