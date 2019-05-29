package com.yhh.hbao.web.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.service.BrokenLogsService;
import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.ReceiveLogsService;
import com.yhh.hbao.api.service.UserCouponService;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.enums.CouponTypeEnums;
import com.yhh.hbao.core.enums.ReceiveOpenEnums;
import com.yhh.hbao.core.enums.ToBReceiveLogsEnums;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.redis.RedisClientUtil;
import com.yhh.hbao.core.utils.BeanUtils;
import com.yhh.hbao.core.utils.DateUtils;
import com.yhh.hbao.orm.mapper.CouponInfoMapper;
import com.yhh.hbao.orm.mapper.ReceiveLogsMapper;
import com.yhh.hbao.orm.mapper.UserCouponMapper;
import com.yhh.hbao.orm.model.ReceiveLogs;
import com.yhh.hbao.web.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 领取记录 服务实现类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Service
public class ReceiveLogsServiceImpl extends BaseServiceImpl<ReceiveLogsMapper,ReceiveLogs> implements ReceiveLogsService {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private ReceiveLogsMapper receiveLogsMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponInfoMapper couponInfoMapper;

    @Autowired
    private BrokenLogsService brokenLogsService;

    @Autowired
    private RedisClientUtil redisClientUtil;

    //伪造数据
   private static final List<String> fakeNames = Arrays.asList(
            "撞入你怀",
            "以后的以后 拿命爱自己",
            "泪再咸没有海水咸",
            "心再酸没有柠檬酸",
            "敏感侵蚀",
            "真理是赤裸的〃",
            "男人也可以是女人的衣服、",
            "我跟你不熟。/",
            "- 连回忆都是负荷i",
            "萌面怪受 | 假面超人",
            "素面白裳红酥手^!",
            "孤雁掠过",
            "如果可以， | 我想和你结婚。",
            "远得要命的爱情真要命.",
            "南方小镇 | 北方酒馆",
            "灵魂永远伴随着你",
            "故港唐人",
            "泪几行",
            "落笔风流",
            "伴我余生",
            "飞逝的梦",
            "∝独守半座空城",
            "秀恩爱 | 遭雷劈",
            "深爱之心",
            "沉沦ヽ犹如骄傲过后的湮灭",
            "疏桐吹绿",
            "看著妳丶傻傻的笑",
            "渲染· | 离别·",
            "捧出这肺腑",
            "我的女人乀你不配拥有"
    );

   private static final String REDIS_KEY_PREFIX = "ReceiveLogsService";


    @Override
    public ReceiveLogsDto selectById(Long id) {
        ReceiveLogs receiveLogs = super.selectById(id);
        if(null==receiveLogs){
            throw new CouponBizException(CouponBizExceptionEnum.RECEIVELOGS_IS_NOT_FOUND);
        }
        return receiveLogs.toDto(ReceiveLogsDto.class);
    }

    @Override
    public ReceiveLogsDto selectExtById(Long id){
        ReceiveLogsDto receiveLogs = receiveLogsMapper.selectExtById(id);
        Long couponId = userCouponMapper.selectCouponIdByReceiveLogId(receiveLogs.getId());
        if (couponId != null ){
            String couponCode = couponInfoMapper.selectCouponCodeById(couponId);
            receiveLogs.setCouponCode(couponCode);
        }
        return receiveLogs;
    }

    @Override
    public Page<ReceiveLogsDto> selectPage(PageDto page, ReceiveLogsDto receiveLogs) {
        List<ReceiveLogs> selectListByParam = receiveLogsMapper.selectList(new EntityWrapper<ReceiveLogs>().like(receiveLogs.getNickName() != null,"nick_name",receiveLogs.getNickName())
        .like(receiveLogs.getMobile() != null,"mobile",String.valueOf(receiveLogs.getMobile())).eq(receiveLogs.getCity()!=null,"city",receiveLogs.getCity()!=null));
        Page<ReceiveLogsDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
        List<ReceiveLogsDto> target = BeanUtils.copyPropertiesList(selectListByParam,ReceiveLogsDto.class);
        pageDto.setRecords(target);
        return pageDto;
    }

