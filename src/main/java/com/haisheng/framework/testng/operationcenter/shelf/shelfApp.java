package com.haisheng.framework.testng.operationcenter.shelf;

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
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
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
import java.util.UUID;

public class shelfApp {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_04e816df";
    private static String APP_CODE = "0d28ec728799";
    private static String AK = "64bef7a65f11bffd";
    private static String SK = "d3a899a9c79e92200e89771d9df3aa41";
    private String UNIT_CODE = "QA-TEST【勿动】";
    private String SHELVES_CODE = "QA-TEST【勿动】";
    private String PLATE_CODE = "666";

    private String UNIT_CODE_1 = "QA-TEST-1【勿动】";
    private String SHELVES_CODE_1 = "QA-TEST-1【勿动】";
    private String PLATE_CODE_1 = "912";

    private static long SHOP_ID = 2867;
    private String genAuthURL = "http://dev.sso.winsenseos.cn/sso/login";

    private String URL_prefix = "http://dev.app.winsenseos.cn/operation/app/COMMODITY/";
    private String realTimeListServiceId = "3015";
    private String unitDetailServiceId = "3016";
    private String latticeDetailServiceId = "3017";
    private String latticeCheckServiceId = "3018";
    private String latticeBindingServiceId = "3019";
    private String unitstockingFinishServiceId = "3020";
    private String shopstockingFinishServiceId = "3021";
    private String latticeUnbindServiceId = "3022";

    private static String typePick = "PICK";
    private static String typeDrop = "DROP";

    private static String checkTypeStocktaking = "STOCKTAKING";

    private static String alarmStatesOutAndSensor = "OUT_OF_STOCK:SENSOR_ERROR";
    private static String alarmStatesOutofStock = "OUT_OF_STOCK";
    private static String alarmStatesSensorError = "SENSOR_ERROR";
    private static String alarmStatesWrongPlace = "WRONG_PLACE";

    HashMap<String, String> header = new HashMap();

    private String authorization = null;

    private long goodsId3Add2 = 139;

    private ApiResponse apiResponse;
    private String response;

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/commodity-management/buildWithParameters?case_name=";

    private static int accuracyCaseTotalNum = 0;
    private static int accuracyCasePassNum = 0;


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

