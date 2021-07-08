package com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck;

import ai.winsense.retail.scenario.gate.domain.SystemConstant;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.OTSTableData;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.RuleDataSource;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.OTSContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.config.OTSPrimaryKey;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.config.OTSPrimaryKeyBuilder;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.util.Md5Utility;
import com.haisheng.framework.util.DateTimeUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private final String queryPrimaryKeyName;
    private String date;
    private IContainer ruleContainer;
    private List<OTSTableData> otsTableDataList;

    public DataCheckRunner(Builder builder) {
        this.rulePath = builder.rulePath;
        this.shopId = builder.shopId;
//        this.date = builder.date;
        this.queryPrimaryKeyName = builder.queryPrimaryKeyName;
        initRuleContainer();
    }

    private void load() {
        ITable dataContainerTable = ruleContainer.getTable(Constants.SHEET_TITLE_CONTAINER);
        dataContainerTable.load();
        Arrays.stream(dataContainerTable.getRows()).forEach(sheetOne -> {
            AliyunConfig config = new AliyunConfig();
            config.initConfig(sheetOne.getField(Constants.CONTAINER_COLUMN_PATH).getValue());
            ITable dataSourceTable = ruleContainer.getTable(Constants.SHEET_TITLE_DATA_SOURCE);
            dataSourceTable.load();
            //遍历数据源表
            List<OTSTableData> list = new ArrayList<>();
            IRow[] dataSourceRows = dataSourceTable.getRows();
            RuleDataSource ruleDataSource = new RuleDataSource();
            Arrays.stream(dataSourceRows).map(ruleDataSource::initDataSource)
                    .map(newRuleDataSource -> initOTSContainer(config, newRuleDataSource)).map(IContainer::getTables)
                    .forEach(iTables -> Arrays.stream(iTables).map(otsTable -> initOTSTableData(otsTable, ruleDataSource)).forEach(list::add));
            otsTableDataList = list;
        });
    }

    /**
     * 初始化OTSTableData数据
     * 通过拿到的阿里云数据与规则表得到OTSTableData
     *
     * @param otsTable       阿里云读取的ots表
     * @param ruleDataSource 规则文件数据
     * @return OTSTableData
     */
    private OTSTableData initOTSTableData(ITable otsTable, RuleDataSource ruleDataSource) {
        initDate(ruleDataSource);
        OTSTableData otsTableData = new OTSTableData();
        loadTable(otsTable, ruleDataSource.getPrimaryKeys());
        IRow[] otsRows = otsTable.getRows();
        otsTableData.setInstanceName(ruleDataSource.getInstancePath());
        otsTableData.setTableName(ruleDataSource.getTablePath());
        otsTableData.setSourceName(ruleDataSource.getSourceName());
        otsTableData.setRows(otsRows);
        return otsTableData;
    }

    /**
     * 初始化时间
     * 根据规则文件判断获取阿里云数据的时间
     *
     * @param ruleDataSource 规则文件数据
     */
    private void initDate(@NotNull RuleDataSource ruleDataSource) {
        date = ruleDataSource.getSourceName().contains("实时") ? DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd")
                : DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
    }

    /**
     * 加载表
     *
     * @param table       表对象
     * @param primaryKeys 主键
     */
    private void loadTable(@NotNull ITable table, String[] primaryKeys) {
        //此处不设置主键无法执行
        OTSPrimaryKeyBuilder primaryKeyBuilder = initOTSPrimaryKeyBuilder(primaryKeys, queryPrimaryKeyName, shopId, date);
        table.setOTSPrimaryKeyBuilder(primaryKeyBuilder);
        table.load();
    }

    /**
     * 获取对外提供的结果集合
     *
     * @return 结果集合
     */
    public List<OTSTableData> getOtsTableDataList() {
        this.load();
        return this.otsTableDataList;
    }

    /**
     * 获取对外提供的字段规则表集合
     *
     * @return 规则表集合
     */
    public ITable[] getFieldRuleTables() {
        return ruleContainer.findTables(Constants.RULE_COLUMN_FIELD);
    }

    /**
     * 初始化规则容器
     */
    private void initRuleContainer() {
        IContainer container = new ExcelContainer.Builder().path(rulePath).build();
        container.init();
        ruleContainer = container;
    }

    /**
     * 初始化ots容器
     *
     * @param config         容器配置
     * @param ruleDataSource 数据源
     * @return ots容器
     */
    @NotNull
    private IContainer initOTSContainer(@NotNull AliyunConfig config, @NotNull RuleDataSource ruleDataSource) {
        IContainer otsContainer = new OTSContainer.Builder().endPoint(config.getEndPoint())
                .accessKeyId(config.getAccessKeyId()).accessKeySecret(config.getAccessKeySecret())
                .instanceName(ruleDataSource.getInstancePath()).path(ruleDataSource.getTablePath()).build();
        otsContainer.init();
        return otsContainer;
    }

    /**
     * 初始化ots主键构造器
     *
     * @param primaryValueNames   主键名集合
     * @param queryPrimaryKeyName 需要查询的主键名
     * @param scope               规则
     * @param date                日期
     * @return OTSPrimaryKeyBuilder 主键构造器
     */
    private OTSPrimaryKeyBuilder initOTSPrimaryKeyBuilder(String[] primaryValueNames, String queryPrimaryKeyName, String scope, String date) {
        OTSPrimaryKey.Builder inclusiveBuilder = new OTSPrimaryKey.Builder();
        Arrays.stream(primaryValueNames).map(e -> inclusiveBuilder.primaryKey(e, PrimaryKeyValue.INF_MIN)).filter(e -> e.containsKey(queryPrimaryKeyName))
                .forEach(e -> e.primaryKey(queryPrimaryKeyName, PrimaryKeyValue.fromString(scopeKeyGen(scope, date))));
        OTSPrimaryKey.Builder exclusiveBuilder = new OTSPrimaryKey.Builder();
        Arrays.stream(primaryValueNames).map(e -> exclusiveBuilder.primaryKey(e, PrimaryKeyValue.INF_MAX)).filter(e -> e.containsKey(queryPrimaryKeyName))
                .forEach(e -> e.primaryKey(queryPrimaryKeyName, PrimaryKeyValue.fromString(scopeKeyGen(scope, date))));
        return new OTSPrimaryKeyBuilder().inclusiveStartPrimaryKey(inclusiveBuilder.build()).exclusiveEndPrimaryKey(exclusiveBuilder.build());
    }

    /**
     * 主键加密方法
     *
     * @param scope 规则
     * @param date  日期
     * @return 加密后数据
     */
    @NotNull
    private String scopeKeyGen(String scope, String date) {
        String builder = "record" + SystemConstant.DB_SPLIT_STR +
                scope + SystemConstant.DB_SPLIT_STR + date;
        String prefix = Md5Utility.getMD5String(builder).substring(0, 4);
        return prefix + SystemConstant.DB_SPLIT_STR + builder;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private String rulePath;
        private String shopId;
        //        private String date;
        private String queryPrimaryKeyName;

        public DataCheckRunner build() {
            Preconditions.checkNotNull(rulePath, "规则文件不能为空");
            Preconditions.checkNotNull(shopId, "门店id不能为空");
//            Preconditions.checkNotNull(date, "查询日期不能空");
            Preconditions.checkNotNull(queryPrimaryKeyName, "主键查询不能为空");
            return new DataCheckRunner(this);
        }
    }
}
