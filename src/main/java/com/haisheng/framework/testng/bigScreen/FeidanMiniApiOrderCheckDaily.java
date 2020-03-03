package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import org.apache.commons.lang.text.StrSubstitutor;
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

import java.io.*;
import java.util.*;

//import org.apache.pdfbox.util.PDFTextStripper;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiOrderCheckDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    //  -----------------------------------------渠道------------------------------------------
    String wudongChannelIdStr = "5";
    String wudongChannelNameStr = "测试【勿动】";
    int wudongChannelInt = 5;
    String wudongOwnerPhone = "16600000000";


    String maiTianChannelStr = "2";
    String maiTianChannelNameStr = "麦田";
    int maiTianChannelInt = 2;

    String lianjiaChannelStr = "1";
    int lianjiaChannelInt = 1;
    String lianjiaChannelName = "链家";
    String lianjiaOwnerPhone = "16600000001";

//  ------------------------------------------业务员-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String lianjiaFreezeStaffName = "链家-【勿动】";
    String lianjiaFreezeStaffPhone = "14112345678";

    String lianjiaStaffIdStr = "2136";
    int lianjiaStaffIdInt = 2136;
    String lianjiaStaffName = "链家业务员";
    String lianjiaStaffPhone = "17711111024";


    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098";
    int wudongStaffIdInt = 2098;

    String maiTianStaffStr = "2";
    int maiTianStaffInt = 2;
    int maitianStaffIdInt = 2449;
    String maitianStaffIdStr = "2449";
    String maitianStaffName = "喵喵喵";
    String maitianStaffPhone = "14422110039";


//    -------------------------------------------置业顾问-----------------------------------------------------

    String anShengIdStr = "15";
    String anShengName = "安生";
    String anShengPhone = "16622222222";

    String zhangIdStr = "8";
    int zhangIdInt = 8;
    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";

    long wudongReportTime = 1547024264000L;//2019-01-09 16:57:44
    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25

    long firstAppearTime = 1582684439509L;

    String normalOrderType = "NORMAL";
    String riskOrderType = "RISK";

    String defaultRuleId = "837";
    String ahead1hRuleId = "996";
    String ahead24hRuleId = "842";
    String ahead7dayRuleId = "844";
    String ahead30dayRuleId = "846";
    String aheadMaxRuleId = "1003";

    String protect1DayRuleId = "840";

    int pageSize = 10000;

    //    -----------------------------------------------测试case--------------------------------------------------------------


    //    @Test
    public void maitianPCT() throws Exception {

        String customerPhone = "12300000001";
        String customerName = "小麦";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        int channelId = 2;
        int channelStaffId = 2449;
        String channelStaffName = "喵喵喵";
        String channelStaffPhone = "14422110039";
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = afterReportTime - 25 * 60 * 60 * 1000;

        updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, beforeReportTime);
    }


    //        @Test
    public void PCT() throws Exception {

        String customerPhone = "14422110015";
        String smsCode = "805931";
        String customerName = "2333";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        int channelId = 1;
        int channelStaffId = 2124;
        String channelStaffName = lianjiaStaffName;
        String channelStaffPhone = lianjiaStaffPhone;
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = lianjiaReportTime;

        updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, beforeReportTime);
    }

    //    @Test
    public void PCF() throws Exception {

        String customerPhone = "14422110015";
        String smsCode = "805931";
        String customerName = "2334";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        int channelId = -1;
        String channelStaffName = "";
        String channelStaffPhone = "";
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = noChannelReportTime;

        updateReportTime_PCF(customerPhone, customerName, afterReportTime);

    }

    //@Test
    public void H5WuDong() throws Exception {

        String customerPhone = "14422110176";
        String smsCode = "805931";
        String customerName = "27.6";

        customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = wudongReportTime;

//        updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, afterReportTime);

    }

    //    @Test
    public void H5Lianjia() throws Exception {

        String customerPhone = "176****8107";
        String smsCode = "805931";
        String customerName = "猜猜猜";

        customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = lianjiaReportTime;

        updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, afterReportTime);
    }

    //@Test
    public void Self() throws Exception {

        String customerPhone = "14422110176";
        String selfCode = "783662";
        String smsCode = "387714";
        String customerName = "27.6";

        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = noChannelReportTime;

        selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");
        updateReportTime_S(customerPhone, customerName, afterReportTime);
    }

    //    @Test
    public void witnessUploadChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String function = "人证对比机数据上传>>>";

        try {

            String cardId = "100000000017566018";
            String personName = "不好";

            String s = witnessUpload(cardId, personName);

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + s);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//    --------------------------------------------------------正常单------------------------------------------

    /**
     * H5报备-顾客到场-创单（选择H5渠道）-更改置业顾问2次，置业顾问张钧甯
     * 选H5
     */
    @Test(dataProvider = "NORMAL")
    public void _H5ARule(String caseNamePro, String ruleId, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改置业顾问2次
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);
            customerEditPC(cid, customerName, customerPhone, zhangName, zhangPhone);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, reportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = zhangName;
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "正常";
//            String firstAppear = "-";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderLinkData, true, normalOrderType);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-创单（选择H5渠道）-更改姓名2次");

        }
    }

    /**
     * PC（有渠道）-顾客到场,置业顾问是张钧甯
     * 选PC报备渠道
     */
    @Test(dataProvider = "NORMAL")
    public void _PCTARule(String caseNamePro, String ruleId, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            // PC报备
            String customerPhone = "14422110015";
            String smsCode = "805931";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;
            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderLinkData, true, normalOrderType);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "PC（渠道报备）-顾客到场-创单（选择PC报备渠道）");
        }
    }

    /**
     * PC（无渠道）-顾客到场-创单（选择无渠道），置业顾问是张钧甯
     * 选无渠道
     */
    @Test
    public void _PCFA() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {

            // PC报备
            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = -1;

            newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerName, "MALE");
            updateReportTime_PCF(customerPhone, customerName, noChannelReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, normalOrderType);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "PC（无渠道）-顾客到场-创单（选择无渠道）");

        }
    }

    /**
     * 自助扫码(选自助)-顾客到场，置业顾问：安生
     */
    @Test
    public void _SA() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            更改置业顾问2次
            JSONArray list = customerList(customerName, "", "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);
            customerEditPC(cid, customerName, customerPhone, zhangName, zhangPhone);

//            更改登记时间
            updateReportTime_S(customerPhone, customerName, noChannelReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = zhangName;
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, normalOrderType);
        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "自助扫码（选自助）顾客到场--创单（选择无渠道）");

        }
    }

    /**
     * 顾客到场-自助扫码(选自助)，置业顾问：安生
     */
    @Test
    public void A_S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "安生";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, normalOrderType);
        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-自助扫码（选自助）-创单（选择无渠道）");

        }
    }

    /**
     * PC（无渠道）-顾客到场-创单（选择无渠道），置业顾问是张钧甯
     * 选无渠道
     */
    @Test
    public void A_PCF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-PC（无渠道）-创单（选择无渠道）";

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = -1;

            newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, normalOrderType);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);

        }
    }

    /**
     * H5报备-顾客到场-自助扫码(选H5)
     */
    @Test
    public void _H5AS() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, normalOrderType);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-自助扫码-创单（选择H5报备渠道）");
        }
    }

    /**
     * H5报备-顾客到场-自助扫码(选H5)
     */
    @Test
    public void _H5APCF() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerName = caseName + "-" + getNamePro();
            int channelId = -1;

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            pc无渠道登记
            newCustomer(channelId, "", "", "", "", customerPhone, customerName, "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, normalOrderType);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-PC（无渠道）-创单（选择H5报备渠道）");

        }
    }

