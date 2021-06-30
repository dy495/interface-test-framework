package com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.config;

import lombok.Getter;

/**
 * ots主键构造器
 *
 * @author wangmin
 * @data 2021-06-18
 */
@Getter
public class OTSPrimaryKeyBuilder {

    private OTSPrimaryKey inclusiveStartPrimaryKey;
    private OTSPrimaryKey exclusiveEndPrimaryKey;

    public OTSPrimaryKeyBuilder inclusiveStartPrimaryKey(OTSPrimaryKey inclusiveStartPrimaryKey) {
        this.inclusiveStartPrimaryKey = inclusiveStartPrimaryKey;
        return this;
    }

    public OTSPrimaryKeyBuilder exclusiveEndPrimaryKey(OTSPrimaryKey exclusiveEndPrimaryKey) {
        this.exclusiveEndPrimaryKey = exclusiveEndPrimaryKey;
        return this;
    }
}
