package com.haisheng.framework.testng.bigScreen.xundianOnline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author : qingqing
 * @date :  2020/07/06 10:00
 */

public class SurveillanceForZDF extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtilOnline Md = StoreScenarioUtilOnline.getInstance();
    long shop_id = 13260;
    int startM=2;

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
     * ====================周大福线上历史客流监控======================
     * */
    @Test
    public void  surveDataHistory(){
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray trendList = Md.historyShopTrendV3("RECENT_SEVEN","",13134l).getJSONArray("trend_list");
            int pv =0;
             for(int i=0;i<trendList.size();i++){
                 if(trendList.size()-i==1){
                    pv = trendList.getJSONObject(i).getInteger("pv");
                 }
             }
            Preconditions.checkArgument(pv < 800 && pv>50 ,"昨日周大福的历史客流到店人次超过/低于了800，现在的pv="+pv+"需线上确认数据是否有异常");
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
            JSONArray list = Md.realTimeShopPvV3(13134l).getJSONArray("list");
            int today_pv =0;
            for(int i=0;i<list.size();i++){
                Integer count =list.getJSONObject(i).getInteger("today_pv");
                if(count != null ){
                    today_pv += count;
                }

            }
            Preconditions.checkArgument(today_pv < 800 ,"今日实时周大福的到店人次超过了800，现在的pv="+today_pv+"需线上确认数据是否有异常");
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
     * ====================周大福线上客群漏斗-兴趣客群两个橱窗的监控======================
     * */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = StoreScenarioUtilOnline.class)
    public void  surveDataInter(String cycle_type){
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            boolean result = false;
            JSONArray list = Md.historyShopConversionV3(13134l,cycle_type,"").getJSONArray("list");
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


}
