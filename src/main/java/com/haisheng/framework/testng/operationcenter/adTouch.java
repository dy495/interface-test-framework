package com.haisheng.framework.testng.operationcenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

public class adTouch {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String setStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/people_found/insert";
    private String activateStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/process/people_found";
    private String deleteStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/delete/V2";

    private String shopId = "8";
    private String customerId = "000";
    String discoveryTime = "1";
    private String startTime = "1560130771670"; // 2019-06-10 09:39:31
    private String endTime = "1591753140000"; // 2020-06-10 09:39:00
    private String age = "20";
    private String endpointType = "TOUCH_CUSTOMER";
    private String endpointGroups = "55";
    private String endpointCrowdIds = "40";
    private String adSpaceId = "30";

    private String memberId1 = "";
    private String memberId2 = "";
    private String invalidMemberId1= "";

    private String response = "";

    public StrategyPara setStrategy(String desc, String testPara,String testOp, String value,String adId) throws Exception {
        logger.info("setStrategyBetween--------------------------------------------------");
        HashMap<String,String> header = new HashMap();
        header.put("creator","%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId","55");

        StrategyPara strategyPara;
        String json = "{" +
                "    \"shop_id\": \"" + shopId + "\"," +
                "    \"nodeId\":\"55\"," +
                "    \"brand_id\": \"55\"," +
                "    \"trigger_conditions\": [" +
                "    {" +
                "            \"desc\": \"" + desc + "\"," +
                "            \"parameter\": \"" + testPara +"\"," +
                "            \"operator\": \"" + testOp +"\"," +
//                "            \"value\": " + value + "" +
                "            \"value\": \"" + value + "\"" +
                "        }" +
                "    ]," +
                "    " +
                "    \"duration_time\": {" +
                "        \"start_time\": \"" + startTime + "\"," +
                "        \"end_time\": \"" + endTime + "\"," +
                "    }," +
                "    " +
                "    \"action_list\": [" +
                "        [" +
                "            {" +
                "                \"parameter\": \"shopId\"," +
                "                \"value\": "+ "\"8\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointIds\"," +
                "                \"value\": [" +
                "                    \"57d5b4c9ead8bf2ce10dcf01a91d87a2\"" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointGroups\"," +
                "                \"value\": [" +
                "                    \"55\"" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointCrowdIds\"," +
                "                \"value\": [" +
                "                    \"40\"" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointType\"," +
                "                \"value\": \"" +endpointType +"\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"adId\"," +
                "                \"value\": \"" + adId + "\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"adSpaceId\"," +
                "                \"value\": \"30\"" +
                "            }" +
                "        ]" +
                "    ]" +
                "}";
        try {
            String strategyId;
            response = sendRequestWithHeader(setStrategyURL,json,header);
            int code = JSON.parseObject(response).getInteger("code");
            if(1000==code){
                strategyId = JSON.parseObject(response).getJSONObject("data").getString("id");
            }else if(4016==code){
                String resMessage = JSON.parseObject(response).getString("message");
                strategyId = resMessage.substring(resMessage.indexOf("[") + 1, resMessage.indexOf("]"));
            }else{
                throw new Exception("set strategy failed!");
            }

            strategyPara = new StrategyPara();
            strategyPara.desc = desc;
            strategyPara.testPara = testPara;
            strategyPara.testOp = testOp;
            strategyPara.adId = adId;
            strategyPara.endPointType = endpointType;
            strategyPara.value = value;
            strategyPara.adSpaceId = adSpaceId;
            strategyPara.strategyId = strategyId;

        } catch (Exception e) {
            throw e;
        }
        return strategyPara;
    }


