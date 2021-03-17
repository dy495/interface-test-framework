package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class XundianScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile XundianScenarioUtil instance = null;

    private XundianScenarioUtil() {
    }

    public static XundianScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (XundianScenarioUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new XundianScenarioUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://123.57.148.247";

    //----------------------登陆--------------------
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
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");
    }

    /*
    3.3新建定检任务
   */
    public JSONObject scheduleCheckAdd(String name, String cycle, JSONArray dates, String send_time, String valid_start, String valid_end, String inspector_id, JSONArray shop_list) throws Exception {
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
    public JSONObject scheduleCheckPage(int page, int size) throws Exception {
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
        return JSON.parseObject(res);
    }

    /*
  3.6 编辑定检任务
  */
    public JSONObject scheduleCheckEdit(long id, String name, String cycle, JSONArray dates, String send_time, String valid_start, String valid_end, String inspector_id, JSONArray shop_list) throws Exception {
        String url = "/patrol/schedule-check/edit";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
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
    4.1 获取门店列表
    */
    public JSONObject ShopPage(int page, int size) throws Exception {
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
    public JSONObject shopChecksPage(int page, int size, long shop_id) throws Exception {
        String url = "/patrol/shop/checks/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + ",\n" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
   4.4 获取门店巡店记录详情
  */
    public JSONObject shopChecksDetail(Integer id, long shop_id) throws Exception {
        String url = "/patrol/shop/checks/detail";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
     4.8 开始或继续巡店
   */
    public JSONObject shopChecksStart(Long shop_id, String check_type, Integer reset, Long task_id) throws Exception {
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
    public JSONObject shopChecksItemSubmit(Long shop_id, Long patrol_id, Long list_id, Long item_id, Integer check_result, String audit_comment, JSONArray pic_list) throws Exception {
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
    public JSONObject shopChecksSubmit(Long shop_id, Long id, String comment) throws Exception {
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
      5.3 待办/已办列表
  */
    public JSONObject MTaskList(Integer type, Integer size, Long last_id) throws Exception {
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
    public JSONObject MstepSumit(Long shop_id, Long id, String comment, JSONArray pic_list, Integer recheck_result) throws Exception {
        String url = "/store/m-app/auth/task/step/submit";
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
    public JSONObject checkListAdd(String name, String desc, JSONArray items, JSONArray shop_list) throws Exception {
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
    public JSONObject checklistPage(int page, int size) throws Exception {
        String url = "/patrol/check-list/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n"
                        + "} ";

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
    public JSONObject checkListEdit(Long id, String name, String desc, JSONArray items, JSONArray shop_list) throws Exception {
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

    /*
10.1 新建定检规则(2020-08-13)
*/
    public JSONObject scheduleRuleAdd(String name, String start_time, String end_time, int interval_hour, JSONArray shop_list) throws Exception {
        String url = "/patrol/schedule-rule/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"start_time\" :\"" + start_time + "\",\n" +
                        "\"end_time\" :\"" + end_time + "\",\n" +
                        "\"interval_hour\" :" + interval_hour + ",\n" +
                        "\"shop_list\" :" + shop_list + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /*
   10.2 定检规则详情页(2020-08-13)
   */
    public JSONObject scheduleRuleDetail(long id) throws Exception {
        String url = "/patrol/schedule-rule/detail";
        String json =
                "{" +

                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /*
  10.3 定检规则列表(2020-08-13)
  */
    public JSONObject scheduleRuleList(int page, int size) throws Exception {
        String url = "/patrol/schedule-rule/list";
        String json =
                "{" +

                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*
10.3 编辑定检规则(2020-08-13)
*/
    public JSONObject scheduleRuleEdit(String name, String start_time, String end_time, int interval_hour, JSONArray shop_list, int id) throws Exception {
        String url = "/patrol/schedule-rule/edit";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"start_time\" :\"" + start_time + "\",\n" +
                        "\"end_time\" :\"" + end_time + "\",\n" +
                        "\"interval_hour\" :" + interval_hour + ",\n" +
                        "\"shop_list\" :" + shop_list + ",\n" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /*
10.3 删除定检规则(2020-08-13)
*/
    public JSONObject scheduleRuleDelete(JSONArray rule_ids) throws Exception {
        String url = "/patrol/schedule-rule/delete";
        String json =
                "{" +
                        "\"rule_ids\" :" + rule_ids + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /*
   10.3 定检规则的开关(2020-08-13)
   */
    public JSONObject scheduleRuleSwith(int id, int status) throws Exception {
        String url = "/patrol/schedule-rule/switch";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"status\" :" + status + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

//---------------------------------------------------分界线-----------------------------------
    /**
     * 巡检门店
     */
    public JSONObject shopList(String inspectorId, String districtCode) throws Exception {
        String url = "/patrol/schedule-check/shop/list";
        String json =
                "{\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"district_code\":\"" + districtCode + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //门店详情
    public JSONObject taskDetail() throws Exception {
        String url = "/patrol/m/task/detail";
        JSONObject json = new JSONObject();
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * 巡检员列表
     *
     * @return
     * @throws Exception
     */
    public JSONObject inspectorList() throws Exception {
        String url = "/patrol/schedule-check/inspector/list";
        JSONObject json = new JSONObject();
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        System.out.println(res);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //app开始巡店,适用于定检任务巡店
    public JSONObject checkStartapp(Long shop_id, String check_type, Integer reset, Long task_id) throws Exception {
        String url = "/store/m-app/auth/shop/checks/start";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("check_type", check_type);
        json.put("reset", reset);
        json.put("task_id", task_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //适用于现场巡店
    public JSONObject checkStartapp(Long shop_id, String check_type, Integer reset,boolean is_personalized_check_list) throws Exception {
        String url = "/store/m-app/auth/shop/checks/start";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("check_type", check_type);
        json.put("reset", reset);
        json.put("is_personalized_check_list", is_personalized_check_list);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @description :新增清单
     * @date :2020/6/20 16:42
     **/
    public JSONObject CheckListAdd(String name, String desc, String title, String comment) throws Exception {
        String url = "/patrol/check-list/add";
        //TODO：此处优化动态查询店铺list
        List<Long> shop_list = new ArrayList<>();
        long i = Long.valueOf(getXunDianShop());
        shop_list.add(i);

        JSONObject json1 = new JSONObject();
        json1.put("order", 1);
        json1.put("title", title);
        json1.put("comment", comment);

        JSONArray item = new JSONArray();
        item.add(0, json1);

        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("desc", desc);
        json.put("items", item);
        json.put("shop_list", shop_list);


        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        System.out.println(res);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * @description :新建定检任务
     * @date :2020/6/21 9:58
     **/
    public JSONObject addScheduleCheck(String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                       String inspectorId) throws Exception {
        String url = "/patrol/schedule-check/add";
        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":" + dates + ",\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + getXunDianShop() + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * 不合格提交图片
     */
    public JSONObject checksItemSubmitN(long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/shop/checks/item/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("patrol_id", patrolId);
        json.put("list_id", listId);
        json.put("item_id", itemId);
        json.put("pic_list", picList);
        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :不合格图片提交返回code
     * @date :2020/6/22 20:54
     **/

    public Integer checksItemSubmitY(long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/shop/checks/item/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("patrol_id", patrolId);
        json.put("list_id", listId);
        json.put("item_id", itemId);
        json.put("pic_list", picList);

        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getInteger("code");
    }

    /**
     * submit one
     */
    public JSONObject submitOne(Integer check_result, long item_id, long list_id, long patrol_id) throws Exception {
        String url = "/patrol/shop/checks/item/submit";
        JSONObject json = new JSONObject();
        String shopid = getXunDianShop();
        json.put("shop_id", shopid);
        json.put("check_result", check_result);
        json.put("item_id", item_id);
        json.put("list_id", list_id);
        json.put("patrol_id", patrol_id);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject submitOne(Integer check_result, long item_id, long list_id, long patrol_id, String audit_comment) throws Exception {
        String url = "/patrol/shop/checks/item/submit";
        JSONObject json = new JSONObject();
        String shopid = getXunDianShop();
        json.put("shop_id", shopid);
        json.put("check_result", check_result);
        json.put("item_id", item_id);
        json.put("list_id", list_id);
        json.put("patrol_id", patrol_id);
        json.put("audit_comment", audit_comment);


        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * start 巡店
     */
    public JSONObject checkStart(String check_type, Integer reset) throws Exception {
        String url = "/patrol/shop/checks/start";
        String json = "{\n" +
                "    \"shop_id\":" + getXunDianShop() + ",\n" +
                "    \"check_type\":" + check_type + ",\n" +
                "    \"reset\":" + reset + "\n" +
                "}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :不合格留痕截屏
     * @date :2020/6/21 10:58
     **/
    public JSONObject picUpload(int type, String pic_data) throws Exception {
        String url = "/patrol/pic/base64/upload";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("type", type);
        json.put("pic_data", pic_data);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");


    }

    /**
     * checks submit
     */
    public JSONObject checkSubmit(String commit, Long id) throws Exception {
        String url = "/patrol/shop/checks/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("comment", commit);
        json.put("id", id);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * shop detail
     */
    public JSONObject xunDianCenterDetail() throws Exception {
        String url = "/patrol/shop/detail";
        JSONObject json = new JSONObject();
        json.put("id", getXunDianShop());

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :门店详情页 page
     * @date :2020/6/23 18:42
     **/
    public JSONObject xunDianCenterPage(int page, int size) throws Exception {
        String url = "/patrol/shop/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :门店详情页 page
     * @date :2020/6/23 18:42
     **/
    public JSONObject xunDianCenterselect(int page, int size, String name) throws Exception {
        String url = "/patrol/shop/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description :巡店详情
     * @date :2020/6/24 15:26
     **/
    //巡店结果+处理状态+巡店者
    public JSONObject xundianDetil(int check_result, int page, int size, int handle_status, String inspector_id) throws Exception {
        String url = "/patrol/shop/checks/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("check_result", check_result);
        json.put("page", page);
        json.put("size", size);
        json.put("handle_status", handle_status);
        json.put("inspector_id", inspector_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //巡店结果+处理状态
    public JSONObject xundianDetil(int check_result, int page, int size, int handle_status) throws Exception {
        String url = "/patrol/shop/checks/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("check_result", check_result);
        json.put("page", page);
        json.put("size", size);
        json.put("handle_status", handle_status);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :门店巡检员列表
     * @date :2020/6/24 16:47
     **/
    public JSONObject mendianinSpectorList() throws Exception {
        String url = "/patrol/shop/inspectors";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //门店详情页列表信息
    public JSONObject xundianDetilpage(int page, int size) throws Exception {
        String url = "/patrol/shop/checks/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("page", page);
        json.put("size", size);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :门店巡店详情
     * @date :2020/6/24 18:43
     **/
    public JSONObject xundianCheckpage(Long id) throws Exception {
        String url = "/patrol/shop/checks/detail";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("id", id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :截屏留痕
     * @date :2020/6/25 13:56
     **/
    public JSONObject problemMark(String responsor_id, Long list_id, Long item_id, JSONArray pic_list, String audit_comment) throws Exception {
        String url = "/patrol/shop/problem/mark";
        JSONObject json = new JSONObject();
        json.put("responsor_id", responsor_id);
        json.put("list_id", list_id);
        json.put("item_id", item_id);
        json.put("pic_list", pic_list);
        json.put("shop_id", getXunDianShop());
        json.put("audit_comment", audit_comment);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :截屏留痕+限时整改时间
     * @date :2020/6/25 13:56
     **/
    public JSONObject problemMarkTime(String responsor_id, Long list_id, Long item_id, JSONArray pic_list, String audit_comment,int limit_time) throws Exception {
        String url = "/patrol/shop/problem/mark";
        JSONObject json = new JSONObject();
        json.put("responsor_id", responsor_id);
        json.put("list_id", list_id);
        json.put("item_id", item_id);
        json.put("pic_list", pic_list);
        json.put("shop_id", getXunDianShop());
        json.put("audit_comment", audit_comment);
        json.put("limit_time", limit_time);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :截屏留痕时，获取店铺整改负责人uid  /patrol/m/shop/problem/responsors
     * @date :2020/6/25 16:47
     **/
    public JSONObject problemesponsors() throws Exception {
        String url = "/patrol/shop/problem/responsors";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :门店 清单项目列表
     * @date :2020/6/25 17:18
     **/
    public JSONObject problemeItems() throws Exception {
        String url = "/patrol/shop/problem/items";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description :远程巡店时，批量处理清单清单项数
     * @date :
     **/
    public JSONObject submitAllItem(int shop_id,int patrol_id,int list_id) throws Exception {
        String url = "patrol/shop/check/item/submit_all_item";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("patrol_id,", patrol_id);
        json.put("list_id", list_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }




    /**
     * @author zhoutao
     * @description 3.13 获取门店巡检员列表（V1.1）
     */
    public JSONObject authShopInspectors(Long shop_id)throws Exception {
        String url = "/patrol/m-app/auth/shop/inspectors";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject logout() throws Exception {
        String url = "/m/patrol-logout";
        JSONObject json = new JSONObject();
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * app checks submit 定检任务单项审核之后，总提交
     */
    public JSONObject responsors(Long shop_id) throws Exception {
        String url = "/patrol/m/shop/problem/responsors";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    @DataProvider(name = "TASK_TYPE")
    public static Object[] task_type() {

        return new String[]{
                "SCHEDULE_UNQUALIFIED",
                "REMOTE_UNQUALIFIED",
                "SPOT_UNQUALIFIED",
                "RECHECK_UNQUALIFIED",
        };
    }

    @DataProvider(name = "CHECK_TYPE")
    public static Object[] check_type() {

        return new String[]{
                "REMOTE",
                "SPOT",
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
    /**
     * ***********************************************二十六、巡店分析(2020-11-21)************************************************
     */


    /**
     * @description:1. 巡店概况
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_data() throws Exception {
        String url = "/patrol/patrol-analysis-data/overview";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:2. 巡店核心指标
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_indeicators(String cycle_type,String month) throws Exception {
        String url = "/patrol/patrol-analysis-data/core-indicators";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3. 巡店不合格项趋势
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_uncheckTotal(String cycle_type,String month) throws Exception {
        String url = "/patrol/patrol-analysis-data/unqualified-check-total";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:4. 问题分析
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_question(String cycle_type,String month) throws Exception {
        String url = "/patrol/patrol-analysis-data/problem";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:5. 异常图片
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_pictrue(String cycle_type,String month,String day) throws Exception {
        String url = "/patrol/patrol-analysis-data/abnormal-picture";
        String json =
                "{" +
                        "\"cycle_type\" :\"" + cycle_type + "\",\n" +
                        "\"month\" :\"" + month + "\",\n" +
                        "\"day\" :\"" + day + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * ***********************************************二十七、巡店报告中心（2020-11-21）************************************************
     */

    /**
     * @description:1. 巡店报告中心列表
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_report_list(String patrol_person,String shop_name,String report_status,String deal_status,Integer sort_event_type_order,Integer page,Integer size) throws Exception {
        String url = "/patrol/patrol-report/report-list";

        JSONObject json = new JSONObject();
        json.put("patrol_person", patrol_person);
        json.put("shop_name", shop_name);
        json.put("report_status", report_status);
        json.put("deal_status", deal_status);
        json.put("sort_event_type_order", sort_event_type_order);
        json.put("page", page);
        json.put("size", size);


        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ***********************************************三. 巡店相关(APP1.1)************************************************
     */

    /**
     * @description:3.0 巡店中心
     * @author: qingqing
     * @time:
     */
    public JSONObject patrol_center() throws Exception {
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
    public JSONObject device_list(long shop_id) throws Exception {
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
    public JSONObject device_live(String device_id,Long shop_id) throws Exception {
        String url = "/store/m-app/auth/patrol/device-live";
        JSONObject json = new JSONObject();
        json.put("device_id", device_id);
        json.put("shop_id", shop_id);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * @description:3.3 获取设备重播流
     * @author: qingqing
     * @time:
     */
    public JSONObject device_replay(String device_id,long shop_id,String date,String time ) throws Exception {
        String url = "/store/m-app/auth/shop/device/replay";

        JSONObject json = new JSONObject();
        json.put("device_id", device_id);
        json.put("shop_id", shop_id);
        json.put("date", date);
        json.put("time", time);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     * @description:3.5 待办/已办列表（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject task_list(Integer page,Integer size,Integer type,Long last_value) throws Exception {
        String url = "/store/m-app/auth/patrol/task/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
        json.put("last_value", last_value);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @description:3.6 待办/已办列表详情（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject task_detail(Long id,Integer type) throws Exception {
        String url = "/store/m-app/auth/patrol/task/detail";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"type\" :" + type + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3.7 复检、不合格处理步骤提交（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject task_step_submit(Long shop_id,Long id,JSONArray pic_list,Integer recheck_result,String comment)  {
        String url = "/store/m-app/auth/patrol/task/step/submit";
        JSONObject object = new JSONObject();
        object.put("shop_id", shop_id);
        object.put("id", id);
        object.put("pic_list", pic_list);
        object.put("recheck_result", recheck_result);
        object.put("comment", comment);
        return invokeApi(url, object,false);
      //  return JSON.parseObject(res);
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
                appendFailReason(e.toString());
            }
            return JSON.parseObject(result);
        }
    }
    /**
     * @description:3.8 开始或继续巡店(V1.1)
     * @author: qingqing
     * @time:
     */
    public JSONObject shopChecks_start(Long shop_id,String check_type,Integer reset,Long task_id) throws Exception {
        String url = "/store/m-app/auth/shop/checks/start";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("check_type", check_type);
        json.put("reset", reset);
        json.put("task_id", task_id);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3.9 提交巡检项目结果（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject checks_item_submit(Long shop_id,Long patrol_id,Long list_id,Long item_id,Integer check_result,String audit_comment,JSONArray pic_list) throws Exception {
        String url = "/store/m-app/auth/shop/checks/item/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("patrol_id", patrol_id);
        json.put("list_id", list_id);
        json.put("item_id", item_id);
        json.put("check_result", check_result);
        json.put("audit_comment", audit_comment);
        json.put("pic_list", pic_list);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * @description:3.10 提交巡检结果（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject checks_submit(Long shop_id,Long id,String comment) throws Exception {
        String url = "/store/m-app/auth/shop/checks/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        json.put("comment", comment);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    public JSONObject checks_submitNotChk(Long shop_id,Long patrol_id,String comment) throws Exception {
        String url = "/store/m-app/auth/shop/checks/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", patrol_id);
        json.put("comment", comment);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     * @description:3.11 门店当前清单项目列表（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject problem_items(Long shop_id) throws Exception {
        String url = "/store/m-app/auth/shop/problem/items";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3.12 留痕（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject problem_items(Long shop_id,Long patrol_id,Long list_id,Long item_id,Integer check_result,String audit_comment,JSONArray pic_list) throws Exception {
        String url = "/store/m-app/auth/shop/problem/mark";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("patrol_id", patrol_id);
        json.put("list_id", list_id);
        json.put("item_id", item_id);
        json.put("check_result", check_result);
        json.put("audit_comment", audit_comment);
        json.put("pic_list", pic_list);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3.13 获取门店巡检员列表（V1.1）
     * @author: qingqing
     * @time:
     */
    public JSONObject inspectors_list(Long shop_id) throws Exception {
        String url = "/store/m-app/auth/shop/inspectors";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:3.14 设备定检图片列表（V1.1）
     * @author: qingqing
     * @time:
     */
    /**
     * app checks submit 3.15 获取门店巡店记录列表（V1.1）
     */
    public JSONObject schedule_pic_list(Long shop_id,String device_id,String date) throws Exception {
        String url = "/store/m-app/auth/patrol/task/schedule-pic/list";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("device_id",device_id);
        json.put("date", date);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * app checks submit 3.15 获取门店巡店记录列表（V1.1）
     */
    public JSONObject getShopChecksPage(Long shop_id, Integer check_result,Integer handle_status,String inspector_name,String inspector_id,String order_rule,Integer size,Long last_value) throws Exception {
        String url = "/store/m-app/auth/shop/checks/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("check_result",check_result);
        json.put("handle_status", handle_status);
        json.put("inspector_name",inspector_name);
        json.put("inspector_id",inspector_id);
        json.put("order_rule",order_rule);
        json.put("size",size);
        json.put("last_value",last_value);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * app checks submit 3.16获取门店巡店记录详情（V1.1）
     */
    public JSONObject getShopChecksDetail(Long id,Long shop_id,Long check_list_id,Long check_result) throws Exception {
        String url = "/store/m-app/auth/shop/checks/detail";
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("shop_id", shop_id);
        json.put("check_list_id",check_list_id);
        json.put("check_result",check_result);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @author zhoutao
     * @description 3.18 巡店记录处理事项下拉列表（V1.1）
     */
    public JSONObject handleStatusList() throws Exception {
        String url = "/store/m-app/auth/shop/handle_status/list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @author zhoutao
     * @description 3.19 巡店记录巡店结果下拉列表（V1.1）
     */
    public JSONObject resultTypeList() throws Exception {
        String url = "/store/m-app/auth/shop/result-type/list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @author zhoutao
     * @description 3.20 巡店报告详情执行项结果下拉列表（V1.1）
     */
    public JSONObject checkResultList()throws Exception{
        String url = "/store/m-app/auth/shop/check_result/list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @author qingqing
     * @description 3.21 摄像头云台控制（V1.1）
     */
    public JSONObject device_control(String device_id,String command) throws Exception {
        String url = "/store/m-app/auth/shop/device/control";
        JSONObject json = new JSONObject();
        json.put("device_id",device_id);
        json.put("command", command);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 3.22 移动端base64图片上传（V1.1）
     */
    public JSONObject upload_pic(String pic_data,Long shop_id,Integer type) throws Exception {
        String url = "/store/m-app/auth/pic/base64/upload";
        JSONObject json = new JSONObject();
        json.put("pic_data",pic_data);
        json.put("shop_id", shop_id);
        json.put("type", type);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @author qingqing
     * @description 3.26 获取当前巡店记录详情清单列表（V1.1）
     */
    public JSONObject patrol_detail(Long shop_id,Long id) throws Exception {
        String url = "/store/m-app/auth/shop/problem/patrol-detail-items";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @author qingqing
     * @description 3.26 获取当前巡店记录详情清单列表（V1.1）
     */
    public JSONObject patrol_pic(String patrol_type,String start_time,String end_time,String shop_name,Integer is_abnormal,Integer page,Integer size) throws Exception {
        String url = "/patrol/shop/remark/picture/page";
        JSONObject json = new JSONObject();
        json.put("patrol_type", patrol_type);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        json.put("shop_name", shop_name);
        json.put("is_abnormal", is_abnormal);
        json.put("page", page);
        json.put("size", size);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

   /**---------------------------------------------------四、账号相关app1.1----------------------------------------------------**/
    /**
     * @author qingqing
     * @description 1.1 获取登录验证码（V1.1）
     */
    public JSONObject getCode(String phone) throws Exception {
        String url = "/store/m-app/login-verification-code";
        JSONObject json = new JSONObject();
        json.put("phone",phone);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 1.2 登录（V1.1）
     */
    public JSONObject loginWay(String phone,String verification_code) throws Exception {
        String url = "/store/m-app/login";
        JSONObject json = new JSONObject();
        json.put("phone",phone);
        json.put("verification_code",verification_code);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @author qingqing
     * @description 1.3 通过token获取用户信息（V1.1）
     */
    public JSONObject getUserInfo() throws Exception {
        String url = "/store/m-app/auth/login-user/detail";
        JSONObject json = new JSONObject();
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 1.4 登出（V1.1）
     */
    public JSONObject loginOut() throws Exception {
        String url = "/store/m-app/logout";
        JSONObject json = new JSONObject();
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 1.5 人脸检测（V1.1）
     */
    public JSONObject face_check(String image_base64) {
        String url = "/store/m-app/auth/login-user/face-check";
        JSONObject json = new JSONObject();
        json.put("image_base64",image_base64);
        return invokeApi(url, json,false);
       // String res = httpPost(url, json.toJSONString(), IpPort);
       // return JSON.parseObject(res);
    }

    /**
     * @author qingqing
     * @description1.6 人脸上传（V1.1）
     */
    public JSONObject face_unload(String face_url) throws Exception {
        String url = "/store/m-app/auth/login-user/face-upload";
        JSONObject json = new JSONObject();
        json.put("face_url",face_url);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 1.7 个人中心(V1.1新增字段)（V1.1）
     */
    public JSONObject user_center() throws Exception {
        String url = "/store/m-app/auth/user/center";
        JSONObject json = new JSONObject();
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @author zt
     * @description 1.7 个人中心(V1.1新增字段)（V1.1） 消息中心列表
     */
    public JSONObject user_message_center(Boolean is_read,Integer last_value,int size) throws Exception {
        String url = "/store/m-app/auth/user/message-center";
        JSONObject json = new JSONObject();
        json.put("is_read",is_read);
        json.put("last_value",last_value);
        json.put("size",size);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @author zt
     * @description 1.7 个人中心(V1.1新增字段)（V1.1） 消息中心列表
     */
    public JSONObject user_message_center_detail(int id) throws Exception {
        String url = "/store/m-app/auth/user/message-center-detail";
        JSONObject json = new JSONObject();
        json.put("id",id);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //我的报表-报表类型枚举
    public JSONObject reporttype() throws Exception {
        String url = "/patrol/download-center/report-type-list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //我的报表-报表时间维度枚举
    public JSONObject reporttime() throws Exception {
        String url = "/patrol/download-center/report-time-type-list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //我的报表-列表
    public JSONObject reportList(int page,int size, String report_name, String report_type,String report_time_dimensio,String shop_name) throws Exception {
        String url = "/patrol/download-center/my-report";
        JSONObject json = new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("report_name",report_name);
        json.put("report_type",report_type);
        json.put("report_time_dimensio",report_time_dimensio);
        json.put("shop_name",shop_name);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //我的报表-导出
    public JSONObject reportExport(Integer id) throws Exception {
        String url = "/patrol/download-center/report-export";
        JSONObject json = new JSONObject();
        json.put("id",id);
        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res);
    }


    //我的报表-自定义导出
    public JSONObject customizeReportExport(Integer id,String report_type,String report_time_dimension,JSONArray shop_id_List,String start_time,String end_time,String data_dimension) throws Exception {
        String url = "/patrol/download-center/report-export";
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("report_type",report_type);
        json.put("report_time_dimension",report_time_dimension);
        json.put("shop_id_List",shop_id_List);
        json.put("start_time",start_time);
        json.put("end_time",end_time);
        json.put("data_dimension",data_dimension);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }



    //下载任务-任务类型枚举
    public JSONObject downldTaskType() throws Exception {
        String url = "/patrol/download-center/download-task-type-list";
        JSONObject json = new JSONObject();
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //下载任务-列表
    public JSONObject downldPage(int page,int size, String task_name,String task_type ,String shop_name,String applicant) throws Exception {
        String url = "/patrol/download-center/download-page";
        JSONObject json = new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("task_name",task_name);
        json.put("task_type",task_type);
        json.put("shop_name",shop_name);
        json.put("applicant",applicant);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


}
