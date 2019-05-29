package com.yhh.hbao.core.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yangjj.
 *
 * @DATE 2018/2/7 - 15:09
 * @company WeiMob
 * @description 时间操作的工具类
 */
public class DataUtils {

    /**
     * 计算间隔毫秒数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 间隔毫秒数
     */
    public static Long calculateTime(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) return 0L;
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 将毫秒时间转化成天 小时 分
     *
     * @param time 毫秒时间
     * @return 数据
     */
    public static String longTimeToDays(long time) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = time / (1000 * 60 * 60 * 24);
        Long hour = (time - day * dd) / (1000 * 60 * 60);
        Long minute = (time - day * dd - hour * hh) / (1000 * 60);
        StringBuilder dateStr = new StringBuilder();
        if (day > 0) {
            dateStr.append(day).append("天");
        }
        if (hour > 0) {
            dateStr.append(hour).append("小时");
        }
        if (minute > 0) {
            dateStr.append(minute).append("分");
        }
        return dateStr.toString();
    }


    public static boolean sameDate(Date d1, Date d2) {
        if (null == d1 || null == d2)
            return false;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal1.getTime().equals(cal2.getTime());
    }

}
