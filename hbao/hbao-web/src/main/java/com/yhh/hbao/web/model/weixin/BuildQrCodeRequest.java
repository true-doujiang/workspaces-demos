package com.yhh.hbao.web.model.weixin;

import lombok.Data;

import java.util.Map;

/**
 * package: com.weimob.voteplaza.core.service.model
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 10/13/17
 * creat_time: 5:14 PM
 **/
@Data
public class BuildQrCodeRequest {
    /****
     *
     * 跳转参数
     * 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     */
    private String scene;
    /****
     * 跳转URL
     * 必须是已经发布的小程序页面，例如 "pages/index/index" ,根路径前不要填加'/',不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     */
    private String page;
    /****
     * 二维码宽度
     */
    private Integer width;
    /****
     * 是否设置颜色
     */
    private Boolean auto_color;
    /****
     * 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
     * {"r":"xxx","g":"xxx","b":"xxx"}
     */
    private Map<String,Object> line_color;
}
