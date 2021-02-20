package com.haisheng.framework.testng.bigScreen.xundianOnline.testShop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * @author : qingqing
 * @date :  2020/07/06 10:00
 */

public class StorePcCaseOnline extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    Integer status = 1;
    String type = "PHONE";
    String cycle_type = "RECENT_THIRTY";
    String district_code = "";
    String month = "";
    long shop_id = 14630;
    int startM = 2;
    String districtCode = "";
    String shopManager = "";
    int page = 1;
    int size = 50;
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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "青青";

//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 线上");

        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + md);

        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");


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
    public void getA() throws Exception {

        for (int i=0;i<2;i++){

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
            String uid = "uid_580f244a";//13260: uid_0ba743d8,14630:uid_580f244a
            String appId = "c30dcafc59c8";//13260: 672170545f50,14630:c30dcafc59c8
            String ak = "0d17651c55595b9b";//13260: 691ff41137d954f3,14630:0d17651c55595b9b
            String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
            String nonce = UUID.randomUUID().toString();
            String sk = "0ebe6128aedb44e0a7bd3f7a5378a7fc";//13260:d76f2d8a7846382f633c1334139767fe,14630:0ebe6128aedb44e0a7bd3f7a5378a7fc
            // java代码示例
            // java代码示例
            String requestUrl = "http://api.winsenseos.com/retail/api/data/biz";

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
            String transId = "QAtest_" + CommonUtil.getRandom(5);
            String transTime = ""+System.currentTimeMillis();
            String userId = "tester"+ CommonUtil.getRandom(6);
//        String s = "\"" + transId + "\"";
//        System.err.println(s);
            String str = "{\n" +
                    "  \"uid\": \"uid_580f244a\",\n" +
                    "  \"app_id\": \"c30dcafc59c8\",\n" +
                    "  \"request_id\": \"5d45a085-3774-4e0f-943e-ded373ca6a76\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"14630\",\n" +
                    "        \"trans_id\": " + "\"" + transId + "\"" + " ,\n" +
                    "        \"trans_time\": " + "\"" + transTime + "\"" + " ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"W\"\n" +
                    "        ],\n" +
                    "        \"user_id\":  " + "\""+userId+"\"" + " ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"青青\",\n" +
                    "        \"receipt_type\":\"小票类型\",\n" +
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"iPhone12\",\n" +
                    "                \"commodity_name\":\"苹果派12\",\n" +
                    "                \"unit_price\": 200,\n" +
                    "                \"num\": 1\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"banana\",\n" +
                    "                \"commodity_name\":\"香蕉2根\",\n" +
                    "                \"unit_price\": 2,\n" +
                    "                \"num\": 1\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"Apple\",\n" +
                    "                \"commodity_name\":\"苹果16个\",\n" +
                    "                \"unit_price\": 3,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
//        System.err.println(str);

            JSONObject jsonObject = JSON.parseObject(str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);

            String post = HttpClientUtil.post(config);
            System.out.println(post);
        }
    }

    /**
     *生成交易订单
     **/
    @Test
    public void getA1() throws Exception {

        for (int i=0;i<1;i++){

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
            String uid = "uid_0ba743d8";//13260: uid_0ba743d8,14630:uid_580f244a
            String appId = "672170545f50";//13260: 672170545f50,14630:c30dcafc59c8
            String ak = "691ff41137d954f3";//13260: 691ff41137d954f3,14630:0d17651c55595b9b
            String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
            String nonce = UUID.randomUUID().toString();
            String sk = "d76f2d8a7846382f633c1334139767fe";//13260:d76f2d8a7846382f633c1334139767fe,14630:0ebe6128aedb44e0a7bd3f7a5378a7fc
            // java代码示例
            // java代码示例
            String requestUrl = "http://api.winsenseos.com/retail/api/data/biz";

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
            String transId = "QAtest_" + CommonUtil.getRandom(5);
            String transTime = ""+System.currentTimeMillis();
            String userId = "tester"+ CommonUtil.getRandom(6);
//        String s = "\"" + transId + "\"";
//        System.err.println(s);
            String str = "{\n" +
                    "  \"uid\": \"uid_0ba743d8\",\n" +
                    "  \"app_id\": \"672170545f50\",\n" +
                    "  \"request_id\": \"5d45a085-3774-4e0f-943e-ded377823\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"13260\",\n" +
                    "        \"trans_id\": " + "\"" + transId + "\"" + " ,\n" +
                    "        \"trans_time\": " + "\"" + transTime + "\"" + " ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"W\"\n" +
                    "        ],\n" +
                    "        \"user_id\":  " + "\""+userId+"\"" + " ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"2387979\",\n" +
                    "        \"memberName\":\"无人风控\",\n" +
                    "        \"receipt_type\":\"小票类型\",\n" +
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"iPhone122\",\n" +
                    "                \"commodity_name\":\"苹果派12\",\n" +
                    "                \"unit_price\": 200,\n" +
                    "                \"num\": 1\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"banana2\",\n" +
                    "                \"commodity_name\":\"香蕉2根\",\n" +
                    "                \"unit_price\": 2,\n" +
                    "                \"num\": 1\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"Apple2\",\n" +
                    "                \"commodity_name\":\"苹果16个\",\n" +
                    "                \"unit_price\": 3,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
//        System.err.println(str);

            JSONObject jsonObject = JSON.parseObject(str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);

            String post = HttpClientUtil.post(config);
            System.out.println(post);
        }
    }
    /**
     * ====================门店类型（单选、多选、全选、不选）======================
     */
    @Test
    public void storeType() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //多选
            String district_code = "";
            String[] shopType = new String[]{"NORMAL", "COMMUNITY"};
            String shopName = "";
            String shopManager = "";
            int page = 1;
            int size = 10;
            int num = 0;
            int num1 = 0;
            JSONArray storeList = md.patrolShopRealV3A(district_code, shopType, shopName, shopManager, page, size).getJSONArray("list");
            for (int i = 0; i < storeList.size(); i++) {
                Preconditions.checkArgument(storeList.getJSONObject(i).getString("type").equals("NORMAL") || storeList.getJSONObject(i).getString("type").equals("COMMUNITY"), "筛选栏多选数据有问题");
            }

            //单选
            String[] shopType1 = new String[]{"NORMAL"};
            JSONArray storeList1 = md.patrolShopRealV3A(district_code, shopType1, shopName, shopManager, page, size).getJSONArray("list");
            for (int j = 0; j < storeList1.size(); j++) {
                Preconditions.checkArgument(storeList1.getJSONObject(j).getString("type").equals("NORMAL"), "筛选栏单选数据有问题");
            }

            //全选
            String[] shopType2 = new String[]{"NORMAL", "COMMUNITY", "PLAZA", "FLAGSHIP"};
            JSONArray storeList2 = md.patrolShopRealV3A(district_code, shopType2, shopName, shopManager, page, size).getJSONArray("list");
            for (int m = 0; m < storeList2.size(); m++) {
                num++;
                System.out.println("num为  " + num);
                Preconditions.checkArgument(storeList2.getJSONObject(m).getString("type").equals("PLAZA") || storeList2.getJSONObject(m).getString("type").equals("FLAGSHIP") || storeList2.getJSONObject(m).getString("type").equals("COMMUNITY") || storeList2.getJSONObject(m).getString("type").equals("NORMAL"), "筛选栏全选数据有问题");
            }

            //不选
            String[] shopType3 = new String[]{};
            JSONArray storeList3 = md.patrolShopRealV3A(district_code, shopType3, shopName, shopManager, page, size).getJSONArray("list");
            for (int n = 0; n < storeList3.size(); n++) {
                num1++;
                //System.out.println("num1为  "+num1);
                Preconditions.checkArgument(storeList2.getJSONObject(n).getString("type").equals("PLAZA") || storeList2.getJSONObject(n).getString("type").equals("FLAGSHIP") || storeList2.getJSONObject(n).getString("type").equals("COMMUNITY") || storeList2.getJSONObject(n).getString("type").equals("NORMAL"), "筛选栏不选数据有问题");
            }
            Preconditions.checkArgument(num == num1, "全选：" + num + "不选：" + num1);
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
        String district_code = "";
        String[] shopType = {};
        String shopName = "t";
        String shopManager = "";
        int page = 1;
        int size = 10;
        try {
            JSONArray storeList = md.patrolShopRealV3A(district_code, shopType, shopName, shopManager, page, size).getJSONArray("list");
            for (int i = 0; i < storeList.size(); i++) {
                String string = storeList.getJSONObject(i).getString("name");
                if (string != null && string.contains(shopName)) {
                    Preconditions.checkArgument(true, "\"t\"的模糊搜索的结果为：" + string);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            saveData("门店筛选栏--模糊搜索");
        }


    }

    /**
     * ====================新增角色======================
     */
    @Test
    public void role_add() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");

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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
            md.deleteRole();


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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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

            md.deleteRole();

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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String phone1 = "";
            List<String> r_dList = new ArrayList<String>();
            r_dList.add("107");


            List<String> shop_list = new ArrayList<String>();
            shop_list.add("14630");

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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            List<String> r_dList = new ArrayList<String>();//乱写的角色，要改！！！
            r_dList.add("107");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("14630");

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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String email1 = "";
            String phone1 = "";
            List<String> r_dList = new ArrayList<String>();
            r_dList.add("107");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("14630");

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
    @Test
    public void accountPage_search() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String email = list.getJSONObject(0).getString("email");
            String phone = list.getJSONObject(2).getString("phone");

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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
        md.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");
        try {

            JSONArray list = md.cashier_traceBack(shop_id, "", "", page, size).getJSONArray("list");
            String order_id = list.getJSONObject(0).getString("order_id");
            Long date = list.getJSONObject(0).getLong("order_time");
            String date_01 = dt.timestampToDate("yyyy-MM-dd", date);
            //根据日期进行查询

            JSONArray list1 = md.cashier_traceBack(shop_id, date_01, "", page, size).getJSONArray("list");

            if (list.size() > 0) {
                for (int i = 0; i < list1.size(); i++) {
                    Long the_date = list1.getJSONObject(i).getLong("order_time");//the_date需要对时间戳进行转换，在调试接口时勿忘
                    String the_dates = dt.timestampToDate("yyyy-MM-dd", the_date);
                    checkArgument(date_01.contains(the_dates), "根据日期" + date + "搜索,没有查询到应有的结果");
                }

                //根据小票单号进行查询
                JSONArray list2 = md.cashier_traceBack(shop_id, "", order_id, page, size).getJSONArray("list");
                String order_id1 = list2.getJSONObject(0).getString("order_id");
                checkArgument(order_id.equals(order_id1), "根据小票单号" + order_id + "搜索,没有查询到应有的结果");
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
        try {
            md.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");
            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "", "", page, size).getJSONArray("list");
            String event_name = list.getJSONObject(0).getString("event_name");
            String order_id = list.getJSONObject(0).getString("order_id");
            String order_date = list.getJSONObject(0).getString("order_date").substring(0, 10);
            String order_date_01 = list.getJSONObject(0).getString("order_date");
            String handle_result = list.getJSONObject(0).getString("handle_result");
            String current_state = list.getJSONObject(0).getString("current_state");

            //根据事件名称进行筛选
            JSONArray list1 = md.cashier_riskPage(shop_id, event_name, "", "", "", "", "", page, size).getJSONArray("list");
            String event_name1 = list1.getJSONObject(0).getString("event_name");
            checkArgument(event_name.contains(event_name1), "根据日期" + event_name + "搜索,没有查询到应有的结果");

            //根据小票单号进行筛选
            JSONArray list2 = md.cashier_riskPage(shop_id, "", order_id, "", "", "", "", page, size).getJSONArray("list");
            String order_id1 = list1.getJSONObject(0).getString("order_id");
            checkArgument(order_id.contains(order_id1), "根据订单编号" + order_id + "搜索,没有查询到应有的结果");

            //根据收银日期进行筛选
            JSONArray list3 = md.cashier_riskPage(shop_id, "", "", order_date, "", "", "", page, size).getJSONArray("list");
                String order_date1 = list3.getJSONObject(0).getString("order_date");
                checkArgument(order_date_01.equals(order_date1), "根据收银日期" + order_date + "搜索,没有查询到应有的结果");



            //根据处理结果进行筛选
            JSONArray list4 = md.cashier_riskPage(shop_id, "", "", "", "", handle_result, "", page, size).getJSONArray("list");
            for (int i = 0; i < list4.size(); i++) {
                String handle_result1 = list4.getJSONObject(i).getString("handle_result");
                checkArgument(handle_result.contains(handle_result1), "根据订单编号" + handle_result + "搜索,没有查询到应有的结果");
            }

            //根据当前状态进行筛选
            JSONArray list5 = md.cashier_riskPage(shop_id, "", "", "", "", "", current_state, page, size).getJSONArray("list");
            for (int i = 0; i < list5.size(); i++) {
                String current_state1 = list5.getJSONObject(i).getString("current_state");
                checkArgument(current_state.contains(current_state1), "根据当前状态" + current_state + "搜索,没有查询到应有的结果");
            }

            //根据全部结果进行筛选
            JSONArray list6 = md.cashier_riskPage(shop_id, event_name, order_id, order_date, "", "", current_state, page, size).getJSONArray("list");

            checkArgument(list6.size() == 1, "根据列表第一个内容作为条件进行筛选搜索,没有查询到应有的结果");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("收银风控事件的筛选（单一条件筛选）");
        }

    }

    /**
     * ====================风控规则列表筛选（单一查询）======================
     */
    @Test
    public void rule_pageSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.alarm_page("", "", "", page, size).getJSONArray("list");
            String name = list.getJSONObject(0).getString("name");
            String type = list.getJSONObject(0).getString("type");
            String shop_name = list.getJSONObject(0).getString("shop_name");

            //根据列表第一个告警规则的名字进行筛查
            JSONArray list1 = md.alarm_page(name, "", "", page, size).getJSONArray("list");
            String name1 = list1.getJSONObject(0).getString("name");
            checkArgument(name.equals(name1), "根据告警名称" + name + "筛查，没有查询到相应的结果");


            //根据列表第一个告警规则的类型进行筛查
            JSONArray list2 = md.alarm_page("", type, "", page, size).getJSONArray("list");
                String type1 = list2.getJSONObject(0).getString("type");
                checkArgument(type.equals(type1), "根据告警类型" + type + "筛查，没有查询到相应的结果");


            //根据列表第一个告警规则的门店名字进行筛查
            JSONArray list3 = md.alarm_page("", "", shop_name, page, size).getJSONArray("list");
                String shop_name1 = list3.getJSONObject(0).getString("shop_name");
                checkArgument(shop_name.equals(shop_name1), "根据门店名称" + shop_name + "筛查，没有查询到相应的结果");


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
     * ====================风控事项的处理为正常======================
     */
    @Test
    public void trace_dealWith_true() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "", "PENDING", page, size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");
            String order = list.getJSONObject(0).getString("order_id");

            //将待处理的风控事件处理成正常
            int code1 = md.cashier_riskEventHandle(id, 1, "人工处理订单无异常").getInteger("code");
            checkArgument(code1 == 1000, "将待处理事件中小票单号为" + order + "处理成正常报错了" + code1);
//            //查巡列表该事件的状态
//            JSONArray list1 = md.cashier_riskPage(shop_id, "", order, "", "", "", "", page, size).getJSONArray("list");
//            String state_name = list1.getJSONObject(0).getString("state_name");
//            String result_name = list1.getJSONObject(0).getString("result_name");
//            checkArgument(state_name.equals("已处理") && result_name.equals("正常"), "将待处理事件中小票单号为" + order + "处理成正常，但在风控事件列表中该事件的当前状态为：" + state_name + "处理结果：" + result_name);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控事项的处理");
        }

    }
    /**
     * ====================风控事项的处理成异常======================
     */
    @Test
    public void trace_dealWith_false() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "", "PENDING", page, size).getJSONArray("list");
            long id1 = list.getJSONObject(1).getInteger("id");
            String order1 = list.getJSONObject(1).getString("order_id");

            //将待处理的风控事件处理成异常
            int code2 = md.cashier_riskEventHandle(id1, 0, "该客户有刷单造假的嫌疑，请注意").getInteger("code");
            checkArgument(code2 == 1000, "将待处理事件中id为" + id1 + "处理成异常报错了" + code2);

