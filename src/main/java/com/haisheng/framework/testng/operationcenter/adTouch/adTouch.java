package com.haisheng.framework.testng.operationcenter.adTouch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.DateTimeUtil;
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

    private String setStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/insert/people_found";//5.2.2.4
    private String activateStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/process/people_found";//5.2.1
    private String deleteStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/delete/V2";//5.2.4
    private String clearStrategyURL = "http://39.97.20.113:7001/ads/touch/strategy/rule_field/clear/people_found";//文档上没有

    private String shopId = "8";
    private String grpName = "55";

    private static String customerId = "021c880a-8454-451e-858f-45a6625181ca";
    private static String memberId = "TestingFaces1998";
    private static String nickName = "nick_name";
    private static String fullName = "full_name";
    private static String channel = "WEB";

    private String discoveryTime = "0";
    private long startTime = System.currentTimeMillis(); // 2019-06-10 09:39:31
    private long endTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000; // 2020-06-10 09:39:00
    private String age = "0";
    private String isMale = "false";
    private String deviceId = "152";

    private String endpointType = "TOUCH_CUSTOMER";
    private String endpointGroups = "55";
    private String endpointCrowdIds = "40";
    private String adSpaceId = "30";
    private String yuID = "b7167b646ce82464e4c55d643bc3900f";
    private String maKunId = "95cba19646d9d2a3fa5fcfc36a90d344";

    private static String memberMa = "95cba19646d9d2a3fa5fcfc36a90d344";
    private static String memberYu = "b7167b646ce82464e4c55d643bc3900f";
    private static String memberLiao = "64772a18d139fe2121070b6488ade637";

    private static String channelWechat = "WECHAT";
    private static String channelWechatApp = "WECHAT_APPLET";
    private static String channelWeb = "WEB";
    private static String channelAndroid = "ANDROID";
    private static String channelIos = "IOS";
    private static String channelServer = "SERVER";

    private String yuNumber = "18210113587";
    private String maKunNumber = "13581630214";

    private String response = "";

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_OPEN_PLATFORM_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_AD_SERVICE;

    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/ad_test/buildWithParameters?case_name=";

    boolean IS_DEBUG = false;

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public StrategyPara setStrategyTemp(String desc, String template, Case aCase, int step) throws Exception {

        //用152测试可以满足两个要测试的模板组合
        logger.info("\n");
        logger.info("set Strategy test pri--------------------------------------------------");
        HashMap<String, String> header = new HashMap();
        header.put("creator", "%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId", "55");

        String adId = "106";

        StrategyPara strategyPara;

        String json =
                "{" +
                        "    \"shop_id\":\"8\"," +
                        "    \"action_list\":[" +
                        "        [" +
                        "            {" +
                        "                \"parameter\":\"shopId\"," +
                        "                \"value\":\"8\"" +
                        "            }," +
                        "            {" +
                        "                \"parameter\":\"endpointIds\"," +
                        "                \"value\":[" +
                        "                    \"" + yuID + "\"," +
                        "                    \"" + maKunId + "\"" +
                        "                ]" +
                        "            }," +
                        "            {" +
                        "                \"parameter\":\"endpointType\"," +
                        "                \"value\":\"TOUCH_CUSTOMER\"" +
                        "            }," +
                        "            {" +
                        "                \"parameter\":\"adId\"," +
                        "                \"value\":\"" + adId + "\"" +
                        "            }," +
                        "            {" +
                        "                \"parameter\":\"adSpaceId\"," +
                        "                \"value\":\"" + adSpaceId + "\"" +
                        "            }" +
                        "        ]" +
                        "    ]," +
                        "    \"trigger_conditions\":[" +
                        "        {" +
                        "            \"parameter\":\"customerPosition=>deviceId\"," +
                        "            \"value\":\"152\"," +
                        "            \"operator\":\"==\"," +
                        "            \"desc\":\"" + desc + "\"" +
                        "        }," +
                        "{" +
                        "            \"parameter\":\"customerPosition=>deviceId\"," +
                        "            \"value\":\"150\"," +
                        "            \"operator\":\"!=\"," +
                        "            \"desc\":\"" + desc + "\"" +
                        "        }," +
                        "{" +
                        "            \"parameter\":\"customerPosition=>deviceId\"," +
                        "            \"value\":[150,152]," +
                        "            \"operator\":\"in\"," +
                        "            \"desc\":\"" + desc + "\"" +
                        "        }," +
                        "{" +
                        "            \"parameter\":\"customerPosition=>deviceId\"," +
                        "            \"value\":[153,154]," +
                        "            \"operator\":\"not in\"," +
                        "            \"desc\":\"" + desc + "\"" +
                        "        }" +
                        "    ]," +
                        "\"template\": \"" + template + "\"," +
                        "    \"duration_time\":{" +
                        "        \"start_time\":\"" + startTime + "\"," +
                        "        \"end_time\":\"" + endTime + "\"" +
                        "    }," +
                        "    \"nodeId\":\"55\"," +
                        "    \"brand_id\":\"55\"" +
                        "}";

        String strategyId = "";

        response = sendRequestWithHeader(setStrategyURL, json, header);

        sendResAndReqIdToDb(response, aCase, step);

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
        strategyPara.adId = adId;
        strategyPara.endPointType = endpointType;
        strategyPara.adSpaceId = adSpaceId;
        strategyPara.strategyId = strategyId;
        strategyPara.endpointIds = new String[]{yuID, maKunId};
        strategyPara.touch_members = new String[]{yuNumber, maKunNumber};

        return strategyPara;
    }

    public StrategyPara setStrategyTestPri(String desc, String testPara, String testOp, String value, String adId, String isId, String isGrp, String isCrowd, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("set Strategy test pri--------------------------------------------------");
        HashMap<String, String> header = new HashMap();
        header.put("creator", "%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId", "55");

        StrategyPara strategyPara;

        StringBuffer json = new StringBuffer(
                "{" +
                        "    \"shop_id\": \"" + shopId + "\"," +
                        "    \"nodeId\":\"55\"," +
                        "    \"brand_id\": \"55\"," +
                        "    \"trigger_conditions\": [" +
                        "    {" +
                        "            \"desc\": \"" + desc + "\"," +
                        "            \"parameter\": \"" + testPara + "\"," +
                        "            \"operator\": \"" + testOp + "\"," +
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
                        "            },"
        );

        if ("true".equals(isId)) {
            json.append("            {" +
                    "                \"parameter\": \"endpointIds\"," +
                    "                \"value\": [" +
                    "                    \"" + yuID + "\"," +
                    "                    \"" + maKunId + "\"" +
                    "                ]" +
                    "            },");
        }

        if ("true".equals(isGrp)) {
            json.append(
                    "            {" +
                            "                \"parameter\": \"endpointGroups\"," +
                            "                \"value\": [" +
                            "                    \"55\"" +
                            "                ]" +
                            "            },"
            );
        }

        if ("true".equals(isCrowd)) {
            json.append(
                    "            {" +
                            "                \"parameter\": \"endpointCrowdIds\"," +
                            "                \"value\": [" +
                            "                    \"40\"" +
                            "                ]" +
                            "            },");
        }

        json.append(
                "            {" +
                        "                \"parameter\": \"endpointType\"," +
                        "                \"value\": \"" + endpointType + "\"" +
                        "            }," +
                        "            {" +
                        "                \"parameter\": \"adId\"," +
                        "                \"value\": \"" + "105" + "\"" +
                        "            }," +
                        "            {" +
                        "                \"parameter\": \"adSpaceId\"," +
                        "                \"value\": \"30\"" +
                        "            }" +
                        "        ]" +
                        "    ]" +
                        "}");

        try {
            String strategyId = "";

            response = sendRequestWithHeader(setStrategyURL, json.toString(), header);

            sendResAndReqIdToDb(response, aCase, step);

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

            strategyPara.requestPara = json.toString();
            strategyPara.response = response;
            strategyPara.desc = desc;
            strategyPara.testPara = testPara;
            strategyPara.testOp = testOp;
            strategyPara.adId = adId;
            strategyPara.endPointType = endpointType;
            strategyPara.value = value;
            strategyPara.adSpaceId = adSpaceId;
            strategyPara.strategyId = strategyId;
            strategyPara.endpointIds = new String[]{yuID, maKunId};
            strategyPara.touch_members = new String[]{yuNumber, maKunNumber};

        } catch (Exception e) {
            throw e;
        }
        return strategyPara;
    }

    public StrategyPara setStrategyCom(String desc, String testPara, String testOp, String value, String adId,
                                       long startTime, long endTime, boolean isBetween, Case aCase, int step) throws Exception {

        StrategyPara strategyPara = new StrategyPara();

        setStrategyPublic(strategyPara, desc, testPara, testOp, value, adId, startTime, endTime, isBetween, aCase, step);

        checkSetStrategyCodeSuccess(strategyPara);

        return strategyPara;
    }

    public void setStrategyPublic(StrategyPara strategyPara, String desc, String testPara, String testOp, String value, String adId,
                                  long startTime, long endTime, boolean isBetween, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("set Strategy （=/!=）--------------------------------------------------");
        HashMap<String, String> header = new HashMap();
        header.put("creator", "%7B%22id%22%3A%20%22uid_15cdb5eb%22%2C%20%22name%22%3A%20%22%E9%A9%AC%E9%94%9F%22%7D");
        header.put("nodeId", "55");

        StringBuffer json = new StringBuffer("{" +
                "    \"shop_id\": \"" + shopId + "\"," +
                "    \"nodeId\":\"55\"," +
                "    \"brand_id\": \"55\"," +
                "    \"trigger_conditions\": [" +
                "    {" +
                "            \"desc\": \"" + desc + "\"," +
                "            \"parameter\": \"" + testPara + "\"," +
                "            \"operator\": \"" + testOp + "\",");

        if (isBetween) {
            json.append("            \"value\": " + value + "");
        } else {
            json.append("            \"value\": \"" + value + "\"");
        }

        json.append("        }" +
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
                "                    \"" + maKunId + "\"" +
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
                "}");

        try {

            response = sendRequestWithHeader(setStrategyURL, json.toString(), header);

            sendResAndReqIdToDb(response, aCase, step);

            strategyPara.requestPara = json.toString();
            strategyPara.response = response;
            strategyPara.desc = desc;
            strategyPara.testPara = testPara;
            strategyPara.testOp = testOp;
            strategyPara.adId = adId;
            strategyPara.endPointType = endpointType;
            strategyPara.value = value;
            strategyPara.adSpaceId = adSpaceId;
            strategyPara.endpointIds = new String[]{yuID, maKunId};
            strategyPara.touch_members = new String[]{yuNumber, maKunNumber};

        } catch (Exception e) {
            throw e;
        }
    }

    public void checkSetStrategyCodeSuccess(StrategyPara strategyPara) {
        JSONObject resJo = JSON.parseObject(strategyPara.response);
        String message = resJo.getString("message");
        int code = resJo.getInteger("code");
        String strategyId = "";

        if (1000 == code) {
            strategyId = resJo.getJSONObject("data").getString("id");
        } else if (4016 == code) {
            String resMessage = resJo.getString("message");
            strategyId = resMessage.substring(resMessage.indexOf("[") + 1, resMessage.indexOf("]"));
        } else {
            failReason = "set strategy failed!" + message;
            Assert.fail(failReason);
        }

        strategyPara.strategyId = strategyId;
    }

    public String setStrategyTestDurationTime(String desc, String testPara, String testOp, String value, String adId,
                                              long startTime, long endTime, boolean isBetween, int expectCode, Case aCase, int step) throws Exception {
        StrategyPara strategyPara = new StrategyPara();

        setStrategyPublic(strategyPara, desc, testPara, testOp, value, adId, startTime, endTime, isBetween, aCase, step);

        checkCode(strategyPara.response, expectCode, "code is wrong! expect code : " + expectCode);

        String strategyId = "";

        if (1000 == expectCode) {
            JSONObject resJo = JSON.parseObject(strategyPara.response);
            strategyId = resJo.getJSONObject("data").getString("id");
            strategyPara.strategyId = strategyId;

            return strategyId;
        }

        return strategyPara.strategyId;
    }

    public String genActivateStrategyPara(String testParaKey, String testParaValue) throws Exception {

        String json = "{" +
                "    \"msg_time\":\"1559564036184\"," +
                "    \"shop_id\":\"" + shopId + "\"," +
                "    \"brand_id\":\"55\"," +
                "    \"person\":[" +
                "        {" +
                "            \"customer_id\":\"" + customerId + "\", " +
                "            \"member_id\":\"" + memberId + "\", " +
                "            \"nick_name\":\"" + nickName + "\", " +
                "            \"full_name\":\"" + fullName + "\", " +
                "            \"group_name\":\"" + grpName + "\", " +
                "            \"channel\":\"" + channel + "\", " +
                "            \"discovery_times\": \"" + discoveryTime + "\"," +
                "            \"customer_type\":\"SPECIAL\", " +
                "            \"customer_property\":{" +
                "                \"age\":\"" + age + "\"," +
                "                \"is_male\":\"" + isMale + "\"," +
                "                \"quality_level\":\"GOOD\" " +
                "            }," +
                "            \"customer_position\":{" +
                "                \"device_id\":\"" + deviceId + "\", " +
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

        json = replaceKey(json, testParaKey, testParaValue);

        return json;
    }

    public String replaceKey(String json, String key, String value) throws Exception {
        JSONObject JSonJo = JSON.parseObject(json);
        JSONObject personJo = JSonJo.getJSONArray("person").getJSONObject(0);
        if (key.contains("=>")) {
            String parentKey = key.substring(0, key.indexOf("="));
            parentKey = changeUpperToUnderLine(parentKey);
            String childKey = key.substring(key.indexOf(">") + 1);
            childKey = changeUpperToUnderLine(childKey);

            JSONObject parentJo = personJo.getJSONObject(parentKey);
            if (parentJo.containsKey(childKey)) {
                parentJo.put(childKey, value);
//                parentJo.put("\"" + childKey + "\"", value);
            } else {
                throw new Exception("key is not exist---" + childKey);
            }

        } else {
            key = changeUpperToUnderLine(key);
            personJo.put(key, value);
        }

        return JSON.toJSONString(JSonJo);
    }

    public String changeUpperToUnderLine(String key) {
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
//            或Character.toString(char)
            if ("QWERTYUIOPLKJHGFDSAZXCVBNM".contains(String.valueOf(c))) {
                key = key.replace(key.valueOf(c), "_" + key.valueOf(c).toLowerCase());
            }
        }

        return key;
    }


    public ActivatePara activateStrategy(String testParaKey, String testParaValue, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("activateStrategy------------------------------------------------------------------");

        ActivatePara activatePara = new ActivatePara();

        String json = genActivateStrategyPara(testParaKey, testParaValue);
        try {
            response = sendRequestOnly(activateStrategyURL, json);

            sendResAndReqIdToDb(response, aCase, step);

            activatePara.requestPara = json;
            activatePara.response = response;

        } catch (Exception e) {
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
            logger.info(failReason);
            Assert.assertTrue(false);
            throw e;
        }
    }

    //    @Test(dataProvider = "GEN_10_STRATEGY")
    public void gen10Strategy(String op, String value, String adId) throws Exception {

        Case aCase = new Case();
        int step = 0;
        String desc = "desc";
        String testKey = "customerProperty=>age";

        aCase.setResult("dsdsad");

        setStrategyCom(desc, testKey, op, value, adId, startTime, endTime, false, aCase, step);

    }

    @Test(dataProvider = "CUSTOMERID_==")
    public void customerIdEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerId---==";
        String testKey = "customerId";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testValue;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CUSTOMERID_!=")
    public void customerIdNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerId---!=";
        String testKey = "customerId";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testValue;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CUSTOMERID_IN")
    public void customerIdIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerId---in";
        String testKey = "customerId";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testValue;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CUSTOMERID_NOT_IN")
    public void customerIdNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerId---not in";
        String testKey = "customerId";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = testValue;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "MEMBERID_==")
    public void memberIdEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "memberId---==";
        String testKey = "memberId";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "MEMBERID_!=")
    public void memberIdNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "memberId---!=";
        String testKey = "memberId";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "MEMBERID_IN")
    public void memberIdIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "memberId---in";
        String testKey = "memberId";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "MEMBERID_NOT_IN")
    public void memberIdNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "memberId---not in";
        String testKey = "memberId";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "FULL_NAME_==")
    public void fullNameEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "fullName---==";
        String testKey = "fullName";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "FULL_NAME_!=")
    public void fullNameNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "fullName---!=";
        String testKey = "fullName";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "FULL_NAME_IN")
    public void fullNameIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "fullName---in";
        String testKey = "fullName";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "FULL_NAME_NOT_IN")
    public void fullNameNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "fullName---not in";
        String testKey = "fullName";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "NICK_NAME_==")
    public void nickNameEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "nickName---==";
        String testKey = "nickName";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "NICK_NAME_!=")
    public void nickNameNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "nickName---!=";
        String testKey = "nickName";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "NICK_NAME_IN")
    public void nickNameIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "nickName---in";
        String testKey = "nickName";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "NICK_NAME_NOT_IN")
    public void nickNameNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "nickName---not in";
        String testKey = "nickName";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CHANNEL_==")
    public void channelEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "channel---==";
        String testKey = "channel";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CHANNEL_!=")
    public void channelNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "channel---!=";
        String testKey = "channel";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CHANNEL_IN")
    public void channelIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "channel---in";
        String testKey = "channel";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CHANNEL_NOT_IN")
    public void channelNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "channel---not in";
        String testKey = "channel";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_<")
    public void discoveryTimesLess(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---<";
        String testKey = "discoveryTimes";
        String testOp = "<";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_<=")
    public void discoveryTimesLessOrEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---<=";
        String testKey = "discoveryTimes";
        String testOp = "<=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_==")
    public void discoveryTimesEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---==";
        String testKey = "discoveryTimes";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_!=")
    public void discoveryTimesNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---!=";
        String testKey = "discoveryTimes";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_>=")
    public void discoveryTimesMoreOrEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes--->=";
        String testKey = "discoveryTimes";
        String testOp = ">=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_>")
    public void discoveryTimesMore(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes--->";
        String testKey = "discoveryTimes";
        String testOp = ">";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_IN")
    public void discoveryTimesIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---in";
        String testKey = "discoveryTimes";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_NOT_IN")
    public void discoveryTimesNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---not in";
        String testKey = "discoveryTimes";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DISCOVERY_TIMES_BETWEEN")
    public void discoveryTimesBetween(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "discoveryTimes---between";
        String testKey = "discoveryTimes";
        String testOp = "between";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "BAD_DISCOVERY_TIMES")
    public void badDiscoveryTimes(String value, String adId, String testValue) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---bad discoveryTimes";
        String testKey = "discoveryTimes";
        String testOp = "<";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            strategyPara.customerId = customerId;

            checkCode(activatePara.response, 4017, "bad age return wrong code");

            String message = JSON.parseObject(response).getString("message");
            if (!message.contains(testValue)) {
                failReason = "message do not contain testValue" + message;
            }

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_<")
    public void ageLess(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---<";
        String testKey = "customerProperty=>age";
        String testOp = "<";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_<=")
    public void ageLessOrEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---<=";
        String testKey = "customerProperty=>age";
        String testOp = "<=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_==")
    public void ageEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---==";
        String testKey = "customerProperty=>age";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_>")
    public void ageMore(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age--->";
        String testKey = "customerProperty=>age";
        String testOp = ">";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_>=")
    public void ageMoreOrEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age--->=";
        String testKey = "customerProperty=>age";
        String testOp = ">=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_!=")
    public void ageNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---!=";
        String testKey = "customerProperty=>age";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_IN")
    public void ageIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---in";
        String testKey = "customerProperty=>age";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_NOT_IN")
    public void ageNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---not in";
        String testKey = "customerProperty=>age";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_BETWEEN")
    public void ageBetween(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---between";
        String testKey = "customerProperty=>age";
        String testOp = "between";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "BAD_AGE")
    public void badAge(String value, String adId, String testValue) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---bad age";
        String testKey = "customerProperty=>age";
        String testOp = "<";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            strategyPara.customerId = customerId;

            checkCode(activatePara.response, 4017, "bad age return wrong code");

            String message = JSON.parseObject(response).getString("message");
            if (!message.contains(testValue)) {
                failReason = "message do not contain testValue" + message;
            }

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "IS_MALE_==")
    public void isMaleEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>isMale---==";
        String testKey = "customerProperty=>isMale";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "IS_MALE_!=")
    public void isMaleNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>isMale---!=";
        String testKey = "customerProperty=>isMale";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "BAD_IS_MALE")
    public void badIsMale(String value, String adId, String testValue) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerProperty=>age---bad isMale";
        String testKey = "customerProperty=>isMale";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            strategyPara.customerId = customerId;

            checkCode(activatePara.response, 4017, "bad age return wrong code");

            String message = JSON.parseObject(response).getString("message");
            if (!message.contains(testValue)) {
                failReason = "message do not contain testValue" + message;
            }

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DEVICEID_==")
    public void deviceIdEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerPosition=>deviceId---==";
        String testKey = "customerPosition=>deviceId";
        String testOp = "==";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DEVICEID_!=")
    public void deviceIdNotEqual(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerPosition=>deviceId---!=";
        String testKey = "customerPosition=>deviceId";
        String testOp = "!=";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DEVICEID_IN")
    public void deviceIdIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerPosition=>deviceId---in";
        String testKey = "customerPosition=>deviceId";
        String testOp = "in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DEVICEID_NOT_IN")
    public void deviceIdNotIn(String value, String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + value + "---" + testValue;
        String caseDesc = caseName + value + ", testValue: " + testValue;
        logger.info(caseDesc + "--------------------");

        String desc = "customerPosition=>deviceId---not in";
        String testKey = "customerPosition=>deviceId";
        String testOp = "not in";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, true, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "TEST_PRI")
    public void testPri(String id, String desc, String isId, String isGrp, String isCrowd) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + id;
        String caseDesc = caseName + desc;
        logger.info(caseDesc + "--------------------");

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        String testPara = "customerPosition=>deviceId";
        String testOp = "==";
        String value = "152";
        String adId = "105";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyTestPri(desc, testPara, testOp, value, adId, isId, isGrp, isCrowd, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testPara, value, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkPriIsSuccess(activatePara.response, strategyPara, id);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "TEST_TEMPLATE")
    public void testTemplate(String desc, String template, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + template;
        String caseDesc = caseName + desc;
        logger.info(caseDesc + "--------------------");

        String testPara = "customerPosition=>deviceId";
        String value = "152";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyTemp(desc, template, aCase, step);

            strategyId = strategyPara.strategyId;
            activatePara = activateStrategy(testPara, value, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "AGE_MULTI")
    public void testActivateMultiStrategy(String adId, String testValue, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + testValue;
        String caseDesc = caseName;
        logger.info(caseDesc + "--------------------");

        String desc = "test Activate Multiple Strategy";
        String testKey = "customerProperty=>age";

        String testOp1 = ">";
        String testOp2 = "<";

        String value1 = "20", value2 = "21", value3 = "30", value4 = "31";

        StrategyPara strategyPara1, strategyPara2, strategyPara3, strategyPara4;
        ActivatePara activatePara;
        String strategyId1 = "";
        String strategyId2 = "";
        String strategyId3 = "";
        String strategyId4 = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara1 = setStrategyCom(desc, testKey, testOp1, value1, adId, startTime, endTime, false, aCase, step);
            strategyPara2 = setStrategyCom(desc, testKey, testOp1, value2, adId, startTime, endTime, false, aCase, step);
            strategyPara3 = setStrategyCom(desc, testKey, testOp2, value3, adId, startTime, endTime, false, aCase, step);
            strategyPara4 = setStrategyCom(desc, testKey, testOp2, value4, adId, startTime, endTime, false, aCase, step);

            strategyId1 = strategyPara1.strategyId;
            strategyId2 = strategyPara2.strategyId;
            strategyId3 = strategyPara3.strategyId;
            strategyId4 = strategyPara4.strategyId;

            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara1.customerId = customerId;

            String[] expectIds = expectResult.split(",");
            String[] expectStrategyIds = new String[expectIds.length];
            for (int i = 0; i < expectIds.length; i++) {
                if ("1".equals(expectIds[i])) {
                    expectStrategyIds[i] = strategyId1;
                } else if ("2".equals(expectIds[i])) {
                    expectStrategyIds[i] = strategyId2;
                } else if ("3".equals(expectIds[i])) {
                    expectStrategyIds[i] = strategyId3;
                } else if ("4".equals(expectIds[i])) {
                    expectStrategyIds[i] = strategyId4;
                }
            }

            checkMultiIsSuccess(activatePara.response, expectStrategyIds);

            deleteStrategy(strategyId1);
            deleteStrategy(strategyId2);
            deleteStrategy(strategyId3);
            deleteStrategy(strategyId4);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId1);
            deleteStrategy(strategyId2);
            deleteStrategy(strategyId3);
            deleteStrategy(strategyId4);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DURATION_TIME_VALID")
    public void testDurationTimeValid(String id, long startTime, long endTime, long sleepTime, String expectResult) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + id;
        String caseDesc = caseName + ", startTime: " + dateTimeUtil.timestampToDate(PATTERN, startTime)
                + ",endTime: " + dateTimeUtil.timestampToDate(PATTERN, endTime);
        logger.info(caseDesc + "--------------------");

        String desc = "测试是否duration_time是否生效";
        String testKey = "customerPosition=>deviceId";
        String testOp = "==";
        String value = "152";
        String adId = "107";
        String testValue = "152";

        StrategyPara strategyPara;
        ActivatePara activatePara;
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==1000||code==4016 " + "\n\n" + "code==1000");

            strategyPara = setStrategyCom(desc, testKey, testOp, value, adId, startTime, endTime, false, aCase, step);

            strategyId = strategyPara.strategyId;
            Thread.sleep(sleepTime);
            activatePara = activateStrategy(testKey, testValue, aCase, step);

            int expectCode = StatusCode.SUCCESS;
            checkCode(activatePara.response, expectCode, "activate strategy para!");

            strategyPara.customerId = customerId;

            checkIsSuccess(activatePara.response, strategyPara, expectResult);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "DURATION_TIME_LIMIT")
    public void testDurationTimeLimit(String id, long startTime, long endTime, int expectCode) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + id;
        String caseDesc = caseName + ", startTime: " + dateTimeUtil.timestampToDate(PATTERN, startTime)
                + ",endTime: " + dateTimeUtil.timestampToDate(PATTERN, endTime);
        logger.info(caseDesc + "--------------------");

        String desc = "测试duration_time是否生效";
        String testKey = "customerPosition=>deviceId";
        String testOp = "==";
        String value = "152";
        String adId = "107";
        String strategyId = "";
        failReason = "";

        Case aCase = new Case();
        int step = 0;
        try {
            setBasicPara(aCase, caseName, caseDesc, ciCaseName);
            aCase.setExpect("code==" + expectCode);

            strategyId = setStrategyTestDurationTime(desc, testKey, testOp, value, adId, startTime, endTime, false, expectCode, aCase, step);

            deleteStrategy(strategyId);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
            aCase.setFailReason(failReason);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
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

    private void checkCode(String response, int expect, String message) throws Exception {
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, message);
    }

    private void checkIsSuccess(String activeResponse, StrategyPara strategyPara, String expectResult) throws Exception {
        String strategyId = null;
        JSONArray matchList = JSON.parseObject(activeResponse).getJSONObject("data").getJSONArray("match_list");
        try {
            if ("true".equals(expectResult) && matchList.size() == 0) {
                throw new Exception("have not matched any strategy,but ecpect to match.");
            }

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

                    JSONObject taskSingle;
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
                        String[] endpointIdsArr = new String[2];
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
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
        }
    }

    private void checkDurationIsSuccess(String activeResponse, StrategyPara strategyPara, String expectResult) throws Exception {
        String strategyId = null;
        JSONArray matchList = JSON.parseObject(activeResponse).getJSONObject("data").getJSONArray("match_list");
        try {
            if ("true".equals(expectResult) && matchList.size() == 0) {
                throw new Exception("have not matched any strategy,but ecpect to match.");
            }

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

                    JSONObject taskSingle;
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
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } catch (Exception e) {
            failReason += e.getMessage();
            Assert.fail(failReason + "\n" + e.toString());
        } finally {
            deleteStrategy(strategyId);
        }
    }

    private void checkPriIsSuccess(String activeResponse, StrategyPara strategyPara, String id) throws Exception {

        JSONArray matchList = JSON.parseObject(activeResponse).getJSONObject("data").getJSONArray("match_list");

        if (matchList == null || matchList.size() == 0) {
            throw new Exception("match failed! matchList.size() == 0");
        }

        JSONObject singleMatch = matchList.getJSONObject(0);
        if (singleMatch == null || singleMatch.size() == 0) {
            throw new Exception("match failed! singleMatch.size() == 0");
        }

        JSONObject matchPeople = singleMatch.getJSONObject("match_people_found_strategy");
        if (matchPeople == null || matchPeople.size() == 0) {
            throw new Exception("match failed! matchPeople.size() == 0");
        }

        JSONArray tasks = matchPeople.getJSONArray("tasks");
        if (tasks == null || tasks.size() == 0) {
            throw new Exception("match failed! tasks.size() == 0");
        }

        JSONObject singleTask = tasks.getJSONObject(0);
        if (singleTask == null || singleTask.size() == 0) {
            throw new Exception("match failed! singleTask.size() == 0");
        }

        JSONObject touchEndPoint = singleTask.getJSONObject("touch_endpoint");
        if (touchEndPoint == null || touchEndPoint.size() == 0) {
            throw new Exception("match failed! touchEndPoint.size() == 0");
        }

        JSONObject touchNumber = touchEndPoint.getJSONObject("touch_members");
        JSONArray endPointIds = touchEndPoint.getJSONArray("endpoint_ids");
        JSONArray endPointGrp = touchEndPoint.getJSONArray("endpoint_groups");
        JSONArray endPointCrowd = touchEndPoint.getJSONArray("endpoint_crowd_ids");

        if ("1".equals(id)) {
            if (touchNumber == null || touchNumber.size() == 0) {
                throw new Exception("match failed! touchNumber.size() == 0");
            } else if (touchNumber.size() != 2) {
                throw new Exception("match failed! touchNumber.size() ==" + touchNumber.size() + ", expect 3");
            }

            if (endPointIds == null || endPointIds.size() == 0) {
                throw new Exception("match failed! endPointIds.size() == 0");
            } else if (endPointIds.size() != 2) {
                throw new Exception("match failed! endPointIds.size() ==" + endPointIds.size() + ", expect 3");
            }

        } else if ("2".equals(id)) {

            if (!(touchNumber == null || touchNumber.size() == 0)) {
                throw new Exception("match failed!  touchNumber.size() != 0");
            }

            if (!(endPointIds == null || endPointIds.size() == 0)) {
                throw new Exception("match failed!  endPointIds.size() != 0");
            }

            if (endPointGrp == null || endPointGrp.size() == 0) {
                throw new Exception("match failed! endPointGrp.size()==0");
            }

        } else if ("3".equals(id)) {
            if (!(touchNumber == null || touchNumber.size() == 0)) {
                throw new Exception("match failed! touchNumber.size() != 0");
            }

            if (!(endPointIds == null || endPointIds.size() == 0)) {
                throw new Exception("match failed! endPointIds.size() != 0");
            }

            if (!(endPointGrp == null || endPointGrp.size() == 0)) {
                throw new Exception("match failed! endPointGrp.size()!=0");
            }

            if (endPointCrowd == null || endPointCrowd.size() == 0) {
                throw new Exception("match failed! endPointCrowd.size()==0");
            }
        }
    }

    private void checkMultiIsSuccess(String activeResponse, String[] strategyIds) throws Exception {

        JSONArray matchList = JSON.parseObject(activeResponse).getJSONObject("data").getJSONArray("match_list");
        if (matchList == null || matchList.size() == 0) {
            throw new Exception("matchList.size()==0!");
        }

        JSONObject singleMatch = matchList.getJSONObject(0);
        if (singleMatch == null || singleMatch.size() == 0) {
            throw new Exception("match failed! singleMatch.size() == 0");
        }

        JSONObject matchPeople = singleMatch.getJSONObject("match_people_found_strategy");
        if (matchPeople == null || matchPeople.size() == 0) {
            throw new Exception("match failed! matchPeople.size() == 0");
        }

        JSONArray tasks = matchPeople.getJSONArray("tasks");

        String[] strategyIdsRes = new String[tasks.size()];

        for (int i = 0; i < tasks.size(); i++) {
            JSONObject singleTask = tasks.getJSONObject(i);
            String strategyIdRes = singleTask.getString("touch_trigger_rule_id");
            strategyIdsRes[i] = strategyIdRes;
        }

        Assert.assertEqualsNoOrder(strategyIdsRes, strategyIds, "expect: " + Arrays.toString(strategyIds) +
                ",actual:" + Arrays.toString(strategyIdsRes));
    }

    public void sendResAndReqIdToDb(String response, Case acase, int step) {

        if (response != null && response.trim().length() > 0) {
            String requestId = JSON.parseObject(response).getString("request_id");
            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }

//            将response存入数据库
            String responseBefore = acase.getResponse();
            if (responseBefore != null && responseBefore.trim().length() > 0) {
                acase.setResponse(responseBefore + "(" + step + ") " + JSON.parseObject(response) + "\n\n");
            } else {

                acase.setResponse(JSON.parseObject(response) + "\n\n");
            }
        }
    }

    public void setBasicPara(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
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

    @DataProvider(name = "MEMBERID_==")
    public static Object[][] memberIdEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{memberId, "89", memberId, "true"},
                new Object[]{customerId, "89", memberId, "false"},
        };
    }

    @DataProvider(name = "MEMBERID_!=")
    public static Object[][] memberIdNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{memberId, "90", memberId, "false"},
                new Object[]{customerId, "90", memberId, "true"},//memberMa的人物id
        };
    }

    @DataProvider(name = "MEMBERID_IN")
    public static Object[][] memberIdIn() {
        //value, adId, testValue, expectResult

        return new Object[][]{
                new Object[]{"[\"" + memberMa + "\",\"" + memberId + "\"]", "91", memberId, "true"},
                new Object[]{"[\"" + memberMa + "\",\"" + memberYu + "\"]", "91", memberId, "false"},
        };
    }

    @DataProvider(name = "MEMBERID_NOT_IN")
    public static Object[][] memberIdNotIn() {
        //value, adId, testValue, expectResult

        return new Object[][]{
                new Object[]{"[\"" + memberMa + "\",\"" + memberId + "\"]", "92", memberId, "false"},
                new Object[]{"[\"" + memberMa + "\",\"" + memberYu + "\"]", "92", memberId, "true"},
        };
    }

    @DataProvider(name = "FULL_NAME_==")
    public static Object[][] fullNameEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{nickName, "97", fullName, "false"},
                new Object[]{fullName, "97", fullName, "true"},
        };
    }

    @DataProvider(name = "FULL_NAME_!=")
    public static Object[][] fullNameNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{nickName, "98", fullName, "true"},
                new Object[]{fullName, "98", fullName, "false"},
        };
    }

    @DataProvider(name = "FULL_NAME_IN")
    public static Object[][] fullNameIn() {
        //value, adId, testValue, expectResult

        return new Object[][]{
                new Object[]{"[\"" + fullName + "\"]", "99", fullName, "true"},
                new Object[]{"[\"" + nickName + "\"]", "99", fullName, "false"},
        };
    }

    @DataProvider(name = "FULL_NAME_NOT_IN")
    public static Object[][] fullNameNotIn() {
        //value, adId, testValue, expectResult

        return new Object[][]{
                new Object[]{"[\"" + fullName + "\"]", "100", fullName, "false"},
                new Object[]{"[\"" + nickName + "\"]", "100", fullName, "true"},
        };
    }

    @DataProvider(name = "NICK_NAME_==")
    public static Object[][] nickNameEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{nickName, "101", nickName, "true"},
                new Object[]{fullName, "101", nickName, "false"},
        };
    }

    @DataProvider(name = "NICK_NAME_!=")
    public static Object[][] nickNameNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{nickName, "102", nickName, "false"},
                new Object[]{fullName, "102", nickName, "true"},
        };
    }

    @DataProvider(name = "NICK_NAME_IN")
    public static Object[][] nickNameIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"" + fullName + "\"]", "103", nickName, "false"},
                new Object[]{"[\"" + nickName + "\"]", "103", nickName, "true"},
        };
    }

    @DataProvider(name = "NICK_NAME_NOT_IN")
    public static Object[][] nickNameNotIn() {
        //value, adId, testValue, expectResult
        String value = "[\"" + nickName + "\"]";
        return new Object[][]{
                new Object[]{"[\"" + fullName + "\"]", "104", nickName, "true"},
                new Object[]{"[\"" + nickName + "\"]", "104", nickName, "false"},
        };
    }

    @DataProvider(name = "CHANNEL_==")
    public static Object[][] channelEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{channelWeb, "93", channelWeb, "true"},
                new Object[]{channelWechat, "93", channelWeb, "false"},
        };
    }

    //由于系统是根据传入的这个人的信息去拉取该人的信息，所以在我的代码里人为写死该人的注册渠道是没有用的，要以系统真实的信息为准。