    public StrategyPara setStrategyBetween(String desc, String testPara,String testOp, String value,String adId) throws Exception {
        logger.info("setStrategyBetween--------------------------------------------------");
        HashMap<String,String> header = new HashMap();
        header.put("creator","%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId","55");

        StrategyPara strategyPara;
        String json = "{" +
                "    \"shop_id\": \"" + shopId + "\"," +
                "    \"nodeId\":\"55\"," +
                "    \"brand_id\": \"55\"," +
                "    \"trigger_conditions\": [" +
                "    {" +
                "            \"desc\": \"" + desc + "\"," +
                "            \"parameter\": \"" + testPara +"\"," +
                "            \"operator\": \"" + testOp +"\"," +
                "            \"value\": " + value + "" +
//                "            \"value\": \"" + value + "\"" +
                "        }" +
                "    ]," +
                "    " +
                "    \"duration_time\": {" +
                "        \"start_time\": \"" + startTime + "\"," +
                "        \"end_time\": \"" + endTime + "\"," +
                "    }," +
                "    " +
                "    \"action_list\": [" +
                "        [" +
                "            {" +
                "                \"parameter\": \"shopId\"," +
                "                \"value\": "+ "\"8\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointIds\"," +
                "                \"value\": [" +
                "                    \"57d5b4c9ead8bf2ce10dcf01a91d87a2\"" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointGroups\"," +
                "                \"value\": [" +
                "                    \"55\"" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointCrowdIds\"," +
                "                \"value\": [" +
                "                    \"40\"" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointType\"," +
                "                \"value\": \"" +endpointType +"\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"adId\"," +
                "                \"value\": \"" + adId + "\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"adSpaceId\"," +
                "                \"value\": \"30\"" +
                "            }" +
                "        ]" +
                "    ]" +
                "}";
        try {
            String strategyId;
            response = sendRequestWithHeader(setStrategyURL,json,header);
            int code = JSON.parseObject(response).getInteger("code");
            if(1000==code){
                strategyId = JSON.parseObject(response).getJSONObject("data").getString("id");
            }else if(4016==code){
                String resMessage = JSON.parseObject(response).getString("message");
                strategyId = resMessage.substring(resMessage.indexOf("[") + 1, resMessage.indexOf("]"));
            }else{
                throw new Exception("set strategy failed!");
            }

            strategyPara = new StrategyPara();
            strategyPara.desc = desc;
            strategyPara.testPara = testPara;
            strategyPara.testOp = testOp;
            strategyPara.adId = adId;
            strategyPara.endPointType = endpointType;
            strategyPara.value = value;
            strategyPara.adSpaceId = adSpaceId;
            strategyPara.strategyId = strategyId;

        } catch (Exception e) {
            throw e;
        }
        return strategyPara;
    }

    public String activateStrategy(String customerIdPara,String discoveryTimesPara,String agePara, String isMalePara) throws Exception {
        logger.info("activateStrategy------------------------------------------------------------------");
        HashMap<String,String> header = new HashMap();
        header.put("creator","%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId","55");
        String json = "{" +
                "    \"msg_time\":\"1559564036184\"," +
                "    \"shop_id\":\"8\"," +
                "    \"brand_id\":\"55\"," +
                "    \"person\":[" +
                "        {" +
                "            \"customer_id\":\""+ customerIdPara +"\", " +
                "            \"group_name\":\"55\", " +
                "            \"discovery_times\": \"" + discoveryTimesPara + "\"," +
                "            \"customer_type\":\"SPECIAL\", " +
                "            \"customer_property\":{" +
                "                \"age\":\"" + agePara + "\"," +
                "                \"is_male\":\"" + isMalePara + "\"," +
                "                \"quality_level\":\"GOOD\" " +
                "            }," +
                "            \"customer_position\":{" +
                "                \"device_id\":\"152\", " +
                "                \"region\":[{ \n" +
                "                    \"region_id\":\"123\"," +
                "                    \"start_time\":" + "1560130956399" + "," +
                "                    \"end_time\":" + "1560130966053" + "," +
                "                    \"enter_entrance_id\":\"111\"," +
                "                    \"leave_entrance_id\":\"\"" +
                "                }]" +
                "            }" +
                "        }" +
                "    ]" +
                "}";
        try {
            int expectCode = StatusCode.SUCCESS;
            response = sendRequestOnly(activateStrategyURL,json);
//            response = sendRequestWithHeader(activateStrategyURL,json,header);
            checkCode(response, expectCode);
        } catch (Exception e) {
            throw e;
        }
        return response;
    }


