package com.yhh.hbao.web.model.weixin;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * package: com.weimob.voteplaza.service.model
 * describe: 推送微信模板消息
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/20/17
 * creat_time: 2:23 PM
 **/
@Data
public class PushWeixinMessageRequest implements Serializable {

    /****
     *  到达用户ID
     */
    private String touser;
    /****
     * 模板ID
     */
    private String template_id;
    /****
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
     */
    private String page;
    /****
     * 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     */
    private String form_id;
    /****
     * 模板内容，不填则下发空模板
     */
    private Map<String,Keyword> data;
    /****
     * 模板内容字体的颜色，不填默认黑色
     */
    private String color;
    /****
     * 模板需要放大的关键词，不填则默认无放大
     */
    private String emphasis_keyword;
}
