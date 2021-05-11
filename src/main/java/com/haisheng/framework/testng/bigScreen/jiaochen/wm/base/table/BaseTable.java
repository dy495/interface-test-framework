package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.property.BaseProperty;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row.IRow;
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

    protected BaseTable(@NotNull BaseBuilder<?, ?> baseBuilder) {
        super(baseBuilder);

    }

    @Override
    public abstract boolean init();

    @Override
    public boolean addRow(IRow row) {
        if (row != null) {
            row.init();
            if (rows.containsKey(row.getKey())) {
                int currCount = rowsCount.get(row);
                int count = currCount + 1;
                rowsCount.put(row, count);
            } else {
                rows.put(row.getKey(), row);
                rowsCount.put(row, 1);
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

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends ITable>
            extends BaseProperty.BaseBuilder<T, R> {

        @Override
        protected R buildProperty() {
            return buildTable();
        }

        public abstract R buildTable();
    }
}
