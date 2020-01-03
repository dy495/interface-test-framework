package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @author : xiezhidong
 * @date :  2019/10/12  14:55
 */

public class MagicMirrorApiDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_MAGIC_MIRROR_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/magic-mirror-daily-test/buildWithParameters?case_name=";

    private String loginPathDaily = "/mirror-login";
    private String jsonDaily = "{\"username\":\"demo@winsense.ai\",\"passwd\":\"fe01ce2a7fbac8fafaed7c982a04e229\"}";
    private String authorization = "";

    private String realTimeShopPath = "/magic-mirror/real-time/shop";
    private String realTimeDevicePath = "/magic-mirror/real-time/device";
    private String realTimeAccumulatedPath = "/magic-mirror/real-time/persons/accumulated";
    private String realTimeCustomerTypePath = "/magic-mirror/real-time/customer-type/distribution";

    private String historyShopPath = "/magic-mirror/history/shop";
    private String historyAccumulatedPath = "/magic-mirror/history/persons/accumulated";
    private String historyAgeGenderPath = "/magic-mirror/history/age-gender/distribution";
    private String historyCustomertypePath = "/magic-mirror/history/customer-type/distribution";

    private HttpConfig config;


    /**
     * 测试环境使用以下customerId  正式环境不确定哪些reId一定存在
     */

    /**
     * 环境   线上为 ONLINE 测试为 DAILY
     */
    private String DEBUG_PARA = System.getProperty("DEBUG", "true");
    boolean DEBUG = Boolean.valueOf(DEBUG_PARA);
//    private String DEBUG = System.getProperty("DEBUG", "true");

    private long SHOP_ID_DAILY = 7042;

    String URL_PREFIX = "http://39.97.198.251";

    int pageSize = 10000;

    CheckUtil checkUtil = new CheckUtil();

    //    -----------------------------------------------一、登录------------------------------------------------
