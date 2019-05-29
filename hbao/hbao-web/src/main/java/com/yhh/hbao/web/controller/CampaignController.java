package com.yhh.hbao.web.controller;

import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.BeanUtils;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.vo.CampaignStatusVo;
import com.yhh.hbao.web.vo.CampaignVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 活动信息 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/api/campaign")
public class CampaignController extends BaseController {

    @Autowired
    private CampaignService campaignService;

    /**
     * 分页查询活动信息
     * @return
     */
    @GetMapping("/list")
    public ResultResponse list(PageDto page, CampaignVo campaignVo) {
        return ResultResponse.success(campaignService.selectPage(page,campaignVo.toDto(CampaignDto.class)));
    }

    /**
     * 根据ID查询活动信息
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultResponse selectById(@PathVariable("id") Long id) {
        return ResultResponse.success(campaignService.selectById(id));
    }

    /**
     * 修改活动信息
     * @return
     */
    @PutMapping
    public ResultResponse updateById(@RequestBody CampaignVo campaignVo) {
        return ResultResponse.success(campaignService.updateById(campaignVo.toDto(CampaignDto.class)));
    }

    /**
     * 保存活动信息
     * @return
     */
    @PostMapping
    public ResultResponse save(@RequestBody CampaignVo campaignVo) {
        CampaignDto campaign=new CampaignDto();
        BeanUtils.copyProperties(campaignVo,campaign);
        return ResultResponse.success(campaignService.insert(campaign));
    }

    /**
     * 活动类型(代金券)列表  根据红包的额度查询代金券的额度
     * 用户要去兑换的活动列表， 有领取记录并且是拆取中的可以兑换。 前台去做判断。
     * @return
     */
    @GetMapping
    public ResultResponse getCampaignList(){
        List<CampaignDto> campaignList = campaignService.selectListByPrice();
        return ResultResponse.success(campaignList);
    }

    /**
     * 查询是否有在进行中的活动    有：true    无：false
     */
    @GetMapping("/status")
    public ResultResponse getCampaignStatus() {
        CampaignStatusVo campaignStatusVo = new CampaignStatusVo();
        campaignStatusVo.setStatus(campaignService.campaignStatus());
        return ResultResponse.success(campaignStatusVo);
    }

}

