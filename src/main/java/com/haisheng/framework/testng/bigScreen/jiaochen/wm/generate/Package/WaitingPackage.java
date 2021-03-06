package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Package;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 审核中状态
 *
 * @author wangmin
 * @date 2021/1/20 15:35
 */
public class WaitingPackage extends AbstractPackage {

    public WaitingPackage(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE WAITING START");
        super.visitor = visitor;
        if (scene == null) {
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            SceneUtil util = new SceneUtil(visitor);
            JSONArray voucherArray = util.getVoucherArray(voucherPage, 1);
            util.createPackage(voucherArray, UseRangeEnum.CURRENT);
        } else {
            scene.visitor(visitor).execute();
        }
        logger("CREATE WAITING FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public IPackage buildPackage() {
            return new WaitingPackage(this);
        }
    }
}
