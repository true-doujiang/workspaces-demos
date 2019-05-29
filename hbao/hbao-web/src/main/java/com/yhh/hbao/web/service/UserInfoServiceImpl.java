package com.yhh.hbao.web.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yhh.hbao.api.service.UserInfoService;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.model.ResultAndPage;
import com.yhh.hbao.orm.mapper.UserInfoMapper;
import com.yhh.hbao.orm.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public ResultAndPage selectPage(PageDto page, UserInfoDto userInfo) {
        return this.selectResultAndPage(page, new EntityWrapper(userInfo.toModel(UserInfo.class)), UserInfoDto.class);
    }

    @Override
    public UserInfoDto selectById(Long id) {
        UserInfo userInfo = super.selectById(id);
        if(null != userInfo){
            return userInfo.toDto(UserInfoDto.class);
        }
        return null;
    }

    @Override
    public Boolean updateById(UserInfoDto userInfo) {
        return super.updateById(userInfo.toModel(UserInfo.class));
    }

    @Override
    public Long insert(UserInfoDto userInfoDto) {
        UserInfo userInfo = userInfoDto.toModel(UserInfo.class);
        if(this.insert(userInfo)){
            return userInfo.getId();
        }
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return deleteById(id);
    }

    @Override
    public UserInfoDto selectByOpenId(String openid) {
        Wrapper<UserInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("open_id",openid);
        wrapper.eq("status",0);
        UserInfo userInfo = this.selectOne(wrapper);
        if(userInfo!=null){
            return userInfo.toDto(UserInfoDto.class);
        }
        return null;
    }
}
