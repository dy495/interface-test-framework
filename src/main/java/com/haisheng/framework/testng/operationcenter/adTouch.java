package com.haisheng.framework.testng.operationcenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;

public class adTouch {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //old
//    private String setStrategyURL =      "http://39.97.20.113:7001/ads/touch/strategy/people_found/insert";
//    private String activateStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/process/people_found";
//    private String deleteStrategyURL =   "http://39.97.20.113:7001/ads/touch/strategy/delete/V2";

    //new
    private String setStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/insert/people_found";//5.2.2.4
    private String activateStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/process/people_found";//5.2.1
    private String deleteStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/delete/V2";//5.2.4
    private String clearStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/rule_field/clear/people_found";//文档上没有

    private String shopId = "8";
    private String customerId = "";
    private String discoveryTime = "";
    private String startTime = "1560130771670"; // 2019-06-10 09:39:31
    private String endTime = "1591753140000"; // 2020-06-10 09:39:00
    private String age = "";
    private String isMale = "";
    private String endpointType = "TOUCH_CUSTOMER";
    private String endpointGroups = "55";
    private String endpointCrowdIds = "40";
    private String adSpaceId = "30";
    private String yuID = "b7167b646ce82464e4c55d643bc3900f";
    private String zhiDongId = "57d5b4c9ead8bf2ce10dcf01a91d87a2";
    private String maKunId = "95cba19646d9d2a3fa5fcfc36a90d344";

    private String yuNumber = "18210113587";
    private String zhiDongNumber = "15643576037";
    private String maKunNumber = "13581630214";

    private String response = "";

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_AD_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_AD_SERVICE;

    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/ad_test/buildWithParameters?case_name=";


    public String genSetStrategyPara(String desc, String testPara, String testOp, String value, String adId){

        String json = "{" +
                "    \"shop_id\": \"" + shopId + "\"," +
                "    \"nodeId\":\"55\"," +
                "    \"brand_id\": \"55\"," +
                "    \"trigger_conditions\": [" +
                "    {" +
                "            \"desc\": \"" + desc + "\"," +
                "            \"parameter\": \"" + testPara + "\"," +
                "            \"operator\": \"" + testOp + "\"," +
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
                "                \"value\": " + "\"8\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointIds\"," +
                "                \"value\": [" +
                "                    \"" + yuID + "\"," +
                "                    \"" + maKunId + "\"," +
                "                    \"" + zhiDongId + "\"" +
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
                "                \"value\": \"" + endpointType + "\"" +
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

        return json;
    }

    public StrategyPara setStrategy(String desc, String testPara, String testOp, String value, String adId) throws Exception {
        logger.info("\n");
        logger.info("setStrategyBetween--------------------------------------------------");
        HashMap<String, String> header = new HashMap();
        header.put("creator", "%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId", "55");

        StrategyPara strategyPara = null;

        String json = genSetStrategyPara(desc,testPara,testOp,value,adId);

        try {
            String strategyId = "";

            response = sendRequestWithHeader(setStrategyURL, json, header);

            String message = JSON.parseObject(response).getString("message");
            int code = JSON.parseObject(response).getInteger("code");

            if (1000 == code) {
                strategyId = JSON.parseObject(response).getJSONObject("data").getString("id");
            } else if (4016 == code) {
                String resMessage = JSON.parseObject(response).getString("message");
                strategyId = resMessage.substring(resMessage.indexOf("[") + 1, resMessage.indexOf("]"));
            } else {
                failReason = "set strategy failed!" + message;
                Assert.fail(failReason);
            }

            strategyPara = new StrategyPara();

            strategyPara.requestPara = json;
            strategyPara.response = response;
            strategyPara.desc = desc;
            strategyPara.testPara = testPara;
            strategyPara.testOp = testOp;
            strategyPara.adId = adId;
            strategyPara.endPointType = endpointType;
            strategyPara.value = value;
            strategyPara.adSpaceId = adSpaceId;
            strategyPara.strategyId = strategyId;
            strategyPara.endpointIds = new String[]{yuID, maKunId, zhiDongId};
            strategyPara.touch_members = new String[]{yuNumber, maKunNumber, zhiDongNumber};

        }catch (Exception e) {
            throw e;
        }
        return strategyPara;
    }


