package com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.property.BaseProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public IField[] getFields() {
        List<IField> temp = new LinkedList<>();
        for (String key : fields.keySet()) {
            temp.add(fields.get(key));
        }
        final int size = temp.size();
        return temp.toArray(new IField[size]);
    }

    @Override
    public String[] getFieldsKey() {
        List<String> temp = new LinkedList<>(fields.keySet());
        final int size = temp.size();
        return temp.toArray(new String[size]);
    }

    @Override
    public String[] getFieldsValue() {
        List<String> temp = new LinkedList<>();
        for (String key : fields.keySet()) {
            temp.add(String.valueOf(fields.get(key).getValue()));
        }
        final int size = temp.size();
        return temp.toArray(new String[size]);
    }

    @Override
    public IField getField(String key) {
        if (!StringUtils.isEmpty(key)) {
            return fields.get(key);
        }
        return null;
    }

    @Override
    public IField[] findFields(String name) {
        List<IField> tempFields = new LinkedList<>();
        if (name != null) {
            for (String key : fields.keySet()) {
                String fieldName = fields.get(key).getKey();
                if (fieldName.equals(name)) {
                    tempFields.add(fields.get(key));
                }
            }
        }
        int size = tempFields.size();
        return tempFields.toArray(new IField[size]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleRow [fields=");
        int i = 0;
        for (String key : fields.keySet()) {
            sb.append(fields.get(key).getKey());
            sb.append("(");
            sb.append(fields.get(key).getValue());
            sb.append(")");
            if (i != fields.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        sb.append(";");
        sb.append(super.toString());
        return sb.toString();
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends BaseRow>
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