//            //查巡列表该事件的状态
//            JSONArray list2 = md.cashier_riskPage(shop_id, "", order1, "", "", "", "", page, size).getJSONArray("list");
//            String state_name1 = list2.getJSONObject(0).getString("state_name");
//            String result_name1 = list2.getJSONObject(0).getString("result_name");
//            checkArgument(state_name1.equals("已处理") && result_name1.equals("异常"), "将待处理事件中小票单号为" + order1 + "处理成异常，但在风控事件列表中该事件的当前状态为：" + state_name1 + "。处理结果：" + result_name1);


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
   // @Test
    public void trace_dealMark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "PENDING", "", page, size).getJSONArray("list");
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
     * ====================风控告警规则（增删改查）======================
     */
    @Test
    public void alarm_ruleAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //新增一个沉默时间为10分钟的告警
//            String name = "q_test01";
            String type = "CASHIER";
            //风控告警规则的ID
            JSONArray rule_id = new JSONArray();
            rule_id.add(1);
            //告警接收者按照选择角色的ID
            JSONArray accept_id = new JSONArray();
            accept_id.add(1);

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
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
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
     * ====================收银风控事件的数据一致性（涉当前状态为【待处理】==响应时长；处理人；处理结果；备注为空）========================
     */
    @Test
    public void orderInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "PENDING", "", page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String order_id = list.getJSONObject(i).getString("order_id");
                String response_time = list.getJSONObject(i).getString("response_time");
                String handler = list.getJSONObject(i).getString("handler");
                String remarks = list.getJSONObject(i).getString("remarks");
                String handle_result = list.getJSONObject(i).getString("handle_result");
                Preconditions.checkArgument(response_time.isEmpty(), "待处理的事件：" + order_id + "的响应时间不为空");
                Preconditions.checkArgument(handler.isEmpty(), "待处理的事件：" + order_id + "的处理人不为空");
                Preconditions.checkArgument(remarks.isEmpty(), "待处理的事件：" + order_id + "的备注不为空");
                Preconditions.checkArgument(handle_result.isEmpty(), "待处理的事件：" + order_id + "的处理结果不为空");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("涉当前状态为【待处理】==响应时长；处理人；处理结果；备注为空");
        }

    }

    /**
     * ====================风控规则的数据一致性（新增一个规则==更新者为新增该风控规则的人员;列表风险规则+1;）========================
     */
    @Test
    public void creatRuleInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            int total = md.risk_controlPage("", "", "", null, page, size).getInteger("total");

            //新建一个风控规则rule中的参数再调试时要进行修改
            String name = "QA_test01";
            JSONObject para = new JSONObject();
            para.put("DAY_RANGE", 10);
            para.put("ORDER_QUANTITY_UPPER_LIMIT", 5);

            JSONObject rule = new JSONObject();
            rule.put("type", "CASHIER");
            rule.put("item", "RISK_SINGLE_MEMBER_ORDER_QUANTITY");
            rule.put("parameters", para);

            String shop_type = "NORMAL";

            int id = md.riskRuleAdd(name, shop_type, rule).getJSONObject("data").getInteger("id");
            int total1 = md.risk_controlPage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total == 1, "新增一个规则以后，新增前后风控规则列表总数相差不为1，新增前：" + total + "新增后：" + total1);

            //删除刚刚新建的这个风控规则
            md.risk_controlDelete(id);
            int total2 = md.risk_controlPage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total2 == 1, "删除一个规则以后，删除前后列表的总数相差不为1，删除前：" + total1 + "删除后：" + total2);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控规则的数据一致性（新增一个规则，列表+1/删除一个规则，列表-1）");
        }

    }

    /**
     * ====================风控告警列表的数据一致性（最新告警时间>=首次告警时间）========================
     */
    @Test
    public void ruleSwitch() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.alarm_page("", "", "", page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String first_alarm_time = list.getJSONObject(i).getString("first_alarm_time");
                String last_alarm_time = list.getJSONObject(i).getString("last_alarm_time");
                boolean result = new DateTimeUtil().timeDiff(first_alarm_time, last_alarm_time, "yyyy-MM-dd HH:mm:ss") >= 0;
//                System.err.println(result);
                Preconditions.checkArgument(result = true, "最新告警时间<首次告警时间，首次告警时间：" + first_alarm_time + "最新告警时间" + last_alarm_time);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警列表的数据一致性（最新告警时间>=首次告警时间）");
        }

    }


    /**
     * ====================风控告警规则的数据一致性（新增一个风控告警规则，列表+1；删除一个规则，列表-1；）========================
     */
    @Test
    public void risk_ruleDataInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {

            int total = md.alarm_rulePage("", "", "", null, page, size).getInteger("total");
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

            md.alarm_ruleAdd(name, type, rule_id, accept_id, start_time, end_time, silent_time);
            int total1 = md.alarm_rulePage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total == 1, "新增一个风控告警规则以后，新增前后风控规则列表总数相差不为1，新增前：" + total + "新增后：" + total1);


            //在列表查找这个新增成功的规则
            JSONArray list = md.alarm_rulePage(name, type, "", null, page, size).getJSONArray("list");
            String name2 = list.getJSONObject(0).getString("name");
            int id = list.getJSONObject(0).getInteger("id");
            checkArgument(name2.equals(name), "新增风控告警规则，在列表找不到");

            //删除刚刚新增的风控告警规则
            md.alarm_ruleDelete(id);
            int total2 = md.alarm_rulePage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total2 == 1, "删除一个风控规则以后，删除前后列表的总数相差不为1，删除前：" + total1 + "删除后：" + total2);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警规则的数据一致性（新增一个规则，列表+1/删除一个规则，列表-1）");
        }

    }

    /**
     * ====================今日到访人数<=今天各个时间段内到访人数的累计======================
     */
    @Test
    public void realTimeTotal() {
        logger.logCaseStart(caseResult.getCaseName());

        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = md.realTimeShopTotalV3((long) 14630l).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");

            //获取今日各个时间段内到访得人数且相加
            JSONArray eTlist = md.realTimeShopPvV3((long) 14630l).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer todayUv = eTlist.getJSONObject(i).getInteger("today_uv");
                todayUv = todayUv != null ? todayUv : 0;
                count += todayUv;

            }
            Preconditions.checkArgument(uv <= count, "今日到访人数=" + uv + "今天各个时间段内到访人数的累计=" + count);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("今日到访人数<=今天各个时间段内到访人数的累计");
        }

    }


    /**
     * ====================过店客群总人次==各个门的过店人次之和======================
     */
    @Test
    public void passByTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int pv2 = pass_by.get("pv2");
            int uv1 = pass_by.get("uv1");
            int uv2 = pass_by.get("uv2");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pvIn1 = interest.get("pv1");
            int uvIn1 = interest.get("uv1");

            int passPv = pv2 + pvIn1;
            int passUv = uv2 + uvIn1;
            Preconditions.checkArgument(pv1 == passPv, "过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2 + "+ 兴趣客群总人次" + pvIn1);
            Preconditions.checkArgument(uv1 == passUv, "过店客群总人数=" + uv1 + "各个门的过店人次之数=" + uv2 + "兴趣客群总人次" + uvIn1);
//            Preconditions.checkArgument(pv1 == pv2, "过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2);
//            Preconditions.checkArgument(uv1 == uv2, "过店客群总人数=" + uv1 + "各个门的过店人次之数=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("过店客群总人次==各个门的过店人次之和|过店客群总人数==各个门的过店人次之数");
        }

    }

    //获取各个客群漏斗数据的方法
    private Map<String, Integer> getCount(JSONArray ldlist, String type) {
        int pv1 = 0;
        int uv1 = 0;
        int pv2 = 0;
        int uv2 = 0;
        for (int i = 0; i < ldlist.size(); i++) {
            JSONObject jsonObject = ldlist.getJSONObject(i);
            if (jsonObject != null) {
                String type1 = jsonObject.getString("type");
                if (type.equals(type1)) {
                    pv1 = jsonObject.getInteger("pv");
                    uv1 = jsonObject.getInteger("uv");
                    JSONArray detail = jsonObject.getJSONArray("detail");
                    if (CollectionUtils.isNotEmpty(detail)) {
                        for (int j = 0; j < detail.size(); j++) {
                            Integer pv = detail.getJSONObject(j).getInteger("pv");
                            Integer uv = detail.getJSONObject(j).getInteger("uv");
                            pv2 += pv != null ? pv : 0;
                            uv2 += uv != null ? uv : 0;
                        }
                    } else {
                        pv2 = pv1;
                        uv2 = uv1;
                    }

                }
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("pv1", pv1);
        result.put("uv1", uv1);
        result.put("pv2", pv2);
        result.put("uv2", uv2);
        return result;
    }

    /**
     * ====================进店客群总人次==各个门的进店人次之和======================
     */
    @Test
    public void enterTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv1 = enter.get("pv1");
            int pv2 = enter.get("pv2");
            int uv1 = enter.get("uv1");
            int uv2 = enter.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "进店客群总人次=" + pv1 + "各个门的进店人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "进店客群总人数=" + uv1 + "各个门的进店人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("进店客群总人次==各个门的进店人次之和");
        }

    }

    /**
     * ====================兴趣客群总人次==各个门的进店人次之和======================
     */
    @Test
    public void interestTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv1 = interest.get("pv1");
            int pv2 = interest.get("pv2");
            int uv1 = interest.get("uv1");
            int uv2 = interest.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "兴趣客群总人次=" + pv1 + "各个门的兴趣人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "兴趣客群总人数=" + uv1 + "各个门的兴趣人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("兴趣客群总人次==各个门的进店人次之和");
        }

    }

    /**
     * ====================交易客群总人次==会员+非会员的交易pv之和======================
     */
    @Test
    public void dealTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int pv1 = deal.get("pv1");
            int pv2 = deal.get("pv2");
            int uv1 = deal.get("uv1");
            int uv2 = deal.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "交客客群总人次=" + pv1 + "会员+非会员的人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "交易客群总人数=" + uv1 + "会员+非会员的人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("交易客群总人次==会员+非会员的交易pv之和");
        }

    }

    /**
     * ====================过店客群pv>=兴趣客群pv>=进店客群pv======================
     */
    @Test
    public void enterInterPass() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv2 = interest.get("pv1");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv3 = enter.get("pv1");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int pv4 = enter.get("pv1");
            boolean result = false;
            if (pv1 <= pv2 && pv2 <= pv3 && pv3 <= pv4) {
                result = true;
            }

            Preconditions.checkArgument(result = true, "过店客群" + pv1 + "兴趣客群pv" + pv2 + "进店客群" + pv3 + "进店客群" + pv4);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("过店客群pv>=兴趣客群pv>=进店客群pv");
        }

    }


    /**
     * ====================消费者到店趋势中各天pv累计==到店客群总人次======================
     */
    @Test()
    public void mpvTotals() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            int pvValues = 0;
            //获取到店趋势数据
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            for (int i = 0; i < trend_list.size(); i++) {
                JSONObject jsonObject = trend_list.getJSONObject(i);
                if (jsonObject != null) {
                    Integer pv = jsonObject.getInteger("pv");
                    if (pv != null) {
                        pvValues += pv;//到店趋势中每天的pv累加
                    }

                }
            }

            //获取进店客群总人次
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "ENTER");
            int value1 = pass_by.get("pv1");


            Preconditions.checkArgument(pvValues == value1, "消费者到店趋势中各天pv累计=" + pvValues + "到店客群总人数=" + value1);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("消费者到店趋势中各天pv累计==到店客群总人次");

        }

    }

    /**
     * ====================各个客群总人次==到店时段分布中各个时段pv累计======================
     */
    @Test()
    public void mpvTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {

            //获取交易客群总人次
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int value1 = deal.get("pv1");


            //获取进店客群总人次
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value2 = enter.get("pv1");

            //获取兴趣客群总人次
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value3 = interest.get("pv1");


            //获取过店客群总人次
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int value4 = pass_by.get("pv1");


            int times1 = 0;
            int times2 = 0;
            int times3 = 0;
            int times4 = 0;
            //获取各个客群时段分布的总和
            int count = 30;
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            for (int i = 0; i < showList.size(); i++) {
                Integer deal_pv = showList.getJSONObject(i).getInteger("deal_pv");
                Integer enter_pv = showList.getJSONObject(i).getInteger("enter_pv");
                Integer interest_pv = showList.getJSONObject(i).getInteger("interest_pv");
                Integer pass_pv = showList.getJSONObject(i).getInteger("pass_pv");
                //获取交易客群的各个时段的数据（交易人次*有数据的天数的累加）
                if (deal_pv != null && deal_pv != 0) {
                    int deal_pv1 = deal_pv * count;
                    times1 += deal_pv1;
                }
                if (enter_pv != null && enter_pv != 0) {
                    int enter_pv1 = enter_pv * count;
                    times2 += enter_pv1;
                }
                if (interest_pv != null && interest_pv != 0) {
                    int interest_pv1 = interest_pv * count;
                    times3 += interest_pv1;
                }
                if (pass_pv != null && pass_pv != 0) {
                    int pass_pv1 = pass_pv * count;
                    times4 += pass_pv1;
                }
            }
            int result1 = Math.abs(value1 - times1);
            int result2 = Math.abs(value2 - times2);
            int result3 = Math.abs(value3 - times3);
            int result4 = Math.abs(value4 - times4);
            Preconditions.checkArgument(result1 <= 720, "交易客群总人次=" + value1 + "时段分布中各个时段交易pv累计=" + times1);
            Preconditions.checkArgument(result2 <= 720, "进店客群总人次=" + value1 + "时段分布中各个时段进店pv累计=" + times2);
            Preconditions.checkArgument(result3 <= 720, "兴趣客群总人次=" + value1 + "时段分布中各个时段兴趣pv累计=" + times3);
            Preconditions.checkArgument(result4 <= 720, "过店客群总人次=" + value1 + "时段分布中各个时段过店pv累计=" + times4);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("到店客群总人次==到店时段分布中各个时段pv累计");

        }

    }

    /**
     * ====================吸引率==兴趣客群pv/过店客群pv|进店率==进店客群pv/兴趣客群pv======================
     */
    @Test
    public void attractRate() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //获取过店客群总人次
            JSONObject res = md.historyShopConversionV3(shop_id, cycle_type, month);
            String interestRate = res.getString("interest_percentage");
            String enterRate = res.getString("enter_percentage");
            JSONArray ldlist = res.getJSONArray("list");


            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int value1 = pass_by.get("pv1");//过店客群PV
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value2 = interest.get("pv1");//兴趣客群PV
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value3 = enter.get("pv1");//进店客群PV
            DecimalFormat decimalFormat = new DecimalFormat("0.00%");
            String rate = "";

            if (value2 / value1 == 1) {
                rate = "100%";
            } else {
                rate = decimalFormat.format(new BigDecimal(value2).divide(new BigDecimal(value1), 4, BigDecimal.ROUND_HALF_UP));//吸引率计算
            }
            String rate1 = "";
            if (value3 / value2 == 1) {
                rate1 = "100%";
            } else {
                rate1 = decimalFormat.format(new BigDecimal(value3).divide(new BigDecimal(value2), 4, BigDecimal.ROUND_HALF_UP)); //进店率计算
            }
            boolean reslut = false;
            if (value1 >= value2 && value2 >= value3) {
                reslut = true;
            }


            Preconditions.checkArgument((interestRate.equals(rate)), "吸引率=" + interestRate + "兴趣客群pv/过店客群=" + rate);
            Preconditions.checkArgument((enterRate.equals(rate1)), "进店率=" + interestRate + "进店客群pv/兴趣客群pv=" + rate);
            Preconditions.checkArgument((reslut = true), "过店客群pv>=兴趣客群pv>=进店客群不成立");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("吸引率==兴趣客群pv/过店客群pv");
        }
    }

    /**
     * ====================日均客流==所选时间段内的日均客流uv======================
     */
    @Test
    public void averageFlowTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            int values = 0;
            int values1 = 0;//值不为Null的个数，求平均值时用
            JSONObject res = md.historyShopTrendsV3(cycle_type, month, shop_id);
            int averageFlow = res.getInteger("average_daily_passenger_flow");//获取每天得日均客流
            JSONArray trendList = res.getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer value = trendList.getJSONObject(i).getInteger("uv");
                if (value != null && value != 0) {
                    values += value;
                }
                if (value != null) {
                    values1++;
                }

            }
            int values2 = values / values1;
            int result = Math.abs(averageFlow - values2);
            Preconditions.checkArgument(result <= 1, "日均客流=" + averageFlow + "所选时间段内的日均客流pv=" + values2);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流pv");
        }

    }

    /**
     * ====================各个年龄段的男性比例累计和==男性总比例======================
     */
    @Test
    public void manSexScale() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //过店客群的各个年龄段的男性比例累计和
            double count = 0;
            double count1 = 0;
            JSONObject enter = md.historyShopAgeV3(shop_id, cycle_type, month).getJSONObject("enter");
            JSONArray ageList = md.historyShopAgeV3(shop_id, cycle_type, month).getJSONObject("enter").getJSONArray("list");

            String male_ratio_str = enter.getString("male_ratio_str");
            Double result1 = Double.valueOf(male_ratio_str.replace("%", ""));

            String female_ratio_str = enter.getString("female_ratio_str");
            Double result2 = Double.valueOf(female_ratio_str.replace("%", ""));

            for (int i = 0; i < ageList.size(); i++) {
                String male_percent = ageList.getJSONObject(i).getString("male_percent");
                String female_percent = ageList.getJSONObject(i).getString("female_percent");
                if (male_percent != null) {
                    Double result = Double.valueOf(male_percent.replace("%", ""));//将string格式转换成douBLE
                    count += result;//各个年龄段得男生比例累加
                }
                if (female_percent != null) {
                    Double result3 = Double.valueOf(female_percent.replace("%", ""));//将string格式转换成douBLE
                    count1 += result3;//各个年龄段得女生比例累加
                }
            }
            //获取某一年龄段的比例
            String age_group_percent = ageList.getJSONObject(0).getString("age_group_percent");
            Double resultOther = Double.valueOf(age_group_percent.replace("%", ""));

            //获取该年龄段的男性比例
            String male_percent = ageList.getJSONObject(0).getString("male_percent");
            Double maleResult = Double.valueOf(male_percent.replace("%", ""));

            //获取该年龄段的女性比例
            String female_percent = ageList.getJSONObject(0).getString("female_percent");
            Double femaleResult = Double.valueOf(female_percent.replace("%", ""));
            double theResult = maleResult + femaleResult;

            double resultAll = count + count1;

            double theError1 = Math.abs(result1 - count);
            double theError2 = Math.abs(result2 - count1);
            double theError3 = Math.abs(resultOther - theResult);
            Preconditions.checkArgument(theError1 < 1, "男性总比例=" + result1 + "各个年龄段的男性比例累计和=" + count);
            Preconditions.checkArgument(theError2 < 1, "女性总比例=" + result2 + "各个年龄段的女性比例累计和=" + count1);
            Preconditions.checkArgument(resultAll <= 101 || resultAll >= 99, "男性比例+女性比例" + resultAll + "不在99-100的范围间");
            Preconditions.checkArgument(theError3 < 1, "某一年龄段的比例" + resultOther + "该年龄段男性比例+该年龄段女性比例" + resultAll);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("各个年龄段的男性比例累计和==男性总比例|各个年龄段的女性比例累计和==女性总比例|男性比例+女性比例==100|某一年龄段的比例==该年龄段男性比例+该年龄段女性比例");
        }

    }

    /**
     * ====================门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）==实时客流中的门店基本信息======================
     */
   //@Test
    public void storeInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String district_code = "110000";
            Integer page = 1;
            Integer size = 50;
            JSONObject jsonObject = new JSONObject();
            boolean check = false;
            JSONArray storeList = md.patrolShopPageV3(district_code, page, size).getJSONArray("list");
            long shop_id = 14630;
            JSONObject res = md.shopDetailV3(shop_id);

            if (storeList.contains(res)) {
                check = true;
            }

            int id = storeList.getJSONObject(0).getInteger("id");


            Preconditions.checkArgument((check = true), "门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）不等于实时客流中的门店基本信息");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）等于实时客流中的门店基本信息");
        }

    }

    /**
     * ====================所选周期的顾客总人数<=所有门店各天顾客之和======================
     */
    @Test
    public void memberAllTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {


            Integer customer_uv = 0;
            Integer omni_uv = 0;
            Integer paid_uv = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = md.historyShopMemberCountV3(cycle_type).getJSONArray("trend_list");
            for (int i = 0; i < trend_list.size(); i++) {
                if (i - trend_list.size() == -1) {
                    customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                    paid_uv = trend_list.getJSONObject(i).getInteger("paid_uv_total");
                    if (customer_uv == null || omni_uv == null || paid_uv == null) {
                        customer_uv = 0;
                    }
                }

            }


            String shop_type = "";
            String shop_name = "";
            String shop_manager = "";
            String member_type = "";
            Integer member_type_order = null;
            JSONArray member_list = md.shopPageMemberV3(district_code, shop_type, shop_name, shop_manager, member_type, member_type_order, page, size).getJSONArray("list");
            int cust_uv = 0;
            int channel_uv = 0;
            int pay_uv = 0;
            for (int j = 0; j < member_list.size(); j++) {
                JSONArray memb_info = member_list.getJSONObject(j).getJSONArray("member_info");

                for (int k = 0; k < memb_info.size(); k++) {
                    String type = memb_info.getJSONObject(k).getString("type");
                    Integer uv = memb_info.getJSONObject(k).getInteger("uv");
                    if (uv == null) {
                        uv = 0;
                    }
                    if (type.equals("CUSTOMER")) {
                        cust_uv += uv;
                    }
                    if (type.equals("OMNI_CHANNEL")) {
                        channel_uv += uv;
                    }
                    if (type.equals("PAID")) {
                        pay_uv += uv;
                    }
                }

            }

            Preconditions.checkArgument((customer_uv == cust_uv), "累计的顾客总人数" + customer_uv + "=累计的顾客之和=" + cust_uv);
            Preconditions.checkArgument((omni_uv == channel_uv), "累计的全渠道会员总人数" + omni_uv + "=累计的30天全渠道会员之和=" + channel_uv);
            Preconditions.checkArgument((paid_uv == pay_uv), "累计的付费总人数" + paid_uv + "=累计的付费会员之和=" + pay_uv);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("累计顾客总人数==所有门店顾客之和|累计全渠道会员总人数==所有门店全渠道会员之和|累计付费会员总人数==所有门店付费会员之和");
        }

    }

    /**
     * ====================累计顾客的总数（所有店）==前天的累计客户+今天新增的（顾客+全渠道会员+付费会员）之和======================
     */
    @Test
    public void memberTotalCount() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {

            Integer customer_uv = 0;
            Integer customer_uv_new_today = 0;
            Integer customer_uv_01 = 0;
            Integer omni_uv_today = 0;
            Integer paid_uv_today = 0;
            Integer omni_uv_total = 0;
            Integer omni_uv_total_01 = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = md.historyShopMemberCountV3(cycle_type).getJSONArray("trend_list");
            JSONArray list = md.member_newCount_pic(cycle_type).getJSONArray("list");

            for (int i = 0; i < trend_list.size(); i++) {

                //获取昨天的累计客户总数,今天新增的顾客、全渠道会员、付费会员
                if (i - trend_list.size() == -1) {
                    customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                }

                //获取前天的累计顾客总数
                if (i - trend_list.size() == -2) {
                    customer_uv_01 = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total_01 = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                }

            }
            for(int j=0; j < list.size();j++){
                if (j - list.size() == -1) {
                    customer_uv_new_today = list.getJSONObject(j).getInteger("customer");
                    omni_uv_today = list.getJSONObject(j).getInteger("omni_channel");
                    paid_uv_today = list.getJSONObject(j).getInteger("paid");
                }
            }
            int qa_customer_uv = customer_uv_01 + customer_uv_new_today + omni_uv_today + paid_uv_today;
            int qa_omni_uv = omni_uv_total_01 + omni_uv_today;


            Preconditions.checkArgument((qa_customer_uv == customer_uv), "累计的顾客总人数" + customer_uv + "!=前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和=" + qa_customer_uv);
            Preconditions.checkArgument((qa_omni_uv == omni_uv_total), "累计的全渠道总人数" + omni_uv_total + "!=前天的累计全渠道会员+今天新增的（全渠道会员）之和=" + qa_omni_uv);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("（所有门店）累计顾客的总数==前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和||累计的全渠道总人数===前天的累计全渠道会员+昨天新增的（全渠道会员）之和");
        }

    }

    /**
     * ====================累计顾客的总数(单店)==前天的累计客户+今天新增的（顾客+全渠道会员+付费会员）之和======================
     */
    @Test
    public void shop_memberTotalCount() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {

            Integer customer_uv = 0;
            Integer customer_uv_new_today = 0;
            Integer customer_uv_01 = 0;
            Integer omni_uv_today = 0;
            Integer paid_uv_today = 0;
            Integer omni_uv_total = 0;
            Integer omni_uv_total_01 = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = md.historyShopMemberV3(shop_id, cycle_type, month).getJSONArray("trend_list");

            JSONArray list = md.single_newCount_pic(shop_id,cycle_type).getJSONArray("list");

            for (int i = 0; i < trend_list.size(); i++) {

                //获取昨天的累计客户总数,今天新增的顾客、全渠道会员、付费会员
                if (i - trend_list.size() == -1) {
                    customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                }

                //获取前天的累计顾客总数
                if (i - trend_list.size() == -2) {
                    customer_uv_01 = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total_01 = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                }

            }
            for(int j=0; j < list.size();j++){
                if (j - list.size() == -1) {
                    customer_uv_new_today = list.getJSONObject(j).getInteger("customer");
                    omni_uv_today = list.getJSONObject(j).getInteger("omni_channel");
                    paid_uv_today = list.getJSONObject(j).getInteger("paid");
                }
            }
            int qa_customer_uv = customer_uv_01 + customer_uv_new_today + omni_uv_today + paid_uv_today;
            int qa_omni_uv = omni_uv_total_01 + omni_uv_today;


            Preconditions.checkArgument((qa_customer_uv == customer_uv), "累计的顾客总人数" + customer_uv + "!=前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和=" + qa_customer_uv + "。报错门店shop_id=" + shop_id);
            Preconditions.checkArgument((qa_omni_uv == omni_uv_total), "累计的全渠道总人数" + omni_uv_total + "!=前天的累计全渠道会员+昨天新增的（全渠道会员）之和=" + qa_omni_uv + "。报错门店shop_id=" + shop_id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("（单个门店）累计顾客的总数==前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和||累计的全渠道总人数===前天的累计全渠道会员+昨天新增的（全渠道会员）之和");
        }

    }

    /**
     * ====================实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv======================
     */
   // @Test
    public void yesterdayTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {


            //获取昨天日各个时间段内到访得人次且相加
            JSONArray eTlist = md.realTimeShopPvV3((long) shop_id).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer yesterdayPv = eTlist.getJSONObject(i).getInteger("yesterday_pv");
                yesterdayPv = yesterdayPv != null ? yesterdayPv : 0;
                count += yesterdayPv;

            }

            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int pv = 0;
            int count1 = trend_list.size();
            for (int i = 0; i < count1; i++) {
                if (i == count1 - 1) {
                    pv = trend_list.getJSONObject(i).getInteger("pv");
                }
            }
            Preconditions.checkArgument((count == pv), "实时客流中，昨日到访各个时段的pv之和" + count + ">历史客流中截至日期的的pv=" + pv + "。报错门店的shopId=" + shop_id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv");
        }

    }


    /**
     * ====================uv与pv之间的比例要保持在1：4的范围间========================
     */
    //@Test
    public void uvWithPvScrole() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = md.realTimeShopTotalV3((long) shop_id).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");
            Integer pv = iPvlist.getJSONObject(0).getInteger("value");
            int scrole = 0;
            if (pv != 0 && uv != 0) {
                scrole = pv / uv;
            } else {
                uv = uv + 1;
                pv = pv + 1;
                scrole = pv / uv;
            }
            Preconditions.checkArgument((scrole <= 4), "uv=" + uv + "远远小于pv，不在1：4的范围间 pv=" + pv + "。报错门店的shopId=" + shop_id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("uv与pv之间的比例要保持在1：4的范围间" + "门店shopId=");
        }

    }


    /**
     * ====================云中客中累计不为0，事件也不能为0========================
     */
    @Test
    public void custmerWithThing() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray trend_list = md.historyShopMemberV3(shop_id, cycle_type, month).getJSONArray("trend_list");
            int count1 = trend_list.size();
            int customer_uv_total = 0;
            int customer_uv_new_today = 0;
            for (int i = 0; i < count1; i++) {
                if (i == count1 - 1) {
                    customer_uv_total = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                }
            }
            Integer total = md.memberTotalListV3(shop_id, page, size).getInteger("total");


            Preconditions.checkArgument((customer_uv_total != 0 && total != 0), "累计顾客为：" + customer_uv_total + "事件为" + total + "。报错门店的shopId=" + shop_id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("累计顾客与事件是否异常，有累计顾客但无事件或有事件无累计顾客");
        }

    }

    /**
     * ====================客户详情累计交易的次数==留痕事件中门店下单的次数|||累计到店的数据==留痕事件中进店次数+门店下单的次数========================
     */
    //@Test
    public void custInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