//    -----------------------------------------------1、实时---------------------------------------------

    @Test
    public void shopMTDeviceRealTime() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function =
                "校验：1、实时的今日累计互动人数大于某一个魔镜的累计互动人数; \n" +
                        "2、实时的今日累计互动人数小于等于几个魔镜的累计互动人数之和;\n" +
                        "3、实时的今日累计互动人次等于几个魔镜的累计互动人次之和;\n" +
                        "4、实时的今日UV * 今日人均互动时长 - 各个魔镜的 （UV * 人均互动时长）之和 <= ±3";

        try {

            JSONObject realTimeShopData = realTimeData(realTimeShopPath);

            int uv = realTimeShopData.getInteger("uv");
            int pv = realTimeShopData.getInteger("pv");
            int averageUseTime = realTimeShopData.getInteger("average_use_time");

            JSONArray list = realTimeData(realTimeDevicePath).getJSONArray("list");

            int totalStayTime = 0;
            int totalPv = 0;
            int totalUv = 0;
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String deviceName = single.getString("device_name");
                int pvDevice = single.getInteger("pv");
                totalPv += pvDevice;
                int uvDevice = single.getInteger("uv");
                totalUv += uvDevice;

                int averageUseTimeDevice = single.getInteger("average_use_time");

                totalStayTime += averageUseTimeDevice * uvDevice;

                if (uv < uvDevice) {
                    throw new Exception("总体的uv=" + uv + "小于设备" + deviceName + "的uv=" + uvDevice);
                }

                if (pv < pvDevice) {
                    throw new Exception("总体的pv=" + pv + "小于设备" + deviceName + "的pv=" + pvDevice);
                }
            }

            if (Math.abs(uv * averageUseTime - totalStayTime) > 3) {
                throw new Exception("总停留时长=" + uv * averageUseTime + "与各个设备的总停留时长之和=" + totalStayTime + "相差大于3。");
            }

            if (uv > totalUv) {
                throw new Exception("总人数=" + uv + "大于各个设备的累计人数之和=" + totalUv);
            }

            if (pv != totalPv) {
                throw new Exception("总人次=" + pv + "不等于各个设备的累计人次之和=" + totalPv);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeShopEqualsCustomerType() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function =
                "校验：1、实时的今日累计互动人数等于实时客流身份分布中的新老顾客之和;\n" +
                        "2、实时客流身份分布中的顾客类型比例之和是100%>>>";

        try {
            JSONObject realTimeShopData = realTimeData(realTimeShopPath);

            int uv = realTimeShopData.getInteger("uv");

            JSONArray customerTypeList = realTimeData(realTimeCustomerTypePath).getJSONArray("list");

            int total = 0;

            for (int i = 0; i < customerTypeList.size(); i++) {
                JSONObject single = customerTypeList.getJSONObject(i);

                total += single.getInteger("num");
            }

            if (uv != total) {
                throw new Exception("实时的今日累计互动人数=" + uv + "不等于实时客流身份分布中的新老顾客之和=" + total);
            }

            checkCustomerTypeRate(customerTypeList, "实时客流身份分布>>>");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeShopEqualsAccumulated() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "实时的今日累计互动人数等于客流到场趋势中之前时段的实时互动人数之和>>>";

        try {
            JSONObject realTimeShopData = realTimeData(realTimeShopPath);

            int uv = realTimeShopData.getInteger("uv");

            JSONArray accumulatedList = realTimeData(realTimeAccumulatedPath).getJSONArray("list");

            int accumulatedNum = getAccumulatedNum(accumulatedList);

            if (uv > accumulatedNum) {
                throw new Exception("实时的今日累计互动人数=" + uv + "大于客流到场趋势中之前时段的实时互动人数之和=" + accumulatedNum);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    private int getAccumulatedNum(JSONArray list) {

        int num = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (single.containsKey("present_cycle")) {
                num += single.getInteger("present_cycle");
            }
        }

        return num;
    }

    @Test
    public void realTimeCustomerTypeAccuracy() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "实时客流身份分布中小数点后保留两位小数>>>";

        try {
            JSONObject customerTypeData = realTimeData(realTimeCustomerTypePath);
            checkPercentAccuracy(customerTypeData, 2, "实时客流身份分布>>>");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeShopValidityChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验店铺实时数据的有效性>>>";

        try {
            JSONObject realTimeShopData = realTimeData(realTimeShopPath);

            for (Object obj : realTimeShopValidity()) {
                String key = obj.toString();
                checkUtil.checkDeepKeyValidity("店铺实时数据>>>", realTimeShopData, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeDeviceValidityChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验实时单个魔镜数据的有效性>>>";

        try {
            JSONArray list = realTimeData(realTimeDevicePath).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                String deviceName = single.getString("device_name");

                for (Object obj : realTimeShopValidity()) {
                    String key = obj.toString();
                    checkUtil.checkDeepKeyValidity("魔镜实时数据>>>" + deviceName, single, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeAccumulatedChainRatio() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "今日实时客流互动趋势环比计算是否正确>>>";

        try {

            JSONArray accumulatedList = realTimeData(realTimeAccumulatedPath).getJSONArray("list");

            checkUtil.checkChainRatio("实时客流到场趋势>>>", "real_time", "present_cycle", "last_cycle", accumulatedList);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//    ----------------------------------------------2、历史--------------------------------------------------

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyShopNotNull(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验店铺历史数据的有效性>>>";

        System.out.println(LocalDate.now().getDayOfWeek());

        LocalDate.now().getDayOfWeek();

        String pattern = "yyyy-MM-dd";

        String startTime = "";

        DateTime dateTime = new DateTime().minusDays(minus);

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(dateTime.toLocalDateTime().toDate(), pattern);
            }

            JSONObject historyShopData = historyData(historyShopPath, startTime, cycle);

            for (Object obj : historyShopNotNull()) {
                String key = obj.toString();
                checkUtil.checkNotNull("店铺历史数据>>>", historyShopData, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyShopValidity(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验店铺历史数据的有效性>>>";

        System.out.println(LocalDate.now().getDayOfWeek());

        LocalDate.now().getDayOfWeek();

        String pattern = "yyyy-MM-dd";

        String startTime = "";

        DateTime dateTime = new DateTime().minusDays(minus);

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(dateTime.toLocalDateTime().toDate(), pattern);
            }


            JSONObject historyShopData = historyData(historyShopPath, startTime, cycle);

            for (Object obj : historyShopValidity()) {
                String key = obj.toString();
                checkUtil.checkDeepKeyValidity("店铺历史数据>>>", historyShopData, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyShopDataCheck(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：累计互动人数(去重)<=总uv(不去重)\n" +
                "人均互动时长=总时长/总uv\n" +
                "日均互动人数=总uv/魔镜运行天数 \n";

        System.out.println(LocalDate.now().getDayOfWeek());

        LocalDate.now().getDayOfWeek();

        String pattern = "yyyy-MM-dd";

        String startTime = "";

        Date date = new DateTime().minusDays(minus).toLocalDateTime().toDate();

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(date, pattern);

            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(date, pattern);
            }

            JSONObject historyShopData = historyData(historyShopPath, startTime, cycle);
            JSONObject historyAccumulatedData = historyData(historyAccumulatedPath, startTime, cycle);

//            根据“顾客互动趋势”中是否返回present_cycle判断有效天数
            int validDays = getValidDays(historyAccumulatedData);

            checkHistoryData(historyShopData, validDays);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyAccumulatedChainRatio(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客互动趋势的环比计算是否正确>>>";

        System.out.println(LocalDate.now().getDayOfWeek());

        LocalDate.now().getDayOfWeek();

        String pattern = "yyyy-MM-dd";

        String startTime = "";

        Date date = new DateTime().minusDays(minus).toLocalDateTime().toDate();

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(date, pattern);

            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(date, pattern);
            }

            JSONArray list = historyData(historyAccumulatedPath, startTime, cycle).getJSONArray("list");

            checkUtil.checkChainRatio("顾客互动趋势>>>", "history", "present_cycle", "last_cycle", list);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyShopEqualsAccumulated(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史统计，totalUv与顾客互动趋势的累计uv相等>>>";

        System.out.println(LocalDate.now().getDayOfWeek());

        LocalDate.now().getDayOfWeek();

        String pattern = "yyyy-MM-dd";

        String startTime = "";

        Date date = new DateTime().minusDays(minus).toLocalDateTime().toDate();

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(date, pattern);

            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(date, pattern);
            }

            JSONObject historyShop = historyData(historyShopPath, startTime, cycle);
            int totalUv = historyShop.getInteger("totalUv");

            JSONArray list = historyData(historyAccumulatedPath, startTime, cycle).getJSONArray("list");
            int accumulatedNum = getAccumulatedNum(list);

            if (totalUv != accumulatedNum) {
                throw new Exception("历史统计的totalUv=" + totalUv + "顾客互动趋势的累计人数=" + accumulatedNum);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyShopEqualsCustomerType(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function =
                "校验：1、历史的累计互动人数等于顾客身份识别中的新老顾客之和;\n" +
                        "2、顾客身份识别中的顾客类型比例之和是100%>>>";

        String startTime = "";

        String pattern = "yyyy-MM-dd";

        DateTime dateTime = new DateTime().minusDays(minus);

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(dateTime.toLocalDateTime().toDate(), pattern);
            }


            JSONObject historyShopData = historyData(historyShopPath, startTime, cycle);

            int uv = historyShopData.getInteger("uv");

            JSONArray customerTypeList = historyData(historyCustomertypePath, startTime, cycle).getJSONArray("list");

            int total = 0;

            for (int i = 0; i < customerTypeList.size(); i++) {
                JSONObject single = customerTypeList.getJSONObject(i);

                total += single.getInteger("num");
            }

            if (uv != total) {
                throw new Exception("历史的累计互动人数=" + uv + "不等于顾客身份识别中的新老顾客之和=" + total);
            }

            checkCustomerTypeRate(customerTypeList, "顾客身份识别>>>");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyCustomerTypeAccuracy(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客身份识别中小数点后保留两位小数>>>";
        String startTime = "";

        String pattern = "yyyy-MM-dd";

        DateTime dateTime = new DateTime().minusDays(minus);

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(dateTime.toLocalDateTime().toDate(), pattern);
            }

            JSONObject customerTypeData = historyData(historyCustomertypePath, startTime, cycle);
            checkPercentAccuracy(customerTypeData, 2, "顾客身份识别>>>");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyAgeGenderDistribution(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客年龄/性别分布的各年龄段的比例之和为100%>>>";
        String startTime = "";

        String pattern = "yyyy-MM-dd";

        DateTime dateTime = new DateTime().minusDays(minus);

        try {
            if ("WEEK".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            } else if ("MONTH".equals(cycle)) {
                startTime = dateTimeUtil.getBeginDayOfMonth(dateTime.toLocalDateTime().toDate(), pattern);
            }

            JSONObject customerAgeGenderData = historyData(historyAgeGenderPath, startTime, cycle);

            checkAgeGenderPercent("顾客年龄/性别分布>>>", customerAgeGenderData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//    ------------------------------------------------------三、活动相关-----------------------------------------------------------

    @Test
    public void activityDetailEqualsContrast() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "三个时期的新老顾客之和分别与活动客流占比中的各时期人数相等>>>";

        String activityId = "15";

        try {

            JSONObject detailData = activityDetail(activityId);
            int detailContrastNew = detailData.getJSONObject("contrast_cycle").getInteger("new_num");
            int detailContrastOld = detailData.getJSONObject("contrast_cycle").getInteger("old_num");
            int detailThisNew = detailData.getJSONObject("this_cycle").getInteger("new_num");
            int detailThisOld = detailData.getJSONObject("this_cycle").getInteger("old_num");
            int detailInfluenceNew = detailData.getJSONObject("influence_cycle").getInteger("new_num");
            int detailInfluenceOld = detailData.getJSONObject("influence_cycle").getInteger("old_num");

            JSONObject contrastData = activityContrast(activityId);

            int contrastCycleNum = getContrastPassFlowNum(contrastData, "contrast_cycle");
            int thisCycleNum = getContrastPassFlowNum(contrastData, "this_cycle");
            int influenceCycleNum = getContrastPassFlowNum(contrastData, "influence_cycle");

            contrastActivityNum(activityId, "对比时期", detailContrastNew, detailContrastOld, contrastCycleNum);
            contrastActivityNum(activityId, "活动期间", detailThisNew, detailThisOld, thisCycleNum);
            contrastActivityNum(activityId, "活动后期", detailInfluenceNew, detailInfluenceOld, influenceCycleNum);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void activityDetailPercent() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "三个时期的新老顾客之和分别与活动客流占比中的各时期人数相等>>>";

        String activityId = "15";

        try {

            JSONObject detailData = activityDetail(activityId);
            checkActivityDetailPercent(detailData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    private void checkActivityDetailPercent(JSONObject data) throws Exception {


        String activeName = data.getString("active_name");

        checkPercent(activeName, data.getJSONObject("contrast_cycle"), "活动期间");
        checkPercent(activeName, data.getJSONObject("this_cycle"), "活动期间");
        checkPercent(activeName, data.getJSONObject("influence_cycle"), "活动期间");
    }

    private void checkPercent(String activityName, JSONObject timeData, String time) throws Exception {

        String newRatio = timeData.getString("new_ratio");
        if ("-".equals(newRatio)) {
            return;
        }
        double newRatioD = Double.valueOf(newRatio.substring(0, newRatio.length() - 1));
        String oldRatio = timeData.getString("old_ratio");
        double oldRatioD = Double.valueOf(oldRatio.substring(0, oldRatio.length() - 1));

        if ((int) (newRatioD + oldRatioD) != 100) {
            throw new Exception("activity_name=" + activityName + "活动详情中，" + time + "新客比例=" + newRatio + "，老客比例=" + oldRatio + "之和不是100%");
        }
    }

    public void contrastActivityNum(String activityId, String time, int detailNew, int detailOld, int contrastAccmulated) throws Exception {

        if (detailNew + detailOld > contrastAccmulated) {
            throw new Exception("activity_id=" + activityId + "," + time + "，活动详情中新客=" + detailNew +
                    "与老客=" + detailOld + "之和，大于活动客流对比中的该时期总人数=" + contrastAccmulated + "].");
        }
    }

    private int getContrastPassFlowNum(JSONObject data, String arrayKey) {

        int num = 0;

        JSONArray list = data.getJSONArray(arrayKey);
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (single.containsKey("num")) {
                num += single.getInteger("num");
            }
        }
        return num;
    }


    private int getValidDays(JSONObject data) {
        int num = 0;

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String presentCycle = single.getString("present_cycle");
            if (presentCycle != null && !"".equals(presentCycle)) {
                num++;
            }
        }

        return num;
    }

    private void checkHistoryData(JSONObject data, int validDays) throws Exception {

        if (validDays == 0) {
            return;
        }

        long totalTime = data.getLongValue("totalTime");
        int averageUseTime = (int) Math.ceil((data.getDoubleValue("average_use_time") / 1000) / 60.0d);

        int uv = data.getInteger("uv");

        if (uv == 0) {
            return;
        }

        int totalUv = data.getInteger("totalUv");
        int averageDayUv = data.getInteger("average_day_uv");

        if (uv > totalUv) {
            throw new Exception("店铺历史数据>>>去重后的uv=" + uv + "大于不去重的uv=" + totalUv);
        }

        //totalTime单位是ms，要转成min
        int averageUseTimeRes = (int) Math.ceil((double) totalTime / (double) (60000 * uv));

        if (averageUseTime != averageUseTimeRes) {
            throw new Exception("店铺历史数据>>>人均互动时长，系统返回=" + averageUseTime + "], 期待=" + averageUseTimeRes + "]");
        }

        int averageDayUvRes = (int) Math.ceil((double) totalUv / (double) validDays);

        if (averageDayUv != averageDayUvRes) {
            throw new Exception("店铺历史数据>>>日均互动人数，系统返回=" + averageDayUv + ", 期待=" + averageDayUvRes);
        }
    }


    private void checkAgeGenderPercent(String function, JSONObject jo) throws Exception {

        DecimalFormat df = new DecimalFormat("0.00");

//        校验男女总比例
        String maleRatioStr = jo.getString("male_ratio_str");
        String femaleRatioStr = jo.getString("female_ratio_str");

        float maleD = Float.valueOf(maleRatioStr.substring(0, maleRatioStr.length() - 1));
        float femaleD = Float.valueOf(femaleRatioStr.substring(0, femaleRatioStr.length() - 1));

        if ((int) (maleD + femaleD) != 100) {
            throw new Exception(function + "男比例=" + maleRatioStr + ",女比例=" + femaleRatioStr + "之和不是100%");
        }

//        校验各个年龄段的男女比例

        JSONArray list = jo.getJSONArray("ratio_list");

        int[] nums = new int[list.size()];
        String[] percents = new String[list.size()];
        String[] ageGroups = new String[list.size()];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            int num = single.getInteger("num");
            nums[i] = num;
            String percentStr = single.getString("percent");
            float percentD = Float.valueOf(percentStr.substring(0, percentStr.length() - 1));
            percents[i] = df.format(percentD) + "%";
            ageGroups[i] = single.getString("age_group");
            total += num;
        }

        if (total == 0) {
            for (int i = 0; i < percents.length; i++) {
                if (!"0.00%".equals(percents[i])) {
                    throw new Exception("总数为0，" + ageGroups[i] + "的比例=" + percents[i]);
                }
            }
        }

        for (int i = 0; i < percents.length; i++) {
            float percent = (float) nums[i] / (float) total * 100;
            String percentStr = df.format(percent);

            percentStr += "%";

            if (!percentStr.equals(percents[i])) {
                throw new Exception(function + "期待比例=" + percentStr + ", 系统返回=" + percents[i]);
            }
        }
    }

    private void checkCustomerTypeRate(JSONArray list, String function) throws Exception {

        String[] typeNames = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            typeNames[i] = list.getJSONObject(i).getString("type_name");
        }

        String[] typeNamesRes = new String[list.size()];
        String[] percentageStrs = new String[list.size()];
        int[] nums = new int[list.size()];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String percentageStr = single.getString("percentage_str");
            percentageStrs[i] = percentageStr.substring(0, percentageStr.length() - 1);
            nums[i] = single.getInteger("num");
            typeNamesRes[i] = single.getString("type_name");
            total += nums[i];
        }

        if (total == 0) {
            for (int i = 0; i < nums.length; i++) {
                if (!"-".equals(percentageStrs[i])) {
                    throw new Exception(typeNames[i] + "总数是0，" + "比例为：" + percentageStrs[i]);
                }
            }
        }

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String actualStr = df.format(actual);

            String resStr = df.format(Double.parseDouble(percentageStrs[i]));

            if (!actualStr.equals(resStr)) {
                throw new Exception(function + "type_name: " + typeNamesRes[i] + " 对应的客流身份比例错误！返回：" + resStr + ",期待：" + actualStr);
            }
        }
    }

    private void checkPercentAccuracy(JSONObject data, int num, String message) throws Exception {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String type = single.getString("type");
            String percentStr = single.getString("percentage_str");
            String lengthStr = percentStr.substring(percentStr.indexOf(".") + 1, percentStr.indexOf("%"));
            if (lengthStr.length() > num) {
                throw new Exception(message + ",type=" + type + "小数点后保留了:" + lengthStr.length() + "位,期待最多保留：" + num + "位");
            }
        }
    }

    private int getDayofWeek(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date));

        return cal.get(Calendar.DAY_OF_WEEK);
    }


    //    ----------------------------------调用接口的方法---------------------------------------------------------------

//    ------------------------------------------一、门店选择-----------------------------------------------------------

    public JSONObject shopList() throws Exception {

        String path = "/yuexiu/shop/list";
        String json = "{}";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

//---------------------------------------------二、今日实时统计-----------------------------------------------------------

    public JSONObject realTimeData(String path) throws Exception {
        String json = "{\"shop_id\":" + SHOP_ID_DAILY + "}";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

//    --------------------------------------三、门店历史客流统计----------------------------------------------------

    public JSONObject historyData(String path, String startTime, String cycle) throws Exception {
        String json =
                "{\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\"\n" +
                        "}\n";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    -------------------------------------------------------三、活动相关------------------------------------------------------------------

    public JSONObject getActivityType() throws Exception {
        String path = "/magic-mirror/manage/activity/type/list";
        String json =
                "{}";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject addActivity(String name, String type, String contrastStart, String contrastEnd, String thisStart,
                                  String thisEnd, String influenceStart, String influenceEnd) throws Exception {
        String path = "/magic-mirror/manage/activity/add";
        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\"," +
                        "    \"activity_type\":\"" + type + "\"," +
                        "    \"contrast_start\":\"" + contrastStart + "\"," +
                        "    \"contrast_end\":\"" + contrastEnd + "\"," +
                        "    \"start_date\":\"" + thisStart + "\"," +
                        "    \"end_date\":\"" + thisEnd + "\"," +
                        "    \"influence_start\":\"" + influenceStart + "\"," +
                        "    \"influence_end\":\"" + influenceEnd + "\"" +
                        "}\n";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityList(String name, String type, String date) throws Exception {
        String path = "/magic-mirror/manage/activity/list";
        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\"," +
                        "    \"activity_type\":\"" + type + "\"," +
                        "    \"activity_date\":\"" + date + "\"" +
                        "}\n";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityDelete(String id) throws Exception {
        String path = "/magic-mirror/manage/activity/delete";
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityDetail(String id) throws Exception {
        String path = "/magic-mirror/manage/activity/detail";
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityContrast(String id) throws Exception {
        String path = "/magic-mirror/manage/activity/passenger-flow/contrast";
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    ------------------------------------------------------其他方法---------------------------------------------------------------------

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

//    ---------------------------------------------------发送请求方法---------------------------------------------------------------

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization)
                .other("shop_id", String.valueOf(SHOP_ID_DAILY))
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String getIpPort() {

        return URL_PREFIX;
    }

    private String httpPostCode1000(String path, String json, int expectCode) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        try {
            response = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            return response;
            //throw new RuntimeException("http post 调用异常，url = " + queryUrl, e);
        }

        logger.info("result = {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        checkCode(response, expectCode, "");

        return response;
    }

    private String httpPost(String path, String json) {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        try {
            response = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            return response;
            //throw new RuntimeException("http post 调用异常，url = " + queryUrl, e);
        }

        logger.info("result = {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        return response;
    }

    private String httpDelete(String url, String json, HashMap header, int expectCode) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();

        executor.doDeleteJsonWithHeaders(url, json, header);

        checkCode(executor.getResponse(), expectCode, "");
        return executor.getResponse();
    }

    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000 && key col not null");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("魔镜日常 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        if (!DEBUG) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.dailyRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (!DEBUG && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //15011479599 谢志东
            String[] rd = {"15011479599"};
            alarmPush.alarmToRd(rd);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {

        String json = this.jsonDaily;
        String path = this.loginPathDaily;
        qaDbUtil.openConnection();

        initHttpConfig();
        String loginUrl = getIpPort() + path;

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        String result;
        try {
            result = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(result).getJSONObject("data").getString("token");
            logger.info("authorization: {}", authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, "登录获取authentication");
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @DataProvider(name = "REAL_TIME_SHOP_NOT_NULL")
    private static Object[] realTimeShopNotNull() {
        return new Object[]{
                "total_stay",
                "average_use_time",
                "pv",
                "uv"
        };
    }

    @DataProvider(name = "REAL_TIME_SHOP_VALIDITY")
    private static Object[] realTimeShopValidity() {
        return new Object[]{
                "total_stay>=0",
                "average_use_time>=0",
                "pv>=0",
                "uv>0",
                "uv[<=]pv"
        };
    }

    @DataProvider(name = "CYCLE_MINUS")
    private static Object[][] cycleMinus() {
        return new Object[][]{
                new Object[]{
                        "WEEK", 0
                },

//                new Object[]{
//                        "WEEK", 7
//                },
//
//                new Object[]{
//                        "MONTH", 0
//                },

//                new Object[]{
//                        "MONTH", 30
//                },
        };
    }

//    @DataProvider(name = "HISTORY_SHOP_NOT_NULL")
//    private static Object[] historyShopNotNull() {
//        return new Object[]{
//                "present_cycle",
//                "chain_ratio",
//                "last_cycle",
//                "time",
//                "day"
//        };
//    }
//
//    @DataProvider(name = "HISTORY_SHOP_VALIDITY")
//    private static Object[] historyShopValidity() {
//        return new Object[]{
//                "present_cycle",
//                "chain_ratio",
//                "last_cycle",
//                "time",
//                "day"
//        };
//    }

    @DataProvider(name = "HISTORY_SHOP_NOT_NULL")
    private static Object[] historyShopNotNull() {
        return new Object[]{
                "uv",
                "pv",
                "average_day_uv",
                "average_use_time"
        };
    }

    @DataProvider(name = "HISTORY_SHOP_VALIDITY")
    private static Object[] historyShopValidity() {
        return new Object[]{
                "uv>=0",
                "pv>=0",
                "uv[<=]pv",
                "average_day_uv>=0",
                "average_use_time>=0"
        };
    }
}
