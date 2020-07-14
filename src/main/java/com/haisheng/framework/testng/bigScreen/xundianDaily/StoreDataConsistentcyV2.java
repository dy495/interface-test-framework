package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.apache.commons.collections.CollectionUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StoreDataConsistentcyV2 extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil Md = StoreScenarioUtil.getInstance();
    String cycle_type = "RECENT_SEVEN";
    String month = "2020-07";
    long shop_id = 4116;




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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + Md);

        Md.login("yuexiu@test.com","f5b3e737510f31b88eb2d4b5d0cd2fb4");


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
     * ====================今日到访人次==今天各个时间段内到访人次的累计======================
     * */
    @Test
    public void realTimeTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取今日实时得到访人数pv
            JSONArray iPvlist = Md.realTimeTotal((long) 4116l).getJSONArray("list");
            Integer pv = iPvlist.getJSONObject(0).getInteger("value");

            //获取今日各个时间段内到访得人数且相加
            JSONArray eTlist = Md.StoreRealTimePv((long)4116l).getJSONArray("list");
            int count = 0;
            for(int i=0;i<eTlist.size();i++){
                Integer todaypv = eTlist.getJSONObject(i).getInteger("today");
                    todaypv = todaypv  != null ?  todaypv : 0;
                    count += todaypv;

            }
            Preconditions.checkArgument(pv== count,"今日到访人次=" + pv + "今天各个时间段内到访人次的累计=" + count);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("今日到访人次==今天各个时间段内到访人次的累计");
        }

    }

    /**
     *
     * ====================过店客群总人次==各个门的过店人次之和======================
     * */
    @Test
    public void passByTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取过点客群总人次


            JSONArray ldlist = Md.StoreHistoryConversion(cycle_type,month,shop_id).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
