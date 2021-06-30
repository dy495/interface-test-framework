package com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.field;

import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.property.BaseProperty;
import lombok.Getter;

@Getter
public abstract class BaseField extends BaseProperty implements IField {

    protected BaseField(BaseBuilder<?, ?> baseBuilder) {
        super(baseBuilder);
    }

    public abstract static class BaseBuilder<T extends BaseBuilder<?, ?>, R extends BaseField>
            extends BaseProperty.BaseBuilder<T, R> {

        @Override
        protected R buildProperty() {
            return buildField();
        }

        protected abstract R buildField();
    }

}
