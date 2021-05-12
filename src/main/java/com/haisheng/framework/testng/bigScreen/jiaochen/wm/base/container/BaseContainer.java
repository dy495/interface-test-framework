package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.property.BaseProperty;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table.ITable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
public abstract class BaseContainer extends BaseProperty implements IContainer {
    private final Map<String, ITable> tables = new LinkedHashMap<>(1024);
    private final String path;

    public BaseContainer(@NotNull BaseBuilder<?, ?> baseBuilder) {
        super(baseBuilder);
        this.path = baseBuilder.path;
    }

    @Override
    public abstract boolean init();

    @Override
    public ITable[] getTables() {
        List<ITable> temp = new LinkedList<>();
        for (String key : tables.keySet()) {
            temp.add(tables.get(key));
        }
        int size = temp.size();
        return temp.toArray(new ITable[size]);
    }

    @Override
    public boolean addTable(ITable table) {
        if (table != null) {
            tables.put(table.getKey(), table);
            return true;
        } else {
            return false;
        }
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends IContainer>
            extends BaseProperty.BaseBuilder<T, R> {
        private String path;

        public T path(String path) {
            this.path = path;
            return (T) this;
        }

        @Override
        protected R buildProperty() {
            return buildContainer();
        }

        public abstract R buildContainer();
    }
}
