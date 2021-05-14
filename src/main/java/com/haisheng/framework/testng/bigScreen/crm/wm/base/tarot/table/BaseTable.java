package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.property.BaseProperty;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jooq.tools.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
public abstract class BaseTable extends BaseProperty implements ITable {
    private final Map<String, IRow> rows = new LinkedHashMap<>();
    private final Map<IRow, Integer> rowsCount = new LinkedHashMap<>();
    private final String path;

    protected BaseTable(@NotNull BaseBuilder<?, ?> baseBuilder) {
        super(baseBuilder);
        this.path = baseBuilder.path;
    }

    @Override
    public abstract boolean load();

    @Override
    public boolean addRow(IRow row) {
        if (row != null) {
            row.init();
            if (rows.containsKey(row.getKey())) {
                int currCount = rowsCount.get(row);
                int count = currCount + 1;
                rowsCount.put(row, count);
            } else {
                if (row.getKey() != null) {
                    rows.put(row.getKey(), row);
                    rowsCount.put(row, 1);
                } else {
                    int currCount = rows.size();
                    int count = currCount + 1;
                    rows.put(String.valueOf(count), row);
                    rowsCount.put(row, count);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public IRow getRow(String key) {
        if (!StringUtils.isEmpty(key)) {
            return rows.get(key.toLowerCase());
        }
        return null;
    }

    public IRow[] getRows() {
        List<IRow> temp = new LinkedList<>();
        for (String key : rows.keySet()) {
            temp.add(rows.get(key));
        }
        int size = temp.size();
        return temp.toArray(new IRow[size]);
    }

    @Override
    public void clear() {
        this.rows.clear();
        this.rowsCount.clear();
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends BaseTable>
            extends BaseProperty.BaseBuilder<T, R> {
        private String path;

        public T path(String path) {
            this.path = path;
            return (T) this;
        }

        @Override
        protected R buildProperty() {
            return buildTable();
        }

        public abstract R buildTable();
    }
}
