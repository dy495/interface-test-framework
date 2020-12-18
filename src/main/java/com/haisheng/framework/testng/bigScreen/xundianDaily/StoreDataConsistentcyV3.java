package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
;import static com.google.common.base.Preconditions.checkArgument;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StoreDataConsistentcyV3 extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    StorePackage mds = StorePackage.getInstance();
    String cycle_type = "RECENT_THIRTY";
    String month = "";
    long shop_id = 4116 ;
    long shop_id_01 = 43072l;
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

        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672","18513118484", "18810332354", "15084928847"};
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

        logger.debug("store " + md);

        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


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
        
        try {
            //获取今日实时得到访人数uv
            JSONArray iPvlist = md.realTimeShopTotalV3((long) 43072l).getJSONArray("list");
            Integer uv = iPvlist.getJSONObject(1).getInteger("value");

            //获取今日各个时间段内到访得人数且相加
            JSONArray eTlist = md.realTimeShopPvV3((long) 43072l).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer todayUv = eTlist.getJSONObject(i).getInteger("today_uv");
                todayUv = todayUv != null ? todayUv : 0;
                count += todayUv;

            }
            Preconditions.checkArgument(uv <= count, "今日到访人数=" + uv + "今天各个时间段内到访人数的累计=" + count);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int pv2 = pass_by.get("pv2");
            int uv1 = pass_by.get("uv1");
            int uv2 = pass_by.get("uv2");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pvIn1 = interest.get("pv1");
            int uvIn1 = interest.get("uv1");

            int passPv = pv2 +  pvIn1;
            int passUv = uv2 + uvIn1;
            Preconditions.checkArgument(pv1== passPv,"过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2 +"+ 兴趣客群总人次"+pvIn1);
            Preconditions.checkArgument(uv1== passUv,"过店客群总人数=" + uv1 + "各个门的过店人次之数=" + uv2 +"兴趣客群总人次"+uvIn1);
//            Preconditions.checkArgument(pv1 == pv2, "过店客群总人次=" + pv1 + "各个门的过店人次之和=" + pv2);
//            Preconditions.checkArgument(uv1 == uv2, "过店客群总人数=" + uv1 + "各个门的过店人次之数=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> enter = this.getCount(ldlist, "ENTER");
            int pv1 = enter.get("pv1");
            int pv2 = enter.get("pv2");
            int uv1 = enter.get("uv1");
            int uv2 = enter.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "进店客群总人次=" + pv1 + "各个门的进店人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "进店客群总人数=" + uv1 + "各个门的进店人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> interest = this.getCount(ldlist, "INTEREST");
            int pv1 = interest.get("pv1");
            int pv2 = interest.get("pv2");
            int uv1 = interest.get("uv1");
            int uv2 = interest.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "兴趣客群总人次=" + pv1 + "各个门的兴趣人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "兴趣客群总人数=" + uv1 + "各个门的兴趣人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> deal = this.getCount(ldlist, "DEAL");
            int pv1 = deal.get("pv1");
            int pv2 = deal.get("pv2");
            int uv1 = deal.get("uv1");
            int uv2 = deal.get("uv2");
            Preconditions.checkArgument(pv1 == pv2, "交客客群总人次=" + pv1 + "会员+非会员的人次之和=" + pv2);
            Preconditions.checkArgument(uv1 == uv2, "交易客群总人数=" + uv1 + "会员+非会员的人数之和=" + uv2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("过店客群pv>=兴趣客群pv>=进店客群pv");
        }

    }


    /**
     * ====================消费者到店趋势中各天pv累计==到店客群总人次======================
     */
    @Test()
    public void mpvTotals() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            //获取到店趋势数据
            int pvValues = mds.getArriveCust(cycle_type, month, shop_id);
            //获取进店客群总人次
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "ENTER");
            int value1 = pass_by.get("pv1");
            Preconditions.checkArgument(pvValues == value1, "消费者到店趋势中各天pv累计=" + pvValues + "到店客群总人次=" + value1);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            //获取交易客群总人次
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
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

            //获取最近30天各个客群时段分布的总和
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> result = mds.getAllCustSum(showList, 30);
            int times1 = result.get("deal");
            int times2 = result.get("enter");
            int times3 = result.get("interest");
            int times4 = result.get("pass");

            //获取两个值之间的误差值为多大，是否是在规定的误差值之间
            int result1 = Math.abs(value1 - times1);
            int result2 = Math.abs(value2 - times2);
            int result3 = Math.abs(value3 - times3);
            int result4 = Math.abs(value4 - times4);
            Preconditions.checkArgument(result1 <= 720, "交易客群总人次=" + value1 + "时段分布中各个时段交易pv累计=" + times1);
            Preconditions.checkArgument(result2 <= 720, "进店客群总人次=" + value1 + "时段分布中各个时段进店pv累计=" + times2);
            Preconditions.checkArgument(result3 <= 720, "兴趣客群总人次=" + value1 + "时段分布中各个时段兴趣pv累计=" + times3);
            Preconditions.checkArgument(result4 <= 720, "过店客群总人次=" + value1 + "时段分布中各个时段过店pv累计=" + times4);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            //获取过店客群总人次
            JSONObject res = md.historyShopConversionV3(shop_id, cycle_type, month);
            String interestRate = res.getString("interest_percentage");
            String enterRate = res.getString("enter_percentage");
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            int values = 0;
            int values1 = 0;//值不为Null的个数，求平均值时用
            JSONObject res = md.historyShopTrendsV3(cycle_type, month, shop_id);
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            //过店客群的各个年龄段的男性比例累计和
            double count = 0;
            double count1 = 0;
            JSONObject enter = md.historyShopAgeV3(shop_id, cycle_type, month).getJSONObject("enter");
            JSONArray ageList = md.historyShopAgeV3(shop_id, cycle_type, month).getJSONObject("enter").getJSONArray("list");

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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            String district_code = "110000";
            Integer page = 1;
            Integer size = 50;
            JSONObject jsonObject = new JSONObject();
            boolean check = false;
            JSONArray storeList = md.patrolShopPageV3(district_code, page, size).getJSONArray("list");
            long shop_id = 4116;
            JSONObject res = md.shopDetailV3(shop_id);

            if (storeList.contains(res)) {
                check = true;
            }
            Preconditions.checkArgument((check = true), "门店列表中的信息（门店名称/门店负责人/负责人手机号/门店位置）不等于实时客流中的门店基本信息");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = md.historyShopMemberCountV3(cycle_type).getJSONArray("trend_list");
            Map<String, Integer> uvs = mds.getEverySum(trend_list);
            Integer customer_uv = uvs.get("customer_uv");
            Integer omni_uv = uvs.get("omni_uv");
            Integer paid_uv = uvs.get("paid_uv");

           //获取所选周期的所有门店各天顾客之和
            String shop_type = "";
            String shop_name = "";
            String shop_manager = "";
            String member_type = "";
            Integer member_type_order = null;
            JSONArray member_list = md.shopPageMemberV3(district_code, shop_type, shop_name, shop_manager, member_type, member_type_order, page, size).getJSONArray("list");
            Map<String, Integer> result = mds.getAllStoreCust_sum(member_list);
            int cust_uv = result.get("cust_uv");
            int channel_uv = result.get("channel_uv");
            int pay_uv = result.get("pay_uv");

            Preconditions.checkArgument((customer_uv == cust_uv), "累计的顾客总人数" + customer_uv + "=累计的顾客之和=" + cust_uv);
            Preconditions.checkArgument((omni_uv == channel_uv), "累计的全渠道会员总人数" + omni_uv + "=累计的30天全渠道会员之和=" + channel_uv);
            Preconditions.checkArgument((paid_uv == pay_uv), "累计的付费总人数" + paid_uv + "=累计的付费会员之和=" + pay_uv);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("累计顾客总人数==所有门店顾客之和|累计全渠道会员总人数==所有门店全渠道会员之和|累计付费会员总人数==所有门店付费会员之和");
        }
    }


