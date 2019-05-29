package com.yhh.hbao.web.controller;

import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.ReceiveLogsService;
import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.core.enums.ReceiveOpenEnums;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.model.weixin.TokenInfo;
import com.yhh.hbao.web.utils.TokenUtils;
import com.yhh.hbao.web.vo.ReceiveLogCovertVo;
import com.yhh.hbao.web.vo.ReceiveLogsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 领取记录 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/api/receiveLogs")
public class ReceiveLogsController extends BaseController {

    @Autowired
    private ReceiveLogsService receiveLogsService;

    @Autowired
    private CampaignService campaignService;

    private final static Logger LOGGER = LoggerFactory.getLogger(ReceiveLogsController.class);

    /****
     * 领取活动卡券
     * @return
     */
    @PostMapping
    public ResultResponse receve() {
        ResultResponse resultResponse = ResultResponse.success(receiveLogsService.saveReceive());
        return resultResponse;
    }

    /**
     * 根据状态获取当前用户的领取红包记录
     *
     * 默认获取已领取未兑换且未过期的红包(用户只能有一个未拆取的红包, 即最后一个领取的红包)
     * @return 满足条件的红包红包列表
     */
    @GetMapping
    public ResultResponse list (@RequestParam(value = "status", required = false) Integer status) {
        TokenInfo tokenInfo = TokenUtils.getToken();
        Long userId = tokenInfo.getUserInfoDto().getId();
        Integer[] in_status;

        if(status != null) {
            in_status = new Integer[]{status};
        }else{
            in_status = new Integer[]{ReceiveOpenEnums.BROKENING.getCode(),ReceiveOpenEnums.BROKENED.getCode()};
        }
//        campaignService.campaignStatus();
        return ResultResponse.success(receiveLogsService.getUserBroken(userId, in_status));
    }
    /**
     * 根据状态获取当前用户的领取红包记录
     *
     * 默认获取已领取未兑换且未过期的红包(用户只能有一个未拆取的红包, 即最后一个领取的红包)
     * @return 满足条件的红包红包列表
     */
    @GetMapping("/queryCount")
    public ResultResponse queryCount () {
        TokenInfo tokenInfo = TokenUtils.getToken();
        Long userId = tokenInfo.getUserInfoDto().getId();
        Integer[] in_status = new Integer[]{ReceiveOpenEnums.BROKENING.getCode(),ReceiveOpenEnums.BROKENED.getCode(),ReceiveOpenEnums.EXCHANGED.getCode()};
        return ResultResponse.success(receiveLogsService.getUserBroken(userId, in_status).size());
    }

    /**
     * 分页查询领取记录
     * @return
     */
    @GetMapping(value = "/pageList")
    public ResultResponse list(PageDto page, ReceiveLogsVo receiveLogsVo) {
        return ResultResponse.success(receiveLogsService.selectPage(page, receiveLogsVo.toDto(ReceiveLogsDto.class)));
    }

    /**
     * 根据ID查询领取记录
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultResponse selectById(@PathVariable("id") Long id) {
        return ResultResponse.success(receiveLogsService.selectById(id));
    }

    /**
     * 获取已经兑换人的列表
     * @return
     */
    @GetMapping(value = "/convert")
    public ResultResponse convertInfo() {
        ReceiveLogCovertVo resVo = new ReceiveLogCovertVo();

        resVo.setCount(receiveLogsService.getConvertCount());
        resVo.setBatches(receiveLogsService.getConvertPartNames(30));

        return ResultResponse.success(resVo);
    }

}

