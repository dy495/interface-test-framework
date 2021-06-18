package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck;

import ai.winsense.retail.scenario.gate.domain.SystemConstant;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.DataResult;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.DataSource;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.OTSContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config.OTSPrimaryKey;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config.OTSPrimaryKeyBuilder;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.Md5Utility;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据校验执行
 *
 * @author wangmin
 * @data 2021-06-17
 */
public class DataCheckRunner {
    private final String rulePath;
    private final String shopId;
    private final String date;
    private final String queryPrimaryKeyName;
    private List<DataResult> dataResultList;

    public DataCheckRunner(Builder builder) {
        this.rulePath = builder.rulePath;
        this.shopId = builder.shopId;
        this.date = builder.date;
        this.queryPrimaryKeyName = builder.queryPrimaryKeyName;
    }

    @Test
    public void load() {
        IContainer excelContainer = new ExcelContainer.Builder().path(rulePath).build();
        excelContainer.init();
        ITable dataContainerTable = excelContainer.getTable(Constants.SHEET_TITLE_CONTAINER);
        dataContainerTable.load();
        Arrays.stream(dataContainerTable.getRows()).forEach(sheetOne -> {
            AliyunConfig config = new AliyunConfig();
            config.getConfig(sheetOne.getField(Constants.CONTAINER_COLUMN_PATH).getValue());
            ITable dataSourceTable = excelContainer.getTable(Constants.SHEET_TITLE_DATA_SOURCE);
            dataSourceTable.load();
            List<DataResult> list = new ArrayList<>();
            //遍历数据源表
            Arrays.stream(dataSourceTable.getRows()).forEach(dataSourceRow -> {
                DataResult dataResult = new DataResult();
                DataSource dataSource = new DataSource();
                dataSource.getDataSource(dataSourceRow);
                IContainer otsContainer = createOTSContainer(config, dataSource);
                ITable[] otsTables = otsContainer.getTables();
                //遍历ots表获取所行
                Arrays.stream(otsTables).forEach(otsTable -> {
                    loadTable(otsTable, dataSource.getPrimaryKeys());
                    IRow[] otsRows = otsTable.getRows();
                    dataResult.setInstanceName(dataSource.getInstancePath());
                    dataResult.setTableName(dataSource.getTablePath());
                    dataResult.setRows(otsRows);
                });
                //每张数据源表等于一个dataResult:包含实例名、表名、所有行数据
                list.add(dataResult);
                dataResultList = list;
            });
        });
    }

    public List<DataResult> getDataResultList() {
        load();
        return this.dataResultList;
    }

    /**
     * 构建ots的容器
     *
     * @param config     容器配置
     * @param dataSource 数据来源
     * @return ots容器
     */
    private IContainer createOTSContainer(AliyunConfig config, DataSource dataSource) {
        IContainer otsContainer = new OTSContainer.Builder().endPoint(config.getEndPoint())
                .accessKeyId(config.getAccessKeyId()).accessKeySecret(config.getAccessKeySecret())
                .instanceName(dataSource.getInstancePath()).path(dataSource.getTablePath()).build();
        otsContainer.init();
        return otsContainer;
    }

    /**
     * 加载表
     *
     * @param table       表对象
     * @param primaryKeys 主键
     */
    private void loadTable(ITable table, String[] primaryKeys) {
        OTSPrimaryKeyBuilder otsPrimaryKeyBuilder = creatOTSPrimaryKeyBuilder(primaryKeys, queryPrimaryKeyName, shopId, date);
        table.setOTSPrimaryKeyBuilder(otsPrimaryKeyBuilder);
        table.load();
    }

    /**
     * 构建ots主键构造器
     *
     * @param primaryValueNames   主键名集合
     * @param queryPrimaryKeyName 需要查询的主键名
     * @param scope               规则
     * @param date                日期
     * @return OTSPrimaryKeyBuilder 主键构造器
     */
    private OTSPrimaryKeyBuilder creatOTSPrimaryKeyBuilder(String[] primaryValueNames, String queryPrimaryKeyName, String scope, String date) {
        OTSPrimaryKey.Builder inclusiveStartPrimaryKeyBuilder = new OTSPrimaryKey.Builder();
        Arrays.stream(primaryValueNames).map(e -> inclusiveStartPrimaryKeyBuilder.primaryKey(e, PrimaryKeyValue.INF_MIN)).filter(e -> e.containsKey(queryPrimaryKeyName))
                .forEach(e -> e.primaryKey(queryPrimaryKeyName, PrimaryKeyValue.fromString(scopeKeyGen(scope, date))));
        OTSPrimaryKey.Builder exclusiveEndPrimaryKeyBuilder = new OTSPrimaryKey.Builder();
        Arrays.stream(primaryValueNames).map(e -> exclusiveEndPrimaryKeyBuilder.primaryKey(e, PrimaryKeyValue.INF_MAX)).filter(e -> e.containsKey(queryPrimaryKeyName))
                .forEach(e -> e.primaryKey(queryPrimaryKeyName, PrimaryKeyValue.fromString(scopeKeyGen(scope, date))));
        return new OTSPrimaryKeyBuilder.Builder().inclusiveStartPrimaryKey(inclusiveStartPrimaryKeyBuilder.build())
                .exclusiveEndPrimaryKey(exclusiveEndPrimaryKeyBuilder.build()).build();
    }

    /**
     * 主键加密方法
     *
     * @param scope 规则
     * @param date  日期
     * @return 加密后数据
     */
    private String scopeKeyGen(String scope, String date) {
        String builder = "record" + SystemConstant.DB_SPLIT_STR +
                scope + SystemConstant.DB_SPLIT_STR +
                date;
        String prefix = Md5Utility.getMD5String(builder).substring(0, 4);
        return prefix + SystemConstant.DB_SPLIT_STR + builder;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private String rulePath;
        private String shopId;
        private String date;
        private String queryPrimaryKeyName;

        public DataCheckRunner build() {
            Preconditions.checkNotNull(rulePath, "规则文件不能为空");
            Preconditions.checkNotNull(shopId, "门店id不能为空");
            Preconditions.checkNotNull(date, "查询日期不能空");
            Preconditions.checkNotNull(queryPrimaryKeyName, "主键查询不能为空");
            return new DataCheckRunner(this);
        }
    }


}