//    -------------------------------------------------异常单-------------------------------------------------------

    /**
     * 顾客到场-H5，无置业顾问
     * 选H5
     */
    @Test(dataProvider = "RISK_1")
    public void A_H5(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            // 报备
            String customerPhone = "18210113587";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "805805";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于" + aheadTime, "该顾客的风控规则为提前报备时间:" + aheadTime);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);
            saveData(aCase, ciCaseName, caseName, "顾客到场-H5报备-创单（选择H5报备渠道）");
        }
    }

    /**
     * 顾客到场-PC(有渠道)，置业顾问：张钧甯
     * 选PC报备渠道
     */
    @Test(dataProvider = "RISK_1_1")
    public void A_PCTRule(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            // PC报备
            String customerPhone = "14422110001";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "209237";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于" + aheadTime, "该顾客的风控规则为提前报备时间:" + aheadTime);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "顾客到场-PC（有渠道）-创单（选择PC报备渠道）");
        }
    }

    /**
     * 顾客到场-PC报备-H5报备，置业顾问：张钧甯
     * 选PC报备渠道
     */
    @Test
    public void A_PCTH5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110002";
            String customerName = caseName + "-" + getNamePro();

            // PC报备
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long repTimeLianJia = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, repTimeLianJia);

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            long repTimeWuDong = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, repTimeWuDong);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "384435";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = repTimeLianJia + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于0h0min", "该顾客的风控规则为提前报备时间:0h0min");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-PC报备-H5报备-创单（选择PC报备渠道）");

        }
    }

    /**
     * 顾客到场-PC报备-H5报备，置业顾问：张钧甯
     * 选PC报备渠道
     */
    @Test(dataProvider = "RISK_1_1")
    public void A_PCFH5Rule(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110002";
            String customerName = caseName + "-" + getNamePro();

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            // PC报备
            int channelId = -1;
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;

            newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerName, "MALE");
            updateReportTime_PCF(customerPhone, customerName, reportTime - 20 * 60 * 1000);

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            long repTimeWuDong = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, repTimeWuDong);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "384435";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "顾客到场-PC报备-H5报备-创单（选择PC报备渠道）");

        }
    }

    /**
     * 顾客到场-H5报备-自助扫码，无置业顾问
     * 顾客选H5
     */
    @Test
    public void A_H5S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            long repTimeWuDong = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, repTimeWuDong);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "aa", "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, wudongChannelInt, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = repTimeWuDong + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于0h0min", "该顾客的风控规则为提前报备时间:0h0min");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-H5报备-自助扫码-创单（选择H5报备渠道）");
        }
    }

    /**
     * H5报备-顾客到场-自助扫码(选H5)
     */
    @Test
    public void H5_SA() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");
            updateReportTime_S(customerPhone, customerName, noChannelReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "安生";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-自助扫码-创单（选择H5报备渠道）");

        }
    }

    /**
     * H5报备-顾客到场-H5报备(不同渠道)，无置业顾问
     * 选后者
     */
    @Test(dataProvider = "RISK_2")
    public void H5A_H5Rule(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            String customerPhone = "14422110004";
            String customerName = caseName + "-" + getNamePro();

//            H5报备（链家）
            customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

//            更改报备时间

            updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, reportTime1);

//            H5报备（测试【勿动】）
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, reportTime2);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "704542";
            createOrder(customerPhone, orderId, faceUrl, wudongChannelInt, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime2 + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于" + aheadTime, "该顾客的风控规则为提前报备时间:" + aheadTime);
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家-【勿动】", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备（渠道A）-顾客到场-H5报备(渠道B)--创单（选择渠道B）");
        }
    }

    /**
     * H5报备-顾客到场-PC报备，置业顾问：张钧甯
     * 选PC
     */

    @Test(dataProvider = "RISK_2_2")
    public void H5A_PCTRule(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            String customerPhone = "14422110005";
            String customerName = ciCaseName + "-" + getNamePro();

            // PC报备
            int channelId = 1;
            int channelStaffId = 2124;

            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            更改PC报备时间
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime2);

//            H5报备
            customerReportH5("2098", customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime1);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "127230";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime2 + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于0h0min", "该顾客的风控规则为提前报备时间:0h0min");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);

            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-PC报备-创单（选择PC报备渠道）");

        }
    }

    /**
     * H5报备-顾客到场-自助扫码，置业顾问：安生
     * 选自助
     */
    @Test(dataProvider = "NORMAL")
    public void H5A_SRule(String caseNamePro, String ruleId, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "安生";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-自助扫码-创单（选择无渠道）");

        }
    }

    /**
     * H5报备-顾客到场-PC报备-自助扫码，置业顾问：张钧甯
     * 选PC
     */
    @Test
    public void H5A_PCTS() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            PC报备
            int channelId = 1;
            int channelStaffId = 2124;
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long repTimeLianJia = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, repTimeLianJia);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);


            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = repTimeLianJia + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于0h0min", "该顾客的风控规则为提前报备时间:0h0min");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-PC报备-自助扫码-创单（选择PC报备渠道）");

        }
    }

    /**
     * H5报备-顾客到场-PC报备-自助扫码，置业顾问：安生
     * 选自助扫码
     */
    @Test
    public void H5APCT_S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            PC报备
            String adviserName = anShengName;
            String adviserPhone = anShengPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;


            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long repTimeLianJia = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, repTimeLianJia);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证

            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String channelName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, "-", "-", orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 4);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-PC报备-自助扫码-创单（选择无渠道）");

        }
    }

    /**
     * H5报备-PC报备-顾客到场-自助扫码，置业顾问：安生
     * 选自助扫码
     */
    @Test
    public void H5PCTA_S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            PC报备
            String adviserName = anShengName;
            String adviserPhone = anShengPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, lianjiaReportTime);

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证

            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String channelName = "-";
            channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 4);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-PC报备-顾客到场-自助扫码-创单（选择无渠道）");

        }
    }

    /**
     * H5报备-顾客到场-H5
     * 选前者
     */
    @Test(dataProvider = "RISK_2_2")
    public void _H5AH5Rule(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

//            H5报备(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime1);

//            H5报备（链家）
            customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);
            updateReportTimeChannel(customerPhone, customerName, lianjiaFreezeStaffIdInt, lianjiaFreezeStaffIdInt, reportTime2);

//            刷证
            witnessUpload(genCardId(), customerName);


            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime1 + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家-【勿动】", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5（渠道A）-顾客到场-H5（渠道B）-创单（选择渠道A）");

        }
    }

    /**
     * H5报备-顾客到场-PC报备
     * 选H5
     */
    @Test(dataProvider = "RISK_2_2")
    public void _H5APCTRule(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String smsCode = "805805";

            String customerName = caseName + "-" + getNamePro();

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            // PC报备
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime2);

//            H5报备
            customerReportH5("2098", customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime1);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            adviserName = "-";
            String channelName = "测试【勿动】";
            channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime1 + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-PC报备-创单（选择H5报备渠道）");
        }
    }

    /**
     * H5报备-PC报备-顾客到场
     * 选H5报备渠道
     */
    @Test(dataProvider = "RISK_2_2")
    public void _H5PCTARule(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110012";
            String smsCode = "748338";
            String customerName = caseName + "-" + getNamePro();

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            updateReportTimeChannel(customerPhone, customerName, 5, wudongStaffIdInt, reportTime1);

            // PC报备
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            updateReportTimeChannel(customerPhone, customerName, 1, channelStaffId, reportTime2);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            adviserName = "-";
            String channelName = "测试【勿动】";
            channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime1 + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);

            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备-PC报备-顾客到场-创单（选择H5报备渠道）");

        }
    }

    /**
     * H5报备-顾客到场
     * 成单时选无渠道
     */
    @Test(dataProvider = "NORMAL")
    public void H5A_NoChnanelRule(String caseNamePro, String ruleId, long reportTime) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110013";
            String smsCode = "105793";
            String customerName = caseName + "-" + getNamePro();

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);
//            H5报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            updateReportTimeChannel(customerPhone, customerName, 5, wudongStaffIdInt, reportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-创单（选择无渠道）");

        }
    }


