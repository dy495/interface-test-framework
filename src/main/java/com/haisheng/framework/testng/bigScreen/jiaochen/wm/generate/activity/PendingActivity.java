package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;

/**
 * 待审核的活动
 *
 * @author wangmin
 * @date 2021/1/27 14:01
 */
public class PendingActivity extends AbstractActivity {
    protected PendingActivity(BaseBuilder baseBuilder) {
        super(baseBuilder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE PENDING START");
        if (scene == null) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            scene = new SupporterUtil(visitor).createRecruitActivityScene(voucherId, true, 0, false);
        }
        super.visitor = visitor;
        visitor.invokeApi(scene);
        logger("CREATE PENDING FINISH");
    }

    public static class Builder extends BaseBuilder {

        @Override
        public IActivity buildActivity() {
            return new PendingActivity(this);
        }
    }
}
