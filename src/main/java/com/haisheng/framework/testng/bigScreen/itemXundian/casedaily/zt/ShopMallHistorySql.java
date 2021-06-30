package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.zt;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumJobName;
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


public class ShopMallHistorySql extends TestCaseCommon implements TestCaseStd {
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

    @Test()//ok
    public void historyUvPv() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDay = dt.getHistoryDate(-1);
            String endDay = dt.getHistoryDate(-1);
            String shopId = "33467";
            String regionId = "33468";
            String sql = "SELECT tBase.shop_id,\n" +
                    "    tBase.`day`,\n" +
                    "    CONCAT(\n" +
                    "        tBase.`day`,\n" +
                    "        \",\",\n" +
                    "        tWeather.weather_name,\n" +
                    "        \",\",\n" +
                    "        tWeather.weather_icon_url,\n" +
                    "        \",\",\n" +
                    "        IF (\n" +
                    "            tHoliday.holiday_name IS NULL\n" +
                    "            OR tHoliday.holiday_name = \"\",\n" +
                    "            CASE\n" +
                    "                DAYOFWEEK(tBase.`day`)\n" +
                    "                WHEN 7 THEN \"周六\"\n" +
                    "                WHEN 1 THEN \"周日\"\n" +
                    "                ELSE \"\"\n" +
                    "            END,\n" +
                    "            tHoliday.holiday_name\n" +
                    "        )\n" +
                    "    ) AS day_weather_festival,\n" +
                    "    tShop.district_code,\n" +
                    "    tWeather.weather_name,\n" +
                    "    tWeather.weather_icon_url AS weather_icon,\n" +
                    "    tBase.pv,\n" +
                    "    tBase.uv,\n" +
                    "    tPreCycle.pv AS preCyclePv,\n" +
                    "    tPreCycle.uv AS preCycleuv,\n" +
                    "    (tBase.uv - tPreCycle.uv) / tBase.uv AS uv_ratio,\n" +
                    "    (tBase.pv - tPreCycle.pv) / tBase.pv AS pv_ratio,\n" +
                    "    IF (\n" +
                    "        tHoliday.holiday_name IS NULL\n" +
                    "        OR tHoliday.holiday_name = \"\",\n" +
                    "        CASE\n" +
                    "            DAYOFWEEK(tBase.`day`)\n" +
                    "            WHEN 7 THEN \"周六\"\n" +
                    "            WHEN 1 THEN \"周日\"\n" +
                    "            ELSE NULL\n" +
                    "        END,\n" +
                    "        tHoliday.holiday_name\n" +
                    "    ) AS festival_name\n" +
                    "FROM (\n" +
                    "        SELECT shop_id,\n" +
                    "            `day`,\n" +
                    "            MAX(pv) AS pv,\n" +
                    "            MAX(uv) AS uv\n" +
                    "        FROM mall_statistics_record\n" +
                    "        WHERE 1 = 1\n" +
                    "            AND `day` >= '" + startDay + "'\n" +
                    "            AND `day` <= '" + endDay + "'\n" +
                    "            AND shop_id = '" + shopId + "'\n" +
                    "            AND time_tag = \"d\"\n" +
                    "  \t\t\t\t\tAND `status` = \"ENTER\" \n" +
                    "            AND IF(\n" +
                    "                '" + regionId + "' IS NOT NULL\n" +
                    "                AND '" + regionId + "' <> \"\",\n" +
                    "                region_id = '" + regionId + "',\n" +
                    "                region_type_code = \"ALL\"\n" +
                    "            )\n" +
                    "            AND entrance_tag = 0\n" +
                    "            AND cust_group_id = (\n" +
                    "                SELECT cust_group_code\n" +
                    "                FROM mall_chart_cust_group\n" +
                    "                WHERE page_name = \"ALL\"\n" +
                    "                    AND chart_name = \"ALL\"\n" +
                    "                    AND label = \"ALL\"\n" +
                    "                LIMIT 1\n" +
                    "            )\n" +
                    "        GROUP BY shop_id,\n" +
                    "            `day`\n" +
                    "    ) tBase\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT shop_id,\n" +
                    "            `day`,\n" +
                    "            MAX(pv) AS pv,\n" +
                    "            MAX(uv) AS uv\n" +
                    "        FROM mall_statistics_record\n" +
                    "        WHERE 1 = 1\n" +
                    "            AND `day` >= DATE_SUB('" + startDay + "', INTERVAL 1 DAY)\n" +
                    "            AND `day` <= DATE_SUB('" + endDay + "', INTERVAL 1 DAY)\n" +
                    "            AND shop_id = '" + shopId + "'\n" +
                    "            AND time_tag = \"d\"\n" +
                    "      \t\t\tAND `status` = \"ENTER\" \n" +
                    "            AND IF(\n" +
                    "                '" + regionId + "' IS NOT NULL\n" +
                    "                AND '" + regionId + "' <> \"\",\n" +
                    "                region_id = '" + regionId + "',\n" +
                    "                region_type_code = \"ALL\"\n" +
                    "            )\n" +
                    "            AND entrance_tag = 0\n" +
                    "            AND cust_group_id = (\n" +
                    "                SELECT cust_group_code\n" +
                    "                FROM mall_chart_cust_group\n" +
                    "                WHERE page_name = \"ALL\"\n" +
                    "                    AND chart_name = \"ALL\"\n" +
                    "                    AND label = \"ALL\"\n" +
                    "                LIMIT 1\n" +
                    "            )\n" +
                    "        GROUP BY shop_id,\n" +
                    "            `day`\n" +
                    "    ) tPreCycle ON DATE_SUB(tBase.`day`, INTERVAL 1 DAY) = tPreCycle.`day`\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT shop_id,\n" +
                    "            district_code\n" +
                    "        FROM shop\n" +
                    "    ) tShop ON tBase.shop_id = tShop.shop_id\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT *\n" +
                    "        FROM weather\n" +
                    "        WHERE `weather_date` >= '" + startDay + "'\n" +
                    "            AND `weather_date` <= '" + endDay + "'\n" +
                    "    ) tWeather ON tBase.`day` = tWeather.weather_date\n" +
                    "    AND tShop.district_code = tWeather.district_code\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT *\n" +
                    "        FROM holiday\n" +
                    "        WHERE festival_date >= '" + startDay + "'\n" +
                    "            AND `festival_date` <= '" + endDay + "'\n" +
                    "    ) tHoliday ON tBase.`day` = tHoliday.festival_date\n" +
                    "ORDER BY shop_id,\n" +
                    "    `day`";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            int pvlistValue = entities[0].getIntField("pv");
            int uvlistValue = entities[0].getIntField("uv");
            System.err.println("数据sql的pv值" + pvlistValue);
            System.err.println("自查sql的uv值" + uvlistValue);

