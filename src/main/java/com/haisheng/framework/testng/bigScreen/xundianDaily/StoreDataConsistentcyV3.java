package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import jdk.nashorn.internal.ir.IdentNode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StoreDataConsistentcyV3 extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil Md = StoreScenarioUtil.getInstance();
    String cycle_type = "RECENT_THIRTY";
    String month = "";
    long shop_id = 4116;
    String district_code = "";
    //    String shop_type = "[\"NORMAL\"]";
    String shop_type = "[]";
    Integer page = 1;
    Integer size = 50;


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";


        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");

        commonConfig.dingHook = DingWebhook.DAILY_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13581630214","18810332354", "15084928847"};
        //13436941018 吕雪晴
        //17610248107 廖祥茹
        //15084928847 黄青青
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //18672733045 高凯
        //15898182672 华成裕
        //18810332354 刘峤



        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + Md);

        Md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * ====================今日到访人数<=今天各个时间段内到访人数的累计======================
     */
    @Test
    public void realTimeTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = Md.realTimeShopTotalV3((long) 4116l).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");

            //获取今日各个时间段内到访得人数且相加
            JSONArray eTlist = Md.realTimeShopPvV3((long) 4116l).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer todayUv = eTlist.getJSONObject(i).getInteger("today_uv");
                todayUv = todayUv != null ? todayUv : 0;
                count += todayUv;

            }
            Preconditions.checkArgument(uv <= count, "今日到访人数=" + uv + "今天各个时间段内到访人数的累计=" + count);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("今日到访人数<=今天各个时间段内到访人数的累计");
        }

    }


    /**
     * ====================过店客群总人次==各个门的过店人次之和======================
     */
    @Test
    public void passByTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int pv2 = pass_by.get("pv2");
            int uv1 = pass_by.get("uv1");
            int uv2 = pass_by.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "过店客群总人数=" + uv1 + "各个门的过店人次之数=" + uv2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("过店客群总人次==各个门的过店人次之和|过店客群总人数==各个门的过店人次之数");
        }

    }

    //获取各个客群漏斗数据的方法
    private Map<String, Integer> getCount(JSONArray ldlist, String type) {
        int pv1 = 0;
        int uv1 = 0;
        int pv2 = 0;
        int uv2 = 0;
        for (int i = 0; i < ldlist.size(); i++) {
            JSONObject jsonObject = ldlist.getJSONObject(i);
            if (jsonObject != null) {
                String type1 = jsonObject.getString("type");
                if (type.equals(type1)) {
                    pv1 = jsonObject.getInteger("pv");
                    uv1 = jsonObject.getInteger("uv");
                    JSONArray detail = jsonObject.getJSONArray("detail");
                    if (CollectionUtils.isNotEmpty(detail)) {
                        for (int j = 0; j < detail.size(); j++) {
                            Integer pv = detail.getJSONObject(j).getInteger("pv");
                            Integer uv = detail.getJSONObject(j).getInteger("uv");
                            pv2 += pv != null ? pv : 0;
                            uv2 += uv != null ? uv : 0;
                        }
                    } else {
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
     * ====================进店客群总人次==各个门的进店人次之和======================
     */
    @Test
    public void enterTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv1 = enter.get("pv1");
            int pv2 = enter.get("pv2");
            int uv1 = enter.get("uv1");
            int uv2 = enter.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "进店客群总人次=" + pv1 + "各个门的进店人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "进店客群总人数=" + uv1 + "各个门的进店人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("进店客群总人次==各个门的进店人次之和");
        }

    }

    /**
     * ====================兴趣客群总人次==各个门的进店人次之和======================
     */
    @Test
    public void interestTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv1 = interest.get("pv1");
            int pv2 = interest.get("pv2");
            int uv1 = interest.get("uv1");
            int uv2 = interest.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "兴趣客群总人次=" + pv1 + "各个门的兴趣人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "兴趣客群总人数=" + uv1 + "各个门的兴趣人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("兴趣客群总人次==各个门的进店人次之和");
        }

    }

    /**
     * ====================交易客群总人次==会员+非会员的交易pv之和======================
     */
    @Test
    public void dealTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int pv1 = deal.get("pv1");
            int pv2 = deal.get("pv2");
            int uv1 = deal.get("uv1");
            int uv2 = deal.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "交客客群总人次=" + pv1 + "会员+非会员的人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "交易客群总人数=" + uv1 + "会员+非会员的人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("交易客群总人次==会员+非会员的交易pv之和");
        }

    }

    /**
     * ====================过店客群pv>=兴趣客群pv>=进店客群pv======================
     */
    @Test
    public void enterInterPass() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv2 = interest.get("pv1");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv3 = enter.get("pv1");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int pv4 = enter.get("pv1");
            boolean result = false;
            if (pv1 <= pv2 && pv2 <= pv3 && pv3 <= pv4) {
                result = true;
            }

            Preconditions.checkArgument(result = true, "过店客群" + pv1 + "兴趣客群pv" + pv2 + "进店客群" + pv3 + "进店客群" + pv4);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("过店客群pv>=兴趣客群pv>=进店客群pv");
        }

    }
