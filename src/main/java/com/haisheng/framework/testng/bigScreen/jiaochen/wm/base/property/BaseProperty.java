package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.property;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public abstract class BaseProperty implements IProperty {
    protected final static Logger logger = LoggerFactory.getLogger(BaseProperty.class);
    protected final String key;

    protected BaseProperty(BaseBuilder<?, ?> baseBuilder) {
        this.key = baseBuilder.name;
    }

    public abstract static class BaseBuilder<T, R> {
        private String name;

        public T name(String name) {
            this.name = name;
            return (T) this;
        }

        public R build() {
            return buildProperty();
        }

        protected abstract R buildProperty();
    }
}
