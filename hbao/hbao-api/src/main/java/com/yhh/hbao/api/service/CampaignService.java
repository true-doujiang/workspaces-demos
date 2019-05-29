package com.yhh.hbao.api.service;



import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.core.model.PageDto;

import java.util.List;

/**
 * <p>
 * 活动信息 Interface类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
public interface CampaignService{

    /**
     * 分页查询活动信息
     * @return
     */
    Page<CampaignDto> selectPage(PageDto page, CampaignDto campaign);

    /**
     * 根据ID查询活动信息
     * @return
     */
    CampaignDto selectById(Long id);

    /**
     * 修改活动信息
     * @return
     */
    Integer updateById(CampaignDto campaign);

    /**
     * 保存活动信息
     * @return
     */
    CampaignDto insert(CampaignDto campaign);

    /**
     * 逻辑删除  is_delete = 1
     * @param id
     * @return
     */
    boolean deleteById(Long id);


    /***
     * 根据IDS获取活动
     * @param campaignIds
     * @return
     */
    List<CampaignDto> selecDtoListById(List<Long> campaignIds);


    /****
     * 根据活动等级查询正在进行中的权重最低的活动列表
     * @param level
     * @return
     */
    CampaignDto selectMinWeightCampaign(Integer level);

    /****
     * 根据代金券活动
     * @return
     */
    List<CampaignDto> selectListByPrice();

    /***
     * 查询活动状态，所有活动处于进行中时返回True
     */
    Boolean campaignStatus();

    /**
     * 变更过期活动的状态
     * @return
     */
    List<Long> deactiveCampaign();

    /****
     * 查询等级权重最低的代金券
     * @param level
     * @return
     */
    CampaignDto selectDaiMinWeightCampaign(Integer level, Integer consumerLimitType);

}
