package com.yhh.hbao.orm.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.orm.model.Campaign;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动信息 Mapper 接口
 * </p>
 * @author yhh
 *
 */
@Repository
public interface CampaignMapper extends BaseMapper<Campaign> {


    /***
     * 获取等级权重最低的活动
     * @return
     */
    Campaign selectMinWeightCampaign(Map<String,Object> paramMap);

    /**
     * 根据ID查询活动信息
     * @return
     */
    Campaign selectById(Long id);

    /****
     * 获取代金券活动
     * @param paramMap
     * @return
     */
    List<CampaignDto> selectListByPrice(Map<String,Object> paramMap);

    /****
     * 使用量+1
     * @param campaignId
     */
    void updateUsedCount(Long campaignId);

    /**
     * 修改过期活动的状态
     * @return
     */
    Integer updateCampaignStatusComplete(List<Campaign> campaign);

    /****
     * 根据卡券类型和状态查询等级
     * @return
     */
    List<Integer> selectAllLevel(Map<String,Object> paramMap);

    /****
     * 查询到等级 权重最高的红包
     * @param paramMap
     * @return
     */
    CampaignDto selectHignByLevel(Map<String,Object> paramMap);
}
