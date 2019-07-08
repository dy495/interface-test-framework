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
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
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

import java.util.HashMap;
import java.util.UUID;

public class CommodityMana {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_87803c0c";
    private static String APP_CODE = "7485a90349a2";
    private static String AK = "8da9aeabd74198b1";
    private static String SK = "ec44b94f9b3cf4333c5d000781cb0289";

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

    private String typePick = "PICK";
    private String typeDrop = "DROP";

    private String checkTypeTally = "TALLY";
    private String checkTypeStocktaking = "STOCKTAKING";

    private String alarmStatesOutOfStock = "OUT_OF_STOCK";
    private String alarmStatesCamereError = "CAMERA_ERROR";
    private String alarmStatesCensorError = "CENSOR_ERROR";
    private String alarmStatesWrongPlace = "WRONG_PLACE";
//    private String alarmStatesCamereError =  "CAMERA_ERROR";

    HashMap<String, String> header = new HashMap();


    private String shelvesCode = "hello";

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
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/commodity-management/buildWithParameters?case_name=";


    //    -----------------------------创建感压板------------------------------------------------
    private String createSensor(String plateCode) throws Exception {
        logger.info("\n");
        logger.info("------------create sensor!-----------------------");

        //        组织参数
        String json = genCreateSensorPara(plateCode);

        try {
            response = sendRequestWithHeader(createSensorServiceId,json,header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        return response;
    }

    private String genCreateSensorPara(String plateCode) {

        String json =
                "{\n" +
                        "    \"plate_code\":\"" + plateCode + "\",\n" +
                        "    \"sensors\":[\n" +
                        "        {\n" +
                        "            \"k\":-0.594081,\n" +
                        "            \"plate_code\":\"" + plateCode + "\",\n" +
                        "            \"sensor_id\":0,\n" +
                        "            \"status\":\"NORMAL\",\n" +
                        "            \"zero\":1799.25\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"k\":-0.589696,\n" +
                        "            \"plate_code\":\"" + plateCode + "\",\n" +
                        "            \"sensor_id\":1,\n" +
                        "            \"status\":\"NORMAL\",\n" +
                        "            \"zero\":26882.05\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"width\":1\n" +
                        "}";

        return json;
    }

    //---------------------- 0.0 创建（变更）config-----------------------------
    private String createConfig(String plateCode) throws Exception {
        logger.info("\n");
        logger.info("------------create config!-----------------------");

//        组织参数
        String json = genCreateConfigPara(plateCode);

        try {
            response = sendRequestWithHeader(createConfigServiceId,json,header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        return response;
    }

    private String genCreateConfigPara(String plateCode) {

        String json =
                "{\n" +
                        "  \"plate_code\":\""  + plateCode  + "\",\n" +
                        "  \"sensors\": [\n" +
                        "    {\n" +
                        "  \"plate_code\":\""  + plateCode  + "\",\n" +
                        "      \"sensor_id\": 0,\n" +
                        "      \"k\": 1.01,\n" +
                        "      \"zero\": 1234.9876,\n" +
                        "      \"status\": \"NORMAL\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";

        return json;
    }

    //-------------------------0.1 创建unit---------------------------------
    private String createUnit(String unitCode, String plateCode) throws Exception {
        logger.info("\n");
        logger.info("------------create unit!-----------------------");

        //组织参数
        String json = genCreateUnitPara(unitCode, plateCode);

        try {
            response = sendRequestWithHeader(createUnitServiceId,json,header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        return response;
    }

    private String genCreateUnitPara(String unitCode, String plateCode){

        String json =
                "{\n" +
                        "    \"subject_id\":" +SHOP_ID + ",\n" +
                        "    \"shelves_code\":\"" +  shelvesCode + "\",\n" +
                        "    \"plate_list\":[\n" +
                        "        {\n" +
                        "            \"plate_code\":\"" + plateCode + "\",\n" +
                        "            \"row_num\":0,\n" +
                        "            \"col_num\":0\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"unit_code\":\"" + unitCode + "\",\n" +
                        "    \"position\":{\n" +
                        "        \"x\":0.55,\n" +
                        "        \"y\":0.84\n" +
                        "    }\n" +
                        "}";


        return json;
    }

    private void delete(String shelvesCode, String unitCode) throws Exception {
        logger.info("\n");
        logger.info("------------delete unit!-----------------------");

        String json =
                "{\n" +
                        "  \"shelves_code\": \""   +  shelvesCode  +    "\",\n" +
                        "  \"unit_code\": \""  + unitCode + "\"\n" +
                        "}";
        try {
            response = sendRequestWithHeader(deleteUnitServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
    }

    //------------------------0.2 心跳-----------------------------------
    private String heartBeat(String unitCode, String plateCode) throws Exception {
        logger.info("\n");
        logger.info("------------heart beat!-----------------------");
        String router = "/commodity/external/HEART_BEAT/v1.0";
        String[] resource = new String[]{};
        String message = "";

        //组织参数
        String json = genHeartBeatPara(unitCode, plateCode);

        try {
            apiResponse = sendRequestWithGate(router, resource, json);
            message = apiResponse.getMessage();
            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);
        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }

        return response;
    }

    private String genHeartBeatPara(String unitCode, String plateCode){

        long currentTime = System.currentTimeMillis();

        String json =
                "{\n" +
                        "    \"unit_code\": \""  +  unitCode   +    "\",\n" +
                        "    \"timestamp\": "    +  currentTime  +    ",\n" +
                        "    \"camera\": \"NORMAL\",\n" +
                        "    \"plate\": [\n" +
                        "      {\n" +
                        "        \"plate_code\":\""  +   plateCode +    "\",\n" +
                        "        \"sensors\": [\n" +
                        "          {\n" +
                        "            \"plate_code\":\"" + plateCode  + "\",\n" +
                        "            \"sensor_id\": 0,\n" +
                        "            \"k\": 1.01,\n" +
                        "            \"zero\": 1234.9876,\n" +
                        "            \"status\": \"SENSOR_RUNNING\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"plate_code\":\"" + plateCode  + "\",\n" +
                        "            \"sensor_id\": 1,\n" +
                        "            \"k\": 2.02,\n" +
                        "            \"zero\": 9876.54321,\n" +
                        "            \"status\": \"SENSOR_RUNNING\"\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    ]\n" +
                        "}";
        return json;

    }

    //----------------------------0.3 货架事件通知-------------------------
    private void customerMessage(String unitCode, String type, String posi, long change, long total) throws Exception {
        logger.info("\n");
        logger.info("------------customer message!-----------------------");
        String router = "/commodity/external/CUSTOMER_MESSAGE/v1.0";
        String[] resource = new String[]{};
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        组织参数
        String json = genCustomerMessagePara(unitCode, type, posi, change, total);

        try {
            apiResponse = sendRequestWithGate(router, resource, json);
            message = apiResponse.getMessage();
            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
    }

    private String genCustomerMessagePara(String unitCode, String type, String posi, long change, long total) throws Exception {

        long currentTime = System.currentTimeMillis();
        long startTime = currentTime;
        long endTime = currentTime + 60 * 1000;

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        组织参数
        String json =
                "{\n" +
                        "    \"data\": {\n" +
                        "      \"position\": \"" + posi + "\",\n" +
                        "      \"weight_change\":" + change + ",\n" +
                        "      \"total_weight\":" + total + "\n" +
                        "    },\n" +
                        "    \"end_time\": " + endTime + ",\n" +
                        "    \"start_time\": " + startTime + ",\n" +
                        "    \"timestamp\": " + currentTime + ",\n" +
                        "    \"type\": \"" + type + "\",\n" +
                        "    \"unit_code\": \"" + unitCode + "\"\n" +
                        "}";

        return json;
    }

    @Test
    public void test(){

        String customerId = "328f1427-3aea-4768-8e36-125490fc";
        try {
            customerGoods(customerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String json =
                "{\n" +
                        "    \"subject_id\":\"" + "8" + "\",\n" +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}";

        return json;
    }

    //----------------------1.1 平面图货架列表-----------------------
    private String realTimeList() {
        logger.info("\n");
        logger.info("------------real time list!-----------------------");

//        组织参数
        String json = genRealTimeListPara();

        try {
            response = sendRequestWithHeader(realTimeListServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genRealTimeListPara() {

        String json =
                "{\n" +
                        "\"subject_id\":\"" + SHOP_ID + "\"" +
                        "}";

        return json;
    }

    //-----------------------1.2 货架单元详情-----------------------------------
    private String unitDetail(String unitCode) {
        logger.info("\n");
        logger.info("------------unit detail!-----------------------");

//        组织参数
        String json = genUnitDetailPara(unitCode);

        try {
            response = sendRequestWithHeader(unitDetailServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genUnitDetailPara(String unitCode) {

        String json =
                "{\n" +
                        "\"subject_id\":" + SHOP_ID + ",\n" +
                        "\"shelves_code\":\"" + shelvesCode + "\"," +
                        "\"unit_code\":\"" + unitCode + "\"" +
                        "}";
        return json;
    }

    //-----------------------1.3 单元格物品详情-----------------------------------
    private String latticeDetail(int latticeId) {
        logger.info("\n");
        logger.info("------------lattice detail !-----------------------");

//        组织参数
        String json = genLatticeDetailPara(latticeId);

        try {
            response = sendRequestWithHeader(latticeDetailServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genLatticeDetailPara(int latticeId) {

        String json =
                "{\n" +
                        "\"lattice_id\":" + latticeId + "\n" +
                        "}";
        return json;
    }


    //-----------------------1.4 单元格物品扫描-----------------------------------
    private String latticeCheck(int latticeId, long goodsId, String checkType) {
        logger.info("\n");
        logger.info("------------lattice check!-----------------------");

//        组织参数
        String json = genLatticeCheckPara(latticeId, goodsId, checkType);

        try {
            response = sendRequestWithHeader(latticeCheckServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genLatticeCheckPara(int latticeId, long goodsId, String checkType) {

        String json =
                "{\n" +
                        "\"lattice_id\":" + latticeId + "," +
                        "\"goods_id\":" + goodsId + "," +
                        "\"check_type\":\"" + checkType + "\"" +
                        "}";
        return json;
    }

    //-----------------------1.5 单元格物品绑定-----------------------------------
    private String latticeBinding(long latticeId, long goodsId, long goodsStock, long totalWeight, String bindingType) {
        logger.info("\n");
        logger.info("----------------lattice binding!-----------------------");

//        组织参数
        String json = genLatticeBindingPara(latticeId, goodsId, goodsStock, totalWeight, bindingType);

        try {
            response = sendRequestWithHeader(latticeBindingServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genLatticeBindingPara(long latticeId, long goodsId, long goodsStock, long totalWeight, String bindingType) {

        String json =
                "{\n" +
                        "\"lattice_id\":" + latticeId + ",\n" +
                        "\"goods_id\":" + goodsId + ",\n" +
                        "\"goods_stock\":" + goodsStock + ",\n" +
                        "\"total_weight\":" + totalWeight + ",\n" +
                        "\"binding_type\":\"" + bindingType + "\"\n" +
                        "}";

        return json;
    }

    //-----------------------1.6 单元盘货完成-----------------------------------
    private String unitStocktaking(String unitCode) {
        logger.info("\n");
        logger.info("-------------------------unit stocktaking finish!-----------------------");

//        组织参数
        String json = genUnitStocktakingPara(unitCode);

        try {
            response = sendRequestWithHeader(unitstockingFinishServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genUnitStocktakingPara(String unitCode) {

        String json =
                "{\n" +
                        "\"shelves_code\":\"" + shelvesCode + "\"," +
                        "\"unit_code\":\"" + unitCode + "\"" +
                        "}";

        return json;
    }

    //-----------------------1.7 店铺盘货完成-----------------------------------
    private String shopStocktaking() {
        logger.info("\n");
        logger.info("-------------------------shop stocktaking finish!-----------------------");

//        组织参数
        String json = genShopStocktakingPara();

        try {
            response = sendRequestWithHeader(shopstockingFinishServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genShopStocktakingPara() {

        String json =
                "{\n" +
                        "\"subject_id\":\"" + SHOP_ID + "\"" +
                        "}";

        return json;
    }

    //-----------------------1.8 单元物品解绑-----------------------------------
    private String latticeUnbind(long latticeId, long goodsId) {
        logger.info("\n");
        logger.info("-------------------------lattice unbind!-----------------------");

//        组织参数
        String json = genLatticeUnbindPara(latticeId, goodsId);

        try {
            response = sendRequestWithHeader(latticeUnbindServiceId, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    private String genLatticeUnbindPara(long latticeId, long goodsId) {

        String json =
                "{\n" +
                        "\"lattice_id\":\"" + latticeId + "\"," +
                        "\"goods_id\":" + goodsId + "\n" +
                        "}";

        return json;
    }

    //-----------心跳以后，平面图货架列表中的alarm_states字段为空-------------------------------------------------------------
    //-------------------------（1）---------1、创建单元-2、创建配置-3、心跳-4、平面图货架列表------------------------------------
    @Test
    private void realTimeListAlarmStatesTest() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName ;
        String caseDesc = "心跳以后，平面图货架列表中的alarm_states字段为空";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String createConfigRes = null;
        String realTimeListRes = null;

        JSONObject createUnitResJo;
        JSONObject createConfigResJo;
        JSONObject realTimeListResJo;

        Case aCase = new Case();
        failReason = "";

        String unitCode = ciCaseName;
        String plateCode = "666";
        String message = "";
        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            JSONObject createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            JSONObject createConfigJo = JSON.parseObject(genCreateConfigPara(plateCode));
            JSONObject heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, plateCode));
            JSONObject realTimeListJo = JSON.parseObject(genRealTimeListPara());

            //将入参入库
            aCase.setRequestData("1、create unit:" + createUnitJo + "\n\n" + "2、create config:" + createConfigJo + "\n\n" +
                    "3、heart beat:" + heartBeatJo + "\n\n" + "4、real time list:" + realTimeListJo);

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);

            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, "");

//            2、创建配置
            createConfigRes = createConfig(plateCode);
            message = JSON.parseObject(createConfigRes).getString("message");
            checkCode(createConfigRes, StatusCode.SUCCESS, message);
//
//            3、心跳
            heartBeat(unitCode, plateCode);

//            4、平面图货架列表
            Thread.sleep(2000);
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message);

//            检测返回的状态
            String [] expectStates = new String[0];
            checkRealtimeListAlarmStates(realTimeListRes, unitCode, expectStates);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            createConfigResJo = JSONObject.parseObject(createConfigRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "2、create config: " + createConfigResJo + "\n\n" + "4、real time list: " + realTimeListResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //    ----------------------------创建单元后，单元详情中能查询到新增的单元--------------------------------
    //------------------（2）--------------------1、创建单元-2、货架单元详情-------------------------
    @Test
    private void testUnitDetailResult() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "创建单元后，单元详情中能查询到新增的单元";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String unitDetailRes = null;

        JSONObject createUnitResJo;
        JSONObject unitDetailResJo;

        Case aCase = new Case();
        failReason = "";

        String unitCode = ciCaseName;
        String plateCode = "666";
        String message = "";
        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            JSONObject createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            JSONObject unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

            //将入参入库
            aCase.setRequestData("1、create unit:" + createUnitJo + "\n\n" +  "2、unit detail:" + unitDetailJo);

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            2、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);

//            检测单元详情的lattice数量
            checkUnitDetail(unitDetailRes, 1);//看新建的时候分的单元格数量

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "2、unit detail: " +  unitDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //    ------------------没有事件时直接进行单元格物品扫描（盘、理）---------------------------------
    //-----------------（3）---------------------单元格物品扫描--------------------------------
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

        String latticeCheckRes = null;

        JSONObject latticeCheckResJo;

        Case aCase = new Case();
        failReason = "";

        String message = "";

        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            JSONObject latticeCheckJo = JSON.parseObject(genLatticeCheckPara(1, 1, checkType));

            //将入参入库
            aCase.setRequestData("lattice check: " + latticeCheckJo + "\n");

            latticeCheckRes = latticeCheck(1, 1, checkType);
            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.INTERNAL_SERVER_ERROR, message);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail("直接单元格物品扫描测试失败-" + message);
            throw e;
        } finally {
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);

            aCase.setResponse(latticeCheckResJo + "\n\n");
            qaDbUtil.saveToCaseTable(aCase);

        }
    }

    //    --------------------没有事件时直接单元格物品绑定（盘、理）--------------------------
    //-----------------（4）--------------------单元格物品绑定-------------------------------
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

        String latticeBindingRes = null;

        JSONObject latticeBindingResJo;

        Case aCase = new Case();
        failReason = "";

        String message = "";

        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            JSONObject latticeBindingJo = JSON.parseObject(genLatticeBindingPara(1, goodsId3Add2, 3, 300, checkType));

            //将入参入库
            aCase.setRequestData("lattice binding: " + latticeBindingJo + "\n");


            latticeBindingRes = latticeBinding(1, goodsId3Add2, 3, 300, checkType);
            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.INTERNAL_SERVER_ERROR, message);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail("直接单元格物品绑定测试失败-" + message);
            throw e;
        } finally {
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);

            aCase.setResponse(latticeBindingResJo + "\n\n");
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    --------------------通过扫描盘货和理货，物品放置正确和错误盘货是否成功---------------------------
    //-------------（5）---------------1、新建单元-2、事件通知(pick one)-3、商品扫描-4、货架事件通知(drop )-
    //---------------------------------5、单元格物品绑定-6、单元盘货完成-------------
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
        String caseDesc = "通过扫描盘货和理货，物品放置正确和错误盘货是否成功";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String latticeCheckRes = null;
        String latticeBindingRes = null;
        String unitStocktakingRes = null;

        JSONObject createUnitResJo;
        JSONObject latticeCheckResJo;
        JSONObject latticeBindingResJo;
        JSONObject unitStocktakingResJo;

        JSONObject createUnitJo = null;
        JSONObject customerMessagePickJo = null;
        JSONObject latticeCheckJo = null;
        JSONObject customerMessageDropJo = null;
        JSONObject latticeBindingJo = null;
        JSONObject unitStocktakingJo = null;

        Case aCase = new Case();
        failReason = "";


        String posi = "0,0";               //"position":"row,col"

        String message = "";
        String unitCode = ciCaseName;
        String plateCode = "666";

        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessagePickJo = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, changeP, totalP));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, changeD, totalD));
            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            String unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 1);

//            2、货架事件通知（pick）
            customerMessage(unitCode, typePick, posi, changeP, totalP);


//            3、商品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType);
            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

            //由于latticeId必须在过程中取，所以不能将取扫描的参数放在开头。
            latticeCheckJo = JSON.parseObject(genLatticeCheckPara(latticeId, goodsId3Add2, checkType));

//            4、货架事件通知（drop）
            customerMessage(unitCode, typeDrop, posi, changeD, totalD);

//            5、单元格物品绑定
            if (expectBinding) {
                latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType);

                latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType));
                message = JSON.parseObject(latticeBindingRes).getString("message");
                checkCode(latticeBindingRes, expectCodeBinding, message + "-latticeBinding");
            }

//            6、单元盘货完成
            if (expectUnitStocktaking) {
                unitStocktakingRes = unitStocktaking(unitCode);
                message = JSON.parseObject(unitStocktakingRes).getString("message");
                checkCode(unitStocktakingRes, expectCodeUnitStocktaking, message + "unitStocktaking");
            }

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData("1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessagePickJo + "\n\n" + "3、lattice check: " + latticeCheckJo +
                    "\n\n" + "4、customer message: " + customerMessageDropJo + "\n\n" + "5、lattice binding: " + latticeBindingJo + "\n\n" + "6、unit Stocktaking：" + unitStocktakingJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);

            aCase.setResponse("1、create unit: "  + createUnitResJo + "\n\n" + "3、lattice check: " + latticeCheckResJo + "\n\n" +
                    "5、lattice binding: " + latticeBindingResJo + "\n\n" + "6、unit Stocktaking：" + unitStocktakingResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //--------------------------不扫描就盘货和理货，物品放置正确和错误盘货是否成功---------------------------
    //--------(6)-------------------1、新建单元-2、货架事件通知（drop）-3、单元格物品绑定-4、单元盘货完成----------------
    @Test(dataProvider = "TALLY&STOCKTAKING_WITHOUT_SCAN")
    private void testTallyAndStocktakingWithoutScan(String checkType, long changeD, long totalD,long bindingStock, long bindingTotal,
                                                    int expectCodeLattice, int expectCodeUnit, String caseId) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + caseId;
        String caseDesc = "不扫描就盘货和理货，物品放置正确和错误盘货是否成功";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String latticeBindingRes = null;
        String unitStocktakingRes = null;

        JSONObject createUnitResJo;
        JSONObject latticeBindingResJo;
        JSONObject unitStocktakingResJo;

        JSONObject createUnitJo = null;
        JSONObject customerMessageDropJo = null;
        JSONObject latticeBindingJo = null;
        JSONObject unitStocktakingJo = null;

        Case aCase = new Case();
        failReason = "";

        String posi = "0,0";               //"position":"row,col"

        String response;
        String message = "";
        String unitCode = ciCaseName;
        String plateCode = "666";

        try {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, changeD, totalD));

            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            response = createUnit(unitCode, plateCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "createUnit");

//            货架单元详情，取latticeId
            response = unitDetail(unitCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message  + "unitDetail");
            int latticeId = checkUnitDetail(response, 1);

//            2、货架事件通知（drop）
            customerMessage(unitCode, typeDrop, posi, changeD, totalD);

//            3、单元格物品绑定
            response = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType));

            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCodeLattice, message + "latticeBinding");

//            4、单元盘货完成
            response = unitStocktaking(unitCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCodeUnit, message + "unitStocktaking");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData("1、create unit： " + createUnitJo + "\n\n" + "2、customer message: " +customerMessageDropJo +
                    "\n\n" + "3、lattice binding: " +latticeBindingJo + "\n\n" + "4、unit stocktaking："+ unitStocktakingJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "3、lattice binding: " + latticeBindingResJo + "\n\n" +
                    "4、unit stocktaking："  + unitStocktakingResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //-------------------------测试扫描盘货和理货后，平面图货架列表、货架单元详情和单元格物品详情中的内容是否正确----------------
    //--------(7)-------------------1、新建单元-2、通知(pick one)-3、扫描（盘、理）-4、通知（drop）-5、绑定（同扫描）---------------------
    //------------------------------6、单元盘货-7、心跳-8、平面图货架列表-9、货架单元详情-10、单元格物品详情----------------
    @Test(dataProvider = "TALLY&STOCKTAKING_RESULT")
    private void testTallyAndStocktakingResult(String checkType,long Pchng,long Ptotal, long Dchng,
                                               long Dtotal, long bindingStock,long bindingTotal) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + checkType;
        String caseDesc = "测试扫描盘货和理货后，平面图货架列表、货架单元详情和单元格物品详情中的内容是否正确";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String latticeCheckRes = null;
        String latticeBindingRes = null;
        String unitStocktakingRes = null;
        String realTimeListRes = null;
        String unitDetailRes = null;
        String latticeDetailRes = null;

        JSONObject createUnitResJo;
        JSONObject latticeCheckResJo;
        JSONObject latticeBindingResJo;
        JSONObject unitStocktakingResJo;
        JSONObject realTimeListResJo;
        JSONObject unitDetailResJo;
        JSONObject latticeDetailResJo;

        JSONObject createUnitJo = null;
        JSONObject customerMessagePickJo = null;
        JSONObject latticeCheckJo = null;
        JSONObject customerMessageDropJo = null;
        JSONObject latticeBindingJo = null;
        JSONObject unitStocktakingJo = null;
        JSONObject heartBeatJo = null;
        JSONObject realTimeListJo = null;
        JSONObject unitDetailJo = null;
        JSONObject latticeDetailJo = null;

        Case aCase = new Case();
        failReason = "";

        String posi = "0,0";               //"position":"row,col"

        String message = "";
        String unitCode = ciCaseName;
        String plateCode = "666";

        try {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessagePickJo = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, Pchng, Ptotal));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, Dchng, Dtotal));
            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, plateCode));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message + "createUnit");

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "unitDetail");
            int latticeId = checkUnitDetail(unitDetailRes, 1);

//            2、货架事件通知（pick）
            customerMessage(unitCode, typePick, posi, Pchng, Ptotal);

//            3、单元格物品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType);

//           --- 获取参数
            latticeCheckJo = JSON.parseObject(genLatticeCheckPara(latticeId, goodsId3Add2, checkType));

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message + "latticeCheck");

//            4、货架事件通知（drop 3）
            customerMessage(unitCode, typeDrop, posi, Dchng, Dtotal);

//            5、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "latticeBinding");

//            6、单元盘货完成
            unitStocktakingRes = unitStocktaking(unitCode);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message + "unitStocktaking");

//            7、心跳
            heartBeat(unitCode, plateCode);

//            8、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "realTimeList");

            checkRealtimeListStocktakingStates(realTimeListRes, unitCode);

//            9、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "unitDetail");

            checkUnitDetailStocktakingStates(unitDetailRes, latticeId, bindingStock);

//            10、单元格物品详情
            latticeDetailRes = latticeDetail(latticeId);

            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message + "latticeDetail");

            checkLatticeDetailGoodsStock(latticeDetailRes, bindingStock);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData("1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessagePickJo + "\n\n" +
                    "3、lattice check：" + latticeCheckJo + "\n\n" + "4、customer message: " + customerMessageDropJo +
                    "5、lattice binding: " + latticeBindingJo + "\n\n" + "6、unit stocktaking: " + unitStocktakingJo +
                    "\n\n" +  "7、heart beat: " + heartBeatJo + "\n\n" + "8、real time list: " +  realTimeListJo + "\n\n" +
                    "9、unit detail: " + unitDetailJo + "\n\n" + "10、lattice detail: " + latticeDetailJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);
            latticeDetailResJo = JSONObject.parseObject(latticeDetailRes);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" +  "3、lattice check：" + latticeCheckResJo + "\n\n" +
                    "5、lattice binding: " + latticeBindingResJo + "\n\n" + "6、unit stocktaking: " + unitStocktakingResJo
                    + "\n\n" + "8、real time list: " + realTimeListResJo + "\n\n" + unitDetailResJo + "\n\n" + "10、lattice detail: " + latticeDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //--------------------解绑后货架单元详情和单元格物品详情中都不能查询到该单元格-----------------------------
    //--------(8)-------------------1、新建单元-2、通知(pick one)-3、扫描(盘、理)-4、通知（drop）------------------------
    //------------------------------5、绑定（同扫描）-6、单元盘货-7、解绑-8、心跳-9、平面图货架列表----------------------
    //-----------------------------10、货架单元详情-11、单元格物品详情----------------
    @Test(dataProvider = "UNBIND_RESULT")
    private void testUnbindResult(String checkType,long Pchng,long Ptotal, long Dchng,
                                  long Dtotal, long bindingStock,long bindingTotal) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "---" + checkType;
        String caseDesc = "解绑后货架单元详情和单元格物品详情中都不能查询到该单元格";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String latticeCheckRes = null;
        String latticeBindingRes = null;
        String unitStocktakingRes = null;
        String latticeUnbindRes = null;
        String realTimeListRes = null;
        String unitDetailRes = null;
        String latticeDetailRes = null;

        JSONObject createUnitResJo;
        JSONObject latticeCheckResJo;
        JSONObject latticeBindingResJo;
        JSONObject unitStocktakingResJo;
        JSONObject latticeUnbindResJo;
        JSONObject realTimeListResJo;
        JSONObject unitDetailResJo;
        JSONObject latticeDetailResJo;

        JSONObject createUnitJo = null;
        JSONObject customerMessagePickJo = null;
        JSONObject latticeCheckJo = null;
        JSONObject customerMessageDropJo = null;
        JSONObject latticeBindingJo = null;
        JSONObject unitStocktakingJo = null;
        JSONObject latticeUnbindJo = null;
        JSONObject heartBeatJo = null;
        JSONObject realTimeListJo = null;
        JSONObject unitDetailJo = null;
        JSONObject latticeDetailJo = null;

        Case aCase = new Case();
        failReason = "";

        String posi = "0,0";               //"position":"row,col"

        String message = "";
        String unitCode = ciCaseName;
        String plateCode = "666";

        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessagePickJo = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, Pchng, Ptotal));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, Dchng, Dtotal));
            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, plateCode));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message  + " createUnit");

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + " ----unitDetail");
            int latticeId = checkUnitDetail(unitDetailRes, 1);