///---------------------------------------------------------------4.0门店会员中新增趋势中的数据一致性---------------------------------------------------------------------------

    /**
     * ====================全部店-昨日新增顾客==全部店-新增客户趋势图中昨天的顾客数======================
     */
    @Test
    public void member_both_cust() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            //从新增顾客占比模块中取到昨日新增顾客人数
            JSONArray data_list = md.member_newCount_data().getJSONArray("list");
            int new_uv = mds.getNewCount(data_list,"CUSTOMER");

            //获取客户趋势图中昨日新增的顾客数
            int customer = mds.getYesNew_count( "customer",cycle_type);

            Preconditions.checkArgument((new_uv == customer), "全部店昨日新增顾客" + new_uv + "!=全部店新增客户趋势图中昨天的顾客数=" + customer);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("全部店-昨日新增客户==全部店-新增客户趋势图中昨天的客户数");
        }

    }

    /**
     * ====================全部店-昨日新增全渠道会员==全部店-新增客户趋势图中昨天的全渠道会员数======================
     */
    @Test
    public void member_both_omni() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            //从新增顾客占比模块中取到昨日新增全渠道会员人数
            JSONArray data_list = md.member_newCount_data().getJSONArray("list");
            int new_uv = mds.getNewCount(data_list,"OMNI_CHANNEL");

            //获取客户趋势图中昨日新增的全渠道会员
            int omni_channel = mds.getYesNew_count( "omni_channel",cycle_type);
            Preconditions.checkArgument((new_uv == omni_channel), "全部店昨日新增全渠道会员" + new_uv + "!=全部店新增客户趋势图中昨天的全渠道会员数=" + omni_channel);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("全部店-昨日新增全渠道会员==全部店-新增客户趋势图中昨天的全渠道会员数");
        }

    }
    /**
     * ====================全部店-昨日新增付费会员==全部店-新增客户趋势图中昨天的付费会员数======================
     */
    @Test
    public void member_both_paid() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比模块中取到昨日新增付费会员人数
            JSONArray data_list = md.member_newCount_data().getJSONArray("list");
            int new_uv = mds.getNewCount(data_list,"PAID");
            //获取客户趋势图中昨日新增的付费会员
            int paid = mds.getYesNew_count( "paid",cycle_type);
            Preconditions.checkArgument((new_uv == paid), "全部店昨日新增付费会员" + new_uv + "!=全部店新增客户趋势图中昨天的付费会员数=" + paid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("全部店-昨日新增付费会员==全部店-新增客户趋势图中昨天的付费会员数");
        }

    }


    /**
     * ====================单店昨日新增顾客==单店新增客户趋势图中昨天的顾客数======================
     */
    @Test
    public void member_single_cust() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            //从新增顾客模块中取到昨日新增顾客人数
            JSONArray data_list = md.single_newCount_data(shop_id_01).getJSONArray("list");
            int new_uv = mds.getNewCount(data_list,"CUSTOMER");

            //获取客户趋势图中昨日新增的顾客数
            int customer = mds.getYesNew_count_single( "customer",shop_id_01, cycle_type);
            Preconditions.checkArgument((new_uv == customer), "单店昨日新增顾客" + new_uv + "!=单店新增客户趋势图中昨天的顾客数=" + customer);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("昨日新增客户==新增客户趋势图中昨天的客户数");
        }

    }

    /**
     * ====================单店昨日新增全渠道会员==单店新增客户趋势图中昨天的全渠道会员数======================
     */
    @Test
    public void member_single_omni() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            //从新增顾客占比模块中取到昨日新增全渠道会员人数
            JSONArray data_list = md.single_newCount_data(shop_id_01).getJSONArray("list");
            int new_uv = mds.getNewCount(data_list,"OMNI_CHANNEL");

            //获取客户趋势图中昨日新增的全渠道会员人数
            int omni_channel = mds.getYesNew_count_single( "omni_channel",shop_id_01, cycle_type);
            Preconditions.checkArgument((new_uv == omni_channel), "单店昨日新增全渠道会员" + new_uv + "!=单店新增客户趋势图中昨天的全渠道会员数=" + omni_channel);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日新增全渠道会员==单店新增客户趋势图中昨天的全渠道会员数");
        }

    }

    /**
     * ====================单店昨日新增付费会员==单店新增客户趋势图中昨天的付费会员数======================
     */
    @Test
    public void member_single_paid() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            //从新增顾客占比模块中取到昨日新增全渠道会员人数
            JSONArray data_list = md.single_newCount_data(shop_id).getJSONArray("list");
            int new_uv = mds.getNewCount(data_list,"PAID");

            //获取客户趋势图中昨日新增的全渠道会员人数
            int paid = mds.getYesNew_count_single( "paid",shop_id_01, cycle_type);
            Preconditions.checkArgument((new_uv == paid), "单店昨日新增全渠道会员" + new_uv + "!=单店新增客户趋势图中昨天的全渠道会员数=" + paid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日新增付费会员==单店新增客户趋势图中昨天的付费会员数");
        }

    }

