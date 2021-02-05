package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author : huangqingqing
 * @date :  2020/08/04
 */
public class StorePcCase extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    StoreFuncPackage mds = StoreFuncPackage.getInstance();
    String districtCode = "";
    String shopManager = "";
    int page = 1;
    int size = 50;
    long shop_id = 4116l;
    long shop_id_01 = 43072l;
    String name = "是青青的AUTOtest";
    String email = "317079750@qq.com";
    String phone = "15080900001";
    String remark = "爱心是一盏明灯，照亮了人们前进的道路；爱心是一把温暖的火，温暖了你我的心；爱心是空旷田野的一声呼唤，使心灵冰冷的人得到温暖的慰籍。虽然有时爱心之光如豆，但却可以温暖自己，照亮别人。\n" +
            "那是一个星期日，妈妈领着我去老姨姥家做客。我家是在92路的终点，所以一上车空位特别多。我和妈妈随便找了一个座，便坐下了。坐了4、5站上来了一位老人，他老态龙钟，动作迟缓，似乎每挪动一步都需要付出很大力气。双腿颤颤巍巍，车一起动，好悬没把他晃倒，手在空中划拉半天才抓住立杆。再看看我周围的乘客，有的假装睡觉；有的扭头看窗外风景，还有的掏出手机连眼皮都不抬一下，好像车上没有这个人。看到这番情景，我想：老爷爷都这么大岁数了，竟然谁也不给他让座，既然车上的人都不给老爷爷让座，那我应该给老爷爷让座。车驶进了繁华地段了，也越来越颠簸，老爷爷肯定会坚持不住的。而我却很年少，站几站没事的。想到这儿，我从座位上站起来，对老爷爷说：“老爷爷，您坐这儿吧！”老爷爷推辞道：“谢谢你，孩子！我没有几站就到了，还是你坐吧。”我边搀着老爷爷边骗他说：“爷爷，我马上要下车了，还是您坐吧。”老爷爷听了，笑呵呵地说：“那就谢谢你了，孩子我谢谢你呢哈";
    String remarks = "有爱心是一盏明灯，照亮了人们前进的道路；爱心是一把温暖的火，温暖了你我的心；爱心是空旷田野的一声呼唤，使心灵冰冷的人得到温暖的慰籍。虽然有时爱心之光如豆，但却可以温暖自己，照亮别人。\n" +
            "那是一个星期日，妈妈领着我去老姨姥家做客。我家是在92路的终点，所以一上车空位特别多。我和妈妈随便找了一个座，便坐下了。坐了4、5站上来了一位老人，他老态龙钟，动作迟缓，似乎每挪动一步都需要付出很大力气。双腿颤颤巍巍，车一起动，好悬没把他晃倒，手在空中划拉半天才抓住立杆。再看看我周围的乘客，有的假装睡觉；有的扭头看窗外风景，还有的掏出手机连眼皮都不抬一下，好像车上没有这个人。看到这番情景，我想：老爷爷都这么大岁数了，竟然谁也不给他让座，既然车上的人都不给老爷爷让座，那我应该给老爷爷让座。车驶进了繁华地段了，也越来越颠簸，老爷爷肯定会坚持不住的。而我却很年少，站几站没事的。想到这儿，我从座位上站起来，对老爷爷说：“老爷爷，您坐这儿吧！”老爷爷推辞道：“谢谢你，孩子！我没有几站就到了，还是你坐吧。”我边搀着老爷爷边骗他说：“爷爷，我马上要下车了，还是您坐吧。”老爷爷听了，笑呵呵地说：“那就谢谢你了，孩子我谢谢你呢哈";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();

        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");

        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672","18513118484", "18810332354", "15084928847"};
        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //13436941018 吕雪晴
        //17610248107 廖祥茹
        //15084928847 黄青青
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //18672733045 高凯
        //15898182672 华成裕
        //18810332354 刘峤
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
//        commonConfig.shopId = "43072"; //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + md);

        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


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
    }
    /**
    *生成交易订单
    **/
    @Test
    public void getA() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (int i=0;i<6;i++) {
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
                        "        \"memberName\":\"嘉期狗贼&涛涛22223333333\",\n" +
                        "        \"receipt_type\":\"小票类型\",\n" +
                        "        \"posId\": \"pos-1234586789\",\n" +
                        "        \"commodityList\": [\n" +
                        "            {\n" +
                        "                \"commodityId\": \"iPhone12ABCDE\",\n" +
                        "                \"commodity_name\":\"苹果12s\",\n" +
                        "                \"unit_price\": 200,\n" +
                        "                \"num\": 4\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"commodityId\": \"bananaABCDE\",\n" +
                        "                \"commodity_name\":\"香蕉20根啊\",\n" +
                        "                \"unit_price\": 2,\n" +
                        "                \"num\": 4\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"commodityId\": \"AppleABCDE\",\n" +
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
    /**
     *生成交易订单
     **/
    @Test
    public void getA4116() throws Exception {
        for (int i=0;i<5;i++) {
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
            String router = "/business/bind/TRANS_INFO_RECEIVE/v1.0";
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
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a7498728934\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/bind/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"4116\",\n" +
                    "        \"trans_id\": " + "\"" + transId + "\"" + " ,\n" +
                    "        \"trans_time\": " + "\"" + transTime + "\"" + " ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"W\"\n" +
                    "        ],\n" +
                    "        \"user_id\":  " + "\"" + userId + "\"" + " ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"青青员工下单4116\",\n" +
                    "        \"receipt_type\":\"小票类型\",\n" +
                    "        \"posId\": \"1111\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"iPhone1942349994\",\n" +
                    "                \"commodity_name\":\"苹果派1332\",\n" +
                    "                \"unit_price\": 200,\n" +
                    "                \"num\": 1\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"bananasss404345\",\n" +
                    "                \"commodity_name\":\"香蕉333根\",\n" +
                    "                \"unit_price\": 2,\n" +
                    "                \"num\": 1\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"Applesss45484234288885\",\n" +
                    "                \"commodity_name\":\"苹果111个\",\n" +
                    "                \"unit_price\": 3,\n" +
                    "                \"num\": 1\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

            JSONObject jsonObject = JSON.parseObject(str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);

            String post = HttpClientUtil.post(config);
            System.out.println(post);
        }
    }
//    public static  class  gatewayBO(){
//
//    }

    /**
     * ====================添加事件(结束时间为开始时间&结束时间大于开始时间)======================
     */
    @Test
    public void thingAddT1() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            //新增一个正常进行的添加事项
            String activity_description = "店庆店庆店庆店庆店庆";
            String activity_type = "NEW_COMMODITY";
            String start_date = dt.getHistoryDate(0); //今天日期;
            String end_date = dt.getHistoryDate(0);
            int code = md.activityAddV3(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
            checkArgument(code == 1000, "添加事项不成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("添加事件(结束时间为开始时间&结束时间大于开始时间)");
        }

    }


    /**
     * ====================列表页排序按照上个整点计算的今日到访人次排序(人次从大到小排序)======================
     */
    @Test
    public void storeListRank() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] shopType = {};
            String shopName = "";
            JSONObject response = md.patrolShopRealV3A(districtCode, shopType, shopName, shopManager, page, size);
            JSONArray storeList = response.getJSONArray("list");
            if (storeList.size() > 1) {
                for (int i = 0; i < storeList.size() - 1; i++) {
                    Integer realtimePv = storeList.getJSONObject(i).getInteger("realtime_pv");
                    Integer realtimePv1 = storeList.getJSONObject(i + 1).getInteger("realtime_pv");
                    if (realtimePv != null && realtimePv1 != null) {
                        //System.out.println("i=="+i+"   "+realtimePv+"    "+realtimePv1);
                        checkArgument(realtimePv >= realtimePv1, "人次多的数据为" + realtimePv + "人次少的数据为" + realtimePv1);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("列表页排序按照的今日到访人次从大到小排序");
        }
    }

    /**
     * ====================门店类型（单选、多选、全选、不选）======================
     */
    @Test
    public void storeType() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //多选
            String[] shopType = new String[]{"NORMAL", "COMMUNITY"};
            String shopName = "";
            int num = 0;
            int num1 = 0;
            JSONArray storeList = md.patrolShopRealV3A(districtCode, shopType, shopName, shopManager, page, size).getJSONArray("list");
            for (int i = 0; i < storeList.size(); i++) {
                checkArgument(storeList.getJSONObject(i).getString("type").equals("NORMAL") || storeList.getJSONObject(i).getString("type").equals("COMMUNITY"), "筛选栏多选数据有问题");
            }

            //单选
            String[] shopType1 = new String[]{"NORMAL"};
            JSONArray storeList1 = md.patrolShopRealV3A(districtCode, shopType1, shopName, shopManager, page, size).getJSONArray("list");
            for (int j = 0; j < storeList1.size(); j++) {
                checkArgument(storeList1.getJSONObject(j).getString("type").equals("NORMAL"), "筛选栏单选数据有问题");
            }

            //全选
            String[] shopType2 = new String[]{"NORMAL", "COMMUNITY", "PLAZA", "FLAGSHIP"};
            JSONArray storeList2 = md.patrolShopRealV3A(districtCode, shopType2, shopName, shopManager, page, size).getJSONArray("list");
            for (int m = 0; m < storeList2.size(); m++) {
                num++;
                checkArgument(storeList2.getJSONObject(m).getString("type").equals("PLAZA") || storeList2.getJSONObject(m).getString("type").equals("FLAGSHIP") || storeList2.getJSONObject(m).getString("type").equals("COMMUNITY") || storeList2.getJSONObject(m).getString("type").equals("NORMAL"), "筛选栏全选数据有问题");
            }

            //不选
            String[] shopType3 = new String[]{};
            JSONArray storeList3 = md.patrolShopRealV3A(districtCode, shopType3, shopName, shopManager, page, size).getJSONArray("list");
            for (int n = 0; n < storeList3.size(); n++) {
                num1++;
                checkArgument(storeList2.getJSONObject(n).getString("type").equals("PLAZA") || storeList2.getJSONObject(n).getString("type").equals("FLAGSHIP") || storeList2.getJSONObject(n).getString("type").equals("COMMUNITY") || storeList2.getJSONObject(n).getString("type").equals("NORMAL"), "筛选栏不选数据有问题");
            }
            checkArgument(num == num1, "全选：" + num + "不选：" + num1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店类型（单选、多选、全选、不选）");
        }
    }

    /**
     * ====================模糊搜索======================
     */
    @Test
    public void fuzzySearch() {
        String[] shopType = {};
        String shopName = "t";
        try {
            JSONArray storeList = md.patrolShopRealV3A(districtCode, shopType, shopName, shopManager, page, size).getJSONArray("list");
            for (int i = 0; i < storeList.size(); i++) {
                String string = storeList.getJSONObject(i).getString("name");
                if (string != null && string.contains(shopName)) {
                    checkArgument(true, "\"t\"的模糊搜索的结果为：" + string);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ====================新增角色======================
     */
    @Test
    public void role_add() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            String description = "青青测试给店长用的角色";
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);

            //新增一个角色
            JSONObject res = md.organizationRoleAdd(name, description, moduleId);
            Integer code = res.getInteger("code");

            Long role_id = md.organizationRolePage(name, page, size).getJSONArray("list").getJSONObject(0).getLong("role_id");
            checkArgument(code == 1000, "新增角色失败了");

            //编辑角色
            String name1 = "AUTOtest在编辑";
            Integer code1 = md.organizationRoleEdit(role_id, name1, description, moduleId).getInteger("code");

            checkArgument(code1 == 1000, "编辑角色的信息失败了");

            //列表中编辑过的角色是否已更新
            JSONArray list1 = md.organizationRolePage(name1, page, size).getJSONArray("list");
            String role_name = list1.getJSONObject(0).getString("role_name");

            checkArgument(name1.equals(role_name), "编辑过的角色没有更新在列表");


            //新建成功以后删除新建的账号
            if (name.equals(role_name)) {
                Integer code2 = md.organizationRoleDelete(role_id).getInteger("code");
                checkArgument(code2 == 1000, "删除角色:" + role_id + "失败了");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增删改查角色");
        }

    }

    /**
     * ====================新增角色(名称校验)======================
     */
    @Test
    public void role_add_work() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);

            //新增角色名称20个字的角色
            String description = "青青测试给店长自动化用的角色";
            JSONObject res = md.organizationRoleAdd("这是一个二十字的角色名称是的是的是的", description, moduleId);
            checkArgument(res.getInteger("code") == 1000, "角色名称为20个字，创建失败");

            //新增角色名称20个字英文+中文+数字的角色
            JSONObject res1 = md.organizationRoleAdd("这是一个二十字的角色名称AABB1111", description, moduleId);
            checkArgument(res1.getInteger("code") == 1000, "角色名称为中文+字母+数字，创建失败");

            //新增角色名称20个字英文+中文+数字+字符的角色
            JSONObject res2 = md.organizationRoleAdd("这是一个二十字的角色名称AABB11.。", description, moduleId);
            checkArgument(res2.getInteger("code") == 1000, "角色名称为中文+字母+数字+字符，创建失败");

            //新增角色名称21个字角色
            JSONObject res3 = md.organizationRoleAdd("这是一个二十一字的角色名称是的是的是的是的", description, moduleId);
            checkArgument(res3.getString("message").equals("角色名称需要在1-20个字内"), "角色名称为21个字，创建成功");

            //新增重复角色名称的角色
            JSONObject res4 = md.organizationRoleAdd("这是一个二十字的角色名称AABB11.。", description, moduleId);
            checkArgument(res4.getString("message").equals("新增角色异常:当前角色名称已存在！请勿重复添加"), "重复的角色名称，创建成功");


            //将账户使用次数为0的角色删除
            mds.deleteRole();


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增角色(名称校验)");
        }

    }

    /**
     * ====================新增角色(权限说明校验)======================
     */
    @Test
    public void role_add_work1() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);
            moduleId.add(10);

            //新增角色权限说明50个字的角色
            JSONObject res = md.organizationRoleAdd("auto名字3", "不是这是一个二十字的角色名称是的是的是的不是的的不是的好的好还需要二十个字现在是三十七了吧刚好五个字", moduleId);
            checkArgument(res.getInteger("code") == 1000, "角色权限说明为50个字，创建失败");

            //新增角色权限说明角色字英文+中文+数字的角色
            JSONObject res1 = md.organizationRoleAdd("auto名字1", "22一个二十字的角色名称AABB", moduleId);
            checkArgument(res1.getInteger("code") == 1000, "角色权限说明中文+字母+数字，创建失败");

            //新增角色权限说明角色英文+中文+数字+字符的角色
            JSONObject res2 = md.organizationRoleAdd("auto名字2", "这是一个二十字色名称BB11.。", moduleId);
            checkArgument(res2.getInteger("code") == 1000, "角色权限说明为中文+字母+数字+字符，创建失败");

            //新增角色权限说明51个字的角色
            JSONObject res3 = md.organizationRoleAdd("auto名字4", "不是这是一个二十字的角色名称是的是的是的不是的的不是的好的好还需要二十个字现在是三十七了吧刚好五个字多", moduleId);
            checkArgument(res3.getString("message").equals("角色名称需要在1-50个字内"), "角色权限说明为51个字，创建成功");

            mds.deleteRole();

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增角色(权限说明校验)");
        }

    }


    /**
     * ====================新增账号======================
     */
    @Test
    public void accountAdd_Email() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            String phone1 = "";
            List<String> r_dList = new ArrayList<String>();
            r_dList.add("296");


            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");

            Integer status = 1;
            String type = "EMAIL";
            //用EMAIL新增一个账号
            JSONObject res = md.organizationAccountAdd(name, email, phone1, r_dList, status, shop_list, type);
            Integer code = res.getInteger("code");

            checkArgument(code == 1000, "用emial:" + email + "新增一个账号失败了");

            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", email, "", "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");

            //新建成功以后删除新建的账号
            if (code == 1000) {
                Integer code1 = md.organizationAccountDelete(account).getInteger("code");
                checkArgument(code1 == 1000, "删除emial的账号:" + email + "失败了");

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("用邮箱新增账号");
        }

    }

    /**
     * ====================新增账号======================
     */
    @Test
    public void accountAdd_Phone() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            List<String> r_dList = new ArrayList<String>();//乱写的角色，要改！！！
            r_dList.add("296");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");

            Integer status = 1;
            String type = "PHONE";
            //用EMAIL新增一个账号
            JSONObject res = md.organizationAccountAdd(name, "", phone, r_dList, status, shop_list, type);
            Integer code = res.getInteger("code");

            checkArgument(code == 1000, "用手机号:" + phone + "新增一个账号失败了");

            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", "", phone, "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");


            //新建成功以后删除新建的账号
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            checkArgument(code1 == 1000, "删除手机号的账号:" + phone + "失败了");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("用手机号新增账号，删除账号");
        }

    }

    /**
     * ====================新增账号然后进行登录方式的修改======================
     */
    @Test
    public void accountEdit() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            String email1 = "";
            String phone1 = "";
            List<String> r_dList = new ArrayList<String>();
            r_dList.add("296");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");

            Integer status = 1;
            String type = "PHONE";
            String type1 = "EMAIL";
            //用EMAIL新增一个账号
            JSONObject res = md.organizationAccountAdd(name, email1, phone, r_dList, status, shop_list, type);
            Integer code = res.getInteger("code");
            checkArgument(code == 1000, "用手机号:" + phone + "新增一个账号失败了");

            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", "", phone, "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");
            //编辑刚刚新增的账号
            JSONObject result = md.organizationAccountEdit(account, name, email, phone1, r_dList, status, shop_list, type1);
            Integer code1 = result.getInteger("code");
            checkArgument(code1 == 1000, "编辑手机号:" + phone + "创建的账号失败，修改了登录方式为邮箱登录,邮箱输入为：" + email);

            //新建成功以后删除新建的账号

            Integer code2 = md.organizationAccountDelete(account).getInteger("code");
            checkArgument(code2 == 1000, "删除邮箱的账号:" + email + "失败了");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("用手机号新增账号，修改该账号的登录方式为邮箱登录");
        }

    }

    /**
     * ====================账户列表的筛选（单一查询）======================
     */
    //@Test
    public void accountPage_search() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = md.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String email = list.getJSONObject(0).getString("email");
            String phone = list.getJSONObject(1).getString("phone");

            //根据账号名称筛选
            JSONArray list1 = md.organizationAccountPage(name, "", "", "", "", "", page, size).getJSONArray("list");
            checkArgument(name.equals(list1.getJSONObject(0).getString("name")), "根据列表第一个账号名称" + name + "进行筛选的结果和筛选条件不一致");

            //根据email筛选
            JSONArray list2 = md.organizationAccountPage("", "", email, "", "", "", page, size).getJSONArray("list");
            checkArgument(email.equals(list2.getJSONObject(0).getString("email")), "根据email" + email + "进行筛选的结果和筛选条件不一致");

            //根据phone筛选
            JSONArray list3 = md.organizationAccountPage("", "", "", phone, "", "", page, size).getJSONArray("list");
            checkArgument(phone.equals(list3.getJSONObject(0).getString("phone")), "根据email" + phone + "进行筛选的结果和筛选条件不一致");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("账号列表的筛选（单一条件筛选）");
        }

    }


    /**
     * ====================设备管理摄像头的筛选======================
     */
    @Test(dataProvider = "STATUS", dataProviderClass = StoreFuncPackage.class)
    public void find_camera(String status) {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = md.device_page("", "", "", "", "CAMERA", page, size).getJSONArray("list");
            String device_name = list.getJSONObject(0).getString("device_name");
            String device_id = list.getJSONObject(0).getString("device_id");
            String shop_name = list.getJSONObject(0).getString("shop_name");

            JSONObject res = md.device_page(device_name, "", "", "", "CAMERA", page, size);
            JSONArray list1 = res.getJSONArray("list");
            String device_name1 = list.getJSONObject(0).getString("device_name");
            Integer total = res.getInteger("total");
            checkArgument(device_name.equals(device_name1) && total == 1, "用列表第一个设备的名称进行筛选:" + device_name + "报错了,筛选出来的结果为：" + total);

            JSONObject res1 = md.device_page("", "", device_id, "", "CAMERA", page, size);
            JSONArray list2 = res1.getJSONArray("list");
            String device_id1 = list.getJSONObject(0).getString("device_id");
            Integer total1 = res.getInteger("total");
            checkArgument(device_id.equals(device_id1) && total1 == 1, "用列表第一个设备ID进行筛选:" + device_id + "报错了");

            JSONObject res2 = md.device_page("", shop_name, "", "", "CAMERA", page, size);
            JSONArray list3 = res2.getJSONArray("list");
            String shop_name1 = list.getJSONObject(0).getString("shop_name");
            checkArgument(shop_name.equals(shop_name1), "用列表第一个设备所属门店进行筛选:" + shop_name + "报错了");

//            JSONObject res3 = md.device_page("", "", "", status, "CAMERA", page, size);
//            JSONArray list4 = res3.getJSONArray("list");
//            String status1 = "";
//            for (int i = 0; i < list4.size(); i++) {
//                status1 = list.getJSONObject(i).getString("status");
//                checkArgument(status.equals(status1), "用列表第一个设备所属状态进行筛选:" + status + "，出现了其他的结果：" + status1);
//            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("筛选摄像头是否有异常");
        }

    }

    /**
     * ====================收银风控筛选（单一筛选）======================
     */
    @Test
    public void cashier_page_search() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONObject res = md.cashier_page("", "", "", "", null, page, size);
            JSONArray list = res.getJSONArray("list");
            String shop_name = list.getJSONObject(0).getString("shop_name");
            //根据门店名称进行筛选
            JSONArray list1 = md.cashier_page(shop_name, "", "", "", null, page, size).getJSONArray("list");
            String shop_name1 = list1.getJSONObject(0).getString("shop_name");
            checkArgument(shop_name.contains(shop_name1), "根据门店名称" + shop_name + "搜索,没有查询到应有的结果");

            //根据累计风险事件排序(传0为降序；从大-小)
            JSONArray list2 = md.cashier_page("", "", "", "RISK", 0, page, size).getJSONArray("list");
            for (int i = 0; i < list2.size() - 1; i++) {
                if (list2.size() > 1) {
                    int risk_total1 = list2.getJSONObject(i).getInteger("risk_total");
                    int risk_total2 = list2.getJSONObject(i + 1).getInteger("risk_total");
                    checkArgument(risk_total1 >= risk_total2, "选择累计风险事件从大到小进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }
            }
            //根据累计风险事件排序(传1为升序；从小-大)
            JSONArray list3 = md.cashier_page("", "", "", "RISK", 1, page, size).getJSONArray("list");
            for (int i = 0; i < list3.size() - 1; i++) {
                if (list3.size() > 1) {
                    int risk_total1 = list3.getJSONObject(i).getInteger("risk_total");
                    int risk_total2 = list3.getJSONObject(i + 1).getInteger("risk_total");
                    checkArgument(risk_total1 <= risk_total2, "选择累计风险事件从小到大进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }

            //根据累计异常事件排序(传0为降序；从大-小)
            JSONArray list4 = md.cashier_page("", "", "", "ABNORMAL", 0, page, size).getJSONArray("list");
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
            JSONArray list5 = md.cashier_page("", "", "", "ABNORMAL", 1, page, size).getJSONArray("list");
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
            JSONArray list6 = md.cashier_page("", "", "", "NORMAL", 0, page, size).getJSONArray("list");
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
            JSONArray list7 = md.cashier_page("", "", "", "NORMAL", 1, page, size).getJSONArray("list");
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
            JSONArray list8 = md.cashier_page("", "", "", "PENDINGRISKS", 0, page, size).getJSONArray("list");
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
            JSONArray list9 = md.cashier_page("", "", "", "PENDINGRISKS", 1, page, size).getJSONArray("list");
            for (int i = 0; i < list9.size() - 1; i++) {
                if (list9.size() > 1) {
                    int pending_total1 = list9.getJSONObject(i).getInteger("pending_risks_total");
                    int pending_total2 = list9.getJSONObject(i + 1).getInteger("pending_risks_total");
                    checkArgument(pending_total1 <= pending_total2, "选择待处理事件从小到大进行排序出现了排序错误");
                } else {
                    System.err.println("该数组只有一个数据，无法进行排序");
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("收银风控筛选（单一筛选）和根据累计风险事件，累计正常事件，累计异常事件，累计待处理事件进行排序");
        }
    }


    /**
     * ====================收银追溯的筛选（单一查询）======================
     */
    @Test
    public void trace_backSearch() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");

            JSONArray list = md.cashier_traceBack(shop_id_01, "", "", page, size).getJSONArray("list");
            String order_id = list.getJSONObject(0).getString("order_id");
            Long date = list.getJSONObject(0).getLong("order_time");
            String date_01 = dt.timestampToDate("yyyy-MM-dd", date);
            //根据日期进行查询

            JSONArray list1 = md.cashier_traceBack(shop_id_01, date_01, "", page, size).getJSONArray("list");

            if (list.size() > 0) {
                for (int i = 0; i < list1.size(); i++) {
                    Long the_date = list1.getJSONObject(i).getLong("order_time");//the_date需要对时间戳进行转换，在调试接口时勿忘
                    String the_dates = dt.timestampToDate("yyyy-MM-dd", the_date);
                    checkArgument(date_01.contains(the_dates), "根据日期" + date + "搜索,没有查询到应有的结果");
                }

                //根据小票单号进行查询
                JSONArray list2 = md.cashier_traceBack(shop_id_01, "", order_id, page, size).getJSONArray("list");

                String order_id1 = list2.getJSONObject(0).getString("order_id");
                checkArgument(order_id.contains(order_id1), "根据小票单号" + order_id + "搜索,没有查询到应有的结果");
            } else {
                System.err.println("该数组下无数据，取不到小票单号");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("收银追溯的筛选（单一条件筛选）");
        }

    }

    /**
     * ====================收银风控事件的筛选（单一查询）======================
     */
    @Test
    public void risk_eventSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        try {

            JSONArray list = md.cashier_riskPage(shop_id_01, "", "", "", "", "", "", page, size).getJSONArray("list");
            String event_name = list.getJSONObject(0).getString("event_name");
            String order_id = list.getJSONObject(0).getString("order_id");
            String order_date = list.getJSONObject(0).getString("order_date").substring(0, 10);
            String order_date_01 = list.getJSONObject(0).getString("order_date");
            String handle_result = list.getJSONObject(0).getString("handle_result");
            String current_state = list.getJSONObject(0).getString("current_state");

            //根据事件名称进行筛选
            JSONArray list1 = md.cashier_riskPage(shop_id_01, event_name, "", "", "", "", "", page, size).getJSONArray("list");
            String event_name1 = list1.getJSONObject(0).getString("event_name");
            checkArgument(event_name.contains(event_name1), "根据日期" + event_name + "搜索,没有查询到应有的结果");

            //根据小票单号进行筛选
            JSONArray list2 = md.cashier_riskPage(shop_id_01, "", order_id, "", "", "", "", page, size).getJSONArray("list");
            String order_id1 = list2.getJSONObject(0).getString("order_id");
            checkArgument(order_id.contains(order_id1), "根据订单编号" + order_id + "搜索,没有查询到应有的结果");

            //根据收银日期进行筛选
            JSONArray list3 = md.cashier_riskPage(shop_id_01, "", "", order_date, "", "", "", page, size).getJSONArray("list");
                String order_date1 = list3.getJSONObject(0).getString("order_date");
                checkArgument(order_date_01.equals(order_date1), "根据收银日期" + order_date + "搜索,没有查询到应有的结果");



            //根据处理结果进行筛选
            JSONArray list4 = md.cashier_riskPage(shop_id_01, "", "", "", "", handle_result, "", page, size).getJSONArray("list");
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
     * ====================风控事项的处理======================
     */
    @Test
    public void trace_dealWith() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        try {
            JSONArray list = md.cashier_riskPage(shop_id_01, "", "", "", "", "", "PENDING", page, size).getJSONArray("list");
            if(list.size() != 0 ) {
                long id1 = list.getJSONObject(0).getInteger("id");
                String order1 = list.getJSONObject(0).getString("order_id");
                //将待处理的风控事件处理成异常
                int code2 = md.cashier_riskEventHandle(id1, 0, "该客户有刷单造假的嫌疑，请注意").getInteger("code");
                checkArgument(code2 == 1000, "将待处理事件中id为" + id1 + "处理成异常报错了" + code2);

                //查巡列表该事件的状态
                JSONArray list2 = md.cashier_riskPage(shop_id_01, "", order1, "", "", "", "", page, size).getJSONArray("list");
                String state_name1 = list2.getJSONObject(0).getString("state_name");
                String result_name1 = list2.getJSONObject(0).getString("result_name");
               // checkArgument(state_name1.equals("已处理") && result_name1.equals("异常"), "将待处理事件中小票单号为" + order1 + "处理成异常，但在风控事件列表中该事件的当前状态为：" + state_name1 + "处理结果：" + result_name1);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控事项的处理");
        }

    }
    /**
     * ====================风控事项的处理======================
     */
    @Test
    public void trace_dealWith1() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        try {
            JSONArray list = md.cashier_riskPage(shop_id_01, "", "", "", "", "", "PENDING", page, size).getJSONArray("list");
            if(list.size() != 0 ) {
                long id = list.getJSONObject(0).getInteger("id");
                String order = list.getJSONObject(0).getString("order_id");
                //将待处理的风控事件处理成正常
                int code1 = md.cashier_riskEventHandle(id, 1, "人工处理订单无异常").getInteger("code");
                checkArgument(code1 == 1000, "将待处理事件中小票单号为" + order + "处理成正常报错了" + code1);
                //查巡列表该事件的状态
                JSONArray list1 = md.cashier_riskPage(shop_id_01, "", order, "", "", "", "", page, size).getJSONArray("list");
                String state_name = list1.getJSONObject(0).getString("state_name");
                String result_name = list1.getJSONObject(0).getString("result_name");
                checkArgument(state_name.equals("已处理") && result_name.equals("正常"), "将待处理事件中小票单号为" + order + "处理成正常，但在风控事件列表中该事件的当前状态为：" + state_name + "处理结果：" + result_name);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控事项的处理");
        }

    }

    /**
     * ====================风控事项的处理（订单处理备注的字数）======================
     */
    @Test
    public void trace_dealMark() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        try {
            JSONArray list = md.cashier_riskPage(shop_id_01, "", "", "", "", "PENDING", "", page, size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");
            String order = list.getJSONObject(0).getString("order_id");

            //将待处理的风控事件处理成正常
            JSONObject res = md.cashier_riskEventHandle(id, 1, remark);
            checkArgument(res.getString("message").equals("成功"), "风控事项的处理，备注填写为500字，创建失败");

            //将待处理的风控事件处理成正常
            JSONObject res1 = md.cashier_riskEventHandle(id, 1, remarks);
            checkArgument(res1.getString("message").equals("备注不能超过500字"), "风控事项的处理，备注填写为501字，创建成功");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控事项的处理（订单处理备注的字数）");
        }

    }

    /**
     * ====================风控规则列表筛选（单一查询）======================
     */
    @Test
    public void rule_pageSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.risk_controlPage("", "", "", null, page, size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
            String shop_type = list.getJSONObject(0).getString("shop_type");
            Integer status = list.getJSONObject(0).getInteger("status");

            //根据规则名称进行筛选
            JSONArray list1 = md.risk_controlPage(name, "", "", null, page, size).getJSONArray("list");
            checkArgument(name.equals(list1.getJSONObject(0).getString("name")), "根据列表第一个的规则名称作为条件进行筛选搜索,没有查询到应有的结果");

            //根据规则类型进行筛选
            JSONArray list2 = md.risk_controlPage("", type, "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                String type1 = list2.getJSONObject(i).getString("type");
                checkArgument(type.equals(type1), "根据列表第一个的规则类型作为条件进行筛选搜索,没有查询到应有的结果");

            }
            //根据门店类型进行筛选
            JSONArray list3 = md.risk_controlPage("", "", shop_type, null, page, size).getJSONArray("list");
            for (int i = 0; i < list3.size(); i++) {
                String shop_type1 = list3.getJSONObject(i).getString("shop_type");
                checkArgument(shop_type.equals(shop_type1), "根据列表第一个的门店类型作为条件进行筛选搜索,没有查询到应有的结果");

            }
            //根据规则开关进行筛选
            JSONArray list4 = md.risk_controlPage("", "", "", status, page, size).getJSONArray("list");
            for (int i = 0; i < list4.size(); i++) {
                int status1 = list4.getJSONObject(i).getInteger("status");
                checkArgument(status == status1, "根据列表第一个的规则类型作为条件进行筛选搜索,没有查询到应有的结果");

            }


            //根据全部条件进行筛选
            JSONArray list5 = md.risk_controlPage(name, type, shop_type, status, page, size).getJSONArray("list");
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
     * ====================风控规则的增删查======================
     */
    @Test
    public void riskRuleAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个定检规则rule中的参数再调试时要进行修改
            String name = "QA_test";
            JSONObject para = new JSONObject();
            para.put("DAY_RANGE", 10);
            para.put("ORDER_QUANTITY_UPPER_LIMIT", 5);

            JSONObject rule = new JSONObject();
            rule.put("type", "CASHIER");
            rule.put("item", "RISK_SINGLE_MEMBER_ORDER_QUANTITY");
            rule.put("parameters", para);

            String shop_type = "NORMAL";
            Integer id = md.riskRuleAdd(name, shop_type, rule).getJSONObject("data").getInteger("id");
            ;

            checkArgument(id != null, "新增风控规则不成功，风控规则id：" + id);


            //删除刚刚新增的这个风控规则
            JSONObject res = md.risk_controlDelete(id);
            int code = res.getInteger("code");
            checkArgument(code == 1000, "新增的风控规则删除不成功" + code);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控规则的增删查");
        }

    }

    /**
     * ====================风控规则的新增（必填项内容校验）======================
     */
    @Test
    public void riskRuleCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个定检规则rule中的参数再调试时要进行修改
//            String name = "QA_test";
            JSONObject para = new JSONObject();
            para.put("DAY_RANGE", 10);
            para.put("ORDER_QUANTITY_UPPER_LIMIT", 5);

            JSONObject rule = new JSONObject();
            rule.put("type", "CASHIER");
            rule.put("item", "RISK_SINGLE_MEMBER_ORDER_QUANTITY");
            rule.put("parameters", para);

            String shop_type = "NORMAL";

            JSONObject res = md.riskRuleAdd("是一个名字啊123ABC.。", shop_type, rule);
            Integer id1 = res.getJSONObject("data").getInteger("id");
            checkArgument(res.getInteger("code") == 1000, "规则名称为字母中文数字+字符不超过20字，创建失败");
            //删除刚刚新增成功的风控规则ID
            md.risk_controlDelete(id1);

            JSONObject res1 = md.riskRuleAdd("我只想当一个二十字的规则名字如果不信你就", shop_type, rule);
            Integer id2 = res1.getJSONObject("data").getInteger("id");
            checkArgument(res1.getInteger("code") == 1000, "规则名称为20个字，创建失败");
            //删除刚刚新增成功的风控规则ID
            md.risk_controlDelete(id2);

            JSONObject res2 = md.riskRuleAdd("我只想当一个二十一字的规则名字如果不信你就数数.。", shop_type, rule);
            checkArgument(res2.getString("message").equals("规则名称不能超过20个字"), "规则名称为21个字，创建成功");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控规则的名称在1-20字之间，字母中文数字字符均可");
        }

    }

    /**
     * ====================风控告警列表的筛选（单一查询）======================
     */
    @Test
    public void alarm_pageSearch() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = md.alarm_page("", "", "", page, size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
            String shop_name = list.getJSONObject(0).getString("shop_name");

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list1 = md.alarm_page(name, "", "", page, size).getJSONArray("list");
            String name1 = list1.getJSONObject(0).getString("name");
            checkArgument(name.equals(name1), "根据告警名称" + name + "筛查，没有查询到相应的结果");


//            //根据列表第一个告警规则的类型进行筛查
//            JSONArray list2 = md.alarm_page("", type, "", page, size).getJSONArray("list");
//            for (int i = 0; i < list2.size(); i++) {
//                String type1 = list2.getJSONObject(i).getString("type");
//                checkArgument(type.equals(type1), "根据告警类型" + type + "筛查，没有查询到相应的结果");
//            }

            //根据列表第一个告警规则的门店名字进行筛查
            JSONArray list3 = md.alarm_page("", "", shop_name, page, size).getJSONArray("list");
            for (int i = 0; i < list3.size(); i++) {
                String shop_name1 = list3.getJSONObject(i).getString("shop_name");
                checkArgument(shop_name.equals(shop_name1), "根据门店名称" + shop_name + "筛查，没有查询到相应的结果");
            }

//            //根据列表第一个告警规则的信息进行筛查
//            JSONArray list4 = md.alarm_page(name, "", shop_name, page, size).getJSONArray("list");
//            checkArgument(list4.size() == 1, "根据列表第一个的信息作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警列表的筛选（单一条件筛选）");
        }

    }


    /**
     * ====================风控告警规则（增删改查）======================
     */
    @Test
    public void alarm_ruleAdd() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            //新增一个沉默时间为10分钟的告警
            String name = "q_test01";
            String type = "CASHIER";
            //风控告警规则的ID
            JSONArray rule_id = new JSONArray();
            rule_id.add(57);
            //告警接收者按照选择角色的ID
            JSONArray accept_id = new JSONArray();
            accept_id.add(3);

            String start_time = "08:00";
            String end_time = "16:00";
            String silent_time = "6400000";

            JSONObject res = md.alarm_ruleAdd(name, type, rule_id, accept_id, start_time, end_time, silent_time);
            int code = res.getInteger("code");
            checkArgument(code == 1000, "新增风控告警规则失败了，失败code=" + code);

            //在列表查找这个新增成功的规则
            JSONArray list = md.alarm_rulePage(name, type, "", null, page, size).getJSONArray("list");
            String name2 = list.getJSONObject(0).getString("name");
            int id1 = list.getJSONObject(0).getInteger("id");
            checkArgument(name2.equals(name), "新增风控告警规则，在列表找不到");


            //编辑风控告警规则
            String name1 = "edit——q_test01";
            JSONObject res1 = md.alarm_ruleEdit(id1, name1, type, rule_id, accept_id, start_time, end_time, silent_time);
            int code1 = res1.getInteger("code");
            checkArgument(code1 == 1000, "编辑风控告警规则失败了，失败code=" + code);

            //删除该风控告警规则
            JSONObject res2 = md.alarm_ruleDelete(id1);
            int code2 = res2.getInteger("code");
            checkArgument(code2 == 1000, "删除风控告警规则失败了，失败code=" + code);

//            //在列表查找这个新增成功的规则
//            JSONArray list1 = md.alarm_rulePage(name, type, "", null, page, size).getJSONArray("list");
//            if (list1 == null || list1.size() == 0) {
//                checkArgument(!list.isEmpty(), "风控告警规则删除成功，但是列表仍然可以找到该记录");
//            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警规则（增删改查）");
        }

    }

    /**
     * ====================风控告警规则（风控告警规则名称在1-20字之间，字母中文数字字符均可）======================
     */
    @Test
    public void alarm_ruleCheck() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            //新增一个沉默时间为10分钟的告警
