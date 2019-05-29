package com.yhh.hbao.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类DateUtils的功能描述:
 * 日期处理
 *
 * @author tqj
 * @date 2017-08-25 16:12:36
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_TIME_PATTERN_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long differentDays(Date date1, Date date2) {
        long times = weeHours(date2, 0).getTime() - weeHours(date1, 0).getTime();
        long days = times / (1000 * 3600 * 24);
        return Math.abs(days);
    }




    public static void main(String[] args) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN_SSS);
        Date max = df.parse("2020-03-22 22:24:17");
        Date min = df.parse("2018-03-22 12:24:17");

        long day = DateUtils.differentDays(new Date().compareTo(min) >= 0 ? new Date() : min, max);
        System.out.println(day);
    }


    /**
     * 凌晨
     * @param date
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     *       1 返回yyyy-MM-dd 23:59:59日期
     * @return
     */
    public static Date weeHours(Date date, int flag) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //时分秒（毫秒数）
        long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);

        if (flag == 0) {
            return cal.getTime();
        } else if (flag == 1) {
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
        }
        return cal.getTime();
    }


    /**
     * 凌晨
     *
     * @param date
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00.000日期<br>
     * 1 返回yyyy-MM-dd 23:59:59.000日期
     */
    public static Date weeHoursSSS(Date date, int flag) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df2 = new SimpleDateFormat(DATE_TIME_PATTERN_SSS);
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        String dateStr = df.format(date);
        if (flag == 0) {
            //凌晨00:00:00.000
            try {
                return df2.parse(dateStr + " 00:00:00.000");
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else if (flag == 1) {
            //凌晨23:59:59.000
            try {
                return df2.parse(dateStr + " 23:59:59.000");
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }



}
