package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.Date;


public class ShopMallRealtimeSql extends TestCaseCommon implements TestCaseStd {
    DateTimeUtil dt = new DateTimeUtil();


    @BeforeClass
    @Override
    public void initial() {
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_SHOPMALL_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.SHOPMALL_ONLINE_TEST.getJobName());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
    }

    @Test()
    public void shopRealtimeUv() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopId = "33467";
            String curUvTitle = "今日累计到访";
            String curHereTitle = "当前在场";
            String curAvgStayTimeTitle = "人均停留时长";
            String day = dt.getHistoryDate(-0);
            String sql =
                    "SELECT tBase.title,\n" +
                    "\tIFNULL(tAll.numValue,0) numValue,\n" +
                    "\ttAll.numUnit,\n" +
                    "\tIFNULL(tAll.numValueYestoday,0) numValueYestoday,\n" +
                    "\tIFNULL(tAll.ratio,0) ratio,\n" +
                    "\tIFNULL(tAll.beforeYestodayAvg,0) beforeYestodayAvg\n" +
                    "FROM (\n" +
                    "\t\tSELECT '" + curUvTitle + "' AS title\n" +
                    "\t\tUNION\n" +
                    "\t\tSELECT '" + curHereTitle + "'\n" +
                    "\t\tUNION\n" +
                    "\t\tSELECT '" + curAvgStayTimeTitle + "'\n" +
                    "\t) tBase\n" +
                    "\tLEFT JOIN (\n" +
                    "\t\tSELECT '" + curUvTitle + "' AS title,\n" +
                    "\t\t\tIFNULL(tToday.hourSumUv, 0) AS numValue,\n" +
                    "\t\t\t\"人\" AS numUnit,\n" +
                    "\t\t\tIFNULL(tYestoday.hourSumUv, 0) AS numValueYestoday,\n" +
                    "\t\t\tIF (\n" +
                    "\t\t\t\ttToday.hourSumUv IS NULL || tYestoday.hourSumUv IS NULL,\n" +
                    "\t\t\t\t0,\n" +
                    "\t\t\t\t(tToday.hourSumUv / tYestoday.hourSumUv) - 1\n" +
                    "\t\t\t) AS ratio,\n" +
                    "\t\t\ttBeforeYestodayAvg.avgUv AS beforeYestodayAvg\n" +
                    "\t\tFROM (\n" +
                    "\t\t\t\t#今天实时\n" +
                    "\t\t\t\tSELECT `day`,\n" +
                    "\t\t\t\t\tshop_id,\n" +
                    "\t\t\t\t\tSUM(uv) AS hourSumUv\n" +
                    "\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\tAND `day` = '" + day + "'\n" +
                    "\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\tAND cust_group_id = (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\tGROUP BY `day`,\n" +
                    "\t\t\t\t\tshop_id\n" +
                    "\t\t\t) tToday\n" +
                    "\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t# 昨天\n" +
                    "\t\t\t\tSELECT `day`,\n" +
                    "\t\t\t\t\tshop_id,\n" +
                    "\t\t\t\t\tSUM(uv) AS hourSumUv\n" +
                    "\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\tAND `day` = DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\tAND `hour` <= HOUR(NOW())\n" +
                    "\t\t\t\t\tAND time_tag = \"h\"\n" +
                    "\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\tGROUP BY `day`,\n" +
                    "\t\t\t\t\tshop_id\n" +
                    "\t\t\t) tYestoday ON tToday.`shop_id` = tYestoday.`shop_id`\n" +
                    "\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t# 昨天以及之前的一个月\n" +
                    "\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\tAVG(uv) AS avgUv\n" +
                    "\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\tAND `day` <= DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\tAND `day` >= DATE_SUB('" + day + "', INTERVAL 31 DAY)\n" +
                    "\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t) tBeforeYestodayAvg ON tToday.`shop_id` = tBeforeYestodayAvg.`shop_id`\n" +
                    "\t\tUNION ALL\n" +
                    "\t\tSELECT '" + curHereTitle + "' AS title,\n" +
                    "\t\t\tIFNULL(tToday.hourSumUv, 0) AS numValue,\n" +
                    "\t\t\t\"人\" AS numUnit,\n" +
                    "\t\t\tIFNULL(tYestoday.hourSumUv, 0) AS numValueYestoday,\n" +
                    "\t\t\tIF (\n" +
                    "\t\t\t\ttToday.hourSumUv IS NULL || tYestoday.hourSumUv IS NULL,\n" +
                    "\t\t\t\t0,\n" +
                    "\t\t\t\t(tToday.hourSumUv / tYestoday.hourSumUv) - 1\n" +
                    "\t\t\t) AS ratio,\n" +
                    "\t\t\ttBeforeYestodayAvg.avgUv AS beforeYestodayAvg\n" +
                    "\t\tFROM (\n" +
                    "\t\t\t\tSELECT tTodayEnter.shop_id,\n" +
                    "\t\t\t\t\ttTodayEnter.hourSumUv - tTodayLeave.hourSumUv AS hourSumUv\n" +
                    "\t\t\t\tFROM (\n" +
                    "\t\t\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\t\t\tSUM(uv) AS hourSumUv\n" +
                    "\t\t\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\t\t\tAND `day` = '" + day + "'\n" +
                    "\t\t\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t\t\t) tTodayEnter\n" +
                    "\t\t\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\t\t\tSUM(uv) AS hourSumUv\n" +
                    "\t\t\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\t\t\tAND `day` = '" + day + "'\n" +
                    "\t\t\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\t\t\t\tAND `status` = \"LEAVE\"\n" +
                    "\t\t\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t\t\t) tTodayLeave ON tTodayEnter.shop_id = tTodayLeave.shop_id\n" +
                    "\t\t\t) tToday\n" +
                    "\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t# 昨天 \n" +
                    "\t\t\t\tSELECT tTodayEnter.shop_id,\n" +
                    "\t\t\t\t\ttTodayEnter.hourSumUv - tTodayLeave.hourSumUv AS hourSumUv\n" +
                    "\t\t\t\tFROM (\n" +
                    "\t\t\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\t\t\tSUM(uv) AS hourSumUv\n" +
                    "\t\t\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\t\t\tAND `day` = DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\t\t\tAND time_tag = \"h\"\n" +
                    "\t\t\t\t\t\t\tAND `hour` <= HOUR(NOW())\n" +
                    "\t\t\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t\t\t) tTodayEnter\n" +
                    "\t\t\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\t\t\tSUM(uv) AS hourSumUv\n" +
                    "\t\t\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\t\t\tAND `day` = DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\t\t\tAND time_tag = \"h\"\n" +
                    "\t\t\t\t\t\t\tAND `hour` <= HOUR(NOW())\n" +
                    "\t\t\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\t\t\t\tAND `status` = \"LEAVE\"\n" +
                    "\t\t\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t\t\t) tTodayLeave ON tTodayEnter.shop_id = tTodayLeave.shop_id\n" +
                    "\t\t\t) tYestoday ON tToday.`shop_id` = tYestoday.`shop_id`\n" +
                    "\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t# 昨天之前\n" +
                    "\t\t\t\tSELECT tTodayEnter.shop_id,\n" +
                    "\t\t\t\t\ttTodayEnter.avgUv - tTodayLeave.avgUv AS avgUv\n" +
                    "\t\t\t\tFROM (\n" +
                    "\t\t\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\t\t\tAVG(uv) AS avgUv\n" +
                    "\t\t\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\t\t\tAND `day` <= DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\t\t\tAND `day` >= DATE_SUB('" + day + "', INTERVAL 31 DAY)\n" +
                    "\t\t\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t\t\t) tTodayEnter\n" +
                    "\t\t\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\t\t\tAVG(uv) AS avgUv\n" +
                    "\t\t\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\t\t\tAND `day` <= DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\t\t\tAND `day` >= DATE_SUB('" + day + "', INTERVAL 31 DAY)\n" +
                    "\t\t\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\t\t\t\tAND `status` = \"LEAVE\"\n" +
                    "\t\t\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t\t\t) tTodayLeave ON tTodayEnter.shop_id = tTodayLeave.shop_id\n" +
                    "\t\t\t) tBeforeYestodayAvg ON tToday.`shop_id` = tBeforeYestodayAvg.`shop_id`\n" +
                    "\t\tUNION ALL\n" +
                    "\t\tSELECT '" + curAvgStayTimeTitle + "' AS title,\n" +
                    "\t\t\t ROUND(IFNULL(tToday.hourSumUv, 0) / 60000,2)  AS numValue,\n" +
                    "\t\t\t\"min\" AS numUnit,\n" +
                    "\t\t\tIFNULL(tYestoday.hourSumUv, 0) AS numValueYestoday,\n" +
                    "\t\t\tIF (\n" +
                    "\t\t\t\ttToday.hourSumUv IS NULL || tYestoday.hourSumUv IS NULL,\n" +
                    "\t\t\t\t0,\n" +
                    "\t\t\t\t(tToday.hourSumUv / tYestoday.hourSumUv) - 1\n" +
                    "\t\t\t) AS ratio,\n" +
                    "\t\t\tROUND(IFNULL(tBeforeYestodayAvg.avgUv, 0) / 60000,2)  AS beforeYestodayAvg\n" +
                    "\t\tFROM (\n" +
                    "\t\t\t\t#今天\n" +
                    "\t\t\t\tSELECT `day`,\n" +
                    "\t\t\t\t\tshop_id,\n" +
                    "\t\t\t\t\tAVG(stay_time_avg) AS hourSumUv\n" +
                    "\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\tAND `day` = '" + day + "'\n" +
                    "\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\tGROUP BY `day`,\n" +
                    "\t\t\t\t\tshop_id\n" +
                    "\t\t\t) tToday\n" +
                    "\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t# 昨天\n" +
                    "\t\t\t\tSELECT `day`,\n" +
                    "\t\t\t\t\tshop_id,\n" +
                    "\t\t\t\t\tAVG(stay_time_avg) AS hourSumUv\n" +
                    "\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\tAND `day` = DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\tAND time_tag = \"h\"\n" +
                    "\t\t\t\t\tAND `hour` <= HOUR(NOW())\n" +
                    "\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\tGROUP BY `day`,\n" +
                    "\t\t\t\t\tshop_id\n" +
                    "\t\t\t) tYestoday ON tToday.`shop_id` = tYestoday.`shop_id`\n" +
                    "\t\t\tLEFT JOIN (\n" +
                    "\t\t\t\t# 昨天之前\n" +
                    "\t\t\t\tSELECT shop_id,\n" +
                    "\t\t\t\t\tAVG(stay_time_avg) AS avgUv\n" +
                    "\t\t\t\tFROM mall_statistics_record\n" +
                    "\t\t\t\tWHERE 1 = 1\n" +
                    "\t\t\t\t\tAND `day` <= DATE_SUB('" + day + "', INTERVAL 1 DAY)\n" +
                    "\t\t\t\t\tAND `day` >= DATE_SUB('" + day + "', INTERVAL 31 DAY)\n" +
                    "\t\t\t\t\tAND shop_id = '" + shopId + "'\n" +
                    "\t\t\t\t\tAND time_tag = \"d\"\n" +
                    "\t\t\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "\t\t\t\t\tAND region_type_code = \"ALL\"\n" +
                    "\t\t\t\t\tAND `status` = \"ENTER\"\n" +
                    "\t\t\t\t\tAND entrance_tag = 0\n" +
                    "\t\t\t\tGROUP BY shop_id\n" +
                    "\t\t\t) tBeforeYestodayAvg ON tToday.`shop_id` = tBeforeYestodayAvg.`shop_id`\n" +
                    "\t) tAll ON tBase.title = tAll.title";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            double uvListValue = entities[0].getDoubleField("numValue");
            System.err.println("数据sql的pv值" + uvListValue);

            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_realtime_data").where("data", "like", DateTimeUtil.getFormat(new Date(),"yyyy-MM-dd HH") + "%")
                    .and("environment", "=", "online").and("source", "=", "UV实时数据").end();
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int uvListValue0 = entities0[0].getIntField("map_value");
            System.err.println("数据sql的pv值" + uvListValue0 );
            Preconditions.checkArgument(uvListValue == uvListValue0, !("数据sql查询=" + uvListValue).equals("自建sql查询" + uvListValue0));

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("uv实时数据一致性");
        }
    }

    @Test(dataProvider = "floorRealtime" , dataProviderClass = DataProviderMethod.class)//等待调试
    public void floorRealtimeUv(String lId,String floorName,String levalName) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDay = dt.getHistoryDate(-1);
            String endDay = dt.getHistoryDate(-1);
            String shopId = "33467";
            String sql =
                            "SELECT IFNULL(SUM(tBase.uv), 0) AS uvEnter,\n" +
                            "    IFNULL(SUM(tLeave.uv), 0) AS uvLeave,\n" +
                            "    IFNULL(SUM(tBase.uv), 0) - IFNULL(SUM(tLeave.uv), 0) AS uv,\n" +
                            "    SUM(tBase.uv / tBase.total_rate_uv) AS base_all_shop_uv,\n" +
                            "    SUM(tBase.uv) / SUM(tBase.uv / tBase.total_rate_uv) AS base_floor_uv_rate,\n" +
                            "    SUM(tAllShop.uv) AS all_shop_uv_enter,\n" +
                            "    IFNULL(SUM(tAllShop.uv), 0) - IFNULL(SUM(tAllShopLeave.uv), 0) AS all_shop_uv,\n" +
                            "    SUM(tBase.uv) / SUM(tAllShop.uv) AS floor_uv_rate_enter,\n" +
                            "    (\n" +
                            "        IFNULL(SUM(tBase.uv), 0) - IFNULL(SUM(tLeave.uv), 0)\n" +
                            "    ) /(\n" +
                            "        IFNULL(SUM(tAllShop.uv), 0) - IFNULL(SUM(tAllShopLeave.uv), 0)\n" +
                            "    ) AS floor_uv_rate,\n" +
                            "    IFNULL(SUM(tPreCycle.uv), 0) AS pre_uv_hour_acc_enter,\n" +
                            "    IFNULL(SUM(tPreCycle.uv), 0) - IFNULL(SUM(tPreCycleLeave.uv), 0) AS pre_uv_hour_acc,\n" +
                            "    (\n" +
                            "        (\n" +
                            "            IFNULL(SUM(tBase.uv), 0) - IFNULL(SUM(tLeave.uv), 0)\n" +
                            "        ) - (\n" +
                            "            IFNULL(SUM(tPreCycle.uv), 0) - IFNULL(SUM(tPreCycleLeave.uv), 0)\n" +
                            "        )\n" +
                            "    ) / (\n" +
                            "        IFNULL(SUM(tPreCycle.uv), 0) - IFNULL(SUM(tPreCycleLeave.uv), 0)\n" +
                            "    ) AS pre_rate,\n" +
                            "    IFNULL(SUM(tMoreFloor.uv), 0) AS floor_uv,\n" +
                            "    IFNULL(SUM(tMoreFloor.uv), 0) / SUM(tBase.uv) AS floor_rate,\n" +
                            "    IFNULL(SUM(tPrecycleMoreFloor.uv), 0) AS pre_floor_uv,\n" +
                            "    (\n" +
                            "        IFNULL(SUM(tMoreFloor.uv), 0) - IFNULL(SUM(tPrecycleMoreFloor.uv), 0)\n" +
                            "    ) / IFNULL(SUM(tPrecycleMoreFloor.uv), 0) AS pre_floor_rate,\n" +
                            "    ROUND(\n" +
                            "        IFNULL(AVG(tBase.stay_time_avg), 0) / 60000,\n" +
                            "        2\n" +
                            "    ) AS stay_time_avg,\n" +
                            "    IFNULL(AVG(tPreCycle.stay_time_avg), 0) AS pre_stay_time_avg,\n" +
                            "    (\n" +
                            "        IFNULL(AVG(tBase.stay_time_avg), 0) - IFNULL(AVG(tPreCycle.stay_time_avg), 0)\n" +
                            "    ) / IFNULL(AVG(tPreCycle.stay_time_avg), 0) AS pre_stay_time_avg_rate\n" +
                            "FROM (\n" +
                            "        SELECT *\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= '" + startDay + "'\n" +
                            "            AND `day` <= '" + endDay + "'\n" +
                            "            AND `status` = \"ENTER\"\n" +
                            "            AND time_tag = \"d\"\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND region_type_code = \"FLOOR\"\n" +
                            "            AND region_id IN (\n" +
                            "                SELECT DISTINCT region_id\n" +
                            "                FROM dwd_retail_region_detail_td\n" +
                            "                WHERE scope_id = '" + shopId + "'\n" +
                            "                    AND layout_id = '" + lId + "'\n" +
                            "            )\n" +
                            "    ) tBase\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT shop_id,\n" +
                            "            SUM(uv) uv,\n" +
                            "            SUM(pv) pv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= '" + startDay + "'\n" +
                            "            AND `day` <= '" + endDay + "'\n" +
                            "            AND `status` = \"LEAVE\"\n" +
                            "            AND time_tag = \"d\"\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND region_type_code = \"FLOOR\"\n" +
                            "            AND region_id IN (\n" +
                            "                SELECT DISTINCT region_id\n" +
                            "                FROM dwd_retail_region_detail_td\n" +
                            "                WHERE scope_id = '" + shopId + "'\n" +
                            "                    AND layout_id = '" + lId + "'\n" +
                            "            )\n" +
                            "        GROUP BY shop_id\n" +
                            "    ) tLeave ON tLeave.shop_id = tBase.shop_id\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT shop_id,\n" +
                            "            SUM(uv) AS uv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= '" + startDay + "'\n" +
                            "            AND `day` <= '" + endDay + "'\n" +
                            "            AND `status` = \"ENTER\"\n" +
                            "            AND time_tag = \"d\"\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND region_type_code = \"ALL\"\n" +
                            "    ) tAllShop ON tBase.shop_id = tAllShop.shop_id\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT shop_id,\n" +
                            "            SUM(uv) AS uv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= '" + startDay + "'\n" +
                            "            AND `day` <= '" + endDay + "'\n" +
                            "            AND `status` = \"LEAVE\"\n" +
                            "            AND time_tag = \"d\"\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND region_type_code = \"ALL\"\n" +
                            "    ) tAllShopLeave ON tBase.shop_id = tAllShopLeave.shop_id\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT `day`,\n" +
                            "            SUM(uv) as uv,\n" +
                            "            AVG(stay_time_avg) AS stay_time_avg\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= DATE_SUB(\n" +
                            "                '" + startDay + "',\n" +
                            "                INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "            )\n" +
                            "            AND `day` <= DATE_SUB(\n" +
                            "                '" + endDay + "',\n" +
                            "                INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "            )\n" +
                            "            AND `status` = \"ENTER\"\n" +
                            "            AND time_tag = \"h\"\n" +
                            "\t\t\t\t\t\tAND `hour` <= HOUR(NOW())\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND region_type_code = \"FLOOR\"\n" +
                            "            AND region_id IN (\n" +
                            "                SELECT DISTINCT region_id\n" +
                            "                FROM dwd_retail_region_detail_td\n" +
                            "                WHERE scope_id = '" + shopId + "'\n" +
                            "                    AND layout_id = '" + lId + "'\n" +
                            "            )\n" +
                            "        GROUP BY `day`\n" +
                            "    ) tPreCycle ON DATE_SUB(\n" +
                            "        tBase.`day`,\n" +
                            "        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "    ) = tPreCycle.`day`\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT `day`,\n" +
                            "            SUM(uv) as uv,\n" +
                            "            AVG(stay_time_avg) AS stay_time_avg\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= DATE_SUB(\n" +
                            "                '" + startDay + "',\n" +
                            "                INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "            )\n" +
                            "            AND `day` <= DATE_SUB(\n" +
                            "                '" + endDay + "',\n" +
                            "                INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "            )\n" +
                            "            AND `status` = \"LEAVE\"\n" +
                            "            AND time_tag = \"h\"\n" +
                            "\t\t\t\t\t\tAND `hour` <= HOUR(NOW())\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND region_type_code = \"FLOOR\"\n" +
                            "            AND region_id IN (\n" +
                            "                SELECT DISTINCT region_id\n" +
                            "                FROM dwd_retail_region_detail_td\n" +
                            "                WHERE scope_id = '" + shopId + "'\n" +
                            "                    AND layout_id = '" + lId + "'\n" +
                            "            )\n" +
                            "        GROUP BY `day`\n" +
                            "    ) tPreCycleLeave ON DATE_SUB(\n" +
                            "        tBase.`day`,\n" +
                            "        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "    ) = tPreCycleLeave.`day`\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT region_id,\n" +
                            "            SUM(uv) AS uv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= '" + startDay + "'\n" +
                            "            AND `day` <= '" + endDay + "'\n" +
                            "            AND `status` = \"ENTER\"\n" +
                            "            AND time_tag = \"d\"\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT DISTINCT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"layoutAnalysis\"\n" +
                            "                    AND chart_name = \"customerFlow\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND region_type_code = \"FLOOR\"\n" +
                            "            AND region_id IN (\n" +
                            "                SELECT DISTINCT region_id\n" +
                            "                FROM dwd_retail_region_detail_td\n" +
                            "                WHERE scope_id = '" + shopId + "'\n" +
                            "                    AND layout_id = '" + lId + "'\n" +
                            "            )\n" +
                            "    ) tMoreFloor ON tBase.region_id = tMoreFloor.region_id\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT region_id,\n" +
                            "            SUM(uv) AS uv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE shop_id = '" + shopId + "'\n" +
                            "            AND `day` >= DATE_SUB(\n" +
                            "                '" + startDay + "',\n" +
                            "                INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "            )\n" +
                            "            AND `day` <= DATE_SUB(\n" +
                            "                '" + endDay + "',\n" +
                            "                INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "            )\n" +
                            "            AND `status` = \"ENTER\"\n" +
                            "            AND time_tag = \"d\"\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT DISTINCT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"layoutAnalysis\"\n" +
                            "                    AND chart_name = \"customerFlow\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "            AND region_type_code = \"FLOOR\"\n" +
                            "            AND region_id IN (\n" +
                            "                SELECT DISTINCT region_id\n" +
                            "                FROM dwd_retail_region_detail_td\n" +
                            "                WHERE scope_id = '" + shopId + "'\n" +
                            "                    AND layout_id = '" + lId + "'\n" +
                            "            )\n" +
                            "    ) tPrecycleMoreFloor ON tBase.region_id = tMoreFloor.region_id";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            int uvFloorValue = entities[0].getIntField("uv");
            System.err.println("数据sql的pv值" + uvFloorValue);

            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_realtime_data").where("data", "like", DateTimeUtil.getFormat(new Date(),"yyyy-MM-dd HH") + "%")
                    .and("environment", "=", "online").and("source", "=", floorName).end();
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int uvEnterValue = entities0[0].getIntField("map_value");
            System.err.println("数据sql的v值" + uvEnterValue );

            Sql sql1 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_realtime_data").where("data", "like", DateTimeUtil.getFormat(new Date(),"yyyy-MM-dd HH") + "%")
                    .and("environment", "=", "online").and("source", "=", levalName).end();
            IEntity<?, ?>[] entities1 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql1);
            int uvLevalValue = entities1[0].getIntField("map_value");
            System.err.println("数据sql的v值" + uvLevalValue );

            int floorUv = uvEnterValue - uvLevalValue;
            System.err.println("离场uv值" + uvLevalValue );
            Preconditions.checkArgument(uvFloorValue == floorUv, !("数据sql查询=" + uvFloorValue).equals("自建sql查询" + floorUv));

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层uv实时数据一致性");
        }
    }

    @Test()//ok
    public void shopHourUvPv() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String day = dt.getHistoryDate(-0);
            String shopId = "33467";
            String sql =
                            "SELECT\n" +
                            "\ttHour.showHour AS `hour`,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\ttRight.pv\t,0)) AS pv,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\ttRight.uv\t,0)) AS uv,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\tSUM(tAccHour.uv)\t,0)) AS tAccHour_uv,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\tSUM(tAccHour.pv)\t,0)) AS tAccHour_pv,\n" +
                            "\tCEIL(IF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\tSUM(tAccHour.uv)/(tHour.realHour + 1)\t,0))) AS avg_uv,\n" +
                            "\tCEIL(IF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\tSUM(tAccHour.pv)/(tHour.realHour + 1)\t,0))) AS avg_pv,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\ttRight.device_bad_time\t,0)) AS device_bad_time,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(\ttRight.device_bad_rate\t,0)) AS device_bad_rate\n" +
                            "FROM\n" +
                            "\t(\n" +
                            "\tSELECT\n" +
                            "\t\t- 1 AS realHour,\n" +
                            "\t\t\"00:00\" AS showHour UNION\n" +
                            "\tSELECT\n" +
                            "\t\t0,\n" +
                            "\t\t\"01:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t1,\n" +
                            "\t\t\"02:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t2,\n" +
                            "\t\t\"03:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t3,\n" +
                            "\t\t\"04:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t4,\n" +
                            "\t\t\"05:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t5,\n" +
                            "\t\t\"06:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t6,\n" +
                            "\t\t\"07:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t7,\n" +
                            "\t\t\"08:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t8,\n" +
                            "\t\t\"09:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t9,\n" +
                            "\t\t\"10:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t10,\n" +
                            "\t\t\"11:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t11,\n" +
                            "\t\t\"12:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t12,\n" +
                            "\t\t\"13:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t13,\n" +
                            "\t\t\"14:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t14,\n" +
                            "\t\t\"15:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t15,\n" +
                            "\t\t\"16:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t16,\n" +
                            "\t\t\"17:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t17,\n" +
                            "\t\t\"18:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t18,\n" +
                            "\t\t\"19:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t19,\n" +
                            "\t\t\"20:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t20,\n" +
                            "\t\t\"21:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t21,\n" +
                            "\t\t\"22:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t22,\n" +
                            "\t\t\"23:00\" UNION\n" +
                            "\tSELECT\n" +
                            "\t\t23,\n" +
                            "\t\t\"\" \n" +
                            "\t) tHour\n" +
                            "\tLEFT JOIN (\n" +
                            "\tSELECT\n" +
                            "\t\ttMain.shop_id,\n" +
                            "\t\ttMain.`hour`,\n" +
                            "\t\ttMain.pv,\n" +
                            "\t\ttMain.uv,\n" +
                            "\t\tFLOOR(\n" +
                            "\t\tRAND() * ( 200 + 1 )) AS device_bad_time,# 设备损坏占比（可能会用到）\n" +
                            "\t\tROUND( RAND(), 2 ) AS device_bad_rate \n" +
                            "\tFROM\n" +
                            "\t\t(\n" +
                            "\t\tSELECT\n" +
                            "\t\t\tshop_id,\n" +
                            "\t\t\t`day`,\n" +
                            "\t\t\t`hour`,\n" +
                            "\t\t\tMAX( pv ) AS pv,\n" +
                            "\t\t\tMAX( uv ) AS uv \n" +
                            "\t\tFROM\n" +
                            "\t\t\tmall_statistics_record \n" +
                            "\t\tWHERE\n" +
                            "\t\t\t1 = 1 \n" +
                            "\t\t\tAND `day` = '" +day + "' \n" +
                            "\t\t\tAND shop_id = '" +shopId + "' \n" +
                            "\t\t\tAND region_type_code = \"ALL\" \n" +
                            "\t\t\tAND `status` = \"ENTER\" \n" +
                            "\t\t\tAND entrance_tag = 0 \n" +
                            "\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                            "\t\t\tAND time_tag = \"h\" \n" +
                            "\t\tGROUP BY\n" +
                            "\t\t\tshop_id,\n" +
                            "\t\t\t`day`,\n" +
                            "\t\t\t`hour` \n" +
                            "\t\tORDER BY\n" +
                            "\t\t\t`hour` \n" +
                            "\t\t) AS tMain \n" +
                            "\t) tRight ON tHour.realHour = tRight.`hour` \n" +
                            "\tLEFT JOIN(\n" +
                            "\t\tSELECT\n" +
                            "\t\t  `hour`,\n" +
                            "\t\t\tSUM( uv ) AS uv,\n" +
                            "\t\t\tSUM( pv ) AS pv\n" +
                            "\t\tFROM\n" +
                            "\t\t\tmall_statistics_record \n" +
                            "\t\tWHERE\n" +
                            "\t\t\t1 = 1 \n" +
                            "\t\t\tAND `day` = '" +day + "' \n" +
                            "\t\t\tAND shop_id = '" +shopId + "' \n" +
                            "\t\t\tAND region_type_code = \"ALL\" \n" +
                            "\t\t\tAND `status` = \"ENTER\" \n" +
                            "\t\t\tAND entrance_tag = 0 \n" +
                            "\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                            "\t\t\tAND time_tag = \"h\" \n" +
                            "\t\tGROUP BY \n" +
                            "\t\t\t`hour`\n" +
                            "\t) tAccHour ON tAccHour.`hour` <= tHour.realHour \n" +
                            "GROUP BY\n" +
                            "\ttHour.realHour \n" +
                            "ORDER BY\n" +
                            "\ttHour.realHour";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            int hour = LocalTime.now().getHour();
            int uvListValue = entities[hour].getIntField("uv");
            int pvListValue = entities[hour].getIntField("pv");
            System.err.println("数据sql的uv值" + uvListValue);
            System.err.println("数据sql的pv值" + pvListValue);

            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_moment_data").where("data", "like", DateTimeUtil.getFormat(new Date(),"yyyy-MM-dd HH") + "%")
                    .and("environment", "=", "online").and("source", "=", "UV实时数据").end();
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int uvListValue0 = entities0[0].getIntField("map_value");
            int pvListValue0 = entities0[0].getIntField("list_value");
            System.err.println("自查sql的uv值" + uvListValue0 );
            System.err.println("自查sql的pv值" + pvListValue0);
            Preconditions.checkArgument(uvListValue == uvListValue0, !("数据sql查询=" + uvListValue).equals("自建sql查询" + uvListValue0));
            Preconditions.checkArgument(pvListValue == pvListValue0, !("数据sql查询=" + pvListValue).equals("自建sql查询" + pvListValue0));

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("uv实时小时间数据一致性");
        }
    }

    //ok
    @Test(dataProvider = "floorRealtime",dataProviderClass = DataProviderMethod.class)
    public void floorHourUv(String lId,String floorName) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDay = dt.getHistoryDate(-0);
            String endDay = dt.getHistoryDate(-0);
            String shopId = "33467";
            String sql =
                            "SELECT\n" +
                            "\ttHour.showHour AS `hour`,\n" +
                            "\ttHour.realHour AS hourNum,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(MIN( tBase.uv ),0)) AS uv,\n" +
                            "\tIF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(SUM( tLessBase.uv ),0)) AS hour_avg_uv_sum ,\n" +
                            "\tCEIL(IF(tHour.realHour >= HOUR(NOW()),NULL,IFNULL(SUM( tLessBase.uv ) / (tHour.realHour + 1),0))) AS hour_avg_uv \n" +
                            "FROM\n" +
                            "\t(\n" +
                            "\t\tSELECT -1 AS realHour,\"00:00\" AS showHour\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 0 ,\"01:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 1 ,\"02:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 2 ,\"03:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 3 ,\"04:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 4 ,\"05:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 5 ,\"06:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 6 ,\"07:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 7 ,\"08:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 8 ,\"09:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 9 ,\"10:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 10 ,\"11:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 11 ,\"12:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 12 ,\"13:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 13 ,\"14:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 14 ,\"15:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 15 ,\"16:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 16 ,\"17:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 17 ,\"18:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 18 ,\"19:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 19 ,\"20:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 20 ,\"21:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 21 ,\"22:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 22 ,\"23:00\"\n" +
                            "\t\tUNION\n" +
                            "\t\tSELECT 23 ,\"\"\n" +
                            "\t) tHour\n" +
                            "\tLEFT JOIN\n" +
                            "\t(\n" +
                            "\tSELECT\n" +
                            "\t\t`hour`,\n" +
                            "\t\tSUM( uv ) AS uv \n" +
                            "\tFROM\n" +
                            "\t\tmall_statistics_record \n" +
                            "\tWHERE\n" +
                            "\t\tshop_id = '" +shopId + "' \n" +
                            "\t\tAND `day` >= '" + startDay + "' \n" +
                            "\t\tAND `day` <= '" + endDay + "' \n" +
                            "\t\tAND `status` = \"ENTER\" \n" +
                            "\t\tAND time_tag = \"h\" \n" +
                            "\t\tAND entrance_tag = 0 \n" +
                            "\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1) \n" +
                            "\t\tAND region_type_code = \"FLOOR\" \n" +
                            "\t\tAND region_id IN ( SELECT DISTINCT region_id FROM dwd_retail_region_detail_td WHERE scope_id = '" +shopId + "' AND layout_id = '" + lId + "' ) \n" +
                            "\tGROUP BY\n" +
                            "\t\t`hour` \n" +
                            "\t) tBase ON tHour.realHour = tBase.`hour`\n" +
                            "\tLEFT JOIN (\n" +
                            "\tSELECT\n" +
                            "\t\t`hour`,\n" +
                            "\t\tIFNULL(SUM( uv ) ,0) AS uv \n" +
                            "\tFROM\n" +
                            "\t\tmall_statistics_record \n" +
                            "\tWHERE\n" +
                            "\t\tshop_id = '" +shopId + "' \n" +
                            "\t\tAND `day` >= '" + startDay + "' \n" +
                            "\t\tAND `day` <= '" + endDay + "' \n" +
                            "\t\tAND `status` = \"ENTER\" \n" +
                            "\t\tAND time_tag = \"h\" \n" +
                            "\t\tAND entrance_tag = 0 \n" +
                            "\t\t\tAND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1) \n" +
                            "\t\tAND region_type_code = \"FLOOR\" \n" +
                            "\t\tAND region_id IN ( SELECT DISTINCT region_id FROM dwd_retail_region_detail_td WHERE scope_id = '" +shopId + "' AND layout_id = '" + lId + "' ) \n" +
                            "\tGROUP BY\n" +
                            "\t\t`hour` \n" +
                            "\t) tLessBase ON tHour.realHour >= tLessBase.`hour` \n" +
                            "GROUP BY\n" +
                            "\ttHour.realHour\n" +
                            "ORDER BY\n" +
                            "\ttHour.realHour;";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            int hour = LocalTime.now().getHour();
            int uvListValue = entities[hour].getIntField("uv");
            System.err.println("数据sql的uv值" + uvListValue);


            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_moment_data").where("data", "like", DateTimeUtil.getFormat(new Date(),"yyyy-MM-dd HH") + "%")
                    .and("environment", "=", "online").and("source", "=", floorName).end();
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int uvListValue0 = entities0[0].getIntField("map_value");
            System.err.println("自查sql的uv值" + uvListValue0 );
            Preconditions.checkArgument(uvListValue == uvListValue0, !("数据sql查询=" + uvListValue).equals("自建sql查询" + uvListValue0));

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层uv实时小时间数据一致性");
        }
    }
}
