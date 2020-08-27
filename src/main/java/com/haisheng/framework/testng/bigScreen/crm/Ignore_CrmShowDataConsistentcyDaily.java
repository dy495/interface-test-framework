package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;


/**
 * V 3.0页面取消 case作废
 *
 *
 */
public class Ignore_CrmShowDataConsistentcyDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();

    Ignore_Crm crm = new Ignore_Crm();


    //cycle_type="RECENT_SEVEN"最近七天，RECENT_THIRTY最近30天，RECENT_SIXTY，RECENT_NINETY

    String cycle7 = "RECENT_SEVEN";
    String cycle30 = "RECENT_THIRTY";
    String cycle60 = "RECENT_SIXTY";
    String cycle90 = "RECENT_NINETY";

    String dimensionType = "CUSTOMER_TYPE";
    String dimensionSource = "SOURCE";
    String dimensionChannel = "CHANNEL";
    String dimensionVisitTimes = "VISIT_TIMES";

    @Ignore
    @Test
    public void uvLTTrendUvLTPv() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "累计到访人数<=当天抓拍uv每天之和(身份)<=累计到访人次";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String dimension = dimensionType;

            for (int i = 0; i < cycleTypes.length; i++) {

                JSONObject overviewData = crm.overviewCycleS(cycleTypes[i]);

//            总uv
                int uv = crm.getOverviewData(overviewData, "uv");

//            总pv
                int pv = crm.getOverviewData(overviewData, "pv");

                JSONObject arriveTrendData = crm.arriveTrendCycleS(cycleTypes[i], dimension);

//            客流趋势-总客流
                int trendDataUv = crm.getArriveTrendDataUv(arriveTrendData);

                Preconditions.checkArgument(uv <= trendDataUv, "cycleType=" + cycleTypes[i] +
                        "，累计到访人数=" + uv + "不应大于客流趋势中的每天总客流累计=" + trendDataUv);
                Preconditions.checkArgument(trendDataUv <= pv, "cycleType=" + cycleTypes[i] +
                        "，客流趋势中的每天总客流累计=" + trendDataUv + "不应大于累计到访人次=" + pv);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void averageUvEqualsTrendData() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "日均到访人数=回访趋势图每日抓拍uv之和/天数";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};

            String dimension = dimensionType;

            for (int i = 0; i < cycleTypes.length; i++) {

                JSONObject overviewData = crm.overviewCycleS(cycleTypes[i]);

//            日均到访人数
                int averageDayUv = crm.getOverviewData(overviewData, "average_day_uv");

//            客流趋势-总客流
                JSONObject arriveTrendData = crm.arriveTrendCycleS(cycleTypes[i], dimension);
                int trendDataPerUv = crm.getArriveTrendDataAvgUv(arriveTrendData);

                Preconditions.checkArgument(averageDayUv == trendDataPerUv,
                        "cycleType=" + cycleTypes[i] + "，日均到访人数=" + averageDayUv +
                                "不等于回访趋势图每日抓拍uv之和/天数=" + trendDataPerUv);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void trend4Equals() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店客流趋势:身份当天抓拍uv=来源当天抓拍uv=渠道当天抓拍uv=回访当天抓拍uv";
        String caseName = ciCaseName;

        try {

            crm.checkTrendUvDimensionEquals();

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void trend3Equals() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店客流趋势:同一天的身份/来源/渠道的同行客户数量一致";
        String caseName = ciCaseName;

        try {

            crm.checkTrend3DimensionEquals();

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void visitDataShopChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "平均到店间隔>=1，0<游逛深度<=100%，平均每人到店停留时长>=1";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};

            for (int i = 0; i < cycleTypes.length; i++) {

                JSONObject overviewData = crm.overviewCycleS(cycleTypes[i]);
//            总uv
                int uv = crm.getOverviewData(overviewData, "uv");

                if (uv > 0) {
                    JSONObject visitDataS = crm.visitDataCycleS(cycleTypes[i]);
                    crm.chkVisitData(visitDataS, cycleTypes[i]);
                }
            }


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void hourDataUvLTPv() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店时段分布-所有时间区间人数和<=总人次";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};

            for (int i = 0; i < cycleTypes.length; i++) {

//            到店时段分布-所有时间区间人数和
                JSONObject hourData = crm.hourDataCycleS(cycleTypes[i]);
                int hourDataUv = crm.getHourDataUv(hourData);

//            趋势图每天抓拍uv之和
                JSONObject data = crm.overviewCycleS(cycleTypes[i]);
                int pv = crm.getOverviewData(data, "pv");

                Preconditions.checkArgument(hourDataUv <= pv, "cycleType=" + cycleTypes[i] + "，到店时段分布-所有时间区间人数和=" + hourDataUv +
                        "不应大于于累计到访人次=" + pv);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void hourDataUvMTTrendUv() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店时段分布-所有时间区间人数和>=趋势图每天抓拍uv之和";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String dimension = dimensionType;

            for (int i = 0; i < cycleTypes.length; i++) {

//            到店时段分布-所有时间区间人数和
                JSONObject hourData = crm.hourDataCycleS(cycleTypes[i]);
                int hourDataUv = crm.getHourDataUv(hourData);

//            趋势图每天抓拍uv之和
                JSONObject arriveTrendData = crm.arriveTrendCycleS(cycleTypes[i], dimension);
                int trendUv = crm.getArriveTrendDataUv(arriveTrendData);

                Preconditions.checkArgument(hourDataUv >= trendUv, "cycleType=" + cycleTypes[i] + "，到店时段分布-所有时间区间人数和=" + hourDataUv +
                        "不应小于趋势图每天抓拍uv之和=" + trendUv);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void accompanyPercent100() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "伴随分布=100%";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String month = "";

            for (int i = 0; i < cycleTypes.length; i++) {

                JSONArray list = crm.accompanyCycleS(cycleTypes[i], month).getJSONArray("list");

                double total = 0.0d;

                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    total += single.getDoubleValue("percentage");
                }

                Preconditions.checkArgument(Math.abs(1 - total) <= 0.01, "cycleType=" + cycleTypes[i] + "，伴随分布的比例之和=" + total);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void ageGenderPercent100() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "消费者性别/年龄分布=100%";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String month = "";

            for (int k = 0; k < cycleTypes.length; k++) {
                JSONObject jo = crm.ageGenderDistS(cycleTypes[k], month);
                crm.chkAgeGender(jo, cycleTypes[k]);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void visitDataRegionUnique() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店客流趋势：停放位置：唯一不重复";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String month = "";

            for (int i = 0; i < cycleTypes.length; i++) {

//            区域到访数据
                JSONObject visitData = crm.visitDataR(cycleTypes[i], month);
                crm.chkVisitDataRegionUnique(visitData, cycleTypes[i]);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void interestDriveDealNum() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "同一车型：停放位置不同、平均关注度/试乘试驾次数/成交数量一致";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String month = "";

            for (int i = 0; i < cycleTypes.length; i++) {
                JSONObject visitData = crm.visitDataR(cycleTypes[i], month);
                crm.checkVisitData(visitData, cycleTypes[i]);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Ignore
    @Test
    public void visitDataAndSkuRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "同一车型，到店客流趋势与商品排行中试乘试驾、成交量和商品排行中数据一致";
        String caseName = ciCaseName;

        try {

            String[] cycleTypes = {cycle7, cycle30, cycle60, cycle90};
            String month = "";

            for (int i = 0; i < cycleTypes.length; i++) {

//            到店客流趋势
                JSONObject visitData = crm.visitDataR(cycleTypes[i], month);

//            商品排行
                JSONObject skuRank = crm.skuRank(cycleTypes[i], month);

//            校验是否一致
                crm.checkVisitDataAndSkuRank(visitData, skuRank, cycleTypes[i]);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        crm.majordomoLogin();
    }

    @AfterClass
    public void clean() {
        crm.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }


}
