package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 审核中状态
 *
 * @author wangmin
 * @date 2021/1/20 15:35
 */
public class WaitingVoucher extends AbstractVoucher {

    public WaitingVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE WAITING START");
        super.visitor = visitor;
        if (scene == null) {
            new SceneUtil(visitor).createVoucher(10, VoucherTypeEnum.CUSTOM);
        } else {
            visitor.invokeApi(scene);
        }
        logger("CREATE WAITING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new WaitingVoucher(this);
        }
    }
}
