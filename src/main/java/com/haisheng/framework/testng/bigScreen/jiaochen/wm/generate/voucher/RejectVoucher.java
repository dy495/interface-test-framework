package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApprovalScene;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

/**
 * 拒绝状态
 *
 * @author wangmin
 * @date 2021/1/21 16:53
 */
public class RejectVoucher extends AbstractVoucher {
    public RejectVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE REJECT START");
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
        super.visitor = visitor;
        String voucherName = getVoucherName(voucherId);
        applyVoucher(voucherName, "2");
        logger("CREATE REJECT FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractVoucher.BaseBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new RejectVoucher(this);
        }
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).state(ApplyStatusEnum.AUDITING.getId()).build();
        List<ApplyPage> voucherApplies = resultCollectToBean(scene, ApplyPage.class);
        Long id = Objects.requireNonNull(voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null)).getId();
        visitor.invokeApi(ApprovalScene.builder().id(id).status(status).build());
    }

}
