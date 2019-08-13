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
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * 线下消费者，人物信息查询和当日统计查询部分接口的功能验证
 * @author Shine
 */
public class customerStatistics {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_e0d1ebec";
    private static String APP_ID = "a4d4d18741a8";
    private static String SHOP_ID = "134";
    String AK = "e0709358d368ee13";
    String SK = "ef4e751487888f4a7d5331e8119172a3";

//    private String VIDEO_NAME = System.getProperty("VIDEO_NAME");
//    private int ENTER_PV = Integer.valueOf(System.getProperty("ENTER_PV"));
//    private int ENTER_UV = Integer.valueOf(System.getProperty("ENTER_UV"));
//    private int LEAVE_UV = Integer.valueOf(System.getProperty("LEAVE_UV"));
//    private int MALE = Integer.valueOf(System.getProperty("MALE"));
//    private int FEMALE = Integer.valueOf(System.getProperty("FEMALE"));

    private String VIDEO_NAME = System.getProperty("VIDEO_NAME");
    private int ENTER_PV = 100;
    private int ENTER_UV = 100;
    private int LEAVE_PV = 100;
    private int MALE = 100;
    private int FEMALE = 100;

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID_DB = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/zhimaSampleAccuracyTest/buildWithParameters?videoSample=" +
            VIDEO_NAME + "&isPushMsg=true&isSaveToDb=true&case_name=";

//    private static String UID = "uid_7fc78d24";
//    private static String APP_ID = "097332a388c2";
//    private static String SHOP_ID = "8";
//    String AK = "77327ffc83b27f6d";
//    String SK = "7624d1e6e190fbc381d0e9e18f03ab81";

    private ApiResponse apiResponse  = null;


    //    实时人物列表
    public JSONObject currentCustomerHistory(Case aCase,int step) throws Exception {
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        JSONObject responseJo;
        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID+ "\"\n" +
                        "}\n";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);

            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, aCase,step);
        return responseJo;
    }

//    历史人物列表
    public JSONObject customerHistory(Case aCase,int step) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};

        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        String startTime = String.valueOf(dateTimeUtil.initDateByDay());
        String endTime = String.valueOf(dateTimeUtil.initDateByDay()+1000*60*60*24);

        JSONObject responseJo;
        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"," +
                        "        \"time_type\":\"" + "DAY" + "\"," +
                        "        \"start_time\":\"" + startTime+  "\"," +
                        "        \"end_time\":\"" + endTime+  "\"" +
                        "}";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);

            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse,aCase,step);
        return responseJo;
    }

//    单个人物详情查询
    public JSONObject singleCustomer(String customerId, Case aCase,int step) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        String startTime = String.valueOf(dateTimeUtil.initDateByDay());
        String endTime = String.valueOf(Long.valueOf(startTime) + 1000*60*60*24);
        JSONObject responseJo;

        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"," +
                        "        \"customer_id\":\"" + customerId + "\"," +
                        "        \"start_time\":\"" + startTime+  "\"," +
                        "        \"end_time\":\"" + endTime+  "\"" +
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
        sendResAndReqIdToDbApi(apiResponse, aCase,step);
        return responseJo;
    }

    //    历史统计查询
    public JSONObject customerStatistics(Case aCase,int step) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        String startTime = String.valueOf(dateTimeUtil.initDateByDay());
        String endTime = String.valueOf(dateTimeUtil.initDateByDay()+1000*60*60*24);
        JSONObject responseJo;

        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"," +
                        "        \"time_type\":\"" + "DAY" + "\"," +
                        "        \"start_time\":\"" + startTime+  "\"," +
                        "        \"end_time\":\"" + endTime+  "\"" +
                        "}";

        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, aCase, step);
        return responseJo;
    }

//--------------------------------当日统计查询-------------------------------
    public JSONObject currentCustomerStatistics(Case aCase,int step) throws Exception {
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
            checkCode(apiResponse,router,expectCode);
            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }

        sendResAndReqIdToDbApi(apiResponse, aCase,step);
        return responseJo;
    }

