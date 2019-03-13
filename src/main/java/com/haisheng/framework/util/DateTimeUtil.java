package com.haisheng.framework.util;

import org.joda.time.DateTime;

import java.sql.Timestamp;
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

    //实现日期加一天的方法
    public String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);//增加一天
            //cd.add(Calendar.MONTH, n);//增加一个月

            return sdf.format(cd.getTime());

        } catch (Exception e) {
            return null;
        }
    }
}