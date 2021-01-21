package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author wangmin
 * @date 2021/1/20 14:54
 * @desc 卡券生成
 */
public class VoucherGenerator extends BaseGenerator {

    public VoucherGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor) {
        throw new DataException("voucherStatus is null");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IGenerator buildGenerator() {
            return new VoucherGenerator(this);
        }
    }
}
