package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.util.BaseUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.PackagePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.apply.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher.VoucherList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumPushTarget;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherUseStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletVoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.PackageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.VoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.Detail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.Approval;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.CreateVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.SendRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2021/1/20 13:36
 * @desc 业务场景工具
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
     * 创建4种优惠券
     *
     * @param stock 卡券库存
     * @param type  卡券类型
     * @return 卡券名称
     */
    public String createVoucher(Integer stock, VoucherTypeEnum type) {
        String voucherName = createVoucherName(type);
        CreateVoucherScene.CreateVoucherSceneBuilder builder = createVoucherBuilder(true).stock(stock).cardType(type.name()).voucherName(voucherName);
        switch (type.name()) {
            case "FULL_DISCOUNT":
                builder.isThreshold(true).thresholdPrice(999.99);
                break;
            case "COUPON":
                builder.isThreshold(true).thresholdPrice(999.99).discount(2.5).mostDiscount(100.00);
                break;
            case "COMMODITY_EXCHANGE":
                builder.isThreshold(true).thresholdPrice(999.99).exchangeCommodityName("兑换布加迪威龙一辆");
                break;
            default:
                builder.isThreshold(false);
                break;
        }
        visitor.invokeApi(builder.build());
        return voucherName;
    }

    /**
     * 构建卡券信息
     *
     * @param selfVerification 能否核销
     * @return CreateVoucher.CreateVoucherBuilder
     */
    public CreateVoucherScene.CreateVoucherSceneBuilder createVoucherBuilder(Boolean selfVerification) {
        return CreateVoucherScene.builder().isDefaultPic(false).voucherPic(getPicPath()).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
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
        return createVoucherName();
    }

    /**
     * 创建描述
     *
     * @return 描述
     */
    public String getDesc() {
        return EnumContent.B.getContent();
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
        String picture = new ImageUtil().getImageBinary(path);
        IScene scene = FileUpload.builder().isPermanent(false).pic(picture).ratio(1.5).build();
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
        IScene scene = ShopList.builder().build();
        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList());
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
     * @return 领取记录集合
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(Long voucherId) {
        IScene scene = SendRecordScene.builder().id(voucherId).build();
        return collectBean(scene, VoucherSendRecord.class);
    }

    /**
     * 获取卡券领取记录
     *
     * @param voucherName 卡券名称
     * @return 领取记录集合
     */
    public List<VoucherSendRecord> getVoucherSendRecordList(String voucherName) {
        Long voucherId = getVoucherId(voucherName);
        return getVoucherSendRecordList(voucherId);
    }

//    /**
//     * 获取无库存的卡券id
//     *
//     * @return 卡券id
//     */
//    public Long getNoInventoryVoucherId() {
//        List<Long> voucherLIst = new ArrayList<>();
//        VoucherPageScene.VoucherPageSceneBuilder builder = VoucherPageScene.builder();
//        int total = visitor.invokeApi(builder.build()).getInteger("total");
//        int s = CommonUtil.getTurningPage(total, SIZE);
//        for (int i = 1; i < s; i++) {
//            JSONArray list = visitor.invokeApi(builder.page(i).size(SIZE).build()).getJSONArray("list");
//            voucherLIst.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("surplus_inventory") != null
//                    && e.getLong("surplus_inventory") == 0 && !e.getString("invalid_status_name").equals("已作废")
//                    && e.getString("audit_status_name").equals("已通过")).map(e -> e.getLong("voucher_id")).collect(Collectors.toList()));
//        }
//        if (voucherLIst.size() == 0) {
//            String voucherName = createVoucher(1, VoucherTypeEnum.CUSTOM);
//            applyVoucher(voucherName, "1");
//            voucherLIst.add(getVoucherId(voucherName));
//            List<String> phoneList = new ArrayList<>();
//            phoneList.add(EnumAccount.MARKETING.getPhone());
//            //发出此卡券
//            IScene scene = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                    .telList(phoneList).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .type(0).voucherOrPackageList(voucherLIst).useDays(10).ifSendImmediately(true).build();
//            visitor.invokeApi(scene);
//        }
//        return voucherLIst.get(0);
//    }


