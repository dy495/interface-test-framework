package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.applet.granted.PackageList;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.applet.granted.VoucherList;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.packagemanager.*;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.vouchermanage.Create;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.vouchermanage.VerificationPeople;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.vouchermanage.VoucherFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochenOnline.ScenarioUtilOnline;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 业务场景工具
 */
public class BusinessUtil {
    public static final Logger logger = LoggerFactory.getLogger(BusinessUtil.class);
    ScenarioUtil jc = ScenarioUtil.getInstance();
    ScenarioUtilOnline jcOnline = ScenarioUtilOnline.getInstance();
    private static final int size = 100;

    /**
     * 账号登录
     *
     * @param account 账号
     */
    public void login(EnumAccount account) {
        if (account.isDaily()) {
            jc.pcLogin(account.getPhone(), account.getPassword());
        } else {
            jcOnline.pcLogin(account.getPhone(), account.getPassword());
        }
    }

    public void loginApplet(EnumAppletCode appletCode) {
        jc.appletLoginToken(appletCode.getToken());
    }

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
     * 获取主体id
     *
     * @return 主体id
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
     * 创建卡券名称
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
     * 获取卡券名称
     *
     * @param id 卡券id
     * @return 卡券名
     */
    public String getVoucherName(long id) throws Exception {
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (array.getJSONObject(j).getLong("id").equals(id)) {
                    return array.getJSONObject(j).getString("voucher_name");
                }
            }
        }
        throw new Exception("卡券id：" + id + "不存在");
    }

    /**
     * 增发卡券
     *
     * @param voucherName 增发的卡券名
     * @param num         增发数量
     */
    public void addVoucher(String voucherName, Integer num) {
        IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
        int id = CommonUtil.getIntField(jc.invokeApi(scene), 0, "id");
        //增发
        jc.pcAddVoucher((long) id, num);
    }

    /**
     * 作废卡券
     *
     * @param voucherName 被作废卡券的卡券名
     */
    public void invalidVoucher(String voucherName) {
        //获取创建的卡券id
        IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
        JSONObject response = jc.invokeApi(scene);
        Integer id = CommonUtil.getIntField(response, 0, "id");
        //作废
        jc.pcInvalidVoucher((long) id);
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

    /**
     * 创建一个套餐名
     *
     * @return 套餐名
     */
    public String createPackageName() {
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
     * 获取卡券集合
     *
     * @return 卡券信息集合
     */
    public JSONArray getVoucherList() {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        JSONArray list = jc.pcVoucherList().getJSONArray("list");
        if (list.isEmpty()) {
            //创建卡券
            String voucherName = createVoucher(1L);
            //审核通过
            applyVoucher(voucherName, "1");
        }
        long id = jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getLong("id");
        object.put("voucher_id", id);
        object.put("voucher_count", 10);
        array.add(object);
        return array;
    }

    /**
     * 获取卡券集合
     *
     * @param count 卡券种类数
     * @return 券信息集合
     */
    public JSONArray getVoucherList(int count) {
        JSONArray array = jc.pcVoucherList().getJSONArray("list");
        if (count > array.size()) {
            throw new DataException("count 不可大于 array.size()");
        }
        JSONArray voucherList = new JSONArray();
        for (int i = 0; i < count; i++) {
            JSONObject object = array.getJSONObject(i);
            object.put("voucher_count", 1);
            voucherList.add(object);
        }
        return voucherList;
    }

    /**
     * 获套餐id
     *
     * @return 套餐id
     */
    public Long getPackageId() {
        JSONArray array = jc.pcPackageList().getJSONArray("list");
        if (array.size() == 0) {
            createPackage();
        }
        JSONObject data = jc.pcPackageList().getJSONArray("list").getJSONObject(0);
        return data.getLong("package_id");
    }

    /**
     * 获取套餐名
     *
     * @param packageId 套餐id
     * @return 套餐名
     */
    public String getPackageName(long packageId) {
        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (array.getJSONObject(j).getLong("id").equals(packageId)) {
                    return array.getJSONObject(j).getString("package_name");
                }
            }
        }
        return null;
    }

    public Long getPackageId(String packageName) {
        IScene scene = PackageFormPage.builder().packageName(packageName).build();
        JSONArray array = jc.invokeApi(scene).getJSONArray("list");
        return array.size() > 0 ? array.getJSONObject(0).getLong("id") : null;
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
        ApplyPage.ApplyPageBuilder builder = ApplyPage.builder().name(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        int id = 0;
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (array.getJSONObject(j).getString("status_name").equals("审核中")) {
                    id = array.getJSONObject(j).getInteger("id");
                    break;
                }
            }
        }
        //审批
        jc.pcApplyApproval((long) id, status);
    }

    /**
     * 套餐确认支付
     *
     * @param packageName 套餐名
     */
    public void makeSureBuyPackage(String packageName) {
        //获取确认支付id
        IScene buyPackageRecordScene = BuyPackageRecord.builder().packageName(packageName).build();
        int id = CommonUtil.getIntField(jc.invokeApi(buyPackageRecordScene), 0, "id");
        //确认支付
        jc.pcMakeSureBuy((long) id, "AGREE");
    }

    /**
     * 获取非重复电话号
     *
     * @return 电话号
     */
    public String getDistinctPhone() {
        login(EnumAccount.ADMINISTRATOR);
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
        long packageId = getPackageId("凯迪拉克无限套餐");
        //购买固定套餐
        IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
                .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(getPlatNumber(marketing.getPhone()))
                .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
                .subjectType(subjectType).subjectId(getSubjectId(subjectType)).extendedInsuranceYear(10)
                .extendedInsuranceCopies(10).type(type).build();
        jc.invokeApi(purchaseFixedPackageScene);
    }

    /**
     * 发消息
     *
     * @return 发出去的卡券id
     */
    public Long pushMessage() {
        List<String> phoneList = new ArrayList<>();
        phoneList.add(EnumAccount.MARKETING.getPhone());
        List<Long> voucherList = new ArrayList<>();
        long voucherId = getVoucherList().getJSONObject(0).getLong("voucher_id");
        voucherList.add(voucherId);
        //消息发送一张卡券
        IScene sendMesScene = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
                .telList(phoneList).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
                .type(0).voucherOrPackageList(voucherList).useDays(10).ifSendImmediately(true).build();
        jc.invokeApi(sendMesScene);
        return voucherId;
    }

    /**
     * 获取核销码
     *
     * @param verificationIdentity 核销员身份
     * @return 核销码
     */
    public String getVerificationCode(String verificationIdentity) {
        VerificationPeople.VerificationPeopleBuilder builder = VerificationPeople.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray list = jc.invokeApi(builder.build()).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getBoolean("verification_status")
                        && list.getJSONObject(j).getString("verification_identity").equals(verificationIdentity)) {
                    return list.getJSONObject(j).getString("verification_code");
                }
            }
        }
        return null;
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
}
