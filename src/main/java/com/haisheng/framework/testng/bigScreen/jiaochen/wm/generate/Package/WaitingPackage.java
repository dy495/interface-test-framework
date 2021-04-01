package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 审核中状态
 *
 * @author wangmin
 * @date 2021/1/20 15:35
 */
public class WaitingPackage extends AbstractPackage {

    public WaitingPackage(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE WAITING START");
        super.visitor = visitor;
        if (scene == null) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            new SupporterUtil(visitor).getVoucherArray(voucherId, 1);
        } else {
            visitor.invokeApi(scene);
        }
        logger("CREATE WAITING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IPackage buildPackage() {
            return new WaitingPackage(this);
        }
    }
}