//    /**
//     * 获取已作废卡券id
//     *
//     * @return 卡券id
//     */
//    public Long getObsoleteVoucherId() {
//        IScene scene = VoucherPageScene.builder().build();
//        List<VoucherPage> vouchers = collectBean(scene, VoucherPage.class);
//        Long voucherId = vouchers.stream().filter(e -> e.getInvalidStatusName().equals("已作废") && e.getAuditStatusName().equals("已通过") && e.getSurplusInventory() != null && e.getSurplusInventory() != 0).map(VoucherPage::getVoucherId).findFirst().orElse(null);
//        if (voucherId == null) {
//            String voucherName = createVoucher(1, VoucherTypeEnum.CUSTOM);
//            applyVoucher(voucherName, "1");
//            invalidVoucher(voucherName);
//            return getVoucherId(voucherName);
//        }
//        return voucherId;
//    }

//    /**
//     * 增发卡券
//     *
//     * @param voucherName 增发的卡券名
//     * @param number      增发数量
//     */
//    public void addVoucher(String voucherName, Integer number) {
//        Long voucherId = getVoucherId(voucherName);
//        IScene scene = AddVoucherScene.builder().id(voucherId).addNumber(number).build();
//        visitor.invokeApi(scene);
//    }

//    /**
//     * 作废卡券
//     *
//     * @param voucherName 被作废卡券的卡券名
//     */
//    public void invalidVoucher(String voucherName) {
//        Long voucherId = getVoucherId(voucherName);
//        IScene scene = InvalidVoucher.builder().id(voucherId).build();
//        visitor.invokeApi(scene);
//    }

//    /**
//     * 获取重复的核销人员
//     *
//     * @return 电话号
//     */
//    public String getRepetitionVerificationPhone() {
//        IScene scene = VerificationPeople.builder().build();
//        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
//        return array.stream().map(e -> (JSONObject) e).map(e -> e.getString("verification_phone")).findFirst().orElse(null);
//    }

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
        IScene scene = VoucherListScene.builder().build();
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

//    /**
//     * 获取卡券信息集合
//     *
//     * @return 卡券信息集合
//     */
//    public JSONArray getVoucherInfo() {
//        JSONArray array = new JSONArray();
//        JSONObject object = new JSONObject();
//        IScene scene = VoucherListScene.builder().build();
//        JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
//        if (list.isEmpty()) {
//            String voucherName = createVoucher(1, VoucherTypeEnum.CUSTOM);
//            applyVoucher(voucherName, "1");
//        }
//        Long id = Objects.requireNonNull(visitor.invokeApi(scene).getJSONArray("list").stream().map(e -> (JSONObject) e).findFirst().orElse(null)).getLong("id");
//        object.put("voucher_id", id);
//        object.put("voucher_count", 10);
//        array.add(object);
//        return array;
//    }

//    /**
//     * 获取卡券信息集合
//     *
//     * @param count 卡券种类数
//     * @return 卡券信息集合
//     */
//    public JSONArray getVoucherArray(int count) {
//        JSONArray voucherList = new JSONArray();
//        IScene scene = VoucherListScene.builder().build();
//        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
//        if (count > array.size()) {
//            throw new DataException("count 不可大于" + array.size());
//        }
//        for (int i = 0; i < count; i++) {
//            JSONObject object = array.getJSONObject(i);
//            object.put("voucher_count", 1);
//            voucherList.add(object);
//        }
//        return voucherList;
//    }