//            customer_id
            //根据门店id获取customer_id
            JSONObject response = md.memberTotalListV3(shop_id, page, size);
            int total = response.getInteger("total");

            JSONArray list = response.getJSONArray("list");
            boolean listResult = false;
            Integer enter_total = 0;
            Integer total_sum = 0;
            int deal_times = 0;
            Integer deal = 0;
            int enterDealAll_sum = 0;
            String customer_id = "";
            String face_url = "";
            String member_type = "";
            String member_id = "";
            JSONArray list1 = md.memberTotalListV3(shop_id, page, 50).getJSONArray("list");
            for (int j = 0; j < list1.size(); j++) {

                customer_id = list1.getJSONObject(j).getString("customer_id");
                member_type = list1.getJSONObject(j).getString("member_type");
                if (member_type.equals("OMNI_CHANNEL")) {
                    member_id = list1.getJSONObject(j).getString("member_id");
                    if (member_id == null) {
                        member_id = "";
                    }
                    Preconditions.checkArgument(!StringUtils.isEmpty(member_id), "人物ID为：" + customer_id + "的全渠道会员的会员ID为空" + member_id + "。  报错门店的shopId=" + shop_id);
                } else if (member_type.equals("CUSTOMER")) {
                    member_id = list1.getJSONObject(j).getString("member_id");
                    if (member_id == null) {
                        member_id = "";
                    } else {
                        Preconditions.checkArgument(StringUtils.isEmpty(member_id), "人物ID为：" + customer_id + "的会员ID不为空，会员ID为：" + member_id + "。  报错门店的shopId=" + shop_id);
                    }
                }

                total_sum = md.memberDetail(shop_id, customer_id, page, size).getInteger("total");//留痕事件数量
                if (total_sum == null) {
                    total_sum = 0;
                }

                int t = CommonUtil.getTurningPage(total_sum, 50);
                for (int l = 1; l < t; l++) {
                    JSONObject res = md.memberDetail(shop_id, customer_id, l, size);
                    enter_total = res.getInteger("total_visit_times");//累计到店次数
                    if (enter_total == null) {
                        enter_total = 0;
                    }
                    deal = res.getInteger("total_deal_times");//获取累计交易的次数
                    if (deal == null) {
                        deal = 0;
                    }
                    //交易次数+进店次数
                    enterDealAll_sum = enter_total + deal;
                    //或者每个人物的脸部图片地址
                    face_url = res.getString("face_url");
                    if (face_url == null) {
                        face_url = "";
                    }

                    Preconditions.checkArgument(!StringUtils.isEmpty(face_url), "人物ID为:" + customer_id + "的半身照为空" + "。报错门店的shopId=" + shop_id);
                    Preconditions.checkArgument(enterDealAll_sum == total_sum, "人物ID为：" + customer_id + "的【累计进店次数】+【累计交易次数】=" + enterDealAll_sum + "不等于留痕事件总条数=" + total_sum + "。报错门店的shopId=" + shop_id);


                    //获取事件中门店下单的次数
                    JSONArray thingsList = res.getJSONArray("list");

                    if (thingsList.size() == 0 && enter_total != 0 || deal != 0) {
                        listResult = true;
                    } else {
                        for (int k = 0; k < thingsList.size(); k++) {
                            String mark = thingsList.getJSONObject(k).getString("mark");
                            if (mark.equals("门店下单")) {
                                deal_times += 1;
                            }
                            Preconditions.checkArgument(deal_times == deal, "人物ID为:" + customer_id + "累计交易次数：" + deal + "不等于留痕事件中门店下单次数" + deal_times + "。报错门店的shopId=" + shop_id);
                        }
                    }
//                    Preconditions.checkArgument(listResult, "人物ID为:"+customer_id+"人物详情的留痕事件为空 " +"但是该人物的进店次数为："+enter_total+"交易次数为："+deal+"。报错门店的shopId=" + shop_id);

                }

            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("客户详情累计交易的次数==留痕事件中门店下单的次数||累计到店的数据==留痕事件中进店次数+门店下单的次数||门店客户的照片不能为空||全渠道会员一定有会员ID||顾客没有会员ID");
        }

    }

    /**
     * ====================选择自然月的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForMo() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String cycle_type = "";
            String month = "2020-08";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }

            Preconditions.checkArgument((uv_Sum != 0), "历史客流-自然月9月的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认自然月9月的数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-自然月9月的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-自然月9月的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择自然月9月的数据是否正常");
        }

    }

    /**
     * ====================选择最近7天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForS() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String cycle_type = "RECENT_SEVEN";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }

            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近7天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近7天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近7天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择最近7天的数据是否正常");
        }

    }

    /**
     * ====================选择最近14天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForF() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String cycle_type = "RECENT_FOURTEEN";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }
            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近14天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近14天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近14天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择最近14天的数据是否正常");
        }

    }

    /**
     * ====================选择最近30天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForT() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String cycle_type = "RECENT_THIRTY";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }


            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近30天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近30天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近30天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近30天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近30天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择最近30天的数据是否正常");
        }

    }

    /**
     * ====================选择最近60天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForSix() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            String cycle_type = "RECENT_SIXTY";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }


            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近60天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近60天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近60天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近60天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近60天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("选择最近60天的数据是否正常");
        }

    }

    /**
     * ====================门店客户列表的最新留痕时间==客户详情的最新留痕时间========================
     */
   // @Test
    public void arrival_time() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONObject response = md.memberTotalListV3(shop_id, page, 50);
            JSONArray list = response.getJSONArray("list");
            String last_time = "";
            String customer_id = "";
            String time = "";
            for (int i = 0; i < list.size(); i++) {
                last_time = list.getJSONObject(i).getString("latest_arrival_time");
                customer_id = list.getJSONObject(i).getString("customer_id");
                JSONObject res = md.memberDetail(shop_id, customer_id, page, 10);
                JSONArray detailList = res.getJSONArray("list");
                int total = res.getInteger("total");

                if (total != 0) {

                    time = detailList.getJSONObject(0).getString("time");
                    Preconditions.checkArgument((last_time.equals(time)), "客户ID：" + customer_id + "。列表最新留痕时间为：" + last_time + "。该客户详情中的最新留痕时间为：" + time + "。报错门店的shopId=" + shop_id);

                }
//                else {
//                    Preconditions.checkArgument(total != 0, "客户ID：" + customer_id + "该客户的留痕事件为空，列表最新留痕时间为：" + last_time + "。报错门店的shopId=" + shop_id);
//                }

            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("门店客户列表的最新留痕时间==客户详情的最新留痕时间");
        }

    }