//            2、货架事件通知（pick）
            customerMessage(unitCode, typePick, posi, Pchng, Ptotal);

//            3、单元格物品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType);

            latticeCheckJo = JSON.parseObject(genLatticeCheckPara(latticeId, goodsId3Add2, checkType));

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message + "----latticeCheck");

//            4、货架事件通知（drop 3）
            customerMessage(unitCode, typeDrop, posi, Dchng, Dtotal);

//            5、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, bindingStock, bindingTotal, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "----latticeBinding");

//            6、单元盘货完成
            unitStocktakingRes = unitStocktaking(unitCode);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message + "---unitStocktaking");

//            7、单元物品解绑
            latticeUnbindRes = latticeUnbind(latticeId, goodsId3Add2);

            latticeUnbindJo = JSON.parseObject(latticeUnbind(latticeId, goodsId3Add2));

            message = JSON.parseObject(latticeUnbindRes).getString("message");
            checkCode(latticeUnbindRes, StatusCode.SUCCESS, message + "---latticeUnbind");

//            8、心跳
            heartBeat(unitCode, plateCode);

//            9、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "---realTimeList");

//            这里仅校验告警状态是否为空和盘货状态是否为FINISHED
            checkUnbindRealTimeList(realTimeListRes);

//            10、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---unitDetail");

            checkUnbindUnitDetail(response);

