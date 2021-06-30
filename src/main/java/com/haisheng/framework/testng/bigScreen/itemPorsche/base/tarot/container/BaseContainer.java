package com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.container;

import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.property.BaseProperty;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.util.ContainerConstants;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jooq.tools.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
public abstract class BaseContainer extends BaseProperty implements IContainer {
    private final Map<String, ITable> tables = new LinkedHashMap<>(ContainerConstants.COLLECT_INIT_CAPACITY);
    @Getter
    private String path;

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

    @Override
    public ITable[] findTables(String tableName) {
        List<ITable> temp = new LinkedList<>();
        if (!StringUtils.isEmpty(tableName)) {
            temp.addAll(tables.entrySet().stream().filter(e -> e.getKey().contains(tableName))
                    .map(Map.Entry::getValue).collect(Collectors.toCollection(LinkedList::new)));
        }
        final int size = temp.size();
        return temp.toArray(new ITable[size]);
    }

    @Override
    public ITable getTable(String tableName) {
        return tables.entrySet().stream().filter(e -> e.getKey().equals(tableName))
                .map(Map.Entry::getValue).findFirst().orElse(null);
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean setTable(ITable table) {
        return false;
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
