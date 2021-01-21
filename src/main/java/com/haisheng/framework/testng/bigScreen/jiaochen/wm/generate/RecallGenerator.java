package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate;

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
 * @desc 撤销卡券
 */
public class RecallGenerator extends BaseGenerator {

    public RecallGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull Visitor visitor) {
        logger("CREATE RECALL START");
        Long voucherId = new WaitingVoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildGenerator().getVoucherId();
        super.visitor = visitor;
        logger("DO RECALL");
        recallVoucher(voucherId);
        logger("CREATE RECALL FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IGenerator buildGenerator() {
            return new RecallGenerator(this);
        }
    }

    /**
     * 卡券撤销
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void recallVoucher(Long voucherId) {
        IScene scene = RecallVoucherScene.builder().id(voucherId).build();
        visitor.invokeApi(scene);
    }
}
