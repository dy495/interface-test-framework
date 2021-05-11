package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.field.IField;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.property.BaseProperty;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
public abstract class BaseRow extends BaseProperty implements IRow {
    private final Map<String, IField> fields = new LinkedHashMap<>();
    private final int index;

    protected BaseRow(@NotNull BaseBuilder<?, ?> baseBuilder) {
        super(baseBuilder);
        this.index = baseBuilder.index;
    }

    @Override
    public abstract IRow init();

    @Override
    public boolean addField(IField field) {
        if (field != null) {
            if (fields.containsKey(field.getKey())) {
                return true;
            }
            fields.put(field.getKey(), field);
            return true;
        }
        return false;
    }

    public IField[] getFields() {
        List<IField> temp = new LinkedList<>();
        for (String key : fields.keySet()) {
            temp.add(fields.get(key));
        }
        final int size = temp.size();
        return temp.toArray(new IField[size]);
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends IRow>
            extends BaseProperty.BaseBuilder<T, R> {
        private Integer index;

        public T index(int index) {
            this.index = index;
            return (T) this;
        }

        @Override
        protected R buildProperty() {
            return buildRow();
        }

        protected abstract R buildRow();
    }
}
