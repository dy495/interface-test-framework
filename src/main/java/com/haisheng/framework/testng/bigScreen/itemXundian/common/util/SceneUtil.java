package com.haisheng.framework.testng.bigScreen.itemXundian.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.*;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.PatrolLoginScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.realtime.shop.PassPvUvScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.realtime.shop.PvUvScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeGoodsDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.CategoryPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
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
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import com.haisheng.framework.util.MD5Util;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ??????????????????
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class SceneUtil extends BasicUtil {
    private final VisitorProxy visitor;

    /**
     * ????????????
     *
     * @param visitor visitor
     */
    public SceneUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    public void loginPc(AccountEnum account) {
        String password = new MD5Util().getMD5(account.getPassword());
        IScene scene = PatrolLoginScene.builder().password(password).username(account.getUsername()).type(0).build();
        visitor.login(scene);
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
        double ratio = BigDecimal.valueOf(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1])).divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return FileUpload.builder().isPermanent(false).permanentPicType(0).pic(picture).ratioStr(ratioStr).ratio(ratio).build().visitor(visitor).execute().getString("pic_path");
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
                builder.exchangeCommodityName("???????????????????????????").cost(49.99);
                break;
            default:
                builder.isThreshold(false);
                break;
        }
        return builder.stock(stock).cardType(type.name());
    }

    /**
     * ??????????????????
     *
     * @param selfVerification ????????????
     * @return CreateVoucher.CreateVoucherBuilder
     */
    public CreateScene.CreateSceneBuilder createVoucherBuilder(Boolean selfVerification) {
        return CreateScene.builder().voucherDescription(getDesc()).shopType(0).selfVerification(selfVerification).shopIds(getShopIdList());
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
     * @param voucherId ??????id
     * @return ?????????
     */
    public String getVoucherName(long voucherId) {
        return getVoucherPage(voucherId).getVoucherName();
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

    /**
     * ????????????????????????
     *
     * @param voucherId ??????id
     * @return ??????????????????
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(Long voucherId) {
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        return toJavaObjectList(scene, VoucherSendRecord.class);
    }

    /**
     * ????????????????????????
     *
     * @param voucherName ????????????
     * @return ??????????????????
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(String voucherName) {
        Long voucherId = getVoucherId(voucherName);
        return getVoucherSendRecordList(voucherId);
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
     * ??????????????????
     *
     * @return ????????????
     */
    public JSONArray getVoucherArray() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return getVoucherArray(voucherId, 1);
    }

    /**
     * ??????????????????
     *
     * @param voucherId    ??????id
     * @param voucherCount ????????????
     * @return ????????????
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

    //----------------------------------------------------????????????-------------------------------------------------------

    /**
     * ????????????
     *
     * @param type               ?????????????????? 0????????????1?????????
     * @param voucherOrPackageId ??????id
     * @param immediately        ??????????????????
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
        builder.build().visitor(visitor).execute();
    }

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
        JSONArray specificationList = new JSONArray();
        JSONObject response = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
        Long goodsId = response.getLong("id");
        JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().visitor(visitor).execute().getJSONArray("specification_detail_list");
        specificationDetailList.forEach(e -> {
            JSONObject specificationDetail = (JSONObject) e;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", specificationDetail.getInteger("id"));
            jsonObject.put("stock", stock);
            specificationList.add(jsonObject);
        });
        //??????????????????
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                .exchangePrice(1L).isLimit(true).exchangePeopleNum(10)
                .specificationList(specificationList).expireType(2).useDays(10)
                .exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime)
                .build().visitor(visitor).execute();
        return toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * ????????????????????????
     *
     * @param voucherId ??????id
     * @return ??????????????????
     */
    public ExchangePage createExchangeFictitiousGoods(Long voucherId) {
        String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
        //??????????????????
        CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId)
                .exchangePrice(1L).isLimit(true).exchangePeopleNum(10).exchangeStartTime(exchangeStartTime)
                .exchangeEndTime(exchangeEndTime).expireType(2).useDays(10).exchangeNum(1L).build().visitor(visitor).execute();
        return toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param id ????????????id
     * @return ????????????
     */
    public VoucherFormVoucherPageBean getExchangeGoodsContainVoucher(Long id) {
        String voucherName = ExchangeGoodsStockScene.builder().id(id).build().visitor(visitor).execute().getString("goods_name");
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

    //-------------------------------------------------------????????????---------------------------------------------------

    /**
     * ????????????
     *
     * @param categoryName   ????????????
     * @param picPath        ????????????
     * @param categoryLevel  ????????????
     * @param belongCategory ????????????
     */
    public Long createCategory(String categoryName, String picPath, String categoryLevel, Long belongCategory) {
        CreateCategoryScene.builder().categoryName(categoryName).belongPic(picPath).categoryLevel(categoryLevel)
                .belongCategory(belongCategory).build().visitor(visitor).execute();
        return getCategoryId(categoryName);
    }

    /**
     * ????????????
     *
     * @param categoryLevel ????????????
     */
    public Long getCategoryByLevel(IntegralCategoryTypeEnum categoryLevel) {
        if (categoryLevel.equals(IntegralCategoryTypeEnum.SECOND_CATEGORY)) {
            Long id = getCategoryId(IntegralCategoryTypeEnum.FIRST_CATEGORY);
            id = id == null ? createFirstCategory(getCategoryPicPath()) : id;
            CreateCategoryScene.builder().categoryName(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc()).belongPic(getCategoryPicPath()).categoryLevel(categoryLevel.name()).belongCategory(id).build().visitor(visitor).execute();
            return getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc());
        }
        if (categoryLevel.equals(IntegralCategoryTypeEnum.THIRD_CATEGORY)) {
            Long id = getCategoryId(IntegralCategoryTypeEnum.SECOND_CATEGORY);
            id = id == null ? createSecondCategory() : id;
            CreateCategoryScene.builder().categoryName(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc()).belongPic(getCategoryPicPath()).categoryLevel(categoryLevel.name()).belongCategory(id).build().visitor(visitor).execute();
            return getCategoryId(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc());
        } else {
            Long id = getCategoryId(categoryLevel);
            return id == null ? createFirstCategory(getCategoryPicPath()) : id;
        }
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    public String getCategoryPicPath() {
        String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/??????.jpg";
        return getPicPath(pic, "1:1");
    }

    /**
     * ??????????????????
     *
     * @param picPath ????????????
     * @return ??????id
     */
    private Long createFirstCategory(String picPath) {
        String name = IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc();
        CreateCategoryScene.builder().categoryName(name).belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().visitor(visitor).execute();
        return getCategoryId(name);
    }

    /**
     * ??????????????????
     *
     * @return ??????id
     */
    private Long createSecondCategory() {
        return getCategoryByLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY);
    }

    /**
     * ????????????id
     *
     * @param categoryName ????????????
     * @return ??????id
     */
    public Long getCategoryId(String categoryName) {
        IScene scene = CategoryPageScene.builder().build();
        CategoryPageBean categoryPageBean = toJavaObject(scene, CategoryPageBean.class, "category_name", categoryName);
        logger.info("categoryName is???{}", categoryPageBean.getCategoryName());
        logger.info("categoryId is???{}", categoryPageBean.getId());
        return categoryPageBean.getId();
    }

    /**
     * ????????????id
     *
     * @param categoryLevel ????????????
     * @return ??????id
     */
    public Long getCategoryId(IntegralCategoryTypeEnum categoryLevel) {
        IScene scene = CategoryPageScene.builder().build();
        CategoryPageBean categoryPageBean = toJavaObject(scene, CategoryPageBean.class, "category_level", categoryLevel.getDesc());
        logger.info("categoryName is???{}", categoryPageBean.getCategoryName());
        logger.info("categoryId is???{}", categoryPageBean.getId());
        return categoryPageBean.getId();
    }

    /**
     * ??????????????????
     *
     * @param id ??????id
     * @return ????????????
     */
    public String getCategoryName(Long id) {
        IScene scene = CategoryPageScene.builder().build();
        return toJavaObject(scene, CategoryPageBean.class, "id", id).getCategoryName();
    }

    /**
     * ???????????????????????????
     *
     * @param goodsName ????????????
     * @return ???????????????
     */
    public Boolean goodsIsOccupation(String goodsName) {
        IScene scene = ExchangePageScene.builder().exchangeGoods(goodsName).build();
        ExchangePageBean exchangePageBean = toJavaObject(scene, ExchangePageBean.class, "goods_name", goodsName);
        return exchangePageBean != null;
    }

    public GoodsBean createGoods() {
        GoodsParamBean goodsParamBean = getGoodsParam();
        //????????????
        Long goodsId = CreateGoodsScene.builder()
                .goodsName("???????????????")
                .goodsDescription("??????????????????????????????")
                .goodsBrand(goodsParamBean.getBrandId())
                .goodsPicList(new ArrayList<>(Collections.singleton(getCategoryPicPath())))
                .price(9.99)
                .selectSpecifications(getSelectSpecifications(goodsParamBean))
                .goodsSpecificationsList(getGoodsSpecificationsList(goodsParamBean))
                .goodsDetail("<p>" + EnumDesc.DESC_BETWEEN_15_20.getDesc() + "</p>")
                .firstCategory(goodsParamBean.getFirstCategory())
                .secondCategory(goodsParamBean.getSecondCategory())
                .thirdCategory(goodsParamBean.getThirdCategory())
                .build().visitor(visitor).execute().getLong("id");
        GoodsBean goodsBean = new GoodsBean();
        goodsBean.setGoodsId(goodsId);
        goodsBean.setGoodsParamBean(goodsParamBean);
        return goodsBean;
    }

    public JSONArray getSelectSpecifications(GoodsParamBean goodsParamBean) {
        JSONObject data = SpecificationsDetailScene.builder().id(goodsParamBean.getSpecificationsId()).build().visitor(visitor).execute();
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
        JSONObject data = SpecificationsDetailScene.builder().id(goodsParamBean.getSpecificationsId()).build().visitor(visitor).execute();
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
        //??????????????????
        Long id = createCategory("??????", getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
        //??????????????????
        Long secondId = createCategory("?????????", getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
        //??????????????????
        Long thirdId = createCategory("?????????ModelS", getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
        //????????????
        Long brandId = CreateBrandScene.builder().brandPic(getCategoryPicPath()).brandName("?????????").brandDescription("???????????????????????????").build().visitor(visitor).execute().getLong("id");
        //???????????????????????????
        Long specificationsId = CreateSpecificationsScene.builder().belongsCategory(id).specificationsName("??????").build().visitor(visitor).execute().getLong("id");
        EditSpecificationsScene.builder().belongsCategory(id).id(specificationsId).specificationsList(getSpecificationsList()).specificationsName("??????").build().visitor(visitor).execute();
        //???????????????????????????
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
        object.put("specifications_item", "??????");
        array.add(object);
        return array;
    }


    public JSONArray getRuledays() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().visitor(visitor).execute().getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule1 = new JSONObject();
        Rule1.put("days", "1");
        Rule1.put("coupon_id", voucher_id);
        Rule1.put("integral", "100");
        JSONObject Rule2 = new JSONObject();
        Rule2.put("days", "2");
        Rule2.put("coupon_id", voucher_id);
        Rule2.put("integral", "200");
        JSONObject Rule3 = new JSONObject();
        Rule3.put("days", "3");
        Rule3.put("coupon_id", voucher_id);
        Rule3.put("integral", "300");
        JSONObject Rule4 = new JSONObject();
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
                .voucherStatus("WORKING").build().visitor(visitor).execute().getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("date", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd"));
        Rule.put("integral", "200");
        Rule.put("name", "???????????????????????????");
        Rule.put("type", "FIXED");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    public JSONArray dynamic() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().visitor(visitor).execute().getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("integral", "200");
        Rule.put("name", "???????????????????????????");
        Rule.put("type", "DYNAMIC");
        Rule.put("dynamic_type", "BIRTHDAY");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    public JSONArray cyclemonth() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().visitor(visitor).execute().getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("cycle_type", "MONTH");
        Rule.put("date", "1");
        Rule.put("integral", "200");
        Rule.put("name", "???????????????????????????");
        Rule.put("type", "CYCLE");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    public JSONArray cycleweek() {
        JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                .voucherStatus("WORKING").build().visitor(visitor).execute().getJSONArray("list");
        long voucher_id = voucher_list.getJSONObject(0).getLong("id");
        JSONArray RuleDaysList = new JSONArray();
        JSONObject Rule = new JSONObject();
        Rule.put("coupon_id", voucher_id);
        Rule.put("cycle_type", "WEEK");
        Rule.put("date", "MONDAY");
        Rule.put("integral", "200");
        Rule.put("name", "???????????????????????????");
        Rule.put("type", "CYCLE");
        RuleDaysList.add(Rule);
        return RuleDaysList;
    }

    //--------------------------------------------------banner----------------------------------------------------

    /**
     * ????????????id
     *
     * @return ??????id??????
     */
    public List<Long> getArticleIdList() {
        JSONArray array = ArticleList.builder().build().visitor(visitor).execute().getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }


    //--------------------------------------------------????????????----------------------------------------------------

    public List<ShopMessage> getDdcShopData(Stream<ShopMessage> stream) {
        return stream.filter(shopData -> shopData.getRealTimeShopPvUvBean().getTodayPv() == null || shopData.getRealTimeShopPvUvBean().getTodayPv().equals(0)).collect(Collectors.toList());
    }

    public List<ShopMessage> getBgyShopData(Stream<ShopMessage> stream) {
        return stream.filter(shopData -> shopData.getRealTimeShopPvUvBean().getTodayPv() == null || shopData.getRealTimeShopPvUvBean().getTodayPv().equals(0))
                .filter(shopData -> shopData.getRealTimeShopPvUvBean().getTodayUv() == null || shopData.getRealTimeShopPvUvBean().getTodayUv().equals(0)).collect(Collectors.toList());
    }

    public List<ShopMessage> getLzShopData(Stream<ShopMessage> stream) {
        return stream.filter(shopData -> shopData.getRealTimeShopPvUvBean().getTodayPv() == null || shopData.getRealTimeShopPvUvBean().getTodayPv().equals(0))
                .filter(shopData -> shopData.getRealTimeShopPvUvBean().getTodayUv() == null || shopData.getRealTimeShopPvUvBean().getTodayUv().equals(0)).collect(Collectors.toList());
    }

    public List<ShopMessage> getPassLzShopData(Stream<ShopMessage> stream) {
        return stream.filter(shopData -> shopData.getRealTimeShopPassPvUvBean().getTodayPv() == null || shopData.getRealTimeShopPassPvUvBean().getTodayPv().equals(0))
                .filter(shopData -> shopData.getRealTimeShopPassPvUvBean().getTodayUv() == null || shopData.getRealTimeShopPassPvUvBean().getTodayUv().equals(0)).collect(Collectors.toList());
    }

    public ShopMessage setShopData(PassengerFlowBean flowBean, RealTimeShopPvUvBean pvUvBean, RealTimeShopPassPvUvBean passPvUvBean) {
        ShopMessage shopData = new ShopMessage();
        shopData.setShopId(flowBean.getId());
        shopData.setShopName(flowBean.getName());
        shopData.setRealTimeShopPvUvBean(pvUvBean);
        shopData.setRealTimeShopPassPvUvBean(passPvUvBean);
        return shopData;
    }

    /**
     * ?????????????????????????????????
     *
     * @param shopId  ??????id
     * @param nowTime ????????????
     * @return RealTimeShopPassPvUvBean
     */
    public RealTimeShopPassPvUvBean findCurrentTimePassData(String shopId, String nowTime) {
        IScene scene = PassPvUvScene.builder().shopId(shopId).build();
        List<RealTimeShopPassPvUvBean> passPvUvList = toJavaObjectList(scene, RealTimeShopPassPvUvBean.class, "list");
        return passPvUvList.stream().filter(e -> filterTime(e.getTime())).filter(e -> e.getHour().equals(nowTime)).findFirst().orElse(null);
    }

    /**
     * ?????????????????????????????????
     *
     * @param shopId  ??????id
     * @param nowTime ????????????
     * @return RealTimeShopPvUvBean
     */
    public RealTimeShopPvUvBean findCurrentTimeEnterData(String shopId, String nowTime) {
        IScene scene = PvUvScene.builder().shopId(shopId).build();
        List<RealTimeShopPvUvBean> enterPvUvList = toJavaObjectList(scene, RealTimeShopPvUvBean.class, "list");
        return enterPvUvList.stream().filter(e -> filterTime(e.getTime())).filter(e -> e.getTime().substring(0, 2).equals(nowTime)).findFirst().orElse(null);
    }

    /**
     * ????????????
     *
     * @param time ????????????
     * @return boolean
     */
    public boolean filterTime(@org.jetbrains.annotations.NotNull String time) {
        return time.compareTo("09:00") >= 0 && time.compareTo("22:00") <= 0;
    }

    public void enterShopData(String subjectName, String time, List<ShopMessage> shopDataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("##### ").append(subjectName).append(" ").append("??????").append(shopDataList.size()).append("????????? ").append(time).append("???????????????0").append("\n");
        shopDataList.forEach(e -> sb.append("###### ").append(e.getShopName()).append("--").append(e.getShopId()).append("\n"));
        DingPushUtil ding = new DingPushUtil();
        ding.changeWeHook(DingWebhook.PV_UV_ACCURACY_GRP);
        ding.send(sb.toString());
    }

    public void passShopData(String subjectName, String time, List<ShopMessage> shopDataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("##### ").append(subjectName).append(" ").append("??????").append(shopDataList.size()).append("????????? ").append(time).append("???????????????0").append("\n");
        shopDataList.forEach(e -> sb.append("###### ").append(e.getShopName()).append("--").append(e.getShopId()).append("\n"));
        DingPushUtil ding = new DingPushUtil();
        ding.changeWeHook(DingWebhook.PV_UV_ACCURACY_GRP);
        ding.send(sb.toString());
    }

    public String pushMessage(String subjectName, List<DeviceMessage> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("##### ").append(subjectName).append("  ").append("????????????").append(list.size()).append("?????????????????????").append("\n");
        list.forEach(deviceMessage -> sb.append("###### ").append("???????????????").append(deviceMessage.getDeviceName()).append("\n")
                .append("###### ").append("??????ID???").append(deviceMessage.getDeviceId()).append(" ").append("???????????????").append(deviceMessage.getDeviceStatus()).append("\n"));
        DingPushUtil ding = new DingPushUtil();
        ding.changeWeHook(DingWebhook.ONLINE_STORE_MANAGEMENT_VIDEO);
        ding.send(sb.toString());
        return "push dingTalk";
    }
}
