package com.yhh.hbao.api.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.core.model.PageDto;

import java.util.List;

/**
 * <p>
 * 领取记录 Interface类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
public interface ReceiveLogsService{


    /**
     * 根据ID查询领取记录
     * @return
     */
    ReceiveLogsDto selectById(Long id);

    /**
     * 根据ID查询领取记录及其额外信息
     * @return
     */
    ReceiveLogsDto selectExtById(Long id);

    /**
     * 通过参数批量查询领取记录
     */
    Page<ReceiveLogsDto> selectPage(PageDto page, ReceiveLogsDto receiveLogs);

    /**
     * 修改领取记录
     * @return
     */
    Boolean updateById(ReceiveLogsDto receiveLogs);

    /**
     * 领取红包
     * @return
     */
    ReceiveLogsDto saveReceive();

    /****
     * 根据用户ID和领取状态查询领取记录
     * @param userId
     * @return
     */
    List<ReceiveLogsDto> getUserBroken(Long userId,Integer[] status);


    /**
     * 如果是红包，根据参数查询
     */
    Page<ReceiveLogsDto> selectPageByparam(PageDto page,ReceiveLogsDto dto);

    List<ReceiveLogsDto> selectListByparam(ReceiveLogsDto dto);

    /**
     * 更新过期红包状态
     * @return
     */
    List<Long> deactivateReceiveLogs();

    /**
     * 获取已兑换用户
     * @return
     */
    Long getConvertCount();

    /**
     * 获取已兑换部分用户名
     * @return
     */
    List<String> getConvertPartNames(Integer limint);
}