//            int interest = getCount(ldlist, "INTEREST");
//            int enter = getCount(ldlist, "ENTER");
            int value1 = pass_by.get("value1");
            int value2 = pass_by.get("value2");
            Preconditions.checkArgument(value1== value2,"过店客群总人次=" + value1 + "各个门的过店人次之和=" + value2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("过店客群总人次==各个门的过店人次之和");
        }

    }
    //获取各个客群漏斗数据的方法
    private Map<String, Integer> getCount(JSONArray ldlist, String type){
        int value1 = 0;
        int value2 = 0;
        for(int i=0;i<ldlist.size();i++){
            JSONObject jsonObject = ldlist.getJSONObject(i);
            if(jsonObject != null){
                String type1 =jsonObject.getString("type");
                if(type.equals(type1)){
                    value1 = jsonObject.getInteger("value");
                    JSONArray detail =jsonObject.getJSONArray("detail");
                    if (CollectionUtils.isNotEmpty(detail)) {
                        for (int j = 0; j < detail.size(); j++) {
                            Integer values = detail.getJSONObject(j).getInteger("value");
                            value2 += values != null ? values : 0;
                        }
                    }

                }
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("value1", value1);
        result.put("value2", value2);
        return result;
    }
    /**
     *
     * ====================进店客群总人次==各个门的进店人次之和======================
     * */
    @Test
    public void enterTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.StoreHistoryConversion(cycle_type,month,shop_id).getJSONArray("list");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value1 = enter.get("value1");
            int value2 = enter.get("value2");
            Preconditions.checkArgument(value1== value2,"进店客群总人次=" + value1 + "各个门的进店人次之和=" + value2);

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
     * ====================兴趣客群总人次==各个门的进店人次之和======================
     * */
    @Test
    public void interestTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.StoreHistoryConversion(cycle_type,month,shop_id).getJSONArray("list");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value1 = interest.get("value1");
            int value2 = interest.get("value2");
            Preconditions.checkArgument(value1== value2,"兴趣客群总人次=" + value1 + "各个门的进店人次之和=" + value2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("兴趣客群总人次==各个门的进店人次之和");
        }

    }

    /**
     *
     * ====================消费者到店趋势中各天pv累计==到店客群总人次======================
     * */
    @Test
    public void mpvTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int values = 0;
            //获取到店趋势数据
            JSONArray trend_list = Md.StoreHistoryTrend(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trend_list.size();i++){
                JSONObject jsonObject = trend_list.getJSONObject(i);
                if(jsonObject != null){
                    Integer value = jsonObject.getInteger("value");
                    values += value;//到店趋势中每天的pv累加
                }
            }

            //获取到店客群总人次
            JSONArray ldlist = Md.StoreHistoryConversion(cycle_type,month,shop_id).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "ENTER");
            int value1 = pass_by.get("value1");


            int times1 = 0;
            //获取到店时段分布的总和
            JSONArray showList = Md.StoreHistoryHourdata(cycle_type,month,shop_id).getJSONArray("list");
            for(int i=0;i<showList.size();i++){
                Integer times = showList.getInteger(i);
                if(times !=null){
                    times1 +=times;
                }
            }



            Preconditions.checkArgument(values== value1,"消费者到店趋势中各天pv累计=" + values + "到店客群总人次=" + value1);
            Preconditions.checkArgument(values== value1,"到店客群总人次=" + value1 + "到店时段分布中各个时段pv累计=" + times1);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("消费者到店趋势中各天pv累计==到店客群总人次|到店客群总人次==到店时段分布中各个时段pv累计");

        }

    }

    /**
     *
     * ====================吸引率==兴趣客群pv/过店客群pv|进店率==进店客群pv/兴趣客群pv======================
     * */
    @Test
    public void attractRate() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取过店客群总人次
            String interestRate = Md.StoreHistoryConversion(cycle_type,month,shop_id).getString("interest_percentage");
            String enterRate = Md.StoreHistoryConversion(cycle_type,month,shop_id).getString("enter_percentage");
            JSONArray ldlist = Md.StoreHistoryConversion(cycle_type,month,shop_id).getJSONArray("list");


            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int value1 = pass_by.get("value1");//过店客群PV
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value2 = interest.get("value1");//兴趣客群PV
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value3 = enter.get("value1");//进店客群PV
            DecimalFormat decimalFormat = new DecimalFormat("0.00%");
            String rate = decimalFormat.format(new BigDecimal(value1).divide(new BigDecimal(value2),4,BigDecimal.ROUND_HALF_UP));//吸引率计算
            String rate1= decimalFormat.format(new BigDecimal(value3).divide(new BigDecimal(value2),4,BigDecimal.ROUND_HALF_UP)); //进店率计算
            boolean reslut=false;
            if(value1 >= value2 && value2>= value3){
                reslut = true;
            }


            Preconditions.checkArgument((interestRate == rate),"吸引率=" + interestRate + "兴趣客群pv/过店客群=" + rate);
            Preconditions.checkArgument((enterRate == rate1),"进店率=" + interestRate + "进店客群pv/兴趣客群pv=" + rate);
            Preconditions.checkArgument((reslut = true),"过店客群pv>=兴趣客群pv>=进店客群不成立");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("吸引率==兴趣客群pv/过店客群pv");
        }
    }

    /**
     *
     * ====================日均客流==所选时间段内的日均客流pv======================
     * */
    @Test
    public void averageFlowTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int values = 0;
            int averageFlow = Md.StoreHistoryTrend(cycle_type,month,shop_id).getInteger("average_daily_passenger_flow");//获取每天得日均客流
            JSONArray  trendList =  Md.StoreHistoryTrend(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trendList.size();i++){
                int value = trendList.getJSONObject(i).getInteger("value");
                values +=value;
            }
            int values1 = values/trendList.size();
            Preconditions.checkArgument(averageFlow== values1,"日均客流=" + averageFlow + "所选时间段内的日均客流pv=" + values1);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流pv");
        }

    }

    /**
     *
     * ====================门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）==实时客流中的门店基本信息======================
     * */
    @Test
    public void storeInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            String district_code="110000";
            Integer page = 1;
            Integer size = 50;
            JSONObject jsonObject = new JSONObject();
            boolean check = false;
            JSONArray storeList = Md.StoreShopPage(district_code,page,size).getJSONArray("list");
            long shop_id = 4116;
            JSONObject res = Md.StoreShopDetail(shop_id);

            if( storeList.contains(res)){
                check = true;
            }

            int id = storeList.getJSONObject(0).getInteger("id");




            Preconditions.checkArgument((check = true),"门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）不等于实时客流中的门店基本信息");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流pv");
        }

    }
}
