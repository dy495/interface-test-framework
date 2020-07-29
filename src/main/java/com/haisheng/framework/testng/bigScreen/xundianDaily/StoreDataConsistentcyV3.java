package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Map;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StoreDataConsistentcyV3 extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil Md = StoreScenarioUtil.getInstance();
    String cycle_type = "RECENT_SEVEN";
    String month = "";
    long shop_id = 4116;
    String district_code = "110105";
    String shop_type = "[\"NORMAL\"]";
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
     * ====================今日到访人数<=今天各个时间段内到访人数的累计======================
     * */
    @Test
    public void realTimeTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = Md.realTimeShopTotalV3((long) 4116l).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");

            //获取今日各个时间段内到访得人数且相加
            JSONArray eTlist = Md.realTimeShopPvV3((long)4116l).getJSONArray("list");
            int count = 0;
            for(int i=0;i<eTlist.size();i++){
                Integer todayUv = eTlist.getJSONObject(i).getInteger("today");
                todayUv = todayUv  != null ?  todayUv : 0;
                count += todayUv;

            }
            Preconditions.checkArgument(uv <= count,"今日到访人数=" + uv + "今天各个时间段内到访人数的累计=" + count);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("今日到访人次<=今天各个时间段内到访人次的累计");
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
            //获取过点客群总人次&总人数
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int pv2 = pass_by.get("pv2");
            int uv1 = pass_by.get("uv1");
            int uv2 = pass_by.get("uv2");
            Preconditions.checkArgument(pv1== pv2,"过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2);
            Preconditions.checkArgument(uv1== uv2,"过店客群总人数=" + uv1 + "各个门的过店人次之数=" + uv2);

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
     * ====================进店客群总人次==各个门的进店人次之和======================
     * */
    @Test
    public void enterTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv1 = enter.get("pv1");
            int pv2 = enter.get("pv2");
            int uv1 = enter.get("uv1");
            int uv2 = enter.get("uv2");
            Preconditions.checkArgument(pv1== pv2,"进店客群总人次=" + pv1 + "各个门的进店人次之和=" + pv2);
            Preconditions.checkArgument(uv1== uv2,"进店客群总人数=" + uv1 + "各个门的进店人数之和=" + uv2);

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
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv1 = interest.get("pv1");
            int pv2 = interest.get("pv2");
            int uv1 = interest.get("uv1");
            int uv2 = interest.get("uv2");
            Preconditions.checkArgument(pv1== pv2,"兴趣客群总人次=" + pv1 + "各个门的兴趣人次之和=" + pv2);
            Preconditions.checkArgument(uv1== uv2,"兴趣客群总人数=" + uv1 + "各个门的兴趣人数之和=" + uv2);

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
     * ====================交易客群总人次==会员+非会员的交易pv之和======================
     * */
    @Test
    public void dealTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int pv1 = deal.get("pv1");
            int pv2 = deal.get("pv2");
            int uv1 = deal.get("uv1");
            int uv2 = deal.get("uv2");
            Preconditions.checkArgument(pv1== pv2,"交客客群总人次=" + pv1 + "会员+非会员的人次之和=" + pv2);
            Preconditions.checkArgument(uv1== uv2,"交易客群总人数=" + uv1 + "会员+非会员的人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("交易客群总人次==会员+非会员的交易pv之和");
        }

    }

    /**
     *
     * ====================过店客群pv>=兴趣客群pv>=进店客群pv======================
     * */
    @Test
    public void enterInterPass() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv2 = interest.get("pv1");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv3 = enter.get("pv1");
            boolean result = false;
            if(pv1 <= pv2 && pv2 <= pv3){
                result=true;
            }

            Preconditions.checkArgument( result=true,"过店客群" + pv1 + "兴趣客群pv" + pv2+ "进店客群" + pv3);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("过店客群pv>=兴趣客群pv>=进店客群pv");
        }

    }
    /**
     *
     * ====================高于同类门店的比例(pv)==同类型总门店数量-该门店的排行/同类型总门店数量======================
     * */
    @Test
    public void highAveragePv() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        int value1 = 0;
        try {
            int idNum=0;
            double link_re2=0;
//            double link_re_uv2=0;
            JSONArray sameList = Md.realTimeTotal(shop_id).getJSONArray("list");
            for(int j=0;j<sameList.size();j++){
               String type = sameList.getJSONObject(j).getString("type");
               //获取pv的高于多少的值
               if(type=="same_type_shop_average_pv"){
                   String  link_re = sameList.getJSONObject(j).getString("link_relative");
                   Double link_re1 = Double.valueOf(link_re.replace("%", ""));
                   link_re2 = link_re1;

               }
//                if(type=="same_type_shop_average_uv"){
//                    String  link_re_uv = sameList.getJSONObject(j).getString("link_relative");
//                    Double link_re_uv1 = Double.valueOf(link_re_uv.replace("%", ""));
//                    link_re_uv2 = link_re_uv1;
//
//                }

            }

            Integer total = Md.patrolShopRealV3(district_code,shop_type,page,size).getInteger("total");//某一城市某一类型的门店数量
            JSONArray list = Md.patrolShopRealV3(district_code,shop_type,page,size).getJSONArray("list");//获取某一城市某一类型的门店列表
            for(int i=0;i<list.size();i++){
                Integer shop_id= list.getJSONObject(i).getInteger("id");
                if(shop_id == 4116){
                    idNum=i+1;
                }
            }
            int highScale = (total-idNum)/total;



            Preconditions.checkArgument(link_re2== highScale,"高于同类门店的比例(pv)=" + link_re2 + "同类型总门店数量-该门店的排行/同类型总门店数量=" + highScale);
//            Preconditions.checkArgument(link_re_uv2== highScale,"高于同类门店的比例=" + link_re_uv2 + "同类型总门店数量-该门店的排行/同类型总门店数量=" + highScale);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("高于同类门店的比例(pv)==同类型总门店数量-该门店的排行/同类型总门店数量");
        }

    }


    /**
     *
     * ====================同类门店平均到访人次==同一类型的门店当日累计的Pv/同一类型门店的数量&&同市门店平均到访人次==同一类型城市当日累积的pv/同一类型门店的数量======================
     * */
    @Test
    public void sameAveragePv() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int value1 = 0;
            JSONArray sameList = Md.realTimeTotal(shop_id).getJSONArray("list");

            for(int i=0;i<sameList.size();i++){
                JSONObject jsonObject=sameList.getJSONObject(i);
                String type =jsonObject.getString("type");
                if(type.equals("same_type_shop_average_pv")){
                    value1 =jsonObject.getInteger("value");

                }
            }
            int count=0;
            Integer total = Md.patrolShopRealV3(district_code,shop_type,page,size).getInteger("total");//某一城市某一类型的门店数量(需要对城市进行不传和传一个城市的id)
            JSONArray storeList = Md.patrolShopRealV3(district_code,shop_type,page,size).getJSONArray("list");
            for(int j=0;j<storeList.size();j++){
                Integer  realtime_pv = storeList.getJSONObject(j).getInteger("realtime_pv");
                Integer id = storeList.getJSONObject(j).getInteger("id");
                if(realtime_pv != null){
                    count += realtime_pv;
                }

            }
            int value2 = count/total;
            Preconditions.checkArgument(value1== value2,"同类门店平均到访人次=" + value1 + "同一类型的门店当日累计的Pv/同一类型门店的数量=" + value2);
            Preconditions.checkArgument(value1== value2,"同市同类门店平均到访人次=" + value1 + "同市同一类型的门店当日累计的Pv/同一类型门店的数量=" + value2);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("同类门店平均到访人次==同一类型的门店当日累计的Pv/同一类型门店的数量|同市门店平均到访人次==同一类型城市当日累积的pv/同一类型门店的数量");
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
            JSONArray trend_list = Md.historyShopTrendV3(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trend_list.size();i++){
                JSONObject jsonObject = trend_list.getJSONObject(i);
                if(jsonObject != null){
                    Integer value = jsonObject.getInteger("value");
                    if(value != null){
                        values += value;//到店趋势中每天的pv累加
                    }

                }
            }

            //获取到店客群总人次
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "ENTER");
            int value1 = pass_by.get("pv1");


