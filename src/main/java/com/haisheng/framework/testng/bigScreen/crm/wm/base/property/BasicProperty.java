package com.haisheng.framework.testng.bigScreen.crm.wm.base.property;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasicProperty implements IProperty {
    protected final StringBuilder errorMsg;

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg.toString();
    }

    @Override
    public void clearErrorMsg() {
    }

    public static class Builder {
        public BasicProperty build() {
            return new BasicProperty();
        }
    }

    public BasicProperty() {
        this.errorMsg = new StringBuilder();
    }
}
