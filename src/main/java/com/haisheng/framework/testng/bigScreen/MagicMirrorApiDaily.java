package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

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
     * ????????????????????????customerId  ???????????????????????????reId????????????
     */

    /**
     * ??????   ????????? ONLINE ????????? DAILY
     */
    private String DEBUG_PARA = System.getProperty("DEBUG", "true");
    boolean DEBUG = Boolean.valueOf(DEBUG_PARA);
//    private String DEBUG = System.getProperty("DEBUG", "true");

    private long SHOP_ID_DAILY = 7042;

    String URL_PREFIX = "http://39.97.198.251";

    int pageSize = 10000;

    CheckUtil checkUtil = new CheckUtil();

    //    -----------------------------------------------????????????------------------------------------------------
//    -----------------------------------------------1?????????---------------------------------------------

    @Test
    public void shopMTDeviceRealTime() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function =
                "?????????1??????????????????????????????????????????????????????????????????????????????; \n" +
                        "2???????????????????????????????????????????????????????????????????????????????????????;\n" +
                        "3?????????????????????????????????????????????????????????????????????????????????;\n" +
                        "4??????????????????UV * ???????????????????????? - ??????????????? ???UV * ??????????????????????????? <= ??3";

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
                    throw new Exception("?????????uv=" + uv + "????????????" + deviceName + "???uv=" + uvDevice);
                }

                if (pv < pvDevice) {
                    throw new Exception("?????????pv=" + pv + "????????????" + deviceName + "???pv=" + pvDevice);
                }
            }

            if (Math.abs(uv * averageUseTime - totalStayTime) > 3) {
                throw new Exception("???????????????=" + uv * averageUseTime + "???????????????????????????????????????=" + totalStayTime + "????????????3???");
            }

            if (uv > totalUv) {
                throw new Exception("?????????=" + uv + "???????????????????????????????????????=" + totalUv);
            }

            if (pv != totalPv) {
                throw new Exception("?????????=" + pv + "??????????????????????????????????????????=" + totalPv);
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
                "?????????1??????????????????????????????????????????????????????????????????????????????????????????;\n" +
                        "2????????????????????????????????????????????????????????????100%>>>";

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
                throw new Exception("?????????????????????????????????=" + uv + "?????????????????????????????????????????????????????????=" + total);
            }

            checkCustomerTypeRate(customerTypeList, "????????????????????????>>>");

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

        String function = "???????????????????????????????????????????????????????????????????????????????????????????????????>>>";

        try {
            JSONObject realTimeShopData = realTimeData(realTimeShopPath);

            int uv = realTimeShopData.getInteger("uv");

            JSONArray accumulatedList = realTimeData(realTimeAccumulatedPath).getJSONArray("list");

            int accumulatedNum = getAccumulatedNum(accumulatedList);

            if (uv > accumulatedNum) {
                throw new Exception("?????????????????????????????????=" + uv + "??????????????????????????????????????????????????????????????????=" + accumulatedNum);
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

        String function = "?????????????????????????????????????????????????????????>>>";

        try {
            JSONObject customerTypeData = realTimeData(realTimeCustomerTypePath);
            checkPercentAccuracy(customerTypeData, 2, "????????????????????????>>>");

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

        String function = "????????????????????????????????????>>>";

        try {
            JSONObject realTimeShopData = realTimeData(realTimeShopPath);

            for (Object obj : realTimeShopValidity()) {
                String key = obj.toString();
                checkUtil.checkDeepKeyValidity("??????????????????>>>", realTimeShopData, key);
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

        String function = "??????????????????????????????????????????>>>";

        try {
            JSONArray list = realTimeData(realTimeDevicePath).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                String deviceName = single.getString("device_name");

                for (Object obj : realTimeShopValidity()) {
                    String key = obj.toString();
                    checkUtil.checkDeepKeyValidity("??????????????????>>>" + deviceName, single, key);
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

        String function = "??????????????????????????????????????????????????????>>>";

        try {

            JSONArray accumulatedList = realTimeData(realTimeAccumulatedPath).getJSONArray("list");

            checkUtil.checkChainRatio("????????????????????????>>>", "real_time", "present_cycle", "last_cycle", accumulatedList);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//    ----------------------------------------------2?????????--------------------------------------------------

    @Test(dataProvider = "CYCLE_MINUS")
    public void historyShopNotNull(String cycle, int minus) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "????????????????????????????????????>>>";

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
                checkUtil.checkNotNull("??????????????????>>>", historyShopData, key);
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

        String function = "????????????????????????????????????>>>";

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
                checkUtil.checkDeepKeyValidity("??????????????????>>>", historyShopData, key);
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

        String function = "???????????????????????????(??????)<=???uv(?????????)\n" +
                "??????????????????=?????????/???uv\n" +
                "??????????????????=???uv/?????????????????? \n";

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

//            ?????????????????????????????????????????????present_cycle??????????????????
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

        String function = "?????????????????????????????????????????????>>>";

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

            checkUtil.checkChainRatio("??????????????????>>>", "history", "present_cycle", "last_cycle", list);

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

        String function = "???????????????totalUv??????????????????????????????uv??????>>>";

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
                throw new Exception("???????????????totalUv=" + totalUv + "?????????????????????????????????=" + accumulatedNum);
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
                "?????????1??????????????????????????????????????????????????????????????????????????????;\n" +
                        "2??????????????????????????????????????????????????????100%>>>";

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
                throw new Exception("???????????????????????????=" + uv + "???????????????????????????????????????????????????=" + total);
            }

            checkCustomerTypeRate(customerTypeList, "??????????????????>>>");

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

        String function = "???????????????????????????????????????????????????>>>";
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
            checkPercentAccuracy(customerTypeData, 2, "??????????????????>>>");

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

        String function = "????????????/?????????????????????????????????????????????100%>>>";
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

            checkAgeGenderPercent("????????????/????????????>>>", customerAgeGenderData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//    ------------------------------------------------------??????????????????-----------------------------------------------------------

    @Test
    public void activityDetailEqualsContrast() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????????????????????????????????????????????????????>>>";

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

            contrastActivityNum(activityId, "????????????", detailContrastNew, detailContrastOld, contrastCycleNum);
            contrastActivityNum(activityId, "????????????", detailThisNew, detailThisOld, thisCycleNum);
            contrastActivityNum(activityId, "????????????", detailInfluenceNew, detailInfluenceOld, influenceCycleNum);

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

        String function = "???????????????????????????????????????????????????????????????????????????????????????>>>";

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

        checkPercent(activeName, data.getJSONObject("contrast_cycle"), "????????????");
        checkPercent(activeName, data.getJSONObject("this_cycle"), "????????????");
        checkPercent(activeName, data.getJSONObject("influence_cycle"), "????????????");
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
            throw new Exception("activity_name=" + activityName + "??????????????????" + time + "????????????=" + newRatio + "???????????????=" + oldRatio + "????????????100%");
        }
    }

    public void contrastActivityNum(String activityId, String time, int detailNew, int detailOld, int contrastAccmulated) throws Exception {

        if (detailNew + detailOld > contrastAccmulated) {
            throw new Exception("activity_id=" + activityId + "," + time + "????????????????????????=" + detailNew +
                    "?????????=" + detailOld + "?????????????????????????????????????????????????????????=" + contrastAccmulated + "].");
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
            throw new Exception("??????????????????>>>????????????uv=" + uv + "??????????????????uv=" + totalUv);
        }

        //totalTime?????????ms????????????min
        int averageUseTimeRes = (int) Math.ceil((double) totalTime / (double) (60000 * uv));

        if (averageUseTime != averageUseTimeRes) {
            throw new Exception("??????????????????>>>?????????????????????????????????=" + averageUseTime + "], ??????=" + averageUseTimeRes + "]");
        }

        int averageDayUvRes = (int) Math.ceil((double) totalUv / (double) validDays);

        if (averageDayUv != averageDayUvRes) {
            throw new Exception("??????????????????>>>?????????????????????????????????=" + averageDayUv + ", ??????=" + averageDayUvRes);
        }
    }

    private void checkAgeGenderPercent(String function, JSONObject jo) throws Exception {

        DecimalFormat df = new DecimalFormat("0.00");

//        ?????????????????????
        String maleRatioStr = jo.getString("male_ratio_str");
        String femaleRatioStr = jo.getString("female_ratio_str");

        if (!"-".equals(maleRatioStr)) {
            float maleD = Float.valueOf(maleRatioStr.substring(0, maleRatioStr.length() - 1));

            float femaleD = Float.valueOf(femaleRatioStr.substring(0, femaleRatioStr.length() - 1));

            if ((int) (maleD + femaleD) != 100) {
                throw new Exception(function + "?????????=" + maleRatioStr + ",?????????=" + femaleRatioStr + "????????????100%");
            }

//        ????????????????????????????????????

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
                        throw new Exception("?????????0???" + ageGroups[i] + "?????????=" + percents[i]);
                    }
                }
            }

            for (int i = 0; i < percents.length; i++) {
                float percent = (float) nums[i] / (float) total * 100;
                String percentStr = df.format(percent);

                percentStr += "%";

                if (!percentStr.equals(percents[i])) {
                    throw new Exception(function + "????????????=" + percentStr + ", ????????????=" + percents[i]);
                }
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
        String percentageStr = "";
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            percentageStr = single.getString("percentage_str");

//            ???percentage_str="-"????????????????????????????????????????????????????????????????????????????????????0.0%
            if ("-".equals(percentageStr)) {
                return;
            }

            percentageStrs[i] = percentageStr.substring(0, percentageStr.length() - 1);
            nums[i] = single.getInteger("num");
            typeNamesRes[i] = single.getString("type_name");
            total += nums[i];
        }

        if (total == 0) {
            for (int i = 0; i < nums.length; i++) {
                if (!"-".equals(percentageStrs[i])) {
                    throw new Exception(typeNames[i] + "?????????0???" + "????????????" + percentageStrs[i]);
                }
            }
        }

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String actualStr = df.format(actual);

            String resStr = df.format(Double.parseDouble(percentageStrs[i]));

            if (!actualStr.equals(resStr)) {
                throw new Exception(function + "type_name: " + typeNamesRes[i] + " ?????????????????????????????????????????????" + resStr + ",?????????" + actualStr);
            }
        }
    }

    private void checkPercentAccuracy(JSONObject data, int num, String message) throws Exception {
        JSONArray list = data.getJSONArray("list");
        String lengthStr = "";
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String type = single.getString("type");
            String percentStr = single.getString("percentage_str");

            if (!"-".equals(percentStr)) {
                lengthStr = percentStr.substring(percentStr.indexOf(".") + 1, percentStr.indexOf("%"));
                if (lengthStr.length() > num) {
                    throw new Exception(message + ",type=" + type + "?????????????????????:" + lengthStr.length() + "???,?????????????????????" + num + "???");
                }
            }
        }
    }

    private int getDayofWeek(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date));

        return cal.get(Calendar.DAY_OF_WEEK);
    }


    //    ----------------------------------?????????????????????---------------------------------------------------------------