//    /**
//     * 获取卡券信息集合
//     *
//     * @param voucherName  卡券名
//     * @param voucherCount 卡券数量
//     * @return 卡券信息集合
//     */
//    public JSONArray getVoucherInfo(String voucherName, int voucherCount) {
//        JSONArray voucherList = new JSONArray();
//        IScene scene = VoucherPageScene.builder().voucherName(voucherName).size(SIZE).build();
//        JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
//        JSONObject jsonObject = array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).collect(Collectors.toList()).get(0);
//        jsonObject.put("voucher_count", voucherCount);
//        voucherList.add(jsonObject);
//        return voucherList;
//    }

//    /**
//     * 获取一个卡券信息集合
//     *
//     * @param voucherName 卡券名称
//     * @return 卡券信息列表
//     */
//    public JSONArray getOneVoucherInfo(String voucherName) throws Exception {
//        JSONArray list = new JSONArray();
//        VoucherPageScene.VoucherPageSceneBuilder builder = VoucherPageScene.builder().voucherName(voucherName);
//        int total = visitor.invokeApi(builder.build()).getInteger("total");
//        int s = CommonUtil.getTurningPage(total, SIZE);
//        for (int i = 1; i < s; i++) {
//            JSONArray array = visitor.invokeApi(builder.page(i).size(SIZE).build()).getJSONArray("list");
//            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).collect(Collectors.toList()));
//        }
//        if (list.size() == 0) {
//            throw new Exception("无" + voucherName);
//        }
//        list.getJSONObject(0).put("voucher_count", 1);
//        return list;
//    }
//
//    /**
//     * 获取可修改的套餐id
//     *
//     * @return 套餐id
//     */
//    public Long getModifyPackageId() {
//        List<Long> list = new ArrayList<>();
//        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder();
//        int total = jc.invokeApi(builder.build()).getInteger("total");
//        int s = CommonUtil.getTurningPage(total, size);
//        for (int i = 1; i < s; i++) {
//            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> !EnumVP.isContains(e.getString("package_name")))
//                    .map(e -> e.getLong("package_id")).collect(Collectors.toList()));
//        }
//        return list.get(0);
//    }
//

//
//
//
//
//
//
//

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
    public ApplyPage getApplyPage(String voucherName) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.name()).build();
        List<ApplyPage> voucherApplies = collectBean(scene, ApplyPage.class);
        return voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).findFirst().orElse(null);
    }

    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        IScene scene = ApplyPageScene.builder().name(voucherName).status(ApplyStatusEnum.AUDITING.name()).build();
        List<ApplyPage> voucherApplies = collectBean(scene, ApplyPage.class);
        Long applyId = voucherApplies.stream().filter(e -> e.getName().equals(voucherName)).map(ApplyPage::getId).findFirst().orElse(null);
        visitor.invokeApi(Approval.builder().id(applyId).status(status).build());
    }

    //--------------------------------------------------套餐----------------------------------------------------------


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
        visitor.invokeApi(MakeSureBuy.builder().id(id).auditStatus("AGREE").build());
    }

    /**
     * 创建一个套餐名
     *
     * @return 套餐名
     */
    public String createPackageName() {
        int num = CommonUtil.getRandom(1, 10000);
        String packageName = "套餐" + num;
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
        return createPackageName();
    }

    /**
     * 创建一个套餐
     *
     * @param voucherList 套餐所含卡券信息
     * @return 套餐名
     */
    public String createPackage(JSONArray voucherList) {
        String packageName = createPackageName();
        IScene scene = CreatePackageScene.builder().packageName(packageName).validity("30").packageDescription(getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType())).voucherList(voucherList)
                .packagePrice(49.99).status(true).shopIds(getShopIdList(3)).build();
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
        IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
                .validity(2000).subjectType(getSubjectType()).subjectId(getSubjectDesc(getSubjectType()))
                .voucherList(voucherList).packagePrice("1.11").status(true).shopIds(getShopIdList(3))
                .id(String.valueOf(packageId)).build();
        visitor.invokeApi(editPackageScene);
        return packageName;
    }


