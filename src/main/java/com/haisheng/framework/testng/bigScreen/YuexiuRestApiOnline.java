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
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
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

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * @author : xiezhidong
 * @date :  2019/10/12  14:55
 */
public class YuexiuRestApiOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_ONLINE_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-online-test/buildWithParameters?case_name=";

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlrp7pqozlrqREZW1vIiwidWlkIjoidWlkXzdmYzc4ZDI0IiwibG9naW5UaW1lIjoxNTcxNTM3OTYxMjU4fQ.lmIXi-cmw3VsuD6RZrPZDJw70TvWuozEtLqV6yFHXVY";

    //    private String loginPathOnline = "/yuexiu/login";
    private String loginPathOnline = "/yuexiu-login";
    private String jsonOnline = "{\"username\":\"yuexiu@yuexiu.com\",\"passwd\":\"f2c7219953b54583ea11065215f22a8b\"}";

    private HttpConfig config;

    private final static String REAL_TIME_PREFIX = "/yuexiu/data/statistics/real-time/"; //2

    private final static String HISTORY_PREFIX = "/yuexiu/data/statistics/history/";//3

    private final static String REGION_DATA_PREFIX = "/yuexiu/data/statistics/region/";//4

    private final static String CUSTOMER_DATA_PREFIX = "/yuexiu/data/statistics/customer/";//5

    private final static String MANAGE_CUSTOMER_PREFIX = "/yuexiu/manage/customer/";//6

    private final static String ANALYSIS_DATA_PREFIX = "/yuexiu/data/statistics/analysis/";//8

    private final static String MANAGE_STAFF_PREFIX = "/yuexiu/manage/staff/";//9

    private final static String MANAGE_ACTIVITY_PREFIX = "/yuexiu/manage/activity/";//11

    /**
     * 环境   线上为 ONLINE 测试为 DAILY
     */
    private String DEBUG = System.getProperty("DEBUG", "true");

    private long SHOP_ID_ENV = 889;

    String URL_PREFIX = "http://123.57.114.205";

    private String startTime = LocalDate.now().minusDays(1).toString();
    private String endTime = LocalDate.now().toString();

    private int pageSize = 10000;


    private static String RATIO_LIST = "ratio_list";
    private static String LAST_VERSION_LIST = "list";

    //    -----------------------------------------------一、登录------------------------------------------------
