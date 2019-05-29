package com.yhh.hbao.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangjj.
 *
 * @DATE 2017/9/8 - 11:31
 * @company WeiMob
 * @description 数字校验工具类
 */
public class NumberValidationUtils {
    private static boolean isMatch(String regex, String target) {
        if (target == null || target.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(target);
        return isNum.matches();
    }

    public static boolean isPositiveInteger(String target) {
        return isMatch("^\\+{0,1}[1-9]\\d*", target);
    }

    public static boolean isNegativeInteger(String target) {
        return isMatch("^-[1-9]\\d*", target);
    }

    public static boolean isWholeNumber(String target) {
        return isMatch("[+-]{0,1}0", target) || isPositiveInteger(target) || isNegativeInteger(target);
    }

    /**
     * 是否是一个正数
     *
     * @param target 目标
     * @return
     */
    public static boolean isPositiveDecimal(String target) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", target);
    }

    /**
     * @param target 目标
     * @return 是否是一个负数
     */
    public static boolean isNegativeDecimal(String target) {
        return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", target);
    }

    /**
     * @param target 目标
     * @return 是否是一个浮点数
     */
    public static boolean isDecimal(String target) {
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", target);
    }

    /**
     * @param target 目标数字
     * @return 是否是一个完整的数字  负数 正数 浮点数
     */
    public static boolean isRealNumber(String target) {
        return isWholeNumber(target) || isDecimal(target);
    }

    public static void main(String[] args) {
        boolean decimal = isRealNumber("10");
        System.out.println(decimal);
    }
}