//    /**
//     *
//     * ====================高于同城门店的比例(pv)==同城总门店数量-该门店的排行/同城总门店数量======================
//     * */
//    @Test(dataProvider = "AREA_CODE", dataProviderClass = StoreScenarioUtil.class)
//    public void highAveragePv(String area_code ) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        int value1 = 0;
//        try {
//            String district_code = area_code;
//            int idNum=0;
//            String  link_re = "";
//            JSONArray sameList = Md.realTimeShopTotalV3(shop_id).getJSONArray("list");
//            for(int j=0;j<sameList.size();j++){
//                String type = sameList.getJSONObject(j).getString("type");
//                //获取pv的高于多少的值
//                if(type.equals("same_city_shop_average_pv")){
//                    link_re = sameList.getJSONObject(j).getString("link_relative");
//
//                }
////                if(type=="same_type_shop_average_uv"){
////                    String  link_re_uv = sameList.getJSONObject(j).getString("link_relative");
////                    Double link_re_uv1 = Double.valueOf(link_re_uv.replace("%", ""));
////                    link_re_uv2 = link_re_uv1;
////
////                }
//
//            }
//
//            Integer total = Md.patrolShopRealV3(district_code,shop_type,page,size).getInteger("total");//某一城市的门店数量
//            JSONArray list = Md.patrolShopRealV3(district_code,shop_type,page,size).getJSONArray("list");//某一城市的门店列表
//            for(int i=0;i<list.size();i++){
//                Integer shop_id= list.getJSONObject(i).getInteger("id");
//                if(shop_id == 4116){
//                    idNum=i+1;
//                }
//            }
//            String highScale = "";
//            int sales=total-idNum;
//            if(sales*100%total== 0 ){
//                DecimalFormat decimalFormat = new DecimalFormat("0%");
//                 highScale = decimalFormat.format(new BigDecimal(total-idNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP));
//            }else {
//                DecimalFormat decimalFormat = new DecimalFormat("0.00%");
//                 highScale = decimalFormat.format(new BigDecimal(total-idNum).divide(new BigDecimal(total),4,BigDecimal.ROUND_HALF_UP));
//            }
//
//
//
//
//            Preconditions.checkArgument(highScale.equals(link_re),"高于同城门店的比例(pv)=" + link_re + "同城总门店数量-该门店的排行/同城总门店数量=" + highScale);
////            Preconditions.checkArgument(link_re_uv2== highScale,"高于同类门店的比例=" + link_re_uv2 + "同类型总门店数量-该门店的排行/同类型总门店数量=" + highScale);
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("高于同城门店的比例(pv)==同城总门店数量-该门店的排行/同城总门店数量");
//        }
//
//    }
//    /**
//     *
//     * ====================高于同类型门店的比例(pv)==同类型总门店数量-该门店的排行/同类型总门店数量======================
//     * */
//    @Test(dataProvider = "AREA_TYPE", dataProviderClass = StoreScenarioUtil.class)
//    public void highAveragePvs(String area_type ) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        int value1 = 0;
//        try {
//            String shop_type = area_type;
//            int idNum=0;
//            String  link_re = "";
//            JSONArray sameList = Md.realTimeShopTotalV3(shop_id).getJSONArray("list");
//            for(int j=0;j<sameList.size();j++){
//                String type = sameList.getJSONObject(j).getString("type");
//                //获取pv的高于多少的值
//                if(type.equals("same_type_shop_average_pv")){
//                    link_re = sameList.getJSONObject(j).getString("link_relative");
//
//                }
////                if(type=="same_type_shop_average_uv"){
////                    String  link_re_uv = sameList.getJSONObject(j).getString("link_relative");
////                    Double link_re_uv1 = Double.valueOf(link_re_uv.replace("%", ""));
////                    link_re_uv2 = link_re_uv1;
////
////                }
//
//            }
//
//            Integer total = Md.patrolShopRealV3(district_code,shop_type,page,size).getInteger("total");//某一类型的门店数量
//            JSONArray list = Md.patrolShopRealV3(district_code,shop_type,page,size).getJSONArray("list");//某一类型的门店列表
//            for(int i=0;i<list.size();i++){
//                Integer shop_id= list.getJSONObject(i).getInteger("id");
//                if(shop_id == 4116){
//                    idNum=i+1;
//                }
//            }
//            String highScale = "";
//            int sales=total-idNum;
//            if(sales*100%total== 0 ){
//                DecimalFormat decimalFormat = new DecimalFormat("0%");
//                highScale = decimalFormat.format(new BigDecimal(total-idNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP));
//            }else {
//                DecimalFormat decimalFormat = new DecimalFormat("0.00%");
//                highScale = decimalFormat.format(new BigDecimal(total-idNum).divide(new BigDecimal(total),4,BigDecimal.ROUND_HALF_UP));
//            }
//
//
//
//            Preconditions.checkArgument(highScale.equals(link_re),"高于同城门店的比例(pv)=" + link_re + "同城总门店数量-该门店的排行/同城总门店数量=" + highScale);
////            Preconditions.checkArgument(link_re_uv2== highScale,"高于同类门店的比例=" + link_re_uv2 + "同类型总门店数量-该门店的排行/同类型总门店数量=" + highScale);
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("高于同城门店的比例(pv)==同城总门店数量-该门店的排行/同城总门店数量");
//        }
//
//    }
//
//
//    /**
//     *
//     * ====================同类门店平均到访人次==同一类型的门店当日累计的Pv/同一类型门店的数量======================
//     * */
//    @Test
//    public void sameAveragePv() {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            int value1 = 0;
//            JSONArray sameList = Md.realTimeShopTotalV3(shop_id).getJSONArray("list");
//
//            for(int i=0;i<sameList.size();i++){
//                JSONObject jsonObject=sameList.getJSONObject(i);
//                String type =jsonObject.getString("type");
//                if(type.equals("same_type_shop_average_pv")){
//                    value1 =jsonObject.getInteger("value");
//
//                }
//            }
//            int count=0;
//            String shop_type = "[\"NORMAL\"]";
//            Integer total = Md.patrolShopRealV3(district_code,shop_type,page,size).getInteger("total");//某一类型的门店数量
//            JSONArray storeList = Md.patrolShopRealV3(district_code,shop_type,page,size).getJSONArray("list");
//            for(int j=0;j<storeList.size();j++){
//                Integer  realtime_pv = storeList.getJSONObject(j).getInteger("realtime_pv");
//                Integer id = storeList.getJSONObject(j).getInteger("id");
//                if(realtime_pv != null){
//                    count += realtime_pv;
//                }
//
//            }
//            int value2 = count/total;
//
//            int result = Math.abs(value1-value2);
//            Preconditions.checkArgument(result<=1,"同类门店平均到访人次=" + value1 + "同一类型的门店当日累计的Pv/同一类型门店的数量=" + value2);
////            Preconditions.checkArgument(value1== value2,"同市门店平均到访人次=" + value1 + "同市的门店当日累计的Pv/同一类型门店的数量=" + value2);
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("同类门店平均到访人次==同一类型的门店当日累计的Pv/同一类型门店的数量");
//        }
//
//    }
//    /**
//     *
//     * ====================同市门店平均到访人次==同一类型城市当日累积的pv/同一类型门店的数量======================
//     * */
//    @Test
//    public void sameAveragePvs() {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            int value1 = 0;
//            JSONArray sameList = Md.realTimeShopTotalV3(shop_id).getJSONArray("list");
//
//            for(int i=0;i<sameList.size();i++){
//                JSONObject jsonObject=sameList.getJSONObject(i);
//                String type =jsonObject.getString("type");
//                if(type.equals("same_city_shop_average_pv")){
//                    value1 =jsonObject.getInteger("value");
//
//                }
//            }
//            int count=0;
//            String district_code = "110000";
//            Integer total = Md.patrolShopRealV3(district_code,shop_type,page,size).getInteger("total");//某一城市的门店数量
//            JSONArray storeList = Md.patrolShopRealV3(district_code,shop_type,page,size).getJSONArray("list");
//            for(int j=0;j<storeList.size();j++){
//                Integer  realtime_pv = storeList.getJSONObject(j).getInteger("realtime_pv");
//                Integer id = storeList.getJSONObject(j).getInteger("id");
//                if(realtime_pv != null){
//                    count += realtime_pv;
//                }
//
//            }
//            int value2 = count/total;
//            int result = Math.abs(value1-value2);
//            Preconditions.checkArgument(result<=1,"同市门店平均到访人次=" + value1 + "同市的门店当日累计的Pv/同一类型门店的数量=" + value2);
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("同市门店平均到访人次==同一类型城市当日累积的pv/同一类型门店的数量");
//        }
//
//    }

    /**
     * ====================消费者到店趋势中各天pv累计==到店客群总人次======================
     */
    @Test()
    public void mpvTotals() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            int pvValues = 0;
            //获取到店趋势数据
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            for (int i = 0; i < trend_list.size(); i++) {
                JSONObject jsonObject = trend_list.getJSONObject(i);
                if (jsonObject != null) {
                    Integer pv = jsonObject.getInteger("pv");
                    if (pv != null) {
                        pvValues += pv;//到店趋势中每天的pv累加
                    }

                }
            }

            //获取进店客群总人次
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "ENTER");
            int value1 = pass_by.get("pv1");


            Preconditions.checkArgument(pvValues == value1, "消费者到店趋势中各天pv累计=" + pvValues + "到店客群总人数=" + value1);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("消费者到店趋势中各天pv累计==到店客群总人次");

        }

    }

    /**
     * ====================各个客群总人次==到店时段分布中各个时段pv累计======================
     */
    @Test()
    public void mpvTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