    public String genSetStrategyBetweenPara(String desc, String testPara, String testOp, String value, String adId){

        String json = "{" +
                "    \"shop_id\": \"" + shopId + "\"," +
                "    \"nodeId\":\"55\"," +
                "    \"brand_id\": \"55\"," +
                "    \"trigger_conditions\": [" +
                "    {" +
                "            \"desc\": \"" + desc + "\"," +
                "            \"parameter\": \"" + testPara + "\"," +
                "            \"operator\": \"" + testOp + "\"," +
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
                "                \"value\": " + "\"8\"" +
                "            }," +
                "            {" +
                "                \"parameter\": \"endpointIds\"," +
                "                \"value\": [" +
                "                    \"" + yuID + "\"," +
                "                    \"" + maKunId + "\"," +
                "                    \"" + zhiDongId + "\"" +
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
                "                \"value\": \"" + endpointType + "\"" +
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

        return json;
    }

    public StrategyPara setStrategyBetween(String desc, String testPara, String testOp, String value, String adId) throws Exception {
        logger.info("\n");
        logger.info("setStrategyBetween--------------------------------------------------");
        HashMap<String, String> header = new HashMap();
        header.put("creator", "%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId", "55");

        StrategyPara strategyPara = null;

        String json = genSetStrategyBetweenPara(desc,testPara,testOp,value,adId);

        try {
            String strategyId = "";

            response = sendRequestWithHeader(setStrategyURL, json, header);

            String message = JSON.parseObject(response).getString("message");
            int code = JSON.parseObject(response).getInteger("code");

            if (1000 == code) {
                strategyId = JSON.parseObject(response).getJSONObject("data").getString("id");
            } else if (4016 == code) {
                String resMessage = JSON.parseObject(response).getString("message");
                strategyId = resMessage.substring(resMessage.indexOf("[") + 1, resMessage.indexOf("]"));
            } else {
                failReason = "set strategy failed!" + message;
                Assert.fail(failReason);
            }

            strategyPara = new StrategyPara();

            strategyPara.requestPara = json;
            strategyPara.response = response;
            strategyPara.desc = desc;
            strategyPara.testPara = testPara;
            strategyPara.testOp = testOp;
            strategyPara.adId = adId;
            strategyPara.endPointType = endpointType;
            strategyPara.value = value;
            strategyPara.adSpaceId = adSpaceId;
            strategyPara.strategyId = strategyId;
            strategyPara.endpointIds = new String[]{yuID, maKunId, zhiDongId};
            strategyPara.touch_members = new String[]{yuNumber, maKunNumber, zhiDongNumber};

        }catch (Exception e) {
            throw e;
        }
        return strategyPara;
    }

    public String genActivateStrategyPara(String customerIdPara, String discoveryTimesPara, String agePara, String isMalePara){
        String json = "{" +
                "    \"msg_time\":\"1559564036184\"," +
                "    \"shop_id\":\"8\"," +
                "    \"brand_id\":\"55\"," +
                "    \"person\":[" +
                "        {" +
                "            \"customer_id\":\"" + customerIdPara + "\", " +
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

        return json;
    }

    public ActivatePara activateStrategy(String customerIdPara, String discoveryTimesPara, String agePara, String isMalePara) throws Exception {
        logger.info("\n");
        logger.info("activateStrategy------------------------------------------------------------------");

        ActivatePara activatePara = new ActivatePara();

        String json = genActivateStrategyPara(customerIdPara,discoveryTimesPara,agePara,isMalePara);
        try {
            response = sendRequestOnly(activateStrategyURL, json);

            activatePara.requestPara = json;
            activatePara.response = response;

        }catch (Exception e) {
            failReason = "active strategy failed!" + e.getMessage();
            Assert.fail(failReason);
            throw e;
        }
        return activatePara;
    }

