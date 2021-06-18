package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config;

import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ots主键
 *
 * @author wangmin
 * @date 2021-06-18
 */
@Getter
public class OTSPrimaryKey {
    private final Map<String, PrimaryKeyValue> otsPrimaryKey;

    public OTSPrimaryKey(Builder builder) {
        this.otsPrimaryKey = builder.map;
    }

    @Override
    public String toString() {
        return "OTSPrimaryKey{" +
                "otsPrimaryKey=" + otsPrimaryKey +
                '}';
    }

    public static class Builder {
        private final Map<String, PrimaryKeyValue> map = new LinkedHashMap<>();

        public Builder primaryKey(String key, PrimaryKeyValue value) {
            this.map.put(key, value);
            return this;
        }

        public boolean containsKey(String key) {
            return map.containsKey(key);
        }

        public OTSPrimaryKey build() {
            return new OTSPrimaryKey(this);
        }
    }
}
