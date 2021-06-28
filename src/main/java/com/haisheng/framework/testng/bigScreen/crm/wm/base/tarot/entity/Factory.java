package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.CsvTable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.FileUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Factory implements IFactory {
    private final IContainer container;

    private Factory(@NotNull Builder builder) {
        this.container = builder.container;
    }

    @Override
    public IEntity<?, ?>[] create(String sqlStr) {
        IRow[] rows = invoke(sqlStr);
        return createEntity(rows).toArray(new IEntity[0]);
    }

    @Override
    public IEntity<?, ?>[] create(@NotNull Sql sql) {
        return create(sql.getSql());
    }

    @Override
    public IEntity<?, ?>[] createExcel(String path) {
        String relativePath = FileUtil.getResourcePath(path);
        return create(relativePath);
    }

    @Override
    public IEntity<?, ?>[] createCsv(String path) {
        String relativePath = FileUtil.getResourcePath(path);
        ITable table = new CsvTable.Builder().path(relativePath).buildTable();
        IRow[] rows = table.load() ? table.getRows() : new IRow[0];
        return createEntity(rows).toArray(new IEntity[0]);
    }

    /**
     * 数据表值映射java文件
     *
     * @param sql    sql
     * @param aClass java文件
     * @param <T>    T
     * @return List<T>
     */
    public <T> List<T> toJavaObjectList(@NotNull Sql sql, Class<T> aClass) {
        return toJavaObjectList(sql.getSql(), aClass);
    }

    public <T> List<T> toJavaObjectList(String sql, Class<T> aClass) {
        IRow[] rows = invoke(sql);
        return Arrays.stream(rows).map(this::createJSONObject).map(e -> JSONObject.toJavaObject(e, aClass)).collect(Collectors.toList());
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private IContainer container;

        public Factory build() {
            return new Factory(this);
        }

    }

    private IRow[] invoke(String path) {
        container.setPath(path);
        container.init();
        ITable table = container.getTables().length > 0 ? container.getTables()[0] : null;
        if (table == null) {
            return new IRow[0];
        }
        table.setPath(path);
        if (!table.load()) {
            return new IRow[0];
        }
        return table.getRows();
    }

    private List<IEntity<?, ?>> createEntity(IRow[] rows) {
        return Arrays.stream(rows).map(row -> new Entity.Builder().row(row).factory(this).buildEntity()).collect(Collectors.toList());
    }

    @NotNull
    private JSONObject createJSONObject(@NotNull IRow row) {
        JSONObject object = new JSONObject();
        Arrays.stream(row.getFields()).forEach(field -> object.put(field.getKey(), row.getField(field.getKey()).getValue()));
        return object;
    }
}
