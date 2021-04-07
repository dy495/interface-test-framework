package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage.RecallVoucherScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * 撤销状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class RecallVoucher extends AbstractVoucher {

    public RecallVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE RECALL START");
        Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WAITING).visitor(visitor).buildVoucher().getVoucherId();
        super.visitor = visitor;
        recallVoucher(voucherId);
        logger("CREATE RECALL FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

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