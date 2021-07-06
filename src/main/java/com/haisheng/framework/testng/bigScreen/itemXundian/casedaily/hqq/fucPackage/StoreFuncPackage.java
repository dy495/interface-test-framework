package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.fucPackage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class StoreFuncPackage extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile StoreFuncPackage instance = null;
    private StoreFuncPackage() {
    }

    public static StoreFuncPackage getInstance() {

        if (null == instance) {
            synchronized (StoreFuncPackage.class) {
                if (null == instance) {
                    //这里
                    instance = new StoreFuncPackage();
                }
            }
        }

        return instance;
    }

    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://123.57.148.247";
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    public String filepath="src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/64.txt";  //巡店不合格图片base64

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
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");

    }



    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        final String NUMBER = ".";

        String uid = "uid_0ba743d8";
        String appId = "672170545f50";//c30dcafc59c8
        String ak = "691ff41137d954f3";
        String router = "/business/bind/TRANS_INFO_RECEIVE/v1.0";
        String sk = "d76f2d8a7846382f633c1334139767fe";
        final String ALGORITHM = "HmacSHA256";

        Long timestamp = System.currentTimeMillis();
        String non = "2e4b56c4-ac4c-4778-aa12-81657f5feb44";



        System.out.println(timestamp);
    }

    /**
     *
     *云追客（客流分析）模块
     */

    /**获取昨天的累计客户总数,今天新增的顾客、全渠道会员、付费会员，获取前天的累计顾客总数**/
    public  Map<String, Integer> getAllCustomer(Long shop_id,String cycle_type,String month) throws Exception {
        JSONArray trend_list = md.historyShopMemberV3(shop_id, cycle_type, month).getJSONArray("trend_list");
        Integer customer_uv= 0;
        Integer omni_uv_total = 0;
        Integer customer_uv_01 = 0;
        Integer omni_uv_total_01 = 0;
        for (int i = 0; i < trend_list.size(); i++) {
            //获取昨天的累计客户总数,今天新增的顾客、全渠道会员、付费会员
            if (i - trend_list.size() == -1) {
                customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                omni_uv_total = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
            }
            //获取前天的累计顾客总数
            if (i - trend_list.size() == -2) {
                customer_uv_01 = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                omni_uv_total_01 = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("customer_uv", customer_uv);
        result.put("omni_uv_total", omni_uv_total);
        result.put("customer_uv_01", customer_uv_01);
        result.put("omni_uv_total_01", omni_uv_total_01);
        return result;
    }
    /**获取最近x天各个客群时段分布的总和**/
    public Map<String, Integer> getAllCustSum(JSONArray showList,int count) throws Exception {
        int times1 = 0;
        int times2 = 0;
        int times3 = 0;
        int times4 = 0;
        for (int i = 0; i < showList.size(); i++) {
            Integer deal_pv = showList.getJSONObject(i).getInteger("deal_pv");
            Integer enter_pv = showList.getJSONObject(i).getInteger("enter_pv");
            Integer interest_pv = showList.getJSONObject(i).getInteger("interest_pv");
            Integer pass_pv = showList.getJSONObject(i).getInteger("pass_pv");
            //获取交易客群的各个时段的数据（交易人次*有数据的天数的累加）
            if (deal_pv != null && deal_pv != 0) {
                int deal_pv1 = deal_pv * count;
                times1 += deal_pv1;
            }
            if (enter_pv != null && enter_pv != 0) {
                int enter_pv1 = enter_pv * count;
                times2 += enter_pv1;
            }
            if (interest_pv != null && interest_pv != 0) {
                int interest_pv1 = interest_pv * count;
                times3 += interest_pv1;
            }
            if (pass_pv != null && pass_pv != 0) {
                int pass_pv1 = pass_pv * count;
                times4 += pass_pv1;
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("deal", times1);
        result.put("enter", times2);
        result.put("interest", times3);
        result.put("pass", times4);
        return result;
    }

    /**将账户使用次数为0的角色删除**/
    public void deleteRole() throws Exception {
        JSONArray role_list = md.organizationRolePage("",1,100).getJSONArray("list");
        for(int i=0;i<role_list.size();i++){
            int account_number = role_list.getJSONObject(i).getInteger("account_number");
            if(account_number==0){
                Long role_id = role_list.getJSONObject(i).getLong("role_id");
                md.organizationRoleDelete(role_id);
            }

        }
    }
    /**
     *
     *云中客（新增客户）模块
     */

    /**从昨日新增人数分析模块-获取昨日新增顾客/全渠道会员/付费会员的新增人数**/
    public int  getNewCount(JSONArray data_list,String type) throws Exception {
        int new_uv = 0;
        for(int i=0;i<data_list.size();i++){
            String customer_type = data_list.getJSONObject(i).getString("customer_type");
            if(customer_type.equals(type)){
                new_uv = data_list.getJSONObject(i).getInteger("new_uv");
            }
        }
        return new_uv;
    }
    /**获取客户趋势图中昨日新增的客户人数(所有店)**/
    public int  getYesNew_count(String type,String cycle_type) throws Exception {
        JSONArray list = md.member_newCount_pic(cycle_type).getJSONArray("list");
        int num = 0;
        int count = list.size();
        for(int i1 =0;i1<count;i1++) {
            if (i1 == count - 1) {
                num = list.getJSONObject(i1).getInteger(type);
            }
        }
        return num;
    }
    /**获取客户趋势图中昨日新增的顾客人数(所有店)**/
    public Map<String, Integer>  getNew_counts(JSONArray list,String DataType) throws Exception {
        int uv1 = 0;
        int uv2 = 0;
        int count = list.size();
        for(int i1 =0;i1<count;i1++){
            if( i1 == count-1){
                uv1 = list.getJSONObject(i1).getInteger(DataType);
            }
        }
        /**获取客户趋势图中一周前新增的顾客人数**/
        for(int i2 =0;i2<count;i2++){
            if( i2 == count-8){
                uv2 = list.getJSONObject(i2).getInteger(DataType);
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("uv1", uv1);
        result.put("uv2", uv2);
        return result;
    }

    /**获取客户趋势图中昨日新增的客户人数(单店)**/
    public int  getYesNew_count_single(String type,Long shop_id_01,String cycle_type) throws Exception {
        JSONArray list = md.single_newCount_pic(shop_id_01,cycle_type).getJSONArray("list");
        int num = 0;
        int count = list.size();
        for(int i1 =0;i1<count;i1++) {
            if (i1 == count - 1) {
                num = list.getJSONObject(i1).getInteger(type);
            }
        }
        return num;
    }

    /**从新增顾客占比部分获取顾客部分得顾客占比(所有店)**/
    public String getTransformData(String type,String data_type ) throws Exception {
        JSONArray data_list = md.member_newCount_data().getJSONArray("list");
        String transform = "";
        for(int i=0;i<data_list.size();i++){
            String customer_type = data_list.getJSONObject(i).getString("customer_type");
            if(customer_type.equals(type)){
                transform = data_list.getJSONObject(i).getString(data_type);
            }
        }
        return transform;
    }

    /**从新增顾客占比部分获取顾客部分得顾客占比(单店)**/
    public String  getTransform_single(String type,Long shop_id_01,String data_type) throws Exception {
        JSONArray data_list = md.single_newCount_data(shop_id_01).getJSONArray("list");
        String transform = "";
        for(int i=0;i<data_list.size();i++){
            String customer_type = data_list.getJSONObject(i).getString("customer_type");
            if(customer_type.equals(type)){
                transform = data_list.getJSONObject(i).getString(data_type);
            }
        }
        return transform;
    }
    /**获取历史客流中昨日的到店客流总数(所有店客户占比计算时需要的到店客流数)**/
    public int getday_count(String cycle_type, String month, Long shop_id, Long shop_id_01) throws Exception {
        JSONArray trend_list1 = md.historyShopTrendV3(cycle_type, month, shop_id_01).getJSONArray("trend_list");
        int uv1 = 0;
        int uv2 = 0;
        int count1 = trend_list1.size();
        for (int i = 0; i < count1; i++) {
            if (i == count1 - 1) {
                uv1 = trend_list1.getJSONObject(i).getInteger("uv");
            }
        }
        if(shop_id == null){
            uv2 =0;
        }else {
            JSONArray trend_list2 = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int count2 = trend_list2.size();
            for (int i = 0; i < count2; i++) {
                if (i == count2 - 1) {
                    uv2 = trend_list2.getJSONObject(i).getInteger("uv");
                }
            }
        }

        int uvs = uv1 +uv2;
        return uvs;
    }

    /**获取客户趋势图中昨日和前天新增的顾客人数**/
    public Map<String, Integer> getNewCounts(JSONArray list,String DataType) throws Exception {
        int uv1 = 0;
        int uv2 = 0;
        int count = list.size();
        for(int i1 =0;i1<count;i1++){
            if( i1 == count-1){
                uv1 = list.getJSONObject(i1).getInteger(DataType);
            }
            if( i1 == count-2){
                uv2 = list.getJSONObject(i1).getInteger(DataType);
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("uv1", uv1);
        result.put("uv2", uv2);
        return result;
    }


    /**获取历史客流中昨日的到店客流总数(所有店日环比计算需要的客流数)**/
    public Map<String, Double> getday_count_all(String cycle_type, String month, Long shop_id_01, Long shop_id) throws Exception {
        double uv1 = 0;
        double uv2 = 0;
        JSONArray trend_list1 = md.historyShopTrendV3(cycle_type, month, shop_id_01).getJSONArray("trend_list");
        int count1 = trend_list1.size();
        for (int i = 0; i < count1; i++) {
            if (i == count1 - 1) {
                uv1 = trend_list1.getJSONObject(i).getInteger("uv");
            }
            if (i == count1 - 2) {
                uv2 = trend_list1.getJSONObject(i).getInteger("uv");
            }
        }
        double uv3 = 0;
        double uv4 = 0;
        if(shop_id ==null){
            uv3 = 0;
            uv4 = 0;
        }else{
            JSONArray trend_list2 = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int count3 = trend_list2.size();
            for (int i = 0; i < count3; i++) {
                if (i == count3 - 1) {
                    uv3 = trend_list2.getJSONObject(i).getInteger("uv");
                }
                if (i == count3 - 2) {
                    uv4 = trend_list2.getJSONObject(i).getInteger("uv");
                }
            }
        }

        Map<String, Double> result = new HashMap<>();
        result.put("uv1", uv1);
        result.put("uv2", uv2);
        result.put("uv3", uv3);
        result.put("uv4", uv4);
        return result;
    }
    /**获取到店趋势数据**/
    public int  getArriveCust(String cycle_type,String month,Long shop_id) throws Exception {
        JSONArray trend_list = md.historyShopTrendV3(cycle_type, month,shop_id ).getJSONArray("trend_list");
        int pvValues = 0;
        for (int i = 0; i < trend_list.size(); i++) {
            JSONObject jsonObject = trend_list.getJSONObject(i);
            if (jsonObject != null) {
                Integer pv = jsonObject.getInteger("pv");
                if (pv != null) {
                    pvValues += pv;//到店趋势中每天的pv累加
                }
            }
        }
        return pvValues;
    }


    /**所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和**/
    public Map<String, Integer> getEverySum(JSONArray trend_list) throws Exception {
        Integer customer_uv = 0;
        Integer omni_uv = 0;
        Integer paid_uv = 0;
        for (int i = 0; i < trend_list.size(); i++) {
            if (i - trend_list.size() == -1) {
                customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                omni_uv = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                paid_uv = trend_list.getJSONObject(i).getInteger("paid_uv_total");
                if (customer_uv == null || omni_uv == null || paid_uv == null) {
                    customer_uv = 0;
                }
            }

        }
        Map<String, Integer> result = new HashMap<>();
        result.put("customer_uv", customer_uv);
        result.put("omni_uv", omni_uv);
        result.put("paid_uv", paid_uv);
        return result;
    }

    /**获取所选周期的所有门店各天顾客之和**/
    public Map<String, Integer> getAllStoreCust_sum(JSONArray member_list) throws Exception {
        Integer cust_uv = 0;
        Integer channel_uv = 0;
        Integer pay_uv = 0;
        for (int j = 0; j < member_list.size(); j++) {
            JSONArray memb_info = member_list.getJSONObject(j).getJSONArray("member_info");
            for (int k = 0; k < memb_info.size(); k++) {
                String type = memb_info.getJSONObject(k).getString("type");
                Integer uv = memb_info.getJSONObject(k).getInteger("uv");
                if (uv == null) {
                    uv = 0;
                }
                if (type.equals("CUSTOMER")) {
                    cust_uv += uv;
                }
                if (type.equals("OMNI_CHANNEL")) {
                    channel_uv += uv;
                }
                if (type.equals("PAID")) {
                    pay_uv += uv;
                }
            }

        }
        Map<String, Integer> result = new HashMap<>();
        result.put("cust_uv", cust_uv);
        result.put("channel_uv", channel_uv);
        result.put("pay_uv", pay_uv);
        return result;
    }

    /**获取历史客流中昨日的到店客流总数(所有店给周同比)**/
    public Map<String, Double> getweek_count(String cycle_type, String month, Long shop_id, Long shop_id_01) throws Exception {
        JSONArray trend_list1 = md.historyShopTrendV3(cycle_type, month, shop_id_01).getJSONArray("trend_list");
        double uv1 = 0;
        double uv2 = 0;
        int count1 = trend_list1.size();
        for (int i = 0; i < count1; i++) {
            if (i == count1 - 1) {
                uv1 = trend_list1.getJSONObject(i).getInteger("uv");
            }
            if (i == count1 - 8) {
                uv2 = trend_list1.getJSONObject(i).getInteger("uv");
            }
        }
        double uv3 = 0;
        double uv4 = 0;
        if(shop_id == null){
            uv3 = 0;
            uv4 = 0;
        }else {
            JSONArray trend_list2 = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");

            int count3 = trend_list2.size();
            for (int i = 0; i < count1; i++) {
                if (i == count3 - 1) {
                    uv3 = trend_list2.getJSONObject(i).getInteger("uv");
                }
                if (i == count3 - 8) {
                    uv4 = trend_list2.getJSONObject(i).getInteger("uv");
                }
            }
        }

        Map<String, Double> result = new HashMap<>();
        result.put("uv1", uv1);
        result.put("uv2", uv2);
        result.put("uv3", uv3);
        result.put("uv4", uv4);
        return result;

    }
    /**区域关注度中的数据获取**/
    public Map<String, Integer>  getRegionData(JSONArray list) throws Exception {
        Integer total_pv = null;
        Integer total_uv = null;
        Integer total_stay_time = null;
        Integer pv = null;
        Integer uv = null;
        Integer stay_time = null;
        for(int i=0;i<list.size();i++){
            total_pv = list.getJSONObject(i).getInteger("total_pv");
            total_uv = list.getJSONObject(i).getInteger("total_uv");
            total_stay_time = list.getJSONObject(i).getInteger("total_stay_time");

            JSONArray region_day_list = list.getJSONObject(i).getJSONArray("region_data_day_list");
             pv = region_day_list.getJSONObject(i).getInteger("pv");
             uv = region_day_list.getJSONObject(i).getInteger("uv");
             stay_time = region_day_list.getJSONObject(i).getInteger("stay_time");
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("total_pv", total_pv);
        result.put("total_uv", total_uv);
        result.put("total_stay_time", total_stay_time);
        result.put("pv", pv);
        result.put("uv", uv);
        result.put("stay_time", stay_time);
        return result;
    }

    /**区域关注度中的数据获取**/
    public Map<String, Integer>  getRegionData1(JSONArray list) throws Exception {
        Integer total_pv = 0;
        Integer total_uv = 0;
        Integer pvs = 0;
        Integer uvs = 0;
        for(int i=0;i<list.size();i++) {
            total_pv = list.getJSONObject(i).getInteger("total_pv");
            total_uv = list.getJSONObject(i).getInteger("total_uv");
            JSONArray region_day_list = list.getJSONObject(i).getJSONArray("region_data_day_list");
            for (int j = 0; j < region_day_list.size(); j++) {
                Integer pv = region_day_list.getJSONObject(j).getInteger("pv");
                Integer uv = region_day_list.getJSONObject(j).getInteger("uv");
                pvs += pv;
                uvs += uv;
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("total_pv", total_pv);
        result.put("total_uv", total_uv);
        result.put("pv", pvs);
        result.put("uv", uvs);
        return result;
    }
}


