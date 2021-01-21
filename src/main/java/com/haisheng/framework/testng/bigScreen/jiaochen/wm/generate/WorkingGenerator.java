package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherApply;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.Approval;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author wangmin
 * @date 2021/1/20 16:41
 * @desc 进行中卡券
 */
public class WorkingGenerator extends BaseGenerator {

    public WorkingGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull Visitor visitor) {
        logger("CREATE WORKING START");
        Long voucherId = new WaitingVoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildGenerator().getVoucherId();
        super.visitor = visitor;
        String voucherName = getVoucherName(voucherId);
        logger("DO APPLY");
        applyVoucher(voucherName, "1");
        logger("CREATE WORKING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IGenerator buildGenerator() {
            return new WorkingGenerator(this);
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
        List<VoucherApply> voucherApplies = collectBean(scene, VoucherApply.class);
        Long id = Objects.requireNonNull(voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null)).getId();
        visitor.invokeApi(Approval.builder().id(id).status(status).build());
    }
}
