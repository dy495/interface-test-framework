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
    private static long SHOP_ID = 447;
    private String genAuthURL = "http://dev.sso.winsenseos.com/sso/login";
    private String createUnitURL = "http://dev.app.winsenseos.com/operation/app/COMMODITY/external/CREAT_UNIT/v1.0";
    private String cerateConfigURL = "http://dev.app.winsenseos.com/operation/app/COMMODITY/external/MODIFY_PLATE_CONFIG/v1.0";
    private String deleteURL = "http://dev.app.winsenseos.com/operation/app/COMMODITY/external/DELETE_UNIT/v1.0";

    private String URL_prefix = "http://dev.app.winsenseos.com/operation/app/COMMODITY/";
    private String realTimeListServiceId = "3015";
    private String unitDetailServiceId = "3016";
    private String latticeDetailServiceId = "3017";
    private String latticeCheckServiceId = "3018";
    private String latticeBindingServiceId = "3019";
    private String unitstockingFinishServiceId = "3020";
    private String shopstockingFinishServiceId = "3021";
    private String latticeUnbindServiceId = "3022";

    private String typePick = "PICK";
    private String typeDrop = "DROP";

    private String checkTypeTally = "TALLY";
    private String checkTypeStocktaking = "STOCKING";

    private String alarmStatesOutOfStock = "OUT_OF_STOCK";
    private String alarmStatesCamereError = "CAMERA_ERROR";
    private String alarmStatesCensorError = "CENSOR_ERROR";
    private String alarmStatesWrongPlace = "WRONG_PLACE";
