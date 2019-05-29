package com.yhh.hbao.web.service;

import com.yhh.hbao.api.service.UserInfoService;
import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.constants.Constants;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.enums.GenderEnum;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.redis.RedisClientUtil;
import com.yhh.hbao.core.utils.HttpUtil;
import com.yhh.hbao.core.utils.WXBizDataCrypt;
import com.yhh.hbao.web.model.weixin.SessionKeyResponse;
import com.yhh.hbao.web.model.weixin.TokenInfo;
import com.yhh.hbao.web.model.weixin.WxUserInfo;
import com.yhh.hbao.web.utils.TokenUtils;
import com.alibaba.fastjson.JSON;
import com.yhh.hbao.web.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * package: com.weimob.voteplaza.service.impl
 * describe: 微信服务
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/20/17
 * creat_time: 11:36 AM
 **/
@Component
public class WeixinServiceImpl {

    /****
     *  apache log
     */
    private static final Logger LOGGER = Logger.getLogger(WeixinServiceImpl.class);
    /****
     * 微信小程序的用户端APPID
     */
    @Value("${weixin.toc.app.id}")
    private String tocAppId;
    /****
     * 微信小程序的用户端APP secret
     */
    @Value("${weixin.toc.app.secret}")
    private String tocAppSecret;


    @Value("${weixin.api.app.uri}")
    private String apiUrl;

    @Value("${weixin.api.grant_type}")
    private String grantType;


    @Autowired
    private RedisClientUtil redisClientUtil;

    @Autowired
    private UserInfoService userInfoService;

    /*****
     * 登录
     * @param
     * @return
     */
    public Map<String,Object> login(LoginVo loginVo) {
        String encryptedData = loginVo.getEncryptedData();
        String iv = loginVo.getIv();

        //1:获取微信JsCode 信息
        //SessionKeyResponse keyResponse  = this.getJsCode2Session(jsCode);
        SessionKeyResponse keyResponse  = new SessionKeyResponse();

        //用前端传进来的
        SessionKeyResponse sessionKeyResponse = loginVo.getSessionKeyResponse();
        if(null==sessionKeyResponse){
            throw new CouponBizException(CouponBizExceptionEnum.AUTH_USER_LOGIN_PARAM_IS_NULL);
        }
        keyResponse.setUnionid(sessionKeyResponse.getUnionid());
        keyResponse.setOpenid(sessionKeyResponse.getOpenid());
        keyResponse.setSession_key(sessionKeyResponse.getSession_key());

        //若传进来的openId数据库中没有就用我保存到数据库
        WxUserInfo wxUserInfo = loginVo.getWxUserInfo();

        //2:获取票据信息
        TokenInfo tokenInfo = buildToken(encryptedData,iv,keyResponse, wxUserInfo);
        Map<String,Object> resultMap = new HashMap<>();

        //3:封装响应数据
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(tokenInfo.getUserInfoDto().getId());
        userInfoDto.setGender(tokenInfo.getUserInfoDto().getGender());
        userInfoDto.setHeadIcon(tokenInfo.getUserInfoDto().getHeadIcon());
        userInfoDto.setNickName(tokenInfo.getUserInfoDto().getNickName());
        userInfoDto.setUseLanguage(tokenInfo.getUserInfoDto().getUseLanguage());
        resultMap.put("AUTHORIZATION_KEY",tokenInfo.getToken());
        resultMap.put("expirationTime",tokenInfo.getExpirationTime());
        resultMap.put("userInfo",userInfoDto);
        //4:响应数据
        return resultMap;
    }

