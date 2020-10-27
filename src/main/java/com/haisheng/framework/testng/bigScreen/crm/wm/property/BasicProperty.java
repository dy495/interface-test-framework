package com.haisheng.framework.testng.bigScreen.crm.wm.property;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasicProperty implements IProperty {
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
        protected StringBuilder errorMsg;

        public BasicProperty build() {
            return new BasicProperty(this);
        }
    }

    public BasicProperty(Builder builder) {
        this.errorMsg = new StringBuilder();
    }

    protected final StringBuilder errorMsg;
}
