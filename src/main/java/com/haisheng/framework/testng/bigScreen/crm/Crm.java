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
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public String adminName = "";
    public String adminPasswd = "";

    public String managerName = "";
    public String managerPasswd = "";

    public String majordomoName = "";
    public String majordomoPasswd = "";

    public String frontDeskName = "";
    public String frontDeskPasswd = "";

    public String salesPersonName = "";
    public String salesPersonPasswd = "";

    public HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();


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
    public JSONObject overviewS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/overview";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.2 历史互动人数趋势
     * @author: liao
     * @time:
     */
    public JSONObject arriveTrendS(String cycleType, String month, String dimension) throws Exception {
        String url = "/porsche/history/shop/arrive-trend";

        String json = "{";

        if (!"".equals(cycleType)) {
            json += "    \"cycle_type\":\"" + cycleType + "\",";
        }

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
    public JSONObject visitDataS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/visit-data";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.5 历史到店时段客流分布
     * @author: liao
     * @time:
     */
    public JSONObject hourDataS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/hour-data";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1.6 历史到店伴随分布
     * @author: liao
     * @time:
     */
    public JSONObject accompanyS(String cycleType, String month) throws Exception {
        String url = "/porsche/history/shop/accompany";

        String json = genPorscheJson(cycleType, month);

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ******************************************三、区域商品相关接口**************************************************************

    /**
     * @description: 3.1 区域到访数据
     * @author: liao
     * @time:
     */
    public JSONObject rVisitDataR(String cycleType, String month) throws Exception {
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
    public JSONObject roleList(String userName, int roleId) throws Exception {
        String url = "/porsche/user/roleList";

        String json =
                "{\n" +
                        "  \"user_name\":\"" + userName + "\",\n" +
                        "  \"role_id\":" + roleId +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3 创建账号
     * @author: liao
     * @time:
     */
    public JSONObject addUser(String userName, int roleId) throws Exception {
        String url = "/porsche/user/add";

        String json =
                "{\n" +
                        "  \"user_name\":\"" + userName + "\",\n" +
                        "  \"user_login_name\":\"" + userName + "\",\n" +
                        "  \"password\":\"" + userName + "\",\n" +
                        "  \"user_phone\":\"" + userName + "\",\n" +
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
    public JSONObject statusList(String userId) throws Exception {
        String url = "/porsche/user/delete";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ***********************************************前台工作****************************************************************

    /**
     * @description: 1.1 今日接待顾客列表
     * @author: liao
     * @time:
     */
    public JSONObject customerTodayList(String userId) throws Exception {
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
    public JSONObject freeSaleUserList(String userId) throws Exception {
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
    public JSONObject customerDetail(String id) throws Exception {
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
    public JSONObject customerDetail(long id, int customerLevel, int customerSelectType, String customerName, String customerPhone,
                                     int visitCount, int belongsArea, String alreadyCar, int testDriveCar, int sehandAssess,
                                     int carAssess, String preBuyTime, int likeCar, String compareCar, int showPrice,
                                     int payType, int buyCar, int buyCarType, int buyCarAttribute, String reamrks, String comment,
                                     String nextReturnVisitDate) throws Exception {
        String url = "/porsche/customer/edit";

        String json =
                "{\n" +
                        "    \"customer_id\" : 1,\n" +
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
    public JSONObject customerList(String id, int customerLevel, String customerName, String customerPhone,
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

    /**
     * @description: 6.4 删除客户
     * @author: liao
     * @time:
     */
    public JSONObject customerDelete(long id) throws Exception {
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
    public JSONObject todayList(String id, int customerLevel, String customerName, String customerPhone,
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
    public JSONObject customerInfoEnum() throws Exception {
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
    public JSONObject deleteTodayVisit(long id) throws Exception {
        String url = "/porsche/customer/delete_today_visit";

        String json =
                "{\n" +
                        "   \"customer_id\" :" + id + "} ";

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

    public int getArriveTrendDataUv(JSONObject data, int index) {

//        所有天的总uv累计
        JSONArray list = data.getJSONArray("list");

        int total = 0;

        for (int i = 0; i < list.size(); i++) {

            JSONObject single = list.getJSONObject(i);

            total += single.getJSONArray("list").getInteger(index);
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


    public void checkTrendUvtabEquals() throws Exception {

        String cycleType = "";
        String month = "";
        String dimension1 = "";
        JSONArray list1 = arriveTrendS(cycleType, month, dimension1).getJSONArray("list");

        String dimension2 = "";
        JSONArray list2 = arriveTrendS(cycleType, month, dimension2).getJSONArray("list");

        String dimension3 = "";
        JSONArray list3 = arriveTrendS(cycleType, month, dimension3).getJSONArray("list");

        String dimension4 = "";
        JSONArray list4 = arriveTrendS(cycleType, month, dimension4).getJSONArray("list");

        for (int i = 0; i < 7; i++) {

            JSONObject single1 = list1.getJSONObject(i);
            long time = single1.getLongValue("time");
            int uv1 = single1.getJSONArray("list").getInteger(0);

            JSONObject single2 = list2.getJSONObject(i);
            int uv2 = single2.getJSONArray("list").getInteger(0);

            JSONObject single3 = list3.getJSONObject(i);
            int uv3 = single3.getJSONArray("list").getInteger(0);

            JSONObject single4 = list4.getJSONObject(i);
            int uv4 = single4.getJSONArray("list").getInteger(0);

            Preconditions.checkArgument(uv1==uv2,"到店客流趋势-time="+ time + "，dimension=" +
                    dimension1 +"时，总客流=" + uv1 + ",dimension=" + dimension2 + "时，总客流=" + uv2);

            Preconditions.checkArgument(uv1==uv3,"到店客流趋势-time="+ time + "，dimension=" +
                    dimension1 +"时，总客流=" + uv1 + ",dimension=" + dimension3 + "时，总客流=" + uv3);

            Preconditions.checkArgument(uv1==uv4,"到店客流趋势-time="+ time + "，dimension=" +
                    dimension1 +"时，总客流=" + uv1 + ",dimension=" + dimension4 + "时，总客流=" + uv4);
        }
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
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, failReason, "登录获取authentication");
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
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
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
        return "http://dev.store.winsenseos.cn";
    }

}
