package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck;

import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.RuleDataSource;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

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
    public void methodB() {
        String rulePath = "D:\\JetBrains\\IntelliJ IDEAProjects\\interface-test-framework\\src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\crm\\wm\\base\\datacheck\\rule\\规则表.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").date("2021-06-17").build();
        ITable[] fieldRuleTables = dataCheckRunner.getFieldRuleTables();
        //所有表的结果
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
                if (isReception.equals("false")) {
                    list.forEach(e -> map.put(e.getUserId(), e));
                }
                logger.info("去重结果：{}", map.values().size());
                logger.info("不去重结果：{}", list.size());
                logger.info("--------------------{}跑完---------------------------", otsTableData.getName());
            });
        });
    }

    //比较算法
    @Test
    public void methodC() {
        IEntity<?, ?>[] entity = new Factory.Builder().build().createCsv("csv/a316052c-dcab-4abb-ac36-dba1d468e0c4.csv");
        IRow[] rows = Arrays.stream(entity).map(IEntity::getCurrent).toArray(IRow[]::new);
        List<OTSRowData> dataStructureList = new LinkedList<>();
        Arrays.stream(rows).forEach(iRow -> dataStructureList.addAll(JSONArray.parseArray(iRow.getField("region").getValue())
                .stream().map(e -> (JSONObject) e).map(e -> putField(e, iRow, "user_id", "start_time"))
                .map(e -> JSONObject.toJavaObject(e, OTSRowData.class)).collect(Collectors.toList())));
        IContainer container = new ExcelContainer.Builder().path("src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/base/datacheck/rule/规则表.xlsx").build();
        container.init();
        ITable containerTable = container.getTable(Constants.SHEET_TITLE_CONTAINER);
        containerTable.load();
        ITable dataSourceTable = container.getTable(Constants.SHEET_TITLE_DATA_SOURCE);
        dataSourceTable.load();
        IRow[] dataSourceTableRows = dataSourceTable.getRows();
        Arrays.stream(dataSourceTableRows).forEach(row -> {
            String fieldRuleTableName = Constants.SHEET_TITLE_COLUMN + row.getField(Constants.DATA_SOURCE_COLUMN_NAME).getValue();
            logger.info("表名:{}", fieldRuleTableName);
            ITable fieldRuleTable = container.getTable(fieldRuleTableName);
            fieldRuleTable.load();
            IRow[] fieldRuleRows = fieldRuleTable.getRows();
            Map<String, OTSRowData> map = new LinkedHashMap<>();
            String[] regionIds = RuleDataSource.parse(getRowByField(fieldRuleRows, "region_id").getField(Constants.RULE_COLUMN_RANGE).getValue());
            logger.info("regionIds:{}", Arrays.toString(regionIds));
            String status = getRowByField(fieldRuleRows, "status").getField(Constants.RULE_COLUMN_RANGE).getValue();
            logger.info("status:{}", status);
            String isNull = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_NULL).getValue();
            logger.info("isNull:{}", isNull);
            String isReception = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_RECEPTION).getValue();
            logger.info("isReception:{}", isReception);
            List<OTSRowData> list = dataStructureList.stream().filter(e -> e.getStatus().equals(status))
                    .filter(e -> Arrays.asList(regionIds).contains(e.getRegionId())).collect(Collectors.toCollection(LinkedList::new));
            list = isNull.equals("false") ? list.stream().filter(e -> !e.getUserId().equals("N")).collect(Collectors.toCollection(LinkedList::new)) : list;
            if (isReception.equals("false")) {
                list.forEach(e -> map.put(e.getUserId(), e));
            }
            System.err.println("去重:" + map.values().size());
            System.err.println("不去重" + list.size());
            logger.info("--------------------{}跑完---------------------------", fieldRuleTableName);
        });

    }

}