//            11、单元格物品详情
            latticeDetailRes = latticeDetail(latticeId);

            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message + "---latticeDetail");

            checkUnbindLatticeDetail(response);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData("1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessagePickJo +
                    "\n\n" + "3、lattice check: " + latticeCheckJo + "\n\n" + "4、customer message: " + customerMessageDropJo +
                    "\n\n" + "5、lattice binding: " + latticeBindingJo + "\n\n" + "6、unit stocktaking: " + unitStocktakingJo +
                    "\n\n" + "7、lattice unbind: " + latticeUnbindJo + "\n\n" + "8、heart beat: " + heartBeatJo + "\n\n" + "9、real time list: " + realTimeListJo +
                    "\n\n" + "10、unit detail: " + unitDetailJo  + "\n\n" + "11、lattice detail: " + latticeDetailJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);
            latticeUnbindResJo = JSONObject.parseObject(latticeUnbindRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);
            latticeDetailResJo = JSONObject.parseObject(latticeDetailRes);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "3、lattice check: " + latticeCheckResJo +
                    "5、lattice binding: " + "\n\n" + latticeBindingResJo + "\n\n" + "6、unit stocktaking: " + unitStocktakingResJo
                    + "\n\n" + "7、lattice unbind: " + latticeUnbindResJo + "\n\n" + "9、real time list: " + realTimeListResJo +
                    "\n\n" + "10、unit detail: " + unitDetailResJo + "\n\n" + "11、lattice detail: " + latticeDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //----------------(9)---------理货成功后，该单元盘货状态应为UNFINISHED---------------------------------------------------
    //------------------------------1、创建单元-2、通知（drop 3）-3、绑定（盘）-4、通知（drop 1）--------------------------------------------------
    // -----------------------------5、平面图货架详情-6、店铺盘货------------------------------------------------------------------------------

    @Test
    private void testUnfinishedATally() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试一个店铺有两个单元，一个盘货，一个未盘货，那么店铺盘货失败";
        logger.info(caseDesc + "--------------------");

        long Dchng = 100L;
        long Dtotal = 300L;
        long Dchng1 = 100L;
        long Dtotal1 = 400L;
        long bindingStock = 3L;
        long bindingTotal = 300L;

        String createUnitRes = null;
        String latticeBindingRes = null;
        String realTimeListRes = null;
        String shopStocktakingRes = null;

        JSONObject createUnitResJo;
        JSONObject latticeBindingResJo;
        JSONObject realTimeListResJo;
        JSONObject shopStocktakingResJo;

        JSONObject createUnitJo = null;
        JSONObject customerMessageDropJo = null;
        JSONObject latticeBindingJo = null;
        JSONObject customerMessageDropJo1 = null;
        JSONObject realTimeListJo = null;
        JSONObject shopStocktakingJo = null;


        String unitDetailRes;

        Case aCase = new Case();
        failReason = "";

        String posi = "0,0";               //"position":"row,col"

        String message = "";
        String unitCode = ciCaseName;
        String plateCode = "666";
        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, Dchng, Dtotal));
            customerMessageDropJo1 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop,posi, Dchng1, Dtotal1));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            shopStocktakingJo = JSON.parseObject(genShopStocktakingPara());

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message + "1、create unit");

            //            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "----1、unitDetail");
            int latticeId_2 = checkUnitDetail(unitDetailRes, 1);

