package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 卡券生成入口
 *
 * @author wangmin
 * @date 2021/1/20 14:54
 */
public class VoucherGenerator extends AbstractVoucher {

    public VoucherGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {

    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new VoucherGenerator(this);
        }
    }
}
