package com.haisheng.framework.testng.bigScreen.itemMall.casedaily.gly.Util;

import com.haisheng.framework.util.DateTimeUtil;

import java.util.Date;

public class MallBusinessUtil {
    /**
     * 获取当前时间
     */
    public String getDateTime(int day) {
        Date endDate = DateTimeUtil.addDay(new Date(), day);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * 获取当前时间
     */
    public String getDate(int day) {
        Date endDate = DateTimeUtil.addDay(new Date(), day);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd");
    }

}