//            2、货架事件通知
            customerMessage(unitCode, typeDrop, posi, Dchng, Dtotal);

//            3、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId_2, goodsId3Add2, bindingStock, bindingTotal, checkTypeStocktaking);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId_2, goodsId3Add2, bindingStock, bindingTotal, checkTypeStocktaking));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            4、通知（drop 1）
            customerMessage(unitCode, typeDrop,posi, Dchng1, Dtotal1);

//            5、平面图货架详情
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "---5、realTimeList");

            checkRealTimeList(response, "UNFINISHED");

//            6、店铺盘货完成
            shopStocktakingRes = shopStocktaking();
            message = JSON.parseObject(shopStocktakingRes).getString("message");
            checkCode(shopStocktakingRes, StatusCode.stocktakingUnfinished, message + "---6、shopStocktaking");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {
            //将入参入库
            aCase.setRequestData(
                    "1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessageDropJo +
                            "\n\n" + "3、lattice binding：" + latticeBindingJo + "\n\n" + "4、customer message：" + customerMessageDropJo1  +
                            "\n\n" + "5、real time list：" + realTimeListJo + "\n\n" + "6、shop stocktaking: " + shopStocktakingJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            shopStocktakingResJo = JSONObject.parseObject(shopStocktakingRes);

            aCase.setResponse( "1、create unit: " + createUnitResJo + "\n\n" + "3、lattice binding：" + latticeBindingResJo +
                    "\n\n" + "5、real time list：" + realTimeListResJo + "\n\n" + "6、shop stocktaking: " + shopStocktakingResJo);

            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //----------------(9)---------测试一个店铺有两个单元，一个盘货，一个未盘货，那么店铺盘货失败---------------------------------------------------
    //------------------------------1、新建单元-2、通知(pick one)-3、扫描-4、通知（drop）----------------------------------------------------------
    //------------------------------5、绑定(理)-6、单元盘货完成-7、创建单元-8、通知（drop 3）--------------------------------------------------
    // -----------------------------9、绑定（盘）-10、通知（drop 1）-11、店铺盘货------------------------------------------------------------------------------

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

        String createUnitRes1 = null;
        String latticeCheckRes = null;
        String latticeBindingRes = null;
        String unitStocktakingRes = null;
        String createUnitRes2 = null;
        String latticeBindingRes2 = null;
        String shopStocktakingRes = null;

        JSONObject createUnitResJo1;
        JSONObject latticeCheckResJo;
        JSONObject latticeBindingResJo;
        JSONObject unitStocktakingResJo;
        JSONObject createUnitResJo2;
        JSONObject latticeBindingResJo2;
        JSONObject shopStocktakingResJo;

        JSONObject createUnitJo1 = null;
        JSONObject customerMessagePickJo1 = null;
        JSONObject latticeCheckJo = null;
        JSONObject customerMessageDropJo1 = null;
        JSONObject latticeBindingJo = null;
        JSONObject unitStocktakingJo1 = null;
        JSONObject createUnitJo2 = null;
        JSONObject customerMessageDropJo2 = null;
        JSONObject latticeBindingJo2 = null;
        JSONObject customerMessageDropJo3 = null;
        JSONObject shopStocktakingJo2 = null;


        String unitDetailRes;

        Case aCase = new Case();
        failReason = "";

        String posi = "0,0";               //"position":"row,col"

        String message = "";
        String unitCode_1 = ciCaseName + "-1";
        String unitCode_2 = ciCaseName + "-2";
        String plateCode_1 = "666";
        String plateCode_2 = "912";
        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo1 = JSON.parseObject(genCreateUnitPara(unitCode_1, plateCode_1));
            customerMessagePickJo1 = JSON.parseObject(genCustomerMessagePara(unitCode_1, typePick, posi, Pchng_1, Ptotal_1));
            customerMessageDropJo1 = JSON.parseObject(genCustomerMessagePara(unitCode_1, typeDrop, posi, Dchng_1, Dtotal_1));
            unitStocktakingJo1 = JSON.parseObject(genUnitStocktakingPara(unitCode_1));
            createUnitJo2 = JSON.parseObject(genCreateUnitPara(unitCode_2, plateCode_2));
            customerMessageDropJo2 = JSON.parseObject(genCustomerMessagePara(unitCode_2, typeDrop, posi, Dchng_2, Dtotal_2));
            customerMessageDropJo3 = JSON.parseObject(genCustomerMessagePara(unitCode_2, typeDrop,posi, Dchng_3, Dtotal_3));
            shopStocktakingJo2 = JSON.parseObject(genShopStocktakingPara());

//            删除
            delete(shelvesCode, unitCode_1);
            delete(shelvesCode, unitCode_2);

//            1、创建单元
            createUnitRes1 = createUnit(unitCode_1, plateCode_1);
            message = JSON.parseObject(createUnitRes1).getString("message");
            checkCode(createUnitRes1, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode_1);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId_1 = checkUnitDetail(unitDetailRes, 1);

//            2、货架事件通知（pick）
            customerMessage(unitCode_1, typePick, posi, Pchng_1, Ptotal_1);

//            3、单元格物品扫描
            latticeCheckRes = latticeCheck(latticeId_1, goodsId3Add2, checkTypeStocktaking);

            latticeCheckJo = JSON.parseObject(genLatticeCheckPara(latticeId_1, goodsId3Add2, checkTypeStocktaking));

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

//            4、货架事件通知（drop 3）
            customerMessage(unitCode_1, typeDrop, posi, Dchng_1, Dtotal_1);

//            5、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId_1, goodsId3Add2, bindingStock_1, bindingTotal_1, checkTypeStocktaking);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId_1, goodsId3Add2, bindingStock_1, bindingTotal_1, checkTypeStocktaking));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            6、单元盘货完成
            unitStocktakingRes = unitStocktaking(unitCode_1);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message);

//            7、创建单元
            createUnitRes2 = createUnit(unitCode_2, plateCode_2);
            message = JSON.parseObject(createUnitRes2).getString("message");
            checkCode(createUnitRes2, StatusCode.SUCCESS, message);

            //            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode_2);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "----7、unitDetail");
            int latticeId_2 = checkUnitDetail(unitDetailRes, 1);

//            8、货架事件通知
            customerMessage(unitCode_2, typeDrop, posi, Dchng_2, Dtotal_2);

//            9、单元格物品绑定
            latticeBindingRes2 = latticeBinding(latticeId_2, goodsId3Add2, bindingStock_2, bindingTotal_2, checkTypeStocktaking);

            latticeBindingJo2 = JSON.parseObject(genLatticeBindingPara(latticeId_2, goodsId3Add2, bindingStock_2, bindingTotal_2, checkTypeStocktaking));

            message = JSON.parseObject(latticeBindingRes2).getString("message");
            checkCode(latticeBindingRes2, StatusCode.SUCCESS, message + "---9、latticeBinding");

//            10、通知（drop 1）
            customerMessage(unitCode_2, typeDrop,posi, Dchng_3, Dtotal_3);

//            11、店铺盘货完成
            shopStocktakingRes = shopStocktaking();
            message = JSON.parseObject(shopStocktakingRes).getString("message");
            checkCode(shopStocktakingRes, StatusCode.stocktakingUnfinished, message + "---12、shopStocktaking");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {
            //将入参入库
            aCase.setRequestData("1、create unit：" + createUnitJo1 + "\n\n" + "2、customer message: " + customerMessagePickJo1 +
                    "\n\n" + "3、lattice check: " + latticeCheckJo + "\n\n" + "4、customer message: " + customerMessageDropJo1 +
                    "\n\n" + "5、lattice binding: " + latticeBindingJo + "\n\n" +  "6、unit stocktaking: " + unitStocktakingJo1 +
                    "\n\n" + "7、create unit: " + createUnitJo2 + "\n\n" + "8、customer message: " + customerMessageDropJo2 +
                    "\n\n" + "9、lattice binding: " + latticeBindingJo2 + "\n\n" + "10、customer message: " + customerMessageDropJo3 +
                    "\n\n"+ "11、shop stocktaling: " + shopStocktakingJo2);

            //response save to DB.
            createUnitResJo1 = JSONObject.parseObject(createUnitRes1);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);
            createUnitResJo2 = JSONObject.parseObject(createUnitRes2);
            latticeBindingResJo2 = JSONObject.parseObject(latticeBindingRes2);
            shopStocktakingResJo = JSONObject.parseObject(shopStocktakingRes);

            aCase.setResponse("1、create unit：" + createUnitResJo1 + "\n\n" + "3、lattice check: " + latticeCheckResJo +
                    "\n\n" + "5、lattice binding: " + latticeBindingResJo + "\n\n" + "6、unit stocktaking: " + unitStocktakingResJo
                    + "\n\n" +  "7、create unit: " +createUnitResJo2 + "\n\n" + "9、lattice binding: " + latticeBindingResJo2 +
                    "\n\n" + "11、shop stocktaling: " + shopStocktakingResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode_1);
            delete(shelvesCode, unitCode_2);
        }
    }

    //---------------------------------------------盘货时只绑定1个商品，观察此时理货的状态---------------------------------------------
    //-----------------(10)---------------1、创建单元-2、通知(drop one)-3、绑定一个商品(盘货)-4、配置-----------------------
    ////----------------------------------------5、心跳-6、列表(缺货)-7、单元详情（缺货）-8、物品详情（缺货）-----------------------------
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

        String createUnitRes = null;
        String latticeBindingRes = null;
        String createConfigRes = null;
        String realTimeListRes = null;
        String unitDetailRes = null;
        String latticeDetailRes = null;

        JSONObject createUnitResJo;
        JSONObject latticeBindingResJo;
        JSONObject createConfigResJo;
        JSONObject realTimeListResJo;
        JSONObject unitDetailResJo;
        JSONObject latticeDetailResJo;

        JSONObject createUnitJo = null;
        JSONObject customerMessageDropJo = null;
        JSONObject latticeBindingJo = null;
        JSONObject createConfigJo = null;
        JSONObject heartBeatJo = null;
        JSONObject realTimeListJo = null;
        JSONObject unitDetailJo = null;
        JSONObject latticeDetailJo = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = ciCaseName;
        String plateCode = "666";

        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 100, 100));
            createConfigJo = JSON.parseObject(genCreateConfigPara(plateCode));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, plateCode));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message + "---createUnit");

