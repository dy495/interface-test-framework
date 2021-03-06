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
        return dateTime.minusDays(dayOfWeek - 1).toString("yyyy-MM");
    }

    public String getHistoryDate(int num_days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num_days);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public String getHistoryDate1(int num_days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num_days);
        return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
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

    public String dateToTimestamp1(String date) throws Exception {
        return String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(date).getTime());
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
        long diff;

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

        return c.getTimeInMillis();
    }

    public long calTimeDiff(String left, String right) throws ParseException {
        String format = "HH:mm:ss";
        long l = timeDiff(left, right, format);
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long secondDiff = min * 60 + s;
        System.out.println("" + day + "???" + hour + "??????" + min + "???" + s + "???");
        return secondDiff;
    }

    public int calTimeHourDiff(String left, String right) throws ParseException {
        String format = "HH:mm";
        long l = timeDiff(left, right, format);
        long min = (l / (60 * 1000));
        System.out.println(min + "???");
        return (int) min;
    }

    public int calTimeDayDiff(String left, String right) throws ParseException {
        String format = "yyyy-MM-dd";
        long l = timeDiff(left, right, format);
        long day = l / (24 * 60 * 60 * 1000);
        return (int) day;
    }

    public long timeDiff(String left, String right, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date leftDate = df.parse(left);
        Date rightDate = df.parse(right);
        return rightDate.getTime() - leftDate.getTime();
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

        return Long.parseLong(latYearTimestamp);
    }

    public boolean isWeekend(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
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

        int day = cal.getActualMaximum(Calendar.DATE);
        cal.set(getNowYear(date), getNowMonth(date) - 1, day);
        return sdf.format(cal.getTime());
    }

    public int getNowYear(Date date) {

        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);

        return gc.get(Calendar.YEAR);
    }

    public int getNowMonth(Date date) {

        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);

        return gc.get(Calendar.MONTH) + 1;
    }

    public String getHHmm(int n) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, n);// n????????????/???????????????
        Date beforeD = beforeTime.getTime();
        return new SimpleDateFormat("HH:mm").format(beforeD);
    }

    public String getHHmm(int n, String pattern) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, n);// n????????????/???????????????
        Date beforeD = beforeTime.getTime();
        return new SimpleDateFormat(pattern).format(beforeD);
    }

    public long get0OclockStamp(int n) throws ParseException { //??????n??????0????????????
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = c.getTime();
        String day = df.format(date);
        Date date2 = df.parse(day);
        return date2.getTime();
    }

    /**
     * @description: ????????????????????????????????????
     * @author: liao
     * @time:
     */
    public int daysOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();

        c.set(year, month, 0);

        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @description: ???????????????
     * @author: liao
     * @time:
     */
    public int getThisYear() {

        Calendar c = Calendar.getInstance();

        return c.get(Calendar.YEAR);
    }

    /**
     * @description: ???????????????
     * @author: liao
     * @time:
     */
    public int getThisMonth() {
        Calendar c = Calendar.getInstance();

        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * @description: ???????????????
     * @author: liao
     * @time:
     */
    public int getdayOfThisMonth() {
        Calendar c = Calendar.getInstance();

        return c.get(Calendar.DATE);
    }

    /**
     * @description: ???????????????????????????n???
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

        return Integer.parseInt(day.substring(day.length() - 2));
    }

    public String getDaysMinusPlusStr(int n) {

        String day;

        if (n > 0) {
            day = LocalDate.now().plusDays(n).toString();
        } else {

            day = LocalDate.now().minusDays(Math.abs(n)).toString();
        }

        return day;
    }

    public static int timeToSecond(String timeStr) {
        String[] strings = timeStr.split(":");
        int hour = Integer.parseInt(strings[0]) * 60 * 60;
        int minute = Integer.parseInt(strings[1]) * 60;
        return hour + minute + Integer.parseInt(strings[2]);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param date      ??????
     * @param formatStr ???????????????
     * @return String
     */
    public static String getFormat(Date date, String formatStr) {
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);
        return sf.format(date);
    }

    /**
     * ?????????????????????????????????????????????yyyy-MM-dd
     *
     * @param date ??????????????????
     * @return String ???????????????????????????
     */
    public static String getFormat(Date date) {
        String formatStr = "yyyy-MM-dd";
        return getFormat(date, formatStr);
    }

    /**
     * ??????????????????????????????
     *
     * @param date ??????
     * @param i    ??????
     * @return ???????????????
     */
    public static Date addDay(Date date, int i) {
        long curr = date.getTime();
        curr += (long) i * 24 * 60 * 60 * 1000;
        return new Date(curr);
    }

    /**
     * ??????????????????????????????
     *
     * @param date ??????
     * @param s    ??????
     * @return ???????????????
     */
    public static Date addSecond(Date date, int s) {
        long curr = date.getTime();
        curr += (long) s * 1000;
        return new Date(curr);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param date ??????
     * @param i    ??????
     * @return ???????????????
     */
    public static String addDayFormat(Date date, int i, String format) {
        Date newDate = addDay(date, i);
        return getFormat(newDate, format);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param date ??????
     * @param i    ??????
     * @return ???????????????
     */
    public static String addDayFormat(Date date, int i) {
        Date newDate = addDay(date, i);
        return getFormat(newDate);
    }

    /**
     * ???????????????unix?????????
     *
     * @param str ???????????????
     * @return String ??????ms
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
     * ???unix?????????????????????
     *
     * @param stamp ?????????
     * @return String
     */
    public static String stampToDate(String stamp) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return stampToDate(stamp, format);
    }

    public static String stampToDate(String stamp, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(stamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Date strToDate(String stamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(stamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param data ??????
     * @return ?????????
     */
    public static Integer getDayOnMonth(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * ??????????????????????????????
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

    //??????????????????????????????
    public int getDay(int num_day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num_day);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    //?????????????????????
    public String getMounth(int num_day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num_day);
        return new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
    }

    public String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }

}
