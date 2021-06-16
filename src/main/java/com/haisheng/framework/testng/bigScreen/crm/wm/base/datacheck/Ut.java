package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck;


import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.DataSource;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.DataStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class Ut {
    private static final Logger logger = LoggerFactory.getLogger(Ut.class);

    @Test
    public void ss() {
        IEntity<?, ?>[] entity = new Factory.Builder().build().createCsv("csv/a316052c-dcab-4abb-ac36-dba1d468e0c4.csv");
        IRow[] rows = Arrays.stream(entity).map(IEntity::getCurrent).toArray(IRow[]::new);
        List<DataStructure> list = new LinkedList<>();
        Map<String, DataStructure> map = new HashMap<>();
        Arrays.stream(rows).forEach(iRow -> list.addAll(JSONArray.parseArray(iRow.getField("region").getValue())
                .stream().map(e -> (JSONObject) e).map(e -> putField(e, iRow, "user_id", "start_time"))
                .map(e -> JSONObject.toJavaObject(e, DataStructure.class)).collect(Collectors.toList())));
        long count = list.stream().filter(e -> (e.getRegionId().equals("33468") || e.getRegionId().equals("33489"))
                && e.getStatus().equals("ENTER") && !e.getUserId().equals("N"))
                .map(e -> map.put(e.getUserId(), e)).count();
        System.err.println(map.values().size());
        System.err.println(count);
    }

    @Test
    public void test() {
        IEntity<?, ?>[] entity = new Factory.Builder().build().createCsv("csv/dcbbde6f-2002-43fe-9857-3ef41da24c4b.csv");
        IRow[] rows = Arrays.stream(entity).map(IEntity::getCurrent).toArray(IRow[]::new);
        List<DataStructure> dataStructureList = new LinkedList<>();
        Arrays.stream(rows).forEach(iRow -> dataStructureList.addAll(JSONArray.parseArray(iRow.getField("region").getValue())
                .stream().map(e -> (JSONObject) e).map(e -> putField(e, iRow, "user_id", "start_time"))
                .map(e -> JSONObject.toJavaObject(e, DataStructure.class)).collect(Collectors.toList())));
        IContainer container = new ExcelContainer.Builder().path("src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/base/datacheck/rule/规则表.xlsx").build();
        container.init();
        ITable containerTable = container.getTable(Constants.SHEET_TITLE_CONTAINER);
        containerTable.load();
        ITable dataSourceTable = container.getTable(Constants.SHEET_TITLE_DATA_SOURCE);
        dataSourceTable.load();
        IRow[] dataSourceTableRows = dataSourceTable.getRows();
        IRow row = dataSourceTableRows[0];
        String fieldRuleTableName = Constants.SHEET_TITLE_COLUMN + row.getField(Constants.DATA_SOURCE_COLUMN_NAME).getValue();
        logger.info("表名:{}", fieldRuleTableName);
        ITable fieldRuleTable = container.getTable(fieldRuleTableName);
        fieldRuleTable.load();
        IRow[] fieldRuleRows = fieldRuleTable.getRows();
        Map<String, DataStructure> map = new LinkedHashMap<>();
        String[] regionIds = DataSource.parse(getRowByField(fieldRuleRows, "region_id").getField(Constants.RULE_COLUMN_RANGE).getValue());
        logger.info("regionIds:{}", Arrays.toString(regionIds));
        String status = getRowByField(fieldRuleRows, "status").getField(Constants.RULE_COLUMN_RANGE).getValue();
        logger.info("status:{}", status);
        String isNull = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_NULL).getValue();
        logger.info("isNull:{}", isNull);
        String isReception = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_RECEPTION).getValue();
        logger.info("isReception:{}", isReception);
        List<DataStructure> list = dataStructureList.stream().filter(e -> e.getStatus().equals(status))
                .filter(e -> Arrays.asList(regionIds).contains(e.getRegionId())).collect(Collectors.toCollection(LinkedList::new));
        list = isNull.equals("false") ? list.stream().filter(e -> !e.getUserId().equals("N")).collect(Collectors.toCollection(LinkedList::new)) : list;
        if (isReception.equals("false")) {
            list.forEach(e -> map.put(e.getUserId(), e));
        }
        System.err.println(map.values().size());
        System.err.println(list.size());
    }

    public IRow getRowByField(IRow[] rows, String field) {
        return Arrays.stream(rows).filter(e -> e.getField(Constants.RULE_COLUMN_FIELD).getValue().equals(field)).findFirst().orElse(null);
    }

    public JSONObject putField(JSONObject jsonObject, IRow row, String... field) {
        Arrays.stream(field).forEach(e -> jsonObject.put(row.getField(e).getKey(), row.getField(e).getValue()));
        return jsonObject;
    }

    public void init() {
        IContainer container = new ExcelContainer.Builder().path("").build();
        container.init();
        ITable containerTable = container.getTable(Constants.SHEET_TITLE_CONTAINER);
        containerTable.load();
        IRow[] containerTableRows = containerTable.getRows();
        AliyunConfig config = JSONObject.toJavaObject(JSONObject.parseObject(containerTableRows[0].getField(Constants.CONTAINER_COLUMN_PATH).getValue()), AliyunConfig.class);
        ITable dataSourceTable = container.getTable(Constants.SHEET_TITLE_DATA_SOURCE);
        dataSourceTable.load();
        IRow[] dataSourceTableRows = dataSourceTable.getRows();
        Arrays.stream(dataSourceTableRows).forEach(row -> {
            DataSource dataSource = new DataSource();
            dataSource = dataSource.getDataSource(row);
            SyncClient syncClient = getSyncClient(config, dataSource);
            String fieldRuleTableName = Constants.SHEET_TITLE_COLUMN + row.getField(Constants.DATA_SOURCE_COLUMN_NAME).getValue();
            ITable fieldRuleTable = container.getTable(fieldRuleTableName);
            fieldRuleTable.load();
            IRow[] fieldRuleRows = fieldRuleTable.getRows();
            List<DataStructure> dataStructureList = getDataSourceRows(dataSource, syncClient);
            Arrays.stream(fieldRuleRows).forEach(fieldRuleRow -> {
                Map<String, DataStructure> MAP = new HashMap<>();
                String range = fieldRuleRow.getField(Constants.RULE_COLUMN_RANGE).getValue();
                String[] ranges = DataSource.parse(range);
                boolean isNull = Boolean.parseBoolean(fieldRuleRow.getField(Constants.RULE_COLUMN_IS_NULL).getValue());
                boolean isReception = Boolean.parseBoolean(fieldRuleRow.getField(Constants.RULE_COLUMN_IS_RECEPTION).getValue());
                List<DataStructure> newList = dataStructureList.stream().filter(dataStructure -> isNull == dataStructure.getUserId().equals("N")).filter(dataStructure -> Arrays.asList(ranges).contains(dataStructure.getRegionId()))
                        .collect(Collectors.toList());
                if (isReception) {
                    newList.forEach(e -> MAP.put(e.getUserId(), e));
                }
            });
        });
    }

    public SyncClient getSyncClient(AliyunConfig config, DataSource dataSource) {
        String endPoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        String instanceName = dataSource.getInstancePath();
        // ClientConfiguration提供了很多配置项，以下只列举部分。
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        // 设置建立连接的超时时间。
        clientConfiguration.setConnectionTimeoutInMillisecond(5000);
        // 设置socket超时时间。
        clientConfiguration.setSocketTimeoutInMillisecond(5000);
        // 设置重试策略，若不设置，采用默认的重试策略。
        clientConfiguration.setRetryStrategy(new AlwaysRetryStrategy());
        return new SyncClient(endPoint, accessKeyId, accessKeySecret, instanceName);
    }

    @Test
    public List<DataStructure> getDataSourceRows(DataSource dataSource, SyncClient syncClient) {
        String tableName = dataSource.getTablePath();
        String[] primaryKeys = dataSource.getPrimaryKeys();
        Arrays.stream(primaryKeys).forEach(primaryKey -> {
        });
        String key1 = "scope_date";
        String key2 = "request_id";
        MultiRowQueryCriteria multiRowQueryCriteria = new MultiRowQueryCriteria(tableName);
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(key1, PrimaryKeyValue.fromString("2020%%record%%134%%2021-06-15"));
        primaryKeyBuilder.addPrimaryKeyColumn(key2, PrimaryKeyValue.fromString("0036655c-e6d9-479a-bbb8-3f07991e"));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        multiRowQueryCriteria.addRow(primaryKey);
        //添加条件。
        multiRowQueryCriteria.setMaxVersions(1);
        SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(tableName, primaryKey);
//        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("Col0",
//                SingleColumnValueFilter.CompareOperator.EQUAL, ColumnValue.fromLong(0));
//        singleColumnValueFilter.setPassIfMissing(false);
//        multiRowQueryCriteria.setFilter(singleColumnValueFilter);
        BatchGetRowRequest batchGetRowRequest = new BatchGetRowRequest();
        batchGetRowRequest.addMultiRowQueryCriteria(multiRowQueryCriteria);
//        SyncClient syncClient = getSyncClient();
//        Preconditions.checkArgument(syncClient != null, "syncClient 为空");
//        BatchGetRowResponse batchGetRowResponse = syncClient.batchGetRow(batchGetRowRequest);
//        System.out.println("是否全部成功：" + batchGetRowResponse.isAllSucceed());
//        batchGetRowResponse.getTableToRowsResult();
//        System.err.println(batchGetRowResponse.getTableToRowsResult());
//        if (!batchGetRowResponse.isAllSucceed()) {
//            for (BatchGetRowResponse.RowResult rowResult : batchGetRowResponse.getFailedRows()) {
//                System.out.println("失败的行：" + batchGetRowRequest.getPrimaryKey(rowResult.getTableName(), rowResult.getIndex()));
//                System.out.println("失败原因：" + rowResult.getError());
//            }
//
//            /**
//             * 可以通过createRequestForRetry方法再构造一个请求对失败的行进行重试。此处只给出构造重试请求的部分。
//             * 推荐的重试方法是使用SDK的自定义重试策略功能，支持对batch操作的部分行错误进行重试。设置重试策略后，调用接口处无需增加重试代码。
//             */
////            BatchGetRowRequest retryRequest = batchGetRowRequest.createRequestForRetry(batchGetRowResponse.getFailedRows());
//        }
        return null;

    }
}

