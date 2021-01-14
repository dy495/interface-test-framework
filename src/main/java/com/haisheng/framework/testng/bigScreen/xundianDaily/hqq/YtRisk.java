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
public class YtRisk extends TestCaseCommon implements TestCaseStd {
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
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
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
     * 生成交易订单
     **/
    @Test
    public void getA() throws Exception {
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
            String time = dt.getHistoryDate(0);
            String time1 = dt.getHHmm(0);
            String userId = "tester" + CommonUtil.getRandom(6);
            String transId = "QAtest_" + CommonUtil.getRandom(3) + time + time1;
            String transTime = "" + System.currentTimeMillis();
            String str = "{\n" +
                    "  \"uid\": \"uid_ef6d2de5\",\n" +
                    "  \"app_id\": \"49998b971ea0\",\n" +
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a91\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/bind/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"43072\",\n" +
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
                    "        \"memberName\":\"青青涛涛\",\n" +
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
    /**
     * ====================特殊人员管理-风控黑名单搜索======================
     */
    @Test
    public void balck_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.white_black_list(page,size,null,null,null,"BLACK").getJSONArray("list");
            for(int i=0;i<list.size();i++){
               String  customer_id = list.getJSONObject(i).getString("customer_id");
                JSONArray list1 = md.white_black_list(page,size,null,null,customer_id,"BLACK").getJSONArray("list");
                String customer_id1 = list1.getJSONObject(0).getString("customer_id");
                checkArgument(customer_id1.equals(customer_id), "根据列表展示的人物ID进行筛选，没有匹配的值");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("特殊人员管理-风控黑名单搜索");
        }

    }
    /**
     * ====================特殊人员管理-风控黑名单搜索======================
     */
    @Test
    public void balck_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.white_black_list(page,size,"jdjdjsdj",null,null,"BLACK").getJSONArray("list");
            checkArgument(list.isEmpty(), "根据人物ID输入乱码进行筛选，匹配到结果");
            JSONArray list1 = md.white_black_list(page,size,null,"9839128301283",null,"BLACK").getJSONArray("list");
            checkArgument(list1.isEmpty(), "根据会员ID输入乱码进行筛选，匹配到结果");
            JSONArray list2 = md.white_black_list(page,size,null,null,"u9923","BLACK").getJSONArray("list");
            checkArgument(list2.isEmpty(), "根据人物ID输入乱码进行筛选，匹配到结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("特殊人员管理-风控黑名单搜索");
        }
    }

    /**
     * ====================特殊人员管理-风控白名单搜索======================
     */
    @Test
    public void white_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.white_black_list(page,size,"jdjdjsdj",null,null,"WHITE").getJSONArray("list");
            checkArgument(list.isEmpty(), "根据人物ID输入乱码进行筛选，匹配到结果");
            JSONArray list1 = md.white_black_list(page,size,null,"9839128301283",null,"WHITE").getJSONArray("list");
            checkArgument(list1.isEmpty(), "根据会员ID输入乱码进行筛选，匹配到结果");
            JSONArray list2 = md.white_black_list(page,size,null,null,"u9923","WHITE").getJSONArray("list");
            checkArgument(list2.isEmpty(), "根据人物ID输入乱码进行筛选，匹配到结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("特殊人员管理-风控白名单搜索");
        }
    }
    /**
     * ====================特殊人员管理-风控白名单搜索======================
     */
    @Test
    public void white_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.white_black_list(page,size,null,null,null,"WHITE").getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String  customer_id = list.getJSONObject(i).getString("customer_id");
                JSONArray list1 = md.white_black_list(page,size,null,null,customer_id,"WHITE").getJSONArray("list");
                String customer_id1 = list1.getJSONObject(0).getString("customer_id");
                checkArgument(customer_id1.equals(customer_id), "根据列表展示的人物ID进行筛选，没有匹配的值");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("特殊人员管理-风控白名单搜索");
        }

    }

    /**
     * ====================特殊人员管理-黑名单中的客户详情-查看风控事件======================
     */
    @Test
    public void risk_thing() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.white_black_list(page,size,null,null,null,"BLACK").getJSONArray("list");
            String  customer_id = "";
            for(int i=0;i<list.size();i++){
                customer_id = list.getJSONObject(i).getString("customer_id");
                JSONArray list1 = md.black_listPage(page,size,customer_id).getJSONArray("list");
                checkArgument(!list1.isEmpty(), "黑名单-"+customer_id+"客户详情的风控事件为空");
            }
            JSONObject res = md.white_black_addDetail(customer_id,"BLACK");
            String customer_id1 = res.getString("customer_id");
            String half_body = res.getString("half_body");
            JSONArray list3 = md.white_black_operate(page,size,customer_id).getJSONArray("list");
            checkArgument(customer_id1.equals(customer_id), "黑名单-"+customer_id+"客户详情中的人物ID");
            checkArgument(!half_body.isEmpty(), "黑名单-"+customer_id+"客户详情中的头像为空");
            checkArgument(!list3.isEmpty(), "黑名单-"+customer_id+"操作日志为空");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("特殊人员管理-黑名单中的客户详情-查看风控事件");
        }

    }

    /**
     * ====================特殊人员管理-白名单中的客户详情-查看风控事件======================
     */
    @Test
    public void risk_thing1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.white_black_list(page,size,null,null,null,"WHITE").getJSONArray("list");
            String  customer_id = "";
            for(int i=0;i<list.size();i++){
                customer_id = list.getJSONObject(i).getString("customer_id");
            }
            JSONObject res = md.white_black_addDetail(customer_id,"WHITE");
            String customer_id1 = res.getString("customer_id");
            String half_body = res.getString("half_body");
            JSONArray list3 = md.white_black_operate(page,size,customer_id).getJSONArray("list");
            checkArgument(customer_id1.equals(customer_id), "白名单-"+customer_id+"客户详情中的人物ID");
            checkArgument(!half_body.isEmpty(), "白名单-"+customer_id+"客户详情中的头像为空");
            checkArgument(!list3.isEmpty(), "白名单-"+customer_id+"操作日志为空");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("特殊人员管理-白名单中的客户详情-查看风控事件");
        }

    }
}