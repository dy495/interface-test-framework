package com.haisheng.framework.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTimeUtil {
    public String mondayDateStr(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp);
        int dayOfWeek = dateTime.getDayOfWeek();
        return dateTime.minusDays(dayOfWeek - 1).toString("yyyyMMdd");
    }

    public String monthDateStr(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp);
        int dayOfWeek = dateTime.getDayOfMonth();
        return dateTime.minusDays(dayOfWeek - 1).toString("yyyyMM");
    }

    public String getHistoryDate(int num_days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num_days);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public Long getHistoryDateTimestamp(int num_days) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, num_days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public String getHistoryDate(String baseDay, int num_days) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            cal.setTime(sdf.parse(baseDay));
            cal.add(Calendar.DATE, num_days);
            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
     * patten: HH:mm:ss 24 hour
     * patten: hh:mm:ss 12 hour
     * plusTime: hh:mm:ss
     */
    public String getHistoryDate(String patten, String baseTime, String plusTime) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            Calendar cal = Calendar.getInstance();
            cal.get(Calendar.HOUR_OF_DAY);

            cal.setTime(sdf.parse(baseTime));
            String[] hms = plusTime.split(":");
            int hour = Integer.parseInt(hms[0]);
            int minute = Integer.parseInt(hms[1]);
            int second = Integer.parseInt(hms[2]);

            if (hour != 0) {
                cal.add(Calendar.HOUR_OF_DAY, hour);
            }
            if (minute != 0) {
                cal.add(Calendar.MINUTE, minute);
            }
            if (second != 0) {
                cal.add(Calendar.SECOND, second);
            }

            return sdf.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getHistoryDay(int num_days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num_days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        return Integer.parseInt(sdf.format(cal.getTime()));
    }

    public int getHistoryWeek(int num_weeks) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, num_weeks);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);

        return Integer.parseInt(year + "" + week);

    }

    public int getHistoryMonth(int num_months) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, num_months);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        return Integer.parseInt(sdf.format(cal.getTime()));
    }

    public int getHistorYear(int num_years) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, num_years);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        return Integer.parseInt(sdf.format(cal.getTime()));
    }

    /**
     * @param date "yyyy/MM/dd HH:mm:ss:SSS"
     */
    public String dateToTimestamp(String date) throws Exception {
        return String.valueOf(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").parse(date).getTime());
    }

    /**
     * @param date "yyyy/MM/dd HH:mm:ss:SSS"
     */
    public String dateToTimestamp(String pattern, String date) throws Exception {
        return String.valueOf(new SimpleDateFormat(pattern).parse(date).getTime());
    }

    public String changeTimePattern(String timeStr, String pattern) {
        Date time = new Date(timeStr);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(time);
    }

    public String linuxDateToTimestamp(String timeStr) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        return String.valueOf(sdf1.parse(timeStr).getTime());
    }

    public String getTimestampDistance(long from, long to) {
        long diff = 0;

        if (from == to) {
            return "00:00:00";
        } else if (from < to) {
            diff = to - from;
        } else {
            diff = from - to;
        }


        int day = (int) (diff / (24 * 60 * 60 * 1000));
        int hour = (int) (diff / (60 * 60 * 1000) - day * 24);
        int min = (int) ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        int sec = (int) (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        String sep = ":";
        return hour + sep + min + sep + sec;
    }

    public long getTimestampDiff(long from, long to) {
        long diff = 0;

        if (from < to) {
            diff = to - from;
        } else {
            diff = from - to;
        }

        return diff;

    }

    public String getHourBegin(int index) throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay() + index;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour + ":00:00:000";
        return dt.dateToTimestamp(time);
    }

    public String getHourEnd(int index) throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay() + index;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour + ":59:59:000";
        return dt.dateToTimestamp(time);
    }

    public String getCurrentMinuteEnd() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        int minute = dateTime.getMinuteOfHour() + 1;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour + ":" + minute + ":00:000";
        return dt.dateToTimestamp(time);
    }

    public String getCurrentHourMinutesSec() {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        String seperator = ":";

        return hour + seperator + minute + seperator + sec;

    }

    public String getCurrentHour() {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        return String.valueOf(hour);

    }

    public String getCurrentHour(int index) {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY) + index;

        return String.valueOf(hour);

    }

    public String getHourMinutesSec(long timestampMs) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampMs);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        String seperator = ":";

        return hour + seperator + minute + seperator + sec;

    }

    public String timestampToDate(String pattern, long timestampMs) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampMs);

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(calendar.getTime());

    }

    public long initDateByDay() {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis();

        return today;
    }


    public long calTimeDiff(String left, String right) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        java.util.Date leftVal = df.parse(left);

        java.util.Date rightVal = df.parse(right);

        long l = rightVal.getTime() - leftVal.getTime();

        long day = l / (24 * 60 * 60 * 1000);

        long hour = (l / (60 * 60 * 1000) - day * 24);

        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        long secondDiff = min * 60 + s;

        System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
        return secondDiff;
    }

    public int calTimeHourDiff(String left, String right) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        java.util.Date leftVal = df.parse(left);

        java.util.Date rightVal = df.parse(right);

        long l = rightVal.getTime() - leftVal.getTime();


        Long min = ((l / (60 * 1000)));
        CommonUtil.valueView(min + "分");
        return min.intValue();
    }

    public Timestamp currentDateToTimestamp() throws ParseException {
        java.util.Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateTime = df.format(date);
        java.util.Date dateF = df.parse(dateTime);

        return new java.sql.Timestamp(dateF.getTime());

    }

    public Timestamp changeDateToSqlTimestamp(long timestampMs) throws ParseException {
        java.util.Date date = new Date(timestampMs);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateTime = df.format(date);
        java.util.Date dateF = df.parse(dateTime);

        return new java.sql.Timestamp(dateF.getTime());

    }

    public long initLastWeek() throws Exception {

        String pattern = "yyyy/MM/dd HH:mm:ss:SSS";
        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();

        String todayStr = dateTimeUtil.timestampToDate(pattern, dateTimeUtil.initDateByDay());

        Date today = format.parse(todayStr);

        c.setTime(today);
        c.add(Calendar.DATE, -7);
        Date w = c.getTime();
        String latWeek = format.format(w);
        String lastWeekTimestamp = dateTimeUtil.dateToTimestamp(latWeek);

        return Long.parseLong(lastWeekTimestamp);
    }

    public Long initLastMonth() throws Exception {
        String pattern = "yyyy/MM/dd HH:mm:ss:SSS";
        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();

        String todayStr = dateTimeUtil.timestampToDate(pattern, dateTimeUtil.initDateByDay());

        Date today = format.parse(todayStr);

        c.setTime(today);
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        String lastMonTimestamp = dateTimeUtil.dateToTimestamp(mon);

        return Long.valueOf(lastMonTimestamp);
    }

    public long initLastYear() throws Exception {
        String pattern = "yyyy/MM/dd HH:mm:ss:SSS";
        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();

        String todayStr = dateTimeUtil.timestampToDate(pattern, dateTimeUtil.initDateByDay());

        Date today = format.parse(todayStr);

        c.setTime(today);
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String lastYear = format.format(y);
        String latYearTimestamp = dateTimeUtil.dateToTimestamp(lastYear);

        return Long.valueOf(latYearTimestamp);
    }

    public boolean isWeekend(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }

        return false;
    }

    public String getBeginDayOfWeek(Date date, String pattern) {
//        "yyyy-MM-dd"

        DateFormat sdf = new SimpleDateFormat(pattern);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }

        cal.add(Calendar.DATE, 2 - dayOfWeek);

        return sdf.format(cal.getTime());
    }

    public String getEndDayOfWeek(Date date, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(getBeginDayOfWeek(date, pattern)));

        cal.add(Calendar.DAY_OF_WEEK, 6);

        return sdf.format(cal.getTime());

    }

    public String getBeginDayOfMonth(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();

        cal.set(getNowYear(date), getNowMonth(date) - 1, 1);

        return sdf.format(cal.getTime());
    }

    public String getEndDayOfMonth(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();

        cal.set(getNowYear(date), getNowMonth(date) - 1, 1);

        int day = cal.getActualMaximum(5);
        cal.set(getNowYear(date), getNowMonth(date) - 1, day);
        return sdf.format(cal.getTime());
    }

    public int getNowYear(Date date) {

        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);

        return gc.get(1);
    }

    public int getNowMonth(Date date) {

        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);

        return gc.get(2) + 1;
    }

    public String getHHmm(int n) throws ParseException {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, n);// n分钟之前/之后的时间
        Date beforeD = beforeTime.getTime();
        String before = new SimpleDateFormat("HH:mm").format(beforeD);
        return before;
    }

    public long get0OclockStamp(int n) throws ParseException { //前第n天的0点时间戳
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = c.getTime();
        String day = df.format(date);
        Date date2 = df.parse(day);
        long endtime = date2.getTime();
        return endtime;
    }

    /**
     * @description: 获取某年的某月共有多少天
     * @author: liao
     * @time:
     */
    public int daysOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();

        c.set(year, month, 0);

        int days = c.get(Calendar.DAY_OF_MONTH);

        return days;
    }

    /**
     * @description: 获取当前年
     * @author: liao
     * @time:
     */
    public int getThisYear() {

        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);

        return year;
    }

    /**
     * @description: 获取当前月
     * @author: liao
     * @time:
     */
    public int getThisMonth() {
        Calendar c = Calendar.getInstance();

        int month = c.get(Calendar.MONTH) + 1;

        return month;
    }

    /**
     * @description: 获取当前日
     * @author: liao
     * @time:
     */
    public int getdayOfThisMonth() {
        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DATE);

        return day;
    }

    /**
     * @description: 获取当前天的前或后n天
     * @author: liao
     * @time:
     */
    public int getDaysMinusPlusInt(int n) {

        String day = "";

        if (n > 0) {
            day = LocalDate.now().plusDays(n).toString();
        } else {

            day = LocalDate.now().minusDays(Math.abs(n)).toString();
        }

        return Integer.valueOf(day.substring(day.length() - 2));
    }

    public String getDaysMinusPlusStr(int n) {

        String day = "";

        if (n > 0) {
            day = LocalDate.now().plusDays(n).toString();
        } else {

            day = LocalDate.now().minusDays(Math.abs(n)).toString();
        }

        return day;
    }

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
     * 当前时间的前后多少秒
     *
     * @description :
     * @date :2020/8/14 16:43
     **/
    public String currentTimeB(String pattern, int time) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();

        Date today = new Date();
        c.setTime(today);
        c.add(Calendar.SECOND, time);
        Date m = c.getTime();

        return format.format(m);
    }

}
