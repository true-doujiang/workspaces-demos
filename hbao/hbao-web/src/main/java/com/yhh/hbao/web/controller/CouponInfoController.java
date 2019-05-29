package com.yhh.hbao.web.controller;

import com.yhh.hbao.api.service.CouponInfoService;
import com.yhh.hbao.api.transfer.CouponInfoDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 卡券信息 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/api/couponInfo")
public class CouponInfoController extends BaseController {

    @Autowired
    private CouponInfoService couponInfoService;

}