//            2、货架事件通知
            customerMessage(unitCode, typeDrop, posi, 100, 100);

//            货架单元详情，为了获取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---unitDetail");
            latticeId = checkUnitDetail(unitDetailRes, 1);

//            3、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId, 1, 100, checkTypeStocktaking);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId, 1, 100, checkTypeStocktaking));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---latticeBinding");

//            4、配置
            createConfig(plateCode);

//            5、心跳
            heartBeat(unitCode, plateCode);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            6、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message + "---realTimeList");

            String[] expectState = {alarmStatesOutOfStock};
            checkRealtimeListAlarmStates(realTimeListRes, unitCode, expectState);

//            7、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---unitDetail");

            latticeId = checkUnitDetail(unitDetailRes, 1);

            expectState = new String[]{alarmStatesOutOfStock};
            checkUnitDetailAlarmStates(unitDetailRes, latticeId,expectState);

//            8、单元格物品详情
            latticeDetailRes = latticeDetail(latticeId);

            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message + "---latticeDetail");

            JSONArray latticeDetailAlarmStates = checkLatticeDetailAlarmStates(latticeDetailRes);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(latticeDetailAlarmStates, 1, expectState, "latticeDetailAlarmStates---");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData("1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessageDropJo +
                    "\n\n" + "3、lattice binding: " + latticeBindingJo + "\n\n" + "4、create config: " + createConfigJo +
                    "\n\n" + "5、heart beat: " + heartBeatJo + "\n\n" + "6、real time list: " + realTimeListJo +
                    "\n\n" + "7、unit detail: " + unitDetailJo + "\n\n" + "8、lattice detail: "+ latticeDetailJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            createConfigResJo = JSONObject.parseObject(createConfigRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);
            latticeDetailResJo = JSONObject.parseObject(latticeDetailRes);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "3、lattice binding: " + latticeBindingResJo +
                    "\n\n" + "4、create config: " + createConfigResJo + "\n\n" + "6、real time list: " + realTimeListResJo
                    + "\n\n" + "7、unit detail: " + unitDetailResJo + "\n\n" + "8、lattice detail: " + latticeDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    /*
     * -------------------- 给一个单元格放置3个物品，然后拿走直到拿空----------------------------
     * */

    //----(11)---------1、创建单元--2、通知(drop 3)-3、绑定(3个)-4、通知(pick one)-
    //---------------- 5、心跳-6、列表(还剩2个，缺货)-7、通知(pick 2，此时拿空了)-8、心跳-9、列表(拿空了，缺货)
    //-----------------10、单元详情（缺货）-11、物品详情（缺货）-----------------------------
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


        String createUnitRes = null;
        String latticeBindingRes = null;
        String realTimeListRes2 = null;
        String realTimeListRes0 = null;
        String unitDetailRes0 = null;
        String latticeDetailRes0 = null;

        JSONObject createUnitResJo;
        JSONObject latticeBindingResJo;
        JSONObject realTimeListResJo2;
        JSONObject realTimeListResJo0;
        JSONObject unitDetailResJo0;
        JSONObject latticeDetailResJo0;

        JSONObject createUnitJo = null;
        JSONObject customerMessageDropJo3 = null;
        JSONObject latticeBindingJo = null;
        JSONObject customerMessagePickJo1 = null;
        JSONObject heartBeatJo = null;
        JSONObject realTimeListJo = null;
        JSONObject customerMessagePickJo2 = null;
        JSONObject unitDetailJo0 = null;
        JSONObject latticeDetailJo0 = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = "testPickUntilEmpty";
        String plateCode = "666";

        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String message = "";

        try {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));
            customerMessageDropJo3 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, Dchng, Dtotal));
            customerMessagePickJo1 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, Pchng1, Ptotal1));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, plateCode));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            customerMessagePickJo2 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, Pchng2, Ptotal2));
            unitDetailJo0 = JSON.parseObject(genUnitDetailPara(unitCode));

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message + "---createUnit");

