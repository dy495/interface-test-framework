package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2021/1/20 14:38
 * @desc 优惠券生成器抽象类
 */
public abstract class BaseGenerator implements IGenerator {
    protected static final Logger logger = LoggerFactory.getLogger(BusinessUtil.class);
    protected static final int SIZE = 100;
    protected VoucherStatusEnum voucherStatus;
    protected Visitor visitor;

    public BaseGenerator(BaseBuilder baseBuilder) {
        this.voucherStatus = baseBuilder.voucherStatus;
        this.visitor = baseBuilder.visitor;
    }

    @Override
    public Long getVoucherId() {
        VoucherStatusEnum.findById(voucherStatus.getId());
        if (visitor == null) {
            throw new DataException("visitor is null");
        }
        logger("FIND " + voucherStatus.name());
        IScene scene = VoucherPageScene.builder().build();
        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
        VoucherPage voucher = vouchers.stream().filter(e -> e.getAuditStatusName().equals(voucherStatus.getName())).findFirst().orElse(null);
        if (voucher != null) {
            logger("voucherId is: " + voucher.getVoucherId());
            return voucher.getVoucherId();
        }
        logger(voucherStatus.name() + " DIDN'T FIND ");
        voucherStatus.getGenerateBuilder().build().execute(visitor);
        return getVoucherId();
    }

    public abstract void execute(Visitor visitor);

    public static abstract class BaseBuilder {

        private VoucherStatusEnum voucherStatus;
        private Visitor visitor;

        /**
         * @param voucherStatus 卡券类型
         * @return BaseBuilder.voucherStatus
         */
        public BaseBuilder voucherStatus(VoucherStatusEnum voucherStatus) {
            this.voucherStatus = voucherStatus;
            return this;
        }

        /**
         * @param visitor 业务类型
         * @return BaseBuilder.visitor
         */
        public BaseBuilder visitor(Visitor visitor) {
            this.visitor = visitor;
            return this;
        }

        public abstract IGenerator buildGenerator();

        private IGenerator build() {
            return buildGenerator();
        }
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param <T>   T
     * @return bean的集合
     */
    protected <T> List<T> collectBean(IScene scene, Class<T> bean) {
        List<T> list = new ArrayList<>();
        int total = visitor.invokeApi(scene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, bean)).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 获取卡券名称
     *
     * @param voucherId 卡券id
     * @return 卡券名
     */
    protected String getVoucherName(Long voucherId) {
        IScene scene = VoucherPageScene.builder().build();
        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
        return vouchers.stream().filter(e -> e.getVoucherId().equals(voucherId)).map(VoucherPage::getVoucherName).findFirst().orElse(null);
    }

    /**
     * 标记
     *
     * @param str 内容
     */
    protected void logger(String str) {
        String type = "---------------------------------------{}---------------------------------------";
        logger.info(type, str);
    }
}
