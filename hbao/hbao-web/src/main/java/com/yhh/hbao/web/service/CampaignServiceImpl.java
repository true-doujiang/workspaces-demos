package com.yhh.hbao.web.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.ReceiveLogsService;
import com.yhh.hbao.api.service.UserCouponService;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.enums.CampaignStatusEnum;
import com.yhh.hbao.core.enums.CouponTypeEnum;
import com.yhh.hbao.core.enums.ReceiveOpenEnums;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.BeanUtils;
import com.yhh.hbao.core.utils.DateUtils;
import com.yhh.hbao.orm.mapper.CampaignMapper;
import com.yhh.hbao.orm.model.Campaign;
import com.yhh.hbao.web.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;


/**
 * <p>
 * 活动信息 服务实现类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Service
public class CampaignServiceImpl extends BaseServiceImpl<CampaignMapper,Campaign> implements CampaignService {

    @Autowired
    private CampaignMapper campaignMapper;

    @Autowired
    private ReceiveLogsService receiveLogsService;

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public Page<CampaignDto> selectPage(PageDto page, CampaignDto campaign) {
        Map<String, Boolean> orderMap = new HashMap<String, Boolean>();
        orderMap.put("create_time", false);

        List<Campaign> selectListByParam = campaignMapper.selectList(new EntityWrapper<Campaign>()
                .like(campaign.getName() != null,"name",campaign.getName())
                .eq(campaign.getCouponType() != null,"coupon_type",campaign.getCouponType())
                .gt(campaign.getBeginTime() != null ,"begin_time",campaign.getBeginTime())
                .le(campaign.getEndTime() != null ,"end_time",campaign.getEndTime())
                .eq(true, "is_delete", 0)
                .orderBy("create_time",false)
        );

        Page<CampaignDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
        List<CampaignDto> target = BeanUtils.copyPropertiesList(selectListByParam,CampaignDto.class);
        pageDto.setRecords(target);
        return pageDto;
    }

    @Override
    public CampaignDto selectById(Long id) {
        Campaign campaign = campaignMapper.selectById(id);
        if(null==campaign){
            throw new CouponBizException(CouponBizExceptionEnum.CAMPAIGN_IS_NOT_FOUND);
        }
        return campaign.toDto(CampaignDto.class);
    }

    @Override
    public Integer updateById(CampaignDto campaign) {
        super.updateById(campaign.toModel(Campaign.class));
        return 1;
    }

    /**
     * 逻辑删除  is_delete = 1
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Long id) {
        Campaign campaign = new Campaign();
        campaign.setId(id);
        campaign.setIsDelete(1);
        return super.updateById(campaign);
    }

    /**
     * 保存活动
     * @param campaign
     * @return
     */
    @Override
    public CampaignDto insert(CampaignDto campaign) {
        Campaign entity= campaign.toModel(Campaign.class);
        entity.setBeginTime(campaign.getBeginTime());
        entity.setEndTime(campaign.getEndTime());
        entity.setValidBeginTime(campaign.getValidBeginTime());
        entity.setValidEndTime(campaign.getValidEndTime());
        this.insert(entity);
        return entity.toDto(CampaignDto.class);
    }


    @Override
    public List<CampaignDto> selecDtoListById(List<Long> campaignIds) {
        List<Campaign> campaigns = this.selectBatchIds(campaignIds);
        if(null!=campaigns&&campaigns.size()>0){
            return BeanUtils.copyPropertiesList(campaigns,CampaignDto.class);
        }
        return null;
    }

    @Override
    public CampaignDto selectMinWeightCampaign(Integer level) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("level",level);
        paramMap.put("status",1);
        paramMap.put("couponType",CouponTypeEnum.RED.getValue());
        Campaign campaign = campaignMapper.selectMinWeightCampaign(paramMap);
        if(null!=campaign){
            return campaign.toDto(CampaignDto.class);
        }
        return null;
    }

    @Override
    public CampaignDto selectDaiMinWeightCampaign(Integer level, Integer consumerLimitType) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("level",level);
        paramMap.put("status",1);
        paramMap.put("couponType",CouponTypeEnum.EXCHANGE_COUPON.getValue());
        Campaign campaign = campaignMapper.selectMinWeightCampaign(paramMap);
        if(null!=campaign){
            return campaign.toDto(CampaignDto.class);
        }
        return null;
    }

    /**
     * 活动类型(代金券)列表  根据红包的额度查询代金券的额度
     * @return
     */
    @Override
    public List<CampaignDto> selectListByPrice() {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("status",1);
        paramMap.put("couponType",CouponTypeEnum.EXCHANGE_COUPON.getValue());
        paramMap.put("consumerLimitType",1); //无门槛
        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();
        //用户已领取无门槛活动    获取用户已经获取到的卡券所属活动
        List<CampaignDto> limitCampaignDtos = userCouponService.selectIsGetLimit(userInfoDto.getId());
        List<Long> notInIds = limitCampaignDtos.stream().map(CampaignDto::getId).collect(Collectors.toList());
        if(null!=notInIds&&notInIds.size()>0){
            paramMap.put("consumerLimitType",2);//有门槛
        }
        //获取每一个等级,权重最高的代金券活动
        List<Integer> allLevel = campaignMapper.selectAllLevel(paramMap); //查询代金券的所有等级 1、2、3
        List<CampaignDto> campaignDtos = new ArrayList<>();
        allLevel.forEach(level->{
            paramMap.put("level",level);
            //查询等级最高的活动
            campaignDtos.add(campaignMapper.selectHignByLevel(paramMap));
        });

        //获取当前用户已拆完的领取记录
        List<ReceiveLogsDto> receiveLogsDtos = receiveLogsService.getUserBroken(userInfoDto.getId(),new Integer[]{ReceiveOpenEnums.BROKENING.getCode(),ReceiveOpenEnums.BROKENED.getCode(),ReceiveOpenEnums.EXCHANGED.getCode()});
        for(ReceiveLogsDto receiveLogsDto : receiveLogsDtos){
            receiveLogsDto.setCampaignDto(campaignMapper.selectById(receiveLogsDto.getCampaignId()).toDto(CampaignDto.class));
        }
        for(CampaignDto campaignDto:campaignDtos){

           for(ReceiveLogsDto receiveLogsDto:receiveLogsDtos){

               if(campaignDto.getLevel().equals(receiveLogsDto.getCampaignDto().getLevel())){
                   //有领取记录前台就可以拆
                   campaignDto.setReceiveLogsDto(receiveLogsDto);
               }
           }
        }
        return campaignDtos;
    }

    /***
     * 查询活动状态，有红包活动处于进行中时返回True
     */
    @Override
    public Boolean campaignStatus() {
        // 查询有无进行中且未过期的红包活动
        Wrapper<Campaign> redCapaignWrapper = new EntityWrapper<Campaign>();
        redCapaignWrapper.eq("status", CampaignStatusEnum.IN_PROGRESS.getStatus());
        redCapaignWrapper.eq("is_delete", 0);
        Integer count = campaignMapper.selectCount(redCapaignWrapper);
        if(null==count||count==0){
            throw new CouponBizException(CouponBizExceptionEnum.CAMPAIGN_IS_NOT_ARRIVING);
        }
        return count>0;
    }

    @Override
    public List<Long> deactiveCampaign() {
        List<Campaign> campaigns = campaignMapper.selectList(
                new EntityWrapper<Campaign>()
                        .le("valid_end_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
                        .ne( "status", CampaignStatusEnum.IN_COMPLETE.getStatus())
                );

        if(campaigns.size() > 0){
            campaignMapper.updateCampaignStatusComplete(campaigns);
        }
        return campaigns.stream().map(Campaign::getId).collect(Collectors.toList());
    }

}
