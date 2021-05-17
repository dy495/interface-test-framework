package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.property;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public abstract class BaseProperty implements IProperty {
    protected final static Logger logger = LoggerFactory.getLogger(BaseProperty.class);
    private final String key;
    private String value;

    protected BaseProperty(@NotNull BaseBuilder<?, ?> baseBuilder) {
        this.key = baseBuilder.name;
        this.value = baseBuilder.value;
    }

    @Override
    public String toString() {
        return "BasicProperty [key=" + key + ", value=" + value + "]";
    }

    @Override
    public void setValue(String value) {
        this.value = value == null ? "" : value;
    }

    public abstract static class BaseBuilder<T, R> {
        private String name;
        private String value;

        public T name(String name) {
            this.name = name;
            return (T) this;
        }

        public T value(String value) {
            this.value = value;
            return (T) this;
        }

        public R build() {
            return buildProperty();
        }

        protected abstract R buildProperty();
    }
}