//            2、货架事件通知(drop 3)
            customerMessage(unitCode, typeDrop, posi, Dchng, Dtotal);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---2、unitDetail");
            int latticeId = checkUnitDetail(response, 1);

//            3、单元格物品绑定(绑定3个)
            latticeBindingRes = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId, bindingStock, bindingTotal, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            4、货架事件通知(pick one)
            customerMessage(unitCode, typePick, posi, Pchng1, Ptotal1);

//            5、心跳
            heartBeat(unitCode, plateCode);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            6、平面图货架列表(还剩2个)
            realTimeListRes2 = realTimeList();
            message = JSON.parseObject(realTimeListRes2).getString("message");
            checkCode(realTimeListRes2, StatusCode.SUCCESS, message + "---6、realTimeList");

            String[] expectState = {alarmStatesOutOfStock};
            checkRealtimeListAlarmStates(realTimeListRes2, unitCode, expectState);

//            7、通知(pick 2，此时拿空了)
            customerMessage(unitCode, typePick, posi, Pchng2, Ptotal2);

//            8、心跳
            heartBeat(unitCode, plateCode);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            9、平面图货架列表(空了)
            realTimeListRes0 = realTimeList();
            message = JSON.parseObject(realTimeListRes0).getString("message");
            checkCode(realTimeListRes0, StatusCode.SUCCESS, message + "--- 9、realTimeList");

            expectState = new String[]{alarmStatesOutOfStock};
            checkRealtimeListAlarmStates(realTimeListRes0, unitCode,expectState);

//            10、货架单元详情
            unitDetailRes0 = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes0).getString("message");
            checkCode(unitDetailRes0, StatusCode.SUCCESS, message + "---10、unitDetail");

            expectState = new String[]{alarmStatesOutOfStock};
            checkUnitDetailAlarmStates(unitDetailRes0, latticeId,expectState);

            latticeId = checkUnitDetail(unitDetailRes0, 1);

//            11、单元格物品详情
            latticeDetailRes0 = latticeDetail(latticeId);

            latticeDetailJo0 = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes0).getString("message");
            checkCode(latticeDetailRes0, StatusCode.SUCCESS, message + "---11、latticeDetail");

            JSONArray latticeDetailAlarmStates = checkLatticeDetailAlarmStates(latticeDetailRes0);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(latticeDetailAlarmStates, 1, expectState, "---11、latticeDetailAlarmStates-还剩0个商品");

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData("1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessageDropJo3 +
                    "\n\n" + "3、lattice binding: " + latticeBindingJo + "\n\n" + "4、customer message: " + customerMessagePickJo1 +
                    "\n\n" + "5、8、heart beat: " + heartBeatJo + "\n\n" + "6、9、real time list: " + realTimeListJo +
                    "\n\n" + "7、customer message: " +  customerMessagePickJo2 + "\n\n" + "10、unit detail: " + unitDetailJo0 +
                    "\n\n" + "11、lattice detail: " + latticeDetailJo0);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            realTimeListResJo2 = JSONObject.parseObject(realTimeListRes2);
            realTimeListResJo0 = JSONObject.parseObject(realTimeListRes0);
            unitDetailResJo0 = JSONObject.parseObject(unitDetailRes0);
            latticeDetailResJo0 = JSONObject.parseObject(latticeDetailRes0);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "3、lattice binding: " + latticeBindingResJo +
                    "\n\n" + "6、real time list: " + realTimeListResJo2 + "\n\n" + "9、real time list: " + realTimeListResJo0
                    + "\n\n" + "10、unit detail: " + unitDetailResJo0 + "\n\n" + "11、lattice detail: " + latticeDetailResJo0);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //----(12)-----在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态；
    //                                (3)再拿走一个正确商品，观察状态；(4)再拿走一个正确商品观察状态

//	1. 创建单元-2.货架事件通知（drop 3）-3.单元格物品绑定（绑定3个）-4.货架事件通知（drop 1 不同商品 剩余3对1错）-5.心跳
//	6.平面图货架列表（理货）-7.货架单元详情（理货，库存3）-8.单元格物品详情（理货，库存3）-9.货架事件通知（pick 1正确商品 剩余2对1错）
//  10.心跳-11.平面图货架列表（理货）-12.货架单元详情（理货，缺货，库存2）-13.单元格物品详情（理货，缺货，库存2）
//  14.货架事件通知（pick 1正确商品 剩余1对1错）-15.心跳-16.平面图货架列表（理货）-17.货架单元详情（理货，缺货，库存1）-
//  18.单元格物品详情（理货，缺货，库存1）-19.货架事件通知（pick 1错误商品 剩余1对）-20.心跳-21.平面图货架列表（缺货）
//  22.货架单元详情（理货，库存0）-23.单元格物品详情（理货，库存0）

    @Test(dataProvider = "CHECK_TYPE")
    private void TestdropWrongGoods(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + checkType;
        String caseDesc = "用错误商品干扰，观察告警状态";
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

        String unitCode = ciCaseName;
        String plateCode = "666";

        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesOutOfStock,alarmStatesWrongPlace};
        String[] alarmStates3 = {alarmStatesOutOfStock,alarmStatesWrongPlace};
        String[] alarmStates4 = {alarmStatesOutOfStock,alarmStatesWrongPlace};

        try {
//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            response = createUnit(unitCode, plateCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---1、createUnit");

//            2、货架事件通知(drop 3)
            customerMessage(unitCode, typeDrop, posi, Dchng, Dtotal);

//            货架单元详情，为了获取latticeId
            String unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(unitDetailRes, 1);

//            3、单元格物品绑定(绑定3个)
            response = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType);

            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            4、货架事件通知(drop one different good)
            customerMessage(unitCode, typeDrop, posi, Dchng1, Dtotal1);

//            5、心跳
            heartBeat(unitCode, plateCode);

//            6、平面图货架列表(此时有 3个正确 1个错误  期待理货)
            response = realTimeList();
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---6、realTimeList");
            checkRealtimeListAlarmStates(response, unitCode,alarmStates1);

//            7、货架单元详情
            logger.info("---------------------------------(5)---------------------------------------------------");
            response = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(response,latticeId,alarmStates1,3);

//            8、单元格物品详情
            logger.info("---------------------------------(6)---------------------------------------------------");
            response = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates1,3);


//            9、通知(pick 一个正确商品)
            customerMessage(unitCode, typePick, posi, Pchng1, Ptotal1);

//            10、心跳
            heartBeat(unitCode, plateCode);

//            11、平面图货架列表(还剩2个正确 1个错误 期待理货)
            response = realTimeList();
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---9、realTimeList");

            checkRealtimeListAlarmStates(response, unitCode, alarmStates2);

//            12、货架单元详情
            logger.info("---------------------------------(5)---------------------------------------------------");
            response = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(response,latticeId,alarmStates2,2);

//            13、单元格物品详情
            logger.info("---------------------------------(6)---------------------------------------------------");
            response = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates2,2);

//            14、货架事件通知(pick 一个正确商品)
            customerMessage(unitCode, typePick, posi, Pchng2, Ptotal2);

