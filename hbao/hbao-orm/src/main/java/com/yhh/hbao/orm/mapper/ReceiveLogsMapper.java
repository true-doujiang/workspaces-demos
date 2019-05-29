package com.yhh.hbao.orm.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.orm.model.ReceiveLogs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 领取记录 Mapper 接口
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Repository
public interface ReceiveLogsMapper extends BaseMapper<ReceiveLogs> {

    /**
     * 通过id查询领取记录及用户，活动的相关信息
     * @param id
     * @return
     */
    ReceiveLogsDto selectExtById(Long id);

    /**
     * 通过参数查询领取记录及用户，活动的相关信息
     * @param req
     * @return
     */
    List<ReceiveLogsDto> selectListByParam(ReceiveLogsDto req);

    Integer updateReceiveLogsStatusComplete(List<ReceiveLogs> receiveLogs);

    Long statisticsConvertCount();

    List<String> statisticsConvertPartName(@Param("limit") Integer limit);
}