    public void deleteStrategy(String strategyId) throws Exception {
        logger.info("\n");
        logger.info("deleteStrategy-----------------------------------------------------------------");

        String json = "{" +
                "\"id\":\"" + strategyId + "\"" +
                "}";
        String message = "";
        try {
            response = sendRequestOnly(deleteStrategyURL, json);
            message = JSON.parseObject(response).getString("message");
        } catch (Exception e) {
            failReason = message + e.getMessage();
            Assert.assertTrue(false);
            throw e;
        }
    }

    public void clearStrategy() throws Exception {
        logger.info("\n");
        logger.info("clearStrategy-----------------------------------------------------------------");
        String json = "{}";
        try {
            response = sendRequestOnly(clearStrategyURL, json);
        } catch (Exception e) {
            failReason = e.getMessage();
            Assert.assertTrue(false);
            throw e;
        }
    }

    @Test(dataProvider = "CUSTOMERID_==")
    public void CustomerIdEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = testValue;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testValue, discoveryTime, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testValue, discoveryTime, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "CUSTOMERID_!=")
    public void CustomerIdNotEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = testValue;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testValue, discoveryTime, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testValue, discoveryTime, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "CUSTOMERID_IN")
    public void CustomerIdIn(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = testValue;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testValue, discoveryTime, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testValue, discoveryTime, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "CUSTOMERID_NOT_IN")
    public void CustomerIdNotIn(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = testValue;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testValue, discoveryTime, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testValue, discoveryTime, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_<")
    public void discoveryTimesLess(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_<=")
    public void discoveryTimesLessOrEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_==")
    public void discoveryTimesEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_!=")
    public void discoveryTimesNotEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_>=")
    public void discoveryTimesMoreOrEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_>")
    public void discoveryTimesMore(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_NOT_IN")
    public void discoveryTimesNOtIn(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_IN")
    public void discoveryTimesIn(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test(dataProvider = "DISCOVERY_TIMES_BETWEEN")
    public void discoveryTimesBetween(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test(dataProvider = "AGE_<")
    public void ageLess(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_<=")
    public void ageLessOrEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_==")
    public void ageEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_!=")
    public void ageNotEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_>=")
    public void ageNotMoreOrEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_>")
    public void ageNotMore(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_IN")
    public void ageIn(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_NOT_IN")
    public void ageNotIn(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "AGE_BETWEEN")
    public void ageBetween(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyBetweenPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategyBetween(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "IS_MALE_==")
    public void isMaleEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, age, testValue);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, age, testValue);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "IS_MALE_!=")
    public void isMaleNotEqual(String desc, String testPara, String testOp, String value, String adId, String testValue, String expectResult) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, age, testValue);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, age, testValue);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testCustomerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "BAD_AGE")
    public void ageBad(String desc, String testPara, String testOp, String value, String adId, String testValue) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, testValue, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, testValue, isMale);

            strategyPara.customerId = testCustomerId;

            checkCode(activatePara.response, 4017, "bad age return wrong code");

            String message = JSON.parseObject(response).getString("message");
            if(!message.contains(testValue)){
                failReason = "message do not contain testValue" + message;
            }

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "BAD_DISCOVERY_TIMES")
    public void discoveryTimesBad(String desc, String testPara, String testOp, String value, String adId, String testValue) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, testValue, age, isMale);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, testValue, age, isMale);

            strategyPara.customerId = testCustomerId;

            checkCode(activatePara.response, 4017, "bad discoveryTimes return wrong code");

            String message = JSON.parseObject(response).getString("message");
            if(!message.contains(testValue)){
                failReason = "message do not contain testValue" + message;
            }

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(dataProvider = "BAD_IS_MALE")
    public void isMalesBad(String desc, String testPara, String testOp, String value, String adId, String testValue) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");
        StrategyPara strategyPara = new StrategyPara();
        ActivatePara activatePara = new ActivatePara();
        String testCustomerId = customerId;
        String strategyId = "";
        failReason = "";
        JSONObject setJo = null;
        JSONObject setResJo = null;
        JSONObject activeJo = null;
        JSONObject activeResJo = null;
        Case aCase = new Case();
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            // get requestPara and save to db
            String setStrategyPara = genSetStrategyPara(desc, testPara, testOp, value, adId);
            String getStrategyPara = genActivateStrategyPara(testCustomerId, discoveryTime, age, testValue);
            setJo = JSON.parseObject(setStrategyPara);
            activeJo = JSON.parseObject(getStrategyPara);
            aCase.setRequestData(setJo + "\n\n" + activeJo);

            strategyPara = setStrategy(desc, testPara, testOp, value, adId);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testCustomerId, discoveryTime, age, testValue);

            strategyPara.customerId = testCustomerId;

            checkCode(activatePara.response, 4017, "bad isMale return wrong code");

            String message = JSON.parseObject(response).getString("message");
            if(!message.contains(testValue)){
                failReason = "message do not contain testValue" + message;
            }

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            e.printStackTrace();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            logger.info(e.getMessage());
            aCase.setFailReason(failReason + "\n" + e.toString());
        } finally {
            try {
                //get responsePara and save to db
                setResJo = JSONObject.parseObject(strategyPara.response);
                activeResJo = JSONObject.parseObject(activatePara.response);
                aCase.setResponse(setResJo + "\n\n" + activeResJo);

                deleteStrategy(strategyId);

                qaDbUtil.saveToCaseTable(aCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String sendRequestWithHeader(String URL, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(URL, json, header);
        return executor.getResponse();
    }

    private String sendRequestOnly(String URL, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL, json);
        return executor.getResponse();
    }

    private void checkCode(String response, int expect,String message) throws Exception {
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, message);
    }

    private boolean checkIsSuccess(String activeResponse, StrategyPara strategyPara, String expectResult) throws Exception {
        String msg;
        String strategyId = null;
        JSONArray matchList = JSON.parseObject(activeResponse).getJSONObject("data").getJSONArray("match_list");
        try {
            for (int i = 0; i < matchList.size(); i++) {
                JSONObject matchSingle = matchList.getJSONObject(i);
                String triggerCustomerId = matchSingle.getString("touch_trigger_customer_id");

                strategyId = strategyPara.strategyId;
                Assert.assertEquals(triggerCustomerId, strategyPara.customerId, "expect touch_trigger_customer_id == " + triggerCustomerId);

                JSONObject matchStrategy = matchSingle.getJSONObject("match_people_found_strategy");
                JSONArray matchTasks = matchStrategy.getJSONArray("tasks");

                if ("false".equals(expectResult)) {
                    Assert.assertEquals(matchTasks.size(), 0, "");
                } else if ("true".equals(expectResult)) {
                    Assert.assertNotEquals(matchTasks.size(), 0, "have not matched any strategy,but ecpect to match.");

                    JSONObject taskSingle = null;
                    String taskRuleId;
                    JSONArray tasks = matchStrategy.getJSONArray("tasks");
                    for (int j = 0; j < tasks.size(); j++) {
                        taskSingle = tasks.getJSONObject(j);
                        taskRuleId = taskSingle.getString("touch_trigger_rule_id");

                        Assert.assertEquals(taskRuleId, strategyId, "");

                        JSONObject touchEndPoint = taskSingle.getJSONObject("touch_endpoint");

                        String adIdRes = touchEndPoint.getString("ad_id");

                        Assert.assertEquals(adIdRes, strategyPara.adId, "");

                        String adSpaceIdRes = touchEndPoint.getString("ad_space_id");

                        Assert.assertEquals(adSpaceIdRes, strategyPara.adSpaceId, "adSpaceId is wrong！");

                        String endPointType = touchEndPoint.getString("endpoint_type");

                        Assert.assertEquals(endPointType, strategyPara.endPointType, "endPointType is wrong！");

                        String content = taskSingle.getString("content");

                        Assert.assertEquals(content, strategyPara.desc, "desc is wrong！");

                        JSONArray endpointIds = touchEndPoint.getJSONArray("endpoint_ids");
                        String[] endpointIdsArr = new String[3];
                        for (int k = 0; k < endpointIds.size(); k++) {
                            String id = endpointIds.getString(k);
                            endpointIdsArr[k] = id;
                        }
                        Assert.assertEqualsNoOrder(endpointIdsArr, strategyPara.endpointIds);

                        Arrays.sort(endpointIdsArr);
                        JSONObject touchNumbers = touchEndPoint.getJSONObject("touch_members");
                        String number1 = touchNumbers.getString(yuID);
                        Assert.assertEquals(number1, yuNumber);

                        String number2 = touchNumbers.getString(maKunId);
                        Assert.assertEquals(number2, maKunNumber);

                        String number3 = touchNumbers.getString(zhiDongId);
                        Assert.assertEquals(number3, zhiDongNumber);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteStrategy(strategyId);
        }

        return true;
    }

    @DataProvider(name = "CUSTOMERID_==")
    public static Object[][] customerIdEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId---==", "customerId", "==", "001", "42", "！@#￥%……&*（）——+", "false"},
                new Object[]{"customerId---==", "customerId", "==", "001", "42", "001", "true"},
                new Object[]{"customerId---==", "customerId", "==", "001", "42", "000", "false"},
                new Object[]{"customerId---==", "customerId", "==", "001", "42", "002", "false"},
                new Object[]{"customerId---==", "customerId", "==", "001", "42", "003", "false"},
                new Object[]{"customerId---==", "customerId", "==", "001", "42", "004", "false"},

        };
    }

    @DataProvider(name = "CUSTOMERID_!=")
    public static Object[][] customerIdNotEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId---!=", "customerId", "!=", "001", "43", "001", "false"},
                new Object[]{"customerId---!=", "customerId", "!=", "001", "43", "000", "true"},
                new Object[]{"customerId---!=", "customerId", "!=", "001", "43", "002", "true"},
                new Object[]{"customerId---!=", "customerId", "!=", "001", "43", "003", "true"},
                new Object[]{"customerId---!=", "customerId", "!=", "001", "43", "004", "true"},

        };
    }

    @DataProvider(name = "CUSTOMERID_IN")
    public static Object[][] customerIdIn() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId---in", "customerId", "in", "[\"001\",\"002\"]", "44", "001", "true"},
                new Object[]{"customerId---in", "customerId", "in", "[\"001\",\"002\"]", "44", "002", "true"},
                new Object[]{"customerId---in", "customerId", "in", "[\"001\",\"002\"]", "44", "000", "false"},
                new Object[]{"customerId---in", "customerId", "in", "[\"001\",\"002\"]", "44", "432432", "false"},
                new Object[]{"customerId---in", "customerId", "in", "[\"001\",\"002\"]", "44", "534", "false"},
                new Object[]{"customerId---in", "customerId", "in", "[\"001\",\"002\"]", "44", "ger534", "false"},

        };
    }

    @DataProvider(name = "CUSTOMERID_NOT_IN")
    public static Object[][] customerIdNotIn() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"customerId---not in", "customerId", "not in", "[\"001\",\"002\"]", "45", "001", "false"},
                new Object[]{"customerId---not in", "customerId", "not in", "[\"001\",\"002\"]", "45", "002", "false"},
                new Object[]{"customerId---not in", "customerId", "not in", "[\"001\",\"002\"]", "45", "fdse423af", "true"},
                new Object[]{"customerId---not in", "customerId", "not in", "[\"001\",\"002\"]", "45", "003", "true"},
                new Object[]{"customerId---not in", "customerId", "not in", "[\"001\",\"002\"]", "45", "fds96-", "true"},

        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_<")
    public static Object[][] discoveryTimesLess() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "-1", "true"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "0", "true"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "1", "true"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "4", "true"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "5", "false"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "6", "false"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "10", "false"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "50", "false"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "100", "false"},
                new Object[]{"discoveryTimes---<", "discoveryTimes", "<", "5", "46", "1000", "false"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_<=")
    public static Object[][] discoveryTimesLessOrEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "-1", "true"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "0", "true"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "1", "true"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "4", "true"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "5", "true"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "6", "false"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "10", "false"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "500", "false"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "100", "false"},
                new Object[]{"discoveryTimes---<=", "discoveryTimes", "<=", "5", "47", "1000", "false"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_==")
    public static Object[][] discoveryTimesEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "-1", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "0", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "1", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "4", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "5", "true"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "6", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "10", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "50", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "100", "false"},
                new Object[]{"discoveryTimes---==", "discoveryTimes", "==", "5", "48", "1000", "false"},

        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_!=")
    public static Object[][] discoveryTimesNotEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "-1", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "0", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "1", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "4", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "5", "false"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "6", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "10", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "50", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "100", "true"},
                new Object[]{"discoveryTimes---!=", "discoveryTimes", "!=", "5", "49", "1000", "true"},
        };
    }


    @DataProvider(name = "DISCOVERY_TIMES_>=")
    public static Object[][] discoveryTimesMoreOrEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "-1", "false"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "0", "false"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "1", "false"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "4", "false"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "5", "true"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "6", "true"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "10", "true"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "50", "true"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "100", "true"},
                new Object[]{"discoveryTimes--->=", "discoveryTimes", ">=", "5", "50", "1000", "true"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_>")
    public static Object[][] discoveryTimesMore() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "-1", "false"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "0", "false"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "1", "false"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "4", "false"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "5", "false"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "6", "true"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "10", "true"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "50", "true"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "100", "true"},
                new Object[]{"discoveryTimes--->", "discoveryTimes", ">", "5", "51", "1000", "true"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_IN")
    public static Object[][] discoveryTimesIn() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "-1", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "0", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "1", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "4", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "5", "true"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "6", "true"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "7", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "10", "true"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "11", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "50", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "100", "false"},
                new Object[]{"discoveryTimes---in", "discoveryTimes", "in", "[\"5\",\"6\",\"10\"]", "52", "1000", "false"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_NOT_IN")
    public static Object[][] discoveryTimesNotIn() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "-1", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "0", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "1", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "4", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "5", "false"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "6", "false"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "7", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "10", "false"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "11", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "50", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "100", "true"},
                new Object[]{"discoveryTimes---not in", "discoveryTimes", "not in", "[\"5\",\"6\",\"10\"]", "53", "1000", "true"},
        };
    }


    @DataProvider(name = "DISCOVERY_TIMES_BETWEEN")
    public static Object[][] discoveryTimesBetween() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "-1", "flase"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "0", "flase"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "4", "flase"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "5", "true"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "6", "true"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "7", "true"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "10", "flase"},
                new Object[]{"discoveryTimes---between", "discoveryTimes", "between", "[\"5\",\"7\"]", "54", "100", "flase"},
        };
    }

    @DataProvider(name = "AGE_<")
    public static Object[][] ageLess() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "-1", "true"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "0", "true"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "1", "true"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "22", "true"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "23", "false"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "24", "false"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "50", "false"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "100", "false"},
                new Object[]{"age---<", "customerProperty=>age", "<", "23", "55", "100", "false"},
        };
    }

    @DataProvider(name = "AGE_<=")
    public static Object[][] ageLessOrEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "-1", "true"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "0", "true"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "1", "true"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "22", "true"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "23", "true"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "24", "false"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "50", "false"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "100", "false"},
                new Object[]{"age---<=", "customerProperty=>age", "<=", "23", "56", "1000", "false"},
        };
    }

    @DataProvider(name = "AGE_==")
    public static Object[][] ageEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "-1", "false"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "0", "false"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "1", "false"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "22", "false"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "23", "true"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "24", "false"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "50", "false"},
                new Object[]{"age---==", "customerProperty=>age", "==", "23", "57", "100", "false"},
        };
    }

    @DataProvider(name = "AGE_!=")
    public static Object[][] ageNotEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "-1", "true"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "0", "true"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "1", "true"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "22", "true"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "23", "false"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "24", "true"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "50", "true"},
                new Object[]{"age---!=", "customerProperty=>age", "!=", "23", "58", "100", "true"},
        };
    }

    @DataProvider(name = "AGE_>=")
    public static Object[][] ageNotMoreOrEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "-1", "false"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "0", "false"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "1", "false"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "22", "false"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "23", "true"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "24", "true"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "50", "true"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "100", "true"},
                new Object[]{"age--->=", "customerProperty=>age", ">=", "23", "59", "1000", "true"},
        };
    }

    @DataProvider(name = "AGE_>")
    public static Object[][] ageNotMore() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "-1", "false"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "0", "false"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "1", "false"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "22", "false"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "23", "false"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "24", "true"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "50", "true"},
                new Object[]{"age--->", "customerProperty=>age", ">", "23", "60", "100", "true"},
        };
    }

    @DataProvider(name = "AGE_IN")
    public static Object[][] ageIn() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "-1", "false"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "0", "false"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "1", "false"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "23", "true"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "24", "false"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "25", "true"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "50", "true"},
                new Object[]{"age---in", "customerProperty=>age", "in", "[\"23\",\"25\",\"50\"]", "61", "100", "false"},
        };
    }

    @DataProvider(name = "AGE_NOT_IN")
    public static Object[][] ageNotIn() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "-1", "true"},
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "0", "true"},
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "1", "true"},
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "23", "false"},
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "24", "false"},
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "50", "false"},
                new Object[]{"age---not in", "customerProperty=>age", "not in", "[\"23\",\"24\",\"50\"]", "62", "100", "true"},
        };
    }


    @DataProvider(name = "AGE_BETWEEN")
    public static Object[][] ageBetween() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"age---between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","-1","false"},
                new Object[]{"age---between","customerProperty=>age","between","[\"23\",\"27\",\"50\"]","63","22","false"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "23", "true"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "24", "true"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "25", "true"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "26", "true"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "27", "true"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "28", "false"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "50", "false"},
                new Object[]{"age---between", "customerProperty=>age", "between", "[\"23\",\"27\",\"50\"]", "63", "100", "false"},

        };
    }

    @DataProvider(name = "IS_MALE_==")
    public static Object[][] isMaleEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"isMale---==", "customerProperty=>isMale", "==", "true", "64", "true", "true"},
                new Object[]{"isMale---==", "customerProperty=>isMale", "==", "true", "64", "false", "false"},
                new Object[]{"isMale---==", "customerProperty=>isMale", "==", "true", "64", "", "false"},

        };
    }

    @DataProvider(name = "IS_MALE_!=")
    public static Object[][] isMaleNotEqual() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"isMale---!=", "customerProperty=>isMale", "!=", "true", "65", "true", "false"},
                new Object[]{"isMale---!=", "customerProperty=>isMale", "!=", "true", "65", "false", "true"},
                new Object[]{"isMale---!=", "customerProperty=>isMale", "!=", "true", "65", "", "false"},

        };
    }

    @DataProvider(name = "BAD_DISCOVERY_TIMES")
    public static Object[][] discoveryTimesLessBad() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"badDiscoveryTimes", "discoveryTimes", "<", "5", "46", "!@#$%^&*()_+"}
        };
    }

    @DataProvider(name = "BAD_AGE")
    public static Object[][] ageBad() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"badAge", "customerProperty=>age", "<", "23", "55", "!@#$%^&*()_"}
        };
    }

    @DataProvider(name = "BAD_IS_MALE")
    public static Object[][] isMaleBad() {
        //desc, testPara, testOp, value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"badIsMale", "customerProperty=>isMale", "!=", "true", "65", "！！@#￥%……&*（）——+"},
        };
    }

    @BeforeSuite
    public void initial() throws Exception {
        qaDbUtil.openConnection();
        clearStrategy();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}

class StrategyPara {
    String desc, testPara, testOp, value, adId, adSpaceId, endPointType, strategyId, customerId;
    String[] endpointIds;
    String[] touch_members;
    String requestPara;
    String response;
}

class ActivatePara {
    String requestPara, response;
}