//
//    /**
//     * 获取非重复电话号
//     *
//     * @return 电话号
//     */
//    public String getDistinctPhone() {
//        String phone = "155" + CommonUtil.getRandom(8);
//        IScene scene = VerificationPeople.builder().verificationPhone(phone).build();
//        int total = jc.invokeApi(scene).getInteger("total");
//        if (total == 0) {
//            return phone;
//        }
//        return getDistinctPhone();
//    }
//
//    /**
//     * 获取员工电话
//     *
//     * @return 电话号
//     */
//    public String getStaffPhone() {
//        int total = jc.pcStaffPage(null, 1, size).getInteger("total");
//        int s = CommonUtil.getTurningPage(total, size);
//        for (int i = 1; i < s; i++) {
//            JSONArray array = jc.pcStaffPage(null, i, size).getJSONArray("list");
//            for (int j = 0; j < array.size(); j++) {
//                String phone = array.getJSONObject(j).getString("phone");
//                IScene scene = VerificationPeople.builder().verificationPhone(phone).build();
//                int phoneNumber = jc.invokeApi(scene).getInteger("total");
//                if (phoneNumber == 0) {
//                    return phone;
//                }
//            }
//        }
//        return null;
//
//
//    /**
//     * 购买临时套餐
//     *
//     * @param type 0赠送/1购买
//     */
//    public void buyTemporaryPackage(int type) {
//        EnumAccount marketing = EnumAccount.MARKETING;
//        JSONArray voucherList = getVoucherInfo(1);
//        String platNumber = getPlatNumber(marketing.getPhone());
//        IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
//                .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(platNumber).voucherList(voucherList)
//                .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(getSubjectType())
//                .subjectId(getSubjectId(getSubjectType())).extendedInsuranceYear("1")
//                .extendedInsuranceCopies("1").type(type).build();
//        jc.invokeApi(purchaseTemporaryPackageScene);
//    }
//
//    /**
//     * 购买固定套餐
//     *
//     * @param type 0赠送/1购买
//     */
//    public void buyFixedPackage(int type) {
//        EnumAccount marketing = EnumAccount.MARKETING;
//        String subjectType = getSubjectType();
//        long packageId = getPackageId(EnumVP.ONE.getPackageName());
//        //购买固定套餐
//        IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(getPlatNumber(marketing.getPhone()))
//                .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                .subjectType(subjectType).subjectId(getSubjectId(subjectType)).extendedInsuranceYear(10)
//                .extendedInsuranceCopies(10).type(type).build();
//        jc.invokeApi(purchaseFixedPackageScene);
//    }
//
//    /**
//     * 接待时购买固定套餐
//     *
//     * @param type 0赠送/1购买
//     */
//    public void receptionBuyFixedPackage(int type) {
//        IScene pageScene = Page.builder().customerPhone(EnumAccount.MARKETING.getPhone()).build();
//        JSONArray list = jc.invokeApi(pageScene).getJSONArray("list");
//        JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).collect(Collectors.toList()).get(0);
//        Long customerId = jsonObject.getLong("customer_id");
//        Long receptionId = jsonObject.getLong("reception_id");
//        String plateNumber = jsonObject.getString("plate_number");
//        //购买套餐
//        IScene purchaseScene = com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager
//                .PurchaseFixedPackage.builder().customerPhone("").carType(EnumCarType.RECEPTION_CAR.name())
//                .plateNumber(plateNumber).packageId(getPackageId(EnumVP.ONE.getPackageName()))
//                .packagePrice("1.11").expiryDate("1").remark(EnumContent.B.getContent())
//                .subjectType(getSubjectType()).subjectId(getSubjectId(getSubjectType()))
//                .extendedInsuranceYear("").extendedInsuranceCopies("").type(type).receptionId(receptionId)
//                .customerId(customerId).build();
//        jc.invokeApi(purchaseScene);
//    }
//
//    /**
//     * 接待时购买临时套餐
//     *
//     * @param type 0赠送/1购买
//     */
//    public void receptionBuyTemporaryPackage(int type) {
//        IScene pageScene = Page.builder().customerPhone(EnumAccount.MARKETING.getPhone()).build();
//        JSONArray list = jc.invokeApi(pageScene).getJSONArray("list");
//        JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).collect(Collectors.toList()).get(0);
//        Long customerId = jsonObject.getLong("customer_id");
//        Long receptionId = jsonObject.getLong("reception_id");
//        String plateNumber = jsonObject.getString("plate_number");
//        //购买套餐
//        IScene purchaseScene = com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.
//                PurchaseTemporaryPackage.builder().customerPhone("").carType(EnumCarType.RECEPTION_CAR.name())
//                .plateNumber(plateNumber).voucherList(getVoucherInfo(EnumVP.ONE.getVoucherName(), 1))
//                .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(getSubjectType())
//                .subjectId(getSubjectId(getSubjectType())).extendedInsuranceCopies("").extendedInsuranceYear("")
//                .type(type).receptionId(receptionId).customerId(customerId).build();
//        jc.invokeApi(purchaseScene);
//    }
//

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
        visitor.invokeApi(builder.build());
        return voucherId;
    }