//    -----------------------------------------------门店选择---------------------------------------------
    @Test
    public void shopListNotNullCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "门店选择校验 \n";
        String key = "";
        try {

            JSONObject data = shopList();

            for (Object obj : shopListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {

            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -----------------------------------------------二、实时统计接口--------------------------------------------------------
//--------------------------------------------------2.1 门店实时客流统计-----------------------------------------------------

    //    校验data内数据非空(仅需校验data内数据)
    @Test
    public void realTimeShopDataNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "门店实时客流统计校验 \n";
        String key = "";

        try {
            JSONObject data = realTimeShop();

            for (Object obj : realTimeShopNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    //验证pv，uv，stay_time均大于0，pv>=uv,stay_time<=600
    @Test
    public void realTimeShopDataCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "门店实时客流校验 \n";
        try {
            JSONObject data = realTimeShop();
            for (Object obj : realTimeShopDataValidity()) {
                key = obj.toString();
                checkKeyValues(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//--------------------------------------2.2 区域实时人数--------------------------------------------------

    @Test
    public void realTimeRegionDataNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域实时人数校验 \n";
        try {
            JSONObject data = realTimeRegions();
            for (Object obj : realTimeRegionDataNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeRegionRegionsNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域实时人数校验 \n";
        try {
            JSONObject data = realTimeRegions();
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : realTimeRegionNotNull()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject jsonObject = regions.getJSONObject(i);
                    checkNotNull(function, jsonObject, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeRegionRegionsValidity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域实时人数校验 \n";
        try {
            JSONObject data = realTimeRegions();
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : realTimeRegionValidity()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject jsonObject = regions.getJSONObject(i);
                    checkDeepKeyValidity(function, jsonObject, key);
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

//    --------------------------------------------2.3 全场累计客流---------------------------------

    @Test
    public void realTimePersonsAccumulatedDataNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "全场累计客流校验 \n";

        try {
            JSONObject data = realTimeAccumulated();
            for (Object obj : realTimePersonsAccumulatedNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimePersonsAccumulatedValueValidity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "全场累计客流统计 \n";

        try {
            JSONObject data = realTimeAccumulated();
            for (Object object : realTimePersonsAccumulatedValidity()) {
                key = object.toString();
                checkDeepKeyValidity(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

    @Test
    public void realTimePersonsAccumulatedChainRatio() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "全场累计客流 \n";

        try {
            JSONObject data = realTimeAccumulated();

            checkChainRatio(function, "real_time", "history", true, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验环比数");
        }
    }

//    ---------------------------------2.4 全场客流年龄/性别分布------------------------------------------------

    @Test
    public void realTimeAgeGenderDistributionNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "全场年龄性别分布统计 \n";

        try {
            JSONObject data = realTimeAgeGenderDistribution();
            for (Object obj : realTimeAgeGenderDIstributionNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeAgeGenderDistributionPercent() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "全场年龄性别分布统计 \n";

        try {
            JSONObject data = realTimeAgeGenderDistribution();
            checkAgeGenderPercent(function, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "验证比例之和是否为1");
        }
    }

    /**
     * 实时年龄性别分布>>>验证男女会汇总比例之和是否为100%。
     **/
    @Test
    public void realTimeAgeGenderDistributionRatio() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "实时年龄性别分布统计 \n";

        try {
            JSONObject data = realTimeAgeGenderDistribution();
            checkAgeGenderRatio(function, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "验证男女会汇总比例之和是否为100。");
        }
    }

//    -----------------------------------------2.5 实时客流身份分布-------------------------------

    @Test
    public void realTimeCustomerTypeDistributionNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "实时客流身份分布不为空校验 \n";

        try {
            JSONObject data = realTimeCustomerTypeDistribution();
            for (Object obj : realTimeCustomerTypeDistributionNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeCustomerTypeDistributionPercent() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "实时客流身份分布百分比校验 \n";

        try {
            JSONObject data = realTimeCustomerTypeDistribution();
            checkCustomerTypeRate(data, function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验比例之和是否为1 ");
        }
    }

//    ---------------------------------2.6 实时出入口客流流量排行-------------------------------------------

    @Test
    public void realTimeEntranceRankNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-";
        String key = "";

        String function = "实时出入口客流流量排行不为空校验 \n";

        try {
            JSONObject data = realTimeEntranceRankDistribution();
            for (Object obj : realTimeEntranceRankNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -------------------------------------2.7 实时热力图------------------------------

    @Test
    public void realTimeRegionThermalMapDataNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "实时热力图统计不为空校验 \n";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            for (Object obj : realTimeRegionThermalMapDataNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeRegionThermalMapRegionsNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "实时热力图区域不为空校验 \n";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : realTimeRegionThermalMapRegionsNotNull()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject single = regions.getJSONObject(i);
                    checkNotNull(function, single, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeRegionThermalMapThermalMapNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "实时热力图map不为空校验 \n";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONObject thermalMap = data.getJSONObject("thermal_map");
            for (Object obj : realTimeRegionThermalMapThermalMapNotNull()) {
                key = obj.toString();
                checkNotNull(function, thermalMap, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void realTimeRegionThermalMapRegionsValidityCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "实时热力图location不为空校验 \n";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : realTimeRegionThermalMapRegionsValidity()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject single = regions.getJSONObject(i);
                    checkDeepKeyValidity(function, single, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

    @Test
    public void realTimeRegionThermalMapThermalMapValidityCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "实时热力图point校验 \n";

        try {
            JSONObject data = realTimeThermalMapDistribution();
            JSONObject thermalMap = data.getJSONObject("thermal_map");
            for (Object obj : realTimeRegionThermalMapThermalMapValidity()) {
                key = obj.toString();
                checkDeepKeyValidity(function, thermalMap, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

    //    ------------------------------------7.2 门店实时游逛深度------------------------------------------------
    @Test
    public void realTimeWanderDepthDataNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "门店实时游逛深度不为空校验 \n";

        try {
            JSONObject data = realTimeWanderDepth();
            for (Object obj : realTimeWanderDepthDataNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }


//------------------------------------------三、历史数据统计--------------------------------------------------
//    ------------------------------------3.1 门店历史客流统计-----------------------------------------------

    @Test
    public void historyShopCode1000() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "门店历史客流不为空校验 \n";

        try {
            JSONObject data = historyShopCode1000(startTime, endTime);
            for (Object obj : historyShopNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyShopDataValidityCheck() {


        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "门店历史客流统计 \n";
        try {
            JSONObject data = historyShopCode1000(startTime, endTime);
            for (Object obj : historyShopDataValidity()) {
                key = obj.toString();
                checkKeyValues(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    --------------------------------------------3.2 区域历史人数------------------------------------------

    @Test
    public void historyRegionDataNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域历史人数统计 \n";

        try {
            JSONObject data = historyRegion(startTime, endTime);
            for (Object obj : historyRegionNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyRegionRegionsNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域历史人数统计 \n";
        try {
            JSONObject data = historyRegion(startTime, endTime);
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : historyRegionRegionsNotNull()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject jsonObject = regions.getJSONObject(i);
                    checkNotNull(function, jsonObject, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyRegionRegionsValidity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域历史人数区域非空校验 \n";
        try {
            JSONObject data = historyRegion(startTime, endTime);
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : historyRegionValidity()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject jsonObject = regions.getJSONObject(i);
                    checkDeepKeyValidity(function, jsonObject, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

    //    --------------------------------------------3.3 历史累计客流------------------------------------------
    @Test
    public void historyPersonsAccumulated() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "历史累计客流非空校验 \n";

        try {
            JSONObject data = historyAccumulated(startTime, endTime);

            for (Object obj : historyPersonsAccumulatedNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyPersonsAccumulatedValueValidity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史累计客流数值校验 \n";
        String key = "";

        try {
            JSONObject data = historyAccumulated(startTime, endTime);
            for (Object obj : historyPersonsAccumulatedValidity()) {
                key = obj.toString();
                checkDeepKeyValidity(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

    @Test
    public void hidtoryPersonsAccumulatedChainRatio() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史累计客流百分比校验 \n";

        try {
            JSONObject data = historyAccumulated(startTime, endTime);

            checkChainRatio(function, "present_cycle", "last_cycle", false, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验环比数");
        }
    }


//    -------------------------------3.4 历史全场客流年龄/性别分布---------------------------------------------

    @Test
    public void historyAgeGenderDistributionNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "历史全场客流年龄/性别分布统计非空校验 \n";

        try {
            JSONObject data = historyAgeGenderDistribution(startTime, endTime);
            for (Object obj : historyAgeGenderDistributionNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyAgeGenderRate() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史客流年龄性别分布统计 \n";
        try {
            JSONObject data = historyAgeGenderDistribution(startTime, endTime);
            checkAgeGenderRate(data, function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "比例计算是否正确。");
        }
    }

    @Test
    public void historyAgeGenderRatio() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史客流年龄性别分布统计 \n";
        try {
            JSONObject data = historyAgeGenderDistribution(startTime, endTime);
            checkAgeGenderRatio(function, data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//    ------------------------------------3.5 历史客流身份分布--------------------------------------------

    @Test
    public void historyCustomerTypeDistributionNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "历史客流身份分布非空校验 \n";

        try {
            JSONObject data = historyCustomerTypeDistribution(startTime, endTime);
            for (Object obj : historyCustomerTypeDistributionNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyCustomerTypeRate() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史客流身份分布统计 \n";
        try {
            JSONObject data = historyCustomerTypeDistribution(startTime, endTime);
            checkCustomerTypeRate(data, function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//    --------------------------------------3.6 历史出入口客流量排行---------------------------------------

    @Test
    public void historyEntranceRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "历史出入口客流量排行统计 \n";

        try {
            JSONObject data = historyEntranceRank(startTime, endTime);
            for (Object obj : historyEntranceRankNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyEntranceRankRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "历史出入口客流量排行统计 \n";

        try {
            JSONObject data = historyEntranceRank(startTime, endTime);

            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验是否正确排序！");
        }
    }

//    --------------------------------------------3.7 区域历史人数环比---------------------------------------------

    @Test
    public void historyRegionCycle() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域历史人数环比统计 \n";

        try {
            JSONObject data = historyRegionCycle(startTime, endTime);
            JSONArray list = data.getJSONArray("list");
            for (Object obj : historyRegionCycleNotNull()) {
                key = obj.toString();
                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);
                    checkNotNull(function, single, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void historyRegionCycleCheckChainRatio() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "区域历史人数环比统计 \n";

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
            saveData(aCase, ciCaseName, caseName, function + "校验环比数是否正确！");
        }
    }

//    ------------------------------------7.4 门店历史客流游逛深度统计-------------------------------------------

    @Test
    public void historyWanderDepthDataNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "门店历史客流游逛深度统计 \n";
        String key = "";

        JSONObject data;

        try {

            data = historyWanderDepth(startTime, endTime);
            for (Object obj : historyWanderDepthNotNull()) {
                key = obj.toString();
                if ("[statistics_data]-time".equals(key)) {
                    JSONObject dataTime = historyWanderDepth(endTime, endTime);
                    checkNotNull(function, dataTime, key);
                } else {
                    checkNotNull(function, data, key);
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    //    ---------------------------------------四、单人轨迹数据 ---------------------------------------------------

//    -----------------------------------4.2 区域人物轨迹--------------------------------------------

    @Test
    public void customerTraceDataNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;

        String functionPre = "查询顾客信息|";
        String function = "";

        String key = "";
        try {
            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");
            Object[] keyList = customerTraceNotNull();
            int customerSize = customerList.size();
            if (customerList.size() > 5) {
                //10个人，耗时47秒，故改为检测5个人
                customerSize = 5;
            }
            for (int i = 0; i < customerSize; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONArray appearList = manageCustomerDayAppearList(customerId).getJSONArray("list");
                for (int j = 0; j < appearList.size(); j++) {
                    String startTime = appearList.getString(j);
                    startTime = startTime.replace("/", "-");
                    JSONObject customerTraceData = customerTrace(startTime, startTime, customerId);
                    function = functionPre + customerId + ">>>" + startTime + ">>>";
                    for (int index = 0; index < keyList.length; index++) {
                        key = keyList[index].toString();
                        checkNotNull(function, customerTraceData, key);
                    }
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, functionPre + "校验" + key + "非空！");
        }
    }

    @Test
    public void customerTraceTracesNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;

        String functionPre = "查询顾客信息校验 \n";
        String function = "";
        String key = "";

        try {

            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");
            Object[] keyList = customerTraceTracesNotNull();
            int customerSize = customerList.size();
            if (customerList.size() > 5) {
                customerSize = 5;
            }
            for (int i = 0; i < customerSize; i++) {
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
                        for (int index = 0; index < keyList.length; index++) {
                            key = keyList[index].toString();
                            checkNotNull(function, singleTrace, key);
                        }
                    }
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空！");
        }
    }

    @Test
    public void customerTraceTracesValidityTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String functionPre = "查询顾客信息校验 \n";
        String function = "";
        String key = "";

        try {

            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");
            Object[] keyList = customerTraceTracesValidity();
            int customerSize = customerList.size();
            if (customerList.size() > 5) {
                customerSize = 5;
            }
            for (int i = 0; i < customerSize; i++) {
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
                        for (int index = 0; index < keyList.length; index++) {
                            key = keyList[index].toString();
                            checkDeepKeyValidity(function, singleTrace, key);
                        }

                    }
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

//    -------------------------------------五、区域客流数据--------------------------------------
//-------------------------------------------5.1 区域单向客流--------------------------------------

    @Test
    public void movingDirectionRegionsNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "区域单向客流校验 \n";
        String key = "";

        try {
            JSONObject data = regionMovingDirection(startTime, endTime);
            Object[] keyList = movingDirectionRegionsNotNull();
            for (int index = 0; index < keyList.length; index++) {
                key = keyList[index].toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void movingDirectionRelationsNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "区域单向客流校验 \n";
        String key = "";

        try {
            Object[] keyList = movingDirectionRelationsNotNull();
            JSONObject data = regionMovingDirection(startTime, endTime);

            checkNotNull(function, data, "relations");

            JSONArray relations = data.getJSONArray("relations");

            for (int i = 0; i < relations.size(); i++) {
                JSONObject single = relations.getJSONObject(i);

                for (int index = 0; index < keyList.length; index++) {
                    key = keyList[index].toString();
                    checkNotNull(function, single, key);
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void moveDirectionRate() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        String function = "区域单向客流-各区域客流进出比例之和是否为1校验 \n";

        try {
            JSONObject data = regionMovingDirection(startTime, endTime);
            checkDirectionRate(data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//    -----------------------------------5.2 客流进入排行----------------------------------------------

    @Test
    public void regionDataEnterRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";


        String function = "客流进入排行校验 \n";

        try {
            JSONObject data = regionEnterRank(startTime, endTime);
            for (Object obj : regionEnterRankNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void regionEnterRankTestRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        String function = "客流进入排行校验 \n";
        try {
            JSONObject data = regionEnterRank(startTime, endTime);

            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//    ----------------------------------------5.3 区域交叉客流----------------------------------------

    @Test
    public void regionCrossDataRegionsNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "区域交叉客流校验 \n";

        try {
            JSONObject data = regionCrossData(startTime, endTime);
            checkJANotNull(function, data, "regions");
            JSONArray regions = data.getJSONArray("regions");
            for (Object obj : CrossDataRegionsNotNull()) {
                key = obj.toString();
                for (int i = 0; i < regions.size(); i++) {
                    JSONObject single = regions.getJSONObject(i);
                    checkNotNull(function, single, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void regionCrossDataRelationsNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";


        String function = "区域交叉客流校验 \n";

        try {
            JSONObject data = regionCrossData(startTime, endTime);
            checkNotNull(function, data, "relations");
            JSONArray relations = data.getJSONArray("relations");
            for (Object object : crossDataRelationsNotNull()) {
                key = object.toString();
                for (int i = 0; i < relations.size(); i++) {
                    JSONObject single = relations.getJSONObject(i);
                    checkNotNull(function, single, key);
                }
            }


        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    --------------------------------5.4 热门动线排行-----------------------------------------------

    @Test
    public void regionDataMoveLineRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";


        String function = "热门动线排行校验 \n";

        try {
            JSONObject data = regionMoveLineRank(startTime, endTime);
            for (Object obj : regionMoveLineRankNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void regionMoveLineRankTestRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        String function = "热门动线排行校验 \n";

        try {
            JSONObject data = regionMoveLineRank(startTime, endTime);

            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "检测是否正确排序！");
        }
    }

//    --------------------------------------------六、顾客管理--------------------------------------------------

    //    ---------------------------------------------6.1 顾客身份列表-------------------------------------------
    @Test
    public void customerTypeListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";


        String function = "顾客身份列表校验 \n";

        try {
            JSONObject data = manageCustomerTypeList();
            for (Object obj : customerTypeListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    //      ---------------------------------------------6.2 年龄分组-------------------------------------------
    @Test
    public void ageGroupListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";


        String function = "年龄分组校验 \n";

        try {
            JSONObject data = manageCustomerAgeGroupList();
            for (Object obj : ageGroupListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    //    ------------------------------------------------6.3 顾客列表---------------------------------------
    @Test
    public void manageCustomerListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";


        String function = "顾客列表校验 \n";

        try {
            JSONObject data = manageCustomerList("", "", "");
            for (Object obj : manageCustomerListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -----------------------------------------------6.5 顾客详情---------------------------------------------------

    @Test
    public void manageCustomerDetailDataNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客详情校验 \n";
        String key = "";

        try {
            Object[] keyList = manageCustomerDetailDataNotNull();

            JSONArray customerList = manageCustomerList("", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size() && i <= 100; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");
                JSONObject data = manageCustomerDetail(customerId);
                for (int index = 0; index < keyList.length; index++) {
                    key = keyList[index].toString();
                    checkNotNull(function, data, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void manageCustomerDetailValidityTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客详情校验 \n";
        String key = "";

        try {
            Object[] keyList = manageCustomerDetailValidity();

            JSONArray customerList = manageCustomerList("", "", "").getJSONArray("list");
            for (int i = 0; i < customerList.size() && i <= 100; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");
                JSONObject data = manageCustomerDetail(customerId);
                for (int index = 0; index < keyList.length; index++) {
                    key = keyList[index].toString();
                    checkKeyValues(function + customerId, data, key);
                }
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key);
        }
    }

    //    --------------------------------------------------6.6 顾客出现日期分页列表--------------------------------------
    @Test
    public void manageCustomerDayAppearDataNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        String function = "顾客出现日期分页列表校验 \n";
        String key = "";

        try {
            JSONArray customerList = manageCustomerList("", "", "").getJSONArray("list");
            Object[] keyList = manageCustomerDayAppearDataNotNull();
            int size = customerList.size();

            if (size > 60) {
                size = 60;
            }
            for (int i = 0; i < size; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");
                JSONObject data = manageCustomerDayAppearList(customerId);
                for (int index = 0; index < keyList.length; index++) {
                    key = keyList[index].toString();
                    checkNotNull(function + customerId + ">>", data, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    ----------------------------------------------8.1 顾客分析身份列表----------------------------------------------

    @Test
    public void analysisCustomerTypeListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";
        String function = "顾客分析身份列表校验 \n";
        JSONObject data;

        try {

            data = analysisCustomerTypeList();
            for (Object obj : analysisCustomerTypeListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    ------------------------------------------------------8.2 客群质量分析------------------------------------------

    @Test
    public void analysisCustomerQualityNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "客群质量分析校验 \n";

        JSONObject data;
        String key = "";

        try {

            data = analysisCustomerQualityCode1000(startTime, endTime, "HIGH_ACTIVE");
            Object[] keyList = analysisCustomerQualityNotNull();
            for (int index = 0; index < keyList.length; index++) {
                key = keyList[index].toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -------------------------------------------8.3 顾客分析----------------------------------------------

    @Test
    public void analysisCustomerTypeNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客分析校验 \n";

        JSONObject data;
        String key = "";

        try {

            data = analysisCustomerType(startTime, endTime);
            Object[] keyList = analysisCustomerTypeNotNull();
            for (int index = 0; index < keyList.length; index++) {
                key = keyList[index].toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    //    -------------------------------------8.4 顾客生命周期--------------------------------------------

    @Test
    public void analysisCustomerLifeCycleCustomerTypeNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客生命周期校验 \n";

        JSONObject data;
        String key = "";

        try {

            data = analysisCustomerlifeCycle(startTime, endTime, startTime, endTime);
            Object[] keyList = manalysisCustomerLifeCycleDataNotNull();
            for (int index = 0; index < keyList.length; index++) {
                key = keyList[index].toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    @Test
    public void analysisCustomerLifeCycleRelationsNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "顾客生命周期校验 \n";

        JSONObject data;

        try {

            data = analysisCustomerlifeCycle(startTime, endTime, startTime, endTime);
            checkNotNull(function, data, "relations");
            JSONArray relations = data.getJSONArray("relations");
            for (Object obj : analysisCustomerLifeCycleRelationsNotNull()) {
                key = obj.toString();
                for (int i = 0; i < relations.size(); i++) {
                    JSONObject single = relations.getJSONObject(i);
                    checkNotNull(function, single, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    --------------------------------------------九、员工管理接口-------------------------------------------------

//    -----------------------------------------------9.1 员工身份列表--------------------------------------------------

    @Test
    public void staffTypeListNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";
        String function = "员工身份列表校验 \n";

        try {
            JSONObject data = staffTypeList();
            for (Object obj : manageStaffTypeListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -------------------------------------------9.3 员工列表---------------------------------------------------------

    @Test
    public void staffListNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";
        String function = "员工列表校验 \n";

        try {
            JSONObject data = staffList("", "");
            for (Object obj : manageStaffListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    ---------------------------------------------9.4 员工详情----------------------------------------------------

    @Test
    public void staffDetailNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "员工详情校验 \n";

        try {
            JSONArray list = staffList("", "").getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("员工详情为空!");
            }

            for (int i = 0; i < list.size(); i++) {
                String id = list.getJSONObject(i).getString("id");

                JSONObject data = staffDetail(id);

                for (Object obj : manageStaffDetailNotNull()) {
                    key = obj.toString();
                    checkNotNull(function, data, key);
                }
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

    //    --------------------------------------------11.1 获取活动类型---------------------------------------------------

    @Test
    public void activityTypeListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "获取活动类型校验 \n";

        try {

            JSONObject data = activityTypeList();
            for (Object obj : activityTypeListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -------------------------------------------11.2 门店区域--------------------------------------------------------

    @Test
    public void activityRegionListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "门店区域校验 \n";

        try {

            JSONObject data = activityRegionList();
            for (Object obj : activityRegionListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    --------------------------------------------11.4 活动列表--------------------------------------------------------

    @Test
    public void activityListNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";
        String function = "活动列表校验 \n";

        try {

            JSONObject data = activityList("", "", "", "");
            for (Object obj : activityListNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -------------------------------------------11.6 活动详情------------------------------------------------------

    @Test
    public void activityDetailNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";

        String function = "活动详情校验 \n";

        try {

            JSONArray list = activityList("", "", "", "").getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("活动列表为空！");
            }
            String id = list.getJSONObject(0).getString("id");
            JSONObject data = activityDetail(id);
            for (Object obj : activityDetailNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//-----------------------------------------------------11.7 活动客流对比------------------------------------------------

    @Test
    public void activityPassengerFlowNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";
        String function = "活动客流对比校验 \n";

        try {

            JSONArray list = activityList("", "", "", "").getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("活动列表为空！");
            }

            String id = list.getJSONObject(0).getString("id");
            JSONObject data = activityPassengerFlowContrast(id);
            for (Object obj : activityPassengerFlowContrastNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    -----------------------------------------------------11.8 活动区域效果----------------------------------------------------

    @Test
    public void activityRegionEffectNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String key = "";
        String function = "活动区域效果校验 \n";

        try {

            JSONArray list = activityList("", "", "", "").getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("活动列表为空！");
            }

            String id = list.getJSONObject(0).getString("id");
            JSONObject data = activityRegionEffect(id);
            for (Object obj : activityRegionEffectNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }

//    ------------------------------------------------------11.9 客群留存效果----------------------------------------------------------------

    @Test
    public void activityCustomerTypeEffectNotNullCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String function = "客群留存效果校验 \n";
        String key = "";

        try {

            JSONArray list = activityList("", "", "", "").getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("活动列表为空！");
            }

            String id = list.getJSONObject(0).getString("id");

            JSONObject data = activityCustomerTypeEffect(id);
            for (Object obj : activityCustomerTypeEffectNotNull()) {
                key = obj.toString();
                checkNotNull(function, data, key);
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验" + key + "非空");
        }
    }


    //    ---------------------------------------附、交叉验证-------------------------------------------------------
    @Test
    public void realTimeRegionLessThanTotal() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.1 校验：实时，概述中的累计人数大于“售楼处实时停留人数”的某区域的累计人数 \n";

        try {

            JSONObject shopData = realTimeShop();
            JSONObject regionData = realTimeRegions();

            String today = LocalDate.now().toString();
            compareRegionUvTotalUv(today, today, shopData, regionData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void shopHistoryEqualsRealTime() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.2 校验：当天区域单向/交叉客流中的人数等于概述中的总人数 \n";

        try {

            String startTime = LocalDate.now().toString();

            //区域单向客流中的pv,uv,stay_time用的是历史统计的接口
            JSONObject historyShopDataJo = historyShopCode1000(startTime, startTime);
            JSONObject realTimeShopDataJo = realTimeShop();

            compareHistoryToRealTimeShop(realTimeShopDataJo, historyShopDataJo);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void checkRealTimeUvEqualsAnalysisCustomer() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.3 校验:实时，概览中的uv与顾客分析选择今天时的人数相等 \n";


        try {
            JSONObject realTimeShop = realTimeShop();
            Integer uv = realTimeShop.getInteger("uv");

            JSONObject customerTypeData = analysisCustomerType(endTime, endTime);
            int toNewCustomerNum = getCustomerNum(customerTypeData, "new_customer_analysis");

            if (uv != toNewCustomerNum) {
                throw new Exception("实时UV[" + uv + "],顾客分析中顾客拉新人数[" + toNewCustomerNum + "],不相等");
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void AccumulatedEqualsShop() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.4 校验：实时，概览中的uv与全场累计客流的当前时段累计人数、环比相等 \n";

        try {
            JSONObject accumulatedDataJo = realTimeAccumulated();
            JSONObject shopDataJo = realTimeShop();

            compareAccumulatedToShop(shopDataJo, accumulatedDataJo);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeRegionEqualsRegionEnterRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.5 校验：实时，“今日区域客流排行”中的区域pv，与区域单向客流-客流进入区域排行中的pv相等 \n";

        try {
            String startTime = LocalDate.now().toString();

            JSONObject realTimeRegions = realTimeRegions();
            JSONObject regionEnterRank = regionEnterRank(startTime, startTime);

            compareRealTimeRegionRegionEnterRank(realTimeRegions, regionEnterRank);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void checkRegionUvRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.6 校验：区域实时人数的排行 \n";

        try {
            JSONObject data = realTimeRegions();

            checkSort(data);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void WanderDepthRealTimeEqualsHistory() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.7 校验：实时游逛深度的昨日与历史游逛深度选择昨天时的数据一致 \n";

        try {

            String startTime = LocalDate.now().minusDays(1).toString();

            JSONObject realTimeWanderDepth = realTimeWanderDepth();

            JSONObject historyWanderDepth = historyWanderDepth(startTime, startTime);

            compareWanderDepthRealTimeHistory(realTimeWanderDepth, historyWanderDepth);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void realTimeEntranceRankRank() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.8 校验：实时出入口客流流量排行 \n";

        try {
            JSONObject data = realTimeEntranceRankDistribution();
            checkRank(data, "list", "num", function);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验是否正确排序！");
        }
    }

    @Test
    public void realTimeCustomerTypeEqualsHistoryShop() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "1.9、实时，客流身份分布与区域单向客流中的比例相同 \n";

        try {
            JSONObject realTimeData = realTimeCustomerTypeDistribution();
            JSONObject historyData = historyCustomerTypeDistribution(endTime, endTime);

            checkCustomerType(realTimeData, historyData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function + "校验是否正确排序！");
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void historyRegionLessThanTotal(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span;

        String function = "2.1 校验：客流趋势中的累计人数，大于“区域累计客流”中的某区域的累计人数 \n";

        String startTime = "";
        String endTime = LocalDate.now().minusDays(1).toString();

        try {

            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            JSONObject shopData = historyShopCode1000(startTime, endTime);

            JSONObject regionData = historyRegion(startTime, endTime);

            compareRegionUvTotalUv(startTime, endTime, shopData, regionData);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void historyShopEqualsAnalysisCustomer(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span;

        String function = "2.2 校验：客流趋势与顾客分析选择同一时间段的人数相等\n";

        String startTime = "";
        String endTime = LocalDate.now().minusDays(1).toString();

        try {

            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            JSONObject shopData = historyShopCode1000(startTime, endTime);
            Integer uv = shopData.getInteger("uv");

            JSONObject analysisCustomerData = analysisCustomerType(startTime, endTime);

            int toNustomerNum = getCustomerNum(analysisCustomerData, "new_customer_analysis");
            if (toNustomerNum != uv) {
                throw new Exception("[" + startTime + "-" + endTime + "]" + "客流趋势中UV[" + uv + "],顾客分析中总人数[" + toNustomerNum + "],不相等");
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void historyCustomerTypePercentAccuracy(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span;

        String function = "2.3、客流趋势-客流身份分布百分比的精度统一 \n";

        String startTime = "";
        String endTime = LocalDate.now().minusDays(1).toString();

        try {

            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            JSONObject historyData = historyCustomerTypeDistribution(startTime, endTime);
            checkPercentAccuracy(historyData, 2, "[" + startTime + "-" + endTime + "]" + ", 历史客流身份分布, ");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void historyNewCustomerPercentEqualsAnalysis(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span;

        String function = "2.4、客流趋势中新客比例与顾客分析中新客比例相同 \n";

        String startTime = "";
        String endTime = LocalDate.now().minusDays(1).toString();

        try {

            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            String historyPercent = "";
            String analysisPercent = "";

            JSONObject historyData = historyCustomerTypeDistribution(startTime, endTime);
            JSONArray historyList = historyData.getJSONArray("list");
            for (int i = 0; i < historyList.size(); i++) {
                JSONObject single = historyList.getJSONObject(i);
                if ("NEW".equals(single.getString("type"))) {
                    historyPercent = single.getString("percentage_str");
                }
            }

            JSONObject analysisData = analysisCustomerType(startTime, endTime);

            JSONArray newCustomerAnalysis = analysisData.getJSONArray("new_customer_analysis");
            for (int i = 0; i < newCustomerAnalysis.size(); i++) {
                JSONObject single = newCustomerAnalysis.getJSONObject(i);
                if ("NEW".equals(single.getString("type"))) {
                    analysisPercent = single.getString("customer_ratio_str");
                }
            }

            if (!historyPercent.equals(analysisPercent)) {
                throw new Exception("[" + startTime + "-" + endTime + "], " + "客流趋势中新客比例:[" + historyPercent + "],顾客分析中新客比例:[" + analysisPercent + "],不相等。");
            }
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void checkCustomerTypeAnalysis(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span;

        String function = "3.1 校验：顾客分析中顾客拉新和顾客成交的总人数一致 \n";

        String startTime = "";
        String endTime = "";

        try {

//            1、取顾客分析中顾客拉新的人数
            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span).toString();
            }

            endTime = LocalDate.now().toString();
            JSONObject customerTypeData = analysisCustomerType(startTime, endTime);
            int toNewCustomerNum = getCustomerNum(customerTypeData, "new_customer_analysis");

//            2、取顾客分析中顾客成交的人数
            int toSignedCustomerNum = getCustomerNum(customerTypeData, "signed_analysis");

            if (toNewCustomerNum != toSignedCustomerNum) {
                throw new Exception("[" + startTime + "-" + endTime + "], " + "顾客分析中，顾客拉新总数[" + toNewCustomerNum + "],顾客成交[" + toSignedCustomerNum + "],不相等");
            }

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void customerQualityEqualsCustomerType(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span + "天";

        String function = "3.2 校验：顾客分析中的高活跃人数与客群质量分析中的高活跃顾客人数相等 \n";

        String startTime = "";
        String endTime = "";

        try {
            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            endTime = LocalDate.now().minusDays(1).toString();

//            顾客分析中的高活跃顾客数
            JSONObject customerTypeData = analysisCustomerType(startTime, endTime);
            int highActiveCustomerNum = getSubCustomerNum(customerTypeData, "stay_customer_analysis", "HIGH_ACTIVE");

            String customerQuality = analysisCustomerQualityNoCode(startTime, endTime, "HIGH_ACTIVE");

            checkCustomerQualityAndType(startTime, endTime, customerQuality, highActiveCustomerNum);

        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "ACTIVITY_OTHERS_CHECK")
    public void customerAnalysisEqualsActivityDetail(String activityId, String name, String contrastStart, String contrastEnd,
                                                     String thisStart, String thisEnd, String influenceStart, String influenceEnd) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + name;

        String function = "3.3 校验：顾客分析中的高、低活跃数与活动详情中相等 \n";

        try {

            JSONObject contrastData = analysisCustomerType(contrastStart, contrastEnd);
            int contrastLow = getSubCustomerNum(contrastData, "stay_customer_analysis", "LOW_ACTIVE");
            int contrastHigh = getSubCustomerNum(contrastData, "stay_customer_analysis", "HIGH_ACTIVE");
            int contrastLost = getSubCustomerNum(contrastData, "stay_customer_analysis", "LOST");

            JSONObject thisData = analysisCustomerType(thisStart, thisEnd);
            int thisLow = getSubCustomerNum(thisData, "stay_customer_analysis", "LOW_ACTIVE");
            int thisHigh = getSubCustomerNum(thisData, "stay_customer_analysis", "HIGH_ACTIVE");
            int thisLost = getSubCustomerNum(thisData, "stay_customer_analysis", "LOST");

            JSONObject influenceData = analysisCustomerType(influenceStart, influenceEnd);
            int influenceLow = getSubCustomerNum(influenceData, "stay_customer_analysis", "LOW_ACTIVE");
            int influenceHigh = getSubCustomerNum(influenceData, "stay_customer_analysis", "HIGH_ACTIVE");
            int influenceLost = getSubCustomerNum(influenceData, "stay_customer_analysis", "LOST");

//            1、详情
            JSONArray list = activityCustomerTypeEffect(activityId).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                if ("LOW_ACTIVE".equals(single.getString("customer_type"))) {
                    int contrastNum = single.getInteger("contrast_cycle_num");
                    int thisNum = single.getInteger("this_cycle_num");
                    int influenceNum = single.getInteger("influence_cycle_num");

                    if (contrastLow != contrastNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中对比时期，低活跃数[" + contrastNum + "],顾客分析中[" + contrastLow + "]不相等");
                    }

                    if (thisLow != thisNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中活动期间，低活跃数[" + thisNum + "],顾客分析中[" + thisLow + "]不相等");
                    }

                    if (influenceLow != influenceNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中活动后期，低活跃数[" + influenceNum + "],顾客分析中[" + influenceLow + "]不相等");
                    }
                } else if ("HIGH_ACTIVE".equals(single.getString("customer_type"))) {
                    int contrastNum = single.getInteger("contrast_cycle_num");
                    int thisNum = single.getInteger("this_cycle_num");
                    int influenceNum = single.getInteger("influence_cycle_num");

                    if (contrastHigh != contrastNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中对比时期，高活跃数[" + contrastNum + "],顾客分析中[" + contrastHigh + "]不相等");
                    }

                    if (thisHigh != thisNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中活动期间，高活跃数[" + thisNum + "],顾客分析中[" + thisHigh + "]不相等");
                    }

                    if (influenceHigh != influenceNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中活动后期，高活跃数[" + influenceNum + "],顾客分析中[" + influenceHigh + "]不相等");
                    }

                } else if ("LOST".equals(single.getString("customer_type"))) {
                    int contrastNum = single.getInteger("contrast_cycle_num");
                    int thisNum = single.getInteger("this_cycle_num");
                    int influenceNum = single.getInteger("influence_cycle_num");

                    if (contrastLost != contrastNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中对比时期，流失客数[" + contrastNum + "],顾客分析中[" + contrastLost + "]不相等");
                    }

                    if (thisLost != thisNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中活动期间，流失客数[" + thisNum + "],顾客分析中[" + thisLost + "]不相等");
                    }

                    if (influenceLost != influenceNum) {
                        throw new Exception(name + ",活动详情-客群留存效果中活动后期，流失客数[" + influenceNum + "],顾客分析中[" + influenceLost + "]不相等");
                    }

                }
            }
        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void checkCustomerTypeAnalysisPercent(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span;

        String function = "3.4 校验：顾客分析中每个饼图的比例都是100% \n";

        String startTime = "";
        String endTime = LocalDate.now().toString();

        try {

//            1、取顾客分析中顾客拉新的人数
            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span).toString();
            }

            JSONObject customerTypeData = analysisCustomerType(startTime, endTime);

            checkPercent100(customerTypeData.getJSONArray("new_customer_analysis"), "顾客分析-顾客拉新>>>");
            checkPercent100(customerTypeData.getJSONArray("stay_customer_analysis"), "顾客分析-顾客留存>>>");
            checkPercent100(customerTypeData.getJSONArray("signed_analysis"), "顾客分析-顾客成交>>>");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "DAY_SPAN_4")
    public void customerQualityCheck(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + span + "天";

        String function = "4.1 校验：客群质量分析中的三个图数据一致 \n";

        String startTime = "";
        String endTime = "";

        try {
            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            endTime = LocalDate.now().minusDays(1).toString();

//            客群质量分析中的高活跃顾客数
            String customerQuality = analysisCustomerQualityNoCode(startTime, endTime, "HIGH_ACTIVE");

            checkCustomerQuality(startTime, endTime, customerQuality);

        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void activityCheckOneDay() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + "oneDay";

        String function = "4.2.1 校验：活动详情内各数据的一致性 \n";

        String activityId = "45";

        try {
//            1、详情
            JSONObject detailData = activityDetail(activityId);
            int detailContrastOld = detailData.getJSONObject("contrast_cycle").getInteger("old_num");
            int detailContrastNew = detailData.getJSONObject("contrast_cycle").getInteger("new_num");

            int detailThisOld = detailData.getJSONObject("this_cycle").getInteger("old_num");
            int detailThisNew = detailData.getJSONObject("this_cycle").getInteger("new_num");

            int detailInfluenceOld = detailData.getJSONObject("influence_cycle").getInteger("old_num");
            int detailInfluenceNew = detailData.getJSONObject("influence_cycle").getInteger("new_num");

//            2、活动客流对比
            JSONObject flowData = activityPassengerFlowContrast(activityId);
            int flowThis = flowData.getJSONArray("this_cycle").getJSONObject(0).getInteger("num");
            int flowContrst = flowData.getJSONArray("contrast_cycle").getJSONObject(0).getInteger("num");
            int flowInfluence = flowData.getJSONArray("influence_cycle").getJSONObject(0).getInteger("num");

//            3、活动区域效果
            JSONObject regionEffectData = activityRegionEffect(activityId).getJSONArray("list").getJSONObject(0);
            int regionThis = regionEffectData.getInteger("this_cycle");
            int regionContrast = regionEffectData.getInteger("contrast_cycle");
            int regionInfluence = regionEffectData.getInteger("influence_cycle");

//            4、客群留存效果
            JSONObject customerTypeEffect = activityCustomerTypeEffect(activityId);
            int typeEffectThis = getCustomerTypeEffect(customerTypeEffect, "this_cycle_num");
            int typeEffectContrast = getCustomerTypeEffect(customerTypeEffect, "contrast_cycle_num");
            int typeEffectInfluence = getCustomerTypeEffect(customerTypeEffect, "influence_cycle_num");

//            活动详情中新老顾客之和与活动客流对比相等

            if (detailContrastNew + detailContrastOld != flowContrst) {
                throw new Exception("一天，活动详情中对比时期新顾客[" + detailContrastNew + "]与老顾客[" + detailContrastOld +
                        "]之和不等于活动客流对比中人数[" + flowContrst + "]");
            }

            if (detailThisNew + detailThisOld != flowThis) {
                throw new Exception("一天，活动详情中活动期间新顾客[" + detailThisNew + "]与老顾客[" + detailThisOld +
                        "]之和不等于活动客流对比中人数[" + flowThis + "]");
            }

            if (detailInfluenceNew + detailInfluenceOld != flowInfluence) {
                throw new Exception("一天，活动详情中活动后期新顾客[" + detailInfluenceNew + "]与老顾客[" + detailInfluenceOld +
                        "]之和不等于活动客流对比中人数[" + flowInfluence + "]");
            }

//            活动详情中新老顾客之和与活动区域效果相等
            if (detailContrastNew + detailContrastOld != regionContrast) {
                throw new Exception("一天，活动详情中对比时期新顾客[" + detailContrastNew + "]与老顾客[" + detailContrastOld +
                        "]之和不等于活动区域效果中人数[" + regionContrast + "]");
            }

            if (detailThisNew + detailThisOld != regionThis) {
                throw new Exception("活动详情中活动期间新顾客[" + detailThisNew + "]与老顾客[" + detailThisOld +
                        "]之和不等于活动区域效果中人数[" + regionThis + "]");
            }

            if (detailInfluenceNew + detailInfluenceOld != regionInfluence) {
                throw new Exception("一天，活动详情中活动后期新顾客[" + detailInfluenceNew + "]与老顾客[" + detailInfluenceOld +
                        "]之和不等于活动区域效果中人数[" + regionInfluence + "]");
            }

//            活动详情中老顾客与客群留存效果中老顾客之和相等
            if (detailContrastOld != typeEffectContrast) {
                throw new Exception("一天，活动详情中对比时期老顾客[" + detailThisOld +
                        "]不等于客群留存效果中人数[" + typeEffectContrast + "]");
            }

            if (detailThisOld != typeEffectThis) {
                throw new Exception("一天，活动详情中活动期间老顾客[" + detailThisOld +
                        "]不等于客群留存效果中人数[" + typeEffectThis + "]");
            }

            if (detailInfluenceOld != typeEffectInfluence) {
                throw new Exception("一天，活动详情中活动后期老顾客[" + detailThisOld +
                        "]不等于客群留存效果中人数[" + typeEffectInfluence + "]");
            }


        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "ACTIVITY_CHECK")
    public void activityCheckSomeDay(String activityId, int days, String contrastStart, String contrastEnd,
                                     String thisStart, String thisEnd, String influenceStart, String influenceEnd) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + days + "days";

        String function = "4.2.2 校验：活动详情内各数据的一致性 \n";

        try {
//            1、详情
            JSONObject detailData = activityDetail(activityId);
            int detailThisOld = detailData.getJSONObject("this_cycle").getInteger("old_num");
            int detailThisNew = detailData.getJSONObject("this_cycle").getInteger("new_num");
            int detailContrastOld = detailData.getJSONObject("contrast_cycle").getInteger("old_num");
            int detailContrastNew = detailData.getJSONObject("contrast_cycle").getInteger("new_num");
            int detailInfluenceOld = detailData.getJSONObject("influence_cycle").getInteger("old_num");
            int detailInfluenceNew = detailData.getJSONObject("influence_cycle").getInteger("new_num");

//            3、活动区域效果
            JSONObject regionEffectData = activityRegionEffect(activityId).getJSONArray("list").getJSONObject(0);
            int regionThis = regionEffectData.getInteger("this_cycle");
            int regionContrast = regionEffectData.getInteger("contrast_cycle");
            int regionInfluence = regionEffectData.getInteger("influence_cycle");

//            4、客群留存效果
            JSONObject customerTypeEffect = activityCustomerTypeEffect(activityId);
            int typeEffectThis = getCustomerTypeEffect(customerTypeEffect, "this_cycle_num");
            int typeEffectContrast = getCustomerTypeEffect(customerTypeEffect, "contrast_cycle_num");
            int typeEffectInfluence = getCustomerTypeEffect(customerTypeEffect, "influence_cycle_num");

//            活动详情中新老顾客之和与活动区域效果相等
            int averageContrast = (int) Math.floor((double) (detailContrastNew + detailContrastOld) / (double) days);
            if (Math.abs(averageContrast - regionContrast) > 1) {
                throw new Exception(days + "天，活动详情中对比时期新顾客[" + detailContrastNew + "]与老顾客[" + detailContrastOld +
                        "],平均[" + averageContrast + "],不等于活动区域效果中日均客流[" + averageContrast + "]");
            }

            int averageThis = (int) Math.floor((double) (detailThisNew + detailThisOld) / (double) days);
            if (Math.abs(averageThis - regionThis) > 1) {
                throw new Exception(days + "天，活动详情中活动期间新顾客[" + detailThisNew + "]与老顾客[" + detailThisOld +
                        "],平均[" + averageThis + "],不等于活动区域效果中人数[" + regionThis + "]");
            }

            int averageInfluence = (int) Math.floor((double) (detailInfluenceNew + detailInfluenceOld) / (double) days);
            if (Math.abs(averageInfluence - regionInfluence) > 1) {
                throw new Exception(days + "天，活动详情中活动后期新顾客[" + detailInfluenceNew + "]与老顾客[" + detailInfluenceOld +
                        "],平均[" + averageInfluence + "],不等于活动区域效果中人数[" + averageInfluence + "]");
            }

//            活动详情中老顾客与客群留存效果中老顾客和该时期的成交顾客数量之和相等
            JSONObject contrastSign = analysisCustomerType(contrastStart, contrastEnd);
            int contrastSigned = getSubCustomerNum(contrastSign, "signed_analysis", "SIGNED");
            int diff = Math.abs(detailContrastOld - typeEffectContrast - contrastSigned);
            if (diff >= 1) {
                throw new Exception(days + "天，活动详情中对比日期老顾客[" + detailContrastOld +
                        "]不等于客群留存效果中人数[" + typeEffectContrast + "],与该时期成交顾客数[" + contrastSigned + "]之和");
            }

            JSONObject thisSign = analysisCustomerType(thisStart, thisEnd);
            int thisSigned = getSubCustomerNum(thisSign, "signed_analysis", "SIGNED");
            diff = Math.abs(detailThisOld - typeEffectThis - thisSigned);
            if (diff >= 1) {
                throw new Exception(days + "天，活动详情中活动期间老顾客[" + detailThisOld +
                        "]不等于客群留存效果中人数[" + typeEffectThis + "],与该时期成交顾客数[" + thisSigned + "]之和");
            }

            JSONObject influenceSign = analysisCustomerType(influenceStart, influenceEnd);
            int influenceSigned = getSubCustomerNum(influenceSign, "signed_analysis", "SIGNED");
            diff = Math.abs(detailInfluenceOld - typeEffectInfluence - influenceSigned);
            if (diff >= 1) {
                throw new Exception(days + "天，活动详情中活动后期老顾客[" + detailInfluenceOld +
                        "]不等于客群留存效果中人数[" + typeEffectInfluence + "],与该时期成交顾客数[" + influenceSigned + "]之和");
            }
        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "ACTIVITY_OTHERS_CHECK")
    public void activityHistoryShopEquals(String activityId, String name, String contrastStart, String contrastEnd,
                                          String thisStart, String thisEnd, String influenceStart, String influenceEnd) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + name;

        String function = "4.3 校验：活动详情与客流趋势中的uv，stay_time一致 \n";

        try {
//            1、详情

            JSONObject detailData = activityDetail(activityId);
            int detailContrastOld = detailData.getJSONObject("contrast_cycle").getInteger("old_num");
            int detailContrastNew = detailData.getJSONObject("contrast_cycle").getInteger("new_num");
            int detailContrastStayTime = detailData.getJSONObject("contrast_cycle").getInteger("stay_time_per_person");

            int detailThisOld = detailData.getJSONObject("this_cycle").getInteger("old_num");
            int detailThisNew = detailData.getJSONObject("this_cycle").getInteger("new_num");
            int detailThisStayTime = detailData.getJSONObject("this_cycle").getInteger("stay_time_per_person");

            int detailInfluenceOld = detailData.getJSONObject("influence_cycle").getInteger("old_num");
            int detailInfluenceNew = detailData.getJSONObject("influence_cycle").getInteger("new_num");
            int detailInfluenceStayTime = detailData.getJSONObject("influence_cycle").getInteger("stay_time_per_person");

            JSONObject contrastData = historyShopCode1000(contrastStart, contrastEnd);
            int contrastUv = contrastData.getInteger("uv");
            int contrastStayTime = contrastData.getInteger("stay_time");

            if (detailContrastNew + detailContrastOld != contrastUv) {
                throw new Exception(name + ",活动详情中对比日期新顾客[" + detailContrastNew + "]与老顾客[" + detailContrastOld +
                        "]之和不等于客流趋势中人数[" + contrastUv + "]");
            }

            if (detailContrastStayTime != contrastStayTime) {
                throw new Exception(name + ",活动详情中对比日期人均每天停留时时长[" + detailContrastStayTime +
                        "]不等于客流趋势中人数[" + contrastStayTime + "]");
            }

            JSONObject thisData = historyShopCode1000(thisStart, thisEnd);
            int thisUv = thisData.getInteger("uv");
            int thisStayTime = thisData.getInteger("stay_time");

            if (detailThisNew + detailThisOld != thisUv) {
                throw new Exception(name + ",活动详情中对比日期新顾客[" + detailThisNew + "]与老顾客[" + detailThisOld +
                        "]之和不等于客流趋势中人数[" + thisUv + "]");
            }

            if (detailThisStayTime != thisStayTime) {
                throw new Exception(name + ",活动详情中对比日期人均每天停留时时长[" + detailThisStayTime +
                        "]不等于客流趋势中人数[" + thisStayTime + "]");
            }

            JSONObject influence = historyShopCode1000(influenceStart, influenceEnd);
            int influenceUv = influence.getInteger("uv");
            int influenceStayTime = influence.getInteger("stay_time");

            if (detailInfluenceNew + detailInfluenceOld != influenceUv) {
                throw new Exception(name + ",活动详情中对比日期新顾客[" + detailInfluenceNew + "]与老顾客[" + detailInfluenceOld +
                        "]之和不等于客流趋势中人数[" + thisUv + "]");
            }

            if (detailInfluenceStayTime != influenceStayTime) {
                throw new Exception(name + ",活动详情中对比日期人均每天停留时时长[" + detailInfluenceStayTime +
                        "]不等于客流趋势中人数[" + influenceStayTime + "]");
            }

        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test(dataProvider = "ACTIVITY_OTHERS_CHECK")
    public void activityAnalysisCustomerCheck(String activityId, String name, String contrastStart, String contrastEnd,
                                              String thisStart, String thisEnd, String influenceStart, String influenceEnd) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + name;

        String function = "4.4 校验：活动详情与顾客分析中的新客，老客数一致 \n";

        try {
//            1、详情

            JSONObject detailData = activityDetail(activityId);
            int detailContrastOld = detailData.getJSONObject("contrast_cycle").getInteger("old_num");
            int detailContrastNew = detailData.getJSONObject("contrast_cycle").getInteger("new_num");

            int detailThisOld = detailData.getJSONObject("this_cycle").getInteger("old_num");
            int detailThisNew = detailData.getJSONObject("this_cycle").getInteger("new_num");

            int detailInfluenceOld = detailData.getJSONObject("influence_cycle").getInteger("old_num");
            int detailInfluenceNew = detailData.getJSONObject("influence_cycle").getInteger("new_num");

            JSONObject contrast = analysisCustomerType(contrastStart, contrastEnd);
            int contrastNewNum = getSubCustomerNum(contrast, "new_customer_analysis", "NEW");
            int contrastOldNum = getSubCustomerNum(contrast, "new_customer_analysis", "OLD");

            if (detailContrastNew != contrastNewNum) {
                throw new Exception(name + ",活动详情中对比日期新客数[" + detailContrastNew +
                        "与顾客分析中新客数[" + contrastNewNum + "]不相等");
            }

            if (detailContrastOld != contrastOldNum) {
                throw new Exception(name + ",活动详情中对比日期老客数[" + detailContrastOld +
                        "与顾客分析中老客数[" + contrastOldNum + "]不相等");
            }

            JSONObject thisData = analysisCustomerType(thisStart, thisEnd);

            int thisNewNum = getSubCustomerNum(thisData, "new_customer_analysis", "NEW");
            int thisOldNum = getSubCustomerNum(thisData, "new_customer_analysis", "OLD");

            if (detailThisNew != thisNewNum) {
                throw new Exception(name + ",活动详情中对比日期新客数[" + detailThisNew +
                        "与顾客分析中新客数[" + thisNewNum + "]不相等");
            }

            if (detailThisOld != thisOldNum) {
                throw new Exception(name + ",活动详情中对比日期老客数[" + detailThisOld +
                        "与顾客分析中老客数[" + thisOldNum + "]不相等");
            }

            JSONObject influence = analysisCustomerType(influenceStart, influenceEnd);

            int influenceNewNum = getSubCustomerNum(influence, "new_customer_analysis", "NEW");
            int influenceOldNum = getSubCustomerNum(influence, "new_customer_analysis", "OLD");

            if (detailInfluenceNew != influenceNewNum) {
                throw new Exception(name + ",活动详情中对比日期新客数[" + detailInfluenceNew +
                        "与顾客分析中新客数[" + influenceNewNum + "]不相等");
            }

            if (detailInfluenceOld != influenceOldNum) {
                throw new Exception(name + ",活动详情中对比日期老客数[" + detailInfluenceOld +
                        "与顾客分析中老客数[" + influenceOldNum + "]不相等");
            }

        } catch (Exception e) {
            failReason = e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    private int getCustomerTypeEffect(JSONObject data, String key) {
        JSONArray list = data.getJSONArray("list");
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            num += single.getInteger(key);
        }

        return num;
    }

    @Test
    public void newCustomerFirstAppearTimeLTLast() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "5.1 校验：新客的首次出现时间 <= 最后出现时间 \n";

        try {
            JSONArray customerList = manageCustomerList("NEW", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size() && i <= 60; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONObject customerDetail = manageCustomerDetail(customerId);
                Long firstAppearTime = customerDetail.getLong("first_appear_time");
                String firstAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", firstAppearTime);

                Long lastAppearTime = customerDetail.getLong("last_appear_time");
                String lastAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", lastAppearTime);

                if (firstAppearTime > lastAppearTime) {
                    throw new Exception("新客 customerId:" + customerId + "首次出现时间【" + firstAppearTimeStr + "】{晚于}最后出现时间【" + lastAppearTimeStr + "】");
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
    public void newCustomerFirstLastListEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "5.2 校验：新客的首次出现日期==最后出现日期==出现日期列表 \n";

        try {
            JSONArray customerList = manageCustomerList("NEW", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size() && i <= 60; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONObject customerDetail = manageCustomerDetail(customerId);
                Long firstAppearTime = customerDetail.getLong("first_appear_time");
                String firstAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", firstAppearTime);

                Long lastAppearTime = customerDetail.getLong("last_appear_time");
                String lastAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", lastAppearTime);

                JSONArray list = manageCustomerDayAppearList(customerId).getJSONArray("list");
                if (list.size() != 1) {
                    throw new Exception("新客: " + customerId + ", 详情页中左侧日期列表有:" + list.size() + " 个日期！");
                }

                String appearListDate = list.getString(0);

                if (!firstAppearTimeStr.equals(appearListDate)) {
                    throw new Exception("新客 customerId:" + customerId + "详情页中首次出现日期：" + firstAppearTimeStr + ", 左侧日期列表中日期：" + appearListDate);
                }

                if (!lastAppearTimeStr.equals(appearListDate)) {
                    throw new Exception("新客 customerId:" + customerId + "详情页中最后出现日期：" + lastAppearTimeStr + ", 左侧日期列表中日期：" + appearListDate);
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
    public void highCustomerFirstAppearDateLTLast() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "5.3 校验：高活跃用户的首次出现日期 < 最后出现日期 \n";

        try {
            JSONArray customerList = manageCustomerList("HIGH_ACTIVE", "", "").getJSONArray("list");

            int size = customerList.size();

            if (size > 60) {
                size = 60;
            }
            for (int i = 0; i < size; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONObject customerDetail = manageCustomerDetail(customerId);
                Long firstAppearTime = customerDetail.getLong("first_appear_time");
                String firstAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", firstAppearTime);

                Long lastAppearTime = customerDetail.getLong("last_appear_time");
                String lastAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", lastAppearTime);

                if (firstAppearTimeStr.compareTo(lastAppearTimeStr) >= 0) {
                    throw new Exception("高活跃 customerId:" + customerId + "首次出现日期【" + firstAppearTimeStr + "】{没有早于}最后出现日期【" + lastAppearTimeStr + "】");
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
    public void customerFirstLastListEqualsAppearList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "5.4 校验：顾客的首次、最后出现日期==出现日期列表的首次、最后出现日期 \n";

        try {
            JSONArray customerList = manageCustomerList("", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size() && i <= 60; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

                JSONObject customerDetail = manageCustomerDetail(customerId);
                Long firstAppearTime = customerDetail.getLong("first_appear_time");
                String firstAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", firstAppearTime);

                Long lastAppearTime = customerDetail.getLong("last_appear_time");
                String lastAppearTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", lastAppearTime);

                JSONArray list = manageCustomerDayAppearList(customerId).getJSONArray("list");

                Preconditions.checkArgument(list.size() > 0, "[" + customerId + "] 顾客详情出现时间数组为空");
                String appearListFirstDate = list.getString(list.size() - 1);
                String appearListLastDate = list.getString(0);

                if (!firstAppearTimeStr.equals(appearListFirstDate)) {
                    throw new Exception("[" + customerId + "] 详情页中首次出现日期：" + firstAppearTimeStr + ", 左侧日期列表中首次出现日期：" + appearListFirstDate);
                }

                if (!lastAppearTimeStr.equals(appearListLastDate)) {
                    throw new Exception("[" + customerId + "] 详情页中最后出现日期：" + lastAppearTimeStr + ", 左侧日期列表中最后出现日期：" + appearListLastDate);
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
    public void customerTraceMovingLineTrace() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "5.5 校验：有动线，必有轨迹 \n";

        try {
            JSONArray customerList = manageCustomerList("", "", "").getJSONArray("list");

            for (int i = 0; i < customerList.size() && i < 60; i++) {
                String customerId = customerList.getJSONObject(i).getString("customer_id");

//                从出现日期列表中获取轨迹的startTime，endTime参数
                JSONArray appearList = manageCustomerDayAppearList(customerId).getJSONArray("list");

//                每个顾客必然有出现日期，每个出现日期的动线都是一样的，所有取第一个日期的动线
                Preconditions.checkArgument(appearList.size() > 0, "[" + customerId + "] 详情中日期列表为空");
                String startTime = appearList.getString(0);
                startTime = startTime.replace("/", "-");
                JSONObject customerTraceData = customerTrace(startTime, startTime, customerId);

                boolean hasTraces = false;
                int movingLineSize = customerTraceData.getJSONArray("moving_lines").size();
                if (movingLineSize > 0) {
                    for (int j = 0; j < appearList.size(); j++) {
                        startTime = appearList.getString(j);
                        startTime = startTime.replace("/", "-");
                        customerTraceData = customerTrace(startTime, startTime, customerId);
                        JSONArray traces = customerTraceData.getJSONArray("traces");
                        if (traces != null && traces.size() > 0) {
                            hasTraces = true;
                        }
                    }

                    if (!hasTraces) {
                        throw new Exception("customer [" + customerId + "] 有动线，没轨迹。");
                    }
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
    public void missCaptureRatio() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String function = "计算漏抓率";

        try {

            JSONObject data = realTimeRegions();

            calMissRatio(data);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, caseName, function);
        }
    }

    private void calMissRatio(JSONObject data) {

        JSONArray regions = data.getJSONArray("regions");


        JSONObject first = regions.getJSONObject(0);

        int max = first.getJSONObject("statistics").getInteger("uv");

        String regionId = first.getString("region_id");

        String missRatioStr = "0";

//        892是大堂区
        if (!"892".equals(regionId)) {
            for (int i = 0; i < regions.size(); i++) {
                JSONObject single = regions.getJSONObject(i);
                if ("892".equals(single.getString("region_id"))) {
                    int target = single.getJSONObject("statistics").getInteger("uv");
                    double missRatio = (double) target * 100.00 / (double) max;
                    DecimalFormat df = new DecimalFormat("0.00");
                    missRatioStr = df.format(missRatio);
                    break;
                }
            }
        }

        logger.info("漏抓率【" + missRatioStr + "%】");
    }

    private int getHighActiveCustomerNum(JSONObject data, String parentKey, String childType) {
        int num = 0;

        JSONArray list = data.getJSONArray(parentKey);

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (childType.equals(single.getString("type"))) {
                num = single.getInteger("num");
            }
        }

        return num;
    }

    /**
     * 新增接口，未上线
     */
    @Test
    public void historyShopStartMTEnd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "6.6 选择结束日期比开始日期早且仅早一天的日期调用shop接口 \n";

        try {

            String startTime = LocalDate.now().toString();
            String endTime = LocalDate.now().minusDays(1).toString();

            String res = historyShopNoCode(startTime, endTime);

            checkCode(res, StatusCode.BAD_REQUEST, "");

            checkBadMessage(res, "参数校验未通过：查询起始时间不能大于结束时间");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void customerQualityStartMTEnd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "6.7、选择结束日期比开始日期早且仅早一天的日期调用客群质量分析接口 \n";

        try {

            String startTime = LocalDate.now().minusDays(1).toString();
            String endTime = LocalDate.now().minusDays(2).toString();

            String res = analysisCustomerQualityNoCode(startTime, endTime, "HIGH_ACTIVE");

            checkCode(res, StatusCode.BAD_REQUEST, "");

            checkBadMessage(res, "参数校验未通过：查询起始时间不能大于结束时间");

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    // 线上总是报警，需要回归的时候再打开
    //@Test(dataProvider = "DAY_SPAN_4")
    public void regionCrossDataPercent100(int span) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "6.8、区域交叉客流的百分比之和是100% \n";

        String startTime = "";
        String endTime = LocalDate.now().minusDays(1).toString();

        try {

            if (-1 == span) {
                startTime = "2019-10-28";
            } else {
                startTime = LocalDate.now().minusDays(span + 1).toString();
            }

            JSONObject data = regionCrossData(startTime, endTime);
            checkRegionCrossPercent100(data);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    private void checkRegionCrossPercent100(JSONObject data) throws Exception {
        JSONArray relations = data.getJSONArray("relations");

        double ratio = 0.0d;

        for (int i = 0; i < relations.size(); i++) {
            JSONObject single = relations.getJSONObject(i);
            ratio += single.getDoubleValue("ratio");
        }
        System.out.println("ratio = " + ratio);

        if (ratio > 2.02d) {
            throw new Exception("区域交叉客流比例之和大于100.01%");
        } else if (ratio < 1.98d) {
            throw new Exception("区域交叉客流比例之和小于99.99%");
        }
    }

    private void checkPercentAccuracy(JSONObject data, int num, String message) throws Exception {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if ("-".equals(single.getString("percentage_str"))) {
                continue;
            }

            String type = single.getString("type");
            String percentStr = single.getString("percentage_str");
            String lengthStr = percentStr.substring(percentStr.indexOf(".") + 1, percentStr.indexOf("%"));
            if (lengthStr.length() > num) {
                throw new Exception(message + ",type[" + type + "],小数点后保留了[" + lengthStr.length() + "]位,期待最多保留：[" + num + "]位");
            }
        }
    }

    private void checkCustomerType(JSONObject realTimeData, JSONObject historyShop) throws Exception {

        boolean isExist = false;
        JSONArray historyList = historyShop.getJSONArray("list");

        JSONArray realTimeList = realTimeData.getJSONArray("list");
        for (int i = 0; i < realTimeList.size(); i++) {
            JSONObject singleReal = realTimeList.getJSONObject(i);
            String type = singleReal.getString("type");
            String percentStr = singleReal.getString("percentage_str");

            for (int j = 0; j < historyList.size(); j++) {
                JSONObject singleHis = historyList.getJSONObject(j);
                if (type.equals(singleHis.getString("type"))) {
                    isExist = true;
                    String percentStrHis = singleHis.getString("percentage_str");
                    if (!percentStr.equals(percentStrHis)) {
                        throw new Exception("客流身份分布,类型:[" + type + "],实时中[" + percentStr + "],区域交叉客流中[" + percentStrHis + "]");
                    }
                }
            }

            if (!isExist) {
                throw new Exception("客流身份分布,类型:[" + type + "],在实时中存在，区域交叉客流中不存在");
            }
        }

    }

    private void checkIsExistByCustomerList(JSONObject data, String customerId, String faceUrl, boolean isExist, String comment) throws Exception {
        boolean isExistRes = false;
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (customerId.equals(single.getString("customer_id")) || faceUrl.equals(single.getString("face_url"))) {
                isExistRes = true;
                break;
            }
        }

        checkIsExist(comment, isExist, isExistRes);
    }

    private void checkBadMessage(String res, String message) throws Exception {
        String messageRes = JSON.parseObject(res).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception("使用相同手机号新建员工时，期待返回【" + message + "】，实际返回【" + messageRes + "】");
        }
    }

    private void checkCustomerQualityAndType(String startTime, String endTime, String res, int num) throws Exception {
        int crossStayReturnNum = 0;
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

        JSONArray crossStayReturn = data.getJSONArray("cross_stay_return");
        for (int i = 0; i < crossStayReturn.size(); i++) {
            JSONObject single = crossStayReturn.getJSONObject(i);
            crossStayReturnNum += single.getInteger("customer_num");
        }

        if (num != crossStayReturnNum) {
            throw new Exception("[" + startTime + "-" + endTime + "], " + "客群质量分析中高活跃回头客人数[" + crossStayReturnNum + "],与顾客分析中的高活跃人数[" + num + "]不相等。");
        }
    }

    private void checkCustomerQuality(String startTime, String endTime, String res) throws Exception {
        int crossStayReturnNum = 0;
        int stayAnalysisNum = 0;
        int returnCustomerAnalysisNum = 0;
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

//        综合分析
        JSONArray crossStayReturn = data.getJSONArray("cross_stay_return");
        for (int i = 0; i < crossStayReturn.size(); i++) {
            JSONObject single = crossStayReturn.getJSONObject(i);
            crossStayReturnNum += single.getInteger("customer_num");
        }

        checkPercent100(crossStayReturn, "[" + startTime + "-" + endTime + "], " + "客群质量分析-综合分析");

//        停留时长分析
        JSONArray stayAnalysis = data.getJSONArray("stay_analysis");
        for (int i = 0; i < stayAnalysis.size(); i++) {
            JSONObject single = stayAnalysis.getJSONObject(i);
            stayAnalysisNum += single.getInteger("customer_num");

        }

        checkPercent100(crossStayReturn, "[" + startTime + "-" + endTime + "], " + "客群质量分析-停留时长分析");

//        回头客分析（回头天数）
        JSONArray returnCustomerAnalysis = data.getJSONArray("return_customer_analysis");
        for (int i = 0; i < returnCustomerAnalysis.size(); i++) {
            JSONObject single = returnCustomerAnalysis.getJSONObject(i);
            returnCustomerAnalysisNum += single.getInteger("customer_num");
        }

        checkPercent100(crossStayReturn, "[" + startTime + "-" + endTime + "], " + "客群质量分析-回头客分析");

        if (crossStayReturnNum != stayAnalysisNum) {
            throw new Exception("[" + startTime + "-" + endTime + "], " + "综合分析顾客数[" + crossStayReturnNum + "],与停留时长分析中顾客数[" + stayAnalysisNum + "]不相等。");
        }

        if (crossStayReturnNum != returnCustomerAnalysisNum) {
            throw new Exception("[" + startTime + "-" + endTime + "], " + "综合分析顾客数[" + crossStayReturnNum + "],与回头客分析中顾客数[" + returnCustomerAnalysis + "]不相等。");
        }
    }

    public void checkPercent100(JSONArray crossStayReturn, String function) throws Exception {

        DecimalFormat df = new DecimalFormat("0.00");
        double percent = 0.0d;

        for (int i = 0; i < crossStayReturn.size(); i++) {
            JSONObject single = crossStayReturn.getJSONObject(i);
            String customerRatioStr = single.getString("customer_ratio_str");
            String customerRatioTemp = customerRatioStr.substring(0, customerRatioStr.length() - 1);
            double aDouble = Double.valueOf(df.format(Double.valueOf(customerRatioTemp)));
            percent += aDouble;
        }

        if (99 > (int) percent || (int) percent > 100) {
            throw new Exception(function + "的比例之和是[" + percent + "]不是100%");
        }
    }


    private int getLifeStyleNum(JSONObject data) {

        int num = 0;

        JSONArray relations = data.getJSONArray("relations");
        for (int i = 0; i < relations.size(); i++) {
            JSONObject single = relations.getJSONObject(i);

            JSONArray directionList = single.getJSONArray("direction_list");
            for (int j = 0; j < directionList.size(); j++) {
                JSONObject direction = directionList.getJSONObject(j);
                num += direction.getInteger("num");
            }
        }

        return num;
    }

    private int getCustomerNum(JSONObject data, String key) {

        int total = 0;
        JSONArray list = data.getJSONArray(key);
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            total += single.getInteger("num");
        }

        return total;
    }

    private int getSubCustomerNum(JSONObject data, String parentKey, String childKey) {

        int total = 0;
        JSONArray list = data.getJSONArray(parentKey);
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (childKey.equals(single.getString("type"))) {
                total = single.getInteger("num");
            }
        }
        return total;
    }

    private void checkSort(JSONObject data) throws Exception {

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size() - 1; i++) {

            JSONObject singleB = regions.getJSONObject(i);
            String regionB = singleB.getString("region_name");
            int uvB = singleB.getJSONObject("statistics").getInteger("uv");

            JSONObject singleA = regions.getJSONObject(i + 1);
            String regionA = singleA.getString("region_name");
            int uvA = singleA.getJSONObject("statistics").getInteger("uv");

            if (uvB < uvA) {
                throw new Exception("排名第【" + i + "】的区域【" + regionB + "】的uv数是【" + uvB + "】," +
                        "排名第" + (i + 1) + "的区域【" + regionA + "】的uv数是【" + uvA + "】");
            }
        }

    }

    private String getIdByStaffList(JSONObject staffList, String phone) throws Exception {

        return checkIsExistByStaffList(staffList, phone, true);

    }

    private String checkIsExistByStaffList(JSONObject staffList, String phone, boolean isExist) throws Exception {

        JSONArray list = staffList.getJSONArray("list");

        String id = "";

        boolean isExistRes = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String phoneRes = single.getString("phone");
            if (phone.equals(phoneRes)) {
                isExistRes = true;
                id = single.getString("id");
            }
        }

        checkIsExist("是否期待存在手机号为[" + phone + "]的员工", isExist, isExistRes);

        return id;
    }

    public void checkIsExist(String comments, boolean isExist, boolean isExistRes) throws Exception {
        if (isExist != isExistRes) {
            throw new Exception(comments + "，期待：" + isExist + ", 实际：" + isExistRes);
        }
    }


    private String getOneStaffType() throws Exception {
        JSONArray list = staffTypeList().getJSONArray("list");
        Random random = new Random();
        int i = random.nextInt(list.size());
        return list.getJSONObject(i).getString("staff_type");
    }

    private void compareWanderDepthRealTimeHistory(JSONObject realTimeWanderDepth, JSONObject historyWanderDepth) throws Exception {

        checkNotNull("门店实时游逛深度>>>", realTimeWanderDepth, "[statistics_data]");

        JSONArray realTimeData = realTimeWanderDepth.getJSONArray("statistics_data");

        DecimalFormat df = new DecimalFormat("0.00");

        boolean isExist = false;

        for (int i = 0; i < realTimeData.size(); i++) {
            JSONObject singleRealTime = realTimeData.getJSONObject(i);

            String label = singleRealTime.getString("label");

            double realTimeHistory = singleRealTime.getDoubleValue("history");
            String realTimeHistoryStr = df.format(realTimeHistory);

            checkNotNull("门店历史游逛深度>>>", historyWanderDepth, "[statistics_data]");

            JSONArray historyData = historyWanderDepth.getJSONArray("statistics_data");

            for (int j = 0; j < historyData.size(); j++) {
                JSONObject singleHistory = historyData.getJSONObject(j);

                if (label.equals(singleHistory.getString("label"))) {
                    isExist = true;
                    double historyPresent = singleHistory.getDoubleValue("present_cycle");
                    String historyPresentStr = df.format(historyPresent);
                    if (!realTimeHistoryStr.equals(historyPresentStr)) {
                        throw new Exception("历史游逛深度选昨天时，depth=" + historyPresentStr
                                + ", 实时游逛深度的昨日depth=" + realTimeHistoryStr + ",时间是" + label + ",两者不相符。");
                    }
                }
            }

            if (!isExist) {
                throw new Exception("历史游逛深度没有该时间的数据--" + label);
            }
        }
    }

    private void compareRealTimeRegionRegionEnterRank(JSONObject realTimeRegions, JSONObject regionEnterRank) throws Exception {

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
                        throw new Exception("区域单向客流-客流进入区域排行中的" + regionName + "区域的客流:" + num +
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
            throw new Exception("区域单人/交叉动线选择今天时的uv:" + movingUv + "不等于概述的uv:" + shopUv);
        }

        int movingPv = movingDirectionData.getInteger("pv");
        if (shopPv != movingPv) {
            throw new Exception("区域单人/交叉动线选择今天时的pv:" + movingPv + "不等于概述的pv:" + shopPv);
        }

        int movingStayTime = movingDirectionData.getInteger("stay_time");
        if (shopStayTime != movingStayTime) {
            throw new Exception("区域单人/交叉动线选择今天时的stay_time:" + movingStayTime + "不等于概述的stay_time:" + shopStayTime);
        }

    }

    public void compareRegionUvTotalUv(String startTime, String endTime, JSONObject shopDataJo, JSONObject regionDataJo) throws Exception {
        int totalUv = shopDataJo.getInteger("uv");

        JSONArray regions = regionDataJo.getJSONArray("regions");
        checkNotNull("区域总人数", regionDataJo, "regions");

        for (int i = 0; i < regions.size(); i++) {
            JSONObject single = regions.getJSONObject(i);
            JSONObject statistics = single.getJSONObject("statistics");
            int regionUv = statistics.getInteger("uv");
            if (regionUv > totalUv) {
                String regionName = single.getString("region_name");
                throw new Exception("[" + startTime + "-" + endTime + "], " + regionName + "的累计人数：" + regionUv + ", 大于总体的累计人数：" + totalUv);
            }
        }
    }

    public void compareAccumulatedToShop(JSONObject shopDataJo, JSONObject AccumulatedDataJo) throws Exception {
        int totalUv = shopDataJo.getInteger("uv");
        String uvCycleRatio = shopDataJo.getString("uv_cycle_ratio");

        JSONArray statisticsData = AccumulatedDataJo.getJSONArray("statistics_data");
        checkNotNull("全场累计客流>>>", AccumulatedDataJo, "statistics_data");

        String realTimeStr = "";
        String label = "";
        String chainRatio = "";
        for (int i = 0; i < statisticsData.size(); i++) {
            JSONObject single = statisticsData.getJSONObject(i);
            long time = single.getLong("time");
            if (System.currentTimeMillis() < time) {
                return;
            }

            String realTimeRes = single.getString("real_time");
            String chainRatioRes = single.getString("chain_ratio");

            String labelRes = single.getString("label");

            if (realTimeRes != null && !"".equals(realTimeRes)) {
                realTimeStr = realTimeRes;
                label = labelRes;
                chainRatio = chainRatioRes;
            } else {
                break;
            }
        }

        if (totalUv != Integer.valueOf(realTimeStr)) {
            throw new Exception("全场累计客流>>>" + label + "的实时人数：" + realTimeStr + ", 不等于实时概览的累计人数：" + totalUv);
        }

        if (!uvCycleRatio.equals(chainRatio)) {
            throw new Exception("全场累计客流>>>" + label + "的今日到场人数环比：" + chainRatio + ", 不等于实时概览的：" + uvCycleRatio);
        }
    }

    private void checkAgeGenderRate(JSONObject data, String function) throws Exception {
        JSONArray list = data.getJSONArray("ratio_list");

        if (list == null || list.size() != 12) {
            throw new Exception("年龄性别分布的类别为空，或者是不是12个分类。");
        }

        DecimalFormat df = new DecimalFormat("0.00");

        String[] ageGrp = new String[12];
        String[] percents = new String[12];
        int[] nums = new int[12];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String percent = single.getString("percent");
            double percentD = Double.valueOf(percent.substring(0, percent.length() - 1));
            percents[i] = df.format(percentD);
            nums[i] = single.getInteger("num");
            ageGrp[i] = single.getString("age_group");
            total += nums[i];
        }

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;

            String actualStr = df.format(actual);

            if (!percents[i].equals(actualStr)) {
                throw new Exception(function + "age_group: " + ageGrp[i] + " 对应的年龄性别比例错误！系统返回：" + percents[i] + ",期待：" + actualStr);
            }
        }
    }

    private void checkChainRatio(String function, String presentKey, String lastKey, boolean isRealTime, JSONObject data) throws Exception {
        JSONArray statisticsData = data.getJSONArray("statistics_data");
        for (int i = 0; i < statisticsData.size(); i++) {
            JSONObject single = statisticsData.getJSONObject(i);
            long time = single.getLongValue("time");
            if (time <= System.currentTimeMillis() - System.currentTimeMillis() % 36000000) {
                String label = single.getString("label");
                double realTime = single.getDouble(presentKey);
                double history = single.getDouble(lastKey);
                String chainRatio = single.getString("chain_ratio");
                chainRatio = chainRatio.substring(0, chainRatio.length() - 1);
                double expectRatio;

                if (history > 0) {
                    expectRatio = (realTime - history) / history * 100.0d;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String expectRatioStr = df.format(expectRatio);
                    if (expectRatio > 0) {
                        expectRatioStr = "+" + expectRatioStr;
                    }
                    if (!expectRatioStr.equals(chainRatio)) {
                        throw new Exception(function + label + "-期待环比数：" + expectRatioStr + ",系统返回：" + chainRatio);
                    }
                }
            }
        }
    }

    private void checkAgeGenderPercent(String function, JSONObject jo) throws Exception {

        JSONArray list = jo.getJSONArray("ratio_list");

        DecimalFormat df = new DecimalFormat("0.00");

        int[] nums = new int[list.size()];
        String[] percents = new String[list.size()];
        String[] ageGroups = new String[list.size()];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            int num = single.getInteger("num");
            nums[i] = num;
            String percentStr = single.getString("percent");
            Double percentD = Double.valueOf(percentStr.substring(0, percentStr.length() - 1));
            percents[i] = df.format(percentD) + "%";
            ageGroups[i] = single.getString("age_group");
            total += num;
        }

        if (total == 0) {
            for (int i = 0; i < percents.length; i++) {
                if (!"0.00%".equals(percents[i])) {
                    throw new Exception("总数为0，" + ageGroups[i] + "的比例是：" + percents[i]);
                }
            }
        }
        else {
            for (int i = 0; i < percents.length; i++) {
                float percent = (float) nums[i] / (float) total * 100;
                String percentStr = df.format(percent);

                percentStr += "%";

                if (!percentStr.equals(percents[i])) {
                    throw new Exception(function + "期待比例：" + percentStr + ", 系统返回：" + percents[i]);
                }
            }
        }

    }

    private void checkAgeGenderRatio(String function, JSONObject jo) throws Exception {

        DecimalFormat df = new DecimalFormat("0.0");

        String maleRatioStr = jo.getString("male_ratio_str");
        String maleStr = maleRatioStr.substring(0, maleRatioStr.length() - 1);

        double maleRatio = Double.valueOf(df.format(Double.valueOf(maleStr)));

        String femaleRatioStr = jo.getString("female_ratio_str");
        String femaleStr = femaleRatioStr.substring(0, femaleRatioStr.length() - 1);

        double femaleRatio = Double.valueOf(df.format(Double.valueOf(femaleStr)));
        System.out.println(maleRatio);
        System.out.println(femaleRatio);
        System.out.println(maleRatio + femaleRatio);
        if ((int) (maleRatio + femaleRatio) != 100) {
            throw new Exception(function + ",男女汇总比例之和不是100%，男：" + maleRatio + ",女：" + femaleRatio);
        }


    }

    private String getCustomerTraceJson(String startTime, String endTime, String customerId) {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
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
            Preconditions.checkArgument(typeNames[i].contains("新客") ||
                            typeNames[i].contains("高活跃顾客") ||
                            typeNames[i].contains("低活跃顾客") ||
                            typeNames[i].contains("流失客") ||
                            typeNames[i].contains("成交顾客"),
                    "客流身份不是 新客、高活跃顾客、低活跃顾客、流失客、成交顾客之一, type: " + typeNames[i]);
        }

        String[] typeNamesRes = new String[list.size()];
        String[] percentageStrs = new String[list.size()];
        int[] nums = new int[list.size()];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if ("-".equals(single.getString("percentage_str"))) {
                percentageStrs[i] = "-";
                continue;
            }
            String percentageStr = single.getString("percentage_str");
            percentageStrs[i] = percentageStr.substring(0, percentageStr.length() - 1);
            nums[i] = single.getInteger("num");
            typeNamesRes[i] = single.getString("type_name");
            total += nums[i];
            Preconditions.checkArgument(typeNamesRes[i].contains(""));
        }

        if (total == 0) {
            for (int i = 0; i < nums.length; i++) {
                if (!"-".equals(percentageStrs[i])) {
                    throw new Exception("总数是0，" + typeNames[i] + "比例为：" + percentageStrs[i]);
                }
            }
        }
        else {
            for (int i = 0; i < nums.length; i++) {
                double actual = ((double) nums[i] / (double) total) * (double) 100;
                DecimalFormat df = new DecimalFormat("0.00");
                String actualStr = df.format(actual);
                double actualD = Double.valueOf(actualStr);

                String resStr = df.format(Double.parseDouble(percentageStrs[i]));
                double resD = Double.valueOf(resStr);

                if (Math.abs(actual - resD) > 0.01) {
                    throw new Exception(function + "type_name: " + typeNamesRes[i] + " 对应的客流身份比例错误！返回：" + resStr + ",期待：" + actualStr);
                }
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

            if (expectRatio == 0 && Float.parseFloat(expectRatioStr) == 0) {

            } else if (!expectRatioStr.equals(chainRatio)) {
                throw new Exception(function + regionName + "-期待环比数：" + expectRatioStr + ",系统返回：" + chainRatio);
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
                        throw new Exception("region_id: " + regionIds[k] + " 对应的区域动线比例错误！系统返回：" + ratios[k] + ",期待：" + actualStr);
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
            Assert.assertEquals(valueRes, value, function + key + "字段值不相符：列表返回：" + valueRes + ", 期待：" + value);
        } else {
            if (valueRes == null || "".equals(valueRes)) {
                throw new Exception(function + key + "字段值为空！");
            }
        }
    }

    private void checkKeyMoreOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDoubleValue(key);


        if (!(valueRes >= value)) {
            throw new Exception(key + "字段，期待>=" + value + "系统返回的value为：" + valueRes);
        }
    }

    private void checkKeyLessOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDoubleValue(key);

        if (!(valueRes <= value)) {
            throw new Exception(function + "," + key + "字段，期待<=" + value + "系统返回的value为：" + valueRes);
        }
    }

    private void checkKeyLessOrEqualKey(JSONObject jo, String key1, String key2, String function) throws Exception {

        checkNotNull(function, jo, key1);
        checkNotNull(function, jo, key2);

        double value1 = jo.getDoubleValue(key1);
        double value2 = jo.getDoubleValue(key2);

        //防止取值方面出现问题，value为空的时候也是不符合的
        if (!(value1 <= value2)) {
            throw new Exception(function + key1 + "：" + value1 + "，应该<=" + key2 + "：" + value2);
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

    private String getRealTimeParamJson() {

        return "{\"shop_id\":" + SHOP_ID_ENV + "}";
    }

    private String getHistoryParamJson(String startTime, String endTime) {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
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

        json += "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                "    \"page\":1,\n" +
                "    \"size\":100\n" +
                "}";

        return json;
    }

    private String getstaffAddParamJson(String name, String phone, String faceUrl, String staffType) {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"staff_name\":\"" + name + "\",\n" +
                        "    \"phone\":\"" + phone + "\",\n" +
                        "    \"face_url\":\"" + faceUrl + "\",\n" +
                        "    \"staff_type\":\"" + staffType + "\"" +
                        "}\n";

        return json;
    }

    private String getActivityAddParamJson(String name, String type, String regionId, String contrastStart, String contrastEnd,
                                           String startDate, String endDate, String influenceStart, String influenceEnd) {

        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\",\n" +
                        "    \"activity_type\":\"" + type + "\",\n" +
                        "    \"regions\":[" +
                        regionId +
                        "    ],\n" +
                        "    \"contrast_start\":\"" + contrastStart + "\",\n" +
                        "    \"contrast_end\":\"" + contrastEnd + "\",\n" +
                        "    \"start_date\":\"" + startDate + "\",\n" +
                        "    \"end_date\":\"" + endDate + "\",\n" +
                        "    \"influence_start\":\"" + influenceStart + "\",\n" +
                        "    \"influence_end\":\"" + influenceEnd + "\",\n" +

                        "}";

        return json;
    }

    private String getActivityListParamJson(String name, String type, String startDate, String endDate) {

        String json =
                "{\n";
        if (!"".equals(name)) {
            json += "    \"activity_name\":\"" + name + "\",\n";
        }

        if (!"".equals(type)) {
            json += "    \"activity_type\":\"" + type + "\",\n";
        }

        if (!"".equals(startDate)) {
            json += "    \"start_date\":\"" + startDate + "\",\n";
        }

        if (!"".equals(endDate)) {
            json += "    \"end_date\":\"" + endDate + "\",\n";
        }

        json += "    \"shop_id\":\"" + SHOP_ID_ENV + "\"";
        json += "}";

        return json;
    }

    private String getActivityDetailParamJson(String id) {

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}";

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
            dingPush("越秀线上 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();

        if (DEBUG.trim().equals("true")) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);
        }
        alarmPush.onlineMonitorPvuvAlarm(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());

    }

    private void dingPushFinal() {
        if (this.FAIL) {
            AlarmPush alarmPush = new AlarmPush();
            if (DEBUG.trim().equals("true")) {
                alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            } else {
                alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);
            }
            //15898182672 华成裕
            //15011479599 谢志东
            String[] rd = {"15011479599"};
            alarmPush.alarmToRd(rd);
        }
    }

//    ----------------------------------调用接口的方法---------------------------------------------------------------

//    ------------------------------------------1.3 门店选择-----------------------------------------------------------

    public JSONObject shopList() throws Exception {

        String path = "/yuexiu/shop/list";
        String json = "{}";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

//---------------------------------------------二、今日实时统计-----------------------------------------------------------

    public JSONObject realTimeShop() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "shop";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeRegions() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "region";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeAccumulated() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "persons/accumulated";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeAgeGenderDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "age-gender/distribution";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeCustomerTypeDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "customer-type/distribution";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeEntranceRankDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "entrance/rank";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeThermalMapDistribution() throws Exception {
        String json = getRealTimeParamJson();
        String path = REAL_TIME_PREFIX + "region/thermal_map";
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    public JSONObject realTimeWanderDepth() throws Exception {
        String path = REAL_TIME_PREFIX + "wander-depth";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    --------------------------------------三、历史数据统计----------------------------------------------------

    public JSONObject historyShopCode1000(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "shop";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public String historyShopNoCode(String startTime, String endTime) {
        String path = HISTORY_PREFIX + "shop";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPost(path, json);

        return resStr;
    }

    public JSONObject historyRegion(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "region";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyAccumulated(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "persons/accumulated";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyAgeGenderDistribution(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "age-gender/distribution";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyCustomerTypeDistribution(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "customer-type/distribution";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyEntranceRank(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "entrance/rank";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyRegionCycle(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "region/cycle";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject historyWanderDepth(String startTime, String endTime) throws Exception {
        String path = HISTORY_PREFIX + "wander-depth";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    ----------------------------------------四、单人轨迹数据----------------------------------

    public JSONObject customerTrace(String startTime, String endTime, String customerId) throws Exception {
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String json = getCustomerTraceJson(startTime, endTime, customerId);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    --------------------------------------五、区域客流数据-----------------------------------------

    public JSONObject regionMovingDirection(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "moving-direction";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject regionEnterRank(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "enter/rank";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject regionCrossData(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "cross-data";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject regionMoveLineRank(String startTime, String endTime) throws Exception {
        String path = REGION_DATA_PREFIX + "move-line/rank";
        String json = getHistoryParamJson(startTime, endTime);
        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

//    ----------------------------------六、顾客管理------------------------------------------------------

    public JSONObject manageCustomerTypeList() throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerAgeGroupList() throws Exception {
        String path = "/yuexiu/manage/age/group/list";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerList(String customerType, String gender, String ageGroupId) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "list";

        String json = getCustomerListParamJson(customerType, gender, ageGroupId);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject EditCustomer(String customerId, String customerName, String faceUrl, String signedType) throws Exception {
        String path = "/yuexiu/manage/customer/edit/" + customerId;

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"signed_type\":\"" + signedType + "\",\n" +
                        "    \"face_url\":\"" + faceUrl + "\",\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + "\n" +
                        "}";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject customerToStaff(String customerId) throws Exception {
        String path = "/yuexiu/manage/customer/toStaff";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + "\n" +
                        "}";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
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
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"face_url\":\"" + faceUrl + "\"\n" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerDetail(String customerId) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "detail";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + "," +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerDayAppearList(String customerId) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "day/appear/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + "," +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject customerEdit(String customerId, String customerName, String faceUrl, String signedType) throws Exception {
        String path = MANAGE_CUSTOMER_PREFIX + "edit/" + customerId;

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"shop_id\":10,\n" +
                        "    \"face_url\":\"" + faceUrl + "\",\n" +
                        "    \"signed_type\":\"" + signedType + "\"\n" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    //    ---------------------------------------------八、顾客洞察-----------------------------------------
    public JSONObject analysisCustomerTypeList() throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer-type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject analysisCustomerQualityCode1000(String startTime, String endTime, String type) throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer-quality";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"customer_type\":\"" + type + "\"" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public String analysisCustomerQualityNoCode(String startTime, String endTime, String type) {
        String path = ANALYSIS_DATA_PREFIX + "customer-quality";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"customer_type\":\"" + type + "\"" +
                        "}\n";

        String resStr = httpPost(path, json);

        return resStr;
    }

    public JSONObject analysisCustomerType(String startTime, String endTime) throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer/type";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject analysisCustomerlifeCycle(String startTime, String endTime, String influenceStart, String influenceEnd) throws Exception {
        String path = ANALYSIS_DATA_PREFIX + "customer/life-cycle";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"," +
                        "    \"influence_start\":\"" + influenceStart + "\",\n" +
                        "    \"influence_end\":\"" + influenceEnd + "\"" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    //    -----------------------------九、员工管理接口---------------------------------------------------------
    public JSONObject staffTypeList() throws Exception {
        String path = MANAGE_STAFF_PREFIX + "type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffAddCode1000(String name, String phone, String faceUrl, String staffType) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "add";

        String json = getstaffAddParamJson(name, phone, faceUrl, staffType);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public String staffAddNoCode(String name, String phone, String faceUrl, String staffType, int expectCode) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "add";

        String json = getstaffAddParamJson(name, phone, faceUrl, staffType);

        return httpPost(path, json);
    }

    public JSONObject staffList(String namePhone, String staffType) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "page";

        String json =
                "{\n";
        if (!"".equals(namePhone)) {
            json += "    \"name_phone\":\"" + namePhone + "\",\n";
        }

        if (!"".equals(staffType)) {
            json += "    \"staff_type\":\"" + staffType + "\",\n";
        }

        json += "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                "    \"page\":1,\n" +
                "    \"size\":" + pageSize + "\n" +
                "}";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffDetail(String id) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "detail";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject staffAttendancePage(String id) throws Exception {
        String path = MANAGE_STAFF_PREFIX + "attendance/page";

        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100,\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public String staffEditNoCode(String id, String name, String phone, String faceUrl, String staffType) {
        String path = MANAGE_STAFF_PREFIX + "edit/" + id;

        String json = getstaffAddParamJson(name, phone, faceUrl, staffType);

        String resStr = httpPost(path, json);

        return resStr;
    }

    public JSONObject staffDelete(String id) {
        String url = getIpPort() + "/yuexiu/manage/staff/delete/" + id;
//        String path = MANAGE_STAFF_PREFIX + "delete/" + id;

        String json =
                "{}";

        String resStr = null;
        JSONObject data = null;
        try {

            HashMap<String, String> header = new HashMap<>();
            header.put("authorization", authorization);

            resStr = httpDelete(url, json, header, StatusCode.SUCCESS);
            data = JSON.parseObject(resStr).getJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    //    -------------------------------------------------十一、活动相关接口------------------------------------------------------------

    public JSONObject activityTypeList() throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "type/list";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityRegionList() throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "region/list";

        String json = getRealTimeParamJson();

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityAdd(String name, String type, String regionId, String contrastStart, String contrastEnd,
                                  String startDate, String endDate, String influenceStart, String influenceEnd) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "add";

        String json = getActivityAddParamJson(name, type, regionId, contrastStart, contrastEnd, startDate, endDate, influenceStart, influenceEnd);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityList(String name, String type, String startDate, String endDate) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "list";

        String json = getActivityListParamJson(name, type, startDate, endDate);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityDelete(String id) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "delete/" + id;

        String json = "{}";

        HashMap<String, String> header = new HashMap<>();
        header.put("authorization", authorization);

        String resStr = httpDelete(path, json, header, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityDetail(String id) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "detail";

        String json = getActivityDetailParamJson(id);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityPassengerFlowContrast(String id) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "passenger-flow/contrast";

        String json = getActivityDetailParamJson(id);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityRegionEffect(String id) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "region/effect";

        String json = getActivityDetailParamJson(id);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject activityCustomerTypeEffect(String id) throws Exception {
        String path = MANAGE_ACTIVITY_PREFIX + "customer-type/effect";

        String json = getActivityDetailParamJson(id);

        String resStr = httpPostCode1000(path, json, StatusCode.SUCCESS);
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

        String json = this.jsonOnline;
        String path = this.loginPathOnline;
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
                "map_url"
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_REGIONS_NOT_NULL")
    private static Object[] realTimeRegionNotNull() {
        return new Object[]{
                "region_id",
                "region_name",
                "[location]",
                "{statistics}-uv",
                "{statistics}-pv",
                "{statistics}-rank",
                "{statistics}-stay_time"
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_REGIONS_VALIDITY")
    private static Object[] realTimeRegionValidity() {
        return new Object[]{
                "[location]-x>=0",
                "[location]-x<=1",
                "[location]-y>=0",
                "[location]-y<=1",
                "{statistics}-uv>=0",
                "{statistics}-pv>=0",
                "{statistics}-uv[<=]pv",
                "{statistics}-stay_time>=0",
                "{statistics}-stay_time<=600"
        };
    }

    //    -------------------------------2.3 全场累计客流（实时）----------------------------

    @DataProvider(name = "REAL_TIME_PERSONS_ACCUMULATED_NOT_NULL")
    private static Object[] realTimePersonsAccumulatedNotNull() {
        return new Object[]{
                "[statistics_data]-history",
//                "[statistics_data]-chain_ratio",
                "[statistics_data]-label",
                "[statistics_data]-time",
//                "last_statistics_time"
        };
    }

    @DataProvider(name = "REAL_TIME_PERSONS_ACCUMULATED_VALIDITY")
    private static Object[] realTimePersonsAccumulatedValidity() {
        return new Object[]{
                "[statistics_data]-real_time>0",
                "[statistics_data]-history>0",
        };
    }

    //    -----------------------------2.4 全场客流年龄/性别分布--------------------------------------
    @DataProvider(name = "REAL_TIME_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    private static Object[] realTimeAgeGenderDIstributionNotNull() {
        return new Object[]{
                "[ratio_list]-age_group",
                "[ratio_list]-gender",
                "[ratio_list]-percent",
                "[ratio_list]-ratio",
                "male_ratio_str",
                "female_ratio_str"
        };
    }

    //------------------------------------------2.5 实时客流身份分布--------------------------------
    @DataProvider(name = "REAL_TIME_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    private static Object[] realTimeCustomerTypeDistributionNotNull() {
        return new Object[]{
                "[list]-type",
                "[list]-type_name",
//                "[list]-percentage",
                "[list]-percentage_str"
        };
    }

//    ------------------------------------2.6 实时出入口客流流量排行------------------------------------------------

    @DataProvider(name = "REAL_TIME_ENTRANCE_RANK_NOT_NULL")
    private static Object[] realTimeEntranceRankNotNull() {
        return new Object[]{
                "[list]-entrance_id",
                "[list]-entrance_name",
                "[list]-num",
//本次迭代废弃字段
//                "[list]-action"
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
                "points"
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
                "[statistics_data]-time", "[statistics_data]-history"
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
                "{statistics}-stay_time"
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
                "[statistics_data]-label"
        };
    }

    @DataProvider(name = "HISTORY_PERSONS_ACCUMULATED_VALIDITY")
    private static Object[] historyPersonsAccumulatedValidity() {
        return new Object[]{
                "[statistics_data]-present_cycle>=0",
                "[statistics_data]-last_cycle>=0"
        };
    }

    //    ---------------------------------------3.4 全场客流年龄性别分布---------------------------------------
    @DataProvider(name = "HISTORY_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    private static Object[] historyAgeGenderDistributionNotNull() {
        return new Object[]{
                "[ratio_list]-age_group",
                "[ratio_list]-gender",
                "[ratio_list]-ratio",
                "[ratio_list]-percent",
                "male_ratio_str",
                "female_ratio_str"
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
//                "[list]-action",
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
                "stay_times>=1",
                "first_appear_time>=1",
                "stay_time_per_times>=1",
//                "stay_time_per_times<=900",
                "first_appear_time[<=]last_appear_time"
        };
    }

//    ----------------------------------------4.2 区域人物轨迹--------------------------------------

    @DataProvider(name = "CUSTOMER_TRACE_DATA_NOT_NULL")
    private static Object[] customerTraceNotNull() {
        return new Object[]{
                "last_query_time", "[regions]-region_id", "map_url",
//                "[region_turn_points]-region_a", "[region_turn_points]-region_b",
//                "[moving_lines]-source", "[moving_lines]-target", "[moving_lines]-move_times"
        };
    }

    @DataProvider(name = "CUSTOMER_TRACE_TRACES_NOT_NULL")
    private static Object[] customerTraceTracesNotNull() {

        return new Object[]{
//                "{location}-x", "{location}-y", "{location}-region_id", "face_url", "time"
                "{location}-x", "{location}-y", "{location}-region_id", "time"
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
                "[list]-num"
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
                "num",
                "ratio_str"
        };
    }

//    --------------------------------------5.4 热门动线排行--------------------------------------------

    @DataProvider(name = "REGION_MOVE_LINE_RANK_NOT_NULL")
    private static Object[] regionMoveLineRankNotNull() {
        return new Object[]{
                "[list]-region_first",
                "[list]-region_second",
                "[list]-num"
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
                "[list]-show_url", "[list]-customer_id", "[list]-customer_type_name",
                "[list]-gender", "[list]-last_visit_time"
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_FACE_LIST_NOT_NULL")
    private static Object[] manageCustomerFaceListNotNull() {
        return new Object[]{
                "[list]-face_url", "[list]-show_url", "[list]-customer_id", "[list]-customer_type_name",
                "[list]-age_group", "[list]-gender", "[list]-last_visit_time"
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_DETAIL_DATA_NOT_NULL")
    private static Object[] manageCustomerDetailDataNotNull() {
        return new Object[]{
                "customer_id", "show_url", "customer_type", "first_appear_time", "last_appear_time",
                "stay_times", "gender", "customer_type_name", "stay_time_per_times",
        };
    }

    @DataProvider(name = "MANAGE_CUSTOMER_DETAIL_DATA_VALIDITY")
    private static Object[] manageCustomerDetailValidity() {
        return new Object[]{
                "stay_times>=1",
                "stay_time_per_times>=1",
                "stay_time_per_times<=1440"
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
                "[new_customer_analysis]-num", "[new_customer_analysis]-customer_ratio", "[new_customer_analysis]-customer_ratio_str",
                "[new_customer_analysis]-ratio_str",

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
                "[customer_type]-type", "[customer_type]-type_name", "[relations]"
        };
    }

    @DataProvider(name = "ANALYSIS_CUSTOMER_LIFE_CYCLE_RELATIONS_NOT_NULL")
    private static Object[] analysisCustomerLifeCycleRelationsNotNull() {
        return new Object[]{
                "type",
                "[direction_list]-type", "[direction_list]-type_name", "[direction_list]-ratio",
                "[direction_list]-ratio_str", "[direction_list]-num",
        };
    }

//    ------------------------------------------九、员工管理接口-----------------------------------------------------------

//    ---------------------------------------9.1 员工身份列表-------------------------------------------------------

    @DataProvider(name = "MANAGE_STAFF_TYPE_LIST_NOT_NULL")
    private static Object[] manageStaffTypeListNotNull() {
        return new Object[]{
                "[list]-staff_type", "[list]-type_name"
        };
    }

//    ------------------------------------------9.3 员工列表--------------------------------------------------------

    @DataProvider(name = "MANAGE_STAFF_LIST_NOT_NULL")
    private static Object[] manageStaffListNotNull() {
        return new Object[]{
                "[list]-shop_id", "[list]-id", "[list]-staff_id", "[list]-staff_name", "[list]-gender", "[list]-age",
                "[list]-phone", "[list]-staff_type", "[list]-type_name", "[list]-show_url", "[list]-register_time",
        };
    }

    //    ------------------------------------------9.4 员工详情--------------------------------------------------------

    @DataProvider(name = "MANAGE_STAFF_DETAIL_NOT_NULL")
    private static Object[] manageStaffDetailNotNull() {
        return new Object[]{
                "shop_id", "id", "staff_id", "staff_name", "staff_type", "gender", "age",
                "phone", "type_name", "show_url", "register_time"
        };
    }

    //    ------------------------------------------9.5 员工考勤列表--------------------------------------------------------

    @DataProvider(name = "MANAGE_STAFF_ATTENDANCE_LIST_NOT_NULL")
    private static Object[] manageStaffAttendanceListNotNull() {
        return new Object[]{
                "day", "first_appear_time", "last_appear_time", "total_stay_time",
                "state", "state_name", "id"
        };
    }

//    -------------------------------------11.1 获取活动类型----------------------------------------------------------

    @DataProvider(name = "ACTIVITY_TYPE_LIST_NOT_NULL")
    private static Object[] activityTypeListNotNull() {
        return new Object[]{
                "[list]-activity_type", "[list]-type_name"
        };
    }

//    --------------------------------------11.2 门店区域----------------------------------------------------------

    @DataProvider(name = "ACTIVITY_REGION_LIST_NOT_NULL")
    private static Object[] activityRegionListNotNull() {
        return new Object[]{
                "[list]-region_id", "[list]-region_name"
        };
    }

//    --------------------------------------11.4 活动列表---------------------------------------------------------

    @DataProvider(name = "ACTIVITY_LIST_NOT_NULL")
    private static Object[] activityListNotNull() {
        return new Object[]{
                "[list]-id", "[list]-activity_name", "[list]-start_date", "[list]-end_date",
                "[list]-activity_type_name"
        };
    }

//    --------------------------------------11.6 活动详情------------------------------------------------------------

    @DataProvider(name = "ACTIVITY_DETAIL_NOT_NULL")
    private static Object[] activityDetailNotNull() {
        return new Object[]{
                "id", "active_name", "activity_type_name", "[region_names]",

                "{contrast_cycle}-start_date", "{contrast_cycle}-end_date", "{contrast_cycle}-new_num", "{contrast_cycle}-old_num",
                "{contrast_cycle}-new_ratio", "{contrast_cycle}-old_ratio", "{contrast_cycle}-stay_time_per_person",

                "{this_cycle}-start_date", "{this_cycle}-end_date", "{this_cycle}-new_num", "{this_cycle}-old_num",
                "{this_cycle}-new_ratio", "{this_cycle}-old_ratio", "{this_cycle}-stay_time_per_person",

                "{influence_cycle}-start_date", "{influence_cycle}-end_date", "{influence_cycle}-new_num", "{influence_cycle}-old_num",
                "{influence_cycle}-new_ratio", "{influence_cycle}-old_ratio", "{influence_cycle}-stay_time_per_person"


        };
    }

//    --------------------------------------------------11.7 活动客流对比----------------------------------------------------------

    @DataProvider(name = "ACTIVITY_PASSENGER_FLOW_CONSTRAST_NOT_NULL")
    private static Object[] activityPassengerFlowContrastNotNull() {
        return new Object[]{
                "[contrast_cycle]-date", "[contrast_cycle]-num",

                "[this_cycle]-date", "[this_cycle]-num",

                "[influence_cycle]-date", "[influence_cycle]-num"
        };
    }

//    ------------------------------------------------11.8 活动区域效果----------------------------------------------------------

    @DataProvider(name = "ACTIVITY_REGION_EFFECT_NOT_NULL")
    private static Object[] activityRegionEffectNotNull() {
        return new Object[]{
                "[list]-region_id", "[list]-region_name", "[list]-contrast_cycle", "[list]-this_cycle", "[list]-influence_cycle",
        };
    }

//    -------------------------------------------------11.9 客群留存效果---------------------------------------------------------------

    @DataProvider(name = "ACTIVITY_CUSTOMER_TYPE_EFFECT_NOT_NULL")
    private static Object[] activityCustomerTypeEffectNotNull() {
        return new Object[]{
                "[list]-customer_type", "[list]-type_name", "[list]-contrast_cycle_num", "[list]-this_cycle_num", "[list]-influence_cycle_num",
                "[list]-contrast_cycle_ratio", "[list]-this_cycle_ratio", "[list]-influence_cycle_ratio"
        };
    }

    @DataProvider(name = "DAY_SPAN_4")
    private static Object[] daySpan4() {
        return new Object[]{
                1,
                7,
                30,
                -1
        };
    }

    @DataProvider(name = "DAY_SPAN_3")
    private static Object[] daySpan3() {
        return new Object[]{
                1,
                7,
                30
        };
    }

    @DataProvider(name = "ACTIVITY_CHECK")
    private static Object[][] activityCheck() {
        return new Object[][]{
                new Object[]{
                        "6", 1, "2019-11-10", "2019-11-10", "2019-11-11", "2019-11-11", "2019-11-12", "2019-11-12"
                },
                new Object[]{
                        "5", 2, "2019-11-07", "2019-11-08", "2019-11-09", "2019-11-10", "2019-11-11", "2019-11-12"
                }
        };
    }

    @DataProvider(name = "ACTIVITY_OTHERS_CHECK")
    private static Object[][] activityAnalysisCheck() {
        return new Object[][]{
                new Object[]{
                        "6", "oneDay", "2019-11-10", "2019-11-10", "2019-11-11", "2019-11-11", "2019-11-12", "2019-11-12"
                },
                new Object[]{
                        "5", "twoDays", "2019-11-07", "2019-11-08", "2019-11-09", "2019-11-10", "2019-11-11", "2019-11-12"
                }
        };
    }
}
