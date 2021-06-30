package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.table;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.config.OTSPrimaryKey;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.config.OTSPrimaryKeyBuilder;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.field.SimpleField;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.row.SimpleRow;
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
        int index = 1;
        try {
            Iterator<Row> iterator = syncClient.createRangeIterator(rangeIteratorParameter);
            logger.info(">>>>>>开始收集结果");
            while (iterator.hasNext()) {
                IRow otsRow = new SimpleRow.Builder().index(index++).build();
                Row row = iterator.next();
                for (Column column : row.getColumns()) {
                    String name = column.getName();
                    String value = column.getValue().toString();
                    IField field = new SimpleField.Builder().name(name).value(value).build();
                    otsRow.addField(field);
                }
                addRow(otsRow);
            }
            logger.info("<<<<<<结果收集完毕");
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
        logger.info(rangeIteratorParameter.getInclusiveStartPrimaryKey().toString());

        builder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        OTSPrimaryKey otsPrimaryKey = otsPrimaryKeyBuilder.getExclusiveEndPrimaryKey();
        otsPrimaryKey.getOtsPrimaryKey().forEach(builder::addPrimaryKeyColumn);
        rangeIteratorParameter.setExclusiveEndPrimaryKey(builder.build());
        logger.info(rangeIteratorParameter.getExclusiveEndPrimaryKey().toString());

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
