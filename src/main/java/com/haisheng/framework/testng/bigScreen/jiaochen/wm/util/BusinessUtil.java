package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.MessageList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.PackageList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.VoucherList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.Create;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VerificationPeople;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormPage;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 */
public class BusinessUtil {
    public static final Logger logger = LoggerFactory.getLogger(BusinessUtil.class);
    ScenarioUtil jc = ScenarioUtil.getInstance();
    private static final int size = 100;

    /**
     * 创建卡券
     *
     * @param stock 创建数量
     * @return 创建完成的卡券名
     */
    public String createVoucher(Long stock) {
        String voucherName = createVoucherName();
        IScene scene = Create.builder().voucherPic(getPicPath()).voucherName(voucherName).subjectType(getSubjectType())
                .voucherDescription(getDesc()).subjectId(getSubjectId(getSubjectType())).stock(stock).cost(getCost(stock))
                .shopType(0).shopIds(getShopIds()).selfVerification(true).build();
        jc.invokeApi(scene);
        return voucherName;
    }

    /**
     * 创建一个不重复的卡券名
     *
     * @return 卡券名
     */
    public String createVoucherName() {
        int num = CommonUtil.getRandom(1, 100000);
        String voucherName = "立减" + num + "元代金券";
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
            if (array.isEmpty()) {
                return voucherName;
            }
            for (int j = 0; j < array.size(); j++) {
                if (!array.getJSONObject(j).getString("voucher_name").equals(voucherName)) {
                    return voucherName;
                }
            }
        }
        return createVoucherName();
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDesc() {
        return EnumContent.B.getContent();
    }

