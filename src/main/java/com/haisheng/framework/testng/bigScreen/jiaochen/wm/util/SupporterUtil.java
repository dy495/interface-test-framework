package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.util.BaseUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppAppointmentPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppFollowUpPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionReceptorList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.RegisterInfoEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.AppFollowUpPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppAppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppReceptionReceptorListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.Integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.FissionVoucherAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageRecruitAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanager.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanager.TimeTableListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.CommoditySpecificationsListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.CreateExchangeGoodsScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangeGoodsStockScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.ExchangePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manager.EvaluatePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.GroupTotalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionPurchaseFixedPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionPurchaseTemporaryPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionVoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.Detail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.Approval;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class SupporterUtil extends BaseUtil {
    private final Visitor visitor;

    /**
     * 构造函数
     *
     * @param visitor visitor
     */
    public SupporterUtil(Visitor visitor) {
        this.visitor = visitor;
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param <T>   T
     * @return bean的集合
     */
    public <T> List<T> collectBean(IScene scene, Class<T> bean) {
        List<T> list = new ArrayList<>();
        int total = visitor.invokeApi(scene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, bean)).collect(Collectors.toList()));
        }
        return list;
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
        visitor.invokeApi(createVoucherBuilder(stock, type).voucherName(voucherName).build());
        return voucherName;
    }

    public CreateVoucherScene.CreateVoucherSceneBuilder createVoucherBuilder(Integer stock, VoucherTypeEnum type) {
        CreateVoucherScene.CreateVoucherSceneBuilder builder = createVoucherBuilder(true).stock(stock).cardType(type.name());
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
    public CreateVoucherScene.CreateVoucherSceneBuilder createVoucherBuilder(Boolean selfVerification) {
        return CreateVoucherScene.builder().subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).cost(0.01)
                .voucherDescription(getDesc()).parValue(getParValue()).shopType(0).shopIds(getShopIdList(2)).selfVerification(selfVerification);
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
        IScene scene = VoucherPageScene.builder().voucherName(voucherName).build();
        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
        if (vouchers.isEmpty()) {
            return voucherName;
        }
        for (VoucherPage voucher : vouchers) {
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
        return EnumDesc.VOUCHER_DESC.getDesc();
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
        double ratio = BigDecimal.valueOf(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1])).divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        IScene scene = FileUpload.builder().isPermanent(false).permanentPicType(0).pic(picture).ratioStr(ratioStr).ratio(ratio).build();
        return visitor.invokeApi(scene).getString("pic_path");
    }

    /**
     * 获取主体类型
     *
     * @return 主体类型
     */
    public String getSubjectType() {
        IScene scene = SubjectList.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return Objects.requireNonNull(array.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getString("subject_key");
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
        IScene scene = Detail.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public List<Long> getShopIdList() {
        IScene scene = ShopListScene.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList());
    }

    /**
     * 获取包含多个客户的门店
     *
     * @return shopId
     */
    public Long getContainMoreCustomerShopId() {
        List<Long> shopIdList = getShopIdList();
        for (Long shopId : shopIdList) {
            List<Long> shopList = new ArrayList<>();
            shopList.add(shopId);
            int total = GroupTotalScene.builder().pushTarget(AppletPushTargetEnum.SHOP_CUSTOMER.getId()).shopList(shopList).build().execute(visitor, true).getInteger("total");
            if (total > 1) {
                return shopId;
            }
        }
        return null;
    }

    /**
     * 获取门店id
     *
     * @param count 数量
     * @return 门店id
     */
    public List<Long> getShopIdList(Integer count) {
        return getShopIdList().subList(0, count);
    }

    /**
     * 获取卡券名称
     *
     * @param voucherId 卡券id
     * @return 卡券名
     */
    public String getVoucherName(long voucherId) {
        IScene scene = VoucherPageScene.builder().build();
        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
        return vouchers.stream().filter(e -> e.getVoucherId().equals(voucherId)).map(VoucherPage::getVoucherName).findFirst().orElse(null);
    }

    /**
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id(Long)
     */
    public Long getVoucherId(String voucherName) {
        IScene scene = VoucherPageScene.builder().build();
        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
        return vouchers.stream().filter(e -> e.getVoucherName().equals(voucherName)).map(VoucherPage::getVoucherId).findFirst().orElse(null);
    }

    /**
     * 获取卡券领取记录
     *
     * @param voucherId 卡券id
     * @return 领取记录列表
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return collectBean(scene, VoucherSendRecord.class);
    }

    /**
     * 获取卡券领取记录
     *
     * @param voucherName 卡券名称
     * @return 领取记录列表
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(String voucherName) {
        Long voucherId = getVoucherId(voucherName);
        return getVoucherSendRecordList(voucherId);
    }

    /**
     * 获取卡券作废记录
     *
     * @param voucherId 卡券id
     * @return 作废记录列表
     */
    public List<VoucherInvalidPage> getVoucherInvalidList(Long voucherId) {
        IScene scene = VoucherInvalidPageScene.builder().id(voucherId).build();
        return collectBean(scene, VoucherInvalidPage.class);
    }

    /**
     * 获取卡券作废记录
     *
     * @param voucherName 卡券名称
     * @return 作废记录列表
     */
    public List<VoucherInvalidPage> getVoucherInvalidList(String voucherName) {
        Long voucherId = getVoucherId(voucherName);
        return getVoucherInvalidList(voucherId);
    }

    /**
     * 获取重复的核销人员
     *
     * @return 电话号
     */
    public String getRepetitionVerificationPhone() {
        IScene scene = VerificationPeopleScene.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getString("verification_phone")).findFirst().orElse(null);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherName 卡券名称
     * @return 卡券页信息
     */
    public VoucherPage getVoucherPage(String voucherName) {
        IScene scene = VoucherPageScene.builder().voucherName(voucherName).build();
        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
        return vouchers.stream().filter(e -> e.getVoucherName().equals(voucherName)).findFirst().orElse(null);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherId 卡券id
     * @return 卡券页信息
     */
    public VoucherPage getVoucherPage(Long voucherId) {
        String voucherName = getVoucherName(voucherId);
        return getVoucherPage(voucherName);
    }

    /**
     * 获取卡券集合
     *
     * @param voucherKind  卡券种类
     * @param voucherCount 卡券数量
     * @return 卡券集合
     */
    public JSONArray getVoucherArray(int voucherKind, Integer... voucherCount) {
        if (voucherCount.length != voucherKind) {
            throw new DataException("卡券种类数量与给定每个卡券数量不一致");
        }
        JSONArray voucherArray = new JSONArray();
        IScene scene = ReceptionVoucherListScene.builder().build();
        List<VoucherList> voucherLists = visitor.invokeApi(scene).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, VoucherList.class)).collect(Collectors.toList());
        List<VoucherList> subVoucherLists = voucherLists.subList(0, voucherKind);
        for (int i = 0; i < subVoucherLists.size(); i++) {
            JSONObject object = new JSONObject();
            object.put("voucher_id", subVoucherLists.get(i).getVoucherId());
            object.put("voucher_name", subVoucherLists.get(i).getVoucherName());
            object.put("voucher_count", voucherCount[i]);
            voucherArray.add(object);
        }
        return voucherArray;
    }

    /**
     * 获取卡券集合
     *
     * @return 卡券集合
     */
    public JSONArray getVoucherArray() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return getVoucherArray(voucherId, 1);
    }

    /**
     * 获取卡券集合
     *
     * @param voucherId    卡券id
     * @param voucherCount 卡券数量
     * @return 卡券集合
     */
    public JSONArray getVoucherArray(Long voucherId, Integer voucherCount) {
        JSONArray array = new JSONArray();
        String voucherName = getVoucherName(voucherId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("voucher_id", voucherId);
        jsonObject.put("voucher_name", voucherName);
        jsonObject.put("voucher_count", voucherCount);
        array.add(jsonObject);
        return array;
    }

    /**
     * 获取卡券集合
     *
     * @param voucherName  卡券名称
     * @param voucherCount 卡券数量
     * @return 卡券集合
     */
    public JSONArray getVoucherArray(String voucherName, Integer voucherCount) {
        Long voucherId = getVoucherId(voucherName);
        return getVoucherArray(voucherId, voucherCount);
    }

    /**
     * 获取卡券集合
     *
     * @param voucherName  卡券名
     * @param voucherCount 卡券数量
     * @return 卡券信息集合
     */
    public JSONArray getVoucherInfoArray(String voucherName, Integer voucherCount) {
        JSONArray voucherList = new JSONArray();
        IScene scene = VoucherPageScene.builder().voucherName(voucherName).build();
        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
        JSONObject jsonObject = Objects.requireNonNull(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).findFirst().orElse(null));
        jsonObject.put("voucher_count", voucherCount);
        voucherList.add(jsonObject);
        return voucherList;
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
        IScene scene = SearchCustomerScene.builder().customerPhone(phone).build();
        JSONArray plateList = visitor.invokeApi(scene).getJSONArray("plate_list");
        return plateList.stream().map(e -> (JSONObject) e).map(e -> e.getString("plate_number")).findFirst().orElse(null);
    }


    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPage getAuditingApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).state(ApplyStatusEnum.AUDITING.getId()).build();
        List<ApplyPage> voucherApplies = collectBean(scene, ApplyPage.class);
        return voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPage getApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        List<ApplyPage> voucherApplies = collectBean(scene, ApplyPage.class);
        return voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPage getApplyPageByTime(String voucherName, String time) {
        logger.info("time is:{}", time);
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        List<ApplyPage> voucherApplies = collectBean(scene, ApplyPage.class);
        return voucherApplies.stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTime().equals(time)).findFirst().orElse(null);
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).state(ApplyStatusEnum.AUDITING.getId()).build();
        List<ApplyPage> voucherApplies = collectBean(scene, ApplyPage.class);
        Long applyId = voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).map(ApplyPage::getId).findFirst().orElse(null);
        visitor.invokeApi(Approval.builder().id(applyId).status(status).build());
    }

    //--------------------------------------------------套餐----------------------------------------------------------

    /**
     * 获取某个状态的套餐
     *
     * @param packageStatusEnum packageStatusEnum
     * @return 套餐信息
     */
    public PackagePage getPackagePage(PackageStatusEnum packageStatusEnum) {
        String format = "yyyy-MM-dd HH:mm";
        String today = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd") + " 00:00";
        long todayUnix = Long.parseLong(DateTimeUtil.dateToStamp(today, format));
        IScene packageFormPageScene = PackageFormPageScene.builder().build();
        List<PackagePage> packagePageList = collectBean(packageFormPageScene, PackagePage.class);
        if (packageStatusEnum.getName().equals(PackageStatusEnum.EXPIRED.getName())) {
            return packagePageList.stream().filter(e -> getValidityUnix(e, format) < todayUnix && e.getAuditStatusName().equals(PackageStatusEnum.AGREE.getName())).findFirst().orElse(null);
        } else {
            return packagePageList.stream().filter(e -> e.getAuditStatusName().equals(packageStatusEnum.getName())).findFirst().orElse(null);
        }
    }

    /**
     * 获取有效时间戳
     *
     * @param packagePage 套餐列表
     * @return 有效时间戳
     */
    private Long getValidityUnix(PackagePage packagePage, String format) {
        return Long.parseLong(DateTimeUtil.dateToStamp(packagePage.getCreateTime(), format)) + (long) packagePage.getValidity() * 24 * 60 * 60 * 1000;
    }

    /**
     * 获取套餐信息
     *
     * @param packageId 套餐id
     * @return 套餐信息
     */
    public PackagePage getPackagePage(Long packageId) {
        String packageName = getPackageName(packageId);
        return getPackagePage(packageName);
    }

    /**
     * 获取套餐信息
     *
     * @param packageName 套餐名
     * @return 套餐信息
     */
    public PackagePage getPackagePage(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        List<PackagePage> packagePages = collectBean(scene, PackagePage.class);
        return packagePages.stream().filter(e -> e.getPackageName().equals(packageName)).findFirst().orElse(null);
    }

    /**
     * 获取套餐id
     *
     * @param packageName 套餐名称
     * @return 套餐id
     */
    public Long getPackageId(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        List<PackagePage> packagePages = collectBean(scene, PackagePage.class);
        return packagePages.stream().filter(e -> e.getPackageName().equals(packageName)).map(PackagePage::getPackageId).findFirst().orElse(null);
    }

    /**
     * 获取套餐名
     *
     * @param packageId 套餐id
     * @return 套餐名
     */
    public String getPackageName(Long packageId) {
        IScene scene = PackageFormPageScene.builder().build();
        List<PackagePage> packagePages = collectBean(scene, PackagePage.class);
        return packagePages.stream().filter(e -> e.getPackageId().equals(packageId)).map(PackagePage::getPackageName).findFirst().orElse(null);
    }

    /**
     * 套餐确认支付
     *
     * @param packageName 套餐名
     */
    public void makeSureBuyPackage(String packageName) {
        //获取确认支付id
        IScene scene = BuyPackageRecordScene.builder().packageName(packageName).size(SIZE).build();
        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
        Long id = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals(packageName)).map(e -> e.getLong("id")).findFirst().orElse(null);
        visitor.invokeApi(MakeSureBuyScene.builder().id(id).auditStatus("AGREE").build());
    }

    /**
     * 取消套餐支付
     *
     * @param packageName 套餐名
     */
    public void cancelSoldPackage(String packageName) {
        IScene scene = BuyPackageRecordScene.builder().packageName(packageName).size(SIZE).build();
        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
        Long id = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals(packageName)).map(e -> e.getLong("id")).findFirst().orElse(null);
        visitor.invokeApi(CancelSoldPackageScene.builder().id(id).id(id).build());
    }

    /**
     * 套餐确认支付
     *
     * @param packageId 套餐id
     */
    public void makeSureBuyPackage(Long packageId) {
        String packageName = getPackageName(packageId);
        makeSureBuyPackage(packageName);
    }

    /**
     * 创建一个套餐名
     *
     * @return 套餐名
     */
    public String createPackageName(UseRangeEnum useRangeEnum) {
        int num = CommonUtil.getRandom(1, 1000000);
        String packageName = useRangeEnum.getName() + "套餐" + num;
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        List<PackagePage> packagePages = collectBean(scene, PackagePage.class);
        if (packagePages.isEmpty()) {
            return packageName;
        }
        for (PackagePage packagePage : packagePages) {
            if (!packagePage.getPackageName().equals(packageName)) {
                return packageName;
            }
        }
        return createPackageName(useRangeEnum);
    }

    /**
     * 创建一个套餐
     *
     * @param voucherList 套餐所含卡券信息
     * @return 套餐名
     */
    public String createPackage(JSONArray voucherList, UseRangeEnum anEnum) {
        String packageName = createPackageName(anEnum);
        IScene scene = CreatePackageScene.builder().packageName(packageName).validity("30").packageDescription(getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).voucherList(voucherList)
                .packagePrice(49.99).status(true).customerUseValidity(1).shopIds(getShopIdList(3)).build();
        visitor.invokeApi(scene);
        return packageName;
    }

    /**
     * 获取套餐包含的卡券
     *
     * @param packageId 套餐id
     * @return 卡券id集合
     */
    public List<Long> getPackageContainVoucher(Long packageId) {
        IScene scene = PackageDetailScene.builder().id(packageId).build();
        JSONArray voucherList = visitor.invokeApi(scene).getJSONArray("voucher_list");
        return voucherList.stream().map(e -> (JSONObject) e).map(e -> e.getLong("voucher_id")).collect(Collectors.toList());
    }

    /**
     * 获取套餐包含的卡券
     *
     * @param packageId 套餐id
     * @param kind      种类
     * @return 卡券id集合
     */
    public List<Long> getPackageContainVoucher(Long packageId, Integer kind) {
        return getPackageContainVoucher(packageId).subList(0, kind);
    }

    public PackageDetail getPackageDetail(Long packageId) {
        IScene packageDetailScene = PackageDetailScene.builder().id(packageId).build();
        return JSONObject.toJavaObject(visitor.invokeApi(packageDetailScene), PackageDetail.class);
    }

    /**
     * 编辑指定套餐包含的卡券
     *
     * @param packageId   套餐 id
     * @param voucherList 所含卡券列表
     * @return 套餐名
     */
    public String editPackageContainVoucher(Long packageId, JSONArray voucherList) {
        PackageDetail packageDetail = getPackageDetail(packageId);
        IScene editPackageScene = EditPackageScene.builder().packageName(packageDetail.getPackageName()).packageDescription(packageDetail.getPackageDescription())
                .validity(packageDetail.getValidity()).subjectType(packageDetail.getSubjectType()).subjectId(packageDetail.getSubjectId())
                .voucherList(voucherList).packagePrice(packageDetail.getPackagePrice()).status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).customerUseValidity(packageDetail.getCustomerUseValidity()).build();
        visitor.invokeApi(editPackageScene);
        return packageDetail.getPackageName();
    }

    /**
     * 编辑指定套餐
     *
     * @param packageId   套餐 id
     * @param voucherList 所含卡券列表
     * @return 套餐名
     */
    public void editPackage(Long packageId, JSONArray voucherList) {
        String packageName = getPackageName(packageId);
        IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.VOUCHER_DESC.getDesc())
                .validity(2000).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherList(voucherList).packagePrice("1.11").status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).build();
        visitor.invokeApi(editPackageScene);
    }

    /**
     * 编辑套餐
     *
     * @param voucherList 套餐包含的卡券
     * @return 套餐名
     */
    public String editPackage(JSONArray voucherList) {
        IScene packageFormPageScene = PackageFormPageScene.builder().build();
        List<PackagePage> packagePages = collectBean(packageFormPageScene, PackagePage.class);
        Long packageId = packagePages.stream().filter(e -> !EnumVP.isContains(e.getPackageName())).map(PackagePage::getPackageId).findFirst().orElse(null);
        String packageName = getPackageName(packageId);
        IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.VOUCHER_DESC.getDesc())
                .validity(2000).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherList(voucherList).packagePrice("1.11").status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).build();
        visitor.invokeApi(editPackageScene);
        return packageName;
    }

    /**
     * 编辑套餐
     *
     * @param voucherId    卡券id
     * @param voucherCount 卡券数量
     * @return 套餐名
     */
    public Long editPackage(Long voucherId, int voucherCount) {
        JSONArray voucherList = getVoucherArray(voucherId, voucherCount);
        String packageName = editPackage(voucherList);
        return getPackageId(packageName);
    }

    /**
     * 购买临时套餐
     *
     * @param voucherList 包含的卡券名称列表
     * @param type        0赠送/1购买
     */
    public void buyTemporaryPackage(JSONArray voucherList, int type) {
        IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(getPlatNumber(EnumAccount.MARKETING_DAILY.getPhone()))
                .voucherList(voucherList).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(type).build();
        visitor.invokeApi(temporaryScene);
    }

    /**
     * 购买临时套餐
     *
     * @param voucherName 包含的卡券名称
     * @param type        0赠送/1购买
     */
    public void buyTemporaryPackage(String voucherName, int type) {
        JSONArray voucherList = getVoucherArray(voucherName, 1);
        IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(getPlatNumber(EnumAccount.MARKETING_DAILY.getPhone()))
                .voucherList(voucherList).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(type).build();
        visitor.invokeApi(temporaryScene);
    }

    /**
     * 购买固定套餐
     *
     * @param packageId 包含的固定套餐
     * @param type      0赠送/1购买
     */
    public void buyFixedPackage(Long packageId, int type) {
        IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(type).build();
        visitor.invokeApi(purchaseFixedPackageScene);
    }

    /**
     * 接待时购买固定套餐
     *
     * @param packageId 包含的固定套餐
     * @param type      0赠送/1购买
     */
    public void receptionBuyFixedPackage(Long packageId, int type) {
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
        ReceptionPage receptionPage = collectBean(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        IScene purchaseScene = ReceptionPurchaseFixedPackageScene.builder().customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name())
                .plateNumber(receptionPage.getPlateNumber()).packageId(packageId).shopId(receptionPage.getShopId())
                .packagePrice("1.11").expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear("").extendedInsuranceCopies("").type(type).receptionId(receptionPage.getId())
                .customerId(receptionPage.getCustomerId()).build();
        visitor.invokeApi(purchaseScene);
    }


    /**
     * 接待时购买临时套餐
     *
     * @param receptionId 接待id
     * @param voucherName 包含卡券名称
     * @param type        0赠送/1购买
     */
    public void receptionBuyTemporaryPackage(Integer receptionId, String voucherName, int type) {
        JSONArray voucherList = getVoucherInfoArray(voucherName, 1);
        ReceptionPage receptionPage = getReceptionPageById(receptionId);
        //购买套餐
        IScene purchaseScene = ReceptionPurchaseTemporaryPackageScene.builder().customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name())
                .plateNumber(receptionPage.getPlateNumber()).voucherList(voucherList)
                .expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(getSubjectType())
                .subjectId(getSubjectDesc(getSubjectType())).extendedInsuranceCopies("").extendedInsuranceYear("")
                .type(type).receptionId(receptionPage.getId()).customerId(receptionPage.getCustomerId()).build();
        visitor.invokeApi(purchaseScene);
    }


    /**
     * 接待时购买临时套餐
     *
     * @param voucherName 包含卡券名称
     * @param type        0赠送/1购买
     */
    public void receptionBuyTemporaryPackage(String voucherName, int type) {
        JSONArray voucherList = getVoucherInfoArray(voucherName, 1);
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
        ReceptionPage receptionPage = collectBean(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        IScene purchaseScene = ReceptionPurchaseTemporaryPackageScene.builder().customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name())
                .plateNumber(receptionPage.getPlateNumber()).voucherList(voucherList)
                .expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(getSubjectType())
                .subjectId(getSubjectDesc(getSubjectType())).extendedInsuranceCopies("").extendedInsuranceYear("")
                .type(type).receptionId(receptionPage.getId()).customerId(receptionPage.getCustomerId()).build();
        visitor.invokeApi(purchaseScene);
    }

    /**
     * 接待时购买临时套餐
     *
     * @param voucherList 包含的卡券列表
     * @param type        0赠送/1购买
     */
    public void receptionBuyTemporaryPackage(JSONArray voucherList, int type) {
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
        ReceptionPage receptionPage = collectBean(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        IScene purchaseScene = ReceptionPurchaseTemporaryPackageScene.builder().customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name())
                .plateNumber(receptionPage.getPlateNumber()).voucherList(voucherList).shopId(receptionPage.getShopId())
                .expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(getSubjectType())
                .subjectId(getSubjectDesc(getSubjectType())).extendedInsuranceCopies("").extendedInsuranceYear("")
                .type(type).receptionId(receptionPage.getId()).customerId(receptionPage.getCustomerId()).build();
        visitor.invokeApi(purchaseScene);
    }


    //-------------------------------------------------消息----------------------------------------------------------

    /**
     * 消息推送
     *
     * @param type               推送优惠类型 0：卡券，1：套餐
     * @param voucherOrPackageId 卡券id
     * @param immediately        是否立即发送
     * @return 发出去的卡券id
     */
    public void pushMessage(Integer type, boolean immediately, Long... voucherOrPackageId) {
        List<Long> voucherOrPackageList = new ArrayList<>(Arrays.asList(voucherOrPackageId));
        List<String> phoneList = new ArrayList<>();
        phoneList.add(EnumAccount.MARKETING_DAILY.getPhone());
        PushMessageScene.PushMessageSceneBuilder builder = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                .telList(phoneList).messageName(EnumDesc.MESSAGE_TITLE.getDesc()).messageContent(EnumDesc.MESSAGE_DESC.getDesc())
                .type(type).voucherOrPackageList(voucherOrPackageList).useDays("10");
        String d = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm:ss");
        long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(d));
        builder = immediately ? builder.ifSendImmediately(true) : builder.ifSendImmediately(false).sendTime(sendTime);
        visitor.invokeApi(builder.build());
    }

    /**
     * 校验消息详情
     */
    public void checkMessageDetail(String messageType, String messageTypeName, String sendTime, String content, String phone, String customerName, String customerTypeName) {
        IScene pushMessageScene = PushMessageScene.builder().build();
        PushMsgRecord pushMsgRecord = collectBean(pushMessageScene, PushMsgRecord.class).get(0);
        CommonUtil.checkResult("推送原因", messageType, pushMsgRecord.getMessageType());
        CommonUtil.checkResult("推送原因名称", messageTypeName, pushMsgRecord.getCustomerTypeName());
        CommonUtil.checkResult("推送时间", sendTime, pushMsgRecord.getSendTime());
        CommonUtil.checkResult("消息内容", content, pushMsgRecord.getContent());
        CommonUtil.checkResult("联系电话", phone, pushMsgRecord.getPhone());
        CommonUtil.checkResult("接收人", customerName, pushMsgRecord.getCustomerName());
        CommonUtil.checkResult("客户类型", customerTypeName, pushMsgRecord.getCustomerTypeName());
    }

    //----------------------------------------------------预约记录-------------------------------------------------------

    /**
     * 通过预约id查询接待信息
     *
     * @param appointmentId 预约id
     * @return 预约信息
     */
    public AppointmentPage getAppointmentPageById(Integer appointmentId) {
        IScene appointmentPageScene = AppointmentPageScene.builder().build();
        List<AppointmentPage> appointmentPageList = collectBean(appointmentPageScene, AppointmentPage.class);
        return appointmentPageList.stream().filter(e -> e.getId().equals(appointmentId)).findFirst().orElse(null);
    }

    //----------------------------------------------------接待记录-------------------------------------------------------

    /**
     * 通过接待id查询接待信息
     *
     * @param receptionId 接待id
     * @return 接待信息
     */
    public ReceptionPage getReceptionPageById(Integer receptionId) {
        IScene receptionPageScene = ReceptionPageScene.builder().build();
        List<ReceptionPage> receptionPageList = collectBean(receptionPageScene, ReceptionPage.class);
        return receptionPageList.stream().filter(e -> e.getId().equals(receptionId)).findFirst().orElse(null);
    }

    /**
     * 获取第一个接待记录
     *
     * @return 接待信息
     */
    public ReceptionPage getFirstReceptionPage() {
        IScene receptionPageScene = ReceptionPageScene.builder().build();
        List<ReceptionPage> receptionPageList = collectBean(receptionPageScene, ReceptionPage.class);
        return receptionPageList.stream().findFirst().orElse(null);
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
        int total = visitor.invokeApi(scene).getInteger("total");
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
            JSONArray array = visitor.invokeApi(builder.page(i).size(SIZE).build()).getJSONArray("list");
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
        IScene switchVerificationStatusScene = SwitchVerificationStatusScene.builder().id(id).status(status).build();
        visitor.invokeApi(switchVerificationStatusScene);
    }

    /**
     * 获取评价列表
     *
     * @return 评价列表
     */
    public List<EvaluatePage> getEvaluatePageList() {
        IScene scene = EvaluatePageScene.builder().build();
        return collectBean(scene, EvaluatePage.class);
    }

    //-------------------------------------------------小程序----------------------------------------------------------

    /**
     * 小程序预约
     * 需要给个固定门店的顾问，需要shopId固定、staffId固定
     *
     * @param type 预约类型 MAINTAIN：保养，REPAIR：维修
     * @return id 预约id
     */
    public Integer appointment(AppointmentTypeEnum type, String date) {
        AppointmentSubmitScene.AppointmentSubmitSceneBuilder builder = AppointmentSubmitScene.builder().type(type.name()).carId(getCarId())
                .shopId(getShopId()).staffId(getStaffId()).timeId(getTimeId(date)).appointmentName("隔壁小王").appointmentPhone("15321527989");
        if (type.name().equals(AppointmentTypeEnum.REPAIR.name())) {
            builder.faultDescription(EnumDesc.FAULT_DESCRIPTION.getDesc());
        }
        return visitor.invokeApi(builder.build()).getInteger("id");
    }

    /**
     * 获取预约时间id
     */
    public Integer getTimeId(String date) {
        IScene appointmentTimeListScene = AppointmentTimeListScene.builder().type(AppointmentTypeEnum.MAINTAIN.name()).carId(getCarId()).shopId(getShopId()).day(date).build();
        JSONArray array = visitor.invokeApi(appointmentTimeListScene).getJSONArray("list");
        List<AppletAppointmentTimeList> timeList = array.stream().map(object -> (JSONObject) object).map(object -> JSONObject.toJavaObject(object, AppletAppointmentTimeList.class)).collect(Collectors.toList());
        return timeList.stream().filter(e -> !e.getIsFull()).map(AppletAppointmentTimeList::getId).findFirst().orElse(null);
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public Integer getShopId() {
        return visitor.isOnline() ? 20034 : 50401;
    }

    /**
     * 获取员工id
     *
     * @return 员工id
     */
    public String getStaffId() {
        IScene maintainStaffListScene = AppointmentStaffListScene.builder().shopId(getShopId()).type(AppointmentTypeEnum.MAINTAIN.name()).build();
        JSONArray jsonArray = visitor.invokeApi(maintainStaffListScene).getJSONArray("list");
        return Objects.requireNonNull(jsonArray.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getString("uid");
    }

    /**
     * 获取小程序carId
     */
    public Integer getCarId() {
        IScene appletCarListScene = AppletCarListScene.builder().build();
        JSONArray jsonArray = visitor.invokeApi(appletCarListScene).getJSONArray("list");
        return Objects.requireNonNull(jsonArray.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getInteger("id");
    }

    /**
     * 获取小程序卡券编号
     *
     * @return 卡券编号
     */
    public long getAppletVoucherId(String voucherName) {
        List<Long> voucherList = new ArrayList<>();
        Integer id = null;
        Integer status = null;
        JSONArray list;
        do {
            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
            JSONObject response = visitor.invokeApi(scene);
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("title") != null && e.getString("title").equals(voucherName) && !e.getString("status_name").equals(VoucherUseStatusEnum.EXPIRED.getName()) && !e.getString("status_name").equals(VoucherUseStatusEnum.IS_USED.getName())).map(e -> e.getLong("id")).collect(Collectors.toList()));
            logger.info("id:{},status:{}", id, status);
        } while (list.size() == 20);
        return voucherList.get(0);
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
            JSONObject response = visitor.invokeApi(scene);
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
        int id = visitor.invokeApi(appletPackageListScene).getJSONArray("list").getJSONObject(0).getInteger("id");
        IScene appletPackageDetailScene = AppletPackageDetailScene.builder().id((long) id).build();
        JSONArray list = visitor.invokeApi(appletPackageDetailScene).getJSONArray("list");
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            appletPackageId.addAll(array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList()));
        } while (array.size() == 20);
        appletPackageId.forEach(id -> {
            JSONArray jsonArray = visitor.invokeApi(AppletPackageDetailScene.builder().id(id).build()).getJSONArray("list");
            appletVoucherInfoList.addAll(jsonArray.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletVoucherInfo.class)).collect(Collectors.toList()));
        });
        return appletVoucherInfoList;
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = visitor.invokeApi(scene);
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
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().execute(visitor, true);
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
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().execute(visitor, true);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            appletExchangeRecordList.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletExchangeRecord.class)).collect(Collectors.toList()));
        } while (array.size() == 20);
        return appletExchangeRecordList;
    }

