package com.yhh.hbao.web.controller;

import com.yhh.hbao.api.service.BrokenLogsService;
import com.yhh.hbao.api.transfer.BrokenLogsDto;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.BeanUtils;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.vo.ReceiveLogsParamVo;
import com.yhh.hbao.web.vo.BrokenLogsVo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 拆券记录 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/api/brokenLogs")
public class BrokenLogsController extends BaseController {

    @Autowired
    private BrokenLogsService brokenLogsService;

    private final static Logger LOGGER = LoggerFactory.getLogger(BrokenLogsController.class);


    /**
     * 拆券
     * @return
     */
    @PostMapping
    public ResultResponse broken(@Valid @RequestBody ReceiveLogsParamVo brokenLogsReqVo) {
        return ResultResponse.success(brokenLogsService.brokenReceive(brokenLogsReqVo.getReceiveLogsId()));
    }

    /**
     * 根据receiv_log_id 查询拆券记录
     * @return
     */
    @GetMapping("/list/{id}")
    public ResultResponse list(@Valid @PathVariable("id") @NotNull(message = "查询领取编号不能为空") Long receiveLogsId) {
        return ResultResponse.success(brokenLogsService.selectListByReceiveLogsId(receiveLogsId));
    }
    /**
     * 分页查询拆券记录
     * @return
     */
    @GetMapping
    public ResultResponse pageList(PageDto page, BrokenLogsVo brokenLogsVo) {
        return ResultResponse.success(brokenLogsService.selectPage(page,brokenLogsVo.toDto(BrokenLogsDto.class)));
    }

    /**
     * 根据ID查询拆券记录
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultResponse selectById(@PathVariable("id") Long id) {
        return ResultResponse.success(brokenLogsService.selectById(id));
    }


}

