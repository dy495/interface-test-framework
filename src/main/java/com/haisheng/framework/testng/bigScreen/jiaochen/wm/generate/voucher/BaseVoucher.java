package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.AbstractGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;

import java.util.List;

/**
 * 卡券生成抽象类
 *
 * @author wangmin
 * @date 2021/1/20 14:38
 */
public abstract class BaseVoucher extends AbstractGenerator implements IVoucher {
    protected VoucherStatusEnum voucherStatus;
    private final IScene voucherScene;

    public BaseVoucher(BaseBuilder baseBuilder) {
        super(baseBuilder);
        this.voucherStatus = baseBuilder.voucherStatus;
        this.voucherScene = baseBuilder.voucherScene;
    }

    @Override
    public Long getVoucherId() {
        try {
            VoucherStatusEnum.findById(voucherStatus.getId());
            Preconditions.checkArgument(!isEmpty(), "visitor is null");
            logger("FIND " + voucherStatus.name() + " START");
            Preconditions.checkArgument(counter(voucherStatus) < 4, voucherStatus.getName() + " 状态执行次数大于3次，强行停止，请检查此状态生成");
            IScene scene = VoucherPageScene.builder().build();
            List<VoucherPage> vouchers = resultCollectToBean(scene, VoucherPage.class);
            VoucherPage voucher = vouchers.stream().filter(e -> e.getVoucherStatus().equals(voucherStatus.name())).findFirst().orElse(null);
            if (voucher != null) {
                logger("FIND " + voucherStatus.name() + " FINISH");
                logger("voucherId is: " + voucher.getVoucherId());
                logger("voucherName is：" + voucher.getVoucherName());
                return voucher.getVoucherId();
            }
            logger(voucherStatus.name() + " DIDN'T FIND ");
            voucherStatus.getVoucherBuilder().buildVoucher().execute(visitor, voucherScene);
            return getVoucherId();
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg.append(e);
        }
        return null;
    }

    @Override
    public abstract void execute(Visitor visitor, IScene scene);


    public static abstract class BaseBuilder extends AbstractBuilder<BaseBuilder> {
        private VoucherStatusEnum voucherStatus;
        private IScene voucherScene;

        /**
         * @param voucherStatus 卡券状态
         * @return BaseBuilder.voucherStatus
         */
        public BaseBuilder voucherStatus(VoucherStatusEnum voucherStatus) {
            this.voucherStatus = voucherStatus;
            return this;
        }

        /**
         * @param scene 构建初始产品的场景
         * @return BaseBuilder.createScene
         */
        public BaseBuilder createScene(IScene scene) {
            this.voucherScene = scene;
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

    /**
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id
     */
    protected Long getVoucherId(String voucherName) {
        IScene scene = VoucherFormPageScene.builder().voucherName(voucherName).build();
        List<VoucherPage> vouchers = resultCollectToBean(scene, VoucherPage.class);
        return vouchers.stream().filter(e -> e.getVoucherName().equals(voucherName)).map(VoucherPage::getVoucherId).findFirst().orElse(null);
    }

    /**
     * 递归计数器
     *
     * @param voucherStatusEnum 卡券状态
     * @return 执行此状态次数
     */
    private Integer counter(VoucherStatusEnum voucherStatusEnum) {
        logger("计数器次数：" + counter);
        if (this.voucherStatus == voucherStatusEnum) {
            return counter += 1;
        }
        return counter;
    }
}
