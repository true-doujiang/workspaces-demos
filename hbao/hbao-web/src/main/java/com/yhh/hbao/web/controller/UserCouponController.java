package com.yhh.hbao.web.controller;

import com.yhh.hbao.api.service.UserCouponService;
import com.yhh.hbao.api.transfer.UserCouponDto;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.orm.model.UserCoupon;
import com.yhh.hbao.web.utils.TokenUtils;
import com.yhh.hbao.web.vo.ReceiveLogsParamVo;
import com.yhh.hbao.web.vo.UserCouponVo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户卡券信息 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/api/userCoupon")
public class UserCouponController extends BaseController {

    @Autowired
    private UserCouponService userCouponService;

    /**
     * 兑换卡券
     * @param receiveLogsParamVo
     * @return
     */
    @PostMapping
    public ResultResponse exchange(@Valid @RequestBody ReceiveLogsParamVo receiveLogsParamVo) {
        return ResultResponse.success(userCouponService.exchange(receiveLogsParamVo.getReceiveLogsId(),receiveLogsParamVo.getCampaignId()));
    }

    /**
     * 根据用户ID和status获取卡券
     * @param status
     * @return
     */
    @GetMapping
    public ResultResponse getCoupon(@Valid @RequestParam Integer status){
        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();
        List<UserCouponDto> list =  userCouponService.selectCouponList(userInfoDto.getId(),status);
        return ResultResponse.success(list);
    }

    /**
     * 分页查询用户卡券信息
     * @return
     */
    @GetMapping(value = "/list")
    public ResultResponse list(PageDto page, UserCouponVo userCouponVo) {
        return ResultResponse.success(userCouponService.selectPage(page, userCouponVo.toDto(UserCouponDto.class)));
    }

    /**
     * 根据ID查询用户卡券信息
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultResponse selectById(@PathVariable("id") Long id) {
        return ResultResponse.success(userCouponService.selectById(id));
    }

}

