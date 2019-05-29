package com.yhh.hbao.web.service;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.service.BrokenLogsService;
import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.ReceiveLogsService;
import com.yhh.hbao.api.service.UserInfoService;
import com.yhh.hbao.api.transfer.BrokenLogsDto;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.enums.ReceiveOpenEnums;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.HttpUtil;
import com.yhh.hbao.orm.mapper.BrokenLogsMapper;
import com.yhh.hbao.orm.mapper.UserInfoMapper;
import com.yhh.hbao.orm.model.BrokenLogs;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.utils.TokenUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 拆券记录 服务实现类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Service
public class BrokenLogsServiceImpl
        extends BaseServiceImpl<BrokenLogsMapper,BrokenLogs>
        implements BrokenLogsService {

    @Autowired
    private ReceiveLogsService receiveLogsService;

    @Autowired
    private BrokenLogsMapper brokenLogsMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CampaignService campaignService;

    @Value("${user.center.api}")
    private String userCenterApi;

    private static final Logger LOGGER = Logger.getLogger(BrokenLogsServiceImpl.class);


    @Override
    public Page<BrokenLogsDto> selectPage(PageDto page, BrokenLogsDto brokenLogs) {
        List<BrokenLogs> selectListByParam = brokenLogsMapper.selectList(new EntityWrapper<BrokenLogs>().like(brokenLogs.getNiceName() != null,"nice_name",brokenLogs.getNiceName()));
        Page<BrokenLogsDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
        List<BrokenLogsDto> target =  com.yhh.hbao.core.utils.BeanUtils.copyPropertiesList(selectListByParam,BrokenLogsDto.class);
        pageDto.setRecords(target);
        return pageDto;
    }

    @Override
    public BrokenLogsDto selectById(Long id) {
        BrokenLogs record = brokenLogsMapper.selectById(id);
        if (null == record) {
            return null;
        }
        BrokenLogsDto logsDto = record.toDto(BrokenLogsDto.class);
        return logsDto;
    }

    @Override
    public Integer selectCountByReceiveLogId(Long receiveLogsId) {
        Wrapper<BrokenLogs> wrapper = new EntityWrapper<>();
        wrapper.eq("receive_logs_id",receiveLogsId);
        return this.selectCount(wrapper);
    }

    @Override
    public List<BrokenLogsDto> selectListByReceiveLogsId(Long receiveLogsId) {
        Wrapper<BrokenLogs> wrapper = new EntityWrapper<>();
        wrapper.eq("receive_logs_id",receiveLogsId);
        List<BrokenLogsDto> brokenLogsDtoList = this.selectList(wrapper,BrokenLogsDto.class);
        if(brokenLogsDtoList != null){
            List<Long> ids = brokenLogsDtoList.stream().map(BrokenLogsDto::getUserId).collect(Collectors.toList());

//            Wrapper<UserInfo> userInfoWrapper = new EntityWrapper<>();
//            userInfoWrapper.in("id", ids);
//            List<UserInfo> userInfos = userInfoMapper.selectList(userInfoWrapper);
//            Map<Long, String> map = new HashMap();

//            for(UserInfo userInfo: userInfos) {
//                map.put(userInfo.getId(), userInfo.getHeadIcon());
//            }

            //修改为从新项目获取用户信息
            Map<Long, String> map2 = new HashMap();
            if (ids!=null) {
                for (int i=0; i<ids.size(); i++) {
                    ResultResponse response = HttpUtil.doGet(userCenterApi+ids.get(i), null, ResultResponse.class);
                    UserInfoDto userInfoDto = JSON.parseObject(response.getData().toString(), UserInfoDto.class);
                    map2.put(userInfoDto.getId(), userInfoDto.getHeadIcon());
                }
            }

            for(BrokenLogsDto brokenLogsDto:brokenLogsDtoList){
                brokenLogsDto.setHeadIcon(map2.get(brokenLogsDto.getUserId()));
            }

//            for(BrokenLogsDto brokenLogsDto:brokenLogsDtoList){
//                brokenLogsDto.setHeadIcon(map.get(brokenLogsDto.getUserId()));
//            }
        }

        return brokenLogsDtoList;
    }

    @Override
    public String brokenReceive(Long receiveLogsId) {
        //1:获取领取记录
        ReceiveLogsDto receiveLogsDto = receiveLogsService.selectById(receiveLogsId);
        //2:获取当前用户信息
        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();
        if(userInfoDto.getId() .equals(receiveLogsDto.getUserId())) {
            throw new CouponBizException(CouponBizExceptionEnum.BROKEN_USER_SELF_BROKEND);
        }

        //3:校验当前用户是否拆过
        checkBroken(userInfoDto,receiveLogsDto);
        //4:封装拆取记录
        BrokenLogs brokenLogs = new BrokenLogs();
        brokenLogs.setReceiveLogsId(receiveLogsId);
        brokenLogs.setUserId(userInfoDto.getId());
        brokenLogs.setBrokenTime(new Date());
        brokenLogs.setNiceName(userInfoDto.getNickName());
        brokenLogs.setCampaignId(receiveLogsDto.getCampaignId());
        //5:保存拆取记录
        this.insert(brokenLogs);
        if(null==brokenLogs.getId()){
            throw new CouponBizException(CouponBizExceptionEnum.RECEIVELOGS_IS_NOT_BROKENING);
        }
        int brokenCount = this.getBrokenCount(receiveLogsId);
        //6:获取活动拆取人数
        CampaignDto campaignDto = campaignService.selectById(receiveLogsDto.getCampaignId());
        //如果拆取记录+当前拆取 大于设置拆取次数 修改领取状态为已拆完
        if(brokenCount>campaignDto.getBrokenCount()-1){
            ReceiveLogsDto updateDto = new ReceiveLogsDto();
            updateDto.setId(receiveLogsId);
            updateDto.setStatus(ReceiveOpenEnums.BROKENED.getCode());
            receiveLogsService.updateById(updateDto);
        }
        ResultResponse response = HttpUtil.doGet(userCenterApi+receiveLogsDto.getUserId(), null, ResultResponse.class);
        UserInfoDto reciveUser = JSON.parseObject(response.getData().toString(), UserInfoDto.class);
        return reciveUser.getNickName();
        //UserInfoDto reciveUser = userInfoService.selectById(receiveLogsDto.getUserId());
        // return reciveUser.getNickName();
    }

    /***
     * 校验拆取异常
     * @param userInfoDto
     * @param receiveLogsDto
     */
    private void checkBroken(UserInfoDto userInfoDto,ReceiveLogsDto receiveLogsDto){
        //拆取记录是否存在
        if(null==receiveLogsDto){
            throw new CouponBizException(CouponBizExceptionEnum.RECEIVELOGS_IS_NOT_FOUND);
        }
        //拆取记录是否进行中
        if(receiveLogsDto.getStatus()!=ReceiveOpenEnums.BROKENING.getCode()){
            throw new CouponBizException(CouponBizExceptionEnum.RECEIVELOGS_IS_NOT_BROKENING);
        }
        //当前用户是否拆过
        Wrapper<BrokenLogs> wrapper = new EntityWrapper<>();
        wrapper.eq("receive_logs_id",receiveLogsDto.getId());
        wrapper.eq("user_id",userInfoDto.getId());
        BrokenLogs brokenLogs = this.selectOne(wrapper);
        if(null!=brokenLogs){
            throw new CouponBizException(CouponBizExceptionEnum.BROKEN_USER_IS_BROKEND,brokenLogs.getNiceName());
        }
    }

    /****
     * 获取领取记录的拆取次数
     * @param receiveLogsId
     * @return
     */
    private Integer getBrokenCount(Long receiveLogsId){
        Wrapper<BrokenLogs> wrapper = new EntityWrapper<>();
        wrapper.eq("receive_logs_id",receiveLogsId);
        return this.selectCount(wrapper);
    }

    /**
     * 获取领取记录拆取的详细信息
     * @param receiveLogsId
     * @return
     */
    private List<BrokenLogs> selectBrokenLogs(Long receiveLogsId){
        List<BrokenLogs> selectListByParam = brokenLogsMapper.selectList(new EntityWrapper<BrokenLogs>()
                .eq(true, "receive_logs_id",receiveLogsId));;
        return selectListByParam;
    }
}
