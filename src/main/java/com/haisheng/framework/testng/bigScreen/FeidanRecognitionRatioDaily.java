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
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.jsoup.select.Evaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanRecognitionRatioDaily {

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

    private String SHOP_ID = "4116";


    //    @Test
    public void calRecallRatioOneDetail() throws Exception {

        String customers[] = {};

        for (int i = 0; i < customers.length; i++) {
            String customerName = customers[i];
            JSONArray list = orderList(customers[i], 10).getJSONArray("list");
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
            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                if ("WITNESS_RESULT".equals(link.getString("link_key"))) {
                    String faceUrl = link.getJSONObject("link_note").getString("face_url");

                    JSONArray searchList = faceTraces(faceUrl).getJSONArray("list");
                    if (searchList == null || searchList.size() == 0) {
                        throw new Exception("customer_name=" + customerName + "的搜索列表为空!");
                    } else {
                        for (int k = 0; k < searchList.size(); k++) {
                            searchLists.add(searchList.getJSONObject(k).getString("face_url"));
                        }
                    }
                }
            }

            if (searchLists.size() < appearLists.size()) {
                throw new Exception("customer_name=" + customerName + "的搜索结果中图片数量=" + searchLists.size() +
                        "小于风险关键环节列表中的到访记录图片数量=" + appearLists.size());
            } else {
                if (!searchLists.containsAll(appearLists)) {
                    throw new Exception("customer_name=" + customerName + "的搜索结果没有包含所有的到访记录！");
                }
            }
        }
    }


    @Test
    public void calRecallRatioOne() throws Exception {

        String customers[] = {};

        int successNum = 0;
        int totalNum = customers.length;

        for (int i = 0; i < customers.length; i++) {
            String customerName = customers[i];
            JSONArray list = orderList(customers[i], 10).getJSONArray("list");
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
            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                if ("WITNESS_RESULT".equals(link.getString("link_key"))) {
                    String faceUrl = link.getJSONObject("link_note").getString("face_url");

                    JSONArray searchList = faceTraces(faceUrl).getJSONArray("list");
                    if (searchList != null && searchList.size() > 0) {
                        for (int k = 0; k < searchList.size(); k++) {
                            searchLists.add(searchList.getJSONObject(k).getString("face_url"));
                        }
                    }
                }
            }

            if (searchLists.containsAll(appearLists)) {
                successNum++;
            }

            float successRatio = (float) successNum / (float) totalNum;
        }
    }

    @Test
    public void calRecallRatioAll() throws Exception {

        String customers[] = {};

        int successNum = 0;
        int totalNum = customers.length;

        for (int i = 0; i < customers.length; i++) {
            String customerName = customers[i];
            JSONArray list = orderList(customers[i], 10).getJSONArray("list");
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
            for (int j = 0; j < links.size(); j++) {
                JSONObject link = links.getJSONObject(j);
                String linkKey = link.getString("link_key");
                if ("TRACE".equals(linkKey) || "FIRST_APPEAR".equals(linkKey)) {
                    String faceUrl = link.getJSONObject("link_note").getString("face_url");

                    JSONArray searchList = faceTraces(faceUrl).getJSONArray("list");
                    if (searchList != null && searchList.size() > 0) {
                        for (int k = 0; k < searchList.size(); k++) {
                            searchLists.add(searchList.getJSONObject(k).getString("face_url"));
                        }
                    }

                    if (!searchLists.containsAll(appearLists)) {
                        isAll = false;
                    }
                }
            }

            if (isAll) {
                successNum++;
            }

            float successRatio = (float) successNum / (float) totalNum;
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
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
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

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    private String httpPostWithCheckCode(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);

        response = HttpClientUtil.post(config);

        return response;
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
                .other("shop_id", SHOP_ID)
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }
}

