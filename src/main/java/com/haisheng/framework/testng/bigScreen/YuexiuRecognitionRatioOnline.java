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
import com.haisheng.framework.model.bean.OnlineYuexiuCustomerSearch;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.HashMap;

public class YuexiuRecognitionRatioOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private Case aCase = new Case();

    DateTimeUtil dt = new DateTimeUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private HttpConfig config;

    private long SHOP_ID_ENV = 889;

    String URL_PREFIX = "http://123.57.114.205";

    private String loginPathOnline = "/yuexiu-login";
    private String jsonOnline = "{\"username\":\"yuexiu@yuexiu.com\",\"passwd\":\"f2c7219953b54583ea11065215f22a8b\"}";
    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlrp7pqozlrqREZW1vIiwidWlkIjoidWlkXzdmYzc4ZDI0IiwibG9naW5UaW1lIjoxNTcxNTM3OTYxMjU4fQ.lmIXi-cmw3VsuD6RZrPZDJw70TvWuozEtLqV6yFHXVY";

    @Test
    public void searchRatioToday() {

        JSONArray list = manageCustomerTypeList(SHOP_ID_ENV).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {

            searchRatio(list.getJSONObject(i).getString("customer_type"), "day");

        }
    }

    @Test
    public void searchRatioTillNow() {

        JSONArray list = manageCustomerTypeList(SHOP_ID_ENV).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            searchRatio(list.getJSONObject(i).getString("customer_type"), "tillNow");
        }
    }

    public void searchRatio(String customerType, String sample) {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        Case aCase = new Case();

        String desc = "当天人物图片搜索>>>";

        HashMap<String, String> wrongHm = new HashMap();
        HashMap<String, Integer> statisticsHm = new HashMap();
//        1、所有顾客数（total）2、code==1000的顾客数（code1000）3、搜索成功的顾客数（success）

        OnlineYuexiuCustomerSearch searchResult = new OnlineYuexiuCustomerSearch();

        JSONArray customerList;
        int total = 0;
        int code1005 = 0;
        int code1000 = 0;
        int success = 0;
        int searchDiff = 0;
        int searchNoFace = 0;

        float ratio1005 = 0.0f;
        float ratioSuccess = 0.0f;
        float ratioDiffFace = 0.0f;
        float ratioNoFace = 0.0f;

        try {
            customerList = manageCustomerList(customerType, "", "").getJSONArray("list");

            if (customerList != null && customerList.size() > 0) {

//            sample有两种：day和tillnow
                if ("day".equals(sample)) {
                    getWrongDay(customerList, wrongHm, statisticsHm);
                } else {
                    getWrongTillNow(customerList, wrongHm, statisticsHm);
                }

                logger.info("\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~错误数据~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                for (String value : wrongHm.values()) {
                    logger.info(value);
                }

                logger.info("\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~统计数据~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                total = statisticsHm.get("total");
                code1000 = statisticsHm.get("code1000");
                success = statisticsHm.get("success");
                searchDiff = statisticsHm.get("searchDiff");
                searchNoFace = statisticsHm.get("searchNoFace");
                code1005 = total - code1000;

                if (total > 0) {
                    ratio1005 = (float) code1005 / (float) total;
                }

                if (code1000 > 0) {
                    ratioSuccess = (float) success / (float) code1000;
                }

                if (code1000 > 0) {
                    ratioDiffFace = (float) searchDiff / (float) code1000;
                }

                if (code1000 > 0) {
                    ratioNoFace = (float) searchNoFace / (float) code1000;
                }
            }

            searchResult.setDate(LocalDate.now().toString());
            searchResult.setRole(customerType);
            searchResult.setEnv("online");
            searchResult.setUpdateTime(dt.currentDateToTimestamp());
            searchResult.setTotalNum(code1000);
            searchResult.setSuccessNum(success);
            searchResult.setFailNum(code1000 - success);
            searchResult.setFailIdDiffNum(searchDiff);
            searchResult.setFailNoResultNum(searchNoFace);
            searchResult.setPicQualityErrorNum(code1005);
            searchResult.setSuccessRate(ratioSuccess);
            searchResult.setPicQualityErrorRate(ratio1005);
            searchResult.setFailIdDiffRate(ratioDiffFace);
            searchResult.setFailNoResultRate(ratioNoFace);
            searchResult.setSample(sample);

            logger.info(customerType + "顾客总数：" + total);
            logger.info(customerType + "顾客1005数：" + code1005);
            logger.info(customerType + "顾客1000数：" + code1000);
            logger.info(customerType + "顾客搜索成功数：" + success);
            logger.info(customerType + "顾客搜索没有人脸数：" + searchNoFace);
            logger.info(customerType + "顾客搜索不同人脸数：" + searchDiff);

            logger.info(customerType + "顾客正确搜索率：" + ratioSuccess);
            logger.info(customerType + "顾客搜索出不同人脸率：" + ratioDiffFace);
            logger.info(customerType + "顾客没有人脸率：" + ratioNoFace);
            logger.info(customerType + "顾客1005率：" + ratio1005);
            aCase.setResult("PASS");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ("day".equals(sample)){
                saveCase(aCase, caseName + "-" + customerType, desc);
            }
            saveRatio(searchResult);
        }
    }

    int total = 0;
    int code1000 = 0;
    int success = 0;
    int searchDiff = 0;
    int searchNoFace = 0;

    public void getWrongDay(JSONArray customerList, HashMap hmWrong, HashMap hmStatistics) throws Exception {

        total = 0;
        code1000 = 0;
        success = 0;
        searchDiff = 0;
        searchNoFace = 0;

        for (int i = 0; i < customerList.size(); i++) {

            JSONObject single = customerList.getJSONObject(i);
            Long lastVisitTime = single.getLong("last_visit_time");

            DateTimeUtil dateTimeUtil = new DateTimeUtil();
            String lastVisitTimeStr = dateTimeUtil.timestampToDate("yyyy-MM-dd", lastVisitTime);

            String today = LocalDate.now().toString();

            if (today.equals(lastVisitTimeStr)) {
                getData(single, hmWrong);
            }
        }

        hmStatistics.put("total", total);
        hmStatistics.put("code1000", code1000);
        hmStatistics.put("success", success);
        hmStatistics.put("searchDiff", searchDiff);
        hmStatistics.put("searchNoFace", searchNoFace);
    }

    public void getWrongTillNow(JSONArray customerList, HashMap hmWrong, HashMap hmStatistics) throws Exception {

        total = 0;
        code1000 = 0;
        success = 0;
        searchDiff = 0;
        searchNoFace = 0;

        for (int i = 0; i < customerList.size(); i++) {

            JSONObject single = customerList.getJSONObject(i);
            getData(single, hmWrong);
        }

        hmStatistics.put("total", total);
        hmStatistics.put("code1000", code1000);
        hmStatistics.put("success", success);
        hmStatistics.put("searchDiff", searchDiff);
        hmStatistics.put("searchNoFace", searchNoFace);
    }


    public void getData(JSONObject single, HashMap hmWrong) throws Exception {

//                如果是当天到来的顾客，total+1；
        total++;

        String customerId = single.getString("customer_id");

        String showUrl = single.getString("show_url");

        String layoutPic = uploadLayoutPic(showUrl);

        String searchResult = manageCustomerFaceList(layoutPic);
        JSONObject resJo = JSON.parseObject(searchResult);

        int code = resJo.getInteger("code");

        if (code == 1000) {
//                    code==1000时， code1000+1
            code1000++;

            JSONArray faceList = resJo.getJSONObject("data").getJSONArray("list");

            if (faceList == null || faceList.size() != 1) {
                searchNoFace++;
                hmWrong.put(customerId, "customer_id:" + customerId + ",搜索结果为空，show_url:" + showUrl);
            } else {
                JSONObject customerData = faceList.getJSONObject(0);

                String customerIdFind = customerData.getString("customer_id");
                if (!customerId.equals(customerIdFind)) {
                    searchDiff++;
                    String showUrlFind = customerData.getString("show_url");
                    hmWrong.put(customerId, "customer_id:" + customerId + ",搜索结果错误，原show_url：" + showUrl + ",搜索结果中的customer_id为【"
                            + customerIdFind + "】,show_url【" + showUrlFind + "】");
                } else {
//                            搜索结果正确时，成功数+1
                    success++;
                }
            }
        } else if (code == 1005) {
            logger.info("\n\n===============================================================================================");
            logger.info("customer_id【" + customerId + "】" + "人脸质量不合格，show_url【" + showUrl + "】");
            logger.info("===============================================================================================\n\n");
        } else {
            throw new Exception("code不是1000或1005，是【" + code + "】");
        }
    }

    public String manageCustomerFaceList(String faceUrl) throws Exception {
        String path = "/yuexiu/manage/customer/face/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                        "    \"face_url\":\"" + faceUrl + "\"\n" +
                        "}\n";

        String resStr = httpPost(path, json);

        return resStr;
    }

    public String uploadLayoutPic(String picA) {

        String downloadImagePath = "src/main/java/com/haisheng/framework/testng/bigScreen/faceUrl.png";
        FileUtil fileUtil = new FileUtil();
        fileUtil.downloadImage(picA, downloadImagePath);

        JSONObject data = uploadPicture(downloadImagePath);
        String fileUrl = data.getString("pic_url");

        return fileUrl;
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

    public JSONObject manageCustomerList(String customerType, String gender, String ageGroupId) throws Exception {
        String path = "/yuexiu/manage/customer/list";

        String json = getCustomerListParamJson(customerType, gender, ageGroupId);

        String resStr = httpPost(path, json);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

    public JSONObject manageCustomerTypeList(long shopId) {
        String path = "/yuexiu/manage/customer/type/list";

        String json = "{\"shop_id\":" + shopId + "}";

        String resStr = httpPost(path, json);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        return data;
    }

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
                "    \"size\":10000\n" +
                "}";

        return json;
    }

    private void saveCase(Case aCase, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
    }

    private void saveRatio(OnlineYuexiuCustomerSearch searchResult) {
        qaDbUtil.saveYuexiuOnlineCustomerSearch(searchResult);
    }

    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_ONLINE_SERVICE;

        String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-online-test/buildWithParameters?case_name=";

        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000|| 1005");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private String httpPost(String path, String json) {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);

        try {
            response = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            return response;
        }

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
        }
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
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

    private String getIpPort() {

        return URL_PREFIX;
    }

    @BeforeSuite
    public void login() {

        String json = this.jsonOnline;
        String path = this.loginPathOnline;
        qaDbUtil.openConnection();

        initHttpConfig();
        String loginUrl = getIpPort() + path;

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
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }


    @DataProvider(name = "SEARCH_TYPE_TODAY")
    public Object[] todayType() {

        return new Object[]{
                "NEW", "HIGH_ACTIVE", "SIGNED"

        };
    }

    @DataProvider(name = "SEARCH_TYPE_ANYTIME")
    public Object[] anytimeType() {

        return new Object[]{
                "NEW", "HIGH_ACTIVE", "SIGNED", "LOW_ACTIVE", "LOST"

        };
    }
}
