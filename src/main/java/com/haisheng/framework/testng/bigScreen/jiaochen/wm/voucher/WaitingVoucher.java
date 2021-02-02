package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author wangmin
 * @date 2021/1/20 15:35
 * @desc 审核中状态
 */
public class WaitingVoucher extends BaseVoucher {

    public WaitingVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor, IScene scene) {
        logger("CREATE WAITING START");
        super.visitor = visitor;
        if (scene == null) {
            new SupporterUtil(visitor).createVoucher(10, VoucherTypeEnum.CUSTOM);
        } else {
            visitor.invokeApi(scene);
        }
        logger("CREATE WAITING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new WaitingVoucher(this);
        }
    }
}
