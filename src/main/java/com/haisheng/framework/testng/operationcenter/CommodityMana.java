package com.haisheng.framework.testng.operationcenter;

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
import com.haisheng.framework.model.bean.Shelf;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.testng.service.CsvDataProvider;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class CommodityMana {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_87803c0c";
    private static String APP_CODE = "7485a90349a2";
    private static String AK = "8da9aeabd74198b1";
    private static String SK = "ec44b94f9b3cf4333c5d000781cb0289";
    private String UNIT_CODE = "QA-TEST【勿动】";
    private String SHELVES_CODE = "QA-TEST【勿动】";
    private String PLATE_CODE = "666";

    private String UNIT_CODE_1 = "QA-TEST-1【勿动】";
    private String SHELVES_CODE_1 = "QA-TEST-1【勿动】";
    private String PLATE_CODE_1 = "912";

    private static long SHOP_ID = 477;
    private String genAuthURL = "http://dev.sso.winsenseos.com/sso/login";

    private String URL_prefix = "http://dev.app.winsenseos.com/operation/app/COMMODITY/";
    private String realTimeListServiceId = "3015";
    private String unitDetailServiceId = "3016";
    private String latticeDetailServiceId = "3017";
    private String latticeCheckServiceId = "3018";
    private String latticeBindingServiceId = "3019";
    private String unitstockingFinishServiceId = "3020";
    private String shopstockingFinishServiceId = "3021";
    private String latticeUnbindServiceId = "3022";
    private String createUnitServiceId = "3574";
    private String deleteUnitServiceId = "3576";
    private String createConfigServiceId = "3575";
    private String createSensorServiceId = "3579";//创建感压板

    private static String typePick = "PICK";
    private static String typeDrop = "DROP";

    private static String checkTypeTally = "TALLY";
    private static String checkTypeStocktaking = "STOCKTAKING";

    private static String alarmStatesOutAndSensor = "OUT_OF_STOCK:SENSOR_ERROR";
    private static String alarmStatesOutofStock = "OUT_OF_STOCK";
    private static String alarmStatesCamereError = "CAMERA_ERROR";
    private static String alarmStatesSensorError = "SENSOR_ERROR";
    private static String alarmStatesWrongAndSensor = "WRONG_PLACE:SENSOR_ERROR";
    private static String alarmStatesWrongPlace = "WRONG_PLACE";
    private static String alarmStatesWrongAndOutAndSensor = "WRONG_PLACE:OUT_OF_STOCK:SENSOR_ERROR";
//    private static String alarmStatesWrongAndOut = "WRONG_PLACE:OUT_OF_STOCK";

    HashMap<String, String> header = new HashMap();

    private String authorization = null;

    private long goodsId3Add2 = 139;
//    private long goodsIdOreo = 141;
//    private long goodsIdGanten = 142;

    private ApiResponse apiResponse;
    private String response;

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;
    private String CI_CMD = "curl -X POST http://shelf:shelf@192.168.50.2:8080/job/commodity-management/buildWithParameters?case_name=";

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
                append("            \"status\": \"SENSOR_RUNNING\"").
                append("          },").
                append("          {").
                append("            \"plate_code\":\"").append(plateCode).append("\",").
                append("            \"sensor_id\": 1,").
                append("            \"k\": 2.02,").
                append("            \"zero\": 9876.54321,").
                append("            \"status\": \"SENSOR_RUNNING\"").
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
            latticeCheckRes = latticeCheck(1, 1, checkType, aCase, step);
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
    //-------------（3）---------------1、事件通知(pick one)-2、商品扫描-3、货架事件通知(drop )-
    //---------------------------------4、单元格物品绑定-5、单元盘货完成-------------
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

    //--------------------------测试总重对单元盘货成功与否的影响---------------------------
    //--------(4)-------------------1、货架事件通知（drop）-2、单元格物品绑定-3、单元盘货完成-4、货架事件通知
    // -----------------------------5、货架单元详情-6、单元格物品详情-7、单元盘货完成----------------
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

            aCase.setRequestData("1、货架事件通知（drop）-2、单元格物品绑定-3、单元盘货完成-4、货架事件通知" +
                    "5、货架单元详情-6、单元格物品详情-7、单元盘货完成" + "\n\n");
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

//            4、货架事件通知
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type, plateCode, chng, total, aCase, step);

//            5、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm), stock);

//            6、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm), stock);