//---------------------------------------------------------------周同比区域------------------------------------------------------------------------
    /**
     * ====================全部店-昨日【新增顾客模块】的周同比==全部店-昨天新增的客户值-全部店【新增客户趋势】上周昨天新增的顾客值/全部店上周昨天新增的客户值======================
     */
    @Test
    public void wenkOnWeek_cust_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得周同比
            String new_uv_yoy = mds.getTransformData("CUSTOMER",  "new_uv_yoy");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> result = mds.getNew_counts(list,"customer");
            int customer1 = result.get("uv1");
            int customer2 = result.get("uv2");

            String ss=  CommonUtil.getPercent(customer1-customer2,customer2,4);
            String week_on_week =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_yoy.equals(week_on_week)), "全部店昨日周同比" + new_uv_yoy + "!=昨天周x新增的客户值-【新增客户趋势】上周昨天新增的顾客值/上周昨天新增的客户值=" + week_on_week);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("全部店昨日周同比==昨天新增的客户值-【新增客户趋势】上周昨天新增的顾客值/上周昨天新增的客户值");
        }

    }


    /**
     * ====================全部店-昨日周同比==全部店-昨天新增的全渠道会员值-全部店【新增客户趋势】上周昨天新增的全渠道会员值/全部店上周昨天新增的全渠道会员值======================
     */
    @Test
    public void wenkOnWeek_omni_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员部分得周同比
            String new_uv_yoy = mds.getTransformData("OMNI_CHANNEL",  "new_uv_yoy");

            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> result = mds.getNew_counts(list,"omni_channel");
            int omni_channel1 = result.get("uv1");
            int omni_channel2 = result.get("uv2");

            String ss=  CommonUtil.getPercent(omni_channel1-omni_channel2,omni_channel2,4);
            String week_on_week =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_yoy.equals(week_on_week)), "全部店-昨日周同比" + new_uv_yoy + "!=昨天周x新增的全渠道会员值-【新增客户趋势】上周昨天新增的全渠道会员值/上周昨天新增的全渠道会员值=" + week_on_week);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("全部店-昨日周同比==昨天新增的全渠道会员值-【新增客户趋势】上周昨天新增的全渠道会员值/上周昨天新增的全渠道会员值");
        }

    }

    /**
     * ====================全部店-昨日周同比==全部店-昨天新增的付费会员值-全部店【新增客户趋势】上周昨天新增的付费会员值/全部店上周昨天新增的付费会员值======================
     */
    @Test
    public void wenkOnWeek_paid_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取付费会员部分得周同比
            String new_uv_yoy = mds.getTransformData("PAID",  "new_uv_yoy");

            //获取客户趋势图中昨日新增的付费会员人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> result = mds.getNew_counts(list,"paid");
            int paid1 = result.get("uv1");
            int paid2 = result.get("uv2");

            String ss=  CommonUtil.getPercent(paid1-paid2,paid2,4);
            String week_on_week =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_yoy.equals(week_on_week)), "全部店-昨日周同比" + new_uv_yoy + "!=昨天周x新增的付费会员值-【新增客户趋势】上周昨天新增的付费会员值/上周昨天新增的付费会员值=" + week_on_week);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("全部店-昨日周同比==昨天新增的付费会员值-【新增客户趋势】上周昨天新增的付费会员值/上周昨天新增的付费会员值");
        }

    }

    /**
     * ====================单店昨日周同比==昨天新增的客户值-【新增客户趋势】上周昨天新增的顾客值/上周昨天新增的客户值======================
     */
    @Test
    public void wenkOnWeek_cust() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得周同比
            String new_uv_yoy = mds.getTransform_single("CUSTOMER",shop_id,"new_uv_yoy");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> result = mds.getNew_counts(list,"customer");
            int customer1 = result.get("uv1");
            int customer2 = result.get("uv2");

            String ss=  CommonUtil.getPercent(customer1-customer2,customer2,4);
            String week_on_week =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_yoy.equals(week_on_week)), "单店昨日周同比" + new_uv_yoy + "!=昨天周x新增的客户值-【新增客户趋势】上周昨天新增的顾客值/上周昨天新增的客户值=" + week_on_week);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日周同比==昨天新增的客户值-【新增客户趋势】上周昨天新增的顾客值/上周昨天新增的客户值");
        }

    }


    /**
     * ====================单店昨日周同比==昨天新增的全渠道会员值-【新增客户趋势】上周昨天新增的全渠道会员值/上周昨天新增的全渠道会员值======================
     */
    @Test
    public void wenkOnWeek_omni() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得周同比
            String new_uv_yoy = mds.getTransform_single("OMNI_CHANNEL",shop_id,"new_uv_yoy");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> result = mds.getNew_counts(list,"omni_channel");
            int omni_channel1 = result.get("uv1");
            int omni_channel2 = result.get("uv2");

            String ss=  CommonUtil.getPercent(omni_channel1-omni_channel2,omni_channel2,4);
            String week_on_week =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_yoy.equals(week_on_week)), "单店昨日周同比" + new_uv_yoy + "!=昨天新增的全渠道会员值-【新增客户趋势】上周昨天新增的全渠道会员值/上周昨天新增的全渠道会员值=" + week_on_week);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日周同比==昨天新增的全渠道会员值-【新增客户趋势】上周昨天新增的全渠道会员值/上周昨天新增的全渠道会员值");
        }

    }

    /**
     * ====================单店昨日周同比==昨天新增的付费会员值-【新增客户趋势】上周的昨天新增的付费会员值/上周的昨天新增的付费会员值======================
     */
    @Test
    public void wenkOnWeek_paid() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得周同比
            String new_uv_yoy = mds.getTransform_single("PAID",shop_id,"new_uv_yoy");

            //获取客户趋势图中昨日新增的付费人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> result = mds.getNew_counts(list,"paid");
            int paid1 = result.get("uv1");
            int paid2 = result.get("uv2");

            String ss=  CommonUtil.getPercent(paid1-paid2,paid2,4);
            String week_on_week =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_yoy.equals(week_on_week)), "单店昨日周同比" + new_uv_yoy + "!=昨天周x新增的付费会员值-【新增客户趋势】上周周x新增的付费会员值/上周周x新增的付费会员值=" + week_on_week);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日周同比==周1新增的付费会员值-【新增客户趋势】上周1新增的付费会员值/上周1新增的付费会员值");
        }

    }



