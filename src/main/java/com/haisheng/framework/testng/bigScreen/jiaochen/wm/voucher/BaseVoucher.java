package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.AbstractGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;

import java.util.List;

/**
 * 卡券生成期抽象类
 *
 * @author wangmin
 * @date 2021/1/20 14:38
 */
public abstract class BaseVoucher extends AbstractGenerator implements IVoucher {

    protected VoucherStatusEnum voucherStatus;

    public BaseVoucher(BaseBuilder baseBuilder) {
        super(baseBuilder);
        this.voucherStatus = baseBuilder.voucherStatus;
    }

    @Override
    public Long getVoucherId() {
        VoucherStatusEnum.findById(voucherStatus.getId());
        Preconditions.checkArgument(!isEmpty(), "visitor is null");
        logger("FIND " + voucherStatus.name());
        IScene scene = VoucherPageScene.builder().build();
        List<VoucherPage> vouchers = resultCollectToBean(scene, VoucherPage.class);
        VoucherPage voucher = vouchers.stream().filter(e -> e.getAuditStatusName().equals(voucherStatus.getName())).findFirst().orElse(null);
        if (voucher != null) {
            logger("voucherId is: " + voucher.getVoucherId());
            return voucher.getVoucherId();
        }
        logger(voucherStatus.name() + " DIDN'T FIND ");
        voucherStatus.getVoucherBuilder().buildVoucher().execute(visitor);
        return getVoucherId();
    }

    @Override
    public abstract void execute(Visitor visitor);

    public static abstract class BaseBuilder extends AbstractBuilder<BaseBuilder> {
        private VoucherStatusEnum voucherStatus;

        /**
         * @param voucherStatus 卡券类型
         * @return BaseBuilder.voucherStatus
         */
        public BaseBuilder voucherStatus(VoucherStatusEnum voucherStatus) {
            this.voucherStatus = voucherStatus;
            return this;
        }

        /**
         * 构建卡券
         *
         * @return 卡券生成器
         */
        public abstract IVoucher buildVoucher();

        protected IVoucher buildProduct() {
            return buildVoucher();
        }
    }

    /**
     * 获取卡券名称
     *
     * @param voucherId 卡券id
     * @return 卡券名
     */
    protected String getVoucherName(Long voucherId) {
        IScene scene = VoucherPageScene.builder().build();
        List<VoucherPage> vouchers = resultCollectToBean(scene, VoucherPage.class);
        return vouchers.stream().filter(e -> e.getVoucherId().equals(voucherId)).map(VoucherPage::getVoucherName).findFirst().orElse(null);
    }
}
