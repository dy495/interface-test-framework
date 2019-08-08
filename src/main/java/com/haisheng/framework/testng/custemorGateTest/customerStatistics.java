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
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * 线下消费者，人物信息查询和当日统计查询部分接口的功能验证
 * @author Shine
 */
public class pvuvAccuracy {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_e0d1ebec";
    private static String APP_ID = "a4d4d18741a8";
    private static String SHOP_ID = "134";
    String AK = "e0709358d368ee13";
    String SK = "ef4e751487888f4a7d5331e8119172a3";
    private ApiResponse apiResponse  = null;


    //    实时人物列表
    public JSONObject currentCustomerHistory() throws Exception {
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

        return responseJo;
    }

//    历史人物列表
    public JSONObject customerHistory() throws Exception {
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

        return responseJo;
    }

//    单个人物详情查询
    public JSONObject singleCustomer(String customerId) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        String startTime = String.valueOf(dateTimeUtil.initDateByDay());
        String endTime = String.valueOf(dateTimeUtil.initDateByDay()+1000*60*60*24);
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
            checkCode(apiResponse,router,expectCode);
            String responseStr = JSON.toJSONString(apiResponse);
            responseJo = JSON.parseObject(responseStr);

        } catch (Exception e) {
            throw e;
        }

        return responseJo;
    }

    //    历史统计查询
    public JSONObject customerStatistics() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        String startTime = dateTimeUtil.getHourBegin(-1);
        String endTime = dateTimeUtil.getHourBegin(1);
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

        return responseJo;
    }

//--------------------------------当日统计查询-------------------------------
    public JSONObject currentCustomerStatistics() throws Exception {
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

        return responseJo;
    }

//    --------------------------测试实时人物列表-----------------------------------
    @Test
    public void testCurrentCustomerHistory() throws Exception {

        try {
            JSONObject responseJo = currentCustomerHistory();

            String currentCustomerHisRate = getCurrentCustomerHisRate(responseJo, 100);
            logger.info(currentCustomerHisRate);
        } catch (Exception e) {
            throw e;
        }
    }

//    ----------------------------------测试历史人物列表----------------------------------------
    @Test
    public void testCustomerHistory() throws Exception {
       
        try {
            JSONObject responseJo = customerHistory();
            String currentCustomerHisRate = getCurrentCustomerHisRate(responseJo, 100);
            logger.info(currentCustomerHisRate);
        } catch (Exception e) {
            throw e;
        }
    }

//    -------------------------------------测试单个人物详细信息----------------------------------------------
    @Test
    public void testSingleCustomer() throws Exception {

        try {
            JSONObject customerHistoryRes = customerHistory();
            PersonProp perPropByCustomerHistory = getPerPropByCustomerHistory(customerHistoryRes);
            String customerId = perPropByCustomerHistory.getCustomerId();

            JSONObject singleCustomerRes = singleCustomer(customerId);
            PersonProp perPropBySingleCustomer = getPerPropBySingleCustomer(singleCustomerRes);

            if (!perPropByCustomerHistory.equals(perPropBySingleCustomer)){
                throw new Exception("历史人物列表与单个人物详细信息中的人物信息不匹配。");
            }

        } catch (Exception e) {
            throw e;
        }
    }

//    -------------------------------------------测试历史统计查询-----------------------------------------------------------
    @Test
    public void testCustomerStatistics() throws Exception {
        try {
            JSONObject customerStatisticsJo = customerStatistics();
            checkcustomerStatisticsRes(customerStatisticsJo,100,100,100,100,100);

        } catch (Exception e) {
            throw e;
        }
    }

//    ---------------------------------------------测试当日统计查询-------------------------------------------------------------
    @Test
    public void testCurrentCustomerStatistics() throws Exception {
        try {
            JSONObject customerStatisticsJo = currentCustomerStatistics();
            checkcustomerStatisticsRes(customerStatisticsJo,100,100,100,100,100);

        } catch (Exception e) {
            throw e;
        }
    }

    private void checkcustomerStatisticsRes(JSONObject response, int expectEnterPv, int expectEnterUv, int expectLeavePv,
                                            int expectMale, int expectFemale) throws Exception {
        JSONArray statisticsArr = response.getJSONObject("data").getJSONArray("statistics");

        if (statisticsArr!=null){
            JSONObject singlestatistics = statisticsArr.getJSONObject(0);
            JSONObject genderJo = singlestatistics.getJSONObject("gender");
            if (genderJo!=null){

                DecimalFormat decimalFormat = new DecimalFormat(".00");

                int maleRes = genderJo.getInteger("male");
                String maleRate = decimalFormat.format((float)maleRes/(float)expectMale*100) + "%";
                logger.info("male accracy rate: " + maleRate);

                int femaleRes = genderJo.getInteger("female");
                String femaleRate = decimalFormat.format((float)femaleRes/(float)expectFemale*100 )+ "%";
                logger.info("female accracy rate: " + femaleRate);
            } else {
              throw new Exception("返回gender为空！");
            }

            JSONObject passingTimeJo = singlestatistics.getJSONObject("passing_times");
            DecimalFormat decimalFormat = new DecimalFormat(".00");

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

    private PersonProp getPerPropByCustomerHistory(JSONObject response) throws Exception {

        PersonProp personProp = new PersonProp();
        JSONArray personArr = response.getJSONObject("data").getJSONArray("person");
        if (personArr.size()!=0){
            JSONObject singlePerson = personArr.getJSONObject(0);

            String customerId = singlePerson.getString("customer_id");

            String customerType = singlePerson.getString("customer_type");
            boolean isMale = singlePerson.getBoolean("is_male");

            personProp.setCustomerId(customerId);
            personProp.setCustomerType(customerType);
            personProp.setMale(isMale);
        } else {
            throw new Exception("person data is null.");
        }

        return personProp;
    }


    private String getCurrentCustomerHisRate(JSONObject responseJo, int expectNum) {

        JSONArray personArray = responseJo.getJSONObject("data").getJSONArray("person");
        int size = 0;
        if (personArray != null){
            size = personArray.size();
        }

        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String currentCustomerHisRate = decimalFormat.format((float)size/(float)expectNum*100) + "%";

        return currentCustomerHisRate;
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

}
