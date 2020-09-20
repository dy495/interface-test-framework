package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

public class XundianScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     *
     * */

    private static volatile XundianScenarioUtil instance = null;

    private XundianScenarioUtil() {}
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
        return JSON.parseObject(res);
    }
    /*
  3.6 编辑定检任务
  */
    public JSONObject scheduleCheckEdit(long id,String name,String cycle,JSONArray dates,String send_time,String valid_start,String valid_end,String inspector_id,JSONArray shop_list) throws Exception {
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

    /*
10.1 新建定检规则(2020-08-13)
*/
    public JSONObject scheduleRuleAdd(String name,String start_time,String end_time,int interval_hour,JSONArray shop_list) throws Exception {
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
    public JSONObject scheduleRuleList(int page,int size) throws Exception {
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
    public JSONObject scheduleRuleEdit(String name,String start_time,String end_time,int interval_hour,JSONArray shop_list,int id) throws Exception {
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
    public JSONObject scheduleRuleSwith(int id,int status) throws Exception {
        String url = "/patrol/schedule-rule/switch";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"status\" :" + status + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }



}
