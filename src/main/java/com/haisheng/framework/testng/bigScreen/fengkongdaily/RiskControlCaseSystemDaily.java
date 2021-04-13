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
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RiskBusinessTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RiskPersonnelTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.DetailScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.*;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.downloadcenter.DownloadPageScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.AddScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.CommonUsedUtil;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.PublicParam;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.RiskControlUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

public class RiskControlCaseSystemDaily extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.FK_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
//    StoreFuncPackage mds = StoreFuncPackage.getInstance();
    PublicParam pp=new PublicParam();
    CommonUsedUtil cu=new CommonUsedUtil(visitor);
    RiskControlUtil md=new RiskControlUtil();
    public String shopId="43072";


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "FengKong-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "风控 日常");        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer = product.getReferer();
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId = product.getRoleId();
        beforeClassInit(commonConfig);
        logger.debug("FK: " + cu);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
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
        md.pcLogin(pp.userName,pp.password);
    }



    @Test
    public void justTry(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //一人多单/连续天数（一人1天最多3单）
            Long id1=cu.getCashierOrderRuleAdd("1","3").getJSONObject("data").getLong("id");
            System.out.println("--------------"+id1);

            //无人风控
            Long id2=cu.getCashierUnmannedRuleAdd().getJSONObject("data").getLong("id");
            System.out.println("--------------"+id2);

            //员工支付订单监控(一人1天最多1单)
            Long id3=cu.getCashierEmployeeRuleAdd("1","1").getJSONObject("data").getLong("id");
            System.out.println("--------------"+id3);

            //一人多车
            Long id4=cu.getCashierCarRuleAdd("10").getJSONObject("data").getLong("id");
            System.out.println("--------------"+id4);

            //一车多人
            Long id5=cu.getCashierMemberRuleAdd("10").getJSONObject("data").getLong("id");
            System.out.println("--------------"+id5);

            //黑名单风控
            Long id6=cu.getRuleAdd(RuleEnum.BLACK_LIST.getType());
            System.out.println("--------------"+id6);

            //重点观察人员风控
            Long id7=cu.getRuleAdd(RuleEnum.FOCUS_LIST.getType());
            System.out.println("--------------"+id7);


            //收银风控告警规则
            String message1=cu.getAlarmRuleAdd(true,600000L,RuleEnum.CASHIER.getType(),pp.AlarmNameCashier);
            System.out.println("--------------"+message1);

            //黑名单风控告警规则
            String message2=cu.getAlarmRuleAdd(false,null,RuleEnum.BLACK_LIST.getType(),pp.AlarmNameBlack);
            System.out.println("--------------"+message2);

            //重点观察人员风控告警规则
            String message3=cu.getAlarmRuleAdd(true,null,RuleEnum.FOCUS_LIST.getType(),pp.AlarmNameObserve);
            System.out.println("--------------"+message3);

            //生成订单，里面信息可能不正确,想要自己定义，适用下边的
            String port=cu.getOrder();
            System.out.println("--------------"+port);


        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("测试呀");
        }
    }
    //一人多车 user_id  相同; 多个car_vehicle_number车架号 触发；
    //一车多人，多个openid,一个car_vehicle_number 触发;
    //一人多单 ，userId相同，openid相同;  ---userId 相同，或者transId; 单一客户数量监控
    @Test
    public void getA() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (int i=0;i<1;i++) {
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
                String uid = "uid_ef6d2de5";
                String appId = "49998b971ea0";
                String ak = "3fdce1db0e843ee0";
                String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
                String nonce = UUID.randomUUID().toString();
                String sk = "5036807b1c25b9312116fd4b22c351ac";
                // java代码示例
                // java代码示例
                String requestUrl = "http://dev.api.winsenseos.com/retail/api/data/biz";

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
                String time= dt.getHistoryDate(0);
                String time1= dt.getHHmm(0);
                String userId = "tester"+ CommonUtil.getRandom(6);
                String transId = "QAtest_" + CommonUtil.getRandom(3)+time+time1;
                String transTime = "" + System.currentTimeMillis();
                String str = "{\n" +
                        "  \"uid\": \"uid_ef6d2de5\",\n" +
                        "  \"app_id\": \"49998b971ea0\",\n" +
                        "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                        "  \"version\": \"v1.0\",\n" +
                        "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                        "  \"data\": {\n" +
                        "    \"biz_data\":  {\n" +
                        "        \"shop_id\": \"43072\",\n" +
                        "        \"trans_id\": " + "\"" + transId + "\"" + " ,\n" +
                        "        \"trans_time\": " + "\"" + transTime + "\"" + " ,\n" +
                        "        \"trans_type\": [\n" +
                        "            \"W\"\n" +
                        "        ],\n" +
                        "        \"user_id\":  " + "\""+userId+"\"" + " ,\n" +
                        "        \"total_price\": 1800,\n" +
                        "        \"real_price\": 1500,\n" +
//                        "        \"openid\": \"823849023iidijdiwiodede3330\",\n" +
                        "        \"shopType\": \"SHOP_TYPE\",\n" +
                        "        \"orderNumber\": \"13444894484\",\n" +
                        "        \"memberName\":\"自动化在回归\",\n" +
                        "        \"receipt_type\":\"小票类型\",\n" +
                        "        \"posId\": \"pos-1234586789\",\n" +
                        "        \"commodityList\": [\n" +
                        "            {\n" +
                        "                \"commodityId\": \"iPhone12A42234\",\n" +
                        "                \"commodity_name\":\"苹果12s\",\n" +
                        "                \"unit_price\": 200,\n" +
                        "                \"num\": 4\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"commodityId\": \"banan3424724E\",\n" +
                        "                \"commodity_name\":\"香蕉20根啊\",\n" +
                        "                \"unit_price\": 2,\n" +
                        "                \"num\": 4\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"commodityId\": \"Apple3424323234\",\n" +
                        "                \"commodity_name\":\"苹果20ge\",\n" +
                        "                \"unit_price\": 3,\n" +
                        "                \"num\": 4\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

                JSONObject jsonObject = JSON.parseObject(str);
                HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);

                String post = HttpClientUtil.post(config);
                // checkArgument(, "添加事项不成功");
                System.out.println(post);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发送交易事件");
        }

    }

    @Test(description = "同步员工离职在职信息")
    public void get1A() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (int i=0;i<1;i++) {
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
                String uid = "uid_ef6d2de5";
                String appId = "49998b971ea0";
                String ak = "3fdce1db0e843ee0";
                String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
                String nonce = UUID.randomUUID().toString();
                String sk = "5036807b1c25b9312116fd4b22c351ac";
                // java代码示例
                // java代码示例
                String requestUrl = "http://dev.api.winsenseos.com/retail/api/data/biz";

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
                String time= dt.getHistoryDate(0);
                String time1= dt.getHHmm(0);
                String userId = "tester"+ CommonUtil.getRandom(6);
                String transId = "QAtest_" + CommonUtil.getRandom(3)+time+time1;
                System.out.println("transId:"+transId);
                String transTime = "" + System.currentTimeMillis();
                String str = "{\n" +
                        "  \"uid\": \"uid_ef6d2de5\",\n" +
                        "  \"app_id\": \"49998b971ea0\",\n" +
                        "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                        "  \"version\": \"v1.0\",\n" +
                        "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                        "  \"data\": {\n" +
                        "    \"biz_data\":  {\n" +
                        "        \"shop_id\": \"43072\",\n" +
                        "        \"trans_id\": " + "\"" + transId + "\"" + " ,\n" +
                        "        \"trans_time\": " + "\"" + transTime + "\"" + " ,\n" +
                        "        \"trans_type\": [\n" +
                        "            \"W\"\n" +
                        "        ],\n" +
                        "        \"user_id\":  " + "\""+userId+"\"" + " ,\n" +
                        "        \"total_price\": 1800,\n" +
                        "        \"real_price\": 1500,\n" +
//                        "        \"openid\": \"823849023iidijdiwiodede3330\",\n" +
                        "        \"shopType\": \"SHOP_TYPE\",\n" +
                        "        \"orderNumber\": \"13444894484\",\n" +
                        "        \"memberName\":\"自动化在回归\",\n" +
                        "        \"receipt_type\":\"小票类型\",\n" +
                        "        \"posId\": \"pos-1234586789\",\n" +
                        "        \"commodityList\": [\n" +
                        "            {\n" +
                        "                \"commodityId\": \"iPhone12A42234\",\n" +
                        "                \"commodity_name\":\"苹果12s\",\n" +
                        "                \"unit_price\": 200,\n" +
                        "                \"num\": 4\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"commodityId\": \"banan3424724E\",\n" +
                        "                \"commodity_name\":\"香蕉20根啊\",\n" +
                        "                \"unit_price\": 2,\n" +
                        "                \"num\": 4\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"commodityId\": \"Apple3424323234\",\n" +
                        "                \"commodity_name\":\"苹果20ge\",\n" +
                        "                \"unit_price\": 3,\n" +
                        "                \"num\": 4\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"trans_business_params\":{\n" +
                        "               \"car_plate\":\"京A11111\",\n" +
                        "               \"car_vehicle_number\":\"ASDASDSDFS1829387\",\n" +
                        "                \"business_type\":\"FIRST_INSPECTION\",\n" +
                        "                \"business_order_id\":\"27389182\"\n" +
                        "    }"+
                        "    }\n" +
                        "  }\n" +
                        "}";

//                JSONObject jsonObject = JSON.parseObject(str);
                JSONObject jsonObject=staffObject("uid_27f11a9d","店员1","uid_27f11a9d",1);
                logger.info("request:"+jsonObject.toJSONString());
                logger.info("requestUrl:"+requestUrl);

                HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);



                String post = HttpClientUtil.post(config);
                // checkArgument(, "添加事项不成功");
                System.out.println(post);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发送交易事件");
        }

    }
    public JSONObject staffObject(String id,String name,String account_uid, Integer status){
        JSONObject body=new JSONObject();
        body.put("id",id);
        body.put("name",name);
        body.put("account_uid",account_uid);
        body.put("status",status);
        return body;
    }


    /**
     *生成交易订单--触发无人风控(保证摄像头面前没有人)
     **/
    @Test(enabled =true )
    public void getTriggerUnmannedRisk(){
        try{
            //创建无人风控
//            Long ruleId=cu.getCashierUnmannedRuleAdd().getJSONObject("data").getLong("id");

            //交易ID
            String transId=pp.transId;
            //客户ID
            String userId=pp.userId;
            //支付ID
            String openId=pp.openId;
            //车架号
            String carVehicleNumber="AAAAAAAAAA2223345";

            //生成交易订单
            String post=cu.getCreateOrder(shopId,transId,userId,openId,carVehicleNumber);
            System.out.println("---------"+post);
//            Preconditions.checkArgument(post.equals("")&&ruleId!=null,"生成订单失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发无人风控(保证摄像头面前没有人)");
        }
    }

    /**
     *生成交易订单--触发一人多单风控
     * 一人多单 ，userId/openid相同，多个transId交易订单触发
     **/
    @Test(enabled = true)
    public void getTriggerMoreOrderRisk(){
        try{
            //创建一人多单风控规则(1个人1天内最多2单)
//            Long ruleId=cu.getCashierOrderRuleAdd("1","2").getJSONObject("data").getLong("id");
            String time = dt.getHistoryDate(0);
            String time1 = dt.getHHmm(0);

            //交易ID(不同的3个)
            String transId1= "QATest1_" + CommonUtil.getRandom(3) + time + time1;
            String transId2= "QATest2_" + CommonUtil.getRandom(3) + time + time1;
            String transId3= "QATest3_" + CommonUtil.getRandom(3) + time + time1;
            //客户ID
            String userId=pp.userId;
            //支付ID
            String openId=pp.openId;
            //车架号
            String carVehicleNumber="AAAAAAAAAA9998887";

            //生成交易订单
            String post=cu.getCreateOrder(shopId,transId1,userId,openId,carVehicleNumber);
            String post2=cu.getCreateOrder(shopId,transId2,userId,openId,carVehicleNumber);
            String post3=cu.getCreateOrder(shopId,transId3,userId,openId,carVehicleNumber);
            System.out.println("---------"+post);
            System.out.println("---------"+post2);
            System.out.println("---------"+post3);
//            Preconditions.checkArgument(post1.equals("")&&post2.equals("")&&post3.equals("")&&ruleId!=null,"生成订单失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人多单风控");
        }
    }

    /**
     *生成交易订单--触发一人多车风控
     * 一人多车 user_id  相同; 多个car_vehicle_number车架号 触发；
     **/
    @Test(enabled = true)
    public void getTriggerMoreCarRisk(){
        try{
            //创建一人多车风控规则(1个人最多2个车)
//            Long ruleId=cu.getCashierCarRuleAdd("2").getJSONObject("data").getLong("id");

            //交易ID
            String transId=pp.transId;
            //客户ID
            String userId=pp.userId;
            //支付ID
            String openId=pp.openId;
            //车架号1
            String carVehicleNumber1="AAAAAAAAAA1234322";
            //车架号2
            String carVehicleNumber2="AAAAAAAAAA1234323";
            //车架号3
            String carVehicleNumber3="AAAAAAAAAA1234324";

            //生成交易订单
            String post1=cu.getCreateOrder(shopId,transId,userId,openId,carVehicleNumber1);
            String post2=cu.getCreateOrder(shopId,transId,userId,openId,carVehicleNumber2);
            String post3=cu.getCreateOrder(shopId,transId,userId,openId,carVehicleNumber3);
            System.out.println("-----------"+post1);
            System.out.println("-----------"+post2);
            System.out.println("-----------"+post3);
//            Preconditions.checkArgument(post1.equals("")&&post2.equals("")&&post3.equals("")&&ruleId!=null,"生成订单失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人多车风控");
        }
    }

    /**
     *生成交易订单--触发一车多人风控
     * 一车多人，多个openid/userId,一个car_vehicle_number 触发;
     **/
    @Test(enabled = true)
    public void getTriggerMorePersonRisk(){
        try{
            //创建一人多单风控规则(1个人最多2个车)
//            Long ruleId=cu.getCashierMemberRuleAdd("2").getJSONObject("data").getLong("id");
            //指定门店
            //交易ID
            String transId=pp.transId;
            //客户ID1
            String userId1="tester1" + CommonUtil.getRandom(6);
            //客户ID2
            String userId2="tester2" + CommonUtil.getRandom(6);
            //客户ID3
            String userId3="tester3" + CommonUtil.getRandom(6);
            //支付ID
            String openId=pp.openId;
            //车架号
            String carVehicleNumber="AAAAAAAAAA1234888";

            //生成交易订单
            String post1=cu.getCreateOrder(shopId,transId,userId1,openId,carVehicleNumber);
            String post2=cu.getCreateOrder(shopId,transId,userId2,openId,carVehicleNumber);
            String post3=cu.getCreateOrder(shopId,transId,userId3,openId,carVehicleNumber);
            System.out.println("-----------"+post1);
            System.out.println("-----------"+post2);
            System.out.println("-----------"+post3);
//            Preconditions.checkArgument(post1.equals("")&&post2.equals("")&&ruleId!=null,"生成订单失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发一人车多人风控");
        }
    }



    /**
     *生成交易订单--触发员工下单风控(下单时保证摄像头下只有员工，没有顾客)
     **/
    @Test(enabled = true)
    public void getTriggerEmployeeOrderRisk() {
        try{
            //创建员工支付风控规则(一个员工一天最多2单)
//            Long ruleId=cu.getCashierEmployeeRuleAdd("1","2").getJSONObject("data").getLong("id");
            //指定门店
            //交易ID
            String transId=pp.transId;
            //实际金额
            String realPrice="2200";
            //客户ID
            String userId=pp.userId;
            //支付ID
            String openId=pp.openId;
            //车架号
            String carVehicleNumber="AAAAAAAAAA1237878";

            //生成交易订单
            String post1=cu.getCreateOrder(shopId,transId,userId,openId,carVehicleNumber);
            System.out.println("-----------"+post1);

//            Preconditions.checkArgument(post1.equals("")&&post2.equals("")&&post3.equals("")&&ruleId!=null,"生成订单失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("生成交易订单--触发员工下单风控");
        }
    }

    /**
     * 收银风控列表项校验
     */
    @Test(description = "收银风控列表项校验")
     public void authCashierPageSystem1(){
        try{
            //收银风页面的接口
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(PageScene.builder().page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //校验列表的每一项
                    String shopName=list.getJSONObject(i).getString("shop_name");
                    String managerName=list.getJSONObject(i).getString("manager_name");
                    String managerPhone=list.getJSONObject(i).getString("manager_phone");
                    String riskTotal=list.getJSONObject(i).getString("risk_total");
                    String abnormalTotal=list.getJSONObject(i).getString("abnormal_total");
                    String normalTotal=list.getJSONObject(i).getString("normal_total");
                    String pendingRisksTotal=list.getJSONObject(i).getString("pending_risks_total");
                    Preconditions.checkArgument(shopName != null&&managerName!=null&&managerPhone!=null&&riskTotal!=null&&abnormalTotal!=null&&normalTotal!=null&&pendingRisksTotal!=null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控列表项校验");
        }
     }

    /**
     *收银追溯-收银追溯列表校验
     */
    @Test(description = "收银追溯-收银追溯列表校验")
    public void authCashierPageSystem2(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银追溯页面
            IScene scene1= TraceBackScene.builder().shopId(shopId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages=response1.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(TraceBackScene.builder().shopId(shopId).page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String orderId=list.getJSONObject(i).getString("order_id");
                    String orderTime=list.getJSONObject(i).getString("order_time");
                    Preconditions.checkArgument(orderId != null&&orderTime!=null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银追溯-收银追溯列表校验");
        }
    }

    /**
     *收银追溯-门店信息内容校验
     */
    @Test(description = "收银追溯-门店信息内容校验")
    public void authCashierPageSystem3(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银追溯-门店信息页面  todo--怎么创建门店，门店信息怎么查询呢？

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银追溯-门店信息内容校验");
        }
    }

    /**
     *收银追溯-小票详情内容校验
     */
    @Test(description = "收银追溯-小票详情内容校验")
    public void authCashierPageSystem4(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银追溯页面
            IScene scene1= TraceBackScene.builder().shopId(shopId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages=response1.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(TraceBackScene.builder().shopId(shopId).page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String orderId=list.getJSONObject(i).getString("order_id");
                    //小票详情页
                    IScene scene2= OrderDetailScene.builder().shopId(shopId).orderId(orderId).build();
                    JSONObject response2=visitor.invokeApi(scene2);
                    String shopName=response2.getString("shop_name");
                    String memberName=response2.getString("member_name");
                    String memberPhone=response2.getString("member_phone");
                    String carPlate=response2.getString("car_plate");
                    String carVehicleNumber=response2.getString("car_vehicle_number");
                    String orderNumber=response2.getString("order_number");
                    String totalPrice=response2.getString("total_price");
                    String realPrice=response2.getString("real_price");
                    String businessNumber=response2.getString("business_number");
                    String posId=response2.getString("pos_id");
                    String transTypeName=response2.getString("trans_type_name");
                    String orderTime=response2.getString("order_time");
                    String type=response2.getString("type");
                    String orderTypeName=response2.getString("order_type_name");
                    Preconditions.checkArgument(shopName!=null&&memberName!=null&&memberPhone!=null&&carPlate!=null&&carVehicleNumber!=null&&orderNumber!=null,"小票详情中存在字段为空的情况，在第"+i+"行");
                    Preconditions.checkArgument(totalPrice!=null&&realPrice!=null&&businessNumber!=null&&posId!=null&&transTypeName!=null&&orderTime!=null&&type!=null&&orderTypeName!=null,"小票详情中存在字段为空的情况，在第"+i+"行");

                }
            }

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银追溯-小票详情内容校验");
        }
    }

    /**
     *收银追溯-批量下载功能
     */
    @Test(description = "收银追溯-批量下载功能")
    public void authCashierPageSystem5(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银追溯-批量下载
            IScene scene1= TraceBackBatchVideoScene.builder().shopId(shopId).startDate(cu.getStartDate()).endDate(cu.getEndDate()).build();
            String code=visitor.invokeApi(scene1,false).getString("code");
            Preconditions.checkArgument(code.equals("1000"),"批量下载失败");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银追溯-批量下载功能");
        }
    }

    /**
     *收银追溯-提示语校验
     */
    @Test(description = "收银追溯-提示语校验")
    public void authCashierPageSystem6(){
        try{
            IScene scene= traceBackTips.builder().build();
            String tip=visitor.invokeApi(scene).getString("tip");

            Preconditions.checkArgument(tip.equals("你的账号可查看最近30天追溯视频"),"收银追溯中的提示语不正确");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银追溯-提示语校验");
        }
    }

    /**
     * 收银风控事件-列表项校验
     */
    @Test(description = "收银风控事件-列表项校验")
    public void authCashierPageSystem7(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银风控事件页面
            IScene scene1=RiskEventPageScene.builder().shopId(shopId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages=response1.getInteger("pages")>10?10:response1.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(RiskEventPageScene.builder().shopId(shopId).page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String eventName=list.getJSONObject(i).getString("event_name");
                    String orderId=list.getJSONObject(i).getString("order_id");
                    String responseTime=list.getJSONObject(i).getString("response_time");
                    String memberName=list.getJSONObject(i).getString("member_name");
                    String remainTime=list.getJSONObject(i).getString("order_id");
                    String currentState=list.getJSONObject(i).getString("current_state");
                    String orderDate=list.getJSONObject(i).getString("order_date");
                    Preconditions.checkArgument(orderId != null&&eventName!=null&&responseTime!=null&&memberName!=null&&remainTime!=null&&currentState!=null&&orderDate!=null,"第"+i+"行的列表项存在为空的值");
                }
            }

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控事件-列表项校验");
        }
    }

    /**
     * 收银风控事件-事件概览内容校验
     */
    @Test(description = "收银风控事件-事件概览内容校验")
    public void authCashierPageSystem8(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银风控事件页面
            IScene scene1=RiskEventPageScene.builder().shopId(shopId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages=response1.getInteger("pages")>10?10:response1.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(RiskEventPageScene.builder().shopId(shopId).page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long id=list.getJSONObject(i).getLong("id");
                    IScene scene2=RiskEventOverviewScene.builder().id(id).build();
                    JSONObject response2=visitor.invokeApi(scene2);
                    String roleId=response2.getString("role_id");
                    String shopId1=response2.getString("shop_id");
                    String responseTime=response2.getString("response_time");
                    String managerName=response2.getString("manager_name");
                    String managerPhone=response2.getString("manager_phone");
                    String memberName=response2.getString("member_name");
                    String memberId=response2.getString("member_id");
                    String customerId=response2.getString("customer_id");
                    String openId=response2.getString("_id");
                    String transCustomerBodyUrl=response2.getString("trans_customer_body_url");
                    Preconditions.checkArgument(openId!=null&&roleId != null&&shopId1!=null&&responseTime!=null&&managerName!=null&&managerPhone!=null&&memberName!=null&&memberId!=null&&customerId!=null&&transCustomerBodyUrl!=null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控事件-事件概览内容校验");
        }
    }

    /**
     * 收银风控事件-涉及订单列表校验
     */
    @Test(description = "收银风控事件-涉及订单列表校验")
    public void authCashierPageSystem9(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银风控事件页面
            IScene scene1=RiskEventPageScene.builder().shopId(shopId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            Long id=response1.getJSONArray("list").getJSONObject(0).getLong("id");
            //收银风控事件中第一条的涉及订单
            IScene scene2=RiskEventOrdersInvolvedPageScene.builder().id(id).page(1).size(10).build();
            JSONObject response2=visitor.invokeApi(scene2);
            int pages=response1.getInteger("pages")>10?10:response1.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(RiskEventOrdersInvolvedPageScene.builder().id(id).page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String orderId=response2.getString("order_id");
                    String shopId1=response2.getString("shop_id");
                    String orderTime=response2.getString("order_time");
                    String orderAmount=response2.getString("order_amount");

                    Preconditions.checkArgument(orderId != null&&shopId1 != null&&orderTime != null&&orderAmount != null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控事件-涉及订单列表校验");
        }
    }

    /**
     * 收银风控事件-历史风控事件列表校验
     */
    @Test(description = "收银风控事件-历史风控事件列表校验")
    public void authCashierPageSystem10(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银风控事件页面
            IScene scene1=RiskEventPageScene.builder().shopId(shopId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            String memberId=response1.getJSONArray("list").getJSONObject(0).getString("member_id");
            //历史风控事件--即收银风控事件中按照门店和会员ID查询的结果
            IScene scene2=RiskEventPageScene.builder().shopId(shopId).memberId(memberId).page(1).size(10).build();
            JSONObject response2=visitor.invokeApi(scene2);
            JSONArray list1=response2.getJSONArray("list");
            int pages=response1.getInteger("pages")>10?10:response1.getInteger("pages");
            if(list1.size()>0){
                for(int page=1;page<=pages;page++){
                    JSONArray list=visitor.invokeApi(RiskEventPageScene.builder().shopId(shopId).memberId(memberId).page(page).size(10).build()).getJSONArray("list");
                    for(int i=0;i<list.size();i++){
                        String orderId=response2.getString("order_id");
                        String shopId1=response2.getString("shop_id");
                        String orderTime=response2.getString("order_time");
                        String handleResult=response2.getString("handle_result");

                        Preconditions.checkArgument(orderId != null&&shopId1 != null&&orderTime != null&&handleResult != null,"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控事件-历史风控事件列表校验");
        }
    }

    /**
     *风控规则列表项校验
     */
    @Test(description = "风控规则列表项校验")
    public void authCashierPageSystem11(){
        try{
            //风控规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String name=list.getJSONObject(i).getString("name");
                    String type=list.getJSONObject(i).getString("type");
                    String detail=list.getJSONObject(i).getString("detail");
                    String shopId=list.getJSONObject(i).getString("shop_id");
                    String updateTime=list.getJSONObject(i).getString("update_time");
                    String updateUser=list.getJSONObject(i).getString("update_user");
                    Preconditions.checkArgument(name != null&&type!=null&&detail != null&&shopId!=null&&updateTime != null&&updateUser!=null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控规则列表项校验");
        }
    }

    /**
     *风控规则-增加黑名单风控规则           ok
     */
    @Test(description = "风控规则-增加黑名单风控规则")
    public void authCashierPageSystem12(){
        try{
            //新建风控规则
            Long id=cu.getRuleAdd(RuleEnum.BLACK_LIST.getType());
            Preconditions.checkArgument(id>0,"创建黑名单风控失败");

            //删除风控规则
            String message=cu.ruleDelete(id);
            Preconditions.checkArgument(message.equals("success"),"删除黑名单风控失败");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控规则-增加黑名单风控规则");
        }
    }

    /**
     *风控规则-增加重点观察人员风控规则        ok
     */
    @Test(description = "风控规则-增加重点观察人员规则")
    public void authCashierPageSystem13(){
        try{

            //新建风控规则
            Long id=cu.getRuleAdd(RuleEnum.FOCUS_LIST.getType());
            Preconditions.checkArgument(id>0,"创建重点观察人员风控失败");

            //删除风控规则
            String message=cu.ruleDelete(id);
            Preconditions.checkArgument(message.equals("success"),"删除重点观察人员失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控规则-增加重点观察人员风控规则");
        }
    }

    /**
     *风控规则-增加收银风控规则
     */
    @Test(description = "风控规则-增加收银风控规则")
    public void authCashierPageSystem14(){
        try{
            //一人多单/连续天数（一人1天最多3单）
            Long id1=cu.getCashierOrderRuleAdd("1","3").getJSONObject("data").getLong("id");

            //无人风控
            Long id2=cu.getCashierUnmannedRuleAdd().getJSONObject("data").getLong("id");

            //员工支付订单监控(一人1天最多1单)
            Long id3=cu.getCashierEmployeeRuleAdd("1","1").getJSONObject("data").getLong("id");

            //一人多车
            Long id4=cu.getCashierCarRuleAdd("10").getJSONObject("data").getLong("id");

            //一车多人
            Long id5=cu.getCashierMemberRuleAdd("10").getJSONObject("data").getLong("id");

            Preconditions.checkArgument(id1>0&&id2>0&&id3>0&&id4>0&&id5>0,"创建风控失收银风控失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控规则-增加收银风控规则");
        }
    }

    /**
     *风控规则-规则开启和关闭，对规则进行删除
     */
    @Test(description = "风控规则-规则开启和关闭，对规则进行删除")
    public void authCashierPageSystem15(){
        try{
            //新建两个黑名单风控规则
            Long id1=cu.getRuleAdd(RuleEnum.BLACK_LIST.getType());
            Long id2=cu.getRuleAdd(RuleEnum.BLACK_LIST.getType());
            //开启活动1，关闭id2
            cu.ruleSwitch(id2,0);
            cu.ruleSwitch(id1,1);
            //对两个规则进行删除
            String message1=cu.ruleDelete(id1);
            String message2=cu.ruleDelete(id2);
            Preconditions.checkArgument(message1.equals(""),"删除开启中的规则失败");
            Preconditions.checkArgument(message2.equals("success"),"删除已关闭的规则失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控规则-规则开启和关闭，对规则进行删除");
        }
    }

    /**
     *风控告警列表项校验
     */
    @Test(description = "风控告警列表项校验")
    public void authCashierPageSystem16(){
        try{
            //风控规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarm.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String name=list.getJSONObject(i).getString("name");
                    String type=list.getJSONObject(i).getString("type");
                    String detail=list.getJSONObject(i).getString("detail");
                    String shopId=list.getJSONObject(i).getString("shop_id");
                    String firstAlarmTime=list.getJSONObject(i).getString("first_alarm_time");
                    String lastAlarmTime=list.getJSONObject(i).getString("last_alarm_time");
                    String acceptRole=list.getJSONObject(i).getString("accept_role");
                    Preconditions.checkArgument(name != null&&type!=null&&detail != null&&shopId!=null&&firstAlarmTime != null&&lastAlarmTime!=null&&acceptRole!=null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控告警规则列表项校验");
        }
    }

    /**
     *风控告警规则列表项校验
     */
    @Test(description = "风控告警规则列表项校验")
    public void authCashierPageSystem17(){
        try{
            //风控告警规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=visitor.invokeApi(com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(page).size(10).build()).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String name=list.getJSONObject(i).getString("name");
                    String type=list.getJSONObject(i).getString("type");
                    String typeName=list.getJSONObject(i).getString("type_name");
                    String silentTime=list.getJSONObject(i).getString("silent_time");
                    String acceptRoleListName=list.getJSONObject(i).getString("accept_role_list_name");
                    String startTime=list.getJSONObject(i).getString("start_time");
                    String endTime=list.getJSONObject(i).getString("end_time");
                    Preconditions.checkArgument(name != null&&type!=null&&typeName != null&&silentTime!=null&&acceptRoleListName != null&&startTime!=null&&endTime!=null,"第"+i+"行的列表项存在为空的值");
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控告警规则列表项校验");
        }
    }

    /**
     *新建风控告警规则
     */
    @Test(description = "新建风控告警规则")
    public void authCashierPageSystem18(){
        try{
            //收银风控告警规则
            String message1=cu.getAlarmRuleAdd(true,600000L,RuleEnum.CASHIER.getType(),pp.AlarmNameCashier);
            //黑名单风控告警规则
            String message2=cu.getAlarmRuleAdd(false,null,RuleEnum.BLACK_LIST.getType(),pp.AlarmNameBlack);
            //重点观察人员风控告警规则
            String message3=cu.getAlarmRuleAdd(true,null,RuleEnum.FOCUS_LIST.getType(),pp.AlarmNameObserve);

            Preconditions.checkArgument(message1.equals("success")&&message2.equals("success")&&message3.equals("success"),"新建风控告警规则失败，三个类型规则message分别为："+message1+"   "+message2+"    "+message3);
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控告警规则");
        }
    }

    /**
     *编辑风控告警规则
     */
    @Test(description = "编辑风控告警规则")
    public void authCashierPageSystem20(){
        try{//风控告警规则第一条的ID
            Long alarmId=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getLong("id");
            //接收人ID  todo
            List<Long> acceptRoleIdList=new ArrayList();
            //风控规则的ID
            List<Long> ruleIdList=new ArrayList();
            //风控规则中的对风控规则类型进行筛选，取前三个
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).type(RuleEnum.CASHIER.getType()).build();
            Long id=visitor.invokeApi(scene).getJSONArray("list").getJSONObject(0).getLong("id");
            ruleIdList.add(id);
            //关闭风控告警规则的第一条
            cu.getAlarmRuleSwitch(alarmId,0);
            //编辑风控告警规则
            String message=cu.getAlarmRuleEdit(alarmId,true,1800000L,RuleEnum.CASHIER.getType(),ruleIdList,acceptRoleIdList).invoke(visitor,false).getString("message");
            //开启风控告警规则的第一条
            cu.getAlarmRuleSwitch(alarmId,1);
            //查看风控告警规则的详情
            JSONObject response= DetailScene.builder().id(alarmId).build().invoke(visitor,true);
            String name=response.getString("name");
            String type=response.getString("type");
            String realTime=response.getString("real_time");
            Long silentTime=response.getLong("silent_time");
            Long ruleId=response.getJSONArray("rule_id_list").getJSONObject(0).getLong("id");
            Long RoleId=response.getJSONArray("accept_role_id_list").getJSONObject(0).getLong("id");
            Preconditions.checkArgument(message.equals("success")&&name.contains("已编辑风控告警规则")&&type.equals(RuleEnum.CASHIER.getName())&&realTime.equals("true")&&silentTime==1800000L&&ruleId.equals(ruleIdList.get(0))&&RoleId.equals(ruleIdList.get(0)),"编辑风控告警规则，编辑后的各项分别为："+name+"---"+type+"---"+realTime+"---"+silentTime+"---"+ruleId+"----"+RoleId);

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("编辑风控告警规则");
        }
    }

    /**
     *新建风控告警规则.并且删除
     */
    @Test(description = "新建风控告警规则.并且删除")
    public void authCashierPageSystem21(){
        try{
            //新建收银风控告警规则--重点观察人员
            String message1=cu.getAlarmRuleAdd(true,600000L,RuleEnum.FOCUS_LIST.getType(),pp.AlarmNameObserve);
            //删除新建风控告警规则
//            String message=cu.getAlarmRuleDel(alarmId);
//            Preconditions.checkArgument(alarmId>0&&message.equals("success"),"删除新建的风控告警规则失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控告警规则.并且删除");
        }
    }

    /**
     *特殊人员列表项校验--黑名单
     */
    @Test(description = "特殊人员列表项校验")
    public void authCashierPageSystem22(){
        try{
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).type(RiskPersonnelTypeEnum.BLACK.getType()).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(page).size(10).type(RiskPersonnelTypeEnum.BLACK.getType()).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String name=list.getJSONObject(i).getString("name");
                        String faceUrl=list.getJSONObject(i).getString("face_url");
                        String memberId=list.getJSONObject(i).getString("member_id");
                        String customerId=list.getJSONObject(i).getString("customer_id");
                        String addTime=list.getJSONObject(i).getString("add_time");
                        String addReason=list.getJSONObject(i).getString("add_reason");
                        String addUser=list.getJSONObject(i).getString("add_user");
                        Preconditions.checkArgument(name!=null&&faceUrl!=null&&memberId!=null&&customerId!=null&&addTime!=null&&addReason!=null&&addUser!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员列表项校验--黑名单");
        }
    }

    /**
     *特殊人员列表项校验--白名单
     */
    @Test(description = "特殊人员列表项校验")
    public void authCashierPageSystem23(){
        try{
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type(RiskPersonnelTypeEnum.WHITE.getType()).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(page).size(10).type(RiskPersonnelTypeEnum.WHITE.getType()).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String name=list.getJSONObject(i).getString("name");
                        String faceUrl=list.getJSONObject(i).getString("face_url");
                        String memberId=list.getJSONObject(i).getString("member_id");
                        String customerId=list.getJSONObject(i).getString("customer_id");
                        String addTime=list.getJSONObject(i).getString("add_time");
                        String addReason=list.getJSONObject(i).getString("add_reason");
                        String addUser=list.getJSONObject(i).getString("add_user");
                        Preconditions.checkArgument(name!=null&&faceUrl!=null&&memberId!=null&&customerId!=null&&addTime!=null&&addReason!=null&&addUser!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员列表项校验");
        }
    }

    /**
     *特殊人员列表项校验--重点观察人员
     */
    @Test(description = "特殊人员列表项校验")
    public void authCashierPageSystem24(){
        try{
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type(RiskPersonnelTypeEnum.FOCUS.getType()).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(page).size(10).type(RiskPersonnelTypeEnum.FOCUS.getType()).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String name=list.getJSONObject(i).getString("name");
                        String faceUrl=list.getJSONObject(i).getString("face_url");
                        String memberId=list.getJSONObject(i).getString("member_id");
                        String customerId=list.getJSONObject(i).getString("customer_id");
                        String addTime=list.getJSONObject(i).getString("add_time");
                        String addReason=list.getJSONObject(i).getString("add_reason");
                        String addUser=list.getJSONObject(i).getString("add_user");
                        Preconditions.checkArgument(name!=null&&faceUrl!=null&&memberId!=null&&customerId!=null&&addTime!=null&&addReason!=null&&addUser!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员列表项校验");
        }
    }

    /**
     *特殊人员筛选栏校验
     */
    @Test(description = "特殊人员筛选栏校验")
    public void authCashierPageSystem25(){
        try{
            //获取第一行的会员姓名
            String name=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("name");
            //会员姓名查询
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().name(name).page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().name(name).page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String name1=list.getJSONObject(i).getString("name");
                        Preconditions.checkArgument(name1.contains(name),"会员姓名筛选的内容为:"+name+"  列表项展示为："+name1);
                    }
                }
            }

            //获取第一行的会员ID
            String memberId=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("member_id");
            //会员姓名查询
            IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().memberId(memberId).page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages1=response1.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages1;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().memberId(memberId).page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String memberId1=list.getJSONObject(i).getString("member_id");
                        Preconditions.checkArgument(memberId1.contains(memberId),"会员ID筛选的内容为:"+memberId+"  列表项展示为："+memberId1);
                    }
                }
            }

            //获取第一行的人员ID
            String customerId=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("customer_id");
            //会员姓名查询
            IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().customerId(customerId).page(1).size(10).build();
            JSONObject response2=visitor.invokeApi(scene2);
            int pages2=response2.getInteger("pages")>3?3:response.getInteger("pages");
            for(int page=1;page<=pages2;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().customerId(customerId).page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String customerId1=list.getJSONObject(i).getString("customer_id");
                        Preconditions.checkArgument(customerId1.contains(customerId),"人员ID筛选的内容为:"+customerId+"  列表项展示为："+customerId1);
                    }
                }
            }


        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员筛选栏校验");
        }
    }

    /**
     *特殊人员详情页-操作日志
     */
    @Test(description = "特殊人员详情页-操作日志")
    public void authCashierPageSystem26(){
        try{
            //获取特殊人员列表第一行的人员ID
            JSONArray list1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            if(list1.size()>0){
                String customerId=list1.getJSONObject(0).getString("customer_id");
                //操作日志的页面
                IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.BlackWhiteListOperatePageScene.builder().customerId(customerId).page(1).size(10).build();
                JSONObject response=visitor.invokeApi(scene);
                int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
                for(int page=1;page<=pages;page++){
                    JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.BlackWhiteListOperatePageScene.builder().page(page).size(10).customerId(customerId).build().invoke(visitor,true).getJSONArray("list");
                    if(list.size()>0){
                        for(int i=0;i<list.size();i++){
                            String operateName=list.getJSONObject(i).getString("operate_name");
                            String operateReason=list.getJSONObject(i).getString("operate_reason");
                            String operateTime=list.getJSONObject(i).getString("operate_time");
                            String operateUser=list.getJSONObject(i).getString("operate_user");

                            Preconditions.checkArgument(operateName!=null&&operateReason!=null&&operateTime!=null&&operateUser!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                        }
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员详情页-操作日志");
        }
    }

    /**
     *特殊人员详情页-风控事件
     */
    @Test(description = "特殊人员详情页-风控事件")
    public void authCashierPageSystem27(){
        try{
            //获取特殊人员列表第一行的人员ID
            JSONArray list1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            if(list1.size()>0){
                String customerId=list1.getJSONObject(0).getString("customer_id");
                //操作日志的页面
                IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.BlackListEventPageScene.builder().customerId(customerId).page(1).size(10).build();
                JSONObject response=visitor.invokeApi(scene);
                int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
                for(int page=1;page<=pages;page++){
                    JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.BlackListEventPageScene.builder().page(page).size(10).customerId(customerId).build().invoke(visitor,true).getJSONArray("list");
                    if(list.size()>0){
                        for(int i=0;i<list.size();i++){
                            String riskRule=list.getJSONObject(i).getString("risk_rule");
                            String shopName=list.getJSONObject(i).getString("shop_name");
                            String triggerTime=list.getJSONObject(i).getString("trigger_time");
                            String handleStatus=list.getJSONObject(i).getString("handle_status");
                            String operateUser=list.getJSONObject(i).getString("operate_user");

                            Preconditions.checkArgument(riskRule!=null&&shopName!=null&&triggerTime!=null&&handleStatus!=null&&operateUser!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                        }
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员详情页-风控事件");
        }
    }


    /**
     *下载中心-下载列表内容校验
     */
    @Test(description = "下载中心-下载列表内容校验")
    public void authCashierPageSystem28(){
        try{
            IScene scene= DownloadPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=DownloadPageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String taskName=list.getJSONObject(i).getString("task_name");
                        String taskType=list.getJSONObject(i).getString("task_type");
                        String shopName=list.getJSONObject(i).getString("shop_name");
                        String timeFrame=list.getJSONObject(i).getString("time_frame");
                        String applicant=list.getJSONObject(i).getString("applicant");
                        String applicationTime=list.getJSONObject(i).getString("application_time");
                        Preconditions.checkArgument(taskName!=null&&taskType!=null&&shopName!=null&&timeFrame!=null&&applicant!=null&&applicationTime!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("下载中心-下载列表内容校验");
        }
    }

    /**
     *下载中心-下载列表筛选栏校验
     */
    @Test(description = "下载中心-下载列表筛选栏校验")
    public void authCashierPageSystem29(){
        try{
            //下载列表第一行的任务名称
            String taskName1=DownloadPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("task_name");
           //任务名称筛选
            IScene scene= DownloadPageScene.builder().page(1).taskName(taskName1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=DownloadPageScene.builder().taskName(taskName1).page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String taskName=list.getJSONObject(i).getString("task_name");
                        Preconditions.checkArgument(taskName.contains(taskName1),"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }

            //下载列表第一行的任务类型
            String taskType1=DownloadPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("task_type");
            //任务类型筛选
            IScene scene1= DownloadPageScene.builder().page(1).taskType(taskType1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages1=response1.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages1;page++){
                JSONArray list=DownloadPageScene.builder().taskType(taskType1).page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String taskType=list.getJSONObject(i).getString("task_type");
                        Preconditions.checkArgument(taskType.contains(taskType1),"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }

            //下载列表第一行的相关门店
            String shopName1=DownloadPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("shop_name");
            //相关门店筛选
            IScene scene2= DownloadPageScene.builder().page(1).shopName(shopName1).size(10).build();
            JSONObject response2=visitor.invokeApi(scene2);
            int pages2=response2.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages2;page++){
                JSONArray list=DownloadPageScene.builder().shopName(shopName1).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String shopName=list.getJSONObject(i).getString("shop_name");
                        Preconditions.checkArgument(shopName.contains(shopName1),"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }

            //下载列表第一行的申请人
            String applicant1=DownloadPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("applicant");
            //申请人筛选
            IScene scene3= DownloadPageScene.builder().page(1).applicant(applicant1).size(10).build();
            JSONObject response3=visitor.invokeApi(scene3);
            int pages3=response3.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages3;page++){
                JSONArray list=DownloadPageScene.builder().applicant(applicant1).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String applicant=list.getJSONObject(i).getString("applicant");
                        Preconditions.checkArgument(applicant.contains(applicant1),"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("下载中心-下载列表筛选栏校验");
        }
    }

    /**
     * 收银风控事件处理
     */
    @Test(description = "收银风控事件处理为正常")
    public void authCashierPageSystem30(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银风控事件页面-获取待处理第一条的ID
            IScene scene1=RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("待处理").build();
            JSONObject response1=visitor.invokeApi(scene1);
            if(response1.getJSONArray("list").size()>0){
                Long id=response1.getJSONArray("list").getJSONObject(0).getLong("id");
                //处理收银风控事件为正常
                String message=cu.getRiskEventHandle(id,1,pp.remarks,null).getString("message");
                Preconditions.checkArgument(message.equals("success"),"收银风控事件处理正常结果，处理失败");
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控事件处理为正常");
        }
    }

    /**
     * 员工信息查询列表项校验
     */
    @Test(description = "员工信息查询列表项校验")
    public void authCashierPageSystem31(){
        try{
            IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String customerId=list.getJSONObject(i).getString("customer_id");
                        String outStaffId=list.getJSONObject(i).getString("out_staff_id");
                        String outStaffName=list.getJSONObject(i).getString("out_staff_name");
                        String winsenseAccountId=list.getJSONObject(i).getString("winsense_account_id");
                        String faceUrl=list.getJSONObject(i).getString("face_url");
                        String statusName=list.getJSONObject(i).getString("status_name");
                        Preconditions.checkArgument(customerId!=null&&outStaffId!=null&&outStaffName!=null&&winsenseAccountId!=null&&faceUrl!=null&&statusName!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("员工信息查询列表项校验");
        }
    }

    /**
     * 员工信息查询列表--筛选项检验
     */
    @Test(description = "员工信息查询列表--筛选项检验")
    public void authCashierPageSystem32(){
        try{
            IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            //获取第一列的姓名和员工ID
            String name=response.getJSONArray("list").getJSONObject(0).getString("out_staff_name");
            String id=response.getJSONArray("list").getJSONObject(0).getString("out_staff_id");

            IScene scene1= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(1).size(10).outStaffName(name).build();
            JSONObject response1=visitor.invokeApi(scene1);
            int pages=response1.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(page).size(10).outStaffName(name).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String outStaffName=list.getJSONObject(i).getString("out_staff_name");
                        Preconditions.checkArgument(outStaffName.contains(name),"第"+page+"页"+"第"+i+"行的列表项和筛选项不一致，分别为："+outStaffName+"   "+name);
                    }
                }
            }

            IScene scene2= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(1).size(10).outStaffId(id).build();
            JSONObject response2=visitor.invokeApi(scene2);
            int pages2=response1.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages2;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(page).size(10).outStaffId(id).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String outStaffId=list.getJSONObject(i).getString("out_staff_id");

                        Preconditions.checkArgument(outStaffId.contains(id),"第"+page+"页"+"第"+i+"行的列表项和筛选项不一致，分别为："+outStaffId+"   "+id);
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("员工信息查询列表--筛选项检验");
        }
    }

    /**
     * 新建风控规则异常情况-黑名单风控-姓名{21个字}
     */
    @Test(description = "新建风控规则异常情况-黑名单风控-姓名{21个字}")
    public void authCashierPageSystem33(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.BLACK_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.blackNameException)
                    .rule(rule)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则21个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-黑名单风控-姓名{21个字}");
        }
    }

    /**
     * 新建风控规则异常情况-黑名单风控-姓名{为空}
     */
    @Test(description = "新建风控规则异常情况-黑名单风控-姓名{为空}")
    public void authCashierPageSystem34(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.BLACK_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .rule(rule)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则姓名为空个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-黑名单风控-姓名{为空}");
        }
    }

    /**
     * 新建风控规则异常情况-黑名单风控-业务类型为空
     */
    @Test(description = "新建风控规则异常情况-黑名单风控-业务类型为空")
    public void authCashierPageSystem35(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.BLACK_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.blackName)
                    .rule(rule)
                    .shopIds(shopIds)
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则业务类型为空个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-黑名单风控-业务类型为空");
        }
    }

    /**
     * 新建风控规则异常情况-黑名单风控-规则为空
     */
    @Test(description = "新建风控规则异常情况-黑名单风控-规则为空")
    public void authCashierPageSystem36(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.BLACK_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.blackName)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则规则为空个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-黑名单风控-规则类型为空");
        }
    }

    /**
     * 新建风控规则异常情况-黑名单风控-适用门店为空
     */
    @Test(description = "新建风控规则异常情况-黑名单风控-适用门店为空")
    public void authCashierPageSystem37(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.BLACK_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.blackName)
                    .rule(rule)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则适用门店为空个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-黑名单风控-适用门店为空");
        }
    }

    /**
     * 新建风控规则异常情况-重点观察人员风控-姓名{21个字}
     */
    @Test(description = "新建风控规则异常情况-重点观察人员风控-姓名{21个字}")
    public void authCashierPageSystem38(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.FOCUS_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.blackNameException)
                    .rule(rule)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则姓名21个字个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-重点观察人员风控-姓名{21个字}");
        }
    }

    /**
     * 新建风控规则异常情况-重点观察人员风控-姓名{为空}
     */
    @Test(description = "新建风控规则异常情况-重点观察人员风控-姓名{为空}")
    public void authCashierPageSystem39(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.FOCUS_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .rule(rule)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则姓名为空个字个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-重点观察人员风控-姓名{为空}");
        }
    }

    /**
     * 新建风控规则异常情况-重点观察人员风控-规则为空
     */
    @Test(description = "新建风控规则异常情况-重点观察人员风控-规则为空")
    public void authCashierPageSystem40(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.FOCUS_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.observeName)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则-规则为空个字个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-重点观察人员风控-规则为空");
        }
    }

    /**
     * 新建风控规则异常情况-重点观察人员风控-适用门店为空
     */
    @Test(description = "新建风控规则异常情况-重点观察人员风控-适用门店为空")
    public void authCashierPageSystem41(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.FOCUS_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.observeName)
                    .rule(rule)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则-适用门店为空个字个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-重点观察人员风控-适用门店为空");
        }
    }

    /**
     * 新建风控规则异常情况-重点观察人员风控-业务类型为空
     */
    @Test(description = "新建风控规则异常情况-重点观察人员风控-业务类型为空")
    public void authCashierPageSystem42(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type",RuleEnum.FOCUS_LIST.getType());
            //新建风控规则   todo
            IScene scene= AddScene.builder()
                    .name(pp.observeName)
                    .rule(rule)
                    .shopIds(shopIds)
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则-业务类型为空个字个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-重点观察人员风控-业务类型为空");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-无人风控-姓名{21个字}
     */
    @Test(description = "新建风控规则异常情况-收银风控-无人风控-姓名{21个字}")
    public void authCashierPageSystem43(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type", RuleEnum.CASHIER.getType());
            rule.put("item", RuleTypeEnum.UNMANNED_ORDER.getType());
            //新建风控规则
            IScene scene= AddScene.builder()
                    .name(pp.blackNameException)
                    .rule(rule)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则姓名21个字个字创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-无人风控-姓名{21个字}");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-无人风控-姓名为空
     */
    @Test(description = "新建风控规则异常情况-收银风控-无人风控-姓名为空")
    public void authCashierPageSystem44(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type", RuleEnum.CASHIER.getType());
            rule.put("item", RuleTypeEnum.UNMANNED_ORDER.getType());
            //新建风控规则
            IScene scene= AddScene.builder()
                    .rule(rule)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则姓名为空创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-无人风控-姓名为空");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-无人风控-规则为空
     */
    @Test(description = "新建风控规则异常情况-收银风控-无人风控-规则为空")
    public void authCashierPageSystem45(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type", RuleEnum.CASHIER.getType());
            rule.put("item", RuleTypeEnum.UNMANNED_ORDER.getType());
            //新建风控规则
            IScene scene= AddScene.builder()
                    .name(pp.cashierName)
                    .shopIds(shopIds)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则-规则为空创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-无人风控-规则为空");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-无人风控-适用门店为空
     */
    @Test(description = "新建风控规则异常情况-收银风控-无人风控-适用门店为空")
    public void authCashierPageSystem46(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type", RuleEnum.CASHIER.getType());
            rule.put("item", RuleTypeEnum.UNMANNED_ORDER.getType());
            //新建风控规则
            IScene scene= AddScene.builder()
                    .name(pp.cashierName)
                    .rule(rule)
                    .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则适用门店为空创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-无人风控-适用门店为空");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-无人风控-业务类型为空
     */
    @Test(description = "新建风控规则异常情况-收银风控-无人风控-业务类型为空")
    public void authCashierPageSystem47(){
        try{
            //应用的门店     todo
            List<String> shopIds=new ArrayList<>();
            shopIds.add("");
            shopIds.add("");
            //规则中的type
            //规则中的type
            JSONObject rule=new JSONObject();
            rule.put("type", RuleEnum.CASHIER.getType());
            rule.put("item", RuleTypeEnum.UNMANNED_ORDER.getType());
            //新建风控规则
            IScene scene= AddScene.builder()
                    .name(pp.cashierName)
                    .rule(rule)
                    .shopIds(shopIds)
                    .build();
            String message=visitor.invokeApi(scene,false).getString("message");

            Preconditions.checkArgument(message.equals(""),"风控规则业务类型为空创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-无人风控-业务类型为空");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-一人多单-连续天数367天
     */
    @Test(description = "新建风控规则异常情况-收银风控-一人多单-连续天数367天")
    public void authCashierPageSystem48(){
        try{
            //一人多单/连续天数
            String message=cu.getCashierOrderRuleAdd("367","3").getString("message");

            Preconditions.checkArgument(message.equals(""),"一人多单-连续天数367天 创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-一人多单-连续天数376天");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-一人多单-上限单数10001
     */
    @Test(description = "新建风控规则异常情况-收银风控-一人多单-上限单数10001")
    public void authCashierPageSystem49(){
        try{
            //一人多单/连续天数
            String message=cu.getCashierOrderRuleAdd("1","10001").getString("message");

            Preconditions.checkArgument(message.equals(""),"一人多单-上限单数10001 创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-一人多单-上限单数10001");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-一员工支付订单--连续天数367天
     */
    @Test(description = "新建风控规则异常情况-收银风控-员工支付订单--连续天数367天")
    public void authCashierPageSystem50(){
        try{
            //一人多单/连续天数
            String message=cu.getCashierEmployeeRuleAdd("367","3").getString("message");
            Preconditions.checkArgument(message.equals(""),"员工支付订单--连续天数367天 创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控--员工支付订单--连续天数376天");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-员工支付订单-上限单数10001
     */
    @Test(description = "新建风控规则异常情况-收银风控-员工支付订单--上限单数10001")
    public void authCashierPageSystem51(){
        try{
            //一人多单/连续天数
            String message=cu.getCashierEmployeeRuleAdd("1","10001").getString("message");

            Preconditions.checkArgument(message.equals(""),"员工支付订单--上限单数10001 创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-员工支付订单--上限单数10001");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-一人多车-上限单数1000
     */
    @Test(description = "新建风控规则异常情况-收银风控-一人多车--上限单数1000")
    public void authCashierPageSystem52(){
        try{
            //一人多单/连续天数
            String message=cu.getCashierCarRuleAdd("1000").getString("message");

            Preconditions.checkArgument(message.equals(""),"一人多车--上限单数1000 创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-一人多车--上限单数1000");
        }
    }

    /**
     * 新建风控规则异常情况-收银风控-一车多人-上限单数1000
     */
    @Test(description = "新建风控规则异常情况-收银风控-一车多人--上限单数1000")
    public void authCashierPageSystem53(){
        try{
            //一人多单/连续天数
            String message=cu.getCashierMemberRuleAdd("1000").getString("message");

            Preconditions.checkArgument(message.equals(""),"一车多人--上限单数1000 创建成功");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("新建风控规则异常情况-收银风控-一车多人--上限单数1000");
        }
    }








    /*
     * ------------------------------------------------------------------组织架构-------------------------------------------------------
     */

    /**
     * 角色管理列表项校验
     */
    @Test(description = "角色管理列表项校验")
    public void organizationChartSystem1(){
        try{
            IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String name=list.getJSONObject(i).getString("name");
                        String description=list.getJSONObject(i).getString("description");
                        String num=list.getJSONObject(i).getString("num");
                        String createTime=list.getJSONObject(i).getString("create_time");
                        Preconditions.checkArgument(name!=null&&description!=null&&num!=null&&createTime!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("角色管理列表项校验");
        }
    }

    /**
     * 新增角色
     */
    @Test
    public void organizationChartSystem2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新增一个角色---上级角色为【总管理员】
            Long roleId = cu.getAddRole(pp.roleName,pp.descriptionRole);
            checkArgument(roleId!=null, "新增角色失败了");

            //编辑角色
            String message1=cu.getEditRole(roleId,pp.roleEditName,pp.descriptionEditRole);
            checkArgument(message1.equals("success"), "编辑角色的信息失败了");

            //列表中编辑过的角色是否已更新
            IScene scene2= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build();
            JSONObject response2=visitor.invokeApi(scene2);
            int pages=response2.getInteger("pages");
            JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(pages).size(10).build().invoke(visitor,true).getJSONArray("list");
            JSONArray list1 = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(pages).size(list.size()).build().invoke(visitor,true).getJSONArray("list");
            String name = list1.getJSONObject(0).getString("name");
            checkArgument(name.equals(pp.roleEditName), "编辑过的角色没有更新在列表");

            //新建成功以后删除新建的账号
            if (roleId>0) {
                String message13=cu.getDelRole(roleId);
                checkArgument(message13.equals("success"), "删除角色:" + roleId + "失败了");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增删改查角色");
        }

    }

    /**
     * 新增角色(名称校验)异常校验
     */
    @Test
    public void organizationChartSystem3() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            //权限合集
            JSONArray authIds = new JSONArray();
            authIds.add(7);
            authIds.add(9);
            //新增角色名称21个字角色
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder().name(pp.rolNameException).parentRoleId(2).authList(authIds).description(pp.descriptionRole).build();
            JSONObject response=visitor.invokeApi(scene,false);
            checkArgument(response.getString("message").equals("角色名称需要在1-20个字内"), "角色名称为21个字，创建成功");

            //新增重复角色名称的角色
            IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder().name(pp.rolNameException).parentRoleId(2).authList(authIds).description(pp.descriptionRole).build();
            JSONObject response1=visitor.invokeApi(scene1,false);
            checkArgument(response1.getString("message").equals("新增角色异常:当前角色名称已存在！请勿重复添加"), "重复的角色名称，创建成功:"+pp.rolNameException);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增角色(名称校验)");
        }

    }

    /**
     * 新增角色(权限说明校验)--异常校验
     */
    @Test
    public void organizationChartSystem4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //权限合集
            JSONArray authIds = new JSONArray();
            authIds.add(7);
            authIds.add(9);
            //新增角色权限说明51个字的角色
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder().name(pp.roleName).parentRoleId(2).authList(authIds).description(pp.descriptionRoleException).build();
            JSONObject response=visitor.invokeApi(scene,false);
            checkArgument(response.getString("message").equals("角色名称需要在1-50个字内"), "角色权限说明为51个字，创建成功:"+pp.descriptionRoleException);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增角色(权限说明校验)");
        }
    }

    /**
     * 账号管理列表项校验
     */
    @Test(description = "账号管理列表项校验")
    public void organizationChartSystem5(){
        try{
            IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        String name=list.getJSONObject(i).getString("name");
                        String description=list.getJSONObject(i).getString("description");
                        String num=list.getJSONObject(i).getString("num");
                        String createTime=list.getJSONObject(i).getString("create_time");
                        Preconditions.checkArgument(name!=null&&description!=null&&num!=null&&createTime!=null,"第"+page+"页"+"第"+i+"行的列表项存在为空的值");
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("账号管理列表项校验");
        }
    }

    /**
     * 账号管理的增删改
     */
    @Test
    public void organizationChartSystem6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建角色，获取角色Id
            JSONArray authIds = new JSONArray();
            authIds.add(7);
            authIds.add(9);
            //新增一个角色---上级角色为【总管理员】
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder().name(pp.roleName).parentRoleId(2).authList(authIds).description(pp.descriptionRole).build();
            JSONObject response=visitor.invokeApi(scene);
            Long roleId = response.getLong("role_id");
            Preconditions.checkArgument(roleId!=null, "新建角色失败");

            //新建账号       todo
            String id=cu.createAccountNumber(pp.roleName,pp.ownerPhone);
            Preconditions.checkArgument(id!=null, "新建账号失败");

            //编辑账号
            String message1=cu.getEditAccountNumber(id,pp.roleEditName,pp.ownerPhone);
            //获取账号管理页面的第一条的名字
            String name=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getString("name");
            Preconditions.checkArgument(name.equals(pp.staffEditName)&&message1.equals("success"), "编辑账号失败");

            //删除角色，删除失败
            IScene scene3=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.DeleteScene.builder().id(roleId).build();
            String message3=visitor.invokeApi(scene3,false).getString("message");
            Preconditions.checkArgument(message3.equals("使用中的角色不可以删除"), "删除角色:" + roleId + "成功了");

            //新建成功以后删除新建的账号
            IScene scene4=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.DeleteScene.builder().id(id).build();
            String message4=visitor.invokeApi(scene4,false).getString("message");
            Preconditions.checkArgument(message4.equals("success"), "删除账号:" + id + "失败了");

            //删除角色，删除成功
            IScene scene5=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.DeleteScene.builder().id(roleId).build();
            String message5=visitor.invokeApi(scene5,false).getString("message");
            Preconditions.checkArgument(message5.equals("success"), "删除角色:" + roleId + "失败了");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("账号管理的增删改");
        }

    }

    /**
     * 创建账号的异常情况--姓名51个字
     */
    @Test(description = "创建账号的异常情况--姓名51个字")
    public void organizationChartSystem7(){
        try{
            //新建账号    todo
            JSONArray roleList=new JSONArray();
            JSONObject object=new JSONObject();
            List<JSONObject> shopList=new ArrayList<>();
            JSONObject shopObject=new JSONObject();
            shopObject.put("shop_id","");
            shopObject.put("shop_name","");
            shopList.add(shopObject);
            object.put("role_id","");
            object.put("shop_list",shopList);
            roleList.add(object);
            IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.AddScene.builder().name(pp.staffNameException).phone("13373166806").gender("女").roleList(roleList).build();
            String message =visitor.invokeApi(scene2).getString("message");
            Preconditions.checkArgument(message.equals(""),"账号名称51个字创建成功");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("创建账号的异常情况--姓名51个字");
        }
    }

    /**
     * 创建账号的异常情况--手机号{10位。12位，中文。英文。标点符号，电话号码字段}
     */
    @Test(description = "创建账号的异常情况--手机号{10位。12位，中文。英文。标点符号，电话号码字段}")
    public void organizationChartSystem8(){
        try{
            String[] phoneArray={"1337316680","133731668066","哈哈哈哈哈哈哈哈哈哈哈哈","aaaaaaaaaaaa","!@#$%^&*<>?>","0314-6499363"};
            for(int i=0;i<phoneArray.length;i++){
                //新建账号
                JSONArray roleList=new JSONArray();
                JSONObject object=new JSONObject();
                List<JSONObject> shopList=new ArrayList<>();
                JSONObject shopObject=new JSONObject();
                shopObject.put("shop_id","");
                shopObject.put("shop_name","");
                shopList.add(shopObject);
                object.put("role_id","");
                object.put("shop_list",shopList);
                roleList.add(object);
                IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.AddScene.builder().name(pp.staffName).phone(phoneArray[i]).gender("女").roleList(roleList).build();
                String message =visitor.invokeApi(scene2).getString("message");
                Preconditions.checkArgument(message.equals(""),"手机号{10位。12位，中文。英文。标点符号，电话号码字段}创建成功");

            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("创建账号的异常情况--手机号{10位。12位，中文。英文。标点符号，电话号码字段}");
        }
    }

    /**
     * 创建账号的异常情况--添加6个角色
     */
    @Test(description = "创建账号的异常情况--添加6个角色")
    public void organizationChartSystem9(){
        try{
            //新建账号
            JSONArray roleList=new JSONArray();
            JSONObject object=new JSONObject();
            List<JSONObject> shopList=new ArrayList<>();
            JSONObject shopObject=new JSONObject();
            shopObject.put("shop_id","");
            shopObject.put("shop_name","");
            shopList.add(shopObject);
            object.put("role_id","");
            object.put("shop_list",shopList);
            roleList.add(object);
            roleList.add(object);
            roleList.add(object);
            roleList.add(object);
            roleList.add(object);
            roleList.add(object);

            IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.AddScene.builder().name(pp.staffName).phone("13373166806").gender("女").roleList(roleList).build();
            String message =visitor.invokeApi(scene2).getString("message");
            Preconditions.checkArgument(message.equals(""),"创建账号的异常情况--添加6个角色成功");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("创建账号的异常情况--添加6个角色");
        }
    }

    /**
     * 禁用账号，登陆失败，开启账号，登陆成功
     */
    @Test(description = "禁用账号，登陆失败，开启账号，登陆成功")
    public void organizationChartSystem10(){
        try{
            String id="";
            //禁用固定密码的账号
            String message=cu.getStaffStatusChange(id,"").getString("message");
            Preconditions.checkArgument(message.equals("success"),"禁用失败");
            //登录禁用的账号
            md.pcLogin("","");
            //开启账号
            String message2=cu.getStaffStatusChange(id,"").getString("message");
            Preconditions.checkArgument(message2.equals("success"),"开启失败");
            //登录开启的账号
            md.pcLogin("","");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("禁用账号，登陆失败，开启账号，登陆成功");
        }
    }

    /**
     * 账户列表的筛选（单一查询）
     */
    @Test
    public void organizationChartSystem11() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            JSONArray list =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            Long shopId = list.getJSONObject(0).getLong("shop_id");
            Long roleId = list.getJSONObject(0).getLong("role_id");
            String phone = list.getJSONObject(1).getString("phone");

            //根据账号名称筛选
            JSONObject response1 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().name(name).page(1).size(10).build().invoke(visitor,true);
            int pages=response1.getInteger("pages")>3?3:response1.getInteger("pages");
            for(int page=1;page<=pages;page++) {
                JSONArray list1 = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().name(name).page(page).size(10).build().invoke(visitor, true).getJSONArray("list");
                for (int i = 0; i < list1.size(); i++) {
                    Preconditions.checkArgument(name.equals(list1.getJSONObject(i).getString("name")), "根据列表第一个账号名称" + name + "进行筛选的结果和筛选条件不一致");
                }
            }
            //根据phone筛选
            JSONObject response2 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().phone(phone).page(1).size(10).build().invoke(visitor,true);
            int pages2=response2.getInteger("pages")>3?3:response2.getInteger("pages");
            for(int page=1;page<=pages2;page++) {
                JSONArray list2 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().phone(phone).page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                for (int i = 0; i < list2.size(); i++) {
                    Preconditions.checkArgument(phone.equals(list2.getJSONObject(0).getString("phone")), "根据phone" + phone + "进行筛选的结果和筛选条件不一致");
                }
            }

            //根据门店筛选
            JSONObject response3 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().shopId(shopId).page(1).size(10).build().invoke(visitor,true);
            int pages3=response3.getInteger("pages")>3?3:response3.getInteger("pages");
            for(int page=1;page<=pages3;page++) {
                JSONArray list3 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().shopId(shopId).page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                for (int i = 0; i < list3.size(); i++) {
                    Preconditions.checkArgument(phone.equals(list3.getJSONObject(0).getString("shop_id")), "根据shopId" + shopId + "进行筛选的结果和筛选条件不一致");
                }
            }

            //根据门店筛选
            JSONObject response4 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().shopId(shopId).page(1).size(10).build().invoke(visitor,true);
            int pages4=response4.getInteger("pages")>3?3:response4.getInteger("pages");
            for(int page=1;page<=pages4;page++) {
                JSONArray list4 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().roleId(roleId).page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                for (int i = 0; i < list4.size(); i++) {
                    Preconditions.checkArgument(phone.equals(list4.getJSONObject(0).getString("role_id")), "根据roleId" + roleId + "进行筛选的结果和筛选条件不一致");
                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("账号列表的筛选（单一条件筛选）");
        }

    }




    /*
     * --------------------------------------------------导出操作---------------------------------------------------
     */

    /**
     * 收银风控事件导出
     */
    @Test(description = "收银风控事件导出")
    public void ExportChecking1(){
        try{
            //收银风控列表第一条的shopId
            IScene scene = PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long shopId=response.getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //收银风控事件页面
            IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).isExport(true).build();
            String message=visitor.invokeApi(scene1,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"收银风控事件导出失败");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("收银风控事件导出");
        }
    }

    /**
     * 风控告警页面导出
     */
    @Test(description = "风控告警页面导出")
    public void ExportChecking2(){
        try{
            //风控规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarm.PageScene.builder().page(1).size(10).isExport(true).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"风控告警页面导出失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("风控告警页面导出");
        }
    }

    /**
     * 特殊人员管理-风控白名单导出
     */
    @Test(description = "特殊人员管理-风控白名单导出")
    public void ExportChecking3(){
        try{
            //风控规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("WHITE").isExport(true).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"特殊人员管理-风控白名单导出失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员管理-风控白名单导出");
        }
    }

    /**
     * 特殊人员管理-风控黑名单导出
     */
    @Test(description = "特殊人员管理-风控黑名单导出")
    public void ExportChecking4(){
        try{
            //风控规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("BLACK").isExport(true).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"特殊人员管理-风控黑名单导出失败");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员管理-风控黑名单导出");
        }
    }

    /**
     * 特殊人员管理-风控重点观察人员导出
     */
    @Test(description = "特殊人员管理-风控黑名单导出")
    public void ExportChecking5(){
        try{
            //风控规则页面
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("FOCUS").isExport(true).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"特殊人员管理-风控重点观察人员导出");
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("特殊人员管理-风控重点观察人员导出");
        }
    }












    /**
     * ----------------------------------------------复制青青的代码------------------------------------------------
     */

    /**
     * 收银风控筛选（单一筛选）
     */
    @Test
    public void cashier_page_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = md.cashier_page("", "", "", "", null, pp.page, pp.size);
            JSONArray list = res.getJSONArray("list");
            String shop_name = list.getJSONObject(0).getString("shop_name");
            //根据门店名称进行筛选
            JSONArray list1 = md.cashier_page(shop_name, "", "", "", null, pp.page, pp.size).getJSONArray("list");
            String shop_name1 = list1.getJSONObject(0).getString("shop_name");
            checkArgument(shop_name.contains(shop_name1), "根据门店名称" + shop_name + "搜索,没有查询到应有的结果");

            //根据累计风险事件排序(传0为降序；从大-小)
            JSONArray list2 = md.cashier_page("", "", "", "RISK", 0, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list2.size() - 1; i++) {
                int risk_total1 = list2.getJSONObject(i).getInteger("risk_total");
                int risk_total2 = list2.getJSONObject(i + 1).getInteger("risk_total");
                checkArgument(risk_total1 >= risk_total2, "选择累计风险事件从大到小进行排序出现了排序错误");
            }
            //根据累计风险事件排序(传1为升序；从小-大)
            JSONArray list3 = md.cashier_page("", "", "", "RISK", 1, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list3.size() - 1; i++) {
                int risk_total1 = list3.getJSONObject(i).getInteger("risk_total");
                int risk_total2 = list3.getJSONObject(i + 1).getInteger("risk_total");
                checkArgument(risk_total1 <= risk_total2, "选择累计风险事件从小到大进行排序出现了排序错误");

            }

            //根据累计异常事件排序(传0为降序；从大-小)
            JSONArray list4 = md.cashier_page("", "", "", "ABNORMAL", 0, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list4.size() - 1; i++) {
                if (list4.size() > 1) {
                    int abnormal_total1 = list4.getJSONObject(i).getInteger("abnormal_total");
                    int abnormal_total2 = list4.getJSONObject(i + 1).getInteger("abnormal_total");
                    checkArgument(abnormal_total1 >= abnormal_total2, "选择异常事件从大到小进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }
            //根据累计异常事件排序(传1为升序；从小-大)
            JSONArray list5 = md.cashier_page("", "", "", "ABNORMAL", 1, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list5.size() - 1; i++) {
                if (list5.size() > 1) {
                    int abnormal_total1 = list5.getJSONObject(i).getInteger("abnormal_total");
                    int abnormal_total2 = list5.getJSONObject(i + 1).getInteger("abnormal_total");
                    checkArgument(abnormal_total1 <= abnormal_total2, "选择异常事件从小到大进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }
            //根据累计正常事件排序(传0为降序；从大-小)
            JSONArray list6 = md.cashier_page("", "", "", "NORMAL", 0, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list6.size() - 1; i++) {
                if (list6.size() > 1) {
                    int normal_total1 = list6.getJSONObject(i).getInteger("normal_total");
                    int normal_total2 = list6.getJSONObject(i + 1).getInteger("normal_total");
                    checkArgument(normal_total1 >= normal_total2, "选择正常事件从大到小进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }
            //根据累计正常事件排序(传1为升序；从小-大)
            JSONArray list7 = md.cashier_page("", "", "", "NORMAL", 1, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list7.size() - 1; i++) {
                if (list7.size() > 1) {
                    int normal_total1 = list7.getJSONObject(i).getInteger("normal_total");
                    int normal_total2 = list7.getJSONObject(i + 1).getInteger("normal_total");
                    checkArgument(normal_total1 <= normal_total2, "选择正常事件从小到大进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }

            //根据待处理风险事件排序(传0为降序；从大-小)
            JSONArray list8 = md.cashier_page("", "", "", "PENDINGRISKS", 0, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list8.size() - 1; i++) {
                if (list8.size() > 1) {
                    int pending_total1 = list8.getJSONObject(i).getInteger("pending_risks_total");
                    int pending_total2 = list8.getJSONObject(i + 1).getInteger("pending_risks_total");
                    checkArgument(pending_total1 >= pending_total2, "选择待处理事件从大到小进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }
            //根据待处理风险事件排序(传1为升序；从小-大)
            JSONArray list9 = md.cashier_page("", "", "", "PENDINGRISKS", 1, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list9.size() - 1; i++) {
                if (list9.size() > 1) {
                    int pending_total1 = list9.getJSONObject(i).getInteger("pending_risks_total");
                    int pending_total2 = list9.getJSONObject(i + 1).getInteger("pending_risks_total");
                    checkArgument(pending_total1 <= pending_total2, "选择待处理事件从小到大进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        }  finally {
            saveData("收银风控筛选（单一筛选）和根据累计风险事件，累计正常事件，累计异常事件，累计待处理事件进行排序");
        }
    }


    /**
     * 收银风控事件的筛选（单一查询）
     */
    @Test
    public void risk_eventSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = md.cashier_riskPage(pp.shop_id_01, "", "", "", "", "", "", pp.page, pp.size).getJSONArray("list");
            String event_name = list.getJSONObject(0).getString("event_name");
            String order_id = list.getJSONObject(0).getString("order_id");
            String order_date = list.getJSONObject(0).getString("order_date").substring(0, 10);
            String order_date_01 = list.getJSONObject(0).getString("order_date");
            String handle_result = list.getJSONObject(0).getString("handle_result");
            String current_state = list.getJSONObject(0).getString("current_state");

            //根据事件名称进行筛选
            JSONArray list1 = md.cashier_riskPage(pp.shop_id_01, event_name, "", "", "", "", "", pp.page, pp.size).getJSONArray("list");
            String event_name1 = list1.getJSONObject(0).getString("event_name");
            checkArgument(event_name.contains(event_name1), "根据日期" + event_name + "搜索,没有查询到应有的结果");

            //根据小票单号进行筛选
            JSONArray list2 = md.cashier_riskPage(pp.shop_id_01, "", order_id, "", "", "", "", pp.page, pp.size).getJSONArray("list");
            String order_id1 = list2.getJSONObject(0).getString("order_id");
            checkArgument(order_id.contains(order_id1), "根据订单编号" + order_id + "搜索,没有查询到应有的结果");

            //根据收银日期进行筛选
            JSONArray list3 = md.cashier_riskPage(pp.shop_id_01, "", "", order_date, "", "", "", pp.page, pp.size).getJSONArray("list");
            String order_date1 = list3.getJSONObject(0).getString("order_date");
            checkArgument(order_date_01.equals(order_date1), "根据收银日期" + order_date + "搜索,没有查询到应有的结果");


            //根据处理结果进行筛选
            JSONArray list4 = md.cashier_riskPage(pp.shop_id_01, "", "", "", "", handle_result, "", pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list4.size(); i++) {
                String handle_result1 = list4.getJSONObject(i).getString("handle_result");
                checkArgument(handle_result.contains(handle_result1), "根据订单编号" + handle_result + "搜索,没有查询到应有的结果");
            }

//            //根据当前状态进行筛选
//            JSONArray list5 = md.cashier_riskPage(shop_id_01, "", "", "", "", "", current_state, page, size).getJSONArray("list");
//            for (int i = 0; i < list5.size(); i++) {
//                String current_state1 = list1.getJSONObject(i).getString("current_state");
//                checkArgument(current_state.contains(current_state1), "根据当前状态" + current_state + "搜索,没有查询到应有的结果");
//            }

//            //根据全部结果进行筛选
//            JSONArray list6 = md.cashier_riskPage(shop_id_01, event_name, order_id, order_date, "", "", current_state, page, size).getJSONArray("list");
//
//            checkArgument(list6.size() == 1, "根据列表第一个内容作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("收银风控事件的筛选（单一条件筛选）");
        }

    }

    /**
     *风控规则列表筛选（单一查询）
     */
    @Test
    public void rule_pageSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.risk_controlPage("", "", "", null, pp.page, pp.size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
            String shop_type = list.getJSONObject(0).getString("shop_type");
            Integer status = list.getJSONObject(0).getInteger("status");

            //根据规则名称进行筛选
            JSONArray list1 = md.risk_controlPage(name, "", "", null, pp.page, pp.size).getJSONArray("list");
            checkArgument(name.equals(list1.getJSONObject(0).getString("name")), "根据列表第一个的规则名称作为条件进行筛选搜索,没有查询到应有的结果");

            //根据规则类型进行筛选
            JSONArray list2 = md.risk_controlPage("", type, "", null, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                String type1 = list2.getJSONObject(i).getString("type");
                checkArgument(type.equals(type1), "根据列表第一个的规则类型作为条件进行筛选搜索,没有查询到应有的结果");

            }
            //根据门店类型进行筛选
            JSONArray list3 = md.risk_controlPage("", "", shop_type, null, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list3.size(); i++) {
                String shop_type1 = list3.getJSONObject(i).getString("shop_type");
                checkArgument(shop_type.equals(shop_type1), "根据列表第一个的门店类型作为条件进行筛选搜索,没有查询到应有的结果");

            }
            //根据规则开关进行筛选
            JSONArray list4 = md.risk_controlPage("", "", "", status, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list4.size(); i++) {
                int status1 = list4.getJSONObject(i).getInteger("status");
                checkArgument(status == status1, "根据列表第一个的规则类型作为条件进行筛选搜索,没有查询到应有的结果");

            }


            //根据全部条件进行筛选
            JSONArray list5 = md.risk_controlPage(name, type, shop_type, status, pp.page, pp.size).getJSONArray("list");
            checkArgument(list5.size() == 1, "根据列表第一个的信息作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("账号列表的筛选（单一条件筛选）");
        }

    }

    /**
     * 风控告警列表的筛选（单一查询）
     */
    @Test
    public void alarm_pageSearch() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = md.alarm_page("", "", "", pp.page, pp.size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
            String shop_name = list.getJSONObject(0).getString("shop_name");

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list1 = md.alarm_page(name, "", "", pp.page, pp.size).getJSONArray("list");
            String name1 = list1.getJSONObject(0).getString("name");
            checkArgument(name.equals(name1), "根据告警名称" + name + "筛查，没有查询到相应的结果");


//            //根据列表第一个告警规则的类型进行筛查
//            JSONArray list2 = md.alarm_page("", type, "", page, size).getJSONArray("list");
//            for (int i = 0; i < list2.size(); i++) {
//                String type1 = list2.getJSONObject(i).getString("type");
//                checkArgument(type.equals(type1), "根据告警类型" + type + "筛查，没有查询到相应的结果");
//            }

            //根据列表第一个告警规则的门店名字进行筛查
            JSONArray list3 = md.alarm_page("", "", shop_name, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list3.size(); i++) {
                String shop_name1 = list3.getJSONObject(i).getString("shop_name");
                checkArgument(shop_name.equals(shop_name1), "根据门店名称" + shop_name + "筛查，没有查询到相应的结果");
            }

//            //根据列表第一个告警规则的信息进行筛查
//            JSONArray list4 = md.alarm_page(name, "", shop_name, page, size).getJSONArray("list");
//            checkArgument(list4.size() == 1, "根据列表第一个的信息作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警列表的筛选（单一条件筛选）");
        }

    }

    /**
     *风控告警规则的筛选（单一查询）
     */
    @Test
    public void alarm_ruleSearch() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = md.alarm_rulePage("", "", "", null, pp.page, pp.size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
//            String accept_role = list.getJSONObject(0).getString("accept_role");
            int status = list.getJSONObject(0).getInteger("status");

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list1 = md.alarm_rulePage(name, "", "", null, pp.page, pp.size).getJSONArray("list");
            String name1 = list1.getJSONObject(0).getString("name");
            checkArgument(name.equals(name1), "根据告警名称" + name + "筛查，没有查询到相应的结果");


            //根据列表第一个告警规则的类型进行筛查
            JSONArray list2 = md.alarm_rulePage("", type, "", null, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                String type1 = list2.getJSONObject(i).getString("type");
                checkArgument(type.equals(type1), "根据告警类型" + type + "筛查，没有查询到相应的结果");
            }

//            //根据列表第一个告警规则的接收者进行筛查
//            JSONArray list3 = md.alarm_rulePage("", "", accept_role, null, page, size).getJSONArray("list");
//            for (int i = 0; i < list3.size(); i++) {
//                JSONArray accept_role1 = list3.getJSONObject(i).getJSONArray("accept_role");
//                checkArgument(accept_role1.contains(accept_role), "根据告警接收者" + accept_role + "筛查，没有查询到相应的结果");
//            }

            //根据列表第一个告警规则的状态进行筛查
            JSONArray list5 = md.alarm_rulePage("", "", "", status, pp.page, pp.size).getJSONArray("list");
            for (int i = 0; i < list5.size(); i++) {
                int status1 = list5.getJSONObject(i).getInteger("status");
                checkArgument(status == status1, "根据告警规则状态" + status + "筛查，没有查询到相应的结果");
            }

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list4 = md.alarm_rulePage(name, type, "", null, pp.page, pp.size).getJSONArray("list");
            checkArgument(list4.size() == 1, "根据列表第一个的信息作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警列表的筛选（单一条件筛选）");
        }

    }

}
