package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.bigScreen.feidanDaily.Feidan;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DecimalFormat;

public class CrmConsistentcyDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    Feidan feidan = new Feidan();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();

    Crm crm = new Crm();

    @Test
    public void uvLTTrendUvLTPv() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "累计到访人数<=身份当天抓拍uv每天之和<=累计到访人次";
        String caseName = ciCaseName;

        try {

            String cycleType = "";
            String month = "";
            String dimension = "";

            JSONObject overviewData = crm.overviewS(cycleType, month);
//            总uv
            int uv = crm.getOverviewData(overviewData, "uv");

//            总pv
            int pv = crm.getOverviewData(overviewData, "pv");
//            int averageDayUv = crm.getOverviewData(jsonObject, "average_day_uv");
//            int averageUseTime = crm.getOverviewData(jsonObject, "average_use_time");

            JSONObject arriveTrendData = crm.arriveTrendS(cycleType, month, dimension);

//            客流趋势-总客流
            int trendDataUv = crm.getArriveTrendDataUv(arriveTrendData, 0);//0是总客流

            Preconditions.checkArgument(uv <= trendDataUv, "累计到访人数=" + uv + "不应大于客流趋势中的每天总客流累计=" + trendDataUv);
            Preconditions.checkArgument(trendDataUv <= pv, "客流趋势中的每天总客流累计=" + trendDataUv + "不应大于累计到访人次=" + pv);

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

            String cycleType = "";
            String month = "";
            String dimension = "";

            JSONObject overviewData = crm.overviewS(cycleType, month);

//            日均到访人数
            int averageDayUv = crm.getOverviewData(overviewData, "average_day_uv");

//            客流趋势-总客流
            JSONObject arriveTrendData = crm.arriveTrendS(cycleType, month, dimension);
            int trendDataPerUv = crm.getArriveTrendDataUv(arriveTrendData, 0) / 7;//0是总客流

            Preconditions.checkArgument(averageDayUv == trendDataPerUv, "日均到访人数=" + averageDayUv + "不等于回访趋势图每日抓拍uv之和/天数=" + trendDataPerUv);

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
    public void uvMTAccumulatedTrend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店客流趋势:当天抓拍uv>各类型之和";
        String caseName = ciCaseName;

        try {

            String cycleType = "";
            String month = "";
            String[] dimensions = {};

            for (int i = 0; i < dimensions.length; i++) {

                JSONObject arriveTrendData = crm.arriveTrendS(cycleType, month, dimensions[i]);

                crm.checkUvMTAccumulatedTrend(arriveTrendData, dimensions[i]);
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
    public void trend4Equals() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "到店客流趋势:身份当天抓拍uv=来源当天抓拍uv=渠道当天抓拍uv=回访当天抓拍uv";
        String caseName = ciCaseName;

        try {

            crm.checkTrendUvtabEquals();

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
    public void averageUseTimeLTTrendVisit() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "平均每人到店次数 < Σ（回访趋势图次数*人数）/ 累计到访人数 ";
        String caseName = ciCaseName;

        try {

            String cycleType = "";
            String month = "";
            String dimension = "";

            JSONObject overviewData = crm.overviewS(cycleType, month);

//            平均每人到店次数
            int averageUseTime = crm.getOverviewData(overviewData, "average_use_time");
            int uv = crm.getOverviewData(overviewData, "uv");

//            客流趋势-总客流
            JSONArray list = crm.arriveTrendS(cycleType, month, dimension).
                    getJSONArray("list");

            int total = 0;

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                JSONArray list1 = single.getJSONArray("list");

                for (int j = 1; j < list1.size(); j++) {
                    total += list1.getInteger(j) * j;
                }
            }

            total /= uv;

            Preconditions.checkArgument(averageUseTime < total, "平均每人到店次数=" + averageUseTime +
                    "应小于回访趋势图Σ（回访趋势图次数*人数）/ 累计到访人数=" + total);

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
    public void averageUseTimeEqualsPvDivideUv() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "平均每人到店次数 == 累计到访人次/累计到访人数";
        String caseName = ciCaseName;

        try {

            String cycleType = "";
            String month = "";

            JSONObject overviewData = crm.overviewS(cycleType, month);

//            平均每人到店次数
            int averageUseTime = crm.getOverviewData(overviewData, "average_use_time");
            int uv = crm.getOverviewData(overviewData, "uv");
            int pv = crm.getOverviewData(overviewData, "pv");

            Preconditions.checkArgument(averageUseTime == pv / uv, "平均每人到店次数=" + averageUseTime +
                    "不等于累计到访人次=" + pv + "/累计到访人数=" + uv);

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
    public void accompanyPercent100() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "伴随分布=100%";
        String caseName = ciCaseName;

        try {

            String cycleType = "";
            String month = "";

            JSONArray list = crm.accompanyS(cycleType, month).getJSONArray("list");

            double total = 0.0d;

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                total += single.getDoubleValue("percentage");
            }

            Preconditions.checkArgument(Math.abs(1-total)<=0.01, "伴随分布的比例之和=" + total);

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
    public void ageGenderPercent100() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "消费者性别/年龄分布=100%";
        String caseName = ciCaseName;

        try {

            String cycleType = "";
            String month = "";

//        校验男女总比例
            JSONObject jo = crm.ageGenderDistS(cycleType, month);
            DecimalFormat df = new DecimalFormat("0.00");

            String maleRatioStr = jo.getString("male_ratio_str");
            String femaleRatioStr = jo.getString("female_ratio_str");

            if (!"-".equals(maleRatioStr)) {
                float maleD = Float.valueOf(maleRatioStr.substring(0, maleRatioStr.length() - 1));

                float femaleD = Float.valueOf(femaleRatioStr.substring(0, femaleRatioStr.length() - 1));

                if ((int) (maleD + femaleD) != 100) {
                    throw new Exception("性别年龄分布-男比例=" + maleRatioStr + ",女比例=" + femaleRatioStr + "之和不是100%");
                }

//        校验各个年龄段的男女比例

                JSONArray list = jo.getJSONArray("ratio_list");

                int[] nums = new int[list.size()];
                String[] percents = new String[list.size()];
                String[] ageGroups = new String[list.size()];
                int total = 0;
                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);
                    int num = single.getInteger("num");
                    nums[i] = num;
                    String percentStr = single.getString("percent");
                    float percentD = Float.valueOf(percentStr.substring(0, percentStr.length() - 1));
                    percents[i] = df.format(percentD) + "%";
                    ageGroups[i] = single.getString("age_group");
                    total += num;
                }

                if (total == 0) {
                    for (int i = 0; i < percents.length; i++) {
                        if (!"0.00%".equals(percents[i])) {
                            throw new Exception("总数为0，" + ageGroups[i] + "的比例=" + percents[i]);
                        }
                    }
                }

                for (int i = 0; i < percents.length; i++) {
                    float percent = (float) nums[i] / (float) total * 100;
                    String percentStr = df.format(percent);

                    percentStr += "%";

                    if (!percentStr.equals(percents[i])) {
                        throw new Exception("性别年龄分布-期待比例=" + percentStr + ", 系统返回=" + percents[i]);
                    }
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


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        crm.salesPersonLogin();
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
