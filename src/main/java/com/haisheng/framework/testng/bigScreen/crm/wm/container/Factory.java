package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.SqlCreateException;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

public class Factory {
    private final IContainer container;

    public Factory(Builder builder) {
        this.container = builder.container;
    }

    public List<Map<String, Object>> create(String sql) {
        if (StringUtils.isEmpty(sql)) {
            throw new SqlCreateException("sql不可为空");
        }
        container.setPath(sql);
        container.init();
        return container.getTable();
    }


    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private IContainer container;

        public Factory build() {
            return new Factory(this);
        }

    }
}