    public SessionKeyResponse getJsCode2Session(String code) {
        StringBuffer stringBuffer = new StringBuffer(apiUrl);
        stringBuffer.append("/sns/jscode2session?");
        Map<String,String> paramMap = new HashMap<>();
        stringBuffer.append("appid=").append(tocAppId);
        stringBuffer.append("&secret=").append(tocAppSecret);
        stringBuffer.append("&js_code=").append(code);
        stringBuffer.append("&grant_type=authorization_code");
        SessionKeyResponse response = HttpUtil.doGet(stringBuffer.toString(),null,SessionKeyResponse.class);
        if(StringUtils.isEmpty(response.getOpenid()) || StringUtils.isEmpty(response.getSession_key())){
            throw new CouponBizException(CouponBizExceptionEnum.WX_INVOKER_IS_ERROR);
        }
        return response;
    }

    /****
     * 获取用户信息
     * @param keyResponse
     * @return
     */
    private TokenInfo buildToken(String encryptedData, String iv, SessionKeyResponse keyResponse, WxUserInfo wxUserInfo) {
        TokenInfo tokenInfo = null;
        LOGGER.error("data:" + JSON.toJSONString(keyResponse));
        if (null != keyResponse
                && StringUtils.isNotBlank(keyResponse.getOpenid())
                && StringUtils.isNotBlank(keyResponse.getSession_key())) {

            tokenInfo = new TokenInfo();
            // 1:根据第三方OpenID 查询是否存在用户
            UserInfoDto userInfoDto = userInfoService.selectByOpenId(keyResponse.getOpenid());
            //用户状态非正常
            if (null != userInfoDto && userInfoDto.getStatus() == 1) {
                throw new CouponBizException(CouponBizExceptionEnum.AUTH_USER_LOGIN_IS_ERROR);
            }

            if (null == userInfoDto) {
                if (StringUtils.isNotEmpty(encryptedData) && StringUtils.isNotEmpty(iv)) {
                    //WxUserInfo wxUserInfo = this.decryptData(encryptedData, iv, keyResponse);
                    if(null==wxUserInfo){
                        throw new CouponBizException(CouponBizExceptionEnum.AUTH_USER_LOGIN_PARAM_IS_NULL);
                    }
                    userInfoDto = addInitUser(wxUserInfo);
                } else {
                    //用户信息为空,且无法解析用户数据
                    throw new CouponBizException(CouponBizExceptionEnum.AUTH_USER_LOGIN_IS_ERROR);

                }
            }
            String token = TokenUtils.buildToken(userInfoDto);
            tokenInfo.setToken(token);
            tokenInfo.setSessionKey(keyResponse.getSession_key());
            tokenInfo.setUserInfoDto(userInfoDto);
            writeRedisDB(tokenInfo);
        }

        return tokenInfo;
    }

    /**
     * method_name: getAccessToken
     * param:
     * describe: 获取AccessToken
     * creat_user: yhh
     * e-mail: yhh@weimob.com
     * creat_date: 9/20/17
     * creat_time: 2:17 PM
     **/
    private String getAccessToken(Boolean flag) throws Exception {
        String accessToken = null;
        if(!redisClientUtil.exists(Constants.WEIXIN_APPLICATION_ACCESS_TOEKN_KEY)){
            StringBuffer stringBuffer = new StringBuffer(apiUrl);
            stringBuffer.append("/cgi-bin/token");
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid",tocAppId);
            paramMap.put("secret",tocAppSecret);
            paramMap.put("grant_type","client_credential");
            ;
            Map<String,Object> resultMap = HttpUtil.doGet(stringBuffer.toString(),paramMap,HashMap.class);
            if(null!=resultMap&&resultMap.containsKey("access_token")){
                accessToken = String.valueOf(resultMap.get("access_token"));
                Integer expiresIn = (Integer) resultMap.get("expires_in");
                redisClientUtil.set(Constants.WEIXIN_APPLICATION_ACCESS_TOEKN_KEY,accessToken,expiresIn/2);
            }else if(null!=resultMap&&null!=resultMap.get("errcode")){
                LOGGER.error( "xcx:获取AccessToken------->request:"+JSON.toJSONString(paramMap)+",response:"+JSON.toJSONString(resultMap));
                //如果不是参数错误，则重试获取
                if(String.valueOf(resultMap.get("errmsg")).indexOf("invalid")!=-1&&flag){
                    return  getAccessToken(false);
                }
            }

        }else{
            accessToken = redisClientUtil.get(Constants.WEIXIN_APPLICATION_ACCESS_TOEKN_KEY);
        }
        return accessToken;
    }

