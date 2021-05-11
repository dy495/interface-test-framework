package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.field;

import lombok.Getter;

@Getter
public abstract class BaseField implements IField {
    private final String key;
    private final String value;

    protected BaseField(BaseBuilder<?, ?> baseBuilder) {
        this.key = baseBuilder.name;
        this.value = baseBuilder.value;
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends IField> {
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
            return buildField();
        }

        protected abstract R buildField();
    }

}