//
//    /**
//     * 获取核销码
//     *
//     * @param verificationIdentity 核销员身份
//     * @return 核销码
//     */
//    public String getVerificationCode(String verificationIdentity) {
//        return getVerificationCode(true, verificationIdentity);
//    }
//
//    /**
//     * 获取核销码
//     *
//     * @param verificationIdentity 核销员身份
//     * @return 核销码
//     */
//    public String getVerificationCode(boolean verificationStatus, String verificationIdentity) {
//        List<String> list = new ArrayList<>();
//        VerificationPeople.VerificationPeopleBuilder builder = VerificationPeople.builder();
//        int total = jc.invokeApi(builder.build()).getInteger("total");
//        int s = CommonUtil.getTurningPage(total, size);
//        for (int i = 1; i < s; i++) {
//            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getBoolean("verification_status") == verificationStatus
//                    && e.getString("verification_identity").equals(verificationIdentity)).map(e -> e.getString("verification_code")).collect(Collectors.toList()));
//        }
//        if (list.size() == 0) {
//            String code = getVerificationCode(verificationIdentity);
//            list.add(code);
//            switchVerificationStatus(code, false);
//        }
//        return list.get(0);
//    }
//
//    /**
//     * 核销人员状态更改
//     *
//     * @param code   核销码
//     * @param status 状态
//     */
//    public void switchVerificationStatus(String code, boolean status) {
//        IScene scene = VerificationPeople.builder().verificationCode(code).build();
//        long id = jc.invokeApi(scene).getJSONArray("list").getJSONObject(0).getLong("id");
//        IScene scene1 = SwitchVerificationStatus.builder().id(id).status(status).build();
//        jc.invokeApi(scene1);
//    }
//

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
            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("title") != null && e.getString("title").equals(voucherName) && !e.getString("status_name").equals(VoucherUseStatusEnum.EXPIRED.getName()) && !e.getString("status_name").equals(VoucherUseStatusEnum.USED.getName())).map(e -> e.getLong("id")).collect(Collectors.toList()));
            logger.info("id:{},status:{}", id, status);
        } while (list.size() == 20);
        return voucherList.get(0);
    }
