package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.PackageUseTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.BuyPackageRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.MakeSureBuyScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.PurchaseTemporaryPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;

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
    public void execute(VisitorProxy visitor, IScene scene) {
        logger("CREATE SELL OUT START");
        super.visitor = visitor;
        String voucherName = new SupporterUtil(visitor).createVoucher(1, VoucherTypeEnum.CUSTOM);
        applyVoucher(voucherName);
        buyTemporaryPackage(voucherName);
        makeSureBuyPackage();
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
     */
    private void applyVoucher(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).state(ApplyStatusEnum.AUDITING.getId()).build();
        List<ApplyPage> voucherApplies = resultCollectToBean(scene, ApplyPage.class);
        Long id = Objects.requireNonNull(voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null)).getId();
        visitor.invokeApi(ApprovalScene.builder().id(id).status("1").build());
    }

    /**
     * 购买临时套餐
     *
     * @param voucherName 包含的卡券名称
     */
    private void buyTemporaryPackage(String voucherName) {
        JSONArray voucherList = getVoucherArray(voucherName);
        IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                .subjectType(UseRangeEnum.CURRENT.name()).extendedInsuranceYear("1").extendedInsuranceCopies("1").type(0).build();
        visitor.invokeApi(temporaryScene);
    }

    /**
     * 获取卡券集合
     *
     * @param voucherName 卡券名称
     * @return 卡券集合
     */
    private JSONArray getVoucherArray(String voucherName) {
        Long voucherId = getVoucherId(voucherName);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("voucher_id", voucherId);
        jsonObject.put("voucher_name", voucherName);
        jsonObject.put("voucher_count", 1);
        array.add(jsonObject);
        return array;
    }

    /**
     * 套餐确认支付
     */
    private void makeSureBuyPackage() {
        //获取确认支付id
        IScene scene = BuyPackageRecordScene.builder().packageName("临时套餐").size(SIZE).build();
        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
        Long id = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals("临时套餐")).map(e -> e.getLong("id")).findFirst().orElse(null);
        visitor.invokeApi(MakeSureBuyScene.builder().id(id).auditStatus("AGREE").build());
    }

}
