package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.mapper.IEnum;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.appointmentmanage.AppointmentRecordAppointmentPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeGoodsDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.packagemanage.PackageDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.presalesreception.PreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherInvalidPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.brand.AppletBrandListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.style.AppletStyleListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.AppFollowUpPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppCustomerDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppCustomerEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppReceptorListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule.AppSaleScheduleDayListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppAppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionReceptorListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.AuthTreeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.TimeTableListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.UploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.CustomerImportScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPurchaseFixedPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPurchaseTemporaryPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.RoleListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffDeleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.UseRangeDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import com.haisheng.framework.util.UrlOutputUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class SceneUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public SceneUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    public void loginPc(String account, String password) {
        IScene scene = LoginPc.builder().phone(account).verificationCode(password).type(1).build();
        login(scene);
    }


    public void loginApp(EnumAccount enumAccount) {
        IScene scene = LoginApp.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build();
        login(scene);
    }

    public void loginPc(EnumAccount enumAccount) {
        IScene scene = LoginPc.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).type(1).build();
        login(scene);
    }

    public void loginApplet(EnumAppletToken token) {
        visitor.setToken(token.getToken());
    }

    public void login(IScene scene) {
        EnumTestProduct oldProduce = visitor.getProduct();
        EnumTestProduct newProduce = visitor.isDaily() ? EnumTestProduct.JC_DAILY_ZH : EnumTestProduct.JC_ONLINE_ZH;
        visitor.setProduct(newProduce);
        visitor.setToken(scene);
        visitor.setProduct(oldProduce);
    }

    public JSONArray getCoordinate() {
        JSONArray dd = new JSONArray();
        dd.add(39.95933);
        dd.add(116.29845);
        return dd;
    }

    public String getRandomPhone() {
        return "177" + (new Random().nextInt(89999999) + 10000000);
    }

    public String uploadFile() {
        String pic = new ImageUtil().getImageBinary("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/common/resources/picture/touxiang.jpg");
        return UploadScene.builder().pic("data:image/jpeg;base64," + pic).permanentPicType(0).ratio(1.0).ratioStr("1:1").build().execute(visitor).getString("pic_path");
    }

    /**
     * 创建一个卡券id
     *
     * @param stock 库存
     * @param type  类型
     * @return 卡券id
     */
    public Long createVoucherId(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucher(stock, type);
        return getVoucherId(voucherName);
    }

    /**
     * 创建一个卡券页
     *
     * @param stock 库存
     * @param type  类型
     * @return 卡券id
     */
    public VoucherFormVoucherPageBean createVoucherPage(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucher(stock, type);
        return getVoucherPage(voucherName);
    }

    /**
     * 获取占用的卡券
     *
     * @return 卡券id
     */
    public VoucherFormVoucherPageBean getOccupyVoucherId() {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.WORKING.name()).build();
        List<VoucherFormVoucherPageBean> voucherPageBeanList = toJavaObjectList(scene, VoucherFormVoucherPageBean.class);
        return voucherPageBeanList.stream().filter(e -> e.getSurplusInventory() >
                e.getAllowUseInventory() && e.getAllowUseInventory() != 0).findFirst().orElseGet(this::useVoucher);
    }

    /**
     * 使用一个卡券
     *
     * @return 卡券id
     */
    private VoucherFormVoucherPageBean useVoucher() {
        String voucherName = createVoucher(2, VoucherTypeEnum.CUSTOM);
        applyVoucher(voucherName, "1");
        VoucherFormVoucherPageBean voucherPage = getVoucherPage(voucherName);
        JSONArray voucherList = getVoucherArray(voucherPage, 1);
        buyTemporaryPackage(voucherList, 1);
        return voucherPage;
    }

    /**
     * 创建4种优惠券
     *
     * @param stock 卡券库存
     * @param type  卡券类型
     * @return 卡券名称
     */
    public String createVoucher(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucherName(type);
        createVoucherBuilder(stock, type).voucherName(voucherName).build().execute(visitor);
        return voucherName;
    }

    /**
     * 创建卡券
     *
     * @param stock 库存
     * @param type  卡券类型
     * @return 构建卡券的builder
     */
    public CreateScene.CreateSceneBuilder createVoucherBuilder(Integer stock, @NotNull VoucherTypeEnum type) {
        CreateScene.CreateSceneBuilder builder = createVoucherBuilder(true).stock(stock).cardType(type.name());
        switch (type.name()) {
            case "FULL_DISCOUNT":
                builder.isThreshold(true).thresholdPrice(999.99).parValue(49.99);
                break;
            case "COUPON":
                builder.isThreshold(true).thresholdPrice(999.99).discount(2.5).mostDiscount(99.99);
                break;
            case "COMMODITY_EXCHANGE":
                builder.exchangeCommodityName("兑换布加迪威龙一辆").parValue(null);
                break;
            case "CASH_COUPON":
                builder.isThreshold(true).thresholdPrice(999.99).parValue(49.99).replacePrice(99.99);
            default:
                builder.isThreshold(false);
                break;
        }
        return builder;
    }

    /**
     * 构建卡券信息
     *
     * @param selfVerification 能否核销
     * @return CreateVoucher.CreateVoucherBuilder
     */
    public CreateScene.CreateSceneBuilder createVoucherBuilder(Boolean selfVerification) {
        return CreateScene.builder().subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).cost(0.01)
                .voucherDescription("<p>" + getDesc() + "</p>").shopType(0).shopIds(getShopIdList(2))
                .selfVerification(selfVerification).isDefaultPic(true);
    }

    /**
     * 创建一个不重复的卡券名
     *
     * @return 卡券名
     */
    public String createVoucherName() {
        return createVoucherName(VoucherTypeEnum.CUSTOM);
    }

    /**
     * 创建一个不重复的卡券名
     *
     * @return 卡券名
     */
    public String createVoucherName(VoucherTypeEnum typeEnum) {
        int num = CommonUtil.getRandom(1, 100000);
        String voucherName = typeEnum.getDesc() + num;
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
        List<VoucherFormVoucherPageBean> vouchers = toJavaObjectList(scene, VoucherFormVoucherPageBean.class);
        if (vouchers.isEmpty()) {
            return voucherName;
        }
        for (VoucherFormVoucherPageBean voucher : vouchers) {
            if (!voucher.getVoucherName().equals(voucherName)) {
                return voucherName;
            }
        }
        return createVoucherName(typeEnum);
    }

    /**
     * 创建描述
     *
     * @return 描述
     */
    public String getDesc() {
        return EnumDesc.DESC_BETWEEN_20_30.getDesc();
    }

    /**
     * 获取成本
     *
     * @return 卡券成本
     */
    public Double getParValue() {
        return 49.99;
    }

    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
        return getPicPath(path);
    }

    public String getPicPath(String picPath) {
        return getPicPath(picPath, "3:2");
    }

    public String getPicPath(String picPath, String ratioStr) {
        String picture = new ImageUtil().getImageBinary(picPath);
        String[] strings = ratioStr.split(":");
        double ratio = BigDecimal.valueOf(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1]))
                .divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return FileUploadScene.builder().isPermanent(false).permanentPicType(0).pic(picture).ratioStr(ratioStr)
                .ratio(ratio).build().execute(visitor).getString("pic_path");
    }

    /**
     * 获取主体类型
     *
     * @return 主体类型
     */
    public String getSubjectType() {
        JSONArray array = SubjectListScene.builder().build().execute(visitor).getJSONArray("list");
        JSONObject jsonObject = array.stream().map(e -> (JSONObject) e).findFirst().orElse(null);
        Preconditions.checkArgument(jsonObject != null, "主体类型为空");
        return jsonObject.getString("subject_key");
    }

    /**
     * 获取主体详情
     *
     * @return 主体详情
     */
    public Long getSubjectDesc(String subjectType) {
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
    public List<Long> getBrandIdList() {
        JSONArray array = UseRangeDetailScene.builder().subjectKey("BRAND").build().execute(visitor).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * 获取门店id
     *
     * @param shopCount 数量
     * @return 门店id
     */
    public List<Long> getShopIdList(Integer shopCount) {
        return getShopIdList().subList(0, shopCount);
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public List<Long> getShopIdList() {
        JSONArray array = ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList());
    }

    /**
     * 获取卡券名称
     *
     * @param voucherId 卡券id
     * @return 卡券名
     */
    public String getVoucherName(long voucherId) {
        return getVoucherPage(voucherId).getVoucherName();
    }

    /**
     * 修改卡券selfVerification变为可核销
     *
     * @param voucherId 卡券id
     */
    public void editVoucher(Long voucherId) {
        VoucherDetailBean detail = getVoucherDetail(voucherId);
        EditVoucherScene.builder().voucherName(detail.getVoucherName()).subjectType(detail.getSubjectType())
                .subjectId(detail.getSubjectId()).stock(detail.getStock()).cardType(detail.getCardType())
                .isThreshold(detail.getIsThreshold()).thresholdPrice(detail.getThresholdPrice()).exchangeCommodityName(detail.getExchangeCommodityName())
                .parValue(detail.getParValue()).replacePrice(detail.getReplacePrice()).discount(detail.getDiscount())
                .mostDiscount(detail.getMostDiscount()).cost(detail.getCost()).isDefaultPic(detail.getIsDefaultPic())
                .voucherDescription(detail.getVoucherDescription()).selfVerification(true)
                .shopIds(detail.getShopIds().stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList()))
                .shopType(detail.getShopType()).subjectName(detail.getSubjectName()).id(voucherId).build().execute(visitor);
    }

    /**
     * 清理卡券
     */
    public void cleanVoucher() {
        Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
            IScene scene = VoucherFormVoucherPageScene.builder().voucherName(anEnum.getDesc())
                    .voucherStatus(VoucherStatusEnum.WAITING.name()).build();
            List<VoucherFormVoucherPageBean> voucherPageBeanList = toJavaObjectList(scene, VoucherFormVoucherPageBean.class);
            List<Long> voucherIdList = voucherPageBeanList.stream().map(VoucherFormVoucherPageBean::getVoucherId).collect(Collectors.toList());
            voucherIdList.stream().filter(Objects::nonNull).forEach(this::clear);
        });
        Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
            IScene scene = VoucherFormVoucherPageScene.builder().voucherName(anEnum.getDesc()).voucherStatus(VoucherStatusEnum.REJECT.name()).build();
            List<VoucherFormVoucherPageBean> voucherFormVoucherPageBeanList = toJavaObjectList(scene, VoucherFormVoucherPageBean.class);
            List<Long> voucherIdList = voucherFormVoucherPageBeanList.stream().map(VoucherFormVoucherPageBean::getVoucherId).collect(Collectors.toList());
            voucherIdList.stream().filter(Objects::nonNull).forEach(this::deleteVoucher);
        });
    }

    private void clear(Long voucherId) {
        recallVoucher(voucherId);
        deleteVoucher(voucherId);
    }

    /**
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id(Long)
     */
    public Long getVoucherId(String voucherName) {
        return getVoucherPage(voucherName).getVoucherId();
    }

    public void deleteVoucher(Long voucherId) {
        DeleteVoucherScene.builder().id(voucherId).build().execute(visitor);
    }

    public void recallVoucher(Long voucherId) {
        RecallVoucherScene.builder().id(voucherId).build().execute(visitor);
    }

    /**
     * 获取卡券领取记录
     *
     * @param voucherPage 卡券页
     * @return 领取记录列表
     */
    public VoucherSendRecord getVoucherSendRecord(VoucherFormVoucherPageBean voucherPage) {
        return getVoucherSendRecord(voucherPage.getVoucherId());
    }

    /**
     * 获取卡券领取记录
     *
     * @param voucherId 卡券id
     * @return 领取记录列表
     */
    public VoucherSendRecord getVoucherSendRecord(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return toFirstJavaObject(scene, VoucherSendRecord.class);
    }

    /**
     * 获取卡券作废记录
     *
     * @param voucherId 卡券id
     * @return 作废记录列表
     */
    public List<VoucherInvalidPageBean> getVoucherInvalidList(Long voucherId) {
        IScene scene = VoucherInvalidPageScene.builder().id(voucherId).build();
        return toJavaObjectList(scene, VoucherInvalidPageBean.class);
    }

    /**
     * 获取重复的核销人员
     *
     * @return 电话号
     */
    public String getRepetitionVerificationPhone() {
        JSONArray array = VerificationPeopleScene.builder().build().execute(visitor).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getString("verification_phone")).findFirst().orElse(null);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherName 卡券名称
     * @return 卡券页信息
     */
    public VoucherFormVoucherPageBean getVoucherPage(String voucherName) {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
        return toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_name", voucherName);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherId 卡券id
     * @return 卡券页信息
     */
    public VoucherFormVoucherPageBean getVoucherPage(Long voucherId) {
        IScene scene = VoucherFormVoucherPageScene.builder().build();
        return toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
    }

    /**
     * 刷新当前页
     *
     * @param voucherPage 卡券页
     * @return 卡券页信息
     */
    public VoucherFormVoucherPageBean flushVoucherPage(VoucherFormVoucherPageBean voucherPage) {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherPage.getVoucherName()).build();
        return toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherPage.getVoucherId());
    }

    /**
     * 获取卡券详情
     *
     * @param voucherId 卡券id
     * @return 卡券详情
     */
    public VoucherDetailBean getVoucherDetail(Long voucherId) {
        IScene scene = VoucherDetailScene.builder().id(voucherId).build();
        return toJavaObject(scene, VoucherDetailBean.class);
    }

    /**
     * 获取卡券集合
     *
     * @return 卡券集合
     */
    public JSONArray getVoucherArray() {
        VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
        return getVoucherArray(voucherPage, 1);
    }

    /**
     * 获取卡券集合
     *
     * @param voucherPage  卡券页
     * @param voucherCount 卡券数量
     * @return 卡券集合
     */
    public JSONArray getVoucherArray(VoucherFormVoucherPageBean voucherPage, Integer voucherCount) {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("voucher_id", voucherPage.getVoucherId());
        jsonObject.put("voucher_name", voucherPage.getVoucherName());
        jsonObject.put("voucher_count", voucherCount);
        array.add(jsonObject);
        return array;
    }

    /**
     * 获取卡券的最新核销码
     *
     * @param voucherId 卡券id
     * @return 核销码
     */
    public String getVoucherCode(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return visitor.invokeApi(scene).getJSONArray("list").getJSONObject(0).getString("voucher_code");
    }

    /**
     * 获取车牌号
     *
     * @param phone 电话号
     * @return 车牌号
     */
    public String getPlatNumber(String phone) {
        JSONArray plateList = SearchCustomerScene.builder().customerPhone(phone).build().execute(visitor).getJSONArray("plate_list");
        return plateList.stream().map(e -> (JSONObject) e).map(e -> e.getString("plate_number")).findFirst().orElse(null);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPageBean getAuditingApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        return toJavaObject(scene, ApplyPageBean.class, "name", voucherName);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPageBean getApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        return toJavaObject(scene, ApplyPageBean.class, "name", voucherName);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPageBean getApplyPageByTime(String voucherName, String time) {
        logger.info("time is:{}", time);
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        List<ApplyPageBean> voucherApplies = toJavaObjectList(scene, ApplyPageBean.class);
        return voucherApplies.stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTime().contains(time)).findFirst().orElse(null);
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        ApplyPageBean applyPage = toJavaObject(scene, ApplyPageBean.class, "name", voucherName);
        ApplyApprovalScene.builder().id(applyPage.getId()).status(status).build().execute(visitor);
    }

    //--------------------------------------------------套餐----------------------------------------------------------

    /**
     * 获取某个状态的套餐
     *
     * @param packageStatusEnum packageStatusEnum
     * @return 套餐信息
     */
    public PackagePage getPackagePage(PackageStatusEnum packageStatusEnum) {
        IScene packageFormPageScene = PackageFormPageScene.builder().build();
        return toJavaObject(packageFormPageScene, 50, PackagePage.class, "audit_status_name", packageStatusEnum.getName());
    }

    /**
     * 获取套餐信息集合
     *
     * @param packageName 套餐名
     * @return 套餐信息集合
     */
    public PackagePage getPackagePage(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return toJavaObject(scene, 50, PackagePage.class, "package_name", packageName);
    }

    /**
     * 获取套餐信息
     *
     * @param packageName 套餐名
     * @return 套餐信息
     */
    public List<PackagePage> getPackagePageList(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return toJavaObjectList(scene, 50, PackagePage.class);
    }

    /**
     * 获取套餐信息
     *
     * @param packageName 套餐名
     * @return 套餐信息
     */
    public PackagePage getPackagePage(String packageName, Long packageId) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return toJavaObject(scene, PackagePage.class, "package_id", packageId);
    }

    /**
     * 获取套餐信息
     *
     * @param packageId 套餐id
     * @return 套餐信息
     */
    public PackagePage getPackagePage(Long packageId) {
        IScene scene = PackageFormPageScene.builder().build();
        return toJavaObject(scene, 50, PackagePage.class, "id", packageId);
    }

    /**
     * 获取套餐id
     *
     * @param packageName 套餐名称
     * @return 套餐id
     */
    public Long getPackageId(String packageName) {
        return getPackagePage(packageName).getPackageId();
    }

    /**
     * 获取套餐名
     *
     * @param packageId 套餐id
     * @return 套餐名
     */
    public String getPackageName(Long packageId) {
        return getPackagePage(packageId).getPackageName();
    }

    /**
     * 套餐确认支付
     *
     * @param packageName 套餐名
     */
    public void makeSureBuyPackage(String packageName) {
        IScene scene = BuyPackageRecordScene.builder().packageName(packageName).size(SIZE / 10).build();
        JSONObject jsonObject = toJavaObject(scene, JSONObject.class, "package_name", packageName);
        MakeSureBuyScene.builder().id(jsonObject.getLong("id")).auditStatus("AGREE").build().execute(visitor);
    }

    /**
     * 取消套餐支付
     *
     * @param packageName 套餐名
     */
    public void cancelSoldPackage(String packageName) {
        IScene scene = BuyPackageRecordScene.builder().packageName(packageName).size(SIZE / 10).build();
        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
        Long id = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals(packageName)).map(e -> e.getLong("id")).findFirst().orElse(null);
        CancelSoldPackageScene.builder().id(id).id(id).build().execute(visitor);
    }

    /**
     * 创建一个套餐名
     *
     * @return 套餐名
     */
    public String createPackageName(@NotNull UseRangeEnum useRangeEnum) {
        int num = CommonUtil.getRandom(1, 1000000);
        String packageName = useRangeEnum.getName() + "套餐" + num;
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        List<PackagePage> packagePages = toJavaObjectList(scene, PackagePage.class, "package_name", packageName);
        return packagePages.isEmpty() ? packageName : createPackageName(useRangeEnum);
    }

    /**
     * 创建一个套餐
     *
     * @param voucherList 套餐所含卡券信息
     * @return 套餐名
     */
    public PackagePage createPackage(JSONArray voucherList, UseRangeEnum anEnum) {
        String packageName = createPackageName(anEnum);
        CreatePackageScene.builder().packageName(packageName).packageDescription(getDesc()).subjectType(getSubjectType())
                .subjectId(getSubjectDesc(getSubjectType())).voucherList(voucherList).packagePrice("49.99").status(true)
                .shopIds(getShopIdList(3)).expireType(2).expiryDate(10).build().execute(visitor);
        return getPackagePage(packageName);
    }

    public PackageDetailBean getPackageDetail(Long packageId) {
        IScene packageDetailScene = PackageDetailScene.builder().id(packageId).build();
        return toJavaObject(packageDetailScene, PackageDetailBean.class);
    }

    /**
     * 编辑指定套餐
     *
     * @param packageId   套餐 id
     * @param voucherList 所含卡券列表
     * @return 套餐名
     */
    public String editPackage(Long packageId, JSONArray voucherList) {
        IScene scene = PackageDetailScene.builder().id(packageId).build();
        PackageDetailBean packageDetail = toJavaObject(scene, PackageDetailBean.class);
        Preconditions.checkArgument(packageDetail != null, packageId + " 套餐没找到相关信息");
        EditPackageScene.builder().packageName(packageDetail.getPackageName())
                .beginUseTime(packageDetail.getBeginUseTime())
                .endUseTime(packageDetail.getEndUseTime())
                .packageDescription(packageDetail.getPackageDescription())
                .subjectType(packageDetail.getSubjectType())
                .subjectId(packageDetail.getSubjectId())
                .status(packageDetail.getStatus())
                .packagePrice(packageDetail.getPackagePrice())
                .expiryDate(packageDetail.getExpiryDate())
                .expireType(packageDetail.getExpireType())
                .voucherList(voucherList)
                .shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).build()
                .execute(visitor);
        return packageDetail.getPackageName();
    }

    /**
     * 编辑套餐
     *
     * @param voucherList 套餐包含的卡券
     * @return 套餐名
     */
    public PackagePage editPackage(JSONArray voucherList) {
        IScene scene = PackageFormPageScene.builder().size(50).page(1).build();
        PackagePage packagePage = scene.execute(visitor).getJSONArray("list").stream().map(obj -> (JSONObject) obj)
                .map(obj -> JSONObject.toJavaObject(obj, PackagePage.class))
                .filter(pkg -> !EnumVP.isContains(pkg.getPackageName()))
                .findFirst().orElse(null);
        Preconditions.checkArgument(packagePage != null, "套餐为空");
        Long packageId = packagePage.getPackageId();
        String packageName = packagePage.getPackageName();
        EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherList(voucherList).packagePrice("1.11").status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).expireType(2).expiryDate(12).build().execute(visitor);
        return packagePage;
    }

    /**
     * 编辑套餐
     *
     * @param voucherPage  卡券页
     * @param voucherCount 卡券数量
     * @return 套餐名
     */
    public PackagePage editPackage(VoucherFormVoucherPageBean voucherPage, int voucherCount) {
        JSONArray voucherList = getVoucherArray(voucherPage, voucherCount);
        return editPackage(voucherList);
    }

    /**
     * 购买临时套餐
     *
     * @param voucherList 包含的卡券名称列表
     * @param type        0赠送/1购买
     */
    public void buyTemporaryPackage(JSONArray voucherList, int type) {
        PurchaseTemporaryPackageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(getPlatNumber(EnumAppletToken.JC_WM_DAILY.getPhone()))
                .voucherList(voucherList).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(type).build().execute(visitor);
    }

    /**
     * 购买固定套餐
     *
     * @param packageId 包含的固定套餐
     * @param type      0赠送/1购买
     */
    public void buyFixedPackage(Long packageId, int type) {
        PurchaseFixedPackageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone())
                .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("1.00").expiryDate("1")
                .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(type).build().execute(visitor);
    }

    /**
     * 接待时购买固定套餐
     *
     * @param packageId 包含的固定套餐
     * @param type      0赠送/1购买
     */
    public void receptionBuyFixedPackage(Long packageId, int type) {
        IScene scene = PackageDetailScene.builder().id(packageId).build();
        PackageDetailBean packageDetail = toJavaObject(scene, PackageDetailBean.class);
        Preconditions.checkArgument(packageDetail != null, "没找到 " + packageId + " 套餐相关信息");
        Integer expiryDate = packageDetail.getExpiryDate();
        Integer expireType = packageDetail.getExpireType();
        String packagePrice = packageDetail.getPackagePrice();
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone()).build();
        ReceptionPage receptionPage = toJavaObjectList(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        ReceptionPurchaseFixedPackageScene.builder().customerId(receptionPage.getCustomerId())
                .customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name()).expireType(expireType).expiryDate(expiryDate)
                .extendedInsuranceCopies("").extendedInsuranceYear("").packageId(packageId).packagePrice(packagePrice)
                .plateNumber(receptionPage.getPlateNumber()).receptionId(receptionPage.getId()).remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .shopId(receptionPage.getShopId()).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).type(type)
                .build().execute(visitor);
    }

    /**
     * 接待时购买临时套餐
     *
     * @param voucherList 包含的卡券列表
     * @param type        0赠送/1购买
     */
    public void receptionBuyTemporaryPackage(JSONArray voucherList, int type) {
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone()).build();
        ReceptionPage receptionPage = toJavaObjectList(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        ReceptionPurchaseTemporaryPackageScene.builder().customerId(receptionPage.getCustomerId())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).customerPhone("").expireType(2).expiryDate("10")
                .extendedInsuranceCopies("").extendedInsuranceYear("").plateNumber(receptionPage.getPlateNumber())
                .receptionId(receptionPage.getId()).remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).shopId(receptionPage.getShopId())
                .subjectId(getSubjectDesc(getSubjectType())).subjectType(getSubjectType()).type(type).voucherList(voucherList).build().execute(visitor);
    }

    //-------------------------------------------------消息----------------------------------------------------------

    /**
     * 给指定人发消息，如需修改接收人，请在文件中添加手机号
     *
     * @param type               推送优惠类型 0：卡券，1：套餐
     * @param voucherOrPackageId 卡券id
     * @param immediately        是否立即发送
     */
    public void pushCustomMessage(Integer type, boolean immediately, Long... voucherOrPackageId) {
        pushCustomMessage(type, immediately, true, voucherOrPackageId);
    }

    /**
     * 给指定人发消息，如需修改接收人，请在文件中添加手机号
     *
     * @param type               推送优惠类型 0：卡券，1：套餐
     * @param voucherOrPackageId 卡券id
     * @param immediately        是否立即发送
     */
    public JSONObject pushCustomMessage(Integer type, boolean immediately, boolean checkCode, Long... voucherOrPackageId) {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/excel/发消息手机号.xlsx";
        return pushCustomMessage(type, immediately, checkCode, filePath, voucherOrPackageId);
    }

    public JSONObject pushCustomMessage(Integer type, boolean immediately, boolean checkCode, String filePath, Long... voucherOrPackageId) {
        List<Long> voucherOrPackageList = new ArrayList<>(Arrays.asList(voucherOrPackageId));
        JSONObject response = CustomerImportScene.builder().filePath(filePath).build().upload(visitor);
        Preconditions.checkArgument(response.getInteger("code") == 1000);
        List<Long> customerIdList = response.getJSONObject("data").getJSONArray("customer_id_list").toJavaList(Long.class);
        PushMessageScene.PushMessageSceneBuilder builder = PushMessageScene.builder().customerIdList(customerIdList)
                .messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc()).messageContent(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                .type(type).useTimeType(2).useDays("10").voucherOrPackageList(voucherOrPackageList);
        String d = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm:ss");
        long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(d));
        builder = immediately ? builder.ifSendImmediately(true) : builder.ifSendImmediately(false).sendTime(sendTime);
        return builder.build().execute(visitor, checkCode);
    }

    //----------------------------------------------------预约记录-------------------------------------------------------

    /**
     * 通过预约id查询接待信息
     *
     * @param appointmentId 预约id
     * @return 预约信息
     */
    public AppointmentRecordAppointmentPageBean getAppointmentPageById(Long appointmentId, String type) {
        IScene scene = AppointmentPageScene.builder().type(type).build();
        return toJavaObject(scene, AppointmentRecordAppointmentPageBean.class, "id", appointmentId);
    }

    //----------------------------------------------------接待记录-------------------------------------------------------

    /**
     * 通过接待id查询接待信息
     *
     * @param receptionId 接待id
     * @return 接待信息
     */
    public ReceptionPage getReceptionPageById(Long receptionId) {
        IScene receptionPageScene = ReceptionPageScene.builder().build();
        return toJavaObject(receptionPageScene, ReceptionPage.class, "id", receptionId);
    }

    public PreSalesReceptionPageBean getPreSalesReceptionPageById(Long receptionId) {
        IScene scene = PreSalesReceptionPageScene.builder().build();
        return toJavaObject(scene, PreSalesReceptionPageBean.class, "id", receptionId);
    }

    /**
     * 获取第一个接待记录
     *
     * @return 接待信息
     */
    public ReceptionPage getFirstReceptionPage() {
        return toFirstJavaObject(ReceptionPageScene.builder().build(), ReceptionPage.class);
    }

    /**
     * 获取第一个接待记录
     *
     * @return 接待信息
     */
    public PreSalesReceptionPageBean getFirstPreSalesReceptionPage() {
        return toFirstJavaObject(PreSalesReceptionPageScene.builder().build(), PreSalesReceptionPageBean.class);
    }

    //----------------------------------------------------核销人员-------------------------------------------------------

    /**
     * 获取非重复电话号
     *
     * @return 电话号
     */
    public String getDistinctPhone() {
        String phone = "155" + CommonUtil.getRandom(8);
        IScene scene = VerificationPeopleScene.builder().verificationPhone(phone).build();
        int total = scene.execute(visitor).getInteger("total");
        if (total == 0) {
            return phone;
        }
        return getDistinctPhone();
    }

    /**
     * 获取核销码
     *
     * @param verificationIdentity 核销员身份
     * @return 核销码
     */
    public String getVerificationCode(String verificationIdentity) {
        return getVerificationCode(true, verificationIdentity);
    }

    /**
     * 获取核销码
     *
     * @param verificationStatus   核销码状态
     * @param verificationIdentity 核销员身份
     * @return 核销码
     */
    public String getVerificationCode(boolean verificationStatus, String verificationIdentity) {
        List<String> list = new ArrayList<>();
        VerificationPeopleScene.VerificationPeopleSceneBuilder builder = VerificationPeopleScene.builder();
        int total = visitor.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            JSONArray array = builder.page(i).size(SIZE).build().execute(visitor).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getBoolean("verification_status") == verificationStatus
                    && e.getString("verification_identity").equals(verificationIdentity)).map(e -> e.getString("verification_code")).collect(Collectors.toList()));
        }
        if (list.size() == 0) {
            String code = getVerificationCode(verificationIdentity);
            list.add(code);
            switchVerificationStatus(code, false);
        }
        return list.get(0);
    }

    /**
     * 核销人员状态更改
     *
     * @param code   核销码
     * @param status 状态
     */
    public void switchVerificationStatus(String code, boolean status) {
        IScene verificationPeopleScene = VerificationPeopleScene.builder().verificationCode(code).build();
        long id = visitor.invokeApi(verificationPeopleScene).getJSONArray("list").getJSONObject(0).getLong("id");
        SwitchVerificationStatusScene.builder().id(id).status(status).build().execute(visitor);
    }

    //-------------------------------------------------小程序----------------------------------------------------------

    /**
     * 小程序预约
     * 需要给个固定门店的顾问，需要shopId固定、staffId固定
     *
     * @param type 预约类型 MAINTAIN：保养，REPAIR：维修
     * @return id 预约id
     */
    public Long appointment(AppointmentTypeEnum type, String date) {
        AppletAppointmentSubmitScene.AppletAppointmentSubmitSceneBuilder builder = AppletAppointmentSubmitScene.builder().type(type.name())
                .shopId(getShopId()).staffId(getStaffId()).appointmentName("隔壁小王").appointmentPhone("15321527989");
        builder = type.equals(AppointmentTypeEnum.REPAIR) ? builder.faultDescription(EnumDesc.DESC_BETWEEN_15_20.getDesc()).timeId(getTimeId(date, EnumAppointmentType.REPAIR.name())).carId(getCarId())
                : type.equals(AppointmentTypeEnum.TEST_DRIVE) ? builder.carStyleId(getCarStyleId()).staffId(getTestDriverStaffId()).timeId(getTimeId(date, EnumAppointmentType.TEST_DRIVE.name()))
                : builder.timeId(getTimeId(date, EnumAppointmentType.MAINTAIN.name())).carId(getCarId());
        return builder.build().execute(visitor).getLong("id");
    }

    public Long getCarStyleId() {
        JSONArray list = AppletBrandListScene.builder().build().execute(visitor).getJSONArray("list");
        Long brandId = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("name").equals("特斯拉")).map(e -> e.getLong("id")).findFirst().orElse(null);
        JSONObject response = AppletStyleListScene.builder().brandId(brandId).build().execute(visitor).getJSONArray("list").getJSONObject(0);
        return response.getLong("id");
    }

    public String getTestDriverStaffId() {
        //uid_df9293ba JC_DAILY_LXQ
        return visitor.isDaily() ? EnumAccount.JC_DAILY_LXQ.getUid() : "uid_35a1d271";
    }

    /**
     * 获取预约时间id
     */
    public Long getTimeId(String date, String type) {
        AppletAppointmentTimeListScene.AppletAppointmentTimeListSceneBuilder builder = AppletAppointmentTimeListScene.builder();
        builder.type(type).shopId(getShopId()).day(date).build();
        builder = type.equals(EnumAppointmentType.TEST_DRIVE.name()) ? builder.carStyleId(getCarStyleId()) : builder.carId(getCarId());
        JSONArray array = builder.build().execute(visitor).getJSONArray("list");
        List<AppletAppointmentTimeList> timeList = array.stream().map(object -> (JSONObject) object).map(object -> JSONObject.toJavaObject(object, AppletAppointmentTimeList.class)).collect(Collectors.toList());
        return timeList.stream().filter(e -> !e.getIsFull()).map(AppletAppointmentTimeList::getId).findFirst().orElse(null);
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public Long getShopId() {
        return visitor.isDaily() ? 46522L : 20034L;
    }

    /**
     * 获取员工id
     *
     * @return 员工id
     */
    public String getStaffId() {
        IScene maintainStaffListScene = AppletAppointmentStaffListScene.builder().shopId(getShopId()).type(AppointmentTypeEnum.MAINTAIN.name()).build();
        JSONArray jsonArray = maintainStaffListScene.execute(visitor).getJSONArray("list");
        return Objects.requireNonNull(jsonArray.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getString("uid");
    }

    /**
     * 获取小程序carId
     *
     * @return 我的爱车id
     */
    public Long getCarId() {
        IScene appletCarListScene = AppletCarListScene.builder().build();
        JSONObject jsonObject = appletCarListScene.execute(visitor).getJSONArray("list").getJSONObject(0);
        Preconditions.checkArgument(jsonObject != null, "小程序我的爱车为空");
        return jsonObject.getLong("id");
    }

    /**
     * 获取小程序我的爱车列表
     *
     * @return 我的爱车列表
     */
    public List<AppletCarInfo> getAppletCarList() {
        return AppletCarListScene.builder().build().execute(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e)
                .map(e -> JSONObject.toJavaObject(e, AppletCarInfo.class)).collect(Collectors.toList());

    }

    /**
     * 创建我的爱车并返回爱车信息
     *
     * @param plateNumber 车牌号
     * @param carModelId  车型id
     * @return 爱车信息
     */
    public AppletCarInfo createCar(String plateNumber, Long carModelId) {
        AppletCarCreateScene.builder().modelId(carModelId).plateNumber(plateNumber).build().execute(visitor);
        IScene scene = AppletCarListScene.builder().build();
        JSONObject obj = scene.execute(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e)
                .filter(e -> e.getString("plate_number") != null)
                .filter(e -> e.getString("plate_number").equals(plateNumber))
                .findFirst().orElse(null);
        if (obj != null) {
            return JSONObject.toJavaObject(obj, AppletCarInfo.class);
        }
        return null;
    }

    /**
     * 获取小程序卡券信息
     *
     * @param voucherCode 卡券码
     */
    public AppletVoucherInfo getAppletVoucherInfo(String voucherCode) {
        AppletVoucherInfo appletVoucherInfo;
        Integer id = null;
        Integer status = null;
        JSONArray list;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.execute(visitor);
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            appletVoucherInfo = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_code").equals(voucherCode))
                    .map(e -> JSONObject.toJavaObject(e, AppletVoucherInfo.class)).findFirst().orElse(null);
        } while (appletVoucherInfo == null && list.size() == 20);
        return appletVoucherInfo;
    }

    /**
     * 获取小程序卡券信息
     *
     * @param voucherCode 卡券码
     */
    public AppletVoucherInfo getAppletPackageVoucherInfo(String voucherCode) {
        IScene appletPackageListScene = AppletPackageListScene.builder().lastValue(null).type("GENERAL").size(20).build();
        int id = appletPackageListScene.execute(visitor).getJSONArray("list").getJSONObject(0).getInteger("id");
        IScene appletPackageDetailScene = AppletPackageDetailScene.builder().id((long) id).build();
        JSONArray list = appletPackageDetailScene.execute(visitor).getJSONArray("list");
        return list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_code").equals(voucherCode)).map(e -> JSONObject.toJavaObject(e, AppletVoucherInfo.class)).findFirst().orElse(null);
    }

    /**
     * 获取小程序卡券状态的信息
     *
     * @param voucherUseStatusEnum 优惠券状态枚举
     */
    public AppletVoucher getAppletVoucher(VoucherUseStatusEnum voucherUseStatusEnum) {
        AppletVoucher appletVoucher;
        Integer id = null;
        Integer status = null;
        JSONArray array;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.execute(visitor);
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            appletVoucher = array.stream().map(jsonObject -> (JSONObject) jsonObject).filter(e -> e.getString("status_name").equals(voucherUseStatusEnum.getName())).map(jsonObject -> JSONObject.toJavaObject(jsonObject, AppletVoucher.class)).findFirst().orElse(null);
            logger.info("id:{},status:{}", id, status);
        } while (appletVoucher == null && array.size() == 20);
        return appletVoucher;
    }

    /**
     * 获取小程序卡券数量
     *
     * @return 卡券数量
     */
    public int getAppletVoucherNum() {
        Integer id = null;
        Integer status = null;
        JSONArray array;
        int listSize = 0;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.execute(visitor);
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序卡券数量
     *
     * @return 卡券数量
     */
    public int getAppletVoucherNum(VoucherUseStatusEnum voucherUseStatusEnum) {
        Integer id = null;
        Integer status = null;
        JSONArray array;
        int listSize = 0;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.execute(visitor);
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            listSize += array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("status_name").equals(voucherUseStatusEnum.getName())).count();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序套餐数量
     *
     * @return 套餐数量
     */
    public int getAppletPackageNum() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = AppletPackageListScene.builder().lastValue(lastValue).type("type").size(20).build();
            JSONObject response = scene.execute(visitor);
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序套餐内卡券信息
     *
     * @return 套餐数量
     */
    public List<AppletVoucherInfo> getAppletPackageContainVoucherList() {
        List<AppletVoucherInfo> appletVoucherInfoList = new ArrayList<>();
        List<Long> appletPackageId = new ArrayList<>();
        Long lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletPackageListScene.builder().lastValue(lastValue).type("type").size(20).build();
            JSONObject response = scene.execute(visitor);
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            appletPackageId.addAll(array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList()));
        } while (array.size() == 20);
        appletPackageId.forEach(id -> {
            JSONArray jsonArray = AppletPackageDetailScene.builder().id(id).build().execute(visitor).getJSONArray("list");
            appletVoucherInfoList.addAll(jsonArray.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletVoucherInfo.class)).collect(Collectors.toList()));
        });
        return appletVoucherInfoList;
    }

    public AppletMessageList getAppletMessageList(String messageTypeName) {
        Long lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletMessageListScene.builder().lastValue(lastValue).size(20).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            AppletMessageList appletMessageList = array.stream().map(e -> JSONObject.toJavaObject((JSONObject) e, AppletMessageList.class))
                    .filter(e -> e.getMessageTypeName().equals(messageTypeName))
                    .filter(AppletMessageList::getIsCanEvaluate).findFirst().orElse(null);
            if (appletMessageList != null) {
                return appletMessageList;
            }
        } while (array.size() == 20);
        return null;
    }

    /**
     * 获取小程序我的消息列表数
     *
     * @return 消息数量
     */
    public int getAppletMessageNum() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = AppletMessageListScene.builder().lastValue(lastValue).size(20).build();
            JSONObject response = scene.execute(visitor);
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序我的预约数量
     *
     * @return 预约数量
     */
    public int getAppletAppointmentNum() {
        Integer lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = AppointmentListScene.builder().size(20).lastValue(lastValue).build();
            JSONObject response = scene.execute(visitor);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序积分明细数量
     *
     * @return 积分明细数量
     */
    public int getAppletIntegralRecordNum() {
        int listSize = 0;
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletIntegralRecordScene.builder().lastValue(lastValue).size(20).type("ALL").endTime(null).build();
            JSONObject response = scene.execute(visitor);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序积分明细
     *
     * @return 积分明细
     */
    public List<AppletIntegralRecord> getAppletIntegralRecordList() {
        List<AppletIntegralRecord> list = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletIntegralRecordScene.builder().lastValue(lastValue).size(20).type("ALL").endTime(null).build();
            JSONObject response = scene.execute(visitor);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletIntegralRecord.class)).collect(Collectors.toList()));
        } while (array.size() == 20);
        return list;
    }

    /**
     * 获取小程序兑换记录数量
     *
     * @return 兑换数量
     */
    public int getAppletExchangeRecordNum() {
        int listSize = 0;
        Integer lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().execute(visitor);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序兑换记录信息
     *
     * @return 兑换信息
     */
    public List<AppletExchangeRecord> getAppletExchangeRecordList() {
        List<AppletExchangeRecord> appletExchangeRecordList = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().execute(visitor);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            appletExchangeRecordList.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletExchangeRecord.class)).collect(Collectors.toList()));
        } while (array.size() == 20);
        return appletExchangeRecordList;
    }

    /**
     * 获取小程序积分商城列表
     *
     * @param integralSort 排序方式
     * @param status       是否可兑换
     * @return List<AppletCommodity>
     */
    public List<AppletCommodity> getAppletCommodityList(String integralSort, Boolean status) {
        List<AppletCommodity> appletCommodityListList = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(integralSort).status(status).build().execute(visitor);
            lastValue = data.getJSONObject("last_value");
            array = data.getJSONArray("list");
            appletCommodityListList.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletCommodity.class)).collect(Collectors.toList()));
        } while (array.size() == 10);
        return appletCommodityListList;

    }

    /**
     * 获取小程序积分商城数量
     *
     * @return 数量
     */
    public int getAppletCommodityListNum() {
        int listSie = 0;
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(SortTypeEnum.DOWN.name()).status(false).build().execute(visitor);
            lastValue = data.getJSONObject("last_value");
            array = data.getJSONArray("list");
            listSie += array.size();
        } while (array.size() == 10);
        return listSie;

    }

    //--------------------------------------------------app------------------------------------------------------------

    /**
     * 跟进列表
     */
    public List<AppFollowUpPage> getFollowUpPageList() {
        Integer time = null;
        Integer id = null;
        List<AppFollowUpPage> followUpPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppFollowUpPageScene.builder().id(id).time(time).size(20).build();
            JSONObject response = appointmentPageScene.execute(visitor);
            JSONObject lastValue = response.getJSONObject("last_value");
            time = lastValue.getInteger("time");
            id = lastValue.getInteger("id");
            list = response.getJSONArray("list");
            followUpPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppFollowUpPage.class)).collect(Collectors.toList()));
        } while (list.size() == 20);
        return followUpPageList;
    }

    /**
     * 获取预约页
     *
     * @return 预约记表
     */
    public List<AppAppointmentPage> getAppointmentPageList() {
        Integer lastValue = null;
        List<AppAppointmentPage> appAppointmentPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppAppointmentPageScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = appointmentPageScene.execute(visitor);
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            appAppointmentPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppAppointmentPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return appAppointmentPageList;
    }

    /**
     * 获取预约页数量
     *
     * @return 列表数
     */
    public Integer getAppointmentPageNum() {
        return getAppointmentPageList().size();
    }

    /**
     * 预约看板的预约数
     *
     * @param date 日期
     * @return 预约数量
     */
    public Integer appointmentNumber(Date date, String type) {
        String nowDate = DateTimeUtil.getFormat(new Date(), "yyyy-MM");
        IScene scene = TimeTableListScene.builder().type(type).appointmentMonth(nowDate).build();
        return scene.execute(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("day").equals(DateTimeUtil.getDayOnMonth(date)))
                .map(e -> e.getInteger("appointment_number") == null ? 0 : e.getInteger("appointment_number")).findFirst().orElse(0);
    }

    /**
     * 获取销售接待页
     *
     * @return 接待页列表
     */
    public List<AppReceptionPage> getPreSalesReceptionPageList() {
        Integer lastValue = null;
        List<AppReceptionPage> receptionPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppPreSalesReceptionPageScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = appointmentPageScene.execute(visitor);
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            receptionPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return receptionPageList;
    }

    /**
     * 获取接待页
     *
     * @return 接待页列表
     */
    public List<AppReceptionPage> getReceptionPageList() {
        Integer lastValue = null;
        List<AppReceptionPage> receptionPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppReceptionPageScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = appointmentPageScene.execute(visitor);
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            receptionPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return receptionPageList;
    }

    /**
     * 获取接待页数量
     *
     * @return 列表数
     */
    public Integer getReceptionPageNum() {
        return getReceptionPageList().size();
    }

    /**
     * 获取接待页数量
     *
     * @return 列表数
     */
    public Integer getPreSalesReceptionPageNum() {
        return getPreSalesReceptionPageList().size();
    }

    /**
     * 获取售后员工列表
     *
     * @return 售后员工
     */
    public AppReceptionReceptorList getReceptorList() {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        return receptorListScene.execute(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    /**
     * 获取销售员工列表
     *
     * @return 售后员工
     */
    public AppReceptionReceptorList getPreSalesReceptorList() {
        IScene receptorListScene = AppReceptorListScene.builder().shopId(getShopId()).build();
        return receptorListScene.execute(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    /**
     * 获取售后员工列表
     *
     * @param account 员工账号
     * @return 售后员工
     */
    public AppReceptionReceptorList getReceptorList(EnumAccount account) {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        return receptorListScene.execute(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("name").equals(account.getName())).map(e -> toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    /**
     * 编辑资料
     */
    public void editCustomerInfo() {
        PreSalesReceptionPageBean preSalesReceptionPageBean = getFirstPreSalesReceptionPage();
        String shopId = String.valueOf(preSalesReceptionPageBean.getShopId());
        String id = String.valueOf(preSalesReceptionPageBean.getId());
        IScene scene = AppCustomerDetailScene.builder().id(id).shopId(shopId).build();
        AppCustomerDetailBean appCustomerDetailBean = toJavaObject(scene, AppCustomerDetailBean.class);
        AppCustomerEditScene.builder().shopId(shopId).id(id).customerName(appCustomerDetailBean.getCustomerName())
                .gender(appCustomerDetailBean.getCustomerGender()).customerPhone(appCustomerDetailBean.getCustomerPhone())
                .carModel(appCustomerDetailBean.getCarModelId()).estimatedBuyTime(DateTimeUtil.getFormat(new Date()))
                .build().execute(visitor);
    }

    //-------------------------------------------------------积分中心---------------------------------------------------

    public ExchangePage getExchangePage(Long id) {
        IScene exchangePageScene = ExchangePageScene.builder().build();
        return toJavaObject(exchangePageScene, ExchangePage.class, "id", id);
    }

    /**
     * 创建实物积分兑换
     *
     * @return 积分兑换id
     */
    public ExchangePage createExchangeRealGoods() {
        return createExchangeRealGoods(1);
    }

    /**
     * 创建实物积分兑换
     *
     * @return 积分兑换商品
     */
    public ExchangePage createExchangeRealGoods(int stock) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        long goodsId = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().execute(visitor).getJSONArray("specification_detail_list");
        JSONArray specificationList = new JSONArray(specificationDetailList.stream().map(e -> (JSONObject) e).map(e -> put(e.getInteger("id"), stock)).collect(Collectors.toList()));
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                .exchangePrice("1").isLimit(true).exchangePeopleNum("10")
                .specificationList(specificationList).expireType(2).useDays("10")
                .exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime)
                .build().execute(visitor);
        return toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    private JSONObject put(Integer id, Integer stock) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("stock", stock);
        return jsonObject;
    }

    /**
     * 创建虚拟兑换商品
     *
     * @param voucherId 卡券id
     * @return 积分兑换商品
     */
    public ExchangePage createExchangeFictitiousGoods(Long voucherId) {
        return createExchangeFictitiousGoods(voucherId, 1L);
    }

    /**
     * 创建虚拟兑换商品
     *
     * @param voucherId   卡券id
     * @param exchangeNum 可兑换数量
     * @return 积分兑换商品
     */
    public ExchangePage createExchangeFictitiousGoods(Long voucherId, Long exchangeNum) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId)
                .exchangePrice("1").isLimit(true).exchangePeopleNum("10").exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime).expireType(2).useDays("10").exchangeNum(String.valueOf(exchangeNum)).build().execute(visitor);
        return toFirstJavaObject(ExchangePageScene.builder().build(), ExchangePage.class);
    }


    /**
     * 获取积分兑换包含的卡券信息
     *
     * @param id 兑换商品id
     * @return 卡券信息
     */
    public VoucherFormVoucherPageBean getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().execute(visitor).getString("goods_name");
        return getVoucherPage(voucherName);
    }

    public void modifyExchangeGoodsLimit(Long exchangeGoodsId, String exchangeGoodsType, Boolean isLimit) {
        IScene scene = ExchangeGoodsDetailScene.builder().id(exchangeGoodsId).build();
        ExchangeGoodsDetailBean exchangeGoodsDetail = JSONObject.toJavaObject(scene.execute(visitor), ExchangeGoodsDetailBean.class);
        EditExchangeGoodsScene.EditExchangeGoodsSceneBuilder builder = EditExchangeGoodsScene.builder()
                .exchangeGoodsType(exchangeGoodsDetail.getExchangeGoodsType()).goodsId(exchangeGoodsDetail.getGoodsId())
                .exchangePrice(exchangeGoodsDetail.getExchangePrice()).exchangeNum(exchangeGoodsDetail.getExchangeNum())
                .exchangeStartTime(exchangeGoodsDetail.getExchangeStartTime()).exchangeEndTime(exchangeGoodsDetail.getExchangeEndTime())
                .isLimit(isLimit).id(exchangeGoodsDetail.getId());
        builder = isLimit ? builder.exchangePeopleNum(1) : builder;
        builder = exchangeGoodsType.equals(CommodityTypeEnum.REAL.name()) ? builder : builder.expireType(2).useDays(10);
        builder.build().execute(visitor);
    }

    //-------------------------------------------------------活动---------------------------------------------------

    /**
     * 获取文章id
     *
     * @return 文章id集合
     */
    public List<Long> getArticleIdList() {
        JSONArray array = ArticleList.builder().build().execute(visitor).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * 获取行集合
     *
     * @param urlPath   接口路径
     * @param excelName 表格名称
     * @return 行集合
     */
    public IRow[] getRows(String urlPath, String excelName) {
        //下载文件到resources/excel
        String outputPath = "/src/main/resources/excel/" + excelName;
        UrlOutputUtil.toIoSave(urlPath, outputPath);
        String relativePath = "/excel/" + excelName;
        logger.info("relativePath is {}", relativePath);
        IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.EXCEL.getContainer()).build().createExcel(relativePath);
        return Arrays.stream(entities).map(IEntity::getCurrent).toArray(IRow[]::new);
    }

    public void compareResponseAndParam(IScene scene, IEnum[] iEnums) {
        List<JSONObject> list = toJavaObjectList(scene, JSONObject.class);
        scene.getBody().entrySet().stream().filter(body -> body.getValue() != null && !body.getKey().equals("page") && !body.getKey().equals("size"))
                .forEach(body -> list.forEach(jsonObject -> jsonObject.entrySet().stream().filter(e -> e.getKey().equals(getHeader(iEnums, body.getKey())))
                        .forEach(e -> CommonUtil.checkResult(e.getKey(), String.valueOf(body.getValue()), String.valueOf(e.getValue())))));
    }

    private String getHeader(IEnum[] iEnums, String key) {
        return Arrays.stream(iEnums).map(e -> e.findAttributeByKey(key)).collect(Collectors.toList()).get(0);
    }

    public String[] getMessageList(@NotNull IScene scene) {
        List<String> list = scene.getKeyList();
        logger.info("keyList is：{}", list);
        return list.stream().map(e -> JSONObject.toJavaObject(scene.remove(e).execute(visitor, false), Response.class))
                .map(Response::getMessage).collect(Collectors.toList()).toArray(new String[list.size()]);
    }

    /**
     * @description :获取指定休假的销售
     **/
    public String getVacationSaleId() {
        return visitor.isDaily() ? "uid_f1a745c7" : "uid_250e621b";
    }

    /**
     * @description :获取指定忙碌的销售，用于每日初始化设置忙碌，快速获取忙碌的销售
     **/
    public String getBusySaleId() {
        return visitor.isDaily() ? "uid_caf1b799" : "uid_a27173d3";
    }

    /**
     * @description :获取车型id
     **/
    public Long getBuyCarId() {
        return visitor.isDaily() ? 335L : 21540L;
    }


    /**
     * 获取指定父权限可选择的权限
     *
     * @param parentRole 父权限
     * @return 权限map
     */
    public Map<Integer, String> getAuthRoleMap(int parentRole) {
        Map<Integer, String> map = new HashMap<>();
        IScene scene = AuthTreeScene.builder().parentRole(parentRole).build();
        scene.execute(visitor).getJSONArray("children").stream().map(e -> (JSONObject) e)
                .forEach(e -> e.getJSONArray("children").stream().map(a -> (JSONObject) a)
                        .forEach(a -> map.put(a.getInteger("value"), a.getString("label"))));
        return map;
    }

    /**
     * 获取任意一个权限map
     *
     * @return map
     */
    public Map<Integer, String> getRandomRoleMap() {
        Map<Integer, String> map = new HashMap<>();
        IScene scene = RoleListScene.builder().build();
        List<JSONObject> list = toJavaObjectList(scene, JSONObject.class, "list");
        JSONObject response = list.stream().filter(e -> !e.getString("name").equals("超级管理员")).findFirst().orElse(null);
        Preconditions.checkArgument(response != null, "角色为空");
        map.put(response.getInteger("id"), response.getString("name"));
        return map;
    }

    /**
     * 获取门店列表map
     *
     * @return map
     */
    public JSONArray getShopIdArray() {
        return ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
    }

    /**
     * 删除工作人员
     *
     * @param phone 电话号
     */
    public void deleteStaff(String phone) {
        IScene scene = StaffPageScene.builder().phone(phone).build();
        JSONObject response = toFirstJavaObject(scene, JSONObject.class);
        String id = response.getString("id");
        StaffDeleteScene.builder().id(id).build().execute(visitor);
    }

    /**
     * @return : 空闲中 最后一位/第一位 销售的JSONObject
     * {"sale_id":"销售id",
     * "sale_status":"销售状态",
     * "sale_name":"销售姓名",
     * "order":当前状态的排序,
     * "status":状态值 }
     * @params : -
     * @description : 用于检查空闲中最后一位销售
     **/
    public JSONObject getLastSale() {
        return AppSaleScheduleDayListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                filter(e -> Objects.equals(e.getString("sale_status"), "空闲中")).min((x, y) -> y.getInteger("order") - x.getInteger("order")).get();
    }

    /**
     * @param statusId: 状态值：   {0:"空闲中",1:"接待中",2:"忙碌中",3:"休假中"}
     * @return : 指定状态的销售 JSONObject，没有则返回 null
     * @description : 用于获取当日排班中指定状态的一位销售，
     **/
    public JSONObject getNeededSale(Integer statusId) {
        return AppSaleScheduleDayListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                filter(e -> e.getInteger("status").equals(statusId)).findAny().orElse(null);
    }

}