//            String name = "q_test01";
            String type = "CASHIER";
            //风控告警规则的ID
            JSONArray rule_id = new JSONArray();
            rule_id.add(54);
            //告警接收者按照选择角色的ID
            JSONArray accept_id = new JSONArray();
            accept_id.add(296);

            String start_time = "08:00";
            String end_time = "16:00";
            String silent_time = "600000";

            JSONObject res = md.alarm_ruleAdd("我是一个二十字不到的名字", type, rule_id, accept_id, start_time, end_time, silent_time);
            checkArgument(res.getInteger("code").equals(1000), "规则名称不到20个字，创建失败");

            JSONObject res1 = md.alarm_ruleAdd("我是一个二十字不到的名字哈哈不信你可数数", type, rule_id, accept_id, start_time, end_time, silent_time);
            checkArgument(res1.getInteger("code").equals(1000), "规则名称刚好20个字，创建失败");

            JSONObject res2 = md.alarm_ruleAdd("我是一个名字AAA111.;/", type, rule_id, accept_id, start_time, end_time, silent_time);
            checkArgument(res2.getInteger("code").equals(1000), "规则名称带中文字母数字字符，创建失败");

            JSONObject res3 = md.alarm_ruleAdd("我是一个二十字不到的名字哈哈不信你再数数哈", type, rule_id, accept_id, start_time, end_time, silent_time);
            checkArgument(res3.getString("message").equals("风控告警规则名称不能超过20个字"), "风控告警规则为21个字，创建成功");

            //获取列表这四个刚刚新建成功的风控告警规则，然后再删除
            JSONArray list = md.alarm_rulePage("", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < 3; i++) {
                int id = list.getJSONObject(i).getInteger("id");
                md.alarm_ruleDelete(id);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警规则名称在1-20字之间，字母中文数字字符均可");
        }

    }

    /**
     * ====================风控告警规则的筛选（单一查询）======================
     */
    @Test
    public void alarm_ruleSearch() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = md.alarm_rulePage("", "", "", null, page, size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
//            String accept_role = list.getJSONObject(0).getString("accept_role");
            int status = list.getJSONObject(0).getInteger("status");

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list1 = md.alarm_rulePage(name, "", "", null, page, size).getJSONArray("list");
            String name1 = list1.getJSONObject(0).getString("name");
            checkArgument(name.equals(name1), "根据告警名称" + name + "筛查，没有查询到相应的结果");


            //根据列表第一个告警规则的类型进行筛查
            JSONArray list2 = md.alarm_rulePage("", type, "", null, page, size).getJSONArray("list");
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
            JSONArray list5 = md.alarm_rulePage("", "", "", status, page, size).getJSONArray("list");
            for (int i = 0; i < list5.size(); i++) {
                int status1 = list5.getJSONObject(i).getInteger("status");
                checkArgument(status == status1, "根据告警规则状态" + status + "筛查，没有查询到相应的结果");
            }

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list4 = md.alarm_rulePage(name, type, "", null, page, size).getJSONArray("list");
            checkArgument(list4.size() == 1, "根据列表第一个的信息作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警列表的筛选（单一条件筛选）");
        }

    }



    /**
     * ====================历史数据-区域关注度数据不为空======================
     */
   // @Test(dataProvider = "DATATYPE",dataProviderClass = StoreScenarioUtil.class)
    public void region_data(String dateType) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = md.regin_PUv(shop_id,dateType,"2020-02-1");
            JSONObject data = res.getJSONObject("data");
            JSONArray region_list = data.getJSONArray("region_data_day_list");
            for(int i=0;i<region_list.size();i++){
                Integer total_pv = region_list.getJSONObject(i).getInteger("total_pv");
                Integer total_uv = region_list.getJSONObject(i).getInteger("total_uv");
                checkArgument(total_pv!= null&total_uv!=null, "区域关注度存在数据为空，门店ID"+shop_id);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史数据-区域关注度数据不为空");
        }

    }


}
