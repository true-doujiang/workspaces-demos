package com.yhh.hbao.core.validator;


import com.yhh.hbao.core.exception.RRException;
import org.springframework.util.StringUtils;

/**
 * 类Assert的功能描述:
 * 数据校验
 *
 * @author tqj
 * @date 2017-08-25 16:18:34
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
}
