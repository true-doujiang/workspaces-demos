package com.yhh.hbao.api.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.transfer.CouponInfoDto;
import com.yhh.hbao.core.model.PageDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * <p>
 * 卡券信息 Interface类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
public interface CouponInfoService{

    /**
     * 分页查询卡券信息
     * @return
     */
    Page<CouponInfoDto> selectPage(PageDto page, CouponInfoDto couponInfo);

    /**
     * 根据ID查询卡券信息
     * @return
     */
    CouponInfoDto selectById(Long id);

    /**
     * 修改卡券信息
     * @return
     */
    Boolean updateById(CouponInfoDto couponInfo);

    /**
     * 保存卡券信息
     * @return
     */
    CouponInfoDto insert(CouponInfoDto couponInfo);

    /**
     * 删除卡券信息
     * @return
     */
    Integer deleteById(Long id);

    /**
     * toc根据ID查询卡券信息
     * @return
     */
    CouponInfoDto selectByIdToc(Long id);
    /**
     * toc分页查询卡券信息
     * @return
     */
    Page<CouponInfoDto> selectPageToc(PageDto page, CouponInfoDto couponInfo);

    String insertFromExcel(String fileName) throws Exception;

    /**
     * 导入卡券
     * @param file
     * @return
     * @throws Exception
     */
    List<List<String>> uploadCoupon(String file) throws Exception;
    HSSFWorkbook downloadCoupon(List<List<String>> param) throws Exception;

    /***
     * 锁定并返回一张活动卡券
     * @param campaignId
     * @return
     */
    CouponInfoDto getNxCoupon(Long campaignId);
}