    public void deleteStrategy(String strategyId) throws Exception {
        logger.info("deleteStrategy-----------------------------------------------------------------");
        HashMap<String,String> header = new HashMap();
        header.put("creator","%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId","55");
        String json = "{" +
                "\"id\":\"" + strategyId  +"\"" +
                "}";
        try {
            int expectCode = StatusCode.SUCCESS;
            response = sendRequestOnly(deleteStrategyURL,json);
//            checkCode(response, expectCode);
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(dataProvider = "CUSTOMERID_==")
    public void CustomerIdEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("CustomerIdEqual "+ testValue + "--------------------------------------------------------------------");
        StrategyPara strategyPara;
        String activeResponse;
        String testCustomerId = testValue;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testValue,discoveryTime,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "CUSTOMERID_!=")
    public void CustomerIdNotEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("CustomerId != "+ testValue + "--------------------------------------------------------------------");
        StrategyPara strategyPara;
        String activeResponse;
        String testCustomerId = testValue;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testValue,discoveryTime,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "CUSTOMERID_IN")
    public void CustomerIdIn(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("CustomerIdIn "+ testValue + "--------------------------------------------------------------------");
        StrategyPara strategyPara;
        String activeResponse;
        String testCustomerId = testValue;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testValue,discoveryTime,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "CUSTOMERID_NOT_IN")
    public void CustomerIdNotIn(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("CustomerIdNotIn "+ testValue + "--------------------------------------------------------------------");
        StrategyPara strategyPara;
        String activeResponse;
        String testCustomerId = testValue;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testValue,discoveryTime,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_<")
    public void discoveryTimesLess(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesLess "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_<=")
    public void discoveryTimesLessOrEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesLessOrEqual "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_==")
    public void discoveryTimesEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesEqual "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_!=")
    public void discoveryTimesNotEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesNotEqual "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_>=")
    public void discoveryTimesMoreOrEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesMoreOrEqual "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_>")
    public void discoveryTimesMore(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesMore "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_NOT_IN")
    public void discoveryTimesNOtIn(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesIn"+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_IN")
    public void discoveryTimesIn(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesIn"+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }


    @Test(dataProvider = "DISCOVERY_TIMES_BETWEEN")
    public void discoveryTimesBetween(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("discoveryTimesBetween"+ testValue +"--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,testValue,age,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }


    @Test(dataProvider = "AGE_<")
    public void ageLess(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age< "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "AGE_<=")
    public void ageLessOrEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age<= "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "AGE_==")
    public void ageEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age== "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "AGE_!=")
    public void ageNotEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age!= "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "AGE_IN")
    public void ageIn(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age in "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "AGE_NOT_IN")
    public void ageNotIn(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age not in "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "AGE_BETWEEN")
    public void ageBetween(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("age between "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategyBetween(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,testValue,"true");
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test(dataProvider = "IS_MALE_!=")
    public void isMaleNotEqual(String desc, String testPara,String testOp, String value,String adId,String testValue,String expectResult){
        logger.info("isMale!= "+ testValue + "--------------------------------------------------------------------");
        String testCustomerId = customerId;
        StrategyPara strategyPara;
        String activeResponse;
        try {
            strategyPara = setStrategy(desc,testPara,testOp,value,adId);
            String strategyId = strategyPara.strategyId;
            activeResponse = activateStrategy(testCustomerId,discoveryTime,age,testValue);
            strategyPara.customerId = testCustomerId;
            deleteStrategy(strategyId);
            checkIsSuccess(activeResponse, strategyPara, expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }


    private String sendRequestWithHeader(String URL, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(URL,json,header);
        return executor.getResponse();
    }

    private String sendRequestOnly(String URL,String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL,json);
        return executor.getResponse();
    }

    private void checkCode( String response, int expect) throws Exception{
        int i = 0;
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, "expect code == " + expect);
    }

    private boolean checkIsSuccess(String activeResponse,StrategyPara strategyPara,String expectResult ) throws Exception {
        String msg;
        String strategyId = null;
        JSONArray matchList = JSON.parseObject(activeResponse).getJSONObject("data").getJSONArray("match_list");
        try {
            for(int i = 0; i<matchList.size();i++){
                JSONObject matchSingle = matchList.getJSONObject(i);
                String triggerCustomerId = matchSingle.getString("touch_trigger_customer_id");

                strategyId = strategyPara.strategyId;
                Assert.assertEquals(triggerCustomerId, strategyPara.customerId, "expect touch_trigger_customer_id == " + triggerCustomerId);

                JSONObject matchStrategy = matchSingle.getJSONObject("match_people_found_strategy");
                JSONArray matchTasks = matchStrategy.getJSONArray("tasks");

                if("false".equals(expectResult)){
                    Assert.assertEquals(null, matchTasks,"");
                }else if("true".equals(expectResult)){
                    Assert.assertNotEquals(null,matchTasks);

                        JSONObject taskSingle = null;
                        String taskRuleId;
                        JSONArray tasks = matchStrategy.getJSONArray("tasks");
                        for(int j = 0;j<tasks.size();j++){
                            taskSingle = tasks.getJSONObject(j);
                            taskRuleId = taskSingle.getString("touch_trigger_rule_id");

                            Assert.assertEquals(taskRuleId, strategyId, "");

                            JSONObject touchEndPoint = taskSingle.getJSONObject("touch_endpoint");

                            String adIdRes = touchEndPoint.getString("ad_id");

                            Assert.assertEquals(adIdRes,strategyPara.adId,"");

                            String adSpaceIdRes = touchEndPoint.getString("ad_space_id");

                            Assert.assertEquals(adSpaceIdRes,strategyPara.adSpaceId,"");

                            String endPointType = taskSingle.getString("endpoint_type");

                            Assert.assertEquals(endPointType,strategyPara.endPointType,"");

                            String content = taskSingle.getString("content");
                            if(!content.equals(strategyPara.desc)){
                                msg = "content is wrong!";
                                throw new Exception(msg);
                            }

                            Assert.assertEquals(content,strategyPara.desc,"");

                            //还没校验endpoint_ids
                            //JSONArray endpointIds = taskSingle.getJSONArray("endpoint_ids");

                        }
                    }
            }
        }catch (Exception e){

        }finally {
            deleteStrategy(strategyId);
        }

        return true;
    }

    @DataProvider(name="CUSTOMERID_==")
    public static Object[][] customerIdEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId==","customerId","==","001","42","001","true"},
                new Object[]{"customerId==","customerId","==","001","42","000","false"},
                new Object[]{"customerId==","customerId","==","001","42","002","false"},
                new Object[]{"customerId==","customerId","==","001","42","003","false"},
                new Object[]{"customerId==","customerId","==","001","42","004","false"},

        };
    }

    @DataProvider(name="CUSTOMERID_!=")
    public static Object[][] customerIdNotEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId!=","customerId","!=","001","43","001","false"},
                new Object[]{"customerId!=","customerId","!=","001","43","000","true"},
                new Object[]{"customerId!=","customerId","!=","001","43","002","true"},
                new Object[]{"customerId!=","customerId","!=","001","43","003","true"},
                new Object[]{"customerId!=","customerId","!=","001","43","004","true"},

        };
    }

    @DataProvider(name="CUSTOMERID_IN")
    public static Object[][] customerIdIn(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId-In","customerId","in","[\"001\",\"002\"]","44","001","true"},
                new Object[]{"customerId-In","customerId","in","[\"001\",\"002\"]","44","002","true"},
                new Object[]{"customerId-In","customerId","in","[\"001\",\"002\"]","44","000","false"},
                new Object[]{"customerId-In","customerId","in","[\"001\",\"002\"]","44","432432","false"},
                new Object[]{"customerId-In","customerId","in","[\"001\",\"002\"]","44","534","false"},
                new Object[]{"customerId-In","customerId","in","[\"001\",\"002\"]","44","ger534","false"},

        };
    }

    @DataProvider(name="CUSTOMERID_NOT_IN")
    public static Object[][] customerIdNotIn(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId-In","customerId","not in","[\"001\",\"002\"]","45","001","false"},
                new Object[]{"customerId-In","customerId","not in","[\"001\",\"002\"]","45","002","false"},
                new Object[]{"customerId-In","customerId","not in","[\"001\",\"002\"]","45","fdse423af","true"},
                new Object[]{"customerId-In","customerId","not in","[\"001\",\"002\"]","45","003","true"},
                new Object[]{"customerId-In","customerId","not in","[\"001\",\"002\"]","45","fds96-","true"},

        };
    }

    @DataProvider(name="DISCOVERY_TIMES_<")
    public static Object[][] discoveryTimesLess(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","-1","true"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","0","true"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","1","true"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","4","true"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","5","false"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","6","false"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","10","false"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","50","false"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","100","false"},
                new Object[]{"discoveryTimes_<","discoveryTimes","<","5","46","1000","false"},
        };
    }

    @DataProvider(name="DISCOVERY_TIMES_<=")
    public static Object[][] discoveryTimesLessOrEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","-1","true"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","0","true"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","1","true"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","4","true"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","5","true"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","6","false"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","10","false"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","500","false"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","100","false"},
                new Object[]{"discoveryTimes_<=","discoveryTimes","<=","5","47","1000","false"},
        };
    }

    @DataProvider(name="DISCOVERY_TIMES_==")
    public static Object[][] discoveryTimesEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","-1","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","0","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","1","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","4","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","5","true"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","6","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","10","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","50","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","100","false"},
                new Object[]{"discoveryTimes_==","discoveryTimes","==","5","48","1000","false"},

        };
    }

    @DataProvider(name="DISCOVERY_TIMES_!=")
    public static Object[][] discoveryTimesNotEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","-1","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","0","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","1","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","4","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","5","false"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","6","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","10","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","50","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","100","true"},
                new Object[]{"discoveryTimes_!=","discoveryTimes","!=","5","49","1000","true"},
        };
    }


    @DataProvider(name="DISCOVERY_TIMES_>=")
    public static Object[][] discoveryTimesMoreOrEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","-1","false"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","0","false"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","1","false"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","4","false"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","5","true"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","6","true"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","10","true"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","50","true"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","100","true"},
                new Object[]{"discoveryTimes_>=","discoveryTimes",">=","5","50","1000","true"},
        };
    }

    @DataProvider(name="DISCOVERY_TIMES_>")
    public static Object[][] discoveryTimesMore(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","-1","false"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","0","false"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","1","false"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","4","false"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","5","false"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","6","true"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","10","true"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","50","true"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","100","true"},
                new Object[]{"discoveryTimes_>","discoveryTimes",">","5","51","1000","true"},
        };
    }

    @DataProvider(name="DISCOVERY_TIMES_IN")
    public static Object[][] discoveryTimesIn(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "-1", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "0", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "1", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "4", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "5", "true"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "6", "true"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "7", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "10", "true"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "11", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "50", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "100", "false"},
                new Object[]{"discoveryTimes_in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "1000", "false"},
        };
    }

    @DataProvider(name="DISCOVERY_TIMES_NOT_IN")
    public static Object[][] discoveryTimesNotIn(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "-1", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "0", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "1", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "4", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "5", "false"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "6", "false"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "7", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "10", "false"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "11", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "50", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "100", "true"},
                new Object[]{"discoveryTimes_not_in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "1000", "true"},
        };
    }


    @DataProvider(name="DISCOVERY_TIMES_BETWEEN")
    public static Object[][] discoveryTimesBetween(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","-1","flase"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","0","flase"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","4","flase"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","5","true"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","6","true"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","7","true"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","10","flase"},
                new Object[]{"discoveryTimes_Between_5_7","discoveryTimes","between","[\"5\",\"7\"]","54","100","flase"},
        };
    }

    @DataProvider(name="AGE_<")
    public static Object[][] ageLess(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age<","customerProperty=>age","<","23","55","-1","true"},
                new Object[]{"age<","customerProperty=>age","<","23","55","0","true"},
                new Object[]{"age<","customerProperty=>age","<","23","55","1","true"},
                new Object[]{"age<","customerProperty=>age","<","23","55","22","true"},
                new Object[]{"age<","customerProperty=>age","<","23","55","23","false"},
                new Object[]{"age<","customerProperty=>age","<","23","55","24","false"},
                new Object[]{"age<","customerProperty=>age","<","23","55","50","false"},
                new Object[]{"age<","customerProperty=>age","<","23","55","100","false"},
                new Object[]{"age<","customerProperty=>age","<","23","55","100","false"},
        };
    }

    @DataProvider(name="AGE_<=")
    public static Object[][] ageLessOrEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age<=","customerProperty=>age","<=","23","56","-1","true"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","0","true"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","1","true"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","22","true"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","23","true"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","24","false"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","50","false"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","100","false"},
                new Object[]{"age<=","customerProperty=>age","<=","23","56","1000","false"},
