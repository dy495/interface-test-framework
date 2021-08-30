package com.haisheng.framework.testng.bigScreen.itemMall.casedaily.gly.Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.time.DateUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.RegionTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewVenueOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.FullCourtTrendHistoryScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.RegionTrendScene;
import com.haisheng.framework.util.DateTimeUtil;
import org.apache.poi.poifs.filesystem.NDocumentOutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MallBusinessUtil {
    private final VisitorProxy visitor;

    public MallBusinessUtil(VisitorProxy visitor) {
        this.visitor = visitor;
    }

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

    /**
     * 获取上周的星期一的日期
     */
    public String getStartDate(int num) {
        Calendar cal = Calendar.getInstance();
        //对星期的数据进行增减一周的操作
        cal.add(Calendar.WEEK_OF_YEAR, num);
        //设置时间单位
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return DateTimeUtil.getFormat(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取上周周日的日期
     */
    public String getEndDate(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, num);// 一周
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        return DateTimeUtil.getFormat(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取每个月1号的日期
     */
    public String getMonthStartDate(int num) {
        Calendar cal = Calendar.getInstance();
        //对星期的数据进行增减一周的操作
        cal.add(Calendar.MONTH, num);
        //设置时间单位
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return DateTimeUtil.getFormat(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取每个月最后一号的日期
     */
    public String getMonthEndDate(int num) {
        Calendar cal = Calendar.getInstance();
        //对星期的数据进行增减一周的操作
        cal.add(Calendar.MONTH, num);
        //设置时间单位
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return DateTimeUtil.getFormat(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * 输出四舍五入的结果
     */
    public double getNumHalfUp(double num, int place) {
        BigDecimal b = new BigDecimal(num);
        double qaqCount = b.setScale(place, RoundingMode.HALF_UP).doubleValue();
        return qaqCount;
    }

    /**
     * 输出四舍五入的结果
     */
    public double getNumHalfUp(String num, int place) {
        BigDecimal b = new BigDecimal(num);
        double qaqCount = b.setScale(place, RoundingMode.HALF_UP).doubleValue();
        return qaqCount;
    }

    /**
     * 运用decimal进行运算
     */
    public double getDecimalResult(double i, double j, String type, int place) {
        BigDecimal b1 = new BigDecimal(i);
        BigDecimal b2 = new BigDecimal(j);
        double num = 0;
        if (type.equals("add")) {
            BigDecimal result = b1.add(b2);
            num = result.setScale(place, RoundingMode.HALF_UP).doubleValue();
        } else if (type.equals("subtract")) {
            BigDecimal result = b1.subtract(b2);
            BigDecimal result1 = result.abs();
            num = result1.setScale(place, RoundingMode.HALF_UP).doubleValue();
        }
        return num;
    }


    /**
     * 获取场馆客流总览中的日环比
     *
     * @param scene  客流纵览中的当天的数据
     * @param scene1 客流纵览中的隔几天的数据
     * @param type   客流总览中的获取数据的类型
     */
    public JSONObject getDayQaq(IScene scene, IScene scene1, String type) {
        //获取客流总览中的数据
        JSONObject response = scene.visitor(visitor).execute();
        //当前人数的日环比
        String dayQoqUv = response.getJSONObject(type).getString("day_qoq");
        BigDecimal dayNum = new BigDecimal(dayQoqUv);
        double dayPercent = dayNum.setScale(2, RoundingMode.HALF_UP).doubleValue();
        //当前人数
        double numberUv = response.getJSONObject(type).getDouble("number");

        //获取历史客流前一天的人数
        JSONObject response1 = scene1.visitor(visitor).execute();
        double pre = response1.getJSONObject(type).getDouble("number");
        double percent = pre == 0 ? 0 : (numberUv - pre) / pre;
        BigDecimal b = new BigDecimal(percent);
        double qaq = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        JSONObject object = new JSONObject();
        object.put("dayPercent", dayPercent);
        object.put("qaqCount", qaq);
        return object;

    }

    /**
     * 获取场馆客流总览中的周同比
     *
     * @param scene  客流纵览中的当天的数据
     * @param scene1 客流纵览中的隔几天的数据
     * @param type   客流总览中的获取数据的类型
     */
    public JSONObject getWeekQaq(IScene scene, IScene scene1, String type) {
        //获取历史客流数据
        JSONObject response = scene.visitor(visitor).execute();
        //当前人次
        double numberPv = response.getJSONObject(type).getDouble("number");
        //当前人次的周同比
        String preWeekCompare = response.getJSONObject(type).getString("pre_week_compare");
        BigDecimal weekNum = new BigDecimal(preWeekCompare);
        double weekPercent = weekNum.setScale(4, RoundingMode.HALF_UP).doubleValue();

        //获取历史客流上一周同一天的人数pv数据
        JSONObject response2 = scene1.visitor(visitor).execute();
        double Yesterday = response2.getJSONObject(type).getDouble("number");
        double percent = Yesterday == 0 ? 0 : (numberPv - Yesterday) / Yesterday;
        BigDecimal b = new BigDecimal(percent);
        double qaqCount = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        JSONObject object = new JSONObject();
        object.put("weekPercent", weekPercent);
        object.put("qaqCount", qaqCount);
        return object;
    }

    /**
     * 获取全场到访趋势图数据之和
     */
    public double getFullCountSum(IScene scene) {
        //获取全场到访趋势图在中人数的之和
        JSONObject response2 = scene.visitor(visitor).execute();
        int number = 0;
        JSONArray list = response2.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            int hourNum = list.getJSONObject(i).getInteger("current_day");
            number += hourNum;
        }
        return number;
    }


}
