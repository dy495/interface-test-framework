package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table.ITable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseContainer implements IContainer {
    private final Map<String, ITable> tables = new LinkedHashMap<>(1024);

    @Getter
    private final String path;

    public BaseContainer(@NotNull BaseBuilder<?, ?> baseBuilder) {
        this.path = baseBuilder.path;
    }

    @Override
    public abstract boolean init();

    public boolean addTable(ITable table) {
        if (table != null) {
            tables.put(table.getKey(), table);
            return true;
        } else {
            return false;
        }
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends IContainer> {
        private String path;

        public T path(String path) {
            this.path = path;
            return (T) this;
        }

        public R build() {
            return buildContainer();
        }

        public abstract R buildContainer();
    }
}
