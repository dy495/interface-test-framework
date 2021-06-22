package com.haisheng.framework.testng.bigScreen.shopMallDaily;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import org.testng.annotations.Test;

public class DataCheckCase {


    @Test
    public void test() {
        String startDay = "";
        String endDay = "";
        String shopId = "";
        String sql = "SET @startDay = \"" + startDay + "\";\n" +
                "SET @endDay = \"" + endDay + "\";\n" +
                "SET @shopId = \"" + shopId + "\";\n" +
                "SET @regionId = \"\";\n" +
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
                "            AND `day` >= @startDay\n" +
                "            AND `day` <= @endDay\n" +
                "            AND shop_id = @shopId\n" +
                "            AND time_tag = \"d\"\n" +
                "  \t\t\t\t\tAND `status` = \"ENTER\" \n" +
                "            AND IF(\n" +
                "                @regionId IS NOT NULL\n" +
                "                AND @regionId <> \"\",\n" +
                "                region_id = @regionId,\n" +
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
                "            AND `day` >= DATE_SUB(@startDay, INTERVAL 1 DAY)\n" +
                "            AND `day` <= DATE_SUB(@endDay, INTERVAL 1 DAY)\n" +
                "            AND shop_id = @shopId\n" +
                "            AND time_tag = \"d\"\n" +
                "      \t\t\tAND `status` = \"ENTER\" \n" +
                "            AND IF(\n" +
                "                @regionId IS NOT NULL\n" +
                "                AND @regionId <> \"\",\n" +
                "                region_id = @regionId,\n" +
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
                "        WHERE `weather_date` >= @startDay\n" +
                "            AND `weather_date` <= @endDay\n" +
                "    ) tWeather ON tBase.`day` = tWeather.weather_date\n" +
                "    AND tShop.district_code = tWeather.district_code\n" +
                "    LEFT JOIN (\n" +
                "        SELECT *\n" +
                "        FROM holiday\n" +
                "        WHERE festival_date >= @startDay\n" +
                "            AND `festival_date` <= @endDay\n" +
                "    ) tHoliday ON tBase.`day` = tHoliday.festival_date\n" +
                "ORDER BY shop_id,\n" +
                "    `day`";
        new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql);
    }
}