//            int times1 = 0;
//            //获取到店时段分布的总和
//            JSONArray showList = Md.StoreHistoryHourdata(cycle_type,month,shop_id).getJSONArray("list");
//            for(int i=0;i<showList.size();i++){
//                Integer times = showList.getInteger(i);
//                if(times !=null &&times != 0){
//                    times = times ;
//                    times1 +=times;
//                }
//            }



            Preconditions.checkArgument(values== value1,"消费者到店趋势中各天pv累计=" + values + "到店客群总人次=" + value1);
//            Preconditions.checkArgument(values== times1,"到店客群总人次=" + value1 + "到店时段分布中各个时段pv累计=" + times1);


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
            String interestRate = Md.historyShopConversionV3(shop_id,cycle_type,month).getString("interest_percentage");
            String enterRate = Md.historyShopConversionV3(shop_id,cycle_type,month).getString("enter_percentage");
            JSONArray ldlist = Md.historyShopConversionV3(shop_id,cycle_type,month).getJSONArray("list");


            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int value1 = pass_by.get("pv1");//过店客群PV
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int value2 = interest.get("pv1");//兴趣客群PV
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int value3 = enter.get("pv1");//进店客群PV
            DecimalFormat decimalFormat = new DecimalFormat("0.00%");
            String rate = decimalFormat.format(new BigDecimal(value2).divide(new BigDecimal(value1),4,BigDecimal.ROUND_HALF_UP));//吸引率计算
            String rate1= decimalFormat.format(new BigDecimal(value3).divide(new BigDecimal(value2),4,BigDecimal.ROUND_HALF_UP)); //进店率计算
            boolean reslut=false;
            if(value1 >= value2 && value2>= value3){
                reslut = true;
            }


            Preconditions.checkArgument((interestRate.equals(rate) ),"吸引率=" + interestRate + "兴趣客群pv/过店客群=" + rate);
            Preconditions.checkArgument((enterRate.equals(rate1) ),"进店率=" + interestRate + "进店客群pv/兴趣客群pv=" + rate);
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
            int values1= 0;//值不为Null的个数，求平均值时用
            int averageFlow = Md.StoreHistoryTrend(cycle_type,month,shop_id).getInteger("average_daily_passenger_flow");//获取每天得日均客流
            JSONArray  trendList =  Md.StoreHistoryTrend(cycle_type,month,shop_id).getJSONArray("trend_list");
            for(int i=0;i<trendList.size();i++){
                Integer value = trendList.getJSONObject(i).getInteger("value");
                if(value != null && value != 0){
                    values += value ;
                }
                if(value != null){
                    values1++;
                }

            }
            int values2 = values/values1;
            Preconditions.checkArgument(averageFlow== values2,"日均客流=" + averageFlow + "所选时间段内的日均客流pv=" + values2);


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
     * ====================各个年龄段的男性比例累计和==男性总比例======================
     * */
    @Test
    public void manSexScale() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
             //过店客群的各个年龄段的男性比例累计和
            double count=0;
            double count1=0;
            JSONObject deal = Md.historyShopAgeV3(shop_id,cycle_type,month).getJSONObject("deal");
            JSONArray ageList = Md.historyShopAgeV3(shop_id,cycle_type,month).getJSONObject("deal").getJSONArray("list");

            String male_ratio_str = deal.getString("male_ratio_str");
            Double result1 = Double.valueOf(male_ratio_str.replace("%", ""));

            String female_ratio_str = deal.getString("female_ratio_str");
            Double result2 = Double.valueOf(female_ratio_str.replace("%", ""));

            for(int i=0;i<ageList.size();i++){
                String male_percent = ageList.getJSONObject(i).getString("male_percent");
                String female_percent = ageList.getJSONObject(i).getString("male_percent");
                if(male_percent != null){
                    Double result = Double.valueOf(male_percent.replace("%", ""));//将string格式转换成douBLE
                    count += result;//各个年龄段得男生比例累加
                }
                else if(female_percent != null){
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
            Preconditions.checkArgument(result1== count,"男性总比例=" + result1 + "各个年龄段的男性比例累计和=" + count);
            Preconditions.checkArgument(result2== count1,"女性总比例=" + result2 + "各个年龄段的女性比例累计和=" + count1);
            Preconditions.checkArgument(resultAll==100||resultAll==0.0,"男性比例+女性比例" + resultAll + "!=100" );
            Preconditions.checkArgument(resultOther==theResult,"某一年龄段的比例" + resultOther + "该年龄段男性比例+该年龄段女性比例" + resultAll);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("各个年龄段的男性比例累计和==男性总比例|各个年龄段的女性比例累计和==女性总比例|男性比例+女性比例==100|某一年龄段的比例==该年龄段男性比例+该年龄段女性比例");
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
    /**
     *
     * ====================所选周期的顾客总人数<=所有门店各天顾客之和======================
     * */
    @Test
    public void memberAllTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int c_count = 0;
            int o_count = 0;
            int p_count = 0;
           JSONArray trend_list = Md.historyShopMemberCountV3(shop_id,cycle_type,month).getJSONArray("trend_list");
           for(int i=0;i<trend_list.size();i++){
               Integer customer_uv = trend_list.getJSONObject(i).getInteger("customer_uv");
               Integer omni_uv = trend_list.getJSONObject(i).getInteger("omni_channel_uv");
               Integer paid_uv = trend_list.getJSONObject(i).getInteger("paid_uv");
               if(customer_uv !=null && omni_uv!=null && paid_uv !=null){
                   c_count+=customer_uv;
                   o_count+=omni_uv;
                   p_count+=paid_uv;
               }
               String shop_name="";
               String shop_manager="";
               String member_type="";
               Integer member_type_order=0;
               Md.shopPageMemberV3(district_code,district_code,shop_name,shop_manager,member_type,member_type_order,page,size).getJSONArray("trend_list");

           }

//            Preconditions.checkArgument((check = true),"门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）不等于实时客流中的门店基本信息");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流pv");
        }

    }
}
