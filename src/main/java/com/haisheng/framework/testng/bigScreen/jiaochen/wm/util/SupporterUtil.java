package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.Response;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppAppointmentPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppFollowUpPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionReceptorList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.appointmentmanage.AppointmentRecordAppointmentPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeGoodsDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage.EvaluatePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.packagemanage.PackageDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherInvalidPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.AppFollowUpPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppAppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionReceptorListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.TimeTableListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluatePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.CustomerImportScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.GroupTotalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPurchaseFixedPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPurchaseTemporaryPackageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.DetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class SupporterUtil {
    public final static Logger logger = LoggerFactory.getLogger(SupporterUtil.class);
    public final static Integer SIZE = 100;
    private final VisitorProxy visitor;

    /**
     * 构造函数
     *
     * @param visitor visitor
     */
    public SupporterUtil(VisitorProxy visitor) {
        this.visitor = visitor;
    }

    /**
     * 获取message信息集合
     *
     * @param scene 接口
     * @return 信息集合
     */
    public String[] getMessageList(@NotNull IScene scene) {
        List<String> list = scene.getKeyList();
        logger.info("---------------------");
        logger.info("keyList is：{}", list);
        logger.info("---------------------");
        return list.stream().map(e -> JSONObject.toJavaObject(scene.remove(e).invoke(visitor, false), Response.class))
                .map(Response::getMessage).collect(Collectors.toList()).toArray(new String[list.size()]);
    }

    /**
     * 获取接口返回值
     *
     * @param scene 接口
     * @return 返回值内容
     */
    public Response getResponse(IScene scene) {
        return JSONObject.toJavaObject(scene.invoke(visitor, false), Response.class);
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param <T>   T
     * @return List<T>
     */
    public <T> List<T> collectBeanList(IScene scene, Class<T> bean) {
        List<T> list = new ArrayList<>();
        int total = visitor.invokeApi(scene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, bean)).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param key   指定的key
     * @param value 指定key的指定value
     * @param <T>   T
     * @return List<T>
     */
    public <T> List<T> collectBeanList(@NotNull IScene scene, Class<T> bean, String key, Object value) {
        List<T> list = new ArrayList<>();
        int size = scene instanceof BuyPackageRecordScene ? SIZE / 10 : SIZE;
        int total = scene.invoke(visitor).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(size);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass()).equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, bean)).orElse(null);
            list.add(clazz);
        }
        return list;
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param <T>   T
     * @return T
     */
    public <T> T collectBean(IScene scene, Class<T> bean) {
        return JSONObject.toJavaObject(scene.invoke(visitor), bean);
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param key   指定的key
     * @param value 指定key的指定value
     * @param <T>   T
     * @return T
     */
    public <T> T collectBean(@NotNull IScene scene, Class<T> bean, String key, Object value) {
        int size = scene instanceof BuyPackageRecordScene ? SIZE / 10 : SIZE;
        int total = scene.invoke(visitor).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(size);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass()).equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, bean)).orElse(null);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
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
     * 获取占用的卡券
     *
     * @return 卡券id
     */
    public Long getOccupyVoucherId() {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.WORKING.name()).build();
        List<VoucherFormVoucherPageBean> voucherPageBeanList = collectBeanList(scene, VoucherFormVoucherPageBean.class);
        VoucherFormVoucherPageBean voucherPageBean = voucherPageBeanList.stream().filter(e -> e.getSurplusInventory() > e.getAllowUseInventory() && e.getAllowUseInventory() != 0).findFirst().orElse(null);
        return voucherPageBean == null ? useVoucher() : voucherPageBean.getVoucherId();
    }

    /**
     * 使用一个卡券
     *
     * @return 卡券id
     */
    private Long useVoucher() {
        String voucherName = createVoucher(2, VoucherTypeEnum.CUSTOM);
        applyVoucher(voucherName, "1");
        Long voucherId = getVoucherId(voucherName);
        JSONArray voucherList = getVoucherArray(voucherId, 1);
        buyTemporaryPackage(voucherList, 1);
        return voucherId;
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
        createVoucherBuilder(stock, type).voucherName(voucherName).build().invoke(visitor);
        return voucherName;
    }

    public CreateScene.CreateSceneBuilder createVoucherBuilder(Integer stock, VoucherTypeEnum type) {
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
                .voucherDescription(getDesc()).shopType(0).shopIds(getShopIdList(2)).selfVerification(selfVerification);
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
        List<VoucherPage> vouchers = collectBeanList(scene, VoucherPage.class);
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
        double ratio = BigDecimal.valueOf(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1])).divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return FileUpload.builder().isPermanent(false).permanentPicType(0).pic(picture).ratioStr(ratioStr).ratio(ratio).build().invoke(visitor).getString("pic_path");
    }

    /**
     * 获取主体类型
     *
     * @return 主体类型
     */
    public String getSubjectType() {
        JSONArray array = SubjectListScene.builder().build().invoke(visitor).getJSONArray("list");
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
        JSONArray array = DetailScene.builder().build().invoke(visitor).getJSONArray("list");
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
        JSONArray array = ShopListScene.builder().build().invoke(visitor).getJSONArray("list");
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
            int total = GroupTotalScene.builder().pushTarget(AppletPushTargetEnum.SHOP_CUSTOMER.getId()).shopList(shopList).build().invoke(visitor).getInteger("total");
            if (total > 1) {
                return shopId;
            }
        }
        return null;
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
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id(Long)
     */
    public Long getVoucherId(String voucherName) {
        return getVoucherPage(voucherName).getVoucherId();
    }

    /**
     * 获取卡券领取记录
     *
     * @param voucherId 卡券id
     * @return 领取记录列表
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return collectBeanList(scene, VoucherSendRecord.class);
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
    public List<VoucherInvalidPageBean> getVoucherInvalidList(Long voucherId) {
        IScene scene = VoucherInvalidPageScene.builder().id(voucherId).build();
        return collectBeanList(scene, VoucherInvalidPageBean.class);
    }

    /**
     * 获取重复的核销人员
     *
     * @return 电话号
     */
    public String getRepetitionVerificationPhone() {
        JSONArray array = VerificationPeopleScene.builder().build().invoke(visitor).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getString("verification_phone")).findFirst().orElse(null);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherName 卡券名称
     * @return 卡券页信息
     */
    public VoucherPage getVoucherPage(String voucherName) {
        IScene scene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
        return collectBean(scene, VoucherPage.class, "voucher_name", voucherName);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherId 卡券id
     * @return 卡券页信息
     */
    public VoucherPage getVoucherPage(Long voucherId) {
        IScene scene = VoucherFormVoucherPageScene.builder().build();
        return collectBean(scene, VoucherPage.class, "voucher_id", voucherId);
    }

    /**
     * 获取卡券详情
     *
     * @param voucherId 卡券id
     * @return 卡券详情
     */
    public VoucherDetailBean getVoucherDetail(Long voucherId) {
        IScene scene = VoucherDetailScene.builder().id(voucherId).build();
        return collectBean(scene, VoucherDetailBean.class);
    }

    /**
     * 获取卡券集合
     *
     * @return 卡券集合
     */
    public JSONArray getVoucherArray() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
        JSONArray plateList = SearchCustomerScene.builder().customerPhone(phone).build().invoke(visitor).getJSONArray("plate_list");
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
        return collectBean(scene, ApplyPageBean.class, "name", voucherName);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPageBean getApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        return collectBean(scene, ApplyPageBean.class, "name", voucherName);
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
        List<ApplyPageBean> voucherApplies = collectBeanList(scene, ApplyPageBean.class);
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
        ApplyPageBean applyPage = collectBean(scene, ApplyPageBean.class, "name", voucherName);
        ApplyApprovalScene.builder().id(applyPage.getId()).status(status).build().invoke(visitor);
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
        return collectBean(packageFormPageScene, PackagePage.class, "audit_status_name", packageStatusEnum.getName());
    }

    /**
     * 获取套餐信息
     *
     * @param packageName 套餐名
     * @return 套餐信息
     */
    public PackagePage getPackagePage(String packageName) {
        IScene scene = PackageFormPageScene.builder().packageName(packageName).build();
        return collectBean(scene, PackagePage.class, "package_name", packageName);
    }

    /**
     * 获取套餐信息
     *
     * @param packageId 套餐id
     * @return 套餐信息
     */
    public PackagePage getPackagePage(Long packageId) {
        IScene scene = PackageFormPageScene.builder().build();
        return collectBean(scene, PackagePage.class, "id", packageId);
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
        JSONObject jsonObject = collectBean(scene, JSONObject.class, "package_name", packageName);
        MakeSureBuyScene.builder().id(jsonObject.getLong("id")).auditStatus("AGREE").build().invoke(visitor);
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
        CancelSoldPackageScene.builder().id(id).id(id).build().invoke(visitor);
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
        List<PackagePage> packagePages = collectBeanList(scene, PackagePage.class);
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
        CreatePackageScene.builder().packageName(packageName).packageDescription(getDesc()).subjectType(getSubjectType())
                .subjectId(getSubjectDesc(getSubjectType())).voucherList(voucherList).packagePrice("49.99").status(true)
                .shopIds(getShopIdList(3)).expireType(2).expiryDate("10").build().invoke(visitor);
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
        JSONArray voucherList = scene.invoke(visitor).getJSONArray("voucher_list");
        return voucherList.stream().map(e -> (JSONObject) e).map(e -> e.getLong("voucher_id")).collect(Collectors.toList());
    }

    public PackageDetailBean getPackageDetail(Long packageId) {
        IScene packageDetailScene = PackageDetailScene.builder().id(packageId).build();
        return collectBean(packageDetailScene, PackageDetailBean.class);
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
        PackageDetailBean packageDetail = collectBean(scene, PackageDetailBean.class);
        Preconditions.checkArgument(packageDetail != null, packageId + " 套餐没找到相关信息");
        EditPackageScene.builder().packageName(packageDetail.getPackageName())
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
                .invoke(visitor);
        return packageDetail.getPackageName();
    }

    /**
     * 编辑套餐
     *
     * @param voucherList 套餐包含的卡券
     * @return 套餐名
     */
    public String editPackage(JSONArray voucherList) {
        IScene packageFormPageScene = PackageFormPageScene.builder().build();
        List<PackagePage> packagePages = collectBeanList(packageFormPageScene, PackagePage.class);
        Long packageId = packagePages.stream().filter(e -> !EnumVP.isContains(e.getPackageName())).map(PackagePage::getPackageId).findFirst().orElse(null);
        String packageName = getPackageName(packageId);
        EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherList(voucherList).packagePrice("1.11").status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).expireType(2).expiryDate(12).build().invoke(visitor);
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
        PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(getPlatNumber(EnumAccount.MARKETING_DAILY.getPhone()))
                .voucherList(voucherList).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(type).build().invoke(visitor);
    }

    /**
     * 购买固定套餐
     *
     * @param packageId 包含的固定套餐
     * @param type      0赠送/1购买
     */
    public void buyFixedPackage(Long packageId, int type) {
        PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("1.00").expiryDate("1")
                .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(type).build().invoke(visitor);
    }

    /**
     * 接待时购买固定套餐
     *
     * @param packageId 包含的固定套餐
     * @param type      0赠送/1购买
     */
    public void receptionBuyFixedPackage(Long packageId, int type) {
        IScene scene = PackageDetailScene.builder().id(packageId).build();
        PackageDetailBean packageDetail = collectBean(scene, PackageDetailBean.class);
        Preconditions.checkArgument(packageDetail != null, "没找到 " + packageId + " 套餐相关信息");
        Integer expiryDate = packageDetail.getExpiryDate();
        Integer expireType = packageDetail.getExpireType();
        String packagePrice = packageDetail.getPackagePrice();
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
        ReceptionPage receptionPage = collectBeanList(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        ReceptionPurchaseFixedPackageScene.builder().customerId(receptionPage.getCustomerId())
                .customerPhone("").carType(PackageUseTypeEnum.RECEPTION_CAR.name()).expireType(expireType).expiryDate(expiryDate)
                .extendedInsuranceCopies("").extendedInsuranceYear("").packageId(packageId).packagePrice(packagePrice)
                .plateNumber(receptionPage.getPlateNumber()).receptionId(receptionPage.getId()).remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                .shopId(receptionPage.getShopId()).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).type(type)
                .build().invoke(visitor);
    }

    /**
     * 接待时购买临时套餐
     *
     * @param voucherList 包含的卡券列表
     * @param type        0赠送/1购买
     */
    public void receptionBuyTemporaryPackage(JSONArray voucherList, int type) {
        IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
        ReceptionPage receptionPage = collectBeanList(receptionPageScene, ReceptionPage.class).get(0);
        //购买套餐
        ReceptionPurchaseTemporaryPackageScene.builder().customerId(receptionPage.getCustomerId())
                .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).customerPhone("").expireType(2).expiryDate("10")
                .extendedInsuranceCopies("").extendedInsuranceYear("").plateNumber(receptionPage.getPlateNumber())
                .receptionId(receptionPage.getId()).remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).shopId(receptionPage.getShopId())
                .subjectId(getSubjectDesc(getSubjectType())).subjectType(getSubjectType()).type(type).voucherList(voucherList).build().invoke(visitor);
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
                .telList(phoneList).messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc()).messageContent(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                .type(type).voucherOrPackageList(voucherOrPackageList).useDays("10");
        String d = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm:ss");
        long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(d));
        builder = immediately ? builder.ifSendImmediately(true) : builder.ifSendImmediately(false).sendTime(sendTime);
        builder.build().invoke(visitor);
    }

    /**
     * 给指定人发消息，如需修改接收人，请在文件中添加手机号
     *
     * @param type               推送优惠类型 0：卡券，1：套餐
     * @param voucherOrPackageId 卡券id
     * @param immediately        是否立即发送
     */
    public void pushCustomMessage(Integer type, boolean immediately, Long... voucherOrPackageId) {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/excel/发消息手机号.xlsx";
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
        builder.build().invoke(visitor);
    }

    //----------------------------------------------------预约记录-------------------------------------------------------

    /**
     * 通过预约id查询接待信息
     *
     * @param appointmentId 预约id
     * @return 预约信息
     */
    public AppointmentRecordAppointmentPageBean getAppointmentPageById(Integer appointmentId) {
        IScene scene = AppointmentPageScene.builder().build();
        return collectBean(scene, AppointmentRecordAppointmentPageBean.class, "id", appointmentId);
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
        return collectBean(receptionPageScene, ReceptionPage.class, "id", receptionId);
    }

    /**
     * 获取第一个接待记录
     *
     * @return 接待信息
     */
    public ReceptionPage getFirstReceptionPage() {
        JSONObject receptionPage = ReceptionPageScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0);
        return JSONObject.toJavaObject(receptionPage, ReceptionPage.class);
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
        int total = scene.invoke(visitor).getInteger("total");
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
            JSONArray array = builder.page(i).size(SIZE).build().invoke(visitor).getJSONArray("list");
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
        SwitchVerificationStatusScene.builder().id(id).status(status).build().invoke(visitor);
    }

    /**
     * 获取评价列表
     *
     * @return 评价列表
     */
    public List<EvaluatePageBean> getEvaluatePageList() {
        IScene scene = EvaluatePageScene.builder().build();
        return collectBeanList(scene, EvaluatePageBean.class);
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
            builder.faultDescription(EnumDesc.DESC_BETWEEN_15_20.getDesc());
        }
        return builder.build().invoke(visitor).getInteger("id");
    }

    /**
     * 获取预约时间id
     */
    public Integer getTimeId(String date) {
        IScene appointmentTimeListScene = AppointmentTimeListScene.builder().type(AppointmentTypeEnum.MAINTAIN.name()).carId(getCarId()).shopId(getShopId()).day(date).build();
        JSONArray array = appointmentTimeListScene.invoke(visitor).getJSONArray("list");
        List<AppletAppointmentTimeList> timeList = array.stream().map(object -> (JSONObject) object).map(object -> JSONObject.toJavaObject(object, AppletAppointmentTimeList.class)).collect(Collectors.toList());
        return timeList.stream().filter(e -> !e.getIsFull()).map(AppletAppointmentTimeList::getId).findFirst().orElse(null);
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public Integer getShopId() {
        return visitor.isDaily() ? 46522 : 20034;
    }

    /**
     * 获取员工id
     *
     * @return 员工id
     */
    public String getStaffId() {
        IScene maintainStaffListScene = AppointmentStaffListScene.builder().shopId(getShopId()).type(AppointmentTypeEnum.MAINTAIN.name()).build();
        JSONArray jsonArray = maintainStaffListScene.invoke(visitor).getJSONArray("list");
        return Objects.requireNonNull(jsonArray.stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getString("uid");
    }

    /**
     * 获取小程序carId
     */
    public Integer getCarId() {
        IScene appletCarListScene = AppletCarListScene.builder().build();
        JSONObject jsonObject = appletCarListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
        Preconditions.checkArgument(jsonObject != null, "小程序我的爱车为空");
        return jsonObject.getInteger("id");
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
            JSONObject response = scene.invoke(visitor);
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
        int id = appletPackageListScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("id");
        IScene appletPackageDetailScene = AppletPackageDetailScene.builder().id((long) id).build();
        JSONArray list = appletPackageDetailScene.invoke(visitor).getJSONArray("list");
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            appletPackageId.addAll(array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList()));
        } while (array.size() == 20);
        appletPackageId.forEach(id -> {
            JSONArray jsonArray = AppletPackageDetailScene.builder().id(id).build().invoke(visitor).getJSONArray("list");
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = scene.invoke(visitor);
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
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().invoke(visitor);
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
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().invoke(visitor);
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
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(integralSort).status(status).build().invoke(visitor);
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
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(SortTypeEnum.DOWN.name()).status(false).build().invoke(visitor);
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
            JSONObject response = appointmentPageScene.invoke(visitor);
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
            JSONObject response = appointmentPageScene.invoke(visitor);
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
        JSONArray list = scene.invoke(visitor).getJSONArray("list");
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
            JSONObject response = appointmentPageScene.invoke(visitor);
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
        JSONArray jsonArray = receptorListScene.invoke(visitor).getJSONArray("list");
        return jsonArray.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    public AppReceptionReceptorList getReceptorList(EnumAccount account) {
        IScene receptorListScene = AppReceptionReceptorListScene.builder().shopId(getShopId()).build();
        JSONArray jsonArray = receptorListScene.invoke(visitor).getJSONArray("list");
        return jsonArray.stream().map(e -> (JSONObject) e).filter(e -> e.getString("name").equals(account.getName())).map(e -> JSONObject.toJavaObject(e, AppReceptionReceptorList.class)).findFirst().orElse(null);
    }

    //-------------------------------------------------------积分中心---------------------------------------------------

    public ExchangePage getExchangePage(Long id) {
        IScene exchangePageScene = ExchangePageScene.builder().build();
        return collectBean(exchangePageScene, ExchangePage.class, "id", id);
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
        JSONArray specificationList = new JSONArray();
        JSONObject response = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().invoke(visitor).getJSONArray("list").getJSONObject(0);
        long goodsId = response.getLong("id");
        JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().invoke(visitor).getJSONArray("specification_detail_list");
        specificationDetailList.forEach(e -> {
            JSONObject specificationDetail = (JSONObject) e;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", specificationDetail.getInteger("id"));
            jsonObject.put("stock", stock);
            specificationList.add(jsonObject);
        });
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                .exchangePrice("1").isLimit(true).exchangePeopleNum("10")
                .specificationList(specificationList).expireType(2).useDays("10")
                .exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime)
                .build().invoke(visitor);
        return collectBeanList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
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
                .exchangeEndTime(exchangeEndTime).expireType(2).useDays("10").exchangeNum(String.valueOf(exchangeNum)).build().invoke(visitor);
        return collectBeanList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }


    /**
     * 获取积分兑换包含的卡券信息
     *
     * @param id 兑换商品id
     * @return 卡券信息
     */
    public VoucherPage getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().invoke(visitor).getString("goods_name");
        return getVoucherPage(voucherName);
    }

    public void modifyExchangeGoodsLimit(Long exchangeGoodsId, String exchangeGoodsType, Boolean isLimit) {
        IScene scene = ExchangeGoodsDetailScene.builder().id(exchangeGoodsId).build();
        ExchangeGoodsDetailBean exchangeGoodsDetail = JSONObject.toJavaObject(scene.invoke(visitor), ExchangeGoodsDetailBean.class);
        EditExchangeGoodsScene.EditExchangeGoodsSceneBuilder builder = EditExchangeGoodsScene.builder()
                .exchangeGoodsType(exchangeGoodsDetail.getExchangeGoodsType()).goodsId(exchangeGoodsDetail.getGoodsId())
                .exchangePrice(exchangeGoodsDetail.getExchangePrice()).exchangeNum(exchangeGoodsDetail.getExchangeNum())
                .exchangeStartTime(exchangeGoodsDetail.getExchangeStartTime()).exchangeEndTime(exchangeGoodsDetail.getExchangeEndTime())
                .isLimit(isLimit).id(exchangeGoodsDetail.getId());
        builder = isLimit ? builder.exchangePeopleNum(1) : builder;
        builder = exchangeGoodsType.equals(CommodityTypeEnum.REAL.name()) ? builder : builder.expireType(2).useDays(10);
        builder.build().invoke(visitor);
    }

    //-------------------------------------------------------活动---------------------------------------------------

    /**
     * 获取文章id
     *
     * @return 文章id集合
     */
    public List<Long> getArticleIdList() {
        JSONArray array = ArticleList.builder().build().invoke(visitor).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }
}
