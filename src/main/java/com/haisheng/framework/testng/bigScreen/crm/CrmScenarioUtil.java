package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

import java.awt.*;

public class CrmScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     *
     * */

    private static volatile CrmScenarioUtil instance = null;

    private CrmScenarioUtil() {}


    public static CrmScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (CrmScenarioUtil.class) {
                if (null == instance) {
                    instance = new CrmScenarioUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public String shopid = "22728";


    //----------------------登陆--------------------
    public void login(String userName, String passwd) {
        logger.logCaseStart(caseResult.getCaseName());


        initHttpConfig(shopid,authorization);
        String path = "/porsche-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"" + userName + "\",\"passwd\":\"" + passwd + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData("demo测试");
    }

    /*
    创建客户
     */
    public JSONObject customerAdd(JSONObject decision_customer, List along_list) throws Exception {
        String url = "/porsche/app/customer/add";

        String json =
                "{\n" +
                        "   \"decision_customer\" :" + decision_customer + ",\n" +
                        "   \"along_list\" :" + along_list + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //决策客户全部信息
    public JSONObject decisionCstmer_All(Long shop_id, int customer_level,String remark,String analysis_customer_id,String customer_name, String customer_phone,
                                         String visit_count, String belongs_area, String service_date,String service_time,String customer_select_type,
                                         String customer_channel,String leave_time,String already_car,String test_drive_car,String sehand_assess,
                                         String car_assess,String pre_buy_time,String like_car,String compare_car,String show_price,String pay_type,
                                         String buy_car,String buy_car_attribute,String buy_car_type){
        JSONObject object = new JSONObject();
        object.put("customer_level",customer_level);
        object.put("remark",remark);
        object.put("shop_id",shop_id);
        if (!analysis_customer_id.equals("")){
            object.put("analysis_customer_id",analysis_customer_id);
        }
        if (!customer_name.equals("")){
            object.put("customer_name",customer_name);
        }
        if (!customer_phone.equals("")){
            object.put("customer_phone",customer_phone);
        }
        if (!service_date.equals("")){
            object.put("service_date",service_date);
        }
        if (!service_time.equals("")){
            object.put("service_time",service_time);
        }
        if (!leave_time.equals("")){
            object.put("leave_time",leave_time);
        }
        if (!already_car.equals("")){
            object.put("already_car",already_car);
        }
        if (!car_assess.equals("")){
            object.put("car_assess",car_assess);
        }
        if (!pre_buy_time.equals("")){
            object.put("pre_buy_time",pre_buy_time);
        }
        if (!compare_car.equals("")){
            object.put("compare_car",compare_car);
        }
        if (!visit_count.equals("")){
            object.put("visit_count",Integer.parseInt(visit_count));
        }
        if (!belongs_area.equals("")){
            object.put("belongs_area",Integer.parseInt(belongs_area));
        }
        if (!customer_select_type.equals("")){
            object.put("customer_select_type",Integer.parseInt(customer_select_type));
        }
        if (!customer_channel.equals("")){
            object.put("customer_channel",Integer.parseInt(customer_channel));
        }
        if (!test_drive_car.equals("")){
            object.put("test_drive_car",Integer.parseInt(test_drive_car));
        }
        if (!sehand_assess.equals("")){
            object.put("sehand_assess",Integer.parseInt(sehand_assess));
        }
        if (!like_car.equals("")){
            object.put("like_car",Integer.parseInt(like_car));
        }
        if (!show_price.equals("")){
            object.put("show_price",Integer.parseInt(show_price));
        }
        if (!pay_type.equals("")){
            object.put("pay_type",Integer.parseInt(pay_type));
        }
        if (!buy_car.equals("")){
            object.put("buy_car",Integer.parseInt(buy_car));
        }
        if (!buy_car_attribute.equals("")){
            object.put("buy_car_attribute",Integer.parseInt(buy_car_attribute));
        }
        if (!buy_car_type.equals("")){
            object.put("buy_car_type",Integer.parseInt(buy_car_type));
        }
        return object;
    }
    //决策客户仅必填项
    public JSONObject decisionCstmer_onlyNec(Long shop_id, int customer_level,String remark){
        JSONObject object = new JSONObject();
        object.put("customer_level",customer_level);
        object.put("remark",remark);
        object.put("shop_id",shop_id);
        return object;
    }

    //----------------------工作安排------------------
    //PC添加工作安排
    public JSONObject scheduleAdd_PC(String name,String description,String date,String start_time,String end_time) throws Exception{
        String url = "/porsche/daily-work/schedule/add";

        String json =
                "{\n" +
                        "   \"name\" :" + name + ",\n" +
                        "   \"description\" :" + description + ",\n" +
                        "   \"date\" :" + date + ",\n" +
                        "   \"start_time\" :" + start_time + ",\n" +
                        "   \"end_time\" :" + end_time + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //app添加工作安排
    public JSONObject scheduleAdd_APP(String name,String description,String date,String start_time,String end_time) throws Exception{
        String url = "/porsche/daily-work/schedule/app/add";

        String json =
                "{\n" +
                        "   \"name\" :" + name + ",\n" +
                        "   \"description\" :" + description + ",\n" +
                        "   \"date\" :" + date + ",\n" +
                        "   \"start_time\" :" + start_time + ",\n" +
                        "   \"end_time\" :" + end_time + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC删除工作安排
    public JSONObject scheduleDel_PC(Long id) throws Exception{
        String url = "/porsche/daily-work/schedule/delete";

        String json =
                "{\n" +
                        "   \"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //APP删除工作安排
    public JSONObject scheduleDel_APP(Long id) throws Exception{
        String url = "/porsche/daily-work/schedule/app/delete";

        String json =
                "{\n" +
                        "   \"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC工作安排列表
    public JSONObject scheduleList_PC(int page,int size,String date,String status) throws Exception{
        String url = "/porsche/daily-work/schedule/add";

        String json =
                "{\n" +
                        "   \"page\" :" + page + ",\n";

        if (!date.equals("")){
            json = json+"   \"date\" :" + date + ",\n" ;
        }
        if (!status.equals("")){
            json = json +  "   \"status\" :" + status + ",\n";
        }
        json = json + "   \"size\" :" + size + "\n"
                + "} ";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }


    //用户状态
    public JSONObject userStatus() throws Exception{
        String url = "/porsche/app/user/userStatus";

        String json ="{}";

        String res = httpPostWithCheckCode(url, json,shopid,authorization);

        return JSON.parseObject(res).getJSONObject("data");
    }
}