//--------------------------------------------------日环比区域---------------------------------------------------------
    /**
     * ====================所有店昨日日环比==昨天新增的客户值-【新增客户趋势】前天新增的客户值/前天新增的客户值======================
     */
    @Test
    public void dayOnday_cust_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得日环比
            String new_uv_ring = mds.getTransformData("CUSTOMER","new_uv_ring");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> people = mds.getNewCounts(list,"customer");
            int customer1 = people.get("uv1");
            int customer2 = people.get("uv2");


            String ss=  CommonUtil.getPercent(customer1-customer2,customer2,4);
            String day_on_day =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_ring.equals(day_on_day)), "所有店昨日日环比" + new_uv_ring + "!=昨天新增的客户值-【新增客户趋势】前天新增的客户值/前天新增的客户值：" + day_on_day);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("所有店昨日日环比==昨天新增的客户值-【新增客户趋势】前天新增的客户值/前天新增的客户值");
        }

    }

    /**
     * ====================所有店昨日日环比==昨天新增的全渠道会员值-【新增客户趋势】前天新增的全渠道会员值/前天新增的全渠道会员值======================
     */
    @Test
    public void dayOnday_omni_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得日环比
            String new_uv_ring = mds.getTransformData("OMNI_CHANNEL","new_uv_ring");


            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> people = mds.getNewCounts(list,"omni_channel");
            int omni_channel1 = people.get("uv1");
            int omni_channel2 = people.get("uv2");


            String ss=  CommonUtil.getPercent(omni_channel1-omni_channel2,omni_channel2,4);
            String day_on_day =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_ring.equals(day_on_day)), "所有店昨日日环比" + new_uv_ring + "!=昨天新增的全渠道会员值-【新增客户趋势】前天新增的全渠道会员值/前天新增的全渠道会员值：" + day_on_day);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("所有店昨日日环比==昨天新增的全渠道会员值-【新增客户趋势】前天新增的全渠道会员值/前天新增的全渠道会员值");
        }

    }

    /**
     * ====================单店昨日日环比==昨天新增的客户值-【新增客户趋势】前天新增的客户值/前天新增的客户值======================
     */
    @Test
    public void dayOnday_cust() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得日环比
            String new_uv_ring = mds.getTransform_single("CUSTOMER",shop_id,"new_uv_ring");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> people = mds.getNewCounts(list,"customer");
            int customer1 = people.get("uv1");
            int customer2 = people.get("uv2");

           String ss=  CommonUtil.getPercent(customer1-customer2,customer2,4);
           String day_on_day =  ss.replace("%","");


            Preconditions.checkArgument((new_uv_ring.equals(day_on_day)), "单店"+shop_id+"昨日日环比" + new_uv_ring + "!=昨天新增的客户值-【新增客户趋势】前天新增的客户值/前天新增的客户值：" + day_on_day);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日日环比==昨天新增的客户值-【新增客户趋势】前天新增的客户值/前天新增的客户值");
        }

    }



    /**
     * ====================单店昨日日环比==昨天新增的全渠道会员值-【新增客户趋势】前天新增的全渠道会员值/前天新增的全渠道会员值======================
     */
    @Test
    public void dayOnday_omni() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得日环比
            String new_uv_ring = mds.getTransform_single("OMNI_CHANNEL",shop_id,"new_uv_ring");

            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> people = mds.getNewCounts(list,"omni_channel");
            int omni_channel1 = people.get("uv1");
            int omni_channel2 = people.get("uv2");

            String ss=  CommonUtil.getPercent(omni_channel1-omni_channel2,omni_channel2,4);
            String day_on_day =  ss.replace("%","");

            Preconditions.checkArgument((new_uv_ring.equals(day_on_day)), "单店"+shop_id+"昨日日环比" + new_uv_ring + "!=昨天新增的全渠道会员值-【新增客户趋势】前天新增的全渠道会员值/前天新增的全渠道会员值：" + day_on_day);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日新增全渠道会员的日环比==昨天新增的全渠道会员值-【新增客户趋势】前天新增的全渠道会员值/前天新增的全渠道会员值");
        }

    }


    /**
     * ====================单店昨日日环比==昨天新增的付费会员值-【新增客户趋势】前天新增的付费会员值/前天新增的付费会员值======================
     */
    @Test
    public void dayOnday_Paid() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得日环比
            String new_uv_ring = mds.getTransform_single("PAID",shop_id,"new_uv_ring");

            //获取客户趋势图中昨日新增的付费会员人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            Map<String, Integer> people = mds.getNewCounts(list,"paid");
            int paid1 = people.get("uv1");
            int paid2 = people.get("uv2");

            String ss=  CommonUtil.getPercent(paid1-paid2,paid2,4);
            String day_on_day =  ss.replace("%","");
            Preconditions.checkArgument((new_uv_ring.equals(day_on_day)), "单店\"+shop_id+\"昨日日环比" + new_uv_ring + "!=昨天新增的付费会员值-【新增客户趋势】前天新增的付费会员值/前天新增的付费会员值：" + day_on_day);
//
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日新增付费会员的日环比==昨天新增的付费会员值-【新增客户趋势】前天新增的付费会员值/前天新增的付费会员值");
        }

    }



//----------------------------------------------------顾客占比-----------------------------------------------------------------

    /**
     * ===================（所有店）昨日顾客占比==昨天顾客数量/昨日到店客群总数＊100%======================
     */
    @Test
    public void customer_rate_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform =  mds.getTransformData("CUSTOMER","transform");

            //获取客户趋势图中昨日新增的客户人数
           int customer =  mds.getYesNew_count("customer","RECENT_FOURTEEN");
            //获取历史客流中昨日的到店客流总数

            int uvs = mds.getday_count(cycle_type, month, shop_id,shop_id_01);

            String ss=  CommonUtil.getPercent(customer,uvs,4);
            String cust_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform.equals(cust_rate)), "（所有店）昨日客户占比" + transform + "!=昨天顾客数量/昨日到店客群总数＊100%：" + cust_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("所有店店昨日客户占比==昨天顾客数量/昨日到店客群总数＊100%");
        }

    }

    /**
     * ====================（所有店）昨日全渠道会员占比==昨日新增全渠道会员数量/当日到店客群总数＊100%======================
     */
    @Test
    public void omni_rate_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道部分得全渠道占比
            String transform =  mds.getTransformData("OMNI_CHANNEL","transform");

            //获取客户趋势图中昨日新增的全渠道人数
            int omni_channel =  mds.getYesNew_count("omni_channel","RECENT_FOURTEEN");

            //获取历史客流中昨日的到店客流总数
            int uvs = mds.getday_count(cycle_type, month, shop_id,shop_id_01);

            String ss=  CommonUtil.getPercent(omni_channel,uvs,4);
            String omni_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform.equals(omni_rate)), "单店"+shop_id+"昨日全渠道会员占比" + transform + "!=昨天全渠道会员数量/昨日到店客群总数＊100%：" + omni_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日全渠道会员占比==昨天新增全渠道会员数量/当日到店客群总数＊100%");
        }

    }

    /**
     * ====================单店昨日顾客占比==昨天顾客数量/昨日到店客群总数＊100%======================
     */
    @Test
    public void customer_rate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform = mds.getTransform_single("CUSTOMER",shop_id_01,"transform");

            //获取客户趋势图中昨日新增的客户人数
            int customer = mds.getYesNew_count_single("customer",shop_id_01,"RECENT_FOURTEEN");

            //获取历史客流中昨日的到店客流总数
            int uv = mds.getday_count(cycle_type, month, null,shop_id_01);

            String ss=  CommonUtil.getPercent(customer,uv,4);
            String cust_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform.equals(cust_rate)), "单店"+shop_id_01+"昨日客户占比" + transform + "!=昨天顾客数量/昨日到店客群总数＊100%：" + cust_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日客户占比==昨天顾客数量/昨日到店客群总数＊100%");
        }

    }

    /**
     * ====================单店昨日全渠道会员占比==昨日新增全渠道会员数量/当日到店客群总数＊100%======================
     */
    @Test
    public void omni_rate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员得全渠道会员占比
            String transform = mds.getTransform_single("OMNI_CHANNEL",shop_id_01,"transform");

            //获取客户趋势图中昨日新增的全渠道人数
            int omni_channel = mds.getYesNew_count_single("omni_channel",shop_id_01,"RECENT_FOURTEEN");

            //获取历史客流中昨日的到店客流总数
            int uv = mds.getday_count(cycle_type, month, null,shop_id_01);

            String ss=  CommonUtil.getPercent(omni_channel,uv,4);
            String omni_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform.equals(omni_rate)), "单店"+shop_id_01+"昨日全渠道会员占比" + transform + "!=昨天全渠道会员数量/昨日到店客群总数＊100%：" + omni_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店昨日全渠道会员占比==昨天新增全渠道会员数量/当日到店客群总数＊100%");
        }

    }


    /**
     * ====================单店昨日付费会员占比==昨日新增付费会员数量/当日到店客群总数＊100%======================
     */
    @Test
    public void paid_rate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform = mds.getTransform_single("PAID",shop_id_01,"transform");

            //获取客户趋势图中昨日新增的付费人数
            int paid = mds.getYesNew_count_single("paid",shop_id_01,"RECENT_FOURTEEN");

            //获取历史客流中昨日的到店客流总数
            int uv = mds.getday_count(cycle_type, month, null,shop_id_01);

            if(paid !=0 && uv !=0){
                String  paid_rate ="";
                DecimalFormat decimalFormat = new DecimalFormat("0%");
                paid_rate = decimalFormat.format(new BigDecimal(paid).divide(new BigDecimal(uv),2,BigDecimal.ROUND_HALF_UP));

                Preconditions.checkArgument((transform.equals(paid_rate)), "单店昨日客户占比" + transform + "!=昨天顾客数量/昨日到店客群总数＊100%：" + paid_rate);
            }
            else {
                return;
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店付费会员占比==昨天新增付费会员数量/当日到店客群总数＊100%");
        }

    }
