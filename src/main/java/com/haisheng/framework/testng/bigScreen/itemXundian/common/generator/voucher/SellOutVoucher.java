package com.haisheng.framework.testng.bigScreen.itemXundian.common.generator.voucher;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SceneUtil;

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
        SceneUtil supporterUtil = new SceneUtil(visitor);
        String voucherName = supporterUtil.createVoucher(1, VoucherTypeEnum.COMMODITY_EXCHANGE);
        applyVoucher(voucherName);
        IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
        VoucherFormVoucherPageBean voucherPage = findBeanByField(voucherPageScene, VoucherFormVoucherPageBean.class, "voucher_name", voucherName);
        supporterUtil.pushMessage(0, true, voucherPage.getVoucherId());
        logger("CREATE SELL OUT FINISH");
    }

    public static class Builder extends AbstractBuilder {

        @Override
        public SellOutVoucher buildVoucher() {
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
        ApplyPageBean applyPage = findBeanByField(scene, ApplyPageBean.class, "name", voucherName);
        Long id = applyPage.getId();
        ApplyApprovalScene.builder().id(id).status("1").build().visitor(visitor).execute();
    }
}
