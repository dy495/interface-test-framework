package com.haisheng.framework.testng.bigScreen.itemXundian.common.generator.voucher;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage.ChangeProvideStatusScene;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * 停止发放状态
 *
 * @author wangmin
 * @date 2021/1/20 16:41
 */
public class StopVoucher extends AbstractVoucher {

    public StopVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(@NotNull VisitorProxy visitor, IScene scene) {
        logger("CREATE STOP START");
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        super.visitor = visitor;
        changeProvideStatus(voucherId);
        logger("CREATE STOP FINISH");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends AbstractBuilder {

        @Override
        public StopVoucher buildVoucher() {
            return new StopVoucher(this);
        }
    }

    /**
     * 开始发放
     *
     * @param voucherId 卡券id
     */
    public void changeProvideStatus(Long voucherId) {
        IScene scene = ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build();
        visitor.invokeApi(scene);
    }
}