            String sql0 = "select source,map_value,list_value from t_mall_history_data where data like '" + dt.getHistoryDate(-1) + "%' and environment='online' and source='UV历史数据'";
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int pvlistValue0 = entities0[0].getIntField("list_value");
            int uvlistValue0 = entities0[0].getIntField("map_value");
            System.err.println("数据sql的pv值" + pvlistValue0);
            System.err.println("自查sql的uv值" + uvlistValue0);
            Preconditions.checkArgument(pvlistValue == pvlistValue0, !("数据sql查询=" + pvlistValue).equals("自建sql查询" + pvlistValue0));
            Preconditions.checkArgument(uvlistValue == uvlistValue0, !("数据sql查询=" + uvlistValue).equals("自建sql查询" + uvlistValue0));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("uv、pv历史数据一致性");
        }
    }

    @Test(dataProvider = "floorHistory", dataProviderClass = DataProviderMethod.class)//ok
    public void floorHistoryUvPv(String lId, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            logger.info("floorid:{}", lId);
            logger.info("floorname:{}", name);
            String startDay = dt.getHistoryDate(-1);
            String endDay = dt.getHistoryDate(-1);
            String shopId = "33467";
            String sql =
                    "SELECT SUM(tBase.uv) AS uv,\n" +
                            "    SUM(tBase.uv / tBase.total_rate_uv) AS base_all_shop_uv,\n" +
                            "    SUM(tBase.uv) / SUM(tBase.uv / tBase.total_rate_uv) AS base_floor_uv_rate,\n" +
                            "    SUM(tAllShop.uv) AS all_shop_uv,\n" +
                            "    SUM(tBase.uv) / SUM(tAllShop.uv) AS floor_uv_rate,\n" +
                            "    IFNULL(SUM(tPreCycle.uv), 0) AS pre_uv,\n" +
                            "    (\n" +
                            "        SUM(tBase.uv) - IFNULL(SUM(tPreCycle.uv), 0)\n" +
                            "    ) AS pre_sub,\n" +
                            "    (\n" +
                            "        SUM(tBase.uv) - IFNULL(SUM(tPreCycle.uv), 0)\n" +
                            "    ) / IFNULL(SUM(tPreCycle.uv), 0) AS pre_rate,\n" +
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
                            "        GROUP BY `day`\n" +
                            "    ) tPreCycle ON DATE_SUB(\n" +
                            "        tBase.`day`,\n" +
                            "        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                            "    ) = tPreCycle.`day`\n" +
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
            IEntity<?, ?> entity = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql)[0];
            int uv = entity.getIntField("uv");
            logger.info("数据sql的uv值：{}", uv);
            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_history_data").where("data", "like", dt.getHistoryDate(-1) + "%")
                    .and("environment", "=", "online").and("source", "=", name).end();
            IEntity<?, ?> entity1 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0)[0];
            int mapValue = entity1.getIntField("map_value");
            logger.info("自查sql的uv值：{}", mapValue);
            Preconditions.checkArgument(uv == mapValue, !("数据sql查询=" + uv).equals("自建sql查询" + mapValue));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("楼层L1,L2,B1,uv历史数据一致性");
        }
    }


    @Test()//ok
    public void shopPassUv() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDay = dt.getHistoryDate(-1);
            String endDay = dt.getHistoryDate(-1);
            String shopId = "33467";
            String regionId = "33515";
            String sql =
                    "SELECT tEnter.*,\n" +
                    "\t\ttPass.*,\n" +
                    "    tAllStore.*,\n" +
                    "    ROUND(\n" +
                    "        IFNULL(tEnter.numValueEnter / tAllStore.numValueAllStore, 0),\n" +
                    "        2\n" +
                    "    ) AS numValueEnterRatio,\n" +
                    "    ROUND(\n" +
                    "        IFNULL(\n" +
                    "            (\n" +
                    "                tEnter.numValueYestodayEnter / tAllStore.numValueYestodayAllStore\n" +
                    "            ),\n" +
                    "            0\n" +
                    "        ),\n" +
                    "        2\n" +
                    "    ) AS beforeYestodayEnterRatio,\n" +
                    "    ROUND(\n" +
                    "        IFNULL(\n" +
                    "            (tEnter.numValueEnter / tAllStore.numValueAllStore) /(\n" +
                    "                tEnter.numValueYestodayEnter / tAllStore.numValueYestodayAllStore\n" +
                    "            ) - 1,\n" +
                    "            0\n" +
                    "        ),\n" +
                    "        2\n" +
                    "    ) AS ratioEnterRatio,\n" +
                    "    tAvgStay.*\n" +
                    "FROM (\n" +
                    "        SELECT IFNULL(tToday.daySumUv, 0) AS numValueEnter,\n" +
                    "            IFNULL(tYestoday.daySumUv, 0) AS numValueYestodayEnter,\n" +
                    "            ROUND(\n" +
                    "                IFNULL(\n" +
                    "                    IF (\n" +
                    "                        tToday.daySumUv IS NULL || tYestoday.daySumUv IS NULL,\n" +
                    "                        - 1,\n" +
                    "                        (tToday.daySumUv / tYestoday.daySumUv) - 1\n" +
                    "                    ),\n" +
                    "                    0\n" +
                    "                ),\n" +
                    "                2\n" +
                    "            ) AS ratioEnter,\n" +
                    "            ROUND(tBeforeYestodayAvg.avgUv, 2) AS beforeYestodayAvgEnter\n" +
                    "        FROM (\n" +
                    "                #今天到现在小时的累计\n" +
                    "                SELECT IFNULL(SUM(uv), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= '" + startDay + "'\n" +
                    "                    AND `day` <= '" + endDay + "'\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tToday\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天到与现在的小时累计\n" +
                    "                SELECT IFNULL(SUM(uv), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tYestoday ON 1 = 1\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天以及之前的平均\n" +
                    "                SELECT IFNULL(AVG(uv), 0) AS avgUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tBeforeYestodayAvg ON 1 = 1\n" +
                    "    ) tEnter,\n" +
                    "    (\n" +
                    "        SELECT ROUND(IFNULL(tToday.daySumUv, 0) / 60000, 2) AS numValueAvgStay,\n" +
                    "            ROUND(\n" +
                    "                IFNULL(\n" +
                    "                    IF (\n" +
                    "                        tToday.daySumUv IS NULL || tYestoday.daySumUv IS NULL,\n" +
                    "                        - 1,\n" +
                    "                        (tToday.daySumUv / tYestoday.daySumUv) - 1\n" +
                    "                    ),\n" +
                    "                    0\n" +
                    "                ),\n" +
                    "                2\n" +
                    "            ) AS ratioAvgStay,\n" +
                    "            ROUND(tBeforeYestodayAvg.avgUv, 2) AS beforeYestodayAvgAvgStay\n" +
                    "        FROM (\n" +
                    "                #今天到现在小时的累计\n" +
                    "                SELECT IFNULL(AVG(stay_time_avg), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= '" + startDay + "'\n" +
                    "                    AND `day` <= '" + endDay + "'\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tToday\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天到与现在的小时累计\n" +
                    "                SELECT IFNULL(AVG(stay_time_avg), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tYestoday ON 1 = 1\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天以及之前的平均\n" +
                    "                SELECT IFNULL(AVG(stay_time_avg), 0) AS avgUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tBeforeYestodayAvg ON 1 = 1\n" +
                    "    ) tAvgStay,\n" +
                    "    (\n" +
                    "        SELECT IFNULL(tToday.daySumUv, 0) AS numValueAllStore,\n" +
                    "            IFNULL(tYestoday.daySumUv, 0) AS numValueYestodayAllStore,\n" +
                    "            ROUND(\n" +
                    "                IFNULL(\n" +
                    "                    IF (\n" +
                    "                        tToday.daySumUv IS NULL || tYestoday.daySumUv IS NULL,\n" +
                    "                        - 1,\n" +
                    "                        (tToday.daySumUv / tYestoday.daySumUv) - 1\n" +
                    "                    ),\n" +
                    "                    0\n" +
                    "                ),\n" +
                    "                2\n" +
                    "            ) AS ratioAllStore,\n" +
                    "            ROUND(tBeforeYestodayAvg.avgUv, 2) AS beforeYestodayAvgAllStore\n" +
                    "        FROM (\n" +
                    "                #今天到现在小时的累计\n" +
                    "                SELECT IFNULL(SUM(uv), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= '" + startDay + "'\n" +
                    "                    AND `day` <= '" + endDay + "'\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND region_type_code = \"ALL\"\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tToday\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天到与现在的小时累计\n" +
                    "                SELECT IFNULL(SUM(uv), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND region_type_code = \"ALL\"\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tYestoday ON 1 = 1\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天以及之前的平均\n" +
                    "                SELECT IFNULL(AVG(uv), 0) AS avgUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND region_type_code = \"ALL\"\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"ENTER\"\n" +
                    "            ) tBeforeYestodayAvg ON 1 = 1\n" +
                    "    ) tAllStore,\n" +
                    "\t\t(\n" +
                    "        SELECT IFNULL(tToday.daySumUv, 0) AS numValuePass,\n" +
                    "            IFNULL(tYestoday.daySumUv, 0) AS numValueYestodayPass,\n" +
                    "            ROUND(\n" +
                    "                IFNULL(\n" +
                    "                    IF (\n" +
                    "                        tToday.daySumUv IS NULL || tYestoday.daySumUv IS NULL,\n" +
                    "                        - 1,\n" +
                    "                        (tToday.daySumUv / tYestoday.daySumUv) - 1\n" +
                    "                    ),\n" +
                    "                    0\n" +
                    "                ),\n" +
                    "                2\n" +
                    "            ) AS ratioPass,\n" +
                    "            ROUND(tBeforeYestodayAvg.avgUv, 2) AS beforeYestodayAvgPass\n" +
                    "        FROM (\n" +
                    "                #今天到现在小时的累计\n" +
                    "                SELECT IFNULL(SUM(uv), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= '" + startDay + "'\n" +
                    "                    AND `day` <= '" + endDay + "'\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"PASS\"\n" +
                    "            ) tToday\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天到与现在的小时累计\n" +
                    "                SELECT IFNULL(SUM(uv), 0) AS daySumUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"PASS\"\n" +
                    "            ) tYestoday ON 1 = 1\n" +
                    "            LEFT JOIN (\n" +
                    "                # 昨天以及之前的平均\n" +
                    "                SELECT IFNULL(AVG(uv), 0) AS avgUv\n" +
                    "                FROM mall_statistics_record\n" +
                    "                WHERE 1 = 1\n" +
                    "                    AND `day` >= DATE_SUB(\n" +
                    "                        '" + startDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND `day` <= DATE_SUB(\n" +
                    "                        '" + endDay + "',\n" +
                    "                        INTERVAL DATEDIFF('" + endDay + "', '" + startDay + "') + 1 DAY\n" +
                    "                    )\n" +
                    "                    AND shop_id = '" + shopId + "'\n" +
                    "                    AND time_tag = \"d\"\n" +
                    "                    AND cust_group_id =  (SELECT\tcust_group_code FROM\tmall_chart_cust_group WHERE\tpage_name = \"ALL\" AND chart_name = \"ALL\" AND label = \"ALL\" LIMIT 1)\n" +
                    "                    AND IF (\n" +
                    "                        '" + regionId + "' IS NOT NULL\n" +
                    "                        AND '" + regionId + "' <> \"\",\n" +
                    "                        region_id = '" + regionId + "',\n" +
                    "                        region_type_code = \"ALL\"\n" +
                    "                    )\n" +
                    "                    AND entrance_tag = 0\n" +
                    "                    AND `status` = \"PASS\"\n" +
                    "            ) tBeforeYestodayAvg ON 1 = 1\n" +
                    "    ) tPass";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            int floorPassUv = entities[0].getIntField("numValuePass");
            System.err.println("数据sql的过店值" + floorPassUv);

            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_history_data").where("data", "like", dt.getHistoryDate(-1) + "%")
                    .and("environment", "=", "online").and("source", "=", "门店历史过店客流").end();
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int floorPassUv1 = entities0[0].getIntField("map_value");
            System.err.println("自查sql的过店值" + floorPassUv1);
            Preconditions.checkArgument(floorPassUv == floorPassUv1, !("数据sql查询=" + floorPassUv).equals("自建sql查询" + floorPassUv1));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店过店uv，历史数据一致性");
        }
    }

    @Test()//ok
    public void shopEnterUvPv() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDay = dt.getHistoryDate(-1);
            String endDay = dt.getHistoryDate(-1);
            String shopId = "33467";
            String regionId = "33515";
            String sql =
                            "SELECT tBase.shop_id,\n" +
                            "    tBase.`day`,\n" +
                            "    CONCAT(\n" +
                            "        tBase.`day`,\n" +
                            "        \",\",\n" +
                            "        tWeather.weather_name,\n" +
                            "        \",\",\n" +
                            "        tWeather.weather_icon_url,\n" +
                            "        \",\",\n" +
                            "        IF (\n" +
                            "            tHoliday.holiday_name IS NULL\n" +
                            "            OR tHoliday.holiday_name = \"\",\n" +
                            "            CASE\n" +
                            "                DAYOFWEEK(tBase.`day`)\n" +
                            "                WHEN 7 THEN \"周六\"\n" +
                            "                WHEN 1 THEN \"周日\"\n" +
                            "                ELSE \"\"\n" +
                            "            END,\n" +
                            "            tHoliday.holiday_name\n" +
                            "        )\n" +
                            "    ) AS day_weather_festival,\n" +
                            "    tShop.district_code,\n" +
                            "    tWeather.weather_name,\n" +
                            "    tWeather.weather_icon_url AS weather_icon,\n" +
                            "    tBase.pv,\n" +
                            "    tBase.uv,\n" +
                            "    tPreCycle.pv AS preCyclePv,\n" +
                            "    tPreCycle.uv AS preCycleuv,\n" +
                            "    (tBase.uv - tPreCycle.uv) / tBase.uv AS uv_ratio,\n" +
                            "    (tBase.pv - tPreCycle.pv) / tBase.pv AS pv_ratio,\n" +
                            "    IF (\n" +
                            "        tHoliday.holiday_name IS NULL\n" +
                            "        OR tHoliday.holiday_name = \"\",\n" +
                            "        CASE\n" +
                            "            DAYOFWEEK(tBase.`day`)\n" +
                            "            WHEN 7 THEN \"周六\"\n" +
                            "            WHEN 1 THEN \"周日\"\n" +
                            "            ELSE NULL\n" +
                            "        END,\n" +
                            "        tHoliday.holiday_name\n" +
                            "    ) AS festival_name\n" +
                            "FROM (\n" +
                            "        SELECT shop_id,\n" +
                            "            `day`,\n" +
                            "            MAX(pv) AS pv,\n" +
                            "            MAX(uv) AS uv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE 1 = 1\n" +
                            "            AND `day` >= '" + startDay + "'\n" +
                            "            AND `day` <= '" + endDay + "'\n" +
                            "            AND shop_id = '" + shopId+ "'\n" +
                            "            AND time_tag = \"d\"\n" +
                            "  \t\t\t\t\tAND `status` = \"ENTER\" \n" +
                            "            AND IF(\n" +
                            "                '" + regionId+ "' IS NOT NULL\n" +
                            "                AND '" + regionId+ "' <> \"\",\n" +
                            "                region_id = '" + regionId+ "',\n" +
                            "                region_type_code = \"ALL\"\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "        GROUP BY shop_id,\n" +
                            "            `day`\n" +
                            "    ) tBase\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT shop_id,\n" +
                            "            `day`,\n" +
                            "            MAX(pv) AS pv,\n" +
                            "            MAX(uv) AS uv\n" +
                            "        FROM mall_statistics_record\n" +
                            "        WHERE 1 = 1\n" +
                            "            AND `day` >= DATE_SUB('" + startDay + "', INTERVAL 1 DAY)\n" +
                            "            AND `day` <= DATE_SUB('" + endDay + "', INTERVAL 1 DAY)\n" +
                            "            AND shop_id = '" + shopId+ "'\n" +
                            "            AND time_tag = \"d\"\n" +
                            "      \t\t\tAND `status` = \"ENTER\" \n" +
                            "            AND IF(\n" +
                            "                '" + regionId+ "' IS NOT NULL\n" +
                            "                AND '" + regionId+ "' <> \"\",\n" +
                            "                region_id = '" + regionId+ "',\n" +
                            "                region_type_code = \"ALL\"\n" +
                            "            )\n" +
                            "            AND entrance_tag = 0\n" +
                            "            AND cust_group_id = (\n" +
                            "                SELECT cust_group_code\n" +
                            "                FROM mall_chart_cust_group\n" +
                            "                WHERE page_name = \"ALL\"\n" +
                            "                    AND chart_name = \"ALL\"\n" +
                            "                    AND label = \"ALL\"\n" +
                            "                LIMIT 1\n" +
                            "            )\n" +
                            "        GROUP BY shop_id,\n" +
                            "            `day`\n" +
                            "    ) tPreCycle ON DATE_SUB(tBase.`day`, INTERVAL 1 DAY) = tPreCycle.`day`\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT shop_id,\n" +
                            "            district_code\n" +
                            "        FROM shop\n" +
                            "    ) tShop ON tBase.shop_id = tShop.shop_id\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT *\n" +
                            "        FROM weather\n" +
                            "        WHERE `weather_date` >= '" + startDay + "'\n" +
                            "            AND `weather_date` <= '" + endDay + "'\n" +
                            "    ) tWeather ON tBase.`day` = tWeather.weather_date\n" +
                            "    AND tShop.district_code = tWeather.district_code\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT *\n" +
                            "        FROM holiday\n" +
                            "        WHERE festival_date >= '" + startDay + "'\n" +
                            "            AND `festival_date` <= '" + endDay + "'\n" +
                            "    ) tHoliday ON tBase.`day` = tHoliday.festival_date\n" +
                            "ORDER BY shop_id,\n" +
                            "    `day`";
            IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
            int shopEnterUv = entities[0].getIntField("uv");
            int shopEnterPv = entities[0].getIntField("pv");
            System.err.println("数据sql的进店uv值" + shopEnterUv);
            System.err.println("数据sql的进店pv值" + shopEnterPv);

            Sql sql0 = Sql.instance().select("map_value", "list_value")
                    .from("t_mall_history_data").where("data", "like", dt.getHistoryDate(-1) + "%")
                    .and("environment", "=", "online").and("source", "=", "门店历史进店客流").end();
            IEntity<?, ?>[] entities0 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql0);
            int shopEnterUv1 = entities0[0].getIntField("map_value");
            int shopEnterPv1 = entities0[0].getIntField("list_value");
            System.err.println("自查sql的进店uv值" + shopEnterUv1);
            System.err.println("自查sql的进店pv值" + shopEnterPv1);
            Preconditions.checkArgument(shopEnterUv == shopEnterUv1, !("数据sql查询=" + shopEnterUv).equals("自建sql查询" + shopEnterUv1));
            Preconditions.checkArgument(shopEnterPv == shopEnterPv1, !("数据sql查询=" + shopEnterPv).equals("自建sql查询" + shopEnterPv1));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店进店uv、pv历史数据一致性");
        }
    }
}