//----------------------------------------------------------------顾客占比中的周同比---------------------------------------------------------------------------

    /**
     * ====================（所有店）周同比==昨日的顾客占比-上周昨日的顾客占比/上周昨日的顾客占比======================
     */
    @Test
    public void rate_cust_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform_yoy =  mds.getTransformData("CUSTOMER","transform_yoy");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            double customer1 = 0;
            double customer2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    customer1 = list.getJSONObject(i1).getInteger("customer");
                }
                if (i1 == count - 8) {
                    customer2 = list.getJSONObject(i1).getInteger("customer");
                }
            }
            //获取历史客流中昨日的到店客流总数(4116)

            Map<String, Double> uv = mds.getweek_count(cycle_type,month, shop_id,shop_id_01);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");
            double uv3 =uv.get("uv3");
            double uv4 = uv.get("uv4");

            //昨天的顾客占比
            String ss1=  CommonUtil.getPercent(customer1,(uv1+uv3),4);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A);


            //上周昨天的顾客占比
            String ss2=  CommonUtil.getPercent(customer2,(uv2+uv4),4);
            String B =  ss2.replace("%","");
            double B1 = Double.valueOf(B);

            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String cust_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_yoy.equals(cust_rate)), "（所有店）昨日顾客占比周同比" + transform_yoy + "!=昨日的顾客占比-上周昨日的顾客占比/上周昨日的顾客占比：" + cust_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("（所有店）顾客占比周同比==昨日的顾客占比-上周昨日的顾客占比/上周昨日的顾客占比");
        }

    }

    /**
     * ====================（所有店）周同比==昨日的全渠道会员占比-上周昨日的全渠道会员占比/上周昨日的全渠道会员占比======================
     */
    @Test
    public void rate_omni_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员部分得全渠道会员占比
            String transform_yoy =  mds.getTransformData("OMNI_CHANNEL","transform_yoy");

            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            double omni1 = 0;
            double omni2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    omni1 = list.getJSONObject(i1).getInteger("omni_channel");
                }
                if (i1 == count - 8) {
                    omni2 = list.getJSONObject(i1).getInteger("omni_channel");
                }
            }
            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getweek_count(cycle_type,month, shop_id,shop_id_01);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");
            double uv3 =uv.get("uv3");
            double uv4 = uv.get("uv4");
            //昨天的全渠道会员占比
            String ss1=  CommonUtil.getPercent(omni1,(uv1+uv3),4);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A);

            //上周昨天的全渠道会员占比
            String ss2=  CommonUtil.getPercent(omni2,(uv2+uv4),4);
            String B =  ss2.replace("%","");
            double B1 = Double.valueOf(B);


            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String omni_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_yoy.equals(omni_rate)), "(所有店)昨日全渠道会员占比周同比" + transform_yoy + "!=上周昨日的全渠道会员占比-上周昨日的全渠道会员占比/上周昨日的全渠道会员占比：" + omni_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("（所有店）全渠道会员占比周同比==上周昨日的全渠道会员占比-上周昨日的全渠道会员占比/上周昨日的全渠道会员占比");
        }

    }

    /**
     * ====================单店周同比==昨日的顾客占比-上周昨日的顾客占比/上周昨日的顾客占比======================
     */
    @Test
    public void rate_cust() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform_yoy =  mds.getTransform_single("CUSTOMER",shop_id_01,"transform_yoy");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.single_newCount_pic(shop_id_01,"RECENT_FOURTEEN").getJSONArray("list");
            double customer1 = 0;
            double customer2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    customer1 = list.getJSONObject(i1).getInteger("customer");
                }
                if (i1 == count - 8) {
                    customer2 = list.getJSONObject(i1).getInteger("customer");
                }
            }
            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getweek_count(cycle_type,month, null,shop_id_01);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");

            //昨天的顾客占比
            double A = 0;
            if( uv1 ==0){
                A = 0;
            }else {
                A = customer1 / uv1 * 100;
            }

            //上周昨天的顾客占比
            double  B = 0;
            if(uv2 == 0 ){
                B = 0;
            }else {
                B = customer2 / uv2 * 100;
            }

            String ss=  CommonUtil.getPercent(A-B,B,4);
            String cust_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_yoy.equals(cust_rate)), "单店昨日顾客占比周同比" + transform_yoy + "!=昨日的顾客占比-上周昨日的顾客占比/上周昨日的顾客占比：" + cust_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店顾客占比周同比==昨日的顾客占比-上周昨日的顾客占比/上周昨日的顾客占比");
        }

    }

    /**
     * ====================单店周同比==昨日的全渠道会员占比-上周昨日的全渠道会员占比/上周昨日的全渠道会员占比======================
     */
    @Test
    public void rate_omni() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员部分得全渠道会员占比
            String transform_yoy =  mds.getTransform_single("OMNI_CHANNEL",shop_id_01,"transform_yoy");

            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.single_newCount_pic(shop_id_01,"RECENT_FOURTEEN").getJSONArray("list");
            double omni1 = 0;
            double omni2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    omni1 = list.getJSONObject(i1).getInteger("omni_channel");
                }
                if (i1 == count - 8) {
                    omni2 = list.getJSONObject(i1).getInteger("omni_channel");
                }
            }

            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getweek_count(cycle_type,month, null,shop_id_01);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");

            double A = 0;
            //昨天的全渠道会员占比
            if(uv1 == 0){
                 A = 0;
            }else {
                A = omni1 / uv1 * 100;
            }

            //上周昨天的全渠道会员占比
            double B = 0;
            if(uv1 == 0){
                B = 0;
            }else {
                B = omni2 / uv2 * 100;
            }
            String ss=  CommonUtil.getPercent(A-B,B,4);
            String omni_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_yoy.equals(omni_rate)), "单店昨日全渠道会员占比[周同比]" + transform_yoy + "!=上周昨日的全渠道会员占比-上周昨日的全渠道会员占比/上周昨日的全渠道会员占比：" + omni_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店全渠道会员占比周同比==上周昨日的全渠道会员占比-上周昨日的全渠道会员占比/上周昨日的全渠道会员占比");
        }

    }

    /**
     * ====================单店周同比==周1的付费会员占比-上周1的付费会员占比/上周1的全渠道会员占比======================
     */
    @Test
    public void rate_paid() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员部分得全渠道会员占比
            String transform_yoy =  mds.getTransform_single("PAID",shop_id_01,"transform_yoy");

            //获取客户趋势图中昨日新增的付费会员人数
            JSONArray list = md.single_newCount_pic(shop_id,"RECENT_FOURTEEN").getJSONArray("list");
            int paid1 = 0;
            int paid2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    paid1 = list.getJSONObject(i1).getInteger("paid");
                }
                if (i1 == count - 8) {
                    paid2 = list.getJSONObject(i1).getInteger("paid");
                }
            }
            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getweek_count(cycle_type,month, null,shop_id_01);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");

            //昨天的付费会员占比
            double  A = 0;
            if(uv1 == 0 ){
                A = 0;
            }else {
                A = paid1 / uv1;
            }
            //上周昨天的付费会员占比
            double  B = 0;
            if(uv2 == 0 ){
                B = 0;
            }else {
                B = paid2 / uv2;
            }
            String ss=  CommonUtil.getPercent(A-B,B,4);
            String paid_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_yoy.equals(paid_rate)), "单店昨日付费会员占比周同比" + transform_yoy + "!=周1的付费会员占比-上周1的付费会员占比/上周1的付费会员占比：" + paid_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店顾客占比周同比==周1的付费会员占比-上周1的付费会员占比/上周1的付费会员占比");
        }

    }

