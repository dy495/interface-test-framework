package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.Constants;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 数据源表结构
 *
 * @author wangmin
 * @date 2021-06-16
 */
@Getter
public class RuleDataSource implements Serializable {

    private String name;
    private String instancePath;
    private String tablePath;
    private String container;
    private String[] primaryKeys;

    /**
     * 配置数据源
     *
     * @param row 行
     * @return 数据源
     */
    public RuleDataSource initDataSource(IRow row) {
        this.primaryKeys = parse(row.getField(Constants.DATA_SOURCE_COLUMN_KEY).getValue());
        this.name = row.getField(Constants.DATA_SOURCE_COLUMN_NAME).getValue();
        String path = row.getField(Constants.DATA_SOURCE_COLUMN_PATH).getValue();
        String[] paths = path.split("/");
        this.instancePath = paths[0];
        this.tablePath = paths[1];
        this.container = row.getField(Constants.DATA_SOURCE_COLUMN_CONTAINER).getValue();
        return this;
    }

    public static String[] parse(String primaryKey) {
        return primaryKey.replace("]", "").replace("[", "").split(",");
    }
}