//            7、单元盘货
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
    //--------(5)-------------------1、通知(pick one)-2、扫描（盘、理）-3、通知（drop）-4、绑定（同扫描）---------------------
    //------------------------------5、单元盘货-6、心跳-7、平面图货架列表-8、货架单元详情-9、单元格物品详情----------------
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
            ;
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
    //--------(6)-------------------1、通知(pick one)-2、扫描(盘、理)-3、通知（drop）------------------------
    //------------------------------4、绑定（同扫描）-5、单元盘货-6、解绑-7、心跳-8、平面图货架列表----------------------
    //-----------------------------9、货架单元详情-10、单元格物品详情----------------
//    @Test(dataProvider = "UNBIND_RESULT")
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
    //------------------------------1、通知（drop 3）-2、绑定（盘）-3、通知（drop 1）-4、平面图货架详情-5、店铺盘货-------------------------------------------------

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

            aCase.setRequestData("1、通知（drop 3）-2、绑定（盘）-3、通知（drop 1）-4、平面图货架详情-5、店铺盘货" + "\n\n");
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

//            4、平面图货架详情
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

    //----------------(10)---------测试一个店铺有两个单元，一个盘货，一个未盘货，那么店铺盘货失败---------------------------------------------------
    //------------------------------1、通知(pick one)-2、扫描-3、通知（drop）----------------------------------------------------------
    //------------------------------4、绑定(理)-5、单元盘货完成-6、创建单元-7、通知（drop 3）--------------------------------------------------
    // -----------------------------8、绑定（盘）-9、通知（drop 1）-10、店铺盘货------------------------------------------------------------------------------

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
    //-----------------(11)---------------1、通知(drop one)-2、绑定一个商品(盘货)-3、心跳-----------------------
    ////----------------------------------4、列表(期待缺货)-5、单元详情（期待缺货）-6、物品详情（期待缺货）-----------------------------
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

    //----(12)---------1、通知(drop 3)-2、绑定(3个)-3、通知(pick one)-4、心跳-5、列表(还剩2个，缺货)------------------------
    //---------------- 6、通知(pick 2，此时拿空了)-7、心跳-8、列表(拿空了，缺货)-9、单元详情（缺货）-10、物品详情（缺货）----------------

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

    //----(13)-----在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态；
    //                                (3)再拿走一个正确商品，观察状态；(4)再拿走一个正确商品观察状态