//    private String alarmStatesCamereError =  "CAMERA_ERROR";

    HashMap<String, String> header = new HashMap();


    private String shelvesCode = "hello";

    private String authorization = null;

    private long goodsId3Add2 = 139;
    private long goodsIdOreo = 141;
    private long goodsIdGanten = 142;

    private ApiResponse apiResponse;
    private String response;

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_AD_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_AD_SERVICE;
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/ad_test/buildWithParameters?case_name=";


    //---------------------- 0.0 创建config-----------------------------
    private String createConfig(String unitCode, int rowNum, int colNum) throws Exception {
        logger.info("\n");
        logger.info("------------create config!-----------------------");

//        组织参数
        String json = genCreateConfigPara(unitCode, rowNum, colNum);

        try {
            response = sendRequestWithUrl(cerateConfigURL, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        return response;
    }

    private String genCreateConfigPara(String unitCode, int rowNum, int colNum) {

        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"unit_code\":\"" + unitCode + "\"," +
                        "        \"row_num\":\"" + rowNum + "\"," +
                        "        \"col_num\":\"" + colNum + "\"," +
                        "        \"config\":[{\n" +
                        "                        \"sensor_id\":0,\n" +
                        "                        \"k\":0.25273,\n" +
                        "                        \"zero\":5655.46667\n" +
                        "                    },\n" +
                        "                    {\n" +
                        "                        \"sensor_id\":1,\n" +
                        "                        \"k\":0.252754,\n" +
                        "                        \"zero\":1664.816667\n" +
                        "                    }\n" +
                        "]\n" +
                        "        \t\n" +
                        "        \n" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID().toString() + "\",\n" +
                        "    \"system\":{\n" +
                        "        \"app_id\":\"" + APP_CODE + "\",\n" +
                        "        \"source\":\"BIZ\"\n" +
                        "    }\n" +
                        "}";

        return json;
    }

    //-------------------------0.1 创建unit---------------------------------
    private String createUnit(String unitCode, int rowCount, int colMax, String rowProp) throws Exception {
        logger.info("\n");
        logger.info("------------create unit!-----------------------");

        //组织参数
        String json = genCreateUnitPara(unitCode, rowCount, colMax, rowProp);

        try {
            response = sendRequestWithUrl(createUnitURL, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        return response;
    }

    private String genCreateUnitPara(String unitCode, int rowCount, int colMax, String rowProp) throws Exception {

        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"subject_id\":" + SHOP_ID + ",\n" +
                        "        \"shelves_code\":\"" + shelvesCode + "\"," +
                        "        \"unit_code\":\"" + unitCode + "\"," +
                        "        \"position\":{\"x\": 0.55, \"y\": 0.84},\n" +
                        "        \"row_count\":\"" + rowCount + "\"," +
                        "        \"col_max\":\"" + colMax + "\"," +
                        "        \"row_prop\":[" + rowProp + "]\n" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID().toString() + "\",\n" +
                        "    \"system\":{\n" +
                        "        \"app_id\":\"" + APP_CODE + "\",\n" +
                        "        \"source\":\"BIZ\"\n" +
                        "    }\n" +
                        "}";
        return json;
    }

    //------------------------0.2 心跳-----------------------------------
    private String heartBeat(String unitCode, int row, int col) throws Exception {
        logger.info("\n");
        logger.info("------------heart beat!-----------------------");
        String router = "/commodity/external/HEART_BEAT/v1.0";
        String[] resource = new String[]{};
        String message = "";

        //组织参数
        String json = genHeartBeatPara(unitCode, row, col);

        try {
            apiResponse = sendRequestWithAuth(router, resource, json);
            message = apiResponse.getMessage();
            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);
        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }

        return response;
    }

    private String genHeartBeatPara(String unitCode, int row, int col) throws Exception {

        long currentTime = System.currentTimeMillis();

        String json =
                "{\n" +
                        "    \"unit_code\":\"" + unitCode + "\"," +
                        "    \"camera\":\"NORMAL\",\n" +
                        "    \"timestamp\":" + "\"" + currentTime + "\"," +
                        "    \"plate\":[\n" +
                        "        {\n" +
                        "            \"plate_code\":1,\n" +
                        "            \"row\":" + row + "," +
                        "            \"col\":" + col + "," +
                        "            \"sensors\":[\n" +
                        "                {\n" +
                        "                    \"sensor_id\":0,\n" +
                        "                    \"status\":\"NORMAL\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"sensor_id\":1,\n" +
                        "                    \"status\":\"NORMAL\"\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
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
            apiResponse = sendRequestWithAuth(router, resource, json);
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

    private void delete(String shelvesCode, String unitCode) throws Exception {
        logger.info("\n");
        logger.info("------------delete unit!-----------------------");

        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"shelves_code\":\"" + shelvesCode + "\"," +
                        "        \"unit_code\":\"" + unitCode + "\"" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID().toString() + "\",\n" +
                        "    \"system\":{\n" +
                        "        \"app_id\":\"" + APP_CODE + "\",\n" +
                        "        \"source\":\"BIZ\"\n" +
                        "    }\n" +
                        "}";
        try {
            response = sendRequestWithUrl(deleteURL, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }

    }

    //-----------心跳以后，平面图货架列表中的alarm_states字段为空-------------------------------------------------------------
    //-------------------------（1）---------1、创建单元-2、创建配置-3、心跳-4、平面图货架列表------------------------------------
    @Test
    private void alarmStatesTest() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
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

        String unitCodeTest = "testAlarmStates";
        String message = "";
        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            JSONObject createUnitJo = JSON.parseObject(genCreateUnitPara(unitCodeTest, 4, 4,
                    "\"DEFAULT\",\"DEFAULT\",\"DEFAULT\",\"DEFAULT\""));
            JSONObject createConfigJo = JSON.parseObject(genCreateConfigPara(unitCodeTest, 0, 2));
            JSONObject heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCodeTest, 0, 2));
            JSONObject realTimeListJo = JSON.parseObject(genRealTimeListPara());

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + createConfigJo + "\n\n" + heartBeatJo + "\n\n" + realTimeListJo);

//            删除
            delete(shelvesCode, unitCodeTest);

//            1、创建单元
            createUnitRes = createUnit(unitCodeTest, 4, 4, "\"DEFAULT\",\"DEFAULT\",\"DEFAULT\",\"DEFAULT\"");

            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, "");

//            2、创建配置
            createConfigRes = createConfig(unitCodeTest, 0, 2);
            message = JSON.parseObject(createConfigRes).getString("message");
            checkCode(createConfigRes, StatusCode.SUCCESS, message);

//            3、心跳
            heartBeat(unitCodeTest, 0, 2);

//            4、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message);

//            检测返回的状态
            checkRealtimeListAlarmStates(realTimeListRes, unitCodeTest);

//            删除
            delete(shelvesCode, unitCodeTest);

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

            aCase.setResponse(createUnitResJo + "\n\n" + createConfigResJo + "\n\n" + realTimeListResJo);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ----------------------------创建单元后，单元详情中能查询到新增的单元--------------------------------
    //------------------（2）--------------------1、创建单元-2、货架单元详情-------------------------
    @Test
    private void testUnitDetail() throws Exception {

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

        String unitCodeTest = "testUnitDetail";
        String message = "";
        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            JSONObject createUnitJo = JSON.parseObject(genCreateUnitPara(unitCodeTest, 4, 4,
                    "\"DEFAULT\",\"DEFAULT\",\"DEFAULT\",\"DEFAULT\""));
            JSONObject unitDetailJo = JSON.parseObject(unitDetail(unitCodeTest));

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + unitDetailJo);

//            删除
            delete(shelvesCode, unitCodeTest);

//            1、创建单元
            createUnitRes = createUnit(unitCodeTest, 3, 5, "\"1-2-2\",\"3-2\",\"2-2-1\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            2、货架单元详情
            unitDetailRes = unitDetail(unitCodeTest);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);

//            检测单元详情的lattice数量
            checkUnitDetail(unitDetailRes, 8);//看新建的时候分的单元格数量

//            删除
            delete(shelvesCode, unitCodeTest);
        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);

            aCase.setResponse(createUnitResJo + "\n\n" + unitDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ------------------没有事件时直接进行单元格物品扫描（盘、理）---------------------------------
    //-----------------（3）---------------------单元格物品扫描--------------------------------
    @Test(dataProvider = "CHECK_TYPE")
    private void TestLatticeCheck(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "没有事件时直接进行单元格物品扫描（盘、理）";
        logger.info(caseDesc + "--------------------");

        String latticeCheckRes = null;

        JSONObject latticeCheckResJo;

        Case aCase = new Case();
        failReason = "";

        String message = "";

        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            JSONObject latticeCheckJo = JSON.parseObject(genLatticeCheckPara(1, 1, checkType));

            //将入参入库
            aCase.setRequestData(latticeCheckJo + "\n");

            latticeCheckRes = latticeCheck(1, 1, checkType);
            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.INTERNAL_SERVER_ERROR, message);
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
    private void TestLatticeBinding(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "没有事件时直接单元格物品绑定（盘、理）";
        logger.info(caseDesc + "--------------------");

        String latticeBindingRes = null;

        JSONObject latticeBindingResJo;

        Case aCase = new Case();
        failReason = "";

        String message = "";

        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            JSONObject latticeBindingJo = JSON.parseObject(genLatticeBindingPara(1, goodsIdGanten, 3, 300, checkType));

            //将入参入库
            aCase.setRequestData(latticeBindingJo + "\n");


            latticeBindingRes = latticeBinding(1, goodsIdGanten, 3, 300, checkType);
            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.INTERNAL_SERVER_ERROR, message);
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
    @Test(dataProvider = "LATTICE_CHECK&UNIT_STOCKING_WITH_SCAN")
    private void TestLatticeCheckAndUnitStockingWithScan(String checkType, long changeP, long totalP, long changeD, long totalD,
                                                         boolean expectBinding, boolean expectUnitStocktaking,
                                                         boolean expectShopStocktaking, int expectCode) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
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
        String unitCode = "TestLatticeCheckAndUnitStockingWithScan";
        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 4, 4,
                    "\"DEFAULT\",\"DEFAULT\",\"DEFAULT\",\"DEFAULT\""));
            customerMessagePickJo = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, changeP, totalP));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, changeD, totalD));
            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, 2, 3, "\"1-2\",\"2-1\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            String unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 4);