//这些一致性需要进行操作，不可用在线上客户的账户下去验证-----------------------------------------------------------------------3.0版本新增的数据一致性---------------------------------------------------

    /**
     * ====================账户管理中的一致性========================
     */
    @Test
    public void accountInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            Integer total = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("107");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("14630");

            Integer status = 1;
            String type = "EMAIL";
            //用EMAIL新增一个账号
            JSONObject res = md.organizationAccountAdd(name, email, "", r_dList, status, shop_list, type);
            Integer code = res.getInteger("code");


            //从列表获取刚刚新增的账户的account
            JSONArray accountList = md.organizationAccountPage(name, "", email, "", "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");

            //新增账号以后，再查询列表
            Integer total1 = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
            int result = total1 - total;
            Preconditions.checkArgument(result == 1, "新增1个账号，账号列表的数量却加了：" + result);


            //编辑账号的名称，是否与列表该账号的一致
            String reName = "qingqing测编辑";
            md.organizationAccountEdit(account, reName, email, "", r_dList, status, shop_list, type);
            JSONArray accountsList = md.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String name_1 = accountsList.getJSONObject(0).getString("name");
            Preconditions.checkArgument(name_1.equals(reName), "修改账号：" + account + "的名称为：" + reName + "修改后，该账号的名称为：" + name_1);


            //删除账号以后，再查询列表
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "删除emial的账号:" + email + "失败了");
            Integer total2 = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
            int result1 = total1 - total2;
            Preconditions.checkArgument(result1 == 1, "删除1个账号，账号列表的数量却减了：" + result);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    /**
     * ====================账户管理中的一致性========================
     */
    @Test
    public void accountInfoData_1() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            Integer total = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("107");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("14630");


            JSONArray list = md.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String today = dt.getHHmm(0);
            String account = "";
            String old_phone = "";
            String create_time = "";
            for (int i = 1; i < list.size(); i++) {
                create_time = list.getJSONObject(0).getString("create_time");
                if (!create_time.equals(today)) {
                    account = list.getJSONObject(0).getString("account");
                    old_phone = list.getJSONObject(0).getString("phone");
                    break;
                }
            }

            if (old_phone != "" && old_phone != null) {
                //编辑账号的名称，权限
                String reName = "qingqing在测编辑";
                md.organizationAccountEdit(account, reName, "", old_phone, r_dList, status, shop_list, type);
                //获取列表该账号
                JSONArray accountList = md.organizationAccountPage("", "", "", old_phone, "", "", page, size).getJSONArray("list");
                String create_time_1 = "";
                String phone_1 = accountList.getJSONObject(0).getString("phone");//获取通过手机号搜索到的账号的手机号
                if (phone_1.equals(old_phone)) {
                    create_time_1 = accountList.getJSONObject(0).getString("create_time");

                }
                Preconditions.checkArgument(create_time_1.equals(create_time), "编辑昨天" + create_time + "的创建的账号" + old_phone + "列表该账号的创建时间变成了最新编辑的时间" + create_time_1);
                //编辑完以后获取列表的数量，是否有增多或者减少
                Integer total1 = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
                Preconditions.checkArgument(total == total1, "编辑一个账号，账号列表的数量由:" + total + "变成了" + total1);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("编辑账号信息以后，创建者和创建时间是否发生改变");
        }

    }

    /**
     * ====================账户管理中的一致性（使用账号数量==账号列表中的数量）========================
     */
    @Test
    public void accountInfoData_2() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.organizationRolePage("", page, size).getJSONArray("list");
            boolean result = false;
            for (int i = 1; i < list.size(); i++) {
                String role_name = list.getJSONObject(i).getString("role_name");
                JSONArray list1 = md.organizationRolePage(role_name, page, size).getJSONArray("list");
                int account_num = list1.getJSONObject(0).getInteger("account_number");

                Integer Total = md.organizationAccountPage("", "", "", "", role_name, "", page, size).getInteger("total");
                if (account_num == Total) {
                    result = true;
                }
                Preconditions.checkArgument(result = true, "角色名为:" + role_name + "的使用账户数量：" + account_num + "！=【账户列表】中该角色的账户数量：" + Total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("角色的使用账号数量==账号列表中该角色的数量");
        }

    }

    /**
     * ====================收银风控的数据一致性(累计的风险事件==【收银风控事件】待处理+已处理+已过期)========================
     */
    @Test
    public void cashierDataInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                //获取列表门店的累计风险事件
                int risk_total = list.getJSONObject(i).getInteger("risk_total");
                //获取该门店的shop_id
                long shop_id = list.getJSONObject(i).getInteger("shop_id");
                //获取待处理的数量
                String current_state = "PENDING";
                int pend_total = md.cashier_riskPage(shop_id, "", "", "", "", "", current_state, page, size).getInteger("total");

                //获取已处理的数量
                String current_state1 = "PROCESSED";
                int processe_total = md.cashier_riskPage(shop_id, "", "", "", "", "", current_state1, page, size).getInteger("total");

                //获取已过期的数量
                String current_state2 = "EXPIRED";
                int expired_total = md.cashier_riskPage(shop_id, "", "", "", "", "", current_state2, page, size).getInteger("total");

                int result = pend_total + processe_total + expired_total;
                Preconditions.checkArgument(result == risk_total, "累计风险事件：" + risk_total + "!=待处理：" + pend_total + "+已处理：" + processe_total + "+已过期：" + expired_total + "之和：" + result);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("累计的风险事件==【收银风控事件】待处理+已处理+已过期");
        }

    }

    /**
     * ====================收银风控的数据一致性（正常事件==【收银风控事件】列表页处理结果为正常的数量）========================
     */
    @Test
    public void cashierDataInfo1() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int normal_num = list.getJSONObject(i).getInteger("normal_total");
                long shop_id = list.getJSONObject(i).getInteger("shop_id");
                //获取收银风控事件列表的正常事件的数量
                String handle_result = "NORMAL";
                int total = md.cashier_riskPage(shop_id, "", "", "", "", handle_result, "", page, size).getInteger("total");
                Preconditions.checkArgument(normal_num == total, "收银风控列表门店ID：" + shop_id + "的正常事件：" + normal_num + "!=【收银风控事件】中该门店正常事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("正常事件==【收银风控事件】列表页处理结果为正常的数量");
        }

    }

    /**
     * ====================收银风控的数据一致性（异常事件==【收银风控事件】列表页处理结果为异常的数量）========================
     */
    @Test
    public void cashierDataInfo2() {
        logger.logCaseStart(caseResult.getCaseName());
        md.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        try {
            JSONArray list = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int abnormal_num = list.getJSONObject(i).getInteger("abnormal_total");
                long shop_id = list.getJSONObject(i).getInteger("shop_id");
                //获取收银风控事件列表的异常事件的数量
                String handle_result = "ABNORMAL";
                int total = md.cashier_riskPage(shop_id, "", "", "", "", handle_result, "", page, size).getInteger("total");
                Preconditions.checkArgument(abnormal_num == total, "收银风控列表门店ID：" + shop_id + "的正常事件：" + abnormal_num + "！=【收银风控事件】中该门店正常事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("异常事件==【收银风控事件】列表页处理结果为异常的数量");
        }

    }

    /**
     * ====================收银风控的数据一致性（待处理事件==【收银风控事件】列表页中当前状态为待处理的事件）========================
     */
    @Test
    public void cashierDataInfo3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int pending_num = list.getJSONObject(i).getInteger("pending_risks_total");
                long shop_id = list.getJSONObject(i).getInteger("shop_id");
                //获取收银风控事件列表的待处理事件的数量
                String current_state = "PENDING";
                int total = md.cashier_riskPage(shop_id, "", "", "", "", "", current_state, page, size).getInteger("total");
                Preconditions.checkArgument(pending_num == total, "收银风控列表门店ID：" + shop_id + "的正常事件：" + pending_num + "！=【收银风控事件】中该门店正常事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("待处理事件==【收银风控事件】列表页中当前状态为待处理的事件");
        }

    }

    /**
     * ====================收银风控的数据一致性（正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量）========================
     */
    @Test
    public void cashierDataInfo4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                //获取处理结果为正常和异常的数量
                int normal_num = list.getJSONObject(i).getInteger("normal_total");
                int abnormal_num = list.getJSONObject(i).getInteger("abnormal_total");
                int result = normal_num + abnormal_num;
                long shop_id = list.getJSONObject(i).getInteger("shop_id");
                //获取收银风控事件列表的当前状态为已处理的数量
                String current_state = "PROCESSED";
                int total = md.cashier_riskPage(shop_id, "", "", "", "", "", current_state, page, size).getInteger("total");
                Preconditions.checkArgument(result == total, "收银风控列表门店ID：" + shop_id + "处理结果为正常+处理结果为异常的和：" + result + "！=【收银风控事件】中该门店已处理事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量");
        }

    }




}
