package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.BaseGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.IVoucher;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage.VoucherFormVoucherPageScene;
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
    protected VoucherStatusEnum status;
    private final IScene scene;

    public AbstractVoucher(AbstractBuilder abstractBuilder) {
        super(abstractBuilder);
        this.status = abstractBuilder.status;
        this.scene = abstractBuilder.scene;
    }

    @Override
    public Long getVoucherId() {
        try {
            VoucherStatusEnum.findById(status.getId());
            Preconditions.checkArgument(!isEmpty(), "visitor is null");
            logger("FIND " + status.name() + " START");
            Preconditions.checkArgument(counter(status) < 4, status.getName() + " 状态执行次数大于3次，强行停止，请检查此状态生成");
            VoucherPage voucherPage = getPage();
            if (voucherPage != null) {
                logger("FIND " + status.name() + " FINISH");
                logger("voucherId is：" + voucherPage.getVoucherId());
                logger("voucherName is：" + voucherPage.getVoucherName());
                return voucherPage.getVoucherId();
            }
            logger(status.name() + " DIDN'T FIND ");
            status.getVoucherBuilder().buildVoucher().execute(visitor, scene);
            return getVoucherId();
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg.append(e);
        }
        return null;
    }

    public static abstract class AbstractBuilder extends BaseBuilder<AbstractBuilder, IVoucher> {
        private VoucherStatusEnum status;
        private IScene scene;

        /**
         * @param status 卡券状态
         * @return BaseBuilder.status
         */
        public AbstractBuilder status(VoucherStatusEnum status) {
            this.status = status;
            return this;
        }

        /**
         * @param scene 构建初始产品的场景
         * @return BaseBuilder.createScene
         */
        public AbstractBuilder scene(IScene scene) {
            this.scene = scene;
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
     * 获取卡券表单
     *
     * @return 卡券表单
     */
    private VoucherPage getPage() {
        VoucherPage voucherPage = null;
        JSONObject response = VoucherFormVoucherPageScene.builder().build().invoke(visitor, true);
        int total = response.getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            JSONArray array = VoucherFormVoucherPageScene.builder().page(i).size(SIZE).build().invoke(visitor, true).getJSONArray("list");
            List<VoucherPage> voucherPageList = array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, VoucherPage.class)).collect(Collectors.toList());
            voucherPage = status.name().equals(VoucherStatusEnum.WORKING.name())
                    ? voucherPageList.stream().filter(e -> e.getVoucherStatus().equals(status.name()) && e.getSurplusInventory() > 0 && !e.getVoucherName().contains("专用")).findFirst().orElse(null)
                    : voucherPageList.stream().filter(e -> e.getVoucherStatus().equals(status.name()) && !e.getVoucherName().contains("专用")).findFirst().orElse(null);
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
        IScene scene = VoucherFormVoucherPageScene.builder().build();
        return findBeanByField(scene, VoucherPage.class, "voucher_id", voucherId).getVoucherName();
    }

    /**
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id
     */
    protected Long getVoucherId(String voucherName) {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
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
        return this.status == voucherStatusEnum ? counter += 1 : counter;
    }
}
