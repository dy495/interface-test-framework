package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
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


    public void applogin(String userName, String passwd) {

        initHttpConfig();
        String path = "/m/patrol-login";
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
     * @description :获取定检任务列表
     * @date :2020/6/21 9:59
     **/
    public JSONObject scheduleCheckList(int size, int page) throws Exception {
        String url = "/patrol/schedule-check/page";

        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    /**
//     * @description:  删除定检任务
//     */
//    public JSONObject scheduleCheckDelete(long id) throws Exception {
//        String url = "/patrol/schedule-check/delete";
//        JSONObject json=new JSONObject();
//        json.put("id",id);
//        String res = httpPostWithCheckCode(url, json.toJSONString(),IpPort);
//
//        return JSON.parseObject(res).getJSONObject("data");
//    }

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
     * 寻巡店中心门店设备查询
     */
    public JSONObject shopDevice() throws Exception {
        String url = "/patrol/shop/device/list";
        String json = "{\n" +
                "\"shop_id\":" + getXunDianShop() + "\n" +
                "}";
        String res = httpPostWithCheckCode(url, json, IpPort);
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

    //巡店结果+巡店者
    public JSONObject xundianDetil(int check_result, int page, int size, String inspector_id) throws Exception {
        String url = "/patrol/shop/checks/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("check_result", check_result);
        json.put("page", page);
        json.put("size", size);
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
    public JSONObject problemMark(String responsor_id, Long list_id, Long item_id, List<String> pic_list, String audit_comment) throws Exception {
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
        String url = "/patrol/m/shop/problem/items";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ----------------app 相关接口-----------------
     *
     * @description :代办事项列表
     * @date :2020/6/26 20:27
     **/
    public JSONObject Task_list(Integer type, Integer size, Long last_id) throws Exception {
        String url = "/patrol/m/task/list";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("size", size);
        json.put("last_id", last_id);
        String res = httpPostWithCheckCode(url, json.toString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject Task_list(Integer type, Integer size) throws Exception {
        String url = "/patrol/m/task/list";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("size", size);
        String res = httpPostWithCheckCode(url, json.toString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //app开始巡店,适用于定检任务巡店
    public JSONObject checkStartapp(Long shop_id, String check_type, Integer reset, Long task_id) throws Exception {
        String url = "/patrol/m/shop/checks/start";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("check_type", check_type);
        json.put("reset", reset);
        json.put("task_id", task_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //适用于现场巡店
    public JSONObject checkStartapp(Long shop_id, String check_type, Integer reset) throws Exception {
        String url = "/patrol/m/shop/checks/start";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("check_type", check_type);
        json.put("reset", reset);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //获取门店设备
    public JSONObject shopDevice(Long shop_id) throws Exception {
        String url = "/patrol/m/shop/device/list";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //定检任务 定时拍照图片
    public JSONObject picList(Long shop_id, String device_id, String date) throws Exception {
        String url = "/patrol/m/task/schedule-pic/list";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("device_id", device_id);
        json.put("date", date);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :复检不合格提交
     * @date :2020/6/26 20:34
     **/

    public JSONObject StepSubmit(Long id, String comment, Integer recheck_result, List<String> pic_list) throws Exception {
        String url = " /patrol/m/task/step/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", getXunDianShop());
        json.put("id", id);
        json.put("comment", comment);
        json.put("pic_list", pic_list);
        json.put("recheck_result", recheck_result);
        String res = httpPostWithCheckCode(url, json.toString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //提交执行定检 任务 巡店/远程巡店不合格处理
    public JSONObject stepSubmit(Long shop_id, Long id, String comment) throws Exception {
        String url = "/patrol/m/task/step/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        json.put("comment", comment);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject stepSubmit(Long shop_id, Long id, String comment, List<String> pic_list) throws Exception {
        String url = "/patrol/m/task/step/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        json.put("comment", comment);
        json.put("pic_list", pic_list);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //处理待办事项,提交返回code  TODO:
    public Long stepSubmitX(Long shop_id, Long id, String comment, List<String> pic_list) throws Exception {
        String url = "/patrol/m/task/step/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        json.put("comment", comment);
        json.put("pic_list", pic_list);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getLong("code");
    }


    //巡检员查看处理结果 合格与不合格
    public JSONObject stepSubmit2(Long shop_id, Long id, String comment, Integer recheck_result) throws Exception {
        String url = "/patrol/m/task/step/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        json.put("comment", comment);
        json.put("recheck_result", recheck_result);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //巡检员查看处理结果 合格与不合格 返回code
    public Long stepSubmitCode(Long shop_id, Long id, String comment, Integer recheck_result) throws Exception {
        String url = "/patrol/m/task/step/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("id", id);
        json.put("comment", comment);
        json.put("recheck_result", recheck_result);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getLong("code");
    }

    //门店详情
    public JSONObject taskDetail() throws Exception {
        String url = "/patrol/m/task/detail";
        JSONObject json = new JSONObject();
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //app定检任务提交单个审核项，不加评论
    public JSONObject appsubmit(Long shop_id, Integer check_result, long item_id, long list_id, long patrol_id) throws Exception {
        String url = "/patrol/m/shop/checks/item/submit";
        JSONObject json = new JSONObject();
//        String shopid=getXunDianShop();
        json.put("shop_id", shop_id);
        json.put("check_result", check_result);
        json.put("item_id", item_id);
        json.put("list_id", list_id);
        json.put("patrol_id", patrol_id);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //app定检任务提交单个审核项，加评论
    public JSONObject appsubmit(Long shop_id, Integer check_result, long item_id, long list_id, long patrol_id, String comment) throws Exception {
        String url = "/patrol/m/shop/checks/item/submit";
        JSONObject json = new JSONObject();
//        String shopid=getXunDianShop();
        json.put("shop_id", shop_id);
        json.put("check_result", check_result);
        json.put("item_id", item_id);
        json.put("list_id", list_id);
        json.put("patrol_id", patrol_id);
        json.put("audit_comment", comment);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * app定检任务不合格提交图片
     */
    public JSONObject appSubmitN(Long shop_id, long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/m/shop/checks/item/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("patrol_id", patrolId);
        json.put("list_id", listId);
        json.put("item_id", itemId);
        json.put("pic_list", picList);
        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * app定检任务不合格提交图片返回code
     */
    public Long appSubmitNCode(Long shop_id, long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/m/shop/checks/item/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("patrol_id", patrolId);
        json.put("list_id", listId);
        json.put("item_id", itemId);
        json.put("pic_list", picList);
        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getLong("code");
    }

    /**
     * @description :app现场巡店 提交不合格图片提交返回code
     * @date :2020/6/22 20:54
     **/

    public Integer appchecksItemSubmitY(Long shop_id, long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/m/shop/checks/item/submit";

        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("patrol_id", patrolId);
        json.put("list_id", listId);
        json.put("item_id", itemId);
        json.put("pic_list", picList);

        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getInteger("code");
    }

    /**
     * app checks submit 定检任务单项审核之后，总提交
     */
    public JSONObject appcheckSubmit(Long shop_id, String commit, Long id) throws Exception {
        String url = "/patrol/m/shop/checks/submit";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("comment", commit);
        json.put("id", id);
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
    public JSONObject xd_analysis_indeicators(String cycle_type,String month,String day) throws Exception {
        String url = "/patrol/patrol-analysis-data/core-indicators";
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
     * @description:3. 巡店不合格项趋势
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_uncheckTotal(String cycle_type,String month,String day) throws Exception {
        String url = "/patrol/patrol-analysis-data/unqualified-check-total";
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
     * @description:4. 问题分析
     * @author: qingqing
     * @time:
     */
    public JSONObject xd_analysis_question(String cycle_type,String month,String day) throws Exception {
        String url = "/patrol/patrol-analysis-data/problem";
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
        String json =
                "{" +
                        "\"patrol_person\" :\"" + patrol_person + "\",\n" +
                        "\"shop_name\" :\"" + shop_name + "\",\n" +
                        "\"report_status\" :\"" + report_status + "\",\n" +
                        "\"deal_status\" :\"" + deal_status + "\",\n" +
                        "\"sort_event_type_order\" :" + sort_event_type_order + ",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }



}