    @Override
    public Page<ReceiveLogsDto> selectPageByparam(PageDto page,ReceiveLogsDto dto){
        List<ReceiveLogsDto> list = receiveLogsMapper.selectListByParam(dto);
        Page<ReceiveLogsDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
        pageDto.setRecords(list);
        return pageDto;
    }

    @Override
    public List<ReceiveLogsDto> selectListByparam( ReceiveLogsDto userCoupon){
        List<ReceiveLogsDto> list = receiveLogsMapper.selectListByParam(userCoupon);
        for(ReceiveLogsDto dto:list) {
            Long couponId = userCouponMapper.selectCouponIdByReceiveLogId(dto.getId());
            if (couponId != null ){
                String couponCode = couponInfoMapper.selectCouponCodeById(couponId);
                dto.setCouponCode(couponCode);
            }
            dto.setStatusDesc(ToBReceiveLogsEnums.getDesc(dto.getStatus()));
            dto.setCouponTypeDesc(CouponTypeEnums.getDesc(dto.getCouponType()));
        }
        return list;
    };

    @Override
    public Boolean updateById(ReceiveLogsDto receiveLogs) {
        return super.updateById(BeanUtils.copyProperties(receiveLogs, ReceiveLogs.class));
    }

    @Override
    @Transactional
    public ReceiveLogsDto saveReceive() {
        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();

        //1:获取用户拆取中或已拆完的领取记录      已拆完   已兑换
        List<ReceiveLogsDto> receiveLogsDtos = getUserBroken(userInfoDto.getId(),
                new Integer[]{ReceiveOpenEnums.BROKENING.getCode(),
                        ReceiveOpenEnums.BROKENED.getCode(),
                        ReceiveOpenEnums.EXCHANGED.getCode()});

        ReceiveLogsDto receiveLogsDto;
        //2:如果有领取记录
        if(receiveLogsDtos!=null && receiveLogsDtos.size()>0) {
            receiveLogsDto = receiveLogsDtos.get(0);
            //3:如果已拆完 则领取下一个等级红包
            return receive(receiveLogsDto.getCampaignDto().getLevel()+1, userInfoDto);
        }

        //5:如果没有记录 则创建一个领取记录
        return receive(1, userInfoDto);
    }


    /**
     * 用户领取红包
     * @param level
     * @param userInfoDto
     * @return
     */
    private ReceiveLogsDto receive(Integer level,UserInfoDto userInfoDto){


        CampaignDto campaignDtos = campaignService.selectMinWeightCampaign(level);

        if(null==campaignDtos){
            throw new CouponBizException(CouponBizExceptionEnum.CAMPAIGN_LEVEL_IS_NOT_FOUND);
        }

        //库存校验
        if(campaignDtos.getIssueCount()<=campaignDtos.getUsedCount()){
            throw new CouponBizException(CouponBizExceptionEnum.CAMPAIGN_COUNT_IS_ERROR);
        }

        //校验用户 每人每天每个活动限领次数和每人每天每个活动累计次数
        checkReceiveCount(userInfoDto, campaignDtos);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        ReceiveLogs receiveLogs = new ReceiveLogs();
        receiveLogs.setCampaignId(campaignDtos.getId());
        receiveLogs.setGetTime(calendar.getTime());
        receiveLogs.setConsumerLimitType(campaignDtos.getConsumerLimitType());

        //24小时候领取过期
        calendar.add(Calendar.HOUR_OF_DAY,24);
        receiveLogs.setInvalidTime(calendar.getTime());

        receiveLogs.setUserId(userInfoDto.getId());
        receiveLogs.setStatus(ReceiveOpenEnums.BROKENING.getCode());

        //4:保存领取记录
        this.insert(receiveLogs);
        if(null==receiveLogs.getId()){
            throw new CouponBizException(CouponBizExceptionEnum.BROKEN_RECEIVE_NOT_ERROR);
        }

        ReceiveLogsDto receiveLogsDto = receiveLogs.toDto(ReceiveLogsDto.class);
        //获取所属活动的详情
        receiveLogsDto.setCampaignDto(campaignService.selectById(receiveLogsDto.getCampaignId()));
        return receiveLogsDto;
    }

