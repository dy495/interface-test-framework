package com.haisheng.framework.testng.bigScreen.fengkongdaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.routerEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventHandleScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.CommonUsedUtil;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.PublicParam;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.RiskControlUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.QADbProxy;
import com.haisheng.framework.util.QADbUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class RiskControlRulesOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.FK_ONLINE;

    private static final routerEnum router = routerEnum.SHOPONLINE;

    public VisitorProxy visitor = new VisitorProxy(product);
    PublicParam pp = new PublicParam();
    CommonUsedUtil cu = new CommonUsedUtil(visitor, router);
    RiskControlUtil md = RiskControlUtil.getInstance(product);
    public String shopId = router.getShopid();
    FileUtil file = new FileUtil();
    public String face = file.getImgStr(pp.filePath2);
    Random random = new Random();

    private QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //??????checklist???????????????
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "?????????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "FengKong-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "?????? ??????");        //??????????????????
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //??????shopId
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("FK: " + cu);
        pclogin("salesdemo@winsense.ai", "c216d5045fbeb18bcca830c235e7f3c8");
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    public void pclogin(String name, String password) {
        String url = "/risk-control/login-pc";
        JSONObject data = new JSONObject();
        data.put("username", name);
        data.put("password", password);
        data.put("type", 0);
        httpPost(product.getIp(), url, data);
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    public void recode(String tranid, String casename) throws IOException {
        Writer out = null;
        try {
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/fengkongdaily/things.txt".replace("/", File.separator);
//            path.replace("/", File.separator);
            out = new BufferedWriter(new FileWriter(path, true));
            out.write("???????????????" + casename + "\n");
            out.write("??????id???" + tranid + "\n");
            out.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * ??????????????????--??????????????????(??????????????????????????????)
     **/
    @Test(description = "???????????????????????????????????????????????????")
    public void transTypeOnline() {
        try {
            orderParm or = new orderParm();
            or.transId = pp.transId;
            or.shopId = router.getShopid();
            or.userId = pp.userId;
            or.openId = pp.openId;
            or.type = "ONLINE";
            or.carVehicleNumber = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
            or.business_type = null;
            System.out.println(or.carVehicleNumber);
            //??????????????????
            String post = cu.getCreateOrder3(or);
            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "????????????" + post);
            recode(or.transId, "????????????????????????");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????online--?????????????????????");
        }
    }

    @Test(description = "??????????????????????????????", enabled = true)
    public void updateStaffStatus() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (int i = 0; i < 1; i++) {
                final String NUMBER = ".";
                final String ALGORITHM = "HmacSHA256";
                HttpClient client = null;
                try {
                    client = HCB.custom()
                            .pool(50, 10)
                            .retry(3).build();
                } catch (HttpProcessException e) {
                    e.printStackTrace();
                }
                String timestamp = "" + System.currentTimeMillis();

                String uid = router.getUid();
                String appId = router.getAppid();
                String ak = router.getAk();
                String sk = router.getSk();
                String requestUrl = router.getRequestUrl();

//                String uid = "uid_0ba743d8";//13260: uid_0ba743d8,14630:uid_580f244a
//                String appId = "672170545f50";//13260: 672170545f50,14630:c30dcafc59c8
//                String ak = "691ff41137d954f3";//13260: 691ff41137d954f3,14630:0d17651c55595b9b
//                String sk = "d76f2d8a7846382f633c1334139767fe";//13260:d76f2d8a7846382f633c1334139767fe,14630:0ebe6128aedb44e0a7bd3f7a5378a7fc

                String router = "/business/patrol/STAFF_FACE_REGISTER/v1.0";
                String nonce = UUID.randomUUID().toString();
                // java????????????
                // java????????????

                // 1. ???????????????(uid???app_id???ak???router???timestamp???nonce)????????????????????????(.)??????????????????????????????
                String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
                // 2. ??????HmacSHA256????????????, ?????????????????????sk?????????????????????. ????????????????????????????????????????????????,??????byte??????
                Mac sha256Hmac = Mac.getInstance(ALGORITHM);
                SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                sha256Hmac.init(encodeSecretKey);
                byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
                // 3. ???2.??????????????????,???????????????base64??????, ?????????????????????
                String auth = Base64.getEncoder().encodeToString(hash);

                Header[] headers = HttpHeader.custom()
                        .other("Accept", "application/json")
                        .other("Content-Type", "application/json;charset=utf-8")
                        .other("timestamp", timestamp)
                        .other("nonce", nonce)
                        .other("ExpiredTime", "50 * 1000")
                        .other("Authorization", auth)
                        .build();
//                String str = "{\n" +
//                        "  \"uid\": \"uid_ef6d2de5\",\n" +
//                        "  \"app_id\": \"49998b971ea0\",\n" +
//                        "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919897\",\n" +
//                        "  \"version\": \"v1.0\",\n" +
//                        "  \"router\": \"/business/patrol/STAFF_FACE_REGISTER/v1.0\",\n" +
//                        "  \"data\": {\n" +
//                        "    \"biz_data\":  {\n" +
//                        "            \"name\":\"?????????\",\n" +
//                        "            \"id\":\"uid_91cc8031\",\n" +
//                        "            \"account_uid\":\"uid_91cc8031\",\n" +
//                        "        \"face_base64\": " + "\"" + face + "\"" + " ,\n" +
//                        "            \"status\":1\n" +
//
//                        "    }\n" +
//                        "  }\n" +
//                        "}";

//                JSONObject jsonObject = JSON.parseObject(str);
//                String face=file.getImgStr( "src/main/java/com/haisheng/framework/testng/bigScreen/itemPorsche/casedaily/xmf/xia.png");
//                //???????????????  ???????????? 0???  1???
//                JSONObject jsonObject=staffObject("uid_edfe23f0","?????????","uid_edfe23f0",0,face);
//
                String face = file.getImgStr("com/haisheng/framework/testng/bigScreen/itemPorsche/casedaily/xmf/guoliya.jpg");
                //???????????????  ???????????? 0???  1???
                JSONObject jsonObject = staffObject("uid_6b41fd04", "?????????", "uid_6b41fd04", 0, face);  //uid_6b41fd04	?????????	uid_6b41fd04

                logger.info("request:" + jsonObject.toJSONString());
                System.out.println("over");

                HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
                String post = HttpClientUtil.post(config);
                JSONObject pp = JSONObject.parseObject(post);
                System.out.println(post);
                Preconditions.checkArgument(pp.getString("code").equals("1000"), "??????????????????" + pp.getString("message"));
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????");
        }

    }

    public JSONObject staffObject(String id, String name, String account_uid, Integer status, String face) {

        Random random = new Random();

        JSONObject str = new JSONObject();
        str.put("uid", router.getUid());
        str.put("app_id", router.getAppid());
        str.put("request_id", "5d45a085-8774-4jd0-943e-ded373ca6a91998" + random.nextInt(10));
        str.put("version", "v1.0");
        str.put("router", "/business/patrol/STAFF_FACE_REGISTER/v1.0");

        JSONObject data = new JSONObject();
//        data.put("resource",new JSONArray());

        JSONObject body = new JSONObject();
        body.put("id", id);
        body.put("name", name);
        body.put("account_uid", account_uid);
        body.put("status", status);
        body.put("face_base64", face);
        data.put("biz_data", body);

        str.put("data", data);

        return str;
    }

    /**
     * ??????????????????--??????????????????(??????????????????????????????)
     **/
    @Test(enabled = true)
    public void getTriggerUnmannedRisk() {
        try {
            orderParm or = new orderParm();
            or.transId = pp.transId;
            or.shopId = router.getShopid();
            or.userId = pp.userId;
            or.openId = pp.openId;
            or.carVehicleNumber = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
//            or.carVehicleNumber="AAAAAAAAAA2256599";

//            or.business_type="\"GOODS_PAY\"";
            or.business_type = null;
            System.out.println(or.carVehicleNumber);
            //??????????????????
            String post = cu.getCreateOrder3(or);
            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "????????????" + post);
//            recode(or.transId,"??????");
            recode(or.transId, "???1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--??????????????????(??????????????????????????????)");
        }
    }


    /**
     * ??????????????????--????????????????????????
     * ???????????? ???userId/openid???????????????transId??????????????????
     **/
    @Test(enabled = true)
    public void getTriggerMoreOrderRisk() {
        try {
            //??????????????????????????????(1??????1????????????2???)
//            Long ruleId=cu.getCashierOrderRuleAdd("1","2").getJSONObject("data").getLong("id");
            String time = dt.getHistoryDate(0);
            String time1 = dt.getHHmm(0);

            //??????ID(?????????3???)
            String transId1 = "QATest1_" + CommonUtil.getRandom(3) + time + time1;
            String transId2 = "QATest2_" + CommonUtil.getRandom(3) + time + time1;
            String transId3 = "QATest3_" + CommonUtil.getRandom(3) + time + time1;
            //??????ID
            String userId = pp.userId;
            //??????ID
            String openId = pp.openId;
            //?????????
            String carVehicleNumber = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
//            String carVehicleNumber2="AAAAAAAAAA22"+CommonUtil.getRandom(5);
//            String carVehicleNumber3="AAAAAAAAAA22"+CommonUtil.getRandom(5);

            //??????????????????
            String post = cu.getCreateOrderOnline(shopId, transId1, userId, openId, carVehicleNumber);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId, openId, carVehicleNumber);
            String post3 = cu.getCreateOrderOnline(shopId, transId3, userId, openId, carVehicleNumber);

            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "????????????" + post);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "????????????" + post2);
            Preconditions.checkArgument(JSONObject.parseObject(post3).getString("code").equals("1000"), "????????????" + post3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--????????????????????????");
        }
    }

    /**
     * ??????????????????--????????????????????????
     * ???????????? user_id  ??????; ??????car_vehicle_number????????? ?????????
     **/
    @Test(enabled = true, description = "????????????userId ??????")
    public void getTriggerMoreCarRisk() {
        try {
            //??????????????????????????????(1????????????2??????)
//            Long ruleId=cu.getCashierCarRuleAdd("2").getJSONObject("data").getLong("id");

            //??????ID
            String transId = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID
            String userId = "tesdr" + CommonUtil.getRandom(3);
            //??????ID
            String openId = pp.openId;
            //?????????1
            String carVehicleNumber1 = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
            //?????????2
            String carVehicleNumber2 = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
            //?????????3
            String carVehicleNumber3 = "AAAAAAAAAA22" + CommonUtil.getRandom(5);

            //??????????????????
            String post = cu.getCreateOrderOnline(shopId, transId, userId, openId, carVehicleNumber1);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId, openId, carVehicleNumber2);
            String post3 = cu.getCreateOrderOnline(shopId, transId3, userId, openId, carVehicleNumber3);

            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "????????????" + post);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "????????????" + post2);
            Preconditions.checkArgument(JSONObject.parseObject(post3).getString("code").equals("1000"), "????????????" + post3);
            recode(transId, "????????????");
            recode(transId2, "????????????");
            recode(transId3, "????????????");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--????????????????????????");
        }
    }

    /**
     * ??????????????????--????????????????????????
     * ?????????????????????openid/userId,??????car_vehicle_number ??????;QATest_42021-04-1418:17  QATest_16762021-04-1418:17
     **/
    @Test(description = "??????????????????")   //userID  openid ???????????? ????????????
    public void getTriggerMorePersonRisk() {
        try {
            //??????????????????????????????(1????????????2??????)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //????????????
            //??????ID
            String transId = "QATest_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);
            //??????ID2
            String userId2 = "tester2" + CommonUtil.getRandom(6);
            //??????ID3
            String userId3 = "tester2" + CommonUtil.getRandom(6);

            //??????ID TODO???openId ??????????????????  ???
            String openId = "deal" + CommonUtil.getRandom(8);
            String openId2 = "deal" + CommonUtil.getRandom(8);
            String openId3 = "deal" + CommonUtil.getRandom(8);

            //?????????
            String carVehicleNumber = "AAAAAAAAAA15" + CommonUtil.getRandom(5);
//            String carVehicleNumber="AAAAAAAAAA158271127";

            //??????????????????
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId2, openId2, carVehicleNumber);
//            String post3=cu.getCreateOrder(shopId,transId3,userId3,openId,carVehicleNumber);
            Preconditions.checkArgument(JSONObject.parseObject(post1).getString("code").equals("1000"), "????????????" + post1);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "????????????" + post2);
            recode(transId, "???????????? ??????");
            recode(transId2, "???????????? ??????");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--???????????????????????????");
        }
    }

    /**
     * ??????????????????--????????????????????????
     * ?????????????????????openid/userId,??????car_vehicle_number ??????;QATest_42021-04-1418:17  QATest_16762021-04-1418:17
     **/
    @Test(description = "??????????????????,???", enabled = false)   //userID  openid ???????????? ????????????
    public void getTriggerMorePersonRiskface() {
        try {

            String transId = "QATest_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID1
//            String userId1="tester1120008";
            String userId1 = "tester1" + CommonUtil.getRandom(3);
            //??????ID2
//            String userId2="tester344552";
            //??????ID3
            //??????ID TODO???openId ??????????????????  ???
            String openId = "dea123233009" + CommonUtil.getRandom(3);

            //?????????
//            String carVehicleNumber="AAAAAAAAAA15"+CommonUtil.getRandom(5);
            String carVehicleNumber = "AAAAAAAAAA8902805";

            //??????????????????
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
//            sleep(10);
//            String post2=cu.getCreateOrderOnline(shopId,transId2,userId2,openId2,carVehicleNumber);
//            String post3=cu.getCreateOrderOnline(shopId,transId3,userId3,openId,carVehicleNumber);
            Preconditions.checkArgument(JSONObject.parseObject(post1).getString("code").equals("1000"), "????????????" + post1);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--???????????????????????????");
        }
    }

    @Test(description = "?????????????????????userId ??????")   //userID  openid ???????????? ????????????
    public void differentuserID() {
        try {
            //??????????????????????????????(1????????????2??????)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //????????????
            //??????ID
            String transId = "QATestcar_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATestcar_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATestcar_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);

            //??????ID TODO???openId ??????????????????  ???
            String openId = "deal" + CommonUtil.getRandom(8);
            String openId2 = "deal" + CommonUtil.getRandom(8);

            //?????????
            String carVehicleNumber = "AAAAAAAAAA15" + CommonUtil.getRandom(5);

            //??????????????????
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
            sleep(10);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId1, openId2, carVehicleNumber);
//            String post3=cu.getCreateOrderOnline(shopId,transId3,userId3,openId,carVehicleNumber);
//            System.out.println("-----------"+post1);
            System.out.println("-----------" + post2);
//            System.out.println("-----------"+post3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--???????????????????????????");
        }
    }

    @Test(description = "?????????????????????openID ??????")   //userID  openid ???????????? ????????????
    public void diffopenid() {
        try {
            //??????????????????????????????(1????????????2??????)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //????????????
            //??????ID
            String transId = "QATestcar_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATestcar_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATestcar_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);
            //??????ID2
            String userId2 = "tester2" + CommonUtil.getRandom(6);
            //??????ID TODO???openId ??????????????????  ???
            String openId = "deal" + CommonUtil.getRandom(8);

            //?????????
            String carVehicleNumber = "AAAAAAAAAA15" + CommonUtil.getRandom(5);

            //??????????????????
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
            sleep(10);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId1, openId, carVehicleNumber);
//            String post3=cu.getCreateOrderOnline(shopId,transId3,userId3,openId,carVehicleNumber);
//            System.out.println("-----------"+post1);
            System.out.println("-----------" + post2);
//            System.out.println("-----------"+post3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--???????????????????????????");
        }
    }

    @Test(description = "?????????????????????????????????openID ????????????????????????", enabled = false)   //userID  openid ???????????? ????????????
    public void yicheduorenAppend() {
        try {
            //????????????
            //??????ID
            String transId = "QATest_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID1
            String userId1 = "tester1752930";
            //??????ID2
            String userId2 = "tester2" + CommonUtil.getRandom(6);
            //TODO: ???openid
            String openId = "deal" + CommonUtil.getRandom(6);

            //?????????
            String carVehicleNumber = "AAAAAAAAAA1523516";

            //??????????????????
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????--???????????????????????????");
        }
    }


    @Test(enabled = true, description = "???????????? openid??????")
    public void getTriggerMorePersonRisk22() {
        try {

            String transId = "QATest_" + random.nextInt(10) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //??????ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);
            String userId2 = "tester1" + CommonUtil.getRandom(6);
            String userId3 = "tester1" + CommonUtil.getRandom(6);
            //??????ID
            String openId = pp.openId;
            //?????????
            String carVehicleNumber = "AAAAAAAAAA12" + CommonUtil.getRandom(5);
            String carVehicleNumber2 = "AAAAAAAAAA12" + CommonUtil.getRandom(5);
            String carVehicleNumber3 = "AAAAAAAAAA12" + CommonUtil.getRandom(5);

            //??????????????????
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId2, openId, carVehicleNumber2);
            String post3 = cu.getCreateOrderOnline(shopId, transId3, userId3, openId, carVehicleNumber3);
            Preconditions.checkArgument(JSONObject.parseObject(post1).getString("code").equals("1000"), "????????????" + post1);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "????????????" + post2);
            Preconditions.checkArgument(JSONObject.parseObject(post3).getString("code").equals("1000"), "????????????" + post3);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????? openid??????");
        }
    }


    @Test()
    public void zhandle() {
        try {
            for (int j = 1; j < 3; j++) {
                JSONArray list = cashier_riskPage(Long.parseLong(shopId), "PENDING", j, 10).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    Long id = list.getJSONObject(i).getLong("id");
                    RiskEventHandleScene.builder().result(1).remarks("??????????????????").id(id).build().visitor(visitor).execute();
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????");
        }
    }

    public JSONObject cashier_riskPage(long shop_id, String current_state, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/cashier/risk-event/page";
        String json = "{";

        if (current_state != "") {
            json = json + "\"current_state\" :\"" + current_state + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, EnumTestProduct.FK_ONLINE.getIp());

        return JSON.parseObject(res).getJSONObject("data");
    }


}