//            int count = 0;
//            //获取到店趋势数据
//            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
//            for (int i = 0; i < trend_list.size(); i++) {
//                JSONObject jsonObject = trend_list.getJSONObject(i);
//                if (jsonObject != null) {
//                    Integer pv = jsonObject.getInteger("pv");
//                    if (pv != null) {
//                        count++;//不为空的数据的数量
//                    }
//
//                }
//            }

            //获取交易客群总人次
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int value1 = deal.get("pv1");


            //获取进店客群总人次
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value2 = enter.get("pv1");

            //获取兴趣客群总人次
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value3 = interest.get("pv1");


            //获取过店客群总人次
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int value4 = pass_by.get("pv1");


            int times1 = 0;
            int times2 = 0;
            int times3 = 0;
            int times4 = 0;
            //获取各个客群时段分布的总和
            int count =30;
            JSONArray showList = Md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
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
            int result1 = Math.abs(value1 - times1);
            int result2 = Math.abs(value2 - times2);
            int result3 = Math.abs(value3 - times3);
            int result4 = Math.abs(value4 - times4);
            Preconditions.checkArgument(result1 <= 720, "交易客群总人次=" + value1 + "时段分布中各个时段交易pv累计=" + times1);
            Preconditions.checkArgument(result2 <= 720, "进店客群总人次=" + value1 + "时段分布中各个时段进店pv累计=" + times2);
            Preconditions.checkArgument(result3 <= 720, "兴趣客群总人次=" + value1 + "时段分布中各个时段兴趣pv累计=" + times3);
            Preconditions.checkArgument(result4 <= 720, "过店客群总人次=" + value1 + "时段分布中各个时段过店pv累计=" + times4);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("到店客群总人次==到店时段分布中各个时段pv累计");

        }

    }

    /**
     * ====================吸引率==兴趣客群pv/过店客群pv|进店率==进店客群pv/兴趣客群pv======================
     */
    @Test
    public void attractRate() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //获取过店客群总人次
            JSONObject res = Md.historyShopConversionV3(shop_id, cycle_type, month);
            String interestRate =res.getString("interest_percentage");
            String enterRate =res.getString("enter_percentage");
            JSONArray ldlist = res.getJSONArray("list");


            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int value1 = pass_by.get("pv1");//过店客群PV
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value2 = interest.get("pv1");//兴趣客群PV
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value3 = enter.get("pv1");//进店客群PV
            DecimalFormat decimalFormat = new DecimalFormat("0.00%");
            String rate = "";

            if (value2 / value1 == 1) {
                rate = "100%";
            } else {
                rate = decimalFormat.format(new BigDecimal(value2).divide(new BigDecimal(value1), 4, BigDecimal.ROUND_HALF_UP));//吸引率计算
            }
            String rate1 = "";
            if (value3 / value2 == 1) {
                rate1 = "100%";
            } else {
                rate1 = decimalFormat.format(new BigDecimal(value3).divide(new BigDecimal(value2), 4, BigDecimal.ROUND_HALF_UP)); //进店率计算
            }
            boolean reslut = false;
            if (value1 >= value2 && value2 >= value3) {
                reslut = true;
            }


            Preconditions.checkArgument((interestRate.equals(rate)), "吸引率=" + interestRate + "兴趣客群pv/过店客群=" + rate);
            Preconditions.checkArgument((enterRate.equals(rate1)), "进店率=" + interestRate + "进店客群pv/兴趣客群pv=" + rate);
            Preconditions.checkArgument((reslut = true), "过店客群pv>=兴趣客群pv>=进店客群不成立");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("吸引率==兴趣客群pv/过店客群pv");
        }
    }

    /**
     * ====================日均客流==所选时间段内的日均客流uv======================
     */
    @Test
    public void averageFlowTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            int values = 0;
            int values1 = 0;//值不为Null的个数，求平均值时用
            JSONObject res = Md.historyShopTrendsV3(cycle_type, month, shop_id);
            int averageFlow = res.getInteger("average_daily_passenger_flow");//获取每天得日均客流
            JSONArray trendList = res.getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer value = trendList.getJSONObject(i).getInteger("uv");
                if (value != null && value != 0) {
                    values += value;
                }
                if (value != null) {
                    values1++;
                }

            }
            int values2 = values / values1;
            int result = Math.abs(averageFlow - values2);
            Preconditions.checkArgument(result <= 1, "日均客流=" + averageFlow + "所选时间段内的日均客流pv=" + values2);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流pv");
        }

    }

    /**
     * ====================各个年龄段的男性比例累计和==男性总比例======================
     */
    @Test
    public void manSexScale() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //过店客群的各个年龄段的男性比例累计和
            double count = 0;
            double count1 = 0;
            JSONObject enter = Md.historyShopAgeV3(shop_id, cycle_type, month).getJSONObject("enter");
            JSONArray ageList = Md.historyShopAgeV3(shop_id, cycle_type, month).getJSONObject("enter").getJSONArray("list");

            String male_ratio_str = enter.getString("male_ratio_str");
            Double result1 = Double.valueOf(male_ratio_str.replace("%", ""));

            String female_ratio_str = enter.getString("female_ratio_str");
            Double result2 = Double.valueOf(female_ratio_str.replace("%", ""));

            for (int i = 0; i < ageList.size(); i++) {
                String male_percent = ageList.getJSONObject(i).getString("male_percent");
                String female_percent = ageList.getJSONObject(i).getString("female_percent");
                if (male_percent != null) {
                    Double result = Double.valueOf(male_percent.replace("%", ""));//将string格式转换成douBLE
                    count += result;//各个年龄段得男生比例累加
                }
                if (female_percent != null) {
                    Double result3 = Double.valueOf(female_percent.replace("%", ""));//将string格式转换成douBLE
                    count1 += result3;//各个年龄段得女生比例累加
                }
            }
            //获取某一年龄段的比例
            String age_group_percent = ageList.getJSONObject(0).getString("age_group_percent");
            Double resultOther = Double.valueOf(age_group_percent.replace("%", ""));

            //获取该年龄段的男性比例
            String male_percent = ageList.getJSONObject(0).getString("male_percent");
            Double maleResult = Double.valueOf(male_percent.replace("%", ""));

            //获取该年龄段的女性比例
            String female_percent = ageList.getJSONObject(0).getString("female_percent");
            Double femaleResult = Double.valueOf(female_percent.replace("%", ""));
            double theResult = maleResult + femaleResult;

            double resultAll = count + count1;

            double theError1 = Math.abs(result1 - count);
            double theError2 = Math.abs(result2 - count1);
            double theError3 = Math.abs(resultOther - theResult);
            Preconditions.checkArgument(theError1 < 1, "男性总比例=" + result1 + "各个年龄段的男性比例累计和=" + count);
            Preconditions.checkArgument(theError2 < 1, "女性总比例=" + result2 + "各个年龄段的女性比例累计和=" + count1);
            Preconditions.checkArgument(resultAll <= 101 || resultAll >= 99, "男性比例+女性比例" + resultAll + "不在99-100的范围间");
            Preconditions.checkArgument(theError3 < 1, "某一年龄段的比例" + resultOther + "该年龄段男性比例+该年龄段女性比例" + resultAll);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("各个年龄段的男性比例累计和==男性总比例|各个年龄段的女性比例累计和==女性总比例|男性比例+女性比例==100|某一年龄段的比例==该年龄段男性比例+该年龄段女性比例");
        }

    }

    /**
     * ====================门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）==实时客流中的门店基本信息======================
     */
    @Test
    public void storeInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String district_code = "110000";
            Integer page = 1;
            Integer size = 50;
            JSONObject jsonObject = new JSONObject();
            boolean check = false;
            JSONArray storeList = Md.patrolShopPageV3(district_code, page, size).getJSONArray("list");
            long shop_id = 4116;
            JSONObject res = Md.shopDetailV3(shop_id);

            if (storeList.contains(res)) {
                check = true;
            }

            int id = storeList.getJSONObject(0).getInteger("id");


            Preconditions.checkArgument((check = true), "门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）不等于实时客流中的门店基本信息");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）等于实时客流中的门店基本信息");
        }

    }

    /**
     * ====================所选周期的顾客总人数<=所有门店各天顾客之和======================
     */
    @Test
    public void memberAllTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {


            Integer customer_uv = 0;
            Integer omni_uv = 0;
            Integer paid_uv = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = Md.historyShopMemberCountV3(cycle_type, month).getJSONArray("trend_list");
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


            String shop_type = "";
            String shop_name = "";
            String shop_manager = "";
            String member_type = "";
            Integer member_type_order = null;
            JSONArray member_list = Md.shopPageMemberV3(district_code, shop_type, shop_name, shop_manager, member_type, member_type_order, page, size).getJSONArray("list");
            int cust_uv = 0;
            int channel_uv = 0;
            int pay_uv = 0;
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

            Preconditions.checkArgument((customer_uv == cust_uv), "累计的顾客总人数" + customer_uv + "=累计的顾客之和=" + cust_uv);
            Preconditions.checkArgument((omni_uv == channel_uv), "累计的全渠道会员总人数" + omni_uv + "=累计的30天全渠道会员之和=" + channel_uv);
            Preconditions.checkArgument((paid_uv == pay_uv), "累计的付费总人数" + paid_uv + "=累计的付费会员之和=" + pay_uv);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("累计顾客总人数==所有门店顾客之和|累计全渠道会员总人数==所有门店全渠道会员之和|累计付费会员总人数==所有门店付费会员之和");
        }

    }

    /**
     * ====================累计顾客的总数（所有店）==前天的累计客户+今天新增的（顾客+全渠道会员+付费会员）之和======================
     */
    @Test
    public void memberTotalCount() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {

            Integer customer_uv = 0;
            Integer customer_uv_new_today = 0;
            Integer customer_uv_01 = 0;
            Integer omni_uv_today = 0;
            Integer paid_uv_today = 0;
            Integer omni_uv_total = 0;
            Integer omni_uv_total_01 = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = Md.historyShopMemberCountV3(cycle_type, month).getJSONArray("trend_list");
            for (int i = 0; i < trend_list.size(); i++) {

                //获取昨天的累计客户总数,今天新增的顾客、全渠道会员、付费会员
                if (i - trend_list.size() == -1) {
                    customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                    customer_uv_new_today = trend_list.getJSONObject(i).getInteger("customer_uv_new_today");
                    omni_uv_today = trend_list.getJSONObject(i).getInteger("omni_channel_uv_new_today");
                    paid_uv_today = trend_list.getJSONObject(i).getInteger("paid_uv_new_today");
                }

                //获取前天的累计顾客总数
                if(i - trend_list.size() == -2){
                    customer_uv_01 = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total_01 = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                }

            }
            int qa_customer_uv = customer_uv_01 +customer_uv_new_today + omni_uv_today +paid_uv_today;
            int qa_omni_uv =  omni_uv_total_01 + omni_uv_today ;




            Preconditions.checkArgument((qa_customer_uv == customer_uv), "累计的顾客总人数" + customer_uv + "!=前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和=" + qa_customer_uv);
            Preconditions.checkArgument((qa_omni_uv == omni_uv_total), "累计的全渠道总人数" + omni_uv_total + "!=前天的累计全渠道会员+今天新增的（全渠道会员）之和=" + qa_omni_uv);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("（所有门店）累计顾客的总数==前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和||累计的全渠道总人数===前天的累计全渠道会员+昨天新增的（全渠道会员）之和");
        }

    }

    /**
     * ====================累计顾客的总数(单店)==前天的累计客户+今天新增的（顾客+全渠道会员+付费会员）之和======================
     */
    @Test
    public void shop_memberTotalCount() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {

            Integer customer_uv = 0;
            Integer customer_uv_new_today = 0;
            Integer customer_uv_01 = 0;
            Integer omni_uv_today = 0;
            Integer paid_uv_today = 0;
            Integer omni_uv_total = 0;
            Integer omni_uv_total_01 = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = Md.historyShopMemberV3(shop_id,cycle_type, month).getJSONArray("trend_list");
            for (int i = 0; i < trend_list.size(); i++) {

                //获取昨天的累计客户总数,今天新增的顾客、全渠道会员、付费会员
                if (i - trend_list.size() == -1) {
                    customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                    customer_uv_new_today = trend_list.getJSONObject(i).getInteger("customer_uv_new_today");
                    omni_uv_today = trend_list.getJSONObject(i).getInteger("omni_channel_uv_new_today");
                    paid_uv_today = trend_list.getJSONObject(i).getInteger("paid_uv_new_today");

//                    if (customer_uv == null && omni_uv_today == null && paid_uv_today == null && customer_uv_new_today == null && omni_uv_total == null) {
//                        customer_uv = 0;
//                        paid_uv_today = 0;
//                        omni_uv_today = 0;
//                        customer_uv_new_today = 0;
//                        omni_uv_total = 0;
//                    }
                }

                //获取前天的累计顾客总数
                if(i - trend_list.size() == -2){
                    customer_uv_01 = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                    omni_uv_total_01 = trend_list.getJSONObject(i).getInteger("omni_channel_uv_total");
                }

            }
            int qa_customer_uv = customer_uv_01 +customer_uv_new_today + omni_uv_today +paid_uv_today;
            int qa_omni_uv =  omni_uv_total_01 + omni_uv_today ;




            Preconditions.checkArgument((qa_customer_uv == customer_uv), "累计的顾客总人数" + customer_uv + "!=前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和=" + qa_customer_uv  +"。报错门店shop_id="+shop_id);
            Preconditions.checkArgument((qa_omni_uv == omni_uv_total), "累计的全渠道总人数" + omni_uv_total + "!=前天的累计全渠道会员+昨天新增的（全渠道会员）之和=" + qa_omni_uv +"。报错门店shop_id="+shop_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("（单个门店）累计顾客的总数==前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和||累计的全渠道总人数===前天的累计全渠道会员+昨天新增的（全渠道会员）之和");
        }

    }

    /**
     * ====================实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv======================
     */
    @Test
    public void yesterdayTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {


            //获取昨天日各个时间段内到访得人次且相加
            JSONArray eTlist = Md.realTimeShopPvV3((long) shop_id).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer yesterdayPv = eTlist.getJSONObject(i).getInteger("yesterday_pv");
                yesterdayPv = yesterdayPv != null ? yesterdayPv : 0;
                count += yesterdayPv;

            }

            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int pv = 0;
            int count1 = trend_list.size();
            for (int i = 0; i < count1; i++) {
                if (i == count1 - 1) {
                    pv = trend_list.getJSONObject(i).getInteger("pv");
                }
            }
            Preconditions.checkArgument((count == pv), "实时客流中，昨日到访各个时段的pv之和" + count + ">历史客流中截至日期的的pv=" + pv + "。报错门店的shopId=" + shop_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv");
        }

    }

    /**
     * ====================实时客流监控======================
     */
    @Test
    public void surveDataReal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray list = Md.realTimeShopPvV3(shop_id).getJSONArray("list");
            int today_pv = 0;
            for (int i = 0; i < list.size(); i++) {
                Integer count = list.getJSONObject(i).getInteger("today_pv");
                if (count != null) {
                    today_pv += count;
                }

            }
            Preconditions.checkArgument(today_pv < 800 && today_pv > 50, "实时到店人次超过800或低于了50，现在pv=" + today_pv + "需线上确认数据是否有异常" + "。报错门店的shopId=" + shop_id);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("监控今日实时人次是否异常，小于800高于50为正常");
        }

    }


    /**
     * ====================uv与pv之间的比例要保持在1：4的范围间========================
     */
    @Test
    public void uvWithPvScrole() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = Md.realTimeShopTotalV3((long) shop_id).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");
            Integer pv = iPvlist.getJSONObject(0).getInteger("value");
            int scrole = 0;
            if (pv != 0 && uv != 0) {
                scrole = pv / uv;
            } else {
                uv = uv + 1;
                pv = pv + 1;
                scrole = pv / uv;
            }
            Preconditions.checkArgument((scrole <= 4), "uv=" + uv + "远远小于pv，不在1：4的范围间 pv=" + pv + "。报错门店的shopId=" + shop_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("uv与pv之间的比例要保持在1：4的范围间" + "门店shopId=");
        }

    }


    /**
     * ====================云中客中累计不为0，事件也不能为0========================
     */
    @Test
    public void custmerWithThing() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray trend_list = Md.historyShopMemberV3(shop_id, cycle_type, month).getJSONArray("trend_list");
            int count1 = trend_list.size();
            int customer_uv_total = 0;
            int customer_uv_new_today = 0;
            for (int i = 0; i < count1; i++) {
                if (i == count1 - 1) {
                    customer_uv_total = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                }
            }
            Integer total = Md.memberTotalListV3(shop_id, page, size).getInteger("total");


            Preconditions.checkArgument((customer_uv_total != 0 && total != 0), "累计顾客为：" + customer_uv_total + "事件为" + total + "。报错门店的shopId=" + shop_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("累计顾客与事件是否异常，有累计顾客但无事件或有事件无累计顾客");
        }

    }

    /**
     * ====================客户详情累计交易的次数==留痕事件中门店下单的次数|||累计到店的数据==留痕事件中进店次数+门店下单的次数========================
     */
    @Test
    public void custInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
