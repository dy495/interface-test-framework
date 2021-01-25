package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherApply;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.Approval;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

/**
 * @author wangmin
 * @date 2021/1/21 16:53
 * @desc 拒绝状态
 */
public class RejectVoucher extends BaseVoucher {
    public RejectVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor) {
        logger("CREATE REJECT START");
        Long voucherId = new WaitingVoucher.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
        super.visitor = visitor;
        String voucherName = getVoucherName(voucherId);
        logger("DO APPLY");
        applyVoucher(voucherName, "2");
        logger("CREATE REJECT FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseVoucher.BaseBuilder {

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
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        List<VoucherApply> voucherApplies = resultCollectToBean(scene, VoucherApply.class);
        Long id = Objects.requireNonNull(voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null)).getId();
        visitor.invokeApi(Approval.builder().id(id).status(status).build());
    }

}