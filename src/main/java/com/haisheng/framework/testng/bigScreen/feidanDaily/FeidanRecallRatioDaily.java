package com.haisheng.framework.testng.bigScreen.feidanDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.FeidanPicSearch;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.*;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanRecallRatioDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String response = "";

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public HttpConfig config;

    public String SHOP_ID = "4116";

    public String[] customerNames = {"_SA-896c2","黄鑫"};

    /**
     * 飞单版本
     **/
    /**
     * daily or online, default is daily
     **/

    /**
     * 样本总数
     **/
    public int totalNum = customerNames.length;

    /**
     * 订单所有图片样本成功搜索到用户的数量
     **/
    public int sampleSuccessNumAll = customerNames.length;

    /**
     * 订单随机一张图片样本成功搜索到用户的数量
     **/
    public int sampleSuccessNumOne = 0;

    /**
     * 订单所有图片样本搜索无结果的数量
     **/
    public int sampleFailNoResultNumAll = customerNames.length;

    /**
     * 订单随机一张图片样本搜索无结果的数量
     **/
    public int sampleFailNoResultNumOne = 0;

    /**
     * 订单所有图片样本图片质量不合格的数量
     **/
    public int samplePicQualityErrorNumAll = customerNames.length;
    /**
     * 订单随机一张图片样本图片质量不合格的数量
     **/
    public int samplePicQualityErrorNumOne = 0;

    /**
     * 订单所有图片样本的召回率 = (totalNum - sampleFailNoResultNumAll) / totalNum
     */
    public float sampleRecallRateAll;

    /**
     * 订单随机一张图片样本的召回率 = (totalNum - sampleFailNoResultNumOne) / totalNum
     */
    public float sampleRecallRateOne;

    /**
     * 订单所有图片样本，图片质量不合格的占比 = samplePicQualityErrorNumAll / totalNum
     */
    public float samplePicQualityErrorRateAll;

    /**
     * 订单随机一张图片样本，图片质量不合格的占比 = samplePicQualityErrorNumOne / totalNum
     */
    public float samplePicQualityErrorRateOne;


    /**
     * 订单所有图片样本的准确率 = sampleSuccessNumAll / (totalNum - sampleFailNoResultNumAll)
     */
    public float samplePrecisionRateAll;

    /**
     * 订单随机一张图片样本的准确率 = sampleSuccessNumOne / (totalNum - sampleFailNoResultNumOne)
     */
    public float samplePrecisionRateOne;

    /**
     * 订单所有图片样本的正确率 = 订单所有图片样本的召回率 * 订单所有图片样本的准确率
     */
    public float sampleAccuracyRateAll;

    /**
     * 订单随机一张图片样本的正确率 = 订单随机一张图片样本的召回率 * 订单随机一张图片样本的准确率
     */
    public float sampleAccuracyRateOne;

    @Test
    public void testSavePicSearch() throws Exception {
        FeidanPicSearch feidanPicSearch = new FeidanPicSearch();
        feidanPicSearch.setDate(dateTimeUtil.timestampToDate("yyyy-MM-dd",System.currentTimeMillis()));
        feidanPicSearch.setEnv("daily");
        feidanPicSearch.setVersion("V3.0");

        calOne();
        calAll();

        feidanPicSearch.setTotalNum(totalNum);

        feidanPicSearch.setSampleFailNoResultNumAll(sampleFailNoResultNumAll);
        feidanPicSearch.setSampleFailNoResultNumOne(sampleFailNoResultNumOne);
        feidanPicSearch.setSamplePicQualityErrorNumAll(samplePicQualityErrorNumAll);
        feidanPicSearch.setSamplePicQualityErrorNumOne(samplePicQualityErrorNumOne);
        feidanPicSearch.setSampleSuccessNumAll(sampleSuccessNumAll);
        feidanPicSearch.setSampleSuccessNumOne(sampleSuccessNumOne);

        sampleRecallRateAll = (float)(totalNum - sampleFailNoResultNumAll) / (float)totalNum;
        sampleRecallRateOne = (float)(totalNum - sampleFailNoResultNumOne) / (float)totalNum;
        samplePicQualityErrorRateAll = (float)samplePicQualityErrorNumAll / (float)totalNum;
        samplePicQualityErrorRateOne = (float)samplePicQualityErrorNumOne / (float)totalNum;
        samplePrecisionRateAll = (float)sampleSuccessNumAll / (float)(totalNum - sampleFailNoResultNumAll);
        samplePrecisionRateOne = (float)sampleSuccessNumOne / (float)(totalNum - sampleFailNoResultNumOne);
        sampleAccuracyRateAll = sampleRecallRateAll * samplePrecisionRateAll;
        sampleAccuracyRateOne = sampleRecallRateOne * samplePrecisionRateOne;

        System.out.println(sampleRecallRateAll);
        System.out.println(sampleRecallRateOne);
        System.out.println(samplePicQualityErrorRateAll);
        System.out.println(samplePicQualityErrorRateOne);
        System.out.println(samplePrecisionRateAll);
        System.out.println(samplePrecisionRateOne);
        System.out.println(sampleAccuracyRateAll);
        System.out.println(sampleAccuracyRateOne);

        feidanPicSearch.setSampleRecallRateAll(sampleRecallRateAll);
        feidanPicSearch.setSampleRecallRateOne(sampleRecallRateOne);
        feidanPicSearch.setSamplePicQualityErrorRateAll(samplePicQualityErrorRateAll);
        feidanPicSearch.setSamplePicQualityErrorRateOne(samplePicQualityErrorRateOne);
        feidanPicSearch.setSamplePrecisionRateAll(samplePrecisionRateAll);
        feidanPicSearch.setSamplePrecisionRateOne(samplePrecisionRateOne);
        feidanPicSearch.setSampleAccuracyRateAll(sampleAccuracyRateAll);
        feidanPicSearch.setSampleAccuracyRateOne(sampleAccuracyRateOne);

        qaDbUtil.saveFeidanPicSearch(feidanPicSearch);
    }


    public void calOne() throws Exception {

        for (int i = 0; i < customerNames.length; i++) {
            String customerName = customerNames[i];
            JSONObject jsonObject = orderList(customerNames[i], 10);
            JSONArray list = jsonObject.getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("风控列表中不存在name=" + customerName + "的顾客！");
            } else if (list.size() > 1) {
                throw new Exception("风控列表中存在" + list.size() + "个name=" + customerName + "的顾客！,期待只有1个");
            }

            JSONObject single = list.getJSONObject(0);
            String orderId = single.getString("order_id");

            JSONArray links = orderLinkList(orderId).getJSONArray("list");

            ArrayList<String> appearLists = new ArrayList();

            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                String linkKey = link.getString("link_key");
                if ("TRACE".equals(linkKey) || "FIRST_APPEAR".equals(linkKey)) {
                    String faceUrl = link.getJSONObject("link_note").getString("face_url");
                    appearLists.add(faceUrl);
                }
            }

            ArrayList<String> searchLists = new ArrayList();
            boolean qualityError = false;
            boolean isNoResult = false;
            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                String linkKeyRes = link.getString("link_key");
                if ("WITNESS_RESULT".equals(linkKeyRes)) {
                    String faceUrl = link.getJSONObject("link_note").getString("face_url");
                    JSONObject faceTraces = faceTraces(faceUrl);
                    int code = faceTraces.getInteger("code");
                    if (code == 1000) {
                        JSONArray searchList = faceTraces.getJSONObject("data").getJSONArray("list");

                        if (searchList == null || searchList.size() == 0) {
                            isNoResult = true;
                        }
                        if (searchList != null && searchList.size() > 0) {
                            for (int k = 0; k < searchList.size(); k++) {
                                searchLists.add(searchList.getJSONObject(k).getString("face_url"));
                            }
                        }
                    } else {
                        qualityError = true;
                        isNoResult = true;
                    }

                    break;
                }
            }

            if (qualityError) {
                samplePicQualityErrorNumOne++;
            }

            if (isNoResult) {
                sampleFailNoResultNumOne++;
            }

            if (searchLists.containsAll(appearLists)) {
                sampleSuccessNumOne++;
            }
        }
    }

    public void calAll() throws Exception {

        for (int i = 0; i < customerNames.length; i++) {
            String customerName = customerNames[i];
            JSONArray list = orderList(customerNames[i], 10).getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("风控列表中不存在name=" + customerName + "的顾客！");
            } else if (list.size() > 1) {
                throw new Exception("风控列表中存在" + list.size() + "个name=" + customerName + "的顾客！,期待只有1个");
            }

            JSONObject single = list.getJSONObject(0);
            String orderId = single.getString("order_id");

            JSONArray links = orderLinkList(orderId).getJSONArray("list");

            ArrayList<String> appearLists = new ArrayList();

            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                String linkKey = link.getString("link_key");
                if ("TRACE".equals(linkKey) || "FIRST_APPEAR".equals(linkKey)) {
                    String faceUrl = link.getJSONObject("link_note").getString("face_url");
                    appearLists.add(faceUrl);
                }
            }

            ArrayList<String> searchLists = new ArrayList();

            boolean isAll = true;
            boolean isAllFlag = true;

            boolean isQuality = false;

            boolean noResult = true;

            for (int j = 0; j < appearLists.size(); j++) {

                String faceUrl = appearLists.get(j);

                JSONObject faceTraces = faceTraces(faceUrl);
                int code = faceTraces.getInteger("code");
                if (code == 1000) {
                    isQuality = true;
                    JSONArray searchList = faceTraces.getJSONObject("data").getJSONArray("list");
                    if (searchList != null && searchList.size() > 0) {
                        noResult = false;
                        if (isAllFlag) {
                            for (int k = 0; k < searchList.size(); k++) {
                                searchLists.add(searchList.getJSONObject(k).getString("face_url"));
                            }
                            if (!searchLists.containsAll(appearLists)) {
                                isAll = false;
                                isAllFlag = false;
                            }
                        }
                    }
                }

            }

            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                String linkKey = link.getString("link_key");
                if ("TRACE".equals(linkKey) || "FIRST_APPEAR".equals(linkKey)) {

                }
            }

            if (isQuality) {
                samplePicQualityErrorNumAll--;
            }

            if (!noResult) {
                sampleFailNoResultNumAll--;
            }

            if (!isAll) {
                sampleSuccessNumAll--;
            }
        }
    }

    public JSONObject orderList(String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + SHOP_ID + "," +
                        "    \"customer_name\":\"" + namePhone + "\"," +
                        "    \"page\":1" + "," +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPost(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";

        String res = httpPost(url, json);

        return JSON.parseObject(res);
    }

    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPost(url, json);

        checkCode(res, StatusCode.SUCCESS, "");

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);

        response = HttpClientUtil.post(config);

        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
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

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            return;
        }
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", SHOP_ID)
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    public String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

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
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

    }
}

