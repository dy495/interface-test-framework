package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck;

import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.RuleDataSource;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.DetailMessage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.PvUvInfo;
import com.haisheng.framework.util.DateTimeUtil;
import org.jooq.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);
    private PvUvInfo pvUvInfo;

    @Test
    public void methodA() {
        IEntity<?, ?>[] entity = new Factory.Builder().build().createCsv("csv/a316052c-dcab-4abb-ac36-dba1d468e0c4.csv");
        IRow[] rows = Arrays.stream(entity).map(IEntity::getCurrent).toArray(IRow[]::new);
        List<OTSRowData> list = new LinkedList<>();
        Map<String, OTSRowData> map = new HashMap<>();
        Arrays.stream(rows).forEach(iRow -> list.addAll(JSONArray.parseArray(iRow.getField("region").getValue())
                .stream().map(e -> (JSONObject) e).map(e -> putField(e, iRow, "user_id", "start_time"))
                .map(e -> JSONObject.toJavaObject(e, OTSRowData.class)).collect(Collectors.toList())));
        long count = list.stream().filter(e -> (e.getRegionId().equals("33468") || e.getRegionId().equals("33489"))
                && e.getStatus().equals("ENTER") && !e.getUserId().equals("N"))
                .map(e -> map.put(e.getUserId(), e)).count();
        System.err.println(map.values().size());
        System.err.println(count);
    }

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
                Sql sql = Sql.instance().insert()
                        .from("t_shop_realtime_data")
                        .field("shop_id", "source", "map_value", "list_value", "data", "environment")
                        .setValue("33467", e.getName(), e.getNoReception(), e.getHasReception(), date, "online")
                        .end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql);
            }
        });
    }

    @Test
    public void historyData() {
        List<DetailMessage> detailMessageList = pvUvInfo.getDetailMessages();
        detailMessageList.forEach(e -> {
            if (!e.getName().contains("实时")) {
                String date = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
                Sql sql = Sql.instance().insert()
                        .from("t_shop_history_data")
                        .field("shop_id", "source", "map_value", "list_value", "data", "environment")
                        .setValue("33467", e.getName(), e.getNoReception(), e.getHasReception(), date, "online")
                        .end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql);
            }
        });
    }

    @BeforeClass
    public void initData() {
        pvUvInfo = new PvUvInfo();
        String rulePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/base/datacheck/rule/规则表.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").build();
        ITable[] fieldRuleTables = dataCheckRunner.getFieldRuleTables();
        //所有表的结果
        List<DetailMessage> detailMessages = new ArrayList<>();
        dataCheckRunner.getTableStoreList().forEach(otsTableData -> {
            otsTableData.initOTSRowData();
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
}

