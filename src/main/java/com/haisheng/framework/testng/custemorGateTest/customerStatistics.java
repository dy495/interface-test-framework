package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * 线下消费者，人物信息查询和当日统计查询部分接口的功能验证
 *
 * @author Shine
 */
public class customerStatistics {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
//    private static String UID = "uid_e0d1ebec";
//    private static String APP_ID = "a4d4d18741a8";
//    private static String SHOP_ID = "134";
//    String AK = "e0709358d368ee13";
//    String SK = "ef4e751487888f4a7d5331e8119172a3";

//    private static String UID = "uid_87803c0c";
//    private static String APP_ID = "232a40e4d37c";
//    private static String SHOP_ID = "2035";
//    String AK = "759195b1bc378c24";
//    String SK = "df11694093dd9dde9b0bc15e304d3b1b";


    private static String UID = "uid_7fc78d24";
    private static String APP_ID = "097332a388c2";
    private static String SHOP_ID = "8";
    String AK = "77327ffc83b27f6d";
    String SK = "7624d1e6e190fbc381d0e9e18f03ab81";

    private int ENTER_PV = 67;
    private int ENTER_UV = 65;
    private int LEAVE_PV = 111;
    private int MALE = 30;
    private int FEMALE = 3;


    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID_DB = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
//    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/customerApiRgn/buildWithParameters?videoSample=" +
//            VIDEO_NAME + "&is_checklist_run=false" + "&isPushMsg=" + IS_PUSH_MSG + "&isSaveToDb=" + IS_SAVE_TO_DB + "&case_name=";

    private String CI_CMD = "";

    private ApiResponse apiResponse = null;


    //    实时人物列表
    public JSONObject currentCustomerHistory(Case aCase, int step) throws Exception {
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        JSONObject responseJo;
        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"\n" +
                        "}\n";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse, router, expectCode);

            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, aCase, step);
        return responseJo;
    }

    //    历史人物列表
    public JSONObject customerHistory(Case aCase, int step) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};

        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        String startTime = String.valueOf(dateTimeUtil.initDateByDay());
        String endTime = String.valueOf(dateTimeUtil.initDateByDay() + 1000 * 60 * 60 * 24);

        JSONObject responseJo;
        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"," +
                        "        \"time_type\":\"" + "DAY" + "\"," +
                        "        \"start_time\":\"" + startTime + "\"," +
                        "        \"end_time\":\"" + endTime + "\"" +
                        "}";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse, router, expectCode);

            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, aCase, step);
        return responseJo;
    }

    //    单个人物详情查询
    public JSONObject singleCustomer(String customerId, String groupName, boolean isSpecial, Case aCase, int step) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        String startTime = String.valueOf(dateTimeUtil.initDateByDay());
        String endTime = String.valueOf(Long.valueOf(startTime) + 1000 * 60 * 60 * 24);
        JSONObject responseJo;

        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"," +
                        "        \"customer_id\":\"" + customerId + "\"," +
                        "        \"start_time\":\"" + startTime + "\"," +
                        "        \"end_time\":\"" + endTime + "\"" +
                        "}";

        if (isSpecial) {
            json =
                    "{\n" +
                            "        \"shop_id\":\"" + SHOP_ID + "\"," +
                            "        \"customer_id\":\"" + customerId + "\"," +
                            "        \"group_name\":\"" + groupName + "\"," +
                            "        \"start_time\":\"" + startTime + "\"," +
                            "        \"end_time\":\"" + endTime + "\"" +
                            "}";
        }

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse, router, expectCode);
            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, aCase, step);
        return responseJo;
    }

    //    历史统计查询
    public JSONObject customerStatistics(long startTime, long endTime, String type, Case aCase, int step) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        JSONObject responseJo;

        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"," +
                        "        \"time_type\":\"" + type + "\"," +
                        "        \"start_time\":\"" + startTime + "\"," +
                        "        \"end_time\":\"" + endTime + "\"" +
                        "}";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse, router, expectCode);
            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, aCase, step);
        return responseJo;
    }

    //--------------------------------当日统计查询-------------------------------
    public JSONObject currentCustomerStatistics(Case aCase, int step) throws Exception {
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        JSONObject responseJo;

        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"" +
                        "}";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse, router, expectCode);
            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }

        sendResAndReqIdToDbApi(apiResponse, aCase, step);
        return responseJo;
    }

    //    --------------------------测试实时人物列表-----------------------------------