    /****
     * 解密数据
     * @param encryptedData
     * @param iv
     * @param keyResponse
     * @return
     */
    public WxUserInfo decryptData(String encryptedData, String iv, SessionKeyResponse keyResponse) {
        WXBizDataCrypt biz = new WXBizDataCrypt(tocAppId, keyResponse.getSession_key());
        String data = biz.decryptData(encryptedData,iv);

        WxUserInfo wxUserInfo = JSON.parseObject(data,WxUserInfo.class);
        wxUserInfo.setOpenId(keyResponse.getOpenid());
        return wxUserInfo;
    }



    /**
     * 获取httpClient连接
     *
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    @SuppressWarnings("deprecation")
    private static DefaultHttpClient getHttpClient(String key) throws NoSuchAlgorithmException, KeyManagementException {
        // 协议注册
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { new HttpsX509TrustManager() }, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        // 允许所有主机的验证
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // 设置http https支持
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, ssf));
        // 连接池创建
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // 设置最大连接数
        cm.setMaxTotal(600);
        /**
         * 设置每个路由默认连接个数 DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
         * MaxtTotal=100 DefaultMaxPerRoute=50
         * 而我只连接到http://life.baidu.com时，到这个主机的并发最多只有50；而不是100；
         * 而我连接到http://life.baidu.com 和
         * http://baifubao.com时，到每个主机的并发最多只有50；即加起来是100（但不能超过100）
         **/
        cm.setDefaultMaxPerRoute(100);
        BasicHttpParams param = new BasicHttpParams();
        // 设置请求超时时间
        param.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
        // 设置等待数据超时时间
        param.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
        // 在提交请求之前 测试连接是否可用
        param.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);

        return new DefaultHttpClient(cm, param);
    }
    /**
     * 缺省的认证处理
     *
     */
    private static class HttpsX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }



    /****
     * 写入Redis数据库
     * @param tokenInfo
     */
    private void writeRedisDB(TokenInfo tokenInfo) {

        if(redisClientUtil.exists(tokenInfo.getToken())){
            redisClientUtil.del(tokenInfo.getToken());
        }
        tokenInfo.setExpirationTime(Constants.DEFAULT_WX_AUTH_REIDS_EXPIRE_TIME);
        redisClientUtil.set(tokenInfo.getToken(), JSON.toJSONString(tokenInfo), Constants.REDIS＿NXXX,Constants.REDIS＿EXPX,Constants.DEFAULT_WX_AUTH_REIDS_EXPIRE_TIME);
    }




    /****
     *  初始化用户信息
     * @param wxUserInfo
     * @return
     */
    private UserInfoDto addInitUser(WxUserInfo wxUserInfo){
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setGender(GenderEnum.getCode(wxUserInfo.getGender()));
        userInfoDto.setHeadIcon(wxUserInfo.getAvatarUrl());
        userInfoDto.setLastLoginTime(new Date());
        userInfoDto.setNickName(wxUserInfo.getNickName());
        userInfoDto.setUseLanguage(wxUserInfo.getLanguage());
        userInfoDto.setOpenId(wxUserInfo.getOpenId());
        userInfoDto.setUnionId(wxUserInfo.getUnionId());
        userInfoDto.setRegisterTime(new Date());
        userInfoDto.setCity(wxUserInfo.getCity());
        userInfoDto.setCountry(wxUserInfo.getCountry());

        userInfoDto.setProvince(wxUserInfo.getProvince());
        Long userId = userInfoService.insert(userInfoDto);
        if(null!=userId){
            userInfoDto.setId(userId);
            return userInfoDto;
        }
        return null;
    }
}