//    --------------------------测试实时人物列表-----------------------------------
    @Test
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

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");
            JSONObject responseJo = currentCustomerHistory(aCase,step);

            getCurrentCustomerHisRate(responseJo, ENTER_UV, aCase);

        }catch (Exception e) {
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
    @Test
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

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");

            JSONObject responseJo = customerHistory(aCase,step);
            getCurrentCustomerHisRate(responseJo, ENTER_UV,aCase);
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
    @Test
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

            boolean isExist = false;
            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");
            JSONObject customerHistoryRes = currentCustomerHistory(aCase,step);
            JSONArray personArr = customerHistoryRes.getJSONObject("data").getJSONArray("person");
            PersonProp perPropByCurrCustomerHis = new PersonProp();

            for (int i = 0; i < personArr.size(); i++) {
                isExist = true;

                JSONObject singlePerson = personArr.getJSONObject(i);

                String customerId = singlePerson.getString("customer_id");
                aCase.setResponse(customerId + "\n");

                String customerType = singlePerson.getString("customer_type");
                boolean isMale = singlePerson.getBoolean("is_male");

                perPropByCurrCustomerHis.setCustomerId(customerId);
                perPropByCurrCustomerHis.setCustomerType(customerType);
                perPropByCurrCustomerHis.setMale(isMale);

                logger.info("--------------------------(" + (++step) + ")");
                logger.info("\n\n");

                JSONObject singleCustomerRes = singleCustomer(customerId,aCase,step);
                PersonProp perPropBySingleCustomer = getPerPropBySingleCustomer(singleCustomerRes);

                if (!perPropByCurrCustomerHis.equals(perPropBySingleCustomer)){
                    throw new Exception("历史人物列表与单个人物详细信息中的人物信息不匹配。");
                }
            }

            if (!isExist){
                throw new Exception("current customer history---person data is null.");
            }

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
    public void testCustomerStatistics() throws Exception {
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

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");

            JSONObject customerStatisticsJo = customerStatistics(aCase,step);
            checkcustomerStatisticsRes(customerStatisticsJo,ENTER_PV,ENTER_UV,LEAVE_PV,MALE,FEMALE,aCase);

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

//    ---------------------------------------------测试当日统计查询-------------------------------------------------------------
    @Test
    public void testCurrentCustomerStatistics() throws Exception {
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

            logger.info("--------------------------(" + (++step) + ")");
            logger.info("\n\n");

            JSONObject customerStatisticsJo = currentCustomerStatistics(aCase,step);
            checkcustomerStatisticsRes(customerStatisticsJo,ENTER_PV,ENTER_UV,LEAVE_PV,MALE,FEMALE,aCase);

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

    private void checkcustomerStatisticsRes(JSONObject response, int expectEnterPv, int expectEnterUv, int expectLeavePv,
                                            int expectMale, int expectFemale,Case aCase) throws Exception {
        JSONArray statisticsArr = response.getJSONObject("data").getJSONArray("statistics");

        int maleRes = 0;
        int femaleRes;

        DecimalFormat decimalFormat = new DecimalFormat(".00");

        if (statisticsArr!=null){
            JSONObject singlestatistics = statisticsArr.getJSONObject(0);
            JSONObject genderJo = singlestatistics.getJSONObject("gender");
            if (genderJo!=null){
                maleRes = genderJo.getInteger("male");

                femaleRes = genderJo.getInteger("female");
            } else {
              throw new Exception("返回gender为空！");
            }

            JSONObject passingTimeJo = singlestatistics.getJSONObject("passing_times");

            int enterPv = passingTimeJo.getInteger("entrance_enter_total");
            String pvEnterRate = decimalFormat.format((float)enterPv/(float)expectEnterPv*100) + "%";
            logger.info("pv enter accuracy rate: " + pvEnterRate);

            int leavePv = passingTimeJo.getInteger("entrance_leave_total");
            String pvLeaveRate = decimalFormat.format((float)leavePv/(float)expectLeavePv*100) + "%";
            logger.info("pv leave accuracy rate: " + pvLeaveRate);

            JSONObject personNumberJo = singlestatistics.getJSONObject("person_number");

            int enterUv = personNumberJo.getInteger("entrance_enter_total");
            String uvEnterRate = decimalFormat.format((float)enterUv/(float)expectEnterUv*100) + "%";
            logger.info("uv enter accuracy rate: " + uvEnterRate);


            aCase.setResponse("赢识系统输出进入人数：" +  enterUv);
            aCase.setResponse("实际进入人数：" + expectEnterUv);
            aCase.setResponse("进入uv识别率：" + uvEnterRate);

            aCase.setResponse("赢识系统输出进入人次：" + enterPv );
            aCase.setResponse("实际进入人次：" + expectEnterPv);
            aCase.setResponse("进入pv识别率：" + pvEnterRate);

            aCase.setResponse("赢识系统输出离开人次：" + leavePv);
            aCase.setResponse("实际离开人次：" + expectLeavePv);
            aCase.setResponse("离开pv识别率：" + pvLeaveRate);

            aCase.setResponse("赢识系统输出male人数：" + maleRes);
            aCase.setResponse("实际male人数：" + expectMale);

            aCase.setResponse("赢识系统输出female人数：" + femaleRes);
            aCase.setResponse("实际female人数：" + expectFemale);

        } else {
            throw new Exception("返回数据为空！");
        }

    }

    private PersonProp getPerPropBySingleCustomer(JSONObject response) throws Exception {

        JSONObject personJo = response.getJSONObject("data").getJSONObject("person");
        PersonProp personProp = new PersonProp();

        if (personJo!=null){
            String customerId = personJo.getString("customer_id");
            String customerType = personJo.getString("customer_type");
            JSONObject customerProp = personJo.getJSONObject("customer_property");
            boolean isMale = customerProp.getBoolean("is_male");

            personProp.setCustomerId(customerId);
            personProp.setCustomerType(customerType);
            personProp.setMale(isMale);
        } else {
            throw new Exception("single customer ---- person data is null.");
        }

        return personProp;
    }

    private void getCurrentCustomerHisRate(JSONObject responseJo, int expectNum, Case aCase) {

        JSONArray personArray = responseJo.getJSONObject("data").getJSONArray("person");
        int size = 0;
        if (personArray != null){
            size = personArray.size();
        }

        aCase.setResponse("赢识系统返回人数：" + size + "\n");
        aCase.setResponse("实际人数：" + expectNum + "\n");

        logger.info("实时人物列表的人数：" + size);
        logger.info("期待人数：" + expectNum);

        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String currentCustomerHisRate = decimalFormat.format((float)size/(float)expectNum*100) + "%";

        aCase.setResponse("识别率：" + currentCustomerHisRate + "\n");

        logger.info(currentCustomerHisRate);

        return;
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
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
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

    public void checkCode(ApiResponse apiResponse,String router,int expectCode) throws Exception{
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode+ ".";
                throw new Exception(msg);
            }
        }catch(Exception e){
            throw e;
        }
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID_DB);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
        aCase.setExpect("UNKNOWN");
    }

    public void sendResAndReqIdToDbApi(ApiResponse response, Case acase, int step) {

        if (response != null) {
//            将requestId存入数据库
            String requestId = response.getRequestId();
            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }
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