//            customer_id
            //根据门店id获取customer_id
            JSONObject response = Md.memberTotalListV3(shop_id, page, size);
            int total = response.getInteger("total");

            JSONArray list = response.getJSONArray("list");
            boolean listResult = false;
            Integer enter_total = 0;
            Integer total_sum = 0;
            int deal_times = 0;
            Integer deal = 0;
            int enterDealAll_sum = 0;
            String customer_id = "";
            String face_url = "";
            String member_type = "";
            String member_id = "";
            JSONArray list1=  Md.memberTotalListV3(shop_id, page, 50).getJSONArray("list");
            for (int j = 0; j < list1.size(); j++) {

                customer_id = list1.getJSONObject(j).getString("customer_id");
                member_type = list1.getJSONObject(j).getString("member_type");
                if (member_type.equals("OMNI_CHANNEL")) {
                    member_id = list1.getJSONObject(j).getString("member_id");
                    if(member_id == null){
                        member_id = "";
                    }
                    Preconditions.checkArgument(!StringUtils.isEmpty(member_id), "人物ID为："+customer_id +"的全渠道会员的会员ID为空"+member_id +"。  报错门店的shopId=" + shop_id);
                }else if(member_type.equals("CUSTOMER")){
                    member_id = list1.getJSONObject(j).getString("member_id");
                    if(member_id == null){
                        member_id = "";
                    }else {
                        Preconditions.checkArgument(StringUtils.isEmpty(member_id), "人物ID为："+customer_id +"的会员ID不为空，会员ID为："+member_id +"。  报错门店的shopId=" + shop_id);
                    }
                }

                total_sum = Md.memberDetail(shop_id, customer_id, page, size).getInteger("total");//留痕事件数量
                if(total_sum==null){
                    total_sum=0;
                }

                int t = CommonUtil.pageTurning(total_sum, 50);
                for (int l = 1; l < t; l++){
                    JSONObject res = Md.memberDetail(shop_id, customer_id, l, size);
                    enter_total = res.getInteger("total_visit_times");//累计到店次数
                    if(enter_total == null){
                        enter_total=0;
                    }
                    deal = res.getInteger("total_deal_times");//获取累计交易的次数
                    if(deal == null){
                        deal=0;
                    }
                    //交易次数+进店次数
                    enterDealAll_sum = enter_total + deal;
                    //或者每个人物的脸部图片地址
                    face_url = res.getString("face_url");
                    if(face_url==null){
                        face_url="";
                    }

                    Preconditions.checkArgument(!StringUtils.isEmpty(face_url), "人物ID为:"+customer_id+"的半身照为空" +"。报错门店的shopId=" + shop_id);
                    Preconditions.checkArgument(enterDealAll_sum == total_sum, "人物ID为："+customer_id +"的【累计进店次数】+【累计交易次数】=" + enterDealAll_sum +  "不等于留痕事件总条数=" + total_sum + "。报错门店的shopId=" + shop_id);


                    //获取事件中门店下单的次数
                    JSONArray thingsList = res.getJSONArray("list");

                    if(thingsList.size()==0 && enter_total !=0 || deal !=0){
                        listResult=true;
                    }else {
                        for (int k = 0; k < thingsList.size(); k++) {
                            String mark = thingsList.getJSONObject(k).getString("mark");
                            if (mark.equals("门店下单")) {
                                deal_times += 1;
                            }
                            Preconditions.checkArgument(deal_times == deal, "人物ID为:"+customer_id+"累计交易次数：" + deal + "不等于留痕事件中门店下单次数" + deal_times + "。报错门店的shopId=" + shop_id);
                        }
                    }
//                    Preconditions.checkArgument(listResult, "人物ID为:"+customer_id+"人物详情的留痕事件为空 " +"但是该人物的进店次数为："+enter_total+"交易次数为："+deal+"。报错门店的shopId=" + shop_id);

                }

            }
