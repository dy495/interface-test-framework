package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.BaseGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;
import com.haisheng.framework.util.CommonUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 卡券生成抽象类
 *
 * @author wangmin
 * @date 2021/1/20 14:38
 */
public abstract class AbstractVoucher extends BaseGenerator implements IVoucher {
    protected VoucherStatusEnum voucherStatus;
    private final IScene voucherScene;

    public AbstractVoucher(BaseBuilder baseBuilder) {
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
            VoucherPage voucherPage = getVoucherPage();
            if (voucherPage != null) {
                logger("FIND " + voucherStatus.name() + " FINISH");
                logger("voucherId is: " + voucherPage.getVoucherId());
                logger("voucherName is：" + voucherPage.getVoucherName());
                return voucherPage.getVoucherId();
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
    public abstract void execute(VisitorProxy visitor, IScene scene);

    public static abstract class BaseBuilder extends BaseGenerator.AbstractBuilder<BaseBuilder> {
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

    protected VoucherPage getVoucherPage() {
        VoucherPage voucherPage = null;
        JSONObject response = VoucherPageScene.builder().build().invoke(visitor, true);
        int total = response.getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            JSONArray array = VoucherPageScene.builder().page(i).size(SIZE).build().invoke(visitor, true).getJSONArray("list");
            List<VoucherPage> voucherPageList = array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, VoucherPage.class)).collect(Collectors.toList());
            voucherPage = voucherStatus.name().equals(VoucherStatusEnum.WORKING.name())
                    ? voucherPageList.stream().filter(e -> e.getVoucherStatus().equals(voucherStatus.name()) && e.getSurplusInventory() > 0).findFirst().orElse(null)
                    : voucherPageList.stream().filter(e -> e.getVoucherStatus().equals(voucherStatus.name())).findFirst().orElse(null);
            if (voucherPage != null) {
                break;
            }
        }
        return voucherPage;
    }

    /**
     * 获取卡券名称
     *
     * @param voucherId 卡券id
     * @return 卡券名
     */
    protected String getVoucherName(Long voucherId) {
        IScene scene = VoucherPageScene.builder().build();
        return findBeanByField(scene, VoucherPage.class, "voucher_id", voucherId).getVoucherName();
    }

    /**
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id
     */
    protected Long getVoucherId(String voucherName) {
        IScene scene = VoucherFormPageScene.builder().voucherName(voucherName).build();
        return findBeanByField(scene, VoucherPage.class, "voucher_name", voucherName).getVoucherId();
    }

    /**
     * 递归计数器
     *
     * @param voucherStatusEnum 卡券状态
     * @return 执行此状态次数
     */
    private Integer counter(VoucherStatusEnum voucherStatusEnum) {
        logger("计数器次数：" + counter);
        return this.voucherStatus == voucherStatusEnum ? counter += 1 : counter;
    }
}
