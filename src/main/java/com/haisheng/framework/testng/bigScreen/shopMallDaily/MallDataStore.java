package com.haisheng.framework.testng.bigScreen.shopMallDaily;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.Constants;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.DataCheckRunner;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSTableData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.RuleDataSource;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.DetailMessage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.PvUvInfo;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class MallDataStore {
    private static final Logger logger = LoggerFactory.getLogger(MallDataStore.class);
    private PvUvInfo pvUvInfo;

    public IRow getRowByField(IRow[] rows, String field) {
        return Arrays.stream(rows).filter(e -> e.getField(Constants.RULE_COLUMN_FIELD).getValue().equals(field)).findFirst().orElse(null);
    }

    public JSONObject putField(JSONObject jsonObject, IRow row, String... field) {
        Arrays.stream(field).forEach(e -> jsonObject.put(row.getField(e).getKey(), row.getField(e).getValue()));
        return jsonObject;
    }

    @Test
    public void realTimeData() {
        List<DetailMessage> detailMessageList = pvUvInfo.getDetailMessages();
        detailMessageList.forEach(e -> {
            if (e.getName().contains("实时")) {
                String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
                Sql sql = Sql.instance()
                        .insert("t_mall_realtime_data")
                        .set("shop_id", "33467")
                        .set("source", e.getName())
                        .set("map_value", e.getNoReception())
                        .set("list_value", e.getHasReception())
                        .set("data", date)
                        .set("environment", "online")
                        .end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
            }
        });
    }

    @Test
    public void historyData() {
        List<DetailMessage> detailMessageList = pvUvInfo.getDetailMessages();
        detailMessageList.forEach(e -> {
            if (!e.getName().contains("实时")) {
                String date = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
                Sql sql = Sql.instance()
                        .insert("t_mall_history_data")
                        .set("shop_id", "33467")
                        .set("source", e.getName())
                        .set("map_value", e.getNoReception())
                        .set("list_value", e.getHasReception())
                        .set("data", date)
                        .set("environment", "online")
                        .end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
            }
        });
    }

    //    @BeforeClass
    public void initData() {
        pvUvInfo = new PvUvInfo();
        String rulePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/base/datacheck/rule/规则表.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").build();
        ITable[] fieldRuleTables = dataCheckRunner.getFieldRuleTables();
        //所有表的结果
        List<DetailMessage> detailMessages = new ArrayList<>();
        dataCheckRunner.getOtsTableDataList().stream().map(OTSTableData::initOTSRowData).forEach(otsTableData -> {
            List<OTSRowData> otsRowDataList = otsTableData.getRowDataList();
            Preconditions.checkNotNull(otsRowDataList);
            Arrays.stream(fieldRuleTables).filter(iTable -> iTable.getKey().contains(otsTableData.getName())).forEach(iTable -> {
                iTable.load();
                IRow[] fieldRuleRows = iTable.getRows();
                String[] regionIds = RuleDataSource.parse(getRowByField(fieldRuleRows, "region_id").getField(Constants.RULE_COLUMN_RANGE).getValue());
                String status = getRowByField(fieldRuleRows, "status").getField(Constants.RULE_COLUMN_RANGE).getValue();
                String isNull = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_NULL).getValue();
                String isReception = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_RECEPTION).getValue();
                logger.info("regionIds:{}", Arrays.toString(regionIds));
                logger.info("status:{}", status);
                logger.info("isNull:{}", isNull);
                logger.info("isReception:{}", isReception);
                Map<String, OTSRowData> map = new LinkedHashMap<>();
                List<OTSRowData> list = otsRowDataList.stream().filter(e -> e.getStatus().equals(status))
                        .filter(e -> Arrays.asList(regionIds).contains(e.getRegionId())).collect(Collectors.toCollection(LinkedList::new));
                list = isNull.equals("false") ? list.stream().filter(e -> !e.getUserId().equals("N")).collect(Collectors.toCollection(LinkedList::new)) : list;
                list.stream().filter(e -> isReception.equals("false")).forEach(e -> map.put(e.getUserId(), e));
                logger.info("去重结果：{}", map.values().size());
                logger.info("不去重结果：{}", list.size());
                DetailMessage detailMessage = new DetailMessage();
                detailMessage.setName(otsTableData.getName());
                detailMessage.setNoReception(map.values().size());
                detailMessage.setHasReception(list.size());
                detailMessages.add(detailMessage);
                logger.info("--------------------{}跑完---------------------------", otsTableData.getName());
            });
        });
        pvUvInfo.setShopId("33467");
        pvUvInfo.setDetailMessages(detailMessages);
    }

    @Test
    public void test() {
        String sql = "select source,map_value,list_value from t_mall_history_data where data like '2021-06-27%' and environment='online' and source='UV历史数据'";
        IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql);
        int listValue = entities[1].getIntField("list_value");
        System.err.println(listValue);
        String sql1 = "SET @startDay = \"2021-06-27\";\n" +
                "SET @endDay = \"2021-06-27\";\n" +
                "SET @shopId = \"33467\";\n" +
                "SET @regionId = \"33468\";\n" +
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
                "  \t\t\t\t\tAND `status` = \"ENTER\"\n" +
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
                "      \t\t\tAND `status` = \"ENTER\"\n" +
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
        IEntity<?, ?>[] entities1 = new Factory.Builder().container(EnumContainer.MALL_ONLINE.getContainer()).build().create(sql1);
        System.err.println(entities1.length);
//        int listValue = entities[1].getIntField("list_value");

    }
}

