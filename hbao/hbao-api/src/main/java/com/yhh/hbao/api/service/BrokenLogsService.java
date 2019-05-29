package com.yhh.hbao.api.service;



import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.transfer.BrokenLogsDto;
import com.yhh.hbao.core.model.PageDto;

import java.util.List;

/**
 * <p>
 * 拆券记录 Interface类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
public interface BrokenLogsService{



    /**
     * 分页查询
     * @param page
     * @param brokenLogs
     * @return
     */
    Page<BrokenLogsDto> selectPage(PageDto page, BrokenLogsDto brokenLogs) ;

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    BrokenLogsDto selectById(Long id);

    /****
     * 拆领取ID的活动
     * @param receiveLogsId
     * @return
     */
    String brokenReceive(Long receiveLogsId);

    /***
     * 查询拆取列表数量
     * @param id
     * @return
     */
    Integer selectCountByReceiveLogId(Long id);

    /***
     * 查询领取的拆取列表
     * @param receiveLogsId
     * @return
     */
    List<BrokenLogsDto> selectListByReceiveLogsId(Long receiveLogsId);
}