//            int s = CommonUtil.pageTurning(total, 50);
//            for (int i = 1; i < s; i++) {
//              JSONArray list1=  Md.memberTotalListV3(shop_id, i, 50).getJSONArray("list");
//
//                for (int j = 0; j < list1.size(); j++) {
//                     member_type =list1.getJSONObject(j).getString("member_type");
//                     if(member_type.equals("OMNI_CHANNEL")){
//                         member_id = list1.getJSONObject(j).getString("member_id");
//                     }
//                     customer_id = list1.getJSONObject(j).getString("customer_id");
//                    enter_total = Md.memberDetail(shop_id, customer_id, page, size).getInteger("total_visit_times");//累计到店次数
//                    total_sum = Md.memberDetail(shop_id, customer_id, page, size).getInteger("total");//获取所有留痕事件的和
//                    deal = Md.memberDetail(shop_id, customer_id, page, size).getInteger("total_deal_times");//获取累计交易的次数
//
//                    int t = CommonUtil.pageTurning(enter_total, 50);
//                    for (int l = 1; l < t; l++){
//                        JSONArray thingsList = Md.memberDetail(shop_id, customer_id, l, 50).getJSONArray("list");//获取事件中门店下单的次数
//                        face_url = Md.memberDetail(shop_id, customer_id, l, 50).getString("face_url");//或者每个人物的脸部图片地址
//                        for (int k = 0; k < thingsList.size(); k++) {
//                            String mark = thingsList.getJSONObject(k).getString("mark");
//                            if (mark.equals("门店下单")) {
//                                deal_times += 1;
//                            }
//                        }
//                    }
//
//
//                }
//            }






        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("客户详情累计交易的次数==留痕事件中门店下单的次数||累计到店的数据==留痕事件中进店次数+门店下单的次数||门店客户的照片不能为空||全渠道会员一定有会员ID||顾客没有会员ID");
        }

    }

    /**
     * ====================选择自然月的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForMo() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String cycle_type = "";
            String month = "2020-08";
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = Md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }

            Preconditions.checkArgument((uv_Sum != 0), "历史客流-自然月9月的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认自然月9月的数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-自然月9月的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-自然月9月的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("历史客流-选择自然月9月的数据是否正常");
        }

    }

    /**
     * ====================选择最近7天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForS() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String cycle_type = "RECENT_SEVEN";
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = Md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }

            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近7天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近7天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近7天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("历史客流-选择最近7天的数据是否正常");
        }

    }

    /**
     * ====================选择最近14天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForF() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String cycle_type = "RECENT_FOURTEEN";
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = Md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }
            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近14天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近14天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近14天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("历史客流-选择最近14天的数据是否正常");
        }

    }

    /**
     * ====================选择最近30天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForT() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String cycle_type = "RECENT_THIRTY";
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = Md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }


            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近30天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近30天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近30天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近30天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近30天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("历史客流-选择最近30天的数据是否正常");
        }

    }

    /**
     * ====================选择最近60天的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForSix() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String cycle_type = "RECENT_SIXTY";
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = Md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }


            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近60天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近60天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近60天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近60天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近60天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("选择最近60天的数据是否正常");
        }

    }

}