//            2、货架事件通知（pick）
            customerMessage(unitCode, typePick, posi, changeP, totalP);


//            3、商品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType);
            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

            //由于latticeId必须在过程中取，所以不能将取扫描的参数放在开头。
            latticeCheckJo = JSON.parseObject(latticeCheck(latticeId, goodsId3Add2, checkType));

//            4、货架事件通知（drop）
            customerMessage(unitCode, typeDrop, posi, changeD, totalD);

//            5、单元格物品绑定
            if (expectBinding) {
                latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, 3, 300, checkType);

                latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, 3, 300, checkType));
                message = JSON.parseObject(latticeBindingRes).getString("message");
                checkCode(latticeBindingRes, expectCode, message);
            }

//            6、单元盘货完成
            if (expectUnitStocktaking) {
                unitStocktakingRes = unitStocktaking(unitCode);
                message = JSON.parseObject(unitStocktakingRes).getString("message");
                checkCode(unitStocktakingRes, expectCode, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessagePickJo + "\n\n" + latticeCheckJo +
                    "\n\n" + customerMessageDropJo + latticeBindingJo + "\n\n" + unitStocktakingJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeCheckResJo + "\n\n" + latticeBindingResJo + "\n\n" + unitStocktakingResJo);
            qaDbUtil.saveToCaseTable(aCase);


        }
    }

    //--------------------------不扫描就盘货和理货，物品放置正确和错误盘货是否成功---------------------------
    //--------(6)-------------------1、新建单元-2、货架事件通知（drop）-3、单元格物品绑定-4、单元盘货完成----------------
    @Test(dataProvider = "LATTICE_CHECK&UNIT_STOCKING_WITHOUT_SCAN")
    private void TestLatticeCheckAndUnitStockingWithoutScan(String checkType, long changeD, long totalD, int expectCode) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
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
        String unitCode = "TestLatticeCheckAndUnitStockingWithoutScan";

        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 2, 3, "\"1-2\",\"2-1\""));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, changeD, totalD));

            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            response = createUnit(unitCode, 2, 3, "\"1-2\",\"2-1\"");
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            response = unitDetail(unitCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(response, 4);

//            2、货架事件通知（drop）
            customerMessage(unitCode, typeDrop, posi, changeD, totalD);

//            3、单元格物品绑定
            response = latticeBinding(latticeId, goodsId3Add2, 3, 300, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, 3, 300, checkType));

            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCode, message);

//            4、单元盘货完成
            response = unitStocktaking(unitCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, expectCode, message);

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessageDropJo + "\n\n" + latticeBindingJo +
                    "\n\n" + unitStocktakingJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeBindingResJo + "\n\n" + unitStocktakingResJo);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //-------------------------测试扫描盘货和理货后，平面图货架列表、货架单元详情和单元格物品详情中的内容是否正确----------------
    //--------(7)-------------------1、新建单元-2、通知(pick one)-3、扫描（盘、理）-4、通知（drop）-5、绑定（同扫描）---------------------
    //------------------------------6、单元盘货-7、心跳-8、平面图货架列表-9、货架单元详情-10、单元格物品详情----------------
    @Test(dataProvider = "CHECK_TYPE")
    private void testLatticeBinding(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
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
        String unitCode = "testLatticeBinding";
        try {
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 2, 3, "\"1-2\",\"2-1\""));
            customerMessagePickJo = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -100, 0));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 300, 300));
            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, 0, 0));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, 2, 3, "\"1-2\",\"2-1\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 4);

//            2、货架事件通知（pick）
            customerMessage(unitCode, typePick, posi, -100, 0);

//            3、单元格物品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType);

//           --- 获取参数
            latticeCheckJo = JSON.parseObject(genLatticeCheckPara(latticeId, goodsId3Add2, checkType));

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

//            4、货架事件通知（drop 3）
            customerMessage(unitCode, typeDrop, posi, 300, 300);

//            5、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, 3, 300, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, 3, 300, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            6、单元盘货完成
            unitStocktakingRes = unitStocktaking(unitCode);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message);

//            7、心跳
            heartBeat(unitCode, 0, 0);

//            8、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message);

            checkRealtimeListStocktakingStates(realTimeListRes, unitCode);

//            9、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);

            checkUnitDetailStocktakingStates(unitDetailRes, unitCode, latticeId, 3);

//            10、单元格物品详情
            latticeDetailRes = latticeDetail(latticeId);

            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message);

            checkLatticeDetailGoodsStock(latticeDetailRes, 3);

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessagePickJo + "\n\n" + latticeCheckJo +
                    "\n\n" + customerMessageDropJo + latticeBindingJo + "\n\n" + unitStocktakingJo +
                    "\n\n" + heartBeatJo + "\n\n" + realTimeListJo + "\n\n" + unitDetailJo
                    + "\n\n" + latticeDetailJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);
            latticeDetailResJo = JSONObject.parseObject(latticeDetailRes);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeCheckResJo + "\n\n" + latticeBindingResJo + "\n\n" + unitStocktakingResJo
                    + "\n\n" + realTimeListResJo + "\n\n" + unitDetailResJo + "\n\n" + latticeDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

        }
    }

    //--------------------解绑后货架单元详情和单元格物品详情中都不能查询到该单元格-----------------------------
    //--------(8)-------------------1、新建单元-2、通知(pick one)-3、扫描(盘、理)-4、通知（drop）------------------------
    //------------------------------5、绑定（同扫描）-6、单元盘货-7、解绑-8、心跳-9、平面图货架列表----------------------
    //-----------------------------10、货架单元详情-11、单元格物品详情----------------
    @Test(dataProvider = "CHECK_TYPE")
    private void testUnbinding(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
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
        String unitCode = "TestUnbinding";
        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 2, 3, "\"1-2\",\"2-1\""));
            customerMessagePickJo = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -100, 0));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 300, 300));
            unitStocktakingJo = JSON.parseObject(genUnitStocktakingPara(unitCode));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, 0, 0));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, 2, 3, "\"1-2\",\"2-1\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 4);

