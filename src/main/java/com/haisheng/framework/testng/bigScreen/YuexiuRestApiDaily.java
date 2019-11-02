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
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import com.sun.org.apache.bcel.internal.generic.NEW;
import okhttp3.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.lang.model.element.VariableElement;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author : xiezhidong
 * @date :  2019/10/12  14:55
 */

public class YuexiuRestApiDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private Case aCase = new Case();

    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-daily-test/buildWithParameters?case_name=";

    private String loginPathDaily = "/yuexiu-login";
    private String jsonDaily = "{\"username\":\"demo@winsense.ai\",\"passwd\":\"fe01ce2a7fbac8fafaed7c982a04e229\"}";
    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlrp7pqozlrqREZW1vIiwidWlkIjoidWlkXzdmYzc4ZDI0IiwibG9naW5UaW1lIjoxNTcxNTM3OTYxMjU4fQ.lmIXi-cmw3VsuD6RZrPZDJw70TvWuozEtLqV6yFHXVY";

    /**
     * http工具 maven添加以下配置
     * <dependency>
     * <groupId>com.arronlong</groupId>
     * <artifactId>httpclientutil</artifactId>
     * <version>1.0.4</version>
     * </dependency>
     */
    private HttpConfig config;

    private final static String REAL_TIME_PREFIX = "/yuexiu/data/statistics/real-time/"; //2

    private final static String HISTORY_PREFIX = "/yuexiu/data/statistics/history/";//3

    private final static String REGION_DATA_PREFIX = "/yuexiu/data/statistics/region/";//4

    private final static String CUSTOMER_DATA_PREFIX = "/yuexiu/data/statistics/customer/";//5

    private final static String MANAGE_CUSTOMER_PREFIX = "/yuexiu/manage/customer/";//6

    private final static String ANALYSIS_DATA_PREFIX = "/yuexiu/data/statistics/analysis/";//8

    private final static String MANAGE_STAFF_PREFIX = "/yuexiu/manage/staff/";//9

    /**
     * 测试环境使用以下customerId  正式环境不确定哪些reId一定存在
     */

    /**
     * 环境   线上为 ONLINE 测试为 DAILY
     */
    private boolean DEBUG = false;

    private long SHOP_ID_DAILY = 4116;

    String URL_PREFIX = "http://123.57.114.36";

    private String startTime = LocalDate.now().minusDays(7).toString();
    private String endTime = LocalDate.now().toString();

    //    -----------------------------------------------一、登录------------------------------------------------
