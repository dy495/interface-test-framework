package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config.OTSPrimaryKey;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config.OTSPrimaryKeyBuilder;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.field.SimpleField;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.SimpleRow;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * OTStable
 *
 * @author wangmin
 * @date 2021-06-16
 */
public class OTSTable extends BaseTable {
    private final SyncClient syncClient;
    private RangeIteratorParameter rangeIteratorParameter;

    protected OTSTable(@NotNull Builder builder) {
        super(builder);
        this.syncClient = builder.syncClient;
    }

    @Override
    public boolean load() {
        init();
        logger.info(">>>>>>收集结果");
        int index = 1;
        try {
            Iterator<Row> iterator = syncClient.createRangeIterator(rangeIteratorParameter);
            while (iterator.hasNext()) {
                Row row = iterator.next();
                IRow otsRow = new SimpleRow.Builder().index(index++).build();
                Column[] columns = row.getColumns();
                for (Column column : columns) {
                    IField field = new SimpleField.Builder().name(column.getName()).value(column.getValue().toString()).build();
                    otsRow.addField(field);
                }
                addRow(otsRow);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void init() {
        Preconditions.checkNotNull(getOtsPrimaryKeyBuilder(), "主键构造器为空");
        OTSPrimaryKeyBuilder otsPrimaryKeyBuilder = getOtsPrimaryKeyBuilder();
        rangeIteratorParameter = new RangeIteratorParameter(getPath());

        PrimaryKeyBuilder builder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        OTSPrimaryKey inclusiveStartPrimaryKey = otsPrimaryKeyBuilder.getInclusiveStartPrimaryKey();
        inclusiveStartPrimaryKey.getOtsPrimaryKey().forEach(builder::addPrimaryKeyColumn);
        rangeIteratorParameter.setInclusiveStartPrimaryKey(builder.build());
        logger.info(String.valueOf(rangeIteratorParameter.getInclusiveStartPrimaryKey()));

        builder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        OTSPrimaryKey otsPrimaryKey = otsPrimaryKeyBuilder.getExclusiveEndPrimaryKey();
        otsPrimaryKey.getOtsPrimaryKey().forEach(builder::addPrimaryKeyColumn);
        rangeIteratorParameter.setExclusiveEndPrimaryKey(builder.build());
        logger.info(String.valueOf(rangeIteratorParameter.getExclusiveEndPrimaryKey()));

        rangeIteratorParameter.setMaxVersions(1);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder, OTSTable> {

        private SyncClient syncClient;

        @Override
        public OTSTable buildTable() {
            return new OTSTable(this);
        }
    }

}