//    @Test
    public void testCurrentCustomerHistory() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "计算实时人物列表的uv识别率";
        failReason = "";
        int step = 0;

        try {

            aCase.setRequestData("查询实时人物列表，然后取出列表中的人数");

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");
            JSONObject responseJo = currentCustomerHistory(aCase, step);

            getCurrentCustomerHisRate(responseJo, ENTER_UV, "实时人物列表", aCase);

            String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\custemorGateTest\\filePathForCheckUrl.png";

            checkPicUrl(responseJo, filePath);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //------------------------------验证实时人物列表response的数据结构------------------------------
//    @Test
    public void testCurrentCustomerHistoryDs() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "计算实时人物列表的uv识别率";
        failReason = "";
        int step = 0;

        try {

            aCase.setRequestData("验证实时人物列表response的数据结构");
            aCase.setExpect("rsponse中的数据结构与文档中的一致");

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");
            JSONObject responseJo = currentCustomerHistory(aCase, step);

            checkCurrentCustomerHistoryDs(responseJo);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ----------------------------------测试历史人物列表----------------------------------------
//    @Test
    public void testCustomerHistory() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "计算历史人物列表的uv识别率";
        failReason = "";

        int step = 0;

        try {

            Thread.sleep(10 * 60 * 1000);

            aCase.setRequestData("查询历史人物列表，然后取出列表中的人数");

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");

            JSONObject responseJo = customerHistory(aCase, step);
            getCurrentCustomerHisRate(responseJo, ENTER_UV, "历史人物列表", aCase);

            String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\custemorGateTest\\filePathForCheckUrl.png";

            checkPicUrl(responseJo, filePath);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    -------------------------------------测试单个人物详细信息----------------------------------------------
//    @Test
    public void testSingleCustomer() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "验证实时人物列表中的人物是否单个人物详细信息中都能查到";
        failReason = "";

        int step = 0;

        try {

            aCase.setRequestData("1、实时人物列表；" + "\n" + "2-n、用1中返回的customerId查询单个人物详细信息");
            aCase.setExpect("实时列表中的人物都能在单个人物详细信息中查到。");

            boolean isExist = false;
            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");
            JSONObject customerHistoryRes = currentCustomerHistory(aCase, step);
            JSONArray personArr = customerHistoryRes.getJSONObject("data").getJSONArray("person");

            if (personArr == null || personArr.size() == 0) {
                throw new Exception("实时人物列表为空！");
            }

            PersonProp perPropByCurrCustomerHis = new PersonProp();

            for (int i = 0; i < personArr.size(); i++) {
                isExist = true;

                JSONObject singlePerson = personArr.getJSONObject(i);

                String customerId,customerType;
                boolean isMale;

                if (singlePerson.containsKey("customer_id")) {
                    customerId = singlePerson.getString("customer_id");
                } else {
                    throw new Exception("实时任务列表--不存在customer_id字段");
                }

                if (singlePerson.containsKey("is_male")) {
                    isMale = singlePerson.getBoolean("is_male");
                } else {
                    throw new Exception("实时任务列表--不存在is_male字段");
                }

                if (singlePerson.containsKey("customer_type")) {
                    customerType = singlePerson.getString("customer_type");
                } else {
                    throw new Exception("实时任务列表--不存在customer_type字段");
                }

                String groupName = "";
                if ("SPECIAL".equals(customerType)) {
                    if (singlePerson.containsKey("group_name")) {
                        groupName = singlePerson.getString("group_name");
                    } else {
                        throw new Exception("实时任务列表--customer_type为special，但不存在group_name字段");
                    }

                }

                perPropByCurrCustomerHis.setCustomerId(customerId);
                perPropByCurrCustomerHis.setCustomerType(customerType);
                perPropByCurrCustomerHis.setMale(isMale);
                perPropByCurrCustomerHis.setGroupName(groupName);

                logger.info("--------------------------(" + (++step) + ")");
                logger.info("\n\n");

                boolean isSpecial = false;
                if ("SPECIAL".equals(customerType)) {
                    isSpecial = true;
                }

                JSONObject singleCustomerRes = singleCustomer(customerId, groupName, isSpecial, aCase, step);
                PersonProp perPropBySingleCustomer = getPerPropBySingleCustomer(singleCustomerRes, isSpecial);

                if (!perPropByCurrCustomerHis.equals(perPropBySingleCustomer)) {
                    throw new Exception("历史人物列表与单个人物详细信息中的人物信息不匹配。");
                }
            }

            if (!isExist) {
                throw new Exception("current customer history---person data is null.");
            }

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    -------------------------------------------测试历史统计查询-----------------------------------------------------------
    @Test
    public void getHisStatisticsPvUv() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "计算历史统计查询的pv，uv";
        failReason = "";

        int step = 0;

        try {

            aCase.setRequestData("查询历史统计查询中返回的pv，uv数");

            DateTimeUtil dateTimeUtil = new DateTimeUtil();
//            long startTime = dateTimeUtil.initDateByDay();
//            long endTime = dateTimeUtil.initDateByDay() + 1000 * 60 * 60 * 24;
//            String type ="DAY";

            long startTime = dateTimeUtil.initDateByDay();
            long endTime = dateTimeUtil.initDateByDay() + 1000 * 60 * 60 * 24;
            String type ="HOUR";



            JSONObject customerStatisticsJo = customerStatistics(startTime,endTime,type,aCase, step);
            getPvUv(customerStatisticsJo, "历史统计查询", aCase);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ---------------------------------------------测试当日统计查询-------------------------------------------------------------
    @Test
    public void getCurrentStatisticsPvUv() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "计算历史统计查询的pv，uv";
        failReason = "";

        int step = 0;

        try {

            aCase.setRequestData("查询当日统计查询中返回的pv，uv数");

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");

            JSONObject customerStatisticsJo = currentCustomerStatistics(aCase, step);
            getPvUv(customerStatisticsJo, "当日统计查询", aCase);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    private void getPvUv(JSONObject response, String function, Case aCase) throws Exception {
        JSONArray statisticsArr = response.getJSONObject("data").getJSONArray("statistics");

        if (statisticsArr == null || statisticsArr.size()==0) {
            throw new Exception(function + "---返回数据为空！");
        } else {
            JSONObject singlestatistics = statisticsArr.getJSONObject(0);

            JSONObject passingTimeJo = singlestatistics.getJSONObject("passing_times");

            int enterPv = passingTimeJo.getInteger("entrance_enter_total");

            int leavePv = passingTimeJo.getInteger("entrance_leave_total");


            JSONObject personNumberJo = singlestatistics.getJSONObject("person_number");

            int enterUv = personNumberJo.getInteger("entrance_enter_total");

            logger.info("进入uv：" + enterUv);
            logger.info("进入pv：" + enterPv);
            logger.info("离开pv：" + leavePv);

            aCase.setResponse(aCase.getResponse() + "\n" + "进入uv：" + enterUv);

            aCase.setResponse(aCase.getResponse() + "\n" + "进入pv：" + enterPv);

            aCase.setResponse(aCase.getResponse() + "\n" + "离开pv：" + leavePv);

        }
    }

    private PersonProp getPerPropBySingleCustomer(JSONObject response, boolean isSpecial) throws Exception {

        JSONObject personJo = response.getJSONObject("data").getJSONObject("person");
        String personStr = personJo.toJSONString();
        logger.info(personStr);
        PersonProp personProp = new PersonProp();
        String groupName = "";

        String customerId, customerType;
        boolean isMale;

        if (personJo != null || personJo.size() == 0){
            throw new Exception("实时人物列表中返回的人物没有在的单个人物详情中查到。");
        }else {
            if (personJo.containsKey("customer_id")) {
                customerId = personJo.getString("customer_id");
            } else {
                throw new Exception("customerId is null");
            }

            if (personJo.containsKey("customer_type")) {
                customerType = personJo.getString("customer_type");
            } else {
                throw new Exception("customer_type is null");
            }

            JSONObject customerProp = personJo.getJSONObject("customer_property");
            if (customerProp == null) {
                throw new Exception("customer_property is null.");
            } else {
                if (customerProp.containsKey("is_male")) {
                    isMale = customerProp.getBoolean("is_male");
                } else {
                    throw new Exception("is_male is null.");
                }
            }

            if (isSpecial) {
                if (personJo.containsKey("group_name")) {
                    groupName = personJo.getString("group_name");
                } else {
                    throw new Exception("group_name is null.");
                }
            }

            personProp.setCustomerId(customerId);
            personProp.setCustomerType(customerType);
            personProp.setMale(isMale);
            personProp.setGroupName(groupName);
        }

        return personProp;
    }

    private void getCurrentCustomerHisRate(JSONObject responseJo, int expectNum, String function, Case aCase) throws Exception {

        JSONArray personArray = responseJo.getJSONObject("data").getJSONArray("person");
        int size = 0;
        if (personArray != null) {
            size = personArray.size();
        } else {
            throw new Exception("实时人物列表为空！");
        }

        aCase.setResponse(aCase.getRequestData() + "\n" + "赢识系统返回人数：" + size + "\n" + "实际人数：" + expectNum);

        return;
    }

    private void checkPicUrl(JSONObject response, String FilePathForLoad) {

        FileUtil fileUtil = new FileUtil();
        JSONArray personArr = response.getJSONObject("data").getJSONArray("person");
        for (int i = 0; i < personArr.size(); i++) {
            JSONObject singlePerson = personArr.getJSONObject(i);
            if (singlePerson.containsKey("body_url")) {
                String bodyUrl = singlePerson.getString("body_url");
                fileUtil.downloadImage(bodyUrl, FilePathForLoad);
            }

            if (singlePerson.containsKey("face_url")) {
                String faceUrl = singlePerson.getString("face_url");
                fileUtil.downloadImage(faceUrl, FilePathForLoad);
            }
        }

        fileUtil.clearInfoForFile(FilePathForLoad);
    }

    private void checkCurrentCustomerHistoryDs(JSONObject response) throws Exception {
        JSONArray personArr = response.getJSONObject("data").getJSONArray("person");
        if (personArr.size() == 0) {
            throw new Exception("实时人物列表为空！");
        }
        for (int i = 0; i < personArr.size(); i++) {
            JSONObject singlePerson = personArr.getJSONObject(i);
            if (!singlePerson.containsKey("customer_id")) {
                throw new Exception("response中--customer_id 不存在");
            }

            if (!singlePerson.containsKey("is_male")) {
                throw new Exception("response中--is_male 不存在");
            }

            if (!singlePerson.containsKey("first_appear_time")) {
                throw new Exception("response中--first_appear_time 不存在");
            }

//            if (singlePerson.containsKey("today_first_appear_time")){
//                throw new Exception("response中--today_first_appear_time 不存在");
//            }

//            if (singlePerson.containsKey("today_last_appear_time")){
//                throw new Exception("response中--today_last_appear_time 不存在");
//            }

            if (!singlePerson.containsKey("face_url") && !singlePerson.containsKey("body_url")) {
                throw new Exception("response中--face_url和body_url都不存在");
            }

            if (!singlePerson.containsKey("customer_type")) {
                throw new Exception("response中--customer_type不存在");
            }

            if ("SPECIAL".equals(singlePerson.getString("customer_type"))) {
                if (!singlePerson.containsKey("group_name")) {
                    throw new Exception("response中--customer_type为special，但是group_name不存在");
                }
            }

        }
    }

    private ApiResponse sendRequest(String router, String[] resource, String json) throws Exception {
        logMine.logStep("send Request！");
        try {
            Credential credential = new Credential(AK, SK);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant("apiClient" + JSON.toJSONString(apiClient));
            logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
            logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    public void checkCode(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID_DB);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
//        if (StringUtils.isEmpty(IMAGE_EDGE)) {
//            aCase.setCaseDescription(caseDesc);
//        } else {
//            aCase.setCaseDescription(caseDesc + "\n" + IMAGE_EDGE);
//        }
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
    }

    public void sendResAndReqIdToDbApi(ApiResponse response, Case acase, int step) {

        if (response != null) {
//            将requestId存入数据库
            String requestId = response.getRequestId();
            String responseStr = JSON.toJSONString(response);
            JSONObject responseJo = JSON.parseObject(responseStr);

            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "\n" + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }

//            将response存入数据库(太多了，显得乱)
//            String responseBefore = acase.getResponse();
//            if (responseBefore != null && responseBefore.trim().length() > 0) {
//                acase.setResponse(responseBefore + "(" + step + ") " + responseJo + "\n\n");
//            } else {
//                acase.setResponse(responseJo + "\n\n");
//            }
        }
    }

    @BeforeSuite
    public void initial() throws Exception {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}
