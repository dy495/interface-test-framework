package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.testng.annotations.DataProvider;

public class StoreScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     *
     * */

    private static volatile StoreScenarioUtil instance = null;
    public JSONArray patrolShopRealV3;

    private StoreScenarioUtil() {}
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



/**---------------------------------------------------门店相关V2.0-----------------------------------------------------**/

    /**
     * @description:8.1.1 门店列表(获取主账号下所有门店)
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreShopPage(String district_code,Integer page,Integer size) throws Exception {
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
     * @description:8.1.2 门店详情
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreShopDetail(Long shop_id) throws Exception {
        String url = "/patrol/shop/detail";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:8.2.1 查询周期列表
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreQueryClycleList() throws Exception {
        String url = "/patrol/query-cycle/list";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.3.1 客群漏斗
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreHistoryConversion(String cycle_type,String month,Long shop_id) throws Exception {
        String url = "/patrol/history/shop/conversion";
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
     * @description:8.3.2.1 获取天气类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreWeatherList(Long shop_id) throws Exception {
        String url = "/patrol/weather/type/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:8.3.2.1 获取到店趋势数据
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreHistoryTrend(String cycle_type,String month,Long shop_id) throws Exception {
        String url = "/patrol/history/shop/trend";
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
     * @description:8.3.3 到店时段分布
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreHistoryHourdata(String cycle_type,String month,Long shop_id) throws Exception {
        String url = "/patrol/history/shop/hour-data";
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
     * @description:8.3.4.1 获得店铺支持的活动事件类型列表
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreActivityList(Long shop_id) throws Exception {
        String url = "/patrol/activity/type/list";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.3.5.2 添加活动事件
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreActivityAdd(String activity_description,String activity_type,String start_date,String end_date,Long shop_id) throws Exception {
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

    /**
     * @description:8.4.1 门店实时pv和uv
     * @author: qingqing
     * @time:
     */
    public JSONObject realTimeTotal(Long shop_id) throws Exception {
        String url = "/patrol/real-time/shop/total";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:8.4.2 门店小时级别的pv
     * @author: qingqing
     * @time:
     */
    public JSONObject StoreRealTimePv(Long shop_id) throws Exception {
        String url = "/patrol/real-time/shop/pv";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    @DataProvider(name = "CYCLE_TYPE")
    public static Object[] cycle_type() {

        return new String[] {
                "RECENT_SEVEN",
                "RECENT_THIRTY",
                "RECENT_THIRTY",
                "RECENT_SIXTY"
        };
    }

    @DataProvider(name = "END_TIME_TYPE")
    public static Object[] endTimeType() {

        return new String[] {
                "2020-07-14",
                "2020-07-18"

        };
    }

    @DataProvider(name = "DESCRIPTION")
    public static Object[] description() {

        return new String[] {
                "店庆店庆店庆店庆店庆",
                "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆"
        };
    }

    @DataProvider(name = "DESCRIPTION_FALSE")
    public static Object[] description_false() {

        return new String[] {
                "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆",
                ""
        };
    }

    @DataProvider(name = "THING_TYPE")
    public static Object[] thing_type() {

        return new String[] {
                "NEW_COMMODITY",
                "CHANGE_COMMODITY",
                "PROMOTIONS"
        };
    }

    @DataProvider(name = "THING_TYPE_FALSE")
    public static Object[] thing_type_false() {

        return new String[] {
                ""
        };
    }

    @DataProvider(name = "TIME_TYPE_FALSE")
    public static Object[] time_type_false() {

        return new String[] {
                "2019-08-08",
                "2020-07-09",
                ""
        };
    }
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
    public JSONObject patrolShopRealV3(String district_code,String shop_type,Integer page,Integer size) throws Exception {
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
     * @description:8.1.2 门店列表(获取主账号下所有门店)V3.0
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopPageV3(String district_code,Integer page,Integer size) throws Exception {
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
    public JSONObject ShopPageHistoryV3(String district_code,String shop_type,String shop_name,String shop_manager,Integer page,Integer size) throws Exception {
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
    public JSONObject ShopPageMemberV3(String district_code,String shop_type,String shop_name,String shop_manager,String member_type,Integer member_type_order	,Integer page,Integer size) throws Exception {
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
    public JSONObject historyShopTrendV3(String cycle_type,String month,long shop_id) throws Exception {
        String url = "/patrol/history/shop/trend";
        String json =
                "{" +
                        "\"cycle_type\" :" + cycle_type + ",\n" +
                        "\"month\" :" + month + ",\n" +
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
    public JSONObject activityAddV3(String activity_description,String activity_type,String start_date,String end_date,long shop_id) throws Exception {
        String url = "/patrol/activity/add";
        String json =
                "{" +
                        "\"activity_description\" :" + activity_description + ",\n" +
                        "\"activity_type\" :" + activity_type + ",\n" +
                        "\"start_date\" :" + start_date + ",\n" +
                        "\"end_date\" :" + end_date + ",\n" +
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
        String url = "/patrol/real-time/shop/pv";
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
    public JSONObject memberVisitListV3(long shop_id,String member_type,Integer face_register,Integer gender,String member_id,Integer page,Integer size) throws Exception {
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
    public JSONObject memberPrivacyAgreeV3(long shop_id,String member_id) throws Exception {
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
     * @description:8.5.5 会员增长趋势
     * @author: qingqing
     * @time:
     */
    public JSONObject historyShopMemberV3(long shop_id,String cycle_type,String month) throws Exception {
        String url = "/patrol/history/shop/member";
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



    /**--------------------------------------------------------8.6 客群漏斗------------------------------------------------------------**/
    /**
     * @description:8.6.1 客群漏斗列表
     * @author: qingqing
     * @time:
     */
    public JSONObject shopPageConversionV3(String district_code,String shop_type,String shop_name,String shop_manager,String percentage_type,Integer percentage_type_order,Integer page,Integer size) throws Exception {
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
    public JSONObject historyShopConversionV3(long shop_id,String cycle_type,String month) throws Exception {
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
    public JSONObject historyShopHourV3(long shop_id,String cycle_type,String month) throws Exception {
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
    public JSONObject historyShopAgeV3(long shop_id,String cycle_type,String month) throws Exception {
        String url = "/patrols/history/shop/age-gender/distribution";
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
