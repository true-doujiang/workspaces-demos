package com.yhh.hbao.orm.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.api.transfer.UserCouponDto;
import com.yhh.hbao.orm.model.UserCoupon;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户卡券信息 Mapper 接口
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Repository
public interface UserCouponMapper extends BaseMapper<UserCoupon> {


    UserCouponDto selectExtById(Long id);

    Long selectCouponIdByReceiveLogId(Long receiveId);

    /****
     * 查询用户已获取的活动卡券
     * @param userId
     * @return
     */
    List<CampaignDto> selectIsGetLimit(Long userId);

    /**
     *
     * @param userCoupon
     * @return
     */
    List<UserCouponDto> selectListByParam(UserCouponDto userCoupon);

    /**
     * 更新过期的用户卡券状态
     * @param userCoupons
     * @return
     */
    Integer updateUserCouponStatusComplete(List<UserCoupon> userCoupons);

    /**
     * 注销用户已使用的优惠券
     * @param couponCodes
     * @return
     */
    Integer updateUserCouponStatusUserd(List<String> couponCodes);
}
