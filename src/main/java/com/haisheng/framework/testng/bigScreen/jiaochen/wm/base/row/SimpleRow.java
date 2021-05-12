package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row;

public class SimpleRow extends BaseRow {
    protected SimpleRow(Builder builder) {
        super(builder);
    }

    @Override
    public IRow init() {
        return this;
    }

    public static class Builder extends BaseBuilder<Builder, SimpleRow> {

        @Override
        protected SimpleRow buildRow() {
            return new SimpleRow(this);
        }
    }
}
