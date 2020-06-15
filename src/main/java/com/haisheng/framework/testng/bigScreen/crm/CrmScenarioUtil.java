package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
    public String IpPort = "http://dev.porsche.dealer-ydauto.winsenseos.cn";


    //----------------------登陆--------------------
    public void login(String userName, String passwd) {

        initHttpConfig();
        String path = "/porsche-login";
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
    创建客户
     */
    public JSONObject customerAdd(JSONObject decision_customer) throws Exception {
        String url = "/porsche/app/customer/add";

        String json =
                "{" +
                        "\"decision_customer\" :" + decision_customer + ",\n" +
                        "\"along_list\" :[]\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        Thread.sleep(2*1000);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //决策客户全部信息
    public JSONObject decisionCstmer_All(long customer_level,String remark,String analysis_customer_id,String customer_name, String customer_phone,
                                         String visit_count, String belongs_area, String service_date,String service_time,String customer_select_type,
                                         String customer_channel,String leave_time,String already_car,String test_drive_car,String sehand_assess,
                                         String car_assess,String pre_buy_time,String like_car,String compare_car,String show_price,String pay_type,
                                         String buy_car,String buy_car_attribute,String buy_car_type){
        JSONObject object = new JSONObject();
        object.put("customer_level",customer_level);
        object.put("remark",remark);
        object.put("shop_id",getProscheShop());
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
    public JSONObject decisionCstmer_onlyNec(long customer_level,String customerPhone,String remark){
        JSONObject object = new JSONObject();
        object.put("customer_level",customer_level);
        object.put("remark",remark);
        object.put("shop_id",getProscheShop());
        object.put("customer_phone",customerPhone);
        return object;
    }

    //决策客户仅必填项
    public JSONObject decisionCstmer_NamePhone(long customer_level,String remark,String customer_name, String customer_phone){
        JSONObject object = new JSONObject();
        object.put("customer_level",customer_level);
        object.put("remark",remark);
        object.put("customer_name",customer_name);
        object.put("customer_phone",customer_phone);
        object.put("shop_id",getProscheShop());
        return object;
    }

    /*
    app修改客户
     */
    public JSONObject customerEditApp(Long customer_id, String analysis_customer_id, int customer_level, String customer_name, String customer_phone, String pre_buy_time,
                                      int like_car, int buy_car, int visit_count, int belongs_area, int test_drive_car, String compare_car, int customer_select_type,
                                      String already_car, int show_price, String car_assess, int sehand_assess, int pay_type, int buy_car_type, int buy_car_attribute,
                                      List along_list, List reamrks, List return_visits) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{\n" + "   \"customer_id\" :" + customer_id + ",\n" ;
        if (return_visits!=null){
            json = json + "   \"return_visits\" :" + return_visits + ",\n";

        }
        if (along_list!=null){
            json = json + "   \"along_list\" :" + along_list + ",\n";

        }
        if (reamrks!=null){
            json = json + "   \"reamrks\" :" + reamrks + ",\n";

        }

        json = json +
                "   \"shop_id\" :" + getProscheShop() + ",\n" +
                "   \"analysis_customer_id\" :\"" + analysis_customer_id + "\",\n" +
                "   \"customer_level\" :" + customer_level + ",\n" +
                "   \"customer_name\" :\"" + customer_name + "\",\n" +
                "   \"customer_phone\" :\"" + customer_phone + "\",\n" +
                "   \"pre_buy_time\" :\"" + pre_buy_time + "\",\n" +
                "   \"like_car\" :" + like_car + ",\n" +
                "   \"buy_car\" :" + buy_car + ",\n" +
                "   \"visit_count\" :" + visit_count + ",\n" +
                "   \"belongs_area\" :" + belongs_area + ",\n" +
                "   \"test_drive_car\" :" + test_drive_car + ",\n" +
                "   \"compare_car\" :\"" + compare_car + "\",\n" +
                "   \"customer_select_type\" :" + customer_select_type + ",\n" +
                "   \"already_car\" :\"" + already_car + "\",\n" +
                "   \"show_price\" :" + show_price + ",\n" +
                "   \"car_assess\" :\"" + car_assess + "\",\n" +
                "   \"sehand_assess\" :" + sehand_assess + ",\n" +
                "   \"pay_type\" :" + pay_type + ",\n" +
                "   \"buy_car_type\" :" + buy_car_type + ",\n" +
                "   \"buy_car_attribute\" :" + buy_car_attribute + "\n" ;
        json = json + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //app仅修改客户是否订车/是否试驾
    public JSONObject customerEditNec(Long customer_id,String analysis_customer_id,long customer_level, int buy_car) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{\n" + "   \"customer_id\" :" + customer_id + ",\n" +
                "   \"shop_id\" :" + getProscheShop() + ",\n" +
                "   \"analysis_customer_id\" :\"" + analysis_customer_id + "\",\n" +
                "   \"buy_car\" :" + buy_car + "\n,\"customer_phone\":\"这是一个假电话\",\n\"customer_level\":\""+customer_level+"\"";
        json = json + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC修改顾客订车信息
    public JSONObject customerEditPC(Long customer_id,int buy_car, String phone) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"customer_level\" : 1,\n" +
                        "\"buy_car\" :\"" + buy_car + "\""
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerEditPC(Long customer_id,String name, String phone,long level) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_level\" :" + level + "\n"

                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public JSONObject customerEditPC(Long customer_id,String name, String phone,long level,int like_car,int pay_type,String pre_buy_time,int show_price,int test_drive_car,int visit_count) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"like_car\" :" + like_car + ",\n" +
                        "\"pay_type\" :" + pay_type + ",\n" +
                        "\"pre_buy_time\" :\"" +  pre_buy_time + "\",\n" +
                        "\"show_price\" :" + show_price + ",\n" +
                        "\"test_drive_car\" :" + test_drive_car + ",\n" +
                        "\"visit_count\" :" + visit_count + ",\n" +
                        "\"customer_level\" :" + level + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //修改所属顾问
    public JSONObject customerEditsale(Long customer_id,String name, String phone,String belongs_sale_id) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_level\" : 1,\n" +
                        "\"belongs_sale_id\" :\"" + belongs_sale_id + "\"\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }


    //PC详情页添加顾客回访记录
    public JSONObject customerEditVisitPC(Long customer_id,String name,String phone,Long level,JSONObject return_visits) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"return_visits\" :" + "["+return_visits +"]" +""
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject customerEditVisitPCNotChk(Long customer_id,String name,String phone,Long level,JSONObject return_visits) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"return_visits\" :" + "["+return_visits +"]" +""
                        + "} ";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }


    //PC详情页添加备注
    public JSONObject customerEditRemarkPC(Long customer_id,String name,String phone,Long level,String remark) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"remarks\" :" + "[\""+remark +"\"]" +""
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerEditRemarkPCNotChk(Long customer_id,String name,String phone,Long level,String remark) throws Exception{
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"remarks\" :" + "[\""+remark +"\"]" +""
                        + "} ";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //查询正在接待顾客id
    public JSONObject userInfService() throws Exception{
        String url = "/porsche/app/customer/userInfService";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        JSONObject objte=JSON.parseObject(res);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //删除顾客
    public JSONObject customerDeletePC(long id) throws Exception {
        String url = "/porsche/customer/delete";

        String json =
                "{\n" +
                        "   \"customer_id\" : " + id + ",\n" +
                        "   \"shop_id\" :" + getProscheShop() +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //顾客详情
    public JSONObject customerDetailPC(Long id) throws Exception {
        String url = "/porsche/customer/detail";

        String json =
                "{\n" +
                        "   \"customer_id\":" + id + ",\n" +
                        "   \"shop_id\":\"" + getProscheShop() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    //----------------------工作安排------------------
    //PC添加工作安排
    public JSONObject scheduleAdd_PC(String name,String description,String date,String start_time,String end_time) throws Exception{
        String url = "/porsche/daily-work/schedule/add";

        String json =
                "{\n" +
                        "   \"name\" :\"" + name + "\",\n" +
                        "   \"description\" :\"" + description + "\",\n" +
                        "   \"date\" :\"" + date + "\",\n" +
                        "   \"start_time\" :\"" + start_time + "\",\n" +
                        "   \"end_time\" :\"" + end_time + "\"\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject scheduleAdd_PCNotChk(String name,String description,String date,String start_time,String end_time) throws Exception{
        String url = "/porsche/daily-work/schedule/add";

        String json =
                "{\n" +
                        "   \"name\" :\"" + name + "\",\n" +
                        "   \"description\" :\"" + description + "\",\n" +
                        "   \"date\" :\"" + date + "\",\n" +
                        "   \"start_time\" :\"" + start_time + "\",\n" +
                        "   \"end_time\" :\"" + end_time + "\"\n"
                        + "} ";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }


    //app添加工作安排
    public JSONObject scheduleAdd_APP(String name,String description,String date,String start_time,String end_time) throws Exception{
        String url = "/porsche/daily-work/schedule/app/add";

        String json =
                "{\n" +
                        "   \"name\" :\"" + name + "\",\n" +
                        "   \"description\" :\"" + description + "\",\n" +
                        "   \"date\" :\"" + date + "\",\n" +
                        "   \"start_time\" :\"" + start_time + "\",\n" +
                        "   \"end_time\" :\"" + end_time + "\"\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC删除工作安排
    public JSONObject scheduleDel_PC(Long id) throws Exception{
        String url = "/porsche/daily-work/schedule/delete";

        String json =
                "{\n" +
                        "   \"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        //check if schedule work existed
        JSONArray list_object = scheduleList_PC(1,21,dt.getHistoryDate(0),"").getJSONArray("list");
        for (int i = 0; i < list_object.size();i++){
            JSONObject single = list_object.getJSONObject(i);
            if (single.getLong("id").equals(id)) {
                throw new Exception("删除工作id[ " + id + "], 接口返回1000，但该项工作仍存在");
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    //APP删除工作安排
    public JSONObject scheduleDel_APP(Long id) throws Exception{
        String url = "/porsche/daily-work/schedule/app/delete";

        String json =
                "{\n" +
                        "   \"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC工作安排列表
    public JSONObject scheduleList_PC(int page,int size,String date,String status) throws Exception{
        String url = "/porsche/daily-work/schedule/list";

        String json =
                "{\n" +
                        "   \"page\" :" + page + ",\n";

        if (!date.equals("")){
            json = json+"   \"date\" :\"" + date + "\",\n" ;
        }
        if (!status.equals("")){
            json = json +  "   \"status\" :\"" + status + "\",\n";
        }
        json = json + "   \"size\" :" + size + "\n"
                + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC今日来访列表
    public JSONObject todayListPC(int customerLevel, String customerName, String customerPhone,String id,
                                  long startTime, long endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/today-list";

        String json =
                "{\n" +
                        "   \"shop_id\" :" + getProscheShop() + ",\n";

        if (! StringUtils.isEmpty(equals(id))) {
            json += "   \"belongs_sale_id\" : \"" + id + "\",\n";
        }
        if (! StringUtils.isEmpty(customerPhone)) {
            json += "   \"customer_phone\" : \"" + customerPhone + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\" :" + customerLevel + ",\n";
        }

        if (! StringUtils.isEmpty(customerName)) {
            json += "   \"customer_name\" :\"" + customerName + "\",\n";
        }

        if (0 != startTime) {
            json += "   \"start_time\" :" + startTime + " ,\n";
        }

        if (0 != endTime) {
            json += "   \"end_time\" :" + endTime + ",\n";
        }

        json += "   \"page\" :" + page + ",\n" +
                "   \"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC删除今日来访
    public JSONObject todayVisitDelPC(long id) throws Exception {
        String url = "/porsche/customer/delete_today_visit";

        String json =
                "{\n" +
                        "   \"customer_id\" :" + id + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //-------------客户管理------------------
    //PC我的回访任务列表
    public JSONObject taskList_PC(String date, int status, int page,int size,String customerPhoneNumber) throws Exception{
        String url = "/porsche/return-visit/task/list/withFilterAndCustomerDetail";

        String json = "{\n" +
                "   \"page\":" + page + ",\n";

        if (!StringUtils.isEmpty(date)){
            json = json+"   \"date\":\"" + date + "\",\n" ;
        }
        if (status != -1){
            json = json +  "   \"status\":" + status + ",\n"; //0未执行 1已执行
        }
        if (! StringUtils.isEmpty(customerPhoneNumber)){
            json = json +  "   \"customer_phone_number\":\"" + customerPhoneNumber + "\",\n";
        }
        json = json + "   \"size\":" + size + "\n"
            + "} ";

    String res = httpPostWithCheckCode(url, json, IpPort);

    return JSON.parseObject(res).getJSONObject("data");
}

    //APP获得我的回访任务列表
    public JSONObject taskList_APP(String date,int page,int size, String phone) throws Exception{
        String url = "/porsche/return-visit/task/app/list/withFilterAndCustomerDetail";

        String json = "{\n";

        if (page != -1){
            json = json +  "   \"page\":" + page + ",\n"; //0未执行 1已执行
        }
        if (size != -1){
            json = json +  "   \"size\":" + size + ",\n";
        }
        if (! StringUtils.isEmpty(phone)){
            json = json +  "   \"customer_phone_number\":\"" + phone + "\",\n";
        }
        json = json + "   \"date\":\"" + date + "\"\n"
                + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //客户列表
    public JSONObject customerListPC(String id, int customerLevel, String customerName, String customerPhone,
                                     long startTime, long endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/list";

        String json =
                "{\n" +
                        "   \"shop_id\":" + getProscheShop() + ",\n";

        if (! StringUtils.isEmpty(customerPhone)) {
            json += "   \"customer_phone\": \"" + customerPhone + "\",\n";
        }
        if (! StringUtils.isEmpty(id)) {
            json += "   \"belongs_sale_id\": \"" + id + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\":" + customerLevel + ",\n";
        }

        if (! StringUtils.isEmpty(customerName)) {
            json += "   \"customer_name\":\"" + customerName + "\",\n";
        }

        if (0 != startTime) {
            json += "   \"start_time\":" + startTime + " ,\n";
        }

        if (0 != endTime) {
            json += "   \"end_time\":" + endTime + ",\n";
        }

        json += "   \"page\":" + page + ",\n" +
                "   \"size\":" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerListPC(String id, int customerLevel, String customerName, String customerPhone,
                                     String startTime, String endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/list";

        String json =
                "{\n" +
                        "   \"shop_id\":" + getProscheShop() + ",\n";

        if (! StringUtils.isEmpty(customerPhone)) {
            json += "   \"customer_phone\": \"" + customerPhone + "\",\n";
        }
        if (! StringUtils.isEmpty(id)) {
            json += "   \"belongs_sale_id\": \"" + id + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\":" + customerLevel + ",\n";
        }

        if (! StringUtils.isEmpty(customerName)) {
            json += "   \"customer_name\":\"" + customerName + "\",\n";
        }

        if (! StringUtils.isEmpty(startTime)) {
            json += "   \"start_time\":\"" + startTime + "\" ,\n";
        }

        if (! StringUtils.isEmpty(endTime)) {
            json += "   \"end_time\":\"" + endTime + "\",\n";
        }

        json += "   \"page\":" + page + ",\n" +
                "   \"size\":" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC获取用户等级
    public JSONObject customerLevelList() throws Exception{
        String url = "/porsche/customer-level/list";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject finishReception()throws Exception{
        String url = "/porsche/app/customer/finishReception";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //--------------前台工作------------------
    //销售排班
    public JSONObject receptionOrder() throws Exception{
//        String url = "/porsche/reception/order";
        //更换成前端页面调用接口
        String url = "/porsche/reception/freeSaleUserList";

//        String json =
//                "{\n" +
//                        "   \"sale_id\" :\"" + sale_id + "\",\n"+
//                        "   \"sale_order\" :" + sale_order + ",\n"+
//                        "   \"status\" :" + status + "\n"
//
//                + "} ";
        String json = "{" +
                "\"shop_id\" :" + getProscheShop() +
        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //展厅接待-分配销售下拉列表
    public JSONObject freeSaleList(String userId) throws Exception {
        String url = "/porsche/reception/freeSaleList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //用户状态
    public String  userStatus(String salerName) throws Exception{
        //String url = "/porsche/app/user/userStatus"; // not work
        String url = "/porsche/reception/freeSaleUserList";

        String json ="{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        for (int i=0; i<list.size(); i++) {
            JSONObject item = list.getJSONObject(i);
            if (item.getString("sale_name").equals(salerName)) {
                return item.getString("sale_status_name");
            }
        }
        return null;
    }

    public JSONObject roleList() throws Exception {
        String url = "/porsche/user/roleList";

        String json ="{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }



    //--------------------APP----------------

    //新建试驾
    public JSONObject driveradd(String customerName, String idCard,String gender,String phone,
                                String signTime,String activity,  String model, String country,
                                String city,String email,String address,String ward_name,String driverLicensePhoto1Url,
                                String driverLicensePhoto2Url,String electronicContractUrl) throws Exception{
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"customer_id_number\":\"" +  idCard + "\",\n" +
                        "    \"customer_gender\":\"" +  gender + "\",\n" +
                        "    \"customer_phone_number\":\"" +  phone + "\",\n" +
                        "    \"sign_time\":\"" +  signTime + "\",\n" +
                        "    \"model\":\"" +  model + "\",\n" +
                        "    \"country\":\"" +  country + "\",\n" +
                        "    \"city\":\"" +  city + "\",\n" +
                        "    \"email\":\"" +  email + "\",\n" +
                        "    \"address\":\"" +  address + "\",\n" +
                        "    \"activity\":\"" +  activity + "\",\n" +
                        "    \"ward_name\":\"" +  ward_name + "\",\n" +
                        "    \"driver_license_photo_1_url\":\"" +  driverLicensePhoto1Url + "\",\n" +
                        "    \"driver_license_photo_2_url\":\"" +  driverLicensePhoto2Url + "\",\n" +
                        "    \"electronic_contract_url\":\"" +  electronicContractUrl + "\"" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除试驾
    public JSONObject driverDel(long id) throws Exception{
        String url = "/porsche/daily-work/test-drive/delete";

        String json = "{\"id\": " + id + "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //试驾列表
    public JSONObject driveList(String date, String customer_name, String customer_phone_number, int page,int size) throws Exception {
        String url = "/porsche/daily-work/test-drive/list";

        String json =
                "{" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" ;
        if (!customer_name.equals("")){
            json = json + "    \"customer_name\":\"" + customer_name + "\",\n";
        }
        if (!customer_phone_number.equals("")){
            json = json + "    \"customer_phone_number\":\"" + customer_phone_number + "\",\n";
        }
        json = json +  "    \"size\":\"" + size + "\"" +
                "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject driveListNotChk(String date, String customer_name, String customer_phone_number, int page,int size) throws Exception {
        String url = "/porsche/daily-work/test-drive/list";

        String json =
                "{" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" ;
        if (!customer_name.equals("")){
            json = json + "    \"customer_name\":\"" + customer_name + "\",\n";
        }
        if (!customer_phone_number.equals("")){
            json = json + "    \"customer_phone_number\":\"" + customer_phone_number + "\",\n";
        }
        json = json +  "    \"size\":\"" + size + "\"" +
                "}";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //交车列表
    public JSONObject deliverList(String date, int page, int size, String customerName, String phone) throws Exception {
        String url = "/porsche/daily-work/deliver-car/list";

        String json =
                "{" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" ;
        if (! StringUtils.isEmpty(customerName)){
            json = json + "    \"customer_name\":\"" + customerName + "\",\n";
        }
        if (! StringUtils.isEmpty(phone)){
            json = json + "    \"customer_phone_number\":\"" + phone + "\",\n";
        }
        json = json +  "    \"size\":\"" + size + "\"" +
                "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建交车
    public JSONObject deliverAdd(String customer_name, String customer_gender, String customer_phone_number, String deliver_car_time,String model,String path) throws Exception {
        String url = "/porsche/daily-work/deliver-car/app/addWithCustomerInfo";

        String json =
                "{" +
                        "    \"customer_name\":\"" + customer_name + "\",\n" +
                        "    \"customer_gender\":\"" + customer_gender + "\",\n" +
                        "    \"img_file\":\"" + path + "\",\n" +
                        "    \"model\":\"" + model + "\"\n}" ;

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除交车
    public void deliverDelete(int id) throws Exception {
        String url = "/porsche/daily-work/deliver-car/delete";
        String json =
                "{" +
                        "    \"id\":" + id + "" +
                "}" ;

        httpPostWithCheckCode(url, json, IpPort);

    }

    /**
     * 人脸排除-上传图片
     */
    public JSONObject faceOutUpload(String path) throws Exception {
        String url = "http://dev.porsche.dealer-ydauto.winsenseos.cn/porsche/user/faceOutUpload";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("authorization", authorization);
        httppost.addHeader("shop_id", getProscheShop());
        File file = new File(path);
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
        if (file.toString().contains("png")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_PNG, file.getName());
        }
        if (file.toString().contains("txt")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.TEXT_PLAIN, file.getName());
        }
        if (file.toString().contains("jpg")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_JPEG, file.getName());
        }

        mpEntity.addTextBody("path", "undefined", ContentType.MULTIPART_FORM_DATA);
        HttpEntity httpEntity = mpEntity.build();
        httppost.setEntity(httpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        TestCaseCommon.response = EntityUtils.toString(resEntity, "UTF-8");
        return JSON.parseObject(TestCaseCommon.response);
    }

    /**
     * 人脸排除-图片列表
     */
    public JSONObject faceOutList(int page,int size) throws Exception{
        String url = "/porsche/user/faceOutList";

        String json = "{\n";

        if (page != -1){
            json = json +  "\"page\" :" + page + ",\n"+
                            "\"size\" :" + size + "\n";
        }
        json = json+ "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 人脸排除-图片删除
     */
    public JSONObject faceOutDel(long id) throws Exception{
        String url = "/porsche/user/faceOutDelete";

        String json = "{\"id\": " + id + "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     *修改状态
     */
    public JSONObject updateStatus(String sale_status) throws Exception {
        String url = "/porsche/app/user/updateStatus";

        String json = "{\"sale_status\": \"" + sale_status + "\"}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     *查询状态
     */
    public JSONObject userStatus() throws Exception {
        String url = "/porsche/app/user/userStatus";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public List<Integer> getDiff(List<Integer> base, List<Integer> update) {
        List<Integer> list = new LinkedList<Integer>();

        for (Integer item : base) {
            update.remove(item);
        }


        return update;
    }

    @DataProvider(name = "WORK_TYPE")
    public static Object[] workType() {

        return new String[] {
                "回访工作",
                "其他安排",
                "老客接待",
                "交车服务",
                "接待试驾"
        };
    }

    @DataProvider(name = "DRIVER_ACTIVITY")
    public static Object[] driverActivity() {

        return new String[] {
                "试乘试驾",
                "试乘",
                "其他"
        };
    }

    @DataProvider(name="ERR_FORMAT")
    public static Object[] errFormat() {
        return new String[]{
                "10-00",
                "aaaaa",
                "汉字",
                "10：10"
        };
    }

}
