package com.yhh.hbao.api.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.api.transfer.UserCouponDto;
import com.yhh.hbao.core.model.PageDto;

import java.util.List;

/**
 * <p>
 * 用户卡券信息 Interface类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
public interface UserCouponService{

    /**
     * 分页查询用户卡券信息
     * @return
     */
    Page<UserCouponDto> selectPage(PageDto page, UserCouponDto userCoupon);

    /**
     * 根据ID查询用户卡券信息
     * @return
     */
    UserCouponDto selectById(Long id);

    /**
     * 根据ID查询领取记录及其额外信息
     * @return
     */
    UserCouponDto selectExtById(Long id);


    Page<UserCouponDto> selectPageByparam(PageDto page, UserCouponDto userCoupon);

    List<UserCouponDto> selectListByparam( UserCouponDto userCoupon);


    /**
     * 修改用户卡券信息
     * @return
     */
    Integer updateById(UserCouponDto userCoupon);

    /**
     * 保存用户卡券信息
     * @return
     */
    UserCouponDto insert(UserCouponDto userCoupon);

    /**
     * 删除用户卡券信息
     * @return
     */
    Integer deleteById(Long id);

    /***
     * 根据用户ID和status获取卡券
     * @param userId
     * @return
     */
    List<UserCouponDto> selectCouponList(Long userId,Integer status);

    /****
     * 兑换卡券
     * @param receiveLogsId
     * @return
     */
    Long exchange(Long receiveLogsId,Long campaignId);


    /**
     * 查询用户所有已获取卡券无门槛的活动
     * @param userId
     * @return
     */
    List<CampaignDto> selectIsGetLimit(Long userId);

    /**
     * 过期兑换的卡券
     * @return
     */
    List<Long> deactivateUserCoupon();


    /**
     * 注销用户已使用的卡券
     */
//    List<String> updateCouponStatus();

}