//            2、货架事件通知（pick）
            customerMessage(unitCode, typePick, posi, -100, 0);

//            3、单元格物品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkType);

            latticeCheckResJo = JSON.parseObject(genLatticeCheckPara(latticeId, goodsId3Add2, checkType));

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

//            4、货架事件通知（drop 3）
            customerMessage(unitCode, typePick, posi, 300, 300);

//            5、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, 3, 300, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, 3, 300, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            6、单元盘货完成
            unitStocktakingRes = unitStocktaking(unitCode);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message);

//            7、单元物品解绑
            latticeUnbindRes = latticeUnbind(latticeId, goodsId3Add2);

            latticeUnbindJo = JSON.parseObject(latticeUnbind(latticeId, goodsId3Add2));

            message = JSON.parseObject(latticeUnbindRes).getString("message");
            checkCode(latticeUnbindRes, StatusCode.SUCCESS, message);

//            8、心跳
            heartBeat(unitCode, 0, 0);

//            9、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message);

//待定--------------------------------##########################################################################

//            10、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
//待定--------------##############################################################################################

//            11、单元格物品详情
            latticeDetailRes = latticeDetail(latticeId);

            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message);

//待定---------#############################################################################################################

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessagePickJo + "\n\n" + latticeCheckJo +
                    "\n\n" + customerMessageDropJo + latticeBindingJo + "\n\n" + unitStocktakingJo + "\n\n" + latticeUnbindJo +
                    "\n\n" + heartBeatJo + "\n\n" + realTimeListJo + "\n\n" + unitDetailJo
                    + "\n\n" + latticeDetailJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);
            latticeUnbindResJo = JSONObject.parseObject(latticeUnbindRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);
            latticeDetailResJo = JSONObject.parseObject(latticeDetailRes);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeCheckResJo + "\n\n" + latticeBindingResJo + "\n\n" + unitStocktakingResJo
                    + "\n\n" + latticeUnbindResJo + "\n\n" + realTimeListResJo + "\n\n" + unitDetailResJo + "\n\n" + latticeDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

        }
    }

    //----------------(9)---------测试一个店铺有两个单元，一个盘货，一个未盘货，那么店铺盘货失败---------------------------------------------------
    //------------------------------1、新建单元-2、通知(pick one)-3、扫描-4、通知（drop）----------------------------------------------------------
    //------------------------------5、绑定(理)-6、单元盘货完成-7、创建单元-8、通知（drop 3）--------------------------------------------------------
    // -----------------------------9、绑定（理）-10、店铺盘货------------------------------------------------------------------------------------

    //如果这样不成功的话，就unit1先盘后不经扫描理货，unit2直接盘货，这样就是unit1未盘货了。######################################################################
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
        JSONObject shopStocktakingJo2 = null;


        Case aCase = new Case();
        failReason = "";

        String posi = "0,0";               //"position":"row,col"

        String message = "";
        String unitCode_1 = "testShopStocktaking-1";
        String unitCode_2 = "testShopStocktaking-2";
        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo1 = JSON.parseObject(genCreateUnitPara(unitCode_1, 2, 3, "\"1-2\",\"2-1\""));
            customerMessagePickJo1 = JSON.parseObject(genCustomerMessagePara(unitCode_1, typePick, posi, -100, 0));
            customerMessageDropJo1 = JSON.parseObject(genCustomerMessagePara(unitCode_1, typeDrop, posi, 300, 300));
            unitStocktakingJo1 = JSON.parseObject(genUnitStocktakingPara(unitCode_1));
            createUnitJo2 = JSON.parseObject(genCreateUnitPara(unitCode_2, 2, 3, "\"1-2\",\"2-1\""));
            customerMessageDropJo2 = JSON.parseObject(genCustomerMessagePara(unitCode_2, typeDrop, posi, 300, 300));
            shopStocktakingJo2 = JSON.parseObject(genShopStocktakingPara());

            delete(shelvesCode, unitCode_1);

//            1、创建单元
            createUnitRes1 = createUnit(unitCode_1, 2, 3, "\"1-2\",\"2-1\"");
            message = JSON.parseObject(createUnitRes1).getString("message");
            checkCode(createUnitRes1, StatusCode.SUCCESS, message);

//            货架单元详情，取latticeId
            String unitDetailRes = unitDetail(unitCode_1);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 4);

//            2、货架事件通知（pick）
            customerMessage(unitCode_1, typePick, posi, -100, 0);

//            3、单元格物品扫描
            latticeCheckRes = latticeCheck(latticeId, goodsId3Add2, checkTypeStocktaking);

            latticeCheckJo = JSON.parseObject(genLatticeCheckPara(latticeId, goodsId3Add2, checkTypeStocktaking));

            message = JSON.parseObject(latticeCheckRes).getString("message");
            checkCode(latticeCheckRes, StatusCode.SUCCESS, message);

//            4、货架事件通知（drop 3）
            customerMessage(unitCode_1, typeDrop, posi, 300, 300);

//            5、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId3Add2, 3, 300, checkTypeStocktaking);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, 3, 300, checkTypeStocktaking));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            6、单元盘货完成
            unitStocktakingRes = unitStocktaking(unitCode_1);
            message = JSON.parseObject(unitStocktakingRes).getString("message");
            checkCode(unitStocktakingRes, StatusCode.SUCCESS, message);

//            7、创建单元
            createUnitRes2 = createUnit(unitCode_2, 2, 3, "\"1-2\",\"2-1\"");
            message = JSON.parseObject(createUnitRes2).getString("message");
            checkCode(createUnitRes2, StatusCode.SUCCESS, message);

//            8、货架事件通知
            customerMessage(unitCode_2, typeDrop, posi, 300, 300);

