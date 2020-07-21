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
     *
     * */

    private static volatile StoreScenarioUtilOnline instance = null;

    private StoreScenarioUtilOnline() {}
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
    public String IpPort = "http://inspect.store.winsenseos.com/";
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


    /*
    3.3新建定检任务
   */
    public JSONObject scheduleCheckAdd(String name,String cycle,JSONArray dates,String send_time,String valid_start,String valid_end,String inspector_id,JSONArray shop_list) throws Exception {
        String url = "/patrol/schedule-check/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"cycle\" :\"" + cycle + "\",\n" +
                        "\"dates\" :" + dates + ",\n" +
                        "\"send_time\" :\"" + send_time + "\",\n" +
                        "\"valid_start\" :\"" + valid_start + "\",\n" +
                        "\"valid_end\" :\"" + valid_end + "\",\n" +
                        "\"inspector_id\" :\"" + inspector_id + "\",\n" +
                        "\"shop_list\" :" + shop_list + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res);
    }
    /*
    3.4 定检任务列表
    */
    public JSONObject scheduleCheckPage(int page,int size) throws Exception {
        String url = "/patrol/schedule-check/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
   3.5 删除定检任务
   */
    public JSONObject scheduleCheckDelete(long id) throws Exception {
        String url = "/patrol/schedule-check/delete";
        String json =
                "{" +
                        "\"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
    4.1 获取门店列表
    */
    public JSONObject ShopPage(int page,int size) throws Exception {
        String url = "/patrol/shop/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /*
     4.2 获取门店详情
   */
    public JSONObject shopDetail(Long id) throws Exception {
        String url = "/patrol/shop/detail";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
    4.3 获取门店巡店记录列表
   */
    public JSONObject shopChecksPage(int page,int size,long shop_id) throws Exception {
        String url = "/patrol/shop/checks/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + ",\n"+
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
   4.4 获取门店巡店记录详情
  */
    public JSONObject shopChecksDetail(Integer id,long shop_id) throws Exception {
        String url = "/patrol/shop/checks/detail";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n"+
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
     4.8 开始或继续巡店
   */
    public JSONObject shopChecksStart(Long shop_id,String check_type,Integer reset,Long task_id) throws Exception {
        String url = "/patrol/shop/checks/start";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"check_type\" :\"" + check_type + "\",\n" +
                        "\"reset\" :" + reset + ",\n" +
                        "\"task_id\" :" + task_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
    4.9 提交巡检项目结果
   */
    public JSONObject shopChecksItemSubmit(Long shop_id,Long patrol_id,Long list_id,Long item_id,Integer check_result,String audit_comment,JSONArray pic_list) throws Exception {
        String url = "/patrol/shop/checks/item/submit";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"patrol_id\" :" + patrol_id + ",\n" +
                        "\"list_id\" :" + list_id + ",\n" +
                        "\"item_id\" :" + item_id + ",\n" +
                        "\"check_result\" :" + check_result + ",\n" +
                        "\"audit_comment\" :\"" + audit_comment + "\",\n" +
                        "\"pic_list\" :" + pic_list + "\n" +
                        "} ";
        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
   4.10 提交巡检结果
  */
    public JSONObject shopChecksSubmit(Long shop_id,Long id,String comment) throws Exception {
        String url = "/patrol/shop/checks/submit";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"id\" :" + id + ",\n" +
                        "\"comment\" :\"" + comment + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }
    /*
   5.1 获取工作成果
    */
    public JSONObject taskDetail() throws Exception {
        String url = "/patrol/m/task/detail";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * @description :不合格留痕截屏
     * @date :2020/6/21 10:58
     **/
    public JSONObject picUpload(int type,String pic_data)throws Exception{
        String url="/patrol/pic/base64/upload";
        JSONObject json=new JSONObject();
        json.put("shop_id",getXunDianShop());
        json.put("type",type);
        json.put("pic_data",pic_data);
        String res = httpPostWithCheckCode(url, json.toJSONString(),IpPort);

        return JSON.parseObject(res).getJSONObject("data");


    }

    /*
      5.3 待办/已办列表
  */
    public JSONObject MTaskList(Integer type,Integer size,Long last_id) throws Exception {
        String url = "/patrol/m/task/list";
        String json =
                "{" +
                        "\"type\" :" + type + ",\n" +
                        "\"size\" :" + size + ",\n" +
                        "\"last_id\" :" + last_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
   5.5 复检、不合格处理步骤提交
   */
    public JSONObject MstepSumit(Long shop_id,Long id,String comment,JSONArray pic_list,Integer recheck_result) throws Exception {
        String url = "/patrol/m/task/step/submit";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n" +
                        "\"id\" :" + id + ",\n" +
                        "\"comment\" :\"" + comment + "\",\n" +
                        "\"pic_list\" :" + pic_list + ",\n" +
                        "\"recheck_result\" :" + recheck_result + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res);
    }
    /*
   6.2 新建执行清单
   */
    public JSONObject checkListAdd(String name,String desc,JSONArray items,JSONArray shop_list) throws Exception {
        String url = "/patrol/check-list/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"desc\" :\"" + desc + "\",\n" +
                        "\"items\" :" + items + ",\n" +
                        "\"shop_list\" :" + shop_list + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }
    /*
   6.3 执行清单列表
    */
    public JSONObject checklistPage(int page,int size) throws Exception {
        String url = "/patrol/check-list/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n"
                        +"} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
   6.4 删除执行清单任务
  */
    public JSONObject checkListDelete(long id) throws Exception {
        String url = "/patrol/check-list/delete";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }
    /*
   6.5 执行清单详情
  */
    public JSONObject checkListDetail(long id) throws Exception {
        String url = "/patrol/check-list/detail";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /*
   6.6 编辑执行清单
   */
    public JSONObject checkListEdit(Long id,String name,String desc,JSONArray items,JSONArray shop_list) throws Exception {
        String url = "/patrol/check-list/edit";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"desc\" :\"" + desc + "\",\n" +
                        "\"items\" :" + items + ",\n" +
                        "\"shop_list\" :" + shop_list + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }


/**---------------------------------------------------门店相关-----------------------------------------------------**/
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

}
