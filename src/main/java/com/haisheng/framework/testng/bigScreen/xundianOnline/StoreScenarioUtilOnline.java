package com.haisheng.framework.testng.bigScreen.xundianOnline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.testng.annotations.DataProvider;

public class StoreScenarioUtilOnline extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile StoreScenarioUtilOnline instance = null;
    public JSONArray patrolShopRealV3;

    private StoreScenarioUtilOnline() {
    }

    public static StoreScenarioUtilOnline getInstance() {

        if (null == instance) {
            synchronized (StoreScenarioUtilOnline.class) {
                if (null == instance) {
                    //这里
                    instance = new StoreScenarioUtilOnline();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://inspect.store.winsenseos.com";

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

    @DataProvider(name = "LOGINFO")
    public static Object[][] loginInfo() {

        return new String[][]{
                {"zhoudafu@winsense.ai", "d5f396edf97676490dd9e58a7cc60d51"},
                {"baiguoyuan@winsense.ai", "fb95eb1a95a5f061ae1b9d275bd36e02"},
                {"salesdemo@winsense.ai", "c216d5045fbeb18bcca830c235e7f3c8"}
        };
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

    @DataProvider(name = "SHOP_ID")
    public static Object[] shop_id() {

        return new Long[]{
                246l,
                1910l,
                1912l,
                1914l,
                1916l,
                1918l,
                1920l,
                1924l,
                1926l,
                1928l,
                1930l,
                1932l,
                1934l,
                1936l,
                1938l,
                1940l,
                1942l,
                1944l,
                1946l,
                1952l,
                1954l,
                1956l

        };
    }

    @DataProvider(name = "SHOP_ID_T")
    public static Object[] shop_id_t() {

        return new Long[]{
                15615l,//小天才西溪天街店
                15617l //小天才滨江宝龙店

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
                "440305"
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
                        "\"activity_description\" :" + activity_description + ",\n" +
                        "\"activity_type\" :\"" + activity_type + "\",\n" +
                        "\"start_date\" :\"" + start_date + "\",\n" +
                        "\"end_date\" :\"" + end_date + "\",\n" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
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
    public JSONObject historyShopMemberCountV3(String cycle_type) throws Exception {
        String url = "/patrol/history/shop/member/new/count";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\"\n" +
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


//    /**--------------------------------------------------------9.App端接口------------------------------------------------------------**/
//    /**
//     * @description:8.6.1 客群漏斗列表
//     * @author: qingqing
//     * @time:
//     */
//    public JSONObject shopPageConversionV3(String district_code,String shop_type,String shop_name,String shop_manager,String percentage_type,Integer percentage_type_order,Integer page,Integer size) throws Exception {
//        String url = "/patrol/shop/page/conversion";
//        String json =
//                "{" +
//                        "\"district_code\" :\"" + district_code + "\",\n" +
//                        "\"shop_type\" :\"" + shop_type + "\",\n" +
//                        "\"shop_name\" :\"" + shop_name + "\",\n" +
//                        "\"shop_manager\" :\"" + shop_manager + "\",\n" +
//                        "\"shop_name\" :\"" + shop_name + "\",\n" +
//                        "\"percentage_type\" :\"" + percentage_type + "\",\n" +
//                        "\"percentage_type_order\" :\"" + percentage_type_order + "\",\n" +
//                        "\"page\" :" + page + ",\n" +
//                        "\"size\" :" + size + ",\n" +
//                        "} ";
//
//        String res = httpPostWithCheckCode(url, json, IpPort);
//
//        return JSON.parseObject(res).getJSONObject("data");
//    }
}