//            15、心跳
            heartBeat(unitCode, plateCode);

//            16、平面图货架列表(此时还剩1个正确 1个错误 期待理货和缺货)
            response = realTimeList();
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---12、realTimeList");

            checkRealtimeListAlarmStates(response, unitCode,alarmStates3);

//            17、货架单元详情
            logger.info("---------------------------------(5)---------------------------------------------------");
            response = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(response,latticeId,alarmStates3,1);

//            18、单元格物品详情
            logger.info("---------------------------------(6)---------------------------------------------------");
            response = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates3,1);

//            19、货架事件通知(pick 一个错误商品)
            customerMessage(unitCode, typePick, posi, Pchng3, Ptotal3);

//            20、心跳
            heartBeat(unitCode, plateCode);

//            21、平面图货架列表(此时还剩一个错误商品 期待理货)
            response = realTimeList();
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message + "---15、realTimeList");

            checkRealtimeListAlarmStates(response, unitCode,alarmStates4);

//            22、货架单元详情
            logger.info("---------------------------------(5)---------------------------------------------------");
            response = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(response,latticeId,alarmStates4,0);

//            23、单元格物品详情
            logger.info("---------------------------------(6)---------------------------------------------------");
            response = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(response, alarmStates4,0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //将入参入库
            aCase.setRequestData("//1. 创建单元-2.货架事件通知（drop 3）-3.单元格物品绑定（绑定3个）-4.货架事件通知（drop 1 不同商品 剩余3对1错）-5.心跳\n" +
                    "//6.平面图货架列表（理货）-7.货架单元详情（理货，库存3）-8.单元格物品详情（理货，库存3）-9.货架事件通知（pick 1正确商品 剩余2对1错）\n" +
                    "//  10.心跳-11.平面图货架列表（理货）-12.货架单元详情（理货，缺货，库存2）-13.单元格物品详情（理货，缺货，库存2）\n" +
                    "//  14.货架事件通知（pick 1正确商品 剩余1对1错）-15.心跳-16.平面图货架列表（理货）-17.货架单元详情（理货，缺货，库存1）-\n" +
                    "//  18.单元格物品详情（理货，缺货，库存1）-19.货架事件通知（pick 1错误商品 剩余1对）-20.心跳-21.平面图货架列表（缺货）\n" +
                    "//  22.货架单元详情（理货，库存0）-23.单元格物品详情（理货，库存0）");

            aCase.setResponse(JSON.parseObject(response) + "");

            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }

    //----(13)-----在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态；
    //                                (3)再拿走一个错误商品，观察状态；
    // 1、创建单元--2、通知(drop 3)-3、绑定(3个)-4、通知(drop 1错， 3对1错)-5、单元详情(理货 库存为3)-6、单元格详情（理货 库存为3）--------------
    // 7、通知(pick 1正确的， 2对1错)-8、单元详情（理货 库存为2）-9、单元格详情(理货 库存为2)-10、通知(pick 1错的，2对)-----------------------------
    //11、单元详情（缺货 库存为2 ）-12、单元格物品详情(缺货 库存为2 )-----------------------------------------------------------------------------------
    @Test(dataProvider = "CHECK_TYPE")
    private void TestGoodsStock(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + checkType;
        String caseDesc = "用错误商品干扰，观察告警状态和库存";
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

        String createUnitRes = null;
        String latticeBindingRes = null;
        String unitDetailRes1 = null;
        String latticeDetailRes1 = null;
        String unitDetailRes2 = null;
        String latticeDetailRes2 = null;
        String unitDetailRes3 = null;
        String latticeDetailRes3 = null;

        JSONObject createUnitResJo;
        JSONObject latticeBindingResJo;
        JSONObject unitDetailResJo1;
        JSONObject latticeDetailResJo1;
        JSONObject unitDetailResJo2;
        JSONObject latticeDetailResJo2;
        JSONObject unitDetailResJo3;
        JSONObject latticeDetailResJo3;

        JSONObject createUnitJo;
        JSONObject latticeBindingJo;
        JSONObject unitDetailJo;
        JSONObject latticeDetailJo;
        JSONObject customerMessageDropJo30;
        JSONObject customerMessageDropWrongJo31;
        JSONObject customerMessagePickJo21;
        JSONObject customerMessagePickJo20;

        Case aCase = new Case();
        failReason = "";

        String unitCode = ciCaseName;
        String plateCode = "666";

        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String message = "";

        int latticeId = 0;

        String[] alarmStates1 = {alarmStatesWrongPlace};
        String[] alarmStates2 = {alarmStatesOutOfStock,alarmStatesWrongPlace};
        String[] alarmStates3 = {alarmStatesOutOfStock};


        try {

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            logger.info("---------------------------------(1)---------------------------------------------------");
            createUnitRes = createUnit(unitCode, plateCode);
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message + "---1、createUnit");

//            2、货架事件通知(drop 3)
            logger.info("---------------------------------(2)---------------------------------------------------");

            customerMessage(unitCode, typeDrop, posi, Dchng, Dtotal);

//            货架单元详情，为了获取latticeId
            String unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message + "---2、unitDetail");
            latticeId = checkUnitDetail(unitDetailRes, 1);

//            3、单元格物品绑定(绑定3个)
            logger.info("---------------------------------(3)---------------------------------------------------");
            latticeBindingRes = latticeBinding(latticeId, goodsId, bindingStock, bindingTotal, checkType);

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message + "---3、latticeBinding");

//            4、货架事件通知(drop one different good)
            logger.info("---------------------------------(4)---------------------------------------------------");
            customerMessage(unitCode, typeDrop, posi, Dchng1, Dtotal1);

            heartBeat(unitCode,plateCode);

//            5、货架单元详情(理货 stock为3)
            logger.info("---------------------------------(5)---------------------------------------------------");
            unitDetailRes1 = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(unitDetailRes1,latticeId,alarmStates1,3);

//            6、单元格物品详情
            logger.info("---------------------------------(6)---------------------------------------------------");
            latticeDetailRes1 = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(latticeDetailRes1, alarmStates1,3);

//            7、通知(pick 一个正确商品)
            logger.info("---------------------------------(7)---------------------------------------------------");
            customerMessage(unitCode, typePick, posi, Pchng1, Ptotal1);

//            8、货架单元详情(理货 stock为2)
            logger.info("---------------------------------(8)---------------------------------------------------");
            unitDetailRes2 = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(unitDetailRes2,latticeId,alarmStates2,2);

            heartBeat(unitCode,plateCode);

//            9、单元格物品详情
            logger.info("---------------------------------(9)---------------------------------------------------");
            latticeDetailRes2 = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(latticeDetailRes2, alarmStates2,2);

//            10、货架事件通知(pick 一个错误商品)
            logger.info("---------------------------------(10)---------------------------------------------------");
            customerMessage(unitCode, typePick, posi, Pchng2, Ptotal2);

            heartBeat(unitCode,plateCode);

//            11、货架单元详情(缺货，库存为2)
            logger.info("---------------------------------(11)---------------------------------------------------");
            unitDetailRes3 = unitDetail(unitCode);
            checkAlarmStateAndGoodsStockByUnitDetail(unitDetailRes3,latticeId, alarmStates3,2);

//            12、单元格物品详情
            logger.info("---------------------------------(12)---------------------------------------------------");
            latticeDetailRes3 = latticeDetail(latticeId);
            checkAlarmStateAndGoodsStockByLatticeDetail(latticeDetailRes3, alarmStates3,2);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, plateCode));

            customerMessageDropJo30 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, Dchng, Dtotal));
            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId, bindingStock, bindingTotal, checkType));

            customerMessageDropWrongJo31 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, Dchng1, Dtotal1));

            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));
            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            customerMessagePickJo21 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, Pchng1, Ptotal1));
            customerMessagePickJo20 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, Pchng2, Ptotal2));


            //将入参入库
            aCase.setRequestData("1、create unit: " + createUnitJo + "\n\n" + "2、customer message: " + customerMessageDropJo30 +
                    "\n\n" + "3、lattice binding: " + latticeBindingJo +
                    "\n\n" + "4、customer message: "  + customerMessageDropWrongJo31 +
                    "\n\n" + "7、customer message: " + customerMessagePickJo21 + "\n\n" + "10、customer message: " + customerMessagePickJo20 +
                    "\n\n" + "13、unit detail: " + unitDetailJo + "\n\n" + "13、lattice detail: " + latticeDetailJo
            );

            //response save to DB.
            createUnitResJo     = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitDetailResJo1    = JSONObject.parseObject(unitDetailRes1);
            latticeDetailResJo1 = JSONObject.parseObject(latticeDetailRes1);
            unitDetailResJo2    = JSONObject.parseObject(unitDetailRes2);
            latticeDetailResJo2 = JSONObject.parseObject(latticeDetailRes2);
            unitDetailResJo3    = JSONObject.parseObject(unitDetailRes3);
            latticeDetailResJo3 = JSONObject.parseObject(latticeDetailRes3);

            aCase.setResponse("1、create unit: " + createUnitResJo + "\n\n" + "3、lattice binding: " + latticeBindingResJo +
                    "\n\n" + " " +unitDetailResJo1 + "\n\n" + ""  + latticeDetailResJo1 +
                    "\n\n" + " " +unitDetailResJo2 + "\n\n" + " "  + latticeDetailResJo2 +
                    "\n\n" + " "  + unitDetailResJo3 + "\n\n" + ""  + latticeDetailResJo3);
            qaDbUtil.saveToCaseTable(aCase);

