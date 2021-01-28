package com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.Detail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.CreateVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormPageScene;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        createVoucher(10);
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

    /**
     * 创建卡券
     *
     * @param stock 创建数量
     * @return 创建完成的卡券名
     */
    public String createVoucher(Integer stock) {
        String voucherName = createVoucherName();
        IScene scene = CreateVoucherScene.builder().voucherPic(getPicPath()).voucherName(voucherName).subjectType(getSubjectType())
                .voucherDescription(getDesc()).subjectId(getSubjectId(getSubjectType())).stock(stock).cost(getParValue())
                .shopType(0).shopIds(getShopIdList()).selfVerification(true).build();
        visitor.invokeApi(scene);
        return voucherName;
    }

    /**
     * 创建一个不重复的卡券名
     *
     * @return 卡券名
     */
    private String createVoucherName() {
        int num = CommonUtil.getRandom(1, 100000);
        String voucherName = "优惠券" + num;
        IScene scene = VoucherFormPageScene.builder().voucherName(voucherName).build();
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
        return EnumDesc.VOUCHER_DESC.getDesc();
    }

    /**
     * 获取成本
     *
     * @return 卡券成本
     */
    @NotNull
    @Contract(pure = true)
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
    @Nullable
    private Long getSubjectId(String subjectType) {
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
}
