package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeGoodsDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.CategoryPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherInvalidPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralCategoryTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall.CategoryPageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall.CreateCategoryScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.messagemanager.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage.*;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class SupporterUtil {
    public static final Logger logger = LoggerFactory.getLogger(SupporterUtil.class);
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
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param key   指定的key
     * @param value 指定key的指定value
     * @param <T>   T
     * @return bean
     */
    public <T> T collectBeanByField(@NotNull IScene scene, Class<T> bean, String key, Object value) {
        int total = scene.invoke(visitor, true).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.invoke(visitor, true).getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass()).equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, bean)).orElse(null);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
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
        return FileUpload.builder().isPermanent(false).permanentPicType(0).pic(picture).ratioStr(ratioStr).ratio(ratio).build().invoke(visitor, true).getString("pic_path");
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
        createVoucherBuilder(stock, type).voucherName(voucherName).build().invoke(visitor, true);
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
        return CreateScene.builder().voucherDescription(getDesc()).parValue(getParValue()).shopType(0).selfVerification(selfVerification);
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
        JSONArray array = ShopListScene.builder().build().invoke(visitor, true).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList());
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
    public List<VoucherInvalidPageBean> getVoucherInvalidList(Long voucherId) {
        IScene scene = VoucherInvalidPageScene.builder().id(voucherId).build();
        return collectBean(scene, VoucherInvalidPageBean.class);
    }

    /**
     * 获取重复的核销人员
     *
     * @return 电话号
     */
    public String getRepetitionVerificationPhone() {
        JSONArray array = VerificationPeopleScene.builder().build().invoke(visitor, true).getJSONArray("list");
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
        return collectBeanByField(scene, VoucherPage.class, "voucher_name", voucherName);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherId 卡券id
     * @return 卡券页信息
     */
    public VoucherPage getVoucherPage(Long voucherId) {
        IScene scene = VoucherFormVoucherPageScene.builder().build();
        return collectBeanByField(scene, VoucherPage.class, "voucher_id", voucherId);
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
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPage getAuditingApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.getId()).build();
        return collectBeanByField(scene, ApplyPage.class, "name", voucherName);
    }

    /**
     * 获取优惠券申请信息
     *
     * @param voucherName 卡券名称
     * @return 卡券申请信息
     */
    public ApplyPage getApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).build();
        return collectBeanByField(scene, ApplyPage.class, "name", voucherName);
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
        ApplyPage applyPage = collectBeanByField(scene, ApplyPage.class, "name", voucherName);
        ApplyApprovalScene.builder().id(applyPage.getId()).status(status).build().invoke(visitor, true);
    }

    //----------------------------------------------------核销人员-------------------------------------------------------

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
        visitor.invokeApi(builder.build());
    }

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
        SwitchVerificationStatusScene.builder().id(id).status(status).build().invoke(visitor, true);
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
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().invoke(visitor, true);
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
            JSONObject response = AppletExchangeRecordScene.builder().lastValue(lastValue).size(20).status(null).build().invoke(visitor, true);
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
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(integralSort).status(status).build().invoke(visitor, true);
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
            JSONObject data = AppletCommodityListScene.builder().lastValue(lastValue).size(10).integralSort(SortTypeEnum.DOWN.name()).status(false).build().invoke(visitor, true);
            lastValue = data.getJSONObject("last_value");
            array = data.getJSONArray("list");
            listSie += array.size();
        } while (array.size() == 10);
        return listSie;

    }

    //-------------------------------------------------------积分中心---------------------------------------------------

    public ExchangePage getExchangePage(Long id) {
        IScene exchangePageScene = ExchangePageScene.builder().build();
        return collectBeanByField(exchangePageScene, ExchangePage.class, "id", id);
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
        JSONObject response = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().invoke(visitor, true).getJSONArray("list").getJSONObject(0);
        long goodsId = response.getLong("id");
        JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().invoke(visitor, true).getJSONArray("specification_detail_list");
        specificationDetailList.forEach(e -> {
            JSONObject specificationDetail = (JSONObject) e;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", specificationDetail.getInteger("id"));
            jsonObject.put("stock", stock);
            specificationList.add(jsonObject);
        });
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                .exchangePrice(1L).isLimit(true).exchangePeopleNum(10)
                .specificationList(specificationList).expireType(2).useDays(10)
                .exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime)
                .build().invoke(visitor, true);
        return collectBean(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * 创建虚拟兑换商品
     *
     * @param voucherId 卡券id
     * @return 积分兑换商品
     */
    public ExchangePage createExchangeFictitiousGoods(Long voucherId) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        //创建积分兑换
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId)
                .exchangePrice(1L).isLimit(true).exchangePeopleNum(10).exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime).expireType(2).useDays(10).exchangeNum(1L).build().invoke(visitor, true);
        return collectBean(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * 获取积分兑换包含的卡券信息
     *
     * @param id 兑换商品id
     * @return 卡券信息
     */
    public VoucherPage getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor, true).getString("goods_name");
        return getVoucherPage(voucherName);
    }

    public void modifyExchangeGoodsLimit(Long exchangeGoodsId, String exchangeGoodsType, Boolean isLimit) {
        IScene scene = ExchangeGoodsDetailScene.builder().id(exchangeGoodsId).build();
        ExchangeGoodsDetailBean exchangeGoodsDetail = JSONObject.toJavaObject(scene.invoke(visitor, true), ExchangeGoodsDetailBean.class);
        EditExchangeGoodsScene.EditExchangeGoodsSceneBuilder builder = EditExchangeGoodsScene.builder()
                .exchangeGoodsType(exchangeGoodsDetail.getExchangeGoodsType()).goodsId(exchangeGoodsDetail.getGoodsId())
                .exchangePrice(exchangeGoodsDetail.getExchangePrice()).exchangeNum(exchangeGoodsDetail.getExchangeNum())
                .exchangeStartTime(exchangeGoodsDetail.getExchangeStartTime()).exchangeEndTime(exchangeGoodsDetail.getExchangeEndTime())
                .isLimit(isLimit).id(exchangeGoodsDetail.getId());
        builder = isLimit ? builder.exchangePeopleNum(1) : builder;
        builder = exchangeGoodsType.equals(CommodityTypeEnum.REAL.name()) ? builder : builder.expireType(2).useDays(10);
        builder.build().invoke(visitor, true);
    }

    //-------------------------------------------------------商品管理---------------------------------------------------

    /**
     * 创建品类
     *
     * @param categoryName   品类名称
     * @param picPath        图片地址
     * @param categoryLevel  品类等级
     * @param belongCategory 品类所属
     */
    public Long createCategory(String categoryName, String picPath, String categoryLevel, Long belongCategory) {
        CreateCategoryScene.builder().categoryName(categoryName).belongPic(picPath).categoryLevel(categoryLevel)
                .belongCategory(belongCategory).build().invoke(visitor, true);
        return getCategoryId(categoryName);
    }

    /**
     * 创建品类
     *
     * @param categoryLevel 品类等级
     */
    public Long getCategoryByLevel(IntegralCategoryTypeEnum categoryLevel) {
        if (categoryLevel.equals(IntegralCategoryTypeEnum.SECOND_CATEGORY)) {
            Long id = getCategoryId(IntegralCategoryTypeEnum.FIRST_CATEGORY) == null ? createFirstCategory(getCategoryPicPath()) : getCategoryId(IntegralCategoryTypeEnum.FIRST_CATEGORY);
            CreateCategoryScene.builder().categoryName(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc()).belongPic(getCategoryPicPath()).categoryLevel(categoryLevel.name())
                    .belongCategory(id).build().invoke(visitor, true);
            return getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc());
        }
        if (categoryLevel.equals(IntegralCategoryTypeEnum.THIRD_CATEGORY)) {
            Long id = getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY) == null ? createSecondCategory() : getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY);
            CreateCategoryScene.builder().categoryName(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc()).belongPic(getCategoryPicPath()).categoryLevel(categoryLevel.name())
                    .belongCategory(id).build().invoke(visitor, true);
            return getCategoryId(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc());
        } else {
            return getCategoryId(categoryLevel) == null ? createFirstCategory(getCategoryPicPath()) : getCategoryId(categoryLevel);
        }
    }

    public String getCategoryPicPath() {
        String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        return getPicPath(pic, "1:1");
    }

    private Long createFirstCategory(String picPath) {
        String name = IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc();
        CreateCategoryScene.builder().categoryName(name).belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().invoke(visitor, true);
        return getCategoryId(name);
    }

    private Long createSecondCategory() {
        return getCategoryByLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY);
    }

    public Long getCategoryId(String categoryName) {
        IScene scene = CategoryPageScene.builder().build();
        return collectBeanByField(scene, CategoryPageBean.class, "category_name", categoryName).getId();
    }

    public Long getCategoryId(IntegralCategoryTypeEnum categoryLevel) {
        IScene scene = CategoryPageScene.builder().build();
        return collectBeanByField(scene, CategoryPageBean.class, "category_level", categoryLevel.getDesc()).getId();
    }

    public String getCategoryName(Long id) {
        IScene scene = CategoryPageScene.builder().build();
        return collectBeanByField(scene, CategoryPageBean.class, "id", id).getCategoryName();
    }
}
