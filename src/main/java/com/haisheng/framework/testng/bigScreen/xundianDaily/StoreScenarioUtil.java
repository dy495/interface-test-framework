package com.haisheng.framework.testng.bigScreen.xundianDaily;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.jooq.util.derby.sys.Sys;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.apache.poi.poifs.crypt.HashAlgorithm.none;

public class StoreScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile StoreScenarioUtil instance = null;
    private StoreScenarioUtil() {
    }

    public static StoreScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (StoreScenarioUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new StoreScenarioUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://123.57.148.247";

    /**
     * @description:登录
     * @author: qingqing
     * @time:
     */
    public void login(String userName, String passwd) {
        initHttpConfig();
        String path = "/patrol-login";
        String loginUrl = IpPort + path;
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");

    }

    @Test
    public void getA() throws Exception {
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
        String uid = "uid_0ba743d8";
        String appId = "672170545f50";
        String ak = "691ff41137d954f3";
        String router = "/business/bind/TRANS_INFO_RECEIVE/v1.0";
        String nonce = UUID.randomUUID().toString();
        String sk = "d76f2d8a7846382f633c1334139767fe";
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


        String str = "{\n" +
                "  \"uid\": \"uid_0ba743d8\",\n" +
                "  \"app_id\": \"672170545f50\",\n" +
                "  \"request_id\": \"5d45a085-3774-4e0f-943e-ded373ca6a75\",\n" +
                "  \"version\": \"v1.0\",\n" +
                "  \"router\": \"/business/bind/TRANS_INFO_RECEIVE/v1.0\",\n" +
                "  \"data\": {\n" +
                "    \"biz_data\":  {\n" +
                "        \"shop_id\": \"13260\",\n" +
                "        \"trans_id\": \"QAtest1111_0999\",\n" +
                "        \"trans_time\": \"1605065792336\",\n" +
                "        \"trans_type\": [\n" +
                "            \"W\"\n" +
                "        ],\n" +
                "        \"user_id\": \"2020100009\",\n" +
                "        \"total_price\": 1800,\n" +
                "        \"real_price\": 1500,\n" +
                "        \"shopType\": \"SHOP_TYPE\",\n" +
                "        \"orderNumber\": \"13444894484\",\n" +
                "        \"memberName\":\"单笔金额大于1200要触发\",\n" +
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

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        final String NUMBER = ".";

        String uid = "uid_0ba743d8";
        String appId = "672170545f50";//c30dcafc59c8
        String ak = "691ff41137d954f3";
        String router = "/business/bind/TRANS_INFO_RECEIVE/v1.0";
        String sk = "d76f2d8a7846382f633c1334139767fe";
        final String ALGORITHM = "HmacSHA256";

        Long timestamp = System.currentTimeMillis();
        String non = "2e4b56c4-ac4c-4778-aa12-81657f5feb44";



        System.out.println(timestamp);
    }


    @DataProvider(name = "CYCLE_TYPE")
    public static Object[] cycle_type() {

        return new String[]{
                "RECENT_SEVEN",
                "RECENT_FOURTEEN",
                "RECENT_THIRTY",
                "RECENT_SIXTY"
        };
    }

    @DataProvider(name = "END_TIME_TYPE")
    public static Object[] endTimeType() {

        return new String[]{
                "2020-07-14",
                "2020-07-18"

        };
    }

    @DataProvider(name = "DESCRIPTION")
    public static Object[] description() {

        return new String[]{
                "店庆店庆店庆店庆店庆",
                "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆"
        };
    }

    @DataProvider(name = "DESCRIPTION_FALSE")
    public static Object[] description_false() {

        return new String[]{
                "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆",
                ""
        };
    }

    @DataProvider(name = "THING_TYPE")
    public static Object[] thing_type() {

        return new String[]{
                "NEW_COMMODITY",
                "CHANGE_COMMODITY",
                "PROMOTIONS"
        };
    }

    @DataProvider(name = "THING_TYPE_FALSE")
    public static Object[] thing_type_false() {

        return new String[]{
                ""
        };
    }

    @DataProvider(name = "TIME_TYPE_FALSE")
    public static Object[] time_type_false() {

        return new String[]{
                "2019-08-08",
                "2020-07-09",
                ""
        };
    }

    @DataProvider(name = "AREA_CODE")
    public static Object[] area_code() {

        return new String[]{
                "110000"
        };
    }

    @DataProvider(name = "AREA_TYPE")
    public static Object[] area_type() {

        return new String[]{
                "[\"NORMAL\"]"
        };
    }

    @DataProvider(name = "CUSTMER_TYPE")
    public static Object[] custmer_type() {

        return new String[]{
                "PASS_BY",
                "INTEREST",
                "ENTER",
                "DEAL"
        };
    }

    @DataProvider(name = "STATUS")
    public static Object[] status() {

        return new String[]{
                "RUNNING",
                "STREAM_ERROR",
                "OFFLINE"
        };
    }

    @DataProvider(name = "SEARCH")
    public static Object[] search() {

        return new String[]{
                "[\"name\"]",
                "[\"type\"]",
                "[\"accept_role\"]",
                "[\"statu\"]"
        };
    }
    //将账户使用次数为0的角色删除
    public void deleteRole() throws Exception {
        JSONArray role_list = organizationRolePage("",1,100).getJSONArray("list");
        for(int i=0;i<role_list.size();i++){
            int account_number = role_list.getJSONObject(i).getInteger("account_number");
            if(account_number==0){
                Long role_id = role_list.getJSONObject(i).getLong("role_id");
                organizationRoleDelete(role_id);
            }

        }
    }
//    String district_code = "110105";
/**---------------------------------------------------门店相关V3.0新增的接口&修改过的接口-----------------------------------------------------**/

    /**
     * @description:8.1.1 门店类型列表V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject shopTypeList() throws Exception {
        String url = "/patrol/shop/type/list";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.1.3 实时客流门店列表V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopRealV3(String district_code, String shop_type, Integer page, Integer size) throws Exception {
        String url = "/patrol/shop/page/real-time";

        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\",\n" +
                        "\"shop_type\" :" + shop_type + ",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.1.3 实时客流门店列表V3.0
     * @author: guoliya
     * @time:
     */

    public JSONObject patrolShopRealV3A(String district_code, String[] shop_type, String shop_name, String shop_manager, Integer page, Integer size) throws Exception {
        String url = "/patrol/shop/page/real-time";
        JSONObject object = new JSONObject();
        object.put("district_code", district_code);
        object.put("shop_type", shop_type);
        object.put("shop_name", shop_name);
        object.put("shop_manager", shop_manager);
        object.put("page", page);
        object.put("size", size);
        String res = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.1.2 门店列表(获取主账号下所有门店)V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopPageV3(String district_code, Integer page, Integer size) throws Exception {
        String url = "/patrol/shop/page";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.1.4 历史客流门店列表V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject shopPageHistoryV3(String district_code, String shop_type, String shop_name, String shop_manager, Integer page, Integer size) throws Exception {
        String url = "/patrol/shop/page/history";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\",\n" +
                        "\"shop_type\" :\"" + shop_type + "\",\n" +
                        "\"shop_name\" :\"" + shop_name + "\",\n" +
                        "\"shop_manager\" :\"" + shop_manager + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.1.5 会员门店列表V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject shopPageMemberV3(String district_code, String shop_type, String shop_name, String shop_manager, String member_type, Integer member_type_order, Integer page, Integer size) throws Exception {
        String url = "/patrol/shop/page/member";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\",\n" +
                        "\"shop_type\" :\"" + shop_type + "\",\n" +
                        "\"shop_name\" :\"" + shop_name + "\",\n" +
                        "\"shop_manager\" :\"" + shop_manager + "\",\n" +
                        "\"member_type\" :\"" + member_type + "\",\n" +
                        "\"member_type_order\" :" + member_type_order + ",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.1.6 门店详情V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject shopDetailV3(long shop_id) throws Exception {
        String url = "/patrol/shop/detail";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    /**---------------------------------8.2 日期相关-------------------------**/
    /**
     * @description:8.2.1 查询周期列表V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject queryCycleListV3(long shop_id) throws Exception {
        String url = "/patrol/query-cycle/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**---------------------------------8.3 历史客流-------------------------**/
    /**
     * @description:8.3.1 到店趋势V3.0---8.3.1.1 获取天气类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject weatherTypeListV3() throws Exception {
        String url = "/patrol/weather/type/list";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.3.1 到店趋势V3.0---8.3.1.2 获取到店趋势数据（20200716增加同类和同市店铺数据）
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopTrendV3(String cycle_type, String month, long shop_id) throws Exception {
        String url = "/patrol/history/shop/trend-pv-uv";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\",\n" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 8.3.1.3获取到店趋势数据pv & uv (8.3.1.2升级版)
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopTrendsV3(String cycle_type, String month, long shop_id) throws Exception {
        String url = "/patrol/history/shop/trend-pv-uv";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\",\n" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.3.2 活动事件相关V3.0---8.3.2.1 获得店铺支持的活动事件类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject activityTypeListV3(long shop_id) throws Exception {
        String url = "/patrol/activity/type/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.3.2 活动事件相关V3.0---8.3.2.2 添加活动事件
     * @author: qingqing
     * @time:
     */
    public JSONObject activityAddV3(String activity_description, String activity_type, String start_date, String end_date, long shop_id) throws Exception {
        String url = "/patrol/activity/add";
        String json =
                "{" +
                        "\"activity_description\" :\"" + activity_description + "\",\n" +
                        "\"activity_type\" :\"" + activity_type + "\",\n" +
                        "\"start_date\" :\"" + start_date + "\",\n" +
                        "\"end_date\" :\"" + end_date + "\",\n" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**---------------------------------8.4 实时客流-------------------------**/

    /**
     * @description:8.4.1 门店实时数据总览（20200716添加了同类和同城门店平均客流）
     * @author: qingqing
     * @time:
     */
    public JSONObject realTimeShopTotalV3(long shop_id) throws Exception {
        String url = "/patrol/real-time/shop/total";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.4.2 门店小时级别实时客流（20200716添加了活动支持）
     * @author: qingqing
     * @time:
     */
    public JSONObject realTimeShopPvV3(long shop_id) throws Exception {
        String url = "/patrol/real-time/shop/pv-uv";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**-------------------------------------------------------------8.5 会员相关---------------------------------------------**/

    /**
     * @description:8.5.1 会员类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject memberTypeListV3(long shop_id) throws Exception {
        String url = "/patrol/member/type/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:8.5.2 会员人脸注册类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject memberFaceTypeListV3(long shop_id) throws Exception {
        String url = "/patrol/member/face-register-type/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.3 来访抓拍列表
     * @author: qingqing
     * @time:
     */
    public JSONObject memberVisitListV3(long shop_id, String member_type, Integer face_register, Integer gender, String member_id, Integer page, Integer size) throws Exception {
        String url = "/patrol/member/visit/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"member_type\" :\"" + member_type + "\",\n" +
                        "\"face_register\" :" + face_register + ",\n" +
                        "\"gender\" :" + gender + ",\n" +
                        "\"member_id\" :\"" + member_id + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + ",\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.4 通过隐私提醒，获得头像数据
     * @author: qingqing
     * @time:
     */
    public JSONObject memberPrivacyAgreeV3(long shop_id, String member_id) throws Exception {
        String url = "/patrol/member/privacy/agree";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"member_id\" :\"" + member_id + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.5 企业下所有门店会员增长趋势
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopMemberCountV3(String cycle_type, String month) throws Exception {
        String url = "/patrol/history/shop/member/new/count";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.6 会员增长趋势
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopMemberV3(long shop_id, String cycle_type, String month) throws Exception {
        String url = "/patrol/history/shop/new/member";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.6 会员性别默认图片列表
     * @author: qingqing
     * @time:
     */
    public JSONObject memberImaListV3(long shop_id) throws Exception {
        String url = "/patrol/member/gender-default-img/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.7 门店列表-云中客的
     * @author: qingqing
     * @time:
     */
    public JSONObject memberTotalListV3(long shop_id, Integer page, Integer size) throws Exception {
        String url = "/patrol/member/total/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.5.8 门店列表-云中客门店客户详情
     * @author: qingqing
     * @time:
     */
    public JSONObject memberDetail(long shop_id, String customer_id, Integer page, Integer size) throws Exception {
        String url = "/patrol/member/privacy/agree/detail";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**--------------------------------------------------------8.6 客群漏斗------------------------------------------------------------**/
    /**
     * @description:8.6.1 客群漏斗列表
     * @author: qingqing
     * @time:
     */
    public JSONObject shopPageConversionV3(String district_code, String shop_type, String shop_name, String shop_manager, String percentage_type, Integer percentage_type_order, Integer page, Integer size) throws Exception {
        String url = "/patrol/shop/page/conversion";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\",\n" +
                        "\"shop_type\" :\"" + shop_type + "\",\n" +
                        "\"shop_name\" :\"" + shop_name + "\",\n" +
                        "\"shop_manager\" :\"" + shop_manager + "\",\n" +
                        "\"shop_name\" :\"" + shop_name + "\",\n" +
                        "\"percentage_type\" :\"" + percentage_type + "\",\n" +
                        "\"percentage_type_order\" :\"" + percentage_type_order + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + ",\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.6.2 客群漏斗想详情(20200716升级为pv和uv共存版本)
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopConversionV3(long shop_id, String cycle_type, String month) throws Exception {
        String url = "/patrol/history/shop/conversion";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.6.3 到店时段分布（20200716增加多客群）
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopHourV3(long shop_id, String cycle_type, String month) throws Exception {
        String url = "/patrol/history/shop/hour-data";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.6.4 年龄性别分布
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopAgeV3(long shop_id, String cycle_type, String month) throws Exception {
        String url = "/patrol/history/shop/age-gender/distribution";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //---------------------十一、组织架构接口-----------------


    /**
     * @description:11.1.1 账号管理列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountPage(String name, String account, String email, String phone, String role_name, String shop_list, Integer page, Integer size) throws Exception {
        String url = "/patrol/organization/account/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        ;
        if (account != "") {
            json = json + "\"account\" :\"" + account + "\",\n";
        }
        ;
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        ;
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        ;
        if (role_name != "") {
            json = json + "\"role_name\" :\"" + role_name + "\",\n";
        }
        ;
        if (shop_list != "") {
            json = json + "\"shop_list\" :\"" + shop_list + "\",\n";
        }
        ;
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.2 新增账号
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountAdd(String name, String email, String phone, List role_id_list, Integer status, List shop_list, String type) throws Exception {
        String url = "/patrol/organization/account/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n";
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        ;
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        ;
        json = json +
                "\"role_id_list\" :" + role_id_list + ",\n" +
                "\"status\" :" + status + ",\n" +
                "\"shop_list\" :" + shop_list + ",\n" +
                "\"type\" :\"" + type + "\"\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }


    /**
     * @description:11.1.3 账号详情
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountDetail(String account) throws Exception {
        String url = "/patrol/organization/account/detail";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.4 账号编辑
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountEdit(String account, String name, String email, String phone, List role_id_list, Integer status, List shop_list, String type) throws Exception {
        String url = "/patrol/organization/account/edit";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\",\n" +
                        "\"name\" :\"" + name + "\",\n";
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        ;
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        ;
        json = json +

                "\"role_id_list\" :" + role_id_list + ",\n" +
                "\"shop_list\" :" + shop_list + ",\n" +
                "\"type\" :\"" + type + "\"\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.1.5 账号删除
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountDelete(String account) throws Exception {
        String url = "/patrol/organization/account/delete";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.1.6 账号状态更改（不是启用就是禁用，点击就更改对应的状态，前端传入当前账号状态就可以，后台判断更改）
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountButtom(String account, Integer status) throws Exception {
        String url = "/patrol/organization/account/change-status";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\",\n" +
                        "\"status\" :\"" + status + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.7 角色列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleList() throws Exception {
        String url = "/patrol/organization/role/list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.1.8 门店列表
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopList(String district_code) throws Exception {
        String url = "/patrol/shop/list";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.9 修改密码
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopList(String new_password, String old_password) throws Exception {
        String url = "/patrol/organization/account/change-password";
        String json =
                "{" +
                        "\"new_password\" :\"" + new_password + "\",\n" +
                        "\"old_password\" :\"" + old_password + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.2.1 向SSO服务查询当前门店可用的配置模块和对应的权限列表（参数依赖sso服务，这个是新增接口，后续可能变化）
     * @author: qingqing
     * @time:
     */
    public JSONObject query_permission_map() throws Exception {
        String url = "/patrol/organization/role-management/query-permission-map";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.2 角色管理列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRolePage(String role_name, Integer page, Integer size) throws Exception {
        String url = "/patrol/organization/role/page";
        String json =
                "{" +
                        "\"role_name\" :\"" + role_name + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.3 新增角色（参数依赖sso服务，这个是新增接口，后续可能变化）
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleAdd(String name, String description, JSONArray module_id) throws Exception {
        String url = "/patrol/organization/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_id\" :" + module_id + "\n" +

                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), false);


    }


    /**
     * @description:11.2.4 角色详情
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleDetail(long role_id) throws Exception {
        String url = "/patrol/organization/role/detail";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.5 角色编辑
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleEdit(long role_id, String name, String description, JSONArray module_ids) throws Exception {
        String url = "/patrol/organization/role/edit";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + ",\n" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_ids\" :" + module_ids + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.2.6 角色删除
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleDelete(long role_id) throws Exception {
        String url = "/patrol/organization/role/delete";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.7 角色对应的账号列表
     * @author: qingqing
     * @time:
     */
    public JSONObject AccountRolePage(long role_id, Integer page, Integer size) throws Exception {
        String url = "/patrol/organization/account/role/page";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //--------------设备列表--------------

    /**
     * @description:12.1.1 设备列表
     * @author: qingqing
     * @time:
     */
    public JSONObject device_page(String device_name, String shop_name, String device_id, String status, String type, Integer page, Integer size) throws Exception {
        String url = "/patrol/equipment-management/device/page";
        String json = "{";
        if (device_name != "") {
            json = json + "\"device_name\" :\"" + device_name + "\",\n";
        }
        if (shop_name != "") {
            json = json + "\"shop_name\" :\"" + shop_name + "\",\n";
        }
        if (device_id != "") {
            json = json + "\"device_id\" :\"" + device_id + "\",\n";
        }
        if (status != "") {
            json = json + "\"status\" :\"" + status + "\",\n";
        }
        json = json +
                "\"type\" :\"" + type + "\",\n" +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    //--------------------------------收银风控-------------

    /**
     * @description:13.1.1 收银风控列表
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_page(String shop_name, String manager_name, String manager_phone, String sort_event_type, Integer sort_event_type_order, Integer page, Integer size) throws Exception {
        String url = "/patrol/risk-control/cashier/page";
        String json =
                "{";
        if (shop_name != "") {
            json = json + "\"shop_name\" :\"" + shop_name + "\",\n";
        }
        if (manager_name != "") {
            json = json + "\"manager_name\" :\"" + manager_name + "\",\n";
        }
        if (sort_event_type != "") {
            json = json + "\"sort_event_type\" :\"" + sort_event_type + "\",\n";
        }
        if (sort_event_type_order != null) {
            json = json + "\"sort_event_type_order\" :" + sort_event_type_order + ",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.2 收银追溯
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_traceBack(long shop_id, String date, String order_id, Integer page, Integer size) throws Exception {
        String url = "/patrol/risk-control/cashier/trace-back";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n";
        if (date != "") {
            json = json + "\"date\" :\"" + date + "\",\n";
        }
        ;
        if (order_id != "") {
            json = json + "\"order_id\" :\"" + order_id + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.3 收银追溯查看小票详情
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_orderDetail(String order_id) throws Exception {
        String url = "/patrol/risk-control/cashier/order-detail";
        String json =
                "{" +
                        "\"order_id\" :\"" + order_id + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.4 收银风控事件列表
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskPage(long shop_id, String event_name, String order_id, String order_date, String member_name, String handle_result, String current_state, Integer page, Integer size) throws Exception {
        String url = "/patrol/risk-control/cashier/risk-event/page";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" ;
                      if(event_name !=""){
                       json = json+   "\"event_name\" :\"" + event_name + "\",\n";
                      }
                     if(order_id !=""){
                       json = json+   "\"order_id\" :\"" + order_id + "\",\n";
                      }
                     if(order_date !=""){
                       json = json+   "\"order_date\" :\"" + order_date + "\",\n";
                      }
                     if(member_name !=""){
                       json = json+   "\"member_name\" :\"" + member_name + "\",\n";
                      }
                     if(handle_result !=""){
                       json = json+   "\"handle_result\" :\"" + handle_result + "\",\n";
                      }
                     if(current_state !=""){
                       json = json+   "\"current_state\" :\"" + current_state + "\",\n";
                      }
                    json = json +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.5 风控事件概览
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskPage(long id) throws Exception {
        String url = "/patrol/risk-control/cashier/risk-event/overview";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.6 风控事件涉及订单列表
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_ordersInvolvedList(long id) throws Exception {
        String url = "/patrol/risk-control/cashier/risk-event/orders-involved/list";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.7 风控事件处理
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskEventHandle(long id, Integer result, String remarks) throws Exception {
        String url = "/patrol/risk-control/cashier/risk-event/handle";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"result\" :" + result + ",\n" +
                        "\"remarks\" :\"" + remarks + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }


    /**
     * @description:13.1.8 风控事件处理查看
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskEventView(long id) throws Exception {
        String url = "/patrol/risk-control/cashier/risk-event/handle-result";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:13.2.1 风控规则列表
     * @author: qingqing
     * @time:
     */
    public JSONObject risk_controlPage(String name, String type, String shop_type, Integer status, Integer page, Integer size) throws Exception {
        String url = "/patrol/risk-control/rule/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (type != "") {
            json = json + "\"type\" :\"" + type + "\",\n";
        }
        if (shop_type != "") {
            json = json + "\"shop_type\" :\"" + shop_type + "\",\n";
        }
        if (status != null) {
            json = json + "\"status\" :" + status + ",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 20.2 获取新增风控规则树结构
     * @author: qingqing
     * @time:
     */
    public JSONObject riskRuleTree() throws Exception {
        String url = "/patrol/risk-control/rule/tree";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:13.2.3 新增风控规则
     * @author: qingqing
     * @time:
     */
    public JSONObject riskRuleAdd(String name, String shop_type, JSONObject rule) throws Exception {
        String url = "/patrol/risk-control/rule/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"shop_type\" :\"" + shop_type + "\",\n" +
                        "\"rule\" :" + rule + "\n" +
                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), false);
    }

    /**
     * @description:13.2.4 风控规则删除
     * @author: qingqing
     * @time:
     */
    public JSONObject risk_controlDelete(long id) throws Exception {
        String url = "/patrol/risk-control/rule/delete";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:13.2.5 风控规则开关（前端传当前字段值即可，后端自动判断更改）
     * @author: qingqing
     * @time:
     */
    public JSONObject risk_controlPageSwitch(long id, Integer status) throws Exception {
        String url = "/patrol/risk-control/rule/switch";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"status\" :" + status + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.3.1 风控告警规则列表
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_rulePage(String name, String type, String accept_role, Integer status, Integer page, Integer size) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (type != "") {
            json = json + "\"type\" :\"" + type + "\",\n";
        }
        if (accept_role != "") {
            json = json + "\"accept_role\" :\"" + accept_role + "\",\n";
        }
        if (status != null) {
            json = json + "\"status\" :" + status + ",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.3.2 新建风控告警规则
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_ruleAdd(String name, String type, List rule_id_list, List accept_role_id_list, String start_time, String end_time, String silent_time) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"type\" :\"" + type + "\",\n" +
                        "\"rule_id_list\" :" + rule_id_list + ",\n" +
                        "\"accept_role_id_list\" :" + accept_role_id_list + ",\n" +
                        "\"start_time\" :\"" + start_time + "\",\n" +
                        "\"end_time\" :\"" + end_time + "\",\n" +
                        "\"silent_time\" :\"" + silent_time + "\"\n" +
                        "} ";
        return invokeApi(url, JSONObject.parseObject(json), false);
    }

    /**
     * http请求方法调用
     *
     * @param url         url
     * @param requestBody 请求体
     * @param checkCode   是否校验code
     * @return JSONObject response.data
     */
    public JSONObject invokeApi(String url, JSONObject requestBody, boolean checkCode) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url不可为空");
        }
        String request = JSON.toJSONString(requestBody);
        String result = null;
        if (checkCode) {
            result = httpPostWithCheckCode(url, request, IpPort);
            return JSON.parseObject(result).getJSONObject("data");
        } else {
            try {
                result = httpPost(url, request, IpPort);
            } catch (Exception e) {
                appendFailreason(e.toString());
            }
            return JSON.parseObject(result);
        }
    }

    /**
     * @description:13.3.3 风控告警规则详情
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_ruleDetail(long id) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/detail";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.3.4 编辑风控告警规则
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_ruleEdit(long id, String name, String type, List rule_id_list, List accept_role_id_list, String start_time, String end_time, String silent_time) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/edit";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"type\" :\"" + type + "\",\n" +
                        "\"rule_id_list\" :" + rule_id_list + ",\n" +
                        "\"accept_role_id_list\" :" + accept_role_id_list + ",\n" +
                        "\"start_time\" :\"" + start_time + "\",\n" +
                        "\"end_time\" :\"" + end_time + "\",\n" +
                        "\"silent_time\" :\"" + silent_time + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:13.3.5 删除风控告警规则
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_ruleDelete(long id) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/delete";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:13.3.6 风控告警规则开关（前端传当前字段值即可，后端自动判断更改）
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_ruleSwitch(long id, Integer status) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/switch";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"status\" :" + status + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.3.8 获取可用于风控告警配置的风控规则列表
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_rulesList(long id, Integer status) throws Exception {
        String url = "/patrol/risk-control/alarm-rule/list";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"status\" :" + status + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.4.1 风控告警事件列表
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_page(String name, String type, String shop_name, Integer page, Integer size) throws Exception {
        String url = "/patrol/risk-control/alarm/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (type != "") {
            json = json + "\"type\" :\"" + type + "\",\n";
        }
        if (shop_name != "") {
            json = json + "\"shop_name\" :\"" + shop_name + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * -------------------------------门店1.0APP-----------------------------------------------------------------------------------------------
    */


    /**
     * @description:2.1 搜索筛选-门店类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject typeSearch( Integer page, Integer size) throws Exception {
        String url = "/store/m-app/auth/shop/shop-type-list";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2.2搜索筛选-长逛店铺列表
     * @author: qingqing
     * @time:
     */
    public JSONObject longSee_list( Integer page, Integer size) throws Exception {
        String url = "/store/m-app/auth/shop/often-shop-list";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2.3 搜索筛选-省份地区列表
     * @author: qingqing
     * @time:
     */
    public JSONObject citySearch_list(Integer page, Integer size) throws Exception {
        String url = "/store/m-app/auth/shop/district-list";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2.4 搜索筛选-搜索门店
     * @author: qingqing
     * @time:
     */
    public JSONObject shopName_Search(String district_code ,JSONArray shop_type ,String shop_name,Integer page, Integer size) throws Exception {
        String url = "/store/m-app/auth/shop/shop-search";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\",\n" +
                        "\"shop_type\" :" + district_code + ",\n" +
                        "\"shop_name\" :\"" + shop_name + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2.5 门店详情页
     * @author: qingqing
     * @time:
     */
    public JSONObject app_shopDetail(String shop_id ) throws Exception {
        String url = "/store/m-app/auth/shop/shop-detail";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2.6 门店数量查询
     * @author: qingqing
     * @time:
     */
    public JSONObject app_shopNum() throws Exception {
        String url = "/store/m-app/auth/shop/shop-statistic";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2.7 切换门店
     * @author: qingqing
     * @time:
     */
    public JSONObject app_changeStore() throws Exception {
        String url = "/store/m-app/auth/shop/switch";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * @description:3.1 巡店中心
     * @author: qingqing
     * @time:
     */
    public JSONObject app_patrolCenter() throws Exception {
        String url = "/store/m-app/auth/patrol/center";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3.1 门店详情-设备列表
     * @author: qingqing
     * @time:
     */
    public JSONObject app_deviceList( long shop_id) throws Exception {
        String url = "/store/m-app/auth/patrol/device-list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @description:3.2 巡店-直播流
     * @author: qingqing
     * @time:
     */
    public JSONObject app_deviceList( long shop_id,String device_id) throws Exception {
        String url = "/store/m-app/auth/patrol/device-live";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"device_id\" :\"" + device_id + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }




    /**----------------------------------------------------------------------四. 账号相关---------------------------**/
    /**
     * @description:1.1 获取登录验证码
     * @author: qingqing
     * @time:
     */
    public JSONObject app_deviceList( String phone) throws Exception {
        String url = "/store/m-app/login-verification-code";
        String json =
                "{" +
                        "\"phone\" :\"" + phone + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:1.2 登录
     * @author: qingqing
     * @time:
     */
    public JSONObject app_login( String phone,String verification_code) throws Exception {
        String url = "/store/m-app/login";
        String json =
                "{" +
                        "\"verification_code\" :\"" + verification_code + "\",\n" +
                        "\"phone\" :\"" + phone + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:4.2 个人中心
     * @author: qingqing
     * @time:
     */
    public JSONObject user_center() throws Exception {
        String url = "/store/m-app/auth/user/center";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    //--------------------------------------五. 数据统计相关---------------------------
    /**
     * @description:5.3 门店详情-实时客流-用户画像
     * @author: qingqing
     * @time:
     */
    public JSONObject real_userInfo( long shop_id) throws Exception {
        String url = "/store/m-app/auth/shop/real-hour/age-gender/distribution";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:5.4 门店详情-实时客流-趋势图
     * @author: qingqing
     * @time:
     */
    public JSONObject real_pv_uv( long shop_id) throws Exception {
        String url = "/store/m-app/auth/shop/real-hour/pv-uv";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:5.5 门店详情-历史客流-用户画像
     * @author: qingqing
     * @time:
     */
    public JSONObject history_userInfo( long shop_id) throws Exception {
        String url = "/store/m-app/auth/shop/history/age-gender/distribution";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @description:5.6 门店详情-历史客流-到店趋势图
     * @author: qingqing
     * @time:
     */
    public JSONObject history_pv_uv( long shop_id) throws Exception {
        String url = "/store/m-app/auth/shop/history/age-gender/distribution";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:5.7 门店详情-历史客流-客群漏斗
     * @author: qingqing
     * @time:
     */
    public JSONObject history_conversion( long shop_id,String date,String cecle_type) throws Exception {
        String url = "/store/m-app/auth/shop/history/conversion";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"date\" :\"" + date + "\",\n" +
                        "\"cecle_type\" :\"" + cecle_type + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


}


