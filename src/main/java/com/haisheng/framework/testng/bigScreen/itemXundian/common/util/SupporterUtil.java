package com.haisheng.framework.testng.bigScreen.itemXundian.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeGoodsDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.CategoryPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherInvalidPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralCategoryTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.generator.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall.*;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.messagemanager.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage.*;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class SupporterUtil extends BasicUtil {
    private final VisitorProxy visitor;

    /**
     * 构造函数
     *
     * @param visitor visitor
     */
    public SupporterUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
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
        CreateScene.CreateSceneBuilder builder = createVoucherBuilder(true);
        switch (type.name()) {
            case "FULL_DISCOUNT":
                builder.isThreshold(true).thresholdPrice(999.99).parValue(49.99).cost(49.99);
                break;
            case "COUPON":
                builder.isThreshold(true).thresholdPrice(999.99).discount(2.5).mostDiscount(99.99).cost(99.99);
                break;
            case "COMMODITY_EXCHANGE":
                builder.exchangeCommodityName("兑换布加迪威龙一辆").cost(49.99);
                break;
            default:
                builder.isThreshold(false);
                break;
        }
        return builder.stock(stock).cardType(type.name());
    }

    /**
     * 构建卡券信息
     *
     * @param selfVerification 能否核销
     * @return CreateVoucher.CreateVoucherBuilder
     */
    public CreateScene.CreateSceneBuilder createVoucherBuilder(Boolean selfVerification) {
        return CreateScene.builder().voucherDescription(getDesc()).shopType(0).selfVerification(selfVerification).shopIds(getShopIdList());
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
        List<VoucherPage> vouchers = toJavaObjectList(scene, VoucherPage.class);
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
        JSONArray array = ShopListScene.builder().build().invoke(visitor).getJSONArray("list");
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
        return toJavaObjectList(scene, VoucherSendRecord.class);
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
        return toJavaObjectList(scene, VoucherInvalidPageBean.class);
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
        return toJavaObject(scene, VoucherPage.class, "voucher_name", voucherName);
    }

    /**
     * 获取卡券页信息
     *
     * @param voucherId 卡券id
     * @return 卡券页信息
     */
    public VoucherPage getVoucherPage(Long voucherId) {
        IScene scene = VoucherFormVoucherPageScene.builder().build();
        return toJavaObject(scene, VoucherPage.class, "voucher_id", voucherId);
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
        ApplyApprovalScene.builder().id(applyPage.getId()).status(status).build().invoke(visitor);
    }

    //----------------------------------------------------核销人员-------------------------------------------------------

    /**
     * 消息推送
     *
     * @param type               推送优惠类型 0：卡券，1：套餐
     * @param voucherOrPackageId 卡券id
     * @param immediately        是否立即发送
     */
    public void pushMessage(Integer type, boolean immediately, Long... voucherOrPackageId) {
        List<Long> voucherOrPackageList = new ArrayList<>(Arrays.asList(voucherOrPackageId));
        List<String> phoneList = new ArrayList<>();
        phoneList.add(EnumAppletToken.JC_WM_DAILY.getPhone());
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
        SwitchVerificationStatusScene.builder().id(id).status(status).build().invoke(visitor);
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
        JSONArray specificationList = new JSONArray();
        JSONObject response = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().invoke(visitor).getJSONArray("list").getJSONObject(0);
        Long goodsId = response.getLong("id");
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
                .exchangePrice(1L).isLimit(true).exchangePeopleNum(10)
                .specificationList(specificationList).expireType(2).useDays(10)
                .exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime)
                .build().invoke(visitor);
        return toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
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
                .exchangeEndTime(exchangeEndTime).expireType(2).useDays(10).exchangeNum(1L).build().invoke(visitor);
        return toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * 获取积分兑换包含的卡券信息
     *
     * @param id 兑换商品id
     * @return 卡券信息
     */
    public VoucherPage getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor).getString("goods_name");
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
                .belongCategory(belongCategory).build().invoke(visitor);
        return getCategoryId(categoryName);
    }

    /**
     * 创建品类
     *
     * @param categoryLevel 品类等级
     */
    public Long getCategoryByLevel(IntegralCategoryTypeEnum categoryLevel) {
        if (categoryLevel.equals(IntegralCategoryTypeEnum.SECOND_CATEGORY)) {
            Long id = getCategoryId(IntegralCategoryTypeEnum.FIRST_CATEGORY);
            id = id == null ? createFirstCategory(getCategoryPicPath()) : id;
            CreateCategoryScene.builder().categoryName(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc()).belongPic(getCategoryPicPath()).categoryLevel(categoryLevel.name()).belongCategory(id).build().invoke(visitor);
            return getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc());
        }
        if (categoryLevel.equals(IntegralCategoryTypeEnum.THIRD_CATEGORY)) {
            Long id = getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY);
            id = id == null ? createSecondCategory() : id;
            CreateCategoryScene.builder().categoryName(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc()).belongPic(getCategoryPicPath()).categoryLevel(categoryLevel.name()).belongCategory(id).build().invoke(visitor);
            return getCategoryId(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc());
        } else {
            Long id = getCategoryId(categoryLevel);
            return id == null ? createFirstCategory(getCategoryPicPath()) : id;
        }
    }

    /**
     * 获取图片地址
     *
     * @return 品类地址
     */
    public String getCategoryPicPath() {
        String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        return getPicPath(pic, "1:1");
    }

    /**
     * 创建一级品类
     *
     * @param picPath 图片路径
     * @return 品类id
     */
    private Long createFirstCategory(String picPath) {
        String name = IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc();
        CreateCategoryScene.builder().categoryName(name).belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().invoke(visitor);
        return getCategoryId(name);
    }

    /**
     * 创建二级品类
     *
     * @return 品类id
     */
    private Long createSecondCategory() {
        return getCategoryByLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY);
    }

    /**
     * 获取品类id
     *
     * @param categoryName 品类名称
     * @return 品类id
     */
    public Long getCategoryId(String categoryName) {
        IScene scene = CategoryPageScene.builder().build();
        CategoryPageBean categoryPageBean = toJavaObject(scene, CategoryPageBean.class, "category_name", categoryName);
        logger.info("categoryName is：{}", categoryPageBean.getCategoryName());
        logger.info("categoryId is：{}", categoryPageBean.getId());
        return categoryPageBean.getId();
    }

    /**
     * 获取品类id
     *
     * @param categoryLevel 品类等级
     * @return 品类id
     */
    public Long getCategoryId(IntegralCategoryTypeEnum categoryLevel) {
        IScene scene = CategoryPageScene.builder().build();
        CategoryPageBean categoryPageBean = toJavaObject(scene, CategoryPageBean.class, "category_level", categoryLevel.getDesc());
        logger.info("categoryName is：{}", categoryPageBean.getCategoryName());
        logger.info("categoryId is：{}", categoryPageBean.getId());
        return categoryPageBean.getId();
    }

    /**
     * 获取品类名称
     *
     * @param id 品类id
     * @return 品类名称
     */
    public String getCategoryName(Long id) {
        IScene scene = CategoryPageScene.builder().build();
        return toJavaObject(scene, CategoryPageBean.class, "id", id).getCategoryName();
    }

    /**
     * 判断商品是否被占用
     *
     * @param goodsName 商品名称
     * @return 是否被占用
     */
    public Boolean goodsIsOccupation(String goodsName) {
        IScene scene = ExchangePageScene.builder().exchangeGoods(goodsName).build();
        ExchangePageBean exchangePageBean = toJavaObject(scene, ExchangePageBean.class, "goods_name", goodsName);
        return exchangePageBean != null;
    }

    public GoodsBean createGoods() {
        GoodsParamBean goodsParamBean = getGoodsParam();
        //创建商品
        Long goodsId = CreateGoodsScene.builder()
                .goodsName("自定义商品")
                .goodsDescription("自定义商品，用完销毁")
                .goodsBrand(goodsParamBean.getBrandId())
                .goodsPicList(new ArrayList<>(Collections.singleton(getCategoryPicPath())))
                .price(9.99)
                .selectSpecifications(getSelectSpecifications(goodsParamBean))
                .goodsSpecificationsList(getGoodsSpecificationsList(goodsParamBean))
                .goodsDetail("<p>" + EnumDesc.DESC_BETWEEN_15_20.getDesc() + "</p>")
                .firstCategory(goodsParamBean.getFirstCategory())
                .secondCategory(goodsParamBean.getSecondCategory())
                .thirdCategory(goodsParamBean.getThirdCategory())
                .build().invoke(visitor).getLong("id");
        GoodsBean goodsBean = new GoodsBean();
        goodsBean.setGoodsId(goodsId);
        goodsBean.setGoodsParamBean(goodsParamBean);
        return goodsBean;
    }

    public JSONArray getSelectSpecifications(GoodsParamBean goodsParamBean) {
        JSONObject data = SpecificationsDetailScene.builder().id(goodsParamBean.getSpecificationsId()).build().invoke(visitor);
        String specificationsName = data.getString("specifications_name");
        JSONObject specificationsDetail = data.getJSONArray("specifications_list").getJSONObject(0);
        JSONArray selectSpecifications = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("specifications_id", goodsParamBean.getSpecificationsId());
        object.put("specifications_name", specificationsName);
        JSONArray specificationsList = new JSONArray();
        JSONObject object1 = new JSONObject();
        object1.put("specifications_detail_id", specificationsDetail.getLong("specifications_id"));
        object1.put("specifications_detail_name", specificationsDetail.getString("specifications_item"));
        specificationsList.add(object1);
        object.put("specifications_list", specificationsList);
        selectSpecifications.add(object);
        return selectSpecifications;
    }

    public JSONArray getGoodsSpecificationsList(GoodsParamBean goodsParamBean) {
        JSONObject data = SpecificationsDetailScene.builder().id(goodsParamBean.getSpecificationsId()).build().invoke(visitor);
        JSONObject specificationsDetail = data.getJSONArray("specifications_list").getJSONObject(0);
        JSONArray goodsSpecificationsList = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("first_specifications", specificationsDetail.getLong("specifications_id"));
        object.put("first_specifications_name", specificationsDetail.getString("specifications_item"));
        object.put("head_pic", getCategoryPicPath());
        object.put("price", 9.99);
        goodsSpecificationsList.add(object);
        return goodsSpecificationsList;
    }

    public GoodsParamBean getGoodsParam() {
        GoodsParamBean goodsParamBean = new GoodsParamBean();
        //创建一级品类
        Long id = createCategory("汽车", getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
        //创建二级品类
        Long secondId = createCategory("豪华车", getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
        //创建三级品类
        Long thirdId = createCategory("特斯拉ModelS", getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
        //创建品牌
        Long brandId = CreateBrandScene.builder().brandPic(getCategoryPicPath()).brandName("特斯拉").brandDescription("商品使用，用完销毁").build().invoke(visitor).getLong("id");
        //给一级品类创建规格
        Long specificationsId = CreateSpecificationsScene.builder().belongsCategory(id).specificationsName("颜色").build().invoke(visitor).getLong("id");
        EditSpecificationsScene.builder().belongsCategory(id).id(specificationsId).specificationsList(getSpecificationsList()).specificationsName("颜色").build().invoke(visitor);
        //给规格创建规格参数
        goodsParamBean.setFirstCategory(id);
        goodsParamBean.setSecondCategory(secondId);
        goodsParamBean.setThirdCategory(thirdId);
        goodsParamBean.setBrandId(brandId);
        goodsParamBean.setSpecificationsId(specificationsId);
        return goodsParamBean;
    }

    public JSONArray getSpecificationsList() {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("specifications_item", "红色");
        array.add(object);
        return array;
    }


    public JSONArray getRuledays() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule1= new JSONObject();
        Rule1.put("days", "1");
        Rule1.put("coupon_id", voucher_id);
        Rule1.put("integral", "100");
        JSONObject Rule2= new JSONObject();
        Rule2.put("days", "2");
        Rule2.put("coupon_id", voucher_id);
        Rule2.put("integral", "200");
        JSONObject Rule3= new JSONObject();
        Rule3.put("days", "3");
        Rule3.put("coupon_id", voucher_id);
        Rule3.put("integral", "300");
        JSONObject Rule4= new JSONObject();
        Rule4.put("days", "7");
        Rule4.put("coupon_id", voucher_id);
        Rule4.put("integral", "700");
        RuleDaysList.add(Rule1);
        RuleDaysList.add(Rule2);
        RuleDaysList.add(Rule3);
        RuleDaysList.add(Rule4);
        return RuleDaysList;
    }
    public JSONArray fixed() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("date",DateTimeUtil.getFormat(new Date(),"yyyy-MM-dd"));
        Rule.put("integral","200");
        Rule.put("name","自动化创建固定日期");
        Rule.put("type","FIXED");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    public JSONArray dynamic() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("integral","200");
        Rule.put("name","自动化创建生日日期");
        Rule.put("type","DYNAMIC");
        Rule.put("dynamic_type","BIRTHDAY");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }
    public JSONArray cyclemonth() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("cycle_type","MONTH");
        Rule.put("date","1");
        Rule.put("integral","200");
        Rule.put("name","自动化周期日期按月");
        Rule.put("type","CYCLE");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    public JSONArray cycleweek() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("cycle_type","WEEK");
        Rule.put("date","MONDAY");
        Rule.put("integral","200");
        Rule.put("name","自动化周期日期按周");
        Rule.put("type","CYCLE");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    //--------------------------------------------------banner----------------------------------------------------

    /**
     * 获取文章id
     *
     * @return 文章id集合
     */
    public List<Long> getArticleIdList() {
        JSONArray array = visitor.invokeApi(ArticleList.builder().build()).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }
}
