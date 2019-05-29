package com.yhh.hbao.web.controller;

import com.yhh.hbao.api.service.UserInfoService;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.HttpUtil;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.orm.model.UserInfo;
import com.yhh.hbao.web.vo.UserInfoVo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/api/userInfo")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;


    @Value("${user.center.api}")
    private String userCenterApi;

    /**
     * 分页查询用户信息表
     * @return
     */
    @GetMapping
    public ResultResponse list(PageDto page, UserInfoVo userInfoVo) {
        return ResultResponse.success(userInfoService.selectPage(page, userInfoVo.toDto(UserInfoDto.class)));
    }

    /**
     * 根据ID查询用户信息表
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultResponse selectById(@PathVariable("id") Long id) {
        ResultResponse response = HttpUtil.doGet(userCenterApi+id, null, ResultResponse.class);
        UserInfoDto userInfoDto = JSON.parseObject(response.getData().toString(), UserInfoDto.class);
        return ResultResponse.success(userInfoDto);
        //return ResultResponse.success(userInfoService.selectById(id));
    }

    /**
     * 修改用户信息表
     * @return
     */
    @PutMapping
    public ResultResponse updateById(@RequestBody UserInfoVo userInfoVo) {
        return ResultResponse.success(userInfoService.updateById(userInfoVo.toDto(UserInfoDto.class)));
    }

    /**
     * 保存用户信息表
     * @return
     */
    @PostMapping
    public ResultResponse save(@RequestBody UserInfoVo userInfoVo) {
        return ResultResponse.success(userInfoService.insert(userInfoVo.toDto(UserInfoDto.class)));
    }

}

