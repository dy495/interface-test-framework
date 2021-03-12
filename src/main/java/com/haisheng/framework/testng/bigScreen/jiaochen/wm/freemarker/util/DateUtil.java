package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 将一个时间日期格式化为指定格式
     *
     * @param date      时间
     * @param formatStr 格式字符串
     * @return String
     */
    public static String getFormat(Date date, String formatStr) {
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);
        return sf.format(date);
    }

    /**
     * 生成格式化时间，默认使用模板：yyyy-MM-dd
     *
     * @param date 日期时间对象
     * @return String 格式化后的时间信息
     */
    public static String getFormat(Date date) {
        String formatStr = "yyyy-MM-dd";
        return getFormat(date, formatStr);
    }

    /**
     * 给指定日期增加若干天
     *
     * @param date 日期
     * @param i    天数
     * @return 最后的日期
     */
    public static Date addDay(Date date, int i) {
        long curr = date.getTime();
        curr += (long) i * 24 * 60 * 60 * 1000;
        return new Date(curr);
    }

    /**
     * 给指定日期增加若干秒
     *
     * @param date 日期
     * @param s    秒数
     * @return 最后的日期
     */
    public static Date addSecond(Date date, int s) {
        long curr = date.getTime();
        curr += (long) s * 1000;
        return new Date(curr);
    }

    /**
     * 给指定日期增加若干天并装换格式
     *
     * @param date 日期
     * @param i    天数
     * @return 最后的日期
     */
    public static String addDayFormat(Date date, int i) {
        Date newDate = addDay(date, i);
        return getFormat(newDate);
    }

    /**
     * 将时间转为unix时间戳
     *
     * @param str 时间字符串
     * @return String 单位ms
     */
    public static String dateToStamp(String str) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return dateToStamp(str, format);
    }

    public static String dateToStamp(String str, String format) {
        String res = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(str);
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将unix时间戳转为时间
     *
     * @param stamp 时间戳
     * @return String
     */
    public static String stampToDate(String stamp) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return stampToDate(stamp, format);
    }

    /**
     * 将时间转为unix时间戳
     *
     * @param stamp  时间戳
     * @param format 格式
     * @return String
     */
    public static String stampToDate(String stamp, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(stamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取当前时间属于本月第几天
     *
     * @param data 日期
     * @return 第几天
     */
    public static Integer getDayOnMonth(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