//    -------------------------------------------顾客信息修改---------------------------------------------------------

//    ------------------------------------------更改手机号---------------------------------------------------

    /**
     * H5-顾客到场，没有置业顾问，更改手机号
     * 选H5
     */
    @Test
    public void _H5AChngPhoneRule() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhoneB = "18210113588";
            String customerPhoneA = "18210113587";
            String smsCode = "805805";

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

            JSONArray list = customerList(customerPhoneB, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

//            更改手机号
            customerEditPC(cid, customerName, customerPhoneA, "", "");

            updateReportTimeChannel(customerPhoneA, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            list = orderList(-1, "", 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            createOrder(customerPhoneA, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhoneA, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, wudongReportTime + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "INFO_CHANGE", "18210113588更改为18210113587", "顾客⼿机号被修改");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-更改手机号-创单（选择H5报备渠道）");

        }
    }

//    -------------------------------更改置业顾问3次-----------------------------------------

    /**
     * PC（有渠道）-顾客到场,置业顾问是张钧甯,更改置业顾问3次
     * 选PC报备渠道
     */
    @Test
    public void _PCTAChngAdviser3Rule() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110015";
            String smsCode = "805931";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;
            int channelStaffId = 2124;

            channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONArray list = customerList(customerPhone, lianjiaChannelStr, "8", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);
            customerEditPC(cid, customerName, customerPhone, zhangName, zhangPhone);
            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, lianjiaReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);


            list = orderList(-1, "", 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            adviserName = anShengName;

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, lianjiaReportTime + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "INFO_CHANGE", "张钧甯更改为安生", "异常提示:顾客置业顾问被多次更换");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            saveData(aCase, ciCaseName, caseName, "PC（有渠道）-顾客到场-更改置业顾问3次-创单（选择PC报备渠道）");
        }
    }

    //    -------------------------------更改姓名1次-----------------------------------------

    /**
     * 顾客到场-PC(无渠道)，置业顾问是张钧甯
     * 选无渠道
     */
    @Test
    public void _PCFAChngName() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerNameOLd = caseName + "-" + getNamePro() + "-old";
            String customerNameNew = caseName + "-" + getNamePro() + "-new";
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = -1;

            newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerNameOLd, "MALE");

            JSONArray list = customerList(customerPhone, "", "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerNameNew, customerPhone, zhangName, zhangPhone);

            updateReportTime_PCF(customerPhone, customerNameNew, noChannelReportTime);

//            刷证
            witnessUpload(genCardId(), customerNameNew);

            list = orderList(-1, "", 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerNameNew, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "INFO_CHANGE", customerNameOLd + "更改为" + customerNameNew, "顾客姓名被修改");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-PC（无渠道）-更改姓名-创单（选择无渠道）");

        }
    }

    /**
     * 顾客到场-PC（无渠道）-创单（选择无渠道），置业顾问是张钧甯
     * 选无渠道
     */
    @Test
    public void witnessFail() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-PC（无渠道）-创单（选择无渠道）";

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110018";
            String smsCode = "721183";
            String customerName = caseName + "-" + getNamePro();
            int channelId = -1;

            newCustomer(channelId, "", "", "", "", customerPhone, customerName, "MALE");

//            刷证
            witnessUploadFail(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String adviserName = "张钧甯";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "WITNESS_RESULT", "", "异常:⼈证比对照⽚未上传,请检查⽹络连接,请再次刷证");
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);

        }
    }

//    --------------------------------------------------------隐藏手机号报备------------------------------------------

    /**
     * H5报备-顾客到场-同一渠道隐藏手机号报备-创单（选择H5渠道），置业顾问无
     * 选H5
     */
    @Test
    public void c1Hide1Evident1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "渠道A报备全号-到场-渠道A报备隐藏手机号-刷证-创单（选择渠道A）";

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            customerReportH5(wudongStaffIdStr, customerName, "144****0014", "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "测试【勿动】-【勿动】1\n" +
                    "报备号码:144****0014", "异常提示:顾客手机号与报备⼿机号码部分匹配");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 渠道一H5报备-顾客到场-渠道一隐藏手机号报备-渠道二隐藏手机号报备-创单（选择H5渠道）置业顾问无
     * 选H5
     */
    @Test
    public void c2Hide2Evident1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "渠道A报备全号-到场-渠道A、B报备隐藏手机号-刷证-创单（选择渠道A）";

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            customerReportH5(lianjiaFreezeStaffIdStr, customerName, "144****0014", "MALE", lianjiaToken);
            customerReportH5(wudongStaffIdStr, customerName, "144****0014", "MALE", wudongToken);

//            更改报备时间
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家-【勿动】\n" +
                    "报备号码:144****0014", "异常提示:顾客手机号与报备⼿机号码部分匹配");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "测试【勿动】-【勿动】1\n" +
                    "报备号码:144****0014", "异常提示:顾客手机号与报备⼿机号码部分匹配");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void c2hide1Evident1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报备全号-渠道A报备隐藏手机号-刷证-创单（选择渠道A）";

        try {
            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            long repTime = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, repTime + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);
//
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于0h0min", "该顾客的风控规则为提前报备时间:0h0min");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家-【勿动】\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备⼿机号码部分匹配");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void c2hide1evident1_No() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报备全号-渠道B报备隐藏手机号-刷证-创单（选择无渠道）";

        try {

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            long repTime = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 4);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家-【勿动】\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备⼿机号码部分匹配");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void c2Hide2_No() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报隐藏手机号-渠道B报备隐藏手机号-刷证-创单（选择无渠道）";

        try {

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, "144****0000", "MALE", wudongToken);
            customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            long repTime = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 3);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "测试【勿动】-【勿动】1\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备⼿机号码部分匹配");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家-【勿动】\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备⼿机号码部分匹配");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void c2Hide2Comp2_No() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报备隐藏-渠道A补全-渠道B报备隐藏手机号-B补全-刷证-创单（选择无渠道）";

        try {

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, "144****0000", "MALE", wudongToken);

//            补全
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, "", "");

//            报备
            customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

//            补全

            list = customerList(customerName, lianjiaChannelStr, "", 1, 10).getJSONArray("list");

            cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, "", "");

            long repTime = System.currentTimeMillis();
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 4);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家-【勿动】", "异常提示:多个渠道报备同一顾客");
            checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

//    ------------------------------------------------------下载风控单验证-------------------------------------------------------

    //    @Test
    public void _H5A1NATURE() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            审核
            orderAudit(orderId, "NATURE");
//
////            生成报告
//            JSONObject reportCreateData = reportCreate(orderId);
//
////            验证报告
//            checkNormalOrderLink(orderId, reportCreateData);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-创单（选择H5渠道）-审核为自然访客-验证报告");
        }
    }

    //    @Test
    public void _H5A$CHANNEL() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            审核
            orderAudit(orderId, "CHANNEL");

//            生成报告
            JSONObject reportCreateData = reportCreate(orderId);

//            验证报告
            checkNormalOrderLink(orderId, reportCreateData);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "H5报备-顾客到场-创单（选择H5渠道）-审核为渠道访客-验证报告");
        }
    }

    //    @Test
    public void A_H5$NATURE() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            审核
            orderAudit(orderId, "CHANNEL");

//            生成报告
            JSONObject reportCreateData = reportCreate(orderId);

//            验证报告
            checkOrderRiskLinkNum(orderId, reportCreateData, 3);

            checkOrderRiskLinkMess(orderId, reportCreateData, "AUDIT_RESULT", "⼈工核验结果生成", "系统判定结果:风险,还存在3个异常环节\n" +
                    "⼈工核验结果:自然访客");
            checkOrderRiskLinkMess(orderId, reportCreateData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, reportCreateData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:报备晚于首次到访");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-H5报备-创单（选择H5报备渠道）-审核为自然访客-验证报告");
        }
    }

    //    @Test
    public void A_H5$CHANNEL() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

