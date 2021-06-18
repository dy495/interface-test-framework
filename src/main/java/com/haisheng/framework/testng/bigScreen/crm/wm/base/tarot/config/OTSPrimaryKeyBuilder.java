package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config;

import lombok.Data;

import java.io.Serializable;

/**
 * ots主键构造器
 *
 * @author wangmin
 * @data 2021-06-18
 */
@Data
public class OTSPrimaryKeyBuilder implements Serializable {

    public OTSPrimaryKeyBuilder(Builder builder) {
        this.inclusiveStartPrimaryKey = builder.inclusiveStartPrimaryKey;
        this.exclusiveEndPrimaryKey = builder.exclusiveEndPrimaryKey;
    }

    private final OTSPrimaryKey inclusiveStartPrimaryKey;
    private final OTSPrimaryKey exclusiveEndPrimaryKey;

    public static class Builder {
        private OTSPrimaryKey inclusiveStartPrimaryKey;
        private OTSPrimaryKey exclusiveEndPrimaryKey;

        public Builder inclusiveStartPrimaryKey(OTSPrimaryKey inclusiveStartPrimaryKey) {
            this.inclusiveStartPrimaryKey = inclusiveStartPrimaryKey;
            return this;
        }

        public Builder exclusiveEndPrimaryKey(OTSPrimaryKey exclusiveEndPrimaryKey) {
            this.exclusiveEndPrimaryKey = exclusiveEndPrimaryKey;
            return this;
        }

        public OTSPrimaryKeyBuilder build() {
            return new OTSPrimaryKeyBuilder(this);
        }

    }
}