//-------------------------------------------------------------------------顾客占比中的日环比------------------------------------

    /**
     * ======================（所有店）顾客占比日环比==昨日的顾客占比-前天的顾客占比/前天的顾客占比======================
     */
    @Test
    public void cust_rate_day_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform_ring =  mds.getTransformData("CUSTOMER","transform_ring");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            double customer1 = 0;
            double customer2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    customer1 = list.getJSONObject(i1).getInteger("customer");
                }
                if (i1 == count - 2) {
                    customer2 = list.getJSONObject(i1).getInteger("customer");
                }
            }
            //获取历史客流中昨日的到店客流总数()
            Map<String, Double> uv = mds.getday_count_all(cycle_type,month, shop_id_01,4116l);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");
            double uv3 =uv.get("uv3");
            double uv4 =uv.get("uv4");


            //昨天的顾客占比
            String ss1=  CommonUtil.getPercent(customer1,uv1+uv3,4);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A) ;

            //昨天的顾客占比
            String ss2=  CommonUtil.getPercent(customer2,uv2+uv4,4);
            String B =  ss2.replace("%","");
            double B1 = Double.valueOf(B) ;

            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String cust_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_ring.equals(cust_rate)), "（所有店）昨日顾客占比日环比" + transform_ring + "!=昨日的顾客占比-前天的顾客占比/前天的顾客占比：" + cust_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("（所有店）顾客占比日环比==昨日的顾客占比-前天的顾客占比/前天的顾客占比");
        }

    }

    /**
     * ====================（所有店）全渠道会员占比日环比==昨日的全渠道会员占比-前天的全渠道会员占比/前天的全渠道会员占比======================
     */
    @Test
    public void omni_rate_day_both() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员部分得全渠道会员占比
            String transform_ring =  mds.getTransformData("OMNI_CHANNEL","transform_ring");


            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.member_newCount_pic("RECENT_FOURTEEN").getJSONArray("list");
            double omni_channel1 = 0;
            double omni_channel2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    omni_channel1 = list.getJSONObject(i1).getInteger("omni_channel");
                }
                if (i1 == count - 2) {
                    omni_channel2 = list.getJSONObject(i1).getInteger("omni_channel");
                }
            }
            //获取历史客流中昨日的到店客流总数()
            Map<String, Double> uv = mds.getday_count_all(cycle_type,month, shop_id,shop_id_01);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");
            double uv3 =uv.get("uv3");
            double uv4 =uv.get("uv4");

            //昨天的全渠道会员占比
            String ss1=  CommonUtil.getPercent(omni_channel1,uv1+uv3,4);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A) ;

            //上周昨天的全渠道会员占比
            String ss2=  CommonUtil.getPercent(omni_channel2,uv2+uv4,4);
            String B =  ss2.replace("%","");
            double B1 = Double.valueOf(B) ;

            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String omni_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_ring.equals(omni_rate)), "所有店店昨日全渠道会员占比日环比" + transform_ring + "!=昨日的全渠道会员占比-前天日的全渠道会员占比/前天的全渠道会员占比：" + omni_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("所有店全渠道会员占比日环比==昨日的全渠道会员占比-前天的全渠道会员占比/前天的全渠道会员占比");
        }

    }

    /**
     * ====================单店顾客占比日环比==昨日的顾客占比-前天的顾客占比/前天的顾客占比======================
     */
    @Test
    public void cust_rate_day() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取顾客部分得顾客占比
            String transform_ring =  mds.getTransform_single("CUSTOMER",shop_id_01,"transform_ring");

            //获取客户趋势图中昨日新增的顾客人数
            JSONArray list = md.single_newCount_pic(shop_id_01,"RECENT_FOURTEEN").getJSONArray("list");
            double customer1 = 0;
            double customer2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    customer1 = list.getJSONObject(i1).getInteger("customer");
                }
                if (i1 == count - 2) {
                    customer2 = list.getJSONObject(i1).getInteger("customer");
                }
            }
            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getday_count_all(cycle_type,month, shop_id_01,null);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");


            //昨天的顾客占比
            String ss1=  CommonUtil.getPercent(customer1,uv1,4);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A);
            //上周昨天的顾客占比
            String ss2=  CommonUtil.getPercent(customer2,uv2,4);
            String B =  ss2.replace("%","");
            double B1 = Double.valueOf(B);

            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String cust_rate =  ss.replace("%","");

            Preconditions.checkArgument((transform_ring.equals(cust_rate)), "单店"+shop_id_01+"昨日顾客占比日环比" + transform_ring + "!=昨日的顾客占比-前天的顾客占比/前天的顾客占比：" + cust_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店顾客占比日环比==昨日的顾客占比-前天的顾客占比/前天的顾客占比");
        }

    }

    /**
     * ====================单店全渠道会员占比日环比==昨日的全渠道会员占比-前天的全渠道会员占比/前天的全渠道会员占比======================
     */
    @Test
    public void omni_rate_day() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取全渠道会员部分得全渠道会员占比
            String transform_ring =  mds.getTransform_single("OMNI_CHANNEL",shop_id_01,"transform_ring");

            //获取客户趋势图中昨日新增的全渠道会员人数
            JSONArray list = md.single_newCount_pic(shop_id_01,"RECENT_FOURTEEN").getJSONArray("list");
            double omni_channel1 = 0;
            double omni_channel2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    omni_channel1 = list.getJSONObject(i1).getInteger("omni_channel");
                }
                if (i1 == count - 2) {
                    omni_channel2 = list.getJSONObject(i1).getInteger("omni_channel");
                }
            }
            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getday_count_all(cycle_type,month, shop_id_01,null);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");
            //昨天的全渠道会员占比
            String ss1=  CommonUtil.getPercent(omni_channel1,uv1,5);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A);
            //上周昨天的全渠道会员占比
            String ss2=  CommonUtil.getPercent(omni_channel2,uv2,5);
            String B =  ss2.replace("%","");
            double B1 = Double.valueOf(B);


            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String omni_rate =  ss.replace("%","");
            double omni_rate1 = Double.valueOf(omni_rate);

            double transform_ring1 = Double.valueOf(transform_ring);

            double result = Math.abs(omni_rate1-transform_ring1);

            Preconditions.checkArgument(result <= 0.5, "单店昨日全渠道会员占比日环比" + transform_ring + "!=昨日的全渠道会员占比-前天日的全渠道会员占比/前天的全渠道会员占比：" + omni_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店全渠道会员占比日环比==昨日的全渠道会员占比-前天的全渠道会员占比/前天的全渠道会员占比");
        }

    }

    /**
     * ====================单店付费会员占比日环比==昨日的付费会员占比-前天的付费会员占比/前天的付费会员占比======================
     */
    @Test
    public void paid_rate_day() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从新增顾客占比部分获取付费会员部分得付费会员占比
            String transform_ring =  mds.getTransform_single("PAID",shop_id_01,"transform_ring");


            //获取客户趋势图中昨日新增的付费会员人数
            JSONArray list = md.single_newCount_pic(shop_id_01,"RECENT_FOURTEEN").getJSONArray("list");
            double paid1 = 0;
            double paid2 = 0;
            int count = list.size();
            for(int i1 =0;i1<count;i1++) {
                if (i1 == count - 1) {
                    paid1 = list.getJSONObject(i1).getInteger("paid");
                }
                if (i1 == count - 2) {
                    paid2 = list.getJSONObject(i1).getInteger("paid");
                }
            }
            //获取历史客流中昨日的到店客流总数
            Map<String, Double> uv = mds.getday_count_all(cycle_type,month, shop_id_01,null);
            double uv1 =uv.get("uv1");
            double uv2 =uv.get("uv2");
            //昨天的付费会员占比
            String ss1=  CommonUtil.getPercent(paid1,uv1,4);
            String A =  ss1.replace("%","");
            double A1 = Double.valueOf(A);
            //上周昨天的付费会员占比
            String ss2=  CommonUtil.getPercent(paid2,uv2,4);
            String B =  ss1.replace("%","");
            double B1 = Double.valueOf(B);


            String ss=  CommonUtil.getPercent(A1-B1,B1,4);
            String paid_rate =  ss.replace("%","");
            Preconditions.checkArgument((transform_ring.equals(paid_rate)), "单店昨日付费会员占比日环比" + transform_ring + "!=昨日的付费会员占比-前天的付费会员占比/上周昨日的付费会员占比：" + paid_rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("单店付费会员占比日环比==昨日的付费会员占比-前天的付费会员占比/前天的付费会员占比");
        }

    }

    /**
     * ====================累计顾客的总数（所有店）==前天的累计客户+今天新增的（顾客+全渠道会员+付费会员）之和======================
     */
    @Test
    public void memberTotalCount() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {

            Integer customer_uv = 0;
            Integer customer_uv_new_today = 0;
            Integer customer_uv_01 = 0;
            Integer omni_uv_today = 0;
            Integer paid_uv_today = 0;
            Integer omni_uv_total = 0;
            Integer omni_uv_total_01 = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray trend_list = md.historyShopMemberCountV3(cycle_type).getJSONArray("trend_list");
            JSONArray list = md.member_newCount_pic(cycle_type).getJSONArray("list");

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
            for(int j=0; j < list.size();j++){
                if (j - list.size() == -1) {
                    customer_uv_new_today = list.getJSONObject(j).getInteger("customer");
                    omni_uv_today = list.getJSONObject(j).getInteger("omni_channel");
                    paid_uv_today = list.getJSONObject(j).getInteger("paid");
                }
            }
            int qa_customer_uv = customer_uv_01 +customer_uv_new_today;
            int qa_omni_uv =  omni_uv_total_01 + omni_uv_today ;




            Preconditions.checkArgument((qa_customer_uv == customer_uv), "累计的顾客总人数" + customer_uv + "!=前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和=" + qa_customer_uv);
            Preconditions.checkArgument((qa_omni_uv == omni_uv_total), "累计的全渠道总人数" + omni_uv_total + "!=前天的累计全渠道会员+今天新增的（全渠道会员）之和=" + qa_omni_uv);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            Integer customer_uv_new_today = 0;
            Integer omni_uv_today = 0;
            //所选周期内（30天）的所有门店的各天顾客/全渠道/付费会员的累计和
            JSONArray list = md.single_newCount_pic(shop_id,cycle_type).getJSONArray("list");
            Map<String, Integer> result = mds.getAllCustomer(shop_id, cycle_type, month);
            Integer customer_uv = result.get("customer_uv");
            Integer omni_uv_total = result.get("omni_uv_total");
            Integer customer_uv_01 = result.get("customer_uv_01");
            Integer omni_uv_total_01 = result.get("omni_uv_total_01");
            //获取今天新增的顾客和全渠道会员
            for(int j=0; j < list.size();j++){
                if (j - list.size() == -1) {
                    customer_uv_new_today = list.getJSONObject(j).getInteger("customer");
                    omni_uv_today = list.getJSONObject(j).getInteger("omni_channel");
                }
            }
            int qa_customer_uv = customer_uv_01 +customer_uv_new_today;
            int qa_omni_uv =  omni_uv_total_01 + omni_uv_today ;

            Preconditions.checkArgument((qa_customer_uv == customer_uv), "累计的顾客总人数" + customer_uv + "!=前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和=" + qa_customer_uv  +"。报错门店shop_id="+shop_id);
            Preconditions.checkArgument((qa_omni_uv == omni_uv_total), "累计的全渠道总人数" + omni_uv_total + "!=前天的累计全渠道会员+昨天新增的（全渠道会员）之和=" + qa_omni_uv +"。报错门店shop_id="+shop_id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("（单个门店）累计顾客的总数==前天的累计客户+昨天新增的（顾客+全渠道会员+付费会员）之和||累计的全渠道总人数===前天的累计全渠道会员+昨天新增的（全渠道会员）之和");
        }

    }

    /**
     * ====================实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv======================
     */
   // @Test
    public void yesterdayTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取昨天日各个时间段内到访得人次且相加
            JSONArray eTlist = md.realTimeShopPvV3((long) shop_id_01).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer yesterdayPv = eTlist.getJSONObject(i).getInteger("yesterday_pv");
                yesterdayPv = yesterdayPv != null ? yesterdayPv : 0;
                count += yesterdayPv;
            }
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id_01).getJSONArray("trend_list");
            int pv = 0;
            int count1 = trend_list.size();
            for (int i = 0; i < count1; i++) {
                if (i == count1 - 1) {
                    pv = trend_list.getJSONObject(i).getInteger("pv");
                }
            }
            Preconditions.checkArgument((count == pv), "实时客流中，昨日到访各个时段的pv之和" + count + ">历史客流中截至日期的的pv=" + pv + "。报错门店的shopId=" + 43072l);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv");
        }

    }


    /**
     * ====================云中客中累计不为0，事件也不能为0========================
     */
    @Test
    public void custmerWithThing() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray trend_list = md.historyShopMemberV3(shop_id, cycle_type, month).getJSONArray("trend_list");
            int count1 = trend_list.size();
            int customer_uv_total = 0;
            for (int i = 0; i < count1; i++) {
                if (i == count1 - 1) {
                    customer_uv_total = trend_list.getJSONObject(i).getInteger("customer_uv_total");
                }
            }
            Integer total = md.memberTotalListV3(shop_id, page, size).getInteger("total");
            Preconditions.checkArgument((customer_uv_total != 0 && total != 0), "累计顾客为：" + customer_uv_total + "事件为" + total + "。报错门店的shopId=" + shop_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            //根据门店id获取customer_id
            Integer total_sum = 0;
            Integer deal = 0;
            String customer_id = "";
            int count =0;
            JSONArray list1 = md.memberTotalListV3(shop_id, page, 50).getJSONArray("list");
            for (int j = 0; j < list1.size(); j++) {
                customer_id = list1.getJSONObject(j).getString("customer_id");
                total_sum = md.memberDetail(shop_id, customer_id, page, size).getInteger("total");//留痕事件数量
                if (total_sum == null) {
                    total_sum = 0;
                }
                int t = CommonUtil.getTurningPage(total_sum, 50);
                for (int l = 1; l < t; l++) {
                    JSONObject res = md.memberDetail(shop_id, customer_id, l, size);
                    JSONArray list01 = res.getJSONArray("list");
                    for(int k=0;k<list01.size();k++){
                        String mark = list01.getJSONObject(k).getString("mark");
                        if(mark.equals("门店下单")){
                            count++;
                        }
                    }
                    deal = res.getInteger("total_deal_times");//获取累计交易的次数
                    if (deal == null) {
                        deal = 0;
                    }
                    Preconditions.checkArgument(count == deal, "累计交易：" + deal + "门店下单留痕：" +count+ "。报错门店的shopId=" + shop_id);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("累计交易次数==留痕事件中门店下单留痕的数量");
        }
    }

    /**
     * ====================客户详情累计交易的次数==留痕事件中门店下单的次数|||累计到店的数据==留痕事件中进店次数+门店下单的次数========================
     */
    @Test
    public void custInfoData1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //根据门店id获取customer_id
            Integer enter_total = 0;
            Integer total_sum = 0;
            Integer deal = 0;
            String customer_id = "";
            String face_url = "";
            JSONArray list1 = md.memberTotalListV3(shop_id, page, 50).getJSONArray("list");
            for (int j = 0; j < list1.size(); j++) {
                customer_id = list1.getJSONObject(j).getString("customer_id");
                total_sum = md.memberDetail(shop_id, customer_id, page, size).getInteger("total");//留痕事件数量
                if (total_sum == null) {
                    total_sum = 0;
                }
                int t = CommonUtil.getTurningPage(total_sum, 50);
                for (int l = 1; l < t; l++) {
                    JSONObject res = md.memberDetail(shop_id, customer_id, l, size);
                    JSONArray list01 = res.getJSONArray("list");
                    enter_total = res.getInteger("total_visit_times");//累计到店天数
                    if (enter_total == null) {
                        enter_total = 0;
                    }
                    deal = res.getInteger("total_deal_times");//获取累计交易的次数
                    if (deal == null) {
                        deal = 0;
                    }
                    //或者每个人物的脸部图片地址
                    face_url = res.getString("face_url");
                    if (face_url == null) {
                        face_url = "";
                    }
                    Preconditions.checkArgument(!StringUtils.isEmpty(face_url), "人物ID为:" + customer_id + "的半身照为空" + "。报错门店的shopId=" + shop_id);
                    Preconditions.checkArgument(enter_total !=0 ||deal !=0 && list01.size() !=0, "累计到店天数" + enter_total +".累计交易" + deal + "留痕列表的长度" +list01.size()+ "。报错门店的shopId=" + shop_id);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("累计到店和累计交易不为0，留痕列表不能为0||门店客户的照片不能为空||全渠道会员一定有会员ID||顾客没有会员ID");
        }
    }



    /**
     * ====================选择自然月的数据展示是否正常========================
     */
    @Test
    public void dataSurveillanceForMo() {
        logger.logCaseStart(caseResult.getCaseName());
        
        try {
            String cycle_type = "";
            String month = "2020-08";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }

            Preconditions.checkArgument((uv_Sum != 0), "历史客流-自然月9月的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认自然月9月的数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-自然月9月的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-自然月9月的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            String cycle_type = "RECENT_SEVEN";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }

            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近7天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近7天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近7天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近7天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        
        try {
            String cycle_type = "RECENT_FOURTEEN";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }
            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近14天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近14天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近14天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近14天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            String cycle_type = "RECENT_THIRTY";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }

            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }
            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近30天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近30天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近30天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近30天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近30天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
        try {
            String cycle_type = "RECENT_SIXTY";
            JSONArray trend_list = md.historyShopTrendV3(cycle_type, month, shop_id).getJSONArray("trend_list");
            int uv_Sum = 0;
            Integer uv = 0;
            for (int i = 0; i < trend_list.size(); i++) {
                uv = trend_list.getJSONObject(i).getInteger("uv");
                if (uv != null) {
                    uv_Sum += uv;
                }
            }
            //获取过点客群总人次&总人数
            JSONArray ldlist = md.historyShopConversionV3(shop_id, cycle_type, month).getJSONArray("list");
            Map<String, Integer> pass_by = this.getCount(ldlist, "PASS_BY");
            int pv1 = pass_by.get("pv1");
            int uv1 = pass_by.get("uv1");

            //获取客群时段分布
            JSONArray showList = md.historyShopHourV3(shop_id, cycle_type, month).getJSONArray("list");
            boolean result = false;
            if (showList != null) {
                result = true;
            }
            Preconditions.checkArgument((uv_Sum != 0), "历史客流-最近60天的数据相加等于" + uv_Sum + "。报错门店的shopId=" + shop_id + "请线上确认最近60天数据为0是否为正常，");
            Preconditions.checkArgument((pv1 != 0 && uv1 != 0), "客群漏斗-最近60天的数据过店pv等于" + pv1 + "过店uv" + uv1 + "。报错门店的shopId=" + shop_id + "请线上确认最近60天数据为0是否为正常，");
            Preconditions.checkArgument((result = true), "客群漏斗-最近60天的客群时段分布数据为空" + "。报错门店的shopId=" + shop_id + "请线上确认");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("选择最近60天的数据是否正常");
        }

    }

    /**
     * ====================实时客流中，昨日到访各个时段的pv之和==历史客流中截至日期的的pv======================
     */
    @Test
    public void todayTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取昨天日各个时间段内到访得人次且相加
            JSONArray eTlist = md.realTimeShopPvV3((long) shop_id_01).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < eTlist.size(); i++) {
                Integer yesterdayPv = eTlist.getJSONObject(i).getInteger("today_pv");
                yesterdayPv = yesterdayPv != null ? yesterdayPv : 0;
                count += yesterdayPv;
            }

           // Preconditions.checkArgument((count == pv), "实时客流中，昨日到访各个时段的pv之和" + count + ">历史客流中截至日期的的pv=" + pv + "。报错门店的shopId=" + shop_id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("实时客流中，今日到访各个时段的pv之和");
        }

    }

}