    /**
     * 获取成本
     *
     * @param stock 卡券数量
     * @return 卡券成本
     */
    public Double getCost(Long stock) {
        if (StringUtils.isEmpty(stock)) {
            return null;
        }
        return stock == 0 ? 10.05 : 50.05;
    }

    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        return jc.pcFileUpload(picture, false, 1.5).getString("pic_path");
    }

    /**
     * 获取主体类型
     *
     * @return 主体类型
     */
    public String getSubjectType() {
        JSONArray array = jc.pcSubjectList().getJSONArray("list");
        return array.getJSONObject(0).getString("subject_key");
    }

    /**
     * 获取主体详情
     *
     * @return 主体详情
     */
    public Long getSubjectId(String subjectType) {
        if (StringUtils.isEmpty(subjectType)) {
            return null;
        }
        switch (subjectType) {
            case "STORE":
                return getShopIds().get(0);
            case "BRAND":
                return getBrandIds().get(0);
            default:
                return null;
        }
    }

    /**
     * 获取品牌id
     *
     * @return 品牌id
     */
    public List<Long> getBrandIds() {
        List<Long> brandIds = new ArrayList<>();
        JSONArray array = jc.pcUserRangeDetail().getJSONArray("list");
        Long brandId = array.getJSONObject(0).getLong("id");
        brandIds.add(brandId);
        return brandIds;
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public List<Long> getShopIds() {
        List<Long> shopIds = new ArrayList<>();
        JSONArray array = jc.pcShopList().getJSONArray("list");
        Long shopId = array.getJSONObject(0).getLong("shop_id");
        shopIds.add(shopId);
        return shopIds;
    }

    /**
     * 获取卡券名称
     *
     * @param voucherId 卡券id
     * @return 卡券名
     */
    public String getVoucherName(long voucherId) {
        List<String> list = new ArrayList<>();
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("voucher_id").equals(voucherId))
                    .map(e -> e.getString("voucher_name")).collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获取卡券id
     *
     * @param voucherName 卡券名称
     * @return 卡券id(Long)
     */
    public Long getVoucherId(String voucherName) {
        IScene scene = VoucherFormPage.builder().voucherName(voucherName).size(size).build();
        JSONArray array = jc.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).map(e -> e.getLong("voucher_id")).collect(Collectors.toList()).get(0);
    }

    /**
     * 获取无库存的卡券id
     *
     * @return 卡券id
     */
    public Long getNoInventoryVoucherId() {
        List<Long> voucherLIst = new ArrayList<>();
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray list = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            voucherLIst.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("surplus_inventory") != null
                    && e.getLong("surplus_inventory") == 0 && !e.getString("invalid_status_name").equals("已作废")
                    && e.getString("audit_status_name").equals("已通过")).map(e -> e.getLong("voucher_id")).collect(Collectors.toList()));
        }
        if (voucherLIst.size() == 0) {
            String voucherName = createVoucher(1L);
            applyVoucher(voucherName, "1");
            voucherLIst.add(getVoucherId(voucherName));
        }
        return voucherLIst.get(0);
    }

    /**
     * 获取已作废卡券id
     *
     * @return 卡券id
     */
    public Long getObsoleteVoucherId() {
        List<Long> voucherList = new ArrayList<>();
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray list = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("invalid_status_name").equals("已作废")
                    && e.getString("audit_status_name").equals("已通过")).map(e -> e.getLong("voucher_id")).collect(Collectors.toList()));
        }
        if (voucherList.size() == 0) {
            String voucherName = createVoucher(1L);
            applyVoucher(voucherName, "1");
            invalidVoucher(voucherName);
            voucherList.add(getVoucherId(voucherName));
        }
        return voucherList.get(0);
    }

    /**
     * 增发卡券
     *
     * @param voucherName 增发的卡券名
     * @param num         增发数量
     */
    public void addVoucher(String voucherName, Integer num) {
        Long voucherId = getVoucherId(voucherName);
        jc.pcAddVoucher(voucherId, num);
    }

    /**
     * 作废卡券
     *
     * @param voucherName 被作废卡券的卡券名
     */
    public void invalidVoucher(String voucherName) {
        Long voucherId = getVoucherId(voucherName);
        jc.pcInvalidVoucher(voucherId);
    }

    /**
     * 获取重复的核销人员
     *
     * @return 电话号
     */
    public String getRepetitionVerificationPhone() {
        IScene scene = VerificationPeople.builder().build();
        return CommonUtil.getStrField(jc.invokeApi(scene), 0, "verification_phone");
    }

    /**
     * 创建套餐
     *
     * @return 套餐名
     */
    public String createPackage() {
        String packageName = createPackageName();
        IScene scene = CreatePackage.builder().packageName(createPackageName()).validity("30").packageDescription(getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectId(getSubjectType())).voucherList(getVoucherList())
                .packagePrice(5000.00).status(true).shopIds(getShopIds()).build();
        jc.invokeApi(scene);
        return packageName;
    }

    public void ss() {
        List<Long> packageList = new ArrayList<>();
        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray list = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            packageList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> !e.getString("package_name").equals(EnumVP.ONE.getPackageName())).map(e -> e.getLong("package_id")).collect(Collectors.toList()));
        }
        jc.pcPackageDetail(packageList.get(0));
    }

    /**
     * 创建一个套餐名
     *
     * @return 套餐名
     */
    public String createPackageName() {
        List<String> list = new ArrayList<>();
        int num = CommonUtil.getRandom(1, 10000);
        String packageName = "立减套餐" + num;
        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder().packageName(packageName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
            if (array.isEmpty()) {
                return packageName;
            }
            for (int j = 0; j < array.size(); j++) {
                if (!array.getJSONObject(j).getString("package_name").equals(packageName)) {
                    return packageName;
                }
            }
        }
        return createPackageName();
    }

    /**
     * 获取卡券信息集合
     *
     * @return 卡券信息集合
     */
    public JSONArray getVoucherList() {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        JSONArray list = jc.pcVoucherList().getJSONArray("list");
        if (list.isEmpty()) {
            String voucherName = createVoucher(1L);
            applyVoucher(voucherName, "1");
        }
        long id = jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getLong("id");
        object.put("voucher_id", id);
        object.put("voucher_count", 10);
        array.add(object);
        return array;
    }

    /**
     * 获取卡券信息集合
     *
     * @param count 卡券种类数
     * @return 卡券信息集合
     */
    public JSONArray getVoucherList(int count) {
        JSONArray voucherList = new JSONArray();
        JSONArray array = jc.pcVoucherList().getJSONArray("list");
        if (count > array.size()) {
            throw new DataException("count 不可大于 array.size()");
        }
        for (int i = 0; i < count; i++) {
            JSONObject object = array.getJSONObject(i);
            object.put("voucher_count", 1);
            voucherList.add(object);
        }
        return voucherList;
    }

    /**
     * 获取卡券信息集合
     *
     * @param voucherName 卡券名
     * @return 卡券信息集合
     */
    public JSONArray getVoucherList(String voucherName) {
        JSONArray voucherList = new JSONArray();
        JSONArray array = jc.pcVoucherList().getJSONArray("list");
        JSONObject jsonObject = array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).collect(Collectors.toList()).get(0);
        jsonObject.put("voucher_count", 2);
        voucherList.add(jsonObject);
        return voucherList;
    }

    /**
     * 获取一个卡券信息集合
     *
     * @param voucherName 卡券名称
     * @return 卡券信息列表
     */
    public JSONArray getOneVoucherList(String voucherName) throws Exception {
        JSONArray list = new JSONArray();
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).collect(Collectors.toList()));
        }
        if (list.size() == 0) {
            throw new Exception("无" + voucherName);
        }
        list.getJSONObject(0).put("voucher_count", 1);
        return list;
    }

    /**
     * 获取可修改的套餐id
     *
     * @return 套餐id
     */
    public Long getModifyPackageId() {
        List<Long> list = new ArrayList<>();
        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> !EnumVP.isContains(e.getString("package_name")))
                    .map(e -> e.getLong("package_id")).collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获套餐id
     *
     * @return 套餐id
     */
    public Long getPackageId() {
        JSONArray array = jc.pcPackageList().getJSONArray("list");
        if (array.size() == 0) {
            String packageName = createPackage();
            logger.info("packageName is：{}", packageName);
        }
        JSONObject data = jc.pcPackageList().getJSONArray("list").getJSONObject(0);
        return data.getLong("package_id");
    }

    /**
     * 获取套餐id
     *
     * @param packageName 套餐名称
     * @return 套餐id
     */
    public Long getPackageId(String packageName) {
        IScene scene = PackageFormPage.builder().packageName(packageName).page(1).size(size).build();
        JSONArray array = jc.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals(packageName))
                .map(e -> e.getLong("package_id")).collect(Collectors.toList()).get(0);
    }

    /**
     * 获取套餐名
     *
     * @param packageId 套餐id
     * @return 套餐名
     */
    public String getPackageName(long packageId) {
        List<String> list = new ArrayList<>();
        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(packageId))
                    .map(e -> e.getString("package_name")).collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获取套餐包含的卡券
     *
     * @param packageId 套餐id
     * @return 卡券id集合
     */
    public List<Long> getPackageContainVoucher(Long packageId) {
        List<Long> list = new ArrayList<>();
        JSONArray voucherList = jc.pcPackageDetail(packageId).getJSONArray("voucher_list");
        for (int i = 0; i < voucherList.size(); i++) {
            list.add(voucherList.getJSONObject(i).getLong("voucher_id"));
        }
        return list;
    }

    /**
     * 获取车牌号
     *
     * @param phone 电话号
     * @return 车牌号
     */
    public String getPlatNumber(String phone) {
        JSONArray array = jc.pcSearchCustomer(phone).getJSONArray("plate_list");
        return array.size() > 0 ? array.getJSONObject(0).getString("plate_number") : null;
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        List<Long> list = new ArrayList<>();
        ApplyPage.ApplyPageBuilder builder = ApplyPage.builder().name(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("status_name").equals("审核中")
                    && e.getString("name").equals(voucherName)).map(e -> e.getLong("id")).collect(Collectors.toList()));
        }
        jc.pcApplyApproval(list.get(0), status);
    }

    /**
     * 套餐确认支付
     *
     * @param packageName 套餐名
     */
    public void makeSureBuyPackage(String packageName) {
        //获取确认支付id
        IScene scene = BuyPackageRecord.builder().packageName(packageName).size(size).build();
        JSONArray list = jc.invokeApi(scene).getJSONArray("list");
        long id = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("package_name").equals(packageName))
                .map(e -> e.getLong("id")).collect(Collectors.toList()).get(0);
        //确认支付
        jc.pcMakeSureBuy(id, "AGREE");
    }

    /**
     * 获取非重复电话号
     *
     * @return 电话号
     */
    public String getDistinctPhone() {
        String phone = "155" + CommonUtil.getRandom(8);
        IScene scene = VerificationPeople.builder().verificationPhone(phone).build();
        int total = jc.invokeApi(scene).getInteger("total");
        if (total == 0) {
            return phone;
        }
        return getDistinctPhone();
    }

    /**
     * 获取员工电话
     *
     * @return 电话号
     */
    public String getStaffPhone() {
        int total = jc.pcStaffPage(null, 1, size).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.pcStaffPage(null, i, size).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                String phone = array.getJSONObject(j).getString("phone");
                IScene scene = VerificationPeople.builder().verificationPhone(phone).build();
                int phoneNumber = jc.invokeApi(scene).getInteger("total");
                if (phoneNumber == 0) {
                    return phone;
                }
            }
        }
        return null;
    }

    /**
     * 购买临时套餐
     *
     * @param type 0赠送/1购买
     */
    public void buyTemporaryPackage(int type) {
        EnumAccount marketing = EnumAccount.MARKETING;
        JSONArray voucherList = getVoucherList(1);
        String platNumber = getPlatNumber(marketing.getPhone());
        IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
                .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(platNumber).voucherList(voucherList)
                .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(getSubjectType())
                .subjectId(getSubjectId(getSubjectType())).extendedInsuranceYear("1")
                .extendedInsuranceCopies("1").type(type).build();
        jc.invokeApi(purchaseTemporaryPackageScene);
    }

    /**
     * 购买固定套餐
     *
     * @param type 0赠送/1购买
     */
    public void buyFixedPackage(int type) {
        EnumAccount marketing = EnumAccount.MARKETING;
        String subjectType = getSubjectType();
        long packageId = getPackageId(EnumVP.ONE.getPackageName());
        //购买固定套餐
        IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
                .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(getPlatNumber(marketing.getPhone()))
                .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
                .subjectType(subjectType).subjectId(getSubjectId(subjectType)).extendedInsuranceYear(10)
                .extendedInsuranceCopies(10).type(type).build();
        jc.invokeApi(purchaseFixedPackageScene);
    }

    /**
     * 消息推送
     *
     * @param immediately 是否立即发送
     * @return 发出去的卡券id
     */
    public Long pushMessage(boolean immediately) {
        List<String> phoneList = new ArrayList<>();
        phoneList.add(EnumAccount.MARKETING.getPhone());
        List<Long> voucherList = new ArrayList<>();
        Long voucherId = getVoucherId(EnumVP.ONE.getVoucherName());
        voucherList.add(voucherId);
        PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
                .telList(phoneList).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
                .type(0).voucherOrPackageList(voucherList).useDays(10);
        String d = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 60), "yyyy-MM-dd HH:mm:ss");
        long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(d));
        builder = immediately ? builder.ifSendImmediately(true) : builder.ifSendImmediately(false).sendTime(sendTime);
        jc.invokeApi(builder.build());
        return voucherId;
    }

    /**
     * 获取核销码
     *
     * @param verificationIdentity 核销员身份
     * @return 核销码
     */
    public String getVerificationCode(String verificationIdentity) {
        List<String> list = new ArrayList<>();
        VerificationPeople.VerificationPeopleBuilder builder = VerificationPeople.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getBoolean("verification_status")
                    && e.getString("verification_identity").equals(verificationIdentity))
                    .map(e -> e.getString("verification_code")).collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获取小程序卡券编号
     *
     * @return 卡券编号
     */
    public long getVoucherListId(String voucherName) throws Exception {
        Integer id = null;
        Integer status = null;
        JSONArray list;
        AtomicReference<Long> newId = new AtomicReference<>();
        AtomicBoolean sign = new AtomicBoolean(false);
        do {
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            list.forEach(e -> {
                JSONObject jsonObject = (JSONObject) e;
                String name = jsonObject.getString("title");
                String statusName = jsonObject.getString("status_name");
                if (voucherName.equals(name) && !statusName.equals(EnumAppletVoucherStatus.EXPIRED.getName())
                        && !statusName.equals(EnumAppletVoucherStatus.USED.getName())) {
                    sign.set(true);
                    newId.set(jsonObject.getLong("id"));
                }
            });
            logger.info("id:{},status:{}", id, status);
            if (sign.get()) {
                return newId.get();
            }
        } while (list.size() == 20);
        throw new Exception("无此卡券");
    }

    /**
     * 获取可用卡券id
     *
     * @return 卡券id
     */
    public JSONObject getVoucherListId() throws Exception {
        Integer id = null;
        Integer status = null;
        JSONArray list;
        AtomicBoolean sign = new AtomicBoolean(false);
        JSONObject object = new JSONObject();
        do {
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            list.forEach(e -> {
                JSONObject jsonObject = (JSONObject) e;
                String statusName = jsonObject.getString("status_name");
                if (!statusName.equals(EnumAppletVoucherStatus.EXPIRED.getName())
                        && !statusName.equals(EnumAppletVoucherStatus.USED.getName())) {
                    sign.set(true);
                    object.put("id", jsonObject.getLong("id"));
                    object.put("voucherName", jsonObject.getString("title"));
                }
            });
            logger.info("id:{},status:{}", id, status);
            if (sign.get()) {
                return object;
            }
        } while (list.size() == 20);
        throw new Exception("无可用卡券");
    }


    /**
     * 获取小程序卡券数量
     *
     * @return 卡券数量
     */
    public int getVoucherListSize() {
        Integer id = null;
        Integer status = null;
        JSONArray array;
        int listSize = 0;
        do {
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取小程序套餐数量
     *
     * @return 套餐数量
     */
    public int getPackageListSize() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            PackageList.PackageListBuilder builder = PackageList.builder().lastValue(lastValue).type("type").size(20);
            JSONObject response = jc.invokeApi(builder.build());
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 我的消息列表数
     *
     * @return 消息数量
     */
    public int getMessageListSize() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            MessageList.MessageListBuilder builder = MessageList.builder().lastValue(lastValue).size(20);
            JSONObject response = jc.invokeApi(builder.build());
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }
}