//            审核
            orderAudit(orderId, "CHANNEL");

//            生成报告
            JSONObject reportCreateData = reportCreate(orderId);

//            验证报告
            checkOrderRiskLinkNum(orderId, reportCreateData, 3);

            checkOrderRiskLinkMess(orderId, reportCreateData, "AUDIT_RESULT", "⼈工核验结果生成", "系统判定结果:风险,还存在3个异常环节\n" +
                    "⼈工核验结果:渠道访客");
            checkOrderRiskLinkMess(orderId, reportCreateData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            checkOrderRiskLinkMess(orderId, reportCreateData, "CHANNEL_REPORT", "测试【勿动】-【勿动】1", "异常提示:报备晚于首次到访");

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-H5报备-创单（选择H5报备渠道）-审核为渠道访客-验证报告");
        }
    }

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    /**
     * 同一业务员报备同一顾客两次（全号）
     */
    @Test
    public void dupReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "重复报备";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            String report2 = customerReportH5NoCheckCode(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            checkCode(report2, StatusCode.BAD_REQUEST, "重复报备");

            checkMessage("重复报备", report2, "报备失败！当前顾客信息已报备完成，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


//    --------------------------------------------规则页报备保护期验证-----------------------------------------------------------------------

    /**
     * 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void inProtectOthersFail() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内其他渠道报备";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110180";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            其他渠道报备

            String report2 = customerReportH5NoCheckCode(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            checkCode(report2, StatusCode.BAD_REQUEST, "保护期内其他渠道报备");

            checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(麦田)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 保护渠道报备 -> 顾客现场登记（PC无渠道）
     */
    @Test
    public void inProtectPCF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内顾客现场登记（PC无渠道）";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110180";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            现场注册
            newCustomer(-1, "", "", zhangName, zhangPhone, customerPhone, customerName, "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 保护渠道报备 -> 顾客现场登记（自助报备）
     */
    @Test
    public void inProtectSelf() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内顾客现场登记（自助报备）";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "18210113587";
            String selfCode = "805805";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            selfRegister(customerName, customerPhone, selfCode, "", "", "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道报备 -> 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void othersInProtectOthers() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "其他渠道报备 -> 保护渠道报备 -> 保护期内其他渠道报备";

        logger.info("\n\n" + caseName + "\n");


        try {
            // 报备
            String customerPhone = "14422110014";
            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            String report2 = customerReportH5NoCheckCode(lianjiaStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

            checkCode(report2, StatusCode.BAD_REQUEST, "");

            checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(麦田)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道报备 -> 保护渠道报备 -> PC(无渠道)
     */
    @Test
    public void othersInProtectPCF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "其他渠道报备 -> 保护渠道报备 -> 保护期内PC(无渠道)登记";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            newCustomer(-1, "", "", zhangName, zhangPhone, customerPhone, customerName, "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道报备 -> 保护渠道报备 -> 自助扫码
     */
    @Test
    public void othersInProtectSELF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "其他渠道报备 -> 保护渠道报备 -> 保护期内自助扫码";
        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            selfRegister(customerName, customerPhone, selfCode, "", "", "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersCompleteBefore() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneHide = "144****0014";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备（麦田）
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneHide, "MALE", wudongToken);

            updateReportTimeChannel(customerPhoneHide, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            补全手机号
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersCompleteIn() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneHide = "144****0014";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备（麦田）
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneHide, "MALE", wudongToken);

//            补全手机号
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersChangeBefore() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道修改手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhoneB = "13322110014";
            String customerPhone = "14422110014";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备（麦田）
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            报备原手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);
            updateReportTimeChannel(customerPhoneB, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            更改手机号
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersChangeIn() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道修改手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhoneB = "13322110014";
            String customerPhone = "14422110014";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备（麦田）
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            报备原手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

//            更改手机号
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void outProtectOthersSuccess() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "保护渠道报备 -> 保护期外其他渠道报备";

        try {
            String customerPhone = "14422110180";
            String customerName = "麦田【勿动】";

            String customerNameA = "麦田【勿动】" + "-" + getNamePro();

//            其他渠道报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void outProtectOthersCompleteBefore() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "14422110180";
            String customerPhoneHide = "144****0180";
            String customerName = "麦田【勿动】";

            String customerNameA = "麦田【勿动】" + "-" + getNamePro();

//            其他渠道报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            updateReportTimeChannel(customerPhone, customerPhoneHide, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void OutProtectOthersCompleteAfter() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "14422110180";
            String customerPhoneHide = "144****0180";
            String customerName = "麦田【勿动】";

            String customerNameA = "麦田【勿动】" + "-" + getNamePro();

//            其他渠道报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneHide, "MALE", wudongToken);

            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道修改手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void outProtectOthersChangeBefore() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护过期，其他渠道更改手机号为保护过期顾客信息";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneB = "13422110014";

            String customerName = "麦田【勿动】";

            String customerNameA = "麦田【勿动】" + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

            updateReportTimeChannel(customerPhoneB, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            更改手机号
            JSONArray list = customerList(customerPhoneB, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void OutProtectOthersChangeAfter() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneB = "13422110014";

            String customerName = "麦田【勿动】";

            String customerNameA = "麦田【勿动】" + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

//            更改手机号
            JSONArray list = customerList(customerPhoneB, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 保护渠道报备带*手机号
     */
    @Test
    public void protectHidePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备隐藏手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "144****0014";

            String customerName = caseName + "-" + getNamePro();

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", protect1DayRuleId);

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            customerReportH5(lianjiaStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", defaultRuleId);
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM")
    public void ruleAheadInvalidStr(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", number, "10");

            checkCode(addRiskRule, StatusCode.UNKNOWN_ERROR, "提前报备时间为【" + number + "】");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM_AHEAD")
    public void ruleAheadInvalidNumber(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", number, "10");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "提前报备时间为【" + number + "】");

            checkMessage("新建风控规则", addRiskRule, "提前报备时间不能超过半年");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NUM_AHEAD")
    public void ruleAheadValidNumber(int number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM")
    public void ruleProtectInvalidStr(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", "60", number);

            checkCode(addRiskRule, StatusCode.UNKNOWN_ERROR, "");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM_PROTECT")
    public void ruleProtectInvalidNum(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", "60", number);

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "");

            checkMessage("新建风控规则", addRiskRule, "报备保护期不能超过10000天");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NUM_PROTECT")
    public void ruleProtectValidNumber(int number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NAME")
    public void ruleNameInvalid(String name) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建风控规则名称为【" + name + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode(name, "60", "60");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "新建风控规则");

            if ("".equals(name.trim())) {
                checkMessage("新建风控规则", addRiskRule, "规则名称不可为空");
            } else if ("默认规则".equals(name)) {
                checkMessage("新建风控规则", addRiskRule, "规则名称不允许重复");
            } else {
                checkMessage("新建风控规则", addRiskRule, "规则名称最长为20个字符");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NAME")
    public void ruleNameValid(String name) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建风控规则名称为【" + name + "】";

        logger.info("\n\n" + caseName + "\n");

        try {
            addRiskRule(name, "60", "60");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_DELETE_RULE")
    public void romoveRuleInvalid(String id) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "非法删除规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = deleteRiskRuleNoCheckCode(id);

            checkCode(s, StatusCode.BAD_REQUEST, caseDesc);

            if (defaultRuleId.equals(id)) {
                checkMessage("新建风控规则", s, "只允许删除自定义规则");
            } else {
                checkMessage("新建风控规则", s, "规则已被渠道引用, 不可删除");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


    /**
     * 顾客到场-H5，无置业顾问
     * 选H5
     */
    @Test
    public void deleteUnusedRule() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "删除顾客引用后删除规则";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建规则
            String name = getNamePro();

            addRiskRule(name, "60", "");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

//            编辑渠道规则
            channelEdit(wudongChannelIdStr, "测试【勿动】", "于老师", genPhoneNum(), id);

//            报备
            String customerPhone = "18210113587";

            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            long repTime = System.currentTimeMillis() - 59 * 60 * 1000;
            updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            witnessUpload(genCardId(), customerName);

            list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "805805";

//            创单
            createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

            //引用后删除规则
            String s = deleteRiskRuleNoCheckCode(id);
            checkMessage("新建风控规则", s, "规则已被渠道引用, 不可删除");

//            删除引用
            channelEdit(wudongChannelIdStr, "测试【勿动】", "于老师", genPhoneNum(), defaultRuleId);

//            删除引用后删除规则
            deleteRiskRule(id);

//            校验
            String adviserName = "-";
            String channelName = "测试【勿动】";
            String channelStaffName = "【勿动】1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrder(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, repTime + "", orderLinkData, true, riskOrderType);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于1h0min", "该顾客的风控规则为提前报备时间:1h0min");
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

//    ----------------------------------------------新建顾客验证---------------------------------------------

    @Test(dataProvider = "NEW_CUSTOMER_BAD")
    public void newCustomerBad(int channelId, String channelStaffName, String channelStaffPhone, String adviserName,
                               String adviserPhone, String phone, String customerName, String gender) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = newCustomerNoCheckCode(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, phone, customerName, gender);
            checkCode(s, StatusCode.BAD_REQUEST, "");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void newCustomerNewStaff() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客-不存在的业务员和置业顾问";

        logger.info("\n\n" + caseName + "\n");

        try {

            String channelStaffName = getNamePro();
            String channelStaffPhone = genPhoneNum();

            String adviserName = getNamePro();
            String adviserPhone = genPhoneNum();

            String customerName = getNamePro();
            String customerPhone = genPhoneNum();

            newCustomer(lianjiaChannelInt, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONObject channelStaff = channelStaffList(channelStaffName, lianjiaChannelStr, 1, 10).getJSONArray("list").getJSONObject(0);

            String phoneRes = channelStaff.getString("phone");
            if (!channelStaffPhone.equals(phoneRes)) {
                throw new Exception("没有找到业务员【" + channelStaffName + "】");
            }

            JSONObject staff = staffList(adviserName, 1, 10).getJSONArray("list").getJSONObject(0);

            String adviserPhoneRes = staff.getString("phone");
            if (!adviserPhone.equals(adviserPhoneRes)) {
                throw new Exception("没有找到置业顾问【" + adviserName + "】");
            }

            String id = staff.getString("id");

            staffDelete(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

//    @Test
    public void newCustomerXML() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客-xml文件";

        logger.info("\n\n" + caseName + "\n");

        try {

            String dirPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\newCustomerFile";
            dirPath = dirPath.replace("\\", File.separator);
            File file = new File(dirPath);
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                String xmlPath = dirPath + File.separator + files[i].getName();

                importFile(xmlPath);

                checkCode(this.response, StatusCode.BAD_REQUEST, files[i].getName() + ">>>");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


    @Test
    public void getTxtFromPdf() {

        //加载测试文档
        String pdfPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\pdfFile\\1583230242073.pdf";
        pdfPath  = pdfPath.replace("\\",File.separator);
        PdfDocument pdf = new PdfDocument(pdfPath);

        //实例化StringBuilder类
        StringBuilder sb = new StringBuilder();
        //定义一个int型变量
        int index = 0;

        //遍历PDF文档中每页
        PdfPageBase page;
        for (int i= 0; i<pdf.getPages().getCount();i++) {
            page = pdf.getPages().get(i);
            //调用extractText()方法提取文本
            sb.append(page.extractText(true));
            FileWriter writer;
            try {
                //将StringBuilder对象中的文本写入到txt
                String txtPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\pdfFile\\1.txt";
                pdfPath  = pdfPath.replace("\\",File.separator);
                writer = new FileWriter(txtPath);
                writer.write(sb.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            //调用extractImages方法获取图片
//            for (BufferedImage image : page.extractImages()) {
//                //指定输出图片名，指定图片格式
//                File output = new File(String.format("Image_%d.png", index++));
//                ImageIO.write(image, "PNG", output);
//            }
        }
        pdf.close();

    }


//    ----------------------------------------数据验证方法--------------------------------------------------------------------


    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    private void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    private void checkDetail(String orderId, String customerName, String phone, String adviserName, String channelName,
                             String channelStaffName, String orderStatusTips, String faceUrl, String firstAppearTime,
                             String reportTime) throws Exception {

        JSONObject orderDetail = orderDetail(orderId);
        String function = "订单详情-";

        if (!"-".equals(customerName)) {

            checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "customer_name");
        }

        if (!"-".equals(phone)) {

            checkUtil.checkKeyValue(function, orderDetail, "phone", phone, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "phone");
        }

        if (!"-".equals(adviserName)) {

            checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "adviser_name");
        }

        if (!"-".equals(channelName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_name");
        }

        if (!"-".equals(channelStaffName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_staff_name");
        }

        if (!"-".equals(orderStatusTips)) {

            checkUtil.checkKeyValue(function, orderDetail, "order_status_tips", orderStatusTips, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "order_status_tips");
        }

        if (!"-".equals(faceUrl)) {
            checkUtil.checkKeyValue(function, orderDetail, "face_url", faceUrl, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "face_url");
        }

        if (!"-".equals(firstAppearTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "first_appear_time");
        }

        if (!"-".equals(reportTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "report_time");
        }

        checkUtil.checkKeyValue(function, orderDetail, "deal_time", "", false);
    }


    public void checkNormalOrderLink(String orderId, JSONObject data) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);
            Integer linkStatus = link.getInteger("link_status");
            String linkName = link.getString("link_name");
            if (linkStatus != 1) {
                throw new Exception("order_id=" + orderId + "，环节=" + linkName + "，应为正常环节，系统返回为异常!");
            }
        }
    }

    private void checkFirstVisitAndTrace(String orderId, JSONObject data, boolean expectExist) throws Exception {
        JSONArray linkLists = data.getJSONArray("list");

        boolean isExist = false;


        String functionPre = "orderId=" + orderId + "，";

        for (int i = 0; i < linkLists.size(); i++) {
            String function;
            JSONObject link = linkLists.getJSONObject(i);
            String linkKey = link.getString("link_key");
            String id = link.getString("id");

            function = functionPre + "环节id=" + id + "，";


            if ("FIRST_APPEAR".equals(linkKey) || "TRACE".equals(linkKey)) {
                isExist = true;

                checkUtil.checkNotNull(function, link, "link_point");
                checkUtil.checkNotNull(function, link.getJSONObject("link_note"), "face_url");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_name");

                Integer linkStatus = link.getInteger("link_status");
                String linkName = link.getString("link_name");
                if (linkStatus != 1) {
                    throw new Exception("order_id=" + orderId + "，环节=" + linkName + "，环节id=" + id + "，应为正常环节，系统返回为异常!");
                }
            }
        }

        if (isExist != expectExist) {
            throw new Exception("是否期待存在场内轨迹，期待：" + expectExist + ",实际：" + isExist);
        }

    }


    public void checkOrderRiskLinkNum(String orderId, JSONObject data, int num) throws Exception {

        JSONArray list = data.getJSONArray("list");

        int riskNum = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            int linkStatus = single.getInteger("link_status");
            if (linkStatus == 0) {
                riskNum++;
            }
        }

        if (riskNum != num) {
            throw new Exception("order_id=" + orderId + "，期待异常环节数=" + num + "，系统返回异常环节数=" + riskNum);
        }
    }

    public void checkOrderRiskLinkMess(String orderId, JSONObject data, String linkKey, String content, String linkPoint) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        boolean isExistLinkKey = false;
        boolean isExistLinkKeyContent = false;

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);

            String linkKeyRes = link.getString("link_key");
            if (linkKey.equals(linkKeyRes)) {
                isExistLinkKey = true;

                String contentRes = link.getJSONObject("link_note").getString("content");

                if ("".equals(content) || content.equals(contentRes)) {
                    isExistLinkKeyContent = true;

                    int linkStatus = link.getInteger("link_status");
                    if (linkStatus != 0) {
                        throw new Exception("order_id=" + orderId + "，环节=" + linkKey + "，应为异常环节，系统返回为正常！");
                    }

                    String linkPointRes = link.getString("link_point");

                    if (!linkPoint.equals(linkPointRes)) {
                        throw new Exception("order_id=" + orderId + "，环节=" + linkKey + "的异常提示应该=" + linkPoint + "，系统提示为【" + linkPointRes + "】");
                    }

                    break;
                }
            }
        }

        if (!isExistLinkKey) {
            throw new Exception("order_id=" + orderId + "，不存在环节=" + linkKey);
        }

        if (!isExistLinkKeyContent) {
            throw new Exception("order_id=" + orderId + "环节=" + linkKey + "，不存在留痕=" + content + "，的环节。");
        }
    }

    public void checkOrder(String orderId, String customerName, String phone, String adviserName, String channelName,
                           String channelStaffName, String orderStatusTips, String faceUrl, String firstAppearTime,
                           String reportTime, JSONObject orderLinkData, boolean expectExistTrace, String orderType) throws Exception {

//        订单详情
        checkDetail(orderId, customerName, phone, adviserName, channelName, channelStaffName, orderStatusTips,
                faceUrl, firstAppearTime, reportTime);

//        订单详情，列表，关键环节中信息一致性
        detailListLinkConsist(orderId, phone);

//        订单环节风险/正常

        if ("NORMAL".equals(orderType)) {
            checkNormalOrderLink(orderId, orderLinkData);
        }

//        场内轨迹
        checkFirstVisitAndTrace(orderId, orderLinkData, expectExistTrace);
    }

    public String getNamePro() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 5);
    }

    private void detailListLinkConsist(String orderId, String phone) throws Exception {

        JSONArray list = orderList(3, phone, 100).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderIdRes = single.getString("order_id");

            if (orderId.equals(orderIdRes)) {

                JSONObject orderDetail = orderDetail(orderId);
                String function = "订单详情>>>";

                String customerName = single.getString("customer_name");
                checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);

                String adviserName = single.getString("adviser_name");
                if (adviserName == null || "".equals(adviserName)) {
                    String adviserNameDetail = orderDetail.getString("adviser_name");
                    if (adviserNameDetail != null && !"".equals(adviserNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",adviser_name在订单列表中是空，在订单详情中=" + adviserNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
                }

                String channelName = single.getString("channel_name");
                if (channelName == null || "".equals(channelName)) {
                    String channelNameDetail = orderDetail.getString("channel_name");
                    if (channelNameDetail != null && !"".equals(channelNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_name在订单列表中是空，在订单详情中=" + channelNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
                }

                String channelStaffName = single.getString("channel_staff_name");
                if (channelStaffName == null || "".equals(channelStaffName)) {
                    String channelStaffNameDetail = orderDetail.getString("channel_staff_name");
                    if (channelStaffNameDetail != null && !"".equals(channelStaffNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_staff_name在订单列表中是空，在订单详情中=" + channelStaffNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
                }

                String firstAppearTime = single.getString("first_appear_time");
                if (firstAppearTime == null || "".equals(firstAppearTime)) {
                    String firstAppearTimeDetail = orderDetail.getString("first_appear_time");
                    if (firstAppearTimeDetail != null && !"".equals(firstAppearTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",first_appear_time在订单列表中是空，在订单详情中=" + firstAppearTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
                }

                String dealTime = single.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    String dealTimeDetail = orderDetail.getString("deal_time");
                    if (dealTimeDetail != null && !"".equals(dealTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",deal_time在订单列表中是空，在订单详情中=" + dealTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "deal_time", dealTime, true);
                }

                String reportTime = single.getString("report_time");
                if (reportTime == null || "".equals(reportTime)) {
                    String reportTimeDetail = orderDetail.getString("report_time");
                    if (reportTimeDetail != null && !"".equals(reportTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",report_time在订单列表中是空，在订单详情中=" + reportTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
                }

                String isAudited = single.getString("is_audited");
                if (isAudited == null || "".equals(isAudited)) {
                    String isAuditedDetail = orderDetail.getString("report_time");
                    if (isAuditedDetail != null && !"".equals(isAuditedDetail)) {
                        throw new Exception("orderId=" + orderId + ",is_audited在订单列表中是空，在订单详情中=" + isAuditedDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "is_audited", isAudited, true);
                }

                break;
            }
        }
    }

    public String genCardId() {
        Random random = new Random();
        long num = 100000000000000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
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
        }
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPostWithCheckCode(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        checkCode(response, StatusCode.SUCCESS, "");

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                throw new Exception(message + "，expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, "登录获取authentication");
    }

    @AfterClass
    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    public void updateReportTimeChannel(String phone, String customerName, int channelId, int staffId, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(channelId);
        reportTime.setChannelStaffId(staffId);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    public void updateReportTime_S(String phone, String customerName, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(0);
        reportTime.setChannelStaffId(0);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    public void updateReportTime_PCF(String phone, String customerName, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(-1);
        reportTime.setChannelStaffId(0);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    private Object getShopId() {
        return "4116";
    }

    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String CUSTOMER_INSERT = "/risk/customer/insert";
    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";
    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

//    ----------------------------------------------接口方法--------------------------------------------------------------------

    /**
     * 3.4 顾客列表
     */
    public JSONObject customerList(String phone, String channel, String adviser, int page, int pageSize) throws Exception {

        String json =
                "{\n";

        if (!"".equals(phone)) {
            json += "    \"phone_or_name\":\"" + phone + "\",";
        }

        if (!"".equals(channel)) {
            json += "    \"channel_id\":" + channel + ",";
        }

        if (!"".equals(adviser)) {
            json += "    \"adviser_id\":" + adviser + ",";
        }

        json += "    \"shop_id\":" + getShopId() + "," +
                "    \"page\":" + page + "," +
                "    \"page_size\":" + pageSize +
                "}";


        String res = httpPostWithCheckCode(CUSTOMER_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void newCustomer(int channelId, String channelStaffName, String channelStaffPhone, String adviserName, String adviserPhone, String phone, String customerName, String gender) throws Exception {

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        httpPostWithCheckCode(CUSTOMER_INSERT, json);
    }

    public String newCustomerNoCheckCode(int channelId, String channelStaffName, String channelStaffPhone, String adviserName,
                                         String adviserPhone, String phone, String customerName, String gender) {

        String res = "";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        try {
            res = httpPost(CUSTOMER_INSERT, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public JSONObject importFile(String imagePath) {
        String url = "http://dev.store.winsenseos.cn/risk/customer/file/import";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", getShopId()+"");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.getMessage();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
    }

    public JSONObject customerEditPC(String cid, String customerName, String phone, String adviserName, String adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        Thread.sleep(500);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.10 修改顾客信息
     */
    public String customerEditPCNoCheckCode(String cid, String customerName, String phone, String adviserName, String adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        Thread.sleep(500);

        return res;
    }

    public void customerEditH5(String cid, String customerName, String phone, String token) throws Exception {


        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"cid\":\"" + cid + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        httpPostWithCheckCode(CUSTOMER_INSERT, json);
    }

    public JSONObject channelStaffList(String namePhone, String channelId, int page, int size) throws Exception {
        String url = "/risk/channel/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"channel_id\":\"" + channelId + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject staffList(String namePhone, int page, int size) throws Exception {
        String url = "/risk/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject staffDelete(String id) throws Exception {
        String url = "/risk/staff/delete/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.4 创建订单
     */
    public JSONObject createOrder(String phone, String orderId, String faceUrl, int channelId, String smsCode) throws Exception {

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"order_id\":\"" + orderId + "\",";
        if (channelId != -1) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"sms_code\":\"" + smsCode + "\"" +
                "}";
        String res = httpPostWithCheckCode(ADD_ORDER, json);

        return JSON.parseObject(res);
    }

    /**
     * 4.5 订单详情
     */
    public JSONObject orderDetail(String orderId) throws Exception {
        String json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_DETAIL, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.6 订单关键步骤接口
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.8 订单列表
     */
    public JSONObject orderList(int status, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        json += "    \"size\":" + pageSize + "" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.15 订单审核
     */
    public JSONObject orderAudit(String orderId, String visitor) throws Exception {
        String url = "/risk/order/status/audit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"visitor\":\"" + visitor + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 15.2 生成风险单
     */
    public JSONObject reportCreate(String orderId) throws Exception {
        String url = "/risk/evidence/risk-report/create";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.15 渠道客户报备H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return response;
    }

    /**
     * 6.15 渠道客户报备H5
     */
    public String customerReportH5NoCheckCode(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
//        return JSON.parseObject(response);
    }

    /**
     * 9.6 自主注册
     */
    public JSONObject selfRegister(String customerName, String phone, String verifyCode, String adviserId, String hotPoints, String gender) throws Exception {
        String url = "/external/self-register/confirm";

        String json =
                "{\n" +
                        "    \"name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\",";
        if (!"".equals(adviserId)) {
            json += "    \"adviser_id\":\"" + adviserId + "\",";
        }
        if (!"".equals(hotPoints)) {
            json += "    \"hot_points\":[1],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String witnessUpload(String cardId, String personName) throws Exception {
        String router = "/risk-inner/witness/upload";
        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"person_name\":\"" + personName + "\"," +
                        "        \"capture_pic\":\"@1\"," +
                        "        \"is_pass\":true," +
                        "        \"card_pic\":\"@0\"," +
                        "        \"card_id\":\"" + cardId + "\"" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID() + "\"," +
                        "    \"resource\":[" +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1612575519&Signature=5nntV5uCcxSdhDul3HP4FcJeQDg%3D\"," +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1612575519&Signature=5nntV5uCcxSdhDul3HP4FcJeQDg%3D\"" +
                        "    ],\n" +
                        "    \"system\":{" +
                        "        \"app_id\":\"49998b971ea0\"," +
                        "        \"device_id\":\"6934268400763904\"," +
//                        "        \"device_id\":\"6798257327342592\"," + //shop 2606de
                        "        \"scope\":[" +
                        "            \"4116\"" +
                        "        ]," +
//                        "        \"scope\":[" +
//                        "            \"2606\"" +
//                        "        ]," +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\"," +
                        "        \"source\":\"DEVICE\"" +
                        "    }" +
                        "}";

        Thread.sleep(3000);

        return httpPostWithCheckCode(router, json);
    }

    public String witnessUploadFail(String cardId, String personName) throws Exception {
        String router = "/risk-inner/witness/upload";
        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"person_name\":\"" + personName + "\"," +
                        "        \"capture_pic\":\"@1\"," +
                        "        \"is_pass\":true," +
                        "        \"card_pic\":\"@0\"," +
                        "        \"card_id\":\"" + cardId + "\"" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID() + "\"," +
                        "    \"resource\":[" +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000962662/1c32c393-21c2-48b2-afeb-11c197436194?Expires=1580882241&OSSAccessKeyId=TMP.hj3MfDhaCX3aSbKjRM9Rx1WScRdTdWZN3cLj2fsLxnAkxXHTnRz9BXDebaX6qhG2x15xP2zULU6q3mRT7JgZ3aCbSs4RtyXfHAnXCZUAY6oRAaDx9iaE5eCeGmv2P5.tmp&Signature=LTQnJJ5jKkh45rImVZDCZzpotLI%3D\"," +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000962662/1c32c393-21c2-48b2-afeb-11c197436194?Expires=1580882241&OSSAccessKeyId=TMP.hj3MfDhaCX3aSbKjRM9Rx1WScRdTdWZN3cLj2fsLxnAkxXHTnRz9BXDebaX6qhG2x15xP2zULU6q3mRT7JgZ3aCbSs4RtyXfHAnXCZUAY6oRAaDx9iaE5eCeGmv2P5.tmp&Signature=LTQnJJ5jKkh45rImVZDCZzpotLI%3D\"" +
                        "    ],\n" +
                        "    \"system\":{" +
                        "        \"app_id\":\"49998b971ea0\"," +
                        "        \"device_id\":\"6934268400763904\"," +
                        "        \"scope\":[" +
                        "            \"4116\"" +
                        "        ]," +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\"," +
                        "        \"source\":\"DEVICE\"" +
                        "    }" +
                        "}";

        Thread.sleep(3000);

        return httpPostWithCheckCode(router, json);
    }

    /**
     * 16.1 新增风控规则
     */
    public String addRiskRuleNoCheckCode(String name, String aheadReportTime, String reportProtect) throws Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        return httpPost(url, json);
    }

    /**
     * 16.1 新增风控规则
     */
    public void addRiskRule(String name, String aheadReportTime, String reportProtect) throws Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    /**
     * 16.1 风控规则列表
     */
    public JSONObject riskRuleList() throws Exception {

        String url = "/risk/rule/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");


    }

    /**
     * 16.1 删除风控规则
     */
    public void deleteRiskRule(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public String deleteRiskRuleNoCheckCode(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String s = httpPost(url, json);
        return s;
    }

    /**
     * 编辑渠道
     */
    public void channelEdit(String channelId, String channelName, String owner, String phone, String ruleId) throws Exception {

        String url = "/risk/channel/edit/" + channelId;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public void channelEditFinally(String channelId, String channelName, String owner, String phone, String ruleId) {

        String url = "/risk/channel/edit/" + channelId;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"" +
                        "}";

        try {
            httpPostWithCheckCode(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 17.1 OCR二维码拉取-PC
     */
    public JSONObject getOcrQrcode() throws Exception {

        String url = "/risk/shop/ocr/qrcode";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * OCR二维码刷新-PC
     */
    public JSONObject refreshQrcode() throws Exception {

        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 17.3 OCR验证码确认-H5
     */
    public JSONObject confirmQrcode(String code) throws Exception {

        String url = "/external/ocr/code/confirm";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"code\":\"" + code + "\"" +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 17.3 OCR验证码确认-H5
     */
    public void ocrPicUpload(String token, String idCard, String face) throws Exception {

        String url = "/external/ocr/pic/upload";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":" + token + "," +
                        "    \"identity_card\":" + idCard + "," +//身份证图片base64
                        "    \"face\":\"" + face + "\"" +//人脸图片base64
                        "}";

        httpPostWithCheckCode(url, json);
    }

    //    @Test
    public void witnessUploadOcr() throws Exception {
        String code = refreshQrcode().getString("code");
        String token = confirmQrcode(code).getString("token");
        String idCardPath = "";
        String facePath = "";
        ImageUtil imageUtil = new ImageUtil();
        String imageBinary = imageUtil.getImageBinary(idCardPath);
        String faceBinary = imageUtil.getImageBinary(facePath);
        ocrPicUpload(token, imageBinary, faceBinary);

        Thread.sleep(3000);
    }

    public void witnessUploadOcrBadCode() throws Exception {
        String code = refreshQrcode().getString("code");
        String token = confirmQrcode(code).getString("token");
        String idCardPath = "";
        String facePath = "";
        ImageUtil imageUtil = new ImageUtil();
        String imageBinary = imageUtil.getImageBinary(idCardPath);
        String faceBinary = imageUtil.getImageBinary(facePath);
        ocrPicUpload(token, imageBinary, faceBinary);

        Thread.sleep(3000);
    }


    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
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
            dingPush("飞单日常 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + aCase.getFailReason().replace("java.lang.Exception: ", "异常："));
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }


    @DataProvider(name = "NORMAL")
    public Object[][] normalCase() {
        return new Object[][]{
//                caseName,ruleId,reportTime
                new Object[]{
                        "0min", defaultRuleId, firstAppearTime - 61 * 1000
                },
                new Object[]{
                        "60min", ahead1hRuleId, firstAppearTime - 61 * 60 * 1000
                },
                new Object[]{
                        "1day", ahead24hRuleId, firstAppearTime - (24 * 60 + 1) * 60 * 1000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, firstAppearTime - (7 * 24 * 60 + 1) * 60 * 1000
                },
                new Object[]{
                        "30day", ahead30dayRuleId, firstAppearTime - 2592060000L
                },
                new Object[]{
                        "aheadMax", aheadMaxRuleId, firstAppearTime - 15600060000L
                }
        };
    }

    @DataProvider(name = "RISK_1")
    public Object[][] riskCase1Channel() {
        return new Object[][]{
//                caseName,ruleId,aheadTime,reportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime
                },
                new Object[]{
                        "60min", ahead1hRuleId, "1h0min", firstAppearTime - 60 * 60 * 1000
                },
                new Object[]{
                        "60min", ahead1hRuleId, "1h0min", firstAppearTime + 61 * 60 * 1000
                },
                new Object[]{
                        "1day", ahead24hRuleId, "24h0min", firstAppearTime - (24 * 60) * 60 * 1000
                },
                new Object[]{
                        "1day", ahead24hRuleId, "24h0min", firstAppearTime + (24 * 60 + 1) * 60 * 1000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, "168h0min", firstAppearTime - (7 * 24 * 60) * 60 * 1000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, "168h0min", firstAppearTime + (7 * 24 * 60 + 1) * 60 * 1000
                },
                new Object[]{
                        "30day", ahead30dayRuleId, "720h0min", firstAppearTime - 2592000000L
                },
                new Object[]{
                        "30day", ahead30dayRuleId, "720h0min", firstAppearTime + 2592060000L
                },
                new Object[]{
                        "max", aheadMaxRuleId, "4333h20min", firstAppearTime - 15600000000L
                }
        };
    }

    @DataProvider(name = "RISK_1_1")
    public Object[][] riskCase1Channel1() {
        return new Object[][]{
//                caseName,ruleId,aheadTime,reportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime
                },
//                new Object[]{
//                        "60min", ahead1hRuleId, "1h0min",firstAppearTime - 60 * 60 * 1000
//                },
//                new Object[]{
//                        "60min", ahead1hRuleId,"1h0min", firstAppearTime + 61 * 60 * 1000
//                },
//                new Object[]{
//                        "1day", ahead24hRuleId, "24h0min",firstAppearTime - (24 * 60) * 60 * 1000
//                },
//                new Object[]{
//                        "1day", ahead24hRuleId, "24h0min",firstAppearTime + (24 * 60 + 1) * 60 * 1000
//                },
//                new Object[]{
//                        "7day", ahead7dayRuleId,"168h0min", firstAppearTime - (7 * 24 * 60) * 60 * 1000
//                },
//                new Object[]{
//                        "7day", ahead7dayRuleId, "168h0min",firstAppearTime + (7 * 24 * 60 + 1) * 60 * 1000
//                },
//                new Object[]{
//                        "30day", ahead30dayRuleId,"720h0min", firstAppearTime - 2592000000L
//                },
//                new Object[]{
//                        "30day", ahead30dayRuleId, "720h0min",firstAppearTime + 2592060000L
//                },
//                new Object[]{
//                        "max", aheadMaxRuleId, "4333h20min",firstAppearTime - 15600000000L
//                }
        };
    }

    @DataProvider(name = "RISK_2")
    public Object[][] riskCase2Channel() {
        return new Object[][]{
//                caseName,ruleId,AreportTime,BreportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime - 1 * 60000, firstAppearTime + 600 * 1000
                },
                new Object[]{
                        "60min", ahead1hRuleId, "1h0min", firstAppearTime - 61 * 60000, firstAppearTime - 60 * 60000
                },
                new Object[]{
                        "1day", ahead24hRuleId, "24h0min", firstAppearTime - (24 * 60 + 1) * 60000, firstAppearTime - (24 * 60) * 60000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, "168h0min", firstAppearTime - (7 * 24 * 60 + 1) * 60000, firstAppearTime - (7 * 24 * 60) * 60000
                },
                new Object[]{
                        "30day", ahead30dayRuleId, "720h0min", firstAppearTime - 2592060000L, firstAppearTime - 2592000000L
                },
                new Object[]{
                        "max", ahead1hRuleId, "4333h20min", firstAppearTime - 156000006000L, firstAppearTime - 156000006000L
                }
        };
    }

    @DataProvider(name = "RISK_2_2")
    public Object[][] riskCase2Channel2() {
        return new Object[][]{
//                caseName,ruleId,AreportTime,BreportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime - 1 * 60000, firstAppearTime + 600 * 1000
                }
        };
    }

    @DataProvider(name = "RULE_ID")
    public Object[] ruleId() {
        return new Object[]{
                "0min", "60min", "1day", "7day", "30day", "max"
        };
    }

    @DataProvider(name = "INVALID_NUM")
    public Object[] invalidNum() {
        return new Object[]{
                "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890",
                "[]@-+~！#$^&()={}|;:'\\\"<>.?/",
                "·！￥……（）——【】、；：”‘《》。？、,%*",
                "-1",
                "20.20",
        };
    }

    @DataProvider(name = "INVALID_NUM_AHEAD")
    public Object[] invalidNumAhead() {
        return new Object[]{
                "260001"

        };
    }

    @DataProvider(name = "INVALID_NUM_PROTECT")
    public Object[] invalidNumProtect() {
        return new Object[]{
                "10001"
        };
    }

    @DataProvider(name = "VALID_NUM_AHEAD")
    public Object[] validNumAhead() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10000
        };
    }

    @DataProvider(name = "VALID_NUM_PROTECT")
    public Object[] validNumProtect() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10001, 260000
        };
    }

    @DataProvider(name = "INVALID_NAME")
    public Object[] invalidName() {
        return new Object[]{
                "",
                "   ",
                "qwer@tyui&opas.dfgh#？",
                "qwer tyui opas dfg  h",
                "默认规则"
        };
    }

    @DataProvider(name = "VALID_NAME")
    public Object[] validName() {
        return new Object[]{
                "正常一点-飞单V3.0",
                "qwer@tyui&opas.dfgh？",
                "qwer tyui opas dfgh ",
                "qwer tyui opas dfg h",
                "      qwer1yuio2asdf3hjkl4        ",
                "·！￥……（）——【】、；：‘《》。？、",
        };
    }

    @DataProvider(name = "INVALID_DELETE_RULE")
    public Object[] invalidDeleteRule() {
        return new Object[]{
                defaultRuleId,
                protect1DayRuleId
        };
    }


    @DataProvider(name = "NEW_CUSTOMER_BAD")
    public Object[][] newCUstomerBad() {
        return new Object[][]{
//channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE"
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "123****0001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", ""
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, "", "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, "123****0000", "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, "name", zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, "", zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, "123****1111", zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, "", lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, "name", lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, "", "", zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, "name", zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        lianjiaChannelInt, "name", lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", "MALE"
                }
        };
    }
}

