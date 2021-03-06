package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * 进行中状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class WorkingVoucher extends AbstractVoucher {

    public WorkingVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE WORKING START");
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
        super.visitor = visitor;
        String voucherName = getVoucherName(voucherId);
        applyVoucher(voucherName, "1");
        logger("CREATE WORKING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new WorkingVoucher(this);
        }
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        ApplyPageBean applyPage = findBeanByField(scene, ApplyPageBean.class, "name", voucherName);
        Long id = applyPage.getId();
        ApplyApprovalScene.builder().id(id).status(status).build().visitor(visitor).execute();
    }
}
