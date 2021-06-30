package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.container;

import com.alicloud.openservices.tablestore.SyncClient;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.table.OTSTable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * nosql容器
 *
 * @author wangmin
 * @data 2021-06-15
 */
public abstract class NoSqlContainer extends BaseContainer {

    public NoSqlContainer(@NotNull BaseBuilder<?, ?> baseBuilder) {
        super(baseBuilder);
    }

    abstract SyncClient getSyncClient();

    @Override
    public boolean init() {
        SyncClient syncClient = getSyncClient();
        if (syncClient == null) {
            return false;
        }
        logger.info("tableName is:{}", getPath());
        if (!StringUtils.isEmpty(getPath())) {
            ITable table = new OTSTable.Builder().path(getPath()).name(getPath()).syncClient(syncClient).buildTable();
            addTable(table);
            return true;
        }
        return false;
    }
}
