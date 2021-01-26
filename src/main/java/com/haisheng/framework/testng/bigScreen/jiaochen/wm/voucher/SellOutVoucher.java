package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.apply.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumPushTarget;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.Detail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.Approval;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.CreateVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2021/1/25 14:04
 * @desc 无库存状态
 */
public class SellOutVoucher extends BaseVoucher {
    public SellOutVoucher(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor) {
        logger("CREATE SELL OUT START");
        super.visitor = visitor;
        logger("DO CREATE");
        String voucherName = createVoucher(VoucherTypeEnum.CUSTOM);
        logger("DO APPLY");
        applyVoucher(voucherName);
        logger("DO SEND");
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
     * 创建4种优惠券
     *
     * @param type 卡券类型
     * @return 卡券名称
     */
    private String createVoucher(VoucherTypeEnum type) {
        String voucherName = createVoucherName(type);
        CreateVoucherScene.CreateVoucherSceneBuilder builder = createVoucherBuilder().stock(1).cardType(type.name()).voucherName(voucherName);
        switch (type.name()) {
            case "FULL_DISCOUNT":
                builder.isThreshold(true).thresholdPrice(999.99);
                break;
            case "COUPON":
                builder.isThreshold(true).thresholdPrice(999.99).discount(2.5).mostDiscount(100.00);
                break;
            case "COMMODITY_EXCHANGE":
                builder.isThreshold(true).thresholdPrice(999.99).exchangeCommodityName("兑换布加迪威龙一辆");
                break;
            default:
                builder.isThreshold(false);
                break;
        }
        visitor.invokeApi(builder.build());
        return voucherName;
    }

    /**
     * 构建卡券信息
     *
     * @return CreateVoucher.CreateVoucherBuilder
     */
    private CreateVoucherScene.CreateVoucherSceneBuilder createVoucherBuilder() {
        return CreateVoucherScene.builder().isDefaultPic(false).voucherPic(getPicPath()).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherDescription(getDesc()).parValue(getParValue()).shopType(0).shopIds(getShopIdList()).selfVerification(true);
    }


    /**
     * 创建一个不重复的卡券名
     *
     * @return 卡券名
     */
    private String createVoucherName() {
        return createVoucherName(VoucherTypeEnum.CUSTOM);
    }

    /**
     * 创建一个不重复的卡券名
     *
     * @return 卡券名
     */
    private String createVoucherName(VoucherTypeEnum typeEnum) {
        int num = CommonUtil.getRandom(1, 100000);
        String voucherName = typeEnum.getDesc() + num;
        IScene scene = VoucherPageScene.builder().voucherName(voucherName).build();
        List<VoucherPage> vouchers = resultCollectToBean(scene, VoucherPage.class);
        if (vouchers.isEmpty()) {
            return voucherName;
        }
        for (VoucherPage voucher : vouchers) {
            if (!voucher.getVoucherName().equals(voucherName)) {
                return voucherName;
            }
        }
        return createVoucherName();
    }

    /**
     * 创建描述
     *
     * @return 描述
     */
    private String getDesc() {
        return EnumContent.B.getContent();
    }

    /**
     * 获取成本
     *
     * @return 卡券成本
     */
    private Double getParValue() {
        return 49.99;
    }

    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    private String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        IScene scene = FileUpload.builder().isPermanent(false).pic(picture).ratio(1.5).build();
        return visitor.invokeApi(scene).getString("pic_path");
    }

    /**
     * 获取主体类型
     *
     * @return 主体类型
     */
    private String getSubjectType() {
        IScene scene = SubjectList.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return Objects.requireNonNull(array.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getString("subject_key");
    }

    /**
     * 获取主体详情
     *
     * @return 主体详情
     */
    private Long getSubjectDesc(String subjectType) {
        if (StringUtils.isEmpty(subjectType)) {
            return null;
        }
        switch (subjectType) {
            case "STORE":
                return getShopIdList().get(0);
            case "BRAND":
                return getBrandIdList().get(0);
            default:
                return null;
        }
    }

    /**
     * 获取品牌id
     *
     * @return 品牌id
     */
    private List<Long> getBrandIdList() {
        IScene scene = Detail.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    private List<Long> getShopIdList() {
        IScene scene = ShopList.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList());
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     */
    private void applyVoucher(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        List<ApplyPage> voucherApplies = resultCollectToBean(scene, ApplyPage.class);
        Long id = Objects.requireNonNull(voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null)).getId();
        visitor.invokeApi(Approval.builder().id(id).status("1").build());
    }

    /**
     * 消息推送
     *
     * @return 发出去的卡券id
     */
    private void pushMessage(Long voucherId) {
        List<String> phoneList = new ArrayList<>();
        phoneList.add(EnumAccount.MARKETING.getPhone());
        List<Long> voucherList = new ArrayList<>();
        voucherList.add(voucherId);
        PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
                .telList(phoneList).messageName(EnumContent.MESSAGE_TITLE.getContent()).messageContent(EnumContent.D.getContent())
                .type(0).voucherOrPackageList(voucherList).useDays(10).ifSendImmediately(true);
        visitor.invokeApi(builder.build());
    }
}
