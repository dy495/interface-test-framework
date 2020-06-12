package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Crm {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/crm-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public String adminName = "baoshijie";
    public String adminPasswd = "e10adc3949ba59abbe56e057f20f883e";

    public String managerName = "";
    public String managerPasswd = "";

//    public String majordomoName = "hand_off_majordomo";
//    public String majordomoPasswd = "e10adc3949ba59abbe56e057f20f883e";

    public String majordomoName = "majordomo";
    public String majordomoPasswd = "e10adc3949ba59abbe56e057f20f883e";

    public String frontDeskName = "";
    public String frontDeskPasswd = "";

    public String salesPersonName = "s6";
    public String salesPersonPasswd = "e10adc3949ba59abbe56e057f20f883e";

    public HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();

    String cycle7 = "RECENT_SEVEN";
    String cycle30 = "RECENT_THIRTY";
    String cycle60 = "RECENT_SIXTY";
    String cycle90 = "RECENT_NINETY";

//    ##########################################接口封装#########################################################################

    public String genPorscheJson(String cycleType, String month) {

        String json = "";

        if (!"".equals(cycleType)) {
            json =
                    "{\"cycle_type\":\"" + cycleType + "\"}";
        }

        if (!"".equals(month)) {
            json =
                    "{\"month\":\"" + month + "\"}";
        }

        return json;

    }

//    ***************************************** 1.1 日期相关**********************************************************************

    /**
     * @description: 1.1.1 查询周期列表
     * @author: liao
     * @time:
     */
    public JSONObject queryCycleList() throws Exception {
        String url = "/porsche/query-cycle/list";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    **************************************************二、历史数据统计**********************************************************************

    /**
     * @description: 2.1.1 门店历史客流统计
     * @author: liao
     * @time:
     */
    public JSONObject overviewCycleS(String cycleType) throws Exception {
        String url = "/porsche/history/shop/overview";

        String json =
                "{\"cycle_type\":\"" + cycleType + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.1 门店历史客流统计
     * @author: liao
     * @time:
     */
    public JSONObject overviewSMonth(String month) throws Exception {
        String url = "/porsche/history/shop/overview";

        String json = "{\"month\":\"" + month + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.2 历史互动人数趋势
     * @author: liao
     * @time:
     */
    public JSONObject arriveTrendCycleS(String cycleType, String dimension) throws Exception {
        String url = "/porsche/history/shop/arrive-trend";

        String json = "{";

        if (!"".equals(cycleType)) {
            json += "    \"cycle_type\":\"" + cycleType + "\",";
        }

        json += "    \"dimension\":\"" + dimension + "\"\n" +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject arriveTrendMonthS(String month, String dimension) throws Exception {
        String url = "/porsche/history/shop/arrive-trend";

        String json = "{";

        if (!"".equals(month)) {
            json += "    \"month\":\"" + month + "\",\n";
        }

        json += "    \"dimension\":\"" + dimension + "\"\n" +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.3 全场客流年龄/性别分布
     * @author: liao
     * @time:
     */
    public JSONObject ageGenderDistS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/age-gender/distribution";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.4 历史客流数据
     * @author: liao
     * @time:
     */
    public JSONObject visitDataCycleS(String cycleType) throws Exception {
        String url = "/porsche/history/shop/visit-data";

        String json = "{\"cycle_type\":\"" + cycleType + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject visitDataMonthS(String month) throws Exception {
        String url = "/porsche/history/shop/visit-data";

        String json = "{\"month\":\"" + month + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.5 历史到店时段客流分布
     * @author: liao
     * @time:
     */
    public JSONObject hourDataCycleS(String cycleType) throws Exception {
        String url = "/porsche/history/shop/hour-data";

        String json = "{\"cycle_type\":\"" + cycleType + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject hourDataMonthS(String month) throws Exception {
        String url = "/porsche/history/shop/hour-data";

        String json = "{\"month\":\"" + month + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.6 历史到店伴随分布
     * @author: liao
     * @time:
     */
    public JSONObject accompanyCycleS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/accompany";

        String json = "{\"cycle_type\":\"" + cycleType + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject accompanyMonthS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/accompany";

        String json = "{\"month\":\"" + month + "\"}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ******************************************三、区域商品相关接口**************************************************************

    /**
     * @description: 3.1 区域到访数据
     * @author: liao
     * @time:
     */
    public JSONObject visitDataR(String cycleType, String month) throws Exception {
        String url = "/porsche/history/region/visit-data";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ********************************************四、商品相关接口************************************************************************

    /**
     * @description: 4.1 商品排行
     * @author: liao
     * @time:
     */
    public JSONObject skuRank(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sku/rank";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    *********************************************五、销售数据************************************************************************

    /**
     * @description: 5.1 总销售额
     * @author: liao
     * @time:
     */
    public JSONObject salesTotal(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/total";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.2 进店客流
     * @author: liao
     * @time:
     */
    public JSONObject salesVisitFlow(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/visit-flow";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.3 订单成交量
     * @author: liao
     * @time:
     */
    public JSONObject salesDeal(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/deal";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.4 销售额趋势
     * @author: liao
     * @time:
     */
    public JSONObject salesTrend(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/trend";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.5 销售员排行
     * @author: liao
     * @time:
     */
    public JSONObject salesStaffRank(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/staff-rank";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.6 销售额车型占比
     * @author: liao
     * @time:
     */
    public JSONObject salesRatio(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/sales-ratio";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.7 订单量车型占比
     * @author: liao
     * @time:
     */
    public JSONObject dealRatio(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/deal-ratio";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.8 销售渠道分析
     * @author: liao
     * @time:
     */
    public JSONObject channels(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/channels";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.9 销售转化率分析
     * @author: liao
     * @time:
     */
    public JSONObject channelConversionRates(String cycleType, String month) throws Exception {
        String url = "/porsche/history/sales/channel-conversion-rates";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ******************************************用户管理*************************************************************

    /**
     * @description: 1.1 角色列表
     * @author: liao
     * @time:
     */
    public JSONObject roleList() throws Exception {
        String url = "/porsche/user/roleList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.2 账号分页列表
     * @author: liao
     * @time:
     */
    public JSONObject userPage(int page, int size) throws Exception {
        String url = "/porsche/user/userPage";

        String json =
                "{\n" +
                        "  \"page\":\"" + page + "\",\n" +
                        "  \"size\":" + size +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3 创建账号
     * @author: liao
     * @time:
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

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.6.1 所有销售人员-下拉列表
     * @author: liao
     * @time:
     */
    public JSONObject allSaleList(String userName, String userLoginName, String phone, int roleId) throws Exception {
        String url = "/porsche/reception/allSaleList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.7 销售状态列表
     * @author: liao
     * @time:
     */
    public JSONObject statusList(String userName, String userLoginName, String phone, int roleId) throws Exception {
        String url = "/porsche/user/statusList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.8 删除账号
     * @author: liao
     * @time:
     */
    public JSONObject deleteUser(String userId) throws Exception {
        String url = "/porsche/user/delete";

        String json =
                "{\n" +
                        "    \"user_id\":\"" + userId + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ***********************************************前台工作****************************************************************

    /**
     * @description: 1.1 今日接待顾客列表
     * @author: liao
     * @time:
     */
    public JSONObject customerTodayList() throws Exception {
        String url = "/porsche/reception/customerTodayList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.2 当日排班销售人员列表
     * @author: liao
     * @time:
     */
    public JSONObject freeSaleUserList() throws Exception {
        String url = "/porsche/reception/freeSaleUserList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.4.1 分配销售人员-下拉列表
     * @author: liao
     * @time:
     */
    public JSONObject freeSaleList(String userId) throws Exception {
        String url = "/porsche/reception/freeSaleList";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.4.2 分配销售人员
     * @author: liao
     * @time:
     */
    public JSONObject distributionUser(String id, String saleId, String leaveTime, int userStatus) throws Exception {
        String url = "/porsche/reception/distributionUser";

        String json =
                "{\n" +
                        "   \"id\":\"" + id + "\",\n" +
                        "   \"sale_id\":\"" + saleId + "\",\n" +
                        "   \"leave_time_str\":\"" + leaveTime + "\",\n" +
                        "   \"user_status\":\"" + userStatus +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ************************************************六、客户管理*************************************************************

    /**
     * @description: 6.1 查询客户详细信息
     * @author: liao
     * @time:
     */
    public JSONObject customerDetailPC(String id) throws Exception {
        String url = "/porsche/customer/detail";

        String json =
                "{\n" +
                        "   \"customer_id\":\"" + id + "\"," +
                        "   \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.2 修改客户信息
     * @author: liao
     * @time:
     */
    public JSONObject customerEditPC(String customerId, int customerLevel, int customerSelectType, String customerName, String customerPhone,
                                     int visitCount, int belongsArea, String alreadyCar, int testDriveCar, int sehandAssess,
                                     int carAssess, String preBuyTime, int likeCar, String compareCar, int showPrice,
                                     int payType, int buyCar, int buyCarType, int buyCarAttribute, String reamrks, String comment,
                                     String nextReturnVisitDate) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{\n" +
                        "    \"customer_id\" :" + customerId + ",\n" +
                        "    \"shop_id\" :" + getShopId() + ",\n" +
                        "    \"customer_level\" :" + customerLevel + ",\n" + //客户级别(0-H,1-A,2-B,3-C,4-F)
                        "    \"customer_select_type\" :" + customerSelectType + ",\n" +//销售员所选客户类型(0-老客户重购,1-自然到访,2-亲友推荐,3-线上推广，4-官方推广)
                        "    \"customer_name\" : \"" + customerName + "\",\n" +
                        "    \"customer_phone\" : \"" + customerPhone + "\",\n" +
                        "    \"visit_count\" :" + visitCount + ",\n" + //到店人数(0-1人,1-2人,2-3人以上)
                        "    \"belongs_area\" :" + belongsArea + ",\n" + //所属区域(0-本地,1-南通,2-常州,3-其他)
                        "    \"already_car\" : \"" + alreadyCar + "\",\n" +
                        "    \"test_drive_car\" :" + testDriveCar + ",\n" + //试驾车型(0-TAYCAN,1-718,2-911,3-PANAMERA,4-MACAN,5-CAYENNE)
                        "    \"sehand_assess\" :" + sehandAssess + ",\n" + //二手车评估 (0:是,1:否)
                        "    \"car_assess\" : \"" + carAssess + "\",\n" +
                        "    \"pre_buy_time\" : \"" + preBuyTime + "\",\n" +
                        "    \"like_car\" :" + likeCar + ",\n" + //意向车型（0-TAYCAN,1-718,2-911,3-PANAMERA,4-MACAN,5-CAYENNE）
                        "    \"compare_car\" : \"" + compareCar + "\",\n" +
                        "    \"show_price\" :" + showPrice + ",\n" +//是否报价(0:是,1:否)
                        "    \"pay_type\" :" + payType + ",\n" +  //付款方式(0:全款,1:贷款)
                        "    \"buy_car\" :" + buyCar + ",\n" + //是否订车(0:是,1:否)
                        "    \"buy_car_attribute\" :" + buyCarAttribute + ",\n" +
                        "    \"buy_car_type\" :" + buyCarType + ",\n" +
                        "    \"reamrks\" : [\n" +
                        reamrks +
                        "    ],\n" +
                        "    \"return_visits\" : [{\n" +
                        "        \"comment\" : \"" + comment + "\",\n" +
                        "        \"next_return_visit_date\" : \"" + nextReturnVisitDate + "\"\n" +
                        "    }]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.3 查看客户信息列表
     * @author: liao
     * @time:
     */
    public JSONObject customerListPC(String id, int customerLevel, String customerName, String customerPhone,
                                     long startTime, long endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/list";

        String json =
                "{\n" +
                        "   \"belongs_sale_id\" : \"" + id + "\",\n" +
                        "   \"shop_id\" :" + getShopId() + ",\n";

        if ("".equals(customerPhone)) {
            json += "   \"customer_phone\" : \"" + customerPhone + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\" :" + customerLevel + ",\n";
        }

        if ("".equals(customerName)) {
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

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerListPC(String startTime, String endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/list";

        String json =
                "{\n" +
                        "   \"start_time\" :\"" + startTime + "\",\n" +
                        "   \"end_time\" :\"" + endTime + "\",\n" +
                        "   \"page\" :" + page + ",\n" +
                        "   \"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.4 删除客户
     * @author: liao
     * @time:
     */
    public JSONObject customerDeletePC(long id) throws Exception {
        String url = "/porsche/customer/delete";

        String json =
                "{\n" +
                        "   \"consumer_id\" : \"" + id + "\",\n" +
                        "   \"shop_id\" :" + getShopId() +
                        "} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.5 今日来访
     * @author: liao
     * @time:
     */
    public JSONObject todayListPC(String id, int customerLevel, String customerName, String customerPhone,
                                  long startTime, long endTime, int page, int size) throws Exception {
        String url = "/porsche/customer/today-list";

        String json =
                "{\n" +
                        "   \"belongs_sale_id\" : \"" + id + "\",\n" +
                        "   \"shop_id\" :" + getShopId() + ",\n";

        if ("".equals(customerPhone)) {
            json += "   \"customer_phone\" : \"" + customerPhone + "\",\n";
        }

        if (-1 != customerLevel) {
            json += "   \"customer_level\" :" + customerLevel + ",\n";
        }

        if ("".equals(customerName)) {
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

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.6 客户详情使用枚举值
     * @author: liao
     * @time:
     */
    public JSONObject customerInfoEnumPC() throws Exception {
        String url = "/porsche/customer/customer_info_enum";

        String json =
                "{\n" +
                        "   \"shop_id\" :" + getShopId() + "} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.7 删除今日来访
     * @author: liao
     * @time:
     */
    public JSONObject deleteTodayVisitPC(long id) throws Exception {
        String url = "/porsche/customer/delete_today_visit";

        String json =
                "{\n" +
                        "   \"customer_id\" :" + id + "} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    **********************************************八、回访任务分配模块接口**********************************************************************

    /**
     * @description: 8.1 新增回访任务
     * @author: liao
     * @time:
     */
    public JSONObject addTask(long customerId, String inChargeSaleId, long returnVisitDate, long remindTime) throws Exception {
        String url = "/porsche/return-visit/task/add";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"in_charge_sale_id\":\"" + inChargeSaleId + "\",\n" +
                        "    \"return_visit_date\":\"" + returnVisitDate + "\",\n" +
                        "    \"remind_time\":\"" + remindTime + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 8.2 更新回访任务
     * @author: liao
     * @time:
     */
    public JSONObject updateTask(long id, long customerId, String inChargeSaleId, long returnVisitDate, long remindTime) throws Exception {
        String url = "/porsche/return-visit/task/update";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"in_charge_sale_id\":\"" + inChargeSaleId + "\",\n" +
                        "    \"return_visit_date\":\"" + returnVisitDate + "\",\n" +
                        "    \"remind_time\":\"" + remindTime + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 8.3 根据id删除回访任务
     * @author: liao
     * @time:
     */
    public JSONObject deleteTaskById(long id) throws Exception {
        String url = "/porsche/return-visit/task/deleteById";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 8.4 根据销售人员userId删除回访任务
     * @author: liao
     * @time:
     */
    public JSONObject deleteTaskBySaleId(long saleId) throws Exception {
        String url = "/porsche/return-visit/task/deleteBySaleId";

        String json =
                "{\n" +
                        "    \"sale_id\":\"" + saleId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 8.5 获得回访任务列表
     * @author: liao
     * @time:
     */
    public JSONObject taskList(long saleId, String returnVisitDate, int status, int page, int size) throws Exception {
        String url = "/porsche/return-visit/task/list";

        String json =
                "{\n" +
                        "    \"user_id\":" + saleId + "," +
                        "    \"return_visit_date\":\"" + returnVisitDate + "\"," +
                        "    \"status\":" + status + "," +
                        "    \"page\":" + page + "," +
                        "    \"size\":" + size +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 8.6 获得回访任务详情
     * @author: liao
     * @time:
     */
    public JSONObject taskDetail(long id) throws Exception {
        String url = "/porsche/return-visit/task/detail";

        String json =
                "{\n" +
                        "    \"id\":" + id +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ******************************************九、推送服务******************************************************************

    /**
     * @description: 9.1  推送通知
     * @author: liao
     * @time:
     */
    public JSONObject pushNotification(long userId, long pushTime, long expireTime, boolean storeOffline, String title, String payload) throws Exception {
        String url = "/porsche/push-service/push-notification";

        String json =
                "{\n" +
                        "    \"user_id\":" + userId + ",\n" +
                        "    \"push_time\":" + pushTime + ",\n" +
                        "    \"expire_time\":" + expireTime + ",\n" +
                        "    \"store_offline\":" + storeOffline + ",\n" +
                        "    \"title\":" + title + ",\n" +
                        "    \"payload\":\"{" + payload + "}\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    *************************************************十、销售日常工作相关**********************************************************************

//    -----------------------------------------------------10.1 工作安排-------------------------------------------------------

    /**
     * @description: 10.1.1-WEB 获得工作安排列表
     * @author: liao
     * @time:
     */
    public JSONObject scheduleListPC(String date, int status, int page, int size) throws Exception {
        String url = "/porsche/daily-work/schedule/list";

        String json =
                "{\n" +
                        "    \"date\":\"" + date + "\",\n" +
                        "    \"status\":\"" + status + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.1.1-APP 获得工作安排列表(APP端接口)
     * @author: liao
     * @time:
     */
    public JSONObject scheduleListAPP(String date, int status, int page, int size) throws Exception {
        String url = "/porsche/daily-work/schedule/app/list";

        String json =
                "{\n" +
                        "    \"date\":\"" + date + "\",\n" +
                        "    \"status\":\"" + status + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.1.2-WEB 添加新的工作安排
     * @author: liao
     * @time:
     */
    public JSONObject scheduleListAPP(String name, String desc, String date, String startTime, String endTime) throws Exception {
        String url = "/porsche/daily-work/schedule/add";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"description\":\"" + desc + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ***************************************************10.2 我的回访******************************************************************

    /**
     * @description: 10.2.1-WEB 获得回访记录列表
     * @author: liao
     * @time:
     */
    public JSONObject visitRecordListPC(String customerId, String date, int page, int size) throws Exception {
        String url = "/porsche/daily-work/return-visit-record/list";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.2.1-APP 获得回访记录列表
     * @author: liao
     * @time:
     */
    public JSONObject visitRecordListAPP(String customerId, String date, int page, int size) throws Exception {
        String url = "/porsche/daily-work/return-visit-record/app/list";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.2.4-WEB 获得我的回访任务列表(WEB端接口=>客户管理->今日工作-> 我的回访->回访任务列表)
     * @author: liao
     * @time:
     */
    public JSONObject withFilterAndCustomerDetailPC(String customerId, String date, int page, int size) throws Exception {
        String url = "/porsche/return-visit/task/list/withFilterAndCustomerDetail";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"date\":\"" + date + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.2.4-APP 获得我的回访任务列表(APP端接口=>今日工作->未联系/已联系列表)
     * @author: liao
     * @time:
     */
    public JSONObject withFilterAndCustomerDetailAPP(String customerId, int page, int size) throws Exception {
        String url = "/porsche/return-visit/task/list/withFilterAndCustomerDetail";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.2.4-WEB 删除回访任务(WEB端接口=>客户管理->今日工作-> 我的回访->回访任务列表->删除)
     * @author: liao
     * @time:
     */
    public JSONObject deleteTaskById(String id) throws Exception {
        String url = "/porsche/return-visit/task/deleteById";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ****************************************************预约试驾**********************************************************8

    /**
     * @description: 10.3.1 登记客户信息并新增预约试驾记录(APP端接口)
     * @author: liao
     * @time:
     */
    public JSONObject addDrive(String customerName, String idCard,String gender,String phone,
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


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }






    /**
     * @description: 10.3.3 删除预约试驾记录(WEB端接口=>客户管理->今日工作-> 我的试驾->预约试驾列表->删除)
     * @author: liao
     * @time:
     */
    public JSONObject driveDelete(long id) throws Exception {
        String url = "/porsche/daily-work/test-drive/delete";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject addJob(String name, String desc, String start, String end) throws Exception {
        String url = "/porsche/daily-work/schedule/add";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"description\":\"" + desc + "\",\n" +
                        "    \"start_time\":\"" + start + "\",\n" +
                        "    \"end_time\":\"" + end + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description: 10.3.4 获得预约试驾详情(WEB端接口=>客户管理->今日工作-> 我的试驾->预约试驾列表->详情)
     * @author: liao
     * @time:
     */
    public JSONObject driveDetail(long id) throws Exception {
        String url = "/porsche/daily-work/test-drive/detail";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 10.3.5 获得预约试驾列表(WEB端接口=>客户管理->今日工作-> 我的试驾->预约试驾列表)
     * @author: liao
     * @time:
     */
    public JSONObject driveList(int page, int size) throws Exception {
        String url = "/porsche/daily-work/test-drive/list";

        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    *****************************************************交车***********************************************************************

    /**
     * @description:
     * @author: liao
     * @time:
     */
    public JSONObject delieveList(int page, int size) throws Exception {
        String url = "/porsche/daily-work/deliver-car/list";

        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ################################################APP接口######################################################################

//    ***************************************************客户相关**********************************************************

    /**
     * @description: 2.0 完成接待
     * @author: liao
     * @time:
     */
    public JSONObject finishReception() throws Exception {
        String url = "/porsche/app/customer/finishReception";

        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    #################################################用户相关#####################################################################

    /**
     * @description: 1.1 用户状态列表
     * @author: liao
     * @time:
     */
    public JSONObject userStatusListAPP() throws Exception {
        String url = "/porsche/app/user/statusList";

        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.2 修改状态
     * @author: liao
     * @time:
     */
    public JSONObject updateStatusAPP(String saleStatus) throws Exception {
        String url = "/porsche/app/user/updateStatus";

        String json =
                "{\n" +
                        "    \"sale_status\":\"" + saleStatus + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3 用户状态
     * @author: liao
     * @time:
     */
    public JSONObject userStatusAPP(String saleStatus) throws Exception {
        String url = "/porsche/user/userStatus";

        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    #################################################客户管理##############################################################

    /**
     * @description: 2.1 APP创建客户
     * @author: liao
     * @time:
     */
    public JSONObject addCustomerApp(String customerId) throws Exception {
        String url = "/porsche/app/customer/add";

        String json =
                "{\n" +
                        "  \"decision_customer\" : {\n" +
                        "    \"remark\" : \"测试1备注信息\",\n" +
                        "    \"analysis_customer_id\" : \"" + customerId + "\",\n" +
                        "    \"customer_level\" : 0,\n" +
                        "    \"shop_id\" : \"" + getShopId() + "\"" +
                        "  }\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1 APP创建客户
     * @author: liao
     * @time:
     */
    public JSONObject addCustomerApp(String customerId, String analysisCustomerId, int customerLevel, int customerSelectType, String customerName, String customerPhone,
                                     int visitCount, int belongsArea, String alreadyCar, int testDriveCar, int sehandAssess,
                                     int carAssess, String preBuyTime, int likeCar, String compareCar, int showPrice,
                                     int payType, int buyCar, int buyCarType, int buyCarAttribute, String reamrks, String comment,
                                     String nextReturnVisitDate) throws Exception {
        String url = "/porsche/app/customer/add";

        String json =
                "{\n" +
                        "    \"customer_id\" :" + customerId + ",\n" +
                        "    \"shop_id\" :" + getShopId() + ",\n" +
                        "    \"analysis_customer_id\" : \"" + analysisCustomerId + "\"\n" +
                        "    \"along_list\" : [{\n" +
                        "        \"id\" : " + 1 + ",\n" +
                        "        \"analysis_customer_id\" : \"analysis_customer_id1\",\n" +
                        "    }],\n" +
                        "    \"customer_level\" :" + customerLevel + ",\n" + //客户级别(0-H,1-A,2-B,3-C,4-F)
                        "    \"customer_select_type\" :" + customerSelectType + ",\n" +//销售员所选客户类型(0-老客户重购,1-自然到访,2-亲友推荐,3-线上推广，4-官方推广)
                        "    \"customer_name\" : \"" + customerName + "\",\n" +
                        "    \"customer_phone\" : \"" + customerPhone + "\",\n" +
                        "    \"visit_count\" :" + visitCount + ",\n" + //到店人数(0-1人,1-2人,2-3人以上)
                        "    \"belongs_area\" :" + belongsArea + ",\n" + //所属区域(0-本地,1-南通,2-常州,3-其他)
                        "    \"already_car\" : \"" + alreadyCar + "\",\n" +
                        "    \"test_drive_car\" :" + testDriveCar + ",\n" + //试驾车型(0-TAYCAN,1-718,2-911,3-PANAMERA,4-MACAN,5-CAYENNE)
                        "    \"sehand_assess\" :" + sehandAssess + ",\n" + //二手车评估 (0:是,1:否)
                        "    \"car_assess\" : \"" + carAssess + "\",\n" +
                        "    \"pre_buy_time\" : \"" + preBuyTime + "\",\n" +
                        "    \"like_car\" :" + likeCar + ",\n" + //意向车型（0-TAYCAN,1-718,2-911,3-PANAMERA,4-MACAN,5-CAYENNE）
                        "    \"compare_car\" : \"" + compareCar + "\",\n" +
                        "    \"show_price\" :" + showPrice + ",\n" +//是否报价(0:是,1:否)
                        "    \"pay_type\" :" + payType + ",\n" +  //付款方式(0:全款,1:贷款)
                        "    \"buy_car\" :" + buyCar + ",\n" + //是否订车(0:是,1:否)
                        "    \"buy_car_attribute\" :" + buyCarAttribute + ",\n" +
                        "    \"buy_car_type\" :" + buyCarType + ",\n" +
                        "    \"reamrks\" : [\n" +
                        reamrks +
                        "    ],\n" +
                        "    \"return_visits\" : [{\n" +
                        "        \"comment\" : \"" + comment + "\",\n" +
                        "        \"next_return_visit_date\" : \"" + nextReturnVisitDate + "\"\n" +
                        "    }]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.2 查询客户详细信息
     * @author: liao
     * @time:
     */
    public JSONObject customerDetailApp(String customerId) throws Exception {
        String url = "/porsche/app/customer/detail";

        String json =
                "{\n" +
                        "    \"customer_id\" :" + customerId + ",\n" +
                        "    \"shop_id\" :" + getShopId() + ",\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.3 修改客户信息
     * @author: liao
     * @time:
     */
    public JSONObject customerEditApp(String customerId, String analysisCustomerId, int customerLevel, int customerSelectType, String customerName, String customerPhone,
                                      int visitCount, int belongsArea, String alreadyCar, int testDriveCar, int sehandAssess,
                                      int carAssess, String preBuyTime, int likeCar, String compareCar, int showPrice,
                                      int payType, int buyCar, int buyCarType, int buyCarAttribute, String reamrks, String comment,
                                      String nextReturnVisitDate) throws Exception {
        String url = "/porsche/app/customer/edit";

        String json =
                "{\n" +
                        "    \"customer_id\" :" + customerId + ",\n" +
                        "    \"shop_id\" :" + getShopId() + ",\n" +
                        "    \"analysis_customer_id\" : \"" + analysisCustomerId + "\"\n" +
                        "    \"along_list\" : [{\n" +
                        "        \"id\" : " + 1 + ",\n" +
                        "        \"analysis_customer_id\" : \"analysis_customer_id1\",\n" +
                        "    }],\n" +
                        "    \"customer_level\" :" + customerLevel + ",\n" + //客户级别(0-H,1-A,2-B,3-C,4-F)
                        "    \"customer_select_type\" :" + customerSelectType + ",\n" +//销售员所选客户类型(0-老客户重购,1-自然到访,2-亲友推荐,3-线上推广，4-官方推广)
                        "    \"customer_name\" : \"" + customerName + "\",\n" +
                        "    \"customer_phone\" : \"" + customerPhone + "\",\n" +
                        "    \"visit_count\" :" + visitCount + ",\n" + //到店人数(0-1人,1-2人,2-3人以上)
                        "    \"belongs_area\" :" + belongsArea + ",\n" + //所属区域(0-本地,1-南通,2-常州,3-其他)
                        "    \"already_car\" : \"" + alreadyCar + "\",\n" +
                        "    \"test_drive_car\" :" + testDriveCar + ",\n" + //试驾车型(0-TAYCAN,1-718,2-911,3-PANAMERA,4-MACAN,5-CAYENNE)
                        "    \"sehand_assess\" :" + sehandAssess + ",\n" + //二手车评估 (0:是,1:否)
                        "    \"car_assess\" : \"" + carAssess + "\",\n" +
                        "    \"pre_buy_time\" : \"" + preBuyTime + "\",\n" +
                        "    \"like_car\" :" + likeCar + ",\n" + //意向车型（0-TAYCAN,1-718,2-911,3-PANAMERA,4-MACAN,5-CAYENNE）
                        "    \"compare_car\" : \"" + compareCar + "\",\n" +
                        "    \"show_price\" :" + showPrice + ",\n" +//是否报价(0:是,1:否)
                        "    \"pay_type\" :" + payType + ",\n" +  //付款方式(0:全款,1:贷款)
                        "    \"buy_car\" :" + buyCar + ",\n" + //是否订车(0:是,1:否)
                        "    \"buy_car_attribute\" :" + buyCarAttribute + ",\n" +
                        "    \"buy_car_type\" :" + buyCarType + ",\n" +
                        "    \"reamrks\" : [\n" +
                        reamrks +
                        "    ],\n" +
                        "    \"return_visits\" : [{\n" +
                        "        \"comment\" : \"" + comment + "\",\n" +
                        "        \"next_return_visit_date\" : \"" + nextReturnVisitDate + "\"\n" +
                        "    }]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.3 修改客户信息
     * @author: liao
     * @time:
     */
    public JSONObject customerInfoEnumApp() throws Exception {
        String url = "/porsche/app/customer/customer_info_enum";

        String json =
                "{\n" +
                        "    \"shop_id\" :" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ###########################################数据验证方法#########################################################

    public int getOverviewData(JSONObject data, String type) {

        JSONArray list = data.getJSONArray("list");

        int value = 0;


        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);

            if (type.equals(single.getString("type"))) {
                value = single.getInteger("value");
                break;
            }
        }

        return value;
    }

    public int getArriveTrendDataUv(JSONObject data) {

//        所有天的总uv累计
        JSONArray list = data.getJSONArray("list");

        int total = 0;

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);
            JSONArray list1 = single.getJSONArray("list");

            if (list1 != null && list1.size() > 0) {
                if (list1.getString(list1.size() - 1) != null) {
                    if (list1.getString(list1.size() - 1) != null) {
                        total += list1.getInteger(list1.size() - 1);
                    }
                }
            }
        }

        return total;
    }

    public int getArriveTrendDataAvgUv(JSONObject data) {

//        所有天的总uv累计
        JSONArray list = data.getJSONArray("list");

        int dayNum = 0;

        int total = 0;

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);
            JSONArray list1 = single.getJSONArray("list");

            if (list1 != null && list1.size() > 0) {
                if (list1.getString(list1.size() - 1) != null) {
                    dayNum++;
                    total += list1.getInteger(list1.size() - 1);
                }
            }
        }

        if (dayNum > 0) {
            total = (int) Math.ceil((double) total / dayNum);
        }

        return total;
    }

    public void checkUvMTAccumulatedTrend(JSONObject data, String dimension) {

//        每一天的总客流都大于该天的类型累计
        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);

            JSONArray list1 = single.getJSONArray("list");
//            当天抓拍uv
            int total = list1.getInteger(0);
            int total1 = 0;
            for (int j = 1; j < list1.size(); j++) {
                list1.getInteger(0);

//                当天类型累计
                total1 += list1.getInteger(j);
            }

            Preconditions.checkArgument(total > total1, "客流趋势，dimension=" + dimension +
                    "，timestamp=" + single.getLongValue("time") + "总客流=" + total + "应大于 各个类型累计总和=" + total1);
        }
    }

    public void checkTrendUvDimensionEquals() throws Exception {

        String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};

        for (int k = 0; k < cycleTypes.length; k++) {

            String dimension1 = "CUSTOMER_TYPE";
            JSONArray list1 = arriveTrendCycleS(cycleTypes[k], dimension1).getJSONArray("list");

            String dimension2 = "SOURCE";
            JSONArray list2 = arriveTrendCycleS(cycleTypes[k], dimension2).getJSONArray("list");

            String dimension3 = "CHANNEL";
            JSONArray list3 = arriveTrendCycleS(cycleTypes[k], dimension3).getJSONArray("list");

            String dimension4 = "VISIT_TIMES";
            JSONArray list4 = arriveTrendCycleS(cycleTypes[k], dimension4).getJSONArray("list");

            int uv1 = 0;
            int uv2 = 0;
            int uv3 = 0;
            int uv4 = 0;

            for (int i = 0; i < list1.size(); i++) {

                JSONObject single1 = list1.getJSONObject(i);
                long time = single1.getLongValue("time");
                JSONArray list11 = single1.getJSONArray("list");

                if (list11 != null && list11.size() > 0) {
                    if (list11.getString(list11.size() - 1) == null) {
                        uv1 = 0;
                    } else {
                        uv1 = list11.getInteger(list11.size() - 1);
                    }
                }

                JSONObject single2 = list2.getJSONObject(i);
                JSONArray list21 = single2.getJSONArray("list");

                if (list21 != null && list21.size() > 0) {

                    if (list21.getString(list21.size() - 1) == null) {
                        uv2 = 0;
                    } else {
                        uv2 = list21.getInteger(list21.size() - 1);
                    }
                }

                JSONObject single3 = list3.getJSONObject(i);
                JSONArray list31 = single3.getJSONArray("list");

                if (list31 != null && list31.size() > 0) {

                    if (list31.getString(list31.size() - 1) == null) {
                        uv3 = 0;
                    } else {
                        uv3 = list31.getInteger(list31.size() - 1);
                    }

                }

                JSONObject single4 = list4.getJSONObject(i);
                JSONArray list41 = single4.getJSONArray("list");
                if (list41 != null && list41.size() > 0) {

                    if (list41.getString(list41.size() - 1) == null) {
                        uv4 = 0;
                    } else {
                        uv4 = list41.getInteger(list41.size() - 1);
                    }
                }

                Preconditions.checkArgument(uv1 == uv2, "cycleType=" + cycleTypes[k] + "，到店客流趋势-time=" + time + "，dimension=" +
                        dimension1 + "时，总客流=" + uv1 + ",dimension=" + dimension2 + "时，总客流=" + uv2);

                Preconditions.checkArgument(uv1 == uv3, "cycleType=" + cycleTypes[k] + "，到店客流趋势-time=" + time + "，dimension=" +
                        dimension1 + "时，总客流=" + uv1 + ",dimension=" + dimension3 + "时，总客流=" + uv3);

                Preconditions.checkArgument(uv1 == uv4, "cycleType=" + cycleTypes[k] + "，到店客流趋势-time=" + time + "，dimension=" +
                        dimension1 + "时，总客流=" + uv1 + ",dimension=" + dimension4 + "时，总客流=" + uv4);
            }

        }
    }

    public void checkTrend3DimensionEquals() throws Exception {

        String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};

        for (int k = 0; k < cycleTypes.length; k++) {

            String dimension1 = "CUSTOMER_TYPE";
            JSONArray list1 = arriveTrendCycleS(cycleTypes[k], dimension1).getJSONArray("list");

            String dimension2 = "SOURCE";
            JSONArray list2 = arriveTrendCycleS(cycleTypes[k], dimension2).getJSONArray("list");

            String dimension3 = "CHANNEL";
            JSONArray list3 = arriveTrendCycleS(cycleTypes[k], dimension3).getJSONArray("list");

            int uv1 = 0;
            int uv2 = 0;
            int uv3 = 0;

            for (int i = 0; i < list1.size(); i++) {

                JSONObject single1 = list1.getJSONObject(i);
                long time = single1.getLongValue("time");
                JSONArray list11 = single1.getJSONArray("list");
                if (list11 != null && list11.size() > 0) {
                    if (list11.getString(list11.size() - 2) == null) {
                        uv1 = 0;
                    } else {
                        uv1 = list11.getInteger(list11.size() - 2);
                    }
                }

                JSONObject single2 = list2.getJSONObject(i);
                JSONArray list21 = single2.getJSONArray("list");

                if (list21 != null && list21.size() > 0) {

                    if (list21.getString(list21.size() - 2) == null) {
                        uv2 = 0;
                    } else {
                        uv2 = list21.getInteger(list21.size() - 2);
                    }
                }

                JSONObject single3 = list3.getJSONObject(i);
                JSONArray list31 = single3.getJSONArray("list");
                if (list31 != null && list31.size() > 0) {

                    if (list31.getString(list31.size() - 2) == null) {
                        uv3 = 0;
                    } else {
                        uv3 = list31.getInteger(list31.size() - 2);
                    }
                }

                Preconditions.checkArgument(uv1 == uv2, "cycleType=" + cycleTypes[k] + "，到店客流趋势-time=" + time + "，dimension=" +
                        dimension1 + "时，同行客户人数=" + uv1 + ",dimension=" + dimension2 + "时，同行客户人数=" + uv2);

                Preconditions.checkArgument(uv1 == uv3, "cycleType=" + cycleTypes[k] + "，到店客流趋势-time=" + time + "，dimension=" +
                        dimension1 + "时，同行客户人数=" + uv1 + ",dimension=" + dimension3 + "时，同行客户人数=" + uv3);
            }
        }
    }

    public void chkVisitData(JSONObject data, String cycleType) {

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);

            if ("TIMES".equals(single.getString("type"))) {
                Preconditions.checkArgument(single.getInteger("value") > 0, "cycleType=" + cycleType + "，平均到店次数=" +
                        single.getInteger("value") + "期待>0");
            }

            if ("INTERVAL".equals(single.getString("type"))) {
                Preconditions.checkArgument(single.getString("value") == null || single.getInteger("value") > 0, "cycleType=" + cycleType + "，平均到店间隔=" +
                        single.getInteger("value") + "期待>0");
            }

            if ("TRAVEL_DEPTH".equals(single.getString("type"))) {
                double value = single.getDoubleValue("value");
                Preconditions.checkArgument(value > 0 && value <= 100, "cycleType=" + cycleType + "，游逛深度=" +
                        value + "期待0<游逛深度<=100%");
            }

            if ("STAY_TIME".equals(single.getString("type"))) {
                Preconditions.checkArgument(single.getInteger("value") > 0, "cycleType=" + cycleType + "，平均每天停留时长=" +
                        single.getInteger("value") + "期待>0");
            }
        }
    }

    public void chkAgeGender(JSONObject data, String cycleType) throws Exception {

        DecimalFormat df = new DecimalFormat("0.00");

        String maleRatioStr = data.getString("male_ratio_str");
        String femaleRatioStr = data.getString("female_ratio_str");

        float maleF = 0f;
        float femaleF = 0f;

        if (!"-".equals(maleRatioStr)) {

//                校验男女总比例
            maleF = Float.valueOf(maleRatioStr.substring(0, maleRatioStr.length() - 1));
            femaleF = Float.valueOf(femaleRatioStr.substring(0, femaleRatioStr.length() - 1));

            if ((int) (maleF + femaleF) != 100) {
                throw new Exception("cycleType=" + cycleType + "，性别年龄分布-男比例=" + maleRatioStr + ",女比例=" + femaleRatioStr + "之和不是100%");
            }

//                校验各个年龄段的男女比例
            JSONArray list = data.getJSONArray("ratio_list");

            float percentMaleF = 0f;
            float percentFemaleF = 0f;
            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                if ("MALE".equals(single.getString("gender"))) {
                    String percentMale = single.getString("percent");
                    percentMale = percentMale.substring(0, percentMale.length() - 1);
                    percentMaleF += Float.valueOf(percentMale);
                } else {
                    String percentFemale = single.getString("percent");
                    percentFemale = percentFemale.substring(0, percentFemale.length() - 1);
                    percentFemaleF += Float.valueOf(percentFemale);
                }
            }

            Preconditions.checkArgument(Math.abs(maleF - percentMaleF) < 0.02, "cycleType=" + cycleType +
                    "，消费者年龄性别分布,各个年龄段的男性比例=" + percentMaleF +
                    "，不等于男性总比例=" + maleF);

            Preconditions.checkArgument(Math.abs(femaleF - percentFemaleF) < 0.02, "cycleType=" + cycleType +
                    "，消费者年龄性别分布,各个年龄段的女性比例=" + percentFemaleF +
                    "，不等于女性总比例=" + femaleF);
        }
    }

    public int getHourDataUv(JSONObject data) throws Exception {

        int total = 0;

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            total += list.getInteger(i);
        }

        return total;
    }

    public int getTrendPv(JSONObject data) throws Exception {

        JSONArray list = data.getJSONArray("list");

        int total = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            JSONArray list1 = single.getJSONArray("list");

            if (list1 != null && list1.size() > 0) {
                for (int j = 1; j < list1.size(); j++) {
                    total += list1.getInteger(j) * j;
                }
            }
        }

        return total;
    }

    public int getTrendUv(JSONObject data) throws Exception {

        JSONArray list = data.getJSONArray("list");

        int total = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            JSONArray list1 = single.getJSONArray("list");

            for (int j = 1; j < list1.size(); j++) {
                total += list1.getInteger(j) * j;
            }
        }

        return total;
    }

    public HashMap<String, Double> getInterestContrast(JSONObject data) {

        HashMap<String, Double> hm = new HashMap<>();

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);
            String skuName = single.getString("sku_name");
            double interestContrast = single.getDoubleValue("interest_contrast_str");

            hm.put(skuName, interestContrast);
        }

        return hm;
    }

    public void chkVisitDataRegionUnique(JSONObject data, String cycleType) throws Exception {

        HashMap<String, Integer> hm = new HashMap<>();

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);
            String regionId = single.getString("region_id");

            if (hm.containsKey(regionId)) {
                throw new Exception("cycleType=" + cycleType + "，到店客流趋势，存在两个region_id=" + regionId + "的区域");
            } else {
                hm.put(regionId, 1);
            }
        }
    }

    public void checkInterestContrast(JSONObject data, HashMap hm, String cycleType) throws Exception {

        DecimalFormat df = new DecimalFormat("0.00");

        JSONArray list = data.getJSONArray("list");

        double total = 0.0d;

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);
            total += single.getDoubleValue("interest");
        }

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);

            String skuName = single.getString("sku_name");
            double interest = single.getDoubleValue("interest");

            String actual = df.format(interest * 100 / (total / 5)) + "%";

            Preconditions.checkArgument(actual.equals(hm.get(skuName)), "cycleType=" + cycleType + "，车型=" + skuName +
                    "，到店客流趋势中返回对比店内车型平均关注度=" + hm.get(skuName) + "，根据客流排行中数据计算=" + actual);
        }
    }

    public void checkVisitData(JSONObject data, String cycleType) throws Exception {

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size() - 1; i++) {

            JSONObject single = list.getJSONObject(i);

            String skuName = single.getString("sku_name");
            String regionName = single.getString("region_name");
//            String interestContrastStr = single.getString("interest_contrast_str");
            String drive = single.getString("drive");
            String dealNum = single.getString("deal_num");

            for (int j = i + 1; j < list.size(); j++) {

                JSONObject single1 = list.getJSONObject(i);

                if (skuName.equals(single1.getString("sku_name"))) {
                    String regionName1 = single.getString("region_name");
                    String drive1 = single1.getString("drive");
                    String dealNum1 = single1.getString("deal_num");

                    Preconditions.checkArgument(drive.equals(drive1), "cycleType=" + cycleType + "，到店客流趋势，试乘试驾次数，车型=" + skuName +
                            "，在区域【" + regionName + "】中是=" + drive + "，在区域【" + regionName1 +
                            "】中是=" + drive1);

                    Preconditions.checkArgument(dealNum.equals(dealNum1), "cycleType=" + cycleType + "，到店客流趋势，成交量，车型=" + skuName +
                            "，在区域【" + regionName + "】中是=" + dealNum + "，在区域【" + regionName1 +
                            "】中是=" + dealNum1);
                    break;
                }
            }
        }
    }

    public void checkVisitDataAndSkuRank(JSONObject visitData, JSONObject skuRank, String cycleType) throws Exception {

        JSONArray list = skuRank.getJSONArray("list");
        JSONArray list1 = visitData.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);

            String skuName = single.getString("sku_name");
            String drive = single.getString("drive");
            String dealNum = single.getString("deal_num");

            for (int j = 0; j < list1.size(); j++) {

                JSONObject single1 = list1.getJSONObject(j);

                if (skuName.equals(single1.getString("sku_name"))) {
                    String regionName = single.getString("region_name");
                    String drive1 = single1.getString("drive");
                    String dealNum1 = single1.getString("deal_num");

                    Preconditions.checkArgument(drive.equals(drive1), "cycleType=" + cycleType + "，试乘试驾次数，车型=" + skuName +
                            "，在到店客流趋势的区域【" + regionName + "】中是=" + drive + "，在商品排行中=" + drive1);

                    Preconditions.checkArgument(dealNum.equals(dealNum1), "cycleType=" + cycleType + "，成交量，车型=" + skuName +
                            "，在到店客流趋势的区域【" + regionName + "】中是=" + dealNum + "，在商品排行中=" + dealNum1);
                    break;
                }
            }
        }
    }

    public void checkRegionLTUv(JSONObject visitData, int total, String cycleType) throws Exception {

        JSONArray list = visitData.getJSONArray("list");

        for (int j = 0; j < list.size(); j++) {

            JSONObject single = list.getJSONObject(j);

            String regionName = single.getString("region_name");
            int uv = single.getInteger("uv");

            Preconditions.checkArgument(uv <= total, "cycleType=" + cycleType + "，到店客流趋势的区域【" + regionName + "】中的uv=" + uv
                    + "不应大于累计到访人数=" + total);
        }
    }

    public void checkRegionsLTPv(JSONObject visitData, int pv, String cycleType) {

        JSONArray list = visitData.getJSONArray("list");

        int total = 0;

        for (int j = 0; j < list.size(); j++) {

            JSONObject single = list.getJSONObject(j);

            if ("WORK_AREA".equals(single.getString("region_type"))) {
                total += single.getInteger("uv");
            }

            Preconditions.checkArgument(total <= pv, "cycleType=" + cycleType + "，到店客流趋势的区域uv之和=" + total +
                    "不应大于累计到访人次=" + pv);
        }
    }

    public void checkRegionStayTimeMT0(JSONObject visitData, String cycleType) {

        JSONArray list = visitData.getJSONArray("list");


        for (int j = 0; j < list.size(); j++) {

            JSONObject single = list.getJSONObject(j);

            if (single.getInteger("uv") > 0) {

                Preconditions.checkArgument(single.getInteger("stay_time") > 0, "cycleType=" + cycleType + "，到店客流趋势的区域【" + single.getString("region_name") +
                        "】的人均停留时长不应=" + single.getInteger("stay_time"));
            } else {
                Preconditions.checkArgument("-".equals(single.getInteger("stay_time")), "cycleType=" + cycleType + "，到店客流趋势的区域【" + single.getString("region_name") +
                        "】的uv=0,人均停留时长应为“-”，不应=" + single.getInteger("stay_time"));
            }
        }
    }

    public int getSalerCustomerNum(JSONObject data, String saleId) throws Exception {

        JSONArray list = data.getJSONArray("list");

        int customerNum = 0;

        for (int j = 0; j < list.size(); j++) {

            JSONObject single = list.getJSONObject(j);

            if (saleId.equals(single.getString("sale_id"))) {

                customerNum += single.getInteger("today_customer_num");
            }
        }

        return customerNum;
    }

    public void checkSalerStatus(JSONObject data, String saleId, String status) throws Exception {

        JSONArray list = data.getJSONArray("list");

        for (int j = 0; j < list.size(); j++) {

            JSONObject single = list.getJSONObject(j);

            if (saleId.equals(single.getString("sale_id"))) {
                String statusRes = single.getString("sale_status");
                Preconditions.checkArgument(status.equals(statusRes), "majordomoSaleId=" + saleId +
                        ",期待状态为【" + status + "】" + "，系统返回其状态为=" + statusRes);
            }
        }
    }

    public void checkAddCustomer(JSONObject data, String customerId, String analysisCustomerId, int customerLevel,
                                 int customerSelectType, String customerName, String customerPhone,
                                 int visitCount, int belongsArea, String alreadyCar, int testDriveCar, int sehandAssess,
                                 int carAssess, String preBuyTime, int likeCar, String compareCar, int showPrice,
                                 int payType, int buyCar, int buyCarType, int buyCarAttribute, String reamrks, String comment,
                                 String nextReturnVisitDate) throws Exception {

        checkUtil.checkKeyValue("顾客详情", data, "", customerLevel + "", true);

    }

    public void checkAddDrive(JSONObject data, String customerName, String idCard, String gender, String phone,
                              long signTime, long appointmentTime, String model, String country,
                              String city, String email, String address, String driverLicensePhoto1Url,
                              String driverLicensePhoto2Url, String electronicContractUrl) throws Exception {

        checkUtil.checkKeyValue("试驾详情", data, "customer_name", customerName, true);
        checkUtil.checkKeyValue("试驾详情", data, "customer_id_number", idCard, true);
        checkUtil.checkKeyValue("试驾详情", data, "customer_gender", gender, true);
        checkUtil.checkKeyValue("试驾详情", data, "customer_phone_number", phone, true);
        checkUtil.checkKeyValue("试驾详情", data, "sign_time", signTime + "", true);
        checkUtil.checkKeyValue("试驾详情", data, "appointment_time", appointmentTime + "", true);
        checkUtil.checkKeyValue("试驾详情", data, "model", model, true);
        checkUtil.checkKeyValue("试驾详情", data, "country", country, true);
        checkUtil.checkKeyValue("试驾详情", data, "city", city, true);
        checkUtil.checkKeyValue("试驾详情", data, "email", email, true);
        checkUtil.checkKeyValue("试驾详情", data, "address", address, true);
        checkUtil.checkKeyValue("试驾详情", data, "driver_license_photo_1_url", driverLicensePhoto1Url, true);
        checkUtil.checkKeyValue("试驾详情", data, "driver_license_photo_2_url", driverLicensePhoto2Url, true);
        checkUtil.checkKeyValue("试驾详情", data, "electronic_contract_url", electronicContractUrl, true);
    }

    public void checkDeleteDrive(JSONObject data, long id) throws Exception {

        boolean isExist = false;

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if (id == single.getLongValue("id")) {
                isExist = true;
            }
        }

        Preconditions.checkArgument(isExist == false, "删除试驾后，该试驾信息没有从试驾列表中删除,试驾id=" + id);
    }

    public void checkOnservice1(JSONObject data) {

        HashMap<String, Integer> hm = new HashMap<>();

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            String saleName = single.getString("sale_name");

            if (saleName != null) {
                if (hm.containsKey(saleName) && "接待中".equals(single.getString("user_status_name"))) {
                    hm.put(saleName, hm.get(saleName) + 1);
                } else {
                    if ("接待中".equals(single.getString("user_status_name"))) {
                        hm.put(saleName, 1);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : hm.entrySet()) {
            Preconditions.checkArgument(entry.getValue() <= 1, "saleName=" + entry.getKey() + "的销售员有【" + entry.getValue() + "】个接待中状态的接待信息。");
        }
    }

    public int getTodayListOnService(JSONObject data, String statusName) throws Exception {

        int total = 0;

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if (statusName.equals(single.getString("user_status_name"))) {
                total++;
            }
        }

        return total;
    }

    public int getOrderListOnService(JSONObject data, String statusName) throws Exception {

        int total = 0;

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if (statusName.equals(single.getString("sale_status_name"))) {
                total++;
            }
        }

        return total;
    }

    public void getTodayListWaitNew0(JSONObject data) throws Exception {

        int total = 0;
        String userId = "";

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if ("新客".equals(single.getString("type_name")) && "等待中".equals(single.getString("user_status_name"))) {
                total++;
                userId = single.getString("user_id");
                break;
            }
        }

        Preconditions.checkArgument(total == 0, "新客不应该有等待中状态，userId= " + userId);
    }

    public void checkSaleStatus(JSONObject data, String saleId, String expectStatus) throws Exception {

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if (saleId.equals(single.getString("sale_id"))) {

                Preconditions.checkArgument(expectStatus.equals(single.getString("sale_status_name")), "majordomoSaleId= " + saleId +
                        "，退出登陆后，状态不应是【" + single.getString("sale_status_name") + "】");
                break;
            }
        }
    }

    public void checkDeleteUser(JSONObject data, String userId) throws Exception {

        JSONArray list = data.getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            if (userId.equals(single.getString("user_id"))) {
                isExist = false;
                break;
            }
        }

        Preconditions.checkArgument(isExist == false, "删除账号后，该账号依然存在，userId=" + userId);

    }

    public int getUserPageSalerNUm() throws Exception {

        int pages = userPage(1, 10).getInteger("pages");

        int total = 0;

        for (int i = 1; i <= pages; i++) {
            JSONObject data = userPage(i, 10);
            total += getSaler(data);
        }

        return total;
    }

    public int getSaler(JSONObject data) {

        int total = 0;

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String role_id = single.getString("role_id");
            if ("13".equals(role_id)) {
                total++;
            }
        }

        return total;
    }


    public int genRoleId() {

        int[] roleIds = {10, 11, 12, 14};

        Random random = new Random();

        return roleIds[random.nextInt(5)];
    }


//    #######################################################公共方法##########################################################################

    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String genRandom7() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 7);
    }

    public String genRandom() {

        String tmp = UUID.randomUUID().toString();

        return tmp;
    }


    public Object getShopId() {
        return "22728";
    }

    public String httpPostWithCheckCode(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        checkCode(response, StatusCode.SUCCESS, path);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();


        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expect != code) {
                if (code != 1000) {
                    message += resJo.getString("message");
                }
                Assert.assertEquals(code, expect, message);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    public void adminLogin() {
        login(adminName, adminPasswd);
    }

    public void managerLogin() {
        login(managerName, managerPasswd);
    }

    public void majordomoLogin() {
        login(majordomoName, majordomoPasswd);
    }

    public void frontDeskLogin() {
        login(frontDeskName, frontDeskPasswd);
    }


    public void salesPersonLogin() {
        login(salesPersonName, salesPersonPasswd);
    }

    public void login(String userName, String passwd) {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        initHttpConfig();
        String path = "/porsche-login";
        String loginUrl = getIpPort() + path;
        String json =
                "{\n" +
                        "    \"username\":\"" + userName + "\",\n" +
                        "    \"password\":\"" + passwd + "\",\n" +
                        "    \"type\":0\n" +
                        "}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, failReason, "登录获取authentication");
    }

    public void logout() throws Exception {
        String url = "/porsche-logout";

        String json =
                "{}";

        httpPostWithCheckCode(url, json);
    }

    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }

        FAIL = false;
    }

    public void saveData(Case aCase, String ciCaseName, String caseName, String failReason, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, failReason, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());

            failReason = aCase.getFailReason();

            String message = "CRM 日常 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + failReason;

            dingPush(aCase, message);
        }
    }

    public void dingPush(Case aCase, String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    public void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String failReason, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "异常");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "异常");
            } else if (failReason.contains("java.lang.IllegalArgumentException")) {
                failReason = failReason.replace("java.lang.IllegalArgumentException", "异常");
            }
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    public String getIpPort() {
        return "http://dev.porsche.dealer-ydauto.winsenseos.cn";
    }
}
