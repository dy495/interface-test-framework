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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.presalesreception.AppPreSalesReceptionPageBean;
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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.carmodel.AppCarModelTreeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.*;
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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.UseRangeSubjectListScene;
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
 * ??????????????????
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
        visitor.login(scene);
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
        String pic = new ImageUtil().getImageBinary("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/common/multimedia/picture/touxiang.jpg");
        return UploadScene.builder().pic("data:image/jpeg;base64," + pic).permanentPicType(0).ratio(1.0).ratioStr("1:1").build().visitor(visitor).execute().getString("pic_path");
    }

    /**
     * ??????????????????id
     *
     * @param stock ??????
     * @param type  ??????
     * @return ??????id
     */
    public Long createVoucherId(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucher(stock, type);
        return getVoucherId(voucherName);
    }

    /**
     * ?????????????????????
     *
     * @param stock ??????
     * @param type  ??????
     * @return ??????id
     */
    public VoucherFormVoucherPageBean createVoucherPage(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucher(stock, type);
        return getVoucherPage(voucherName);
    }

    /**
     * ?????????????????????
     *
     * @return ??????id
     */
    public VoucherFormVoucherPageBean getOccupyVoucherId() {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.WORKING.name()).build();
        List<VoucherFormVoucherPageBean> voucherPageBeanList = toJavaObjectList(scene, VoucherFormVoucherPageBean.class);
        return voucherPageBeanList.stream().filter(e -> e.getSurplusInventory() >
                e.getAllowUseInventory() && e.getAllowUseInventory() != 0).findFirst().orElseGet(this::useVoucher);
    }

    /**
     * ??????????????????
     *
     * @return ??????id
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
     * ??????4????????????
     *
     * @param stock ????????????
     * @param type  ????????????
     * @return ????????????
     */
    public String createVoucher(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucherName(type);
        createVoucherBuilder(stock, type).voucherName(voucherName).build().visitor(visitor).execute();
        return voucherName;
    }

    /**
     * ????????????
     *
     * @param stock ??????
     * @param type  ????????????
     * @return ???????????????builder
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
                builder.exchangeCommodityName("???????????????????????????").parValue(null);
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
     * ??????????????????
     *
     * @param selfVerification ????????????
     * @return CreateVoucher.CreateVoucherBuilder
     */
    public CreateScene.CreateSceneBuilder createVoucherBuilder(Boolean selfVerification) {
        return CreateScene.builder().subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).cost(0.01)
                .voucherDescription("<p>" + getDesc() + "</p>").shopType(0).shopIds(getShopIdList(2))
                .selfVerification(selfVerification).isDefaultPic(true);
    }

    /**
     * ?????????????????????????????????
     *
     * @return ?????????
     */
    public String createVoucherName() {
        return createVoucherName(VoucherTypeEnum.CUSTOM);
    }

    /**
     * ?????????????????????????????????
     *
     * @return ?????????
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
     * ????????????
     *
     * @return ??????
     */
    public String getDesc() {
        return EnumDesc.DESC_BETWEEN_20_30.getDesc();
    }

    /**
     * ????????????
     *
     * @return ????????????
     */
    public Double getParValue() {
        return 49.99;
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/?????????.jpg";
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
                .ratio(ratio).build().visitor(visitor).execute().getString("pic_path");
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    public String getSubjectType() {
        JSONArray array = UseRangeSubjectListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        JSONObject jsonObject = array.stream().map(e -> (JSONObject) e).findFirst().orElse(null);
        Preconditions.checkArgument(jsonObject != null, "??????????????????");
        return jsonObject.getString("subject_key");
    }

    /**
     * ??????????????????
     *
     * @return ????????????
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
     * ????????????id
     *
     * @return ??????id
     */
    public List<Long> getBrandIdList() {
        JSONArray array = UseRangeDetailScene.builder().subjectKey("BRAND").build().visitor(visitor).execute().getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * ????????????id
     *
     * @param shopCount ??????
     * @return ??????id
     */
    public List<Long> getShopIdList(Integer shopCount) {
        return getShopIdList().subList(0, shopCount);
    }

    /**
     * ????????????id
     *
     * @return ??????id
     */
    public List<Long> getShopIdList() {
        JSONArray array = ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList());
    }

    /**
     * ??????????????????
     *
     * @param voucherId ??????id
     * @return ?????????
     */
    public String getVoucherName(long voucherId) {
        return getVoucherPage(voucherId).getVoucherName();
    }

    /**
     * ????????????selfVerification???????????????
     *
     * @param voucherId ??????id
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
                .shopType(detail.getShopType()).subjectName(detail.getSubjectName()).id(voucherId).build().visitor(visitor).execute();
    }

    /**
     * ????????????
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
     * ????????????id
     *
     * @param voucherName ????????????
     * @return ??????id(Long)
     */
    public Long getVoucherId(String voucherName) {
        return getVoucherPage(voucherName).getVoucherId();
    }

    public void deleteVoucher(Long voucherId) {
        DeleteVoucherScene.builder().id(voucherId).build().visitor(visitor).execute();
    }

    public void recallVoucher(Long voucherId) {
        RecallVoucherScene.builder().id(voucherId).build().visitor(visitor).execute();
    }

    /**
     * ????????????????????????
     *
     * @param voucherPage ?????????
     * @return ??????????????????
     */
    public VoucherSendRecord getVoucherSendRecord(VoucherFormVoucherPageBean voucherPage) {
        return getVoucherSendRecord(voucherPage.getVoucherId());
    }

    /**
     * ????????????????????????
     *
     * @param voucherId ??????id
     * @return ??????????????????
     */
    public VoucherSendRecord getVoucherSendRecord(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return toFirstJavaObject(scene, VoucherSendRecord.class);
    }

    /**
     * ????????????????????????
     *
     * @param voucherId ??????id
     * @return ??????????????????
     */
    public List<VoucherInvalidPageBean> getVoucherInvalidList(Long voucherId) {
        IScene scene = VoucherInvalidPageScene.builder().id(voucherId).build();
        return toJavaObjectList(scene, VoucherInvalidPageBean.class);
    }

    /**
     * ???????????????????????????
     *
     * @return ?????????
     */
    public String getRepetitionVerificationPhone() {
        JSONArray array = VerificationPeopleScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getString("verification_phone")).findFirst().orElse(null);
    }

    /**
     * ?????????????????????
     *
     * @param voucherName ????????????
     * @return ???????????????
     */
    public VoucherFormVoucherPageBean getVoucherPage(String voucherName) {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
        return toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_name", voucherName);
    }

    /**
     * ?????????????????????
     *
     * @param voucherId ??????id
     * @return ???????????????
     */
    public VoucherFormVoucherPageBean getVoucherPage(Long voucherId) {
        IScene scene = VoucherFormVoucherPageScene.builder().build();
        return toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
    }

    /**
     * ???????????????
     *
     * @param voucherPage ?????????
     * @return ???????????????
     */
    public VoucherFormVoucherPageBean flushVoucherPage(VoucherFormVoucherPageBean voucherPage) {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherPage.getVoucherName()).build();
        return toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherPage.getVoucherId());
    }

    /**
     * ??????????????????
     *
     * @param voucherId ??????id
     * @return ????????????
     */
    public VoucherDetailBean getVoucherDetail(Long voucherId) {
        IScene scene = VoucherDetailScene.builder().id(voucherId).build();
        return toJavaObject(scene, VoucherDetailBean.class);
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    public JSONArray getVoucherArray() {
        VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
        return getVoucherArray(voucherPage, 1);
    }

    /**
     * ??????????????????
     *
     * @param voucherPage  ?????????
     * @param voucherCount ????????????
     * @return ????????????
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
     * ??????????????????????????????
     *
     * @param voucherId ??????id
     * @return ?????????
     */
    public String getVoucherCode(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return scene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("voucher_code");
    }

    /**
     * ???????????????
     *
     * @param phone ?????????
     * @return ?????????
     */
    public String getPlatNumber(String phone) {
        JSONArray plateList = SearchCustomerScene.builder().customerPhone(phone).build().visitor(visitor).execute().getJSONArray("plate_list");
        return plateList.stream().map(e -> (JSONObject) e).map(e -> e.getString("plate_number")).findFirst().orElse(null);
    }

    /**
     * ???????????????????????????
     *
     * @param voucherName ????????????
     * @return ??????????????????
     */
    public ApplyPageBean getAuditingApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        return toJavaObject(scene, ApplyPageBean.class, "name", voucherName);
    }

    /**
     * ???????????????????????????
     *
     * @param voucherName ????????????
     * @return ??????????????????
     */
    public ApplyPageBean getApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        return toJavaObject(scene, ApplyPageBean.class, "name", voucherName);
    }

    /**
     * ???????????????????????????
     *
     * @param voucherName ????????????
     * @return ??????????????????
     */
    public ApplyPageBean getApplyPageByTime(String voucherName, String time) {
        logger.info("time is:{}", time);
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        List<ApplyPageBean> voucherApplies = toJavaObjectList(scene, ApplyPageBean.class);
        return voucherApplies.stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTime().contains(time)).findFirst().orElse(null);
    }

    /**
     * ????????????
     *
     * @param voucherName ????????????
     * @param status      ?????? 1/??????2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        ApplyPageBean applyPage = toJavaObject(scene, ApplyPageBean.class, "name", voucherName);
        ApplyApprovalScene.builder().id(applyPage.getId()).status(status).build().visitor(visitor).execute();
    }

    //--------------------------------------------------??????----------------------------------------------------------

    /**
     * ???????????????????????????
     *
     * @param packageStatusEnum packageStatusEnum
     * @return ????????????
     */
    public PackagePage getPackagePage(PackageStatusEnum packageStatusEnum) {
        IScene packageFormPageScene = PackageFormPageScene.builder().build();
        return toJavaObject(packageFormPageScene, 50, PackagePage.class, "audit_status_name", packageStatusEnum.getName());
    }

    /**
     * ????????????????????????
     *
     * @param packageName ?????????
     * @return ??????????????????
     */
    public PackagePage getPackagePage(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return toJavaObject(scene, 50, PackagePage.class, "package_name", packageName);
    }

    /**
     * ??????????????????
     *
     * @param packageName ?????????
     * @return ????????????
     */
    public List<PackagePage> getPackagePageList(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return toJavaObjectList(scene, 50, PackagePage.class);
    }

    /**
     * ??????????????????
     *
     * @param packageName ?????????
     * @return ????????????
     */
    public PackagePage getPackagePage(String packageName, Long packageId) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return toJavaObject(scene, PackagePage.class, "package_id", packageId);
    }

    /**
     * ??????????????????
     *
     * @param packageId ??????id
     * @return ????????????
     */
    public PackagePage getPackagePage(Long packageId) {
        IScene scene = PackageFormPageScene.builder().build();
        return toJavaObject(scene, 50, PackagePage.class, "id", packageId);
    }

    /**
     * ????????????id
     *
     * @param packageName ????????????
     * @return ??????id
     */
    public Long getPackageId(String packageName) {
        return getPackagePage(packageName).getPackageId();
    }

    /**
     * ???????????????
     *
     * @param packageId ??????id
     * @return ?????????
     */
    public String getPackageName(Long packageId) {
        return getPackagePage(packageId).getPackageName();
    }

    /**
     * ??????????????????
     *
     * @param packageName ?????????
     */
    public void makeSureBuyPackage(String packageName) {
        IScene scene = BuyPackageRecordScene.builder().packageName(packageName).size(SIZE / 10).build();
        JSONObject jsonObject = toJavaObject(scene, JSONObject.class, "package_name", packageName);
        MakeSureBuyScene.builder().id(jsonObject.getLong("id")).auditStatus("AGREE").build().visitor(visitor).execute();
    }

    /**
     * ??????????????????
     *
     * @param packageName ?????????
     */
    public void cancelSoldPackage(String packageName) {
        IScene scene = BuyPackageRecordScene.builder().packageName(packageName).size(SIZE / 10).build();
        JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
        Long id = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals(packageName)).map(e -> e.getLong("id")).findFirst().orElse(null);
        CancelSoldPackageScene.builder().id(id).id(id).build().visitor(visitor).execute();
    }

    /**
     * ?????????????????????
     *
     * @return ?????????
     */
    public String createPackageName(@NotNull UseRangeEnum useRangeEnum) {
        int num = CommonUtil.getRandom(1, 1000000);
        String packageName = useRangeEnum.getName() + "??????" + num;
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        List<PackagePage> packagePages = toJavaObjectList(scene, PackagePage.class, "package_name", packageName);
        return packagePages.isEmpty() ? packageName : createPackageName(useRangeEnum);
    }

    /**
     * ??????????????????
     *
     * @param voucherList ????????????????????????
     * @return ?????????
     */
    public PackagePage createPackage(JSONArray voucherList, UseRangeEnum anEnum) {
        String packageName = createPackageName(anEnum);
        CreatePackageScene.builder().packageName(packageName).packageDescription(getDesc()).subjectType(getSubjectType())
                .subjectId(getSubjectDesc(getSubjectType())).voucherList(voucherList).packagePrice("49.99").status(true)
                .shopIds(getShopIdList(3)).expireType(2).expiryDate(10).build().visitor(visitor).execute();
        return getPackagePage(packageName);
    }

    public PackageDetailBean getPackageDetail(Long packageId) {
        IScene packageDetailScene = PackageDetailScene.builder().id(packageId).build();
        return toJavaObject(packageDetailScene, PackageDetailBean.class);
    }

    /**
     * ??????????????????
     *
     * @param packageId   ?????? id
     * @param voucherList ??????????????????
     * @return ?????????
     */
    public String editPackage(Long packageId, JSONArray voucherList) {
        IScene scene = PackageDetailScene.builder().id(packageId).build();
        PackageDetailBean packageDetail = toJavaObject(scene, PackageDetailBean.class);
        Preconditions.checkArgument(packageDetail != null, packageId + " ???????????????????????????");
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
                .visitor(visitor).execute();
        return packageDetail.getPackageName();
    }

    /**
     * ????????????
     *
     * @param voucherList ?????????????????????
     * @return ?????????
     */
    public PackagePage editPackage(JSONArray voucherList) {
        IScene scene = PackageFormPageScene.builder().size(50).page(1).build();
        PackagePage packagePage = scene.visitor(visitor).execute().getJSONArray("list").stream().map(obj -> (JSONObject) obj)
                .map(obj -> JSONObject.toJavaObject(obj, PackagePage.class))
                .filter(pkg -> !EnumVP.isContains(pkg.getPackageName()))
                .findFirst().orElse(null);
        Preconditions.checkArgument(packagePage != null, "????????????");
        Long packageId = packagePage.getPackageId();
        String packageName = packagePage.getPackageName();
        EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherList(voucherList).packagePrice("1.11").status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).expireType(2).expiryDate(12).build().visitor(visitor).execute();
        return packagePage;
    }

    /**
     * ????????????
     *
     * @param voucherPage  ?????????
     * @param voucherCount ????????????
     * @return ?????????
     */
    public PackagePage editPackage(VoucherFormVoucherPageBean voucherPage, int voucherCount) {
        JSONArray voucherList = getVoucherArray(voucherPage, voucherCount);
        return editPackage(voucherList);
    }

    /**
     * ??????????????????
     *
     * @param voucherList ???????????????????????????
     * @param type        0??????/1??????
     */
    public void buyTemporaryPackage(JSONArray voucherList, int type) {
        PurchaseTemporaryPackageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(getPlatNumber(EnumAppletToken.JC_WM_DAILY.getPhone()))
                .voucherList(voucherList).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(type).build().visitor(visitor).execute();
    }

    /**
     * ??????????????????
     *
     * @param packageId ?????????????????????
     * @param type      0??????/1??????
     */
    public void buyFixedPackage(Long packageId, int type) {
        PurchaseFixedPackageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone())
                .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("1.00").expiryDate("1")
                .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(type).build().visitor(visitor).execute();
    }

    /**
     * ???????????????????????????
     *
     * @param packageId ?????????????????????
     * @param type      0??????/1??????
     */
    public void receptionBuyFixedPackage(Long packageId, int type) {
        IScene scene = PackageDetailScene.builder().id(packageId).build();
        PackageDetailBean packageDetail = toJavaObject(scene, PackageDetailBean.class);
        Preconditions.checkArgument(packageDetail != null, "????????? " + packageId + " ??????????????????");
        Integer expiryDate = packageDetail.getExpiryDate();
        Integer expireType = packageDetail.getExpireType();
        String packagePrice = packageDetail.getPackagePrice();
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone()).build();
        ReceptionPage receptionPage = toJavaObjectList(receptionPageScene, ReceptionPage.class).get(0);
        //????????????
        ReceptionPurchaseFixedPackageScene.builder().customerId(receptionPage.getCustomerId())
                .customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name()).expireType(expireType).expiryDate(expiryDate)
                .extendedInsuranceCopies("").extendedInsuranceYear("").packageId(packageId).packagePrice(packagePrice)
                .plateNumber(receptionPage.getPlateNumber()).receptionId(receptionPage.getId()).remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .shopId(receptionPage.getShopId()).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).type(type)
                .build().visitor(visitor).execute();
    }

    /**
     * ???????????????????????????
     *
     * @param voucherList ?????????????????????
     * @param type        0??????/1??????
     */
    public void receptionBuyTemporaryPackage(JSONArray voucherList, int type) {
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAppletToken.JC_WM_DAILY.getPhone()).build();
        ReceptionPage receptionPage = toJavaObjectList(receptionPageScene, ReceptionPage.class).get(0);
        //????????????
        ReceptionPurchaseTemporaryPackageScene.builder().customerId(receptionPage.getCustomerId())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).customerPhone("").expireType(2).expiryDate("10")
                .extendedInsuranceCopies("").extendedInsuranceYear("").plateNumber(receptionPage.getPlateNumber())
                .receptionId(receptionPage.getId()).remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).shopId(receptionPage.getShopId())
                .subjectId(getSubjectDesc(getSubjectType())).subjectType(getSubjectType()).type(type).voucherList(voucherList).build().visitor(visitor).execute();
    }

    //-------------------------------------------------??????----------------------------------------------------------

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     *
     * @param type               ?????????????????? 0????????????1?????????
     * @param voucherOrPackageId ??????id
     * @param immediately        ??????????????????
     */
    public Response pushCustomMessage(Integer type, boolean immediately, Long... voucherOrPackageId) {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/excel/??????????????????.xlsx";
        return pushCustomMessage(type, immediately, filePath, voucherOrPackageId);
    }

    public Response pushCustomMessage(Integer type, boolean immediately, String filePath, Long... voucherOrPackageId) {
        List<Long> voucherOrPackageList = new ArrayList<>(Arrays.asList(voucherOrPackageId));
        JSONObject response = CustomerImportScene.builder().filePath(filePath).build().visitor(visitor).upload();
        Preconditions.checkArgument(response.getInteger("code") == 1000);
        List<Long> customerIdList = response.getJSONObject("data").getJSONArray("customer_id_list").toJavaList(Long.class);
        PushMessageScene.PushMessageSceneBuilder builder = PushMessageScene.builder().customerIdList(customerIdList)
                .messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc()).messageContent(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                .type(type).useTimeType(2).useDays("10").voucherOrPackageList(voucherOrPackageList);
        String d = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm:ss");
        long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(d));
        builder = immediately ? builder.ifSendImmediately(true) : builder.ifSendImmediately(false).sendTime(sendTime);
        return builder.build().visitor(visitor).getResponse();
    }

    //----------------------------------------------------????????????-------------------------------------------------------

    /**
     * ????????????id??????????????????
     *
     * @param appointmentId ??????id
     * @return ????????????
     */
    public AppointmentRecordAppointmentPageBean getAppointmentPageById(Long appointmentId, String type) {
        IScene scene = AppointmentPageScene.builder().type(type).build();
        return toJavaObject(scene, AppointmentRecordAppointmentPageBean.class, "id", appointmentId);
    }

    //----------------------------------------------------????????????-------------------------------------------------------

    /**
     * ????????????id??????????????????
     *
     * @param receptionId ??????id
     * @return ????????????
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
     * ???????????????????????????
     *
     * @return ????????????
     */
    public ReceptionPage getFirstReceptionPage() {
        return toFirstJavaObject(ReceptionPageScene.builder().build(), ReceptionPage.class);
    }

    /**
     * ???????????????????????????
     *
     * @return ????????????
     */
    public PreSalesReceptionPageBean getFirstPreSalesReceptionPage() {
        return toFirstJavaObject(PreSalesReceptionPageScene.builder().build(), PreSalesReceptionPageBean.class);
    }

    //----------------------------------------------------????????????-------------------------------------------------------

    /**
     * ????????????????????????
     *
     * @return ?????????
     */
    public String getDistinctPhone() {
        String phone = "155" + CommonUtil.getRandom(8);
        IScene scene = VerificationPeopleScene.builder().verificationPhone(phone).build();
        int total = scene.visitor(visitor).execute().getInteger("total");
        if (total == 0) {
            return phone;
        }
        return getDistinctPhone();
    }

    /**
     * ???????????????
     *
     * @param verificationIdentity ???????????????
     * @return ?????????
     */
    public String getVerificationCode(String verificationIdentity) {
        return getVerificationCode(true, verificationIdentity);
    }

    /**
     * ???????????????
     *
     * @param verificationStatus   ???????????????
     * @param verificationIdentity ???????????????
     * @return ?????????
     */
    public String getVerificationCode(boolean verificationStatus, String verificationIdentity) {
        List<String> list = new ArrayList<>();
        VerificationPeopleScene.VerificationPeopleSceneBuilder builder = VerificationPeopleScene.builder();
        int total = builder.build().visitor(visitor).execute().getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            JSONArray array = builder.page(i).size(SIZE).build().visitor(visitor).execute().getJSONArray("list");
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
     * ????????????????????????
     *
     * @param code   ?????????
     * @param status ??????
     */
    public void switchVerificationStatus(String code, boolean status) {
        IScene verificationPeopleScene = VerificationPeopleScene.builder().verificationCode(code).build();
        long id = verificationPeopleScene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
        SwitchVerificationStatusScene.builder().id(id).status(status).build().visitor(visitor).execute();
    }

    //-------------------------------------------------?????????----------------------------------------------------------

    /**
     * ???????????????
     * ??????????????????????????????????????????shopId?????????staffId??????
     *
     * @param type ???????????? MAINTAIN????????????REPAIR?????????
     * @return id ??????id
     */
    public Long appointment(AppointmentTypeEnum type, String date) {
        AppletAppointmentSubmitScene.AppletAppointmentSubmitSceneBuilder builder = AppletAppointmentSubmitScene.builder().type(type.name())
                .shopId(getShopId()).staffId(getStaffId()).appointmentName("????????????").appointmentPhone("15321527989");
        builder = type.equals(AppointmentTypeEnum.REPAIR) ? builder.faultDescription(EnumDesc.DESC_BETWEEN_15_20.getDesc()).timeId(getTimeId(date, EnumAppointmentType.REPAIR.name())).carId(getCarId())
                : type.equals(AppointmentTypeEnum.TEST_DRIVE) ? builder.carStyleId(getCarStyleId()).staffId(getTestDriverStaffId()).timeId(getTimeId(date, EnumAppointmentType.TEST_DRIVE.name()))
                : builder.timeId(getTimeId(date, EnumAppointmentType.MAINTAIN.name())).carId(getCarId());
        return builder.build().visitor(visitor).execute().getLong("id");
    }

    public Long getCarStyleId() {
        JSONArray list = AppletBrandListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        Long brandId = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("name").equals("?????????")).map(e -> e.getLong("id")).findFirst().orElse(null);
        JSONObject response = AppletStyleListScene.builder().brandId(brandId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
        return response.getLong("id");
    }

    /**
     * ????????????id
     *
     * @param shopId ??????id
     * @return ??????id
     */
    public String getCarModelId(String shopId) {
        IScene carModelTreeScene = AppCarModelTreeScene.builder().shopId(Long.parseLong(shopId)).build();
        return carModelTreeScene.visitor(visitor).execute().getJSONArray("children").getJSONObject(0).getJSONArray("children")
                .getJSONObject(0).getJSONArray("children").getJSONObject(0).getString("value");
    }

    public String getTestDriverStaffId() {
        //uid_df9293ba JC_DAILY_LXQ
        return visitor.isDaily() ? EnumAccount.JC_DAILY_LXQ.getUid() : "uid_35a1d271";
    }

    /**
     * ??????????????????id
     */
    public Long getTimeId(String date, String type) {
        AppletAppointmentTimeListScene.AppletAppointmentTimeListSceneBuilder builder = AppletAppointmentTimeListScene.builder();
        builder.type(type).shopId(getShopId()).day(date).build();
        builder = type.equals(EnumAppointmentType.TEST_DRIVE.name()) ? builder.carStyleId(getCarStyleId()) : builder.carId(getCarId());
        JSONArray array = builder.build().visitor(visitor).execute().getJSONArray("list");
        List<AppletAppointmentTimeList> timeList = array.stream().map(object -> (JSONObject) object).map(object -> JSONObject.toJavaObject(object, AppletAppointmentTimeList.class)).collect(Collectors.toList());
        return timeList.stream().filter(e -> !e.getIsFull()).map(AppletAppointmentTimeList::getId).findFirst().orElse(null);
    }

    /**
     * ????????????id
     *
     * @return ??????id
     */
    public Long getShopId() {
        return visitor.isDaily() ? 46522L : 20034L;
    }

    /**
     * ????????????id
     *
     * @return ??????id
     */
    public String getStaffId() {
        IScene maintainStaffListScene = AppletAppointmentStaffListScene.builder().shopId(getShopId()).type(AppointmentTypeEnum.MAINTAIN.name()).build();
        JSONArray jsonArray = maintainStaffListScene.visitor(visitor).execute().getJSONArray("list");
        return Objects.requireNonNull(jsonArray.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getString("uid");
    }

    /**
     * ???????????????carId
     *
     * @return ????????????id
     */
    public Long getCarId() {
        IScene appletCarListScene = AppletCarListScene.builder().build();
        JSONObject jsonObject = appletCarListScene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
        Preconditions.checkArgument(jsonObject != null, "???????????????????????????");
        return jsonObject.getLong("id");
    }

    /**
     * ?????????????????????????????????
     *
     * @return ??????????????????
     */
    public List<AppletCarInfo> getAppletCarList() {
        return AppletCarListScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .map(e -> JSONObject.toJavaObject(e, AppletCarInfo.class)).collect(Collectors.toList());

    }

    /**
     * ???????????????????????????????????????
     *
     * @param plateNumber ?????????
     * @param carModelId  ??????id
     * @return ????????????
     */
    public AppletCarInfo createCar(String plateNumber, Long carModelId) {
        AppletCarCreateScene.builder().modelId(carModelId).plateNumber(plateNumber).build().visitor(visitor).execute();
        IScene scene = AppletCarListScene.builder().build();
        JSONObject obj = scene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .filter(e -> e.getString("plate_number") != null)
                .filter(e -> e.getString("plate_number").equals(plateNumber))
                .findFirst().orElse(null);
        if (obj != null) {
            return JSONObject.toJavaObject(obj, AppletCarInfo.class);
        }
        return null;
    }

    /**
     * ???????????????????????????
     *
     * @param voucherCode ?????????
     */
    public AppletVoucherInfo getAppletVoucherInfo(String voucherCode) {
        AppletVoucherInfo appletVoucherInfo;
        Integer id = null;
        Integer status = null;
        JSONArray list;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.visitor(visitor).execute();
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
     * ??????app????????????
     *
     * @return ????????????
     */
    public List<AppPreSalesReceptionPageBean> getAppPreSalesReceptionPageList() {
        List<AppPreSalesReceptionPageBean> list = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppPreSalesReceptionPageScene.builder().size(10).lastValue(lastValue).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppPreSalesReceptionPageBean.class)).forEach(list::add);
        } while (array.size() == 10);
        return list;
    }

    /**
     * ???????????????????????????
     *
     * @param voucherCode ?????????
     */
    public AppletVoucherInfo getAppletPackageVoucherInfo(String voucherCode) {
        IScene appletPackageListScene = AppletPackageListScene.builder().lastValue(null).type("GENERAL").size(20).build();
        int id = appletPackageListScene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("id");
        IScene appletPackageDetailScene = AppletPackageDetailScene.builder().id((long) id).build();
        JSONArray list = appletPackageDetailScene.visitor(visitor).execute().getJSONArray("list");
        return list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_code").equals(voucherCode)).map(e -> JSONObject.toJavaObject(e, AppletVoucherInfo.class)).findFirst().orElse(null);
    }

    /**
     * ????????????????????????????????????
     *
     * @param voucherUseStatusEnum ?????????????????????
     */
    public AppletVoucher getAppletVoucher(VoucherUseStatusEnum voucherUseStatusEnum) {
        AppletVoucher appletVoucher;
        Integer id = null;
        Integer status = null;
        JSONArray array;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.visitor(visitor).execute();
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
     * ???????????????????????????
     *
     * @return ????????????
     */
    public int getAppletVoucherNum() {
        Integer id = null;
        Integer status = null;
        JSONArray array;
        int listSize = 0;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.visitor(visitor).execute();
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ???????????????????????????
     *
     * @return ????????????
     */
    public int getAppletVoucherNum(VoucherUseStatusEnum voucherUseStatusEnum) {
        Integer id = null;
        Integer status = null;
        JSONArray array;
        int listSize = 0;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = scene.visitor(visitor).execute();
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            listSize += array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("status_name").equals(voucherUseStatusEnum.getName())).count();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ???????????????????????????
     *
     * @return ????????????
     */
    public int getAppletPackageNum() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = AppletPackageListScene.builder().lastValue(lastValue).type("type").size(20).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ????????????????????????????????????
     *
     * @return ????????????
     */
    public List<AppletVoucherInfo> getAppletPackageContainVoucherList() {
        List<AppletVoucherInfo> appletVoucherInfoList = new ArrayList<>();
        List<Long> appletPackageId = new ArrayList<>();
        Long lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletPackageListScene.builder().lastValue(lastValue).type("type").size(20).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            appletPackageId.addAll(array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList()));
        } while (array.size() == 20);
        appletPackageId.forEach(id -> {
            JSONArray jsonArray = AppletPackageDetailScene.builder().id(id).build().visitor(visitor).execute().getJSONArray("list");
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
     * ????????????????????????????????????
     *
     * @return ????????????
     */
    public int getAppletMessageNum() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = AppletMessageListScene.builder().lastValue(lastValue).size(20).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ?????????????????????????????????
     *
     * @return ????????????
     */
    public int getAppletAppointmentNum() {
        Integer lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = AppointmentListScene.builder().size(20).lastValue(lastValue).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ?????????????????????????????????
     *
     * @return ??????????????????
     */
    public int getAppletIntegralRecordNum() {
        int listSize = 0;
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletIntegralRecordScene.builder().lastValue(lastValue).size(20).type("ALL").endTime(null).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ???????????????????????????
     *
     * @return ????????????
     */
    public List<AppletIntegralRecord> getAppletIntegralRecordList() {
        List<AppletIntegralRecord> list = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppletIntegralRecordScene.builder().lastValue(lastValue).size(20).type("ALL").endTime(null).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletIntegralRecord.class)).collect(Collectors.toList()));
        } while (array.size() == 20);
        return list;
    }

    /**
     * ?????????????????????????????????
     *
     * @return ????????????
     */
    public int getAppletExchangeRecordNum() {
        int listSize = 0;
        Integer lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * ?????????????????????????????????
     *
     * @return ????????????
     */
    public List<AppletExchangeRecord> getAppletExchangeRecordList() {
        List<AppletExchangeRecord> appletExchangeRecordList = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            appletExchangeRecordList.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletExchangeRecord.class)).collect(Collectors.toList()));
        } while (array.size() == 20);
        return appletExchangeRecordList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param integralSort ????????????
     * @param status       ???????????????
     * @return List<AppletCommodity>
     */
    public List<AppletCommodity> getAppletCommodityList(String integralSort, Boolean status) {
        List<AppletCommodity> appletCommodityListList = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(integralSort).status(status).build().visitor(visitor).execute();
            lastValue = data.getJSONObject("last_value");
            array = data.getJSONArray("list");
            appletCommodityListList.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletCommodity.class)).collect(Collectors.toList()));
        } while (array.size() == 10);
        return appletCommodityListList;

    }

    /**
     * ?????????????????????????????????
     *
     * @return ??????
     */
    public int getAppletCommodityListNum() {
        int listSie = 0;
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(SortTypeEnum.DOWN.name()).status(false).build().visitor(visitor).execute();
            lastValue = data.getJSONObject("last_value");
            array = data.getJSONArray("list");
            listSie += array.size();
        } while (array.size() == 10);
        return listSie;

    }

    //--------------------------------------------------app------------------------------------------------------------

    /**
     * ????????????
     *
     * @param shopId ??????id
     * @return AppPreSalesReceptionPageBean
     */
    public AppPreSalesReceptionPageBean createReception(String shopId) {
        AppPreSalesReceptionCreateScene.builder().customerName("???????????????????????????").customerPhone(EnumAppletToken.JC_GLY_DAILY.getPhone()).sexId("1").intentionCarModelId(getCarModelId(shopId)).estimateBuyCarTime("2100-07-12").build().visitor(visitor).execute();
        return getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("???????????????????????????")).findFirst().orElse(null);
    }

    /**
     * ????????????
     */
    public void finishReception() {
        JSONArray list = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list");
        JSONObject data = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("customer_name").equals("???????????????????????????")).findFirst().orElse(null);
        if (data != null) {
            Long id = data.getLong("id");
            Long shopId = data.getLong("shop_id");
            AppFinishReceptionScene.builder().id(id).shopId(shopId).build().visitor(visitor).execute();
        }
    }

    /**
     * ????????????
     */
    public List<AppFollowUpPage> getFollowUpPageList() {
        Integer time = null;
        Integer id = null;
        List<AppFollowUpPage> followUpPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppFollowUpPageScene.builder().id(id).time(time).size(20).build();
            JSONObject response = appointmentPageScene.visitor(visitor).execute();
            JSONObject lastValue = response.getJSONObject("last_value");
            time = lastValue.getInteger("time");
            id = lastValue.getInteger("id");
            list = response.getJSONArray("list");
            followUpPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppFollowUpPage.class)).collect(Collectors.toList()));
        } while (list.size() == 20);
        return followUpPageList;
    }

    /**
     * ???????????????
     *
     * @return ????????????
     */
    public List<AppAppointmentPage> getAppointmentPageList() {
        Integer lastValue = null;
        List<AppAppointmentPage> appAppointmentPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppAppointmentPageScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = appointmentPageScene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            appAppointmentPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppAppointmentPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return appAppointmentPageList;
    }

    /**
     * ?????????????????????
     *
     * @return ?????????
     */
    public Integer getAppointmentPageNum() {
        return getAppointmentPageList().size();
    }

    /**
     * ????????????????????????
     *
     * @param date ??????
     * @return ????????????
     */
    public Integer appointmentNumber(Date date, String type) {
        String nowDate = DateTimeUtil.getFormat(new Date(), "yyyy-MM");
        IScene scene = TimeTableListScene.builder().type(type).appointmentMonth(nowDate).build();
        return scene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("day").equals(DateTimeUtil.getDayOnMonth(date)))
                .map(e -> e.getInteger("appointment_number") == null ? 0 : e.getInteger("appointment_number")).findFirst().orElse(0);
    }

    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    public List<AppReceptionPage> getPreSalesReceptionPageList() {
        Integer lastValue = null;
        List<AppReceptionPage> receptionPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppPreSalesReceptionPageScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = appointmentPageScene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            receptionPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return receptionPageList;
    }

    /**
     * ???????????????
     *
     * @return ???????????????
     */
    public List<AppReceptionPage> getReceptionPageList() {
        Integer lastValue = null;
        List<AppReceptionPage> receptionPageList = new ArrayList<>();
        JSONArray list;
        do {
            IScene appointmentPageScene = AppReceptionPageScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = appointmentPageScene.visitor(visitor).execute();
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            receptionPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return receptionPageList;
    }

    /**
     * ?????????????????????
     *
     * @return ?????????
     */
    public Integer getReceptionPageNum() {
        return getReceptionPageList().size();
    }

    /**
     * ?????????????????????
     *
     * @return ?????????
     */
    public Integer getPreSalesReceptionPageNum() {
        return getPreSalesReceptionPageList().size();
    }

    /**
     * ????????????????????????
     *
     * @return ????????????
     */
    public AppReceptionReceptorList getAftSalesReceptor() {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        return receptorListScene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> toJavaObject(e, AppReceptionReceptorList.class))
                .filter(e -> !e.getName().contains("??????")).findFirst().orElse(null);
    }

    /**
     * ????????????????????????
     *
     * @return ????????????
     */
    public AppReceptionReceptorList getPreSalesReceptor() {
        IScene receptorListScene = AppReceptorListScene.builder().shopId(getShopId()).build();
        return receptorListScene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> toJavaObject(e, AppReceptionReceptorList.class))
                .filter(e -> !e.getName().contains("??????")).findFirst().orElse(null);
    }

    /**
     * ????????????????????????
     *
     * @param account ????????????
     * @return ????????????
     */
    public AppReceptionReceptorList getReceptorList(EnumAccount account) {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        return receptorListScene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("name").equals(account.getName())).map(e -> toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    /**
     * ????????????
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
                .build().visitor(visitor).execute();
    }

    //-------------------------------------------------------????????????---------------------------------------------------

    public ExchangePage getExchangePage(Long id) {
        IScene exchangePageScene = ExchangePageScene.builder().build();
        return toJavaObject(exchangePageScene, ExchangePage.class, "id", id);
    }

    /**
     * ????????????????????????
     *
     * @return ????????????id
     */
    public ExchangePage createExchangeRealGoods() {
        return createExchangeRealGoods(1);
    }

    /**
     * ????????????????????????
     *
     * @return ??????????????????
     */
    public ExchangePage createExchangeRealGoods(int stock) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        long goodsId = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
        JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().visitor(visitor).execute().getJSONArray("specification_detail_list");
        JSONArray specificationList = new JSONArray(specificationDetailList.stream().map(e -> (JSONObject) e).map(e -> put(e.getInteger("id"), stock)).collect(Collectors.toList()));
        //??????????????????
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                .exchangePrice("1").isLimit(true).exchangePeopleNum("10")
                .specificationList(specificationList).expireType(2).useDays("10")
                .exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime)
                .build().visitor(visitor).execute();
        return toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    private JSONObject put(Integer id, Integer stock) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("stock", stock);
        return jsonObject;
    }

    /**
     * ????????????????????????
     *
     * @param voucherId ??????id
     * @return ??????????????????
     */
    public ExchangePage createExchangeFictitiousGoods(Long voucherId) {
        return createExchangeFictitiousGoods(voucherId, 1L);
    }

    /**
     * ????????????????????????
     *
     * @param voucherId   ??????id
     * @param exchangeNum ???????????????
     * @return ??????????????????
     */
    public ExchangePage createExchangeFictitiousGoods(Long voucherId, Long exchangeNum) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        //??????????????????
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId)
                .exchangePrice("1").isLimit(true).exchangePeopleNum("10").exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime).expireType(2).useDays("10").exchangeNum(String.valueOf(exchangeNum)).build().visitor(visitor).execute();
        return toFirstJavaObject(ExchangePageScene.builder().build(), ExchangePage.class);
    }


    /**
     * ???????????????????????????????????????
     *
     * @param id ????????????id
     * @return ????????????
     */
    public VoucherFormVoucherPageBean getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().visitor(visitor).execute().getString("goods_name");
        return getVoucherPage(voucherName);
    }

    public void modifyExchangeGoodsLimit(Long exchangeGoodsId, String exchangeGoodsType, Boolean isLimit) {
        IScene scene = ExchangeGoodsDetailScene.builder().id(exchangeGoodsId).build();
        ExchangeGoodsDetailBean exchangeGoodsDetail = JSONObject.toJavaObject(scene.visitor(visitor).execute(), ExchangeGoodsDetailBean.class);
        EditExchangeGoodsScene.EditExchangeGoodsSceneBuilder builder = EditExchangeGoodsScene.builder()
                .exchangeGoodsType(exchangeGoodsDetail.getExchangeGoodsType()).goodsId(exchangeGoodsDetail.getGoodsId())
                .exchangePrice(exchangeGoodsDetail.getExchangePrice()).exchangeNum(exchangeGoodsDetail.getExchangeNum())
                .exchangeStartTime(exchangeGoodsDetail.getExchangeStartTime()).exchangeEndTime(exchangeGoodsDetail.getExchangeEndTime())
                .isLimit(isLimit).id(exchangeGoodsDetail.getId());
        builder = isLimit ? builder.exchangePeopleNum(1) : builder;
        builder = exchangeGoodsType.equals(CommodityTypeEnum.REAL.name()) ? builder : builder.expireType(2).useDays(10);
        builder.build().visitor(visitor).execute();
    }

    //-------------------------------------------------------??????---------------------------------------------------

    /**
     * ????????????id
     *
     * @return ??????id??????
     */
    public List<Long> getArticleIdList() {
        JSONArray array = ArticleList.builder().build().visitor(visitor).execute().getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * ???????????????
     *
     * @param urlPath   ????????????
     * @param excelName ????????????
     * @return ?????????
     */
    public IRow[] getRows(String urlPath, String excelName) {
        //???????????????resources/excel
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
        logger.info("keyList is???{}", list);
        return list.stream().map(e -> scene.remove(e).visitor(visitor).getResponse())
                .map(Response::getMessage).collect(Collectors.toList()).toArray(new String[list.size()]);
    }

    /**
     * @description :???????????????????????????
     **/
    public String getVacationSaleId() {
        return visitor.isDaily() ? "uid_f1a745c7" : "uid_250e621b";
    }

    /**
     * @description :?????????????????????????????????????????????????????????????????????????????????????????????
     **/
    public String getBusySaleId() {
        return visitor.isDaily() ? "uid_caf1b799" : "uid_a27173d3";
    }

    /**
     * @description :????????????id
     **/
    public Long getBuyCarId() {
        return visitor.isDaily() ? 335L : 21540L;
    }


    /**
     * ???????????????????????????????????????
     *
     * @param parentRole ?????????
     * @return ??????map
     */
    public Map<Integer, String> getAuthRoleMap(int parentRole) {
        Map<Integer, String> map = new HashMap<>();
        IScene scene = AuthTreeScene.builder().parentRole(parentRole).build();
        scene.visitor(visitor).execute().getJSONArray("children").stream().map(e -> (JSONObject) e)
                .forEach(e -> e.getJSONArray("children").stream().map(a -> (JSONObject) a)
                        .forEach(a -> map.put(a.getInteger("value"), a.getString("label"))));
        return map;
    }

    /**
     * ????????????????????????map
     *
     * @return map
     */
    public Map<Integer, String> getRandomRoleMap() {
        Map<Integer, String> map = new HashMap<>();
        IScene scene = RoleListScene.builder().build();
        List<JSONObject> list = toJavaObjectList(scene, JSONObject.class, "list");
        JSONObject response = list.stream().filter(e -> !e.getString("name").equals("???????????????")).findFirst().orElse(null);
        Preconditions.checkArgument(response != null, "????????????");
        map.put(response.getInteger("id"), response.getString("name"));
        return map;
    }

    /**
     * ??????????????????map
     *
     * @return map
     */
    public JSONArray getShopIdArray() {
        return ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
    }

    /**
     * ??????????????????
     *
     * @param phone ?????????
     */
    public void deleteStaff(String phone) {
        IScene scene = StaffPageScene.builder().phone(phone).build();
        JSONObject response = toFirstJavaObject(scene, JSONObject.class);
        String id = response.getString("id");
        StaffDeleteScene.builder().id(id).build().visitor(visitor).execute();
    }

    /**
     * @return : ????????? ????????????/????????? ?????????JSONObject
     * {"sale_id":"??????id",
     * "sale_status":"????????????",
     * "sale_name":"????????????",
     * "order":?????????????????????,
     * "status":????????? }
     * @params : -
     * @description : ???????????????????????????????????????
     **/
    public JSONObject getLastSale() {
        return AppSaleScheduleDayListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                filter(e -> Objects.equals(e.getString("sale_status"), "?????????")).min((x, y) -> y.getInteger("order") - x.getInteger("order")).get();
    }

    /**
     * @param statusId: ????????????   {0:"?????????",1:"?????????",2:"?????????",3:"?????????"}
     * @return : ????????????????????? JSONObject?????????????????? null
     * @description : ?????????????????????????????????????????????????????????
     **/
    public JSONObject getNeededSale(Integer statusId) {
        return AppSaleScheduleDayListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                filter(e -> e.getInteger("status").equals(statusId)).findAny().orElse(null);
    }

}
