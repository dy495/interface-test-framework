package com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.entity;

import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.row.IRow;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 实体抽象类
 *
 * @param <S> sql类
 * @param <F> 工厂类
 */
@Data
public abstract class BaseEntity<S, F> implements IEntity<S, F> {
    private IRow current;
    private IRow last;
    private final F factory;

    protected BaseEntity(BaseBuilder<?, ?, ?> baseBuilder) {
        this.current = baseBuilder.row;
        this.factory = (F) baseBuilder.factory;
    }

    protected void saveNewRow(IRow newRow) {
        last = current;
        current = newRow;
    }

    @Override
    public F getFactory() {
        return this.factory;
    }

    @Override
    public abstract S getSql();

    @Override
    public abstract IRow refresh();

    @Override
    public String getFieldValue(String fieldName) {
        if (current != null && current.getField(fieldName) != null) {
            return current.getField(fieldName).getValue();
        } else {
            return null;
        }
    }

    /**
     * 将字段值以长浮点型返回
     *
     * @param fieldName 字段名
     * @return long
     */
    @Override
    public long getLongField(String fieldName) {
        if (!StringUtils.isEmpty(getFieldValue(fieldName))) {
            return Long.parseLong(getFieldValue(fieldName));
        }
        return -1L;
    }

    /**
     * 将字段值以短整型返回
     *
     * @param fieldName 字段名
     * @return int
     */
    @Override
    public int getIntField(String fieldName) {
        if (!StringUtils.isEmpty(getFieldValue(fieldName))) {
            return Integer.parseInt(getFieldValue(fieldName));
        }
        return -1;
    }

    @Override
    public int getIntFieldIfNullReturnZero(String fieldName) {
        if (!StringUtils.isEmpty(getFieldValue(fieldName))) {
            return Integer.parseInt(getFieldValue(fieldName));
        }
        return 0;
    }

    /**
     * 将字段值以单浮点型返回
     *
     * @param fieldName 字段名
     * @return float
     */
    @Override
    public float getFloatField(String fieldName) {
        if (!StringUtils.isEmpty(getFieldValue(fieldName))) {
            return Float.parseFloat(getFieldValue(fieldName));
        }
        return -1.0F;
    }

    /**
     * 将字段值以多浮点型返回
     *
     * @param fieldName 字段名
     * @return double
     */
    @Override
    public double getDoubleField(String fieldName) {
        if (!StringUtils.isEmpty(getFieldValue(fieldName))) {
            return Double.parseDouble(getFieldValue(fieldName));
        }
        return -1.0D;
    }

    @Override
    public String toString() {
        return "Entity [current row=" + current + "]";
    }

    public abstract static class BaseBuilder<S, F, B extends BaseBuilder<?, ?, ?>> {
        private IRow row;
        private F factory;

        public B row(IRow row) {
            this.row = row;
            return (B) this;
        }

        public B factory(F factory) {
            this.factory = factory;
            return (B) this;
        }

        public S build() {
            return buildEntity();
        }

        protected abstract S buildEntity();
    }
}
