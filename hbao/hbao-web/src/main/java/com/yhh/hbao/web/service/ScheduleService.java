package com.yhh.hbao.web.service;

import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.ReceiveLogsService;
import com.yhh.hbao.api.service.UserCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
public class ScheduleService {

    private final static long ONE_MINUTE = 60 * 1000;

    private final static long FIVE_MINUTE = 5 * ONE_MINUTE;

    private final static long TEN_MINUTE = 10 * ONE_MINUTE;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private ReceiveLogsService receiveLogsService;

    @Autowired
    private UserCouponService userCouponService;

    private Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    @Value("${task.runing.ip}")
    private String runingIp;

    /**
     * 把活动过期
     */
    @Scheduled(fixedRate = FIVE_MINUTE)
    public void deactivateCampaignEntery() {
        if(isCanExcute()){
            List<Long> campaignIds = campaignService.deactiveCampaign();
            LOGGER.info(String.format("[定时任务] - 更新过期活动状态 %d 条, 过期的记录Id: %s ", campaignIds.size(), campaignIds.toString()));

        }
    }

    /**
     * 把用户领取的红包过期
     */
    @Scheduled(fixedRate = FIVE_MINUTE)
    public void deactivateReceiveLogsEntery() {

        if(isCanExcute()){
            List<Long> receiveLogIds = receiveLogsService.deactivateReceiveLogs();
            LOGGER.info(String.format("[定时任务] - 更新过期用户红包状态 %d 条, 过期的记录Id: %s ", receiveLogIds.size(), receiveLogIds.toString()));

        }


    }

    /**
     * 更新兑换的过期卡券状态
     */
    @Scheduled(fixedRate = FIVE_MINUTE)
    public void deactivateUserCouponEntery() {

        if(isCanExcute()){
            List<Long> userCouponIds = userCouponService.deactivateUserCoupon();
            LOGGER.info(String.format("[定时任务] - 更新兑换的过期卡券状态 %d 条, 过期的记录Id: %s ", userCouponIds.size(), userCouponIds.toString()));
        }

    }

    /**
     * 更新用户已经使用的优惠券状态
     */
//    @Scheduled(fixedRate = 5*1000)
//    public void cancelCoupon() {
//        if(isCanExcute()){
//            List<String> couponCodes = userCouponService.updateCouponStatus();
//            LOGGER.info(String.format("[定时任务] - 更新已经使用兑换卡券状态 %d 条, 过期记录优惠券号: %s", couponCodes.size(), couponCodes.toString()));
//        }
//    }


    private boolean isCanExcute(){
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            return host.equals(runingIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }
}
