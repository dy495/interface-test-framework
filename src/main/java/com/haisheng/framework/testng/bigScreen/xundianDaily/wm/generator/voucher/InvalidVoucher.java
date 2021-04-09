package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage.InvalidVoucherScene;

/**
 * 作废状态
 *
 * @author wangmin
 * @date 2021/1/22 15:49
 */
public class InvalidVoucher extends AbstractVoucher {
    public InvalidVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE INVALID START");
        Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
        super.visitor = visitor;
        invalidVoucher(voucherId);
        logger("CREATE INVALID FINISH");
    }

    public static class Builder extends AbstractBuilder {
        @Override
        public InvalidVoucher buildVoucher() {
            return new InvalidVoucher(this);
        }
    }

    /**
     * 作废卡券
     *
     * @param voucherId 优惠券id
     */
    public void invalidVoucher(Long voucherId) {
        IScene scene = InvalidVoucherScene.builder().id(voucherId).build();
        visitor.invokeApi(scene);
    }
}
