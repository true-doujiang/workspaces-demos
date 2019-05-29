package com.yhh.hbao.api.service;


import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.model.ResultAndPage;

/**
 * <p>
 * 用户信息表 Interface类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
public interface UserInfoService{

    /**
     * 分页查询用户信息表
     * @return
     */
    ResultAndPage selectPage(PageDto page, UserInfoDto userInfo);

    /**
     * 根据ID查询用户信息表
     * @return
     */
    UserInfoDto selectById(Long id);

    /**
     * 修改用户信息表
     * @return
     */
    Boolean updateById(UserInfoDto userInfo);

    /**
     * 保存用户信息表
     * @return
     */
    Long insert(UserInfoDto userInfo);

    /**
     * 删除用户信息表
     * @return
     */
    Boolean deleteById(Long id);

    /****
     * 根据OpenID查询用户信息
     * @param openid
     * @return
     */
    UserInfoDto selectByOpenId(String openid);
}