//            9、单元格物品绑定
            latticeBindingRes2 = latticeBinding(latticeId, goodsId3Add2, 3, 300, checkTypeTally);

            latticeBindingJo2 = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId3Add2, 3, 300, checkTypeTally));

            message = JSON.parseObject(latticeBindingRes2).getString("message");
            checkCode(latticeBindingRes2, StatusCode.SUCCESS, message);

//            10、店铺盘货完成
            shopStocktakingRes = shopStocktaking();
            message = JSON.parseObject(shopStocktakingRes).getString("message");
            checkCode(shopStocktakingRes, StatusCode.SUCCESS, message);

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {
            //将入参入库
            aCase.setRequestData(createUnitJo1 + "\n\n" + customerMessagePickJo1 + "\n\n" + latticeCheckJo +
                    "\n\n" + customerMessageDropJo1 + latticeBindingJo + "\n\n" + unitStocktakingJo1 +
                    "\n\n" + createUnitJo2 + "\n\n" + customerMessageDropJo2 + "\n\n" + latticeBindingJo2
                    + "\n\n" + shopStocktakingJo2);

            //response save to DB.
            createUnitResJo1 = JSONObject.parseObject(createUnitRes1);
            latticeCheckResJo = JSONObject.parseObject(latticeCheckRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            unitStocktakingResJo = JSONObject.parseObject(unitStocktakingRes);
            createUnitResJo2 = JSONObject.parseObject(createUnitRes2);
            latticeBindingResJo2 = JSONObject.parseObject(latticeBindingRes2);
            shopStocktakingResJo = JSONObject.parseObject(shopStocktakingRes);

            aCase.setResponse(createUnitResJo1 + "\n\n" + latticeCheckResJo + "\n\n" + latticeBindingResJo + "\n\n" + unitStocktakingResJo
                    + "\n\n" + createUnitResJo2 + "\n\n" + latticeBindingResJo2 + "\n\n" + shopStocktakingResJo);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------------------------盘货时只绑定1个商品，观察此时理货的状态---------------------------------------------
    //-----------------(10)---------------1、创建单元-2、通知(drop one)-3、绑定一个商品(盘货)-4、配置-----------------------
    ////----------------------------------------5、心跳-6、列表(缺货)-7、单元详情（缺货）-8、物品详情（缺货）-----------------------------
    @Test
    private void TestStockTakingOne() throws Exception {
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

        String unitCode = "TestStockTakingOne";
        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String response;
        String message = "";

        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 2, 2, "\"DEFAULT\",\"DEFAULT\""));
            customerMessageDropJo = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 100, 100));
            createConfigJo = JSON.parseObject(genCreateConfigPara(unitCode, 0, 0));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, 0, 0));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            unitDetailJo = JSON.parseObject(genUnitDetailPara(unitCode));

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, 2, 2, "\"DEFAULT\",\"DEFAULT\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            2、货架事件通知
            customerMessage(unitCode, typeDrop, posi, 100, 100);

//            货架单元详情，为了获取latticeId
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 4);

//            3、单元格物品绑定
            latticeBindingRes = latticeBinding(latticeId, goodsId, 1, 100, checkTypeStocktaking);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId, 1, 100, checkTypeStocktaking));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            4、配置
            createConfig(unitCode, 0, 0);

//            5、心跳
            heartBeat(unitCode, 0, 0);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            6、平面图货架列表
            realTimeListRes = realTimeList();
            message = JSON.parseObject(realTimeListRes).getString("message");
            checkCode(realTimeListRes, StatusCode.SUCCESS, message);

            JSONArray realtimeListAlarmStates = checkRealtimeListAlarmStates(realTimeListRes, unitCode);
            String[] expectState = {alarmStatesOutOfStock};
            checkAlarmState(realtimeListAlarmStates, 2, expectState, "realtimeListAlarmStates");

//            7、货架单元详情
            unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);

            latticeId = checkUnitDetail(unitDetailRes, 4);

            JSONArray unitDetailAlarmStates = checkUnitDetailAlarmStates(unitDetailRes, unitCode, latticeId);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(unitDetailAlarmStates, 2, expectState, "unitDetailAlarmStates");

//            8、单元格物品详情
            latticeDetailRes = latticeDetail(latticeId);

            latticeDetailJo = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes).getString("message");
            checkCode(latticeDetailRes, StatusCode.SUCCESS, message);

            JSONArray latticeDetailAlarmStates = checkLatticeDetailAlarmStates(latticeDetailRes);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(latticeDetailAlarmStates, 2, expectState, "latticeDetailAlarmStates");

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessageDropJo + "\n\n" + latticeBindingJo +
                    "\n\n" + createConfigJo + "\n\n" + heartBeatJo + "\n\n" + realTimeListJo + "\n\n" + unitDetailJo
                    + "\n\n" + latticeDetailJo);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            createConfigResJo = JSONObject.parseObject(createConfigRes);
            realTimeListResJo = JSONObject.parseObject(realTimeListRes);
            unitDetailResJo = JSONObject.parseObject(unitDetailRes);
            latticeDetailResJo = JSONObject.parseObject(latticeDetailRes);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeBindingResJo + "\n\n" + createConfigResJo + "\n\n" + realTimeListResJo
                    + "\n\n" + unitDetailResJo + "\n\n" + latticeDetailResJo);
            qaDbUtil.saveToCaseTable(aCase);

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
        String caseName = ciCaseName;
        String caseDesc = "给一个单元格放置3个物品，然后拿走直到拿空";
        logger.info(caseDesc + "--------------------");

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
        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String message = "";

        try {

            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 2, 2, "\"DEFAULT\",\"DEFAULT\""));
            customerMessageDropJo3 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 300, 300));
            customerMessagePickJo1 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -100, 200));
            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, 0, 0));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            customerMessagePickJo2 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -200, 0));
            unitDetailJo0 = JSON.parseObject(genUnitDetailPara(unitCode));

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, 2, 2, "\"DEFAULT\",\"DEFAULT\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            2、货架事件通知(drop 3)
            customerMessage(unitCode, typeDrop, posi, 300, 300);