//
//    /**
//     * 小程序我的报名列表
//     *
//     * @return 报名数量
//     */
//    public int getAppletArticleNum() {
//        Long lastValue = null;
//        int listSize = 0;
//        JSONArray array;
//        do {
//            IScene scene = AppointmentActivityListScene.builder().lastValue(lastValue).size(20).build();
//            JSONObject response = visitor.invokeApi(scene);
//            lastValue = response.getLong("last_value");
//            array = response.getJSONArray("list");
//            listSize += array.size();
//        } while (array.size() == 20);
//        return listSize;
//    }
//
//    /**
//     * 获取小程序报名活动集合
//     *
//     * @return 报名活动集合
//     */
//    public List<AppointmentActivity> getAppletArticleList() {
//        List<AppointmentActivity> list = new ArrayList<>();
//        Long lastValue = null;
//        JSONArray array;
//        do {
//            IScene scene = AppointmentActivityListScene.builder().lastValue(lastValue).size(20).build();
//            JSONObject response = visitor.invokeApi(scene);
//            lastValue = response.getLong("last_value");
//            array = response.getJSONArray("list");
//            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppointmentActivity.class)).collect(Collectors.toList()));
//        } while (array.size() == 20);
//        return list;
//    }
//
//    /**
//     * 获取能报名的文章id
//     *
//     * @param count 活动数量
//     */
//    public List<Long> getCanApplyArticleList(Integer count) {
//        List<Long> activityIds = new ArrayList<>();
//        String startDate = "2020-12-01";
//        String endDate = DateTimeUtil.addDayFormat(new Date(), 365);
//        ArticlePage.ArticlePageBuilder builder = ArticlePage.builder().registerStartDate(startDate).registerEndDate(endDate);
//        int total = jc.invokeApi(builder.build()).getInteger("total");
//        int s = CommonUtil.getTurningPage(total, size);
//        for (int i = 1; i < s; i++) {
//            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//            activityIds.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("register_end_date").compareTo(DateTimeUtil.getFormat(new Date())) > 0
//                    && e.getInteger("total_quota") > 100).map(e -> e.getLong("id")).collect(Collectors.toList()));
//        }
//        return count == null ? activityIds : activityIds.subList(0, count);
//    }
//

