package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.SupporterUtil;

/**
 * 无库存状态
 *
 * @author wangmin
 * @date 2021/1/25 14:04
 */
public class SellOutVoucher extends AbstractVoucher {
    public SellOutVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE SELL OUT START");
        super.visitor = visitor;
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        String voucherName = supporterUtil.createVoucher(1, VoucherTypeEnum.CUSTOM);
        applyVoucher(voucherName);
        IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
        VoucherPage voucherPage = findBeanByField(voucherPageScene, VoucherPage.class, "voucher_name", voucherName);
        supporterUtil.pushMessage(0, true, voucherPage.getVoucherId());
        logger("CREATE SELL OUT FINISH");
    }

    public static class Builder extends AbstractBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new SellOutVoucher(this);
        }
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     */
    private void applyVoucher(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        ApplyPage applyPage = findBeanByField(scene, ApplyPage.class, "name", voucherName);
        Long id = applyPage.getId();
        ApplyApprovalScene.builder().id(id).status("1").build().invoke(visitor, true);
    }
}