//            货架单元详情，为了获取latticeId
            response = unitDetail(unitCode);
            message = JSON.parseObject(response).getString("message");
            checkCode(response, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(response, 4);

//            3、单元格物品绑定(绑定3个)
            latticeBindingRes = latticeBinding(latticeId, goodsId, 3, 300, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId, 3, 300, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            4、货架事件通知(pick one)
            customerMessage(unitCode, typePick, posi, -100, 200);

//            5、心跳
            heartBeat(unitCode, 0, 0);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            6、平面图货架列表(还剩2个)
            realTimeListRes2 = realTimeList();
            message = JSON.parseObject(realTimeListRes2).getString("message");
            checkCode(realTimeListRes2, StatusCode.SUCCESS, message);

            JSONArray realtimeListAlarmStates = checkRealtimeListAlarmStates(realTimeListRes2, unitCode);
            String[] expectState = {alarmStatesOutOfStock};
            checkAlarmState(realtimeListAlarmStates, 2, expectState, "realtimeListAlarmStates-还剩2个商品");


//            7、通知(pick 2，此时拿空了)
            customerMessage(unitCode, typePick, posi, -200, 0);

//            8、心跳
            heartBeat(unitCode, 0, 0);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            9、平面图货架列表(空了)
            realTimeListRes0 = realTimeList();
            message = JSON.parseObject(realTimeListRes0).getString("message");
            checkCode(realTimeListRes0, StatusCode.SUCCESS, message);

            realtimeListAlarmStates = checkRealtimeListAlarmStates(realTimeListRes0, unitCode);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(realtimeListAlarmStates, 2, expectState, "realtimeListAlarmStates-还剩0个商品");

//            10、货架单元详情
            unitDetailRes0 = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes0).getString("message");
            checkCode(unitDetailRes0, StatusCode.SUCCESS, message);

            JSONArray unitDetailAlarmStates = checkUnitDetailAlarmStates(unitDetailRes0, unitCode, latticeId);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(unitDetailAlarmStates, 2, expectState, "unitDetailAlarmStates-还剩0个商品");


            latticeId = checkUnitDetail(unitDetailRes0, 4);

//            11、单元格物品详情
            latticeDetailRes0 = latticeDetail(latticeId);

            latticeDetailJo0 = JSON.parseObject(genLatticeDetailPara(latticeId));

            message = JSON.parseObject(latticeDetailRes0).getString("message");
            checkCode(latticeDetailRes0, StatusCode.SUCCESS, message);

            JSONArray latticeDetailAlarmStates = checkLatticeDetailAlarmStates(latticeDetailRes0);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(latticeDetailAlarmStates, 2, expectState, "latticeDetailAlarmStates-还剩0个商品");


        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessageDropJo3 + "\n\n" + latticeBindingJo +
                    "\n\n" + customerMessagePickJo1 + heartBeatJo + "\n\n" + realTimeListJo +
                    "\n\n" + customerMessagePickJo2 + "\n\n" + unitDetailJo0 + "\n\n" + latticeDetailJo0);

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            realTimeListResJo2 = JSONObject.parseObject(realTimeListRes2);
            realTimeListResJo0 = JSONObject.parseObject(realTimeListRes0);
            unitDetailResJo0 = JSONObject.parseObject(unitDetailRes0);
            latticeDetailResJo0 = JSONObject.parseObject(latticeDetailRes0);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeBindingResJo + "\n\n" + realTimeListResJo2 + "\n\n" + realTimeListResJo0
                    + "\n\n" + unitDetailResJo0 + "\n\n" + latticeDetailResJo0);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //----(12)-----在有三个正确商品时，(1)放置一个错误商品，(2)然后拿走一个正确商品，观察状态；
    //                                (3)再拿走一个正确商品，观察状态；(4)再拿走一个正确商品观察状态
    // 1、创建单元--2、通知(drop 3)-3、绑定(3个)-4、通知(pick one)-
    // 5、心跳-6、列表(3对1错)-7、通知(pick 1正确的)-8、心跳-9、列表(2对1错)-10、通知(pick 1正确的)-----------------------------
    //11、心跳-12、列表(1对1错)-13、列表(pick 1错的)-14、心跳-15、列表(1正确的)
    @Test(dataProvider = "CHECK_TYPE")
    private void TestdropWrongGoods(String checkType) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "用错误商品干扰，观察告警状态";
        logger.info(caseDesc + "--------------------");

        String createUnitRes = null;
        String latticeBindingRes = null;
        String realTimeListRes31 = null;
        String realTimeListRes21 = null;
        String realTimeListRes11 = null;
        String realTimeListRes10 = null;

        JSONObject createUnitResJo;
        JSONObject latticeBindingResJo;
        JSONObject realTimeListResJo31;
        JSONObject realTimeListResJo21;
        JSONObject realTimeListResJo11;
        JSONObject realTimeListResJo10;

        JSONObject createUnitJo = null;
        JSONObject customerMessageDropJo30 = null;
        JSONObject latticeBindingJo = null;
        JSONObject customerMessageDropWrongJo31 = null;
        JSONObject heartBeatJo = null;
        JSONObject realTimeListJo = null;
        JSONObject customerMessagePickJo21 = null;
        JSONObject customerMessagePickJo11 = null;
        JSONObject customerMessagePickJo10 = null;

        Case aCase = new Case();
        failReason = "";

        String unitCode = "testPickUntilEmpty";
        String posi = "0,0";               //"position":"row,col"

        long goodsId = 139;  //3+2饼干

        String message = "";

        try {
            
            aCase.setApplicationId(APP_ID);
            aCase.setConfigId(CONFIG_ID);
            aCase.setCaseName(caseName);
            aCase.setCaseDescription(caseDesc);
            aCase.setCiCmd(CI_CMD + ciCaseName);
            aCase.setQaOwner("廖祥茹");
            aCase.setExpect("code==1000");

            //组织入参
            createUnitJo = JSON.parseObject(genCreateUnitPara(unitCode, 2, 2, "\"DEFAULT\",\"DEFAULT\""));
            customerMessageDropJo30 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 300, 300));
            customerMessageDropWrongJo31 = JSON.parseObject(genCustomerMessagePara(unitCode, typeDrop, posi, 50, 350));

            heartBeatJo = JSON.parseObject(genHeartBeatPara(unitCode, 0, 0));
            realTimeListJo = JSON.parseObject(genRealTimeListPara());
            customerMessagePickJo21 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -100, 250));
            customerMessagePickJo11 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -100, 150));
            customerMessagePickJo10 = JSON.parseObject(genCustomerMessagePara(unitCode, typePick, posi, -50, 100));

