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
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "FengKong-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "风控 日常");        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer = product.getReferer();
        commonConfig.shopId = shopId;
        commonConfig.roleId = product.getRoleId();
        beforeClassInit(commonConfig);
        logger.debug("FK: " + cu);
        System.out.println(product.getIp());
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
            out.write("测试规则：" + casename + "\n");
            out.write("订单id：" + tranid + "\n");
            out.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 生成交易订单--触发无人风控(保证摄像头面前没有人)
     **/
    @Test(description = "无人风控，交易类型线上，不触发风控")
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
            //生成交易订单
            String post = cu.getCreateOrder3(or);
            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "创单失败" + post);
            recode(or.transId, "线上交易无人风控");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单online--不触发无人风控");
        }
    }

    @Test(description = "同步员工离职在职信息", enabled = true)
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
                // java代码示例
                // java代码示例

                // 1. 将以下参数(uid、app_id、ak、router、timestamp、nonce)的值之间使用顿号(.)拼接成一个整体字符串
                String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
                // 2. 使用HmacSHA256加密算法, 使用平台分配的sk作为算法的密钥. 对上面拼接后的字符串进行加密操作,得到byte数组
                Mac sha256Hmac = Mac.getInstance(ALGORITHM);
                SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                sha256Hmac.init(encodeSecretKey);
                byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
                // 3. 对2.中的加密结果,再进行一次base64操作, 得到一个字符串
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
//                        "            \"name\":\"吕雪晴\",\n" +
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
//                //夏明凤的脸  是否在职 0否  1是
//                JSONObject jsonObject=staffObject("uid_edfe23f0","夏明凤","uid_edfe23f0",0,face);
//
                String face = file.getImgStr("com/haisheng/framework/testng/bigScreen/itemPorsche/casedaily/xmf/guoliya.jpg");
                //夏明凤的脸  是否在职 0否  1是
                JSONObject jsonObject = staffObject("uid_6b41fd04", "郭丽雅", "uid_6b41fd04", 0, face);  //uid_6b41fd04	郭丽雅	uid_6b41fd04

                logger.info("request:" + jsonObject.toJSONString());
                System.out.println("over");

                HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
                String post = HttpClientUtil.post(config);
                JSONObject pp = JSONObject.parseObject(post);
                System.out.println(post);
                Preconditions.checkArgument(pp.getString("code").equals("1000"), "人脸同步失败" + pp.getString("message"));
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发送交易事件");
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
     * 生成交易订单--触发无人风控(保证摄像头面前没有人)
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
            //生成交易订单
            String post = cu.getCreateOrder3(or);
            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "创单失败" + post);
