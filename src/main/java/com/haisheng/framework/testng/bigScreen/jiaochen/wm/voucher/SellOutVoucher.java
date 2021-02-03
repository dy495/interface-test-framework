package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumPushTarget;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.Approval;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 无库存状态
 *
 * @author wangmin
 * @date 2021/1/25 14:04
 */
public class SellOutVoucher extends BaseVoucher {
    public SellOutVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor, IScene scene) {
        logger("CREATE SELL OUT START");
        super.visitor = visitor;
        String voucherName = new SupporterUtil(visitor).createVoucher(1, VoucherTypeEnum.CUSTOM);
        applyVoucher(voucherName, "1");
        pushMessage(getVoucherId(voucherName));
        logger("CREATE SELL OUT FINISH");
    }

    public static class Builder extends BaseBuilder {

        @Override
        public IVoucher buildVoucher() {
            return new SellOutVoucher(this);
        }
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).state(ApplyStatusEnum.AUDITING.getId()).build();
        List<ApplyPage> voucherApplies = resultCollectToBean(scene, ApplyPage.class);
        Long id = Objects.requireNonNull(voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null)).getId();
        visitor.invokeApi(Approval.builder().id(id).status(status).build());
    }

    /**
     * 消息推送
     */
    private void pushMessage(Long voucherId) {
        List<String> phoneList = new ArrayList<>();
        phoneList.add(EnumAccount.MARKETING_DAILY.getPhone());
        List<Long> voucherList = new ArrayList<>();
        voucherList.add(voucherId);
        PushMessageScene.PushMessageSceneBuilder builder = PushMessageScene.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
                .telList(phoneList).messageName(EnumDesc.MESSAGE_TITLE.getDesc()).messageContent(EnumDesc.MESSAGE_DESC.getDesc())
                .type(0).voucherOrPackageList(voucherList).useDays(10).ifSendImmediately(true);
        visitor.invokeApi(builder.build());
    }
}
