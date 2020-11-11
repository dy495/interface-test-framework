package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import com.haisheng.framework.testng.bigScreen.crm.wm.property.BasicProperty;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

public class Factory extends BasicProperty {
    private final IContainer container;

    public Factory(Builder builder) {
        super(builder);
        this.container = builder.container;
    }

    public List<Map<String, Object>> create(String sql) {
        container.setPath(sql);
        container.init();
        return container.getTable();
    }

    public <T> List<T> create(String sql, Class<T> clazz) {
        container.setPath(sql);
        return container.getTable(clazz);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BasicProperty.Builder {
        private IContainer container;

        public Factory build() {
            return new Factory(this);
        }

    }
}
