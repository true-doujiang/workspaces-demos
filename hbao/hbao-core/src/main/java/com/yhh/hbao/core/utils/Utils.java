package com.yhh.hbao.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final String REG_MOBILE = "1[23456789]\\d{9}";// 手机号正则表达试
    public static final String REG_TEL = "(0\\d{2,3}-?\\d{7,8}(-\\d{3,})?)|(400\\d{7})";// 验证座机
    public static final String REG_MAIL = "\\w+([\\-+\\.]\\w+)*@\\w+([\\-\\.]\\w+)*\\.\\w+([\\-\\.]\\w+)*";// 验证邮箱
    public static final int MILLISECOND = 0;
    public static final int SECOND = 1;
    public static final int MINUTE = 2;
    public static final int HOUR = 3;
    public static final int DAY = 4;

    /**
     * 获取专利授权年度
     *
     * @param apply_date
     * @param auth_date
     * @return
     */
    public static int getAuthYear(String apply_date, String auth_date) {
        if (!apply_date.matches("\\d{4}-\\d{2}-\\d{2}") || !auth_date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return 0;
        }
        int apply_year = Utils.parseInt(apply_date.substring(0, 4));
        int apply_month = Utils.parseInt(apply_date.substring(5, 7));
        int apply_day = Utils.parseInt(apply_date.substring(8, 10));
        int auth_year = Utils.parseInt(auth_date.substring(0, 4));
        int auth_month = Utils.parseInt(auth_date.substring(5, 7));
        int auth_day = Utils.parseInt(auth_date.substring(8, 10));

        if (apply_year == auth_year) {
            return 1;
        }
        if (auth_month > apply_month) {
            return auth_year - apply_year + 1;
        }
        if (auth_month == apply_month) {
            if (auth_day >= apply_day) {
                return auth_year - apply_year + 1;
            }
        }
        return auth_year - apply_year;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 返回不为空的字符串
     *
     * @param obj
     * @return
     */
    public static String getString(Object obj) {
        if (obj == null || obj.toString().equals("null")) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 返回不为空的字符串
     *
     * @param obj
     * @param def 默认值
     * @return
     */
    public static String getString(Object obj, String def) {
        if (obj == null || obj.toString().equals("null")) {
            return def;
        }
        return obj.toString().trim();
    }

    /**
     * 返回给定格式的服务器时间
     *
     * @param parrent String 默认返回yyyy-MM-dd HH:mm:ss格式的时间
     * @return
     */
    public static String getServerTime(String parrent) {
        if (parrent == null || parrent.equals("")) {
            parrent = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            Calendar c = Calendar.getInstance(Locale.CHINESE);
            SimpleDateFormat sformat = new SimpleDateFormat(parrent, Locale.CHINA);
            return sformat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return (new Timestamp(System.currentTimeMillis()) + "").substring(0, 19);
        }
    }

    /**
     * 格式化时间 date转string
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        try {
            SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            return sformat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回给定格式的服务器时间
     *
     * @param parrent String 默认返回yyyy-MM-dd HH:mm:ss格式的时间
     * @param field   the calendar field,eg:Calendar.WEEK_OF_YEAR
     * @param offset  the amount of date or time to be added to the field, eg:-2
     * @return
     */
    public static String getServerTime(String parrent, int field, int offset) {
        if (parrent == null || parrent.equals("")) {
            parrent = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            Calendar c = Calendar.getInstance(Locale.CHINESE);
            SimpleDateFormat sformat = new SimpleDateFormat(parrent);
            c.add(field, offset);
            return sformat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return (new Timestamp(System.currentTimeMillis()) + "").substring(0, 19);
        }
    }

    public static double parseDouble(Object str) {
        if (str == null) {
            return 0;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+(\\.\\d+)?")) {
            return 0;
        }
        return Double.parseDouble(s);
    }

    public static float parseFloat(Object str) {
        if (str == null) {
            return 0;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+(\\.\\d+)?")) {
            return 0;
        }
        return Float.parseFloat(s);
    }

    public static int parseInt(Object str) {
        return parseInt(str, 0);
    }

    public static int parseInt(Object str, int defaultValue) {
        if (str == null || str.equals("")) {
            return defaultValue;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+")) {
            return defaultValue;
        }
        return Integer.parseInt(s);
    }

    public static long parseLong(Object str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+")) {
            return 0;
        }
        return Long.parseLong(s);
    }

    /**
     * 左补字符0
     *
     * @param str
     * @param length
     * @return
     */
    public static String fill0Left(String str, int length) {
        if (str == null) {
            str = "";
        }
        int len = length - str.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                str = "0" + str;
            }
        }
        return str;
    }

    /**
     * Map 转成 String
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String toString(Map<?, ?> map) {
        if (map == null) {
            return "";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuffer buf = new StringBuffer();
        buf.append("{");

        Iterator<?> i = map.entrySet().iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            Entry<?, ?> e = (Entry<?, ?>) (i.next());
            Object key = e.getKey();
            Object value = e.getValue();
            buf.append((key == map ? "(this Map)" : key));

            buf.append("=");
            if (value == map) {
                buf.append("(this Map)");
            } else {
                if (value instanceof Object[]) {
                    Object[] objs = (Object[]) value;
                    buf.append("[");
                    for (int j = 0; j < objs.length; j++) {
                        if (j > 0) {
                            buf.append(", ");
                        }
                        buf.append(objs[j]);
                    }
                    buf.append("]");
                } else if (value instanceof Map) {
                    buf.append(toString((Map<?, ?>) value));
                } else {
                    buf.append(value);
                }
            }
            hasNext = i.hasNext();
            if (hasNext) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    /**
     * 是否早于当天
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public static boolean beforeToday(String date) {
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sformat.parse(date + " 00:00:00");
            Date now = sformat.parse(getServerTime("yyyy-MM-dd") + " 00:00:00");
            return d.before(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否晚于当天
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public static boolean afterToday(String date) {
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sformat.parse(date + " 00:00:00");
            Date now = sformat.parse(getServerTime("yyyy-MM-dd") + " 00:00:00");
            return d.after(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否是当天
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public static boolean isToday(String date) {
        return date.equals(getServerTime("yyyy-MM-dd"));
    }

    /**
     * 是否早于当前时间
     *
     * @param time HHmm
     * @return
     */
    public static boolean beforeNow(String time) {
        time = time.replace(":", "");
        if (time.length() == 4) {
            time = time + "00";
        }
        int now = Integer.parseInt(getServerTime("HHmmss"));
        if (Integer.parseInt(time) < now) {
            return true;
        }
        return false;
    }

    /**
     * 是否晚于当前时间
     *
     * @param time HHmm
     * @return
     */
    public static boolean afterNow(String time) {
        time = time.replace(":", "");
        if (time.length() == 4) {
            time = time + "00";
        }
        int now = Integer.parseInt(getServerTime("HHmmss"));
        if (Integer.parseInt(time) > now) {
            return true;
        }
        return false;
    }

    public static String toString(double d) {
        BigDecimal bd = new BigDecimal(d);
        String s = bd.toPlainString();
        String[] ss = s.split("\\.");
        if (ss.length == 2) {
            if (ss[1].length() > 10) {
                return ss[0] + "." + ss[1].substring(0, 10);
            }
        }
        return s;
    }

    public static double toDouble(double d) {
        BigDecimal bd = new BigDecimal(d);
        String s = bd.toPlainString();
        String[] ss = s.split("\\.");
        if (ss.length == 2) {
            if (ss[1].length() > 10) {
                return Double.parseDouble(ss[0] + "." + ss[1].substring(0, 10));
            }
        }
        return Double.parseDouble(s);
    }

    public static String getWeekStr(int week) {
        switch (week) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
        }
        return "未知[" + week + "]";
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String decode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String simpleDatetime(String datetime) {
        if (datetime == null) {
            return "";
        }
        if (datetime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            return datetime.substring(5, 16);
        } else if (datetime.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return datetime.substring(5, 10);
        } else if (datetime.matches("\\d{2}:\\d{2}:\\d{2}")) {
            return datetime.substring(0, 5);
        }
        return datetime;
    }

    public static String formatDate(String datetime) {
        if (datetime == null) {
            return "";
        }
        if (datetime.length() > 10) {
            return datetime.substring(0, 10);
        }
        return datetime;
    }

    /**
     * 当date2 >= date1 时返回true
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean matchDate(String date1, String date2) {
        if (date1 == null || date1.equals("")) {
            return true;
        }
        if (date2 == null || date2.equals("")) {
            return false;
        }
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sformat.parse(date1 + " 00:00:00");
            Date d2 = sformat.parse(date2 + " 00:00:00");
            return d1.equals(d2) || d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否是有效的手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return str.matches(REG_MOBILE);
    }

    /**
     * 是否是有效的座机
     *
     * @param str
     * @return
     */
    public static boolean isTelephone(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return str.matches(REG_TEL);
    }

    /**
     * 是否是有效的邮箱号
     *
     * @param str
     * @return
     */
    public static boolean isMail(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return str.matches(REG_MAIL);
    }

    public static String date(int y, int m, int d) {
        String date = y + "-";
        if (m < 10) {
            date += "0" + m + "-";
        } else {
            date += m + "-";
        }
        if (d < 10) {
            date += "0" + d;
        } else {
            date += d;
        }
        return date;
    }

    /**
     * 将字符串时间转换成日期格式
     *
     * @param  datetime 默认返回yyyy-MM-dd HH:mm:ss格式的时间
     * @return
     */
    public static Date toDate(String datetime) {
        if (datetime == null || datetime.length() < 0) {
            return null;
        }
        if (datetime.matches("\\d{14,}")) {
            datetime = datetime.substring(0, 4) + "-" + datetime.substring(4, 6) + "-" + datetime.substring(6, 8) + " " + datetime.substring(8, 10) + ":" + datetime.substring(10, 12) + ":" + datetime.substring(12, 14);
        } else if (datetime.matches("\\d{4}-\\d{2}-\\d{2}")) {
            datetime = datetime + " 00:00:00";
        } else if (datetime.matches("\\d{2}:\\d{2}:\\d{2}(.\\d+)?")) {
            datetime = getServerTime("yyyy-MM-dd") + " " + datetime;
        } else if (!datetime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(.\\d+)?")) {
            try {
                return new Date(datetime);
            } catch (Exception e) {
                System.err.println("Unparseable date: \"" + datetime + "\"");
                return null;
            }
        }

        try {
            SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sformat.parse(datetime);
        } catch (Exception e) {
            System.err.println("Unparseable date: \"" + datetime + "\"");
            return null;
        }
    }

    /**
     * 计算时间差 (时间单位,开始时间,结束时间) 调用方法
     *
     * @unit ：0-毫秒；1-秒；2-分；3-小时；4-天
     */
    public static long timeDiff(String time1, String time2, int unit) {
        return timeDiff(time1, time2, unit, true);
    }

    /**
     * 计算时间差 (时间单位,开始时间,结束时间) 调用方法
     *
     * @unit ：0-毫秒；1-秒；2-分；3-小时；4-天
     * @flag : true-返回绝对值,false-区分正分
     */
    public static long timeDiff(String time1, String time2, int unit, boolean flag) {
        // 单位(如：不足1天(24小时) 则返回0)，开始时间，结束时间
        Date date1 = toDate(time1);
        Date date2 = toDate(time2);
        long ltime = date2.getTime() - date1.getTime();
        if (flag) {
            ltime = Math.abs(ltime);
        }
        if (unit == SECOND) {
            return ltime / 1000;// 返回秒
        } else if (unit == MINUTE) {
            return ltime / 60000;// 返回分钟
        } else if (unit == HOUR) {
            return ltime / 3600000;// 返回小时
        } else if (unit == DAY) {
            return ltime / 86400000;// 返回天数
        } else {
            return ltime;// 毫秒
        }
    }

    public static String genRandomNum(int len) {
        // 35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 10;
        int i; // 生成的随机数
        int count = 0; // 生成的长度
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer num = new StringBuffer("");
        Random r = new Random();
        while (count < len) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
            if (i >= 0 && i < str.length) {
                num.append(str[i]);
                count++;
            }
        }
        return num.toString();
    }

    /**
     * 获取SN号码
     *
     * @param currentMaxSN 当前最大值SN码
     * @param firstPart    需要SN码第一部分
     * @param len          生产号码的长度(第二部分)
     * @return
     */
    public static String getMaxSN(String currentMaxSN, String firstPart, int len) {
        String initSN = "";
        if ("".equals(currentMaxSN) || null == currentMaxSN) {
            String initTwoPart = "";
            for (int i = 0; i < len; i++) {
                initTwoPart += "0";
            }
            initSN = firstPart + initTwoPart;
        } else {
            initSN = currentMaxSN;
        }
        int num = Integer.parseInt(initSN.replace(firstPart, ""));
        String numStr = ++num + "";
        int length = numStr.length();
        for (int i = length; i < len; i++) {
            numStr = "0" + numStr;
        }
        return firstPart + numStr;
    }

    /**
     * 获取SN号码
     *
     * @param firstPart 前缀
     * @param len       生产随机数
     * @return
     */
    public static String getSN(String firstPart, int len) {
        return firstPart + getServerTime("yyyyMMdd") + genRandomNum(len);
    }

    /**
     * 获取SN号码
     *
     * @param firstPart
     * @param format
     * @param len
     * @return
     */
    public static String getSN(String firstPart, String format, int len) {
        return firstPart + getServerTime(format) + genRandomNum(len);
    }

    public static String getFileStr(String[] files) {
        String fileStr = "";
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (i == 0) {
                    fileStr += files[i];
                } else {
                    fileStr += "," + files[i];
                }
            }
        }
        return fileStr;
    }

    /**
     * 获取文件JSON字符串
     *
     * @param fileList
     * @return
     */
    public static String getFileListStr(String[] fileList) {
        if (null != fileList) {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < fileList.length; i++) {
                arr.add(JSONObject.parse(fileList[i]));
            }
            return arr.toString();
        }
        return null;
    }

    /**
     * 人命币 分转换成元
     *
     * @param cent 分
     */
    public static String cent2Yuan(String cent) {
        if (null == cent || "".equals(cent)) {
            return "0";
        }
        String result = (float) (Math.round(Double.valueOf(cent) / 100 * 100)) / 100 + "";
        return result.substring(0, result.indexOf("."));
    }

    /**
     * 人命币 元转换成分
     *
     * @param yuan
     * @return
     */
    public static String yuan2Cent(String yuan) {
        if (null == yuan) {
            return "0";
        }
        return Math.round(Double.valueOf(yuan) * 100) + "";
    }

    public static String formatDateTime(String datetime) {
        if (datetime == null) {
            return "";
        }
        if (datetime.length() > 19) {
            return datetime.substring(0, 19);
        }
        return datetime;
    }

    public static String getDefaultPassword(int length) {
        String password = "";
        for (int i = 0; i < length; i++) {
            char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};
            password += String.valueOf(codeSeq[new Random().nextInt(codeSeq.length)]);// random.nextInt(10));
        }
        return password;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return true 是邮箱 false 不是邮箱
     */
    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            // String check =
            // "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(REG_MAIL);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 几天前
     *
     * @param day
     * @return
     */
    public static String getDateBefore(int day) {
        try {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            // SimpleDateFormat show_date_format = new SimpleDateFormat("d/M",
            // Locale.CHINA);
            now.setTime(sformat.parse(getServerTime("yyyy-MM-dd")));
            now.set(Calendar.DATE, now.get(Calendar.DATE) - day - 1);
            return sformat.format(now.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getServerTime("yyyy-MM-dd");
    }

    public static void main(String[] args) {
        System.out.println(getAuthYear("2014-02-10", "2014-03-10"));
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 格式化金额
     *
     * @param amount
     * @return
     */
    public static String getDecimalFormat(String amount) {
        double formatAmount = Utils.parseDouble(amount);
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("##,###");
        return decimalFormat.format(formatAmount);
    }

    /**
     * 生成code字符串
     *
     * @param prefix 编码前缀
     * @return
     */
    public static String getCode(String prefix) {
        String code = "";
        if (!StringUtils.isEmpty(prefix)) {
            code = getServerTime("yyyyMMddHHmmss");
        }
        return code;
    }


    /**
     * 校验是否为URL地址
     *
     * @param pInput 输入
     * @return 是否是URL地址
     */
    public static boolean isUrl(String pInput) {
        if (pInput == null) {
            return false;
        }
        String regEx = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return pInput.matches(regEx);
    }

    /**
     * 校验是否是hdfs地址
     *
     * @param fileSystemUrl hdfs地址
     * @return 是否是hdfs地址
     */
    public static boolean isHdfsUrl(String fileSystemUrl) {
        String regEx = "^(hdfs)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return fileSystemUrl.matches(regEx);
    }

    /**
     * 校验是否为空
     *
     * @param target 目标集合
     * @return 是否为NULL
     */
    public static boolean isBlank(Collection target) {
        return target == null || target.size() == 0;
    }

    /**
     * 校验是否为空
     *
     * @param target 目标对象
     * @return 是否为NULL
     */
    public static boolean isBlank(Object target) {
        return target == null;
    }

    /**
     * 校验是否为空
     *
     * @param target 目标对象
     * @return 是否为NULL
     */
    public static boolean isBlank(String target) {
        return target == null || target.length() == 0;
    }

    /**
     * 校验是否为NULL
     *
     * @param target 目标对象
     * @return 是否为NULL
     */
    public static boolean isNull(Object target) {
        return target == null;
    }

    /**
     * 校验是否为空
     *
     * @param target 目标map
     * @return 是否为NULL
     */
    public static boolean isBlank(Map target) {
        return target == null || target.size() == 0;
    }

    /**
     * 校验数组是否为空
     *
     * @param target 目标数组
     * @param <T>    泛型T
     * @return 是否为空
     */
    public static <T> boolean isBlank(T[] target) {
        return target == null || target.length == 0;
    }


    /**
     * 获取唯一的UUID编号值
     *
     * @return 唯一的UUID值
     */
    public static String getUniqueCode() {
        String uuidValue = UUID.randomUUID().toString();
        uuidValue = uuidValue.replaceAll("-", BLANK);
        return uuidValue;
    }

    /**
     * 计算获取日期 +：增加   -:减少
     *
     * @param targetTime 目标时间
     * @param year       年
     * @param month      月
     * @param day        日
     * @param hours      小时
     * @param minute     分钟
     * @param seconds    秒
     * @return 计算过后的时间
     */
    public static Date calculateDate(Date targetTime, int year, int month, int day, int hours, int minute, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetTime);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minute);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }


    /**
     * 将对象转换成string类型
     *
     * @param target 目标对象
     * @param <T>    任意对象
     * @return string
     */
    public static <T> String ObjectToString(T target) {
        if (Utils.isBlank(target)) return null;
        return String.valueOf(target);
    }


    /**
     * 判断当前时间是否在
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否当前时间在此时间间隔内
     */
    public static boolean isNotInterval(Integer startTime, Integer endTime) {
        if (startTime == null || endTime == null) return false;
        String hours_str = Utils.dateFormat(new Date(), "HH");
        int hours = Integer.parseInt(hours_str);
        //说明隔天
        if (endTime < startTime) {
            if (((startTime <= hours) && (hours <= 24)) || ((0 <= hours) && (hours <= endTime))) {
                return true;
            }
        } else {
            if (hours >= startTime && hours <= endTime) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否在当前时间的间隔小时数据
     *
     * @param transferDate 目标时间
     * @param compareDate  比较时间
     * @param minute       分钟
     * @return 比较结果
     */
    public static boolean compareToDate(Date transferDate, Date compareDate, int minute) {
        if (transferDate == null || compareDate == null) return false;
        Date nowCalculateDate = calculateDate(compareDate, 0, 0, 0, 0, minute, 0);
        return transferDate.compareTo(nowCalculateDate) >= 0;
    }


    public static boolean equalsDay(Date targetTime, Date equalsTime) {
        return !(targetTime == null || equalsTime == null)
                && dateFormat(targetTime, "yyyyMMdd").equals(dateFormat(equalsTime, "yyyyMMdd"));
    }

    /**
     * 判断是否为数字
     *
     * @param target
     * @return 是否为数字
     */
    public static boolean isNum(String target) {
        if (null == target || target.isEmpty()) {
            return false;
        }

        String regex = "^[0-9]+$";
        return target.matches(regex);
    }

    /**
     * 正则表达式验证日期格式（验证年月日）
     * ex: 2017-10-01 2017-1-1 2017-01-01 2017-01-1
     *
     * @param target 日期
     * @return 返回校验结果
     */
    public static boolean isDateContainsDay(String target) {
        if (Utils.isBlank(target)) return false;
        String regex = "^((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-" +
                "(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-" +
                "(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$";
        return target.matches(regex);
    }

    /**
     * 正则表达式验证日期格式（验证年月）
     * ex: 2017-10 2017-01 2017-1
     *
     * @param target 日期
     * @return 返回校验结果
     */
    public static boolean isDateContainsMonth(String target) {
        if (Utils.isBlank(target)) return false;
        String regex = "^((((19|20)\\d{2})-(0?(1|[3-9])|1[012]))|(((19|20)\\d{2})-(0?[13578]|1[02]))|(((19|20)\\d{2})-0?2)|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2))$";
        return target.matches(regex);
    }

    private static final DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 空数据
     */
    private static final String BLANK = "";


    private static Logger LOG = Logger.getLogger(Utils.class);


    /**
     * 日期格式化
     *
     * @param sourceDate    日期
     * @param formatPattern format 时间格式
     * @return 格式化日期数据
     */
    public static String dateFormat(Date sourceDate, String... formatPattern) {
        if (sourceDate == null) return null;
        if (formatPattern == null || formatPattern.length == 0) return defaultDateFormat.format(sourceDate);
        DateFormat dateFormat = new SimpleDateFormat(formatPattern[0]);
        return dateFormat.format(sourceDate);
    }

    /**
     * @param date          需要计算的日期
     * @param formatPattern 返回的日期格式
     * @param year          年
     * @param month         月
     * @param day           日
     * @param hours         时
     * @param minute        分
     * @param seconds       秒
     * @return
     */
    public static String getCurDate(String date, String formatPattern, int year, int month, int day, int hours, int minute, int seconds) {
        if (!isBlank(date)) {
            Date time = toDate(date);
            Date newDate = calculateDate(time, year, month, day, hours, minute, seconds);
            return dateFormat(newDate, formatPattern);
        } else {
            return null;
        }
    }

    /**
     * 获取模糊查询的地区码
     * @param areaCode 源地区码
     * @return 模糊地区码
     */
    public static String getLikeAreaCode(String areaCode) {
        //判断地区码是省，市还是区
        if (!Utils.isBlank(areaCode)) {
            if (areaCode.equals("100000")) {
                return null;
            } else if (Pattern.matches(".*0000", areaCode)) {
                //后4位位0 为省
                return areaCode.substring(0, 2)+"%";
            } else if (Pattern.matches(".*00", areaCode)) {
                //后2位位0 为市
                return areaCode.substring(0, 4)+"%";
            } else {
                //否则这位县
                return areaCode+"%";
            }
        }
        return null;
    }

}