//            删除
            delete(shelvesCode, unitCode);

//            1、创建单元
            createUnitRes = createUnit(unitCode, 2, 2, "\"DEFAULT\",\"DEFAULT\"");
            message = JSON.parseObject(createUnitRes).getString("message");
            checkCode(createUnitRes, StatusCode.SUCCESS, message);

//            2、货架事件通知(drop 3)
            customerMessage(unitCode, typeDrop, posi, 300, 300);

//            货架单元详情，为了获取latticeId
            String unitDetailRes = unitDetail(unitCode);
            message = JSON.parseObject(unitDetailRes).getString("message");
            checkCode(unitDetailRes, StatusCode.SUCCESS, message);
            int latticeId = checkUnitDetail(unitDetailRes, 4);

//            3、单元格物品绑定(绑定3个)
            latticeBindingRes = latticeBinding(latticeId, goodsId, 3, 300, checkType);

            latticeBindingJo = JSON.parseObject(genLatticeBindingPara(latticeId, goodsId, 3, 300, checkType));

            message = JSON.parseObject(latticeBindingRes).getString("message");
            checkCode(latticeBindingRes, StatusCode.SUCCESS, message);

//            4、货架事件通知(drop one different good)
            customerMessage(unitCode, typeDrop, posi, 50, 350);

//            5、心跳
            heartBeat(unitCode, 0, 0);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            6、平面图货架列表(此时有 3个正确 1个错误  期待理货)
            realTimeListRes31 = realTimeList();
            message = JSON.parseObject(realTimeListRes31).getString("message");
            checkCode(realTimeListRes31, StatusCode.SUCCESS, message);
            JSONArray realtimeListAlarmStates = checkRealtimeListAlarmStates(realTimeListRes31, unitCode);

            String[] expectState = {alarmStatesWrongPlace};
            checkAlarmState(realtimeListAlarmStates, 1, expectState, "realtimeListAlarmStates");

//            7、通知(pick 一个正确商品)
            customerMessage(unitCode, typePick, posi, -100, 250);

//            8、心跳
            heartBeat(unitCode, 0, 0);//因为我latticeId取的第一个单元格，所以这里取0行0列

//            9、平面图货架列表(还剩2个正确 1个错误 期待理货)
            realTimeListRes21 = realTimeList();
            message = JSON.parseObject(realTimeListRes21).getString("message");
            checkCode(realTimeListRes21, StatusCode.SUCCESS, message);

            realtimeListAlarmStates = checkUnitDetailAlarmStates(realTimeListRes21, unitCode, latticeId);
            expectState = new String[]{alarmStatesWrongPlace};
            checkAlarmState(realtimeListAlarmStates, 1, expectState, "realtimeListAlarmStates");

//            10、货架事件通知(pick 一个正确商品)
            customerMessage(unitCode, typePick, posi, -100, 150);

//            11、心跳
            heartBeat(unitCode, 0, 0);

//            12、平面图货架列表(此时还剩1个正确 1个错误 期待理货)
            realTimeListRes11 = realTimeList();
            message = JSON.parseObject(realTimeListRes11).getString("message");
            checkCode(realTimeListRes11, StatusCode.SUCCESS, message);

            realtimeListAlarmStates = checkUnitDetailAlarmStates(realTimeListRes11, unitCode, latticeId);
            expectState = new String[]{alarmStatesWrongPlace};
            checkAlarmState(realtimeListAlarmStates, 1, expectState, "realtimeListAlarmStates");

//            13、货架事件通知(pick 一个错误商品)
            customerMessage(unitCode, typePick, posi, -50, 100);

//            14、心跳
            heartBeat(unitCode, 0, 0);

