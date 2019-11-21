package com.haisheng.framework.util;

import okhttp3.internal.http2.PushObserver;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public String mondayDateStr(Timestamp timestamp){
        DateTime dateTime = new DateTime(timestamp);
        int dayOfWeek = dateTime.getDayOfWeek();
        return dateTime.minusDays(dayOfWeek-1).toString("yyyyMMdd");
    }

    public String monthDateStr(Timestamp timestamp){
        DateTime dateTime = new DateTime(timestamp);
        int dayOfWeek = dateTime.getDayOfMonth();
        return dateTime.minusDays(dayOfWeek-1).toString("yyyyMM");
    }

    public String getHistoryDate(int num_days){
        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE, num_days);
        return new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
    }

    public Long getHistoryDateTimestamp(int num_days){
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, num_days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public String getHistoryDate(String baseDay, int num_days){

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
    public String getHistoryDate(String patten, String baseTime, String plusTime){

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
        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE, num_days);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd");

        return Integer.parseInt(sdf.format(cal.getTime()));
    }

    public int getHistoryWeek(int num_weeks) {
        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, num_weeks);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);

        return Integer.parseInt(year + "" + week);

    }

    public int getHistoryMonth(int num_months) {
        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.MONTH, num_months);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMM");

        return Integer.parseInt(sdf.format(cal.getTime()));
    }

    public int getHistorYear(int num_years) {
        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.YEAR, num_years);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy");

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

    public String changeTimePattern(String timeStr, String pattern){
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


        int day = (int) (diff/(24 * 60 * 60 * 1000));
        int hour = (int) (diff/(60 * 60 * 1000) - day * 24);
        int min = (int) ((diff/(60 * 1000)) - day * 24 * 60 - hour * 60);
        int sec = (int) (diff/1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

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
        String time = year + "/" + month + "/" + day + " " + hour +":00:00:000";
        return dt.dateToTimestamp(time);
    }

    public String getHourEnd(int index) throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay() + index;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":59:59:000";
        return dt.dateToTimestamp(time);
    }

    public String getCurrentMinuteEnd() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        int minute = dateTime.getMinuteOfHour()+1;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":"+minute+":00:000";
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

    public String timestampToDate(String pattern , long timestampMs) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampMs);

        SimpleDateFormat sdf = new SimpleDateFormat( pattern);

        return sdf.format(calendar.getTime());

    }

    public long initDateByDay() {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today=c.getTimeInMillis();

        return today;
    }

    @Test
    public long calTimeDiff(String left,String right) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        java.util.Date leftVal = df.parse(left);

        java.util.Date rightVal=df.parse(right);

        long l=rightVal.getTime()-leftVal.getTime();

        long day=l/(24*60*60*1000);

        long hour=(l/(60*60*1000)-day*24);

        long min=((l/(60*1000))-day*24*60-hour*60);

        long s=(l/1000-day*24*60*60-hour*60*60-min*60);

        long secondDiff = min*60 + s;

        System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
        return secondDiff;
    }

    public Timestamp currentDateToTimestamp() throws ParseException {
        java.util.Date date = new Date();
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

        String todayStr = dateTimeUtil.timestampToDate(pattern,dateTimeUtil.initDateByDay());

        Date today = format.parse(todayStr);

        c.setTime(today);
        c.add(Calendar.DATE,-7);
        Date w = c.getTime();
        String latWeek = format.format(w);
        String lastWeekTimestamp = dateTimeUtil.dateToTimestamp(latWeek);

        return Long.valueOf(lastWeekTimestamp);
    }

    public Long initLastMonth() throws Exception {
        String pattern = "yyyy/MM/dd HH:mm:ss:SSS";
        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();

        String todayStr = dateTimeUtil.timestampToDate(pattern,dateTimeUtil.initDateByDay());

        Date today = format.parse(todayStr);

        c.setTime(today);
        c.add(Calendar.MONTH,-1);
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

        String todayStr = dateTimeUtil.timestampToDate(pattern,dateTimeUtil.initDateByDay());

        Date today = format.parse(todayStr);

        c.setTime(today);
        c.add(Calendar.YEAR,-1);
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
        if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            return true;
        }

        return false;

    }
}