//            删除
            delete(shelvesCode, unitCode);
        }
    }


    //--------------------------------------------------测试创建感压板------------------------------------------------------------------------
    @Test
    public void checkCreateSensor() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "测试创建感压板";
        logger.info(caseDesc + "--------------------");
        Case aCase = new Case();
        failReason = "";

        JSONObject createSensorJo;

        JSONObject createSensorResJo;

        String createSensorRes = "";

        String plateCode = UUID.randomUUID().toString();

        try {

            createSensorRes = createSensor(plateCode);
            checkCode(createSensorRes,StatusCode.SUCCESS,"");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织入参
            createSensorJo = JSON.parseObject(genCreateSensorPara(plateCode));

            aCase.setRequestData(createSensorJo + "");

            createSensorResJo = JSON.parseObject(createSensorRes);

            aCase.setResponse(createSensorResJo + "");

            qaDbUtil.saveToCaseTable(aCase);
        }
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

        Assert.assertEquals(alarmStatesRes,expectStates);
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
        String[] alarmStatesRes = new  String[size];
        for (int i = 0; i < alarmStatesJsonArr.size(); i++) {
            alarmStatesRes[i] = alarmStatesJsonArr.getString(i);
        }

        Assert.assertEqualsNoOrder(alarmStatesRes,expectStates);
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

        Assert.assertEqualsNoOrder(stateRes, expectState, message);
    }


    private int checkUnitDetail(String response, int expectNum) {
        JSONArray latticeList = JSON.parseObject(response).getJSONObject("data").getJSONArray("lattice_list");
        if (latticeList.size() != expectNum) {
            Assert.fail("checkUnitDetail - lattice numbers is not as created! ");
        }

        int latticeId = latticeList.getJSONObject(0).getInteger("lattice_id");
//        String latticeId = latticeList.getJSONObject(0).getString("lattice_id");

        return latticeId;
    }

    private  void checkUnbindRealTimeList(String response){

        String message = "解绑后，平面图货架列表";
        JSONObject singleFloor = JSON.parseObject(response).getJSONObject("data").getJSONArray("floors").getJSONObject(0);
        JSONArray unitList = singleFloor.getJSONArray("unit_list");
        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);

            JSONArray alarmStates = singleUnit.getJSONArray("alarm_states");
            Assert.assertEquals(alarmStates.size(),0, message + "告警状态没有置空！");

            String stocktakingStates = singleUnit.getString("stocktaking_state");
            Assert.assertEquals(stocktakingStates, "FINISHED", "没有将stocktaking_state置成FINISHED");
        }
    }

    private  void checkUnbindUnitDetail(String response){

        String message = "解绑后，货架单元详情";
        JSONArray latticeList = JSON.parseObject(response).getJSONObject("data").getJSONArray("lattice_list");
        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLattice = latticeList.getJSONObject(i);

            JSONArray alarmStates = singleLattice.getJSONArray("alarm_states");
            Assert.assertEquals(alarmStates.size(),0, message + "告警状态没有置空！");

            String stocktakingStates = singleLattice.getString("stocktaking_state");
            Assert.assertEquals(stocktakingStates, "FINISHED", message + "没有将stocktaking_state置成FINISHED");
        }
    }

    private  void checkUnbindLatticeDetail(String response){

        String message = "解绑后，货架单元详情的data字段不为空！";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        Assert.assertEquals(data.size(), 0, message);
    }

    private  void checkRealTimeList(String response, String stocktakingStateExpect){

        String message = "离火后，平面图货架列表";
        JSONObject singleFloor = JSON.parseObject(response).getJSONObject("data").getJSONArray("floors").getJSONObject(0);
        JSONArray unitList = singleFloor.getJSONArray("unit_list");
        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);

            JSONArray alarmStates = singleUnit.getJSONArray("alarm_states");
            Assert.assertEquals(alarmStates.size(),0, message + "告警状态没有置空！");

            String stocktakingStates = singleUnit.getString("stocktaking_state");
            Assert.assertEquals(stocktakingStates, stocktakingStateExpect, "没有将stocktaking_state置成" + stocktakingStateExpect);
        }
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
                String[]  alarmStatesArr = new String[len];
                for (int i1 = 0; i1 < len; i1++) {
                    alarmStatesArr[i1] = alarmStatesArrRes.getString(i1);
                }

                Assert.assertEqualsNoOrder(alarmStatesArr,alarmStates, "告警状态不对！");

                int goodsStockRes = singleLittice.getInteger("goods_stock");
                Assert.assertEquals(goodsStockRes,goodsStock,"库存信息不对！");
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
        String[]  alarmStatesArr = new String[len];
        for (int i1 = 0; i1 < len; i1++) {
            alarmStatesArr[i1] = alarmStatesArrRes.getString(i1);
        }
        Assert.assertEqualsNoOrder(alarmStatesArr,alarmStates, "告警状态不对！");

        long goodsStockRes = responseJo.getJSONObject("data").getLong("goods_stock");
        Assert.assertEquals(goodsStockRes, goodsStock, "库存信息不对!");
    }


    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, message);
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

//    private ApiResponse sendRequestWithGate(String router, String[] resource, String json) throws Exception {
//        try {
//            Credential credential = new Credential("77327ffc83b27f6d", "7624d1e6e190fbc381d0e9e18f03ab81");
//            // 封装request对象
//            String requestId = UUID.randomUUID().toString();
//            ApiRequest apiRequest = new ApiRequest.Builder()
//                    .uid("uid_7fc78d24")
//                    .appId("097332a388c2")
//                    .requestId(requestId)
//                    .version(SdkConstant.API_VERSION)
//                    .router(router)
//                    .dataResource(resource)
//                    .dataBizData(JSON.parseObject(json))
//                    .build();
//
//            // client 请求
//            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
//            apiResponse = apiClient.doRequest(apiRequest);
//            logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
//            logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));
//        } catch (SdkClientException e) {
//            String msg = e.getMessage();
//            throw new Exception(msg);
//        } catch (Exception e) {
//            throw e;
//        }
//        return apiResponse;
//    }
//
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

    private void genAuth() {

        String json =
                "{\n" +
                        "  \"email\": \"demo@winsense.ai\",\n" +
                        "  \"password\": \"fe01ce2a7fbac8fafaed7c982a04e229\"\n" +
                        "}";
        try {
            response = sendRequestWithUrl(genAuthURL, json, header);
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authorization = data.getString("token");

            header.put("Authorization", authorization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName){
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
                new Object[]{"TALLY", -100, 0, 100, 100, 3, 300, true, true, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "TALLY-1"},//单元盘货是成功的，因为没绑定成功就相当于
//                                                                                                                该单元没有物品，就是已盘状态
                new Object[]{"TALLY", -100, 0, 100, 100, 3, 300, false, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "TALLY-2"},
                new Object[]{"TALLY", -100, 0, 50, 120, 3, 300, true, false, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "TALLY-3"},
                new Object[]{"STOCKTAKING", -100, 500, 100, 600, 6, 600, true, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "STOCKTAKING-1"},
                new Object[]{"STOCKTAKING", -100, 0, 100, 100, 3, 300, false, true, StatusCode.SUCCESS, StatusCode.SUCCESS, "STOCKTAKING-2"},
                new Object[]{"STOCKTAKING", -100, 0, 50, 120, 3, 300, true, true, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "STOCKTAKING-3"}
        };
    }

    @DataProvider(name = "TALLY&STOCKTAKING_WITHOUT_SCAN")
    private static Object[][] tallyAndStocktakingWithoutScan() {
        //checkType, changeD, totalD, bindingStock, bindingToatal, LatticeBindingExpectCode, unitStocktakingExpectCode,caseId
        return new Object[][]{
                new Object[]{"TALLY", 100, 300, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS, "TALLY-1" },
                new Object[]{"TALLY", 100, 100, 3, 300, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "TALLY-2"},
                new Object[]{"TALLY", 50, 120, 3, 300, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "TALLY-3"},
                new Object[]{"STOCKTAKING", 100, 300, 3, 300, StatusCode.SUCCESS, StatusCode.SUCCESS, "STOCKTAKING-1"},
                new Object[]{"STOCKTAKING", 100, 100, 3, 300, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "STOCKTAKING-2"},
                new Object[]{"STOCKTAKING", 50, 120, 3, 300, StatusCode.INTERNAL_SERVER_ERROR, StatusCode.SUCCESS, "STOCKTAKING-3"},
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

    @DataProvider(name = "UNBIND_RESULT")
    private static Object[][] testUnbindResult() {
        //checkType, Pchng, Ptotal, Dchng, Dtotal, bindingStock, bindingToatal,
        return new Object[][]{
                new Object[]{"TALLY", -100, 400, 100, 500, 5, 500},
                new Object[]{"STOCKTAKING", -100, 400, 100, 500, 5, 500},
        };
    }
}
