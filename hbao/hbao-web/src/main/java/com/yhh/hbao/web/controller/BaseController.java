package com.yhh.hbao.web.controller;

import com.yhh.hbao.core.model.ResultAndPage;
import com.yhh.hbao.web.model.ResultResponse;
import org.springframework.stereotype.Controller;


@Controller
public abstract class BaseController {

    /*****
     * 构建响应对象
     * @param result
     * @return
     */
    public ResultResponse buildResponse(Object result){
        if (result instanceof ResultAndPage) {
            ResultAndPage resultAndPage = (ResultAndPage) result;
            return ResultResponse.buildSuccJoinPage(resultAndPage.getPagination(), resultAndPage.getData());
        } else {
            return ResultResponse.success(result);
        }
    }
}
