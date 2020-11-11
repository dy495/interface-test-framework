package com.haisheng.framework.testng.bigScreen.crm.wm.property;

import com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil;
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

    @Override
    public void sendDing() {
        if (errorMsg.length() > 0) {
            DingPushUtil.sendText(errorMsg.toString());
        }
    }

    public static class Builder {
        public BasicProperty build() {
            return new BasicProperty(this);
        }
    }

    public BasicProperty(Builder builder) {
        this.errorMsg = new StringBuilder();
    }
}
