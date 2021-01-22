package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.RecallVoucherScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangmin
 * @date 2021/1/20 16:41
 * @desc 撤销状态
 */
public class RecallVoucher extends BaseVoucher {

    public RecallVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull Visitor visitor) {
        logger("CREATE RECALL START");
        Long voucherId = new WaitingVoucher.Builder().voucherStatus(VoucherStatusEnum.WAITING).visitor(visitor).buildVoucher().getVoucherId();
        super.visitor = visitor;
        logger("DO RECALL");
        recallVoucher(voucherId);
        logger("CREATE RECALL FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new RecallVoucher(this);
        }
    }

    /**
     * 卡券撤销
     *
     * @param voucherId 卡券id
     */
    public void recallVoucher(Long voucherId) {
        IScene scene = RecallVoucherScene.builder().id(voucherId).build();
        visitor.invokeApi(scene);
    }
}
