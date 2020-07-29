package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.model.experiment.enumerator.EnumAddress;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.util.StatusCode;
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
     */
    private static volatile CrmScenarioUtil instance = null;

    public CrmScenarioUtil() {
    }

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
    public String IpPort = EnumAddress.PORSCHE.getAddress();

    //----------------------登陆--------------------
    public void login(String userName, String password) {
        initHttpConfig();
        String path = "/porsche-login";
        String loginUrl = IpPort + path;
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + password + "\"}";
        config.url(loginUrl).json(json);
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

    public JSONObject tryLogin(String userName, String passwd) throws Exception {
        String url = "/porsche-login";
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //小程序登录
    public void appletLogin(String code) {
        initHttpConfig();
        String path = "/WeChat-applet-login";
        String loginUrl = IpPort + path;
        String json = "{ \"code\":\"" + code + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
//            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            authorization = "qa_need_not_delete";

            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");
    }

    public void appletLoginLxq(String code) {
        initHttpConfig();
        String path = "/WeChat-applet-login";
        String loginUrl = IpPort + path;
        String json = "{ \"code\":\"" + code + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
//            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            authorization = "qa_need_not_delete1";
            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");
    }


    /*
    创建客户 V1.1作废了
     */
    public JSONObject customerAdd(JSONObject decision_customer) throws Exception {
        String url = "/porsche/app/customer/add";
        String json =
                "{" +
                        "\"decision_customer\" :" + decision_customer + ",\n" +
                        "\"along_list\" :[]\n"
                        + "} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //手动创建临时客户 V1.1
    public Long getCustomerId() throws Exception {
        String url = "/porsche/app/customer/createTempCustomer";
        String json = "{} ";
        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data").getLong("customer_id");
    }

    //修改顾客全部信息 V1.1
    public JSONObject customerEditALL(Long customer_id, int customer_level, String customer_name, String customer_phone,
                                      String pre_buy_time, String compare_car, String already_car, String car_assess,
                                      int like_car, int buy_car, int visit_count, int belongs_area, int test_drive_car,
                                      int show_price, int sehand_assess, int pay_type, int buy_car_type, int buy_car_attribute, List<Object> remarks, List<Object> along_list
    ) throws Exception {
        String url = "/porsche/app/customer/edit";
        String json =
                "{" +
                        "\"customer_id\" :" + customer_id + ",\n" +
                        "\"shop_id\" :" + getProscheShop() + ",\n" +
                        "\"along_list\" :" + along_list + ",\n" +
                        "\"customer_level\" :" + customer_level + ",\n" +
                        "\"customer_name\" :\"" + customer_name + "\",\n" +
                        "\"customer_phone\" :\"" + customer_phone + "\",\n" +
                        "\"pre_buy_time\" :\"" + pre_buy_time + "\",\n" +
                        "\"compare_car\" :\"" + compare_car + "\",\n" +
                        "\"already_car\" :\"" + already_car + "\",\n" +
                        "\"car_assess\" :\"" + car_assess + "\",\n" +
                        "\"like_car\" :" + like_car + ",\n" +
                        "\"buy_car\" :" + buy_car + ",\n" +
                        "\"visit_count\" :" + visit_count + ",\n" +
                        "\"belongs_area\" :" + belongs_area + ",\n" +
                        "\"test_drive_car\" :" + test_drive_car + ",\n" +
                        "\"show_price\" :" + show_price + ",\n" +
                        "\"sehand_assess\" :" + sehand_assess + ",\n" +
                        "\"pay_type\" :" + pay_type + ",\n" +
                        "\"buy_car_type\" :" + buy_car_type + ",\n" +
                        "\"buy_car_attribute\" :" + buy_car_attribute + ",\n" +
                        "\"remarks\" :[\"" + remarks + "\"]\n"
                        + "} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //修改顾客必填项 V1.1
    public JSONObject customerEdit_onlyNec(Long customer_id, int customer_level, String customer_name, String customer_phone, String remarks) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{" +
                        "\"customer_id\" :" + customer_id + ",\n" +
                        "\"shop_id\" :" + getProscheShop() + ",\n" +
                        "\"customer_level\" :" + customer_level + ",\n" +
                        "\"customer_name\" :\"" + customer_name + "\",\n" +
                        "\"customer_phone\" :\"" + customer_phone + "\",\n" +
                        "\"remarks\" :[\"" + remarks + "\"]\n"
                        + "} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //修改顾客全部信息 V1.1
    public JSONObject customerEdit_car(Long customer_id, int customer_level, String customer_name, String customer_phone,
                                       String pre_buy_time, String compare_car, int like_car, int buy_car, int visit_count,
                                       int test_drive_car, int buy_car_attribute, String remarks
    ) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{" +
                        "\"customer_id\" :" + customer_id + ",\n" +
                        "\"shop_id\" :" + getProscheShop() + ",\n" +
                        "\"customer_level\" :" + customer_level + ",\n" +
                        "\"customer_name\" :\"" + customer_name + "\",\n" +
                        "\"customer_phone\" :\"" + customer_phone + "\",\n" +
                        "\"pre_buy_time\" :\"" + pre_buy_time + "\",\n" +
                        "\"compare_car\" :\"" + compare_car + "\",\n" +
                        "\"like_car\" :" + like_car + ",\n" +
                        "\"buy_car\" :" + buy_car + ",\n" +
                        "\"visit_count\" :" + visit_count + ",\n" +
                        "\"test_drive_car\" :" + test_drive_car + ",\n" +
                        "\"buy_car_attribute\" :" + buy_car_attribute + ",\n" +
                        "\"remarks\" :[\"" + remarks + "\"]\n"
                        + "} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //修改顾客部分信息 V1.1
    public JSONObject customerEdit_several(Long customer_id, int customer_level, String customer_name, String customer_phone,
                                           String pre_buy_time, String compare_car, int pay_type, String remarks
    ) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{" +
                        "\"customer_id\" :" + customer_id + ",\n" +
                        "\"shop_id\" :" + getProscheShop() + ",\n" +
                        "\"customer_level\" :" + customer_level + ",\n" +
                        "\"customer_name\" :\"" + customer_name + "\",\n" +
                        "\"customer_phone\" :\"" + customer_phone + "\",\n" +
                        "\"pre_buy_time\" :\"" + pre_buy_time + "\",\n" +
                        "\"compare_car\" :\"" + compare_car + "\",\n" +
                        "\"pay_type\" :" + pay_type + ",\n" +
                        "\"remarks\" :[\"" + remarks + "\"]\n"
                        + "} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //决策客户全部信息
    public JSONObject decisionCstmer_All(long customer_level, String remark, String analysis_customer_id, String customer_name, String customer_phone,
                                         String visit_count, String belongs_area, String service_date, String service_time, String customer_select_type,
                                         String customer_channel, String leave_time, String already_car, String test_drive_car, String sehand_assess,
                                         String car_assess, String pre_buy_time, String like_car, String compare_car, String show_price, String pay_type,
                                         String buy_car, String buy_car_attribute, String buy_car_type) {
        JSONObject object = new JSONObject();
        object.put("customer_level", customer_level);
        object.put("remark", remark);
        object.put("shop_id", getProscheShop());
        if (!analysis_customer_id.equals("")) {
            object.put("analysis_customer_id", analysis_customer_id);
        }
        if (!customer_name.equals("")) {
            object.put("customer_name", customer_name);
        }
        if (!customer_phone.equals("")) {
            object.put("customer_phone", customer_phone);
        }
        if (!service_date.equals("")) {
            object.put("service_date", service_date);
        }
        if (!service_time.equals("")) {
            object.put("service_time", service_time);
        }
        if (!leave_time.equals("")) {
            object.put("leave_time", leave_time);
        }
        if (!already_car.equals("")) {
            object.put("already_car", already_car);
        }
        if (!car_assess.equals("")) {
            object.put("car_assess", car_assess);
        }
        if (!pre_buy_time.equals("")) {
            object.put("pre_buy_time", pre_buy_time);
        }
        if (!compare_car.equals("")) {
            object.put("compare_car", compare_car);
        }
        if (!visit_count.equals("")) {
            object.put("visit_count", Integer.parseInt(visit_count));
        }
        if (!belongs_area.equals("")) {
            object.put("belongs_area", Integer.parseInt(belongs_area));
        }
        if (!customer_select_type.equals("")) {
            object.put("customer_select_type", Integer.parseInt(customer_select_type));
        }
        if (!customer_channel.equals("")) {
            object.put("customer_channel", Integer.parseInt(customer_channel));
        }
        if (!test_drive_car.equals("")) {
            object.put("test_drive_car", Integer.parseInt(test_drive_car));
        }
        if (!sehand_assess.equals("")) {
            object.put("sehand_assess", Integer.parseInt(sehand_assess));
        }
        if (!like_car.equals("")) {
            object.put("like_car", Integer.parseInt(like_car));
        }
        if (!show_price.equals("")) {
            object.put("show_price", Integer.parseInt(show_price));
        }
        if (!pay_type.equals("")) {
            object.put("pay_type", Integer.parseInt(pay_type));
        }
        if (!buy_car.equals("")) {
            object.put("buy_car", Integer.parseInt(buy_car));
        }
        if (!buy_car_attribute.equals("")) {
            object.put("buy_car_attribute", Integer.parseInt(buy_car_attribute));
        }
        if (!buy_car_type.equals("")) {
            object.put("buy_car_type", Integer.parseInt(buy_car_type));
        }
        return object;
    }

    //决策客户仅必填项
    public JSONObject decisionCstmer_onlyNec(long customer_level, String customerPhone, String remark) {
        JSONObject object = new JSONObject();
        object.put("customer_level", customer_level);
        object.put("remark", remark);
        object.put("shop_id", getProscheShop());
        object.put("customer_phone", customerPhone);
        return object;
    }

    //决策客户带名字
    public JSONObject decisionCstmer_NamePhone(long customer_level, String remark, String customer_name, String customer_phone) {
        JSONObject object = new JSONObject();
        object.put("customer_level", customer_level);
        object.put("remark", remark);
        object.put("customer_name", customer_name);
        object.put("customer_phone", customer_phone);
        object.put("shop_id", getProscheShop());
        return object;
    }

    public JSONObject decisionCstmer_list(long customer_level, String remark, String customer_name, String customer_phone, int like_car, int buy_car, String pre_buy_time) {
        JSONObject object = new JSONObject();
        object.put("customer_level", customer_level);
        object.put("remark", remark);
        object.put("customer_name", customer_name);
        object.put("customer_phone", customer_phone);
        object.put("shop_id", getProscheShop());
        object.put("like_car", like_car);
        object.put("buy_car", buy_car);
        object.put("pre_buy_time", pre_buy_time);
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
                "{\n" + "   \"customer_id\" :" + customer_id + ",\n";
        if (return_visits != null) {
            json = json + "   \"return_visits\" :" + return_visits + ",\n";

        }
        if (along_list != null) {
            json = json + "   \"along_list\" :" + along_list + ",\n";

        }
        if (reamrks != null) {
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
                "   \"buy_car_attribute\" :" + buy_car_attribute + "\n";
        json = json + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //app仅修改客户是否订车/是否试驾
    public JSONObject customerEditNec(Long customer_id, String analysis_customer_id, long customer_level, int buy_car) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{\n" + "   \"customer_id\" :" + customer_id + ",\n" +
                        "   \"shop_id\" :" + getProscheShop() + ",\n" +
                        "   \"analysis_customer_id\" :\"" + analysis_customer_id + "\",\n" +
                        "   \"buy_car\" :" + buy_car + "\n,\"customer_phone\":\"这是一个假电话\",\n\"customer_level\":\"" + customer_level + "\"";
        json = json + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC修改顾客订车信息
    public JSONObject customerEditPC(Long customer_id, int buy_car, String phone) throws Exception {
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

    public JSONObject customerEditPC(Long customer_id, String name, String phone, long level) throws Exception {
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

    public JSONObject customerEditPC(Long customer_id, String name, String phone, long level, int like_car, int pay_type, String pre_buy_time, int show_price, int test_drive_car, int visit_count) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"like_car\" :" + like_car + ",\n" +
                        "\"pay_type\" :" + pay_type + ",\n" +
                        "\"pre_buy_time\" :\"" + pre_buy_time + "\",\n" +
                        "\"show_price\" :" + show_price + ",\n" +
                        "\"test_drive_car\" :" + test_drive_car + ",\n" +
                        "\"visit_count\" :" + visit_count + ",\n" +
                        "\"customer_level\" :" + level + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //修改所属顾问
    public JSONObject customerEditsale(Long customer_id, String name, String phone, String belongs_sale_id) throws Exception {
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
    public JSONObject customerEditVisitPC(Long customer_id, String name, String phone, Long level, JSONObject return_visits) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"return_visits\" :" + "[" + return_visits + "]" + ""
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerEditVisitPCNotChk(Long customer_id, String name, String phone, Long level, JSONObject return_visits) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"return_visits\" :" + "[" + return_visits + "]" + ""
                        + "} ";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //PC我的回访页面点击按钮添加回访
    public JSONObject customerEditVisitPC_button(Long customer_id, Long return_visit_task_id, String next_return_visit_time, String comment) throws Exception {
        String url = "/porsche/daily-work/return-visit-record/execute";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"return_visit_task_id\" :" + return_visit_task_id + ",\n" +
                        "\"next_return_visit_time\" :\"" + next_return_visit_time + "\",\n" +
                        "\"comment\" :\"" + comment + "\""
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    //PC详情页添加备注
    public JSONObject customerEditRemarkPC(Long customer_id, String name, String phone, Long level, String remark) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"remarks\" :" + "[\"" + remark + "\"]" + ""
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerEditRemarkPCNotChk(Long customer_id, String name, String phone, Long level, String remark) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{" +
                        "\"shop_id\" :\"" + getProscheShop() + "\",\n" +
                        "\"customer_id\" :\"" + customer_id + "\",\n" +
                        "\"customer_level\" :\"" + level + "\",\n" +
                        "\"customer_name\" :\"" + name + "\",\n" +
                        "\"customer_phone\" :\"" + phone + "\",\n" +
                        "\"remarks\" :" + "[\"" + remark + "\"]" + ""
                        + "} ";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //查询正在接待顾客id
    public JSONObject userInfService() throws Exception {
        String url = "/porsche/app/customer/userInfService";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        JSONObject objte = JSON.parseObject(res);
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

    public JSONObject customerDeletePCNotChk(long id) throws Exception {
        String url = "/porsche/customer/delete";

        String json =
                "{\n" +
                        "   \"customer_id\" : " + id + ",\n" +
                        "   \"shop_id\" :" + getProscheShop() +
                        "} ";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
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

    public JSONObject customerDetailPCNotChk(Long id) throws Exception {
        String url = "/porsche/customer/detail";

        String json =
                "{\n" +
                        "   \"customer_id\":" + id + ",\n" +
                        "   \"shop_id\":\"" + getProscheShop() + "\"" +
                        "}";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }


    //----------------------工作安排------------------
    //PC添加工作安排
    public JSONObject scheduleAdd_PC(String name, String description, String date, String start_time, String end_time) throws Exception {
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

    public JSONObject scheduleAdd_PCNotChk(String name, String description, String date, String start_time, String end_time) throws Exception {
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
    public JSONObject scheduleAdd_APP(String name, String description, String date, String start_time, String end_time) throws Exception {
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
    public JSONObject scheduleDel_PC(Long id) throws Exception {
        String url = "/porsche/daily-work/schedule/delete";

        String json =
                "{\n" +
                        "   \"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        //check if schedule work existed
        JSONArray list_object = scheduleList_PC(1, 21, dt.getHistoryDate(0), "").getJSONArray("list");
        for (int i = 0; i < list_object.size(); i++) {
            JSONObject single = list_object.getJSONObject(i);
            if (single.getLong("id").equals(id)) {
                throw new Exception("删除工作id[ " + id + "], 接口返回1000，但该项工作仍存在");
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    //APP删除工作安排
    public JSONObject scheduleDel_APP(Long id) throws Exception {
        String url = "/porsche/daily-work/schedule/app/delete";

        String json =
                "{\n" +
                        "   \"id\" :" + id + "\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC工作安排列表
    public JSONObject scheduleList_PC(int page, int size, String date, String status) throws Exception {
        String url = "/porsche/daily-work/schedule/list";

        String json =
                "{\n" +
                        "   \"page\" :" + page + ",\n";

        if (!date.equals("")) {
            json = json + "   \"date\" :\"" + date + "\",\n";
        }
        if (!status.equals("")) {
            json = json + "   \"status\" :\"" + status + "\",\n";
        }
        json = json + "   \"size\" :" + size + "\n"
                + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC今日来访列表
    public JSONObject todayListPC(int customerLevel, String customerName, String customerPhone, String id,
                                  long startTime, long endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/today-list";

        String json =
                "{\n" +
                        "   \"shop_id\" :" + getProscheShop() + ",\n";

        if (!StringUtils.isEmpty(equals(id))) {
            json += "   \"belongs_sale_id\" : \"" + id + "\",\n";
        }
        if (!StringUtils.isEmpty(customerPhone)) {
            json += "   \"customer_phone\" : \"" + customerPhone + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\" :" + customerLevel + ",\n";
        }

        if (!StringUtils.isEmpty(customerName)) {
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
    public JSONObject taskList_PC(String date, int status, int page, int size, String customer_name) throws Exception {
        String url = "/porsche/return-visit/task/list/withFilterAndCustomerDetail";

        String json = "{\n" +
                "   \"page\":" + page + ",\n";

        if (!StringUtils.isEmpty(date)) {
            json = json + "   \"date\":\"" + date + "\",\n";
        }
        if (status != -1) {
            json = json + "   \"status\":" + status + ",\n"; //0未执行 1已执行
        }
        if (!StringUtils.isEmpty(customer_name)) {
            json = json + "   \"customer_name\":\"" + customer_name + "\",\n";
        }
        json = json + "   \"size\":" + size + "\n"
                + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //APP获得我的回访任务列表
    public JSONObject taskList_APP(String date, int page, int size, String phone) throws Exception {
        String url = "/porsche/return-visit/task/app/list/withFilterAndCustomerDetail";

        String json = "{\n";

        if (page != -1) {
            json = json + "   \"page\":" + page + ",\n"; //0未执行 1已执行
        }
        if (size != -1) {
            json = json + "   \"size\":" + size + ",\n";
        }
        if (!StringUtils.isEmpty(phone)) {
            json = json + "   \"customer_phone_number\":\"" + phone + "\",\n";
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

        if (!StringUtils.isEmpty(customerPhone)) {
            json += "   \"customer_phone\": \"" + customerPhone + "\",\n";
        }
        if (!StringUtils.isEmpty(id)) {
            json += "   \"belongs_sale_id\": \"" + id + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\":" + customerLevel + ",\n";
        }

        if (!StringUtils.isEmpty(customerName)) {
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

        if (!StringUtils.isEmpty(customerPhone)) {
            json += "   \"customer_phone\": \"" + customerPhone + "\",\n";
        }
        if (!StringUtils.isEmpty(id)) {
            json += "   \"belongs_sale_id\": \"" + id + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\":" + customerLevel + ",\n";
        }

        if (!StringUtils.isEmpty(customerName)) {
            json += "   \"customer_name\":\"" + customerName + "\",\n";
        }

        if (!StringUtils.isEmpty(startTime)) {
            json += "   \"start_time\":\"" + startTime + "\" ,\n";
        }

        if (!StringUtils.isEmpty(endTime)) {
            json += "   \"end_time\":\"" + endTime + "\",\n";
        }

        json += "   \"page\":" + page + ",\n" +
                "   \"size\":" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //PC获取用户等级
    public JSONObject customerLevelList() throws Exception {
        String url = "/porsche/customer-level/list";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject finishReception(Long customer_id, int customer_level, String customer_name, String customer_phone, String remarks) throws Exception {
        String url = "/porsche/app/customer/finishReception";

        String json =
                "{" +
                        "\"customer_id\" :" + customer_id + ",\n" +
                        "\"shop_id\" :" + getProscheShop() + ",\n" +
                        "\"customer_level\" :" + customer_level + ",\n" +
                        "\"customer_name\" :\"" + customer_name + "\",\n" +
                        "\"customer_phone\" :\"" + customer_phone + "\",\n" +
                        "\"remarks\" :[\"" + remarks + "\"]\n"
                        + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
//    public JSONObject finishReception()throws Exception{
//        String url = "/porsche/app/customer/finishReception";
//
//        String json =
//                "{}";
//
//        String res = httpPostWithCheckCode(url, json, IpPort);
//
//        return JSON.parseObject(res).getJSONObject("data");
//    }

    //--------------前台工作------------------
    //销售排班
    public JSONObject receptionOrder() throws Exception {
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
    public String userStatus(String salerName) throws Exception {
        //String url = "/porsche/app/user/userStatus"; // not work
        String url = "/porsche/reception/freeSaleUserList";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject item = list.getJSONObject(i);
            if (item.getString("sale_name").equals(salerName)) {
                return item.getString("sale_status_name");
            }
        }
        return null;
    }

    public JSONObject roleList() throws Exception {
        String url = "/porsche/user/roleList";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }


    //--------------------APP----------------

    //新建试驾
    public JSONObject driveradd(String customerName, String idCard, String gender, String phone,
                                String signTime, String activity, String model, String country,
                                String city, String email, String address, String ward_name, String driverLicensePhoto1Url,
                                String driverLicensePhoto2Url, String electronicContractUrl) throws Exception {
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"customer_id_number\":\"" + idCard + "\",\n" +
                        "    \"customer_gender\":\"" + gender + "\",\n" +
                        "    \"customer_phone_number\":\"" + phone + "\",\n" +
                        "    \"sign_time\":\"" + signTime + "\",\n" +
                        "    \"model\":\"" + model + "\",\n" +
                        "    \"country\":\"" + country + "\",\n" +
                        "    \"city\":\"" + city + "\",\n" +
                        "    \"email\":\"" + email + "\",\n" +
                        "    \"address\":\"" + address + "\",\n" +
                        "    \"activity\":\"" + activity + "\",\n" +
                        "    \"ward_name\":\"" + ward_name + "\",\n" +
                        "    \"driver_license_photo_1_url\":\"" + driverLicensePhoto1Url + "\",\n" +
                        "    \"driver_license_photo_2_url\":\"" + driverLicensePhoto2Url + "\",\n" +
                        "    \"electronic_contract_url\":\"" + electronicContractUrl + "\"" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除试驾
    public JSONObject driverDel(long id) throws Exception {
        String url = "/porsche/daily-work/test-drive/delete";

        String json = "{\"id\": " + id + "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //试驾列表
    public JSONObject driveList(String date, String customer_name, String customer_phone_number, int page, int size) throws Exception {
        String url = "/porsche/daily-work/test-drive/list";

        String json =
                "{" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"date\":\"" + date + "\",\n";
        if (!customer_name.equals("")) {
            json = json + "    \"customer_name\":\"" + customer_name + "\",\n";
        }
        if (!customer_phone_number.equals("")) {
            json = json + "    \"customer_phone_number\":\"" + customer_phone_number + "\",\n";
        }
        json = json + "    \"size\":\"" + size + "\"" +
                "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject driveListNotChk(String date, String customer_name, String customer_phone_number, int page, int size) throws Exception {
        String url = "/porsche/daily-work/test-drive/list";

        String json =
                "{" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"date\":\"" + date + "\",\n";
        if (!customer_name.equals("")) {
            json = json + "    \"customer_name\":\"" + customer_name + "\",\n";
        }
        if (!customer_phone_number.equals("")) {
            json = json + "    \"customer_phone_number\":\"" + customer_phone_number + "\",\n";
        }
        json = json + "    \"size\":\"" + size + "\"" +
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
                        "    \"date\":\"" + date + "\",\n";
        if (!StringUtils.isEmpty(customerName)) {
            json = json + "    \"customer_name\":\"" + customerName + "\",\n";
        }
        if (!StringUtils.isEmpty(phone)) {
            json = json + "    \"customer_phone_number\":\"" + phone + "\",\n";
        }
        json = json + "    \"size\":\"" + size + "\"" +
                "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject deliverList(int page, int size, String customerName) throws Exception {
        String url = "/porsche/daily-work/deliver-car/list";

        String json =
                "{" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建交车
    public JSONObject deliverAdd(String customer_name, String customer_gender, String customer_phone_number, String deliver_car_time, String model, String path) throws Exception {
        String url = "/porsche/daily-work/deliver-car/app/addWithCustomerInfo";

        String json =
                "{" +
                        "    \"customer_name\":\"" + customer_name + "\",\n" +
                        "    \"customer_gender\":\"" + customer_gender + "\",\n" +
                        "    \"img_file\":\"" + path + "\",\n" +
                        "    \"model\":\"" + model + "\"\n}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除交车
    public void deliverDelete(int id) throws Exception {
        String url = "/porsche/daily-work/deliver-car/delete";
        String json =
                "{" +
                        "    \"id\":" + id + "" +
                        "}";

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
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        TestCaseCommon.response = EntityUtils.toString(resEntity, "UTF-8");
        logger.info(url);
        logger.info(TestCaseCommon.response);
        return JSON.parseObject(TestCaseCommon.response);
    }

    /**
     * 人脸排除-图片列表
     */
    public JSONObject faceOutList(int page, int size) throws Exception {
        String url = "/porsche/user/faceOutList";

        String json = "{\n";

        if (page != -1) {
            json = json + "\"page\" :" + page + ",\n" +
                    "\"size\" :" + size + "\n";
        }
        json = json + "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 人脸排除-图片删除
     */
    public JSONObject faceOutDel(long id) throws Exception {
        String url = "/porsche/user/faceOutDelete";

        String json = "{\"id\": " + id + "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 修改状态
     */
    public JSONObject updateStatus(String sale_status) throws Exception {
        String url = "/porsche/app/user/updateStatus";

        String json = "{\"sale_status\": \"" + sale_status + "\"}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * 查询状态
     */
    public JSONObject userStatus() throws Exception {
        String url = "/porsche/app/user/userStatus";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 创建账号
     */
    public JSONObject addUser(String userName, String userLoginName, String phone, String passwd, int roleId) throws Exception {
        String url = "/porsche/user/add";

        String json =
                "{\n" +
                        "  \"user_name\":\"" + userName + "\",\n" +
                        "  \"user_login_name\":\"" + userLoginName + "\",\n" +
                        "  \"user_phone\":\"" + phone + "\",\n" +
                        "  \"password\":\"" + passwd + "\",\n" +
                        "  \"role_id\":" + roleId +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addUserNotChk(String userName, String userLoginName, String phone, String passwd, int roleId) throws Exception {
        String url = "/porsche/user/add";

        String json =
                "{\n" +
                        "  \"user_name\":\"" + userName + "\",\n" +
                        "  \"user_login_name\":\"" + userLoginName + "\",\n" +
                        "  \"user_phone\":\"" + phone + "\",\n" +
                        "  \"password\":\"" + passwd + "\",\n" +
                        "  \"role_id\":" + roleId +
                        "}";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }

    //账号分页列表
    public JSONObject userPage(int page, int size) throws Exception {
        String url = "/porsche/user/userPage";

        String json =
                "{\n" +
                        "  \"page\":\"" + page + "\",\n" +
                        "  \"size\":" + size +
                        "}";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除账号
    public JSONObject userDel(String userId) throws Exception {
        String url = "/porsche/user/delete";

        String json =
                "{\n" +
                        "    \"user_id\":\"" + userId + "\"\n" +
                        "}";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject userDelNotChk(String userId) throws Exception {
        String url = "/porsche/user/delete";

        String json =
                "{\n" +
                        "    \"user_id\":\"" + userId + "\"\n" +
                        "}";

        String res = httpPost(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public JSONObject customerTodayList() throws Exception {
        String url = "/porsche/reception/customerTodayList";

        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    //展厅接待分配销售
    public JSONObject reception(int id, String sale_id, int user_status) throws Exception {
        String url = "/porsche/reception/distributionUser";

        String json = "{\n" +
                "    \"id\":" + id + ",\n" +
                "    \"sale_id\":\"" + sale_id + "\",\n" +
                "    \"user_status\":" + user_status + "\n" +
                "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public List<Integer> getDiff(List<Integer> base, List<Integer> update) {
        List<Integer> list = new LinkedList<Integer>();

        for (Integer item : base) {
            update.remove(item);
        }


        return update;
    }

    //*****************************applet relate **************************
    //预约试驾
    public JSONObject appointmentDrive(String customer_name, String customer_phone, String appointment_date, Integer car_type) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/test-drive";
        JSONObject json = new JSONObject();
        json.put("customer_name", customer_name);
        json.put("customer_phone_number", customer_phone);
        json.put("appointment_date", appointment_date);
        json.put("car_type", car_type);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约保养
    public JSONObject appointmentMaintain(Long my_car_id, String customer_name, String customer_phone_number, String appointment_date, String appointment_time) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/maintain";
        JSONObject json1 = new JSONObject();
        json1.put("my_car_id", my_car_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("appointment_time", appointment_time);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约维修
    public JSONObject appointmentRepair(Long my_car_id, String customer_name, String customer_phone_number, String appointment_date, String appointment_time, String description) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/repair";
        JSONObject json1 = new JSONObject();
        json1.put("my_car_id", my_car_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("appointment_time", appointment_time);
        json1.put("description", description);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动报名
    public JSONObject joinActivity(String activity_id, String customer_name, String customer_phone_number, String appointment_date, Integer car_type, String other_brand, String customer_number) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/activity";
        JSONObject json1 = new JSONObject();
        json1.put("activity_id", activity_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("car_type", car_type);
        json1.put("other_brand", other_brand);
        json1.put("customer_number", customer_number);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //预约详情
    public JSONObject appointmentInfo(Long appointment_id) throws Exception {
        String url = "/WeChat-applet/porsche/appointment/info";
        JSONObject json = new JSONObject();
        json.put("appointment_id", appointment_id);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约列表
    public JSONObject appointmentList1() throws Exception {
        String url = "/WeChat-applet/porsche/appointment/list";
        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //取消预约
    public JSONObject cancle(Long appointment_id) throws Exception {
        String url = "/WeChat-applet/porsche/appointment/cancel";
        JSONObject json = new JSONObject();
        json.put("appointment_id", appointment_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //添加车辆
    public JSONObject myCarAdd(Integer car_type, String plate_number) throws Exception {
        String url = "/WeChat-applet/porsche/my-car/add";
        JSONObject json = new JSONObject();
        json.put("car_type", car_type);
        json.put("plate_number", plate_number);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //添加车辆
    public Long myCarAddCode(Integer car_type, String plate_number) throws Exception {
        String url = "/WeChat-applet/porsche/my-car/add";
        JSONObject json = new JSONObject();
        json.put("car_type", car_type);
        json.put("plate_number", plate_number);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getLong("code");
    }

    //车辆列表
    public JSONObject myCarList() throws Exception {
        String url = "/WeChat-applet/porsche/my-car/list";
        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //车辆删除
    public JSONObject myCarDelete(String my_car_id) throws Exception {
        String url = "/WeChat-applet/porsche/my-car/delete";
        JSONObject json = new JSONObject();
        json.put("my_car_id", my_car_id);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //车型列表
    public JSONObject myCarTypeList() throws Exception {
        String url = "/WeChat-applet/porsche/car/drive-car-list";
        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //applet 文章详情
    public JSONObject articleDetial(Long activity_id) throws Exception {
        String url = "/WeChat-applet/porsche/article/detail/" + activity_id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //文章列表
    public JSONObject articleList() throws Exception {
        String url = "/WeChat-applet/porsche/home-page/articles";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //applet看车页列表
    public JSONObject appletwatchCarList() throws Exception {
        String url = "/WeChat-applet/porsche/watch-car/list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //看车详情
    public JSONObject watchCarDeatil(Integer car_id) throws Exception {
        String url = "/WeChat-applet/porsche/watch-car/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", car_id);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject addCheckListEmpty(String customer_name, String customer_phone, String appointment_date, Integer car_type, String emptyPara, String message) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/test-drive";

        JSONObject json1 = new JSONObject();
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone);
        json1.put("appointment_date", appointment_date);
        json1.put("car_type", car_type);
        json1.put(emptyPara, "");

        String json = json1.toJSONString();

        String res = httpPost(url, json, IpPort);

        checkCode(res, StatusCode.BAD_REQUEST, "预约试驾，" + emptyPara + "为空！");
        //checkMessage("预约试驾，" + emptyPara + "为空！", res, message);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    //******************2.0 pc relate**************************
    //预约试驾列表 查询
    public JSONObject appointmentpage(String start_day, String end_day, Integer page, Integer szie) throws Exception {
        String url = "/porsche/order-manage/order/test-drive/page";
        JSONObject json1 = new JSONObject();
        json1.put("start_day", start_day);
        json1.put("end_day", end_day);
        json1.put("page", page);
        json1.put("szie", szie);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约试驾列表
    public JSONObject appointmentpage(Integer page, Integer szie) throws Exception {
        String url = "/porsche/order-manage/order/test-drive/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("szie", szie);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //保养列表
    public JSONObject mainAppointmentpage(Integer page, Integer szie) throws Exception {
        String url = "/porsche/order-manage/order/maintain/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("szie", szie);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //维修列表
    public JSONObject repairAppointmentpage(Integer page, Integer szie) throws Exception {
        String url = "/porsche/order-manage/order/repair/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("szie", szie);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动报名顾客列表
    public JSONObject activityList(Integer page, Integer size, Long activity_id) throws Exception {
        String url = "/porsche/activity/customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("activity_id", activity_id);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动报名审批 0 未审批 1 通过 2 不通过
    public JSONObject chackActivity(Integer status, String appointment_id) throws Exception {
        String url = "/porsche/activity/customer/audit/" + appointment_id;
        JSONObject json1 = new JSONObject();
        json1.put("status", status);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动审批加入黑名单
    public JSONObject blackadd(String customer_id) throws Exception {
        String url = "/porsche/activity/customer/black/add";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", customer_id);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //黑名单顾客列表
    public JSONObject blacklist(Integer page, Integer size) throws Exception {
        String url = "/porsche/activity/customer/black/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //文章管理，获取人群人数
    public JSONObject groupTotal(String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property) throws Exception {
        String url = "/porsche/customer/group/total";
        JSONObject json1 = new JSONObject();
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建活动
    public JSONObject createArticle(String[] positions, String valid_start, String valid_end, String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String article_title, String article_bg_pic, String article_content, String article_remarks, boolean is_online_activity, String reception_name, String reception_phone, String customer_max, String simulation_num, String activity_start, String activity_end, Integer role_id, String task_customer_num, Boolean is_create_poster
    ) throws Exception {
        String url = "/porsche/article/add";
        JSONObject json1 = new JSONObject();
        json1.put("positions", positions);
        json1.put("valid_start", valid_start);
        json1.put("valid_end", valid_end);
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("article_title", article_title);
        json1.put("article_bg_pic", article_bg_pic);
        json1.put("article_content", article_content);
        json1.put("article_remarks", article_remarks);
        json1.put("is_online_activity", is_online_activity);
        json1.put("reception_name", reception_name);
        json1.put("reception_phone", reception_phone);
        json1.put("customer_max", customer_max);
        json1.put("simulation_count", simulation_num);
        json1.put("activity_start", activity_start);
        json1.put("activity_end", activity_end);
        json1.put("role_id", role_id);
        json1.put("task_customer_num", task_customer_num);
        json1.put("is_create_poster", is_create_poster);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建文章
    public JSONObject createArticleReal(String[] positions, String valid_start, String valid_end, String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String article_title, String article_bg_pic, String article_content, String article_remarks, boolean is_online_activity
    ) throws Exception {
        String url = "/porsche/article/add";
        JSONObject json1 = new JSONObject();
        json1.put("positions", positions);
        json1.put("valid_start", valid_start);
        json1.put("valid_end", valid_end);
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("article_title", article_title);
        json1.put("article_bg_pic", article_bg_pic);
        json1.put("article_content", article_content);
        json1.put("article_remarks", article_remarks);
        json1.put("is_online_activity", is_online_activity);

        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc文章详情
    public JSONObject artilceDetailpc(Long id) throws Exception {
        String url = "/porsche/article/detail/" + id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject appartilceDetail(Long id) throws Exception {
        String url = "/WeChat-applet/porsche/article/detail/" + id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //文章列表
    public JSONObject articlePage(Integer page, Integer size) throws Exception {
        String url = "/porsche/article/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //获取文章关联顾客数
    public JSONObject activityPeople(Long activity_id) throws Exception {
        String url = "/porsche/article/group-total/" + activity_id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //添加人员列表
    public JSONObject manageList(Integer role_id) throws Exception {
        String url = "/porsche/customer/group/total";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc 商品管理 添加车辆
    public JSONObject addCarPc(String car_type_name, double lowest_price, double highest_price, String car_discount, String car_introduce, String car_pic, String big_pic, String interior_pic, String space_pic, String appearance_pic) throws Exception {
        String url = "/porsche/goods-manage/add-car";
        JSONObject json1 = new JSONObject();
        json1.put("car_type_name", car_type_name);
        json1.put("lowest_price", lowest_price);
        json1.put("highest_price", highest_price);
        json1.put("car_discount", car_discount);
        json1.put("car_introduce", car_introduce);
        json1.put("car_pic", car_pic);
        json1.put("big_pic", big_pic);
        json1.put("interior_pic", interior_pic);
        json1.put("space_pic", space_pic);
        json1.put("appearance_pic", appearance_pic);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc 商品管理 添加车辆
    public Long addCarPccode(String car_type_name, double lowest_price, double highest_price, String car_discount, String car_introduce, String car_pic, String big_pic, String interior_pic, String space_pic, String appearance_pic) throws Exception {
        String url = "/porsche/goods-manage/add-car";
        JSONObject json1 = new JSONObject();
        json1.put("car_type_name", car_type_name);
        json1.put("lowest_price", lowest_price);
        json1.put("highest_price", highest_price);
        json1.put("car_discount", car_discount);
        json1.put("car_introduce", car_introduce);
        json1.put("car_pic", car_pic);
        json1.put("big_pic", big_pic);
        json1.put("interior_pic", interior_pic);
        json1.put("space_pic", space_pic);
        json1.put("appearance_pic", appearance_pic);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getLong("code");
    }

    //pc车辆列表
    public JSONObject carList() throws Exception {
        String url = "/porsche/goods-manage/car-list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc车辆删除
    public JSONObject carDelete(Integer id) throws Exception {
        String url = "/porsche/goods-manage/car-delete";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //pc文章详情
    public JSONObject articleDeileList(Integer id) throws Exception {
        String url = "/porsche/article/detail/" + id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc文章删除
    public JSONObject articleDelete(Long id) throws Exception {
        String url = "/porsche/article/delete/" + id;
        String json = "{}";
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //pc文章下架
    public JSONObject articleStatusChange(Long id) throws Exception {
        String url = "/porsche/article/status/change/" + id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc文章列表-for banner
    public JSONObject articleShowList() throws Exception {
        String url = "/porsche/article/show-list";
        JSONObject json1 = new JSONObject();
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //*************************app relate*************************
    //app 预约试驾全部预约及今日预约人数
    public JSONObject appointmentDriverNum() throws Exception {
        String url = "/porsche/app/appointment/appointment_test_driver_number";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //app 预约保养全部预约及今日预约人数
    public JSONObject mainAppointmentDriverNum() throws Exception {
        String url = "/porsche/app/appointment/appointment_maintain_number";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //app 预约维修全部预约及今日预约人数
    public JSONObject repairAppointmentDriverNum() throws Exception {
        String url = "/porsche/app/appointment/appointment_repair_number";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约试驾列表展示
    public JSONObject appointmentlist() throws Exception {
        String url = "/porsche/app/after_sale/appointment_test_driver_list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约保养列表展示
    public JSONObject mainAppointmentlist() throws Exception {
        String url = "/porsche/app/after_sale/appointment_maintain_list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约维修列表展示
    public JSONObject repairAppointmentlist() throws Exception {
        String url = "/porsche/app/after_sale/appointment_mend_list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //---------------------app前台-----------------
    public JSONObject creatReception() throws Exception {
        String url = "/porsche/app/sale-reception/createReception";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject freeSaleList() throws Exception {
        String url = "/porsche/app/sale-reception/freeSaleList";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //移除黑名单
    public JSONObject blackRemove(String customer_id) throws Exception {
        String url = "/porsche/activity/customer/black/remove";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", customer_id);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //文章投放位置
    @DataProvider(name = "POSITIONS")
    public static Object[] positions() {

        return new String[]{
                "MODEL_RECOMMENDATION",
                "PURCHASE_GUIDE",
                "BRAND_CULTURE",
                "CAR_ACTIVITY"
        };
    }

    //------------------------售后------------------------
    //售后：客户管理->列表展示
    public JSONObject afterSale_custList(String search_condition, String search_date_start, String search_date_end, int page, int size) throws Exception {
        String url = "/porsche/app/after_sale/reception_after_customer_list";
        JSONObject json1 = new JSONObject();
        if (!search_condition.equals("")) {
            json1.put("search_condition", search_condition);
        }
        if (!search_date_start.equals("")) {
            json1.put("search_date_start", search_date_start);
        }
        if (!search_date_end.equals("")) {
            json1.put("search_date_end", search_date_end);
        }
        if (page != -1) {
            json1.put("page", page);
        }
        if (size != -1) {
            json1.put("size", size);
        }
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：客户管理->统计数据
    public JSONObject afterSale_custTotal() throws Exception {
        String url = "/porsche/app/after_sale/reception_after_customer_total";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：客户管理->编辑客户信息
    public JSONObject afterSale_custList(Long after_record_id, String customer_name, String customer_phone_number, String customer_secondary_phone,
                                         String plate_number, int travel_mileage, int car_type, int maintain_type, int maintain_secondary_type,
                                         boolean service_complete, int customer_source, List<String> remarks) throws Exception {
        String url = "/porsche/app/after_sale/edit_after_sale_customer";
        JSONObject json1 = new JSONObject();
        json1.put("after_record_id", after_record_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("plate_number", plate_number);
        json1.put("travel_mileage", travel_mileage);
        json1.put("car_type", car_type);
        json1.put("maintain_type", maintain_type);
        json1.put("service_complete", service_complete);
        json1.put("customer_source", customer_source);


        if (!customer_secondary_phone.equals("")) {
            json1.put("customer_secondary_phone", customer_secondary_phone);
        }
        if (maintain_secondary_type != -1) {
            json1.put("maintain_secondary_type", maintain_secondary_type);
        }
        if (remarks.size() != 0) {
            json1.put("remarks", remarks);
        }
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：我的预约，点击接待按钮
    public JSONObject reception_customer(Long appointment_id) throws Exception {
        String url = "/porsche/app/after_sale/reception_after_sale_customer";
        String json = "{\"appointment_id\":" + appointment_id + "}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：查询客户信息
    public JSONObject afterSale_custDetail(Long after_record_id) throws Exception {
        String url = "/porsche/app/after_sale/detail_after_sale_customer";
        String json = "{\"after_record_id\":" + after_record_id + "}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：回访操作
    public JSONObject afterSale_addVisitRecord(Long id, String return_visit_pic, String comment, String next_return_visit_time) throws Exception {
        String url = "/porsche/app/return-visit-record/execute";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("return_visit_pic", return_visit_pic);
        json1.put("comment", comment);
        json1.put("next_return_visit_time", next_return_visit_time);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：查看回访列表
    public JSONObject afterSale_VisitRecordList(int page, int size, String search_name_phone, String search_start_day, String search_end_day) throws Exception {
        String url = "/porsche/app/return-visit-record/after-sale/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        if (!search_name_phone.equals("")) {
            json1.put("search_name_phone", search_name_phone);
        }
        if (!search_start_day.equals("")) {
            json1.put("search_start_day", search_start_day);
        }
        if (!search_end_day.equals("")) {
            json1.put("search_end_day", search_end_day);
        }
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动：添加报名人信息
    public JSONObject registeredCustomer(Long activity_task_id, String customer_name, String customer_phone_number) throws Exception {
        String url = "/porsche/app/activity-task/registeredCustomer";
        JSONObject json1 = new JSONObject();
        json1.put("activity_task_id", activity_task_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //获取二维码
    public JSONObject porscheAppShopGrCode() {
        String url = "/porsche/app/shop/qrcode";
        String result = httpPostWithCheckCode(url, "{}", IpPort);
        return JSON.parseObject(result);
    }

    //人员管理-销售顾问列表
    public JSONObject ManageList(Integer role_id) throws Exception {
        String url = "/porsche/role-staff/manage/list";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //人员管理-销售顾问列表
    public JSONObject ManageListNoSelect(Integer role_id) throws Exception {
        String url = "/porsche/role-staff/manage/list/no-selected";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //人员管理-销售顾问列表
    public JSONObject ManageAdd(Integer role_id, String uid) throws Exception {
        String url = "/porsche/role-staff/manage/add";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        json1.put("uid", uid);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除销售排版中销售
    public JSONObject ManageDelete(Integer id) throws Exception {
        String url = "/porsche/role-staff/manage/delete/" + id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //销售
    //预约试驾列表,姓名/联系方式搜索
    public JSONObject appointmentlist(String search_condition) throws Exception {
        String url = "/porsche/app/after_sale/appointment_test_driver_list";
        JSONObject json1 = new JSONObject();
        json1.put("search_condition", search_condition);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约试驾列表,根据时间搜索
    public JSONObject appointmentlist(String search_date_start, String search_date_end) throws Exception {
        String url = "/porsche/app/after_sale/appointment_test_driver_list";
        JSONObject json1 = new JSONObject();
        json1.put("search_date_start", search_date_start);
        json1.put("search_date_end", search_date_end);
        String res = httpPost(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    //创建接待
    public JSONObject createReception() {
        String url = "/porsche/app/sale-reception/createReception";
        String result = httpPostWithCheckCode(url, "{}", IpPort);
        return JSON.parseObject(result);
    }

    //分配销售
    public JSONObject allocationSale(String saleId, Long customerId) {
        String url = "/porsche/app/sale-reception/allocationSale";
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("customer_id", customerId);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
    }

    //获取活动列表
    public JSONObject activityTaskPage() {
        String url = "/porsche/app/activity-task/page";
        JSONObject object = new JSONObject();
        object.put("page", 1);
        object.put("size", 10);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
    }

    //删除报名人员
    public JSONObject deleteCustomer(String activityTaskId, String customerId) {
        String url = "/porsche/app/activity-task/deleteCustomer";
        JSONObject object = new JSONObject();
        object.put("activity_task_id", activityTaskId);
        object.put("customer_id", customerId);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
    }

    //获取任务客户列表
    public JSONObject customerTaskPage(int size, int page, Long activityId) {
        String url = "/porsche/activity/customer/task/page";
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("activity_id", activityId);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
    }

    @DataProvider(name = "APPOINTMENT_TYPE")
    public static Object[] appointment_type() {
        return new String[]{
                "TEST_DRIVE",
                "TEST_MAINTAIN",
                "TEST_REPAIR",
                "TEST_ACTIVITY"  //TODO:预约消息类型待确认
        };
    }


    @DataProvider(name = "WORK_TYPE")
    public static Object[] workType() {
        return new String[]{
                "回访工作",
                "其他安排",
                "老客接待",
                "交车服务",
                "接待试驾"
        };
    }

    @DataProvider(name = "DRIVER_ACTIVITY")
    public static Object[] driverActivity() {

        return new String[]{
                "试乘试驾",
                "试乘",
                "其他"
        };
    }

    @DataProvider(name = "ERR_FORMAT")
    public static Object[] errFormat() {
        return new String[]{
                "10-00",
                "aaaaa",
                "汉字",
                "10：10"
        };
    }

    @DataProvider(name = "ERR_PHONE")
    public static Object[] errPhone() {
        return new String[]{
                "1231234123",
                "aaaaaaaaaaa",
                "汉字汉字",
                "10：10",
                "!@#$%^&*()_+{}:",
                "123a123好*123",
                "1         1",
                "123123412345"
        };
    }

    @DataProvider(name = "ROLE_ID")
    public static Object[] role() {
        return new String[]{
                "13",
                "11",
                "12",
                "14"
        };
    }

    @DataProvider(name = "NO_FACE")
    public static Object[] noFace() {
        return new String[]{
                "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/猫.png",
                "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/分辨率较低.png",
                "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/人脸搜索.txt",
                "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/风景.png"
        };
    }


    @DataProvider(name = "ROLE_IDS")
    public static Object[] role_ids() {
        return new Integer[]{
                13,
                15,
                16
        };
    }
}
