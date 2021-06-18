package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import lombok.Data;

import java.io.Serializable;

/**
 * 表结果
 *
 * @author wangmin
 * @date 2021-06-18
 */
@Data
public class DataResult implements Serializable {
    /**
     * 实例名称
     */
    String instanceName;

    /**
     * 表名
     */
    String tableName;

    /**
     * 表中行数据
     */
    IRow[] rows;
}
