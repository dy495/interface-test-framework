package com.haisheng.framework.testng.bigScreen.xundian.caseonline.jkqs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundian.util.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.apache.commons.collections.CollectionUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StoreDataConsistentcyLz extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    String cycle_type = "RECENT_THIRTY";
    String month = "";
    long shop_id = 28647l;
    String district_code = "";
    //    String shop_type = "[\"NORMAL\"]";
    String shop_type = "[]";
    Integer page = 1;
    Integer size = 50;





    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "青青";


        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 线上");

        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672","18513118484", "18810332354", "15084928847"};

        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + md);

        md.login("jiekeqiongsi@jiekeqiongsi.com","d6160551e6a52ebf6b5066eedcf0cbd9");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     *
     * ====================今日到访人数<=今天各个时间段内到访人数的累计======================
     * */
    @Test()
    public void realTimeTotal( ) {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = md.realTimeShopTotalV3((long) shop_id).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");

            //获取今日各个时间段内到访得人数且相加
            JSONArray eTlist = md.realTimeShopPvV3((long)shop_id).getJSONArray("list");
            int count = 0;
            for(int i=0;i<eTlist.size();i++){
                Integer todayUv = eTlist.getJSONObject(i).getInteger("today_uv");
                todayUv = todayUv  != null ?  todayUv : 0;
                count += todayUv;

            }
            Preconditions.checkArgument(uv <= count,"今日到访人数=" + uv + "今天各个时间段内到访人数的累计=" + count+"。报错门店的shopId="+shop_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("今日到访人数<=今天各个时间段内到访人数的累计");
        }

    }

    //获取各个客群漏斗数据的方法
    private Map<String, Integer> getCount(JSONArray ldlist, String type){
        int pv1 = 0;
        int uv1 = 0;
        int pv2 = 0;
        int uv2 = 0;
        for(int i=0;i<ldlist.size();i++){
            JSONObject jsonObject = ldlist.getJSONObject(i);
            if(jsonObject != null){
                String type1 =jsonObject.getString("type");
                if(type.equals(type1)){
                    pv1 = jsonObject.getInteger("pv");
                    uv1 = jsonObject.getInteger("uv");
                    JSONArray detail =jsonObject.getJSONArray("detail");
                    if (CollectionUtils.isNotEmpty(detail)) {
                        for (int j = 0; j < detail.size(); j++) {
                            Integer pv = detail.getJSONObject(j).getInteger("pv");
                            Integer uv = detail.getJSONObject(j).getInteger("uv");
                            pv2 += pv != null ? pv : 0;
                            uv2 += uv != null ? uv : 0;
                        }
                    }else {
                        pv2 = pv1;
                        uv2 = uv1;
                    }

                }
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("pv1", pv1);
        result.put("uv1", uv1);
        result.put("pv2", pv2);
        result.put("uv2", uv2);
        return result;
    }


    /**
     *
     * ====================日均客流==所选时间段内的日均客流uv======================
     * */
    @Test()
    public void averageFlowTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int values = 0;
            int values1= 0;//值不为Null的个数，求平均值时用
            int averageFlow = md.historyShopTrendsV3(cycle_type,month,shop_id).getInteger("average_daily_passenger_flow");//获取每天得日均客流
            JSONArray  trendList =  md.historyShopTrendsV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trendList.size();i++){
                Integer value = trendList.getJSONObject(i).getInteger("uv");
                if(value != null && value != 0){
                    values += value ;
                }
                    values1++;


            }
            int values2 = values/values1;
            int result = Math.abs(averageFlow-values2);
            Preconditions.checkArgument(result <= 1,"日均客流=" + averageFlow + "所选时间段内的日均客流uv=" + values2+"。报错门店的shopId="+shop_id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流uv");
        }

    }




    /**
     //     *
     //     * ====================百果园线上昨日客流监控======================
     * */
    @Test()
    public void  surveDataTrend(){
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
             JSONArray trend_list =  md.historyShopTrendV3("RECENT_SEVEN","",shop_id).getJSONArray("trend_list");
             int yestPv = trend_list.getJSONObject(6).getInteger("pv");
             int yestUv = trend_list.getJSONObject(6).getInteger("uv");
             String yestDate = trend_list.getJSONObject(6).getString("date");

             Preconditions.checkArgument(yestPv < 800 && yestPv >10 ,"杰克琼斯门店ID:"+shop_id+"在"+yestDate+"到店人次超过800或低于了10，pv="+yestPv+"需线上确认数据是否有异常");
             Preconditions.checkArgument(yestUv < 700 && yestUv >5 ,"杰克琼斯门店ID:"+shop_id+"在"+yestDate+"到店人次超过700或低于了5，pv="+yestUv+"需线上确认数据是否有异常");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("监控百果园昨日pv/uv是否异常");
        }

    }

    /**
     *
     * ====================选择自然月的数据展示是否正常========================
     * */
    @Test()
    public void dataSurveillanceForMo() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            String cycle_type = "";
            String month = "2021-05";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv =0;
            for(int i=0;i<trend_list.size();i++){
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if(uv !=null ){
                    uv_Sum += uv;
                }
            }

            Preconditions.checkArgument(( uv_Sum != 0),"历史客流-自然月5月的数据相加等于"+uv_Sum+"。报错门店的shopId="+shop_id+"请线上确认自然月5月的数据为0是否为正常，");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择自然月5月的数据是否正常");
        }

    }

    /**
     *
     * ====================选择最近7天的数据展示是否正常========================
     * */
    @Test()
    public void dataSurveillanceForS() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String cycle_type = "RECENT_SEVEN";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv =0;
            for(int i=0;i<trend_list.size();i++){
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if(uv !=null ){
                    uv_Sum += uv;
                }
            }
            Preconditions.checkArgument(( uv_Sum != 0),"历史客流-最近7天的数据相加等于"+uv_Sum+"。报错门店的shopId="+shop_id+"请线上确认最近7天数据为0是否为正常，");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择最近7天的数据是否正常");
        }

    }
    /**
     *
     * ====================选择最近14天的数据展示是否正常========================
     * */
    @Test()
    public void dataSurveillanceForF() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            String cycle_type = "RECENT_FOURTEEN";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv =0;
            for(int i=0;i<trend_list.size();i++){
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if(uv !=null ){
                    uv_Sum += uv;
                }

            }

            Preconditions.checkArgument(( uv_Sum != 0),"历史客流-最近14天的数据相加等于"+uv_Sum+"。报错门店的shopId="+shop_id+"请线上确认最近14天数据为0是否为正常，");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择最近14天的数据是否正常");
        }

    }

    /**
     *
     * ====================选择最近30天的数据展示是否正常========================
     * */
    @Test()
    public void dataSurveillanceForT() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            String cycle_type = "RECENT_THIRTY";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv =0;
            for(int i=0;i<trend_list.size();i++){
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if(uv !=null ){
                    uv_Sum += uv;
                }

            }
            Preconditions.checkArgument(( uv_Sum != 0),"历史客流-最近30天的数据相加等于"+uv_Sum+"。报错门店的shopId="+shop_id+"请线上确认最近30天数据为0是否为正常，");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("历史客流-选择最近30天的数据是否正常");
        }

    }
    /**
     *
     * ====================选择最近60天的数据展示是否正常========================
     * */
    @Test()
    public void dataSurveillanceForSix() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            String cycle_type = "RECENT_SIXTY";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv =0;
            for(int i=0;i<trend_list.size();i++){
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if(uv !=null ){
                    uv_Sum += uv;
                }

            }

            Preconditions.checkArgument(( uv_Sum != 0),"历史客流-最近60天的数据相加等于"+uv_Sum+"。报错门店的shopId="+shop_id+"请线上确认最近60天数据为0是否为正常，");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("选择最近60天的数据是否正常");
        }

    }


}