//
//    /**
//     * 获取小程序卡券名称
//     *
//     * @return 卡券编号
//     */
//    public String getAppletVoucherName(Long voucherId) {
//        List<String> voucherList = new ArrayList<>();
//        Integer id = null;
//        Integer status = null;
//        JSONArray list;
//        do {
//            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(size).id(id).status(status);
//            JSONObject response = jc.invokeApi(builder.build());
//            JSONObject lastValue = response.getJSONObject("last_value");
//            id = lastValue.getInteger("id");
//            status = lastValue.getInteger("status");
//            list = response.getJSONArray("list");
//            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(voucherId))
//                    .map(e -> e.getString("title")).collect(Collectors.toList()));
//            logger.info("id:{},status:{}", id, status);
//        } while (list.size() == size);
//        return voucherList.get(0);
//    }
//
//    /**
//     * 获取小程序卡券集合
//     *
//     * @param type 类型：已使用/已过期/快过期
//     * @return 卡券id集合
//     */
//    public List<Long> getAppletVoucherList(String type) {
//        List<Long> voucherList = new ArrayList<>();
//        Integer id = null;
//        Integer status = null;
//        JSONArray list;
//        do {
//            VoucherList.VoucherListBuilder builder = VoucherList.builder().type("GENERAL").size(20).id(id).status(status);
//            JSONObject response = jc.invokeApi(builder.build());
//            JSONObject lastValue = response.getJSONObject("last_value");
//            id = lastValue.getInteger("id");
//            status = lastValue.getInteger("status");
//            list = response.getJSONArray("list");
//            voucherList.addAll(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("status_name").equals(type))
//                    .map(e -> e.getLong("id")).collect(Collectors.toList()));
//            logger.info("id:{},status:{}", id, status);
//        } while (list.size() == 20);
//        return voucherList;
//    }
//

//    /**
//     * 获取小程序可用卡券的信息
//     *
//     * @return 卡券id
//     */
//    public List<AppletVoucherList> getAppletCanUsedVoucherList() {
//        List<AppletVoucherList> list = new ArrayList<>();
//        Integer id = null;
//        Integer status = null;
//        JSONArray array;
//        do {
//            IScene scene = AppletVoucherListScene.builder().type("GENERAL").size(20).id(id).status(status).build();
//            JSONObject response = visitor.invokeApi(scene);
//            JSONObject lastValue = response.getJSONObject("last_value");
//            id = lastValue.getInteger("id");
//            status = lastValue.getInteger("status");
//            array = response.getJSONArray("list");
//            list.addAll(array.stream().map(jsonObject -> (JSONObject) jsonObject).filter(this::compareType).map(jsonObject -> JSONObject.toJavaObject(jsonObject, AppletVoucherList.class)).collect(Collectors.toList()));
//            logger.info("id:{},status:{}", id, status);
//        } while (array.size() == 20);
//        return list;
//    }
//
//    private boolean compareType(JSONObject jsonObject) {
//        String statusName = jsonObject.getString("status_name");
//        return !statusName.equals(EnumAppletVoucherStatus.EXPIRED.getName()) && !statusName.equals(EnumAppletVoucherStatus.USED.getName());
//    }
//

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
     * 获取小程序套餐数量
     *
     * @return 套餐数量
     */
    public int getAppletPackageNum() {
        Long lastValue = null;
        int listSize = 0;
        JSONArray array;
        do {
            IScene scene = PackageListScene.builder().lastValue(lastValue).type("type").size(20).build();
            JSONObject response = visitor.invokeApi(scene);
            lastValue = response.getLong("last_value");
            array = response.getJSONArray("list");
            listSize += array.size();
        } while (array.size() == 20);
        return listSize;
    }
//
//    /**
//     * 获取小程序我的消息列表数
//     *
//     * @return 消息数量
//     */
//    public int getAppletMessageNum() {
//        Long lastValue = null;
//        int listSize = 0;
//        JSONArray array;
//        do {
//            IScene scene = MessageListScene.builder().lastValue(lastValue).size(20).build();
//            JSONObject response = visitor.invokeApi(scene);
//            lastValue = response.getLong("last_value");
//            array = response.getJSONArray("list");
//            listSize += array.size();
//        } while (array.size() == 20);
//        return listSize;
//    }
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
//    /**
//     * 活动报名
//     *
//     * @param articleId 活动id
//     */
//    public void applyArticle(Long articleId) {
//        IScene scene = ActivityRegisterScene.builder().id(articleId).name(EnumAccount.MARKETING.name()).phone(EnumAccount.MARKETING.getPhone()).num(1).build();
//        visitor.invokeApi(scene, false);
//    }

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
}