//    /**
//     * 获取活动名称
//     *
//     * @param articleId 活动id
//     * @return 活动名称
//     */
//    public String getArticleName(Long articleId) {
//        IScene scene = ArticlePage.builder().build();
//        List<OperationArticle> operationArticles = collectBean(scene, OperationArticle.class);
//        return operationArticles.stream().filter(e -> e.getId().equals(articleId)).map(OperationArticle::getTitle).findFirst().orElse(null);
//    }

//    /**
//     * 获取含有待审批的申请
//     * 如果没有时需要提供token报名一个活动
//     * 使用此方法后需要重新登录pc
//     *
//     * @param token 小程序token
//     * @return 活动id
//     */
//    public OperationRegister getContainApplyRegisterInfo(EnumAppletToken token) {
//        Long articleId;
//        List<OperationRegister> operationRegisters = getRegisterList();
//        OperationRegister operationRegister = operationRegisters.stream().filter(e -> getApprovalList(e.getId()).stream().anyMatch(approval -> approval.getStatusName().equals("待审批"))).findFirst().orElse(null);
//        if (operationRegister == null) {
//            articleId = getCanApplyArticleList(1).get(0);
//            new LoginUtil().loginApplet(token);
//            applyArticle(articleId);
//            new LoginUtil().login(EnumAccount.ADMINISTRATOR);
//        } else {
//            articleId = operationRegister.getId();
//        }
//        return getRegisterInfo(articleId);
//    }