    /**
     * 获取有效期内的用户卡劵记录
     * @param userId
     * @return
     */
    @Override
    public List<ReceiveLogsDto> getUserBroken(Long userId,Integer[] status) {
        Wrapper<ReceiveLogs> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.in("status",status);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY,-24);
        wrapper.gt("get_time",calendar.getTime());
        wrapper.orderBy("get_time",false);


        List<ReceiveLogsDto> receiveLogs = this.selectList(wrapper, ReceiveLogsDto.class);

        if(receiveLogs == null){
            receiveLogs = new ArrayList<>();
        }

        for(ReceiveLogsDto receiveLogsDto: receiveLogs){
            receiveLogsDto.setBrokenCount(brokenLogsService.selectCountByReceiveLogId(receiveLogsDto.getId()));
            receiveLogsDto.setCampaignDto(campaignService.selectById(receiveLogsDto.getCampaignId()));
        }

        return receiveLogs;
    }

    @Override
    public List<Long> deactivateReceiveLogs() {
        List<ReceiveLogs> receiveLogs = receiveLogsMapper.selectList(
                new EntityWrapper<ReceiveLogs>()
                        .le("invalid_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"))
                        .in("status", new Integer[]{ReceiveOpenEnums.BROKENING.getCode(), ReceiveOpenEnums.BROKENED.getCode()})
        );

        if(receiveLogs.size() > 0){
            receiveLogsMapper.updateReceiveLogsStatusComplete(receiveLogs);
        }
        return receiveLogs.stream().map(ReceiveLogs::getId).collect(Collectors.toList());
    }

    @Override
    public Long getConvertCount() {

        String getConvertCountCacheKey = REDIS_KEY_PREFIX + "." + "getConvertCount";

        if (redisClientUtil.exists(getConvertCountCacheKey))
            return new Long(redisClientUtil.get(getConvertCountCacheKey));

        Long count = receiveLogsMapper.statisticsConvertCount();

        //伪造数据
        count += 6632;
        redisClientUtil.set(getConvertCountCacheKey, count.toString(), 300);

        return count;
    }

    @Override
    public List<String> getConvertPartNames(Integer limint) {
        //todo 这里做优化 数据放redis里面
        String getConvertPartNameCacheKey = REDIS_KEY_PREFIX + "." + "getConvertPartNames";

        if (redisClientUtil.exists(getConvertPartNameCacheKey)) {
            return JSON.parseArray(redisClientUtil.get(getConvertPartNameCacheKey), String.class);
        }

        List<String> partNickname =  receiveLogsMapper.statisticsConvertPartName(limint);

        if(partNickname.size() < 30){
            partNickname.addAll(partNickname.size(), fakeNames.subList(0, 30 - partNickname.size()));
        }

        redisClientUtil.set(getConvertPartNameCacheKey, JSON.toJSONString(partNickname), 300);

        return partNickname;
    }


    /***
     * 获取用户_活动_累计已领次数
     * @param userInfoDto
     * @param campaignDto
     */
    private void checkReceiveCount(UserInfoDto userInfoDto, CampaignDto campaignDto){
        if(null==campaignDto){
            throw new CouponBizException(CouponBizExceptionEnum.CAMPAIGN_IS_NOT_FOUND);
        }

        if(campaignDto.getReceiveType() == 2) {
            Wrapper<ReceiveLogs> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id", userInfoDto.getId());
            wrapper.eq("campaign_id", campaignDto.getId());
            int allCount = this.selectCount(wrapper);
            if (campaignDto.getUserAllCount() <= allCount) {
                throw new CouponBizException(CouponBizExceptionEnum.USER_RECEIVE_ALL_COUNT_ERROR);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date zero = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String endTime = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
            wrapper.lt("get_time", endTime);
            wrapper.ge("get_time", zero);
            wrapper.eq("user_id", userInfoDto.getId());
            int dayCount = this.selectCount(wrapper);
            if (campaignDto.getUserDayCount() <= dayCount) {
                throw new CouponBizException(CouponBizExceptionEnum.USER_RECEIVE_DAYS_COUNT_ERROR);
            }
        }
    }
    public  static void main(String[] args){
        Integer i = new Integer(1);
        System.out.println(i ==1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        System.out.println(zero.toString());
    }

}
