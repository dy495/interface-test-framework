package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.InvalidVoucherScene;

/**
 *
 * 作废状态
 * @author wangmin
 * @date 2021/1/22 15:49
 */
public class InvalidVoucher extends BaseVoucher {
    public InvalidVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor, IScene scene) {
        logger("CREATE INVALID START");
        Long voucherId = new VoucherGenerator.Builder().voucherStatus(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
        super.visitor = visitor;
        invalidVoucher(voucherId);
        logger("CREATE INVALID FINISH");
    }

    public static class Builder extends BaseBuilder {
        @Override
        public IVoucher buildVoucher() {
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
