package com.yhh.hbao.orm.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yhh.hbao.orm.model.CouponInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 卡券信息 Mapper 接口
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Repository
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {


    /**
     * 注销用户已使用的优惠券
     * @param couponCodes
     * @return
     */
    Integer updateCouponStatusUsed(List<String> couponCodes);

    String selectCouponCodeById(Long id);

}
