package com.haisheng.framework.testng.bigScreen.crm;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAddress;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.Driver;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.destDriver;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.finishReceive;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.orderCar;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.selectTest;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.StatusCode;
import com.haisheng.framework.util.HttpExecutorUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.*;

public class CrmScenarioUtil extends TestCaseCommon {

    /**
     * 单例，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile CrmScenarioUtil instance = null;
    CustomerInfo cstm = new CustomerInfo();

    private CrmScenarioUtil() {
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
    public void login(String username, String password) {
        initHttpConfig();
        String path = "/porsche-login";
        String loginUrl = IpPort + path;
        String json = "{\"type\":0, \"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
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
    }

    public JSONObject tryLogin(String userName, String passwd) throws Exception {
        String url = "/porsche-login";
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    /**
     * 小程序通用登录
     *
     * @param token 自己的token
     */
    public void appletLoginToken(String token) {
        authorization = token;
        logger.info("applet authorization is:{}", authorization);
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

    //修改顾客必填项 V4.0
    public JSONObject customerEdit_onlyNec(Long reception_id, Long customer_id, String name) throws Exception {
        String url = "/porsche/app/customer/edit";

        JSONObject json1 = new JSONObject();
        json1.put("reception_id", reception_id);
        json1.put("customer_id", customer_id);
        json1.put("name", name);
        json1.put("subject_type", "BB");
        json1.put("call", "call");         //TODO:call
        json1.put("district_code", "110101");
        json1.put("address", "东城");
//        json1.put("birthday",birthday);
//        json1.put("id_number",id_number);
//        json1.put("face_list",face_list);
//        json1.put("phone_list",phone_list);
        json1.put("remark", "auto-remark-12345678901234567890");
//        json1.put("palte_number_one",palte_number_one);
//        json1.put("palte_number_two",palte_number_two);
        json1.put("intention_car_model", 37);
        json1.put("expected_buy_day", dt.getHistoryDate(1));
        json1.put("test_drive_car_model", 37);
        json1.put("pay_type", 2);
        json1.put("assess_car_model", 37);
//        json1.put("source_channel",source_channel);
        json1.put("compare_car_model", 37);
        json1.put("own_car_model", 37);
        json1.put("visit_count_type", 1);
        json1.put("is_offer", 1);
        json1.put("buy_car_type", 1);
        json1.put("is_assessed", 1);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject finishReception(Long reception_id, Long customer_id, String name) throws Exception {
        String url = "/porsche/app/customer/finishReception";
        JSONObject json1 = new JSONObject();
        json1.put("reception_id", reception_id);
        json1.put("customer_id", customer_id);
        json1.put("name", name);
        json1.put("subject_type", "BB");
        json1.put("call", "call");         //TODO:call
        json1.put("district_code", "110101");
        json1.put("address", "东城");
        json1.put("remark", "auto-remark-12345678901234567890");

        json1.put("intention_car_model", 37);
        json1.put("expected_buy_day", dt.getHistoryDate(1));
        json1.put("test_drive_car_model", 37);
        json1.put("pay_type", 2);
        json1.put("assess_car_model", 37);
        json1.put("compare_car_model", 37);
        json1.put("own_car_model", 37);
        json1.put("visit_count_type", 1);
        json1.put("is_offer", 1);
        json1.put("buy_car_type", 1);
        json1.put("is_assessed", 1);
        String json = json1.toJSONString();
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
    public JSONObject customerEditsale(Long customer_id, String name, String phone, String belongs_sale_id) {
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
    public JSONObject userInfService() {
        String url = "/porsche/app/customer/userInfService";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }


    //删除顾客
    public JSONObject customerDeletePC(long id) {
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

    //客户列表*
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

    public JSONObject customerListPC(String customerPhone, int page, int size) {
        String url = "/porsche/customer/list";
        JSONObject json = new JSONObject();
        json.put("customer_phone", customerPhone);
        json.put("page", page);
        json.put("size", size);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);

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

    //PC获取用户等级
    public JSONObject districtTree() throws Exception {
        String url = "/porsche/app/district/tree";

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

    /**
     * 完成接待接口
     *
     * @param belongsSaleId 所属销售
     * @param customerId    客户id
     * @param customerLevel 客户等级
     * @param customerName  客户名称
     * @param customerPhone 客户电话
     * @param remark        备注
     */
    public JSONObject customerFinishReception(String belongsSaleId, long customerId, int customerLevel, String customerName, String customerPhone, String... remark) throws Exception {
        JSONArray array = new JSONArray();
        String url = "/porsche/app/customer/finishReception";
        JSONObject object = new JSONObject();
        object.put("belongs_sale_id", belongsSaleId);
        object.put("customer_id", customerId);
        object.put("shop_id", getProscheShop());
        object.put("customer_level", customerLevel);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        array.addAll(Arrays.asList(remark));
        object.put("remarks", array);
        String res = httpPost(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(res);
    }


    //--------------前台工作------------------
    public JSONObject receptionOrder() throws Exception {
        String url = "/porsche/reception/freeSaleUserList";
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

    //新建试驾 2.1修改
    public JSONObject driveradd(Long receptionId, Long customer_id, String customerName, String idCard, String gender, String phone,
                                String activity, Long model, String country,
                                String city, String email, String address, String ward_name, String driverLicensePhoto1Url,
                                String driverLicensePhoto2Url, String electronicContractUrl, String sign_date, String sign_time, String call) {
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        String json =
                "{\n" +
                        "    \"reception_id\":" + receptionId + ",\n" +
                        "    \"customer_id\":" + customer_id + ",\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"customer_id_number\":\"" + idCard + "\",\n" +
                        "    \"customer_gender\":\"" + gender + "\",\n" +
                        "    \"customer_phone_number\":\"" + phone + "\",\n" +
                        "    \"carModel\":\"" + model + "\",\n" +
                        "    \"country\":\"" + country + "\",\n" +
                        "    \"city\":\"" + city + "\",\n" +
                        "    \"email\":\"" + email + "\",\n" +
                        "    \"address\":\"" + address + "\",\n" +
                        "    \"activity\":\"" + activity + "\",\n" +
                        "    \"ward_name\":\"" + ward_name + "\",\n" +
                        "    \"driver_license_photo_1_url\":\"" + driverLicensePhoto1Url + "\",\n" +
                        "    \"driver_license_photo_2_url\":\"" + driverLicensePhoto2Url + "\",\n" +
                        "    \"electronic_contract_url\":\"" + electronicContractUrl + "\",\n" +
                        "    \"sign_date\":\"" + sign_date + "\",\n" +
                        "    \"sign_time\":\"" + sign_time + "\",\n" +
                        "    \"call\":\"" + call + "\"" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建试驾 4.0,增删部分无用参数
    public JSONObject driveradd3(Long receptionId, Long customer_id, String customerName, String phone,
                                 Long activity, Long model, String country, String city, String email, String address, String ward_name, String driverLicensePhoto1Url,
                                 String driverLicensePhoto2Url, String electronicContractUrl, String sign_date, String sign_time, String call, String apply_time, Long test_drive_car) {
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        String json =
                "{\n" +
                        "    \"reception_id\":" + receptionId + ",\n" +
                        "    \"customer_id\":" + customer_id + ",\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"customer_phone_number\":\"" + phone + "\",\n" +
                        "    \"car_model\":\"" + model + "\",\n" +
                        "    \"country\":\"" + country + "\",\n" +
                        "    \"city\":\"" + city + "\",\n" +
                        "    \"email\":\"" + email + "\",\n" +
                        "    \"address\":\"" + address + "\",\n" +
                        "    \"activity\":\"" + activity + "\",\n" +
                        "    \"ward_name\":\"" + ward_name + "\",\n" +
                        "    \"driver_license_photo_1_url\":\"" + driverLicensePhoto1Url + "\",\n" +
                        "    \"driver_license_photo_2_url\":\"" + driverLicensePhoto2Url + "\",\n" +
                        "    \"electronic_contract_url\":\"" + electronicContractUrl + "\",\n" +
                        "    \"sign_date\":\"" + sign_date + "\",\n" +
                        "    \"sign_time\":\"" + sign_time + "\",\n" +
                        "    \"call\":\"" + call + "\"" +
                        "    \"apply_time\":\"" + apply_time + "\"" +
                        "    \"test_drive_car\":\"" + test_drive_car + "\"" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject driveradd4(Long receptionId, Long customer_id, String customerName, String phone,
                                 Long activity, Long model, String country, String city, String address, String driverLicensePhoto1Url,
                                 String driverLicensePhoto2Url, String electronicContractUrl, String sign_date, String sign_time, String call, String apply_time, Long test_drive_car) {
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        JSONObject json1 = new JSONObject();
        json1.put("customer_id", customer_id);
        json1.put("customer_name", customerName);
        json1.put("customer_gender", 0);
        json1.put("customer_phone_number", phone);
        json1.put("car_model", test_drive_car);
        json1.put("country", country);
        json1.put("city", city);
        json1.put("address", address);
        json1.put("activity", activity);
        json1.put("ward_name_url", driverLicensePhoto1Url);
        json1.put("driver_license_photo_1_url", driverLicensePhoto1Url);
        json1.put("driver_license_photo_2_url", driverLicensePhoto2Url);
        json1.put("electronic_contract_url", electronicContractUrl);
        json1.put("sign_date", sign_date);
        json1.put("sign_time", sign_time);
        json1.put("call", call);
        json1.put("test_drive_time", apply_time);
//        json1.put("test_drive_car",test_drive_car);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject driveradd5(Long receptionId, Long customer_id, String customerName, String phone, String driverLicensePhoto1Url,
                                 String sign_date, String sign_time, String apply_time, Long test_drive_car) {
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        JSONObject json1 = new JSONObject();
        json1.put("reception_id", receptionId);
        json1.put("customer_id", customer_id);
        json1.put("customer_name", customerName);
        json1.put("customer_gender", 0);
        json1.put("customer_phone_number", phone);
        json1.put("car_model", test_drive_car);
        json1.put("country", "中国");//
        json1.put("city", "苏州");//
        json1.put("address", "西湖");//
        json1.put("activity", 1);//
        json1.put("ward_name_url", driverLicensePhoto1Url);
        json1.put("driver_license_photo_1_url", driverLicensePhoto1Url);
        json1.put("driver_license_photo_2_url", driverLicensePhoto1Url);
        json1.put("electronic_contract_url", driverLicensePhoto1Url);
        json1.put("sign_date", sign_date);
        json1.put("sign_time", sign_time);
        json1.put("call", "MEN");      //
        json1.put("test_drive_time", apply_time);
        json1.put("is_fill", false);
        json1.put("test_drive_car", test_drive_car);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject driveradd6(destDriver dd) throws Exception {
        String url = "/porsche/daily-work/test-drive/app/addWithCustomerInfo";

        JSONObject json1 = new JSONObject();
        json1.put("reception_id", dd.receptionId);
        json1.put("customer_id", dd.customer_id);
        json1.put("customer_name", dd.customerName);
        json1.put("customer_gender", dd.customer_gender);
        json1.put("customer_phone_number", dd.phone);
        json1.put("car_model", dd.test_drive_car);
        json1.put("country", dd.country);//
        json1.put("city", dd.city);//
        json1.put("address", dd.address);//
        json1.put("activity", 1);//
        json1.put("email", dd.email);//
        json1.put("ward_name_url", dd.driverLicensePhoto1Url);
        json1.put("driver_license_photo_1_url", dd.driverLicensePhoto1Url);
        json1.put("driver_license_photo_2_url", dd.driverLicensePhoto1Url);
        json1.put("electronic_contract_url", dd.driverLicensePhoto1Url);
        json1.put("sign_date", dd.sign_date);
        json1.put("sign_time", dd.sign_time);
        json1.put("call", "MEN");      //
        json1.put("test_drive_time", dd.apply_time);
        json1.put("is_fill", dd.is_fill);
        json1.put("test_drive_car", dd.test_drive_car);
        String json = json1.toJSONString();
        String res = "";
        JSONObject result;
        if (dd.checkCode) {
            res = httpPostWithCheckCode(url, json, IpPort);
            result = JSON.parseObject(res).getJSONObject("data");
        } else {
            res = httpPost(url, json, IpPort);
            result = JSON.parseObject(res);
        }
        return result;
    }

    //删除试驾
    public JSONObject driverDel(long id) throws Exception {
        String url = "/porsche/daily-work/test-drive/delete";

        String json = "{\"id\": " + id + "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //试驾审核
    public JSONObject driverAudit(long id, int audit_status) throws Exception { //1-通过，2-拒绝
        String url = "/porsche/daily-work/test-drive/app/test-driver-audit";

        String json =
                "{\n" +
                        "  \"id\":\"" + id + "\",\n" +
                        "  \"audit_status\":" + audit_status +
                        "}";


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

    //2.1新增 订车
    public JSONObject orderCar(Long customer_id) throws Exception {
        String url = "/porsche/daily-work/order-car/app/order-car";

        String json =
                "{" +
                        "    \"customer_id\":\"" + customer_id + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建交车 2.1修改
    public JSONObject deliverAdd(Long car_id, Long reception_id, Long customer_id, String customer_name, String deliver_car_time, Long model, String img_file,
                                 Boolean accept_show, String sign_name_url, String vehicle_chassis_code) {
        String url = "/porsche/daily-work/deliver-car/app/addWithCustomerInfo";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", customer_id);
        json1.put("car_id", car_id);
        json1.put("reception_id", reception_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "女");
        json1.put("deliver_car_time", deliver_car_time);
        json1.put("CarModel", model);
        json1.put("img_file", img_file);
        json1.put("accept_show", accept_show);
        json1.put("works", "金融");
        json1.put("likes", "宠物");
        json1.put("greeting", "自动化-恭喜QA同学喜提车车一辆");
        json1.put("call", "先生");
        json1.put("id_card", "222402199708150628");
        json1.put("sign_name_url", sign_name_url);
        json1.put("vehicle_chassis_code", vehicle_chassis_code);


        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建交车 2.1修改
    public JSONObject deliverAddcode(Long car_id, Long reception_id, Long customer_id, String customer_name, String deliver_car_time, Long model, String img_file,
                                     Boolean accept_show, String sign_name_url, String vehicle_chassis_code) throws Exception {
        String url = "/porsche/daily-work/deliver-car/app/addWithCustomerInfo";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", customer_id);
        json1.put("car_id", car_id);
        json1.put("reception_id", reception_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "女");
        json1.put("deliver_car_time", deliver_car_time);
        json1.put("CarModel", model);
        json1.put("img_file", img_file);
        json1.put("accept_show", accept_show);
        json1.put("works", "金融");
        json1.put("likes", "宠物");
        json1.put("greeting", "自动化-恭喜QA同学喜提车车一辆");
        json1.put("call", "先生");
        json1.put("id_card", "222402199708150628");
        json1.put("sign_name_url", sign_name_url);
        json1.put("vehicle_chassis_code", vehicle_chassis_code);

        String json = json1.toJSONString();

        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
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
    public JSONObject addUser(String userName, String userLoginName, String phone, String passwd, int roleId, String platNumber1, String platNumber2) {
        String url = "/porsche/user/add";
        JSONObject object = new JSONObject();
        object.put("user_name", userName);
        object.put("user_login_name", userLoginName);
        object.put("user_phone", phone);
        object.put("password", passwd);
        object.put("role_id", roleId);
        List<String> list = new ArrayList<>();
        if (!StringUtils.isEmpty(platNumber1)) {
            list.add(platNumber1);
        }
        if (!StringUtils.isEmpty(platNumber2)) {
            list.add(platNumber2);
        }
        object.put("plate_number", list);
        String res = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
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

    public JSONObject userUserPage(int page, int size) {
        String url = "/porsche/user/userPage";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //删除账号
    public JSONObject userDel(String userId) throws Exception {
        String url = "/porsche/user/delete";
        JSONObject object = new JSONObject();
        object.put("user_id", userId);
        String res = httpPost(url, JSON.toJSONString(object), IpPort);
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

    /**
     * 预约试驾接口
     *
     * @param gender              性别
     * @param customerName        姓名
     * @param customerPhoneNumber 电话号
     * @param appointmentDate     预约日期
     * @param carType             预约车型 4：718/1-6：不知道
     */
    public JSONObject appointmentTestDrive(String gender, String customerName, String customerPhoneNumber, String appointmentDate, Integer carType, Integer car_model) {
        String url = "/WeChat-applet/porsche/a/appointment/test-drive";
        JSONObject object = new JSONObject();
        object.put("customer_gender", gender);
        object.put("customer_name", customerName);
        object.put("customer_phone_number", customerPhoneNumber);
        object.put("appointment_date", appointmentDate);
        object.put("car_style", carType);
        object.put("car_model", car_model);
        return invokeApi(url, object);
    }

    public JSONObject appointmentTestDrivecode(String gender, String customerName, String customerPhoneNumber, String appointmentDate, Integer carType, Integer car_model) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/test-drive";
        JSONObject object = new JSONObject();
        object.put("customer_gender", gender);
        object.put("customer_name", customerName);
        object.put("customer_phone_number", customerPhoneNumber);
        object.put("appointment_date", appointmentDate);
        object.put("car_style", carType);
        object.put("car_model", car_model);
        String res = httpPost(url, object.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     * 取消预约
     *
     * @param appointmentId 预约id
     */
    public JSONObject appointmentCancel(Integer appointmentId) {
        String url = "/WeChat-applet/porsche/a/appointment/cancel/" + appointmentId;
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    //预约保养
    public JSONObject appointmentMaintain(Long my_car_id, String customer_name, String customer_phone_number, String appointment_date, String appointment_time, Long time_range_id) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/maintain";
        JSONObject json1 = new JSONObject();
        json1.put("my_car_id", my_car_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "MALE");
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("appointment_time", appointment_time);
        json1.put("time_range_id", time_range_id);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约保养
    public JSONObject appointmentMaintainCode(Long my_car_id, String customer_name, String customer_phone_number, String appointment_date, String appointment_time, Long time_range_id) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/maintain";
        JSONObject json1 = new JSONObject();
        json1.put("my_car_id", my_car_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "MALE");
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("appointment_time", appointment_time);
        json1.put("time_range_id", time_range_id);
        String json = json1.toJSONString();
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }


    //预约维修
    public JSONObject appointmentRepair(Long my_car_id, String customer_name, String customer_phone_number, String appointment_date, String appointment_time, String description, Long time_range_id) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/repair";
        JSONObject json1 = new JSONObject();
        json1.put("my_car_id", my_car_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "MALE");
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("appointment_time", appointment_time);
        json1.put("description", description);
        json1.put("time_range_id", time_range_id);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约维修
    public JSONObject appointmentRepaircode(Long my_car_id, String customer_name, String customer_phone_number, String appointment_date, String appointment_time, String description, Long time_range_id) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/repair";
        JSONObject json1 = new JSONObject();
        json1.put("my_car_id", my_car_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "MALE");
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("appointment_time", appointment_time);
        json1.put("description", description);
        json1.put("time_range_id", time_range_id);
        String json = json1.toJSONString();
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //活动报名
    public JSONObject joinActivity(String activity_id, String customer_name, String customer_phone_number, String appointment_date, Integer car_type, String other_brand, String customer_number, Integer car_model) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/activity";
        JSONObject json1 = new JSONObject();
        json1.put("activity_id", activity_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_gender", "MALE");
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("car_type", car_type);
        json1.put("car_model", car_model);
        json1.put("other_brand", other_brand);
        json1.put("customer_number", customer_number);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动报名
    public JSONObject joinActivityCode(String activity_id, String customer_name, String customer_phone_number, String appointment_date, Integer car_type, String other_brand, String customer_number, Integer car_model) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/activity";
        JSONObject json1 = new JSONObject();
        json1.put("activity_id", activity_id);
        json1.put("customer_name", customer_name);
        json1.put("gender", "MALE");
        json1.put("customer_phone_number", customer_phone_number);
        json1.put("appointment_date", appointment_date);
        json1.put("car_type", car_type);
        json1.put("car_model", car_model);
        json1.put("other_brand", other_brand);
        json1.put("customer_number", customer_number);
        String json = json1.toJSONString();
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //评价
    public JSONObject appointmentEvaluate(Long appointment_id, String suggestion, JSONArray evaluate_list) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/evaluate/" + appointment_id;
        JSONObject json = new JSONObject();
        json.put("suggestion", suggestion);
        json.put("evaluate_list", evaluate_list);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //评价
    public JSONObject messageEvaluate(Long appointment_id, String suggestion, JSONArray evaluate_list) throws Exception {
        String url = "/WeChat-applet/porsche/a/message/evaluate/" + appointment_id;
        JSONObject json = new JSONObject();
        json.put("suggestion", suggestion);
        json.put("evaluate_list", evaluate_list);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //预约详情
    public JSONObject appointmentInfo(Long appointment_id) {
        String url = "/WeChat-applet/porsche/a/appointment/detail/" + appointment_id;
        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约列表  v3.0接口变更不适用
    public JSONObject appointmentList1() throws Exception {
        String url = "/WeChat-applet/porsche/appointment/list";
        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //取消预约
    public JSONObject cancle(Long appointment_id) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/cancel/" + appointment_id;
        String json = "";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //添加车辆
    public JSONObject myCarAdd(Integer car_type, String plate_number, Integer car_model) throws Exception {
        String url = "/WeChat-applet/porsche/a/my-car/add";
        JSONObject json = new JSONObject();
        json.put("car_type", car_type);
        json.put("car_model", car_model);
        json.put("plate_number", plate_number);

        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //添加车辆
    public JSONObject myCarAddCode(Integer car_type, Integer car_model, String plate_number) throws Exception {
        String url = "/WeChat-applet/porsche/a/my-car/add";
        JSONObject json = new JSONObject();
        json.put("car_type", car_type);
        json.put("car_model", car_model);
        json.put("plate_number", plate_number);

        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    //车辆列表
    public JSONObject myCarList() throws Exception {
        String url = "/WeChat-applet/porsche/a/my-car/list";
        String json = "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //车辆删除
    public JSONObject myCarDelete(String my_car_id) throws Exception {
        String url = "/WeChat-applet/porsche/a/my-car/delete";
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

    //看车详情
    public JSONObject carOwnernew() throws Exception {
        String url = "/WeChat-applet/porsche/car-owner-splendour/newest";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //看车详情
    public JSONObject carOwner() {
        String url = "/WeChat-applet/porsche/car-owner-splendour/today-list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //看车详情
    public JSONObject shop() {
        String url = "/WeChat-applet/porsche/shop/current";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约时间段v3.0---xia

    public JSONObject timeList(String type, String date) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/time/list";
        JSONObject json1 = new JSONObject();
        json1.put("type", type);
        json1.put("date", date);

        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addCheckListEmpty(String customer_name, String customer_phone, String appointment_date, Integer car_type, String emptyPara, String message, Integer car_model) throws Exception {
        String url = "/WeChat-applet/porsche/a/appointment/test-drive";

        JSONObject json1 = new JSONObject();
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone);
        json1.put("appointment_date", appointment_date);
        json1.put("car_type", car_type);
        json1.put("car_model", car_model);
        json1.put(emptyPara, "");

        String json = json1.toJSONString();

        String res = httpPost(url, json, IpPort);

        checkCode(res, StatusCode.BAD_REQUEST, "预约试驾，" + emptyPara + "为空！");
//        checkMessage("预约试驾，" + emptyPara + "为空！", res, message);

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
    public JSONObject appointmentpage(String start_day, String end_day, Integer page, Integer size) throws Exception {
        String url = "/porsche/order-manage/order/test-drive/page";
        JSONObject json1 = new JSONObject();
        json1.put("start_day", start_day);
        json1.put("end_day", end_day);
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约试驾列表
    public JSONObject appointmentpage(Integer page, Integer size) throws Exception {
        String url = "/porsche/order-manage/order/test-drive/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //保养列表
    public JSONObject mainAppointmentpage(String start_day, String end_day, Integer page, Integer size) throws Exception {
        String url = "/porsche/order-manage/order/maintain/page";
        JSONObject json1 = new JSONObject();
        json1.put("start_day", start_day);
        json1.put("end_day", end_day);
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //保养列表
    public JSONObject mainAppointmentpage(Integer page, Integer size) throws Exception {
        String url = "/porsche/order-manage/order/maintain/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //维修列表
    public JSONObject repairAppointmentpage(String start_day, String end_day, Integer page, Integer size) throws Exception {
        String url = "/porsche/order-manage/order/repair/page";
        JSONObject json1 = new JSONObject();
        json1.put("start_day", start_day);
        json1.put("end_day", end_day);
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //维修列表
    public JSONObject repairAppointmentpage(Integer page, Integer size) throws Exception {
        String url = "/porsche/order-manage/order/repair/page";
        JSONObject json1 = new JSONObject();

        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //活动报名顾客列表
    public JSONObject activityList(Integer page, Integer size, Long activity_id) {
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
    public JSONObject blacklist(String start_day, String end_day, Integer page, Integer size) throws Exception {
        String url = "/porsche/activity/customer/black/page";
        JSONObject json1 = new JSONObject();
        json1.put("start_date", start_day);
        json1.put("end_date", end_day);
        json1.put("page", page);
        json1.put("size", size);
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
    public JSONObject createArticle(String positions, String valid_start, String valid_end, String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String article_title, Boolean is_pic_content, String article_bg_pic, String article_content, String article_remarks, boolean is_online_activity, String reception_name, String reception_phone, String customer_max, String simulation_num, String activity_start, String activity_end, Integer role_id, String task_customer_num, Boolean is_create_poster
    ) throws Exception {
        String url = "/porsche/article/add";
        JSONObject json1 = new JSONObject();
        json1.put("position", positions);
        json1.put("valid_start", valid_start);
        json1.put("valid_end", valid_end);
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("article_title", article_title);
        json1.put("is_pic_content", is_pic_content);
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

    public JSONObject createArticlecode(String positions, String valid_start, String valid_end, String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String article_title, Boolean is_pic_content, String article_bg_pic, String article_content, String article_remarks, boolean is_online_activity, String reception_name, String reception_phone, String customer_max, String simulation_num, String activity_start, String activity_end, Integer role_id, String task_customer_num, Boolean is_create_poster
    ) throws Exception {
        String url = "/porsche/article/add";
        JSONObject json1 = new JSONObject();
        json1.put("position", positions);
        json1.put("valid_start", valid_start);
        json1.put("valid_end", valid_end);
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("article_title", article_title);
        json1.put("is_pic_content", is_pic_content);
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

        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //新建文章
    public JSONObject createArticleReal(String positions, String valid_start, String valid_end, String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String article_title, Boolean is_pic_content, String article_bg_pic, String article_content, String article_remarks, boolean is_online_activity
    ) throws Exception {
        String url = "/porsche/article/add";
        JSONObject json1 = new JSONObject();
        json1.put("position", positions);
        json1.put("valid_start", valid_start);
        json1.put("valid_end", valid_end);
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("article_title", article_title);
        json1.put("is_pic_content", is_pic_content);
        json1.put("article_bg_pic", article_bg_pic);
        json1.put("article_content", article_content);
        json1.put("article_remarks", article_remarks);
        json1.put("is_online_activity", is_online_activity);

        String json = json1.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc文章详情
    public JSONObject artilceDetailpc(Long id) {
        String url = "/porsche/article/view/";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject appartilceDetail(Long id, String article_type) throws Exception {
        String url = "/WeChat-applet/porsche/article/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("article_type", article_type);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //文章列表
    public JSONObject articlePage(Integer page, Integer size, String position) throws Exception {
        String url = "/porsche/article/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("position", position);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //坑位列表
    public JSONObject positionList() throws Exception {
        String url = "/porsche/article/position/list";
        String json = "{}";
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

        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res).getLong("code");
    }

    //pc车辆列表
    public JSONObject carList() {
        String url = "/porsche/goods-manage/car-list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc车辆删除
    public JSONObject carDelete(Integer id) {
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
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //pc活动列表
    public JSONObject activityShowList() throws Exception {
        String url = "/porsche/activity/list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :站内消息，有效活动列表--x
     * @date :2020/8/12 18:14
     **/

    public JSONObject activityVaild() throws Exception {
        String url = "/porsche/activity/valid/list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :新建站内活动
     * @date :2020/8/12 18:22
     **/
    public JSONObject createMessage(String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String send_time, String title, String content, String appointment_type, Long activity_id) throws Exception {
        String url = "/porsche/message/add";
        JSONObject json1 = new JSONObject();
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("send_time", send_time);
        json1.put("title", title);
        json1.put("content", content);
        json1.put("appointment_type", appointment_type);
        json1.put("activity_id", activity_id);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");

    }

    public JSONObject createMessage(String[] customer_types, int[] car_types, int[] customer_level, String[] customer_property, String send_time, String title, String content) throws Exception {
        String url = "/porsche/message/add";
        JSONObject json1 = new JSONObject();
        json1.put("customer_types", customer_types);
        json1.put("car_types", car_types);
        json1.put("customer_level", customer_level);
        json1.put("customer_property", customer_property);
        json1.put("send_time", send_time);
        json1.put("title", title);
        json1.put("content", content);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");

    }


    //*************************app relate*************************
    //app 预约试驾全部预约及今日预约人数
    public JSONObject appointmentTestDriverNumber() {
        String url = "/porsche/app/appointment/appointment_test_driver_number";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
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

    public JSONObject mainAppointmentList(int page, int size) {
        String url = "/porsche/app/after_sale/appointment_maintain_list";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //预约维修列表展示
    public JSONObject repairAppointmentlist() {
        String url = "/porsche/app/after_sale/appointment_mend_list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //预约维修列表展示
    public JSONObject provinceList() {
        String url = "/WeChat-applet/porsche/car/plate-number-province-list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    //---------------------app前台-----------------
    public JSONObject creatReception(String FIRST_VISIT) throws Exception {
        String url = "/porsche/app/sale-reception/reception";
        JSONObject json = new JSONObject();
        json.put("reception_type", FIRST_VISIT);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject creatReception2(String FIRST_VISIT, JSONArray new_customer_list) throws Exception {
        String url = "/porsche/app/sale-reception/reception";
        JSONObject json = new JSONObject();
        json.put("reception_type", FIRST_VISIT);
        json.put("new_customer_list", new_customer_list);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject freeSaleList() {
        String url = "/porsche/app/sale-reception/freeSaleList";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //移除黑名单
    public JSONObject blackRemove(String customer_id) throws Exception {
        String url = "/porsche/activity/customer/black/remove";
        JSONObject object = new JSONObject();
        object.put("customer_id", customer_id);
        String json = object.toJSONString();

        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //创建编辑页面添加人脸列表
    public JSONObject publicFaceList() {
        String url = "/porsche/app/customer/publicFaceList";
        JSONObject object = new JSONObject();
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSONObject.parseObject(result);
    }

    //售后客户标记
    public JSONObject afterSalelCustomer(String analysisCustomerId) {
        String url = "/porsche/app/customer/afterSalelCustomer";
        JSONObject object = new JSONObject();
        object.put("analysis_customer_id", analysisCustomerId);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSONObject.parseObject(result);
    }

    //文章投放位置
    @DataProvider(name = "POSITIONS")
    public static Object[] positions() {

        return new String[]{
                //"MODEL_RECOMMENDATION",
                "PURCHASE_GUIDE",
                "BRAND_CULTURE",
                "CAR_ACTIVITY"
        };
    }

    //------------------------售后------------------------
    //售后：客户管理->列表展示
    public JSONObject afterSaleCustList(String search_condition, String search_date_start, String search_date_end, int page, int size) {
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

    //售后：客户管理->编辑客户信息
    public JSONObject afterSaleCustList(Long after_record_id, String customer_name, String customer_phone_number, String customer_secondary_phone,
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

    //售后：客户管理->列表展示
    public JSONObject afterSaleCustomerList(String search_condition, String search_date_start, String search_date_end, int page, int size) throws Exception {
        String url = "/porsche/app/after_sale/after-sale-customer-list";
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
    public JSONObject reception_customer(Long appointment_id) {
        String url = "/porsche/app/after_sale/reception_after_sale_customer";
        String json = "{\"appointment_id\":" + appointment_id + "}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //售后：查询客户信息
    public JSONObject detailAfterSaleCustomer(String afterRecordId) {
        String url = "/porsche/app/after_sale/detail_after_sale_customer";
        JSONObject object = new JSONObject();
        object.put("after_record_id", afterRecordId);
        return invokeApi(url, object);
    }

    //售后：回访操作
    public JSONObject afterSale_addVisitRecord(Long id, String return_visit_pic, String comment, String next_return_visit_time) {
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
    public JSONObject afterSale_VisitRecordList(int page, int size, String search_name_phone, String search_start_day, String search_end_day) {
        String url = "/porsche/app/return-visit-record/after-sale/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        if (!StringUtils.isEmpty(search_name_phone)) {
            object.put("search_name_phone", search_name_phone);
        }
        if (!StringUtils.isEmpty(search_start_day)) {
            object.put("search_start_day", search_start_day);
        }
        if (!StringUtils.isEmpty(search_end_day)) {
            object.put("search_end_day", search_end_day);
        }
        return invokeApi(url, object);
    }

    //售后：首保提醒列表
    public JSONObject afterSale_firstmMintainRecordList(int page, int size, String search_name_phone, String search_start_day, String search_end_day) throws Exception {
        String url = "/porsche/app/return-visit-record/first-maintain-record/page";
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

    //售后：流失预警列表
    public JSONObject afterSale_customerChurnWarningList(int page, int size, String search_name_phone, String search_start_day, String search_end_day) throws Exception {
        String url = "/porsche/app/return-visit-record/customer-churn-warning/page";
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
    public JSONObject registeredCustomer(Long activity_task_id, String customer_name, String customer_phone_number) {
        String url = "/porsche/app/activity-task/registeredCustomer";
        JSONObject json1 = new JSONObject();
        json1.put("activity_task_id", activity_task_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        String json = json1.toJSONString();
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject registeredCustomer1(Long activity_task_id, String customer_name, String customer_phone_number) throws Exception {
        String url = "/porsche/app/activity-task/registeredCustomer";
        JSONObject json1 = new JSONObject();
        json1.put("activity_task_id", activity_task_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        String json = json1.toJSONString();
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //活动：添加报名人信息
    public Long registeredCustomerCode(Long activity_task_id, String customer_name, String customer_phone_number) throws Exception {
        String url = "/porsche/app/activity-task/registeredCustomer";
        JSONObject json1 = new JSONObject();
        json1.put("activity_task_id", activity_task_id);
        json1.put("customer_name", customer_name);
        json1.put("customer_phone_number", customer_phone_number);
        String json = json1.toJSONString();
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res).getLong("code");
    }

    //活动：活动报名人数
    public JSONObject TaskInfo(Long activity_task_id, String customer_phone_number) throws Exception {
        String url = "/porsche/app/activity-task/info";
        JSONObject json1 = new JSONObject();
        json1.put("activity_task_id", activity_task_id);
        json1.put("customer_phone_number", customer_phone_number);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //人员管理-销售顾问列表
    public JSONObject ManageList(Integer role_id) {
        String url = "/porsche/role-staff/manage/list";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //人员管理-销售顾问列表
    public JSONObject ManageListNoSelect(Integer role_id) {
        String url = "/porsche/role-staff/manage/list/no-selected";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //人员管理-销售顾问列表
    public JSONObject ManageAdd(Integer role_id, String uid) {
        String url = "/porsche/role-staff/manage/add";
        JSONObject json1 = new JSONObject();
        json1.put("role_id", role_id);
        json1.put("uid", uid);
        String res = httpPostWithCheckCode(url, json1.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //删除销售排版中销售
    public JSONObject ManageDelete(Integer id) {
        String url = "/porsche/role-staff/manage/delete/" + id;
        String json = "{}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    //获取活动列表 --勿删除
    public JSONObject activityTaskPageX() {
        String url = "/porsche/app/activity-task/page";
        JSONObject object = new JSONObject();
        object.put("page", 1);
        object.put("size", 10);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
    }

    //删除报名人员--勿删除
    public JSONObject deleteCustomerX(String activityTaskId, String customerId) {
        String url = "/porsche/app/activity-task/deleteCustomer";
        JSONObject object = new JSONObject();
        object.put("activity_task_id", activityTaskId);
        object.put("customer_id", customerId);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
    }

    //获取任务客户列表--勿删除
    public JSONObject customerTaskPageX(int size, int page, Long activityId) {
        String url = "/porsche/activity/customer/task/page";
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("activity_id", activityId);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(object), IpPort);
        return JSON.parseObject(result);
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

    //--------------------------web1.0------------------------

    /**
     * 修改客户信息
     *
     * @param customerId    客户id
     * @param customerName  客户姓名
     * @param customerPhone 客户电话
     * @param customerLevel 客户等级
     * @param belongsSaleId 所属销售id
     */
    public JSONObject customerEdit(Long customerId, String customerName, String customerPhone, int customerLevel, String belongsSaleId) {
        String url = "/porsche/customer/edit";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("shop_id", EnumShopId.PORSCHE_SHOP.getShopId());
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("customer_level", customerLevel);
        object.put("belongs_sale_id", belongsSaleId);
        return invokeApi(url, object);
    }

    /**
     * 战败客户列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public JSONObject failureCustomerList(String startTime, String endTime, Integer page, Integer size) {
        String url = "/porsche/customer/failure_customer_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * 查看客户信息列表接口
     *
     * @param customerName  客户名称
     * @param customerPhone 客户电话
     * @param customerLevel 客户等级
     * @param startTime     开始时间
     * @param endTime       结束时间
     */
    public JSONObject customerList(String customerName, String customerPhone, String customerLevel, String startTime, String endTime, Integer page, Integer size) {
        String url = "/porsche/customer/list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(customerName)) {
            object.put("customer_name", customerName);
        }
        if (!StringUtils.isEmpty(customerPhone)) {
            object.put("customer_phone", customerPhone);
        }
        if (!StringUtils.isEmpty(customerLevel)) {
            object.put("customer_level", customerLevel);
        }
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        if (!(page <= 0)) {
            object.put("page", page);
        }
        if (!(size <= 0)) {
            object.put("size", size);
        }
        return invokeApi(url, object);
    }

    /**
     * 获得我的回访任务列表接口
     *
     * @param data                ”yyyy-mm-dd”形式的日期,可以用来过滤要查看的范围
     * @param status              0：未执行； 1：已执行
     * @param customerName        客户姓名（过滤选项）
     * @param customerPhoneNumber 客户手机号码（过滤选项）
     * @param customerLevel       客户级别（过滤选项）
     */
    public JSONObject withFilterAndCustomerDetail(String data, Integer status, Integer page, Integer size, String customerName, String customerPhoneNumber, String customerLevel) {
        String url = "/porsche/return-visit/task/list/withFilterAndCustomerDetail";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(data)) {
            object.put("data", data);
        }
        if (!StringUtils.isEmpty(customerName)) {
            object.put("customer_name", customerName);
        }
        if (!StringUtils.isEmpty(customerPhoneNumber)) {
            object.put("customer_phone_number", customerPhoneNumber);
        }
        if (!StringUtils.isEmpty(customerLevel)) {
            object.put("customer_level", customerLevel);
        }
        object.put("status", status);
        if (!(page <= 0)) {
            object.put("page", page);
        }
        if (!(size <= 0)) {
            object.put("size", size);
        }
        return invokeApi(url, object);
    }

    //--------------------------web2.0------------------------

    /**
     * 战败转公海接口
     *
     * @param customerId 客户id
     */
    public JSONObject failureCustomerToPublic(long... customerId) {
        String url = "/porsche/customer/failure_customer_to_public";
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        Arrays.stream(customerId).forEach(e -> {
            JSONObject obj = new JSONObject();
            obj.put("customer_id", e);
            array.add(obj);
        });
        object.put("customer_list", array);
        return invokeApi(url, object);
    }

    /**
     * 小程序列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public JSONObject wechatCustomerList(String startTime, String endTime, Integer page, Integer size) {
        String url = "/porsche/customer/wechat_customer_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * 商品管理添加车辆接口
     *
     * @param appearancePic 外观照片
     * @param bigPic        大图照片
     * @param carDiscount   车辆优惠
     * @param carIntroduce  车辆介绍
     * @param carPic        车辆照片
     * @param carTypeName   车型
     * @param highestPrice  最高价
     * @param interiorPic   内饰照片
     * @param lowestPrice   最低价
     * @param spacePic      空间照片
     */
    public JSONObject goodsManagerAddCar(String appearancePic, String bigPic, String carDiscount, String carIntroduce,
                                         String carPic, String carTypeName, double highestPrice, String interiorPic,
                                         double lowestPrice, String spacePic) throws Exception {
        String url = "/porsche/goods-manage/add-car";
        JSONObject object = new JSONObject();
        object.put("car_type_name", carTypeName);
        if (!(lowestPrice < 0)) {
            object.put("lowest_price", lowestPrice);
        }
        if (!(highestPrice < 0)) {
            object.put("highest_price", highestPrice);
        }
        if (!StringUtils.isEmpty(carDiscount)) {
            object.put("car_discount", carDiscount);
        }
        object.put("car_introduce", carIntroduce);
        object.put("car_pic", carPic);
        object.put("big_pic", bigPic);
        object.put("appearance_pic", appearancePic);
        object.put("interior_pic", interiorPic);
        object.put("space_pic", spacePic);
        String request = JSON.toJSONString(object);
        String result = httpPost(url, request, IpPort);
        return JSON.parseObject(result);
    }


    //--------------------------web2.0------------------------

    /**
     * 活动任务顾客列表分页接口
     *
     * @param size       大于0，小于等于100
     * @param page       第几页 小于1 按第一页处理，大于最大页数，默认按最后一页处理
     * @param activityId 活动id
     * @return response
     */
    public JSONObject customerTaskPage(int size, int page, Long activityId) {
        String url = "/porsche/activity/customer/task/page";
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("activity_id", activityId);
        return invokeApi(url, object);
    }

    /**
     * 预约试驾列表
     *
     * @param startDay 开始日期 YYYY-MM-dd
     * @param endDay   结束日期 YYYY-MM-dd
     */
    public JSONObject testDriverPage(String startDay, String endDay, Integer page, Integer size) {
        String url = "/porsche/order-manage/order/test-drive/page";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startDay)) {
            object.put("start_day", startDay);
        }
        if (!StringUtils.isEmpty(endDay)) {
            object.put("emd_day", endDay);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * 公海列表接口
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public JSONObject publicCustomerList(String startTime, String endTime, Integer size, Integer page) {
        String url = "/porsche/customer/public_customer_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        if (!(page <= 0)) {
            object.put("page", page);
        }
        if (!(size <= 0)) {
            object.put("size", size);
        }
        return invokeApi(url, object);
    }

    //--------------------------web3.0------------------------

    /**
     * 查看消息详情接口
     *
     * @param id 消息id
     */
    public JSONObject messageDetail(Integer id) {
        String url = "/porsche/message/detail";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    /**
     * 消息删除接口
     *
     * @param id 消息id
     */
    public JSONObject messageDelete(Integer id) {
        String url = "/porsche/message/delete";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    /**
     * 新增消息接口
     *
     * @param customerTypes    客户类别  PRE_SALES:售前  AFTER_SALES：售后
     * @param carTypes         车型 使用 https://winsense.yuque.com/staff-qt5ptf/umvi00/dyxsu5#mrKqG 车型列表
     * @param customerLevel    客户级别 使用 https://winsense.yuque.com/staff-qt5ptf/umvi00/ksu0kd#11235be0 等级列表
     * @param customerProperty 客户属性 LOST：流失客户，MAINTENANCE：保养客户，LOYAL：忠诚客户
     * @param sendTime         格式yyyy-MM-dd HH:mm
     * @param title            消息标题
     * @param content          消息标题
     * @param appointmentType  预约类型类型 TEST_DRIVE("试驾"), REPAIR("维修"), MAINTAIN("保养"), ACTIVITY("活动")
     * @param activityId       预约类型为是时 不能为空，活动id使用3.2接口获取
     */
    public JSONObject messageAdd(String carTypes, String customerLevel, String customerProperty, String sendTime, String title, String content, String appointmentType, String activityId, String... customerTypes) {
        String url = "/porsche/message/add";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(carTypes)) {
            object.put("car_types", carTypes);
        }
        if (!StringUtils.isEmpty(customerLevel)) {
            object.put("customer_level", customerLevel);
        }
        if (!StringUtils.isEmpty(customerProperty)) {
            object.put("customer_property", customerProperty);
        }
        if (!StringUtils.isEmpty(appointmentType)) {
            object.put("appointment_type", appointmentType);
        }
        if (!StringUtils.isEmpty(activityId)) {
            object.put("activity_id", activityId);
        }
        object.put("send_time", sendTime);
        object.put("title", title);
        object.put("content", content);
        List<String> list = new ArrayList<>(Arrays.asList(customerTypes));
        object.put("customer_types", list);
        return invokeApi(url, object);
    }

    //--------------------------web4.0------------------------

    /**
     * 新建车系接口
     *
     * @param carStyleName 车系名称
     */
    public JSONObject carStyleAddNewCar(String carStyleName) {
        String url = "/porsche/car-style/add-new-car";
        JSONObject object = new JSONObject();
        object.put("car_style_name", carStyleName);
        return invokeApi(url, object);
    }

    /**
     * 启用or禁用车系接口
     *
     * @param takeEffect 是
     */
    public JSONObject carStyleTaskEffect(boolean takeEffect) {
        String url = "/porsche/car-style/take-effect";
        JSONObject object = new JSONObject();
        object.put("take_effect", takeEffect);
        return invokeApi(url, object);
    }

    /**
     * 修改车系接口
     *
     * @param carStyleId   车系id
     * @param carStyleName 车系名称
     */
    public JSONObject carStyleEditCar(long carStyleId, String carStyleName) {
        String url = "/porsche/car-style/edit-car";
        JSONObject object = new JSONObject();
        object.put("car_style_id", carStyleId);
        object.put("car_style_name", carStyleName);
        return invokeApi(url, object);
    }

    /**
     * 车系列表-其他编辑页面的下拉列表接口
     *
     * @param carStyleId   车系id
     * @param carStyleName 车系名称
     */
    public JSONObject carStyleList(long carStyleId, String carStyleName) {
        String url = "/porsche/car-style/car_style_list";
        JSONObject object = new JSONObject();
        object.put("car_style_id", carStyleId);
        object.put("car_style_name", carStyleName);
        return invokeApi(url, object);
    }

    /**
     * 新建车型接口
     *
     * @param carStyleId   车系id
     * @param carModelName 车型名称
     */
    public JSONObject carModelAddNewCar(long carStyleId, String carModelName) {
        String url = "/porsche/car-model/add-new-car";
        JSONObject object = new JSONObject();
        object.put("car_style_id", carStyleId);
        object.put("car_model_name", carModelName);
        return invokeApi(url, object);
    }

    /**
     * 新增试驾车接口
     *
     * @param carName            试驾车名字
     * @param carStyleId         试驾车系id
     * @param carModelId         试驾车型id
     * @param plateNumber        车牌号
     * @param vehicleChassisCode 车架号
     * @param serviceTimeStart   服役开始时间
     * @param serviceTimeEnd     服役结束时间
     */
    public JSONObject testDriveCarAdd(String carName, long carStyleId, long carModelId, String plateNumber, String vehicleChassisCode, long serviceTimeStart, long serviceTimeEnd) {
        String url = "/porsche/test-drive-car/management/add";
        JSONObject object = new JSONObject();
        object.put("car_name", carName);
        object.put("car_style_id", carStyleId);
        object.put("car_model_id", carModelId);
        object.put("plate_number", plateNumber);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("service_time_start", serviceTimeStart);
        object.put("service_time_end", serviceTimeEnd);
        return invokeApi(url, object);
    }

    /**
     * dcc客户列表
     */
    public JSONObject dccList(String customerName, String customerPhone, String startTime, String endTime, int page, int size) {
        String url = "/porsche/customer/dcc-list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(customerName)) {
            object.put("customer_name", customerName);
        }
        if (!StringUtils.isEmpty(customerPhone)) {
            object.put("customer_phone", customerPhone);
        }
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }


    //--------------------------app2.0------------------------

    /**
     * 销售前台分配销售接口
     */
    public JSONObject saleReceptionCreatReception() {
        String url = "/porsche/app/sale-reception/createReception";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    /**
     * 删除报名人信息接口
     *
     * @param activityTaskId 活动任务Id
     * @param customerId     顾客id
     */
    public JSONObject deleteCustomer(String activityTaskId, Integer customerId) {
        String url = "/porsche/app/activity-task/deleteCustomer";
        JSONObject object = new JSONObject();
        object.put("activity_task_id", activityTaskId);
        object.put("customer_id", customerId);
        return invokeApi(url, object);
    }

    /**
     * 分配销售接口
     *
     * @param saleId     销售id
     * @param customerId 客户id
     */
    public JSONObject allocationSale(String saleId, Long customerId) {
        String url = "/porsche/app/sale-reception/allocationSale";
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("customer_id", customerId);
        return invokeApi(url, object);
    }

    //--------------------------app2.1------------------------

    /**
     * 客户级别列表接口
     */
    public JSONObject appCustomerLevelList() {
        String url = "/porsche/app/customer/customer-level/list";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    /**
     * 创建线索枚举接口
     */
    public JSONObject afterSaleEnumInfo() {
        String url = "/porsche/app/after_sale/enum_info";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    /**
     * 销售排班列表接口
     */
    public JSONObject saleOrderList() {
        String url = "/porsche/app/sale-reception/sale-order-list";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    /**
     * 销售排班接口
     *
     * @param saleId 销售Id
     * @param order  顺序
     */
    public JSONObject saleOrder(String saleId, Integer order) {
        String url = "/porsche/app/sale-reception/sale-order";
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("order", order);
        return invokeApi(url, object);
    }

    /**
     * 活动任务列表接口
     */
    public JSONObject activityTaskPage(Integer page, Integer size) {
        String url = "/porsche/app/activity-task/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * 活动任务详情接口
     *
     * @param activityTaskId 任务id
     */
    public JSONObject activityTaskInfo(String activityTaskId) {
        String url = "/porsche/app/activity-task/info";
        JSONObject object = new JSONObject();
        object.put("activity_task_id", activityTaskId);
        return invokeApi(url, object);
    }

    /**
     * 接待列表接口
     *
     * @param page      页码
     * @param size      页大小
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return response
     */
    public JSONObject receptionPage(Integer page, Integer size, String startTime, String endTime) {
        String url = "/porsche/app/sale-reception/reception-page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("startTime", startTime);
        object.put("endTime", endTime);
        return invokeApi(url, object);
    }

    /**
     * 接待列表接口
     *
     * @param page              页码
     * @param size              页大小
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param customerNamePhone 客户名称/联系方式
     */
    public JSONObject receptionPage(String customerNamePhone, String startTime, String endTime, String page, String size) {
        String url = "/porsche/app/sale-reception/reception-page";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(customerNamePhone)) {
            object.put("customer_name_phone", customerNamePhone);
        }
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * 我的回访列表接口
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     */
    public JSONObject returnVisitTaskPage(int page, int size, String startTime, String endTime) {
        String url = "/porsche/app/return-visit-task/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        return invokeApi(url, object);
    }

    /**
     * 回访详情接口
     *
     * @param taskId 回访任务Id
     */
    public JSONObject showPicResult(final int taskId) {
        String url = "/porsche/app/return-visit-task/show-pic-result";
        JSONObject object = new JSONObject();
        object.put("task_id", taskId);
        String request = JSON.toJSONString(object);
        String result = httpPostWithCheckCode(url, request, IpPort);
        return JSON.parseObject(result);
    }

    /**
     * @param comment             回访记录
     * @param failureCause        战败原因 {@link com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumFailureCause}
     * @param failureCauseRemark  战败备注
     * @param ifSystemRecommend   是否系统推荐
     * @param nextReturnVisitDate 下次回访日期
     * @param otherStoreCarType   他店购车车型 {@link com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarModel}
     * @param preBuyCarTime       预计购车时间
     * @param returnVisitResult   回访结果 {@link com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumReturnVisitResult}
     * @param taskId              任务id
     * @param returnVisitPic      回访图片
     */
    public JSONObject returnVisitTaskExecute(String comment, String failureCause, String failureCauseRemark, boolean ifSystemRecommend, String nextReturnVisitDate, String otherStoreCarType, String preBuyCarTime, String returnVisitResult, String taskId, String... returnVisitPic) {
        String url = "/porsche/app/return-visit-task/execute";
        JSONArray array = new JSONArray();
        Arrays.stream(returnVisitPic).forEach(e -> {
            JSONObject object = new JSONObject();
            object.put("return_visit_pic", e);
            array.add(object);
        });
        JSONObject object = new JSONObject();
        object.put("comment", comment);
        if (!StringUtils.isEmpty(failureCause)) {
            object.put("failure_cause", failureCause);
        }
        if (!StringUtils.isEmpty(failureCauseRemark)) {
            object.put("failure_cause_remark", failureCauseRemark);
        }
        if (!StringUtils.isEmpty(otherStoreCarType)) {
            object.put("other_store_car_type", otherStoreCarType);
        }
        object.put("if_system_recommend", ifSystemRecommend);
        object.put("next_return_visit_date", nextReturnVisitDate);
        object.put("pre_buy_car_time", preBuyCarTime);
        object.put("return_visit_pic_list", array);
        object.put("return_visit_result", returnVisitResult);
        object.put("task_id", taskId);
        String request = JSON.toJSONString(object);
        String result = null;
        try {
            result = httpPost(url, request, IpPort);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        return JSON.parseObject(result);
    }


    public JSONObject deliverSelect(int page, int size) {
        String url = "/porsche/daily-work/deliver-car/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject deliverSelect(int page, int size, String search_condition) {
        String url = "/porsche/daily-work/deliver-car/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("search_condition", search_condition);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject deliverSelect(int page, int size, String start_date, String end_date) {
        String url = "/porsche/daily-work/deliver-car/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject deliverSelect(int page, int size, String search_condition, String start_date, String end_date) {
        String url = "/porsche/daily-work/deliver-car/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        json.put("search_condition", search_condition);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject jiaocheTotal() {
        String url = "/porsche/daily-work/deliver-car/app/deliver-car-total";
        String json = "{}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject driverTotal() {
        String url = "/porsche/daily-work/test-drive/app/test-driver-total";
        String json = "{}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject driverSelect(int page, int size) {
        String url = "/porsche/daily-work/deliver-car/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject driverSelect(int page, int size, String search_condition) {
        String url = "/porsche/daily-work/test-drive/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("search_condition", search_condition);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject driverSelect(int page, int size, String start_date, String end_date) {
        String url = "/porsche/daily-work/test-drive/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject driverSelect(int page, int size, String search_condition, String start_date, String end_date) {
        String url = "/porsche/daily-work/test-drive/app/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        json.put("search_condition", search_condition);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //我的客户
    public JSONObject customerSelect(int page, int size) {
        String url = "/porsche/app/customer/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject customerSelect(int page, int size, String search_condition) {
        String url = "/porsche/app/customer/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("customer_name_phone", search_condition);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject customerSelect(int page, int size, String start_time, String end_time) {
        String url = "/porsche/app/customer/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject customerSelect(int page, int size, String search_condition, String start_time, String end_time) {
        String url = "/porsche/app/customer/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("page", page);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        json.put("customer_name_phone", search_condition);
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //创建线索
    public JSONObject createLine(String customer_name, int like_car, String customer_phone, Integer customer_level, String remark) throws Exception {
        String url = "/porsche/app/customer/create";
        JSONObject json = new JSONObject();
        json.put("customer_name", customer_name);
        if (!(like_car <= 0)) {
            json.put("intention_car_model", 37);
        }
        json.put("intention_car_style", 1);
        json.put("customer_phone", customer_phone);
        if (!(customer_level <= 0)) {
            json.put("customer_level", customer_level);
        }
        json.put("remark", remark);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result);
    }

    /**
     * 创建线索
     *
     * @param intentionCarModel 车型编号
     * @param intentionCarStyle 车系编号
     */
    public JSONObject customerCreate(String customerName, String customerLevel, String customerPhone, String intentionCarModel, String intentionCarStyle, String remark) throws Exception {
        String url = "/porsche/app/customer/create";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(customerName)) {
            object.put("customer_name", customerName);
        }
        if (!StringUtils.isEmpty(customerPhone)) {
            object.put("customer_phone", customerPhone);
        }
        if (!StringUtils.isEmpty(customerLevel)) {
            object.put("customer_level", customerLevel);
        }
        if (!StringUtils.isEmpty(intentionCarModel)) {
            object.put("intention_car_model", intentionCarModel);
        }
        if (!StringUtils.isEmpty(intentionCarStyle)) {
            object.put("intention_car_style", intentionCarStyle);
        }
        if (!StringUtils.isEmpty(remark)) {
            object.put("remark", remark);
        }
        String request = JSON.toJSONString(object);
        String result = httpPost(url, request, IpPort);
        return JSON.parseObject(result);
    }

    public JSONObject deliverCarList(int page, int size) throws Exception {
        String url = "/porsche/daily-work/deliver-car/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //车型推荐列表
    public JSONObject carReCommendList(int page, int size) throws Exception {
        String url = "/porsche/sku/car/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //applet 我的消息分页
    public JSONObject messageList(int size, String type) throws Exception {
        String url = "/WeChat-applet/porsche/a/message/list";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("type", type);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 微信我的消息列表接口
     *
     * @param lastValue
     * @param size
     * @return
     */
    public JSONObject wechatMessageList(String lastValue, int size) {
        String url = "/WeChat-applet/porsche/a/message/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return invokeApi(url, object);
    }


    //applet 我的消息分页
    public JSONObject messageDetail(Long id) throws Exception {
        String url = "/WeChat-applet/porsche/a/message/detail/" + id;
        JSONObject json = new JSONObject();

        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //评价查询
    public JSONObject evaluateList(Integer page, Integer size, String start_date, String end_date, String name) throws Exception {
        String url = "/porsche/evaluate/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("size", size);
        if (!start_date.equals("")) {
            json.put("start_date", size);
        }
        if (!end_date.equals("")) {
            json.put("end_date", size);
        }
        if (!name.equals("")) {
            json.put("reception_sale_name", name);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject messagePage(int page, int size) throws Exception {
        String url = "/porsche/message/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject phoneCheck(String customer_phone) throws Exception {
        String url = "/porsche/app/sale-reception/phoneCheck";
        JSONObject json = new JSONObject();
        json.put("customer_phone", customer_phone);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject receptionOld(Long phone_check_customer_id, String reception_type) throws Exception {
        String url = "/porsche/app/sale-reception/reception";
        JSONObject json = new JSONObject();
        json.put("phone_check_customer_id", phone_check_customer_id);
        json.put("reception_type", reception_type);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    public JSONObject importCustom(String file, String type) throws Exception {
        String url = "/porsche/import/customer";
//        String result = httpPostFile(url, file, type, IpPort);
        HttpExecutorUtil httpExecutorUtil = new HttpExecutorUtil();
        Map<String, String> headers = new ConcurrentReferenceHashMap<>();
        headers.put("Authorization", super.authorization);
        headers.put("shop_id", "22728");
        httpExecutorUtil.uploadFile(IpPort + url, file, headers, type);
        return JSON.parseObject(httpExecutorUtil.getResponse());
    }

    public String outportC() throws Exception {
        String url = "/porsche/administration/reception/export?sale_type=PRE_SALES&name=&phone=";
        JSONObject json = new JSONObject();
        String result = httpGet(url, JSON.toJSONString(json), IpPort);
        return result;
    }

    /**
     * 我的接待查询接口
     *
     * @param searchCondition 查询条件，姓名or联系方式
     * @param searchDateStart 日期起始时间
     * @param searchDateEnd   日期结束时间
     */
    public JSONObject customerMyReceptionList(String searchCondition, String searchDateStart, String searchDateEnd, Integer size, Integer page) {
        String url = "/porsche/app/customer/my-reception-list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(searchCondition)) {
            object.put("search_condition", searchCondition);
        }
        if (!StringUtils.isEmpty(searchDateStart)) {
            object.put("search_date_start", searchDateStart);
        }
        if (!StringUtils.isEmpty(searchDateEnd)) {
            object.put("search_date_end", searchDateEnd);
        }
        if (!(page <= 0)) {
            object.put("size", size);
        }
        if (!(size <= 0)) {
            object.put("page", page);
        }
        return invokeApi(url, object);
    }

    /**
     * 删除客户接口
     *
     * @param customerId 客户id
     */
    public JSONObject customerDelete(int customerId) {
        String url = "/porsche/customer/delete";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return invokeApi(url, object);
    }

    /**
     * 我的接待接口
     */
    public JSONObject customerReceptionTotalInfo() {
        String url = "/porsche/app/customer/reception-total-info";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    /**
     * 我的客户列表接口
     */
    public JSONObject customerPage(Integer size, Integer page, String customerNamePhone, String startTime, String endTime) {
        String url = "/porsche/app/customer/page";
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        if (!StringUtils.isEmpty(customerNamePhone)) {
            object.put("customer_name_phone", customerNamePhone);
        }
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        return invokeApi(url, object);
    }

    /**
     * 公海客户分配销售接口
     *
     * @param saleId     分配销售的id
     * @param customerId 公海客户id，可输入多个
     *                   eg：1，2，3，4
     */
    public JSONObject customerAllot(String saleId, long... customerId) {
        String url = "/porsche/customer/customer_allot";
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        Arrays.stream(customerId).forEach(e -> {
            JSONObject obj = new JSONObject();
            obj.put("customer_id", e);
            array.add(obj);
        });
        object.put("customer_list", array);
        object.put("sale_id", saleId);
        return invokeApi(url, object);
    }

    /**
     * 我的预约列表
     *
     * @param searchCondition 按照客户名称或者联系方式搜索
     * @param searchDateStart 按照预约日期搜索(yyyy-MM-dd)
     * @param searchDateEnd   按照预约日期搜索(yyyy-MM-dd)
     */
    public JSONObject appointmentTestDriverList(String searchCondition, String searchDateStart, String searchDateEnd, int page, int size) {
        String url = "/porsche/app/after_sale/appointment_test_driver_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(searchCondition)) {
            object.put("search_condition", searchCondition);
        }
        if (!StringUtils.isEmpty(searchDateStart)) {
            object.put("search_date_start", searchDateStart);
        }
        if (!StringUtils.isEmpty(searchDateEnd)) {
            object.put("search_date_end", searchDateEnd);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * 我的交车列表
     */
    public JSONObject deliverCarTotal() {
        String url = "/porsche/daily-work/deliver-car/app/deliver-car-total";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    /**
     * 我的交车列表查询接口
     *
     * @param searchCondition 搜索条件，客户姓名
     * @param page            页数，若想接口无此参数传入0
     * @param size            页大小，若想接口无此参数传入0
     * @param startDate       开始时间
     * @param endDate         结束时间
     */
    public JSONObject deliverCarAppList(String searchCondition, Integer page, Integer size, String startDate, String endDate) {
        String url = "/porsche/daily-work/deliver-car/app/list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(searchCondition)) {
            object.put("search_condition", searchCondition);
        }
        if (!(page <= 0)) {
            object.put("page", page);
        }
        if (!(size <= 0)) {
            object.put("size", size);
        }
        if (!StringUtils.isEmpty(startDate)) {
            object.put("start_date", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            object.put("end_date", endDate);
        }
        return invokeApi(url, object);
    }

    /**
     * 销售前台客户分配接口
     *
     * @param receptionType 接待类型（FIRST_VISIT:首次到店 / INVITATION :邀约 /AGAIN_VISIT:再次到店）
     */
    public JSONObject saleReception(String receptionType, JSONArray customer, JSONArray newCustomer) {
        String url = "/porsche/app/sale-reception/reception";
        JSONObject object = new JSONObject();
        object.put("reception_type", receptionType);
        object.put("customer_list", customer);
        object.put("new_customer_list", newCustomer);
        return invokeApi(url, object);
    }

    /**
     * 我的试驾列表接口
     *
     * @param searchCondition 搜索条件，客户姓名
     * @param startDate       开始时间
     * @param endDate         结束时间
     */
    public JSONObject testDriverAppList(String searchCondition, String startDate, String endDate, Integer size, Integer page) {
        String url = "/porsche/daily-work/test-drive/app/list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(searchCondition)) {
            object.put("search_condition", searchCondition);
        }
        if (!StringUtils.isEmpty(startDate)) {
            object.put("start_date", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            object.put("end_date", endDate);
        }
        object.put("size", size);
        object.put("page", page);
        return invokeApi(url, object);
    }

    /**
     * DCC客户列表接口
     */
    public JSONObject dccList(String searchCondition, String customerLevel, String startTime, String endTime, String page, String size) {
        String url = "/porsche/app/customer/dcc-list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(searchCondition)) {
            object.put("search_condition", searchCondition);
        }
        if (!StringUtils.isEmpty(customerLevel)) {
            object.put("customer_level", customerLevel);
        }
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //--------------------------applet3.0------------------------

    /**
     * 小程序我的试驾列表
     *
     * @param lastValue 传<0的值表示此字段为空
     */
    public JSONObject appointmentList(Long lastValue, String type, Integer size) {
        String url = "/WeChat-applet/porsche/a/appointment/list";
        JSONObject object = new JSONObject();
        if (lastValue <= 0) {
            object.put("last_value", "");
        }
        if (lastValue > 0) {
            object.put("last_value", lastValue);
        }
        object.put("type", type);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * http请求方法调用
     *
     * @param url         url
     * @param requestBody 请求体
     * @return JSONObject response.data
     */
    private JSONObject invokeApi(String url, JSONObject requestBody) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url不可为空");
        }
        String request = JSON.toJSONString(requestBody);
        String result = httpPostWithCheckCode(url, request, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject invokeApi(IScene scene) {
        return invokeApi(scene.getPath(), scene.getJSONObject());
    }


    /**
     * 3.0数据分析 查询周期列表
     */
    public JSONObject queryCycle() throws Exception {
        String url = "/porsche/analysis2/query-cycle/list";
        JSONObject json = new JSONObject();
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 查询车型列表
     */
    public JSONObject carType() throws Exception {
        String url = "/porsche/analysis2/car-type";
        JSONObject json = new JSONObject();
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 潜在客户分析-服务趋势
     */
    public JSONObject potentialTrend(String dimension, String cycle_type, String month, String sale_id) throws Exception {
        String url = "/porsche/analysis2/potential/trend";
        JSONObject json = new JSONObject();
        json.put("dimension", dimension);
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 所有销售
     */
    public JSONObject allSale() throws Exception {
        String url = "/porsche/reception/allSaleList";
        JSONObject json = new JSONObject();
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 潜在客户分析-客户级别分布
     */
    public JSONObject customerLevel(String cycle_type, String month, String sale_id) throws Exception {
        String url = "/porsche/analysis2/potential/customer-level";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 展厅热区分析接口
     */
    public JSONObject skuRank(String cycleType, String month) {
        String url = "/porsche/analysis2/sku/rank";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(cycleType)) {
            object.put("cycle_type", cycleType);
        }
        if (!StringUtils.isEmpty(month)) {
            object.put("month", month);
        }
        return invokeApi(url, object);
    }

    /**
     * 3.0数据分析 潜在客户分析-潜客年龄分布
     */
    public JSONObject customerAge(String cycle_type, String month, String sale_id, String car_type) throws Exception {
        String url = "/porsche/analysis2/potential/age";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        if (!car_type.equals("")) {
            json.put("car_type", car_type);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 潜在客户分析-潜客性别分布
     */
    public JSONObject customerGender(String cycle_type, String month, String sale_id, String car_type) throws Exception {
        String url = "/porsche/analysis2/potential/gender";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        if (!car_type.equals("")) {
            json.put("car_type", car_type);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * 3.0数据分析 店面数据分析-概览数据
     */
    public JSONObject shopPannel(String cycle_type, String month, String sale_id) {
        String url = "/porsche/analysis2/shop/pannel";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 店面数据分析-顾客接待时长
     */
    public JSONObject receptTime(String cycle_type, String month, String sale_id) {
        String url = "/porsche/analysis2/shop/recept-time";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 店面数据分析-销售顾问漏斗
     */
    public JSONObject saleFunnel(String cycle_type, String month, String sale_id) {
        String url = "/porsche/analysis2/shop/sale-funnel";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject shopSaleFunnel(String cycle_type, String month, String sale_id) {
        String url = "/porsche/analysis2/shop/sale-funnel";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!sale_id.equals("")) {
            json.put("sale_id", sale_id);
        }
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * 3.0数据分析 成交客户分析-车主类型占比
     */
    public JSONObject carOwner(String cycle_type, String month, String car_type) throws Exception {
        String url = "/porsche/analysis2/deal/car-owner";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!car_type.equals("")) {
            json.put("car_type", car_type);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * 3.0数据分析 成交客户分析-车主年龄/性别占比
     */
    public JSONObject genderAge(String cycle_type, String month, String car_type) throws Exception {
        String url = "/porsche/analysis2/deal/gender-age";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!car_type.equals("")) {
            json.put("car_type", car_type);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * 3.0数据分析 成交客户分析-全国成交量
     */
    public JSONObject wholeCountry(String cycle_type, String month, String car_type) throws Exception {
        String url = "/porsche/analysis2/deal/whole-country";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!car_type.equals("")) {
            json.put("car_type", car_type);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * 3.0数据分析 成交客户分析-市成交量
     */
    public JSONObject city(String cycle_type, String month, String car_type) throws Exception {
        String url = "/porsche/analysis2/deal/city";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        if (!month.equals("")) {
            json.put("month", month);
        }
        if (!car_type.equals("")) {
            json.put("car_type", car_type);
        }
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject city(String cycle_type, String month, String car_type, int adcode) {
        String url = "/porsche/analysis2/deal/city";
        JSONObject json = new JSONObject();
        json.put("cycle_type", cycle_type);
        json.put("adcode", adcode);
        if (!StringUtils.isEmpty(month)) {
            json.put("month", month);
        }
        if (!StringUtils.isEmpty(car_type)) {
            json.put("car_type", car_type);
        }
        String result = httpPostWithCheckCode(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    @DataProvider(name = "APPOINTMENT_TYPE")
    public static Object[] appointment_type() {
        return new String[]{
                "TEST_DRIVE",
                "MAINTAIN",
                "REPAIR",
                "ACTIVITY"  //TODO:预约消息类型待确认
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
                16,
                23,

        };
    }

    @DataProvider(name = "SELECT_DATE")
    public static Object[] select_date() {
        return new String[]{
                dt.getHistoryDate(0),
                dt.getHistoryDate(-1),
                dt.getHistoryDate(-2),
                dt.getHistoryDate(1)
        };
    }

    //文章投放位置
    @DataProvider(name = "APOSITIONS")
    public static Object[] apositions() {

        return new String[]{
                "ACTIVITY_1",
                "ACTIVITY_2",
                "ACTIVITY_3"
        };
    }

    //小程序token
    @DataProvider(name = "APPLET_TOKENS")
    public static Object[] appletTokens() {
        return new String[]{
                EnumAppletCode.XMF.getCode(),
//                EnumAppletCode.BB.getCode(),
//                EnumAppletCode.WM.getCode(),
//                EnumAppletCode.LXQ.getCode(),
//                EnumAppletCode.GLY.getCode()
        };
    }


    @DataProvider(name = "car")
    public static Object[] car() {
        return new String[]{
//                "吉A000001"
                //"吉A000002"
                //"吉A000003"
                //"吉A000004"// 客户4 没人脸 0805xsgw
//                "吉C000005",
//                "吉C000006",
//                "吉C000007",
//                "吉C000008",
//                "吉C000009",
//                "吉C000000",
                // "苏B123456"
                //"陕A123456" //0805xsgw 杨航
//                "吉E000000",
//                "吉E000001",
//                "吉E000002",
//                "吉E000003",
//                "吉E000004",
//                "吉E000005",
//                "吉E000006",
//                "吉E000007",
//                "吉E000008",
//                "吉E000009",
//                "吉E000010"
                "吉Q6465Q"

        };
    }

    //方法封装
//    //前台点击创建接待按钮创建顾客
//    public JSONObject creatCust() throws Exception {
//        JSONObject object = new JSONObject();
//        //前台登陆
//        login(cstm.qt, cstm.pwd);
//        Long customerid = -1L;
//        //获取当前空闲第一位销售id
//
//        String sale_id = freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
//        //
//        String userLoginName = "";
//        JSONArray userlist = userPage(1, 100).getJSONArray("list");
//        for (int i = 0; i < userlist.size(); i++) {
//            JSONObject obj = userlist.getJSONObject(i);
//            if (obj.getString("user_id").equals(sale_id)) {
//                userLoginName = obj.getString("user_login_name");
//            }
//        }
//        object.put("loginname", userLoginName);
//        //创建接待
//        creatReception("FIRST_VISIT");
//        //销售登陆，获取当前接待id
//        login(userLoginName, cstm.pwd);
//        customerid = userInfService().getLong("customer_id");
//        object.put("customerid", customerid);
//        //创建某级客户
//        String name = "zdh";
//        String phone = "zdh" + (int) ((Math.random() * 9 + 1) * 100000);
//        object.put("name", name);
//        object.put("phone", phone);
//
//        JSONObject customer = finishReception(customerid, 7, name, phone, "自动化---------创建----------H级客户");
//        return customer;
//    }

//    public JSONObject creatCust(String cName, String cPhone) throws Exception {
//        JSONObject object = new JSONObject();
//
//        //前台登陆
//        login(cstm.qt, cstm.pwd);
//        Long customerid = -1L;
//        //获取当前空闲第一位销售id
//        String sale_id = freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
//        String userLoginName = "";
//        JSONArray userlist = userPage(1, 100).getJSONArray("list");
//        for (int i = 0; i < userlist.size(); i++) {
//            JSONObject obj = userlist.getJSONObject(i);
//            if (obj.getString("user_id").equals(sale_id)) {
//                userLoginName = obj.getString("user_login_name");
//            }
//        }
//        object.put("loginname", userLoginName);
//        //创建接待
//        creatReception("FIRST_VISIT");
//        //销售登陆，获取当前接待id
//        login(userLoginName, cstm.pwd);
//        customerid = userInfService().getLong("customer_id");
//        object.put("customerid", customerid);
//        //创建某级客户
//        String name = cName;
//        String phone = cPhone;
//        object.put("name", name);
//        object.put("phone", phone);
//        JSONObject customer = finishReception(customerid, 3, name, phone, "自动化---------创建----------C级客户");
//        return customer;
//    }

    //新建试驾+审核封装
    public void creatDriver(Driver driver) throws Exception {  //1-通过，2-拒绝
        Long receptionId = 1L;    //接待记录id
        String idCard = "110226198210260078";
        String gender = "男";
        String signTime = dt.getHistoryDate(0);
        Long model = 1L;
        String country = "中国";
        String city = "图们";
        String email = dt.getHistoryDate(0) + "@qq.com";
        String address = "北京市昌平区";
        String ward_name = "小小";
        String driverLicensePhoto1Url = cstm.picurl;
        String driverLicensePhoto2Url = cstm.picurl;
        String electronicContractUrl = cstm.picurl;

        String call = "先生";
        int driverid = driveradd(receptionId, driver.customerId, driver.name, idCard, gender, driver.phone, "试乘试驾", model, country, city, email, address, ward_name, driverLicensePhoto1Url, driverLicensePhoto2Url, electronicContractUrl, driver.signDate, driver.signTime, call).getInteger("id");
        //销售总监登陆
        login(cstm.xszj, cstm.pwd);
        driverAudit(driverid, driver.auditStatus);
        //最后销售要再登陆一次

    }

    /**
     * 保时捷日常环境专用，与保时捷测试日常环境配置一致
     * 上传车牌号
     */
    public JSONObject carUploadToDaily(String router, String deviceId, String[] resource, String json) throws Exception {
        ApiResponse apiResponse = null;
        CommonConfig commonConfig = new CommonConfig();
        try {
            Credential credential = new Credential("77327ffc83b27f6d", "7624d1e6e190fbc381d0e9e18f03ab81");
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_7fc78d24")
                    .appId("097332a388c2")
                    .dataDeviceId(deviceId)
                    .requestId(requestId)
                    .version("1.0")
                    .dataResourceUnencryptedIdx(new Integer[]{0})
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            ApiClient apiClient = new ApiClient(commonConfig.gatewayDevice, credential);
            apiResponse = apiClient.doRequest(apiRequest);
            caseResult.setResponse(JSON.toJSONString(apiResponse));

            logger.printImportant(JSON.toJSONString(apiRequest));
            logger.printImportant(JSON.toJSONString(apiResponse));

            checkCode(commonConfig.gatewayDevice, apiResponse, router, StatusCode.SUCCESS);
        } catch (Exception e) {
            throw e;
        }
        return JSON.parseObject(JSON.toJSONString(apiResponse));
    }

    //接待列表导出

    public String receptionExport() throws Exception {
        String url = "/porsche/administration/reception/export?sale_type=PRE_SALES&name=&phone=";
        JSONObject json = new JSONObject();
        String result = httpGet(url, JSON.toJSONString(json), IpPort);
        return result;
    }

    /**
     * 汽车赢识线上环境专用
     * 上传车牌号
     */
    public JSONObject carUploadToOnline(String router, String deviceId, String[] resource, String json) throws Exception {
        ApiResponse apiResponse = null;
        CommonConfig commonConfig = new CommonConfig();
        try {
            Credential credential = new Credential("77327ffc83b27f6d", "7624d1e6e190fbc381d0e9e18f03ab81");
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_7fc78d24")
                    .appId("111112a388c2")
                    .dataDeviceId(deviceId)
                    .requestId(requestId)
                    .version("1.0")
                    .dataResourceUnencryptedIdx(new Integer[]{0})
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            ApiClient apiClient = new ApiClient(commonConfig.gatewayDeviceOnline, credential);
            apiResponse = apiClient.doRequest(apiRequest);
            caseResult.setResponse(JSON.toJSONString(apiResponse));

            logger.printImportant(JSON.toJSONString(apiRequest));
            logger.printImportant(JSON.toJSONString(apiResponse));
            checkCode(commonConfig.gatewayDevice, apiResponse, router, StatusCode.SUCCESS);
        } catch (Exception e) {
            throw e;
        }
        return JSON.parseObject(JSON.toJSONString(apiResponse));
    }

    public JSONObject receptionPage1(int page, String sale_type, int size) throws Exception {
        String url = "/porsche/administration/reception/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("sale_type", sale_type);
        json.put("size", size);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject receptionPage(String name, String phone, String saleType, int page, int size) {
        String url = "/porsche/administration/reception/page";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(name)) {
            object.put("name", name);
        }
        if (!StringUtils.isEmpty(page)) {
            object.put("phone", phone);
        }
        object.put("sale_type", saleType);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    public JSONObject modifyPasswordJk(String oldPassword, String newPassword) throws Exception {
        String url = "/porsche/app/user/modifyPassword";
        JSONObject json = new JSONObject();
        json.put("oldPassword", oldPassword);
        json.put("newPassword", newPassword);
        String result = httpPost(url, JSON.toJSONString(json), IpPort);
        return JSON.parseObject(result);

    }

    //crm4.0  车系
    public JSONObject carStyleList() {
        String url = "/WeChat-applet/porsche/car-style/list";
        String json = "{}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result);
    }

    /**
     * @description :车型
     * @date :2020/9/10 14:47
     **/
    public JSONObject carModelList(Long car_style) {
        String url = "/WeChat-applet/porsche/car-model/list";
        JSONObject json = new JSONObject();
        json.put("car_style", car_style);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //预约试驾取消
    public JSONObject appCanle(Long id) {
        String url = "/porsche/daily-work/test-drive/app/cancel";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //添加试驾车
    public JSONObject carManagementAdd(String car_name, Long car_style_id, Long car_model_id, String plate_number, String vehicle_chassis_code, Long service_time_start, Long service_time_end) {
        String url = "/porsche/test-drive-car/management/add";
        JSONObject json1 = new JSONObject();
        json1.put("car_name", car_name);
        json1.put("car_style_id", car_style_id);
        json1.put("car_model_id", car_model_id);
        json1.put("plate_number", plate_number);
        json1.put("vehicle_chassis_code", vehicle_chassis_code);
        json1.put("service_time_start", service_time_start);
        json1.put("service_time_end", service_time_end);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //添加试驾车不校验
    public JSONObject carManagementAddNotChk(String car_name, Long car_style_id, Long car_model_id, String plate_number, String vehicle_chassis_code, Long service_time_start, Long service_time_end) throws Exception {
        String url = "/porsche/test-drive-car/management/add";
        JSONObject json1 = new JSONObject();
        json1.put("car_name", car_name);
        json1.put("car_style_id", car_style_id);
        json1.put("car_model_id", car_model_id);
        json1.put("plate_number", plate_number);
        json1.put("vehicle_chassis_code", vehicle_chassis_code);
        json1.put("service_time_start", service_time_start);
        json1.put("service_time_end", service_time_end);
        String json = json1.toJSONString();
        String result = httpPost(url, json, IpPort);
        return JSON.parseObject(result);
    }

    //修改试驾车
    public JSONObject carManagementEdit(int test_car_id, String car_name, Long car_style_id, Long car_model_id, String plate_number, String vehicle_chassis_code, Long service_time_start, Long service_time_end) {
        String url = "/porsche/test-drive-car/management/edit";
        JSONObject json1 = new JSONObject();
        json1.put("test_car_id", test_car_id);
        json1.put("car_name", car_name);
        json1.put("car_style_id", car_style_id);
        json1.put("car_model_id", car_model_id);
        json1.put("plate_number", plate_number);
        json1.put("vehicle_chassis_code", vehicle_chassis_code);
        json1.put("service_time_start", service_time_start);
        json1.put("service_time_end", service_time_end);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //试驾车注销
    public JSONObject carLogout(Long id) {
        String url = "/porsche/test-drive-car/management/logout";
        JSONObject json = new JSONObject();
        json.put("test_car_id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //试驾车列表
    public JSONObject driverCarList() {
        String url = "/porsche/test-drive-car/management/list";
        String json = "{}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //crm4.0 -----------pc--------------------
    //新建车系
    public JSONObject addCarStyle(String car_style_name) {
        String url = "/porsche/car-style/add-new-car";
        JSONObject json = new JSONObject();
        json.put("car_style_name", car_style_name);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //禁用车系
    public JSONObject carStyleEffect(Long car_style_id, boolean take_effect) {
        String url = "/porsche/car-style/take-effect";
        JSONObject json = new JSONObject();
        json.put("car_style_id", car_style_id);
        json.put("take_effect", take_effect);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //修改车系
    public JSONObject editCar(Long car_style_id, String car_style_name) {
        String url = "/porsche/car-style/edit-car";
        JSONObject json = new JSONObject();
        json.put("car_style_id", car_style_id);
        json.put("car_style_name", car_style_name);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //车系列表
    public JSONObject carStyleList(int page, int size) {
        String url = "/porsche/car-style/car_style_list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //新建车型
    public JSONObject addCarmodel(long car_style_id, String car_model_name) {
        String url = "/porsche/car-model/add-new-car";
        JSONObject json = new JSONObject();
        json.put("car_style_id", car_style_id);
        json.put("car_model_name", car_model_name);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //禁用车型
    public JSONObject carmodelEffect(Long car_model_id, boolean take_effect) {
        String url = "/porsche/car-model/take-effect";
        JSONObject json = new JSONObject();
        json.put("car_model_id", car_model_id);
        json.put("take_effect", take_effect);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //修改车型
    public JSONObject editCarmodel(Long car_model_id, String car_model_name) {
        String url = "/porsche/car-model/edit-car";
        JSONObject json = new JSONObject();
        json.put("car_model_id", car_model_id);
        json.put("car_model_name", car_model_name);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //车型列表
    public JSONObject carmodelList(Long car_style_id, int page, int size) {
        String url = "/porsche/car-style/car_style_list";
        JSONObject json = new JSONObject();
        json.put("car_style_id", car_style_id);
        json.put("page", page);
        json.put("size", size);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //工作计划枚举
    public JSONObject taskEnum() {
        String url = "/porsche/work-plan/task-enum";
        JSONObject json = new JSONObject();
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    } //新建计划

    public JSONObject addplan(String task_enum_type, Long customer_id, String working_date, String working_time, String plate_number, String remark) {
        String url = "/porsche/work-plan/add";
        JSONObject json1 = new JSONObject();
        json1.put("task_enum_type", task_enum_type);
        json1.put("customer_id", customer_id);
        json1.put("working_date", working_date);
        json1.put("working_time", working_time);
        json1.put("plate_number", plate_number);
        json1.put("remark", remark);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //工作计划取消
    public JSONObject planCancle(Long work_plan_id) {
        String url = "/porsche/work-plan/cancel";
        JSONObject json = new JSONObject();
        json.put("work_plan_id", work_plan_id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //工作计划列表
    public JSONObject workPlanList() {
        String url = "/porsche/work-plan/list";
        JSONObject json = new JSONObject();
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //手机号搜索手机号
    public JSONObject searchByPhone(String customer_phone) {
        String url = "/porsche/app/customer/search-by-phone";
        JSONObject json = new JSONObject();
        json.put("customer_phone", customer_phone);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //DCC创建线索
    public JSONObject dccCreate(String customer_name, String customer_phone, String plate_number) {
        String url = "/porsche/app/customer/dcc-create";
        JSONObject json = new JSONObject();
        json.put("customer_name", customer_name);
        json.put("customer_phone", customer_phone);
        if (!plate_number.equals("")) {
            json.put("plate_number", plate_number);
        }
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //创建线索不校验
    public JSONObject dccCreateNotChk(String customer_name, String customer_phone, String plate_number) throws Exception {
        String url = "/porsche/app/customer/dcc-create";
        JSONObject json = new JSONObject();
        json.put("customer_name", customer_name);
        json.put("customer_phone", customer_phone);
        if (!plate_number.equals("")) {
            json.put("plate_number", plate_number);
        }
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }

    //DCC创建线索
    public JSONObject dcclist(String customer_phone, String customer_name, String start_time, String end_time, int page, int size) {
        String url = "/porsche/app/customer/dcc-list";
        JSONObject json1 = new JSONObject();
        json1.put("customer_phone", customer_phone);
        json1.put("customer_name", customer_name);
        json1.put("start_time", start_time);
        json1.put("end_time", end_time);
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject dcclist(int page, int size) {
        String url = "/porsche/app/customer/dcc-list";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    public JSONObject driverTimelist(Long car_model) {
        String url = "/porsche/daily-work/test-drive/app/test-drive-time-list";
        JSONObject json1 = new JSONObject();
        json1.put("car_model", car_model);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //配置收件箱校验返回值
    public JSONObject mailConfig(String email) {
        String url = "/porsche/daily-work/test-drive/app/mail-config";
        JSONObject json1 = new JSONObject();
        json1.put("email", email);

        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    //配置收件箱不校验返回值
    public JSONObject mailConfigNotChk(String email) throws Exception {
        String url = "/porsche/daily-work/test-drive/app/mail-config";
        JSONObject json1 = new JSONObject();
        json1.put("email", email);
        String json = json1.toJSONString();
        String result = httpPost(url, json, IpPort);
        return JSON.parseObject(result);
    }

    //购车
    public JSONObject addOrderCar(String customer_id, String reception_id, String vehicle_chassis_code) throws Exception {
        String url = "/porsche/app/customer/add-order-car";
        JSONObject json1 = new JSONObject();
        json1.put("car_model_id", 36);
        json1.put("car_style_id", 1);
        json1.put("customer_id", customer_id);
        json1.put("defray_type", 1);
        json1.put("pay_type", 1);
        json1.put("plate_type", 0);
        json1.put("reception_id", reception_id);
        json1.put("vehicle_chassis_code", vehicle_chassis_code);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //购车
    public JSONObject addOrderCar1(orderCar oc) throws Exception {
        String url = "/porsche/app/customer/add-order-car";
        JSONObject json1 = new JSONObject();
        json1.put("car_model_id", oc.car_model_id);
        json1.put("car_style_id", oc.car_style_id);
        json1.put("customer_id", oc.customer_id);
        json1.put("defray_type", oc.defray_type);
        json1.put("pay_type", oc.pay_type);
        json1.put("plate_type", oc.plate_type);
        json1.put("reception_id", oc.receptionId);
        json1.put("vehicle_chassis_code", oc.vehicle_chassis_code);
        String json = json1.toJSONString();
        JSONObject result;
        String res = "";
        if (oc.checkCode) {
            res = httpPostWithCheckCode(url, json, IpPort);
            result = JSON.parseObject(res).getJSONObject("data");
        } else {
            res = httpPost(url, json, IpPort);
            result = JSON.parseObject(res);
        }
        return result;
    }

    //查看收件箱已配置邮箱
    public JSONObject mailDetail() {
        String url = "/porsche/daily-work/test-drive/app/mail-detail";
        String json = "{}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //获取购车车id
    public JSONObject customerOrderCar(String customer_id) {
        String url = "/porsche/daily-work/deliver-car/app/customer-order-car";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", customer_id);
        String json = json1.toJSONString();
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //预约·试驾车列表
    public JSONObject testDriverList() {
        String url = "/porsche/test-drive-car/management/drop-down-list";
        String json = "{}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    @DataProvider(name = "ADD_CAR")
    public static Object[][] add_car() {

        return new String[][]{
                {"ZDH" + (int) ((Math.random() * 9 + 1) * 10), "苏ZDH" + (int) ((Math.random() * 9 + 1) * 100), "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000)}, //名字5位，车牌号7位，车架号17位
                {"ZDH20WEIAAAAA" + (int) ((Math.random() * 9 + 1) * 1000000), "苏ZDH" + (int) ((Math.random() * 9 + 1) * 1000), "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000)}, //名字5位，车牌号8位，车架号17位


        };
    }

    @DataProvider(name = "EMAIL")
    public static Object[] email() {
        return new String[]{
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789011@163.com",
                "1@qq.com",
                "12345Lxsd67890@gmail.com",
                "1234567890@baiyahoo.com",
                "wertyui@baimsn.com",
                "WERTY@hotmail.com",
                "_____@aol.com",
                "KJHGFYTU@ask.com",
                "12KKJ567890@live.com",
                "123OOO000@0355.net",
                "1234567890@163.net",
                "1234567890@263.net",
                "1234567890@3721.net",
                "2842726905@qq.com",
        };
    }

    @DataProvider(name = "EMAILERR")
    public static Object[] emailerr() {
        return new String[]{
                "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890111@163.com", //101位
                "汉字@qq.com", //汉字
                "gmail.com", //没@
                "1234567890", //纯数字
                "wertyui", //纯英文
                "WERTY@hotmail。com", //点为中文
                "～！@#¥%……&*（@qq.com", //标点符号
                "KJHGFYTU@ask—com",
                "12KKJ567890@live_com",
        };
    }

    @DataProvider(name = "DCCCREAT")
    public static Object[][] dcc_creat() {
        return new String[][]{
                {"ZDH", "139000" + (int) ((Math.random() * 9 + 1) * 10000), ""}, //名字3位，手机号，不填车牌号
                {"姓名50位姓名50位姓名50位姓名50位姓名50位姓名50位姓名50位姓名50位姓名50位" + (int) ((Math.random() * 9 + 1) * 10000), "139000" + (int) ((Math.random() * 9 + 1) * 10000), ""}, //名字50位，手机号，不填车牌号
                {"!@#$%^&*()}{:?><~!", "139000" + (int) ((Math.random() * 9 + 1) * 10000), "苏ZDH" + (int) ((Math.random() * 9 + 1) * 100)}, //名字符号，手机号，车牌号7位
                {"啊", "139000" + (int) ((Math.random() * 9 + 1) * 10000), "苏ZDH" + (int) ((Math.random() * 9 + 1) * 1000)}, //名字1位，手机号，车牌号8位


        };
    }

    @DataProvider(name = "PLATE")
    public static Object[] plate() {
        return new String[]{
                "苏BJ123",   //6位
                "BJ12345",    //不含汉字
                "京1234567",  //不含英文
                "京bj12345", //含小写
                "京B@12345", //含字母
                "苏BJ123456",//9位
        };
    }

    //文章投放位置
    @DataProvider(name = "NUM")
    public static Object[] num() {
        return new Integer[]{
                1,
                3,
        };
    }

    @DataProvider(name = "PHONE")
    public static Object[] phone() {
        return new String[]{
                "150372860133", "1503728601a", "150372860哈", "150372860$@", "11111111111"
        };
    }

    //前台展厅接待查询
    public JSONObject qtreceptionPage(String customerNamePhone, String startTime, String endTime, String page, String size) {
        String url = "/porsche/app/sale-reception/reception-page";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(customerNamePhone)) {
            object.put("customer_name_phone", customerNamePhone);
        }
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_time", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_time", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //app 到访记录查询
    public JSONObject visitList(String startTime, String endTime, String page, String size) {
        String url = "/porsche/app/sale-reception/visit-list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startTime)) {
            object.put("start_day", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("end_day", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //app 非客查询
    public JSONArray nonCustomerList(String startTime, String endTime, String page, String size) {
        String url = "/porsche/app/customer/non_customer_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startTime)) {
            object.put("search_date_start", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("search_date_end", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        String result = httpPostWithCheckCode(url, object.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONArray("data");
    }

    //app 未接待离店查询
    public JSONArray nonReceptionList(String startTime, String endTime, String page, String size) {
        String url = "/porsche/app/customer/non_reception_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(startTime)) {
            object.put("search_date_start", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            object.put("search_date_end", endTime);
        }
        object.put("page", page);
        object.put("size", size);
        String result = httpPostWithCheckCode(url, object.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONArray("data");
    }

    //app 变更接待
    public JSONArray changeReceptionSale(String reception_id, String sale_id) {
        String url = "/porsche/app/sale-reception/change-reception-sale";
        JSONObject object = new JSONObject();
        object.put("reception_id", reception_id);
        object.put("sale_id", sale_id);
        String result = httpPostWithCheckCode(url, object.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONArray("data");
    }

    public JSONArray nonReceptionList(selectTest ss) {
        String url = "/porsche/app/customer/non_reception_list";
        JSONObject object = new JSONObject();
        if (!StringUtils.isEmpty(ss.startTime)) {
            object.put("search_date_start", ss.startTime);
        }
        if (!StringUtils.isEmpty(ss.endTime)) {
            object.put("search_date_end", ss.endTime);
        }
        object.put("page", ss.page);
        object.put("size", ss.size);
        String result = httpPostWithCheckCode(url, object.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONArray("data");
    }

    public JSONObject finishReception3(finishReceive pm) throws Exception {
        String url = "/porsche/app/customer/finishReception";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", pm.customer_id);
        json1.put("reception_id", pm.reception_id);
        json1.put("belongs_sale_id", pm.belongs_sale_id);
        json1.put("reception_type", pm.reception_type);
        json1.put("name", pm.name);
        json1.put("subjectType", pm.subjectType);
        json1.put("call", pm.call);
        json1.put("districtCode", pm.districtCode);
        json1.put("address", pm.address);
        json1.put("birthday", pm.birthday);
        json1.put("id_number", pm.id_number);
        json1.put("plate_number_one_id", pm.plate_number_one_id);
        json1.put("plate_number_one", pm.plate_number_one);
        json1.put("plate_number_two", pm.plate_number_two);
        json1.put("intention_car_model", pm.intention_car_model);
        json1.put("expected_buy_day", pm.expected_buy_day);
        json1.put("test_drive_car_model", pm.test_drive_car_model);
        json1.put("pay_type", pm.pay_type);
        json1.put("assess_car_model", pm.assess_car_model);
        json1.put("source_channel", pm.source_channel);
        json1.put("compare_car_model", pm.compare_car_model);
        json1.put("own_car_model", pm.own_car_model);
        json1.put("visit_count_type", pm.visit_count_type);
        json1.put("is_offer", pm.is_offer);
        json1.put("buy_car_type", pm.buy_car_type);
        json1.put("power_type", pm.power_type);
        json1.put("is_assessed", pm.is_assessed);
        json1.put("phoneList", pm.phoneList);
        json1.put("remark", pm.remark);
        json1.put("face_list", pm.face_list);
        String json = json1.toJSONString();

        String res = "";
        JSONObject result;
        if (pm.checkCode) {
            res = httpPostWithCheckCode(url, json, IpPort);
            result = JSON.parseObject(res).getJSONObject("data");
        } else {
            res = httpPost(url, json, IpPort);
            result = JSON.parseObject(res);
        }
        return result;
    }

    public JSONObject editCustomer(finishReceive pm) throws Exception {
        String url = "/porsche/app/customer/edit";
        JSONObject json1 = new JSONObject();
        json1.put("customer_id", pm.customer_id);
        json1.put("reception_id", pm.reception_id);
        json1.put("belongs_sale_id", pm.belongs_sale_id);
        json1.put("reception_type", pm.reception_type);
        json1.put("name", pm.name);
        json1.put("subjectType", pm.subjectType);
        json1.put("call", pm.call);
        json1.put("districtCode", pm.districtCode);
        json1.put("address", pm.address);
        json1.put("birthday", pm.birthday);
        json1.put("id_number", pm.id_number);
        json1.put("plate_number_one_id", pm.plate_number_one_id);
        json1.put("plate_number_one", pm.plate_number_one);
        json1.put("plate_number_two", pm.plate_number_two);
        json1.put("intention_car_model", pm.intention_car_model);
        json1.put("expected_buy_day", pm.expected_buy_day);
        json1.put("test_drive_car_model", pm.test_drive_car_model);
        json1.put("pay_type", pm.pay_type);
        json1.put("assess_car_model", pm.assess_car_model);
        json1.put("source_channel", pm.source_channel);
        json1.put("compare_car_model", pm.compare_car_model);
        json1.put("own_car_model", pm.own_car_model);
        json1.put("visit_count_type", pm.visit_count_type);
        json1.put("is_offer", pm.is_offer);
        json1.put("buy_car_type", pm.buy_car_type);
        json1.put("power_type", pm.power_type);
        json1.put("is_assessed", pm.is_assessed);
        json1.put("phoneList", pm.phoneList);
        json1.put("remark", pm.remark);
        json1.put("face_list", pm.face_list);
        String json = json1.toJSONString();

        String res = "";
        JSONObject result;
        if (pm.checkCode) {
            res = httpPostWithCheckCode(url, json, IpPort);
            result = JSON.parseObject(res).getJSONObject("data");
        } else {
            res = httpPost(url, json, IpPort);
            result = JSON.parseObject(res);
        }
        return result;
    }

    //applet 编辑车
    public JSONObject appletEditCar(Long car_id, Integer car_type, String plate_number, Integer car_model) throws Exception {
        String url = "/WeChat-applet/porsche/a/my-car/edit";
        JSONObject json = new JSONObject();
        json.put("my_car_id", car_id);
        json.put("car_style", car_type);
        json.put("car_model", car_model);
        json.put("plate_number", plate_number);
        json.put("province", "");

        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }

    //前台客户列表
    public JSONObject markcustomerList() throws Exception {
        String url = "/porsche/app/sale-reception/customerList";
        JSONObject json = new JSONObject();
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //前台标记非客
    public JSONObject markNocustomer(JSONArray list) throws Exception {
        String url = "/porsche/app/customer/nonGuest";

        JSONObject json = new JSONObject();
        json.put("list", list);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //删除前台标记非客
    public JSONObject deleteNocustomer(String analysis_customer_id) throws Exception {
        String url = "/porsche/app/customer/risk_delete";
        JSONObject json = new JSONObject();
        json.put("analysis_customer_id", analysis_customer_id);

        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //前台标记为接待离店
    public JSONObject marknonReception(JSONArray list) throws Exception {
        String url = "/porsche/app/customer/nonReception";

        JSONObject json = new JSONObject();
        json.put("list", list);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //删除前台标记为接待离店
    public JSONObject deleteNoReception(String analysis_customer_id) throws Exception {
        String url = "/porsche/app/customer/risk_delete";
        JSONObject json = new JSONObject();
        json.put("analysis_customer_id", analysis_customer_id);

        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //我的接待编辑客户-车牌号一列表
    public JSONObject platepic() throws Exception {
        String url = "/porsche/app/customer/plate-number-list-pic";
        JSONObject json = new JSONObject();

        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }
}