//    -----------------------------------------------门店选择---------------------------------------------
    @Test(dataProvider = "SHOP_LIST_NOT_NULL")
    public void shopListNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店选择>>>";
        String path = "/yuexiu/shop/list";
        String json = "{}";
        String resStr = null;
        try {
            resStr = httpPost(path, json, StatusCode.SUCCESS);
            JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {

            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

//    -----------------------------------------------二、实时统计接口--------------------------------------------------------
//--------------------------------------------------2.1 门店实时客流统计-----------------------------------------------------

    //    校验data内数据非空(仅需校验data内数据)
    @Test(dataProvider = "REAL_TIME_SHOP_DATA_NOT_NULL")
    public void realTimeShopDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店实时客流统计>>>";

        try {
            JSONObject data = realTimeShop();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    //验证pv，uv，stay_time均大于0，pv>=uv,stay_time<=600
    @Test(dataProvider = "REAL_TIME_SHOP_DATA_VALIDITY")
    public void realTimeShopDataValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店实时客流统计>>>";
        try {
            JSONObject data = realTimeShop();
            checkKeyValues(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

//--------------------------------------2.2 区域实时人数--------------------------------------------------

    @Test(dataProvider = "REAL_TIME_REGION_DATA_NOT_NULL")
    public void realTimeRegionDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域实时人数>>>";
        try {
            JSONObject data = realTimeRegions();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REAL_TIME_REGION_REGIONS_NOT_NULL")
    public void realTimeRegionRegionsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域实时人数>>>";
        try {
            JSONObject data = realTimeRegions();
            JSONArray regions = data.getJSONArray("regions");
            for (int i = 0; i < regions.size(); i++) {
                JSONObject jsonObject = regions.getJSONObject(i);
                checkNotNull(function, jsonObject, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REAL_TIME_REGION_REGIONS_VALIDITY")
    public void realTimeRegionRegionsValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域实时人数>>>";
        try {
            JSONObject data = realTimeRegions();
            JSONArray regions = data.getJSONArray("regions");
            for (int i = 0; i < regions.size(); i++) {
                JSONObject jsonObject = regions.getJSONObject(i);
                checkDeepKeyValidity(function, jsonObject, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key);
        }
    }

//    --------------------------------------------2.3 全场累计客流---------------------------------

    @Test(dataProvider = "REAL_TIME_PERSONS_ACCUMULATED_NOT_NULL")
    public void realTimePersonsAccumulatedDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "全场累计客流>>>";

        try {
            JSONObject data = realTimeAccumulated();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REAL_TIME_PERSONS_ACCUMULATED_VALIDITY")
    public void realTimePersonsAccumulatedValueValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "全场累计客流>>>";

        try {
            JSONObject data = realTimeAccumulated();
            checkDeepKeyValidity(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key);
        }
    }

    @Test
    public void realTimePersonsAccumulatedChainRatio() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "全场累计客流>>>";

        try {
            JSONObject data = realTimeAccumulated();

            checkChainRatio(function, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验环比数");
        }
    }

//    ---------------------------------2.4 全场客流年龄/性别分布------------------------------------------------

    @Test(dataProvider = "REAL_TIME_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    public void realTimeAgeGenderDistributionNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "全场年龄性别分布>>>";

        try {
            JSONObject data = realTimeAgeGenderDistribution();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeAgeGenderDistributionPercent() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "全场年龄性别分布>>>";

        try {
            JSONObject data = realTimeAgeGenderDistribution();
            checkAgeGenderPercent(function, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "验证比例之和是否为1");
        }
    }

//    -----------------------------------------2.5 实时客流身份分布-------------------------------

    @Test(dataProvider = "REAL_TIME_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    public void realTimeCustomerTypeDistribution(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时客流身份分布>>>";

        try {
            JSONObject data = realTimeCustomerTypeDistribution();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeCustomerTypeDistributionPercent() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时客流身份分布>>>";

        try {
            JSONObject data = realTimeCustomerTypeDistribution();
            checkCustomerTypeRate(data, function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验百分比之和是否为1 ");
        }
    }

//    ---------------------------------2.6 实时出入口客流流量排行-------------------------------------------

    @Test(dataProvider = "REAL_TIME_ENTRANCE_RANK_NOT_NULL")
    public void realTimeEntranceRankNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时出入口客流流量排行>>>";

        try {
            JSONObject data = realTimeEntranceRankDistribution();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeEntranceRankRank() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时出入口客流流量排行>>>";

        try {
            JSONObject data = realTimeEntranceRankDistribution();
            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验是否正确排序！");
        }
    }

//    -------------------------------------2.7 实时热力图------------------------------

    @Test(dataProvider = "REAL_TIME_REGION_THERMAL_MAP_DATA_NOT_NULL")
    public void realTimeRegionThermalMapDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时热力图>>>";

        try {
            JSONObject data = realTimeThermalMapDistribution();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REAL_TIME_REGION_THERMAL_MAP_REGIONS_NOT_NULL")
    public void realTimeRegionThermalMapRegionsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时热力图>>>";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONArray regions = data.getJSONArray("regions");
            for (int i = 0; i < regions.size(); i++) {
                JSONObject single = regions.getJSONObject(i);
                checkNotNull(function, single, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REAL_TIME_REGION_THERMAL_MAP_THERMAL_MAP_NOT_NULL")
    public void realTimeRegionThermalMapThermalMapNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时热力图>>>";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONObject thermalMap = data.getJSONObject("thermal_map");

            checkNotNull(function, thermalMap, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REAL_TIME_REGION_THERMAL_MAP_REGIONS_VALIDITY")
    public void realTimeRegionThermalMapRegionsValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时热力图>>>";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONArray regions = data.getJSONArray("regions");

            for (int i = 0; i < regions.size(); i++) {
                JSONObject single = regions.getJSONObject(i);
                checkDeepKeyValidity(function, single, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key);
        }
    }

    @Test(dataProvider = "REAL_TIME_REGION_THERMAL_MAP_THERMAL_MAP_VALIDITY")
    public void realTimeRegionThermalMapThermalMapValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "实时热力图>>>";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONObject thermalMap = data.getJSONObject("thermal_map");

            checkDeepKeyValidity(function, thermalMap, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key);
        }
    }

    //    ------------------------------------7.2 门店实时游逛深度------------------------------------------------
    @Test(dataProvider = "REAL_TIME_WANDER_DEPTH_DATA_NOT_NULL")
    public void realTimeWanderDepthDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店实时游逛深度>>>";

        try {
            JSONObject data = realTimeWanderDepth();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }


//------------------------------------------三、历史数据统计--------------------------------------------------
//    ------------------------------------3.1 门店历史客流统计-----------------------------------------------

    @Test(dataProvider = "HISTORY_SHOP_NOT_NULL")
    public void historyShop(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店历史客流统计>>>";

        try {
            JSONObject data = historyShop(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "HISTORY_SHOP_DATA_VALIDITY")
    public void historyShopDataValidity(String key) {


        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店历史客流统计>>>";
        try {
            JSONObject data = historyShop(startTime, endTime);

            checkKeyValues(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

//    --------------------------------------------3.2 区域历史人数------------------------------------------

    @Test(dataProvider = "HISTORY_REGION_DATA_NOT_NULL")
    public void historyRegionDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域历史人数>>>";

        try {
            JSONObject data = historyRegion(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "HISTORY_REGION_REGIONS_NOT_NULL")
    public void historyRegionRegionsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域历史人数>>>";
        try {
            JSONObject data = historyRegion(startTime, endTime);
            JSONArray regions = data.getJSONArray("regions");
            for (int i = 0; i < regions.size(); i++) {
                JSONObject jsonObject = regions.getJSONObject(i);
                checkNotNull(function, jsonObject, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "HISTORY_REGION_REGIONS_VALIDITY")
    public void historyRegionRegionsValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域历史人数>>>";
        try {
            JSONObject data = historyRegion(startTime, endTime);
            JSONArray regions = data.getJSONArray("regions");
            for (int i = 0; i < regions.size(); i++) {
                JSONObject jsonObject = regions.getJSONObject(i);
                checkDeepKeyValidity(function, jsonObject, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key);
        }
    }

    //    --------------------------------------------3.3 历史累计客流------------------------------------------
    @Test(dataProvider = "HISTORY_PERSONS_ACCUMULATED_NOT_NULL")
    public void historyPersonsAccumulated(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史累计客流>>>";

        try {
            JSONObject data = historyAccumulated(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "HISTORY_PERSONS_ACCUMULATED_VALIDITY")
    public void historyPersonsAccumulatedValueValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史累计客流>>>";

        try {
            JSONObject data = historyAccumulated(startTime, endTime);
            checkDeepKeyValidity(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key);
        }
    }

    @Test
    public void hidtoryPersonsAccumulatedChainRatio() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史累计客流>>>";

        try {
            JSONObject data = historyAccumulated(startTime, endTime);

            checkHistoryChainRatio(function, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验环比数");
        }
    }


//    -------------------------------3.4 历史全场客流年龄/性别分布---------------------------------------------

    @Test(dataProvider = "HISTORY_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    public void historyAgeGenderDistributionNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史全场客流年龄/性别分布>>>";

        try {
            JSONObject data = historyAgeGenderDistribution(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyAgeGenderRate() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史客流年龄性别分布>>>";
        try {
            JSONObject data = historyAgeGenderDistribution(startTime, endTime);
            checkAgeGenderRate(data, function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

//    ------------------------------------3.5 历史客流身份分布--------------------------------------------

    @Test(dataProvider = "HISTORY_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    public void historyCustomerTypeDistributionNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史客流身份分布>>>";

        try {
            JSONObject data = historyCustomerTypeDistribution(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyCustomerTypeRate() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史客流身份分布>>>";
        try {
            JSONObject data = historyCustomerTypeDistribution(startTime, endTime);
            checkCustomerTypeRate(data, function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

//    --------------------------------------3.6 历史出入口客流量排行---------------------------------------

    @Test(dataProvider = "HISTORY_ENTRANCE_RANK_NOT_NULL")
    public void historyEntranceRank(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史出入口客流量排行>>>";

        try {
            JSONObject data = historyEntranceRank(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyEntranceRankRank() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史出入口客流量排行>>>";

        try {
            JSONObject data = historyEntranceRank(startTime, endTime);

            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验是否正确排序！");
        }
    }

//    --------------------------------------------3.7 区域历史人数环比---------------------------------------------

    @Test(dataProvider = "HISTORY_REGION_CYCLE_LIST_NOT_NULL")
    public void historyRegionCycle(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域历史人数环比>>>";

        try {
            JSONObject data = historyRegionCycle(startTime, endTime);
            JSONArray list = data.getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                checkNotNull(function, single, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyRegionCycleCheckChainRatio() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域历史人数环比>>>";

        try {
            JSONObject data = historyRegionCycle(startTime, endTime);
            JSONArray list = data.getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                checkHistoryCycleChainRatio(function, single);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验环比数是否正确！");
        }
    }

//    ------------------------------------7.4 门店历史客流游逛深度统计-------------------------------------------

    @Test(dataProvider = "HISTORY_WANDER_DEPTH_DATA_NOT_NULL")
    public void historyWanderDepthDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "门店历史客流游逛深度统计>>>";

        JSONObject data;

        try {

            data = historyWanderDepth(startTime, endTime);

            if ("[statistics_data]-time".equals(key)) {
                data = historyWanderDepth(endTime, endTime);
            }

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    //    --------------------------------------------八、顾客洞察------------------------------------------------

//    ----------------------------------------------8.1 顾客分析身份列表----------------------------------------------

    @Test(dataProvider = "ANALYSIS_CUSTOMER_TYPE_LIST_NOT_NULL")
    public void analysisCustomerTypeListNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "顾客分析身份列表>>>";

        JSONObject data;

        try {

            data = analysisCustomerTypeList();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

//    ------------------------------------------------------8.2 客群质量分析------------------------------------------

    @Test(dataProvider = "ANALYSIS_CUSTOMER_QUALITY_NOT_NULL")
    public void analysisCustomerQualityNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "客群质量分析>>>";

        JSONObject data;

        try {

            data = analysisCustomerQuality(startTime, endTime, "HIGH_ACTIVE");

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

//    -------------------------------------------8.3 顾客分析----------------------------------------------

    @Test(dataProvider = "ANALYSIS_CUSTOMER_TYPE_NOT_NULL")
    public void analysisCustomerTypeNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "顾客分析>>>";

        JSONObject data;

        try {

            data = analysisCustomerType(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    //    -------------------------------------8.4 顾客生命周期--------------------------------------------
    @Test(dataProvider = "ANALYSIS_CUSTOMER_LIFE_CYCLE_DATA_NOT_NULL")
    public void manalysisCustomerLifeCycleDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "顾客生命周期>>>";

        JSONObject data;

        try {

            data = analysisCustomerlifeCycle(startTime, endTime, startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }


    //    ---------------------------------------四、单人轨迹数据 ---------------------------------------------------

//    -----------------------------------4.2 区域人物轨迹--------------------------------------------

    @Test(dataProvider = "CUSTOMER_TRACE_DATA_NOT_NULL")
    public void customerTraceDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String functionPre = "查询顾客信息|";
        String function = "";

        try {
            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size(); i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONArray appearList = manageCustomerDayAppearList(customerId).getJSONArray("list");

                for (int j = 0; j < appearList.size(); j++) {
                    String startTime = appearList.getString(j);
                    startTime = startTime.replace("/", "-");
                    JSONObject customerTraceData = customerTrace(startTime, startTime, customerId);
                    function = functionPre + customerId + ">>>" + startTime + ">>>";
                    checkNotNull(function, customerTraceData, key);
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + key, functionPre + "校验" + key + "非空！");
        }
    }

    @Test(dataProvider = "CUSTOMER_TRACE_TRACES_NOT_NULL")
    public void customerTraceTracesNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String functionPre = "查询顾客信息>>>";
        String function = "";

        try {

            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size(); i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONArray appearList = manageCustomerDayAppearList(customerId).getJSONArray("list");

                for (int j = 0; j < appearList.size(); j++) {
                    String startTime = appearList.getString(j);
                    startTime = startTime.replace("/", "-");
                    JSONObject traceData = customerTrace(startTime, startTime, customerId);
                    function = functionPre + customerId + ">>>" + startTime + ">>>";
                    checkJANotNull(function, traceData, "traces");

                    JSONArray traces = traceData.getJSONArray("traces");

                    for (int k = 0; k < traces.size(); k++) {
                        JSONObject singleTrace = traces.getJSONObject(k);
                        checkNotNull(function, singleTrace, key);
                    }
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验" + key + "非空！");
        }
    }

    @Test(dataProvider = "CUSTOMER_TRACE_TRACES_VALIDITY")
    public void customerTraceTracesValidity(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String functionPre = "查询顾客信息>>>";
        String function = "";

        try {

            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size(); i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONArray appearList = manageCustomerDayAppearList(customerId).getJSONArray("list");

                for (int j = 0; j < appearList.size(); j++) {
                    String startTime = appearList.getString(j);
                    startTime = startTime.replace("/", "-");
                    JSONObject traceData = customerTrace(startTime, startTime, customerId);
                    function = functionPre + customerId + ">>>" + startTime + ">>>";
                    checkJANotNull(function, traceData, "traces");

                    JSONArray traces = traceData.getJSONArray("traces");

                    for (int k = 0; k < traces.size(); k++) {
                        JSONObject singleTrace = traces.getJSONObject(k);
                        checkDeepKeyValidity(function, singleTrace, key);
                    }
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "校验" + key);
        }
    }

//    -------------------------------------四、区域客流数据--------------------------------------
//-------------------------------------------5.1 区域单向客流--------------------------------------

    @Test(dataProvider = "MOVING_DIRECTION_REGIONS_NOT_NULL")
    public void movingDirectionRegionsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域单向客流>>>";

        try {
            JSONObject data = regionMovingDirection(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "MOVING_DIRECTION_RELATIONS_NOT_NULL")
    public void movingDirectionRelationsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域单向客流>>>";

        try {
            JSONObject data = regionMovingDirection(startTime, endTime);

            checkNotNull(function, data, "relations");

            JSONArray relations = data.getJSONArray("relations");

            for (int i = 0; i < relations.size(); i++) {
                JSONObject single = relations.getJSONObject(i);
                checkNotNull(function, single, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void moveDirectionRate() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "区域单向客流-各区域客流进出比例之和是否为1>>>";

        try {
            JSONObject data = regionMovingDirection(startTime, endTime);
            checkDirectionRate(data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

//    -----------------------------------5.2 客流进入排行----------------------------------------------

    @Test(dataProvider = "REGION_ENTER_RANK_NOT_NULL")
    public void regionDataEnterRank(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "客流进入排行>>>";

        try {
            JSONObject data = regionEnterRank(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void regionEnterRankTestRank() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "客流进入排行>>>";
        try {
            JSONObject data = regionEnterRank(startTime, endTime);

            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }


//    ----------------------------------------5.3 区域交叉客流----------------------------------------

    @Test(dataProvider = "REGION_CROSS_DATA_REGIONS_NOT_NULL")
    public void regionCrossDataRegionsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "区域交叉客流>>>";

        try {
            JSONObject data = regionCrossData(startTime, endTime);

            checkJANotNull(function, data, "regions");

            JSONArray regions = data.getJSONArray("regions");

            for (int i = 0; i < regions.size(); i++) {
                JSONObject single = regions.getJSONObject(i);
                checkNotNull(function, single, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "REGION_CROSS_DATA_RELATIONS_NOT_NULL")
    public void regionCrossDataRelationsNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "区域交叉客流>>>";

        try {
            JSONObject data = regionCrossData(startTime, endTime);

            checkNotNull(function, data, "relations");

            JSONArray relations = data.getJSONArray("relations");

            for (int i = 0; i < relations.size(); i++) {
                JSONObject single = relations.getJSONObject(i);
                checkNotNull(function, single, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

//    --------------------------------5.4 热门动线排行-----------------------------------------------

    @Test(dataProvider = "REGION_MOVE_LINE_RANK_NOT_NULL")
    public void regionDataMoveLineRank(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "热门动线排行>>>";

        try {
            JSONObject data = regionMoveLineRank(startTime, endTime);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test
    public void regionMoveLineRankTestRank() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "热门动线排行>>>";

        try {
            JSONObject data = regionMoveLineRank(startTime, endTime);

            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function + "检测是否正确排序！");
        }
    }

//    --------------------------------------------六、顾客管理--------------------------------------------------

    @Test(dataProvider = "CUSTOMER_TYPE_LIST_NOT_NULL")
    public void customerTypeListNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "顾客身份列表>>>";

        try {
            JSONObject data = manageCustomerTypeList();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "AGE_GROUP_LIST_NOT_NULL")
    public void ageGroupListNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "年龄分组>>>";

        try {
            JSONObject data = manageCustomerAgeGroupList();

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "MANAGE_CUSTOMER_LIST_NOT_NULL")
    public void manageCustomerListNotNullCheck(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "顾客列表>>>";

        try {
            String customerType = "HIGH_ACTIVE";
            String gender = "MALE";
            String ageGroupId = "11";
            JSONObject data = manageCustomerList(customerType, gender, ageGroupId);

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "MANAGE_CUSTOMER_FACE_LIST_NOT_NULL")
    public void manageCustomerFaceListNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "人脸搜索顾客列表>>>";

        try {

            String imagePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\yu.jpg";
            imagePath = imagePath.replace("\\", File.separator);
            JSONObject uploadPictureData = uploadPicture(imagePath);

            JSONObject data = manageCustomerFaceList(uploadPictureData.getString("pic_url"));

            checkNotNull(function, data, key);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "MANAGE_CUSTOMER_DETAIL_DATA_NOT_NULL")
    public void manageCustomerDetailDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "顾客详情>>>";

        try {

            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size(); i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");
                JSONObject data = manageCustomerDetail(customerId);
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    @Test(dataProvider = "MANAGE_CUSTOMER_DAY_APPEAR_DATA_NOT_NULL")
    public void manageCustomerDayAppearDataNotNull(String key) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        String function = "顾客出现日期分页列表>>>";

        try {
            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size(); i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");
                JSONObject data = manageCustomerDayAppearList(customerId);
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
        }
    }

    //    ---------------------------------------附、交叉验证-------------------------------------------------------
    @Test
    public void realTimeRegionLessThanTotal() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "概述,各区域累计人数小于总体的人数>>>";

        try {

            JSONObject shopData = realTimeShop();

            JSONObject regionData = realTimeRegions();

            compareRegionUvTotalUv(shopData, regionData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

    @Test
    public void historyRegionLessThanTotal() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "历史统计,各区域累计人数小于总体的人数>>>";

        try {

            JSONObject shopData = historyShop(startTime, endTime);

            JSONObject regionData = historyRegion(startTime, endTime);

            compareRegionUvTotalUv(shopData, regionData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

    @Test
    public void AccumulatedEqualsShop() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "全场累计客流的当前时段累计人数等于概述中的总人数";

        try {
            JSONObject accumulatedDataJo = realTimeAccumulated();
            JSONObject shopDataJo = realTimeShop();

            compareAccumulatedToShop(shopDataJo, accumulatedDataJo);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

    @Test
    public void shopHistoryEqualsRealTime() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "当天区域单向/交叉客流中的人数等于概述中的总人数";

        try {

            String startTime = LocalDate.now().toString();

            //区域单向客流中的pv,uv,stay_time用的是历史统计的接口
            JSONObject historyShopDataJo = historyShop(startTime, startTime);
            JSONObject realTimeShopDataJo = realTimeShop();

            compareHistoryToRealTimeShop(realTimeShopDataJo, historyShopDataJo);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

    @Test
    public void realTimeRegionEqualsRegionEnterRank() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "概述中的区域uv，与区域单向客流-客流进入区域排行中的uv相等";

        try {

            String startTime = LocalDate.now().toString();

            JSONObject realTimeRegions = realTimeRegions();
            JSONObject regionEnterRank = regionEnterRank(startTime, startTime);

            comparerealTimeRegionRegionEnterRank(realTimeRegions, regionEnterRank);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, function);
        }
    }

    private void comparerealTimeRegionRegionEnterRank(JSONObject realTimeRegions, JSONObject regionEnterRank) throws Exception {

        JSONArray list = regionEnterRank.getJSONArray("list");
        JSONArray regions = realTimeRegions.getJSONArray("regions");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String regionId = single.getString("region_id");
            int num = single.getInteger("num");
            boolean isExist = false;
            for (int j = 0; j < regions.size(); j++) {
                JSONObject singleRegion = regions.getJSONObject(j);
                String regionId1 = singleRegion.getString("region_id");
                int pv = singleRegion.getJSONObject("statistics").getInteger("pv");
                if (regionId.equals(regionId1)) {
                    isExist = true;
                    if (num != pv) {
                        String regionName = singleRegion.getString("region_name");
                        throw new Exception("区域单人动线-客流进入区域排行中的" + regionName + "区域的客流:" + num +
                                ",与概述中该区域客流:" + pv + "不相等");
                    }
                }
            }

            if (!isExist) {
                throw new Exception("该regionId不存在：" + regionId);
            }

        }
    }

    private void compareHistoryToRealTimeShop(JSONObject shopDataJo, JSONObject movingDirectionData) throws Exception {
        int shopUv = shopDataJo.getInteger("uv");
        int shopPv = shopDataJo.getInteger("pv");
        int shopStayTime = shopDataJo.getInteger("stay_time");

        int movingUv = movingDirectionData.getInteger("uv");
        if (shopUv != movingUv) {
            throw new Exception("区域单人/交叉动线中的uv:" + movingUv + "不等于概述的uv:" + shopUv);
        }

        int movingPv = movingDirectionData.getInteger("pv");
        if (shopPv != movingPv) {
            throw new Exception("区域单人/交叉动线中的pv:" + movingPv + "不等于概述的pv:" + shopPv);
        }

        int movingStayTime = movingDirectionData.getInteger("stay_time");
        if (shopStayTime != movingStayTime) {
            throw new Exception("区域单人/交叉动线中的stay_time:" + movingStayTime + "不等于概述的stay_time:" + shopStayTime);
        }

    }

    public void compareRegionUvTotalUv(JSONObject shopDataJo, JSONObject regionDataJo) throws Exception {
        int totalUv = shopDataJo.getInteger("uv");

        JSONArray regions = regionDataJo.getJSONArray("regions");
        checkNotNull("区域实时人数", regionDataJo, "regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject single = regions.getJSONObject(i);
            JSONObject statistics = single.getJSONObject("statistics");
            int regionUv = statistics.getInteger("uv");
            if (regionUv > totalUv) {
                String regionName = single.getString("region_name");
                throw new Exception(regionName + "的累计人数：" + regionUv + ", 大于总体的累计人数：" + totalUv);
            }
        }
    }

    public void compareAccumulatedToShop(JSONObject shopDataJo, JSONObject AccumulatedDataJo) throws Exception {
        int totalUv = shopDataJo.getInteger("uv");

        JSONArray statisticsData = AccumulatedDataJo.getJSONArray("statistics_data");
        checkNotNull("全场累计客流>>>", AccumulatedDataJo, "statistics_data");

        JSONObject lastData = statisticsData.getJSONObject(statisticsData.size() - 1);

        int realTime = lastData.getInteger("real_time");

        String label = lastData.getString("label");

        if (totalUv != realTime) {
            throw new Exception("全场累计客流>>>" + label + "的实时人数：" + realTime + ", 大于总体的累计人数：" + totalUv);
        }
    }

    private void checkHistoryChainRatio(String function, JSONObject data) throws Exception {
        JSONArray statisticsData = data.getJSONArray("statistics_data");
        for (int i = 0; i < statisticsData.size(); i++) {
            JSONObject single = statisticsData.getJSONObject(i);
            String label = single.getString("label");
            double realTime = single.getDouble("present_cycle");
            double history = single.getDouble("last_cycle");
            String chainRatio = single.getString("chain_ratio");
            chainRatio = chainRatio.substring(0, chainRatio.length() - 1);
            double expectRatio = 0d;

            if (history > 0) {
                expectRatio = (realTime - history) / history * 100.0d;
                DecimalFormat df = new DecimalFormat("0.00");
                String expectRatioStr = df.format(expectRatio);
                if (!expectRatioStr.equals(chainRatio)) {
                    throw new Exception(function + label + "-期待环比数：" + expectRatioStr + ",实际：" + chainRatio);
                }
            }
        }
    }

    private void checkAgeGenderRate(JSONObject data, String function) throws Exception {
        JSONArray list = data.getJSONArray("list");

        if (list == null || list.size() != 12) {
            throw new Exception("年龄性别分布的类别为空，或者是不是12个分类。");
        }

        String[] ageGrp = new String[12];
        String[] percents = new String[12];
        int[] nums = new int[12];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String percent = single.getString("percent");
            percents[i] = percent.substring(0, percent.length() - 1);
            nums[i] = single.getInteger("num");
            ageGrp[i] = single.getString("age_group");
            total += nums[i];
        }

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String actualStr = df.format(actual);

            if (!percents[i].equals(actualStr)) {
                throw new Exception(function + "age_group: " + ageGrp[i] + " 对应的年龄性别比例错误！返回：" + percents[i] + ",实际：" + actualStr);
            }
        }
    }

    private void checkChainRatio(String function, JSONObject data) throws Exception {
        JSONArray statisticsData = data.getJSONArray("statistics_data");
        for (int i = 0; i < statisticsData.size(); i++) {
            JSONObject single = statisticsData.getJSONObject(i);
            String label = single.getString("label");
            double realTime = single.getDouble("real_time");
            double history = single.getDouble("history");
            String chainRatio = single.getString("chain_ratio");
            chainRatio = chainRatio.substring(0, chainRatio.length() - 1);
            double expectRatio = 0d;

            if (history > 0) {
                expectRatio = (realTime - history) / history * 100.0d;
                DecimalFormat df = new DecimalFormat("0.00");
                String expectRatioStr = df.format(expectRatio);
                if (expectRatio > 0) {
                    expectRatioStr = "+" + expectRatioStr;
                }
                if (!expectRatioStr.equals(chainRatio)) {
                    throw new Exception(function + label + "-期待环比数：" + expectRatioStr + ",实际：" + chainRatio);
                }
            }
        }
    }

    private void checkAgeGenderPercent(String function, JSONObject jo) throws Exception {

        JSONArray list = jo.getJSONArray("list");

        int[] nums = new int[list.size()];
        String[] percents = new String[list.size()];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            int num = single.getInteger("num");
            nums[i] = num;
            String percent = single.getString("percent");
            percents[i] = percent;
            total += num;
        }

        for (int i = 0; i < percents.length; i++) {
            float percent = (float) nums[i] / (float) total * 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String percentStr = df.format(percent);

            percentStr += "%";

            if (!percentStr.equals(percents[i])) {
                throw new Exception(function + "期待比例：" + percentStr + ", 实际：" + percents[i]);
            }
        }
    }

    private String getCustomerTraceJson(String startTime, String endTime, String customerId) {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"\n" +
                        "}";

        return json;
    }

    private void checkCustomerTypeRate(JSONObject data, String function) throws Exception {
        JSONArray list = data.getJSONArray("list");

        JSONArray typeList = manageCustomerTypeList().getJSONArray("list");
        String[] typeNames = new String[typeList.size()];
        for (int i = 0; i < typeList.size(); i++) {
            typeNames[i] = typeList.getJSONObject(i).getString("type_name");
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

        Assert.assertEquals(typeNamesRes, typeNames, "返回的顾客类型与期待的不相符--返回：" +
                Arrays.toString(typeNamesRes) + ",期待：" + Arrays.toString(typeNames));

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String actualStr = df.format(actual);

            if (!percentageStrs[i].equals(actualStr)) {
                throw new Exception(function + "type_name: " + typeNamesRes[i] + " 对应的客流身份比例错误！返回：" + percentageStrs[i] + ",实际：" + actualStr);
            }
        }
    }

    private void checkHistoryCycleChainRatio(String function, JSONObject single) throws Exception {
        String regionName = single.getString("region_name");

        JSONObject statistics = single.getJSONObject("statistics");

        double realTime = statistics.getDoubleValue("present_cycle");
        double history = statistics.getDoubleValue("last_cycle");
        String chainRatio = statistics.getString("chain_ratio");
        chainRatio = chainRatio.substring(0, chainRatio.length() - 1);
        double expectRatio = 0d;

        if (history > 0) {
            expectRatio = (realTime - history) / history * 100.0d;
            DecimalFormat df = new DecimalFormat("0.00");
            String expectRatioStr = df.format(expectRatio);
            if (!expectRatioStr.equals(chainRatio)) {
                throw new Exception(function + regionName + "-期待环比数：" + expectRatioStr + ",实际：" + chainRatio);
            }
        }
    }

    private void checkDirectionRate(JSONObject data) throws Exception {
        JSONArray relations = data.getJSONArray("relations");

        for (int i = 0; i < relations.size(); i++) {
            JSONObject relation = relations.getJSONObject(i);
            JSONArray directionList = relation.getJSONArray("direction_list");
            if (directionList != null) {
                int[] nums = new int[directionList.size()];
                String[] ratios = new String[directionList.size()];
                String[] regionIds = new String[directionList.size()];
                int total = 0;
                for (int j = 0; j < directionList.size(); j++) {
                    JSONObject direction = directionList.getJSONObject(j);
                    nums[j] = direction.getInteger("num");
                    String retioStr = direction.getString("ratio");
                    ratios[j] = retioStr.substring(0, retioStr.length() - 1);
                    regionIds[j] = direction.getString("region_id");
                    total += nums[j];
                }

                for (int k = 0; k < nums.length; k++) {
                    double actual = ((double) nums[k] / (double) total) * (double) 100;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String actualStr = df.format(actual);

                    if (!ratios[k].equals(actualStr)) {
                        throw new Exception("region_id: " + regionIds[k] + " 对应的区域动线比例错误！返回：" + ratios[k] + ",期待：" + actualStr);
                    }
                }
            }
        }
    }

    private void checkRank(JSONObject data, String arrayKey, String key, String function) throws Exception {

        JSONArray ja = data.getJSONArray(arrayKey);

        if (!(ja == null || ja.size() == 0)) {

            int[] nums = new int[ja.size()];

            for (int i = 0; i < ja.size(); i++) {
                JSONObject single = ja.getJSONObject(i);

                nums[i] = single.getInteger(key);

                if (i > 0) {
                    if (nums[i - 1] < nums[i]) {
                        throw new Exception(function + "没有正确排序！目前排序：" + Arrays.toString(nums));
                    }
                }
            }
        }
    }

    private void checkKeyValue(String function, JSONObject jo, String key, String value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        String valueRes = jo.getString(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        } else {
            if (valueRes == null || "".equals(valueRes)) {
                throw new Exception(function + "---" + key + "字段值为空！");
            }
        }
    }

    private void checkKeyMoreOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDouble(key);


        if (!(valueRes >= value)) {
            throw new Exception(key + "字段，应该>=" + value + "实际返回的value为：" + value);
        }
    }

    private void checkKeyLessOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDouble(key);

        if (!(valueRes <= value)) {
            throw new Exception(key + "字段，应该>=" + value + "实际返回的value为：" + value);
        }
    }

    private void checkKeyLessOrEqualKey(JSONObject jo, String key1, String key2, String function) throws Exception {

        checkNotNull(function, jo, key1);
        checkNotNull(function, jo, key2);

        double value1 = jo.getDouble(key1);
        double value2 = jo.getDouble(key2);

        //防止取值方面出现问题，value为空的时候也是不符合的
        if (!(value1 <= value2)) {
            throw new Exception(function + key1 + "=" + value1 + "，应该<=" + key2 + "=" + value2);
        }
    }

    private void checkKeyValues(String function, JSONObject jo, String... keyValues) throws Exception {

        for (String keyValue : keyValues) {
            //            注意其他判断与=判断的顺序
            if (keyValue.contains("[<=]")) {
                String key1 = keyValue.substring(0, keyValue.indexOf("["));
                String key2 = keyValue.substring(keyValue.indexOf("]") + 1);
                checkKeyLessOrEqualKey(jo, key1, key2, function);
            } else if (keyValue.contains(">=")) {
                String key = keyValue.substring(0, keyValue.indexOf(">"));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);
                checkKeyMoreOrEqualValue(function, jo, key, Double.valueOf(value));
            } else if (keyValue.contains("<=")) {
                String key = keyValue.substring(0, keyValue.indexOf("<"));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);
                checkKeyLessOrEqualValue(function, jo, key, Double.valueOf(value));
            } else if (keyValue.contains("=")) {
                String key = keyValue.substring(0, keyValue.indexOf("="));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);

                checkKeyValue(function, jo, key, value, true);

            }
        }
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        int code = resJo.getInteger("code");
        message += resJo.getString("message");

        if (expect != code) {
            throw new Exception(message + " expect code: " + expect + ",actual: " + code);
        }
    }

    private String getRealTimeParamJson() {

        return "{\"shop_id\":" + SHOP_ID_DAILY + "}";
    }

    private String getHistoryParamJson(String startTime, String endTime) {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"\n" +
                        "}";

        return json;
    }

    //    顾客列表json
    private String getCustomerListParamJson(String customerType, String gender, String ageGroupId) {

        String json =
                "{\n";
        if (!"".equals(customerType)) {
            json += "    \"customer_type\":\"" + customerType + "\",\n";
        }

        if (!"".equals(gender)) {
            json += "    \"gender\":\"" + gender + "\",\n";
        }

        if (!"".equals(gender)) {
            json += "    \"age_group_id\":\"" + ageGroupId + "\",\n";
        }

        json += "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                "    \"page\":1,\n" +
                "    \"size\":100\n" +
                "}";

        return json;
    }

    private String getstaffAddParamJson(String name, String phone, String faceUrl, String staffType) {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"staff_name\":\"" + name + "\",\n" +
                        "    \"phone\":\"" + phone + "\",\n" +
                        "    \"face_url\":\"" + faceUrl + "\",\n" +
                        "    \"staff_type\":\"" + staffType + "\"" +
                        "}\n";

        return json;
    }

    private String getIpPort() {

        return URL_PREFIX;
    }

    private void checkJONotNull(String function, JSONObject jo, String key) throws Exception {

        if (jo.containsKey(key)) {
            JSONObject value = jo.getJSONObject(key);
            if (value == null || value.size() == 0) {
                throw new Exception(function + "返回结果中" + key + "对应的值为空");
            }
        } else {
            throw new Exception(function + "返回结果中不包含该key：" + key);
        }
    }

    private void checkJANotNull(String function, JSONObject jo, String key) throws Exception {

        if (jo.containsKey(key)) {
            JSONArray value = jo.getJSONArray(key);
            if (value == null || value.size() == 0) {
                throw new Exception(function + "返回结果中" + key + "对应的值为空");
            }
        } else {
            throw new Exception(function + "返回结果中不包含该key：" + key);
        }
    }

    private void checkStrNotNull(String function, JSONObject jo, String key) throws Exception {

        if (jo.containsKey(key)) {
            String value = jo.getString(key);
            if (value == null || "".equals(value)) {
                throw new Exception(function + "返回结果中" + key + "对应的值为空");
            }
        } else {
            throw new Exception(function + "返回结果中不包含该key：" + key);
        }
    }

    private void checkNotNull(String function, JSONObject jo, String key) throws Exception {

        if (key.contains("]-")) {
            String parentKey = key.substring(1, key.indexOf("]"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkDeepArrKeyNotNull(function, jo, parentKey, childKey);
        } else if (key.contains("}-")) {
            String parentKey = key.substring(1, key.indexOf("}"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkDeepObjectKeyNotNull(function, jo, parentKey, childKey);
        } else if (key.contains("]")) {
            key = key.substring(1, key.length() - 1);
            checkJANotNull(function, jo, key);
        } else if (key.contains("}")) {
            key = key.substring(1, key.length() - 1);
            checkJONotNull(function, jo, key);
        } else {
            checkStrNotNull(function, jo, key);
        }
    }

    private void checkDeepKeyValidity(String function, JSONObject jo, String key) throws Exception {

        if (key.contains("]-")) {
            String parentKey = key.substring(1, key.indexOf("]"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkArrKeyValidity(function, jo, parentKey, childKey);

        } else if (key.contains("}-")) {
            String parentKey = key.substring(1, key.indexOf("}"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkObjectKeyValidity(function, jo, parentKey, childKey);

        }
    }

    private void checkArrKeyValidity(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        JSONArray parent = jo.getJSONArray(parentKey);
        for (int i = 0; i < parent.size(); i++) {
            JSONObject single = parent.getJSONObject(i);
            checkKeyValues(function, single, childKey);
        }
    }

    private void checkObjectKeyValidity(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        JSONObject parent = jo.getJSONObject(parentKey);
        checkKeyValues(function, parent, childKey);
    }


    private void checkDeepArrKeyNotNull(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        checkJANotNull(function, jo, parentKey);
        JSONArray parent = jo.getJSONArray(parentKey);
        for (int i = 0; i < parent.size(); i++) {
            JSONObject single = parent.getJSONObject(i);
            checkKeyValue(function, single, childKey, "", false);
        }
    }

    private void checkDeepObjectKeyNotNull(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        checkJONotNull(function, jo, parentKey);

        JSONObject parent = jo.getJSONObject(parentKey);

        checkKeyValue(function, parent, childKey, "", false);
    }

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
            //throw new RuntimeException("初始化http配置异常", e);
        }
        //String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPost(String path, String json, int expectCode) throws Exception {
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

    private String httpDelete(String url, String json, int expectCode) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();

        executor.doDeleteJsonWithHeaders(url, json, null);

        checkCode(executor.getResponse(), expectCode, "");
        return executor.getResponse();
    }

    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000 && key col not null");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("越秀日常 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        if (!DEBUG) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.onlineMonitorPvuvAlarm(msg);
        }
        Assert.assertNull(aCase.getFailReason());

    }

//    ----------------------------------调用接口的方法---------------------------------------------------------------

//---------------------------------------------二、今日实时统计-----------------------------------------------------------

    public JSONObject realTimeShop() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "shop";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeRegions() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "region";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeAccumulated() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "persons/accumulated";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeAgeGenderDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "age-gender/distribution";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeCustomerTypeDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "customer-type/distribution";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeEntranceRankDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "entrance/rank";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeThermalMapDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "region/thermal_map";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeWanderDepth() throws Exception {
        String path = REAL_TIME_PREFIX + "wander-depth";

        String json = getRealTimeParamJson();

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    --------------------------------------三、历史数据统计----------------------------------------------------

    public JSONObject historyShop(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "shop";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyRegion(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "region";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyAccumulated(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "persons/accumulated";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyAgeGenderDistribution(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "age-gender/distribution";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyCustomerTypeDistribution(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "customer-type/distribution";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyEntranceRank(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "entrance/rank";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyRegionCycle(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "region/cycle";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyWanderDepth(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "wander-depth";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    ----------------------------------------四、单人轨迹数据----------------------------------

    public JSONObject customerTrace(String startTime, String endTime, String customerId) throws Exception {
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String json = getCustomerTraceJson(startTime, endTime, customerId);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    --------------------------------------五、区域客流数据-----------------------------------------

    public JSONObject regionMovingDirection(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "moving-direction";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject regionEnterRank(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "enter/rank";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject regionCrossData(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "cross-data";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject regionMoveLineRank(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "move-line/rank";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    ----------------------------------六、顾客管理------------------------------------------------------

    public JSONObject manageCustomerTypeList() throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerAgeGroupList() throws Exception {
        String path = "/yuexiu/manage/age/group/list";

        String json = getRealTimeParamJson();

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerList(String customerType, String gender, String ageGroupId) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "list";

        String json = getCustomerListParamJson(customerType, gender, ageGroupId);

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject uploadPicture(String imagePath) {
        String url = URL_PREFIX + "/yuexiu/upload/picture";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "pic_data",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            checkCode(this.response, StatusCode.SUCCESS, file.getName() + ">>>");
            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.getMessage();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
    }

    public JSONObject manageCustomerFaceList(String faceUrl) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "face/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"face_url\":\"" + faceUrl + "\"\n" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerDetail(String customerId) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "detail";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + "," +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerDayAppearList(String customerId) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "day/appear/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + "," +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerEdit(String customerId, String customerName, String faceUrl, String signedType) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "edit/" + customerId;

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"shop_id\":10,\n" +
                        "    \"face_url\":\"" + faceUrl + "\",\n" +
                        "    \"signed_type\":\"" + signedType + "\"\n" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    //    ---------------------------------------------八、顾客洞察-----------------------------------------
    public JSONObject analysisCustomerTypeList() throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer-type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject analysisCustomerQuality(String startTime, String endTime, String customerType) throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer-quality";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"customer_type\":\"" + customerType + "\"\n" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject analysisCustomerType(String startTime, String endTime) throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer/type";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject analysisCustomerlifeCycle(String startTime, String endTime, String influenceStart, String influenceEnd) throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer/life-cycle";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"," +
                        "    \"influence_start\":\"" + influenceStart + "\",\n" +
                        "    \"influence_end\":\"" + influenceEnd + "\"" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    //    -----------------------------九、员工管理接口---------------------------------------------------------
    public JSONObject staffTypeList() throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffAdd(String name, String phone, String faceUrl, String staffType) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "add";

        String json = getstaffAddParamJson(name, phone, faceUrl, staffType);

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffpage(String namePhone, String faceUrl, String staffType) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "page";

        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100,\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"staff_type\":\"" + staffType + "\",\n" +
                        "    \"name_phone\":\"" + namePhone + "\",\n" +
                        "    \"face_url\":\"" + faceUrl + "\"" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffDetail(String id) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "detail";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffAttendancePage(String id) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "attendance/page";

        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100,\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffEdit(String id, String name, String phone, String faceUrl, String staffType) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "edit/" + id;

        String json = getstaffAddParamJson(name, phone, faceUrl, staffType);

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject attendanceEdit(String attendanceId, String comments) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "attendance/edit/" + attendanceId;

        String json =
                "{\"comments\":\"" + comments + "\"}";

        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject attendanceDelete(String attendanceId) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "delete/" + attendanceId;

        String json =
                "{}";

        String resStr = httpDelete(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
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

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

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

        saveData(aCase, caseName, "登录获取authentication");
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    //    -------------------------------------1.3 门店选择-----------------------------------------------------
    @DataProvider(name = "SHOP_LIST_NOT_NULL")
    private static Object[] shopListNotNull() {
        return new Object[]{
                "[list]-shop_id",
                "[list]-shop_name",
                "[list]-icon"
        };
    }


//    ---------------------------------------------2.1 门店实时客流统计------------------------------------------

    @DataProvider(name = "REAL_TIME_SHOP_DATA_NOT_NULL")
    private static Object[] realTimeShopNotNull() {
        return new Object[]{
                "uv",
                "pv",
                "stay_time",
                "uv_cycle_ratio"
        };
    }

    @DataProvider(name = "REAL_TIME_SHOP_DATA_VALIDITY")
    private static Object[] realTimeShopDataValidity() {
        return new Object[]{
                "uv>=0",
                "pv>=0",
                "stay_time>=0",
                "uv[<=]pv",
                "stay_time<=600"

        };
    }

    //---------------------------------------------2.2 区域实时人数--------------------------------------------

    @DataProvider(name = "REAL_TIME_REGION_DATA_NOT_NULL")
    private static Object[] realTimeRegionDataNotNull() {
        return new Object[]{
                "[regions]",
                "map_url",
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_REGIONS_NOT_NULL")
    private static Object[] realTimeRegionNotNull() {
        return new Object[]{
                "{stay_num}-num",
                "{stay_num}-rank",
                "[location]",
                "region_name",
                "statistics",
                "{statistics}-uv",
                "{statistics}-pv",
                "{statistics}-stay_time",
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_REGIONS_VALIDITY")
    private static Object[] realTimeRegionValidity() {
        return new Object[]{
                "{stay_num}-num>=0",
                "{stay_num}-rank>=1",
                "[location]-x>=0",
                "[location]-x<=1",
                "[location]-y>=0",
                "[location]-y<=1",
                "{statistics}-uv>=0",
                "{statistics}-pv>=0",
                "{statistics}-uv[<=]pv",
                "{statistics}-stay_time>=0",
                "{statistics}-stay_time<=600",
        };
    }

    //    -------------------------------2.3 全场累计客流（实时）----------------------------

    @DataProvider(name = "REAL_TIME_PERSONS_ACCUMULATED_NOT_NULL")
    private static Object[] realTimePersonsAccumulatedNotNull() {
        return new Object[]{
                "[statistics_data]-real_time",
                "[statistics_data]-history",
                "[statistics_data]-chain_ratio",
                "[statistics_data]-label",
                "[statistics_data]-time",
                "last_statistics_time"
        };
    }

    @DataProvider(name = "REAL_TIME_PERSONS_ACCUMULATED_VALIDITY")
    private static Object[] realTimePersonsAccumulatedValidity() {
        return new Object[]{
                "[statistics_data]-real_time>=0",
                "[statistics_data]-history>=0",
        };
    }

    //    -----------------------------2.4 全场客流年龄/性别分布--------------------------------------
    @DataProvider(name = "REAL_TIME_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    private static Object[] realTimeAgeGenderDIstributionNotNull() {
        return new Object[]{
                "[list]-age_group",
                "[list]-gender",
                "[list]-percent",
                "[list]-ratio",
        };
    }

    //------------------------------------------2.5 实时客流身份分布--------------------------------
    @DataProvider(name = "REAL_TIME_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    private static Object[] realTimeCustomerTypeDistributionNotNull() {
        return new Object[]{
                "[list]-type",
                "[list]-type_name",
                "[list]-percentage",
                "[list]-percentage_str",
        };
    }

//    ------------------------------------2.6 实时出入口客流流量排行------------------------------------------------

    @DataProvider(name = "REAL_TIME_ENTRANCE_RANK_NOT_NULL")
    private static Object[] realTimeEntranceRankNotNull() {
        return new Object[]{
                "[list]-entrance_id",
                "[list]-entrance_name",
                "[list]-num",
                "[list]-action"
        };
    }

//    ------------------------------------2.7 实时热力图---------------------------------------

    @DataProvider(name = "REAL_TIME_REGION_THERMAL_MAP_DATA_NOT_NULL")
    private static Object[] realTimeRegionThermalMapDataNotNull() {
        return new Object[]{
                "[regions]",
                "map_url",
                "{thermal_map}"
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_THERMAL_MAP_REGIONS_NOT_NULL")
    private static Object[] realTimeRegionThermalMapRegionsNotNull() {
        return new Object[]{
                "region_name",
                "[location]"
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_THERMAL_MAP_THERMAL_MAP_NOT_NULL")
    private static Object[] realTimeRegionThermalMapThermalMapNotNull() {
        return new Object[]{
                "start_time",
                "end_time",
                "width",
                "height",
                "max_value",
                "points",
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_THERMAL_MAP_REGIONS_VALIDITY")
    private static Object[] realTimeRegionThermalMapRegionsValidity() {
        return new Object[]{
                "[location]-x>=0",
                "[location]-x<=1",
                "[location]-y>=0",
                "[location]-y<=1"
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_THERMAL_MAP_THERMAL_MAP_VALIDITY")
    private static Object[] realTimeRegionThermalMapThermalMapValidity() {
        return new Object[]{
                "[points]-x>=0",
                "[points]-x<=1",
                "[points]-y>=0",
                "[points]-y<=1"
        };
    }

    //    ----------------------------------------7.2 门店实时游逛深度---------------------------------------------------
    @DataProvider(name = "REAL_TIME_WANDER_DEPTH_DATA_NOT_NULL")
    private static Object[] realTimeWanderDepthDataNotNull() {
        return new Object[]{
                "[statistics_data]-time", "[statistics_data]-real_time", "[statistics_data]-history", "[statistics_data]-chain_ratio"
        };
    }

    //---------------------------------------3.1 门店历史客流统计---------------------------
    @DataProvider(name = "HISTORY_SHOP_NOT_NULL")
    private static Object[] historyShopNotNull() {
        return new Object[]{
                "uv",
                "pv",
                "stay_time",
                "uv_per_day"
        };
    }

    @DataProvider(name = "HISTORY_SHOP_DATA_VALIDITY")
    private static Object[] historyShopDataValidity() {
        return new Object[]{
                "uv>=0",
                "pv>=0",
                "stay_time>=0",
                "uv[<=]pv",
                "stay_time<=600"

        };
    }

    //    --------------------------------3.2 区域历史人数------------------------------------
    @DataProvider(name = "HISTORY_REGION_DATA_NOT_NULL")
    private static Object[] historyRegionNotNull() {
        return new Object[]{
                "[regions]",
                "map_url"
        };
    }

    @DataProvider(name = "HISTORY_REGION_REGIONS_NOT_NULL")
    private static Object[] historyRegionRegionsNotNull() {
        return new Object[]{
                "[location]",
                "region_name",
                "{statistics}-uv",
                "{statistics}-pv",
                "{statistics}-stay_time",
        };
    }

    @DataProvider(name = "HISTORY_REGION_REGIONS_VALIDITY")
    private static Object[] historyRegionValidity() {
        return new Object[]{
                "{statistics}-stay_time>=0",
                "[location]-x>=0",
                "[location]-x<=1",
                "[location]-y>=0",
                "[location]-y<=1",
                "{statistics}-uv>=0",
                "{statistics}-pv>=0",
                "{statistics}-uv[<=]pv",
                "{statistics}-stay_time>=0",
                "{statistics}-stay_time<=600",
        };
    }

    //    ----------------------------3.3 历史累计客流-----------------------------------

    @DataProvider(name = "HISTORY_PERSONS_ACCUMULATED_NOT_NULL")
    private static Object[] historyPersonsAccumulatedNotNull() {
        return new Object[]{
                "last_statistics_time",
                "[statistics_data]-time",
                "[statistics_data]-present_cycle",
                "[statistics_data]-last_cycle",
                "[statistics_data]-chain_ratio",
                "[statistics_data]-label",
        };
    }

    @DataProvider(name = "HISTORY_PERSONS_ACCUMULATED_VALIDITY")
    private static Object[] historyPersonsAccumulatedValidity() {
        return new Object[]{
                "[statistics_data]-present_cycle>=0",
                "[statistics_data]-last_cycle>=0",
        };
    }

    //    ---------------------------------------3.4 全场客流年龄性别分布---------------------------------------
    @DataProvider(name = "HISTORY_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    private static Object[] historyAgeGenderDistributionNotNull() {
        return new Object[]{
                "[list]-age_group",
                "[list]-gender",
                "[list]-ratio",
                "[list]-percent"
        };
    }

    //    -------------------------------3.5 历史客流身份分布--------------------------------------
    @DataProvider(name = "HISTORY_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    private static Object[] historyCustomerTypeDistributionNotNull() {
        return new Object[]{
                "[list]-type",
                "[list]-type_name",
                "[list]-percentage",
                "[list]-percentage_str"
        };
    }

    //    ---------------------------------3.6 历史出入口客流量排行--------------------------------------
    @DataProvider(name = "HISTORY_ENTRANCE_RANK_NOT_NULL")
    private static Object[] historyEntranceRankNotNull() {
        return new Object[]{
                "[list]-entrance_id",
                "[list]-entrance_name",
                "[list]-num",
                "[list]-action",
        };
    }

//    ------------------------------3.7 区域历史人数环比----------------------------

    @DataProvider(name = "HISTORY_REGION_CYCLE_LIST_NOT_NULL")
    private static Object[] historyRegionCycleNotNull() {
        return new Object[]{
                "region_id",
                "region_name",
                "{statistics}-present_cycle",
                "{statistics}-last_cycle",
                "{statistics}-chain_ratio"
        };
    }

//    --------------------------------门店历史客流游逛深度统计-----------------------------------

    @DataProvider(name = "HISTORY_WANDER_DEPTH_DATA_NOT_NULL")
    private static Object[] historyWanderDepthNotNull() {
        return new Object[]{
                "[statistics_data]-time", "[statistics_data]-present_cycle", "[statistics_data]-last_cycle",
                "[statistics_data]-chain_ratio", "[statistics_data]-label"
        };
    }

//    4.1 查询顾客信息

    @DataProvider(name = "CUSTOMER_DETAIL_NOT_NULL")
    private static Object[] customerDataDetailNotNull() {
        return new Object[]{
                "first_appear_time",
                "last_appear_time",
                "stay_time_per_times",
                "sex",
                "face_url",
                "customer_type_name",
                "customer_id",
                "age",
                "shop_id"
        };
    }

    @DataProvider(name = "CUSTOMER_DETAIL_VALIDITY")
    private static Object[] customerDataDS() {
        return new Object[]{
                "first_appear_time>=1",
                "stay_time_per_times>=1",
                "stay_time_per_times<=300",
                "first_appear_time[<=]last_appear_time"
        };
    }

//    ----------------------------------------4.2 区域人物轨迹--------------------------------------

    @DataProvider(name = "CUSTOMER_TRACE_DATA_NOT_NULL")
    private static Object[] customerTraceNotNull() {
        return new Object[]{
                "last_query_time", "[regions]-region_id", "map_url",
//                "[region_turn_points]-region_a", "[region_turn_points]-region_b",
                "[moving_lines]-source", "[moving_lines]-target", "[moving_lines]-move_times"
        };
    }

    @DataProvider(name = "CUSTOMER_TRACE_TRACES_NOT_NULL")
    private static Object[] customerTraceTracesNotNull() {

        return new Object[]{
                "{location}-x", "{location}-y", "{location}-region_id", "face_url", "time"
        };
    }


    @DataProvider(name = "CUSTOMER_TRACE_TRACES_VALIDITY")
    private static Object[] customerTraceTracesValidity() {

        return new Object[]{
                "{location}-x>=0", "{location}-x<=1", "{location}-y>=0", "{location}-y<=1"
        };
    }

//-----------------------------------------5.1 区域单向客流----------------------------------------

    @DataProvider(name = "MOVING_DIRECTION_REGIONS_NOT_NULL")
    private static Object[] movingDirectionRegionsNotNull() {
        return new Object[]{
                "[regions]-region_id",
                "[regions]-location",
                "[regions]-region_name",
                "[regions]-region_type"
        };
    }

    @DataProvider(name = "MOVING_DIRECTION_RELATIONS_NOT_NULL")
    private static Object[] movingDirectionRelationsNotNull() {
        return new Object[]{
                "[direction_list]-region_id",
                "[direction_list]-ratio",
                "[direction_list]-num",
                "region_id"
        };
    }


    //    --------------------------------5.2 客流进入排行------------------------------------------
    @DataProvider(name = "REGION_ENTER_RANK_NOT_NULL")
    private static Object[] regionEnterRankNotNull() {
        return new Object[]{
                "[list]-region_id",
                "[list]-region_name",
                "[list]-num",
        };
    }
//    --------------------------------5.3 区域交叉客流-----------------------------------

    @DataProvider(name = "REGION_CROSS_DATA_REGIONS_NOT_NULL")
    private static Object[] CrossDataRegionsNotNull() {
        return new Object[]{
                "region_id",
                "region_name",
                "region_type",
                "[location]"
        };
    }

    @DataProvider(name = "REGION_CROSS_DATA_RELATIONS_NOT_NULL")
    private static Object[] crossDataRelationsNotNull() {
        return new Object[]{
                "region_first",
                "region_second",
                "num"
        };
    }

//    --------------------------------------5.4 热门动线排行--------------------------------------------

    @DataProvider(name = "REGION_MOVE_LINE_RANK_NOT_NULL")
    private static Object[] regionMoveLineRankNotNull() {
        return new Object[]{
                "[list]-region_first",
                "[list]-region_second",
                "[list]-num",
        };
    }


//    ----------------------------------六、顾客管理-------------------------------------------------------

    //    ------------------------------------1、顾客身份列表-----------------------------------------
    @DataProvider(name = "CUSTOMER_TYPE_LIST_NOT_NULL")
    private static Object[] customerTypeListNotNull() {
        return new Object[]{
                "[list]-customer_type", "[list]-type_name"
        };
    }

    @DataProvider(name = "AGE_GROUP_LIST_NOT_NULL")
    private static Object[] ageGroupListNotNull() {
        return new Object[]{
                "[list]-age_group_id", "[list]-type_name"
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_LIST_NOT_NULL")
    private static Object[] manageCustomerListNotNull() {
        return new Object[]{
                "[list]-face_url", "[list]-show_url", "[list]-customer_id", "[list]-customer_type_name",
                "[list]-age_group", "[list]-gender", "[list]-last_visit_time"
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_FACE_LIST_NOT_NULL")
    private static Object[] manageCustomerFaceListNotNull() {
        return new Object[]{
                "[list]-face_url", "[list]-customer_id", "[list]-customer_name", "[list]-customer_type_name",
                "[list]-age_group", "[list]-gender", "[list]-last_visit_time"
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_DETAIL_DATA_NOT_NULL")
    private static Object[] manageCustomerDetailDataNotNull() {
        return new Object[]{
                "customer_id", "face_url", "show_url", "age_group_id",
                "age_group", "gender", "customer_type", "customer_type_name", "stay_time_per_times",
                "first_appear_time", "last_appear_time", "stay_times"
//                "[list]-customer_id", "[list]-customer_name", "[list]-face_url", "[list]-show_url", "[list]-age_group_id",
//                "[list]-age_group", "[list]-gender", "[list]-customer_type", "[list]-customer_type_name", "[list]-stay_time_per_times",
//                "[list]-first_appear_time", "[list]-last_appear_time", "[list]-stay_times"
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_DAY_APPEAR_DATA_NOT_NULL")
    private static Object[] manageCustomerDayAppearDataNotNull() {
        return new Object[]{
                "[list]"
        };
    }

//    ---------------------------------------八、顾客洞察------------------------------------------------------

    //    -------------------------8.1 顾客分析身份列表---------------------------------------------------------
    @DataProvider(name = "ANALYSIS_CUSTOMER_TYPE_LIST_NOT_NULL")
    private static Object[] analysisCustomerTypeListNotNull() {
        return new Object[]{
                "[list]-type", "[list]-type_name"
        };
    }

    //    ---------------------------8.2 客群质量分析--------------------------------------------------------------
    @DataProvider(name = "ANALYSIS_CUSTOMER_QUALITY_NOT_NULL")
    private static Object[] analysisCustomerQualityNotNull() {
        return new Object[]{

                "[return_customer_analysis]-visit_day", "[return_customer_analysis]-customer_num",
                "[return_customer_analysis]-customer_ratio", "[return_customer_analysis]-customer_ratio_str",

                "[stay_analysis]-stay_time", "[stay_analysis]-customer_num", "[stay_analysis]-customer_ratio", "[stay_analysis]-customer_ratio_str",

                "[cross_stay_return]-visit_day", "[cross_stay_return]-stay_time", "[cross_stay_return]-customer_num",
                "[cross_stay_return]-customer_ratio", "[cross_stay_return]-customer_ratio_str"
        };
    }

    //    -----------------------------------8.3 顾客分析-----------------------------------------------------------------
    @DataProvider(name = "ANALYSIS_CUSTOMER_TYPE_NOT_NULL")
    private static Object[] analysisCustomerTypeNotNull() {
        return new Object[]{
                "[new_customer_analysis]-type", "[new_customer_analysis]-type_name", "[new_customer_analysis]-customer_num",
                "[new_customer_analysis]-customer_ratio", "[new_customer_analysis]-customer_ratio_str",

                "[stay_customer_analysis]-type", "[stay_customer_analysis]-type_name", "[stay_customer_analysis]-customer_num",
                "[stay_customer_analysis]-customer_ratio", "[stay_customer_analysis]-customer_ratio_str",

                "[signed_analysis]-type", "[signed_analysis]-type_name", "[signed_analysis]-customer_num",
                "[signed_analysis]-customer_ratio", "[signed_analysis]-customer_ratio_str"
        };
    }

//    -------------------------------------8.4 顾客生命周期----------------------------------------------------------------

    @DataProvider(name = "ANALYSIS_CUSTOMER_LIFE_CYCLE_DATA_NOT_NULL")
    private static Object[] manalysisCustomerLifeCycleDataNotNull() {
        return new Object[]{
                "[customer_type]-type", "[customer_type]-type_name"
        };
    }

    @DataProvider(name = "ANALYSIS_CUSTOMER_LIFE_CYCLE_RELATIONS_NOT_NULL")
    private static Object[] manalysisCustomerLifeCycleRelationsNotNull() {
        return new Object[]{
                "type",
                "[direction_list]-type", "[direction_list]-type_name", "[direction_list]-ratio",
                "[direction_list]-ratio_str", "[direction_list]-num",
        };
    }
}