//    所以不要改testValue了，要改value

    @DataProvider(name = "CHANNEL_!=")
    public static Object[][] channelNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{channelWechat, "94", channelWeb, "true"},
                new Object[]{channelWeb, "94", channelWeb, "false"},
        };
    }

    @DataProvider(name = "CHANNEL_IN")
    public static Object[][] channelIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"" + channelWechat + "\",\"" + channelAndroid + "\"]", "95", channelWeb, "false"},
                new Object[]{"[\"" + channelWechat + "\",\"" + channelWeb + "\"]", "95", channelWeb, "true"},
        };
    }

    @DataProvider(name = "CHANNEL_NOT_IN")
    public static Object[][] channelNotIn() {
        //value, adId, testValue, expectResult
        String value = "[\"" + channelWechat + "\",\"" + channelServer + "\"]";
        return new Object[][]{
                new Object[]{"[\"" + channelWechat + "\"]", "96", channelWeb, "true"},
                new Object[]{"[\"" + channelWeb + "\",\"" + channelWechat + "\"]", "96", channelWeb, "false"},
        };
    }

    @DataProvider(name = "DEVICEID_==")
    public static Object[][] deviceIdEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"152", "66", "！@#￥%……&*（）—+", "false"},
                new Object[]{"152", "66", "152", "true"},
                new Object[]{"152", "66", "153", "false"},
                new Object[]{"152", "66", "154", "false"},
                new Object[]{"152", "66", "155", "false"},
                new Object[]{"152", "66", "156", "false"},
        };
    }

    @DataProvider(name = "DEVICEID_!=")
    public static Object[][] deviceIdNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"152", "67", "！@#￥%……&*（）—+", "true"},
                new Object[]{"152", "67", "152", "false"},
                new Object[]{"152", "67", "153", "true"},
                new Object[]{"152", "67", "154", "true"},
                new Object[]{"152", "67", "155", "true"},
                new Object[]{"152", "67", "156", "true"},
        };
    }

    @DataProvider(name = "DEVICEID_IN")
    public static Object[][] deviceIdIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"152\",\"150\"]", "68", "！@#￥%……&*（）—+", "false"},
                new Object[]{"[\"152\",\"150\"]", "68", "152", "true"},
                new Object[]{"[\"152\",\"150\"]", "68", "150", "true"},
                new Object[]{"[\"152\",\"150\"]", "68", "154", "false"},
                new Object[]{"[\"152\",\"150\"]", "68", "155", "false"},
                new Object[]{"[\"152\",\"150\"]", "68", "156", "false"},
        };
    }

    @DataProvider(name = "DEVICEID_NOT_IN")
    public static Object[][] deviceIdNotIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"152\",\"150\"]", "69", "！@#￥%……&*（）—+", "true"},
                new Object[]{"[\"152\",\"150\"]", "69", "152", "false"},
                new Object[]{"[\"152\",\"150\"]", "69", "150", "false"},
                new Object[]{"[\"152\",\"150\"]", "69", "154", "true"},
                new Object[]{"[\"152\",\"150\"]", "69", "155", "true"},
                new Object[]{"[\"152\",\"150\"]", "69", "156", "true"},
        };
    }

    @DataProvider(name = "TEST_PRI")
    public static Object[][] testPri() {
        //desc, isId, isGrp, isCrowd,expectResult
        return new Object[][]{
                new Object[]{"1", "设置endpointIds,endpointGroups,endpointCrowdIds", "true", "true", "true"},
                new Object[]{"2", "设置endpointGroups,endpointCrowdIds", "false", "true", "true"},
                new Object[]{"3", "设置endpointCrowdIds", "false", "false", "true"}
        };
    }

    @DataProvider(name = "TEST_TEMPLATE")
    public static Object[][] testTemplate() {
        //desc, template, expectResult
        return new Object[][]{
                new Object[]{"testTemplate---", "(${0} and ${1})  or (${2} and ${3})", "true"},
                new Object[]{"testTemplate---", "(${0}  or ${1}) and (${2}  or ${3})", "true"},
        };
    }

    @DataProvider(name = "AGE_<")
    public static Object[][] ageLess() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "55", "-1", "true"},
                new Object[]{"23", "55", "0", "true"},
                new Object[]{"23", "55", "1", "true"},
                new Object[]{"23", "55", "22", "true"},
                new Object[]{"23", "55", "23", "false"},
                new Object[]{"23", "55", "24", "false"},
                new Object[]{"23", "55", "50", "false"},
                new Object[]{"23", "55", "100", "false"},
                new Object[]{"23", "55", "100", "false"},
        };
    }

    @DataProvider(name = "AGE_<=")
    public static Object[][] ageLessOrEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "56", "-1", "true"},
                new Object[]{"23", "56", "0", "true"},
                new Object[]{"23", "56", "1", "true"},
                new Object[]{"23", "56", "22", "true"},
                new Object[]{"23", "56", "23", "true"},
                new Object[]{"23", "56", "24", "false"},
                new Object[]{"23", "56", "50", "false"},
                new Object[]{"23", "56", "100", "false"},
                new Object[]{"23", "56", "1000", "false"},
        };
    }

    @DataProvider(name = "AGE_==")
    public static Object[][] ageEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "57", "-1", "false"},
                new Object[]{"23", "57", "0", "false"},
                new Object[]{"23", "57", "1", "false"},
                new Object[]{"23", "57", "22", "false"},
                new Object[]{"23", "57", "23", "true"},
                new Object[]{"23", "57", "24", "false"},
                new Object[]{"23", "57", "50", "false"},
                new Object[]{"23", "57", "100", "false"},
        };
    }

    @DataProvider(name = "AGE_!=")
    public static Object[][] ageNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "58", "-1", "true"},
                new Object[]{"23", "58", "0", "true"},
                new Object[]{"23", "58", "1", "true"},
                new Object[]{"23", "58", "22", "true"},
                new Object[]{"23", "58", "23", "false"},
                new Object[]{"23", "58", "24", "true"},
                new Object[]{"23", "58", "50", "true"},
                new Object[]{"23", "58", "100", "true"},
        };
    }

    @DataProvider(name = "AGE_>=")
    public static Object[][] ageNotMoreOrEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "59", "-1", "false"},
                new Object[]{"23", "59", "0", "false"},
                new Object[]{"23", "59", "1", "false"},
                new Object[]{"23", "59", "22", "false"},
                new Object[]{"23", "59", "23", "true"},
                new Object[]{"23", "59", "24", "true"},
                new Object[]{"23", "59", "50", "true"},
                new Object[]{"23", "59", "100", "true"},
                new Object[]{"23", "59", "1000", "true"},
        };
    }

    @DataProvider(name = "AGE_>")
    public static Object[][] ageNotMore() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "60", "-1", "false"},
                new Object[]{"23", "60", "0", "false"},
                new Object[]{"23", "60", "1", "false"},
                new Object[]{"23", "60", "22", "false"},
                new Object[]{"23", "60", "23", "false"},
                new Object[]{"23", "60", "24", "true"},
                new Object[]{"23", "60", "50", "true"},
                new Object[]{"23", "60", "100", "true"},
        };
    }

    @DataProvider(name = "AGE_IN")
    public static Object[][] ageIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "-1", "false"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "0", "false"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "1", "false"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "23", "true"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "24", "false"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "25", "true"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "50", "true"},
                new Object[]{"[\"23\",\"25\",\"50\"]", "61", "100", "false"},
        };
    }

    @DataProvider(name = "AGE_NOT_IN")
    public static Object[][] ageNotIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "-1", "true"},
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "0", "true"},
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "1", "true"},
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "23", "false"},
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "24", "false"},
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "50", "false"},
                new Object[]{"[\"23\",\"24\",\"50\"]", "62", "100", "true"},
        };
    }


    @DataProvider(name = "AGE_BETWEEN")
    public static Object[][] ageBetween() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "-1", "false"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "22", "false"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "23", "true"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "24", "true"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "25", "true"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "26", "true"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "27", "true"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "28", "false"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "50", "false"},
                new Object[]{"[\"23\",\"27\",\"50\"]", "63", "100", "false"},

        };
    }

    @DataProvider(name = "AGE_MULTI")
    public static Object[][] ageMulti() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"63", "-1", "3,4"},
                new Object[]{"63", "19", "3,4"},
                new Object[]{"63", "20", "3,4"},
                new Object[]{"63", "21", "1,3,4"},
                new Object[]{"63", "22", "1,2,3,4"},
                new Object[]{"63", "29", "1,2,3,4"},
                new Object[]{"63", "30", "1,2,4"},
                new Object[]{"63", "31", "1,2"},
                new Object[]{"63", "32", "1,2"},
                new Object[]{"63", "50", "1,2"},
                new Object[]{"63", "100", "1,2"}
        };
    }

    @DataProvider(name = "IS_MALE_==")
    public static Object[][] isMaleEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"true", "64", "true", "true"},
                new Object[]{"true", "64", "false", "false"},
                new Object[]{"true", "64", "", "false"},

        };
    }

    @DataProvider(name = "IS_MALE_!=")
    public static Object[][] isMaleNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"true", "65", "true", "false"},
                new Object[]{"true", "65", "false", "true"},
                new Object[]{"true", "65", "", "false"},

        };
    }

    @DataProvider(name = "BAD_DISCOVERY_TIMES")
    public static Object[][] discoveryTimesLessBad() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "46", "!@#$%^&*()_+"}
        };
    }

    @DataProvider(name = "BAD_AGE")
    public static Object[][] ageBad() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"23", "55", "!@#$%^&*()_"}
        };
    }

    @DataProvider(name = "BAD_IS_MALE")
    public static Object[][] isMaleBad() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"true", "65", "！！@#￥%……&*（）——+"},
        };
    }

    @DataProvider(name = "CUSTOMERID_==")
    public static Object[][] customerIdEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"001", "42", "！@#￥%……&*（）——+", "false"},
                new Object[]{"001", "42", "001", "true"},
                new Object[]{"001", "42", "000", "false"},
                new Object[]{"001", "42", "002", "false"},
                new Object[]{"001", "42", "003", "false"},
                new Object[]{"001", "42", "004", "false"},
        };
    }

    @DataProvider(name = "CUSTOMERID_!=")
    public static Object[][] customerIdNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"001", "43", "001", "false"},
                new Object[]{"001", "43", "000", "true"},
                new Object[]{"001", "43", "002", "true"},
                new Object[]{"001", "43", "003", "true"},
                new Object[]{"001", "43", "004", "true"},

        };
    }

    @DataProvider(name = "CUSTOMERID_IN")
    public static Object[][] customerIdIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"001\",\"002\"]", "44", "001", "true"},
                new Object[]{"[\"001\",\"002\"]", "44", "002", "true"},
                new Object[]{"[\"001\",\"002\"]", "44", "000", "false"},
                new Object[]{"[\"001\",\"002\"]", "44", "432432", "false"},
                new Object[]{"[\"001\",\"002\"]", "44", "534", "false"},
                new Object[]{"[\"001\",\"002\"]", "44", "ger534", "false"},

        };
    }

    @DataProvider(name = "CUSTOMERID_NOT_IN")
    public static Object[][] customerIdNotIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"001\",\"002\"]", "45", "001", "false"},
                new Object[]{"[\"001\",\"002\"]", "45", "002", "false"},
                new Object[]{"[\"001\",\"002\"]", "45", "fdse423af", "true"},
                new Object[]{"[\"001\",\"002\"]", "45", "003", "true"},
                new Object[]{"[\"001\",\"002\"]", "45", "fds96-", "true"},

        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_<")
    public static Object[][] discoveryTimesLess() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "46", "-1", "true"},
                new Object[]{"5", "46", "0", "true"},
                new Object[]{"5", "46", "1", "true"},
                new Object[]{"5", "46", "4", "true"},
                new Object[]{"5", "46", "5", "false"},
                new Object[]{"5", "46", "6", "false"},
                new Object[]{"5", "46", "10", "false"},
                new Object[]{"5", "46", "50", "false"},
                new Object[]{"5", "46", "100", "false"},
                new Object[]{"5", "46", "1000", "false"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_<=")
    public static Object[][] discoveryTimesLessOrEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "47", "-1", "true"},
                new Object[]{"5", "47", "0", "true"},
                new Object[]{"5", "47", "1", "true"},
                new Object[]{"5", "47", "4", "true"},
                new Object[]{"5", "47", "5", "true"},
                new Object[]{"5", "47", "6", "false"},
                new Object[]{"5", "47", "10", "false"},
                new Object[]{"5", "47", "500", "false"},
                new Object[]{"5", "47", "100", "false"},
                new Object[]{"5", "47", "1000", "false"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_==")
    public static Object[][] discoveryTimesEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "48", "-1", "false"},
                new Object[]{"5", "48", "0", "false"},
                new Object[]{"5", "48", "1", "false"},
                new Object[]{"5", "48", "4", "false"},
                new Object[]{"5", "48", "5", "true"},
                new Object[]{"5", "48", "6", "false"},
                new Object[]{"5", "48", "10", "false"},
                new Object[]{"5", "48", "50", "false"},
                new Object[]{"5", "48", "100", "false"},
                new Object[]{"5", "48", "1000", "false"},

        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_!=")
    public static Object[][] discoveryTimesNotEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "49", "-1", "true"},
                new Object[]{"5", "49", "0", "true"},
                new Object[]{"5", "49", "1", "true"},
                new Object[]{"5", "49", "4", "true"},
                new Object[]{"5", "49", "5", "false"},
                new Object[]{"5", "49", "6", "true"},
                new Object[]{"5", "49", "10", "true"},
                new Object[]{"5", "49", "50", "true"},
                new Object[]{"5", "49", "100", "true"},
                new Object[]{"5", "49", "1000", "true"},
        };
    }


    @DataProvider(name = "DISCOVERY_TIMES_>=")
    public static Object[][] discoveryTimesMoreOrEqual() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "50", "-1", "false"},
                new Object[]{"5", "50", "0", "false"},
                new Object[]{"5", "50", "1", "false"},
                new Object[]{"5", "50", "4", "false"},
                new Object[]{"5", "50", "5", "true"},
                new Object[]{"5", "50", "6", "true"},
                new Object[]{"5", "50", "10", "true"},
                new Object[]{"5", "50", "50", "true"},
                new Object[]{"5", "50", "100", "true"},
                new Object[]{"5", "50", "1000", "true"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_>")
    public static Object[][] discoveryTimesMore() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"5", "51", "-1", "false"},
                new Object[]{"5", "51", "0", "false"},
                new Object[]{"5", "51", "1", "false"},
                new Object[]{"5", "51", "4", "false"},
                new Object[]{"5", "51", "5", "false"},
                new Object[]{"5", "51", "6", "true"},
                new Object[]{"5", "51", "10", "true"},
                new Object[]{"5", "51", "50", "true"},
                new Object[]{"5", "51", "100", "true"},
                new Object[]{"5", "51", "1000", "true"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_IN")
    public static Object[][] discoveryTimesIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "-1", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "0", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "1", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "4", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "5", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "6", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "7", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "10", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "11", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "50", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "100", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "52", "1000", "false"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_NOT_IN")
    public static Object[][] discoveryTimesNotIn() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "-1", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "0", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "1", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "4", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "5", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "6", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "7", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "10", "false"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "11", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "50", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "100", "true"},
                new Object[]{"[\"5\",\"6\",\"10\"]", "53", "1000", "true"},
        };
    }

    @DataProvider(name = "DISCOVERY_TIMES_BETWEEN")
    public static Object[][] discoveryTimesBetween() {
        //value, adId, testValue, expectResult
        return new Object[][]{
                new Object[]{"[\"5\",\"7\"]", "54", "-1", "flase"},
                new Object[]{"[\"5\",\"7\"]", "54", "0", "flase"},
                new Object[]{"[\"5\",\"7\"]", "54", "4", "flase"},
                new Object[]{"[\"5\",\"7\"]", "54", "5", "true"},
                new Object[]{"[\"5\",\"7\"]", "54", "6", "true"},
                new Object[]{"[\"5\",\"7\"]", "54", "7", "true"},
                new Object[]{"[\"5\",\"7\"]", "54", "10", "flase"},
                new Object[]{"[\"5\",\"7\"]", "54", "100", "flase"},
        };
    }

    @DataProvider(name = "GEN_10_STRATEGY")
    public static Object[][] gen10Strategy1() {
        //op,value, adId
        return new Object[][]{
                new Object[]{">", "10", "22"},
                new Object[]{">=", "10", "22"},
                new Object[]{"<", "100", "22"},
                new Object[]{"<=", "100", "22"},
                new Object[]{">", "11", "22"},
                new Object[]{">=", "11", "22"},
                new Object[]{"<", "99", "22"},
                new Object[]{"<=", "99", "22"},
                new Object[]{">", "9", "22"},
                new Object[]{"<", "200", "22"},
        };
    }

    @DataProvider(name = "DURATION_TIME_VALID")
    public static Object[][] durationTime() {
        //id,startTime,endTime,expectresult
        return new Object[][]{
                new Object[]{"1", System.currentTimeMillis() + 5 * 1000, System.currentTimeMillis() + 10 * 1000, 0, "false"},
                new Object[]{"2", System.currentTimeMillis() + 5 * 1000, System.currentTimeMillis() + 10 * 1000, 6 * 1000, "true"},
                new Object[]{"3", System.currentTimeMillis() + 5 * 1000, System.currentTimeMillis() + 10 * 1000, 12 * 1000, "false"},
                new Object[]{"4", System.currentTimeMillis() + 60 * 60 * 1000, System.currentTimeMillis() + 2 * 60 * 60 * 1000, 0, "false"},
        };
    }

    @DataProvider(name = "DURATION_TIME_LIMIT")
    public static Object[][] durationTimeLimit() {
        //id,startTime,endTime,expectresult
        return new Object[][]{
                new Object[]{"1", System.currentTimeMillis() - 2 * 60 * 60 * 1000, System.currentTimeMillis() + 60 * 1000, StatusCode.SUCCESS},
                new Object[]{"2", System.currentTimeMillis() + 60 * 60 * 1000, System.currentTimeMillis() + 2 * 60 * 60 * 1000, StatusCode.SUCCESS},
                new Object[]{"3", System.currentTimeMillis() - 2 * 60 * 60 * 1000, System.currentTimeMillis() - 60 * 60 * 1000, StatusCode.illegalRuleSetting},
                new Object[]{"4", System.currentTimeMillis() - 24 * 60 * 60 * 1000, System.currentTimeMillis() - 60 * 60 * 1000, StatusCode.illegalRuleSetting},
                new Object[]{"5", System.currentTimeMillis() - 24 * 60 * 60 * 1000, System.currentTimeMillis() - 60 * 60 * 1000, StatusCode.illegalRuleSetting},
                new Object[]{"6", System.currentTimeMillis() - 60 * 60 * 1000, System.currentTimeMillis() - 30 * 60 * 1000, StatusCode.illegalRuleSetting},
                new Object[]{"7", System.currentTimeMillis() - 2000, System.currentTimeMillis() - 1000, StatusCode.illegalRuleSetting},
                new Object[]{"8", System.currentTimeMillis() + 24 * 60 * 60 * 1000, System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000, StatusCode.SUCCESS},
                new Object[]{"9", System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000, System.currentTimeMillis() + 24 * 60 * 60 * 1000, StatusCode.illegalRuleSetting},
                new Object[]{"10", System.currentTimeMillis() + 24 * 60 * 60 * 1000, System.currentTimeMillis() + 24 * 60 * 60 * 1000, StatusCode.illegalRuleSetting},
        };
    }
}