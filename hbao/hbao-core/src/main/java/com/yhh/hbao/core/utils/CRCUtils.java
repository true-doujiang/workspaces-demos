package com.yhh.hbao.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

/**
 * Created by yangjj.
 *
 * @DATE 2017/5/22 - 14:37
 * @company WeiMob
 * @description 一致性 hash工具类
 */
public class CRCUtils {
    public static Long crc32(String crcKey) {
        try {
            CRC32 crc32 = new CRC32();
            crc32.update(crcKey.getBytes("utf-8"));
            return crc32.getValue();
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(CRCUtils.class, "设置crc 32的编码错误", e);
        } catch (Exception e) {
            LogUtils.error(CRCUtils.class, "设置crc32未知错误", e);
        }
        return null;
    }


    public static Long crc32Mod(String crcKey, int mod) {
        Long value = crc32(crcKey);
        if (value != null) {
            return value % mod;
        }
        return 0L;
    }


    public static void main(String[] args) {
        Long result = crc32Mod("585", 16);
        System.out.println(result);
    }
}