//    /**
//     * 获取报名详情
//     *
//     * @param articleId 活动id
//     * @return 报名详情
//     */
//    public OperationRegister getRegisterInfo(Long articleId) {
//        return getRegisterList().stream().filter(e -> e.getId().equals(articleId)).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegister.class)).findFirst().orElse(null);
//    }
//
//    /**
//     * 获取报名列表
//     *
//     * @return 报名列表
//     */
//    public List<OperationRegister> getRegisterList() {
//        IScene scene = RegisterPage.builder().build();
//        return collectBean(scene, OperationRegister.class);
//    }
//
//    /**
//     * 获取待审批列表
//     *
//     * @param articleId 文章id
//     * @return 审批详情
//     */
//    public List<OperationApproval> getApprovalList(Long articleId) {
//        IScene scene = ApprovalPage.builder().articleId(String.valueOf(articleId)).build();
//        return collectBean(scene, OperationApproval.class);
//    }
//
//    /**
//     * 获取文章id
//     *
//     * @return 文章id集合
//     */
//    public List<Long> getArticleIdList() {
//        IScene scene = ArticleList.builder().build();
//        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
//        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
//    }
//

    /**
     * 活动报名
     *
     * @param activityId 活动id
     */
    public void activityRegister(Long activityId) {
        IScene scene = AppletActivityRegisterScene.builder().id(activityId).name(EnumAccount.MARKETING_DAILY.name()).phone(EnumAccount.MARKETING_DAILY.getPhone()).num(1).build();
        visitor.invokeApi(scene, false);
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
            JSONObject response = visitor.invokeApi(appointmentPageScene);
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
            JSONObject response = visitor.invokeApi(appointmentPageScene);
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
    public Integer appointmentNumber(Date date) {
        String nowDate = DateTimeUtil.getFormat(new Date(), "yyyy-MM");
        IScene scene = TimeTableListScene.builder().appointmentMonth(nowDate).build();
        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
        return list.stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("day").equals(DateTimeUtil.getDayOnMonth(date)))
                .map(e -> e.getInteger("appointment_number") == null ? 0 : e.getInteger("appointment_number")).findFirst().orElse(0);
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
            JSONObject response = visitor.invokeApi(appointmentPageScene);
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            receptionPageList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionPage.class)).collect(Collectors.toList()));
        } while (list.size() == 10);
        return receptionPageList;
    }

    /**
     * 获取预约页数量
     *
     * @return 列表数
     */
    public Integer getReceptionPageNum() {
        return getReceptionPageList().size();
    }

    /**
     * 获取售后员工列表
     *
     * @return 售后员工
     */
    public AppReceptionReceptorList getReceptorList() {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        JSONArray jsonArray = visitor.invokeApi(receptorListScene).getJSONArray("list");
        return jsonArray.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    public AppReceptionReceptorList getReceptorList(EnumAccount account) {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        JSONArray jsonArray = visitor.invokeApi(receptorListScene).getJSONArray("list");
        return jsonArray.stream().map(e -> (JSONObject) e).filter(e -> e.getString("name").equals(account.getName())).map(e -> JSONObject.toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }


    //-------------------------------------------------------积分中心---------------------------------------------------

    public ExchangePage getExchangePage(Long id) {
        IScene exchangePageScene = ExchangePageScene.builder().build();
        return collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * 创建实物积分兑换
     *
     * @return 积分兑换id
     */
    public ExchangePage CreateExchangeRealGoods() {
        return CreateExchangeRealGoods(1);
    }

    /**
     * 创建实物积分兑换
     *
     * @return 积分兑换商品
     */
    public ExchangePage CreateExchangeRealGoods(int stock) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        JSONArray specificationList = new JSONArray();
        JSONObject response = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
        long goodsId = response.getLong("id");
        JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().execute(visitor, true).getJSONArray("specification_detail_list");
        specificationDetailList.forEach(e -> {
            JSONObject specificationDetail = (JSONObject) e;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", specificationDetail.getInteger("id"));
            jsonObject.put("stock", stock);
            specificationList.add(jsonObject);
        });
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                .isLimit(true).exchangePeopleNum("10").specificationList(specificationList).exchangeStartTime(exchangeStartTime)
                .exchangePrice("1").exchangeEndTime(exchangeEndTime).build().execute(visitor, true);
        return collectBean(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * 创建虚拟兑换商品
     *
     * @param voucherId 卡券id
     * @return 积分兑换商品
     */
    public ExchangePage CreateExchangeFictitiousGoods(Long voucherId) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId)
                .exchangeNum("1").isLimit(true).exchangePeopleNum("10").exchangeStartTime(exchangeStartTime)
                .exchangePrice("1").exchangeEndTime(exchangeEndTime).build().execute(visitor, true);
        return collectBean(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * 获取积分兑换包含的卡券信息
     *
     * @param id 兑换商品id
     * @return 卡券信息
     */
    public VoucherPage getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(id).build().execute(visitor, true).getString("goods_name");
        return getVoucherPage(voucherName);
    }

    //-------------------------------------------------------活动---------------------------------------------------

    /**
     * 获取文章id
     *
     * @return 文章id集合
     */
    public List<Long> getArticleIdList() {
        JSONArray array = visitor.invokeApi(ArticleList.builder().build()).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * 活动审批
     *
     * @param status     201通过/301不通过
     * @param activityId 活动id
     */
    public void approvalActivity(Integer status, Long... activityId) {
        List<Long> ids = new ArrayList<>(Arrays.asList(activityId));
        IScene manageApprovalScene = ManageApprovalScene.builder().ids(ids).status(status).build();
        visitor.invokeApi(manageApprovalScene);
    }

    /**
     * 创建裂变活动-分享者奖励
     **/
    public JSONObject getShareVoucher(Long id, int type, int num, Integer expireType, String voucherStart, String
            voucherEnd, Integer voucherEffectiveDays) {
        JSONObject shareVoucher = new JSONObject();
        shareVoucher.put("id", id);
        shareVoucher.put("type", type);
        shareVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else {
            voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        }
        shareVoucher.put("voucher_valid", voucherValid);
        return shareVoucher;
    }

    /**
     * 创建裂变活动-被邀请者奖励
     **/
    public JSONObject getInvitedVoucher(Long id, int type, int num, Integer expireType, String voucherStart, String
            voucherEnd, Integer voucherEffectiveDays) {
        JSONObject invitedVoucher = new JSONObject();
        invitedVoucher.put("id", id);
        invitedVoucher.put("type", type);
        invitedVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else {
            voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        }
        invitedVoucher.put("voucher_valid", voucherValid);
        return invitedVoucher;
    }

    /**
     * 创建招募活动-报名所需信息
     **/
    public JSONArray getRegisterInformationList(List<Boolean> isShow, List<Boolean> isRequired) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < RegisterInfoEnum.values().length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", RegisterInfoEnum.values()[i].getId());
            jsonObject.put("name", RegisterInfoEnum.values()[i].getName());
            jsonObject.put("is_show", isShow.get(i));
            jsonObject.put("is_required", isRequired.get(i));
            array.add(jsonObject);
        }
        return array;
    }

    /**
     * 创建招募活动-卡券奖励
     **/
    public JSONArray getRewardVouchers(Long id, int type, int num) {
        JSONArray rewardVouchers = new JSONArray();
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("id", id);
        voucherValid.put("type", type);
        voucherValid.put("num", num);
        rewardVouchers.add(voucherValid);
        return rewardVouchers;
    }

    /**
     * 创建招募活动-奖励有效期
     *
     * @param expireType           卡券有效期类型 1：时间段，2：有效天数
     * @param voucherStart         卡券有效开始日期 卡券有效期类型为1（时间段）必填
     * @param voucherEnd           卡券有效结束日期 卡券有效期类型为1（时间段）必填
     * @param voucherEffectiveDays 卡券有效天数 卡券有效期类型为2（有效天数）必填
     **/
    public JSONObject getVoucherValid(int expireType, String voucherStart, String voucherEnd,
                                      int voucherEffectiveDays) {
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else {
            voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        }
        return voucherValid;
    }


    /**
     * 获取开始时间
     */
    public String getStartDate() {
        return DateTimeUtil.getFormat(new Date());
    }

    /**
     * 获取结束时间
     */
    public String getEndDate() {
        return DateTimeUtil.addDayFormat(new Date(), 10);
    }

    /**
     * 活动管理-创建裂变活动
     */
    public void createFissionActivity() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        visitor.invokeApi(createFissionActivityScene(voucherId));
    }

    /**
     * 构建裂变活动
     *
     * @param voucherId 包含卡券信息
     * @return IScene
     */
    public IScene createFissionActivityScene(Long voucherId) {
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(supporterUtil.getPicPath());
        //获取优惠券ID
        // 创建被邀请者和分享者的信息字段
        JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, getVoucherSurplusInventory(voucherId), 2, "", "", 3);
        JSONObject shareVoucher = getShareVoucher(voucherId, 2, getVoucherSurplusInventory(voucherId), 2, "", "", 3);
        return FissionVoucherAddScene.builder()
                .type(0)
                .participationLimitType(0)
                .receiveLimitType(0)
                .title(ActivityTypeEnum.FISSION_VOUCHER.getName())
                .rule(EnumDesc.ACTIVITY_DESC.getDesc())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("RED_PAPER")
                .picList(picList)
                .shareNum("3")
                .shareVoucher(shareVoucher)
                .invitedVoucher(invitedVoucher)
                .build();
    }

    /**
     * 创建招募活动
     *
     * @return 活动id
     */
    public Long createRecruitActivity() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId, true, 0, true);
    }

    /**
     * 活动管理-创建招募活动
     *
     * @param voucherId         奖励卡券信息
     * @param award             是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public Long createRecruitActivity(Long voucherId, boolean award, int rewardReceiveType, boolean isNeedApproval) {
        IScene scene = createRecruitActivityScene(voucherId, award, rewardReceiveType, isNeedApproval);
        return visitor.invokeApi(scene).getLong("id");
    }

    /**
     * 活动管理-创建招募活动
     *
     * @param voucherId         奖励卡券信息
     * @param award             是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean award, int rewardReceiveType,
                                             boolean isNeedApproval) {
        List<String> picList = new ArrayList<>();
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        picList.add(supporterUtil.getPicPath());
        //填写报名所需要信息
        List<Boolean> isShow = new ArrayList<>();
        isShow.add(true);
        isShow.add(true);
        isShow.add(true);
        isShow.add(true);
        List<Boolean> isRequired = new ArrayList<>();
        isRequired.add(true);
        isRequired.add(true);
        isRequired.add(true);
        isRequired.add(true);
        JSONArray registerInformationList = getRegisterInformationList(isShow, isRequired);
        //报名成功奖励

        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherSurplusInventory(voucherId));
        //卡券有效期
        JSONObject voucherValid = getVoucherValid(1, "", "", 10);
        //创建招募活动-共有的--基础信息
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                .type(1)
                .participationLimitType(0)
                .receiveLimitType(0)
                .title(ActivityTypeEnum.FISSION_VOUCHER.getName())
                .rule(EnumDesc.ACTIVITY_DESC.getDesc())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("RED_PAPER")
                .picList(picList)
                .applyStart(getStartDate())
                .applyEnd(getEndDate())
                .isLimitQuota(true)
                .quota(2)
                .address(EnumDesc.MESSAGE_TITLE.getDesc())
                .registerInformationList(registerInformationList)
                .successReward(true)
                .rewardReceiveType(rewardReceiveType)
                .isNeedApproval(isNeedApproval);
        if (award) {
            builder.rewardVouchers(registerObject)
                    .voucherValid(voucherValid);
        }
        return builder.build();
    }

    private int getVoucherSurplusInventory(Long voucherId) {
        Long surplusInventory = getVoucherPage(voucherId).getSurplusInventory();
        return (int) (surplusInventory == 1 ? surplusInventory : surplusInventory - 1);

    }
}