//            recode(or.transId,"员工");
            recode(or.transId, "白1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发无人风控(保证摄像头面前没有人)");
        }
    }


    /**
     * 生成交易订单--触发一人多单风控
     * 一人多单 ，userId/openid相同，多个transId交易订单触发
     **/
    @Test(enabled = true)
    public void getTriggerMoreOrderRisk() {
        try {
            //创建一人多单风控规则(1个人1天内最多2单)
//            Long ruleId=cu.getCashierOrderRuleAdd("1","2").getJSONObject("data").getLong("id");
            String time = dt.getHistoryDate(0);
            String time1 = dt.getHHmm(0);

            //交易ID(不同的3个)
            String transId1 = "QATest1_" + CommonUtil.getRandom(3) + time + time1;
            String transId2 = "QATest2_" + CommonUtil.getRandom(3) + time + time1;
            String transId3 = "QATest3_" + CommonUtil.getRandom(3) + time + time1;
            //客户ID
            String userId = pp.userId;
            //支付ID
            String openId = pp.openId;
            //车架号
            String carVehicleNumber = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
//            String carVehicleNumber2="AAAAAAAAAA22"+CommonUtil.getRandom(5);
//            String carVehicleNumber3="AAAAAAAAAA22"+CommonUtil.getRandom(5);

            //生成交易订单
            String post = cu.getCreateOrderOnline(shopId, transId1, userId, openId, carVehicleNumber);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId, openId, carVehicleNumber);
            String post3 = cu.getCreateOrderOnline(shopId, transId3, userId, openId, carVehicleNumber);

            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "创单失败" + post);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "创单失败" + post2);
            Preconditions.checkArgument(JSONObject.parseObject(post3).getString("code").equals("1000"), "创单失败" + post3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人多单风控");
        }
    }

    /**
     * 生成交易订单--触发一人多车风控
     * 一人多车 user_id  相同; 多个car_vehicle_number车架号 触发；
     **/
    @Test(enabled = true, description = "一人多车userId 触发")
    public void getTriggerMoreCarRisk() {
        try {
            //创建一人多车风控规则(1个人最多2个车)
//            Long ruleId=cu.getCashierCarRuleAdd("2").getJSONObject("data").getLong("id");

            //交易ID
            String transId = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID
            String userId = "tesdr" + CommonUtil.getRandom(3);
            //支付ID
            String openId = pp.openId;
            //车架号1
            String carVehicleNumber1 = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
            //车架号2
            String carVehicleNumber2 = "AAAAAAAAAA22" + CommonUtil.getRandom(5);
            //车架号3
            String carVehicleNumber3 = "AAAAAAAAAA22" + CommonUtil.getRandom(5);

            //生成交易订单
            String post = cu.getCreateOrderOnline(shopId, transId, userId, openId, carVehicleNumber1);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId, openId, carVehicleNumber2);
            String post3 = cu.getCreateOrderOnline(shopId, transId3, userId, openId, carVehicleNumber3);

            Preconditions.checkArgument(JSONObject.parseObject(post).getString("code").equals("1000"), "创单失败" + post);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "创单失败" + post2);
            Preconditions.checkArgument(JSONObject.parseObject(post3).getString("code").equals("1000"), "创单失败" + post3);
            recode(transId, "一人多车");
            recode(transId2, "一人多车");
            recode(transId3, "一人多车");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人多车风控");
        }
    }

    /**
     * 生成交易订单--触发一车多人风控
     * 一车多人，多个openid/userId,一个car_vehicle_number 触发;QATest_42021-04-1418:17  QATest_16762021-04-1418:17
     **/
    @Test(description = "一车多人成功")   //userID  openid 都不一样 触发成功
    public void getTriggerMorePersonRisk() {
        try {
            //创建一人多单风控规则(1个人最多2个车)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //指定门店
            //交易ID
            String transId = "QATest_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);
            //客户ID2
            String userId2 = "tester2" + CommonUtil.getRandom(6);
            //客户ID3
            String userId3 = "tester2" + CommonUtil.getRandom(6);

            //支付ID TODO：openId 是否也要不同  是
            String openId = "deal" + CommonUtil.getRandom(8);
            String openId2 = "deal" + CommonUtil.getRandom(8);
            String openId3 = "deal" + CommonUtil.getRandom(8);

            //车架号
            String carVehicleNumber = "AAAAAAAAAA15" + CommonUtil.getRandom(5);
//            String carVehicleNumber="AAAAAAAAAA158271127";

            //生成交易订单
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId2, openId2, carVehicleNumber);
//            String post3=cu.getCreateOrder(shopId,transId3,userId3,openId,carVehicleNumber);
            Preconditions.checkArgument(JSONObject.parseObject(post1).getString("code").equals("1000"), "创单失败" + post1);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "创单失败" + post2);
            recode(transId, "一车多人 无脸");
            recode(transId2, "一车多人 无脸");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人车多人风控");
        }
    }

    /**
     * 生成交易订单--触发一车多人风控
     * 一车多人，多个openid/userId,一个car_vehicle_number 触发;QATest_42021-04-1418:17  QATest_16762021-04-1418:17
     **/
    @Test(description = "一车多人成功,脸", enabled = false)   //userID  openid 都不一样 触发成功
    public void getTriggerMorePersonRiskface() {
        try {

            String transId = "QATest_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID1
//            String userId1="tester1120008";
            String userId1 = "tester1" + CommonUtil.getRandom(3);
            //客户ID2
//            String userId2="tester344552";
            //客户ID3
            //支付ID TODO：openId 是否也要不同  是
            String openId = "dea123233009" + CommonUtil.getRandom(3);

            //车架号
//            String carVehicleNumber="AAAAAAAAAA15"+CommonUtil.getRandom(5);
            String carVehicleNumber = "AAAAAAAAAA8902805";

            //生成交易订单
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
//            sleep(10);
//            String post2=cu.getCreateOrderOnline(shopId,transId2,userId2,openId2,carVehicleNumber);
//            String post3=cu.getCreateOrderOnline(shopId,transId3,userId3,openId,carVehicleNumber);
            Preconditions.checkArgument(JSONObject.parseObject(post1).getString("code").equals("1000"), "创单失败" + post1);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人车多人风控");
        }
    }

    @Test(description = "一车多人失败，userId 相同")   //userID  openid 都不一样 触发成功
    public void differentuserID() {
        try {
            //创建一人多单风控规则(1个人最多2个车)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //指定门店
            //交易ID
            String transId = "QATestcar_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATestcar_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATestcar_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);

            //支付ID TODO：openId 是否也要不同  是
            String openId = "deal" + CommonUtil.getRandom(8);
            String openId2 = "deal" + CommonUtil.getRandom(8);

            //车架号
            String carVehicleNumber = "AAAAAAAAAA15" + CommonUtil.getRandom(5);

            //生成交易订单
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
            saveData("生成交易订单--触发一人车多人风控");
        }
    }

    @Test(description = "一车多人失败，openID 相同")   //userID  openid 都不一样 触发成功
    public void diffopenid() {
        try {
            //创建一人多单风控规则(1个人最多2个车)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //指定门店
            //交易ID
            String transId = "QATestcar_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATestcar_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATestcar_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);
            //客户ID2
            String userId2 = "tester2" + CommonUtil.getRandom(6);
            //支付ID TODO：openId 是否也要不同  是
            String openId = "deal" + CommonUtil.getRandom(8);

            //车架号
            String carVehicleNumber = "AAAAAAAAAA15" + CommonUtil.getRandom(5);

            //生成交易订单
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
            saveData("生成交易订单--触发一人车多人风控");
        }
    }

    @Test(description = "一车多人订单追加，与原openID 相同，车架号相同", enabled = false)   //userID  openid 都不一样 触发成功
    public void yicheduorenAppend() {
        try {
            //指定门店
            //交易ID
            String transId = "QATest_" + CommonUtil.getRandom(1) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID1
            String userId1 = "tester1752930";
            //客户ID2
            String userId2 = "tester2" + CommonUtil.getRandom(6);
            //TODO: 原openid
            String openId = "deal" + CommonUtil.getRandom(6);

            //车架号
            String carVehicleNumber = "AAAAAAAAAA1523516";

            //生成交易订单
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人车多人风控");
        }
    }


    @Test(enabled = true, description = "一人多车 openid触发")
    public void getTriggerMorePersonRisk22() {
        try {

            String transId = "QATest_" + random.nextInt(10) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId2 = "QATest_" + CommonUtil.getRandom(4) + dt.getHistoryDate(0) + dt.getHHmm(0);
            String transId3 = "QATest_" + CommonUtil.getRandom(3) + dt.getHistoryDate(0) + dt.getHHmm(0);
            //客户ID1
            String userId1 = "tester1" + CommonUtil.getRandom(6);
            String userId2 = "tester1" + CommonUtil.getRandom(6);
            String userId3 = "tester1" + CommonUtil.getRandom(6);
            //支付ID
            String openId = pp.openId;
            //车架号
            String carVehicleNumber = "AAAAAAAAAA12" + CommonUtil.getRandom(5);
            String carVehicleNumber2 = "AAAAAAAAAA12" + CommonUtil.getRandom(5);
            String carVehicleNumber3 = "AAAAAAAAAA12" + CommonUtil.getRandom(5);

            //生成交易订单
            String post1 = cu.getCreateOrderOnline(shopId, transId, userId1, openId, carVehicleNumber);
            String post2 = cu.getCreateOrderOnline(shopId, transId2, userId2, openId, carVehicleNumber2);
            String post3 = cu.getCreateOrderOnline(shopId, transId3, userId3, openId, carVehicleNumber3);
            Preconditions.checkArgument(JSONObject.parseObject(post1).getString("code").equals("1000"), "创单失败" + post1);
            Preconditions.checkArgument(JSONObject.parseObject(post2).getString("code").equals("1000"), "创单失败" + post2);
            Preconditions.checkArgument(JSONObject.parseObject(post3).getString("code").equals("1000"), "创单失败" + post3);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("一人多车 openid触发");
        }
    }


    @Test()
    public void zhandle() {
        try {
            for (int j = 1; j < 3; j++) {
                JSONArray list = cashier_riskPage(Long.parseLong(shopId), "PENDING", j, 10).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    Long id = list.getJSONObject(i).getLong("id");
                    IScene handle = RiskEventHandleScene.builder().result(1).remarks("自动正常处理").id(id).build();
                    visitor.invokeApi(handle);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("处理风控");
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
