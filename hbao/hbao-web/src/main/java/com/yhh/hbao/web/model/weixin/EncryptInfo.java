package com.yhh.hbao.web.model.weixin;

import com.yhh.hbao.core.utils.AesCBC;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 *
 * 加密数据解密算法
 接口如果涉及敏感数据（如wx.getUserInfo当中的 openId 和unionId ），接口的明文内容将不包含这些敏感数据。开发者如需要获取敏感数据，需要对接口返回的加密数据( encryptedData )进行对称解密。 解密算法如下：

 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充。
 对称解密的目标密文为 Base64_Decode(encryptedData)。
 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
 微信官方提供了多种编程语言的示例代码（点击下载）。每种语言类型的接口名字均一致。调用方式可以参照示例。

 另外，为了应用能校验数据的有效性，会在敏感数据加上数据水印( watermar
 * package: com.weimob.voteplaza.core.service.model
 * describe: 微信解密请求
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 10/13/17
 * creat_time: 6:28 PM
 **/
@Data
public class EncryptInfo implements Serializable{

    private static final Logger LOGGER = Logger.getLogger(EncryptInfo.class);

    /****
     * session Key
     */
    private String sessionKey;

    private String encryptedData;

    private String iv;

    /****
     * 解密数据
     * @return
     * @throws Exception
     */
    public String decryptData() throws Exception {
        if(StringUtils.isNotEmpty(this.getEncryptedData())&&
                StringUtils.isNotEmpty(this.getSessionKey())&&
                StringUtils.isNotEmpty(this.getIv())){
            byte[] data = Base64.decodeBase64(this.getEncryptedData());
            byte[] sessionKey = Base64.decodeBase64(this.getSessionKey());
            byte[] iv = Base64.decodeBase64(this.getIv());
            AesCBC aes = new AesCBC();
            byte[] resultByte = null;
            try{
                resultByte = aes.decryptOfDiyIV(data,sessionKey,iv);
            }catch (Exception e){
                LOGGER.error("EncryptInfo ERROR:"+ JSON.toJSONString(this));
            }
            if(null==resultByte){
                return null;
            }

            return new String(resultByte);

        }
        return null;

    }

}