//                new Object[]{"age<=","customerProperty=>age","<=","23","56","#","false"},
        };
    }

    @DataProvider(name="AGE_==")
    public static Object[][] ageEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age==","customerProperty=>age","==","23","57","-1","false"},
                new Object[]{"age==","customerProperty=>age","==","23","57","0","false"},
                new Object[]{"age==","customerProperty=>age","==","23","57","1","false"},
                new Object[]{"age==","customerProperty=>age","==","23","57","22","false"},
                new Object[]{"age==","customerProperty=>age","==","23","57","23","true"},
                new Object[]{"age==","customerProperty=>age","==","23","57","24","false"},
                new Object[]{"age==","customerProperty=>age","==","23","57","50","false"},
                new Object[]{"age==","customerProperty=>age","==","23","57","100","false"},
//                new Object[]{"age==","customerProperty=>age","==","23","57","*","false"},
        };
    }

    @DataProvider(name="AGE_!=")
    public static Object[][] ageNotEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age!=","customerProperty=>age","!=","23","58","-1","true"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","0","true"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","1","true"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","22","true"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","23","false"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","24","true"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","50","true"},
                new Object[]{"age!=","customerProperty=>age","!=","23","58","100","true"},
//                new Object[]{"age!=","customerProperty=>age","!=","23","58","$","false"},
        };
    }

    @DataProvider(name="AGE_>=")
    public static Object[][] ageNotMoreOrEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age>=","customerProperty=>age",">=","23","59","-1","false"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","0","false"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","1","false"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","22","false"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","23","true"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","23.0","true"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","24","true"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","50","true"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","100","true"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","1000","true"},
                new Object[]{"age>=","customerProperty=>age",">=","23","59","%","false"},
        };
    }

    @DataProvider(name="AGE_>")
    public static Object[][] ageNotMore(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age>","customerProperty=>age",">","23","60","-1","false"},
                new Object[]{"age>","customerProperty=>age",">","23","60","0","false"},
                new Object[]{"age>","customerProperty=>age",">","23","60","1","false"},
                new Object[]{"age>","customerProperty=>age",">","23","60","22","false"},
                new Object[]{"age>","customerProperty=>age",">","23","60","23","false"},
                new Object[]{"age>","customerProperty=>age",">","23","60","24","true"},
                new Object[]{"age>","customerProperty=>age",">","23","60","50","true"},
                new Object[]{"age>","customerProperty=>age",">","23","60","100","true"},
                new Object[]{"age>","customerProperty=>age",">","23","60","&","false"},
        };
    }

    @DataProvider(name="AGE_IN")
    public static Object[][] ageIn(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","-1","false"},
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","0","false"},
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","1","false"},
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","23","true"},
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","24","true"},
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","50","true"},
                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","100","false"},
//                new Object[]{"age-in","customerProperty=>age","in","[\"23\",\"24\",\"50\"]","61","23.0","true"},
        };
    }

    @DataProvider(name="AGE_NOT_IN")
    public static Object[][] ageNotIn(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","-1","true"},
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","0","true"},
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","1","true"},
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","23","false"},
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","24","false"},
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","50","false"},
                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","100","true"},
//                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","23.0","true"},
//                new Object[]{"age-not in","customerProperty=>age","not in","[\"23\",\"24\",\"50\"]","62","~","false"},
        };
    }


    @DataProvider(name="AGE_BETWEEN")
    public static Object[][] ageBetween(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","-1","false"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","22","false"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","23","true"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","24","true"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","25","true"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","26","true"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","27","true"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","28","false"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","50","false"},
                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","100","false"},
//                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","24.0","true"},
//                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","-24.0","false"},
//                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","24.0-","false"},
//                new Object[]{"age-between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","24.5","true"},

        };
    }

    @DataProvider(name="IS_MALE_==")
    public static Object[][] isMaleEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"isMale==","customerProperty=>isMale","==","true","64","true","true"},
                new Object[]{"isMale==","customerProperty=>isMale","==","true","64","false","false"},
                new Object[]{"isMale==","customerProperty=>isMale","==","true","64","","false"},

        };
    }

    @DataProvider(name="IS_MALE_!=")
    public static Object[][] isMaleNotEqual(){
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"isMale!=","customerProperty=>isMale","!=","true","64","true","false"},
                new Object[]{"isMale!=","customerProperty=>isMale","!=","true","64","true","true"},
                new Object[]{"isMale!=","customerProperty=>isMale","!=","true","64","","true"},

        };
    }



}

class StrategyPara{
    String desc, testPara, testOp, value, adId, adSpaceId, endPointType, strategyId, customerId;
}