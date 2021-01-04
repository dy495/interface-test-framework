package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.AppletVoucherListVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationApprovalVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationRegisterVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherInfoVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ApprovalPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticlePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.RegisterPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.BuyPackageRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PurchaseFixedPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PurchaseTemporaryPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.Page;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务场景工具
 */
public class BusinessUtilOnline {
    public static final Logger logger = LoggerFactory.getLogger(BusinessUtil.class);
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
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
                .shopType(0).shopIds(getShopIdList()).selfVerification(true).build();
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
    public List<Long> getShopIdList() {
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
        VoucherInfoVO voucherInfoVO = getVoucherInfo(voucherName);
        return voucherInfoVO.getVoucherId();
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
            List<String> phoneList = new ArrayList<>();
            phoneList.add(EnumAccount.MARKETING.getPhone());
            //发出此卡券
            PushMessage.PushMessageBuilder pushMessageBuilder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
                    .telList(phoneList).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
                    .type(0).voucherOrPackageList(voucherLIst).useDays(10).ifSendImmediately(true);
            jc.invokeApi(pushMessageBuilder.build());
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
                    && e.getString("audit_status_name").equals("已通过") && e.getLong("surplus_inventory") != null && e.getLong("surplus_inventory") != 0)
                    .map(e -> e.getLong("voucher_id")).collect(Collectors.toList()));
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
        IScene scene = AddVoucher.builder().id(voucherId).addNumber(num).build();
        jc.invokeApi(scene);
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
     * 获取卡券信息
     *
     * @param voucherName 卡券名称
     * @return 卡券信息
     */
    public VoucherInfoVO getVoucherInfo(String voucherName) {
        List<VoucherInfoVO> list = new ArrayList<>();
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName))
                    .map(e -> JSON.parseObject(JSON.toJSONString(e), VoucherInfoVO.class)).collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获取卡券信息集合
     *
     * @return 卡券信息集合
     */
    public JSONArray getVoucherInfo() {
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
    public JSONArray getVoucherInfo(int count) {
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
     * @param voucherName  卡券名
     * @param voucherCount 卡券数量
     * @return 卡券信息集合
     */
    public JSONArray getVoucherInfo(String voucherName, int voucherCount) {
        JSONArray voucherList = new JSONArray();
        IScene scene = VoucherFormPage.builder().voucherName(voucherName).size(size).build();
        JSONArray array = jc.invokeApi(scene).getJSONArray("list");
        JSONObject jsonObject = array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).collect(Collectors.toList()).get(0);
        jsonObject.put("voucher_count", voucherCount);
        voucherList.add(jsonObject);
        return voucherList;
    }

    /**
     * 获取一个卡券信息集合
     *
     * @param voucherName 卡券名称
     * @return 卡券信息列表
     */
    public JSONArray getOneVoucherInfo(String voucherName) throws Exception {
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
        JSONArray voucherList = getVoucherInfo(1);
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
     * 接待时购买固定套餐
     *
     * @param type 0赠送/1购买
     */
    public void receptionBuyFixedPackage(int type) {
        IScene pageScene = Page.builder().customerPhone(EnumAccount.MARKETING.getPhone()).build();
        JSONArray list = jc.invokeApi(pageScene).getJSONArray("list");
        JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).collect(Collectors.toList()).get(0);
        Long customerId = jsonObject.getLong("customer_id");
        Long receptionId = jsonObject.getLong("reception_id");
        String plateNumber = jsonObject.getString("plate_number");
        //购买套餐
        IScene purchaseScene = com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager
                .PurchaseFixedPackage.builder().customerPhone("").carType(EnumCarType.RECEPTION_CAR.name())
                .plateNumber(plateNumber).packageId(getPackageId(EnumVP.ONE.getPackageName()))
                .packagePrice("1.11").expiryDate("1").remark(EnumContent.B.getContent())
                .subjectType(getSubjectType()).subjectId(getSubjectId(getSubjectType()))
                .extendedInsuranceYear("").extendedInsuranceCopies("").type(type).receptionId(receptionId)
                .customerId(customerId).build();
        jc.invokeApi(purchaseScene);
    }

    /**
     * 接待时购买临时套餐
     *
     * @param type 0赠送/1购买
     */
    public void receptionBuyTemporaryPackage(int type) {
        IScene pageScene = Page.builder().customerPhone(EnumAccount.MARKETING.getPhone()).build();
        JSONArray list = jc.invokeApi(pageScene).getJSONArray("list");
        JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).collect(Collectors.toList()).get(0);
        Long customerId = jsonObject.getLong("customer_id");
        Long receptionId = jsonObject.getLong("reception_id");
        String plateNumber = jsonObject.getString("plate_number");
        //购买套餐
        IScene purchaseScene = com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.
                PurchaseTemporaryPackage.builder().customerPhone("").carType(EnumCarType.RECEPTION_CAR.name())
                .plateNumber(plateNumber).voucherList(getVoucherInfo(EnumVP.ONE.getVoucherName(), 1))
                .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(getSubjectType())
                .subjectId(getSubjectId(getSubjectType())).extendedInsuranceCopies("").extendedInsuranceYear("")
                .type(type).receptionId(receptionId).customerId(customerId).build();
        jc.invokeApi(purchaseScene);
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
        String d = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm:ss");
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
        return getVerificationCode(true, verificationIdentity);
    }

    /**
     * 获取核销码
     *
     * @param verificationIdentity 核销员身份
     * @return 核销码
     */
    public String getVerificationCode(boolean verificationStatus, String verificationIdentity) {
        List<String> list = new ArrayList<>();
        VerificationPeople.VerificationPeopleBuilder builder = VerificationPeople.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
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
        IScene scene = VerificationPeople.builder().verificationCode(code).build();
        long id = jc.invokeApi(scene).getJSONArray("list").getJSONObject(0).getLong("id");
        IScene scene1 = SwitchVerificationStatus.builder().id(id).status(status).build();
        jc.invokeApi(scene1);
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
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("title") != null
                    && e.getString("title").equals(voucherName)
                    && !e.getString("status_name").equals(EnumAppletVoucherStatus.EXPIRED.getName())
                    && !e.getString("status_name").equals(EnumAppletVoucherStatus.USED.getName()))
                    .map(e -> e.getLong("id")).collect(Collectors.toList()));
            logger.info("id:{},status:{}", id, status);
        } while (list.size() == 20);
        return voucherList.get(0);
    }

    /**
     * 获取小程序卡券名称
     *
     * @return 卡券编号
     */
    public String getAppletVoucherName(Long voucherId) {
        List<String> voucherList = new ArrayList<>();
        Integer id = null;
        Integer status = null;
        JSONArray list;
        do {
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(size).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(voucherId))
                    .map(e -> e.getString("title")).collect(Collectors.toList()));
            logger.info("id:{},status:{}", id, status);
        } while (list.size() == size);
        return voucherList.get(0);
    }

    /**
     * 获取小程序卡券集合
     *
     * @param type 类型：已使用/已过期/快过期
     * @return 卡券id集合
     */
    public List<Long> getAppletVoucherList(String type) {
        List<Long> voucherList = new ArrayList<>();
        Integer id = null;
        Integer status = null;
        JSONArray list;
        do {
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            list = response.getJSONArray("list");
            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("status_name").equals(type))
                    .map(e -> e.getLong("id")).collect(Collectors.toList()));
            logger.info("id:{},status:{}", id, status);
        } while (list.size() == 20);
        return voucherList;
    }

    /**
     * 获取小程序可用卡券的信息
     *
     * @return 卡券id
     */
    public List<AppletVoucherListVO> getAppletCanUsedVoucherInfoList() {
        List<AppletVoucherListVO> list = new ArrayList<>();
        Integer id = null;
        Integer status = null;
        JSONArray array;
        do {
            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
            JSONObject response = jc.invokeApi(builder.build());
            JSONObject lastValue = response.getJSONObject("last_value");
            id = lastValue.getInteger("id");
            status = lastValue.getInteger("status");
            array = response.getJSONArray("list");
            list.addAll(array.stream().map(jsonObject -> (JSONObject) jsonObject).filter(this::compareType)
                    .map(jsonObject -> JSON.parseObject(JSON.toJSONString(jsonObject), AppletVoucherListVO.class))
                    .collect(Collectors.toList()));
            logger.info("id:{},status:{}", id, status);
        } while (array.size() == 20);
        return list;
    }

    private boolean compareType(JSONObject jsonObject) {
        String statusName = jsonObject.getString("status_name");
        return !statusName.equals(EnumAppletVoucherStatus.EXPIRED.getName())
                && !statusName.equals(EnumAppletVoucherStatus.USED.getName());
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
    public int getAppletPackageNum() {
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
     * 获取小程序我的消息列表数
     *
     * @return 消息数量
     */
    public int getAppletMessageNum() {
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

    /**
     * 我的报名列表
     *
     * @return 报名数量
     */
    public int getAppletActivityNum() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            AppointmentActivityList.AppointmentActivityListBuilder builder = AppointmentActivityList.builder().lastValue(lastValue).size(20);
            JSONObject response = jc.invokeApi(builder.build());
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }

    /**
     * 获取能报名的活动id
     *
     * @param count 活动数量
     */
    public List<Long> getCanApplyActivityList(Integer count) {
        List<Long> activityIds = new ArrayList<>();
        String startDate = "2020-12-01";
        String endDate = DateTimeUtil.addDayFormat(new Date(), 365);
        ArticlePage.ArticlePageBuilder builder = ArticlePage.builder().registerStartDate(startDate).registerEndDate(endDate);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            activityIds.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("register_end_date").compareTo(DateTimeUtil.getFormat(new Date())) > 0
                    && e.getInteger("total_quota") > 100).map(e -> e.getLong("id")).collect(Collectors.toList()));
        }
        return count == null ? activityIds : activityIds.subList(0, count);
    }

    /**
     * 获取活动名称
     *
     * @param activityId 活动id
     * @return 活动名称
     */
    public String getActivityName(Long activityId) {
        List<String> list = new ArrayList<>();
        ArticlePage.ArticlePageBuilder builder = ArticlePage.builder();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(activityId)).map(e -> e.getString("title"))
                    .collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获取报名详情
     *
     * @param activityId 活动id
     * @return 报名详情
     */
    public OperationRegisterVO getOperationRegisterInfo(Long activityId) {
        List<OperationRegisterVO> operationRegisters = new ArrayList<>();
        RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
        int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
            operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(activityId)).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegisterVO.class)).collect(Collectors.toList()));
        }
        return operationRegisters.get(0);
    }

    /**
     * 获取审批详情
     *
     * @param articleId  文章id
     * @param approvalId 审批id
     * @return 审批详情
     */
    public OperationApprovalVO getOperationApprovalInfo(Long articleId, Long approvalId) {
        List<OperationApprovalVO> list = new ArrayList<>();
        ApprovalPage.ApprovalPageBuilder builder = ApprovalPage.builder().articleId(String.valueOf(articleId));
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(approvalId))
                    .map(e -> JSON.parseObject(JSON.toJSONString(e), OperationApprovalVO.class)).collect(Collectors.toList()));
        }
        return list.get(0);
    }

    /**
     * 获取文章id
     *
     * @return 文章id集合
     */
    public List<Long> getArticleIdList() {
        JSONArray array = jc.invokeApi(ArticleList.builder().build()).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    /**
     * 获取审批详情
     *
     * @param articleId 文章id
     * @return 审批详情
     */
    public List<OperationApprovalVO> getOperationApprovalInfo(Long articleId) {
        List<OperationApprovalVO> list = new ArrayList<>();
        ApprovalPage.ApprovalPageBuilder builder = ApprovalPage.builder().articleId(String.valueOf(articleId));
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationApprovalVO.class)).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 活动报名
     *
     * @param activityId 活动id
     */
    public void applyActivity(Long activityId) {
        jc.invokeApi(ActivityRegister.builder().id(activityId).name(EnumAccount.MARKETING.name()).phone(EnumAccount.MARKETING.getPhone()).num(1).build(), false);
    }
}
