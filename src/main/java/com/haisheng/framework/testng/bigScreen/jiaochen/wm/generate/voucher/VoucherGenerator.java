package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 卡券生成入口
 *
 * @author wangmin
 * @date 2021/1/20 14:54
 */
public class VoucherGenerator extends BaseVoucher {

    public VoucherGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor, IScene scene) {

    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseVoucher.BaseBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new VoucherGenerator(this);
        }
    }
}
