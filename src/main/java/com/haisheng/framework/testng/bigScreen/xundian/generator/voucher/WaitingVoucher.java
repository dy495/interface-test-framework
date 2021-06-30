package com.haisheng.framework.testng.bigScreen.xundian.generator.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.xundian.util.SupporterUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 审核中状态
 *
 * @author wangmin
 * @date 2021/1/20 15:35
 */
public class WaitingVoucher extends AbstractVoucher {

    public WaitingVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE WAITING START");
        super.visitor = visitor;
        if (scene == null) {
            new SupporterUtil(visitor).createVoucher(10, VoucherTypeEnum.COMMODITY_EXCHANGE);
        } else {
            visitor.invokeApi(scene);
        }
        logger("CREATE WAITING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public WaitingVoucher buildVoucher() {
            return new WaitingVoucher(this);
        }
    }
}