//"1.货架事件通知（drop 3）-2.单元格物品绑定（绑定3个）-3.货架事件通知（drop 1 不同商品 剩余3对1错）-4.心跳\n" +
//        "5.平面图货架列表（理货）-6.货架单元详情（理货，库存3）-7.单元格物品详情（理货，库存3）-8.货架事件通知（pick 1正确商品 剩余2对1错）\n" +
//        "9.心跳-10.平面图货架列表（理货）-11.货架单元详情（理货，缺货，库存2）-12.单元格物品详情（理货，缺货，库存2）\n" +
//        "13.货架事件通知（pick 1正确商品 剩余1对1错）-14.心跳-15.平面图货架列表（理货）-16.货架单元详情（理货，缺货，库存1）-\n" +
//        "17.单元格物品详情（理货，缺货，库存1）-18.货架事件通知（pick 1错误商品 剩余1对）-19.心跳-20.平面图货架列表（缺货）\n" +
//        "21.货架单元详情（理货，库存0）-22.单元格物品详情（理货，库存0）" + "\n\n"

    @Test(dataProvider = "CHECK_TYPE")
    private void TestdropWrongGoods(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + checkType;
        String caseDesc = "在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态 (3)再拿走一个正确商品，观察状态；" +
                "(4)再拿走一个正确商品观察状态";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long bindingStock = 3L;
        long bindingTotal = 300L;
        long Dchng1 = 50L;
        long Dtotal1 = 350L;
        long Pchng1 = -100L;
        long Ptotal1 = 250L;
        long Pchng2 = -100L;
        long Ptotal2 = 150L;
        long Pchng3 = -100L;
        long Ptotal3 = 50L;

        String response = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesOutofStock, alarmStatesWrongPlace};
        String[] alarmStates3 = {alarmStatesOutofStock, alarmStatesWrongPlace};
        String[] alarmStates4 = {alarmStatesOutofStock, alarmStatesWrongPlace};

        int step = 0;

        try {
            aCase.setRequestData("1.货架事件通知（drop 3）-2.单元格物品绑定（绑定3个）-3.货架事件通知（drop 1 不同商品 剩余3对1错）-4.心跳\n" +
                    "5.平面图货架列表（理货）-6.货架单元详情（理货，库存3）-7.单元格物品详情（理货，库存3）-8.货架事件通知（pick 1正确商品 剩余2对1错）\n" +
                    "9.心跳-10.平面图货架列表（理货）-11.货架单元详情（理货，缺货，库存2）-12.单元格物品详情（理货，缺货，库存2）\n" +
                    "13.货架事件通知（pick 1正确商品 剩余1对1错）-14.心跳-15.平面图货架列表（理货）-16.货架单元详情（理货，缺货，库存1）-\n" +
                    "17.单元格物品详情（理货，缺货，库存1）-18.货架事件通知（pick 1错误商品 剩余1对）-19.心跳-20.平面图货架列表（缺货）\n" +
                    "21.货架单元详情（理货，库存0）-22.单元格物品详情（理货，库存0）" + "\n\n");

//            1、货架事件通知(drop 3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            String unitDetailRes = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(unitDetailRes, 1);

//            2、单元格物品绑定(绑定3个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(drop one different good)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng1, Dtotal1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、平面图货架列表(此时有 3个正确 1个错误  期待理货)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = realTimeList(aCase, step);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---6、realTimeList");
            checkRealtimeListAlarmStates(response, unitCode, alarmStates1);

//            6、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates1, 3);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates1, 3);

//            8、通知(pick 一个正确商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            9、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            10、平面图货架列表(还剩2个正确 1个错误 期待理货)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = realTimeList(aCase, step);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---9、realTimeList");

            checkRealtimeListAlarmStates(response, unitCode, alarmStates2);

//            11、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates2, 2);

//            12、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates2, 2);

//            13、货架事件通知(pick 一个正确商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng2, Ptotal2, aCase, step);

//            14、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            15、平面图货架列表(此时还剩1个正确 1个错误 期待理货和缺货)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = realTimeList(aCase, step);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---12、realTimeList");

            checkRealtimeListAlarmStates(response, unitCode, alarmStates3);

//            16、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates3, 1);

//            17、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates3, 1);

//            18、货架事件通知(pick 一个错误商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng3, Ptotal3, aCase, step);

//            19、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            20、平面图货架列表(此时还剩一个错误商品 期待理货)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = realTimeList(aCase, step);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---15、realTimeList");

            checkRealtimeListAlarmStates(response, unitCode, alarmStates4);

//            21、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates4, 0);

//            22、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates4, 0);

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

    //----(14)-----在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态；
    //                                (3)再拿走一个错误商品，观察状态；
//    1、通知(drop 3)2、绑定(3个)-3、通知(drop 1错， 3对1错)-4、心跳-5、单元详情(理货 库存为3)-6、单元格详情（理货 库存为3）" +
//            "7、通知(pick 1正确的， 2对1错)-8、心跳-9、单元详情（理货 库存为2）-10、单元格详情(理货 库存为2)-11、通知(pick 1错的，2对)-" +
//            "12、心跳-13、单元详情（缺货 库存为2 ）-14、单元格物品详情(缺货 库存为2 )
    @Test(dataProvider = "CHECK_TYPE")
    private void TestGoodsStock(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + checkType;
        String caseDesc = "在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态-(3)再拿走一个错误商品，观察状态；";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long bindingStock = 3L;
        long bindingTotal = 300L;
        long Dchng1 = 50L;
        long Dtotal1 = 350L;
        long Pchng1 = -100L;
        long Ptotal1 = 250L;
        long Pchng2 = -50L;
        long Ptotal2 = 200L;

        String latticeBindingRes;
        String unitDetailRes1;
        String latticeDetailRes1;
        String unitDetailRes2;
        String latticeDetailRes2;
        String unitDetailRes3;
        String latticeDetailRes3;

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesOutofStock, alarmStatesWrongPlace};
        String[] alarmStates3 = {alarmStatesOutofStock};

        int step = 0;

        try {

            aCase.setRequestData("1、通知(drop 3)2、绑定(3个)-3、通知(drop 1错， 3对1错)-4、心跳-5、单元详情(理货 库存为3)-6、单元格详情（理货 库存为3）" +
                    "7、通知(pick 1正确的， 2对1错)-8、心跳-9、单元详情（理货 库存为2）-10、单元格详情(理货 库存为2)-11、通知(pick 1错的，2对)-" +
                    "12、心跳-13、单元详情（缺货 库存为2 ）-14、单元格物品详情(缺货 库存为2 )" + "\n\n");
            aCase.setRequestData("");

//            1、货架事件通知(drop 3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            String unitDetailRes = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(unitDetailRes, 1);

//            2、单元格物品绑定(绑定3个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeBindingRes = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(drop one different good)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng1, Dtotal1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、货架单元详情(理货 stock为3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes1 = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(unitDetailRes1, latticeId, alarmStates1, 3);

//            6、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes1 = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(latticeDetailRes1, alarmStates1, 3);

//            7、通知(pick 一个正确商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            8、货架单元详情(理货 stock为2)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes2 = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(unitDetailRes2, latticeId, alarmStates2, 2);

//            9、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            10、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes2 = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(latticeDetailRes2, alarmStates2, 2);

//            11、货架事件通知(pick 一个错误商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng2, Ptotal2, aCase, step);

//            12、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            13、货架单元详情(缺货，库存为2)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            unitDetailRes3 = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(unitDetailRes3, latticeId, alarmStates3, 2);

//            14、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            latticeDetailRes3 = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(latticeDetailRes3, alarmStates3, 2);

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

//------------（15）--------在有三个正确商品时，(1)放置一个错误商品（3对1错）；(2)然后拿走一个正确商品，观察状态（2对1错）；----------------------
//-----------------------(3)再放置一个正确商品（3对1错）；（4）再拿走一个错误商品，观察状态（3对0错)----------------------------------------

//1.货架事件通知（drop 3）-2.单元格物品绑定（绑定3个）-3.货架事件通知（drop 1 不同商品 剩余3对1错）\n" +
//            "4.心跳-5.货架单元详情（理货，库存为3）-6.单元格物品详情（理货，库存为3）-7.货架事件通知（pick 1正确商品 剩余2对1错）\n" +
//            "8.心跳-9.货架单元详情（理货，库存为2）-10.单元格物品详情（理货，库存为2）-11.货架事件通知（drop 1正确商品 剩余3对1错）\n" +
//            "12.心跳-13.货架单元详情（理货，库存为3）-14.单元格物品详情（理货，库存为3）-15.货架事件通知（pick 1错误商品 剩余3对）\n" +
//            "16.心跳-17.货架单元详情（正常，库存为3）-18.单元格物品详情（正常，库存为3）

    @Test
    private void TestWrongToRight() throws Exception {
        String checkType = checkTypeStocktaking;
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "在有三个正确商品时，(1)放置一个错误商品（3对1错）；(2)然后拿走一个正确商品，观察状态（2对1错）；\n" +
                "(3)再放置一个正确商品（3对1错）；（4）再拿走一个错误商品，观察状态（3对0错)";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long bindingStock = 3L;
        long bindingTotal = 300L;
        long Dchng1 = 50L;
        long Dtotal1 = 350L;
        long Pchng1 = -100L;
        long Ptotal1 = 250L;
        long Dchng2 = 100L;
        long Dtotal2 = 350L;
        long Pchng2 = -50L;
        long Ptotal2 = 300L;

        String response = "";

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesOutofStock, alarmStatesWrongPlace};
        String[] alarmStates3 = {alarmStatesWrongPlace};
        String[] alarmStates4 = new String[0];

        int step = 0;
        try {

            aCase.setRequestData("1.货架事件通知（drop 3）-2.单元格物品绑定（绑定3个）-3.货架事件通知（drop 1 不同商品 剩余3对1错）\n" +
                    "4.心跳-5.货架单元详情（理货，库存为3）-6.单元格物品详情（理货，库存为3）-7.货架事件通知（pick 1正确商品 剩余2对1错）\n" +
                    "8.心跳-9.货架单元详情（理货，库存为2）-10.单元格物品详情（理货，库存为2）-11.货架事件通知（drop 1正确商品 剩余3对1错）\n" +
                    "12.心跳-13.货架单元详情（理货，库存为3）-14.单元格物品详情（理货，库存为3）-15.货架事件通知（pick 1错误商品 剩余3对）\n" +
                    "16.心跳-17.货架单元详情（正常，库存为3）-18.单元格物品详情（正常，库存为3）" + "\n\n");

//            1、货架事件通知(drop 3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(response, 1);

//            2、单元格物品绑定(绑定3个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(drop one different goods)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng1, Dtotal1, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、货架单元详情(理货 stock为3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates1, 3);

//            6、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates1, 3);

//            7、通知(pick 一个正确商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            8、货架单元详情(理货 stock为2)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates2, 2);

//            9、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            10、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates2, 2);

//            11、货架事件通知(drop 一个正确商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng2, Dtotal2, aCase, step);

//            12、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            13、货架单元详情(理货，库存为3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates3, 3);

//            14、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates3, 3);

//            15、货架事件通知(pick 一个错误商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng2, Ptotal2, aCase, step);

//            16、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            17、货架单元详情(正常，库存为3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates4, 3);

//            18、单元格物品详情(正常，库存为3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates4, 3);

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

//------------------（16）----------在有三个正确商品时，(1)放置一个错误商品（3对1错）；(2)同时拿走一个正确和一个错误商品，观察状态（2对）；

//"1.货架事件通知（drop 3）-2.单元格物品绑定（绑定3个）-3.货架事件通知（drop 1 不同商品 剩余3对1错）\n" +
//        "4.货架单元详情（理货，库存为3）-5.单元格物品详情（理货，库存为3）-6.货架事件通知（pick 1正确商品+一个错的 剩余2对）\n" +
//        "7.货架单元详情（补货，库存为2）-8.单元格物品详情（补货，库存为2）"

    @Test
    private void TestPickWrongAndRight() throws Exception {
        String checkType = checkTypeStocktaking;
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "在有三个正确商品时，(1)放置一个错误商品（3对1错）；(2)同时拿走一个正确和一个错误商品，观察状态（2对）,期待补货";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long bindingStock = 3L;
        long bindingTotal = 300L;
        long Dchng1 = 50L;
        long Dtotal1 = 350L;
        long Pchng1 = -150L;
        long Ptotal1 = 200L;

        String response = "";

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesOutofStock};

        int step = 0;
        try {


            aCase.setRequestData("1.货架事件通知（drop 3）-2.单元格物品绑定（绑定3个）-3.货架事件通知（drop 1 不同商品 剩余3对1错）\n" +
                    "4.货架单元详情（理货，库存为3）-5.单元格物品详情（理货，库存为3）-6.货架事件通知（pick 1正确商品+一个错的 剩余2对）\n" +
                    "7.货架单元详情（补货，库存为2）-8.单元格物品详情（补货，库存为2）" + "\n\n");

//            1、货架事件通知(drop 3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(response, 1);

//            2、单元格物品绑定(绑定3个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(drop one different goods)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng1, Dtotal1, aCase, step);

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            4、货架单元详情(理货 stock为3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates1, 3);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates1, 3);

//            6、通知(pick 一个正确商品+一个错误商品)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            7、货架单元详情(缺货 stock为2)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates2, 2);

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            8、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates2, 2);

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


//------------(17)-----------放置错误商品后，然后按照一定的策略逐步拿空！-----------------------------------------

//"1. 货架事件通知（drop 5）-2.单元格物品绑定（绑定5个）-3.货架事件通知（drop 50g 剩余5对1错）\n" +
//        "4.货架单元详情（理货，库存为5）-5.单元格物品详情（理货，库存为5）-6.货架事件通知（drop 110g 剩余5对2错）\n" +
//        "7.货架单元详情（理货，库存为5）-8.单元格物品详情（理货，库存为5）-9.货架事件通知（drop 330 剩余6对3错）\n" +
//        "10.货架单元详情（理货，库存为6）-11.单元格物品详情（理货，库存为6）-12.货架事件通知（pick 250g 剩余5对2错）\n" +
//        "13.货架单元详情（理货，库存为5）-14.单元格物品详情（理货，库存为5）-15.货架事件通知（pick 440g 剩余4对）\n" +
//        "16.货架单元详情（正常，库存为4）-17.单元格物品详情（正常，库存为4）-18.货架事件通知（全部拿走（800g））\n" +
//        "19.货架单元详情（缺货，库存为0）-20.单元格物品详情（缺货，库存为0）"

    @Test
    private void TestPickWrongToEmpty() throws Exception {
        String checkType = checkTypeStocktaking;
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "放置错误商品后，然后按照一定的策略逐步拿空！";
        logger.info(caseDesc + "--------------------");

        long Dchng = 200L;
        long Dtotal = 1000L;
        long bindingStock = 5L;
        long bindingTotal = 1000L;
        long Dchng1 = 50L;
        long Dtotal1 = 1050L;
        long Dchng2 = 110L;
        long Dtotal2 = 1160L;
        long Dchng3 = 330L;
        long Dtotal3 = 1490L;
        long Pchng1 = -250L;
        long Ptotal1 = 1240L;
        long Pchng2 = -440L;
        long Ptotal2 = 800L;
        long Pchng3 = -800L;
        long Ptotal3 = 0L;

        String response = "";

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesWrongPlace};
        String[] alarmStates3 = {alarmStatesWrongPlace};
        String[] alarmStates4 = {alarmStatesWrongPlace};
        String[] alarmStates5 = new String[0];
        String[] alarmStates6 = {alarmStatesOutofStock};

        int stock1 = 5;
        int stock2 = 5;
        int stock3 = 6;
        int stock4 = 5;
        int stock5 = 4;
        int stock6 = 0;

        int step = 0;

        try {

            aCase.setRequestData("1. 货架事件通知（drop 5）-2.单元格物品绑定（绑定5个）-3.货架事件通知（drop 50g 剩余5对1错）\n" +
                    "4.货架单元详情（理货，库存为5）-5.单元格物品详情（理货，库存为5）-6.货架事件通知（drop 110g 剩余5对2错）\n" +
                    "7.货架单元详情（理货，库存为5）-8.单元格物品详情（理货，库存为5）-9.货架事件通知（drop 330 剩余6对3错）\n" +
                    "10.货架单元详情（理货，库存为6）-11.单元格物品详情（理货，库存为6）-12.货架事件通知（pick 250g 剩余5对2错）\n" +
                    "13.货架单元详情（理货，库存为5）-14.单元格物品详情（理货，库存为5）-15.货架事件通知（pick 440g 剩余4对）\n" +
                    "16.货架单元详情（正常，库存为4）-17.单元格物品详情（正常，库存为4）-18.货架事件通知（全部拿走（800g））\n" +
                    "19.货架单元详情（缺货，库存为0）-20.单元格物品详情（缺货，库存为0）" + "\n\n");


//            1、货架事件通知(drop 3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(response, 1);

//            2、单元格物品绑定(绑定5个)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(drop 50g)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng1, Dtotal1, aCase, step);
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            4、货架单元详情(理货 stock为5)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates1, stock1);

//            5、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates1, stock1);

//            6、通知(drop 110g)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng2, Dtotal2, aCase, step);
//
////            心跳
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            7、货架单元详情(理货 stock为5)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates2, stock2);

//            8、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates2, stock2);

//            9、通知(drop 330g)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng3, Dtotal3, aCase, step);

//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            10、货架单元详情(缺货 stock为5)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates3, stock3);

//            11、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates3, stock3);

//            12、通知(pick 250g)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            13、货架单元详情(缺货 stock为5)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates4, stock4);

//            14、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates4, stock4);

//            15、通知(pick 440g)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng2, Ptotal2, aCase, step);

//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            16、货架单元详情(正常 stock为4)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates5, stock5);

//            17、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates5, stock5);

//            18、通知(pick 800g)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng3, Ptotal3, aCase, step);

//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            19、货架单元详情(缺货 stock为0)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, alarmStates6, stock6);

//            20、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates6, stock6);

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

//    --------(18)-----------给一个单元先盘货2-5个物品，然后放置1个错误物品，用不同的拿取数量和顺序测试-----------------------------
    /*1. 创建单元-2. 货架事件通知-3. 单元格物品绑定-4. 货架事件通知-5. 心跳-6. 货架单元详情-7. -单元格物品详情-8. 货架事件通知
	9. 心跳-10. 货架单元详情-11. 单元格物品详情-12. 货架事件通知-13. 心跳-14. 货架单元详情-15. 单元格物品详情-16. 货架事件通知
	17. 心跳-18. 货架单元详情-19. 单元格物品详情-20. 货架事件通知-21. 心跳-22. 货架单元详情-23. 单元格物品详情-24. 货架事件通知
	25. 心跳-26. 货架单元详情-27. 单元格物品详情-28. 货架事件通知-29. 心跳-30. 货架单元详情-31. 单元格物品详情
*/

    @Test(dataProvider = "ACCURACY_RATE",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.CommodityManaAccuracyRate.class,
            priority = 2)
    private void testAccuracyRateOneWrong(long id, long Dchng, long Dtotal, long bindingStock, long bindingTotal,
                                          long DwrongChng, long DwrongTotal, String wrongAlarm, int wrongStock,
                                          long Pchng1, long Ptotal1, String alarm1, int stock1,
                                          long Pchng2, long Ptotal2, String alarm2, int stock2,
                                          long Pchng3, long Ptotal3, String alarm3, int stock3,
                                          long Pchng4, long Ptotal4, String alarm4, int stock4,
                                          long Pchng5, long Ptotal5, String alarm5, int stock5,
                                          long Pchng6, long Ptotal6, String alarm6, int stock6) throws Exception {
        String checkType = checkTypeStocktaking;
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + id;
        String caseDesc = "给一个单元先盘货2-5个物品，然后放置1个错误物品，用不同的拿取数量和顺序测试";
        logger.info(caseDesc + "--------------------");

        String response = "";

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId;
        int step = 0;

        try {

            accuracyCaseTotalNum++;
            aCase.setRequestData("1.货架事件通知-2. 单元格物品绑定-3. 货架事件通知-4. 心跳-5. 货架单元详情-6. -单元格物品详情-7. 货架事件通知\n" +
                    "8. 心跳-9. 货架单元详情-10. 单元格物品详情-11. 货架事件通知-12. 心跳-13. 货架单元详情-14. 单元格物品详情-15. 货架事件通知\n" +
                    "16. 心跳-17. 货架单元详情-18. 单元格物品详情-19. 货架事件通知-20. 心跳-21. 货架单元详情-22. 单元格物品详情-23. 货架事件通知\n" +
                    "24. 心跳-25. 货架单元详情-26. 单元格物品详情-27. 货架事件通知-28. 心跳-29. 货架单元详情-30. 单元格物品详情" + "\n\n");

//            1、货架事件通知(初始放置)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dchng, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(response, 1);

//            2、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            3、货架事件通知(drop wrong)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, DwrongChng, DwrongTotal, aCase, step);

//            4、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            5、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(wrongAlarm), wrongStock);

//            6、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(wrongAlarm), wrongStock);

//            7、通知（pick 1st）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng1, Ptotal1, aCase, step);

//            8、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            9、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm1), stock1);

//            10、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm1), stock1);

//            11、通知(pick 2nd)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng2, Ptotal2, aCase, step);

//            12、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            13、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm2), stock2);

//            14、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm2), stock2);

//            15、通知（pick 3rd）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng3, Ptotal3, aCase, step);

//            16、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            17、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm3), stock3);

//            18、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm3), stock3);

//            19、通知(pick 4th)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng4, Ptotal4, aCase, step);

//            20、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            21、货架单元详情(正常 stock为4)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm4), stock4);

//            22、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm4), stock4);

//            23、通知(pick 5th)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng5, Ptotal5, aCase, step);

//            24、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            25、货架单元详情(缺货 stock为0)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm5), stock5);

//            26、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm5), stock5);

//            27、通知(pick 6th)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typePick, plateCode, Pchng6, Ptotal6, aCase, step);

//            28、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            29、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm6), stock6);

//            30、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm6), stock6);

            aCase.setResult("PASS"); //FAIL, PASS

            accuracyCasePassNum++;

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

//    -----------------------(19)给一个单元先盘货2-5个物品，然后放置5-2个错误物品，用不同的取放数量顺序测试-------------------

    @Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class, priority = 1)
    public void shelf_accuracyratecases(String id, String DsingleP, String DtotalP, String bindingStockP, String bindingTotalP,
                                        String type1, String chng1P, String total1P, String stock1P, String alarm1,
                                        String type2, String chng2P, String total2P, String stock2P, String alarm2,
                                        String type3, String chng3P, String total3P, String stock3P, String alarm3,
                                        String type4, String chng4P, String total4P, String stock4P, String alarm4,
                                        String type5, String chng5P, String total5P, String stock5P, String alarm5,
                                        String type6, String chng6P, String total6P, String stock6P, String alarm6) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + id;
        String caseDesc = "给一个单元先盘货2-5个物品，然后放置5-2个错误物品，用不同的取放数量顺序测试";
        logger.info(caseDesc + "--------------------");

        String response = "";

        Case aCase = new Case();
        failReason = "";

        String unitCode = UNIT_CODE;
        String plateCode = PLATE_CODE;

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId;

        int step = 0;

        long Dsingle = Long.parseLong(DsingleP);
        long Dtotal = Long.parseLong(DtotalP);
        long bindingStock = Long.parseLong(bindingStockP);
        long bindingTotal = Long.parseLong(bindingTotalP);

        long chng1 = Long.parseLong(chng1P);
        long chng2 = Long.parseLong(chng2P);
        long chng3 = Long.parseLong(chng3P);
        long chng4 = Long.parseLong(chng4P);
        long chng5 = Long.parseLong(chng5P);
        long chng6 = Long.parseLong(chng6P);

        long total1 = Long.parseLong(total1P);
        long total2 = Long.parseLong(total2P);
        long total3 = Long.parseLong(total3P);
        long total4 = Long.parseLong(total4P);
        long total5 = Long.parseLong(total5P);
        long total6 = Long.parseLong(total6P);

        int stock1 = Integer.parseInt(stock1P);
        int stock2 = Integer.parseInt(stock2P);
        int stock3 = Integer.parseInt(stock3P);
        int stock4 = Integer.parseInt(stock4P);
        int stock5 = Integer.parseInt(stock5P);
        int stock6 = Integer.parseInt(stock6P);

        try {

            accuracyCaseTotalNum++;

            aCase.setRequestData("1. 货架事件通知-2. 单元格物品绑定-3. 货架事件通知-4. 心跳-5. 货架单元详情-6. -单元格物品详情-7. 货架事件通知\n" +
                    "8. 心跳-9. 货架单元详情-10. 单元格物品详情-11. 货架事件通知-12. 心跳-13. 货架单元详情-14. 单元格物品详情-15. 货架事件通知\n" +
                    "16. 心跳-17. 货架单元详情-18. 单元格物品详情-19. 货架事件通知-20. 心跳-21. 货架单元详情-22. 单元格物品详情-23. 货架事件通知\n" +
                    "24. 心跳-25. 货架单元详情-26. 单元格物品详情" + "\n\n");

//            2、货架事件通知(初始放置)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, typeDrop, plateCode, Dsingle, Dtotal, aCase, step);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode, SHELVES_CODE);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "  " + step + "、unitDetail");
            latticeId = checkUnitDetail(response, 1);

//            3、单元格物品绑定
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkTypeStocktaking, aCase, step);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "  " + step + "、latticeBinding");

//            4、货架事件通知(1)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type1, plateCode, chng1, total1, aCase, step);

//            5、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            6、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm1), stock1);

//            7、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm1), stock1);

//            8、通知（2）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type2, plateCode, chng2, total2, aCase, step);

//            9、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            10、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm2), stock2);

//            11、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm2), stock2);

//            12、通知(3)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type3, plateCode, chng3, total3, aCase, step);

//            13、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            14、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm3), stock3);

//            15、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm3), stock3);

//            16、通知（4）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type4, plateCode, chng4, total4, aCase, step);

//            17、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            18、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm4), stock4);

//            19、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm4), stock4);

//            20、通知(5)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type5, plateCode, chng5, total5, aCase, step);

//            21、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            22、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm5), stock5);

//            23、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm5), stock5);

//            24、通知(6)
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            customerMessage(unitCode, type6, plateCode, chng6, total6, aCase, step);

//            25、心跳
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            heartBeat(unitCode, plateCode, aCase, step);

//            26、货架单元详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unitDetail(unitCode, SHELVES_CODE, aCase, step);
            checkAlarmStateAndGoodsStockByUnitDetail(response, latticeId, formatAlarm(alarm6), stock6);

//            27、单元格物品详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = latticeDetail(latticeId, aCase, step);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, formatAlarm(alarm6), stock6);

            aCase.setResult("PASS"); //FAIL, PASS

            accuracyCasePassNum++;

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

    //    ----------------------------(20)计算准确率------------------------------------------------
    @Test(priority = 3)
    public void calAccuracyRate() {

        Shelf shelf = new Shelf();
        DateTimeUtil dt = new DateTimeUtil();
        shelf.setDate(dt.getHistoryDate(0));
        shelf.setType("cloud");

        float accuracyRate = 0f;

        if (accuracyCaseTotalNum > 0) {
            accuracyRate = (float) accuracyCasePassNum / (float) accuracyCaseTotalNum;
            BigDecimal b = new BigDecimal(accuracyRate);
            accuracyRate = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
            shelf.setAccuracy(accuracyRate);
        }

        logger.info(accuracyRate + "");

        qaDbUtil.saveShelfAccuracy(shelf);
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
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
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
                        typePick, -100, 200, alarmStatesOutAndSensor, 2, StatusCode.SUCCESS, "STOCKTAKING-1"},
                new Object[]{"STOCKTAKING", 100, 100, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typePick, -100, 0, alarmStatesOutAndSensor, 0, StatusCode.SUCCESS, "STOCKTAKING-2"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typePick, -50, 70, alarmStatesOutAndSensor, 2, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-3"},
                new Object[]{"STOCKTAKING", 100, 300, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 100, 400, alarmStatesSensorError, 4, StatusCode.SUCCESS, "STOCKTAKING-4"},
                new Object[]{"STOCKTAKING", 100, 100, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 100, 200, alarmStatesSensorError, 4, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-5"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 50, 220, alarmStatesSensorError, 4, StatusCode.SUCCESS, "STOCKTAKING-6"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS,
                        typeDrop, 50, 230, alarmStatesSensorError, 4, StatusCode.INTERNAL_SERVER_ERROR, "STOCKTAKING-7"},
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
}
