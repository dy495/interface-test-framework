package com.haisheng.framework.util;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    * plusTime: hh:mm:ss
    */
    public String getHistoryDate(String patten, String baseTime, String plusTime){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            Calendar cal = Calendar.getInstance();

            cal.setTime(sdf.parse(baseTime));
            String[] hms = plusTime.split(":");
            int hour = Integer.parseInt(hms[0]);
            int minute = Integer.parseInt(hms[1]);
            int second = Integer.parseInt(hms[2]);

            cal.add(Calendar.HOUR, hour);
            cal.add(Calendar.MINUTE, minute);
            cal.add(Calendar.SECOND, second);
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
}