//    ------------------------------------------??????????????????-----------------------------------------------------------

    public JSONObject shopList() throws Exception {

        String path = "/yuexiu/shop/list";
        String json = "{}";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

//---------------------------------------------????????????????????????-----------------------------------------------------------

    public JSONObject realTimeData(String path) throws Exception {
        String json = "{\"shop_id\":" + SHOP_ID_DAILY + "}";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

//    --------------------------------------??????????????????????????????----------------------------------------------------

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

//    -------------------------------------------------------??????????????????------------------------------------------------------------------

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

//    ------------------------------------------------------????????????---------------------------------------------------------------------

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
            throw new Exception("?????????????????????status???" + status + ",path:" + path);
        }
    }

//    ---------------------------------------------------??????????????????---------------------------------------------------------------

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "?????????http????????????" + "\n" + e;
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
            failReason = "http post ???????????????url = " + queryUrl + "\n" + e;
            return response;
            //throw new RuntimeException("http post ???????????????url = " + queryUrl, e);
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
            failReason = "http post ???????????????url = " + queryUrl + "\n" + e;
            return response;
            //throw new RuntimeException("http post ???????????????url = " + queryUrl, e);
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
        aCase.setQaOwner("?????????");
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
            dingPush("???????????? \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        if (!DEBUG) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.dailyRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (!DEBUG && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 ?????????
            //15011479599 ?????????
            String[] rd = {"15011479599"};
            alarmPush.alarmToRd(rd);
        }
    }

    /**
     * ?????????????????? ??????????????????????????????initHttpConfig????????????authorization ????????????????????????????????????
     *
     * @ ??????
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
            aCase.setFailReason("http post ???????????????url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, "????????????authentication");
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