//            15、平面图货架列表(此时还剩一个正确商品 期待缺货)
            realTimeListRes10 = realTimeList();
            message = JSON.parseObject(realTimeListRes10).getString("message");
            checkCode(realTimeListRes10, StatusCode.SUCCESS, message);

            realtimeListAlarmStates = checkUnitDetailAlarmStates(realTimeListRes10, unitCode, latticeId);
            expectState = new String[]{alarmStatesOutOfStock};
            checkAlarmState(realtimeListAlarmStates, 1, expectState, "realtimeListAlarmStates");

        } catch (Exception e) {
            e.printStackTrace();
            failReason = e.toString();
            aCase.setFailReason(failReason + "\n" + e.toString());
            Assert.fail(message);
            throw e;
        } finally {

            //将入参入库
            aCase.setRequestData(createUnitJo + "\n\n" + customerMessageDropJo30 + "\n\n" + latticeBindingJo +
                    "\n\n" + customerMessageDropWrongJo31 + heartBeatJo + "\n\n" + realTimeListJo +
                    "\n\n" + customerMessagePickJo21 + "\n\n" + customerMessagePickJo11 + "\n\n" + customerMessagePickJo10
            );

            //response save to DB.
            createUnitResJo = JSONObject.parseObject(createUnitRes);
            latticeBindingResJo = JSONObject.parseObject(latticeBindingRes);
            realTimeListResJo31 = JSONObject.parseObject(realTimeListRes31);
            realTimeListResJo21 = JSONObject.parseObject(realTimeListRes21);
            realTimeListResJo11 = JSONObject.parseObject(realTimeListRes11);
            realTimeListResJo10 = JSONObject.parseObject(realTimeListRes10);

            aCase.setResponse(createUnitResJo + "\n\n" + latticeBindingResJo + "\n\n" + realTimeListResJo31 + "\n\n" + realTimeListResJo21
                    + "\n\n" + realTimeListResJo11 + "\n\n" + realTimeListResJo10);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    private JSONArray checkRealtimeListAlarmStates(String response, String testUnit) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray floors = responseJo.getJSONObject("data").getJSONArray("floors");
        JSONArray unitList = floors.getJSONObject(0).getJSONArray("unit_list");
        JSONArray alarmStates = null;
        boolean existed = false;

        for (int i = 0; i < unitList.size(); i++) {
            JSONObject singleUnit = unitList.getJSONObject(i);
            String unitCodeRes = singleUnit.getString("unit_code");
            if (testUnit.equals(unitCodeRes)) {
                existed = true;
                alarmStates = singleUnit.getJSONArray("alarm_states");
                break;
            }
        }

        if (!existed) {
            Assert.fail("checkAlarmStates failed,unit:" + testUnit + " is not existed!");
        }
        return alarmStates;
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
                Assert.assertEquals(alarmStates, null, message);
                String stocktakingState = singleUnit.getString("stocktaking_state");
                message = "盘货完成，“平面图货架列表”的stocktaking_state没有置为FINISHED！";
                Assert.assertEquals(stocktakingState, "FINISHED", message);
            }
        }

        if (!existed) {
            Assert.fail("checkAlarmStates failed,unit:" + testUnit + " is not existed!");
        }
    }

    private JSONArray checkUnitDetailAlarmStates(String response, String unitCode, int litticeId) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray latticeList = responseJo.getJSONObject("data").getJSONArray("lattice_list");

        JSONArray alarmStates = null;
        boolean existed = false;

        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLittice = latticeList.getJSONObject(i);
            int litticeIdRes = singleLittice.getInteger("lattice_id");
            String unitCodeRes = singleLittice.getString("unit_code");
            if (unitCode.equals(unitCodeRes) && litticeId == litticeIdRes) {
                existed = true;
                alarmStates = singleLittice.getJSONArray("alarm_states");
            }
        }

        if (!existed) {
            Assert.fail("checkAlarmStates failed,unit:" + unitCode + ",lattice_id: " + litticeId + " is not existed!");
        }
        return alarmStates;
    }

    private void checkUnitDetailStocktakingStates(String response, String unitCode, int latticeId, long goodsStock) {
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray latticeList = responseJo.getJSONObject("data").getJSONArray("lattice_list");
        boolean existed = false;

        for (int i = 0; i < latticeList.size(); i++) {
            JSONObject singleLattice = latticeList.getJSONObject(i);
            int latticeIdRes = singleLattice.getInteger("lattice_id");
            String unitCodeRes = singleLattice.getString("unit_code");
            if (unitCode.equals(unitCodeRes) && latticeId == latticeIdRes) {
                existed = true;
                JSONArray alarmStates = singleLattice.getJSONArray("alarm_states");
                String message = "盘货完成，“货架单元详情”的alarm_states没有置空！";
                Assert.assertEquals(alarmStates, null, message);

                String stocktakingState = singleLattice.getString("stocktaking_state");
                message = "盘货完成，“货架单元详情”的stocktaking_state没有置为 FINISHED！";
                Assert.assertEquals(stocktakingState, "FINISHED", message);

                long goodsStockRes = singleLattice.getLong("goods_stock");
                message = "“货架单元详情”中的goods_stock与盘货设置的goods_stock不符！";
                Assert.assertEquals(goodsStockRes, goodsStock, message);
            }
        }

        if (!existed) {
            Assert.fail("checkAlarmStates failed,unit:" + unitCode + ",lattice_id: " + latticeId + " is not existed!");
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
        Assert.assertEquals(alarmStates, null, message);

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

    private void checkCode(String response, int expect, String message) throws Exception {
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

    private ApiResponse sendRequestWithAuth(String router, String[] resource, String json) throws Exception {
        try {
            Credential credential = new Credential("77327ffc83b27f6d", "7624d1e6e190fbc381d0e9e18f03ab81");
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

    @DataProvider(name = "LATTICE_CHECK&UNIT_STOCKING_WITH_SCAN")
    private static Object[][] LatticeCheckAndUnitStockingWithScan() {
        //checkType, changeP, totalP, changeD, totalD, expectBinding, expectUnitStocktaking, expectCode
        return new Object[][]{
                new Object[]{"TALLY", -100, 0, 100, 100, true, true, StatusCode.SUCCESS},
                new Object[]{"TALLY", -100, 0, 100, 100, false, true, StatusCode.SUCCESS},
                new Object[]{"TALLY", -100, 0, 50, 120, true, false, StatusCode.INTERNAL_SERVER_ERROR},
                new Object[]{"STOCKTAKING", -100, 0, 100, 100, true, true, StatusCode.SUCCESS},
                new Object[]{"STOCKTAKING", -100, 0, 100, 100, false, true, StatusCode.SUCCESS},
                new Object[]{"STOCKTAKING", -100, 0, 50, 120, true, true, StatusCode.INTERNAL_SERVER_ERROR}
        };
    }

    @DataProvider(name = "LATTICE_CHECK&UNIT_STOCKING_WITHOUT_SCAN")
    private static Object[][] LatticeCheckAndUnitStockingWithoutScan() {
        //checkType, changeD, totalD, expectCode
        return new Object[][]{
                new Object[]{"TALLY", 100, 100, StatusCode.INTERNAL_SERVER_ERROR},
                new Object[]{"TALLY", 100, 100, StatusCode.INTERNAL_SERVER_ERROR},
                new Object[]{"TALLY", 50, 120, StatusCode.INTERNAL_SERVER_ERROR},
                new Object[]{"STOCKTAKING", 100, 100, StatusCode.SUCCESS},
                new Object[]{"STOCKTAKING", 100, 100, StatusCode.SUCCESS},
                new Object[]{"STOCKTAKING", 50, 120, StatusCode.INTERNAL_SERVER_ERROR}
        };
    }
}
