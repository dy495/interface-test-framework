package com.haisheng.framework.testng.bigScreen.xundianOnline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
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
 * @date :  2020/07/06 10:00
 */

public class SurveillanceForZDF extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtilOnline Md = StoreScenarioUtilOnline.getInstance();
    long shop_id = 13134;
    int startM=2;
    String cycle_type ="RECENT_SEVEN";
    String month = "";

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

        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 线上");

        commonConfig.dingHook = DingWebhook.ONLINE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13581630214","15084928847"};


        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + Md);

        Md.login("zhoudafu@winsense.ai","d5f396edf97676490dd9e58a7cc60d51");


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
     * ====================实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv======================
     * */
    @Test
    public void yesterdayTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {


            //获取昨天日各个时间段内到访得人次且相加
            JSONArray eTlist = Md.realTimeShopPvV3((long)shop_id).getJSONArray("list");
            int count = 0;
            for(int i=0;i<eTlist.size();i++){
                Integer yesterdayPv = eTlist.getJSONObject(i).getInteger("yesterday_pv");
                yesterdayPv = yesterdayPv  != null ?  yesterdayPv : 0;
                count += yesterdayPv;

            }

            JSONArray trend_list = Md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int pv = 0;
            int count1= trend_list.size();
            for(int i=0;i<count1;i++){
                if(i == count1 - 1){
                    pv = trend_list.getJSONObject(i).getInteger("pv");
                }
            }
            Preconditions.checkArgument((count == pv),"百果园实时客流中，昨日到访各个时段的pv之和" + count + ">历史客流中截至日期的的pv=" + pv+"。报错门店的shopId="+shop_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("百果园实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv");
        }

    }
    /**
     *
     * ====================周大福线上历史客流监控======================
     * */
    @Test
    public void  surveDataHistory(){
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray trendList = Md.historyShopTrendV3("RECENT_SEVEN","",shop_id).getJSONArray("trend_list");
            int pv =0;
             for(int i=0;i<trendList.size();i++){
                 if(trendList.size()-i==1){
                    pv = trendList.getJSONObject(i).getInteger("pv");
                 }
             }
            Preconditions.checkArgument(pv < 800 && pv>50 ,"昨日周大福的历史客流到店人次超过800或低于了50，现在的pv="+pv+"需线上确认数据是否有异常");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("监控周大福人次是否异常，50-800之间为正常");
        }

    }

    /**
     *
     * ====================周大福线上实时客流监控======================
     * */
    @Test
    public void  surveDataReal(){
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray list = Md.realTimeShopPvV3(shop_id).getJSONArray("list");
            int today_pv =0;
            for(int i=0;i<list.size();i++){
                Integer count =list.getJSONObject(i).getInteger("today_pv");
                if(count != null ){
                    today_pv += count;
                }

            }
            Preconditions.checkArgument(today_pv < 800 && today_pv >30 ,"今日周大福的历史客流到店人次超过800或低于了50，现在的pv="+today_pv+"需线上确认数据是否有异常");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("监控周大福今日实时人次是否异常，小于800为正常");
        }

    }

    /**
     *
     * ====================消费者到店趋势中各天pv累计==到店客群总人次======================
     * */
    @Test()
    public void mpvTotals() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int pvValues = 0;
            //获取到店趋势数据
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trend_list.size();i++){
                JSONObject jsonObject = trend_list.getJSONObject(i);
                if(jsonObject != null){
                    Integer pv = jsonObject.getInteger("pv");
                    if(pv != null){
                        pvValues += pv;//到店趋势中每天的pv累加
                    }

                }
            }

            //获取进店客群总人次
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "ENTER");
            int value1 = pass_by.get("pv1");


            Preconditions.checkArgument(pvValues== value1,"消费者到店趋势中各天pv累计=" + pvValues + "到店客群总人次=" + value1);



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("消费者到店趋势中各天pv累计==到店客群总人次"+"。报错门店的shopId="+shop_id);

        }

    }


    /**
     *
     * ====================各个客群总人次==到店时段分布中各个时段pv累计======================
     * */
    @Test()
    public void mpvTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int count = 0;
            //获取到店趋势数据
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trend_list.size();i++){
                JSONObject jsonObject = trend_list.getJSONObject(i);
                if(jsonObject != null){
                    Integer pv = jsonObject.getInteger("pv");
                    if(pv != null){
                        count ++;//不为空的数据的数量
                    }

                }
            }

            //获取交易客群总人次
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int value1 = deal.get("pv1");


            //获取进店客群总人次
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value2 = enter.get("pv1");

            //获取兴趣客群总人次
            Map<String, Integer> interest= this.getCount(ldlist, "INTEREST");
            int value3 = interest.get("pv1");


            //获取过店客群总人次
            Map<String, Integer> pass_by= this.getCount(ldlist, "PASS_BY");
            int value4 = pass_by.get("pv1");


            int times1 = 0;
            int times2= 0;
            int times3 = 0;
            int times4 = 0;
            //获取各个客群时段分布的总和
            JSONArray showList = Md.historyShopHourV3(shop_id,cycle_type,month).getJSONArray("list");
            for(int i=0;i<showList.size();i++){
                Integer deal_pv = showList.getJSONObject(i).getInteger("deal_pv");
                Integer enter_pv = showList.getJSONObject(i).getInteger("enter_pv");
                Integer interest_pv = showList.getJSONObject(i).getInteger("interest_pv");
                Integer pass_pv = showList.getJSONObject(i).getInteger("pass_pv");
                //获取交易客群的各个时段的数据（交易人次*有数据的天数的累加）
                if(deal_pv !=null && deal_pv != 0){
                    int deal_pv1=deal_pv * count;
                    times1 += deal_pv1;
                }
                if(enter_pv !=null &&enter_pv != 0){
                    int enter_pv1 = enter_pv * count;
                    times2 +=enter_pv1;
                }
                if(interest_pv !=null &&interest_pv != 0){
                    int interest_pv1 = interest_pv * count;
                    times3 +=interest_pv1;
                }
                if(pass_pv !=null &&pass_pv != 0){
                    int pass_pv1 = pass_pv * count;
                    times4 +=pass_pv1;
                }
            }
            int result1 = Math.abs(value1-times1);
            int result2 = Math.abs(value2-times2);
            int result3 = Math.abs(value3-times3);
            int result4 = Math.abs(value4-times4);
            Preconditions.checkArgument(result1<=168,"交易客群总人次=" + value1 + "时段分布中各个时段交易pv累计=" + times1);
            Preconditions.checkArgument(result2<=168,"进店客群总人次=" + value2 + "时段分布中各个时段进店pv累计=" + times2);
            Preconditions.checkArgument(result3<=168,"兴趣客群总人次=" + value3 + "时段分布中各个时段兴趣pv累计=" + times3);
            Preconditions.checkArgument(result4<=168,"过店客群总人次=" + value4 + "时段分布中各个时段过店pv累计=" + times4);





        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("各个客群总人次==到店时段分布中各个客群各个时段pv累计"+"。报错门店的shopId="+shop_id);

        }

    }
    /**
     *
     * ====================周大福线上客群漏斗-兴趣客群两个橱窗的监控======================
     * */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = StoreScenarioUtilOnline.class)
    public void  surveDataInter(String cycle_type){
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            boolean result = false;
            JSONArray list = Md.historyShopConversionV3(shop_id,cycle_type,"").getJSONArray("list");
              for(int i=0;i<list.size();i++){
                  String type = list.getJSONObject(i).getString("type");

                  if(type.equals("INTEREST")){
                      JSONArray detail = list.getJSONObject(i).getJSONArray("detail");
                      Integer pvL = detail.getJSONObject(0).getInteger("pv");
                      Integer pvR = detail.getJSONObject(1).getInteger("pv");
                      if(pvL != 0 && pvR != 0 ){
                          result=true;
                      }
                  }

              }
            Preconditions.checkArgument(result = true ,"周大福客群漏斗兴趣客群两侧漏斗不正常");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("监控周大福客群漏斗兴趣客群两个橱窗是否正常");
        }

    }
    /**
     *
     * ====================过店客群总人次==各个门的过店人次之和+兴趣客群======================
     * */
    @Test()
    public void passByTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int pv2 = pass_by.get("pv2");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pvIn1 = interest.get("pv1");
            int uvIn1 = interest.get("uv1");

            int passPv = pv2 +  pvIn1;
            Preconditions.checkArgument(pv1== passPv,"过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2 +"+ 兴趣客群总人次"+pvIn1 +"。报错门店的shopId="+shop_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("过店客群总人次==各个门的过店人次+兴趣客群人次|过店客群总人数==各个门的过店人数+兴趣客群人数");
        }

    }

    /**
     *
     * ====================兴趣客群总人次==各个门的进店人次之和 + 进店的客群======================
     * */
    @Test()
    public void interestTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv1 = interest.get("pv1");
            int pv2 = interest.get("pv2");

            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pvEn1 = enter.get("pv1");

            int intPv = pv2 +  pvEn1;
            Preconditions.checkArgument(pv1== intPv,"兴趣客群总人次=" + pv1 + "各个门的兴趣人次之和=" + pv2 + "+ 进店客群的总人次" + pvEn1+"。报错门店的shopId="+shop_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("兴趣客群总人次==各个门的兴趣人次之和 + 进店客群的总人次|兴趣客群总人数==各个门的兴趣人数之和 + 进店客群的总人数");
        }

    }

    /**
     *
     * ====================进店客群总人次==各个门的进店人次之和======================
     * */
    @Test()
    public void enterTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv1 = enter.get("pv1");
            int pv2 = enter.get("pv2");
            Preconditions.checkArgument(pv1== pv2,"进店客群总人次=" + pv1 + "各个门的进店人次之和=" + pv2+"。报错门店的shopId="+shop_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("进店客群总人次==各个门的进店人次之和");
        }

    }

    /**
     *
     * ====================选择最近x天的数据展示是否正常========================
     * */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = StoreScenarioUtilOnline.class)
    public void dataSurveillanceForF(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer pv =0;
            for(int i=0;i<trend_list.size();i++){
                pv = trend_list.getJSONObject(i).getInteger("pv");
                if(pv !=null ){
                    uv_Sum += pv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");

            //获取客群时段分布
            JSONArray showList = Md.historyShopHourV3(shop_id,cycle_type,month).getJSONArray("list");
            boolean result = false;
            if(showList != null){
                result=true;
            }

            Preconditions.checkArgument(( uv_Sum != 0),"历史客流-"+cycle_type+"的数据相加等于"+uv_Sum+"。报错门店的shopId="+shop_id+"请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument(( pv1 != 0 ),"客群漏斗-"+cycle_type+"数据过店pv等于"+pv1+"。报错门店的shopId="+shop_id+"请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument(( result = true),"客群漏斗-"+cycle_type+"的客群时段分布数据为空"+"。报错门店的shopId="+shop_id+"请线上确认");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("历史客流/客群漏斗-选择\"+cycle_type+\"数据是否正常");
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
}