    //------------------------0.2 心跳-----------------------------------
    private void heartBeat(String unitCode, String plateCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------heart beat!-----------------------");
        String router = "/commodity/external/HEART_BEAT/v1.0";
        String[] resource = new String[]{};
        String message = "";

        long currentTime = System.currentTimeMillis();

        //组织参数
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{").
                append("    \"unit_code\": \"").append(unitCode).append("\",").
                append("    \"timestamp\": ").append(currentTime).append(",").
                append("    \"camera\": \"NORMAL\",").
                append("    \"plate\": [").
                append("      {").
                append("        \"plate_code\":\"").append(plateCode).append("\",").
                append("        \"sensors\": [").
                append("          {").
                append("            \"plate_code\":\"").append(plateCode).append("\",").
                append("            \"sensor_id\": 0,").
                append("            \"k\": 1.01,").
                append("            \"zero\": 1234.9876,").
                append("            \"status\": \"NORMAL\"").
                append("          },").
                append("          {").
                append("            \"plate_code\":\"").append(plateCode).append("\",").
                append("            \"sensor_id\": 1,").
                append("            \"k\": 2.02,").
                append("            \"zero\": 9876.54321,").
                append("            \"status\": \"NORMAL\"").
                append("          }").
                append("        ]").
                append("      }").
                append("    ]").append("}");

        try {
            apiResponse = sendRequestWithGate(router, resource, jsonBF.toString());
            message = apiResponse.getMessage();

            sendResAndReqIdToDbApi(apiResponse, acase, step);

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);
        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
    }

    //----------------------------0.3 货架事件通知-------------------------
    private void customerMessage(String unitCode, String type, String plateCode, long change, long total, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------customer message!-----------------------");
        String router = "/commodity/external/CUSTOMER_MESSAGE/v1.0";
        String[] resource = new String[]{};
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        组织参数
        String json =
                genCustomerMessagePara(unitCode, type, plateCode, change, total);

        try {
            apiResponse = sendRequestWithGate(router, resource, json);
            message = apiResponse.getMessage();

            sendResAndReqIdToDbApi(apiResponse, acase, step);

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }


    }

    private String genCustomerMessagePara(String unitCode, String type, String plateCode, long change, long total) throws Exception {

        long currentTime = System.currentTimeMillis();
        long startTime = currentTime;
        long endTime = currentTime + 60 * 1000;

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        组织参数
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{").
                append("    \"data\": {").
                append("      \"plate_code\": \"").append(plateCode).append("\",").
                append("      \"weight_change\":").append(change).append(",").
                append("      \"total_weight\":").append(total).
                append("    },").
                append("    \"end_time\": ").append(endTime).append(",").
                append("    \"start_time\": ").append(startTime).append(",").
                append("    \"timestamp\": ").append((currentTime - 1000)).append(",").
                append("    \"type\": \"").append(type).append("\",").
                append("    \"unit_code\": \"").append(unitCode).append("\"").
                append("}");

        return jsonBF.toString();
    }

    private void customerMessagePersonAndGoods(String image64, String personId, String unitCode, String type,
                                               String plateCode, long change, long total, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------customer message!-----------------------");
        String router = "/commodity/external/CUSTOMER_MESSAGE/v1.0";
        String[] resource = new String[]{};
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        组织参数
        String json = "";
//        String json = genCustomerMessagePara(image64, personId, UNIT_CODE, type, plateCode, change, total);

        try {
            apiResponse = sendRequestWithGate(router, resource, json);
            message = apiResponse.getMessage();

            sendResAndReqIdToDbApi(apiResponse, acase, step);

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
    }

    private String genCustomerMessagePersonAndGoodsPara(String image64, String personId, String unitCode, String type, String plateCode, long change, long total) throws Exception {

        long currentTime = System.currentTimeMillis();
        long startTime = currentTime;
        long endTime = currentTime + 60 * 1000;

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        组织参数
        String json =
                "{" +


                        "    \"data\": {" +
                        "      \"plate_code\": \"" + plateCode + "\"," +
                        "      \"weight_change\":" + change + "," +
                        "      \"total_weight\":" + total + "\n" +
                        "    }," +
                        "    \"end_time\": " + endTime + "," +
                        "    \"start_time\": " + startTime + "," +
                        "    \"timestamp\": " + (currentTime - 1000) + "," +
                        "    \"type\": \"" + type + "\"," +
                        "    \"unit_code\": \"" + unitCode + "\"" +
                        "}";

        String json1 =
                "{" +
                        "    \"start_time\": " + startTime + "," +
                        "    \"face_list\":[" +
                        "        {" +
                        "            \"image\":\"" + image64 + "\"," +
                        "            \"sunglasses\":0," +
                        "            \"illumination\":0," +
                        "            \"roll\":4.6147565841674805," +
                        "            \"blur\":0," +
                        "            \"pitch\":-0.48800623416900635," +
                        "            \"yaw\":-32.653717041015625," +
                        "            \"axis_str\":\"[370,170,433,250]\"," +
                        "            \"frame_time\":1563275794915," +
                        "            \"mask\":0," +
                        "            \"quality\":0.28726914525032043" +
                        "        }" +
                        "    ]," +
                        "    \"end_time\": " + endTime + "," +
                        "    \"unit_code\": \"" + unitCode + "\"" +
                        "    \"type\": \"" + type + "\"," +
                        "    \"person_id\":" + personId + "," +
                        "    \"timestamp\": " + (currentTime - 1000) + "," +
                        "    \"data\":{" +
                        "        \"sensors\":[" +
                        "            {" +
                        "                \"sensor_id\":0," +
                        "                \"adc\":29663," +
                        "                \"weight\":603.9901123046875" +
                        "            }," +
                        "            {" +
                        "                \"sensor_id\":1," +
                        "                \"adc\":31582," +
                        "                \"weight\":724.873779296875" +
                        "            }" +
                        "        ]," +
                        "        \"position\":\"4,7\"," +
                        "      \"plate_code\": \"" + plateCode + "\"," +
                        "      \"weight_change\":" + change + "," +
                        "      \"total_weight\":" + total + "" +
                        "    }" +
                        "}";

        return json;
    }


    private void customerGoods(String customerId) throws Exception {
        logger.info("\n");
        logger.info("------------customer goods!-----------------------");
        String router = "/commodity/external/CUSTOMER_GOODS/v1.0";
        String[] resource = new String[]{};
        String message = "";


//        组织参数
        String json = genCustomerGoodsPara(customerId);

        try {
            apiResponse = sendRequestWithGate(router, resource, json);
//            message = apiResponse.getMessage();
            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(failReason);
            throw e;
        }
    }

    private String genCustomerGoodsPara(String customerId) {

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id


//        组织参数
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{").
                append("    \"subject_id\":\"").append(SHOP_ID).append("\",").
                append("    \"customer_id\":\"").append(customerId).append("\"").
                append("}");

        String json =
                "{" +
                        "    \"subject_id\":\"" + SHOP_ID + "\"," +
                        "    \"customer_id\":\"" + customerId + "\"" +
                        "}";

        return jsonBF.toString();
    }

    //----------------------1.1 平面图货架列表-----------------------
    private String realTimeList(Case acase, int step) {
        logger.info("\n");
        logger.info("------------real time list!-------------------------------------");

//        组织参数
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{").
                append("\"subject_id\":\"").append(SHOP_ID).append("\"").
                append("}");

        String json =
                "{" +
                        "\"subject_id\":\"" + SHOP_ID + "\"" +
                        "}";

        try {
            response = sendRequestWithHeader(realTimeListServiceId, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-----------------------1.2 货架单元详情-----------------------------------
    private String unitDetail(String unitCode, String shelvesCode, Case acase, int step) {
        logger.info("\n");
        logger.info("------------unit detail!-----------------------");

//        组织参数
        String json = genUnitDetailPara(unitCode, shelvesCode);

        try {
            response = sendRequestWithHeader(unitDetailServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String unitDetail(String unitCode, String shelvesCode) {
        logger.info("\n");
        logger.info("------------unit detail!-----------------------");

//        组织参数

        String json = genUnitDetailPara(unitCode, shelvesCode);

        try {
            response = sendRequestWithHeader(unitDetailServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    public String genUnitDetailPara(String unitCode, String shelvesCode) {

        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{").
                append("\"subject_id\":").append(SHOP_ID).append(",").
                append("\"shelves_code\":\"").append(shelvesCode).append("\",").
                append("\"unit_code\":\"").append(unitCode).append("\"").
                append("}");
        return jsonBF.toString();
    }

    //-----------------------1.3 单元格物品详情-----------------------------------
    private String latticeDetail(int latticeId, Case acase, int step) {
        logger.info("\n");
        logger.info("------------lattice detail !-----------------------");

//        组织参数
        StringBuffer jsonBF = new StringBuffer();

        jsonBF.append("{").append("\"lattice_id\":").append(latticeId).
                append("}");

        try {
            response = sendRequestWithHeader(latticeDetailServiceId, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-----------------------1.4 单元格物品扫描-----------------------------------
    private String latticeCheck(int latticeId, long goodsId, String checkType, Case acase, int step) {
        logger.info("\n");
        logger.info("------------lattice check!-----------------------");

//        组织参数
        StringBuffer jsonBF = new StringBuffer();

        jsonBF.append("{").
                append("\"lattice_id\":").append(latticeId).append(",").
                append("\"goods_id\":").append(goodsId).append(",").
                append("\"check_type\":\"").append(checkType).append("\"").
                append("}");

        try {
            response = sendRequestWithHeader(latticeCheckServiceId, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-----------------------1.5 单元格物品绑定-----------------------------------
    private String latticeBinding(long latticeId, long goodsId, long goodsStock, long totalWeight, String bindingType, Case acase, int step) {
        logger.info("\n");
        logger.info("----------------lattice binding!-----------------------");

//        组织参数
        StringBuffer jsonBF = new StringBuffer();

        jsonBF.append("{").append(
                "\"lattice_id\":").append(latticeId).append(",").append(
                "\"goods_id\":").append(goodsId).append(",").append(
                "\"goods_stock\":").append(goodsStock).append(",").append(
                "\"total_weight\":").append(totalWeight).append(",").append(
                "\"last_pick_time\":").append(totalWeight).append(",").append(
                "\"binding_type\":\"").append(bindingType).append("\"").append(
                "}");

        try {
            response = sendRequestWithHeader(latticeBindingServiceId, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-----------------------1.6 单元盘货完成-----------------------------------
    private String unitStocktaking(String unitCode, Case acase, int step) {
        logger.info("\n");
        logger.info("-------------------------unit stocktaking finish!-----------------------");

//        组织参数
        StringBuffer jsonBF = new StringBuffer();

        jsonBF.append("{").append(
                "\"shelves_code\":\"").append(SHELVES_CODE).append("\",").append(
                "\"unit_code\":\"").append(unitCode).append("\"").append(
                "}");

        try {
            response = sendRequestWithHeader(unitstockingFinishServiceId, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-----------------------1.7 店铺盘货完成-----------------------------------
    private String shopStocktaking(Case acase, int step) {
        logger.info("\n");
        logger.info("-------------------------shop stocktaking finish!-----------------------");

//        组织参数
        String json =
                "{" +
                        "\"subject_id\":\"" + SHOP_ID + "\"" +
                        "}";

        try {
            response = sendRequestWithHeader(shopstockingFinishServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-----------------------1.8 单元物品解绑-----------------------------------
    private String latticeUnbind(long latticeId, long goodsId, Case acase, int step) {
        logger.info("\n");
        logger.info("-------------------------lattice unbind!-----------------------");

//        组织参数
        StringBuffer jsonBF = new StringBuffer();

        jsonBF.append("{").append(
                "\"lattice_id\":\"").append(latticeId).append("\",").append(
                "\"goods_id\":").append(goodsId).append("\n").append(
                "}");

        try {
            response = sendRequestWithHeader(latticeUnbindServiceId, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //    ------------------没有事件时直接进行单元格物品扫描（盘、理）---------------------------------
    //-----------------（1）---------------------单元格物品扫描--------------------------------
    @Test(dataProvider = "CHECK_TYPE")
    private void testLatticeCheckWithoutCustomerMessage(String checkType) {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + checkType;
        String caseDesc = "没有事件时直接进行单元格物品扫描（盘、理）";
        logger.info(caseDesc + "--------------------");

        String latticeCheckRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        int step = 0;

        try {

            aCase.setRequestData("单元格物品扫描" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            //现在逻辑改成先查goodsId，如果不存在就去爬取，那个爬取的网站返回的500，所以将goodsId改成一个存在的goodsId就行了。
            latticeCheckRes = latticeCheck(1, goodsId3Add2, checkType, aCase, step);
            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.INTERNAL_SERVER_ERROR, message);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);

        }
    }

    //---------------------------------没有事件时直接单元格物品绑定（盘、理）--------------------------
    //-----------------（2）--------------------单元格物品绑定-------------------------------
    @Test(dataProvider = "CHECK_TYPE")
    private void testLatticeBindingWithoutCustomerMessage(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + checkType;
        String caseDesc = "没有事件时直接单元格物品绑定（盘、理）";
        logger.info(caseDesc + "--------------------");

        String latticeBindingRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        int step = 0;

        try {
            aCase.setRequestData("单元格物品绑定" + "\n\n");

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(1, goodsId3Add2, 3, 300, checkType, aCase, step);
            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.actionNotFound, message);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    --------------------通过扫描盘货和理货，检测物品放置正确和错误盘货是否成功---------------------------
    @Test(dataProvider = "TALLY&STOCKTAKING_WITH_SCAN")
    private void testTallyAndStocktakingWithScan(String checkType, long changeP, long totalP, long changeD, long totalD,
                                                 long bindingStock, long bindingTotal, boolean expectBinding,
                                                 boolean expectUnitStocktaking, int expectCodeBinding, int expectCodeUnitStocktaking, String caseId) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + caseId;
        String caseDesc = "通过扫描盘货和理货，检测物品放置正确和错误盘货是否成功";
        logger.info(caseDesc + "--------------------");

        String latticeCheckRes;
        String latticeBindingRes;
        String unitStocktakingRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        String plateCode = "666";
        int step = 0;


//        -100, 0, 50, 120, 3, 300,

        try {

            aCase.setRequestData("1、事件通知(pick one)-2、商品扫描-3、货架事件通知(drop )-" +
                    "4、单元格物品绑定-5、单元盘货完成" + "\n\n");

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            货架单元详情，取latticeId
            logger.info("\n\n");
            String unitDetailRes = unitDetail(UNIT_CODE, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 1);

//            1、货架事件通知（pick）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(UNIT_CODE, typePick, plateCode, changeP, totalP, aCase, step);

//            2、商品扫描
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType, aCase, step);
            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

//            3、货架事件通知（drop）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(UNIT_CODE, typeDrop, plateCode, changeD, totalD, aCase, step);

//            4、单元格物品绑定

            if (expectBinding) {
                logger.info("\n\n");
                logger.info("--------------------------------（" + (++step) + ")------------------------------");
                latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType, aCase, step);

                message = JSON.parseObject(latticeBindingRes).getString("message");
                checkCode(latticeBindingRes, expectCodeBinding, message + "-latticeBinding");
            }

//            5、单元盘货完成
            if (expectUnitStocktaking) {
                logger.info("\n\n");
                logger.info("--------------------------------（" + (++step) + ")------------------------------");
                unitStocktakingRes = unitStocktaking(UNIT_CODE, aCase, step);
                message = JSON.parseObject(unitStocktakingRes).getString("message");
                checkCode(unitStocktakingRes, expectCodeUnitStocktaking, message + "unitStocktaking");
            }

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "STOCKTAKING_WITHOUT_SCAN")
    private void testTallyAndStocktakingWithoutScan(String checkType, long changeD, long totalD, long bindingStock, long bindingTotal,
                                                    int expectCodeLattice, int expectCodeUnit, String type, long chng, long total,
                                                    String alarm, int stock, int expectCodeUnit1, String caseId) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + caseId;
        String caseDesc = "测试总重对单元盘货成功与否的影响";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        String response;
        String message = "";
        String unitCode = UNIT_CODE;
        String plateCode = "666";
        int step = 0;

        try {
            aCase.setRequestData("1、货架事件通知（drop）-2、单元格物品绑定-3、单元盘货完成-4、心跳-5、货架事件通知" +
                    "6、货架单元详情-7、单元格物品详情-8、单元盘货完成" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            货架单元详情，取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "unitDetail");
            int latticeId = checkUnitDetail(response, 1);

//            1、货架事件通知（drop）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, changeD, totalD, aCase, step);

//            2、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCodeLattice, message + "latticeBinding");

//            3、单元盘货完成
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitStocktaking(unitCode, aCase, step);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCodeUnit, message + "unitStocktaking");

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、货架事件通知
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type, plateCode, chng, total, aCase, step);

//            6、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm), stock);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm), stock);

//            8、单元盘货
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitStocktaking(unitCode, aCase, step);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCodeUnit1, message + "unitStocktaking");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //-------------------------测试扫描盘货和理货后，平面图货架列表、货架单元详情和单元格物品详情中的内容是否正确----------------
    @Test(dataProvider = "TALLY&STOCKTAKING_RESULT")
    private void testTallyAndStocktakingResult(String checkType, long Pchng, long Ptotal, long Dchng,
                                               long Dtotal, long bindingStock, long bindingTotal) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + checkType;
        String caseDesc = "测试扫描盘货和理货后，平面图货架列表、货架单元详情和单元格物品详情中的内容是否正确";
        logger.info(caseDesc + "--------------------");

        String latticeCheckRes;
        String latticeBindingRes;
        String unitStocktakingRes;
        String realTimeListRes;
        String unitDetailRes;
        String latticeDetailRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        String unitCode = UNIT_CODE;
        String plateCode = "666";

        int step = 0;

        try {

            aCase.setRequestData("1、通知(pick one)-2、扫描（盘、理）-3、通知（drop）-4、绑定（同扫描）" +
                    "5、单元盘货-6、心跳-7、平面图货架列表-8、货架单元详情-9、单元格物品详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "unitDetail");
            int latticeId = checkUnitDetail(unitDetailRes, 1);

//            1、货架事件通知（pick）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng, Ptotal, aCase, step);

//            2、单元格物品扫描
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType, aCase, step);

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message + "latticeCheck");

//            3、货架事件通知（drop 3）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            4、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "latticeBinding");

//            5、单元盘货完成
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitStocktakingRes = unitStocktaking(unitCode, aCase, step);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message + "unitStocktaking");

//            6、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            7、平面图货架列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            realTimeListRes = realTimeList(aCase, step);
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "realTimeList");

            checkRealtimeListStocktakingStates(realTimeListRes, unitCode);

//            8、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "unitDetail");

            checkUnitDetailStocktakingStates(unitDetailRes, latticeId, bindingStock);

//            9、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes = latticeDetail(latticeId, aCase, step);

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message + "latticeDetail");

            checkLatticeDetailGoodsStock(latticeDetailRes, bindingStock);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //--------------------解绑后货架单元详情和单元格物品详情中都不能查询到该单元格-----------------------------
    @Test(dataProvider = "CHECK_TYPE")
    private void testUnbindResult(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + checkType;
        String caseDesc = "解绑后货架单元详情和单元格物品详情中都不能查询到该单元格";
        logger.info(caseDesc + "--------------------");

//       Tally/stocktaking  -100, 400, 100, 500, 5, 500

        long Pchng = -100L;
        long Ptotal = 400L;
        long Dchng = 100L;
        long Dtotal = 500L;
        long bindingStock = 5;
        long bindingTotal = 500;

        String latticeCheckRes;
        String latticeBindingRes;
        String unitStocktakingRes;
        String latticeUnbindRes;
        String realTimeListRes;
        String unitDetailRes;
        String latticeDetailRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        String unitCode = UNIT_CODE;
        String plateCode = "666";

        int step = 0;

        try {

            aCase.setRequestData("1、通知(pick one)-2、扫描(盘、理)-3、通知（drop）" +
                    "4、绑定（同扫描）-5、单元盘货-6、解绑-7、心跳-8、平面图货架列表" +
                    "9、货架单元详情-10、单元格物品详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + " ----unitDetail");
            int latticeId = checkUnitDetail(unitDetailRes, 1);

//            1、货架事件通知（pick -100）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng, Ptotal, aCase, step);

//            2、单元格物品扫描
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType, aCase, step);

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message + "----latticeCheck");

//            3、货架事件通知（drop 3）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            4、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "----latticeBinding");

//            5、单元盘货完成
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitStocktakingRes = unitStocktaking(unitCode, aCase, step);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message + "---unitStocktaking");

//            6、单元物品解绑
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeUnbindRes = latticeUnbind(latticeId, goodsId3Add2, aCase, step);

            message = JSON.parseObject(latticeUnbindRes).getString("message");
            checkCode(latticeUnbindRes, StatusCode.SUCCESS, message + "---latticeUnbind");

//            7、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            8、平面图货架列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            realTimeListRes = realTimeList(aCase, step);
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "---realTimeList");

//            这里仅校验告警状态是否为空和盘货状态是否为FINISHED
            checkUnbindRealTimeList(realTimeListRes, unitCode);

//            9、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---unitDetail");

            checkUnbindUnitDetail(unitDetailRes, latticeId);

//            10、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes = latticeDetail(latticeId, aCase, step);

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message + "---latticeDetail");

            checkUnbindLatticeDetail(latticeDetailRes);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //----------------(7)---------理货成功后，该单元盘货状态应为UNFINISHED---------------------------------------------------

    @Test
    private void testUnfinishedATally() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "理货成功后，该单元盘货状态应为UNFINISHED";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long Dchng1 = 100L;
        long Dtotal1 = 400L;
        long bindingStock = 3L;
        long bindingTotal = 300L;

        String latticeBindingRes;
        String realTimeListRes;
        String shopStocktakingRes;

        String unitDetailRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        String unitCode = UNIT_CODE;
        String plateCode = "666";

        int step = 0;

        try {

            aCase.setRequestData("1、通知（drop 3）-2、绑定（盘）-3、通知（drop 1）-4、平面图货架列表-5、店铺盘货" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "----1、unitDetail");
            int latticeId_2 = checkUnitDetail(unitDetailRes, 1);

//            1、货架事件通知
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            2、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId_2, goodsId3Add2, bindingStock, bindingTotal, checkTypeStocktaking, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、通知（drop 1）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng1, Dtotal1, aCase, step);

//            4、平面图货架列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            realTimeListRes = realTimeList(aCase, step);
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "---5、realTimeList");

            checkRealTimeList(realTimeListRes, unitCode, "UNFINISHED");

//            5、店铺盘货完成
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            shopStocktakingRes = shopStocktaking(aCase, step);
            message = JSON.parseObject(shopStocktakingRes).getString("message");
            checkCode(shopStocktakingRes, StatusCode.stocktakingUnfinished, message + "---6、shopStocktaking");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //----------------(8)---------测试一个店铺有两个单元，一个盘货，一个未盘货，那么店铺盘货失败---------------------------------------------------

    @Test
    private void testShopStocktaking() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试一个店铺有两个单元，一个盘货，一个未盘货，那么店铺盘货失败";
        logger.info(caseDesc + "--------------------");

        long Pchng_1 = -100L;
        long Ptotal_1 = 400L;
        long Dchng_1 = 100L;
        long Dtotal_1 = 500L;
        long bindingStock_1 = 5L;
        long bindingTotal_1 = 500L;
        long Dchng_2 = 100L;
        long Dtotal_2 = 300L;
        long Dchng_3 = 100L;
        long Dtotal_3 = 400L;
        long bindingStock_2 = 3L;
        long bindingTotal_2 = 300L;

        String latticeCheckRes;
        String latticeBindingRes;
        String unitStocktakingRes;
        String latticeBindingRes2;
        String shopStocktakingRes;

        String unitDetailRes;

        Case aCase = new Case();
        failReason = "";

        String message = "";
        String unitCode_1 = UNIT_CODE_1;
        String unitCode_2 = UNIT_CODE;
        String plateCode_1 = PLATE_CODE_1;
        String plateCode_2 = PLATE_CODE;

        String shelvesCode_1 = SHELVES_CODE_1;
        String shelvesCode_2 = SHELVES_CODE;

        int step = 0;
        try {

            aCase.setRequestData("1、通知(pick one)-2、扫描-3、通知（drop）-4、绑定(理)-5、单元盘货完成-6、创建单元-7、通知（drop 3）" +
                    "8、绑定（盘）-9、通知（drop 1）-10、店铺盘货" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode_1, shelvesCode_1);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId_1 = checkUnitDetail(unitDetailRes, 1);

//            2、货架事件通知（pick）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode_1, typePick, plateCode_1, Pchng_1, Ptotal_1, aCase, step);

//            3、单元格物品扫描
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeCheckRes = latticeCheck(latticeId_1, goodsId3Add2, checkTypeStocktaking, aCase, step);

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

//            4、货架事件通知（drop 3）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode_1, typeDrop, plateCode_1, Dchng_1, Dtotal_1, aCase, step);

//            5、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId_1, goodsId3Add2, bindingStock_1, bindingTotal_1, checkTypeStocktaking, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            6、单元盘货完成
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitStocktakingRes = unitStocktaking(unitCode_1, aCase, step);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message);


            //            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode_2, shelvesCode_2);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "----7、unitDetail");
            int latticeId_2 = checkUnitDetail(unitDetailRes, 1);

//            8、货架事件通知
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode_2, typeDrop, plateCode_2, Dchng_2, Dtotal_2, aCase, step);

//            9、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes2 = latticeBinding(latticeId_2, goodsId3Add2, bindingStock_2, bindingTotal_2, checkTypeStocktaking, aCase, step);

            message = JSON.parseObject(latticeBindingRes2).getString("message");
            checkCode(latticeBindingRes2, StatusCode.SUCCESS, message + "---9、latticeBinding");

//            10、通知（drop 1）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode_2, typeDrop, plateCode_2, Dchng_3, Dtotal_3, aCase, step);

//            11、店铺盘货完成
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            shopStocktakingRes = shopStocktaking(aCase, step);
            message = JSON.parseObject(shopStocktakingRes).getString("message");
            checkCode(shopStocktakingRes, StatusCode.stocktakingUnfinished, message + "---12、shopStocktaking");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------------------------盘货时只绑定1个商品，观察此时理货的状态---------------------------------------------
    @Test
    private void TestStockTakingOnlyOne() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "盘货时只绑定1个商品，观察此时理货的状态";
        logger.info(caseDesc + "--------------------");

        String latticeBindingRes;
        String realTimeListRes;
        String unitDetailRes;
        String latticeDetailRes;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        int step = 0;

        try {

            aCase.setRequestData("1、通知(drop one)-2、绑定一个商品(盘货)-3、心跳-" +
                    "4、列表(期待缺货)-5、单元详情（期待缺货）-6、物品详情（期待缺货）" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、货架事件通知
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, 100, 100, aCase, step);

//            货架单元详情，为了获取latticeId
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---unitDetail");
            latticeId = checkUnitDetail(unitDetailRes, 1);

//            2、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId, goodsId, 1, 100, checkTypeStocktaking, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---latticeBinding");

//            3、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            4、平面图货架列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            realTimeListRes = realTimeList(aCase, step);
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "---realTimeList");

            String[] expectState = {alarmStatesOutofStock};
            checkRealtimeListAlarmStates(realTimeListRes, unitCode, expectState);

//            5、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---unitDetail");

            latticeId = checkUnitDetail(unitDetailRes, 1);

            expectState = new String[]{alarmStatesOutofStock};
            checkUnitDetailAlarmStates(unitDetailRes, latticeId, expectState);

//            6、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes = latticeDetail(latticeId, aCase, step);

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message + "---latticeDetail");

            JSONArray latticeDetailAlarmStates = checkLatticeDetailAlarmStates(latticeDetailRes);
            expectState = new String[]{alarmStatesOutofStock};
            checkAlarmState(latticeDetailAlarmStates, 1, expectState, "latticeDetailAlarmStates---");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//      -------------------- 给一个单元格放置3个物品，然后拿走直到拿空----------------------------

    @Test(dataProvider = "CHECK_TYPE")
    private void TestPickUntilEmpty(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + checkType;
        String caseDesc = "给一个单元格放置3个物品，然后拿走直到拿空";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long bindingStock = 3L;
        long bindingTotal = 300L;
        long Pchng1 = -100L;
        long Ptotal1 = 200L;
        long Pchng2 = -200L;
        long Ptotal2 = 0L;

        String latticeBindingRes;
        String realTimeListRes2;
        String realTimeListRes0;
        String unitDetailRes0;
        String latticeDetailRes0;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int step = 0;

        try {

            aCase.setRequestData("1、通知(drop 3)-2、绑定(3个)-3、通知(pick one)-4、心跳-5、列表(还剩2个，缺货)-" +
                    "6、通知(pick 2，此时拿空了)-7、心跳-8、列表(拿空了，缺货)-9、单元详情（缺货）-10、物品详情（缺货）" + "\n\n");

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、货架事件通知(drop 3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
            int latticeId = checkUnitDetail(response, 1);

//            2、单元格物品绑定(绑定3个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(pick one)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、平面图货架列表(还剩2个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            realTimeListRes2 = realTimeList(aCase, step);
            message = JSON.parseObject(realTimeListRes2).getString("message");
            checkCode(realTimeListRes2, StatusCode.SUCCESS, message + "---6、realTimeList");

            String[] expectState = {alarmStatesOutofStock};
            checkRealtimeListAlarmStates(realTimeListRes2, unitCode, expectState);

//            6、通知(pick 2，此时拿空了)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng2, Ptotal2, aCase, step);

//            7、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            8、平面图货架列表(空了)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            realTimeListRes0 = realTimeList(aCase, step);
            message = JSON.parseObject(realTimeListRes0).getString("message");
            checkCode(realTimeListRes0, StatusCode.SUCCESS, message + "--- 9、realTimeList");

            expectState = new String[]{alarmStatesOutofStock};
            checkRealtimeListAlarmStates(realTimeListRes0, unitCode, expectState);

//            9、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes0 = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            message = JSON.parseObject(unitDetailRes0).getString("message");
            checkCode(unitDetailRes0, StatusCode.SUCCESS, message + "---10、unitDetail");

            expectState = new String[]{alarmStatesOutofStock};
            checkUnitDetailAlarmStates(unitDetailRes0, latticeId, expectState);

            latticeId = checkUnitDetail(unitDetailRes0, 1);

//            10、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes0 = latticeDetail(latticeId, aCase, step);

            message = JSON.parseObject(latticeDetailRes0).getString("message");
            checkCode(latticeDetailRes0, StatusCode.SUCCESS, message + "---11、latticeDetail");

            JSONArray latticeDetailAlarmStates = checkLatticeDetailAlarmStates(latticeDetailRes0);
            expectState = new String[]{alarmStatesOutofStock};
            checkAlarmState(latticeDetailAlarmStates, 1, expectState, "---11、latticeDetailAlarmStates-还剩0个商品");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }


    public int stockTaking(Case aCase, int step) throws Exception {

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;
        String checkType = "STOCKTAKING";

        long goodsId = 139;  //3+2饼干

        long Dchng = 100L;
        long Dtotal = 600L;

        long bindingStock = 6;
        long bindingTotal = 600;

//        1、货架事件通知(drop 6)
        logger.info("\n\n");
        logger.info("--------------------------------（" + (++step) + ")------------------------------");
        customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
        response = unitDetail(unitCode, SHELVES_CODE);
        String message = JSON.parseObject(response).getString("message");
        checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
        int latticeId = checkUnitDetail(response, 1);

//        2、单元格物品绑定(绑定6个)
        logger.info("\n\n");
        logger.info("--------------------------------（" + (++step) + ")------------------------------");
        String latticeBindingRes = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

        message = JSON.parseObject(latticeBindingRes).getString("message");
        checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

        return latticeId;
    }

    @Test(dataProvider = "NORMAL_INIT")
    public void normalInitial(String type, long chng, long total, int expectStock, String expectStates) {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "-------------------------------------------------------------------------------");

        String response = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        int step = 0;

        int latticeId = 0;
        try {
//            1、盘货
            latticeId = stockTaking(aCase, step);

//            3、事件一
            step = 2;
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type, plateCode, chng, total, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(expectStates), expectStock);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "ONE_WRONG_GOODS_INIT")
    public void oneWrongGoodsInitial(int id, String type, long chng, long total, int expectStock, String expectStates) {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "-" + id;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        String response = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        int step = 0;

        int latticeId = 0;
        try {
//            1、盘货
            latticeId = stockTaking(aCase, step);

//            3、事件一
            step = 2;
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, 150, 750, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarmStatesWrongPlace), 6);

//            ----------------------------------变化---------------------------------
//            6、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type, plateCode, chng, total, aCase, step);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(expectStates), expectStock);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "TWO_WRONG_GOODS_7_STEP_INIT")
    public void twoWrongGoodsInitial7Step(int id, String type1, long change1, long total1, String alarm1, int stock1,
                                          String type2, long change2, long total2, String alarm2, int stock2, String desc) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "-" + id;
        String caseDesc = desc;
        logger.info(caseDesc + "--------------------");

        String response = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        int step = 0;

        int latticeId = 0;
        try {
//            1、盘货
            latticeId = stockTaking(aCase, step);

//            3、事件一
            step = 2;
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type1, plateCode, change1, total1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm1), stock1);

//            ----------------------------------变化---------------------------------
//            6、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type2, plateCode, change2, total2, aCase, step);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm2), stock2);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "TWO_WRONG_GOODS_9_STEP_INIT")
    public void twoWrongGoodsInitial9Step(int id, String type1, long change1, long total1, String alarm1, int stock1,
                                          String type2, long change2, long total2, String alarm2, int stock2,
                                          String type3, long change3, long total3, String alarm3, int stock3, String desc) {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "-" + id;
        String caseDesc = desc;
        logger.info(caseDesc + "--------------------");

        String response = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        int step = 0;

        int latticeId = 0;
        try {
//            1、盘货
            latticeId = stockTaking(aCase, step);

//            3、事件一
            step = 2;
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type1, plateCode, change1, total1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm1), stock1);


//            ----------------------------------变化---------------------------------
//            6、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type2, plateCode, change2, total2, aCase, step);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm2), stock2);

//            8、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type3, plateCode, change3, total3, aCase, step);

//            9、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm3), stock3);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "TWO_WRONG_GOODS_11_STEP_INIT")
    public void twoWrongGoodsInitial11Step(int id, String type1, long change1, long total1, String alarm1, int stock1,
                                           String type2, long change2, long total2, String alarm2, int stock2,
                                           String type3, long change3, long total3, String alarm3, int stock3,
                                           String type4, long change4, long total4, String alarm4, int stock4, String desc) {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "-" + id;
        String caseDesc = desc;
        logger.info(caseDesc + "--------------------");

        String response;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        int step = 0;

        int latticeId = 0;
        try {
//            1、盘货
            latticeId = stockTaking(aCase, step);

//            3、事件一
            step = 2;
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type1, plateCode, change1, total1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm1), stock1);


//            ----------------------------------变化---------------------------------
//            6、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type2, plateCode, change2, total2, aCase, step);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm2), stock2);

//            8、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type3, plateCode, change3, total3, aCase, step);

//            9、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm3), stock3);

//            10、事件二
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type4, plateCode, change4, total4, aCase, step);

//            11、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm4), stock4);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.toString();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }


    public String[] formatAlarm(String alarmStr) {
        String[] alarmArr = new String[0];

        if (null == alarmStr || alarmStr.trim().length() < 1) {
        } else {
            alarmArr = alarmStr.split(":");
        }

        return alarmArr;
    }

    private void checkRealtimeListAlarmStates(String response, String testUnit, String[] expectStates) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray floors = responseJo.getJSONObject("data").getJSONArray("floors");
        JSONArray unitList = floors.getJSONObject(0).getJSONArray("unit_list");
        JSONArray alarmStatesJsonArrRes = null;
        boolean existed = false;

        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);
            String unitCodeRes = singleUnit.getString("unit_code");
            if (testUnit.equals(unitCodeRes)) {
                existed = true;
                alarmStatesJsonArrRes = singleUnit.getJSONArray("alarm_states");
                break;
            }
        }

        if (!existed) {
            Assert.fail("checkAlarmStates failed,unit:" + testUnit + " is not existed!");
        }

        int size = alarmStatesJsonArrRes.size();
        String[] alarmStatesRes = new String[size];
        for (int i = 0; i < size; i++) {
            alarmStatesRes[i] = alarmStatesJsonArrRes.getString(i);
        }

        Assert.assertEquals(alarmStatesRes, expectStates);
    }

    private void checkRealtimeListStocktakingStates(String response, String testUnit) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray floors = responseJo.getJSONObject("data").getJSONArray("floors");
        JSONArray unitList = floors.getJSONObject(0).getJSONArray("unit_list");
        boolean existed = false;

        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);
            String unitCodeRes = singleUnit.getString("unit_code");
            if (testUnit.equals(unitCodeRes)) {
                existed = true;
                JSONArray alarmStates = singleUnit.getJSONArray("alarm_states");
                String message = "盘货完成，“平面图货架列表”报警状态没有置空！";
                Assert.assertEquals(alarmStates.size(), 0, message);
                String stocktakingState = singleUnit.getString("stocktaking_state");
                message = "盘货完成，“平面图货架列表”的stocktaking_state没有置为FINISHED！";
                Assert.assertEquals(stocktakingState, "FINISHED", message);
            }
        }

        if (!existed) {
            Assert.fail("checkRealtimeListStocktakingStates failed,unit:" + testUnit + " is not existed!");
        }
    }

    private void checkUnitDetailAlarmStates(String response, int litticeId, String[] expectStates) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray latticeList = responseJo.getJSONObject("data").getJSONArray("lattice_list");

        JSONArray alarmStatesJsonArr = null;
        boolean existed = false;

        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLittice = latticeList.getJSONObject(i);
            int litticeIdRes = singleLittice.getInteger("lattice_id");
            if (litticeId == litticeIdRes) {
                existed = true;
                alarmStatesJsonArr = singleLittice.getJSONArray("alarm_states");
            }
        }

        if (!existed) {
            Assert.fail("checkUnitDetailAlarmStates failed, lattice_id: " + litticeId + " is not existed!");
        }

        int size = alarmStatesJsonArr.size();
        String[] alarmStatesRes = new String[size];
        for (int i = 0; i < alarmStatesJsonArr.size(); i++) {
            alarmStatesRes[i] = alarmStatesJsonArr.getString(i);
        }

        Assert.assertEqualsNoOrder(alarmStatesRes, expectStates);
    }

    private void checkUnitDetailStocktakingStates(String response, int latticeId, long goodsStock) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray latticeList = responseJo.getJSONObject("data").getJSONArray("lattice_list");
        boolean existed = false;

        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLattice = latticeList.getJSONObject(i);
            int latticeIdRes = singleLattice.getInteger("lattice_id");
            if (latticeId == latticeIdRes) {
                existed = true;
                JSONArray alarmStates = singleLattice.getJSONArray("alarm_states");
                String message = "盘货完成，“货架单元详情”的alarm_states没有置空！";
                Assert.assertEquals(alarmStates.size(), 0, message);

                String stocktakingState = singleLattice.getString("stocktaking_state");
                message = "盘货完成，“货架单元详情”的stocktaking_state没有置为 FINISHED！";
                Assert.assertEquals(stocktakingState, "FINISHED", message);

                long goodsStockRes = singleLattice.getLong("goods_stock");
                message = "“货架单元详情”中的goods_stock与盘货设置的goods_stock不符！";
                Assert.assertEquals(goodsStockRes, goodsStock, message);
            }
        }

        if (!existed) {
            Assert.fail("checkUnitDetailStocktakingStates failed, lattice_id: " + latticeId + " is not existed!");
        }
    }

    private JSONArray checkLatticeDetailAlarmStates(String response) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray alarmStates = responseJo.getJSONObject("data").getJSONArray("alarm_states");

        return alarmStates;
    }

    private void checkLatticeDetailGoodsStock(String response, long goodsStock) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray alarmStates = responseJo.getJSONObject("data").getJSONArray("alarm_states");
        String message = "盘货完成，该单元格的alarm_states没有置空！";
        Assert.assertEquals(alarmStates.size(), 0, message);

        long goodsStockRes = responseJo.getJSONObject("data").getLong("goods_stock");
        message = "“单元格物品详情”中的goods_stock与盘货设置的goods_stock不符！";
        Assert.assertEquals(goodsStockRes, goodsStock, message);

    }

    private void checkAlarmState(JSONArray alarmState, int expectSize, String[] expectState, String message) {

        Assert.assertEquals(alarmState.size(), expectSize, message);

        String[] stateRes = new String[alarmState.size()];
        for (int i = 0; i < alarmState.size(); i++) {
            String singleState = alarmState.getString(i);
            stateRes[i] = singleState;
        }

        message += "--expect: " + Arrays.toString(expectState) + ", actual: " + Arrays.toString(stateRes);

        Assert.assertEqualsNoOrder(stateRes, expectState, message);
    }


    private int checkUnitDetail(String response, int expectNum) {
        JSONArray latticeList = JSON.parseObject(response).getJSONObject("data").getJSONArray("lattice_list");
        String message = "checkUnitDetail - lattice quantity is wrong，expect：" + expectNum + ",actual: " + latticeList.size();
        Assert.assertEquals(latticeList.size(), expectNum, message);

        int latticeId = latticeList.getJSONObject(0).getInteger("lattice_id");

        return latticeId;
    }

//    private void checkUnbindRealTimeList(String response) {
//
//        String message = "解绑后，平面图货架列表";
//        JSONObject singleFloor = JSON.parseObject(response).getJSONObject("data").getJSONArray("floors").getJSONObject(0);
//        JSONArray unitList = singleFloor.getJSONArray("unit_list");
//        for (int i = 0; i < unitList.size(); i++) {
//            JSONObject singleUnit = unitList.getJSONObject(i);
//
//            JSONArray alarmStates = singleUnit.getJSONArray("alarm_states");
//            Assert.assertEquals(alarmStates.size(), 0, message + "告警状态没有置空！");
//
//            String stocktakingStates = singleUnit.getString("stocktaking_state");
//            Assert.assertEquals(stocktakingStates, "FINISHED", "没有将stocktaking_state置成FINISHED");
//        }
//    }

    private void checkUnbindRealTimeList(String response, String unitCode) {

        boolean isExist = false;
        String message = "解绑后，平面图货架列表";
        JSONObject singleFloor = JSON.parseObject(response).getJSONObject("data").getJSONArray("floors").getJSONObject(0);
        JSONArray unitList = singleFloor.getJSONArray("unit_list");
        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);
            if (unitCode.equals(singleUnit.getString("unit_code"))) {
                isExist = true;
                JSONArray alarmStates = singleUnit.getJSONArray("alarm_states");
                Assert.assertEquals(alarmStates.size(), 0, message + "告警状态没有置空！");

                String stocktakingStates = singleUnit.getString("stocktaking_state");
                Assert.assertEquals(stocktakingStates, "FINISHED", "没有将stocktaking_state置成FINISHED");
            }
        }

        Assert.assertEquals(isExist, true, "UNIT_CODE: " + unitCode + " is not exist!");
    }

    private void checkUnbindUnitDetail(String response, int latticeId) {
        boolean isExist = false;
        String message = "解绑后，货架单元详情";
        JSONArray latticeList = JSON.parseObject(response).getJSONObject("data").getJSONArray("lattice_list");
        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLattice = latticeList.getJSONObject(i);

            if (latticeId == singleLattice.getInteger("lattice_id")) {
                isExist = true;
                JSONArray alarmStates = singleLattice.getJSONArray("alarm_states");
                Assert.assertEquals(alarmStates.size(), 0, message + "告警状态没有置空！");

                String stocktakingStates = singleLattice.getString("stocktaking_state");
                Assert.assertEquals(stocktakingStates, "FINISHED", message + "没有将stocktaking_state置成FINISHED");
            }
        }

        Assert.assertEquals(isExist, true, "latticeId: " + latticeId + " is not exist!");
    }

    private void checkUnbindLatticeDetail(String response) {

        String message = "解绑后，货架单元详情的data字段不为空！";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        Assert.assertEquals(data.size(), 0, message);
    }

    private void checkRealTimeList(String response, String unitCode, String stocktakingStateExpect) {

        boolean isExist = false;
        String message = "理货后，平面图货架列表";
        JSONObject singleFloor = JSON.parseObject(response).getJSONObject("data").getJSONArray("floors").getJSONObject(0);
        JSONArray unitList = singleFloor.getJSONArray("unit_list");
        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);
            String unitCodeRes = singleUnit.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                isExist = true;
                JSONArray alarmStates = singleUnit.getJSONArray("alarm_states");
                Assert.assertEquals(alarmStates.size(), 0, message + "告警状态没有置空！");

                String stocktakingStates = singleUnit.getString("stocktaking_state");
                Assert.assertEquals(stocktakingStates, stocktakingStateExpect, "没有将stocktaking_state置成" + stocktakingStateExpect);
            }
        }

        Assert.assertEquals(isExist, true, "UNIT_CODE: " + unitCode + " is not exist!");
    }

    private void checkAlarmStateAndGoodsStockByUnitDetail(String response, int litticeId, String[] alarmStates, int goodsStock) {

        JSONObject responseJo = JSON.parseObject(response);
        JSONArray latticeList = responseJo.getJSONObject("data").getJSONArray("lattice_list");
        boolean existed = false;

        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLittice = latticeList.getJSONObject(i);
            int litticeIdRes = singleLittice.getInteger("lattice_id");
            if (litticeId == litticeIdRes) {
                existed = true;

                JSONArray alarmStatesArrRes = singleLittice.getJSONArray("alarm_states");
                int len = alarmStatesArrRes.size();
                String[] alarmStatesArr = new String[len];
                for (int i1 = 0; i1 < len; i1++) {
                    alarmStatesArr[i1] = alarmStatesArrRes.getString(i1);
                }

                Assert.assertEqualsNoOrder(alarmStatesArr, alarmStates, "expect:" + Arrays.toString(alarmStates) + ",actual:" + Arrays.toString(alarmStatesArr));

                int goodsStockRes = singleLittice.getInteger("goods_stock");
                Assert.assertEquals(goodsStockRes, goodsStock, "goodsStock--expect:" + goodsStock + ",actual:" + goodsStockRes);
            }
        }

        if (!existed) {
            Assert.fail("checkUnitDetailAlarmStates failed, lattice_id: " + litticeId + " is not existed!");
        }
    }

    private void checkAlarmStateAndGoodsStockByLatticeDetail(String response, String[] alarmStates, int goodsStock) {

        JSONObject responseJo = JSON.parseObject(response);
        JSONArray alarmStatesArrRes = responseJo.getJSONObject("data").getJSONArray("alarm_states");
        int len = alarmStatesArrRes.size();
        String[] alarmStatesArr = new String[len];
        for (int i1 = 0; i1 < len; i1++) {
            alarmStatesArr[i1] = alarmStatesArrRes.getString(i1);
        }
        Assert.assertEqualsNoOrder(alarmStatesArr, alarmStates, "expect:" + Arrays.toString(alarmStates) + ",actual:" + Arrays.toString(alarmStatesArr));

        long goodsStockRes = responseJo.getJSONObject("data").getLong("goods_stock");
        Assert.assertEquals(goodsStockRes, goodsStock, "expect:" + goodsStock + ",actual:" + goodsStockRes);
    }


    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, message + " expect:" + expect + ",actual:" + code);
    }

    private void checkCodeApi(ApiResponse apiResponse, String router, int expectCode) throws Exception {
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

    private ApiResponse sendRequestWithGate(String router, String[] resource, String json) throws Exception {
        try {
            Credential credential = new Credential(AK, SK);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_CODE)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            apiResponse = apiClient.doRequest(apiRequest);
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

    private String sendRequestWithUrl(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private String sendRequestWithHeader(String serviceId, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(URL_prefix + serviceId, json, header);
        return executor.getResponse();
    }

    void genAuth() {

        String json =
                "{" +
                        "  \"email\": \"demo@winsense.ai\"," +
                        "  \"password\": \"fe01ce2a7fbac8fafaed7c982a04e229\"\n" +
                        "}";
        try {
            response = sendRequestWithUrl(genAuthURL, json, header);
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authorization = data.getString("token");

            header.put("Authorization", authorization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
        aCase.setExpect("code==1000");
    }

    @BeforeSuite
    public void initial() throws Exception {
        genAuth();
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    @DataProvider(name = "CHECK_TYPE")
    private static Object[] checkType() {
        return new String[]{
                "TALLY", "STOCKTAKING"
        };
    }

    @DataProvider(name = "TALLY&STOCKTAKING_WITH_SCAN")
    private static Object[][] tallyAndStocktakingWithScan() {
        //checkType, changeP, totalP, changeD, totalD, bindingStock, bindingTotal, expectBinding, expectUnitStocktaking, expectCodeBinding，expectCodeUnitStockTaking
        return new Object[][]{
                new Object[]{"TALLY", -100, 0, 100, 100, 3, 300, true, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "TALLY-1"},//单元盘货是成功的，因为没绑定成功就相当于
//                                                                                                                该单元没有物品，就是已盘状态
                new Object[]{"TALLY", -100, 0, 100, 100, 3, 300, false, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "TALLY-2"},
                new Object[]{"TALLY", -100, 0, 50, 120, 3, 300, true, false, StatusCode.SUCCESS, StatusCode.INTERNAL_SERVER_ERROR, "TALLY-3"},
                new Object[]{"STOCKTAKING", -100, 500, 100, 600, 6, 600, true, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "STOCKTAKING-1"},
                new Object[]{"STOCKTAKING", -100, 0, 100, 100, 3, 300, false, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "STOCKTAKING-2"},
                new Object[]{"STOCKTAKING", -100, 0, 50, 120, 3, 300, true, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "STOCKTAKING-3"}
        };
    }

    @DataProvider(name = "STOCKTAKING_WITHOUT_SCAN")
    private static Object[][] tallyAndStocktakingWithoutScan() {
        //checkType, changeD, totalD, bindingStock, bindingToatal, LatticeBindingExpectCode, unitStocktakingExpectCode,
        // type, chng, total, alarm, stock, unitStocktakingExpectCode, caseId
        return new Object[][]{
                new Object[]{"STOCKTAKING", 100, 300, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typePick, -100, 200, alarmStatesOutofStock, 2, StatusCode.SUCCESS, "STOCKTAKING-1"},
                new Object[]{"STOCKTAKING", 100, 100, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typePick, -100, 0, alarmStatesOutofStock, 0, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-2"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typePick, -50, 70, alarmStatesOutofStock, 2, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-3"},
                new Object[]{"STOCKTAKING", 100, 300, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 100, 400, "", 4, StatusCode.SUCCESS, "STOCKTAKING-4"},
                new Object[]{"STOCKTAKING", 100, 100, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 100, 200, "", 4, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-5"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 50, 220, "", 4, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-6"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 50, 230, "", 4, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-7"},
        };
    }

    @DataProvider(name = "TALLY&STOCKTAKING_RESULT")
    private static Object[][] testTallyAndStocktakingResult() {
        //checkType, Pchng, Ptotal, Dchng, Dtotal, bindingStock, bindingToatal,
        return new Object[][]{
                new Object[]{"TALLY", -100, 400, 100, 500, 5, 500},
                new Object[]{"STOCKTAKING", -100, 400, 100, 500, 5, 500},
        };
    }

    @DataProvider(name = "NORMAL_INIT")
    private static Object[][] normalInit() {
        return new Object[][]{
//                type, change, expectStock,expectStates
                new Object[]{typeDrop, 150, 750, 6, alarmStatesWrongPlace},
                new Object[]{typeDrop, 50, 650, 6, alarmStatesWrongPlace}
        };
    }

    @DataProvider(name = "ONE_WRONG_GOODS_INIT")
    private static Object[][] oneWrongGoodsInit() {
        return new Object[][]{
//                type, change, expectStock,expectStates
                new Object[]{1, typePick, -100, 650, 5, alarmStatesWrongPlace},
                new Object[]{2, typePick, -300, 450, 3, alarmStatesWrongPlace},
                new Object[]{3, typePick, -150, 600, 6, ""},
                new Object[]{4, typePick, -250, 500, 5, ""},
                new Object[]{5, typePick, -450, 300, 3, ""},
        };
    }

    @DataProvider(name = "TWO_WRONG_GOODS_7_STEP_INIT")
    private static Object[][] twoWrongGoods6StepInit() {
        return new Object[][]{
//                id，type，change1,total1，alarm1,stock1, type2, change2, total2, alarm2,stock2
                new Object[]{1, typeDrop, 150, 750, alarmStatesWrongPlace, 6, typePick, -150, 600, "", 6,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 150=70+80g）\n-" +
                                "5.单元格物品详情（理货，库存6）-6.事件通知（pick 150g）-7.单元格物品详情（正常，库存6）\n"},
                new Object[]{2, typeDrop, 150, 750, alarmStatesWrongPlace, 6, typePick, -170, 580, alarmStatesWrongPlace, 5,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 150=70+80g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（pick 170=100+70g）-7.单元格物品详情（理货，库存5）"},
                new Object[]{3, typeDrop, 150, 750, alarmStatesWrongPlace, 6, typePick, -100, 650, alarmStatesWrongPlace, 5,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 150=70+80g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（pick 100g）-7.单元格物品详情（理货，库存5）"},
                new Object[]{4, typeDrop, 250, 850, alarmStatesWrongPlace, 6, typePick, -250, 600, "", 6,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 250=170+80g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（pick 250g）-7.单元格物品详情（正常，库存6）"},
                new Object[]{5, typeDrop, 200, 800, "", 8, typePick, -150, 650, alarmStatesWrongPlace, 6,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 200=150+50g）\n" +
                                "-5.单元格物品详情（正常，库存8）-6.事件通知（pick 150g）-7.单元格物品详情（理货，库存6）"},
                new Object[]{6, typeDrop, 200, 800, "", 8, typePick, -50, 750, alarmStatesWrongPlace, 6,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 200=150+50g）\n" +
                                "-5.单元格物品详情（正常，库存8）-6.事件通知（pick 50g）-7.单元格物品详情（理货，库存6）"},

        };
    }

    @DataProvider(name = "TWO_WRONG_GOODS_9_STEP_INIT")
    private static Object[][] twoWrongGoods8StepInit() {
        return new Object[][]{
//                id,type1, change1, total1,expectStates1,expectStock1,type2,change2,total2,alarm2,stock2,type3,change3,total3,alarm3,stock3
                new Object[]{1, typeDrop, 150, 750, alarmStatesWrongPlace, 6, typePick, -70, 680, alarmStatesWrongPlace, 6, typePick, -80, 600, "", 6,
                        "1. 事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 150=70+80g）\n-" +
                                "5.单元格物品详情（理货，库存6）-6.事件通知（pick 70g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 80g）" +
                                "\n-9.单元格物品详情（正常，库存6）\n"},
                new Object[]{2, typeDrop, 250, 850, alarmStatesWrongPlace, 6, typePick, -170, 680, alarmStatesWrongPlace, 6, typePick, -80, 600, "", 6,
                        "1.事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 250=170+80g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（pick 170g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 80g）\n" +
                                "-9.单元格物品详情（正常，库存6）"},
                new Object[]{3, typeDrop, 250, 850, alarmStatesWrongPlace, 6, typePick, -80, 770, alarmStatesWrongPlace, 6, typePick, -170, 600, "", 6,
                        "1.事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 250=170+80g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（pick 80g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 170g）\n" +
                                "-9.单元格物品详情（正常，库存6）"},
                new Object[]{4, typeDrop, 150, 750, alarmStatesWrongPlace, 6, typeDrop, 50, 800, alarmStatesWrongPlace, 6, typePick, -200, 600, "", 6,
                        "1.事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 150g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（drop 50g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 200g）\n" +
                                "-9.单元格物品详情（正常，库存6）"},
                new Object[]{5, typeDrop, 70, 670, alarmStatesWrongPlace, 6, typeDrop, 80, 750, alarmStatesWrongPlace, 6, typePick, -150, 600, "", 6,
                        "1.事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 70g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.drop 80g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 150g）\n" +
                                "-9.单元格物品详情（正常，库存6）"},

        };
    }

    @DataProvider(name = "TWO_WRONG_GOODS_11_STEP_INIT")
    private static Object[][] twoWrongGoods10StepInit() {
        return new Object[][]{
//                id,type1, change1, total1,alarm1,stock1,change2,total2,alarm2,stock2,type3, change3, total3,alarm3,stock3,change4,total4,alarm4,stock4,
                new Object[]{1, typeDrop, 150, 750, alarmStatesWrongPlace, 6, typeDrop, 50, 800, alarmStatesWrongPlace, 6,
                        typePick, -150, 650, alarmStatesWrongPlace, 6, typePick, -50, 600, "", 6,
                        "1.事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 150g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（drop 50g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 150g）\n" +
                                "-9.单元格物品详情（理货，库存6）-10.事件通知（pick 50g）-11.单元格物品详情（正常，库存为6）"},
                new Object[]{2, typeDrop, 70, 670, alarmStatesWrongPlace, 6, typeDrop, 80, 750, alarmStatesWrongPlace, 6,
                        typePick, -70, 680, alarmStatesWrongPlace, 6, typePick, -80, 600, "", 6,
                        "1.事件通知（drop 100g，total 600）-2.单元格物品绑定（stock 6）-3.心跳-4.事件通知（drop 70g）\n" +
                                "-5.单元格物品详情（理货，库存6）-6.事件通知（drop 80g）-7.单元格物品详情（理货，库存6）-8.事件通知（pick 70g）\n" +
                                "-9.单元格物品详情（理货，库存6）-10.事件通知（pick 80g）-11.单元格物品详情（正常，库存为6）"},
        };
    }
}
