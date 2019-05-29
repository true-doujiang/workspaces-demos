package com.yhh.hbao.web.vo;

import com.yhh.hbao.web.model.weixin.SessionKeyResponse;
import com.yhh.hbao.web.model.weixin.WxUserInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登录请求Vo
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-19 上午9:53
 **/
@Data
public class LoginVo implements Serializable {

    @NotNull(message = "微信code不能为空")
    private String code;

    private String encryptedData;

    private String iv;





    private SessionKeyResponse sessionKeyResponse;

    private WxUserInfo wxUserInfo;
}