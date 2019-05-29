package com.yhh.hbao.web.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.CouponInfoService;
import com.yhh.hbao.api.service.ReceiveLogsService;
import com.yhh.hbao.api.service.UserCouponService;
import com.yhh.hbao.api.transfer.*;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.enums.*;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.DBUtil;
import com.yhh.hbao.core.utils.DateUtils;
import com.yhh.hbao.orm.mapper.CampaignMapper;
import com.yhh.hbao.orm.mapper.CouponInfoMapper;
import com.yhh.hbao.orm.mapper.UserCouponMapper;
import com.yhh.hbao.orm.model.Campaign;
import com.yhh.hbao.orm.model.UserCoupon;
import com.yhh.hbao.web.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户卡券信息 服务实现类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Service
public class UserCouponServiceImpl
        extends BaseServiceImpl<UserCouponMapper,UserCoupon>
        implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponInfoMapper couponInfoMapper;

    @Autowired
    private CampaignMapper campaignMapper;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private ReceiveLogsService receiveLogsService;

    @Autowired
    private CouponInfoService couponInfoService;

//    @Autowired
//    DBUtil dbUtil;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserCouponServiceImpl.class);


    public UserCouponDto selectExtById(Long id){
        return userCouponMapper.selectExtById(id);
    }

    @Override
    public Page<UserCouponDto> selectPage(PageDto page, UserCouponDto userCoupon) {
        List<UserCoupon> selectListByParam = userCouponMapper.selectList(new EntityWrapper<UserCoupon>().eq(userCoupon.getGetType() != null,"get_type",userCoupon.getGetType()));
        Page<UserCouponDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
        List<UserCouponDto> target;
        target =  com.yhh.hbao.core.utils.BeanUtils.copyPropertiesList(selectListByParam,UserCouponDto.class);
        pageDto.setRecords(target);
        return pageDto;
    }
    /**
     * 如果是卡券，根据参数查询用户领取记录
     */
    @Override
    public Page<UserCouponDto> selectPageByparam(PageDto page, UserCouponDto userCoupon){
        List<UserCouponDto> list = userCouponMapper.selectListByParam(userCoupon);
        Page<UserCouponDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
//        List<UserCouponDto> target = new ArrayList<UserCouponDto>();
//        target =  com.yhh.hbao.core.utils.BeanUtils.copyPropertiesList(list,UserCouponDto.class);
        pageDto.setRecords(list);
        return pageDto;
    };

    @Override
    public List<UserCouponDto> selectListByparam( UserCouponDto userCoupon){
        List<UserCouponDto> list = userCouponMapper.selectListByParam(userCoupon);
        for(UserCouponDto dto:list) {
            dto.setStatusDesc(ToBUserCouponEnums.getDesc(dto.getStatus()));
            dto.setCouponTypeDesc(CouponTypeEnums.getDesc(dto.getCouponType()));
        }
        return list;
    };

    @Override
    public UserCouponDto selectById(Long id) {
        UserCoupon record = userCouponMapper.selectById(id);

        if(null != record){
            UserCouponDto userCouponDto = record.toDto(UserCouponDto.class);
            userCouponDto.setCampaignDto(campaignService.selectById(userCouponDto.getCampaignId()));
            userCouponDto.setReceiveLogsDto(receiveLogsService.selectById(userCouponDto.getReceiveId()));
            userCouponDto.setCouponInfoDto(couponInfoService.selectById(userCouponDto.getCouponId()));
            return userCouponDto;
        }
        return null;
    }

    @Override
    public Integer updateById(UserCouponDto userCoupon) {
        return null;
    }

    @Override
    public UserCouponDto insert(UserCouponDto userCoupon) {
        return null;
    }

    @Override
    public Integer deleteById(Long id) {
        return null;
    }

    @Override
    public List<UserCouponDto> selectCouponList(Long userId,Integer status) {
        Wrapper<UserCoupon> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("status",status);
        List<UserCouponDto> userCouponDtoList = this.selectList(wrapper,UserCouponDto.class);
        List<Long> ids = new ArrayList<>();
        if(null!=userCouponDtoList){
            for (UserCouponDto userInfoDto: userCouponDtoList) {
                ids.add(userInfoDto.getCampaignId());
            }
            Wrapper<Campaign> campaignWrapper = new EntityWrapper<>();
            campaignWrapper.in("id",ids);
            List<Campaign> campaignDtos = campaignMapper.selectList(campaignWrapper);
            Map<Long,CampaignDto> map = new HashMap<>();
            for(Campaign campaign:campaignDtos){
                map.put(campaign.getId(), campaign.toDto(CampaignDto.class));
            }
            for(UserCouponDto userCouponDto: userCouponDtoList){
                userCouponDto.setCampaignDto(map.get(userCouponDto.getCampaignId()));
            }
        }
        return userCouponDtoList;
    }

    @Override
    public Long exchange(Long receiveLogsId,Long campaignId) {
        //1:获取领取记录
        ReceiveLogsDto receiveLogsDto = receiveLogsService.selectById(receiveLogsId);

        //2:校验兑换
        //拆取记录是否已拆完
        if(receiveLogsDto.getStatus()!=ReceiveOpenEnums.BROKENED.getCode()){
            throw new CouponBizException(CouponBizExceptionEnum.USER_COUPON_RECEIVE_EXCHANGE_ERROR);
        }

        //3:获取当前用户信息
        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();
        CampaignDto campaignDto = campaignService.selectById(campaignId);

        //4:获取一张活动卡券
        CouponInfoDto couponInfo = couponInfoService.getNxCoupon(campaignDto.getId());
        //5:封装卡券给当前用户
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCampaignId(campaignDto.getId());
        userCoupon.setGetTime(new Date());
        userCoupon.setGetType(1);
        userCoupon.setCouponId(couponInfo.getId());
        userCoupon.setReceiveId(receiveLogsId);
        userCoupon.setUserId(userInfoDto.getId());
        userCoupon.setStatus(UserCouponStatusEnum.USER_COUPON_ACTIVATED.getValue());
        userCoupon.setCouponCode(couponInfo.getCouponCode());
        userCoupon.setCouponCode(couponInfo.getCouponCode()); //给用户绑定优惠券号码
        if(campaignDto.getValidTimeType()==1){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY,campaignDto.getValidHour());
            userCoupon.setInvalidTime(calendar.getTime());
        }else{
            userCoupon.setInvalidTime(couponInfo.getInvalidTime());
        }

        //6:保存用户卡券信息 并修改卡券状态已绑定
        this.insert(userCoupon);
        if(null==userCoupon.getId()){
            throw new CouponBizException(CouponBizExceptionEnum.USER_COUPON__EXCHANGE_ERROR);
        }

        //7:使用的代金券转态改为  已绑定
        CouponInfoDto update = new CouponInfoDto();
        update.setId(couponInfo.getId());
        update.setStatus(CouponStatusEnum.BIND.getValue());
        couponInfoService.updateById(update);

        //8: 更新活动卡券的兑换次数 +1
        campaignMapper.updateUsedCount(receiveLogsDto.getCampaignId());

        //9: 领取记录状态 改为 已兑换
        ReceiveLogsDto updateRe = new ReceiveLogsDto();
        updateRe.setStatus(ReceiveOpenEnums.EXCHANGED.getCode());
        updateRe.setId(receiveLogsId);
        receiveLogsService.updateById(updateRe);

        return userCoupon.getId();
    }

    @Override
    public List<CampaignDto> selectIsGetLimit(Long userId) {
        return userCouponMapper.selectIsGetLimit(userId);
    }

    @Override
    public List<Long> deactivateUserCoupon() {
        List<UserCoupon> userCoupons = userCouponMapper.selectList(
                new EntityWrapper<UserCoupon>()
                        .le("invalid_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
                        .in("status", new Integer[]{UserCouponStatusEnum.USER_COUPON_ACTIVATED.getValue(),UserCouponStatusEnum.USER_COUPON_INIT.getValue()})
        );

        if(userCoupons.size() > 0){
            userCouponMapper.updateUserCouponStatusComplete(userCoupons);
        }

        return userCoupons.stream().map(UserCoupon::getId).collect(Collectors.toList());
    }


    /**
     * 注销用户已使用过的优惠券
     * @return
     */
//    @Override
//    @Transactional
//    public List<String> updateCouponStatus() {
//        Connection conn = dbUtil.getConnection();
//        try {
//            StringBuilder sb = new StringBuilder();
//            sb.append(" SELECT memc_code FROM vmc_b2c_member_couponlog ");
//            sb.append(" WHERE usetime <= UNIX_TIMESTAMP() AND usetime >= UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 5 SECOND)) ");
//            Statement stat = conn.createStatement();
//            ResultSet rs = stat.executeQuery(sb.toString());
//
//            List<String> couponCodes = new ArrayList<String>();
//            while (rs.next()) {
//                couponCodes.add(rs.getString("memc_code"));
//            }
//            if(null!=couponCodes&&couponCodes.size()>0){
//                userCouponMapper.updateUserCouponStatusUserd(couponCodes);
//                couponInfoMapper.updateCouponStatusUsed(couponCodes);
//                conn.commit();
//            }
//            dbUtil.close(conn,stat,rs);
//            return couponCodes;
//        } catch (SQLException e) {
//            LOGGER.error("【注销用户已使用过的优惠券】异常:"+e.getMessage());
//        }
//        return null;
//    }


}
